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

package com.xuexiang.xuidemo.widget.iconfont;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.mikepenz.iconics.IconicsColor;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsSize;
import com.xuexiang.xui.utils.ThemeUtils;

import static androidx.annotation.Dimension.DP;

/**
 * 好好玩字体库图标
 *
 * @author xuexiang
 * @since 2019-10-10 10:37
 */
public class XUIIconImageView extends AppCompatImageView {

    private String mIconText;

    public XUIIconImageView(Context context) {
        super(context);
    }

    public XUIIconImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XUIIconImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public XUIIconImageView setIconText(@NonNull String text) {
        XUIIconFont.Icon icon = XUIIconFont.Icon.get(text);
        if (icon != null) {
            mIconText = text;
            IconicsDrawable drawable = new IconicsDrawable(getContext())
                    .icon(icon)
                    .color(IconicsColor.colorInt(ThemeUtils.getMainThemeColor(getContext())));
            setImageDrawable(drawable);
        }
        return this;
    }

    public XUIIconImageView setIconText(@NonNull String text, @Dimension(unit = DP) int dpSize) {
        XUIIconFont.Icon icon = XUIIconFont.Icon.get(text);
        if (icon != null) {
            mIconText = text;
            IconicsDrawable drawable = new IconicsDrawable(getContext())
                    .icon(icon)
                    .color(IconicsColor.colorInt(ThemeUtils.getMainThemeColor(getContext())))
                    .size(IconicsSize.dp(dpSize));
            setImageDrawable(drawable);
        }
        return this;
    }

    public XUIIconImageView setIconText(@NonNull String text, @Dimension(unit = DP) int dpSize, @ColorInt int color) {
        XUIIconFont.Icon icon = XUIIconFont.Icon.get(text);
        if (icon != null) {
            mIconText = text;
            IconicsDrawable drawable = new IconicsDrawable(getContext())
                    .icon(icon)
                    .color(IconicsColor.colorInt(color))
                    .size(IconicsSize.dp(dpSize));
            setImageDrawable(drawable);
        }
        return this;
    }

    public String getIconText() {
        return mIconText;
    }
}
