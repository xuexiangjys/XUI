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

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.adapter.BaseRecyclerAdapter;
import com.xuexiang.xui.widget.imageview.nine.ItemImageClickListener;
import com.xuexiang.xui.widget.imageview.nine.NineGridImageView;
import com.xuexiang.xui.widget.imageview.nine.NineGridImageViewAdapter;
import com.xuexiang.xui.widget.imageview.preview.PreviewBuilder;
import com.xuexiang.xui.widget.imageview.preview.loader.GlideMediaLoader;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.fragment.components.imageview.preview.ImageViewInfo;
import com.xuexiang.xuidemo.fragment.components.imageview.preview.NineGridInfo;

import java.util.List;

/**
 * 九宫格适配器
 *
 * @author xuexiang
 * @since 2018/12/9 下午11:51
 */
public class NineGridRecycleAdapter extends BaseRecyclerAdapter<NineGridInfo, NineGridRecycleAdapter.NineGridHolder> {

    @NonNull
    @Override
    public NineGridHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == NineGridImageView.STYLE_GRID) {
            return new NineGridHolder(getInflate(parent, R.layout.adapter_item_nine_grid_grid_style));
        } else {
            return new NineGridHolder(getInflate(parent, R.layout.adapter_item_nine_grid_fill_style));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getShowType();
    }

    @Override
    protected void onBindViewHolder(NineGridHolder holder, NineGridInfo model, int position) {
        holder.bind(model);
    }


    public class NineGridHolder extends RecyclerView.ViewHolder {
        private NineGridImageView<ImageViewInfo> mNglContent;
        private TextView mTvContent;
        private NineGridImageViewAdapter<ImageViewInfo> mAdapter = new NineGridImageViewAdapter<ImageViewInfo>() {
            /**
             * 图片加载
             *
             * @param context
             * @param imageView
             * @param imageViewInfo 图片信息
             */
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, ImageViewInfo imageViewInfo) {
                Glide.with(imageView.getContext())
                        .load(imageViewInfo.getUrl())
                        .apply(GlideMediaLoader.getRequestOptions())
                        .into(imageView);
            }

            @Override
            protected ImageView generateImageView(Context context) {
                return super.generateImageView(context);
            }
        };

        public NineGridHolder(View itemView) {
            super(itemView);
            mTvContent = itemView.findViewById(R.id.tv_content);
            mNglContent = itemView.findViewById(R.id.ngl_images);
            mNglContent.setAdapter(mAdapter);
            mNglContent.setItemImageClickListener(new ItemImageClickListener<ImageViewInfo>() {
                @Override
                public void onItemImageClick(Context context, ImageView imageView, int index, List<ImageViewInfo> list) {
                    computeBoundsBackward(list);//组成数据
                    PreviewBuilder.from((Activity) context)
                            .setImgs(list)
                            .setCurrentIndex(index)
                            .setType(PreviewBuilder.IndicatorType.Dot)
                            .start();//启动
                }
            });
        }

        /**
         * 查找信息
         * @param list 图片集合
         */
        private void computeBoundsBackward(List<ImageViewInfo> list) {
            for (int i = 0;i < mNglContent.getChildCount(); i++) {
                View itemView = mNglContent.getChildAt(i);
                Rect bounds = new Rect();
                if (itemView != null) {
                    ImageView thumbView = (ImageView) itemView;
                    thumbView.getGlobalVisibleRect(bounds);
                }
                list.get(i).setBounds(bounds);
                list.get(i).setUrl(list.get(i).getUrl());
            }

        }

        public void bind(NineGridInfo gridInfo) {
            mNglContent.setImagesData(gridInfo.getImgUrlList(), gridInfo.getSpanType());
            mTvContent.setText(gridInfo.getContent());
        }
    }
}
