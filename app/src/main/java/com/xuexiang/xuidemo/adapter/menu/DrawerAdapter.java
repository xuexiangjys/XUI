/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.adapter.menu;

import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 侧滑栏的列表适配器
 *
 * @author xuexiang
 * @since 2019/1/6 上午12:28
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    private List<DrawerItem> items;
    private Map<Class<? extends DrawerItem>, Integer> viewTypes;
    private SparseArray<DrawerItem> holderFactories;

    private OnItemSelectedListener listener;

    public DrawerAdapter(List<DrawerItem> items) {
        this.items = items;
        this.viewTypes = new HashMap<>();
        this.holderFactories = new SparseArray<>();

        processViewTypes();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = holderFactories.get(viewType).createViewHolder(parent);
        holder.adapter = this;
        return holder;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(ViewHolder holder, int position) {
        items.get(position).bindViewHolder(holder);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return viewTypes.get(items.get(position).getClass());
    }

    private void processViewTypes() {
        int type = 0;
        for (DrawerItem item : items) {
            if (!viewTypes.containsKey(item.getClass())) {
                viewTypes.put(item.getClass(), type);
                holderFactories.put(type, item);
                type++;
            }
        }
    }

    public void setSelected(int position) {
        DrawerItem newChecked = items.get(position);
        if (!newChecked.isSelectable()) {
            return;
        }

        for (int i = 0; i < items.size(); i++) {
            DrawerItem item = items.get(i);
            if (item.isChecked()) {
                item.setChecked(false);
                notifyItemChanged(i);
                break;
            }
        }

        newChecked.setChecked(true);
        notifyItemChanged(position);

        if (listener != null) {
            listener.onItemSelected(position);
        }
    }

    public void setListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    static abstract class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private DrawerAdapter adapter;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            adapter.setSelected(getAdapterPosition());
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }
}
