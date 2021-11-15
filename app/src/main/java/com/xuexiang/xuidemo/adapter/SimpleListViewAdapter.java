/*
 * Copyright (C) 2021 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.adapter;

import android.content.Context;
import android.view.View;

import com.xuexiang.xui.adapter.listview.BaseListAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xuidemo.R;

import java.util.Collection;

/**
 * @author xuexiang
 * @since 2021/11/15 7:02 PM
 */
public class SimpleListViewAdapter extends BaseListAdapter<String, RecyclerViewHolder> {

    public SimpleListViewAdapter(Context context) {
        super(context);
    }

    public SimpleListViewAdapter(Context context, Collection<String> data) {
        super(context, data);
    }

    public SimpleListViewAdapter(Context context, String[] data) {
        super(context, data);
    }

    @Override
    protected RecyclerViewHolder newViewHolder(View convertView) {
        return new RecyclerViewHolder(convertView);
    }

    @Override
    protected int getLayoutId() {
        return android.R.layout.simple_list_item_2;
    }

    @Override
    protected void convert(RecyclerViewHolder holder, String item, int position) {
        holder.text(android.R.id.text1, ResUtils.getResources().getString(R.string.item_example_number_title, position));
        holder.text(android.R.id.text2, ResUtils.getResources().getString(R.string.item_example_number_abstract, position));
        holder.textColorId(android.R.id.text2, R.color.xui_config_color_light_blue_gray);
    }

}
