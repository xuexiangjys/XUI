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

package com.xuexiang.xui.widget.layout.linkage.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.widget.Scroller;

import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

import com.xuexiang.xui.widget.layout.linkage.ChildLinkageEvent;
import com.xuexiang.xui.widget.layout.linkage.ILinkageScroll;
import com.xuexiang.xui.widget.layout.linkage.LinkageScrollHandler;
import com.xuexiang.xui.widget.layout.linkage.PosIndicator;

/**
 * 置于联动容器的WebView
 *
 * @author xuexiang
 * @since 2020/3/11 7:13 PM
 */
public class LinkageWebView extends WebView implements ILinkageScroll, NestedScrollingChild {

    private NestedScrollingChildHelper mChildHelper;
    private PosIndicator mPosIndicator;
    private final float DENSITY;
    private VelocityTracker mVelocityTracker;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private Scroller mScroller;
    private ChildLinkageEvent mLinkageEvent;

    private final int[] mScrollConsumed = new int[2];

    public LinkageWebView(Context context) {
        this(context, null);
    }

    public LinkageWebView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LinkageWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mScroller = new Scroller(getContext());
        mChildHelper = new NestedScrollingChildHelper(this);
        mPosIndicator = new PosIndicator();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mPosIndicator.setTouchSlop(viewConfiguration.getScaledTouchSlop());
        DENSITY = context.getResources().getDisplayMetrics().density;
        mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();

        setNestedScrollingEnabled(true);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (!canScrollVertically(1)
                || isScrollToBottom()) {
            if (mLinkageEvent != null) {
                mLinkageEvent.onContentScrollToBottom(this);
            }
        }
        if (!canScrollVertically(-1)) {
            if (mLinkageEvent != null) {
                mLinkageEvent.onContentScrollToTop(this);
            }
        }

        if (mLinkageEvent != null) {
            mLinkageEvent.onContentScroll(this);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            final int currY = mScroller.getCurrY();
            scrollTo(0, currY);
            invalidate();
        }
    }

    /**
     * 是否可以在垂直方向上滚动
     *
     * @param direction
     * @return
     */
    private boolean canScrollVertical(int direction) {
        int scrollY = getScrollY();
        if (direction < 0) {
            return scrollY > 0;
        } else {
            return !isScrollToBottom();
        }
    }

    /**
     * 是否已经滚动到底
     *
     * @return
     */
    private boolean isScrollToBottom() {
        int scrollY = getScrollY();
        int scrollRange = getScrollRange();
        return scrollY >= scrollRange;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPosIndicator.onDown(x, y);
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                initOrResetVelocityTracker();
                mVelocityTracker.addMovement(event);
                mScroller.abortAnimation();
                break;
            case MotionEvent.ACTION_MOVE:
                mPosIndicator.onMove(x, y);
                initVelocityTrackerIfNotExists();
                mVelocityTracker.addMovement(event);
                if (mPosIndicator.isDragging()) {
                    int dy = (int) -mPosIndicator.getOffsetY();
                    if (!dispatchNestedPreScroll(0, dy, mScrollConsumed, null)) {
                        // parent 没有消费事件
                        scrollBy(0, dy);
                    }
                    // 屏蔽webview自身滚动
                    event.setAction(MotionEvent.ACTION_CANCEL);
                }

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mPosIndicator.onRelease(x, y);
                mVelocityTracker.addMovement(event);
                if (mVelocityTracker != null) {
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int yVelocity = (int) mVelocityTracker.getYVelocity();
                    recycleVelocityTracker();
                    if ((Math.abs(yVelocity) > mMinimumVelocity)) {
                        flingWithNestedDispatch(-yVelocity);
                    }
                }
                break;
        }
        super.onTouchEvent(event);
        return true;
    }

    /**
     * 处理嵌套fling
     *
     * @param velocityY
     */
    private void flingWithNestedDispatch(int velocityY) {
        final int scrollY = getScrollY();
        final boolean canFling = (scrollY > 0 || velocityY > 0)
                && (scrollY < getScrollRange() || velocityY < 0);
        if (!dispatchNestedPreFling(0, velocityY)) {
            dispatchNestedFling(0, velocityY, canFling);
            flingScroll(0, velocityY);
        }
    }

    @Override
    public void flingScroll(int vx, int vy) {
        mScroller.fling(0, getScrollY(),
                0, vy,
                0, 0,
                Integer.MIN_VALUE, Integer.MAX_VALUE);
        invalidate();
    }

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * 获取weibview内容高度
     *
     * @return
     */
    public int getWebViewContentHeight() {
        return (int) (getContentHeight() * DENSITY);
    }

    /**
     * 获取最大的滚动距离
     *
     * @return
     */
    public int getScrollRange() {
        return getWebViewContentHeight() - getHeight();
    }

    /**
     * webview内容滚动到底
     */
    public void scrollToBottom() {
        int contentHeight = getWebViewContentHeight();
        super.scrollTo(0, contentHeight - getHeight());
    }

    /**
     * 平滑滚动到底部
     */
    public void smoothScrollToBottom() {
        mScroller.startScroll(0, getScrollY(), 0, getScrollRange() - getScrollY(), 200);
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y) {
        int scrollRange = getScrollRange();
        y = Math.max(y, 0);
        y = Math.min(y, scrollRange);
        super.scrollTo(x, y);
    }

    @Override
    public void setChildLinkageEvent(ChildLinkageEvent event) {
        mLinkageEvent = event;
    }

    @Override
    public LinkageScrollHandler provideScrollHandler() {
        return new LinkageScrollHandler() {
            @Override
            public void flingContent(View target, int velocityY) {
                LinkageWebView.this.flingScroll(0, velocityY);
            }

            @Override
            public void scrollContentToTop() {
                LinkageWebView.this.scrollTo(0, 0);
            }

            @Override
            public void scrollContentToBottom() {
//                mScroller.abortAnimation();
//                smoothScrollToBottom();
                scrollToBottom();
            }

            @Override
            public void stopContentScroll(View target) {
                LinkageWebView.this.flingScroll(0, 0);
            }

            @Override
            public boolean canScrollVertically(int direction) {
                return LinkageWebView.this.canScrollVertical(direction);
            }

            @Override
            public boolean isScrollable() {
                return true;
            }

            @Override
            public int getVerticalScrollExtent() {
                return computeVerticalScrollExtent();
            }

            @Override
            public int getVerticalScrollOffset() {
                return computeVerticalScrollOffset();
            }

            @Override
            public int getVerticalScrollRange() {
                return computeVerticalScrollRange();
            }
        };
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
}
