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

package com.xuexiang.xuidemo.adapter;

import androidx.annotation.NonNull;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsSize;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.widget.iconfont.XUIIconFont;

/**
 * @author xuexiang
 * @since 2019-10-13 19:30
 */
public class IconFontGridAdapter extends BaseRecyclerAdapter<XUIIconFont.Icon> {

    public IconFontGridAdapter(XUIIconFont.Icon[] data) {
        super(data);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.layout_widget_item;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, XUIIconFont.Icon item) {
        if (item != null) {
            holder.text(R.id.item_name, item.getName());

            IconicsDrawable drawable = new IconicsDrawable(holder.getContext())
                    .icon(item)
                    .size(IconicsSize.dp(80));
            holder.image(R.id.item_icon, drawable);
        }
    }
}
