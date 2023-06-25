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
 * 某个view监听CoordinatorLayout里的滑动状态
 * <p>
 * 1.onStartNestedScroll方法: 返回值设置需要关注什么样的滑动，是水平还是垂直方向上的滑动？
 * 2.onNestedPreScroll方法: 处理滑动
 *
 * @author xuexiang
 * @since 11/28/22 11:58 PM
 */
public class ScrollBehavior extends CoordinatorLayout.Behavior<View> {

    public ScrollBehavior(Context context, AttributeSet attrs) {
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
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (type == ViewCompat.TYPE_TOUCH) {
            // 这里简单同步了滚动的高度
            int scrolled = target.getScrollY();
            child.setScrollY(scrolled);
        }
    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        if (child instanceof NestedScrollView) {
            // 同步Fling
            ((NestedScrollView) child).fling((int) velocityY);
        }
        return true;
    }
}
