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

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xuidemo.R;

/**
 * @author xuexiang
 * @since 5/23/23 12:10 AM
 */
public class DragAdapter extends BaseRecyclerAdapter<String> {

    /**
     * 固定菜单
     */
    private int mFixedPosition = 0;

    private OnItemLongClickListener itemLongClickListener;

    public DragAdapter(String[] data) {
        super(data);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.adapter_item_drag_grid;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, String item) {
        holder.text(R.id.item_title, item);
        // 第一个固定菜单
        if (position == mFixedPosition) {
            holder.getButton(R.id.item_title).setBackgroundColor(ResUtils.getColor(holder.getContext(), R.color.app_color_theme_1));
        } else {
            holder.getButton(R.id.item_title).setBackgroundColor(ResUtils.getColor(holder.getContext(), R.color.app_color_theme_6));
        }
    }

    public DragAdapter setFixedPosition(int fixedPosition) {
        mFixedPosition = fixedPosition;
        return this;
    }

    public int getFixedPosition() {
        return mFixedPosition;
    }

    @Override
    protected RecyclerViewHolder processCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerViewHolder holder = super.processCreateViewHolder(parent, viewType);
        if (itemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(v -> {
                final int position = getItemPosition(holder);
                itemLongClickListener.onItemLongClick(holder, getItem(position), position);
                return true;
            });
        }
        return holder;
    }

    public DragAdapter setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
        return this;
    }

    /**
     * 列表条目长按监听
     */
    public interface OnItemLongClickListener<T> {
        /**
         * 条目长按
         *
         * @param viewHolder 条目
         * @param item     数据
         * @param position 索引
         */
        void onItemLongClick(RecyclerView.ViewHolder viewHolder, T item, int position);
    }
}
