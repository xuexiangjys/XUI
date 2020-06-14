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

package com.xuexiang.xuidemo.adapter.base.delegate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

/**
 * 单独布局的DelegateAdapter
 *
 * @author xuexiang
 * @since 2020/3/20 1:04 AM
 */
public abstract class SingleDelegateAdapter extends DelegateAdapter.Adapter<RecyclerViewHolder> {

    private int mLayoutId;

    private LayoutHelper mLayoutHelper;

    /**
     * 构造函数
     *
     * @param layoutId 布局ID
     */
    public SingleDelegateAdapter(int layoutId) {
        this(layoutId, new SingleLayoutHelper());
    }

    /**
     * 构造函数
     *
     * @param layoutId     布局ID
     * @param layoutHelper 布局助手
     */
    public SingleDelegateAdapter(int layoutId, LayoutHelper layoutHelper) {
        mLayoutId = layoutId;
        mLayoutHelper = layoutHelper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    /**
     * 加载布局获取控件
     *
     * @param parent   父布局
     * @param layoutId 布局ID
     * @return
     */
    protected View inflateView(ViewGroup parent, @LayoutRes int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(inflateView(parent, mLayoutId));
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
