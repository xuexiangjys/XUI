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

package com.xuexiang.xui.widget.slideback.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

/**
 * 边缘返回的图标View
 *
 * @author xuexiang
 * @since 2019-08-30 9:29
 */
public class SlideBackIconView extends View {
    private Path bgPath, arrowPath; // 路径对象
    private Paint bgPaint, arrowPaint; // 画笔对象

    @ColorInt
    private int backViewColor = Color.BLACK; // 控件背景色
    private float backViewHeight = 0; // 控件高度
    private float arrowSize = 10; // 箭头图标大小
    private float maxSlideLength = 0; // 最大拉动距离

    private float slideLength = 0; // 当前拉动距离

    public SlideBackIconView(Context context) {
        this(context, null);
    }

    public SlideBackIconView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideBackIconView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化 路径与画笔
     * Path & Paint
     */
    private void init() {
        bgPath = new Path();
        arrowPath = new Path();

        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL_AND_STROKE); // 填充内部和描边
        bgPaint.setColor(backViewColor);
        bgPaint.setStrokeWidth(1); // 画笔宽度

        arrowPaint = new Paint();
        arrowPaint.setAntiAlias(true);
        arrowPaint.setStyle(Paint.Style.STROKE); // 描边
        arrowPaint.setColor(Color.WHITE);
        arrowPaint.setStrokeWidth(8); // 画笔宽度
        arrowPaint.setStrokeJoin(Paint.Join.ROUND); // * 结合处的样子 ROUND:圆弧

        setAlpha(0);
    }

    /**
     * 因为过程中会多次绘制，所以要先重置路径再绘制。
     * 贝塞尔曲线没什么好说的，相关文章有很多。此曲线经我测试比较类似“即刻App”。
     * <p>
     * 方便阅读再写一遍，此段代码中的变量定义：
     * backViewHeight   控件高度
     * slideLength      当前拉动距离
     * maxSlideLength   最大拉动距离
     * arrowSize        箭头图标大小
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 背景
        bgPath.reset(); // 会多次绘制，所以先重置
        bgPath.moveTo(0, 0);
        bgPath.cubicTo(0, backViewHeight * 2 / 9, slideLength, backViewHeight / 3, slideLength, backViewHeight / 2);
        bgPath.cubicTo(slideLength, backViewHeight * 2 / 3, 0, backViewHeight * 7 / 9, 0, backViewHeight);
        canvas.drawPath(bgPath, bgPaint); // 根据设置的贝塞尔曲线路径用画笔绘制

        // 箭头是先直线由短变长再折弯变成箭头状的
        // 依据当前拉动距离和最大拉动距离计算箭头大小值
        // 大小到一定值后开始折弯，计算箭头角度值
        float arrowZoom = slideLength / maxSlideLength; // 箭头大小变化率
        float arrowAngle = arrowZoom < 0.75f ? 0 : (arrowZoom - 0.75f) * 2; // 箭头角度变化率
        // 箭头
        arrowPath.reset(); // 先重置
        // 结合箭头大小值与箭头角度值设置折线路径
        arrowPath.moveTo(slideLength / 2 + (arrowSize * arrowAngle), backViewHeight / 2 - (arrowZoom * arrowSize));
        arrowPath.lineTo(slideLength / 2 - (arrowSize * arrowAngle), backViewHeight / 2);
        arrowPath.lineTo(slideLength / 2 + (arrowSize * arrowAngle), backViewHeight / 2 + (arrowZoom * arrowSize));
        canvas.drawPath(arrowPath, arrowPaint);

        setAlpha(slideLength / maxSlideLength - 0.2f); // 最多0.8透明度
    }

    /**
     * 更新当前拉动距离并重绘
     *
     * @param slideLength 当前拉动距离
     */
    public void updateSlideLength(float slideLength) {
        this.slideLength = slideLength;
        invalidate(); // 会再次调用onDraw
    }

    /**
     * 设置最大拉动距离
     *
     * @param maxSlideLength px值
     */
    public void setMaxSlideLength(float maxSlideLength) {
        this.maxSlideLength = maxSlideLength;
    }

    /**
     * 设置箭头图标大小
     *
     * @param arrowSize px值
     */
    public void setArrowSize(float arrowSize) {
        this.arrowSize = arrowSize;
    }

    /**
     * 设置返回Icon背景色
     *
     * @param backViewColor ColorInt
     */
    public void setBackViewColor(@ColorInt int backViewColor) {
        this.backViewColor = backViewColor;
    }

    /**
     * 设置返回Icon的高度
     *
     * @param backViewHeight px值
     */
    public void setBackViewHeight(float backViewHeight) {
        this.backViewHeight = backViewHeight;
    }

    public float getBackViewHeight() {
        return backViewHeight;
    }
}