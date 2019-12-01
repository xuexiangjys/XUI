/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xui.widget.layout;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.xuexiang.xui.R;
import com.xuexiang.xui.widget.layout.interpolator.FastOutSlowInInterpolator;

import static com.xuexiang.xui.widget.layout.ExpandableLayout.State.COLLAPSED;
import static com.xuexiang.xui.widget.layout.ExpandableLayout.State.COLLAPSING;
import static com.xuexiang.xui.widget.layout.ExpandableLayout.State.EXPANDED;
import static com.xuexiang.xui.widget.layout.ExpandableLayout.State.EXPANDING;

/**
 * 可伸缩的布局
 *
 * @author xuexiang
 * @since 2019-11-22 13:41
 */
public class ExpandableLayout extends FrameLayout {

    public interface State {
        /**
         * 已收缩
         */
        int COLLAPSED = 0;
        /**
         * 收缩中
         */
        int COLLAPSING = 1;
        /**
         * 伸展中
         */
        int EXPANDING = 2;
        /**
         * 已伸展
         */
        int EXPANDED = 3;
    }

    public static final String KEY_SUPER_STATE = "key_super_state";
    public static final String KEY_EXPANSION = "key_expansion";

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    /**
     * 默认伸缩动画持续的时间
     */
    private static final int DEFAULT_DURATION = 300;

    /**
     * 伸缩动画持续的时间
     */
    private int mDuration = DEFAULT_DURATION;
    /**
     * 视差效果
     */
    private float mParallax;
    /**
     * 伸缩比率【Value between 0 (收缩) and 1 (伸展) 】
     */
    private float mExpansion;
    /**
     * 伸缩方向
     */
    private int mOrientation;
    /**
     * 伸缩状态
     */
    private int mState;

    private Interpolator mInterpolator = new FastOutSlowInInterpolator();
    private ValueAnimator mAnimator;
    private OnExpansionChangedListener mListener;

    public ExpandableLayout(Context context) {
        this(context, null);
    }

    public ExpandableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableLayout);
            mDuration = a.getInt(R.styleable.ExpandableLayout_el_duration, DEFAULT_DURATION);
            mExpansion = a.getBoolean(R.styleable.ExpandableLayout_el_expanded, false) ? 1 : 0;
            mOrientation = a.getInt(R.styleable.ExpandableLayout_android_orientation, VERTICAL);
            mParallax = a.getFloat(R.styleable.ExpandableLayout_el_parallax, 1);
            a.recycle();
            mState = mExpansion == 0 ? COLLAPSED : EXPANDED;
            setParallax(mParallax);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        mExpansion = isExpanded() ? 1 : 0;
        bundle.putFloat(KEY_EXPANSION, mExpansion);
        bundle.putParcelable(KEY_SUPER_STATE, superState);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable parcelable) {
        Bundle bundle = (Bundle) parcelable;
        mExpansion = bundle.getFloat(KEY_EXPANSION);
        mState = mExpansion == 1 ? EXPANDED : COLLAPSED;
        Parcelable superState = bundle.getParcelable(KEY_SUPER_STATE);
        super.onRestoreInstanceState(superState);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int size = mOrientation == LinearLayout.HORIZONTAL ? width : height;
        setVisibility(mExpansion == 0 && size == 0 ? GONE : VISIBLE);
        int expansionDelta = size - Math.round(size * mExpansion);
        if (mParallax > 0) {
            float parallaxDelta = expansionDelta * mParallax;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (mOrientation == HORIZONTAL) {
                    int direction = -1;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1 && getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
                        direction = 1;
                    }
                    child.setTranslationX(direction * parallaxDelta);
                } else {
                    child.setTranslationY(-parallaxDelta);
                }
            }
        }

        if (mOrientation == HORIZONTAL) {
            setMeasuredDimension(width - expansionDelta, height);
        } else {
            setMeasuredDimension(width, height - expansionDelta);
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Get mExpansion mState
     *
     * @return one of {@link State}
     */
    public int getState() {
        return mState;
    }

    public boolean isExpanded() {
        return mState == EXPANDING || mState == EXPANDED;
    }

    /**
     * 切换
     */
    public void toggle() {
        toggle(true);
    }

    public void toggle(boolean animate) {
        if (isExpanded()) {
            collapse(animate);
        } else {
            expand(animate);
        }
    }

    /**
     * 伸展
     */
    public void expand() {
        expand(true);
    }

    public void expand(boolean animate) {
        setExpanded(true, animate);
    }

    /**
     * 收缩
     */
    public void collapse() {
        collapse(true);
    }

    public void collapse(boolean animate) {
        setExpanded(false, animate);
    }

    /**
     * Convenience method - same as calling setExpanded(expanded, true)
     */
    public void setExpanded(boolean expand) {
        setExpanded(expand, true);
    }

    public void setExpanded(boolean expand, boolean animate) {
        if (expand == isExpanded()) {
            return;
        }

        int targetExpansion = expand ? 1 : 0;
        if (animate) {
            animateSize(targetExpansion);
        } else {
            setExpansion(targetExpansion);
        }
    }

    public int getDuration() {
        return mDuration;
    }

    public ExpandableLayout setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
        return this;
    }

    public ExpandableLayout setDuration(int duration) {
        mDuration = duration;
        return this;
    }

    public float getExpansion() {
        return mExpansion;
    }

    public void setExpansion(float expansion) {
        if (mExpansion == expansion) {
            return;
        }
        // Infer mState from previous value
        float delta = expansion - this.mExpansion;
        if (expansion == 0) {
            mState = COLLAPSED;
        } else if (expansion == 1) {
            mState = EXPANDED;
        } else if (delta < 0) {
            mState = COLLAPSING;
        } else if (delta > 0) {
            mState = EXPANDING;
        }
        setVisibility(mState == COLLAPSED ? GONE : VISIBLE);
        mExpansion = expansion;
        requestLayout();

        if (mListener != null) {
            mListener.onExpansionChanged(expansion, mState);
        }
    }

    public float getParallax() {
        return mParallax;
    }

    public ExpandableLayout setParallax(float parallax) {
        parallax = Math.min(1, Math.max(0, parallax));
        mParallax = parallax;
        return this;
    }

    public int getOrientation() {
        return mOrientation;
    }

    public ExpandableLayout setOrientation(int orientation) {
        if (orientation < 0 || orientation > 1) {
            throw new IllegalArgumentException("Orientation must be either 0 (horizontal) or 1 (vertical)");
        }
        mOrientation = orientation;
        return this;
    }

    /**
     * 设置伸缩比率变化监听
     * @param listener
     * @return
     */
    public ExpandableLayout setOnExpansionChangedListener(OnExpansionChangedListener listener) {
        mListener = listener;
        return this;
    }

    private void animateSize(int targetExpansion) {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }

        mAnimator = ValueAnimator.ofFloat(mExpansion, targetExpansion);
        mAnimator.setInterpolator(mInterpolator);
        mAnimator.setDuration(mDuration);

        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setExpansion((float) valueAnimator.getAnimatedValue());
            }
        });

        mAnimator.addListener(new ExpansionListener(targetExpansion));

        mAnimator.start();
    }

    /**
     * 伸缩比率变化监听
     */
    public interface OnExpansionChangedListener {
        /**
         * 伸缩比率变化
         *
         * @param expansion 伸缩比率【Value between 0 (收缩) and 1 (伸展) 】
         * @param state     伸缩状态
         */
        void onExpansionChanged(float expansion, int state);
    }

    private class ExpansionListener implements Animator.AnimatorListener {
        private int targetExpansion;
        private boolean canceled;

        public ExpansionListener(int targetExpansion) {
            this.targetExpansion = targetExpansion;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            mState = targetExpansion == 0 ? COLLAPSING : EXPANDING;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (!canceled) {
                mState = targetExpansion == 0 ? COLLAPSED : EXPANDED;
                setExpansion(targetExpansion);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            canceled = true;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }
}
