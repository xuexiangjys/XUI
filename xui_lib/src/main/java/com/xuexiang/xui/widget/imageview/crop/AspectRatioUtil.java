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

import android.graphics.Rect;

/**
 * 固定裁剪比例的计算工具
 */
final class AspectRatioUtil {

    private AspectRatioUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Calculates the aspect ratio given a rectangle.
     */
    static float calculateAspectRatio(float left, float top, float right, float bottom) {
        final float width = right - left;
        final float height = bottom - top;
        return width / height;
    }

    /**
     * Calculates the aspect ratio given a rectangle.
     */
    static float calculateAspectRatio(Rect rect) {
        return (float) rect.width() / (float) rect.height();
    }

    /**
     * Calculates the x-coordinate of the left edge given the other sides of the
     * rectangle and an aspect ratio.
     */
    static float calculateLeft(float top, float right, float bottom, float targetAspectRatio) {
        final float height = bottom - top;
        return right - (targetAspectRatio * height);
    }

    /**
     * Calculates the y-coordinate of the top edge given the other sides of the
     * rectangle and an aspect ratio.
     */
    static float calculateTop(float left, float right, float bottom, float targetAspectRatio) {
        final float width = right - left;
        return bottom - (width / targetAspectRatio);
    }

    /**
     * Calculates the x-coordinate of the right edge given the other sides of
     * the rectangle and an aspect ratio.
     */
    static float calculateRight(float left, float top, float bottom, float targetAspectRatio) {
        final float height = bottom - top;
        return (targetAspectRatio * height) + left;
    }

    /**
     * Calculates the y-coordinate of the bottom edge given the other sides of
     * the rectangle and an aspect ratio.
     */
    static float calculateBottom(float left, float top, float right, float targetAspectRatio) {
        final float width = right - left;
        return (width / targetAspectRatio) + top;
    }

    /**
     * Calculates the width of a rectangle given the top and bottom edges and an
     * aspect ratio.
     */
    static float calculateWidth(float top, float bottom, float targetAspectRatio) {
        final float height = bottom - top;
        return targetAspectRatio * height;
    }

    /**
     * Calculates the height of a rectangle given the left and right edges and
     * an aspect ratio.
     */
    static float calculateHeight(float left, float right, float targetAspectRatio) {
        final float width = right - left;
        return width / targetAspectRatio;
    }
}
