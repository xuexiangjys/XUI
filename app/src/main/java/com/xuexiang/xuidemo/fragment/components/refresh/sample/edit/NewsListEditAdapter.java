/*
 * Copyright (C) 2021 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.components.refresh.sample.edit;

import android.util.SparseBooleanArray;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.adapter.recyclerview.XRecyclerAdapter;
import com.xuexiang.xui.widget.button.SmoothCheckBox;
import com.xuexiang.xui.widget.imageview.ImageLoader;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.base.broccoli.BroccoliRecyclerAdapter;
import com.xuexiang.xuidemo.adapter.entity.NewInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.samlss.broccoli.Broccoli;

/**
 * @author xuexiang
 * @since 2019/4/7 下午12:06
 */
public class NewsListEditAdapter extends BroccoliRecyclerAdapter<NewInfo> {

    /**
     * 是否是管理模式，默认是false
     */
    private boolean mIsManageMode;

    /**
     * 记录选中的信息
     */
    private SparseBooleanArray mSparseArray = new SparseBooleanArray();

    private boolean mIsSelectAll;

    private OnAllSelectStatusChangedListener mListener;

    public NewsListEditAdapter(OnAllSelectStatusChangedListener listener) {
        super(DemoDataProvider.getEmptyNewInfo());
        mListener = listener;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.adapter_news_edit_list_item;
    }

    @Override
    protected void onBindData(RecyclerViewHolder holder, NewInfo model, int position) {
        holder.text(R.id.tv_user_name, model.getUserName());
        holder.text(R.id.tv_tag, model.getTag());
        holder.text(R.id.tv_title, model.getTitle());
        holder.text(R.id.tv_summary, model.getSummary());
        holder.text(R.id.tv_praise, model.getPraise() == 0 ? "点赞" : String.valueOf(model.getPraise()));
        holder.text(R.id.tv_comment, model.getComment() == 0 ? "评论" : String.valueOf(model.getComment()));
        holder.text(R.id.tv_read, "阅读量 " + model.getRead());
        RadiusImageView imageView = holder.findViewById(R.id.iv_image);
        ImageLoader.get().loadImage(imageView, model.getImageUrl());

        holder.visible(R.id.scb_select, mIsManageMode ? View.VISIBLE : View.GONE);
        if (mIsManageMode) {
            SmoothCheckBox checkBox = holder.findViewById(R.id.scb_select);
            checkBox.setCheckedSilent(mSparseArray.get(position));
            checkBox.setOnCheckedChangeListener((checkBox1, isChecked) -> {
                mSparseArray.append(position, isChecked);
                refreshAllSelectStatus();
            });
        }
    }

    @Override
    protected void onBindBroccoli(RecyclerViewHolder holder, Broccoli broccoli) {
        broccoli.addPlaceholders(
                holder.findView(R.id.iv_avatar),
                holder.findView(R.id.tv_user_name),
                holder.findView(R.id.tv_tag),
                holder.findView(R.id.tv_title),
                holder.findView(R.id.tv_summary),
                holder.findView(R.id.iv_image),
                holder.findView(R.id.iv_praise),
                holder.findView(R.id.tv_praise),
                holder.findView(R.id.iv_comment),
                holder.findView(R.id.tv_comment),
                holder.findView(R.id.tv_read)
        );
    }

    /**
     * 切换管理模式
     */
    public void switchManageMode() {
        setManageMode(!mIsManageMode);
    }

    /**
     * 设置管理模式
     *
     * @param isManageMode 是否是管理模式
     */
    public void setManageMode(boolean isManageMode) {
        if (mIsManageMode != isManageMode) {
            mIsManageMode = isManageMode;
            notifyDataSetChanged();
            if (!mIsManageMode) {
                // 退出管理模式时清除选中状态
                mSparseArray.clear();
                onAllSelectStatusChanged(false);
            }
        }
    }

    @Override
    public XRecyclerAdapter refresh(Collection<NewInfo> collection) {
        // 刷新时清除选中状态
        mSparseArray.clear();
        onAllSelectStatusChanged(false);
        return super.refresh(collection);
    }

    @Override
    public XRecyclerAdapter loadMore(Collection<NewInfo> collection) {
        onAllSelectStatusChanged(false);
        return super.loadMore(collection);
    }

    /**
     * 进入管理模式
     */
    public void enterManageMode(int position) {
        mSparseArray.append(position, true);
        setManageMode(true);
    }

    /**
     * 更新选中状态
     *
     * @param position 位置
     */
    public void updateSelectStatus(int position, RecyclerView recyclerView) {
        mSparseArray.append(position, !mSparseArray.get(position));
        refreshAllSelectStatus();
        if (recyclerView != null) {
            // 直接拿到指定view进行设置，这样可以解决闪动的问题
            View view = recyclerView.getChildAt(position);
            if (view != null) {
                RecyclerViewHolder holder = (RecyclerViewHolder) recyclerView.getChildViewHolder(view);
                if (holder != null) {
                    holder.checked(R.id.scb_select, mSparseArray.get(position));
                    return;
                }
            }
        }
        notifyItemChanged(position);

    }

    private void refreshAllSelectStatus() {
        for (int i = 0; i < getItemCount(); i++) {
            if (!mSparseArray.get(i)) {
                onAllSelectStatusChanged(false);
                return;
            }
        }
        onAllSelectStatusChanged(true);
    }

    /**
     * 设置是否全选
     *
     * @param isSelectAll 是否全选
     */
    public void setSelectAll(boolean isSelectAll) {
        mIsSelectAll = isSelectAll;
        if (isSelectAll) {
            for (int i = 0; i < getItemCount(); i++) {
                mSparseArray.append(i, true);
            }
        } else {
            mSparseArray.clear();
        }
        notifyDataSetChanged();
    }

    public boolean isManageMode() {
        return mIsManageMode;
    }

    public void onAllSelectStatusChanged(boolean isSelectAll) {
        if (mIsSelectAll != isSelectAll) {
            mIsSelectAll = isSelectAll;
            if (mListener != null) {
                mListener.onAllSelectStatusChanged(isSelectAll);
            }
        }
    }

    public List<Integer> getSelectedIndexList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
            if (mSparseArray.get(i)) {
                list.add(i);
            }
        }
        return list;
    }

    public List<NewInfo> getSelectedNewInfoList() {
        List<NewInfo> list = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
            if (mSparseArray.get(i)) {
                list.add(getItem(i));
            }
        }
        return list;
    }

    public interface OnAllSelectStatusChangedListener {

        /**
         * 全选状态发生变化
         *
         * @param isSelectAll 是否全选
         */
        void onAllSelectStatusChanged(boolean isSelectAll);
    }
}
