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

package com.xuexiang.xuidemo.adapter;

import android.util.SparseBooleanArray;

import androidx.annotation.NonNull;

import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xuidemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuexiang
 * @since 2019-11-23 01:32
 */
public class FlexboxLayoutAdapter extends BaseRecyclerAdapter<String> {

    private boolean mIsMultiSelectMode;
    private boolean mCancelable;

    private SparseBooleanArray mSparseArray = new SparseBooleanArray();

    public FlexboxLayoutAdapter(String[] data) {
        super(data);
    }

    public FlexboxLayoutAdapter setIsMultiSelectMode(boolean isMultiSelectMode) {
        mIsMultiSelectMode = isMultiSelectMode;
        return this;
    }

    public FlexboxLayoutAdapter setCancelable(boolean cancelable) {
        mCancelable = cancelable;
        return this;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.adapter_flexbox_layout_item;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, String item) {
        holder.text(R.id.tv_tag, item);
        if (mIsMultiSelectMode) {
            holder.select(R.id.tv_tag, mSparseArray.get(position));
        } else {
            holder.select(R.id.tv_tag, getSelectPosition() == position);
        }
    }

    /**
     * 选择
     *
     * @param position
     * @return
     */
    public boolean select(int position) {
        return mIsMultiSelectMode ? multiSelect(position) : singleSelect(position);
    }

    /**
     * 多选
     *
     * @param positions
     */
    public void multiSelect(int... positions) {
        if (!mIsMultiSelectMode) {
            return;
        }
        for (int position : positions) {
            multiSelect(position);
        }
    }

    /**
     * 多选
     *
     * @param position
     */
    public boolean multiSelect(int position) {
        if (!mIsMultiSelectMode) {
            return false;
        }
        mSparseArray.append(position, !mSparseArray.get(position));
        notifyItemChanged(position);
        return true;
    }

    /**
     * 单选
     *
     * @param position
     */
    public boolean singleSelect(int position) {
        return singleSelect(position, mCancelable);
    }

    /**
     * 单选
     *
     * @param position
     * @param cancelable
     */
    public boolean singleSelect(int position, boolean cancelable) {
        if (position == getSelectPosition()) {
            if (cancelable) {
                setSelectPosition(-1);
                return true;
            }
        } else {
            setSelectPosition(position);
            return true;
        }
        return false;
    }


    /**
     * @return 获取选中的内容
     */
    public String getSelectContent() {
        String value = getSelectItem();
        if (value == null) {
            return "";
        }
        return value;
    }


    /**
     * @return 获取多选的内容
     */
    public List<String> getMultiContent() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
            if (mSparseArray.get(i)) {
                list.add(getItem(i));
            }
        }
        return list;
    }
}
