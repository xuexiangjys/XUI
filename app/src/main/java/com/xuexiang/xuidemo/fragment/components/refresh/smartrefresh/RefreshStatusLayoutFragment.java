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

package com.xuexiang.xuidemo.fragment.components.refresh.smartrefresh;

import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.statelayout.StatefulLayout;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.SimpleRecyclerAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.XToastUtils;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2018/12/7 下午3:30
 */
@Page(name = "刷新状态布局\n自动切换状态，包含出错、无网络、暂无数据等")
public class RefreshStatusLayoutFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_stateful)
    StatefulLayout mLlStateful;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    private SimpleRecyclerAdapter mAdapter;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh_status_layout;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        WidgetUtils.initRecyclerView(mRecyclerView);

        mRecyclerView.setAdapter(mAdapter = new SimpleRecyclerAdapter());

        //下拉刷新
        mRefreshLayout.setOnRefreshListener(refreshLayout -> refreshLayout.getLayout().postDelayed(() -> {
            Status status = getRefreshStatus();
            switch (status) {
                case SUCCESS:
                    mAdapter.refresh(DemoDataProvider.getDemoData());
                    mRefreshLayout.resetNoMoreData();//setNoMoreData(false);
                    mLlStateful.showContent();
                    mRefreshLayout.setEnableLoadMore(true);
                    break;
                case EMPTY:
                    mLlStateful.showEmpty();
                    mRefreshLayout.setEnableLoadMore(false);
                    break;
                case ERROR:
                    showError();
                    break;
                case NO_NET:
                    showOffline();
                    break;
                default:
                    break;
            }
            refreshLayout.finishRefresh();

        }, 2000));
        //上拉加载
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> refreshLayout.getLayout().postDelayed(() -> {
            if (mAdapter.getItemCount() > 30) {
                XToastUtils.toast("数据全部加载完毕");
                refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            } else {
                mAdapter.loadMore(DemoDataProvider.getDemoData());
                refreshLayout.finishLoadMore();
            }
        }, 2000));
        mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
    }

    private void showOffline() {
        mLlStateful.showOffline(v -> mRefreshLayout.autoRefresh());
        mRefreshLayout.setEnableLoadMore(false);
    }

    private void showError() {
        mLlStateful.showError(v -> mRefreshLayout.autoRefresh());
        mRefreshLayout.setEnableLoadMore(false);
    }

    private enum Status {
        SUCCESS,
        EMPTY,
        ERROR,
        NO_NET,
    }

    private Status getRefreshStatus() {
        int status = (int) (Math.random() *  10);
        if (status % 2 == 0) {
            return Status.SUCCESS;
        } else if (status % 3 == 0) {
            return Status.EMPTY;
        } else if (status % 5 == 0) {
            return Status.ERROR;
        } else {
            return Status.NO_NET;
        }
    }
}
