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

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * 粘性标签的实现Decoration
 * 通过mRecyclerView.addItemDecoration()实现粘性标签
 *
 * @author xuexiang
 * @since 2020/5/2 4:54 PM
 */
public class StickyItemDecoration extends RecyclerView.ItemDecoration {

    private int mStickyHeadType;
    private int mFirstVisiblePosition;
    private int mStickyHeadPosition;
    private int[] mInto;

    private RecyclerView.Adapter mAdapter;

    private StickyHeadContainer mStickyHeadContainer;
    private boolean mEnableStickyHead = true;

    private OnStickyChangedListener mOnStickyChangedListener;

    /**
     * 构造方法
     *
     * @param stickyHeadContainer 粘性标签容器
     * @param stickyHeadType      粘性标签布局类型
     */
    public StickyItemDecoration(@NonNull StickyHeadContainer stickyHeadContainer, int stickyHeadType) {
        mStickyHeadContainer = stickyHeadContainer;
        mStickyHeadType = stickyHeadType;
    }

    /**
     * 设置粘顶布局滚动变化监听
     *
     * @param onStickyChangedListener 粘顶布局滚动变化监听
     */
    public StickyItemDecoration setOnStickyChangedListener(OnStickyChangedListener onStickyChangedListener) {
        mOnStickyChangedListener = onStickyChangedListener;
        return this;
    }

    /**
     * 当我们调用mRecyclerView.addItemDecoration()方法添加decoration的时候，RecyclerView在绘制的时候，去会绘制decorator，即调用该类的onDraw和onDrawOver方法，
     * 1.onDraw方法先于drawChildren
     * 2.onDrawOver在drawChildren之后，一般我们选择复写其中一个即可。
     * 3.getItemOffsets 可以通过outRect.set()为每个Item设置一定的偏移量，主要用于绘制Decorator。
     */
    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        checkCache(parent);
        if (mAdapter == null || mStickyHeadContainer == null) {
            return;
        }
        calculateStickyHeadPosition(parent);

        if (mEnableStickyHead && mFirstVisiblePosition >= mStickyHeadPosition && mStickyHeadPosition != -1) {
            View belowView = parent.findChildViewUnder(canvas.getWidth() / 2, mStickyHeadContainer.getChildHeight() + 0.01f);
            mStickyHeadContainer.onPositionChanged(mStickyHeadPosition);
            int topOffset = belowView != null ? belowView.getTop() : 0;
            int offset;
            if (isStickyHead(parent, belowView) && topOffset >= 0) {
                offset = topOffset - mStickyHeadContainer.getChildHeight();
            } else {
                offset = 0;
            }
            if (mOnStickyChangedListener == null) {
                mStickyHeadContainer.onScrolling(offset);
            } else {
                mOnStickyChangedListener.onScrolling(offset);
            }
        } else {
            if (mOnStickyChangedListener == null) {
                mStickyHeadContainer.onInVisible();
            } else {
                mOnStickyChangedListener.onInVisible();
            }
        }

    }

    /**
     * 开启/关闭粘性标签
     *
     * @param enableStickyHead 是否开启
     */
    public void enableStickyHead(boolean enableStickyHead) {
        mEnableStickyHead = enableStickyHead;
        if (!mEnableStickyHead) {
            mStickyHeadContainer.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 计算并获取粘性标签的位置
     *
     * @param parent
     */
    private void calculateStickyHeadPosition(RecyclerView parent) {
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        // 获取第一个可见的item位置
        mFirstVisiblePosition = findFirstVisiblePosition(layoutManager);

        // 获取标签的位置
        int stickyHeadPosition = findStickyHeadPosition(mFirstVisiblePosition);
        if (stickyHeadPosition >= 0 && mStickyHeadPosition != stickyHeadPosition) {
            // 标签位置有效并且和缓存的位置不同
            mStickyHeadPosition = stickyHeadPosition;
        }
    }

    /**
     * 从传入位置递减找出标签的位置
     *
     * @param startPosition 开始寻找的位置
     * @return 最近的一个标签的位置
     */
    private int findStickyHeadPosition(int startPosition) {
        for (int position = startPosition; position >= 0; position--) {
            // 位置递减，只要查到位置是标签，立即返回此位置
            final int type = mAdapter.getItemViewType(position);
            if (isStickyHeadType(type)) {
                return position;
            }
        }
        return -1;
    }

    /**
     * 通过适配器告知类型是否为标签
     *
     * @param type
     * @return
     */
    private boolean isStickyHeadType(int type) {
        return mStickyHeadType == type;
    }

    /**
     * 找出第一个可见的Item的位置
     *
     * @param layoutManager
     * @return
     */
    private int findFirstVisiblePosition(RecyclerView.LayoutManager layoutManager) {
        int firstVisiblePosition = 0;
        if (layoutManager instanceof GridLayoutManager) {
            firstVisiblePosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            firstVisiblePosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            mInto = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(mInto);
            firstVisiblePosition = Integer.MAX_VALUE;
            for (int pos : mInto) {
                firstVisiblePosition = Math.min(pos, firstVisiblePosition);
            }
        }
        return firstVisiblePosition;
    }

    /**
     * 找出第一个完全可见的Item的位置
     *
     * @param layoutManager
     * @return
     */
    private int findFirstCompletelyVisiblePosition(RecyclerView.LayoutManager layoutManager) {
        int firstVisiblePosition = 0;
        if (layoutManager instanceof GridLayoutManager) {
            firstVisiblePosition = ((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            firstVisiblePosition = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            mInto = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPositions(mInto);
            firstVisiblePosition = Integer.MAX_VALUE;
            for (int pos : mInto) {
                firstVisiblePosition = Math.min(pos, firstVisiblePosition);
            }
        }
        return firstVisiblePosition;
    }

    /**
     * 检查缓存
     *
     * @param parent
     */
    private void checkCache(final RecyclerView parent) {
        final RecyclerView.Adapter adapter = parent.getAdapter();
        if (mAdapter != adapter) {
            mAdapter = adapter;
            // 适配器为null或者不同，清空缓存
            mStickyHeadPosition = -1;
            if (mAdapter == null) {
                return;
            }
            mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    reset();
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    reset();
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                    reset();
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    reset();
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    reset();
                }

                @Override
                public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                    reset();
                }
            });

        }
    }

    private void reset() {
        mStickyHeadContainer.reset();
    }

    /**
     * 查找到view对应的位置从而判断出是否标签类型
     */
    private boolean isStickyHead(RecyclerView parent, View view) {
        final int position = parent.getChildAdapterPosition(view);
        if (position == RecyclerView.NO_POSITION) {
            return false;
        }
        final int type = mAdapter.getItemViewType(position);
        return isStickyHeadType(type);
    }

}
