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

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.SimpleRecyclerAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
/**
 * @author xuexiang
 * @since 2018/12/6 下午5:57
 */
@Page(name = "下拉刷新基础用法\n上拉加载、下拉刷新、自动刷新和点击事件")
public class RefreshBasicFragment extends BaseFragment {

    private SimpleRecyclerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh_basic;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        WidgetUtils.initRecyclerView(recyclerView);
        recyclerView.setAdapter(mAdapter = new SimpleRecyclerAdapter());

        final RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        // 开启自动加载功能（非必须）
        refreshLayout.setEnableAutoLoadMore(true);
        // 下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout12 -> refreshLayout12.getLayout().postDelayed(() -> {
            mAdapter.refresh(DemoDataProvider.getDemoData());
            refreshLayout12.finishRefresh();
            refreshLayout12.resetNoMoreData();//setNoMoreData(false);
        }, 2000));
        // 上拉加载
        refreshLayout.setOnLoadMoreListener(refreshLayout1 -> refreshLayout1.getLayout().postDelayed(() -> {
            if (mAdapter.getItemCount() > 30) {
                XToastUtils.toast("数据全部加载完毕");
                refreshLayout1.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            } else {
                mAdapter.loadMore(DemoDataProvider.getDemoData());
                refreshLayout1.finishLoadMore();
            }
        }, 2000));

        // 触发自动刷新
        refreshLayout.autoRefresh();
        // item 点击测试
        mAdapter.setOnItemClickListener((itemView, item, position) -> XToastUtils.toast("点击:" + position));

        mAdapter.setOnItemLongClickListener((itemView, item, position) -> XToastUtils.toast("长按:" + position));

//        // 点击测试
//        RefreshFooter footer = refreshLayout.getRefreshFooter();
//        if (footer != null) {
//            refreshLayout.getRefreshFooter().getView().findViewById(ClassicsFooter.ID_TEXT_TITLE).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    XToastUtils.toast("已经到底了！");
//                }
//            });
//        }
    }

}
