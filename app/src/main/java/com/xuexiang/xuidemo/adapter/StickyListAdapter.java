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
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.adapter.recyclerview.sticky.FullSpanUtils;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.entity.NewInfo;
import com.xuexiang.xuidemo.adapter.entity.StickyItem;

/**
 * @author xuexiang
 * @since 2020/4/25 1:23 AM
 */
public class StickyListAdapter extends BaseRecyclerAdapter<StickyItem> {

    /**
     * 头部标题
     */
    public static final int TYPE_HEAD_STICKY = 1;
    /**
     * 新闻信息
     */
    private static final int TYPE_NEW_INFO = 2;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        FullSpanUtils.onAttachedToRecyclerView(recyclerView, this, TYPE_HEAD_STICKY);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerViewHolder holder) {
        FullSpanUtils.onViewAttachedToWindow(holder, this, TYPE_HEAD_STICKY);
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isHeadSticky()) {
            return TYPE_HEAD_STICKY;
        } else {
            return TYPE_NEW_INFO;
        }
    }

    @Override
    public int getItemLayoutId(int viewType) {
        if (viewType == TYPE_HEAD_STICKY) {
            return R.layout.adapter_vlayout_title_item;
        } else {
            return R.layout.adapter_news_xuilayout_list_item;
        }
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, StickyItem item) {
        if (item == null) {
            return;
        }

        if (item.isHeadSticky()) {
            holder.text(R.id.tv_title, item.getHeadTitle());
        } else {
            NewInfo info = item.getNewInfo();
            holder.text(R.id.tv_user_name, info.getUserName());
            holder.text(R.id.tv_tag, info.getTag());
            holder.text(R.id.tv_title, info.getTitle());
            holder.text(R.id.tv_summary, info.getSummary());
            holder.text(R.id.tv_praise, info.getPraise() == 0 ? "点赞" : String.valueOf(info.getPraise()));
            holder.text(R.id.tv_comment, info.getComment() == 0 ? "评论" : String.valueOf(info.getComment()));
            holder.text(R.id.tv_read, "阅读量 " + info.getRead());
            holder.image(R.id.iv_image, info.getImageUrl());
        }
    }
}
