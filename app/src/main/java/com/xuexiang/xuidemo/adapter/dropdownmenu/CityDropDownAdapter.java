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

package com.xuexiang.xuidemo.adapter.dropdownmenu;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xuexiang.xui.adapter.listview.BaseListAdapter;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xuidemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CityDropDownAdapter extends BaseListAdapter<String, CityDropDownAdapter.ViewHolder> {

    public CityDropDownAdapter(Context context) {
        super(context);
    }

    public CityDropDownAdapter(Context context, String[] data) {
        super(context, data);
    }

    @Override
    protected ViewHolder newViewHolder(View convertView) {
        return new ViewHolder(convertView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.adapter_drop_down_list_item;
    }

    @Override
    protected void convert(ViewHolder holder, String item, int position) {
        holder.mText.setText(item);
        if (mSelectPosition != -1) {
            if (mSelectPosition == position) {
                holder.mText.setSelected(true);
                holder.mText.setCompoundDrawablesWithIntrinsicBounds(null, null, ResUtils.getVectorDrawable(holder.mText.getContext(), R.drawable.ic_checked_right), null);
            } else {
                holder.mText.setSelected(false);
                holder.mText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }
    }

    static class ViewHolder {
        @BindView(R.id.text)
        TextView mText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
