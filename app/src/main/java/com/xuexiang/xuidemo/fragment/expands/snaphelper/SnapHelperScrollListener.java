/*
 * Copyright (C) 2023 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.expands.snaphelper;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

public class SnapHelperScrollListener extends RecyclerView.OnScrollListener {

    private final SnapHelper mSnapHelper;

    private final OnPageChangedListener mListener;

    private int mLastPageIndex = -1;

    public SnapHelperScrollListener(@NonNull SnapHelper snapHelper, @NonNull OnPageChangedListener listener) {
        mSnapHelper = snapHelper;
        mListener = listener;
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        if (newState != RecyclerView.SCROLL_STATE_IDLE) {
            return;
        }
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null) {
            return;
        }
        //获取当前选中的itemView
        View snapView = mSnapHelper.findSnapView(manager);
        int newPageIndex = snapView != null ? manager.getPosition(snapView) : -1;
        if (newPageIndex != mLastPageIndex) {
            mListener.onPageChanged(newPageIndex, mLastPageIndex);
            mLastPageIndex = newPageIndex;
        }
    }
}
