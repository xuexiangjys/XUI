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

package com.xuexiang.xuidemo.fragment.components.refresh.sample.selection;

import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xuidemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuexiang
 * @since 2020/9/2 9:33 PM
 */
public class SelectionListAdapter extends BaseRecyclerAdapter<SelectionItem> {

    private static final int SELECTION_GROUP = 0;
    private static final int SELECTION_CHILD = 1;

    private OnSelectionChangedListener mOnSelectionChangedListener;

    @Override
    protected int getItemLayoutId(int viewType) {
        if (viewType == SELECTION_GROUP) {
            return R.layout.adapter_selection_group_list_item;
        }
        return R.layout.adapter_selection_child_list_item;
    }

    public SelectionListAdapter setOnSelectionChangedListener(OnSelectionChangedListener onSelectionChangedListener) {
        mOnSelectionChangedListener = onSelectionChangedListener;
        return this;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).isTitle() ? SELECTION_GROUP : SELECTION_CHILD;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, SelectionItem item) {
        if (item == null) {
            return;
        }
        if (item.isTitle()) {
            holder.text(R.id.tv_subject_title, item.subjectTitle);
            holder.text(R.id.tv_selection_title1, item.selectionTitle1);
            holder.text(R.id.tv_selection_title2, item.selectionTitle2);
        } else {
            holder.text(R.id.tv_subject_name, item.subjectName);
            if (item.isSelection1()) {
                holder.checked(R.id.rb_select1, true);
            } else if (item.isSelection2()) {
                holder.checked(R.id.rb_select2, true);
            }
            RadioGroup radioGroup = holder.findViewById(R.id.rg_select);
            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                switch (checkedId) {
                    case R.id.rb_select1:
                        item.setSelection(0);
                        break;
                    case R.id.rb_select2:
                        item.setSelection(1);
                        break;
                    default:
                        break;
                }
                if (mOnSelectionChangedListener != null) {
                    mOnSelectionChangedListener.onSelectionChanged(item);
                }

            });

        }
    }


    /**
     * @return 选择项目的总数
     */
    public int getTotalSelectionItemCount() {
        return getItemCount() - 1;
    }


    /**
     * 获取选择选项1(selection为0）的数量
     *
     * @return 选择选项1的数量
     */
    public int getSelection1Count() {
        int count = 0;
        for (SelectionItem item : getData()) {
            if (item.isSelection1()) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取选择选项2(selection为1）的数量
     *
     * @return 选择选项1的数量
     */
    public int getSelection2Count() {
        int count = 0;
        for (SelectionItem item : getData()) {
            if (item.isSelection2()) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取选择选项的数量
     *
     * @return 选择选项的数量
     */
    public int[] getSelectionCount() {
        int count1 = 0;
        int count2 = 0;
        for (SelectionItem item : getData()) {
            if (item.isSelection1()) {
                count1++;
            } else if (item.isSelection2()) {
                count2++;
            }
        }
        return new int[]{count1, count2};
    }


    /**
     * @return 选择结果
     */
    public List<SelectionItem> getSelectionResult() {
        List<SelectionItem> items = new ArrayList<>();
        for (SelectionItem item : getData()) {
            if (!item.isTitle()) {
                items.add(item);
            }
        }
        return items;
    }

    /**
     * 选择发生变化的监听
     */
    public interface OnSelectionChangedListener {
        /**
         * 选择发生变化
         *
         * @param item
         */
        void onSelectionChanged(SelectionItem item);
    }


}
