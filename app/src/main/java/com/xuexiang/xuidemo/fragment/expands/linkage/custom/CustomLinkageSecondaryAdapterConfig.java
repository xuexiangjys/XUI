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

package com.xuexiang.xuidemo.fragment.expands.linkage.custom;


import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryFooterViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryHeaderViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryViewHolder;
import com.kunminx.linkage.bean.BaseGroupedItem;
import com.kunminx.linkage.contract.ILinkageSecondaryAdapterConfig;
import com.xuexiang.xuidemo.R;

/**
 * @author xuexiang
 * @since 2019-11-25 17:17
 */
public class CustomLinkageSecondaryAdapterConfig implements ILinkageSecondaryAdapterConfig<CustomGroupedItem.ItemInfo> {

    private OnSecondaryItemClickListener mItemClickListener;
    private static final int SPAN_COUNT = 2;

    public CustomLinkageSecondaryAdapterConfig(OnSecondaryItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public CustomLinkageSecondaryAdapterConfig setOnItemClickListner(OnSecondaryItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
        return this;
    }

    @Override
    public void setContext(Context context) {
    }

    @Override
    public int getGridLayoutId() {
        return R.layout.default_adapter_linkage_secondary_grid;
    }

    @Override
    public int getLinearLayoutId() {
        return R.layout.default_adapter_linkage_secondary_linear;
    }

    @Override
    public int getHeaderLayoutId() {
        return R.layout.default_adapter_linkage_secondary_header;
    }

    @Override
    public int getFooterLayoutId() {
        return R.layout.adapter_linkage_empty_footer;
    }

    @Override
    public int getHeaderTextViewId() {
        return R.id.secondary_header;
    }

    @Override
    public int getSpanCountOfGridMode() {
        return SPAN_COUNT;
    }

    @Override
    public void onBindViewHolder(final LinkageSecondaryViewHolder holder,
                                 final BaseGroupedItem<CustomGroupedItem.ItemInfo> item) {

        ((TextView) holder.getView(R.id.level_2_title)).setText(item.info.getTitle());
        ((TextView) holder.getView(R.id.level_2_content)).setText(item.info.getContent());

        ViewGroup viewGroup = holder.getView(R.id.level_2_item);
        viewGroup.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onSecondaryItemClick(holder, viewGroup, item);
            }
        });

    }

    @Override
    public void onBindHeaderViewHolder(LinkageSecondaryHeaderViewHolder holder,
                                       BaseGroupedItem<CustomGroupedItem.ItemInfo> item) {
        ((TextView) holder.getView(R.id.secondary_header)).setText(item.header);
    }

    @Override
    public void onBindFooterViewHolder(LinkageSecondaryFooterViewHolder holder,
                                       BaseGroupedItem<CustomGroupedItem.ItemInfo> item) {
    }

    public interface OnSecondaryItemClickListener {
        /**
         * we suggest you get position by holder.getAdapterPosition
         *
         * @param holder primaryHolder
         * @param item   内容
         */
        void onSecondaryItemClick(LinkageSecondaryViewHolder holder, ViewGroup view, BaseGroupedItem<CustomGroupedItem.ItemInfo> item);
    }
}
