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
package com.xuexiang.xui.widget.popupwindow.good;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

/**
 * 点赞
 *
 * @author xuexiang
 * @since 2020-01-05 20:04
 */
public interface IGoodView {

    /**
     * 默认移动距离
     */
    int DEFAULT_DISTANCE = 60;
    /**
     * Y轴移动起始偏移量
     */
    int DEFAULT_FROM_Y_DELTA = 0;
    /**
     * Y轴移动最终偏移量
     */
    int DEFAULT_TO_Y_DELTA = DEFAULT_DISTANCE;
    /**
     * 起始时透明度
     */
    float DEFAULT_FROM_ALPHA = 1.0f;
    /**
     * 结束时透明度
     */
    float DEFAULT_TO_ALPHA = 0.0f;
    /**
     * 动画时长
     */
    int DEFAULT_DURATION = 800;
    /**
     * 默认文本
     */
    String DEFAULT_TEXT = "";
    /**
     * 默认文本字体大小
     */
    int DEFAULT_TEXT_SIZE = 16;
    /**
     * 默认文本字体颜色
     */
    int DEFAULT_TEXT_COLOR = Color.BLACK;


    /**
     * 设置文本
     *
     * @param text
     */
    IGoodView setText(String text);

    /**
     * 设置文本颜色
     *
     * @param color
     */
    IGoodView setTextColor(@ColorInt int color);

    /**
     * 设置文本大小
     *
     * @param textSize
     */
    IGoodView setTextSize(int textSize);

    /**
     * 设置文本信息
     *
     * @param text
     * @param textColor
     * @param textSize
     */
    IGoodView setTextInfo(String text, int textColor, int textSize);

    /**
     * 设置图片
     *
     * @param resId
     */
    IGoodView setImageResource(@DrawableRes int resId);

    /**
     * 设置图片
     *
     * @param drawable
     */
    IGoodView setImageDrawable(Drawable drawable);

    /**
     * 设置移动距离
     *
     * @param distance
     */
    IGoodView setDistance(int distance);

    /**
     * 设置Y轴移动属性
     *
     * @param fromY
     * @param toY
     */
    IGoodView setTranslateY(int fromY, int toY);

    /**
     * 设置透明度属性
     *
     * @param fromAlpha
     * @param toAlpha
     */
    IGoodView setAlpha(float fromAlpha, float toAlpha);

    /**
     * 设置动画时长
     *
     * @param duration
     */
    IGoodView setDuration(int duration);

    /**
     * 重置属性
     */
    void reset();

    /**
     * 展示
     *
     * @param view
     */
    void show(View view);

}
