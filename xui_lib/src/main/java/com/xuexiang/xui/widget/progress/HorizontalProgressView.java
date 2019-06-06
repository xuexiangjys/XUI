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

package com.xuexiang.xui.widget.progress;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ThemeUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author xuexiang
 * @since 2019-05-12 12:34
 */
public class HorizontalProgressView extends View {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ACCELERATE_DECELERATE_INTERPOLATOR, LINEAR_INTERPOLATOR, ACCELERATE_INTERPOLATOR, DECELERATE_INTERPOLATOR, OVERSHOOT_INTERPOLATOR})
    private @interface AnimateType {

    }

    /**
     * animation types supported
     */
    public static final int ACCELERATE_DECELERATE_INTERPOLATOR = 0;
    public static final int LINEAR_INTERPOLATOR = 1;
    public static final int ACCELERATE_INTERPOLATOR = 2;
    public static final int DECELERATE_INTERPOLATOR = 3;
    public static final int OVERSHOOT_INTERPOLATOR = 4;
    /**
     * the type of animation
     */
    private int mAnimateType = 0;
    /**
     * the progress of start point
     */
    private float mStartProgress = 0;
    /**
     * the progress of end point
     */
    private float mEndProgress = 60;
    /**
     * the color of start progress
     */
    private int mStartColor = getResources().getColor(R.color.xui_config_color_light_orange);
    /**
     * the color of end progress
     */
    private int mEndColor = getResources().getColor(R.color.xui_config_color_dark_orange);
    /**
     * has track of moving or not
     */
    private boolean trackEnabled = false;
    /**
     * the stroke width of progress
     */
    private int mTrackWidth = 6;
    /**
     * the size of inner text
     */
    private int mProgressTextSize = 48;
    /**
     * the color of inner text
     */
    private int mProgressTextColor;
    /**
     * the color of progress track
     */
    private int mTrackColor = getResources().getColor(R.color.default_pv_track_color);
    /**
     * the duration of progress moving
     */
    private int mProgressDuration = 1200;
    /**
     * show the inner text or not
     */
    private boolean textVisibility = true;
    /**
     * the round rect corner radius
     */
    private int mCornerRadius = 30;
    /**
     * the offset of text padding bottom
     */
    private int mTextPaddingBottomOffset = 5;
    /**
     * moving the text with progress or not
     */
    private boolean isTextMoved = true;

    /**
     * the animator of progress moving
     */
    private ObjectAnimator progressAnimator;
    /**
     * the progress of moving
     */
    private float moveProgress = 0;
    /**
     * the paint of drawing progress
     */
    private Paint progressPaint;
    /**
     * the gradient of color
     */
    private LinearGradient mShader;
    /**
     * the oval's rect shape
     */
    private RectF mRect;
    private RectF mTrackRect;
    private Paint mTextPaint;
    private Interpolator mInterpolator;
    private HorizontalProgressUpdateListener animatorUpdateListener;


    public HorizontalProgressView(Context context) {
        this(context, null);
    }

    public HorizontalProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.HorizontalProgressViewStyle);
    }

    public HorizontalProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainAttrs(context, attrs, defStyleAttr);
        init();
    }

    private void obtainAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HorizontalProgressView, defStyleAttr, 0);

        mStartProgress = typedArray.getInt(R.styleable.HorizontalProgressView_hpv_start_progress, 0);
        mEndProgress = typedArray.getInt(R.styleable.HorizontalProgressView_hpv_end_progress, 60);
        mStartColor = typedArray.getColor(R.styleable.HorizontalProgressView_hpv_start_color, getResources().getColor(R.color.xui_config_color_light_orange));
        mEndColor = typedArray.getColor(R.styleable.HorizontalProgressView_hpv_end_color, getResources().getColor(R.color.xui_config_color_dark_orange));
        trackEnabled = typedArray.getBoolean(R.styleable.HorizontalProgressView_hpv_isTracked, false);
        mProgressTextColor = typedArray.getColor(R.styleable.HorizontalProgressView_hpv_progress_textColor, ThemeUtils.resolveColor(getContext(), R.attr.colorAccent));
        mProgressTextSize = typedArray.getDimensionPixelSize(R.styleable.HorizontalProgressView_hpv_progress_textSize, getResources().getDimensionPixelSize(R.dimen.default_pv_horizontal_text_size));
        mTrackWidth = typedArray.getDimensionPixelSize(R.styleable.HorizontalProgressView_hpv_track_width, getResources().getDimensionPixelSize(R.dimen.default_pv_trace_width));
        mAnimateType = typedArray.getInt(R.styleable.HorizontalProgressView_hpv_animate_type, ACCELERATE_DECELERATE_INTERPOLATOR);
        mTrackColor = typedArray.getColor(R.styleable.HorizontalProgressView_hpv_track_color, getResources().getColor(R.color.default_pv_track_color));
        textVisibility = typedArray.getBoolean(R.styleable.HorizontalProgressView_hpv_progress_textVisibility, true);
        mProgressDuration = typedArray.getInt(R.styleable.HorizontalProgressView_hpv_progress_duration, 1200);
        mCornerRadius = typedArray.getDimensionPixelSize(R.styleable.HorizontalProgressView_hpv_corner_radius, getResources().getDimensionPixelSize(R.dimen.default_pv_corner_radius));
        mTextPaddingBottomOffset = typedArray.getDimensionPixelSize(R.styleable.HorizontalProgressView_hpv_text_padding_bottom, getResources().getDimensionPixelSize(R.dimen.default_pv_corner_radius));
        isTextMoved = typedArray.getBoolean(R.styleable.HorizontalProgressView_hpv_text_movedEnable, true);

        typedArray.recycle();
        moveProgress = mStartProgress;
    }

    private void init() {
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        updateTheTrack();
        drawTrack(canvas);
        progressPaint.setShader(mShader);
        canvas.drawRoundRect(mRect, mCornerRadius, mCornerRadius, progressPaint);

        drawProgressText(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mShader = new LinearGradient(getPaddingLeft() - 50, (getHeight() - getPaddingTop()) - 50, getWidth() - getPaddingRight(), getHeight() / 2 + getPaddingTop() + mTrackWidth,
                mStartColor, mEndColor, Shader.TileMode.CLAMP);


    }

    /**
     * draw the track(moving background)
     *
     * @param canvas mCanvas
     */
    private void drawTrack(Canvas canvas) {
        if (trackEnabled) {
            progressPaint.setShader(null);
            progressPaint.setColor(mTrackColor);
            canvas.drawRoundRect(mTrackRect, mCornerRadius, mCornerRadius, progressPaint);

        }
    }

    /**
     * draw the progress text
     *
     * @param canvas mCanvas
     */
    private void drawProgressText(Canvas canvas) {

        if (textVisibility) {
            mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mTextPaint.setStyle(Paint.Style.FILL);
            mTextPaint.setTextSize(mProgressTextSize);
            mTextPaint.setColor(mProgressTextColor);
            mTextPaint.setTextAlign(Paint.Align.CENTER);

            String progressText = ((int) moveProgress) + "%";
            if (isTextMoved) {
                /**
                 * draw the animated text of progress, should think about the offsets, or text will be covered.
                 */
                canvas.drawText(progressText,
                        (getWidth() - getPaddingLeft() - getPaddingRight() - DensityUtils.dp2px(getContext(), 28)) * (moveProgress / 100) + DensityUtils.dp2px(getContext(), 10),
                        getHeight() / 2 - getPaddingTop() - mTextPaddingBottomOffset, mTextPaint);
            } else {
                canvas.drawText(progressText, (getWidth() - getPaddingLeft()) / 2, getHeight() / 2 - getPaddingTop() - mTextPaddingBottomOffset, mTextPaint);
            }


        }

    }


    /**
     * set progress animate type
     *
     * @param type anim type
     */
    public void setAnimateType(@AnimateType int type) {
        this.mAnimateType = type;
        setObjectAnimatorType(type);
    }

    /**
     * set object animation type by received
     *
     * @param animatorType object anim type
     */
    private void setObjectAnimatorType(int animatorType) {
        switch (animatorType) {
            case ACCELERATE_DECELERATE_INTERPOLATOR:
                if (mInterpolator != null) {
                    mInterpolator = null;
                }
                mInterpolator = new AccelerateDecelerateInterpolator();
                break;
            case LINEAR_INTERPOLATOR:
                if (mInterpolator != null) {
                    mInterpolator = null;
                }
                mInterpolator = new LinearInterpolator();
                break;
            case ACCELERATE_INTERPOLATOR:
                if (mInterpolator != null) {
                    mInterpolator = null;
                    mInterpolator = new AccelerateInterpolator();
                }
                break;
            case DECELERATE_INTERPOLATOR:
                if (mInterpolator != null) {
                    mInterpolator = null;
                }
                mInterpolator = new DecelerateInterpolator();
                break;
            case OVERSHOOT_INTERPOLATOR:
                if (mInterpolator != null) {
                    mInterpolator = null;
                }
                mInterpolator = new OvershootInterpolator();
                break;
            default:
                break;
        }
    }

    /**
     * set move progress
     *
     * @param progress progress of moving
     */
    public void setProgress(float progress) {
        this.moveProgress = progress;
        refreshTheView();
    }

    public float getProgress() {
        return this.moveProgress;
    }

    /**
     * set start progress
     *
     * @param startProgress start progress
     */
    public void setStartProgress(float startProgress) {
        if (startProgress < 0 || startProgress > 100) {
            throw new IllegalArgumentException("Illegal progress value, please change it!");
        }
        this.mStartProgress = startProgress;
        this.moveProgress = mStartProgress;
        refreshTheView();
    }

    /**
     * set end progress
     *
     * @param endProgress end progress
     */
    public void setEndProgress(float endProgress) {
        if (endProgress < 0 || endProgress > 100) {
            throw new IllegalArgumentException("Illegal progress value, please change it!");
        }
        this.mEndProgress = endProgress;
        refreshTheView();

    }

    /**
     * set start color
     *
     * @param startColor start point color
     */
    public void setStartColor(@ColorInt int startColor) {
        this.mStartColor = startColor;
        mShader = new LinearGradient(getPaddingLeft() - 50, (getHeight() - getPaddingTop()) - 50, getWidth() - getPaddingRight(), getHeight() / 2 + getPaddingTop() + mTrackWidth,
                mStartColor, mEndColor, Shader.TileMode.CLAMP);
        refreshTheView();
    }

    /**
     * set end color
     *
     * @param endColor end point color
     */
    public void setEndColor(@ColorInt int endColor) {
        this.mEndColor = endColor;
        mShader = new LinearGradient(getPaddingLeft() - 50, (getHeight() - getPaddingTop()) - 50, getWidth() - getPaddingRight(), getHeight() / 2 + getPaddingTop() + mTrackWidth,
                mStartColor, mEndColor, Shader.TileMode.CLAMP);
        refreshTheView();
    }

    /**
     * set the width of progress stroke
     *
     * @param width stroke
     */
    public void setTrackWidth(int width) {
        this.mTrackWidth = DensityUtils.dp2px(getContext(), width);
        refreshTheView();
    }

    /**
     * set track color for progress background
     *
     * @param color bg color
     */
    public void setTrackColor(@ColorInt int color) {
        this.mTrackColor = color;
        refreshTheView();
    }

    /**
     * set text color for progress text
     *
     * @param textColor
     */
    public void setProgressTextColor(@ColorInt int textColor) {
        this.mProgressTextColor = textColor;
    }

    /**
     * set text size for inner text
     *
     * @param size text size
     */
    public void setProgressTextSize(int size) {
        mProgressTextSize = DensityUtils.sp2px(getContext(), size);
        refreshTheView();
    }

    /**
     * set duration of progress moving
     *
     * @param duration
     */
    public void setProgressDuration(int duration) {
        this.mProgressDuration = duration;
    }

    /**
     * set track for progress
     *
     * @param trackAble whether track or not
     */
    public void setTrackEnabled(boolean trackAble) {
        this.trackEnabled = trackAble;
        refreshTheView();
    }

    /**
     * set the visibility for progress inner text
     *
     * @param visibility text visible or not
     */
    public void setProgressTextVisibility(boolean visibility) {
        this.textVisibility = visibility;
        refreshTheView();
    }

    /**
     * set progress text moving with progress view or not
     *
     * @param moved
     */
    public void setProgressTextMoved(boolean moved) {
        this.isTextMoved = moved;
    }

    /**
     * start the progress's moving
     */
    public void startProgressAnimation() {
        progressAnimator = ObjectAnimator.ofFloat(this, "progress", mStartProgress, mEndProgress);
        progressAnimator.setInterpolator(mInterpolator);
        progressAnimator.setDuration(mProgressDuration);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue("progress");
                if (animatorUpdateListener != null) {
                    animatorUpdateListener.onHorizontalProgressUpdate(HorizontalProgressView.this, progress);
                }

            }

        });
        progressAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (animatorUpdateListener != null) {
                    animatorUpdateListener.onHorizontalProgressStart(HorizontalProgressView.this);
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (animatorUpdateListener != null) {
                    animatorUpdateListener.onHorizontalProgressFinished(HorizontalProgressView.this);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        progressAnimator.start();
    }

    /**
     * stop the progress moving
     */
    public void stopProgressAnimation() {
        if (progressAnimator != null) {
            progressAnimator.cancel();
            progressAnimator = null;
        }
    }

    /**
     * set the corner radius for the rect of progress
     *
     * @param radius the corner radius
     */
    public void setProgressCornerRadius(int radius) {
        this.mCornerRadius = DensityUtils.dp2px(getContext(), radius);
        refreshTheView();
    }

    /**
     * set the text padding bottom offset
     *
     * @param offset the value of padding bottom
     */
    public void setProgressTextPaddingBottom(int offset) {
        this.mTextPaddingBottomOffset = DensityUtils.dp2px(getContext(), offset);
    }


    /**
     * refresh the layout
     */
    private void refreshTheView() {
        invalidate();
        //requestLayout();
    }

    /**
     * update the oval progress track
     */
    private void updateTheTrack() {
        mRect = new RectF(getPaddingLeft() + mStartProgress * (getWidth() - getPaddingLeft() - getPaddingRight() + 60) / 100, getHeight() / 2 - getPaddingTop(),
                (getWidth() - getPaddingRight() - 20) * ((moveProgress) / 100),
                getHeight() / 2 + getPaddingTop() + mTrackWidth);
        mTrackRect = new RectF(getPaddingLeft(), getHeight() / 2 - getPaddingTop(),
                (getWidth() - getPaddingRight() - 20),
                getHeight() / 2 + getPaddingTop() + mTrackWidth);
    }

    /**
     * 进度条更新监听
     */
    public interface HorizontalProgressUpdateListener {
        /**
         * 进度条开始更新
         * @param view
         */
        void onHorizontalProgressStart(View view);

        /**
         * 进度条更新中
         * @param view
         * @param progress
         */
        void onHorizontalProgressUpdate(View view, float progress);

        /**
         * 进度条更新结束
         * @param view
         */
        void onHorizontalProgressFinished(View view);

    }

    /**
     * set the progress update listener for progress view
     *
     * @param listener update listener
     */
    public void setProgressViewUpdateListener(HorizontalProgressUpdateListener listener) {
        this.animatorUpdateListener = listener;
    }


}
