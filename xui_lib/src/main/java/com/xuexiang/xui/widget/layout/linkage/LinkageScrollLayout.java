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

package com.xuexiang.xui.widget.layout.linkage;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Scroller;

import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;

import com.xuexiang.xui.logs.UILog;

import java.util.HashMap;
import java.util.Map;

/**
 * 多个子view在垂直方向联动滚动的容器。
 *
 * <p>
 * 联动容器不限制子view的数量和类型，由于使用了google的嵌套滚动机制，如果置于联动容器中的
 * 子view是scrollable(eg. WebView, RecyclerView...)，子view必须实现NestedScrollingChild。
 *
 * <p>
 * 所有置于联动容器的子view必须实现{@link ILinkageScroll}接口，否则联动容器会抛异常。
 *
 * <p>
 * 子view通过{@link LinkageScrollHandler}中的isScrollable()方法告知联动容器，是否可滚动。
 * 联动容器也会根据子view的高度判断，如果子view的高度小于容器高度，那联动容器会认为子view不可滚动。
 * 子view是否可滚动的判断：((子view的高度 == 容器高度) && isScrollable())
 *
 * <p>
 */
public class LinkageScrollLayout extends ViewGroup implements NestedScrollingParent {

    public static final String TAG = "ELinkageScrollLayout";
    /**
     * Fling方向：以手指滑动方向为准
     */
    public static final int FLING_ORIENTATION_UP = 0x11;
    /**
     * Fling方向：以手指滑动方向为准
     */
    public static final int FLING_ORIENTATION_DOWN = 0x12;
    /**
     * Fling方向：以手指滑动方向为准
     */
    public static final int FLING_ORIENTATION_NONE = 0x00;
    /**
     * 滚动动画时长
     */
    public static final int LOC_SCROLL_DURATION = 280;

    /**
     * NestedScrollingParentHelper
     */
    private NestedScrollingParentHelper mParentHelper;
    /**
     * 所有子view的边界位置
     */
    private HashMap<View, ViewEdge> mEdgeList = new HashMap<>();
    /**
     * 联动容器可滚动的范围
     */
    private int mScrollRange;
    /**
     * 跟踪速度的scroller，仅用于计算速度
     */
    private Scroller mVelocityScroller;
    /**
     * 联动容器滚动的Scroller
     */
    private Scroller mScroller;
    /**
     * 联动容器滚动定位子view
     */
    private Scroller mLocScroller;
    /**
     * VelocityTracker
     */
    private VelocityTracker mVelocityTracker;
    /**
     * 手势辅助工具类
     */
    private PosIndicator mPosIndicator;
    /**
     * MaximumVelocity
     */
    private int mMaximumVelocity;
    /**
     * MinimumVelocity
     */
    private int mMinimumVelocity;
    /**
     * 纪录fling方向
     */
    private int mFlingOrientation = FLING_ORIENTATION_NONE;
    /**
     * 是否拦截事件
     */
    private boolean mIsIntercept;
    /**
     * 纪录上一次的坐标
     */
    private int mLastY;

    /**
     * child view的滚动事件
     */
    private ChildLinkageEvent mChildLinkageEvent = new ChildLinkageEvent() {
        @Override
        public void onContentScrollToTop(View target) {
            UILog.dTag(TAG, "#onContentScrollToTop#");
            // 如果收到子view的ContentScrollToTop事件，mFlingOrientation必须是FLING_ORIENTATION_DOWN
            if (mFlingOrientation != FLING_ORIENTATION_DOWN) {
                UILog.dTag(TAG, "onContentScrollToTop, Invalid Fling Orientation");
                return;
            }
            // 安全性判断
            if (isFirstTarget(target)) {
                return;
            }
            if (!getLinkageScrollHandler(target).isScrollable()) {
                return;
            }
            ViewEdge targetEdge = getTargetEdge(target);
            if (targetEdge == null) {
                return;
            }
            if (getScrollY() != targetEdge.topEdge) {
                return;
            }
            if (mVelocityScroller.computeScrollOffset()) {
                float currVelocity = mVelocityScroller.getCurrVelocity();
                currVelocity = currVelocity < 0 ? currVelocity : -currVelocity;
                UILog.dTag(TAG, "onContentScrollToTop, currVelocity: " + currVelocity);
                mVelocityScroller.abortAnimation();
                parentFling(currVelocity);
            }
        }

        @Override
        public void onContentScrollToBottom(View target) {
            UILog.dTag(TAG, "#onContentScrollToBottom#");
            // 如果收到子view的ContentScrollToBottom事件，mFlingOrientation必须是FLING_ORIENTATION_UP
            if (mFlingOrientation != FLING_ORIENTATION_UP) {
                UILog.dTag(TAG, "onContentScrollToBottom, Invalid Fling Orientation");
                return;
            }
            if (isLastTarget(target)) {
                return;
            }
            if (!getLinkageScrollHandler(target).isScrollable()) {
                return;
            }
            ViewEdge targetEdge = getTargetEdge(target);
            if (targetEdge == null) {
                return;
            }
            if (getScrollY() != targetEdge.topEdge) {
                return;
            }
            if (mVelocityScroller.computeScrollOffset()) {
                float currVelocity = mVelocityScroller.getCurrVelocity();
                currVelocity = currVelocity > 0 ? currVelocity : -currVelocity;
                UILog.dTag(TAG, "onContentScrollToBottom, currVelocity: " + currVelocity);
                mVelocityScroller.abortAnimation();
                parentFling(currVelocity);
            }
        }

        @Override
        public void onContentScroll(View target) {
            // 收到子view滚动事件，显示滚动条
            awakenScrollBars();
        }
    };

    public LinkageScrollLayout(Context context) {
        this(context, null);
    }

    public LinkageScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LinkageScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mParentHelper = new NestedScrollingParentHelper(this);
        mScroller = new Scroller(getContext());
        mLocScroller = new Scroller(getContext());
        mVelocityScroller = new Scroller(getContext());
        mPosIndicator = new PosIndicator();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        mPosIndicator.setTouchSlop(viewConfiguration.getScaledTouchSlop());

        // 确保联动容器调用onDraw()方法
        setWillNotDraw(false);
        // enable vertical scrollbar
        setVerticalScrollBarEnabled(true);
    }

    /**
     * 初始化VelocityTracker
     */
    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    /**
     * 初始化VelocityTracker
     */
    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    /**
     * 回收VelocityTracker
     */
    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * motion event pass to children
     *
     * @param event
     * @return
     */
    private boolean dispatchTouchEventSupper(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        float x = ev.getX();
        float y = ev.getY();

        switch (ev.getAction()
                & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mPosIndicator.onDown(x, y);

                resetScroll();
                initOrResetVelocityTracker();
                mVelocityTracker.addMovement(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                mPosIndicator.onMove(x, y);

                initVelocityTrackerIfNotExists();
                mVelocityTracker.addMovement(ev);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mPosIndicator.onRelease(x, y);
                break;
        }

        return dispatchTouchEventSupper(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE:
                // 拦截落在不可滑动子View的MOVE事件
                View target = getTouchTarget((int) ev.getRawX(), (int) ev.getRawY());
                if (mPosIndicator.isDragging()
                        && target != null
                        && !getLinkageScrollHandler(target).isScrollable()) {
                    mIsIntercept = true;
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_DOWN:
                mIsIntercept = false;
                // 校验一遍所有子view，内容是否滚动正确
                checkTargetsScroll();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsIntercept = false;
                break;
        }

        return mIsIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mLastY == 0) {
                    mLastY = (int) event.getY();
                    return true;
                }
                int y = (int) event.getY();
                int dy = y - mLastY;
                mLastY = y;
                scrollBy(0, -dy);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mLastY = 0;

                if (mVelocityTracker != null) {
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int yVelocity = (int) mVelocityTracker.getYVelocity();
                    recycleVelocityTracker();
                    parentFling(-yVelocity);
                    UILog.dTag(TAG, "#dispatchTouchEvent# ACTION_UP, yVelocity: " + yVelocity);
                }
                break;
        }
        return true;
    }

    /**
     * 重置滚动变量
     */
    private void resetScroll() {
        mFlingOrientation = FLING_ORIENTATION_NONE;
        mVelocityScroller.abortAnimation();
        mScroller.abortAnimation();
        mLocScroller.abortAnimation();
    }

    /**
     * 获取手指触摸的target
     *
     * @param rawX
     * @param rawY
     * @return
     */
    private View getTouchTarget(float rawX, float rawY) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View target = getChildAt(i);
            int[] location = new int[2];
            target.getLocationOnScreen(location);
            int left = location[0];
            int top = location[1];
            int right = left + target.getWidth();
            int bottom = top + target.getHeight();
            RectF rect = new RectF(left, top, right, bottom);
            if (rect.contains(rawX, rawY)) {
                return target;
            }
        }
        return null;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int curY = mScroller.getCurrY();
            scrollTo(0, curY, true);
            invalidate();
        }
        if (mLocScroller.computeScrollOffset()) {
            int curY = mLocScroller.getCurrY();
            scrollTo(0, curY, false);
            invalidate();

            if (mLocScroller.isFinished()) {
                // 滚动结束，校验子view内容的滚动位置
                UILog.dTag(TAG, "#computeScroll#, LocScroll finished");
                checkTargetsScroll();
            }
        }
    }

    /**
     * scroll parent中的子view
     *
     * @param x
     * @param y
     * @param isInertial 是否是惯性滚动
     */
    public void scrollTo(int x, int y, boolean isInertial) {
        // 显示滚动条
        awakenScrollBars();
        // 区分惯性滚动和非惯性滚动
        // 1. 上边界和下边界检查时一致
        // 2. 惯性滚动增加中间态的edge检查
        if (isInertial) {
            y = Math.max(y, 0);
            y = Math.min(y, mScrollRange);

            // 获取下一个滚动边界
            int edge = getNextEdge();
            if (mFlingOrientation == FLING_ORIENTATION_UP) {
                y = Math.min(y, edge);
            }
            if (mFlingOrientation == FLING_ORIENTATION_DOWN) {
                y = Math.max(y, edge);
            }
            // 执行滚动
            scrollTo(x, y);

            // 如果fling到边界，子view继续fling
            int scrollY = getScrollY();
            if (scrollY == edge) {
                int velocity = (int) mScroller.getCurrVelocity();
                if (mFlingOrientation == FLING_ORIENTATION_UP) {
                    velocity = velocity > 0 ? velocity : -velocity;
                }
                if (mFlingOrientation == FLING_ORIENTATION_DOWN) {
                    velocity = velocity < 0 ? velocity : -velocity;
                }
                UILog.dTag(TAG, "#scrollTo# To Edge: " + edge + ", velocity: " + velocity);
                mScroller.abortAnimation();
                View target = getTargetByEdge(edge);
                getLinkageScrollHandler(target)
                        .flingContent(target, velocity);
                trackVelocity(velocity);
            }
        } else {
            y = Math.max(y, 0);
            y = Math.min(y, mScrollRange);
            scrollTo(x, y);
        }

    }

    @Override
    public void scrollBy(int x, int y) {
        // 显示滚动条
        awakenScrollBars();
        // 上下边界检查
        int scrollY = getScrollY();
        int deltaY;
        if (y < 0) {
            deltaY = (scrollY + y) < 0 ? (-scrollY) : y;
        } else {
            deltaY = (scrollY + y) > mScrollRange ?
                    (mScrollRange - scrollY) : y;
        }
        if (deltaY != 0) {
            super.scrollBy(x, deltaY);
        }
    }

    /**
     * 根据边界值获取对应的target
     *
     * @param edge
     * @return
     */
    private View getTargetByEdge(int edge) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View target = getChildAt(i);
            ViewEdge viewEdge = mEdgeList.get(target);
            if (viewEdge.topEdge == edge) {
                return target;
            }
        }
        return null;
    }

    /**
     * 根据fling的方向获取下一个滚动边界，
     * 内部会判断下一个子View是否isScrollable，
     * 如果为false，会顺延取下一个target的edge。
     *
     * @return
     */
    private int getNextEdge() {
        int scrollY = getScrollY();
        if (mFlingOrientation == FLING_ORIENTATION_UP) {
            // 从上往下遍历
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View target = getChildAt(i);
                LinkageScrollHandler targetLinkageHandler
                        = getLinkageScrollHandler(target);
                int topEdge = mEdgeList.get(target).topEdge;
                if (topEdge <= scrollY
                        || !isTargetScrollable(target)
                        || !targetLinkageHandler.canScrollVertically(1)) {
                    continue;
                }
                return topEdge;
            }
        } else if (mFlingOrientation == FLING_ORIENTATION_DOWN) {
            // 从下往上遍历
            for (int i = getChildCount() - 1; i >= 0; i--) {
                View target = getChildAt(i);
                LinkageScrollHandler targetLinkageHandler
                        = getLinkageScrollHandler(target);
                int topEdge = mEdgeList.get(target).topEdge;
                if (topEdge >= scrollY
                        || !isTargetScrollable(target)
                        || !targetLinkageHandler.canScrollVertically(-1)) {
                    continue;
                }
                return topEdge;
            }
        } else {
            throw new RuntimeException("#getNextEdge# unknown Fling Orientation");
        }
        return mFlingOrientation == FLING_ORIENTATION_UP ? mScrollRange : 0;
    }

    /**
     * parent fling
     * <p>
     * 1. 当parent需要直接消费fling，会调用parentFling
     * 2. 当子view惯性滚动到顶或者底，会调用parentFling
     *
     * @param velocityY
     */
    private void parentFling(float velocityY) {
        if (Math.abs(velocityY) > mMinimumVelocity) {
            mFlingOrientation = velocityY > 0 ? FLING_ORIENTATION_UP : FLING_ORIENTATION_DOWN;
            mScroller.fling(0, getScrollY(),
                    1, (int) velocityY,
                    0, 0,
                    Integer.MIN_VALUE, Integer.MAX_VALUE);
            invalidate();
        }
    }

    /**
     * 判断target是否是容器中第一个view
     *
     * @param target
     * @return
     */
    private boolean isFirstTarget(View target) {
        View first = getChildAt(0);
        return target == first;
    }

    /**
     * 判断target是否是容器中最后一个view
     *
     * @param target
     * @return
     */
    private boolean isLastTarget(View target) {
        View last = getChildAt(getChildCount() - 1);
        return target == last;
    }

    /**
     * 校验子view内容滚动位置是否正确
     */
    private void checkTargetsScroll() {
        int scrollY = getScrollY();
        View target = null;
        for (Map.Entry<View, ViewEdge> entry : mEdgeList.entrySet()) {
            ViewEdge value = entry.getValue();
            if (scrollY >= value.topEdge && scrollY < value.bottomEdge) {
                target = entry.getKey();
                break;
            }
        }
        if (target == null) {
            return;
        }
        int index = indexOfChild(target);
        UILog.dTag(TAG, "#checkTargetsScroll# index: " + index);
        for (int i = 0; i < index; i++) {
            final View child = getChildAt(i);
            scrollTargetContentToBottom(child);
        }
        for (int i = index + 1; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            scrollTargetContentToTop(child);
        }
    }

    /**
     * 跟踪加速度
     *
     * @param velocityY
     */
    private void trackVelocity(float velocityY) {
        mVelocityScroller.fling(0, 0,
                0, (int) velocityY,
                0, 0,
                Integer.MIN_VALUE, Integer.MAX_VALUE);
        invalidate();
    }

    /**
     * 获取target的LinkageScrollHandler接口
     *
     * @param target
     * @return
     */
    private LinkageScrollHandler getLinkageScrollHandler(View target) {
        return ((ILinkageScroll) target).provideScrollHandler();
    }

    /**
     * 判断target是否可滚动
     *
     * @param target
     * @return
     */
    private boolean isTargetScrollable(View target) {
        LinkageScrollHandler linkageScrollHandler
                = getLinkageScrollHandler(target);
        boolean isScrollable = linkageScrollHandler.isScrollable();
        return isScrollable && (target.getHeight() >= getHeight());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            child.setVerticalScrollBarEnabled(false);
            child.setHorizontalScrollBarEnabled(false);
            if (!(child instanceof ILinkageScroll)) {
                throw new RuntimeException("Child in LinkageScrollLayout must implement ILinkageScroll");
            }
            ((ILinkageScroll) child).setChildLinkageEvent(mChildLinkageEvent);
            child.setOverScrollMode(OVER_SCROLL_NEVER);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size = getChildCount();
        for (int i = 0; i < size; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mScrollRange = 0;
        int childTop = t;
        int size = getChildCount();
        for (int i = 0; i < size; i++) {
            View child = getChildAt(i);
            int bottom = childTop + child.getMeasuredHeight();
            child.layout(l, childTop, r, bottom);
            childTop = bottom;
            // 联动容器可滚动最大距离
            mScrollRange += child.getHeight();
            mEdgeList.put(child, new ViewEdge(child.getTop(), child.getBottom()));
        }
        // 联动容器可滚动range
        mScrollRange -= getMeasuredHeight();
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        UILog.dTag(TAG, "#onStartNestedScroll# nestedScrollAxes: " + nestedScrollAxes);
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    /**
     * 当onStartNestedScroll返回true，该方法会被调用。
     * <p>
     * 可以在这个方法中执行一些初始化操作
     *
     * @param child
     * @param target
     * @param axes
     */
    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        UILog.dTag(TAG, "#onNestedScrollAccepted# axes: " + axes);
        mParentHelper.onNestedScrollAccepted(child, target, axes);

        // 初始化
        resetScroll();
    }

    @Override
    public void onStopNestedScroll(View child) {
        UILog.dTag(TAG, "#onStopNestedScroll# child: " + child);
        mParentHelper.onStopNestedScroll(child);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
//        Log.d(TAG, "#onNestedScroll# dyConsumed: " + dyConsumed + ", dyUnconsumed: " + dyUnconsumed);
        // target消费一部分，parent消费剩下的
        if (dyConsumed != 0 && dyUnconsumed != 0) {
            scrollBy(0, dyUnconsumed);
        }
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        UILog.dTag(TAG, "#onNestedPreScroll# dy: " + dy);

        // 1. parent处理scroll
        // 1.1 parent全部消费
        // 1.2 parent消费一部分, target消费剩余的
        // 2. target处理scroll
        // 2.1 target消费所有scroll
        // 2.2 target消费一部分，parent消费剩余的

        boolean moveUp = dy > 0;
        boolean moveDown = !moveUp;

        int scrollY = getScrollY();
        ViewEdge targetEdge = getTargetEdge(target);
        LinkageScrollHandler targetScrollHandler
                = ((ILinkageScroll) target).provideScrollHandler();

        if (scrollY == targetEdge.topEdge) {
            if ((moveDown && !targetScrollHandler.canScrollVertically(-1))
                    || (moveUp && !targetScrollHandler.canScrollVertically(1))) {
                scrollBy(0, dy);
                consumed[1] = dy;
            } else {
                // target处理scroll
                UILog.dTag(TAG, "#onNestedPreScroll#, handle scroll by " + target);
            }
        } else if (scrollY > targetEdge.topEdge) {
            if (moveUp) {
                scrollBy(0, dy);
                consumed[1] = dy;
            }
            if (moveDown) {
                int end = scrollY + dy;
                int deltaY;
                deltaY = end > targetEdge.topEdge ? dy : (targetEdge.topEdge - scrollY);
                scrollBy(0, deltaY);
                consumed[1] = deltaY;
            }
        } else {
            if (moveDown) {
                scrollBy(0, dy);
                consumed[1] = dy;
            }
            if (moveUp) {
                int end = scrollY + dy;
                int deltaY;
                deltaY = end < targetEdge.topEdge ? dy : (targetEdge.topEdge - scrollY);
                scrollBy(0, deltaY);
                consumed[1] = deltaY;
            }
        }
    }

    /**
     * 直接跳转到某个子view
     *
     * @param index
     */
    public void gotoChild(int index) {
        // 1. scroll到指定子view
        smoothScrollToIndex(index);
        // 2. child[0] - child[index-1]的子view内容都必须滚动到底部
        for (int i = 0; i < index; i++) {
            final View target = getChildAt(i);
            scrollTargetContentToBottom(target);
        }
        // 3. child[index] - child[childCount - 1]的子view内容都滚动到顶部
        for (int i = index; i < getChildCount(); i++) {
            final View target = getChildAt(i);
            scrollTargetContentToTop(target);
        }
    }

    /**
     * 滚动到某个子view
     *
     * @param index
     */
    private void scrollToIndex(int index) {
        index = Math.max(index, 0);
        index = Math.min(index, getChildCount() - 1);
        View target = getChildAt(index);
        ViewEdge viewEdge = mEdgeList.get(target);
        // 滚动到某个子view
        scrollTo(0, viewEdge.topEdge, false);
    }

    /**
     * 平滑滚动到某个子view
     *
     * @param index
     */
    private void smoothScrollToIndex(int index) {
        index = Math.max(index, 0);
        index = Math.min(index, getChildCount() - 1);
        View target = getChildAt(index);
        ViewEdge viewEdge = mEdgeList.get(target);
        if (viewEdge != null) {
            mLocScroller.startScroll(0, getScrollY(), 0, viewEdge.topEdge - getScrollY(), LOC_SCROLL_DURATION);
            invalidate();
        }

    }

    /**
     * 滚动指定子view的内容到顶部
     *
     * @param target
     */
    private void scrollTargetContentToTop(View target) {
        LinkageScrollHandler targetScrollHandler
                = ((ILinkageScroll) target).provideScrollHandler();
        if (targetScrollHandler.isScrollable()
                && targetScrollHandler.canScrollVertically(-1)) {
            targetScrollHandler.scrollContentToTop();
        }
    }

    /**
     * 滚动指定子view的内容到底部
     *
     * @param target
     */
    private void scrollTargetContentToBottom(View target) {
        LinkageScrollHandler targetScrollHandler
                = ((ILinkageScroll) target).provideScrollHandler();
        if (targetScrollHandler.isScrollable()
                && targetScrollHandler.canScrollVertically(1)) {
            targetScrollHandler.scrollContentToBottom();
        }
    }

    /**
     * 返回targetview的edge
     *
     * @return
     */
    private ViewEdge getTargetEdge(View target) {
        ViewEdge viewEdge = mEdgeList.get(target);
        return viewEdge;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        UILog.dTag(TAG, "#onNestedPreFling# velocityY: " + velocityY);
        int scrollY = getScrollY();
        ViewEdge targetEdge = getTargetEdge(target);
        mFlingOrientation = velocityY > 0 ? FLING_ORIENTATION_UP : FLING_ORIENTATION_DOWN;
        if (scrollY == targetEdge.topEdge) {
            // 跟踪velocity，当target滚动到顶或底，保证parent继续fling
            trackVelocity(velocityY);
            return false;
        } else {
            parentFling(velocityY);
            return true;
        }
    }

    @Override
    public int getNestedScrollAxes() {
        return mParentHelper.getNestedScrollAxes();
    }

    @Override
    protected int computeVerticalScrollExtent() {
        return super.computeVerticalScrollExtent();
    }

    @Override
    protected int computeVerticalScrollRange() {
        int range = 0;

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            ILinkageScroll linkageScroll = (ILinkageScroll) child;
            int childRange = linkageScroll.provideScrollHandler().getVerticalScrollRange();
            range += childRange;
        }

        return range;
    }

    @Override
    protected int computeVerticalScrollOffset() {
        int offset = 0;

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            ILinkageScroll linkageScroll = (ILinkageScroll) child;
            int childOffset = linkageScroll.provideScrollHandler().getVerticalScrollOffset();
            offset += childOffset;
        }
        offset += getScrollY();
        return offset;
    }

    /**
     * view的上边沿和下边沿
     */
    private class ViewEdge {

        public int topEdge;
        public int bottomEdge;

        public ViewEdge(int topEdge, int bottomEdge) {
            this.topEdge = topEdge;
            this.bottomEdge = bottomEdge;
        }
    }
}
