/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright 2014 linger1216
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.xui.widget.textview.label;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 标签控件
 *
 * @author xuexiang
 * @since 2018/12/3 上午12:14
 */
public class LabelView extends AppCompatTextView {

    private float _offsetx;
    private float _offsety;
    private float _anchorx;
    private float _anchory;
    private float _angel;
    private int _labelViewContainerID;
    private Animation _animation = new Animation() {
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            Matrix tran = t.getMatrix();
            tran.postTranslate(_offsetx, _offsety);
            tran.postRotate(_angel, _anchorx, _anchory);
        }
    };

    public enum Gravity {
        LEFT_TOP, RIGHT_TOP
    }

    public LabelView(Context context) {
        this(context, null);
    }

    public LabelView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public LabelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();

        _animation.setFillBefore(true);
        _animation.setFillAfter(true);
        _animation.setFillEnabled(true);

    }


    private void init() {

        if (!(getLayoutParams() instanceof ViewGroup.LayoutParams)) {
            LayoutParams layoutParams =
                    new LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            setLayoutParams(layoutParams);
        }

        // the default value
        //setPadding(dip2Px(40), dip2Px(2), dip2Px(40), dip2Px(2));
        _labelViewContainerID = -1;

        setGravity(android.view.Gravity.CENTER);
        setTextColor(Color.WHITE);
        setTypeface(Typeface.DEFAULT_BOLD);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        setBackgroundColor(Color.BLUE);
    }

    public void setTargetView(View target, int distance, Gravity gravity) {

        if (!replaceLayout(target)) {
            return;
        }

        final int d = dip2Px(distance);
        final Gravity g = gravity;
        final View v = target;

        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                calcOffset(getMeasuredWidth(), d, g, v.getMeasuredWidth(), false);
            }
        });

    }


    public void setTargetViewInBaseAdapter(View target, int targetWidth, int distance, Gravity gravity) {
        if (!replaceLayout(target)) {
            return;
        }
        //measure(0, 0);
        //calcOffset(getMeasuredWidth(), distance, gravity, targetWidth, true);
        calcOffset(dip2Px(targetWidth), distance, gravity, targetWidth, true);
    }

    public void remove() {
        if (getParent() == null || _labelViewContainerID == -1) {
            return;
        }

        ViewGroup frameContainer = (ViewGroup) getParent();
        assert (frameContainer.getChildCount() == 2);
        View target = frameContainer.getChildAt(0);

        ViewGroup parentContainer = (ViewGroup) frameContainer.getParent();
        int groupIndex = parentContainer.indexOfChild(frameContainer);
        if (frameContainer.getParent() instanceof RelativeLayout) {
            for (int i = 0; i < parentContainer.getChildCount(); i++) {
                if (i == groupIndex) {
                    continue;
                }
                View view = parentContainer.getChildAt(i);
                RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) view.getLayoutParams();
                for (int j = 0; j < para.getRules().length; j++) {
                    if (para.getRules()[j] == _labelViewContainerID) {
                        para.getRules()[j] = target.getId();
                    }
                }
                view.setLayoutParams(para);
            }
        }

        ViewGroup.LayoutParams frameLayoutParam = frameContainer.getLayoutParams();
        target.setLayoutParams(frameLayoutParam);
        parentContainer.removeViewAt(groupIndex);
        frameContainer.removeView(target);
        frameContainer.removeView(this);
        parentContainer.addView(target,groupIndex);
        _labelViewContainerID = -1;
    }

    private boolean replaceLayout(View target) {
        if (getParent() != null || target == null || target.getParent() == null || _labelViewContainerID != -1) {
            return false;
        }

        ViewGroup parentContainer = (ViewGroup) target.getParent();

        if (target.getParent() instanceof FrameLayout) {
            ((FrameLayout) target.getParent()).addView(this);
        } else if (target.getParent() instanceof ViewGroup) {

            int groupIndex = parentContainer.indexOfChild(target);
            _labelViewContainerID = generateViewId();

            // relativeLayout need copy rule
            if (target.getParent() instanceof RelativeLayout) {
                for (int i = 0; i < parentContainer.getChildCount(); i++) {
                    if (i == groupIndex) {
                        continue;
                    }
                    View view = parentContainer.getChildAt(i);
                    RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    for (int j = 0; j < para.getRules().length; j++) {
                        if (para.getRules()[j] == target.getId()) {
                            para.getRules()[j] = _labelViewContainerID;
                        }
                    }
                    view.setLayoutParams(para);
                }
            }
            parentContainer.removeView(target);

            // new dummy layout
            FrameLayout labelViewContainer = new FrameLayout(getContext());
            ViewGroup.LayoutParams targetLayoutParam = target.getLayoutParams();
            labelViewContainer.setLayoutParams(targetLayoutParam);
            target.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            // add target and label in dummy layout
            labelViewContainer.addView(target);
            labelViewContainer.addView(this);
            labelViewContainer.setId(_labelViewContainerID);

            // add dummy layout in parent container
            parentContainer.addView(labelViewContainer, groupIndex, targetLayoutParam);
        }
        return true;
    }

    private void calcOffset(int labelWidth, int distance, Gravity gravity, int targetWidth, boolean isDP) {

        int d = dip2Px(distance);
        int tw = isDP ? dip2Px(targetWidth) : targetWidth;

        float edge = (float) ((labelWidth - 2 * d) / (2 * 1.414));
        if (gravity == Gravity.LEFT_TOP) {
            _anchorx = -edge;
            _offsetx = _anchorx;
            _angel = -45;
        } else if (gravity == Gravity.RIGHT_TOP) {
            _offsetx = tw + edge - labelWidth;
            _anchorx = tw + edge;
            _angel = 45;
        }

        _anchory = (float) (1.414 * d + edge);
        _offsety = _anchory;

        clearAnimation();
        startAnimation(_animation);
    }

    private int dip2Px(float dip) {
        return (int) (dip * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }
}
