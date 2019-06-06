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

import androidx.annotation.NonNull;
import android.view.View;
import android.widget.AbsListView;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.SimpleRecyclerAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.Arrays;
import java.util.Collection;
/**
 * @author xuexiang
 * @since 2018/12/6 下午5:57
 */
@Page(name = "下拉刷新基础用法\n上拉加载、下拉刷新、自动刷新和点击事件")
public class RefreshBasicFragment extends BaseFragment {

    private SimpleRecyclerAdapter mAdapter;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh_basic;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        AbsListView listView = findViewById(R.id.listView);
        listView.setAdapter(mAdapter = new SimpleRecyclerAdapter());

        final RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.refresh(DemoDataProvider.getDemoData());
                        refreshLayout.finishRefresh();
                        refreshLayout.resetNoMoreData();//setNoMoreData(false);
                    }
                }, 2000);
            }
        });
        //上拉加载
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter.getItemCount() > 30) {
                            ToastUtils.toast("数据全部加载完毕");
                            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                        } else {
                            mAdapter.loadMore(DemoDataProvider.getDemoData());
                            refreshLayout.finishLoadMore();
                        }
                    }
                }, 2000);
            }
        });

        //触发自动刷新
        refreshLayout.autoRefresh();
        //item 点击测试
        mAdapter.setOnItemClickListener(new SmartViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                ToastUtils.toast("点击:" + position);
            }
        });

        mAdapter.setOnItemLongClickListener(new SmartViewHolder.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View itemView, int position) {
                ToastUtils.toast("长按:" + position);
            }
        });

//        //点击测试
//        RefreshFooter footer = refreshLayout.getRefreshFooter();
//        if (footer != null) {
//            refreshLayout.getRefreshFooter().getView().findViewById(ClassicsFooter.ID_TEXT_TITLE).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ToastUtils.toast("已经到底了！");
//                }
//            });
//        }
    }

}
