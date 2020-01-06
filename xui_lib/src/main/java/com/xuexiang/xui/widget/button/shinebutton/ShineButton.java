/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xui.widget.button.shinebutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.Checkable;

import androidx.fragment.app.Fragment;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;

/**
 * 增强效果的按钮
 *
 * @author xuexiang
 * @since 2020-01-06 13:16
 */
public class ShineButton extends PorterShapeImageView implements Checkable {
    private boolean mIsChecked = false;

    private int mNormalColor;
    private int mCheckedColor;

    private ValueAnimator mShakeAnimator;
    private ShineView.ShineParams mShineParams = new ShineView.ShineParams();

    private OnCheckedChangeListener mOnCheckedChangeListener;

    Window mWindow;
    private boolean mIsDialog;

    public ShineButton(Context context) {
        this(context, null);
    }

    public ShineButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShineButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initButton(context, attrs);
    }

    private void initButton(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShineButton);
        mNormalColor = a.getColor(R.styleable.ShineButton_sb_normal_color, Color.GRAY);
        mCheckedColor = a.getColor(R.styleable.ShineButton_sb_checked_color, ThemeUtils.resolveColor(context, R.attr.colorAccent));
        mShineParams.allowRandomColor = a.getBoolean(R.styleable.ShineButton_sb_allow_random_color, false);
        mShineParams.animDuration = a.getInteger(R.styleable.ShineButton_sb_shine_animation_duration, (int) mShineParams.animDuration);
        mShineParams.bigShineColor = a.getColor(R.styleable.ShineButton_sb_big_shine_color, mShineParams.bigShineColor);
        mShineParams.clickAnimDuration = a.getInteger(R.styleable.ShineButton_sb_click_animation_duration, (int) mShineParams.clickAnimDuration);
        mShineParams.enableFlashing = a.getBoolean(R.styleable.ShineButton_sb_enable_flashing, false);
        mShineParams.shineCount = a.getInteger(R.styleable.ShineButton_sb_shine_count, mShineParams.shineCount);
        mShineParams.shineDistanceMultiple = a.getFloat(R.styleable.ShineButton_sb_shine_distance_multiple, mShineParams.shineDistanceMultiple);
        mShineParams.shineTurnAngle = a.getFloat(R.styleable.ShineButton_sb_shine_turn_angle, mShineParams.shineTurnAngle);
        mShineParams.smallShineColor = a.getColor(R.styleable.ShineButton_sb_small_shine_color, mShineParams.smallShineColor);
        mShineParams.smallShineOffsetAngle = a.getFloat(R.styleable.ShineButton_sb_small_shine_offset_angle, mShineParams.smallShineOffsetAngle);
        mShineParams.shineSize = a.getDimensionPixelSize(R.styleable.ShineButton_sb_shine_size, mShineParams.shineSize);
        a.recycle();
        setTintColor(mNormalColor);
        if (context instanceof Activity) {
            initWindow((Activity) context);
        }
    }

    public void fitDialog(Dialog dialog) {
        mWindow = dialog.getWindow();
        mIsDialog = true;
    }

    public void fitFragment(Fragment fragment) {
        initWindow(fragment.getActivity());
    }

    public int getColor() {
        return mCheckedColor;
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }

    public void setNormalColor(int normalColor) {
        mNormalColor = normalColor;
        setTintColor(mNormalColor);
    }

    public void setCheckedColor(int checkedColor) {
        mCheckedColor = checkedColor;
    }

    public void setChecked(boolean checked, boolean anim) {
        setChecked(checked, anim, true);
    }

    private void setChecked(boolean checked, boolean anim, boolean callBack) {
        mIsChecked = checked;
        if (checked) {
            setTintColor(mCheckedColor);
            mIsChecked = true;
            if (anim) {
                showAnim();
            }
        } else {
            setTintColor(mNormalColor);
            mIsChecked = false;
            if (anim) {
                setCancel();
            }
        }
        if (callBack) {
            onCheckedChanged(checked);
        }
    }

    @Override
    public void setChecked(boolean checked) {
        setChecked(checked, false, false);
    }

    private void onCheckedChanged(boolean checked) {
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, checked);
        }
    }

    public void setCancel() {
        setTintColor(mNormalColor);
        if (mShakeAnimator != null) {
            mShakeAnimator.end();
            mShakeAnimator.cancel();
        }
    }

    public void setAllowRandomColor(boolean allowRandomColor) {
        mShineParams.allowRandomColor = allowRandomColor;
    }

    public void setAnimDuration(int durationMs) {
        mShineParams.animDuration = durationMs;
    }

    public void setBigShineColor(int color) {
        mShineParams.bigShineColor = color;
    }

    public void setClickAnimDuration(int durationMs) {
        mShineParams.clickAnimDuration = durationMs;
    }

    public void enableFlashing(boolean enable) {
        mShineParams.enableFlashing = enable;
    }

    public void setShineCount(int count) {
        mShineParams.shineCount = count;
    }

    public void setShineDistanceMultiple(float multiple) {
        mShineParams.shineDistanceMultiple = multiple;
    }

    public void setShineTurnAngle(float angle) {
        mShineParams.shineTurnAngle = angle;
    }

    public void setSmallShineColor(int color) {
        mShineParams.smallShineColor = color;
    }

    public void setSmallShineOffAngle(float angle) {
        mShineParams.smallShineOffsetAngle = angle;
    }

    public void setShineSize(int size) {
        mShineParams.shineSize = size;
    }

    @Override
    public void setOnClickListener(View.OnClickListener listener) {
        if (listener instanceof OnClickListenWrapper) {
            super.setOnClickListener(listener);
        } else {
            if (mOnClickListenWrapper != null) {
                mOnClickListenWrapper.wrapListener(listener);
            }
        }
    }

    OnClickListenWrapper mOnClickListenWrapper;

    public void initWindow(Activity activity) {
        initWindow(activity.getWindow());
    }

    public void initWindow(Window window) {
        mWindow = window;
        mOnClickListenWrapper = new OnClickListenWrapper();
        setOnClickListener(mOnClickListenWrapper);
    }

    public void showAnim() {
        if (mWindow != null) {
            ShineView shineView = new ShineView(getContext(), this, mShineParams);
            ViewGroup rootView = (ViewGroup) mWindow.getDecorView();
            if (mIsDialog) {
                View innerView = rootView.findViewById(Window.ID_ANDROID_CONTENT);
                rootView.addView(shineView, new ViewGroup.LayoutParams(innerView.getWidth(), innerView.getHeight()));
            } else {
                rootView.addView(shineView, new ViewGroup.LayoutParams(rootView.getWidth(), rootView.getHeight()));
            }
            doShareAnim();
        }
    }

    public void removeView(View view) {
        if (mWindow != null) {
            final ViewGroup rootView = mWindow.findViewById(Window.ID_ANDROID_CONTENT);
            rootView.removeView(view);
        }
    }

    public void setShapeResource(int resId) {
        setShapeDrawable(ResUtils.getDrawable(getContext(), resId));
    }

    private void doShareAnim() {
        mShakeAnimator = ValueAnimator.ofFloat(0.4f, 1f, 0.9f, 1f);
        mShakeAnimator.setInterpolator(new LinearInterpolator());
        mShakeAnimator.setDuration(500);
        mShakeAnimator.setStartDelay(180);
        invalidate();
        mShakeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setScaleX((float) valueAnimator.getAnimatedValue());
                setScaleY((float) valueAnimator.getAnimatedValue());
            }
        });
        mShakeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animator) {
                setTintColor(mCheckedColor);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setTintColor(mIsChecked ? mCheckedColor : mNormalColor);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                setTintColor(mNormalColor);
            }

        });
        mShakeAnimator.start();
    }

    public class OnClickListenWrapper implements View.OnClickListener {

        public void wrapListener(View.OnClickListener listener) {
            mListener = listener;
        }

        private View.OnClickListener mListener;

        OnClickListenWrapper() {
        }

        public OnClickListenWrapper(View.OnClickListener listener) {
            mListener = listener;
        }

        @Override
        public void onClick(View view) {
            if (!mIsChecked) {
                mIsChecked = true;
                showAnim();
            } else {
                mIsChecked = false;
                setCancel();
            }
            onCheckedChanged(mIsChecked);
            if (mListener != null) {
                mListener.onClick(view);
            }
        }
    }

    public void setOnCheckStateChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    /**
     * 点击切换的监听
     */
    public interface OnCheckedChangeListener {
        void onCheckedChanged(ShineButton shineButton, boolean checked);
    }

}
