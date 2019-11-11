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

package com.xuexiang.xui.adapter.simple;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ThemeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 简易的可伸缩列表适配器
 *
 * @author xuexiang
 * @since 2019-11-11 14:01
 */
public class XUISimpleExpandableListAdapter extends BaseExpandableListAdapter {
    /**
     * 数据
     */
    private List<ExpandableItem> mData;

    private int mPaddingLeftPx;

    private int mArrowUpResId = R.drawable.xui_ic_expand_arrow_up;
    private int mArrowDownResId = R.drawable.xui_ic_expand_arrow_down;

    public XUISimpleExpandableListAdapter(List<ExpandableItem> data) {
        mData = data;
    }

    public XUISimpleExpandableListAdapter(ExpandableItem... data) {
        mData = new ArrayList<>(Arrays.asList(data));
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.xui_layout_expand_group_item, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.ivIcon = convertView.findViewById(R.id.iv_icon);
            groupViewHolder.tvTitle = convertView.findViewById(R.id.tv_group_name);
            groupViewHolder.ivIndicator = convertView.findViewById(R.id.iv_indicator);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        AdapterItem group = getGroup(groupPosition).getGroup();
        if (group != null) {
            groupViewHolder.tvTitle.setText(group.getTitle());
            groupViewHolder.ivIndicator.setImageResource(isExpanded ? mArrowUpResId : mArrowDownResId);
            if (group.getIcon() != null) {
                groupViewHolder.ivIcon.setVisibility(View.VISIBLE);
                groupViewHolder.ivIcon.setImageDrawable(group.getIcon());
            } else {
                groupViewHolder.ivIcon.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.xui_layout_expand_child_item, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.llContentView = convertView.findViewById(R.id.ll_content);
            childViewHolder.ivIcon = convertView.findViewById(R.id.iv_icon);
            childViewHolder.tvTitle = convertView.findViewById(R.id.tv_child_name);

            if (mPaddingLeftPx != 0) {
                int padding = ThemeUtils.resolveDimension(parent.getContext(), R.attr.xui_config_content_spacing_vertical);
                childViewHolder.llContentView.setPadding(mPaddingLeftPx, padding, padding, padding);
                childViewHolder.llContentView.setGravity(Gravity.CENTER_VERTICAL);
            } else {
                childViewHolder.llContentView.setGravity(Gravity.CENTER);
            }
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        AdapterItem child = getChild(groupPosition, childPosition);
        if (child != null) {
            childViewHolder.tvTitle.setText(child.getTitle());
            if (child.getIcon() != null) {
                childViewHolder.ivIcon.setVisibility(View.VISIBLE);
                childViewHolder.ivIcon.setImageDrawable(child.getIcon());
            } else {
                childViewHolder.ivIcon.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    @Override
    public int getGroupCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mData != null ? mData.get(groupPosition).getChildSize() : 0;
    }

    @Override
    public ExpandableItem getGroup(int groupPosition) {
        return mData != null ? mData.get(groupPosition) : null;
    }

    @Override
    public AdapterItem getChild(int groupPosition, int childPosition) {
        return mData != null ? mData.get(groupPosition).getChildItem(childPosition) : null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public XUISimpleExpandableListAdapter setPaddingLeftPx(int paddingLeftPx) {
        mPaddingLeftPx = paddingLeftPx;
        return this;
    }

    public XUISimpleExpandableListAdapter setPaddingLeftDp(int paddingLeftDp) {
        mPaddingLeftPx = DensityUtils.dp2px(paddingLeftDp);
        return this;
    }

    /**
     * 设置箭头样式
     * @param arrowDownResId
     * @return
     */
    public XUISimpleExpandableListAdapter setArrowStyle(int arrowDownResId, int arrowUpResId) {
        mArrowDownResId = arrowDownResId;
        mArrowUpResId = arrowUpResId;
        return this;
    }

    private static class GroupViewHolder {
        AppCompatImageView ivIcon;
        TextView tvTitle;
        AppCompatImageView ivIndicator;
    }

    private static class ChildViewHolder {
        LinearLayout llContentView;
        AppCompatImageView ivIcon;
        TextView tvTitle;
    }
}
