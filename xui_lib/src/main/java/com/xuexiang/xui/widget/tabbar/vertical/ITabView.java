/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.xui.widget.tabbar.vertical;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;

import com.xuexiang.xui.widget.textview.badge.Badge;
import com.xuexiang.xui.widget.textview.badge.BadgeView;

/**
 * @author chqiu
 * Email:qstumn@163.com
 */

public interface ITabView {

    ITabView setBadge(TabBadge badge);

    ITabView setIcon(TabIcon icon);

    ITabView setTitle(TabTitle title);

    ITabView setBackground(int resId);

    TabBadge getBadge();

    TabIcon getIcon();

    TabTitle getTitle();

    View getTabView();

    class TabIcon {

        private Builder mBuilder;

        private TabIcon(Builder mBuilder) {
            this.mBuilder = mBuilder;
        }

        public int getSelectedIcon() {
            return mBuilder.mSelectedIcon;
        }

        public int getNormalIcon() {
            return mBuilder.mNormalIcon;
        }

        public int getIconGravity() {
            return mBuilder.mIconGravity;
        }

        public int getIconWidth() {
            return mBuilder.mIconWidth;
        }

        public int getIconHeight() {
            return mBuilder.mIconHeight;
        }

        public int getMargin() {
            return mBuilder.mMargin;
        }

        public static class Builder {
            private int mSelectedIcon;
            private int mNormalIcon;
            private int mIconGravity;
            private int mIconWidth;
            private int mIconHeight;
            private int mMargin;

            public Builder() {
                mSelectedIcon = 0;
                mNormalIcon = 0;
                mIconWidth = -1;
                mIconHeight = -1;
                mIconGravity = Gravity.START;
                mMargin = 0;
            }

            public Builder setIcon(int selectIconResId, int normalIconResId) {
                mSelectedIcon = selectIconResId;
                mNormalIcon = normalIconResId;
                return this;
            }

            public Builder setIconSize(int width, int height) {
                mIconWidth = width;
                mIconHeight = height;
                return this;
            }

            public Builder setIconGravity(int gravity) {
                if (gravity != Gravity.START && gravity != Gravity.END
                        & gravity != Gravity.TOP & gravity != Gravity.BOTTOM) {
                    throw new IllegalStateException("iconGravity only support Gravity.START " +
                            "or Gravity.END or Gravity.TOP or Gravity.BOTTOM");
                }
                mIconGravity = gravity;
                return this;
            }

            public Builder setIconMargin(int margin) {
                mMargin = margin;
                return this;
            }

            public TabIcon build() {
                return new TabIcon(this);
            }
        }
    }

    class TabTitle {
        private Builder mBuilder;

        private TabTitle(Builder mBuilder) {
            this.mBuilder = mBuilder;
        }

        public int getColorSelected() {
            return mBuilder.mColorSelected;
        }

        public int getColorNormal() {
            return mBuilder.mColorNormal;
        }

        public int getTitleTextSize() {
            return mBuilder.mTitleTextSize;
        }

        public String getContent() {
            return mBuilder.mContent;
        }

        public static class Builder {
            private int mColorSelected;
            private int mColorNormal;
            private int mTitleTextSize;
            private String mContent;

            public Builder() {
                mColorSelected = 0xFFFF4081;
                mColorNormal = 0xFF757575;
                mTitleTextSize = 16;
                mContent = "";
            }

            public Builder setTextColor(int colorSelected, int colorNormal) {
                mColorSelected = colorSelected;
                mColorNormal = colorNormal;
                return this;
            }

            public Builder setTextSize(int sizeSp) {
                mTitleTextSize = sizeSp;
                return this;
            }

            public Builder setContent(String content) {
                mContent = content;
                return this;
            }

            public TabTitle build() {
                return new TabTitle(this);
            }
        }
    }

    class TabBadge {
        private Builder mBuilder;

        private TabBadge(Builder mBuilder) {
            this.mBuilder = mBuilder;
        }

        public int getBackgroundColor() {
            return mBuilder.colorBackground;
        }

        public int getBadgeTextColor() {
            return mBuilder.colorBadgeText;
        }

        public float getBadgeTextSize() {
            return mBuilder.badgeTextSize;
        }

        public float getBadgePadding() {
            return mBuilder.badgePadding;
        }

        public int getBadgeNumber() {
            return mBuilder.badgeNumber;
        }

        public String getBadgeText() {
            return mBuilder.badgeText;
        }

        public int getBadgeGravity() {
            return mBuilder.badgeGravity;
        }

        public int getGravityOffsetX() {
            return mBuilder.gravityOffsetX;
        }

        public int getGravityOffsetY() {
            return mBuilder.gravityOffsetY;
        }

        public boolean isExactMode() {
            return mBuilder.exactMode;
        }

        public boolean isShowShadow() {
            return mBuilder.showShadow;
        }

        public Drawable getDrawableBackground() {
            return mBuilder.drawableBackground;
        }

        public int getStrokeColor() {
            return mBuilder.colorStroke;
        }

        public boolean isDrawableBackgroundClip() {
            return mBuilder.drawableBackgroundClip;
        }

        public float getStrokeWidth() {
            return mBuilder.strokeWidth;
        }

        public Badge.OnDragStateChangedListener getOnDragStateChangedListener() {
            return mBuilder.dragStateChangedListener;
        }

        public static class Builder {
            private int colorBackground;
            private int colorBadgeText;
            private int colorStroke;
            private Drawable drawableBackground;
            private boolean drawableBackgroundClip;
            private float strokeWidth;
            private float badgeTextSize;
            private float badgePadding;
            private int badgeNumber;
            private String badgeText;
            private int badgeGravity;
            private int gravityOffsetX;
            private int gravityOffsetY;
            private boolean exactMode;
            private boolean showShadow;
            private Badge.OnDragStateChangedListener dragStateChangedListener;

            public Builder() {
                colorBackground = BadgeView.DEFAULT_COLOR_BACKGROUND;
                colorBadgeText = BadgeView.DEFAULT_COLOR_BADGE_TEXT;
                colorStroke = Color.TRANSPARENT;
                drawableBackground = null;
                drawableBackgroundClip = false;
                strokeWidth = 0;
                badgeTextSize = BadgeView.DEFAULT_TEXT_SIZE;
                badgePadding = BadgeView.DEFAULT_BADGE_PADDING;
                badgeNumber = 0;
                badgeText = null;
                badgeGravity = Gravity.END | Gravity.TOP;
                gravityOffsetX = BadgeView.DEFAULT_GRAVITY_OFFSET;
                gravityOffsetY = BadgeView.DEFAULT_GRAVITY_OFFSET;
                exactMode = false;
                showShadow = true;
            }

            public TabBadge build() {
                return new TabBadge(this);
            }

            public Builder stroke(int color, int strokeWidth) {
                this.colorStroke = color;
                this.strokeWidth = strokeWidth;
                return this;
            }

            public Builder setDrawableBackground(Drawable drawableBackground, boolean clip) {
                this.drawableBackground = drawableBackground;
                this.drawableBackgroundClip = clip;
                return this;
            }

            public Builder setShowShadow(boolean showShadow) {
                this.showShadow = showShadow;
                return this;
            }

            public Builder setOnDragStateChangedListener(Badge.OnDragStateChangedListener dragStateChangedListener) {
                this.dragStateChangedListener = dragStateChangedListener;
                return this;
            }

            public Builder setExactMode(boolean exactMode) {
                this.exactMode = exactMode;
                return this;
            }

            public Builder setBackgroundColor(int colorBackground) {
                this.colorBackground = colorBackground;
                return this;
            }

            public Builder setBadgePadding(float dpValue) {
                this.badgePadding = dpValue;
                return this;
            }

            public Builder setBadgeNumber(int badgeNumber) {
                this.badgeNumber = badgeNumber;
                this.badgeText = null;
                return this;
            }

            public Builder setBadgeGravity(int badgeGravity) {
                this.badgeGravity = badgeGravity;
                return this;
            }

            public Builder setBadgeTextColor(int colorBadgeText) {
                this.colorBadgeText = colorBadgeText;
                return this;
            }

            public Builder setBadgeTextSize(float badgeTextSize) {
                this.badgeTextSize = badgeTextSize;
                return this;
            }

            public Builder setBadgeText(String badgeText) {
                this.badgeText = badgeText;
                this.badgeNumber = 0;
                return this;
            }

            public Builder setGravityOffset(int offsetX, int offsetY) {
                this.gravityOffsetX = offsetX;
                this.gravityOffsetY = offsetY;
                return this;
            }
        }
    }
}
