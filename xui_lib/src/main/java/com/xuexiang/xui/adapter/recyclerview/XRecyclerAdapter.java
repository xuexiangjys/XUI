/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xui.adapter.recyclerview;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 基础的RecyclerView适配器
 *
 * @author xuexiang
 * @since 2019-08-11 16:12
 */
public abstract class XRecyclerAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    /**
     * 数据源
     */
    protected final List<T> mData = new ArrayList<>();
    /**
     * 点击监听
     */
    private RecyclerViewHolder.OnItemClickListener<T> mClickListener;
    /**
     * 长按监听
     */
    private RecyclerViewHolder.OnItemLongClickListener<T> mLongClickListener;

    /**
     * 当前点击的条目
     */
    protected int mSelectPosition = -1;

    public XRecyclerAdapter() {

    }

    public XRecyclerAdapter(Collection<T> list) {
        if (list != null) {
            mData.addAll(list);
        }
    }

    public XRecyclerAdapter(T[] data) {
        if (data != null && data.length > 0) {
            mData.addAll(Arrays.asList(data));
        }
    }

    /**
     * 构建自定义的ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    protected abstract V getViewHolder(@NonNull ViewGroup parent, int viewType);

    /**
     * 绑定数据
     *
     * @param holder
     * @param position 索引
     * @param item     列表项
     */
    protected abstract void bindData(@NonNull V holder, int position, T item);

    /**
     * 加载布局获取控件
     *
     * @param parent   父布局
     * @param layoutId 布局ID
     * @return
     */
    protected View inflateView(ViewGroup parent, @LayoutRes int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    @NonNull
    @Override
    public V onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final V holder = getViewHolder(parent, viewType);
        if (mClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(holder.itemView, getItem(holder.getLayoutPosition()), holder.getLayoutPosition());
                }
            });
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongClickListener.onItemLongClick(holder.itemView, getItem(holder.getLayoutPosition()), holder.getLayoutPosition());
                    return true;
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull V holder, int position) {
        bindData(holder, position, mData.get(position));
    }

    /**
     * 获取列表项
     *
     * @param position
     * @return
     */
    public T getItem(int position) {
        return checkPosition(position) ? mData.get(position) : null;
    }

    private boolean checkPosition(int position) {
        return position >= 0 && position < mData.size();
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * @return 数据源
     */
    public List<T> getData() {
        return mData;
    }

    /**
     * 重置数据源，并不进行刷新操作！！
     *
     * @param collection 数据源
     */
    public XRecyclerAdapter resetDataSource(Collection<T> collection) {
        if (collection != null) {
            mData.clear();
            mData.addAll(collection);
        }
        return this;
    }

    /**
     * 给指定位置添加一项
     *
     * @param pos  位置
     * @param item 数据项
     */
    public XRecyclerAdapter add(int pos, T item) {
        if (pos >= 0 && pos <= getItemCount()) {
            mData.add(pos, item);
            notifyItemInserted(pos);
        }
        return this;
    }

    /**
     * 在列表末端增加一项
     *
     * @param item 数据项
     */
    public XRecyclerAdapter add(T item) {
        mData.add(item);
        notifyItemInserted(mData.size() - 1);
        return this;
    }

    /**
     * 删除列表中指定索引的数据
     *
     * @param pos 位置
     */
    public XRecyclerAdapter delete(int pos) {
        if (checkPosition(pos)) {
            mData.remove(pos);
            notifyItemRemoved(pos);
        }
        return this;
    }

    /**
     * 刷新列表中指定位置的数据
     *
     * @param pos  位置
     * @param item 数据项
     */
    public XRecyclerAdapter refresh(int pos, T item) {
        if (checkPosition(pos)) {
            mData.set(pos, item);
            notifyItemChanged(pos);
        }
        return this;
    }

    /**
     * 刷新列表数据
     *
     * @param collection 加载的数据集合
     */
    public XRecyclerAdapter refresh(Collection<T> collection) {
        if (collection != null) {
            mData.clear();
            mData.addAll(collection);
            mSelectPosition = -1;
            notifyDataSetChanged();
        }
        return this;
    }

    /**
     * 刷新列表数据
     *
     * @param array 加载的数据数组
     */
    public XRecyclerAdapter refresh(T[] array) {
        if (array != null && array.length > 0) {
            mData.clear();
            mData.addAll(Arrays.asList(array));
            mSelectPosition = -1;
            notifyDataSetChanged();
        }
        return this;
    }

    /**
     * 加载更多
     *
     * @param collection 加载的数据集合
     */
    public XRecyclerAdapter loadMore(Collection<T> collection) {
        if (collection != null) {
            mData.addAll(collection);
            notifyDataSetChanged();
        }
        return this;
    }

    /**
     * 加载更多
     *
     * @param array 加载的数据数组
     */
    public XRecyclerAdapter loadMore(T[] array) {
        if (array != null && array.length > 0) {
            mData.addAll(Arrays.asList(array));
            notifyDataSetChanged();
        }
        return this;
    }

    /**
     * 添加一个
     *
     * @param item 数据
     */
    public XRecyclerAdapter load(T item) {
        if (item != null) {
            mData.add(item);
            notifyDataSetChanged();
        }
        return this;
    }

    /**
     * 局部刷新【增量刷新】
     *
     * @param pos   位置
     * @param key   刷新的关键字
     * @param value 刷新的内容
     */
    public void refreshPartly(int pos, String key, Object value) {
        Bundle payload = getBundle(key, value);
        notifyItemChanged(pos, payload);
    }

    private Bundle getBundle(String key, Object value) {
        Bundle payload = new Bundle();
        if (value instanceof String) {
            payload.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            payload.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            payload.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            payload.putFloat(key, (Float) value);
        } else if (value instanceof Double) {
            payload.putDouble(key, (Double) value);
        } else if (value instanceof Short) {
            payload.putDouble(key, (Short) value);
        } else if (value instanceof Long) {
            payload.putDouble(key, (Long) value);
        } else if (value instanceof Parcelable) {
            payload.putParcelable(key, (Parcelable) value);
        } else if (value instanceof Serializable) {
            payload.putSerializable(key, (Serializable) value);
        } else {
            payload.putString(key, value.toString());
        }
        return payload;
    }

    /**
     * 设置列表项点击监听
     *
     * @param listener 列表项点击监听
     */
    public XRecyclerAdapter setOnItemClickListener(RecyclerViewHolder.OnItemClickListener<T> listener) {
        mClickListener = listener;
        return this;
    }

    /**
     * 设置列表项长按监听
     *
     * @param listener 列表项长按监听
     */
    public XRecyclerAdapter setOnItemLongClickListener(RecyclerViewHolder.OnItemLongClickListener<T> listener) {
        mLongClickListener = listener;
        return this;
    }

    /**
     * @return 当前列表的选中项
     */
    public int getSelectPosition() {
        return mSelectPosition;
    }

    /**
     * 设置当前列表的选中项
     *
     * @param selectPosition 选中项
     */
    public XRecyclerAdapter setSelectPosition(int selectPosition) {
        mSelectPosition = selectPosition;
        notifyDataSetChanged();
        return this;
    }

    /**
     * 获取当前列表选中项
     *
     * @return 当前列表选中项
     */
    public T getSelectItem() {
        return getItem(mSelectPosition);
    }

    /**
     * 清除数据
     */
    public void clear() {
        if (!isEmpty()) {
            mData.clear();
            mSelectPosition = -1;
            notifyDataSetChanged();
        }
    }

}
