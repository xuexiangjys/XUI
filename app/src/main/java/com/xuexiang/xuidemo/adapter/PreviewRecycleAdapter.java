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

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scwang.smartrefresh.layout.adapter.BaseRecyclerAdapter;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
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

    public PreviewRecycleAdapter() {
        super(R.layout.adapter_item_image_preview);
    }

    /**
     * 绑定布局控件
     *
     * @param holder
     * @param model
     * @param position
     */
    @Override
    protected void onBindViewHolder(SmartViewHolder holder, ImageViewInfo model, int position) {
        if (model != null) {
            if (model.getVideoUrl() == null){
                holder.findViewById(R.id.btnVideo).setVisibility(View.GONE);
            }else {
                holder.findViewById(R.id.btnVideo).setVisibility(View.VISIBLE);
            }

            ImageView imageView = holder.findViewById(R.id.iv);

            Glide.with(imageView.getContext())
                    .load(model.getUrl())
                    .apply(GlideMediaLoader.getRequestOptions())
                    .into(imageView);

            imageView.setTag(R.id.iv, model.getUrl());
        }
    }
}
