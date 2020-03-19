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

import com.alibaba.android.vlayout.LayoutHelper;

import java.util.Collection;

/**
 * 简易DelegateAdapter适配器
 *
 * @author xuexiang
 * @since 2020/3/20 12:55 AM
 */
public abstract class SimpleDelegateAdapter<T> extends BaseDelegateAdapter<T> {

    private int mLayoutId;

    private LayoutHelper mLayoutHelper;

    public SimpleDelegateAdapter(int layoutId, LayoutHelper layoutHelper) {
        super();
        mLayoutId = layoutId;
        mLayoutHelper = layoutHelper;
    }

    public SimpleDelegateAdapter(int layoutId, LayoutHelper layoutHelper, Collection<T> list) {
        super(list);
        mLayoutId = layoutId;
        mLayoutHelper = layoutHelper;
    }

    public SimpleDelegateAdapter(int layoutId, LayoutHelper layoutHelper, T[] data) {
        super(data);
        mLayoutId = layoutId;
        mLayoutHelper = layoutHelper;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return mLayoutId;
    }


    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }
}
