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

/**
 * A backported {@code Drawable} for indeterminate circular {@code ProgressBar}.
 */
public class IndeterminateCircularProgressDrawable extends BaseIndeterminateProgressDrawable
        implements MaterialProgressDrawable {

    private static final int PROGRESS_INTRINSIC_SIZE_DP = 42;
    private static final int PADDED_INTRINSIC_SIZE_DP = 48;
    private static final RectF RECT_BOUND = new RectF(-21, -21, 21, 21);
    private static final RectF RECT_PADDED_BOUND = new RectF(-24, -24, 24, 24);
    private static final RectF RECT_PROGRESS = new RectF(-19, -19, 19, 19);

    private int mProgressIntrinsicSize;
    private int mPaddedIntrinsicSize;

    private RingPathTransform mRingPathTransform = new RingPathTransform();
    private RingRotation mRingRotation = new RingRotation();

    /**
     * Create a new {@code IndeterminateCircularProgressDrawable}.
     *
     * @param context the {@code Context} for retrieving style information.
     */
    public IndeterminateCircularProgressDrawable(Context context) {
        super(context);

        float density = context.getResources().getDisplayMetrics().density;
        mProgressIntrinsicSize = Math.round(PROGRESS_INTRINSIC_SIZE_DP * density);
        mPaddedIntrinsicSize = Math.round(PADDED_INTRINSIC_SIZE_DP * density);

        mAnimators = new Animator[] {
                Animators.createIndeterminate(mRingPathTransform),
                Animators.createIndeterminateRotation(mRingRotation)
        };
    }

    private int getIntrinsicSize() {
        return mUseIntrinsicPadding ? mPaddedIntrinsicSize : mProgressIntrinsicSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIntrinsicWidth() {
        return getIntrinsicSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIntrinsicHeight() {
        return getIntrinsicSize();
    }

    @Override
    protected void onPreparePaint(Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setStrokeJoin(Paint.Join.MITER);
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

        drawRing(canvas, paint);
    }

    private void drawRing(Canvas canvas, Paint paint) {

        int saveCount = canvas.save();
        canvas.rotate(mRingRotation.mRotation);

        // startAngle starts at 3 o'clock on a watch.
        float startAngle = -90 + 360 * (mRingPathTransform.mTrimPathOffset
                + mRingPathTransform.mTrimPathStart);
        float sweepAngle = 360 * (mRingPathTransform.mTrimPathEnd
                - mRingPathTransform.mTrimPathStart);
        canvas.drawArc(RECT_PROGRESS, startAngle, sweepAngle, false, paint);

        canvas.restoreToCount(saveCount);
    }

    private static class RingPathTransform {

        public float mTrimPathStart;
        public float mTrimPathEnd;
        public float mTrimPathOffset;

        @Keep
        public void setTrimPathStart(float trimPathStart) {
            mTrimPathStart = trimPathStart;
        }

        @Keep
        public void setTrimPathEnd(float trimPathEnd) {
            mTrimPathEnd = trimPathEnd;
        }

        @Keep
        public void setTrimPathOffset(float trimPathOffset) {
            mTrimPathOffset = trimPathOffset;
        }
    }

    private static class RingRotation {

        private float mRotation;

        @Keep
        public void setRotation(float rotation) {
            mRotation = rotation;
        }
    }
}
