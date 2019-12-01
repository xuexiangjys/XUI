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

package com.xuexiang.xui.adapter.listview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 集合列表适配器
 *
 * @param <T>
 * @author XUE
 */
public abstract class XListAdapter<T> extends BaseAdapter {

    private final List<T> mData = new ArrayList<>();
    private Context mContext;
    /**
     * 当前点击的条目
     */
    protected int mSelectPosition = -1;

    public XListAdapter(Context context) {
        mContext = context;
    }

    public XListAdapter(Context context, List<T> data) {
        mContext = context;
        setData(data);
    }

    public XListAdapter(Context context, T[] data) {
        mContext = context;
        setData(data);
    }

    public void setData(List<T> data) {
        if (data != null) {
            mData.clear();
            mData.addAll(data);
            mSelectPosition = -1;
            notifyDataSetChanged();
        }
    }

    public void setData(T[] data) {
        if (data != null && data.length > 0) {
            setData(Arrays.asList(data));
        }
    }

    public void addData(List<T> data) {
        if (data != null && !data.isEmpty()) {
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void addData(T[] data) {
        addData(Arrays.asList(data));
    }

    public void addData(T data) {
        if (data != null) {
            mData.add(data);
            notifyDataSetChanged();
        }
    }

    public void removeElement(T element) {
        if (mData.contains(element)) {
            mData.remove(element);
            notifyDataSetChanged();
        }
    }

    public void removeElement(int position) {
        if (mData.size() > position) {
            mData.remove(position);
            notifyDataSetChanged();
        }
    }

    public void removeElements(List<T> elements) {
        if (elements != null && elements.size() > 0 && mData.size() >= elements.size()) {
            for (T element : elements) {
                if (mData.contains(element)) {
                    mData.remove(element);
                }
            }
            notifyDataSetChanged();
        }
    }

    public void removeElements(T[] elements) {
        if (elements != null && elements.length > 0) {
            removeElements(Arrays.asList(elements));
        }
    }

    public void updateElement(T element, int position) {
        if (checkPosition(position)) {
            mData.set(position, element);
            notifyDataSetChanged();
        }
    }

    public void addElement(T element) {
        if (element != null) {
            mData.add(element);
            notifyDataSetChanged();
        }
    }

    public void clearData() {
        mData.clear();
        mSelectPosition = -1;
        notifyDataSetChanged();
    }

    public void clearNotNotify() {
        mData.clear();
        mSelectPosition = -1;
    }

    protected void visible(boolean flag, View view) {
        if (flag) {
            view.setVisibility(View.VISIBLE);
        }
    }

    protected void gone(boolean flag, View view) {
        if (flag) {
            view.setVisibility(View.GONE);
        }
    }

    protected void inVisible(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    protected Drawable getDrawable(int resId) {
        return mContext.getResources().getDrawable(resId);
    }

    protected String getString(int resId) {
        return mContext.getResources().getString(resId);
    }

    protected int getColor(int resId) {
        return mContext.getResources().getColor(resId);
    }

    public List<T> getItems() {
        return mData;
    }

    public int getSize() {
        return mData.size();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return checkPosition(position) ? mData.get(position) : null;
    }

    private boolean checkPosition(int position) {
        return position >= 0 && position <= mData.size() - 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    public Context getContext() {
        return mContext;
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
    public XListAdapter setSelectPosition(int selectPosition) {
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

}
