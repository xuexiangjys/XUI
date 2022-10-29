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

import java.lang.ref.WeakReference;

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

    WeakReference<Window> mWindow;
    private boolean mIsDialog;

    public ShineButton(Context context) {
        this(context, null);
    }

    public ShineButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.ShineButtonStyle);
    }

    public ShineButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initButton(context, attrs, defStyleAttr);
    }

    private void initButton(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShineButton, defStyleAttr, 0);
        mNormalColor = a.getColor(R.styleable.ShineButton_sb_normal_color, ResUtils.getColor(context, R.color.default_sb_normal_color));
        mCheckedColor = a.getColor(R.styleable.ShineButton_sb_checked_color, ThemeUtils.getMainThemeColor(context));
        mShineParams.allowRandomColor = a.getBoolean(R.styleable.ShineButton_sb_allow_random_color, false);
        mShineParams.animDuration = a.getInteger(R.styleable.ShineButton_sb_shine_animation_duration, (int) mShineParams.animDuration);
        mShineParams.clickAnimDuration = a.getInteger(R.styleable.ShineButton_sb_click_animation_duration, (int) mShineParams.clickAnimDuration);
        mShineParams.enableFlashing = a.getBoolean(R.styleable.ShineButton_sb_enable_flashing, false);
        mShineParams.shineDistanceMultiple = a.getFloat(R.styleable.ShineButton_sb_shine_distance_multiple, mShineParams.shineDistanceMultiple);
        mShineParams.shineCount = a.getInteger(R.styleable.ShineButton_sb_shine_count, mShineParams.shineCount);
        mShineParams.shineSize = a.getDimensionPixelSize(R.styleable.ShineButton_sb_shine_size, mShineParams.shineSize);
        mShineParams.shineTurnAngle = a.getFloat(R.styleable.ShineButton_sb_shine_turn_angle, mShineParams.shineTurnAngle);
        mShineParams.smallShineOffsetAngle = a.getFloat(R.styleable.ShineButton_sb_small_shine_offset_angle, mShineParams.smallShineOffsetAngle);
        mShineParams.smallShineColor = a.getColor(R.styleable.ShineButton_sb_small_shine_color, mShineParams.smallShineColor);
        mShineParams.bigShineColor = a.getColor(R.styleable.ShineButton_sb_big_shine_color, mShineParams.bigShineColor);
        a.recycle();
        setTintColor(mNormalColor);

        if (context instanceof Activity) {
            initWindow((Activity) context);
        }
    }

    /**
     * 修复对话框中显示的问题
     *
     * @param dialog
     */
    public void fitDialog(Dialog dialog) {
        mWindow = new WeakReference<>(dialog.getWindow());
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

    public ShineButton setNormalColor(int normalColor) {
        mNormalColor = normalColor;
        setTintColor(mNormalColor);
        return this;
    }

    public ShineButton setCheckedColor(int checkedColor) {
        mCheckedColor = checkedColor;
        return this;
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

    public ShineButton setAllowRandomColor(boolean allowRandomColor) {
        mShineParams.allowRandomColor = allowRandomColor;
        return this;
    }

    public ShineButton setAnimDuration(int durationMs) {
        mShineParams.animDuration = durationMs;
        return this;
    }

    public ShineButton setBigShineColor(int color) {
        mShineParams.bigShineColor = color;
        return this;
    }

    public ShineButton setClickAnimDuration(int durationMs) {
        mShineParams.clickAnimDuration = durationMs;
        return this;
    }

    public ShineButton enableFlashing(boolean enable) {
        mShineParams.enableFlashing = enable;
        return this;
    }

    public ShineButton setShineCount(int count) {
        mShineParams.shineCount = count;
        return this;
    }

    public ShineButton setShineDistanceMultiple(float multiple) {
        mShineParams.shineDistanceMultiple = multiple;
        return this;
    }

    public ShineButton setShineTurnAngle(float angle) {
        mShineParams.shineTurnAngle = angle;
        return this;
    }

    public ShineButton setSmallShineColor(int color) {
        mShineParams.smallShineColor = color;
        return this;
    }

    public ShineButton setSmallShineOffAngle(float angle) {
        mShineParams.smallShineOffsetAngle = angle;
        return this;
    }

    public ShineButton setShineSize(int size) {
        mShineParams.shineSize = size;
        return this;
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

    private OnClickListenWrapper mOnClickListenWrapper;

    public void initWindow(Activity activity) {
        initWindow(activity.getWindow());
        mIsDialog = false;
    }

    public void initWindow(Window window) {
        mWindow = new WeakReference<>(window);
        mOnClickListenWrapper = new OnClickListenWrapper();
        setOnClickListener(mOnClickListenWrapper);
    }

    public void showAnim() {
        if (getWindow() != null) {
            ShineView shineView = new ShineView(getContext(), this, mShineParams);
            ViewGroup rootView = (ViewGroup) getWindow().getDecorView();
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
        if (getWindow() != null) {
            final ViewGroup rootView = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
            rootView.removeView(view);
        }
    }

    public Window getWindow() {
        if (mWindow != null) {
            return mWindow.get();
        }
        return null;
    }

    public ShineButton setIconResource(int resId) {
        setIconDrawable(ResUtils.getDrawable(getContext(), resId));
        return this;
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

    public ShineButton setOnCheckStateChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
        return this;
    }

    /**
     * 点击切换的监听
     */
    public interface OnCheckedChangeListener {
        /**
         * 选中状态改变
         *
         * @param shineButton
         * @param isChecked
         */
        void onCheckedChanged(ShineButton shineButton, boolean isChecked);
    }

}
