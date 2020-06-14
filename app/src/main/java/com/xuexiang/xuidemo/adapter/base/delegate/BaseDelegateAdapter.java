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

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

import java.util.Collection;

/**
 * 通用的DelegateAdapter适配器
 *
 * @author xuexiang
 * @since 2020/3/20 12:44 AM
 */
public abstract class BaseDelegateAdapter<T> extends XDelegateAdapter<T, RecyclerViewHolder> {

    public BaseDelegateAdapter() {
        super();
    }

    public BaseDelegateAdapter(Collection<T> list) {
        super(list);
    }

    public BaseDelegateAdapter(T[] data) {
        super(data);
    }

    /**
     * 适配的布局
     *
     * @param viewType
     * @return
     */
    protected abstract int getItemLayoutId(int viewType);

    @NonNull
    @Override
    protected RecyclerViewHolder getViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(inflateView(parent, getItemLayoutId(viewType)));
    }
}
