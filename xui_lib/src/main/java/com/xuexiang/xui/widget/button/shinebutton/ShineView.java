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
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.xuexiang.xui.widget.button.shinebutton.interpolator.Ease;
import com.xuexiang.xui.widget.button.shinebutton.interpolator.EasingInterpolator;

import java.util.Random;

/**
 * @author Chad
 * @title com.sackcentury.shinebuttonlib
 * @description
 * @modifier
 * @date
 * @since 16/7/5 下午3:57
 **/
public class ShineView extends View {
    /**
     * default 10ms ,change to 25ms for saving cpu
     */
    private static final long FRAME_REFRESH_DELAY = 25;

    private ShineAnimator mShineAnimator;
    private ValueAnimator mClickAnimator;

    private ShineButton mShineButton;
    private Paint mPaint;
    private Paint mPaint2;
    private Paint mPaintSmall;

    private int mColorCount = 10;
    private static int[] sColorRandom = new int[10];

    private int mShineCount;
    private float mSmallOffsetAngle;
    private float mTurnAngle;
    private long mAnimDuration;
    private long mClickAnimDuration;
    private float mShineDistanceMultiple;
    private int mSmallShineColor = sColorRandom[0];
    private int mBigShineColor = sColorRandom[1];

    private int mShineSize = 0;

    private boolean mAllowRandomColor = false;
    private boolean mEnableFlashing = false;

    private RectF mRectF = new RectF();
    private RectF mRectFSmall = new RectF();

    private Random mRandom = new Random();
    private int mCenterAnimX;
    private int mCenterAnimY;
    private int mBtnWidth;
    private int mBtnHeight;

    private float mValue;
    private float mClickValue = 0;
    private boolean mIsAnimating = false;
    private float mDistanceOffset = 0.2f;

    public ShineView(Context context) {
        super(context);
    }

    public ShineView(Context context, final ShineButton shineButton, ShineParams shineParams) {
        super(context);
        initShineParams(shineParams, shineButton);
        mShineAnimator = new ShineAnimator(mAnimDuration, mShineDistanceMultiple, mClickAnimDuration);
        ValueAnimator.setFrameDelay(FRAME_REFRESH_DELAY);
        mShineButton = shineButton;

        mPaint = new Paint();
        mPaint.setColor(mBigShineColor);
        mPaint.setStrokeWidth(20);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaint2 = new Paint();
        mPaint2.setColor(Color.WHITE);
        mPaint2.setStrokeWidth(20);
        mPaint2.setStrokeCap(Paint.Cap.ROUND);

        mPaintSmall = new Paint();
        mPaintSmall.setColor(mSmallShineColor);
        mPaintSmall.setStrokeWidth(10);
        mPaintSmall.setStyle(Paint.Style.STROKE);
        mPaintSmall.setStrokeCap(Paint.Cap.ROUND);

        mClickAnimator = ValueAnimator.ofFloat(0f, 1.1f);
        ValueAnimator.setFrameDelay(FRAME_REFRESH_DELAY);
        mClickAnimator.setDuration(mClickAnimDuration);
        mClickAnimator.setInterpolator(new EasingInterpolator(Ease.QUART_OUT));
        mClickAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mClickValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mClickAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                mClickValue = 0;
                invalidate();
            }
        });
        mShineAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                shineButton.removeView(ShineView.this);
            }
        });

    }


    public ShineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void showAnimation(ShineButton shineButton) {
        mBtnWidth = shineButton.getWidth();
        mBtnHeight = shineButton.getHeight();
        int[] location = new int[2];
        shineButton.getLocationInWindow(location);
        mCenterAnimX = location[0] + shineButton.getWidth() / 2;
        mCenterAnimY = location[1] + shineButton.getHeight() / 2;

        if (shineButton.mWindow != null) {
            View decor = shineButton.mWindow.getDecorView();
            mCenterAnimX = mCenterAnimX - decor.getPaddingLeft();
            mCenterAnimY = mCenterAnimY - decor.getPaddingTop();
        }

        mShineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mValue = (float) valueAnimator.getAnimatedValue();
                if (mShineSize != 0 && mShineSize > 0) {
                    mPaint.setStrokeWidth((mShineSize) * (mShineDistanceMultiple - mValue));
                    mPaintSmall.setStrokeWidth(((float) mShineSize / 3 * 2) * (mShineDistanceMultiple - mValue));
                } else {
                    mPaint.setStrokeWidth((mBtnWidth / 2) * (mShineDistanceMultiple - mValue));
                    mPaintSmall.setStrokeWidth((mBtnWidth / 3) * (mShineDistanceMultiple - mValue));
                }
                mRectF.set(mCenterAnimX - (mBtnWidth / (3 - mShineDistanceMultiple) * mValue), mCenterAnimY - (mBtnHeight / (3 - mShineDistanceMultiple) * mValue), mCenterAnimX + (mBtnWidth / (3 - mShineDistanceMultiple) * mValue), mCenterAnimY + (mBtnHeight / (3 - mShineDistanceMultiple) * mValue));
                mRectFSmall.set(mCenterAnimX - (mBtnWidth / ((3 - mShineDistanceMultiple) + mDistanceOffset) * mValue), mCenterAnimY - (mBtnHeight / ((3 - mShineDistanceMultiple) + mDistanceOffset) * mValue), mCenterAnimX + (mBtnWidth / ((3 - mShineDistanceMultiple) + mDistanceOffset) * mValue), mCenterAnimY + (mBtnHeight / ((3 - mShineDistanceMultiple) + mDistanceOffset) * mValue));
                invalidate();
            }
        });
        mShineAnimator.start();
        mClickAnimator.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mShineCount; i++) {
            if (mAllowRandomColor) {
                mPaint.setColor(sColorRandom[Math.abs(mColorCount / 2 - i) >= mColorCount ? mColorCount - 1 : Math.abs(mColorCount / 2 - i)]);
            }
            canvas.drawArc(mRectF, 360f / mShineCount * i + 1 + ((mValue - 1) * mTurnAngle), 0.1f, false, getConfigPaint(mPaint));
        }
        for (int i = 0; i < mShineCount; i++) {
            if (mAllowRandomColor) {
                mPaint.setColor(sColorRandom[Math.abs(mColorCount / 2 - i) >= mColorCount ? mColorCount - 1 : Math.abs(mColorCount / 2 - i)]);
            }
            canvas.drawArc(mRectFSmall, 360f / mShineCount * i + 1 - mSmallOffsetAngle + ((mValue - 1) * mTurnAngle), 0.1f, false, getConfigPaint(mPaintSmall));
        }
        mPaint.setStrokeWidth(mBtnWidth * (mClickValue) * (mShineDistanceMultiple - mDistanceOffset));
        if (mClickValue != 0) {
            mPaint2.setStrokeWidth(mBtnWidth * (mClickValue) * (mShineDistanceMultiple - mDistanceOffset) - 8);
        } else {
            mPaint2.setStrokeWidth(0);
        }
        canvas.drawPoint(mCenterAnimX, mCenterAnimY, mPaint);
        canvas.drawPoint(mCenterAnimX, mCenterAnimY, mPaint2);
        if (mShineAnimator != null && !mIsAnimating) {
            mIsAnimating = true;
            showAnimation(mShineButton);
        }
    }

    private Paint getConfigPaint(Paint paint) {
        if (mEnableFlashing) {
            paint.setColor(sColorRandom[mRandom.nextInt(mColorCount - 1)]);
        }
        return paint;
    }

    public static class ShineParams {
        ShineParams() {
            sColorRandom[0] = Color.parseColor("#FFFF99");
            sColorRandom[1] = Color.parseColor("#FFCCCC");
            sColorRandom[2] = Color.parseColor("#996699");
            sColorRandom[3] = Color.parseColor("#FF6666");
            sColorRandom[4] = Color.parseColor("#FFFF66");
            sColorRandom[5] = Color.parseColor("#F44336");
            sColorRandom[6] = Color.parseColor("#666666");
            sColorRandom[7] = Color.parseColor("#CCCC00");
            sColorRandom[8] = Color.parseColor("#666666");
            sColorRandom[9] = Color.parseColor("#999933");
        }

        public boolean allowRandomColor = false;
        public long animDuration = 1500;
        public int bigShineColor = 0;
        public long clickAnimDuration = 200;
        public boolean enableFlashing = false;
        public int shineCount = 7;
        public float shineTurnAngle = 20;
        public float shineDistanceMultiple = 1.5f;
        public float smallShineOffsetAngle = 20;
        public int smallShineColor = 0;
        public int shineSize = 0;
    }

    private void initShineParams(ShineParams shineParams, ShineButton shineButton) {
        mShineCount = shineParams.shineCount;
        mTurnAngle = shineParams.shineTurnAngle;
        mSmallOffsetAngle = shineParams.smallShineOffsetAngle;
        mEnableFlashing = shineParams.enableFlashing;
        mAllowRandomColor = shineParams.allowRandomColor;
        mShineDistanceMultiple = shineParams.shineDistanceMultiple;
        mAnimDuration = shineParams.animDuration;
        mClickAnimDuration = shineParams.clickAnimDuration;
        mSmallShineColor = shineParams.smallShineColor;
        mBigShineColor = shineParams.bigShineColor;
        mShineSize = shineParams.shineSize;
        if (mSmallShineColor == 0) {
            mSmallShineColor = sColorRandom[6];
        }

        if (mBigShineColor == 0) {
            mBigShineColor = shineButton.getColor();
        }

    }
}
