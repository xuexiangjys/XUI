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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xuexiang.xui.widget.textview.badge.BadgeView;

/**
 * 选项卡上的圆点小标记
 *
 * @author xuexiang
 * @since 2018/12/28 下午1:34
 */
public class TabBadgeView extends BadgeView {
    private TabBadgeView(Context context) {
        super(context);
    }

    public static TabBadgeView bindTab(TabView tab) {
        TabBadgeView badge = null;
        for (int i = 0; i < tab.getChildCount(); i++) {
            View child = tab.getChildAt(i);
            if (child instanceof TabBadgeView) {
                badge = (TabBadgeView) child;
                break;
            }
        }
        if (badge == null) {
            badge = new TabBadgeView(tab.getContext());
            tab.addView(badge, new TabView.LayoutParams(TabView.LayoutParams.MATCH_PARENT, TabView.LayoutParams.MATCH_PARENT));
        }
        badge.mTargetView = tab;
        return badge;
    }

    @Override
    protected void screenFromWindow(boolean screen) {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        if (screen) {
            mActivityRoot.addView(this, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT));
        } else {
            if (mTargetView instanceof TabView) {
                ((TabView) mTargetView).addView(this,
                        new TabView.LayoutParams(TabView.LayoutParams.MATCH_PARENT,
                                TabView.LayoutParams.MATCH_PARENT));
            } else {
                bindTarget(mTargetView);
            }
        }
    }
}
