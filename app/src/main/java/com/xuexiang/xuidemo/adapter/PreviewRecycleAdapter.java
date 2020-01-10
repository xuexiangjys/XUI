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

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.widget.imageview.IconImageView;
import com.xuexiang.xui.widget.imageview.preview.loader.GlideMediaLoader;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.fragment.components.imageview.preview.ImageViewInfo;

/**
 * 图片预览的适配器
 *
 * @author xuexiang
 * @since 2018/12/7 下午5:40
 */
public class PreviewRecycleAdapter extends BaseRecyclerAdapter<ImageViewInfo> {

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.adapter_item_image_preview;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, ImageViewInfo item) {
        if (item != null) {
            IconImageView imageView = holder.findViewById(R.id.iv);

            imageView.setIsShowIcon(item.getVideoUrl() != null);

            Glide.with(imageView.getContext())
                    .load(item.getUrl())
                    .apply(GlideMediaLoader.getRequestOptions())
                    .into(imageView);

            imageView.setTag(R.id.iv, item.getUrl());
        }
    }
}
