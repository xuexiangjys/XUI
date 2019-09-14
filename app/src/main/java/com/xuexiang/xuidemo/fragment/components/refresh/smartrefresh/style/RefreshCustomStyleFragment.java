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

package com.xuexiang.xuidemo.fragment.components.refresh.smartrefresh.style;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xuexiang.rxutil2.rxjava.DisposablePool;
import com.xuexiang.rxutil2.rxjava.RxJavaUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.SimpleRecyclerAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.widget.CustomRefreshFooter;
import com.xuexiang.xuidemo.widget.CustomRefreshHeader;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019-07-22 23:40
 */
@Page(name = "自定义下拉刷新样式")
public class RefreshCustomStyleFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private SimpleRecyclerAdapter mAdapter;

    private CustomRefreshHeader mRefreshHeader;

    private int progress = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh_all_style;
    }

    @Override
    protected void initViews() {
        WidgetUtils.initRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter = new SimpleRecyclerAdapter());

        mRefreshLayout.setRefreshHeader(mRefreshHeader = new CustomRefreshHeader(getContext()));

        mRefreshLayout.setRefreshFooter(new CustomRefreshFooter(getContext()));
    }


    @Override
    protected void initListeners() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                handleRefresh();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.loadMore(DemoDataProvider.getDemoData());
                        refreshLayout.finishLoadMore();
                    }
                }, 2000);
            }
        });
        mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果


    }


    private void handleRefresh() {
        progress = 0;
        DisposablePool.get().add(RxJavaUtils.polling(0, 50, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    if (progress <= 100) {
                        updateProgress(progress++);
                    } else {
                        mAdapter.refresh(DemoDataProvider.getDemoData());
                        if (mRefreshLayout != null) {
                            mRefreshLayout.finishRefresh(true);
                        }
                        DisposablePool.get().remove("refresh_polling");
                    }

                }), "refresh_polling");
    }

    @MainThread
    private void updateProgress(int progress) {
        if (mRefreshHeader != null) {
            mRefreshHeader.refreshMessage("正在同步数据（" + progress + "%）");
        }
    }

    @Override
    public void onDestroyView() {
        DisposablePool.get().remove("refresh_polling");
        super.onDestroyView();
    }
}
