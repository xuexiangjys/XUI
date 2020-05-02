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

package com.xuexiang.xui.adapter.recyclerview.sticky;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.ViewCompat;

/**
 * 粘顶布局滑动所占的容器
 *
 * @author xuexiang
 * @since 2020/5/2 11:24 AM
 */
public class StickyHeadContainer extends ViewGroup {

    private int mOffset;
    private int mLastOffset = Integer.MIN_VALUE;
    private int mLastStickyHeadPosition = Integer.MIN_VALUE;

    private OnStickyPositionChangedListener mOnStickyPositionChangedListener;

    public StickyHeadContainer(Context context) {
        this(context, null);
    }

    public StickyHeadContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickyHeadContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 屏蔽点击事件
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desireHeight;
        int desireWidth;

        int count = getChildCount();

        if (count != 1) {
            throw new IllegalArgumentException("只允许容器添加1个子View！");
        }

        final View child = getChildAt(0);
        // 测量子元素并考虑外边距
        measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
        // 获取子元素的布局参数
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        // 计算子元素宽度，取子控件最大宽度
        desireWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
        // 计算子元素高度
        desireHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

        // 考虑父容器内边距
        desireWidth += getPaddingLeft() + getPaddingRight();
        desireHeight += getPaddingTop() + getPaddingBottom();
        // 尝试比较建议最小值和期望值的大小并取大值
        desireWidth = Math.max(desireWidth, getSuggestedMinimumWidth());
        desireHeight = Math.max(desireHeight, getSuggestedMinimumHeight());
        // 设置最终测量值
        setMeasuredDimension(resolveSize(desireWidth, widthMeasureSpec), resolveSize(desireHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final View child = getChildAt(0);
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();

        int left = paddingLeft + lp.leftMargin;
        int right = child.getMeasuredWidth() + left;

        int top = paddingTop + lp.topMargin + mOffset;
        int bottom = child.getMeasuredHeight() + top;

        child.layout(left, top, right, bottom);
    }

    // 生成默认的布局参数
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    // 生成布局参数,将布局参数包装成我们的
    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    // 生成布局参数,从属性配置中生成我们的布局参数
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    // 查当前布局参数是否是我们定义的类型这在code声明布局参数时常常用到
    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }

    /**
     * 滚动中
     *
     * @param offset
     */
    public void onScrolling(int offset) {
        scrollChild(offset);
        setVisibility(View.VISIBLE);
    }

    /**
     * 不可见
     */
    public void onInVisible() {
        reset();
        setVisibility(View.INVISIBLE);
    }

    /**
     * 滚动
     *
     * @param offset
     */
    public void scrollChild(int offset) {
        if (mLastOffset != offset) {
            mOffset = offset;
            ViewCompat.offsetTopAndBottom(getChildAt(0), mOffset - mLastOffset);
        }
        mLastOffset = mOffset;
    }

    protected int getChildHeight() {
        return getChildAt(0).getMeasuredHeight();
    }

    protected void onPositionChanged(int stickyHeadPosition) {
        if (mOnStickyPositionChangedListener != null && mLastStickyHeadPosition != stickyHeadPosition) {
            mOnStickyPositionChangedListener.onPositionChanged(stickyHeadPosition);
        }
        mLastStickyHeadPosition = stickyHeadPosition;
    }

    public void reset() {
        mLastStickyHeadPosition = Integer.MIN_VALUE;
    }

    /**
     * 资源回收
     */
    public void recycle() {
        mLastStickyHeadPosition = Integer.MIN_VALUE;
        mOnStickyPositionChangedListener = null;
    }

    public int getLastStickyHeadPosition() {
        return mLastStickyHeadPosition;
    }

    /**
     * 粘性标签的索引发生变化的监听
     */
    public interface OnStickyPositionChangedListener {
        /**
         * 索引发生变化
         *
         * @param position 索引
         */
        void onPositionChanged(int position);

    }

    /**
     * 设置粘性标签的索引发生变化的监听
     *
     * @param onStickyPositionChangedListener 粘性标签的索引发生变化的监听
     */
    public StickyHeadContainer setOnStickyPositionChangedListener(OnStickyPositionChangedListener onStickyPositionChangedListener) {
        mOnStickyPositionChangedListener = onStickyPositionChangedListener;
        return this;
    }
}
