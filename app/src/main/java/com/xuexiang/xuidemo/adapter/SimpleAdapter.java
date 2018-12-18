/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xuexiang.xui.adapter.BaseListAdapter;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xutil.common.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 主副标题显示适配器
 *
 * @author xuexiang
 * @since 2018/12/19 上午12:19
 */
public class SimpleAdapter extends BaseListAdapter<Map<String, String>, SimpleAdapter.ViewHolder> {

    public static final String KEY_TITLE = "title";
    public static final String KEY_SUB_TITLE = "sub_title";

    public SimpleAdapter(Context context, List<Map<String, String>> data) {
        super(context, data);
    }

    @Override
    protected ViewHolder newViewHolder(View convertView) {
        ViewHolder holder = new ViewHolder();
        holder.mTvTitle = convertView.findViewById(R.id.tv_title);
        holder.mTvSubTitle = convertView.findViewById(R.id.tv_sub_title);
        return holder;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.adapter_item_simple_list_2;
    }

    @Override
    protected void convert(ViewHolder holder, Map<String, String> item, int position) {
        holder.mTvTitle.setText(item.get(KEY_TITLE));
        if (!StringUtils.isEmpty(item.get(KEY_SUB_TITLE))) {
            holder.mTvSubTitle.setText(item.get(KEY_SUB_TITLE));
            holder.mTvSubTitle.setVisibility(View.VISIBLE);
        } else {
            holder.mTvSubTitle.setVisibility(View.GONE);
        }
    }

    public static class ViewHolder {
        /**
         * 标题
         */
        public TextView mTvTitle;
        /**
         * 副标题
         */
        public TextView mTvSubTitle;
    }
}
