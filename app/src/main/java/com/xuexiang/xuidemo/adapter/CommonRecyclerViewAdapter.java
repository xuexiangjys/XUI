/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.xuexiang.xuidemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的 RecyclerView Adapter。
 *
 * @author xuexiang
 * @since 2019/1/3 下午4:54
 */
public class CommonRecyclerViewAdapter extends RecyclerView.Adapter<CommonRecyclerViewAdapter.ViewHolder> {

    private List<Data> mItems;
    private AdapterView.OnItemClickListener mOnItemClickListener;

    public CommonRecyclerViewAdapter() {
        mItems = new ArrayList<>();
    }

    public static List<Data> generateDatas(int count) {
        List<Data> mDatas = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            mDatas.add(new Data(String.valueOf(i)));
        }
        return mDatas;
    }

    public void addItem(int position) {
        if (position > mItems.size()) {
            return;
        }

        mItems.add(position, new Data(String.valueOf(position)));
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        if (position >= mItems.size()) {
            return;
        }
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View root = inflater.inflate(R.layout.adapter_item_common_recycler_view, viewGroup, false);
        return new ViewHolder(root, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Data data = mItems.get(i);
        viewHolder.setText(data.text);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setItemCount(int count) {
        mItems.clear();
        mItems.addAll(generateDatas(count));

        notifyDataSetChanged();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(RecyclerView.ViewHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    public static class Data {
        public String text;

        public Data(String text) {
            this.text = text;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextView;
        private CommonRecyclerViewAdapter mAdapter;

        public ViewHolder(View itemView, CommonRecyclerViewAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);

            mAdapter = adapter;

            mTextView = (TextView) itemView.findViewById(R.id.textView);
        }

        public void setText(String text) {
            mTextView.setText(text);
        }


        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }
    }
}
