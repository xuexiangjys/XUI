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

package com.xuexiang.xuidemo.widget.calendar;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.xuexiang.xui.utils.DensityUtils;

/**
 * @author xuexiang
 * @since 2019-06-27 16:30
 */
public class SimpleMonthView extends MonthView {

    private int mRadius;
    private int mPadding;
    /**
     * 新数据标记的文本画笔
     */
    private Paint mCirclePaint = new Paint();
    private float mCircleRadius;
    /**
     * 背景圆点
     */
    private Paint mPointPaint = new Paint();
    /**
     * 圆点半径
     */
    private float mPointRadius;

    public SimpleMonthView(Context context) {
        super(context);
        setLayerType(View.LAYER_TYPE_SOFTWARE, mSelectedPaint);
        //4.0以上硬件加速会导致无效
        mSelectedPaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.SOLID));
        mCircleRadius = DensityUtils.dp2px(getContext(), 3);
        mPadding = DensityUtils.dp2px(getContext(), 2);
        mPointRadius = DensityUtils.dp2px(context, 2);

        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setTextAlign(Paint.Align.CENTER);
        mCirclePaint.setFakeBoldText(true);

        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;
        mSchemePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onLoopStart(int x, int y) {

    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        boolean isSelected = isSelected(calendar);
        if (isSelected) {
            mPointPaint.setColor(Color.WHITE);
        } else {
            mPointPaint.setColor(Color.GRAY);
        }
        canvas.drawCircle(x + (mItemWidth >> 1), y + mItemHeight - 3 * mPadding, mPointRadius, mPointPaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        if (hasScheme) {
            mCirclePaint.setColor(calendar.getSchemeColor());
            canvas.drawCircle(x + mItemWidth - mPadding - mCircleRadius / 2, y + mPadding + mCircleRadius, mCircleRadius, mCirclePaint);
        }

        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    mSelectTextPaint);
        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }
}
