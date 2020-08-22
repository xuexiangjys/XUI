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
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
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
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ThemeUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author xuexiang
 * @since 2019-05-12 12:34
 */
public class CircleProgressView extends View {

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
    private boolean trackEnabled;
    /**
     * filling the inner space or not
     */
    private boolean fillEnabled;
    /**
     * the stroke width of Track
     */
    private int mTrackWidth;
    /**
     * the stroke width of progress
     */
    private int mProgressWidth;
    /**
     * the size of inner text
     */
    private int mProgressTextSize;
    /**
     * the color of inner text
     */
    private int mProgressTextColor;
    /**
     * the circle of progress broken or not
     */
    private boolean circleBroken;
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
    private boolean textVisibility;

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
     * the paint of drawing track
     */
    private Paint trackPaint;
    /**
     * the gradient of color
     */
    private LinearGradient mShader;
    /**
     * the oval's rect shape
     */
    private RectF mOval;

    private Interpolator mInterpolator;
    private CircleProgressUpdateListener mUpdateListener;

    /**
     * the path of scale zone
     */
    private Path mScaleZonePath;
    /**
     * the width of each scale zone
     */
    private float mScaleZoneWidth;
    /**
     * the length of each scale zone
     */
    private float mScaleZoneLength;
    /**
     * the padding of scale zones
     */
    private int mScaleZonePadding;
    /**
     * open draw the scale zone or not
     */
    private boolean isGraduated = false;
    /**
     * the radius of scale zone corner
     */
    private int mScaleZoneCornerRadius = 0;

    PathEffect pathEffect;


    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.CircleProgressViewStyle);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainAttrs(context, attrs, defStyleAttr);
        init();
    }

    private void obtainAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView, defStyleAttr, 0);

        mStartProgress = typedArray.getInt(R.styleable.CircleProgressView_cpv_start_progress, 0);
        mEndProgress = typedArray.getInt(R.styleable.CircleProgressView_cpv_end_progress, 60);
        mStartColor = typedArray.getColor(R.styleable.CircleProgressView_cpv_start_color, getResources().getColor(R.color.xui_config_color_light_orange));
        mEndColor = typedArray.getColor(R.styleable.CircleProgressView_cpv_end_color, getResources().getColor(R.color.xui_config_color_dark_orange));
        fillEnabled = typedArray.getBoolean(R.styleable.CircleProgressView_cpv_isFilled, false);
        trackEnabled = typedArray.getBoolean(R.styleable.CircleProgressView_cpv_isTracked, false);
        circleBroken = typedArray.getBoolean(R.styleable.CircleProgressView_cpv_circle_broken, false);
        mProgressTextColor = typedArray.getColor(R.styleable.CircleProgressView_cpv_progress_textColor, ThemeUtils.resolveColor(getContext(), R.attr.colorAccent));
        mProgressTextSize = typedArray.getDimensionPixelSize(R.styleable.CircleProgressView_cpv_progress_textSize, getResources().getDimensionPixelSize(R.dimen.default_pv_progress_text_size));
        mTrackWidth = typedArray.getDimensionPixelSize(R.styleable.CircleProgressView_cpv_track_width, getResources().getDimensionPixelSize(R.dimen.default_pv_trace_width));
        mProgressWidth = typedArray.getDimensionPixelSize(R.styleable.CircleProgressView_cpv_progress_width, getResources().getDimensionPixelSize(R.dimen.default_pv_trace_width));
        mAnimateType = typedArray.getInt(R.styleable.CircleProgressView_cpv_animate_type, ACCELERATE_DECELERATE_INTERPOLATOR);
        mTrackColor = typedArray.getColor(R.styleable.CircleProgressView_cpv_track_color, getResources().getColor(R.color.default_pv_track_color));
        textVisibility = typedArray.getBoolean(R.styleable.CircleProgressView_cpv_progress_textVisibility, true);
        mProgressDuration = typedArray.getInt(R.styleable.CircleProgressView_cpv_progress_duration, 1200);
        mScaleZoneLength = typedArray.getDimensionPixelSize(R.styleable.CircleProgressView_cpv_scaleZone_length, getResources().getDimensionPixelSize(R.dimen.default_pv_zone_length));
        mScaleZoneWidth = typedArray.getDimensionPixelSize(R.styleable.CircleProgressView_cpv_scaleZone_width, getResources().getDimensionPixelSize(R.dimen.default_pv_zone_width));
        mScaleZonePadding = typedArray.getDimensionPixelSize(R.styleable.CircleProgressView_cpv_scaleZone_padding, getResources().getDimensionPixelSize(R.dimen.default_pv_zone_padding));
        mScaleZoneCornerRadius = typedArray.getDimensionPixelSize(R.styleable.CircleProgressView_cpv_scaleZone_corner_radius, getResources().getDimensionPixelSize(R.dimen.default_pv_zone_corner_radius));
        isGraduated = typedArray.getBoolean(R.styleable.CircleProgressView_cpv_isGraduated, false);
        moveProgress = mStartProgress;

        typedArray.recycle();
    }

    private void init() {
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(mProgressWidth);

        trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        trackPaint.setStyle(Paint.Style.STROKE);
        trackPaint.setStrokeCap(Paint.Cap.ROUND);
        trackPaint.setStrokeWidth(mTrackWidth);

        mScaleZonePath = new Path();

        drawScaleZones(isGraduated);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawTrack(canvas);

        drawProgress(canvas);

        drawProgressText(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        updateTheTrack();

        mShader = new LinearGradient(mOval.left - 200, mOval.top - 200, mOval.right + 20, mOval.bottom + 20,
                mStartColor, mEndColor, Shader.TileMode.CLAMP);
        /**
         * draw the scale zone shape
         */
        /**
         * the shape of scale zone
         */
        RectF mScaleZoneRect = new RectF(0, 0, mScaleZoneWidth, mScaleZoneLength);
        mScaleZonePath.addRoundRect(mScaleZoneRect, mScaleZoneCornerRadius, mScaleZoneCornerRadius, Path.Direction.CW);


    }

    /**
     * draw the track(moving background)
     *
     * @param canvas mCanvas
     */
    private void drawTrack(Canvas canvas) {
        if (trackEnabled) {
            trackPaint.setShader(null);
            trackPaint.setColor(mTrackColor);
            initTrack(canvas, fillEnabled);
        }
    }

    private void drawProgress(Canvas canvas) {
        progressPaint.setShader(mShader);
        updateTheTrack();
        initProgressDrawing(canvas, fillEnabled);
    }

    /**
     * draw the each scale zone, you can set round corner fot it
     *
     * @link
     */
    private void drawScaleZones(boolean isGraduated) {
        if (isGraduated) {
            if (pathEffect == null) {
                pathEffect = new PathDashPathEffect(mScaleZonePath, mScaleZonePadding, 0, PathDashPathEffect.Style.ROTATE);
            }
            progressPaint.setPathEffect(pathEffect);
        } else {
            pathEffect = null;
            progressPaint.setPathEffect(null);
        }

    }

    /**
     * init for track view
     *
     * @param canvas   mCanvas
     * @param isFilled whether filled or not
     */
    private void initTrack(Canvas canvas, boolean isFilled) {
        if (isFilled) {
            trackPaint.setStyle(Paint.Style.FILL);
        } else {
            trackPaint.setStyle(Paint.Style.STROKE);
        }
        if (circleBroken) {
            canvas.drawArc(mOval, 135, 270, isFilled, trackPaint);
        } else {
            canvas.drawArc(mOval, 90, 360, isFilled, trackPaint);
        }
    }

    /**
     * draw the progress text
     *
     * @param canvas mCanvas
     */
    private void drawProgressText(Canvas canvas) {
        if (textVisibility) {
            Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mTextPaint.setStyle(Paint.Style.FILL);
            mTextPaint.setTextSize(mProgressTextSize);
            mTextPaint.setColor(mProgressTextColor);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mTextPaint.setTypeface(XUI.getDefaultTypeface());

            String progressText = ((int) moveProgress) + "%";
            float x = (getWidth() + getPaddingLeft() - getPaddingRight()) >> 1;
            float y = (getHeight() + getPaddingTop() - getPaddingBottom() - (mTextPaint.descent() + mTextPaint.ascent())) / 2;
            canvas.drawText(progressText, x, y, mTextPaint);

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
        updateTheTrack();
        mShader = new LinearGradient(mOval.left - 200, mOval.top - 200, mOval.right + 20, mOval.bottom + 20,
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
        updateTheTrack();
        mShader = new LinearGradient(mOval.left - 200, mOval.top - 200, mOval.right + 20, mOval.bottom + 20, mStartColor, mEndColor, Shader.TileMode.CLAMP);
        refreshTheView();
    }

    /**
     * set the width of progress stroke
     *
     * @param width stroke
     */
    public void setTrackWidth(int width) {
        mTrackWidth = DensityUtils.dp2px(getContext(), width);
        trackPaint.setStrokeWidth(width);
        updateTheTrack();
        refreshTheView();
    }

    /**
     * set the width of progress stroke
     *
     * @param width stroke
     */
    public void setProgressWidth(int width) {
        mProgressWidth = DensityUtils.dp2px(getContext(), width);
        progressPaint.setStrokeWidth(width);
        refreshTheView();
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
     * set text color for progress text
     *
     * @param textColor
     */
    public void setProgressTextColor(@ColorInt int textColor) {
        this.mProgressTextColor = textColor;
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
     * set track color for progress background
     *
     * @param color bg color
     */
    public void setTrackColor(@ColorInt int color) {
        this.mTrackColor = color;
        refreshTheView();
    }

    /**
     * set content for progress inner space
     *
     * @param fillEnabled whether filled or not
     */
    public void setFillEnabled(boolean fillEnabled) {
        this.fillEnabled = fillEnabled;
        refreshTheView();
    }

    /**
     * set the broken circle for progress
     *
     * @param isBroken the circle broken or not
     */
    public void setCircleBroken(boolean isBroken) {
        this.circleBroken = isBroken;
        refreshTheView();
    }

    /**
     * set the scale zone type for progress view
     *
     * @param isGraduated have scale zone or not
     */
    public void setGraduatedEnabled(final boolean isGraduated) {
        this.isGraduated = isGraduated;
        post(new Runnable() {
            @Override
            public void run() {
                drawScaleZones(isGraduated);
            }
        });

    }

    /**
     * set the scale zone width for it
     *
     * @param zoneWidth each zone 's width
     */
    public void setScaleZoneWidth(float zoneWidth) {
        this.mScaleZoneWidth = DensityUtils.dp2px(getContext(), zoneWidth);
    }

    /**
     * set the scale zone length for it
     *
     * @param zoneLength each zone 's length
     */
    public void setScaleZoneLength(float zoneLength) {
        this.mScaleZoneLength = DensityUtils.dp2px(getContext(), zoneLength);
    }

    /**
     * set each zone's distance
     *
     * @param zonePadding distance
     */
    public void setScaleZonePadding(int zonePadding) {
        this.mScaleZonePadding = DensityUtils.dp2px(getContext(), zonePadding);
    }

    /**
     * set corner radius for each zone
     *
     * @param cornerRadius round rect zone's corner
     */
    public void setScaleZoneCornerRadius(int cornerRadius) {
        this.mScaleZoneCornerRadius = DensityUtils.dp2px(getContext(), cornerRadius);
    }

    /**
     * set the visibility for progress inner text
     *
     * @param visibility text visible or not
     */
    public void setProgressTextVisibility(boolean visibility) {
        this.textVisibility = visibility;
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
                if (mUpdateListener != null) {
                    mUpdateListener.onCircleProgressUpdate(CircleProgressView.this, progress);
                }

            }
        });
        progressAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (mUpdateListener != null) {
                    mUpdateListener.onCircleProgressStart(CircleProgressView.this);
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (mUpdateListener != null) {
                    mUpdateListener.onCircleProgressFinished(CircleProgressView.this);
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
     * refresh the layout
     */
    private void refreshTheView() {
        invalidate();
        requestLayout();
    }

    /**
     * update the oval progress track
     */
    private void updateTheTrack() {
        if (mOval != null) {
            mOval = null;
        }
        mOval = new RectF(getPaddingLeft() + mTrackWidth, getPaddingTop() + mTrackWidth,
                getWidth() - getPaddingRight() - mTrackWidth,
                getHeight() - getPaddingBottom() - mTrackWidth);

    }

    /**
     * init the circle progress drawing
     *
     * @param canvas   mCanvas
     * @param isFilled filled or not
     */
    private void initProgressDrawing(Canvas canvas, boolean isFilled) {
        if (isFilled) {
            progressPaint.setStyle(Paint.Style.FILL);
        } else {
            progressPaint.setStyle(Paint.Style.STROKE);
        }
        if (circleBroken) {
            canvas.drawArc(mOval, 135 + mStartProgress * 2.7f, (moveProgress - mStartProgress) * 2.7f, isFilled, progressPaint);
        } else {
            canvas.drawArc(mOval, 270 + mStartProgress * 3.6f, (moveProgress - mStartProgress) * 3.6f, isFilled, progressPaint);
        }
    }

    /**
     * 进度条更新监听
     */
    public interface CircleProgressUpdateListener {
        /**
         * 进度条开始更新
         *
         * @param view
         */
        void onCircleProgressStart(View view);

        /**
         * 进度条更新中
         *
         * @param view
         * @param progress
         */
        void onCircleProgressUpdate(View view, float progress);

        /**
         * 进度条更新结束
         *
         * @param view
         */
        void onCircleProgressFinished(View view);
    }

    /**
     * set the progress update listener for progress view
     *
     * @param listener update listener
     */
    public void setProgressViewUpdateListener(CircleProgressUpdateListener listener) {
        this.mUpdateListener = listener;
    }


}
