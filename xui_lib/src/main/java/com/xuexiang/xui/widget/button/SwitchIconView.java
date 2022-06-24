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

package com.xuexiang.xui.widget.button;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Region;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ThemeUtils;

/**
 * 可切换图标的按钮
 *
 * @author xuexiang
 * @since 2020-01-05 23:03
 */
public class SwitchIconView extends AppCompatImageView {

    private static final int DEFAULT_ANIMATION_DURATION = 300;
    private static final float DASH_THICKNESS_PART = 1F / 12F;
    private static final float DEFAULT_DISABLED_ALPHA = 0.5F;
    private static final float SIN_45 = (float) Math.sin(Math.toRadians(45));

    private final long mAnimationDuration;
    @FloatRange(from = 0F, to = 1F)
    private final float mDisabledStateAlpha;
    private final int mDashXStart;
    private final int mDashYStart;
    private final Path mClipPath;
    private final int mIconTintColor;
    private final int mDisabledStateColor;
    private final boolean mNoDash;
    private int mDashThickness;
    private int mDashLengthXProjection;
    private int mDashLengthYProjection;
    private PorterDuffColorFilter mColorFilter;
    private final ArgbEvaluator mColorEvaluator = new ArgbEvaluator();

    @FloatRange(from = 0F, to = 1F)
    private float mFraction = 0F;
    private boolean mEnabled;

    @NonNull
    private final Paint mDashPaint;
    @NonNull
    private final Point mDashStart = new Point();
    @NonNull
    private final Point mDashEnd = new Point();

    public SwitchIconView(@NonNull Context context) {
        this(context, null);
    }

    public SwitchIconView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.SwitchIconViewStyle);
    }

    public SwitchIconView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SwitchIconView, defStyleAttr, 0);
        try {
            mIconTintColor = array.getColor(R.styleable.SwitchIconView_siv_tint_color, ThemeUtils.getMainThemeColor(context));
            mAnimationDuration = array.getInteger(R.styleable.SwitchIconView_siv_animation_duration, DEFAULT_ANIMATION_DURATION);
            mDisabledStateAlpha = array.getFloat(R.styleable.SwitchIconView_siv_disabled_alpha, DEFAULT_DISABLED_ALPHA);
            mDisabledStateColor = array.getColor(R.styleable.SwitchIconView_siv_disabled_color, mIconTintColor);
            mEnabled = array.getBoolean(R.styleable.SwitchIconView_siv_enabled, true);
            mNoDash = array.getBoolean(R.styleable.SwitchIconView_siv_no_dash, false);
        } finally {
            array.recycle();
        }

        if (mDisabledStateAlpha < 0f || mDisabledStateAlpha > 1f) {
            throw new IllegalArgumentException("Wrong value for si_disabled_alpha [" + mDisabledStateAlpha + "]. "
                    + "Must be value from range [0, 1]");
        }

        mColorFilter = new PorterDuffColorFilter(mIconTintColor, PorterDuff.Mode.SRC_IN);
        setColorFilter(mColorFilter);

        mDashXStart = getPaddingLeft();
        mDashYStart = getPaddingTop();

        mDashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDashPaint.setStyle(Paint.Style.STROKE);
        mDashPaint.setColor(mIconTintColor);

        mClipPath = new Path();

        initDashCoordinates();
        setFraction(mEnabled ? 0F : 1F);
    }

    /**
     * Changes state with animation
     *
     * @param enabled If TRUE - icon is enabled
     */
    public void setIconEnabled(boolean enabled) {
        setIconEnabled(enabled, true);
    }

    /**
     * Changes state
     *
     * @param enabled If TRUE - icon is enabled
     */
    public void setIconEnabled(boolean enabled, boolean animate) {
        if (mEnabled == enabled) {
            return;
        }
        switchState(animate);
    }

    /**
     * Check state
     *
     * @return TRUE if icon is enabled, otherwise FALSE
     */
    public boolean isIconEnabled() {
        return mEnabled;
    }

    /**
     * Switches icon state with animation
     */
    public void switchState() {
        switchState(true);
    }

    /**
     * Switches icon state
     *
     * @param animate Indicates that state will be changed with or without animation
     */
    public void switchState(boolean animate) {
        float newFraction;
        if (mEnabled) {
            newFraction = 1F;
        } else {
            newFraction = 0F;
        }
        mEnabled = !mEnabled;
        if (animate) {
            animateToFraction(newFraction);
        } else {
            setFraction(newFraction);
            invalidate();
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SwitchIconSavedState savedState = new SwitchIconSavedState(superState);
        savedState.iconEnabled = mEnabled;
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SwitchIconSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SwitchIconSavedState savedState = (SwitchIconSavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mEnabled = savedState.iconEnabled;
        setFraction(mEnabled ? 0F : 1F);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        mDashLengthXProjection = width - getPaddingLeft() - getPaddingRight();
        mDashLengthYProjection = height - getPaddingTop() - getPaddingBottom();
        mDashThickness = (int) (DASH_THICKNESS_PART * (mDashLengthXProjection + mDashLengthYProjection) / 2F);
        mDashPaint.setStrokeWidth(mDashThickness);
        initDashCoordinates();
        updateClipPath();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mNoDash) {
            drawDash(canvas);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                canvas.clipOutPath(mClipPath);
            } else {
                canvas.clipPath(mClipPath, Region.Op.XOR);
            }
        }
        super.onDraw(canvas);
    }

    private void animateToFraction(float toFraction) {
        ValueAnimator animator = ValueAnimator.ofFloat(mFraction, toFraction);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setFraction((float) animation.getAnimatedValue());
            }
        });
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(mAnimationDuration);
        animator.start();
    }

    private void setFraction(float fraction) {
        mFraction = fraction;
        updateColor(fraction);
        updateAlpha(fraction);
        updateClipPath();
        postInvalidateOnAnimationCompat();
    }

    private void initDashCoordinates() {
        float delta1 = 1.5F * SIN_45 * mDashThickness;
        float delta2 = 0.5F * SIN_45 * mDashThickness;
        mDashStart.x = (int) (mDashXStart + delta2);
        mDashStart.y = mDashYStart + (int) (delta1);
        mDashEnd.x = (int) (mDashXStart + mDashLengthXProjection - delta1);
        mDashEnd.y = (int) (mDashYStart + mDashLengthYProjection - delta2);
    }

    private void updateClipPath() {
        float delta = mDashThickness / SIN_45;
        mClipPath.reset();
        mClipPath.moveTo(mDashXStart, mDashYStart + delta);
        mClipPath.lineTo(mDashXStart + delta, mDashYStart);
        mClipPath.lineTo(mDashXStart + mDashLengthXProjection * mFraction, mDashYStart + mDashLengthYProjection * mFraction - delta);
        mClipPath.lineTo(mDashXStart + mDashLengthXProjection * mFraction - delta, mDashYStart + mDashLengthYProjection * mFraction);
    }

    private void drawDash(Canvas canvas) {
        float x = mFraction * (mDashEnd.x - mDashStart.x) + mDashStart.x;
        float y = mFraction * (mDashEnd.y - mDashStart.y) + mDashStart.y;
        canvas.drawLine(mDashStart.x, mDashStart.y, x, y, mDashPaint);
    }

    private void updateColor(float fraction) {
        if (mIconTintColor != mDisabledStateColor) {
            final int color = (int) mColorEvaluator.evaluate(fraction, mIconTintColor, mDisabledStateColor);
            updateImageColor(color);
            mDashPaint.setColor(color);
        }
    }

    private void updateAlpha(float fraction) {
        int alpha = (int) ((mDisabledStateAlpha + (1F - fraction) * (1F - mDisabledStateAlpha)) * 255);
        updateImageAlpha(alpha);
        mDashPaint.setAlpha(alpha);
    }

    private void updateImageColor(int color) {
        mColorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
        setColorFilter(mColorFilter);
    }

    @SuppressWarnings("deprecation")
    private void updateImageAlpha(int alpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setImageAlpha(alpha);
        } else {
            setAlpha(alpha);
        }
    }

    private void postInvalidateOnAnimationCompat() {
        final long fakeFrameTime = 10;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            postInvalidateOnAnimation();
        } else {
            postInvalidateDelayed(fakeFrameTime);
        }
    }

    static class SwitchIconSavedState extends BaseSavedState {
        boolean iconEnabled;

        SwitchIconSavedState(Parcelable superState) {
            super(superState);
        }

        private SwitchIconSavedState(Parcel in) {
            super(in);
            final int enabled = in.readInt();
            iconEnabled = enabled == 1;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(iconEnabled ? 1 : 0);
        }

        public static final Parcelable.Creator<SwitchIconSavedState> CREATOR =
                new Parcelable.Creator<SwitchIconSavedState>() {
                    @Override
                    public SwitchIconSavedState createFromParcel(Parcel in) {
                        return new SwitchIconSavedState(in);
                    }

                    @Override
                    public SwitchIconSavedState[] newArray(int size) {
                        return new SwitchIconSavedState[size];
                    }
                };
    }
}
