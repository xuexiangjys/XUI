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

package com.xuexiang.xui.widget.popupwindow.popup;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ExpandableListView;

import com.xuexiang.xui.adapter.simple.ExpandableItem;
import com.xuexiang.xui.adapter.simple.XUISimpleExpandableListAdapter;

import java.util.List;

/**
 * 简单的可伸缩弹窗
 *
 * @author xuexiang
 * @since 2019-11-11 16:31
 */
public class XUISimpleExpandablePopup<T extends XUISimpleExpandablePopup> extends XUIExpandableListPopup {

    public XUISimpleExpandablePopup(Context context, List<ExpandableItem> data) {
        this(context, new XUISimpleExpandableListAdapter(data));
    }

    public XUISimpleExpandablePopup(Context context, ExpandableItem... data) {
        this(context, new XUISimpleExpandableListAdapter(data));
    }

    public XUISimpleExpandablePopup(Context context, XUISimpleExpandableListAdapter adapter) {
        super(context, adapter);
    }

    /**
     * 创建弹窗
     *
     * @param onExpandableItemClickListener
     * @return
     */
    public T create(int width, final OnExpandableItemClickListener onExpandableItemClickListener) {
        create(width);
        setOnExpandableItemClickListener(onExpandableItemClickListener);
        return (T) this;
    }

    /**
     * 创建弹窗
     *
     * @param width     弹窗的宽度
     * @param maxHeight 弹窗最大的高度
     * @return
     */
    @Override
    public T create(int width, int maxHeight) {
        super.create(width, maxHeight);
        return (T) this;
    }

    /**
     * 创建弹窗
     *
     * @param width
     * @param maxHeight
     * @param onExpandableItemClickListener
     * @return
     */
    public T create(int width, int maxHeight, final OnExpandableItemClickListener onExpandableItemClickListener) {
        create(width, maxHeight);
        setOnExpandableItemClickListener(onExpandableItemClickListener);
        return (T) this;
    }

    /**
     * 设置条目点击监听
     *
     * @param onExpandableItemClickListener
     * @return
     */
    public XUIExpandableListPopup setOnExpandableItemClickListener(final OnExpandableItemClickListener onExpandableItemClickListener) {
        setOnExpandableItemClickListener(true, onExpandableItemClickListener);
        return (T) this;
    }

    /**
     * 设置条目点击监听
     *
     * @param autoDismiss
     * @param onExpandableItemClickListener
     * @return
     */
    public T setOnExpandableItemClickListener(final boolean autoDismiss, final OnExpandableItemClickListener onExpandableItemClickListener) {
        if (mExpandableListView != null) {
            mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    if (onExpandableItemClickListener != null) {
                        onExpandableItemClickListener.onExpandableItemClick(getAdapter(), getAdapter().getGroup(groupPosition), groupPosition, childPosition);
                    }
                    if (autoDismiss) {
                        dismiss();
                    }
                    return false;
                }
            });
        }
        return (T) this;
    }

    @Override
    public XUISimpleExpandableListAdapter getAdapter() {
        return (XUISimpleExpandableListAdapter) mAdapter;
    }

    @Override
    public T create(int width) {
        super.create(width);
        return (T) this;
    }

    @Override
    public T setDividerHeight(int dividerHeight) {
        super.setDividerHeight(dividerHeight);
        return (T) this;
    }

    @Override
    public T setHasDivider(boolean hasDivider) {
        super.setHasDivider(hasDivider);
        return (T) this;
    }

    @Override
    public T setGroupDivider(Drawable divider) {
        super.setGroupDivider(divider);
        return (T) this;
    }

    @Override
    public T setChildDivider(Drawable divider) {
        super.setChildDivider(divider);
        return (T) this;
    }

    @Override
    public T setEnableOnlyExpandOne(boolean enable) {
        super.setEnableOnlyExpandOne(enable);
        return (T) this;
    }

    /**
     * 可伸缩列表条目点击监听
     */
    public interface OnExpandableItemClickListener {
        /**
         * 条目点击
         *
         * @param adapter
         * @param group
         * @param groupPosition 父节点索引
         * @param childPosition 子节点索引
         */
        void onExpandableItemClick(XUISimpleExpandableListAdapter adapter, ExpandableItem group, int groupPosition, int childPosition);
    }


}
