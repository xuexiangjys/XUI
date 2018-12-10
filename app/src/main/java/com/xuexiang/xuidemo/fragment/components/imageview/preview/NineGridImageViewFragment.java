/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.components.imageview.preview;

import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.imageview.preview.PreviewBuilder;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.NineGridRecycleAdapter;
import com.xuexiang.xuidemo.adapter.PreviewRecycleAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.base.decorator.GridDividerItemDecoration;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.List;

import butterknife.BindView;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * @author xuexiang
 * @since 2018/12/9 下午11:23
 */
@Page(name = "NineGrid 九宫格预览")
public class NineGridImageViewFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private NineGridRecycleAdapter mAdapter;

    private int mPage = -1;

    private boolean mIsVideo = false;

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("切换") {
            @Override
            public void performAction(View view) {
                mIsVideo = !mIsVideo;
                mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
            }
        });
        return titleBar;
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_preview_recycleview;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
        mRecyclerView.setAdapter(mAdapter = new NineGridRecycleAdapter());
        mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
    }

    @Override
    protected void initListeners() {
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mRefreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPage++;
                        if (mPage < getMediaRes().size()) {
                            mAdapter.loadMore(getMediaRes().get(mPage));
                            mRefreshLayout.finishLoadMore();
                        } else {
                            ToastUtils.toast("数据全部加载完毕");
                            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                        }
                    }
                }, 1000);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRefreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPage = 0;
                        mAdapter.refresh(getMediaRes().get(mPage));
                        mRefreshLayout.finishRefresh();
                    }
                }, 1000);

            }
        });
    }

    private List<List<NineGridInfo>> getMediaRes() {
        if (mIsVideo) {
            return DemoDataProvider.sNineGridVideos;
        } else {
            return DemoDataProvider.sNineGridPics;
        }
    }
}
