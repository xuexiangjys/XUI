/*
 * Copyright (C) 2022 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.widget.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;

/**
 * @author xuexiang
 * @since 11/28/22 11:58 PM
 */
public class BottomScrollBehavior extends CoordinatorLayout.Behavior<View> {

    public BottomScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param child             需要处理的子view
     * @param directTargetChild 触发滑动的最直接子view
     * @return 返回为true的话，就视为接受并处理滑动事件
     */
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        if (type == ViewCompat.TYPE_TOUCH) {
            // 这里监听垂直方向上的滑动
            return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        }
        return false;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (type == ViewCompat.TYPE_TOUCH) {
            //如果子view欲向上滑动，必须要子view不能向下滑动后，才能交给父view滑动
            boolean showBottom = dy > 0 && parent.getScrollY() < child.getHeight() && !target.canScrollVertically(1);
            //如果子view欲向下滑动，则先交给父view滑动
            boolean hideBottom = dy < 0 && parent.getScrollY() > 0;
            if (hideBottom || showBottom) {
                int consumedy = dy;
                if (dy > 0 && parent.getScrollY() + dy > child.getHeight()) {
                    consumedy = child.getHeight() - parent.getScrollY();
                } else if (dy < 0 && parent.getScrollY() + dy < 0) {
                    consumedy = - parent.getScrollY();
                }
                parent.scrollBy(0, consumedy);
                // consumed[0] 水平消耗的距离，consumed[1] 垂直消耗的距离
                consumed[1] = consumedy;
            }
        }
    }
}
