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

package com.xuexiang.xui.widget.imageview.crop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.TypedValue;
import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ThemeUtils;

/**
 * 裁剪边框画笔的构建工具类.
 */
final class PaintUtil {

    private PaintUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    // Private Constants ///////////////////////////////////////////////////////
    private static final String DEFAULT_LINE_COLOR_ID = "#AAFFFFFF";
    private static final String DEFAULT_BACKGROUND_COLOR_ID = "#B0000000";
    private static final float DEFAULT_LINE_THICKNESS_DP = 3;
    private static final float DEFAULT_CORNER_THICKNESS_DP = 5;
    private static final float DEFAULT_GUIDELINE_THICKNESS_PX = 1;

    // Public Methods //////////////////////////////////////////////////////////

    /**
     * Creates the Paint object for drawing the crop window border.
     *
     * @param context the Context
     * @return new Paint object
     */
    static Paint newBorderPaint(Context context) {
        // Set the line thickness for the crop window border.
        final float lineThicknessPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LINE_THICKNESS_DP, context
                        .getResources().getDisplayMetrics());
        final Paint borderPaint = new Paint();
        borderPaint.setColor(Color.parseColor(DEFAULT_LINE_COLOR_ID));
        borderPaint.setStrokeWidth(lineThicknessPx);
        borderPaint.setStyle(Paint.Style.STROKE);

        return borderPaint;
    }

    /**
     * Creates the Paint object for drawing the crop window guidelines.
     *
     * @return the new Paint object
     */
    static Paint newGuidelinePaint(Context context) {
        final Paint paint = new Paint();
        paint.setColor(Color.parseColor(DEFAULT_LINE_COLOR_ID));
        paint.setStrokeWidth(DEFAULT_GUIDELINE_THICKNESS_PX);
        return paint;
    }

    /**
     * Creates the Paint object for drawing the translucent overlay outside the
     * crop window.
     *
     * @param context the Context
     * @return the new Paint object
     */
    static Paint newBackgroundPaint(Context context) {
        final Paint paint = new Paint();
        paint.setColor(Color.parseColor(DEFAULT_BACKGROUND_COLOR_ID));
        return paint;
    }

    /**
     * Creates the Paint object for drawing the corners of the border 剪切框四角
     *
     * @param context the Context
     * @return the new Paint object
     */
    static Paint newCornerPaint(Context context) {

        // Set the line thickness for the crop window border.
        final float lineThicknessPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CORNER_THICKNESS_DP,
                context.getResources().getDisplayMetrics());

        final Paint cornerPaint = new Paint();
        cornerPaint.setColor(ThemeUtils.getMainThemeColor(context));
        cornerPaint.setStrokeWidth(lineThicknessPx);
        cornerPaint.setStyle(Paint.Style.STROKE);

        return cornerPaint;
    }

    /**
     * Returns the value of the corner thickness
     *
     * @return Float equivalent to the corner thickness
     */
    static float getCornerThickness() {
        return DEFAULT_CORNER_THICKNESS_DP;
    }

    /**
     * Returns the value of the line thickness of the border
     *
     * @return Float equivalent to the line thickness
     */
    static float getLineThickness() {
        return DEFAULT_LINE_THICKNESS_DP;
    }

}
