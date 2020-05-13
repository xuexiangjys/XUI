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

import androidx.appcompat.widget.AppCompatTextView;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 标签控件
 *
 * @author xuexiang
 * @since 2018/12/3 上午12:14
 */
public class LabelView extends AppCompatTextView {

    private float mOffsetX;
    private float mOffsetY;
    private float mAnchorX;
    private float mAnchorY;
    private float mAngel;
    private int mLabelViewContainerID;
    private Animation mAnimation = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            Matrix tran = t.getMatrix();
            tran.postTranslate(mOffsetX, mOffsetY);
            tran.postRotate(mAngel, mAnchorX, mAnchorY);
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

        mAnimation.setFillBefore(true);
        mAnimation.setFillAfter(true);
        mAnimation.setFillEnabled(true);

    }


    private void init() {
        if (getLayoutParams() == null) {
            LayoutParams layoutParams =
                    new LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            setLayoutParams(layoutParams);
        }

        // the default value
        //setPadding(dip2Px(40), dip2Px(2), dip2Px(40), dip2Px(2));
        mLabelViewContainerID = -1;

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
            @Override
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
        if (getParent() == null || mLabelViewContainerID == -1) {
            return;
        }

        ViewGroup frameContainer = (ViewGroup) getParent();
        if (frameContainer.getChildCount() != 2) {
            throw new AssertionError();
        }
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
                    if (para.getRules()[j] == mLabelViewContainerID) {
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
        parentContainer.addView(target, groupIndex);
        mLabelViewContainerID = -1;
    }

    private boolean replaceLayout(View target) {
        if (getParent() != null || target == null || target.getParent() == null || mLabelViewContainerID != -1) {
            return false;
        }

        ViewGroup parentContainer = (ViewGroup) target.getParent();

        if (target.getParent() instanceof FrameLayout) {
            ((FrameLayout) target.getParent()).addView(this);
        } else if (target.getParent() instanceof ViewGroup) {

            int groupIndex = parentContainer.indexOfChild(target);
            mLabelViewContainerID = generateViewId();

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
                            para.getRules()[j] = mLabelViewContainerID;
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
            labelViewContainer.setId(mLabelViewContainerID);

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
            mAnchorX = -edge;
            mOffsetX = mAnchorX;
            mAngel = -45;
        } else if (gravity == Gravity.RIGHT_TOP) {
            mOffsetX = tw + edge - labelWidth;
            mAnchorX = tw + edge;
            mAngel = 45;
        }

        mAnchorY = (float) (1.414 * d + edge);
        mOffsetY = mAnchorY;

        clearAnimation();
        startAnimation(mAnimation);
    }

    private int dip2Px(float dip) {
        return (int) (dip * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    private static final AtomicInteger NEXT_GENERATED_ID = new AtomicInteger(1);

    public static int generateViewId() {
        for (; ; ) {
            final int result = NEXT_GENERATED_ID.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) {
                newValue = 1; // Roll over to 1, not 0.
            }
            if (NEXT_GENERATED_ID.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }
}
