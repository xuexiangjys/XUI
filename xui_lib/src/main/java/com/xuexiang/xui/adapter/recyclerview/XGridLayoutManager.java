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

package com.xuexiang.xui.adapter.recyclerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 防止数据错位导致数组越界的错误
 *
 * @author xuexiang
 * @since 2019-10-14 10:21
 */
public class XGridLayoutManager extends GridLayoutManager {

    /**
     * 是否支持滑动
     */
    private boolean mIsScrollEnabled = true;

    public XGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public XGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public XGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否支持滑动
     *
     * @param flag
     */
    public XGridLayoutManager setScrollEnabled(boolean flag) {
        mIsScrollEnabled = flag;
        return this;
    }

    @Override
    public boolean canScrollVertically() {
        return mIsScrollEnabled && super.canScrollVertically();
    }

    @Override
    public boolean canScrollHorizontally() {
        return mIsScrollEnabled && super.canScrollHorizontally();
    }

}
