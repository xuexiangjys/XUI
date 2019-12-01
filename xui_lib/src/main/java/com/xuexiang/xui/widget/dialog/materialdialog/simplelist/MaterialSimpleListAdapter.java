/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xui.widget.dialog.materialdialog.simplelist;

import android.graphics.PorterDuff;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.dialog.materialdialog.internal.MDAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * See the sample project to understand how this is used. Mimics the Simple List mDialog style
 * displayed on Google's guidelines site:
 * https://www.google.com/design/spec/components/dialogs.html#dialogs-simple-dialogs
 *
 * @author Aidan Follestad (afollestad)
 */
public class MaterialSimpleListAdapter
        extends RecyclerView.Adapter<MaterialSimpleListAdapter.SimpleListVH> implements MDAdapter {

    private MaterialDialog mDialog;
    private List<MaterialSimpleListItem> mItems;
    private OnItemClickListener mOnItemClickListener;

    public MaterialSimpleListAdapter(OnItemClickListener onItemClickListener) {
        mItems = new ArrayList<>(4);
        mOnItemClickListener = onItemClickListener;
    }

    public MaterialSimpleListAdapter(List<MaterialSimpleListItem> lists) {
        mItems = lists;
    }

    public MaterialSimpleListAdapter setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        return this;
    }

    public void add(MaterialSimpleListItem item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public void add(MaterialSimpleListItem.Builder builder) {
        add(builder.build());
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public MaterialSimpleListItem getItem(int index) {
        return mItems.get(index);
    }

    @Override
    public void setDialog(MaterialDialog dialog) {
        mDialog = dialog;
    }

    public MaterialDialog getMaterialDialog() {
        return mDialog;
    }

    @Override
    public SimpleListVH onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.md_layout_simplelist_item, parent, false);
        return new SimpleListVH(view, this);
    }

    @Override
    public void onBindViewHolder(SimpleListVH holder, int position) {
        if (mDialog != null) {
            final MaterialSimpleListItem item = mItems.get(position);
            if (item.getIcon() != null) {
                holder.mIcon.setImageDrawable(item.getIcon());
                holder.mIcon.setPadding(
                        item.getIconPadding(),
                        item.getIconPadding(),
                        item.getIconPadding(),
                        item.getIconPadding());
                holder
                        .mIcon
                        .getBackground()
                        .setColorFilter(item.getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
            } else {
                holder.mIcon.setVisibility(View.GONE);
            }
            holder.mTitle.setTextColor(mDialog.getBuilder().getItemColor());
            holder.mTitle.setText(item.getContent());
            mDialog.setTypeface(holder.mTitle, mDialog.getBuilder().getRegularFont());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public interface OnItemClickListener {

        void onMaterialListItemSelected(MaterialDialog dialog, int index, MaterialSimpleListItem item);
    }

    static class SimpleListVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView mIcon;
        final TextView mTitle;
        final MaterialSimpleListAdapter mAdapter;

        SimpleListVH(View itemView, MaterialSimpleListAdapter adapter) {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(android.R.id.icon);
            mTitle = (TextView) itemView.findViewById(android.R.id.title);
            mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mAdapter != null && mAdapter.mOnItemClickListener != null) {
                mAdapter.getMaterialDialog().dismiss();
                mAdapter.mOnItemClickListener.onMaterialListItemSelected(mAdapter.mDialog, getAdapterPosition(), mAdapter.getItem(getAdapterPosition()));
            }
        }
    }
}
