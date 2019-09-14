/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xui.widget.progress.materialprogressbar;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import android.support.annotation.Keep;

import com.xuexiang.xui.utils.ThemeUtils;

/**
 * A backported {@code Drawable} for indeterminate horizontal {@code ProgressBar}.
 */
public class IndeterminateHorizontalProgressDrawable extends BaseIndeterminateProgressDrawable
        implements MaterialProgressDrawable, ShowBackgroundDrawable {

    private static final int PROGRESS_INTRINSIC_HEIGHT_DP = 4;
    private static final int PADDED_INTRINSIC_HEIGHT_DP = 16;
    private static final RectF RECT_BOUND = new RectF(-180, -1, 180, 1);
    private static final RectF RECT_PADDED_BOUND = new RectF(-180, -4, 180, 4);
    private static final RectF RECT_PROGRESS = new RectF(-144, -1, 144, 1);
    private static final RectTransformX RECT_1_TRANSFORM_X = new RectTransformX(-522.6f, 0.1f);
    private static final RectTransformX RECT_2_TRANSFORM_X = new RectTransformX(-197.6f, 0.1f);

    private int mProgressIntrinsicHeight;
    private int mPaddedIntrinsicHeight;
    private boolean mShowBackground = true;
    private float mBackgroundAlpha;

    private RectTransformX mRect1TransformX = new RectTransformX(RECT_1_TRANSFORM_X);
    private RectTransformX mRect2TransformX = new RectTransformX(RECT_2_TRANSFORM_X);

    /**
     * Create a new {@code IndeterminateHorizontalProgressDrawable}.
     *
     * @param context the {@code Context} for retrieving style information.
     */
    public IndeterminateHorizontalProgressDrawable(Context context) {
        super(context);

        float density = context.getResources().getDisplayMetrics().density;
        mProgressIntrinsicHeight = Math.round(PROGRESS_INTRINSIC_HEIGHT_DP * density);
        mPaddedIntrinsicHeight = Math.round(PADDED_INTRINSIC_HEIGHT_DP * density);

        mBackgroundAlpha = ThemeUtils.resolveFloat(context, android.R.attr.disabledAlpha, 0);

        mAnimators = new Animator[] {
                Animators.createIndeterminateHorizontalRect1(mRect1TransformX),
                Animators.createIndeterminateHorizontalRect2(mRect2TransformX)
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getShowBackground() {
        return mShowBackground;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setShowBackground(boolean show) {
        if (mShowBackground != show) {
            mShowBackground = show;
            invalidateSelf();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIntrinsicHeight() {
        return mUseIntrinsicPadding ? mPaddedIntrinsicHeight : mProgressIntrinsicHeight;
    }

    @Override
    protected void onPreparePaint(Paint paint) {
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas, int width, int height, Paint paint) {

        if (mUseIntrinsicPadding) {
            canvas.scale(width / RECT_PADDED_BOUND.width(), height / RECT_PADDED_BOUND.height());
            canvas.translate(RECT_PADDED_BOUND.width() / 2, RECT_PADDED_BOUND.height() / 2);
        } else {
            canvas.scale(width / RECT_BOUND.width(), height / RECT_BOUND.height());
            canvas.translate(RECT_BOUND.width() / 2, RECT_BOUND.height() / 2);
        }

        if (mShowBackground) {
            paint.setAlpha(Math.round(mAlpha * mBackgroundAlpha));
            drawBackgroundRect(canvas, paint);
            paint.setAlpha(mAlpha);
        }
        drawProgressRect(canvas, mRect2TransformX, paint);
        drawProgressRect(canvas, mRect1TransformX, paint);
    }

    private static void drawBackgroundRect(Canvas canvas, Paint paint) {
        canvas.drawRect(RECT_BOUND, paint);
    }

    private static void drawProgressRect(Canvas canvas, RectTransformX transformX, Paint paint) {

        int saveCount = canvas.save();
        canvas.translate(transformX.mTranslateX, 0);
        canvas.scale(transformX.mScaleX, 1);

        canvas.drawRect(RECT_PROGRESS, paint);

        canvas.restoreToCount(saveCount);
    }

    private static class RectTransformX {

        public float mTranslateX;
        public float mScaleX;

        public RectTransformX(float translateX, float scaleX) {
            mTranslateX = translateX;
            mScaleX = scaleX;
        }

        public RectTransformX(RectTransformX that) {
            mTranslateX = that.mTranslateX;
            mScaleX = that.mScaleX;
        }

        @Keep
        public void setTranslateX(float translateX) {
            mTranslateX = translateX;
        }

        @Keep
        public void setScaleX(float scaleX) {
            mScaleX = scaleX;
        }
    }
}
