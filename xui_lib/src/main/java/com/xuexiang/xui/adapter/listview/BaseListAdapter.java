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
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 简单的集合列表适配器
 * @author XUE
 *
 * @param <T>
 * @param <H>
 */
public abstract class BaseListAdapter<T, H> extends XListAdapter<T> {

    public BaseListAdapter(Context context) {
        super(context);
    }
    public BaseListAdapter(Context context, List<T> data) {
        super(context, data);
    }

    public BaseListAdapter(Context context, T[] data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        H holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), getLayoutId(), null);
            holder = newViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (H) convertView.getTag();
        }
        
        T item = getItem(position);
        if (item != null) {
        	convert(holder, item, position);
        }
        return convertView;
    }

    /**
     * 创建ViewHolder
     * @param convertView
     * @return
     */
    protected abstract H newViewHolder(View convertView);

    /**
     * 获取适配的布局ID
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 转换布局
     * @param holder
     * @param item
     * @param position
     */
    protected abstract void convert(H holder, T item, int position);
}
