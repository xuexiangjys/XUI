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

package com.xuexiang.xui.widget.dialog.materialdialog.internal;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;

import com.xuexiang.xui.widget.dialog.materialdialog.GravityEnum;

/**
 * Use of this is discouraged for now; for internal use only. See the Global Theming section of the
 * README.
 */
public class ThemeSingleton {

    private static ThemeSingleton singleton;
    public boolean darkTheme = false;
    @ColorInt
    public int titleColor = 0;
    @ColorInt
    public int contentColor = 0;
    public ColorStateList positiveColor = null;
    public ColorStateList neutralColor = null;
    public ColorStateList negativeColor = null;
    @ColorInt
    public int widgetColor = 0;
    @ColorInt
    public int itemColor = 0;
    public Drawable icon = null;
    @ColorInt
    public int backgroundColor = 0;
    @ColorInt
    public int dividerColor = 0;
    public ColorStateList linkColor = null;
    @DrawableRes
    public int listSelector = 0;
    @DrawableRes
    public int btnSelectorStacked = 0;
    @DrawableRes
    public int btnSelectorPositive = 0;
    @DrawableRes
    public int btnSelectorNeutral = 0;
    @DrawableRes
    public int btnSelectorNegative = 0;
    public GravityEnum titleGravity = GravityEnum.START;
    public GravityEnum contentGravity = GravityEnum.START;
    public GravityEnum btnStackedGravity = GravityEnum.END;
    public GravityEnum itemsGravity = GravityEnum.START;
    public GravityEnum buttonsGravity = GravityEnum.START;

    public static ThemeSingleton get(boolean createIfNull) {
        if (singleton == null && createIfNull) {
            singleton = new ThemeSingleton();
        }
        return singleton;
    }

    public static ThemeSingleton get() {
        return get(true);
    }
}
