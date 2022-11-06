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

package com.xuexiang.xuidemo.fragment.components.layout;

import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;
import com.xuexiang.xui.widget.layout.linkage.LinkageScrollLayout;
import com.xuexiang.xui.widget.layout.linkage.view.LinkageRecyclerView;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.CommonGridAdapter;
import com.xuexiang.xuidemo.adapter.NewsCardViewListAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.Utils;
import com.xuexiang.xui.utils.XToastUtils;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2020/3/11 7:32 PM
 */
@Page(name = "LinkageScrollLayout\n内联布局")
public class LinkageScrollLayoutFragment extends BaseFragment {

    @BindView(R.id.sib_simple_usage)
    SimpleImageBanner sibSimpleUsage;
    @BindView(R.id.recyclerView)
    LinkageRecyclerView recyclerView;
    @BindView(R.id.lsl_container)
    LinkageScrollLayout lslContainer;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recycler_head)
    RecyclerView recyclerHead;

    private NewsCardViewListAdapter mNewsListAdapter;
    private CommonGridAdapter mGridAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_linkage_scroll_layout;
    }

    @Override
    protected void initViews() {
        sibSimpleUsage.setSource(DemoDataProvider.getBannerList())
                .setOnItemClickListener((view, item, position) -> XToastUtils.toast("headBanner position--->" + position)).startScroll();
        WidgetUtils.initRecyclerView(recyclerView, 0);
        recyclerView.setAdapter(mNewsListAdapter = new NewsCardViewListAdapter());

        WidgetUtils.initGridRecyclerView(recyclerHead, 4, 0);
        recyclerHead.setAdapter(mGridAdapter = new CommonGridAdapter(true));
        mGridAdapter.refresh(DemoDataProvider.getGridItems(getContext()));
    }

    @Override
    protected void initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> refreshLayout.getLayout().postDelayed(() -> {
            mNewsListAdapter.refresh(DemoDataProvider.getDemoNewInfos());
            refreshLayout.finishRefresh();
        }, 1000));
        //上拉加载
        refreshLayout.setOnLoadMoreListener(refreshLayout -> refreshLayout.getLayout().postDelayed(() -> {
            mNewsListAdapter.loadMore(DemoDataProvider.getDemoNewInfos());
            refreshLayout.finishLoadMore();
        }, 1000));
        refreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果

        mNewsListAdapter.setOnItemClickListener((itemView, item, position) -> Utils.goWeb(getContext(), item.getDetailUrl()));
        mGridAdapter.setOnItemClickListener((itemView, item, position) -> XToastUtils.toast("点击了：" + item.getTitle()));
    }
}
