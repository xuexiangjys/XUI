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

package com.xuexiang.xuidemo.fragment.components.refresh.sortedlist;

import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SortedList;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.entity.NewInfo;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xutil.common.logger.Logger;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2020/6/28 12:11 AM
 */
@Page(name = "SortedList 自动数据排序刷新")
public class SortedListRefreshFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    /**
     * 数据缓存
     */
    private SortedList<NewInfo> mData;

    private SortedListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_diffutil_refresh;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("刷新") {
            @SingleClick
            @Override
            public void performAction(View view) {
                mockRefresh();
            }
        });
        return titleBar;
    }

    /**
     * 模拟刷新
     */
    private void mockRefresh() {
        if (mData == null || mData.size() == 0) {
            return;
        }
        int position = getRandomInsertPosition(mData.size());
        Logger.e("动态更新的位置:" + position);
        mData.add(new NewInfo("Android", "这个是刷新更新的内容")
                .setSummary("这里是内容！～～～～～")
                .setID(mData.get(position).getID())
                .setDetailUrl("https://juejin.im/post/5b480b79e51d45190905ef44")
                .setImageUrl("https://user-gold-cdn.xitu.io/2018/7/13/16492d9b7877dc21?imageView2/0/w/1280/h/960/format/webp/ignore-error/1"));
    }

    private int getRandomInsertPosition(int listSize) {
        return (int) (Math.random() * 100) % listSize;
    }

    @Override
    protected void initViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new SortedListAdapter();
        mData = new SortedList<>(NewInfo.class, new SortedListCallback(mAdapter));
        mAdapter.setDataSource(mData);
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setColorSchemeColors(0xff0099cc, 0xffff4444, 0xff669900, 0xffaa66cc, 0xffff8800);
        swipeRefreshLayout.setEnabled(true);
    }

    @Override
    protected void initListeners() {
        // 刷新监听。
        swipeRefreshLayout.setOnRefreshListener(mRefreshListener);
        refresh();
    }

    /**
     * 刷新。
     */
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = this::loadData;

    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadData();
    }

    private void loadData() {
        swipeRefreshLayout.postDelayed(() -> {
            mData.replaceAll(DemoDataProvider.getDemoNewInfos());
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }
}
