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

package com.xuexiang.xuidemo.adapter;

import androidx.annotation.NonNull;

import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.widget.imageview.ImageLoader;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xuidemo.R;

/**
 * 普通九宫格
 *
 * @author xuexiang
 * @since 2020/3/11 8:33 PM
 */
public class CommonGridAdapter extends BaseRecyclerAdapter<AdapterItem> {

    private boolean mIsCircle;

    public CommonGridAdapter(boolean isCircle) {
        super();
        mIsCircle = isCircle;
    }


    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.adapter_common_grid_item;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, AdapterItem item) {
        if (item != null) {
            RadiusImageView imageView = holder.findViewById(R.id.riv_item);
            imageView.setCircle(mIsCircle);
            ImageLoader.get().loadImage(imageView, item.getIcon());

            holder.text(R.id.tv_title, item.getTitle().toString().substring(0, 1));
            holder.text(R.id.tv_sub_title, item.getTitle());
        }
    }
}
