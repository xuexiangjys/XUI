/*
 * Copyright (C) 2023 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.expands.materialdesign.itemtouch;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xuidemo.R;

import java.util.Collections;

/**
 * 拖拽监听
 *
 * @author xuexiang
 * @since 5/23/23 12:40 AM
 */
public class DragItemCallBack extends ItemTouchHelper.Callback {
    private static final String TAG = "DragItemCallBack";

    private DragAdapter mDragAdapter;

    public DragItemCallBack(DragAdapter adapter) {
        mDragAdapter = adapter;
    }

    /**
     * 定义拖动方向
     * 1.网格布局：上下左右
     * 2.线性布局：上下/左右
     * 3.return makeMovementFlags
     */
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags;
        int swipeFlags = 0;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            // 网格布局
            dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.UP | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else if (layoutManager instanceof LinearLayoutManager) {
            // 线性布局
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            // 其他情况可自行处理
            return 0;
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        // 不同的ViewType不能拖拽换位置。
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        // 起始位置
        int fromPosition = viewHolder.getAdapterPosition();
        // 结束位置
        int toPosition = target.getAdapterPosition();

        // 固定位置
        if (fromPosition == mDragAdapter.getFixedPosition() || toPosition == mDragAdapter.getFixedPosition()) {
            return false;
        }
        // 根据滑动方向 交换数据
        if (fromPosition < toPosition) {
            // 索引变大
            for (int index = fromPosition; index < toPosition; index++) {
                Collections.swap(mDragAdapter.getData(), index, index + 1);
            }
        } else {
            for (int index = fromPosition; index > toPosition; index--) {
                Collections.swap(mDragAdapter.getData(), index, index - 1);
            }
        }
        // 刷新布局
        mDragAdapter.notifyItemMoved(fromPosition, toPosition);
        // 返回true表示处理了并可以换位置，返回false表示你没有处理并不能换位置。
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.START) {
            Log.i(TAG, "START--->向左滑");
        } else {
            Log.i(TAG, "END--->向右滑");
        }
        int position = viewHolder.getAdapterPosition();
        mDragAdapter.delete(position);
    }

    /**
     * 拖拽或滑动 发生改变时回调
     */
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder != null) {
                // 因为拿不到recyclerView，无法通过recyclerView.layoutManager来判断是什么布局，所以用item的宽度来判断
                // itemView.width > 500 用这个来判断是否是线性布局，实际取值自己看情况
                if (viewHolder.itemView.getWidth() > 500) {
                    // 线性布局 设置背景颜色
                    viewHolder.itemView.setBackgroundColor(ResUtils.getColor(viewHolder.itemView.getContext(), R.color.app_color_theme_3));
                } else {
                    // 网格布局 设置选中放大
                    ViewCompat.animate(viewHolder.itemView).setDuration(200).scaleX(1.3F).scaleY(1.3F).start();
                }
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 拖拽或滑动 结束时回调
     */
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        // 恢复显示
        // 这里不能用if判断，因为GridLayoutManager是LinearLayoutManager的子类，改用when，类型推导有区别
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            // 网格布局 设置选中大小
            ViewCompat.animate(viewHolder.itemView).setDuration(200).scaleX(1F).scaleY(1F).start();
        } else if (layoutManager instanceof LinearLayoutManager) {
            // 线性布局 设置背景颜色
            viewHolder.itemView.setBackgroundColor(ResUtils.getColor(viewHolder.itemView.getContext(), R.color.app_color_theme_6));
        }
        super.clearView(recyclerView, viewHolder);
    }

    /**
     * 是否支持侧滑，默认true
     * 1.如果需要自定义item的滑动，需要重写此方法禁掉默认
     * 2.通过onTouch或onClick等事件，自定义触发
     * 3.调用mItemTouchHelper.startSwipe方法开启
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    /**
     * 是否支持长按拖拽，默认true
     * 1.如果需要自定义item的拖拽，需要重写此方法禁掉默认
     * 2.通过onTouch或onClick等事件，自定义触发
     * 3.调用mItemTouchHelper.startDrag方法开启
     */
    @Override
    public boolean isLongPressDragEnabled() {
//        return super.isLongPressDragEnabled();
        return false;
    }
}
