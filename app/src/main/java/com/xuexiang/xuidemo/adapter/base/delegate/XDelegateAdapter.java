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

package com.xuexiang.xuidemo.adapter.base.delegate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 基础DelegateAdapter
 *
 * @author xuexiang
 * @since 2020/3/20 12:17 AM
 */
public abstract class XDelegateAdapter<T, V extends RecyclerView.ViewHolder> extends DelegateAdapter.Adapter<V> {
    /**
     * 数据源
     */
    protected final List<T> mData = new ArrayList<>();
    /**
     * 当前点击的条目
     */
    protected int mSelectPosition = -1;

    public XDelegateAdapter() {

    }

    public XDelegateAdapter(Collection<T> list) {
        if (list != null) {
            mData.addAll(list);
        }
    }

    public XDelegateAdapter(T[] data) {
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
        return getViewHolder(parent, viewType);
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
        return position >= 0 && position <= mData.size() - 1;
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
     * 给指定位置添加一项
     *
     * @param pos
     * @param item
     * @return
     */
    public XDelegateAdapter add(int pos, T item) {
        mData.add(pos, item);
        notifyItemInserted(pos);
        return this;
    }

    /**
     * 在列表末端增加一项
     *
     * @param item
     * @return
     */
    public XDelegateAdapter add(T item) {
        mData.add(item);
        notifyItemInserted(mData.size() - 1);
        return this;
    }

    /**
     * 删除列表中指定索引的数据
     *
     * @param pos
     * @return
     */
    public XDelegateAdapter delete(int pos) {
        mData.remove(pos);
        notifyItemRemoved(pos);
        return this;
    }

    /**
     * 刷新列表中指定位置的数据
     *
     * @param pos
     * @param item
     * @return
     */
    public XDelegateAdapter refresh(int pos, T item) {
        mData.set(pos, item);
        notifyItemChanged(pos);
        return this;
    }

    /**
     * 刷新列表数据
     *
     * @param collection
     * @return
     */
    public XDelegateAdapter refresh(Collection<T> collection) {
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
     * @param array
     * @return
     */
    public XDelegateAdapter refresh(T[] array) {
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
     * @param collection
     * @return
     */
    public XDelegateAdapter loadMore(Collection<T> collection) {
        if (collection != null) {
            mData.addAll(collection);
            notifyDataSetChanged();
        }
        return this;
    }

    /**
     * 加载更多
     *
     * @param array
     * @return
     */
    public XDelegateAdapter loadMore(T[] array) {
        if (array != null && array.length > 0) {
            mData.addAll(Arrays.asList(array));
            notifyDataSetChanged();
        }
        return this;
    }

    /**
     * 添加一个
     *
     * @param item
     * @return
     */
    public XDelegateAdapter load(T item) {
        if (item != null) {
            mData.add(item);
            notifyDataSetChanged();
        }
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
     * @param selectPosition
     * @return
     */
    public XDelegateAdapter setSelectPosition(int selectPosition) {
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
