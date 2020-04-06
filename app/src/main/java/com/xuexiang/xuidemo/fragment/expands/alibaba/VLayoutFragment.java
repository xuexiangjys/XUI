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

package com.xuexiang.xuidemo.fragment.expands.alibaba;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;
import com.xuexiang.xui.widget.imageview.ImageLoader;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.base.delegate.SimpleDelegateAdapter;
import com.xuexiang.xuidemo.adapter.base.delegate.SingleDelegateAdapter;
import com.xuexiang.xuidemo.adapter.entity.NewInfo;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.Utils;
import com.xuexiang.xuidemo.utils.XToastUtils;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2020/3/19 11:26 PM
 */
@Page(name = "VLayout\n多布局组件")
public class VLayoutFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private SimpleDelegateAdapter<NewInfo> mNewsAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_v_layout;
    }

    @Override
    protected void initViews() {
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(getContext());
        recyclerView.setLayoutManager(virtualLayoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 20);

        //轮播条
        SingleDelegateAdapter bannerAdapter = new SingleDelegateAdapter(R.layout.include_head_view_banner) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
                SimpleImageBanner banner = holder.findViewById(R.id.sib_simple_usage);
                banner.setSource(DemoDataProvider.getBannerList())
                        .setOnItemClickListener((view, item, position1) -> XToastUtils.toast("headBanner position--->" + position1)).startScroll();
            }
        };

        //九宫格菜单
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
        gridLayoutHelper.setPadding(0, 16, 0, 0);
        gridLayoutHelper.setVGap(10);
        gridLayoutHelper.setHGap(0);
        SimpleDelegateAdapter<AdapterItem> commonAdapter = new SimpleDelegateAdapter<AdapterItem>(R.layout.adapter_common_grid_item, gridLayoutHelper, DemoDataProvider.getGridItems(getContext())) {
            @Override
            protected void bindData(@NonNull RecyclerViewHolder holder, int position, AdapterItem item) {
                if (item != null) {
                    RadiusImageView imageView = holder.findViewById(R.id.riv_item);
                    imageView.setCircle(true);
                    ImageLoader.get().loadImage(imageView, item.getIcon());
                    holder.text(R.id.tv_title, item.getTitle().toString().substring(0, 1));
                    holder.text(R.id.tv_sub_title, item.getTitle());

                    holder.click(R.id.ll_container, v -> XToastUtils.toast("点击了：" + item.getTitle()));
                }
            }
        };

        //资讯的标题
        SingleDelegateAdapter titleAdapter = new SingleDelegateAdapter(R.layout.adapter_title_item) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
                holder.text(R.id.tv_title, "资讯");
                holder.text(R.id.tv_action, "更多");
                holder.click(R.id.tv_action, v -> XToastUtils.toast("更多"));
            }
        };

        //资讯
        mNewsAdapter = new SimpleDelegateAdapter<NewInfo>(R.layout.adapter_news_card_view_list_item, new LinearLayoutHelper()) {
            @Override
            protected void bindData(@NonNull RecyclerViewHolder holder, int position, NewInfo model) {
                if (model != null) {
                    holder.text(R.id.tv_user_name, model.getUserName());
                    holder.text(R.id.tv_tag, model.getTag());
                    holder.text(R.id.tv_title, model.getTitle());
                    holder.text(R.id.tv_summary, model.getSummary());
                    holder.text(R.id.tv_praise, model.getPraise() == 0 ? "点赞" : String.valueOf(model.getPraise()));
                    holder.text(R.id.tv_comment, model.getComment() == 0 ? "评论" : String.valueOf(model.getComment()));
                    holder.text(R.id.tv_read, "阅读量 " + model.getRead());
                    holder.image(R.id.iv_image, model.getImageUrl());

                    holder.click(R.id.card_view, v -> Utils.goWeb(getContext(), model.getDetailUrl()));
                }
            }
        };

        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        delegateAdapter.addAdapter(bannerAdapter);
        delegateAdapter.addAdapter(commonAdapter);
        delegateAdapter.addAdapter(titleAdapter);
        delegateAdapter.addAdapter(mNewsAdapter);

        recyclerView.setAdapter(delegateAdapter);
    }

    @Override
    protected void initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.getLayout().postDelayed(() -> {
                mNewsAdapter.refresh(DemoDataProvider.getDemoNewInfos());
                refreshLayout.finishRefresh();
            }, 1000);
        });
        //上拉加载
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            refreshLayout.getLayout().postDelayed(() -> {
                mNewsAdapter.loadMore(DemoDataProvider.getDemoNewInfos());
                refreshLayout.finishLoadMore();
            }, 1000);
        });
        refreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
    }
}
