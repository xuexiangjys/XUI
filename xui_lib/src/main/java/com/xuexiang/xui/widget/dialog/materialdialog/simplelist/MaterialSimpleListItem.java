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

package com.xuexiang.xui.widget.dialog.materialdialog.simplelist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import android.util.TypedValue;
import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ThemeUtils;

/**
 * @author Aidan Follestad (afollestad)
 */
public class MaterialSimpleListItem {

    private final Builder builder;

    private MaterialSimpleListItem(Builder builder) {
        this.builder = builder;
    }

    public Drawable getIcon() {
        return builder.icon;
    }

    public CharSequence getContent() {
        return builder.content;
    }

    public int getIconPadding() {
        return builder.iconPadding;
    }

    @ColorInt
    public int getBackgroundColor() {
        return builder.backgroundColor;
    }

    public long getId() {
        return builder.id;
    }

    @Nullable
    public Object getTag() {
        return builder.tag;
    }

    @Override
    public String toString() {
        if (getContent() != null) {
            return getContent().toString();
        } else {
            return "(no content)";
        }
    }

    public static class Builder {

        private final Context context;
        protected Drawable icon;
        protected CharSequence content;
        protected long id;

        int iconPadding;
        int backgroundColor;
        Object tag;

        public Builder(Context context) {
            this.context = context;
            backgroundColor = Color.parseColor("#00000000");
            iconPadding(ThemeUtils.resolveDimension(context, R.attr.md_simplelist_icon_padding));
        }

        public Builder icon(Drawable icon) {
            this.icon = icon;
            return this;
        }

        public Builder icon(@DrawableRes int iconRes) {
            return icon(ContextCompat.getDrawable(context, iconRes));
        }

        public Builder iconPadding(@IntRange(from = 0, to = Integer.MAX_VALUE) int padding) {
            this.iconPadding = padding;
            return this;
        }

        public Builder iconPaddingDp(@IntRange(from = 0, to = Integer.MAX_VALUE) int paddingDp) {
            this.iconPadding =
                    (int)
                            TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    paddingDp,
                                    context.getResources().getDisplayMetrics());
            return this;
        }

        public Builder iconPaddingRes(@DimenRes int paddingRes) {
            return iconPadding(context.getResources().getDimensionPixelSize(paddingRes));
        }

        public Builder content(CharSequence content) {
            this.content = content;
            return this;
        }

        public Builder content(@StringRes int contentRes) {
            return content(context.getString(contentRes));
        }

        public Builder backgroundColor(@ColorInt int color) {
            this.backgroundColor = color;
            return this;
        }

        public Builder backgroundColorRes(@ColorRes int colorRes) {
            return backgroundColor(ThemeUtils.getColor(context, colorRes));
        }

        public Builder backgroundColorAttr(@AttrRes int colorAttr) {
            return backgroundColor(ThemeUtils.resolveColor(context, colorAttr));
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder tag(@Nullable Object tag) {
            this.tag = tag;
            return this;
        }

        public MaterialSimpleListItem build() {
            return new MaterialSimpleListItem(this);
        }
    }
}
