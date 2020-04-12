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

package com.xuexiang.xuidemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;

import com.xuexiang.xuidemo.R;

/**
 * 复杂嵌套滚动的演示案例
 *
 * @author xuexiang
 * @since 2020/4/13 12:10 AM
 */
public class ComplexNestedScrollingLayout extends LinearLayout implements NestedScrollingParent2 {

    private NestedScrollingParentHelper mNestedScrollingParentHelper;

    private View mNavigationView;

    /**
     * 父控件可以滚动的距离
     */
    private float mCanScrollDistance = 0F;

    public ComplexNestedScrollingLayout(Context context) {
        this(context, null);
    }

    public ComplexNestedScrollingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ComplexNestedScrollingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    }


    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        //如果子view欲向上滑动，则先交给父view滑动
        boolean hideTop = dy > 0 && getScrollY() < mCanScrollDistance;
        //如果子view欲向下滑动，必须要子view不能向下滑动后，才能交给父view滑动
        boolean showTop = dy < 0 && getScrollY() >= 0 && !target.canScrollVertically(-1);
        if (hideTop || showTop) {
            scrollBy(0, dy);
            // consumed[0] 水平消耗的距离，consumed[1] 垂直消耗的距离
            consumed[1] = dy;
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        mNestedScrollingParentHelper.onStopNestedScroll(target, type);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if (dyUnconsumed < 0) {
            //表示已经向下滑动到头，这里不用区分手势还是fling
            scrollBy(0, dyUnconsumed);
        }
    }

    /**
     * 嵌套滑动时，如果父View处理了fling,那子view就没有办法处理fling了，所以这里要返回为false
     */
    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mNavigationView = findViewById(R.id.ll_navigation_view);
    }

    /**
     * 设置顶部控件的高度
     *
     * @param topViewHeight 顶部控件的高度
     * @param hasNavigation 是否包含导航栏
     */
    public ComplexNestedScrollingLayout setTopViewHeight(float topViewHeight, boolean hasNavigation) {
        mCanScrollDistance = topViewHeight + (hasNavigation ? 0 : mNavigationView.getHeight());
        return this;
    }

    /**
     * 设置父控件可以滚动的距离
     *
     * @param canScrollDistance 父控件可以滚动的距离
     */
    public ComplexNestedScrollingLayout setCanScrollDistance(float canScrollDistance) {
        mCanScrollDistance = canScrollDistance;
        return this;
    }

    public float getCanScrollDistance() {
        return mCanScrollDistance;
    }

    /**
     * @return 获取容器高度
     */
    public int getContainerHeight(boolean hasNavigation) {
        return getMeasuredHeight() - (hasNavigation ? mNavigationView.getHeight() : 0);
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mCanScrollDistance) {
            y = (int) mCanScrollDistance;
        }
        if (getScrollY() != y) {
            super.scrollTo(x, y);
        }
    }


}
