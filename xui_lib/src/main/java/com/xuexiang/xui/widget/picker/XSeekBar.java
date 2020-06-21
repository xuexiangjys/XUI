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

package com.xuexiang.xui.widget.picker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 滑块选择器
 *
 * @author xuexiang
 * @since 2020-01-04 18:30
 */
public class XSeekBar extends View {

    public interface OnSeekBarListener {
        /**
         * 数字改变回调
         *
         * @param seekBar  控件
         * @param newValue 新数值
         */
        void onValueChanged(XSeekBar seekBar, int newValue);
    }

    private static int DEFAULT_TOUCH_TARGET_SIZE;
    private static int DEFAULT_TEXT_MIN_SPACE;
    private static final int DEFAULT_MAX = 100;
    /**
     * 刻度的宽度参数
     */
    private static final float DEFAULT_BIG_SCALE_WITH = 1.7f;
    private static final float DEFAULT_MIDDLE_SCALE_WITH = 1.2f;
    private static final float DEFAULT_SMALL_SCALE_WITH = 1.0f;

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mLineStartX;
    private int mLineEndX;
    private int mLineLength;
    private int mMaxPosition = 0;
    private int mRange;
    private int mMiddleY = 0;
    private Rect mMaxTextRect = new Rect();
    private Rect mRulerTextRect = new Rect();
    /**
     * List of event IDs touching targets
     */
    private Set<Integer> mTouchingMinTarget = new HashSet<>();
    private Set<Integer> mTouchingMaxTarget = new HashSet<>();
    private boolean mIsTouching = false;
    private boolean mLastTouchedMin;
    private int mSelectedNumber = -1;
    private boolean mIsFirstInit = true;
    private float mConvertFactor;
    private OnSeekBarListener mOnSeekBarListener;

    //========属性==========//
    private int mVerticalPadding;
    private int mInsideRangeColor;
    private int mOutsideRangeColor;
    private float mInsideRangeLineStrokeWidth;
    private float mOutsideRangeLineStrokeWidth;
    private int mMax = DEFAULT_MAX;
    private int mMin = 0;
    private Bitmap mSliderIcon;
    private Bitmap mSliderIconFocus;
    private boolean mIsLineRound;

    private boolean mIsShowBubble;
    private Bitmap mBubbleBitmap;
    private boolean mIsShowNumber;
    private int mNumberTextColor;
    private float mNumberTextSize;
    private float mNumberMarginBottom;

    private boolean mIsShowRuler;
    private int mRulerColor;
    private int mRulerTextColor;
    private float mRulerTextSize;
    private float mRulerMarginTop;
    private float mRulerDividerHeight;
    private float mRulerTextMarginTop;
    private int mRulerInterval;

    public XSeekBar(Context context) {
        this(context, null);
    }

    public XSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.XSeekBarStyle);
    }

    public XSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
    }

    public void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        DEFAULT_TOUCH_TARGET_SIZE = DensityUtils.dp2px(20);
        DEFAULT_TEXT_MIN_SPACE = DensityUtils.dp2px(2);
        int colorAccent = ThemeUtils.resolveColor(context, R.attr.colorAccent);
        int colorControlNormal = ThemeUtils.resolveColor(context, R.attr.colorControlNormal);

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.XSeekBar, defStyleAttr, 0);
            mVerticalPadding = array.getDimensionPixelSize(R.styleable.XSeekBar_xsb_verticalPadding, DensityUtils.dp2px(10));
            //滑条
            mInsideRangeColor = array.getColor(R.styleable.XSeekBar_xsb_insideRangeLineColor, colorAccent);
            mOutsideRangeColor = array.getColor(R.styleable.XSeekBar_xsb_outsideRangeLineColor, ResUtils.getColor(R.color.default_xrs_outside_line_color));
            mInsideRangeLineStrokeWidth = array.getDimensionPixelSize(R.styleable.XSeekBar_xsb_insideRangeLineStrokeWidth, DensityUtils.dp2px(5));
            mOutsideRangeLineStrokeWidth = array.getDimensionPixelSize(R.styleable.XSeekBar_xsb_outsideRangeLineStrokeWidth, DensityUtils.dp2px(5));
            mMin = array.getInt(R.styleable.XSeekBar_xsb_min, mMin);
            mMax = array.getInt(R.styleable.XSeekBar_xsb_max, mMax);
            mSliderIcon = BitmapFactory.decodeResource(getResources(), array.getResourceId(R.styleable.XSeekBar_xsb_sliderIcon, R.drawable.xui_ic_slider_icon));
            mSliderIconFocus = BitmapFactory.decodeResource(getResources(), array.getResourceId(R.styleable.XSeekBar_xsb_sliderIconFocus, R.drawable.xui_ic_slider_icon));
            mIsLineRound = array.getBoolean(R.styleable.XSeekBar_xsb_isLineRound, true);

            //气泡
            mIsShowBubble = array.getBoolean(R.styleable.XSeekBar_xsb_isShowBubble, false);
            boolean isFitColor = array.getBoolean(R.styleable.XSeekBar_xsb_isFitColor, true);
            mIsShowNumber = array.getBoolean(R.styleable.XSeekBar_xsb_isShowNumber, true);
            mNumberTextColor = array.getColor(R.styleable.XSeekBar_xsb_numberTextColor, colorAccent);
            mNumberTextSize = array.getDimensionPixelSize(R.styleable.XSeekBar_xsb_numberTextSize, DensityUtils.sp2px(12));
            mNumberMarginBottom = array.getDimensionPixelSize(R.styleable.XSeekBar_xsb_numberMarginBottom, DensityUtils.dp2px(2));
            if (isFitColor) {
                if (mIsShowBubble) {
                    mNumberTextColor = Color.WHITE;
                }
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), array.getResourceId(R.styleable.XSeekBar_xsb_bubbleResource, R.drawable.xui_bg_bubble_blue));
                if (bitmap != null) {
                    mBubbleBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    Canvas canvas = new Canvas(mBubbleBitmap);
                    canvas.drawColor(mInsideRangeColor, PorterDuff.Mode.SRC_IN);
                }
            } else {
                mBubbleBitmap = BitmapFactory.decodeResource(getResources(), array.getResourceId(R.styleable.XSeekBar_xsb_bubbleResource, R.drawable.xui_bg_bubble_blue));
            }

            //刻度尺
            mIsShowRuler = array.getBoolean(R.styleable.XSeekBar_xsb_isShowRuler, false);
            mRulerColor = array.getColor(R.styleable.XSeekBar_xsb_rulerColor, colorControlNormal);
            mRulerTextColor = array.getColor(R.styleable.XSeekBar_xsb_rulerTextColor, colorControlNormal);
            mRulerTextSize = array.getDimensionPixelSize(R.styleable.XSeekBar_xsb_rulerTextSize, DensityUtils.sp2px(12));
            mRulerMarginTop = array.getDimensionPixelSize(R.styleable.XSeekBar_xsb_rulerMarginTop, DensityUtils.dp2px(4));
            mRulerDividerHeight = array.getDimensionPixelSize(R.styleable.XSeekBar_xsb_rulerDividerHeight, DensityUtils.dp2px(4));
            mRulerTextMarginTop = array.getDimensionPixelSize(R.styleable.XSeekBar_xsb_rulerTextMarginTop, DensityUtils.dp2px(4));
            mRulerInterval = array.getInt(R.styleable.XSeekBar_xsb_rulerInterval, 20);

            array.recycle();
        }

        mRange = mMax - mMin;

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int desiredWidth = widthSize;
        int desiredHeight;

        getTextBounds(String.valueOf(mMax), mMaxTextRect);

        if (mIsShowNumber && mIsShowBubble) {
            desiredHeight = (int) (mSliderIcon.getHeight() + mNumberMarginBottom) + mBubbleBitmap.getHeight();
        } else if (mIsShowNumber) {
            desiredHeight = (int) (mSliderIcon.getHeight() + mNumberMarginBottom);
        } else {
            desiredHeight = mSliderIcon.getHeight();
        }

        int rulerHeight = (int) (mRulerMarginTop + mRulerDividerHeight * 3 + mRulerTextMarginTop + mRulerTextRect.height());
        if (mIsShowRuler) {
            getRulerTextBounds(String.valueOf(mMin), mRulerTextRect);
            desiredHeight += rulerHeight;
        }

        int width;
        int height = desiredHeight;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = desiredHeight;
        }

        height += mVerticalPadding;

        int marginStartEnd = mIsShowBubble ? mBubbleBitmap.getWidth() : Math.max(mSliderIcon.getWidth(), mMaxTextRect.width());

        mLineLength = (width - marginStartEnd);
        mMiddleY = mIsShowRuler ? height - rulerHeight - mSliderIcon.getHeight() / 2 : height - mSliderIcon.getHeight() / 2;
        mLineStartX = marginStartEnd / 2;
        mLineEndX = mLineLength + marginStartEnd / 2;

        calculateConvertFactor();

        if (mIsFirstInit) {
            setSelectedValue(mSelectedNumber != -1 ? mSelectedNumber : mMax);
        }

        height += mVerticalPadding;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawEntireRangeLine(canvas);
        drawSelectedRangeLine(canvas);
        if (mIsShowNumber) {
            drawSelectedNumber(canvas);
        }
        drawRuler(canvas);
        drawSelectedTargets(canvas);
    }

    private void drawEntireRangeLine(Canvas canvas) {
        mPaint.setColor(mOutsideRangeColor);
        mPaint.setStrokeWidth(mOutsideRangeLineStrokeWidth);
        canvas.drawLine(mLineStartX, mMiddleY, mLineEndX, mMiddleY, mPaint);

        if (mIsLineRound) {
            mPaint.setColor(mInsideRangeColor);
            canvas.drawCircle(mLineStartX, mMiddleY, mOutsideRangeLineStrokeWidth / 2, mPaint);
            mPaint.setColor(mOutsideRangeColor);
            canvas.drawCircle(mLineEndX, mMiddleY, mOutsideRangeLineStrokeWidth / 2, mPaint);
        }
    }

    private void drawSelectedRangeLine(Canvas canvas) {
        mPaint.setStrokeWidth(mInsideRangeLineStrokeWidth);
        mPaint.setColor(mInsideRangeColor);
        canvas.drawLine(mLineStartX, mMiddleY, mMaxPosition, mMiddleY, mPaint);
    }

    private void drawSelectedNumber(Canvas canvas) {

        String max = String.valueOf(getSelectedNumber());

        getTextBounds(max, mMaxTextRect);


        float yText;
        //bubble
        if (mIsShowBubble) {
            float top = mMiddleY - mSliderIcon.getHeight() / 2F - mBubbleBitmap.getHeight() - mNumberMarginBottom;
            yText = top + mBubbleBitmap.getHeight() / 2F + mMaxTextRect.height() / 2F - 6;
            canvas.drawBitmap(mBubbleBitmap, mMaxPosition - mBubbleBitmap.getWidth() / 2F, top, mPaint);
        } else {
            yText = mMiddleY - mSliderIcon.getHeight() / 2F - mNumberMarginBottom;
        }
        //text
        float maxX = mMaxPosition - mMaxTextRect.width() / 2F;
        mPaint.setTextSize(mNumberTextSize);
        mPaint.setColor(mNumberTextColor);
        canvas.drawText(max, maxX, yText, mPaint);
    }

    private void drawRuler(Canvas canvas) {
        if (mIsShowRuler) {
            float startX = mLineStartX;
            float stopY = 0;
            float startY = 0;
            float divider = (float) mRulerInterval / 10f;
            float scaleLength = (float) mLineLength / ((mMax - mMin) / divider) / divider;

            boolean isMinHasText = false;
            boolean isMaxHasText = false;

            for (int i = mMin; i <= mMax; i++) {
                if (i % mRulerInterval == 0) {
                    //draw big scale
                    startY = mMiddleY + mSliderIcon.getHeight() / 2F + mRulerMarginTop;
                    stopY = startY + mRulerDividerHeight * 3;

                    mPaint.setColor(mRulerTextColor);
                    mPaint.setTextSize(mRulerTextSize);
                    getRulerTextBounds(String.valueOf(i), mRulerTextRect);
                    canvas.drawText(String.valueOf(i), startX - mRulerTextRect.width() / 2F, stopY + mRulerTextRect.height() + mRulerTextMarginTop, mPaint);

                    if (i == mMin) {
                        isMinHasText = true;
                    }
                    if (i == mMax) {
                        isMaxHasText = true;
                    }
                    mPaint.setStrokeWidth(DEFAULT_BIG_SCALE_WITH);


                    mPaint.setColor(mRulerColor);

                    canvas.drawLine(startX, startY, startX, stopY, mPaint);

                } else if (i % (mRulerInterval / 2) == 0 && mRulerInterval % 10 == 0) {
                    //draw middle scale
                    startY = mMiddleY + mSliderIcon.getHeight() / 2F + mRulerMarginTop;
                    stopY = startY + mRulerDividerHeight * 2;
                    mPaint.setStrokeWidth(DEFAULT_MIDDLE_SCALE_WITH);

                    mPaint.setColor(mRulerColor);
                    canvas.drawLine(startX, startY, startX, stopY, mPaint);


                } else {
                    //draw small scale
                    startY = mMiddleY + mSliderIcon.getHeight() / 2F + mRulerMarginTop;
                    stopY = startY + mRulerDividerHeight;
                    mPaint.setStrokeWidth(DEFAULT_SMALL_SCALE_WITH);

                    if (i % (mRulerInterval / 10) == 0) {
                        mPaint.setColor(mRulerColor);
                        canvas.drawLine(startX, startY, startX, stopY, mPaint);
                    }

                }

                if ((i == mMax && !isMaxHasText) || (i == mMin && !isMinHasText)) {

                    mPaint.setColor(mRulerTextColor);
                    mPaint.setTextSize(mRulerTextSize);
                    getRulerTextBounds(String.valueOf(i), mRulerTextRect);

                    float x = startX - mRulerTextRect.width() / 2F;
                    //修正最大值与最小值文本与满刻度文本太靠近时显示重叠问题
                    if (i == mMax && i % mRulerInterval == 1) {
                        x = startX + DEFAULT_TEXT_MIN_SPACE;
                    }

                    if (i == mMin && i % mRulerInterval == mRulerInterval - 1) {
                        x = startX - mRulerTextRect.width() / 2F - DEFAULT_TEXT_MIN_SPACE;
                    }

                    canvas.drawText(String.valueOf(i), x, startY + mRulerDividerHeight * 3 + mRulerTextRect.height() + mRulerTextMarginTop, mPaint);

                }
                startX += scaleLength;
            }
        }
    }

    private void drawSelectedTargets(Canvas canvas) {
        mPaint.setColor(mInsideRangeColor);
        canvas.drawCircle(mMaxPosition, mMiddleY, DensityUtils.dp2px(3), mPaint);
        if (!mIsTouching) {
            canvas.drawBitmap(mSliderIcon, mMaxPosition - mSliderIcon.getWidth() / 2F, mMiddleY - mSliderIcon.getWidth() / 2F, mPaint);
        } else {
            canvas.drawBitmap(mSliderIconFocus, mMaxPosition - mSliderIcon.getWidth() / 2F, mMiddleY - mSliderIcon.getWidth() / 2F, mPaint);
        }
    }

    private void getTextBounds(String text, Rect rect) {
        mPaint.setTextSize(mNumberTextSize);
        mPaint.getTextBounds(text, 0, text.length(), rect);
    }

    private void getRulerTextBounds(String text, Rect rect) {
        mPaint.setTextSize(mRulerTextSize);
        mPaint.getTextBounds(text, 0, text.length(), rect);
    }

    private void jumpToPosition(int index, MotionEvent event) {
        //user has touched outside the target, lets jump to that position
        if (event.getX(index) > mMaxPosition && event.getX(index) <= mLineEndX) {
            mMaxPosition = (int) event.getX(index);
            invalidate();
            callMaxChangedCallbacks();
        } else if (event.getX(index) < mMaxPosition && event.getX(index) >= mLineStartX) {
            mMaxPosition = (int) event.getX(index);
            invalidate();
            callMaxChangedCallbacks();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        mIsFirstInit = false;

        final int actionIndex = event.getActionIndex();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                updateTouchStatus(true);

                if (mLastTouchedMin) {
                    if (!checkTouchingMinTarget(actionIndex, event)
                            && !checkTouchingMaxTarget(actionIndex, event)) {
                        jumpToPosition(actionIndex, event);
                    }
                } else if (!checkTouchingMaxTarget(actionIndex, event)
                        && !checkTouchingMinTarget(actionIndex, event)) {
                    jumpToPosition(actionIndex, event);
                }

                invalidate();
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                updateTouchStatus(false);

                mTouchingMinTarget.remove(event.getPointerId(actionIndex));
                mTouchingMaxTarget.remove(event.getPointerId(actionIndex));

                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                updateTouchStatus(true);

                for (int i = 0; i < event.getPointerCount(); i++) {
                    if (mTouchingMinTarget.contains(event.getPointerId(i))) {
                        int touchX = (int) event.getX(i);
                        touchX = clamp(touchX, mLineStartX, mLineEndX);
                        if (touchX >= mMaxPosition) {
                            mMaxPosition = touchX;
                            callMaxChangedCallbacks();
                        }
                    }
                    if (mTouchingMaxTarget.contains(event.getPointerId(i))) {
                        int touchX = (int) event.getX(i);
                        touchX = clamp(touchX, mLineStartX, mLineEndX);
                        mMaxPosition = touchX;
                        callMaxChangedCallbacks();
                    }
                }
                invalidate();
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                updateTouchStatus(true);

                for (int i = 0; i < event.getPointerCount(); i++) {
                    if (mLastTouchedMin) {
                        if (!checkTouchingMinTarget(i, event)
                                && !checkTouchingMaxTarget(i, event)) {
                            jumpToPosition(i, event);
                        }
                    } else if (!checkTouchingMaxTarget(i, event)
                            && !checkTouchingMinTarget(i, event)) {
                        jumpToPosition(i, event);
                    }
                }

                break;

            case MotionEvent.ACTION_CANCEL:
                updateTouchStatus(false);

                mTouchingMinTarget.clear();
                mTouchingMaxTarget.clear();
                invalidate();
                break;

            default:
                break;
        }

        return true;
    }

    /**
     * 更新触摸状态
     *
     * @param isTouching 是否触摸
     */
    private void updateTouchStatus(boolean isTouching) {
        mIsTouching = isTouching;
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(isTouching);
        }
    }

    /**
     * Checks if given index is touching the min target.  If touching start animation.
     */
    private boolean checkTouchingMinTarget(int index, MotionEvent event) {
        if (isTouchingMinTarget(index, event)) {
            mLastTouchedMin = true;
            mTouchingMinTarget.add(event.getPointerId(index));
            return true;
        }
        return false;
    }

    /**
     * Checks if given index is touching the max target.  If touching starts animation.
     */
    private boolean checkTouchingMaxTarget(int index, MotionEvent event) {
        if (isTouchingMaxTarget(index, event)) {
            mLastTouchedMin = false;
            mTouchingMaxTarget.add(event.getPointerId(index));
            return true;
        }
        return false;
    }


    private void callMaxChangedCallbacks() {
        if (mOnSeekBarListener != null) {
            mOnSeekBarListener.onValueChanged(this, getSelectedNumber());
        }
    }

    private boolean isTouchingMinTarget(int pointerIndex, MotionEvent event) {
        return false;
    }

    private boolean isTouchingMaxTarget(int pointerIndex, MotionEvent event) {
        return event.getX(pointerIndex) > mMaxPosition - DEFAULT_TOUCH_TARGET_SIZE
                && event.getX(pointerIndex) < mMaxPosition + DEFAULT_TOUCH_TARGET_SIZE
                && event.getY(pointerIndex) > mMiddleY - DEFAULT_TOUCH_TARGET_SIZE
                && event.getY(pointerIndex) < mMiddleY + DEFAULT_TOUCH_TARGET_SIZE;
    }

    private void calculateConvertFactor() {
        mConvertFactor = ((float) mRange) / mLineLength;
    }

    public int getSelectedNumber() {
        return Math.round((mMaxPosition - mLineStartX) * mConvertFactor + mMin);
    }

    public void setDefaultValue(int value) {
        mSelectedNumber = value;
        setSelectedValue(value);
        invalidate();
    }

    private void setSelectedValue(int selectedMax) {
        mMaxPosition = Math.round(((selectedMax - mMin) / mConvertFactor) + mLineStartX);
        callMaxChangedCallbacks();
    }

    public void setOnSeekBarListener(OnSeekBarListener listener) {
        mOnSeekBarListener = listener;
    }

    public int getMin() {
        return mMin;
    }

    public void setMin(int min) {
        mMin = min;
        mRange = mMax - min;
    }

    public int getMax() {
        return mMax;
    }

    public void setMax(int max) {
        mMax = max;
        mRange = max - mMin;
    }

    public void setInterval(int rulerInterval) {
        mRulerInterval = rulerInterval;
        invalidate();
    }

    /**
     * Resets selected values to MIN and MAX.
     */
    public void reset() {
        mMaxPosition = mLineEndX;
        if (mOnSeekBarListener != null) {
            mOnSeekBarListener.onValueChanged(this, getSelectedNumber());
        }
        invalidate();
    }


    /**
     * Keeps Number value inside min/max bounds by returning min or max if outside of
     * bounds.  Otherwise will return the value without altering.
     */
    private <T extends Number> T clamp(@NonNull T value, @NonNull T min, @NonNull T max) {
        if (value.doubleValue() > max.doubleValue()) {
            return max;
        } else if (value.doubleValue() < min.doubleValue()) {
            return min;
        }
        return value;
    }

}