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

package com.xuexiang.xuidemo.fragment.expands;


import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.CommonRecyclerViewAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.fragment.expands.snaphelper.SnapHelperScrollListener;

import butterknife.BindView;

/**
 * 使用 {@link SnapHelper} 实现 {@link RecyclerView} 按页滚动。
 *
 * @author xuexiang
 * @since 2019/1/3 下午4:49
 */
@Page(name = "SnapHelper使用", extra = R.drawable.ic_expand_snap_helper)
public class SnapHelperFragment extends BaseFragment {

    @BindView(R.id.pagerWrap)
    ViewGroup mPagerWrap;

    RecyclerView mRecyclerView;
    LinearLayoutManager mPagerLayoutManager;
    CommonRecyclerViewAdapter mRecyclerViewAdapter;
    private SnapHelper mSnapHelper;

    private int mCurrentIndex;

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.icon_topbar_overflow) {
            @Override
            public void performAction(View view) {
                showBottomSheetList();
            }
        });
        return titleBar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pagerlayoutmanager;
    }

    @Override
    protected void initViews() {
        mRecyclerView = new RecyclerView(getContext());
        mPagerLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mPagerLayoutManager);
        mRecyclerViewAdapter = new CommonRecyclerViewAdapter();
        mRecyclerViewAdapter.setItemCount(10);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mPagerWrap.addView(mRecyclerView);
        // PagerSnapHelper每次只能滚动一个item;用LinearSnapHelper则可以一次滚动多个，并最终保证定位
        // mSnapHelper = new LinearSnapHelper();
        mSnapHelper = new PagerSnapHelper();
        mSnapHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.addOnScrollListener(new SnapHelperScrollListener(mSnapHelper, (newPageIndex, oldPageIndex) -> {
            mCurrentIndex = newPageIndex;
            boolean isSwipeLeft = newPageIndex < oldPageIndex;
            XToastUtils.toast(getSnapName(isSwipeLeft) + "到第" + newPageIndex + "页");
        }));
    }

    private String getSnapName(boolean isSwipeLeft) {
        if (mPagerLayoutManager.getOrientation() == RecyclerView.HORIZONTAL) {
            if (isSwipeLeft) {
                return "向左翻页";
            } else {
                return "向右翻页";
            }
        } else {
            if (isSwipeLeft) {
                return "向上翻页";
            } else {
                return "向下翻页";
            }
        }
    }

    private void snapPage(boolean isSwipeLeft) {
        if (mRecyclerView.getAdapter() == null) {
            return;
        }
        int targetPosition = isSwipeLeft ? mCurrentIndex - 1 : mCurrentIndex + 1;
        if (targetPosition < 0 || targetPosition > mRecyclerView.getAdapter().getItemCount() - 1) {
            return;
        }
        mRecyclerView.smoothScrollToPosition(targetPosition);
    }


    private void showBottomSheetList() {
        new BottomSheet.BottomListSheetBuilder(getActivity())
                .addItem("水平方向")
                .addItem("垂直方向")
                .addItem(getSnapName(true))
                .addItem(getSnapName(false))
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    switch (position) {
                        case 0:
                            mPagerLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                            break;
                        case 1:
                            mPagerLayoutManager.setOrientation(RecyclerView.VERTICAL);
                            break;
                        case 2:
                            snapPage(true);
                            break;
                        case 3:
                            snapPage(false);
                            break;
                        default:
                            break;
                    }
                })
                .build()
                .show();
    }

    @Override
    public void onDestroyView() {
        mRecyclerView.clearOnScrollListeners();
        super.onDestroyView();
    }
}
