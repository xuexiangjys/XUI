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

import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.recyclerview.GridDividerItemDecoration;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.imageview.preview.PreviewBuilder;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.PreviewRecycleAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.List;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2018/12/7 下午5:21
 */
@Page(name = "RecycleView 图片预览")
public class PreviewRecycleViewFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private GridLayoutManager mGridLayoutManager;

    private PreviewRecycleAdapter mAdapter;

    private int mPage = -1;

    private boolean mIsVideo = false;

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("切换") {
            @Override
            public void performAction(View view) {
                onChanged(view);
            }
        });
        return titleBar;
    }

    @SingleClick
    private void onChanged(View view) {
        mIsVideo = !mIsVideo;
        mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
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
        mRecyclerView.setLayoutManager(mGridLayoutManager = new GridLayoutManager(getContext(), 2));
        mRecyclerView.addItemDecoration(new GridDividerItemDecoration(getContext(), 2, DensityUtils.dp2px(3)));

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter = new PreviewRecycleAdapter());
        mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
    }

    @Override
    protected void initListeners() {
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(final @NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPage++;
                        if (mPage < getMediaRes().size()) {
                            mAdapter.loadMore(getMediaRes().get(mPage));
                            refreshLayout.finishLoadMore();
                        } else {
                            ToastUtils.toast("数据全部加载完毕");
                            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                        }
                    }
                }, 1000);
            }

            @Override
            public void onRefresh(final @NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPage = 0;
                        mAdapter.refresh(getMediaRes().get(mPage));
                        refreshLayout.finishRefresh();
                    }
                }, 1000);

            }
        });

        mAdapter.setOnItemClickListener(new SmartViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                computeBoundsBackward(mGridLayoutManager.findFirstVisibleItemPosition());
                PreviewBuilder.from(getActivity())
                        .setImgs(mAdapter.getListData())
                        .setCurrentIndex(position)
                        .setSingleFling(true)
                        .setType(PreviewBuilder.IndicatorType.Number)
                        .start();
            }
        });
    }

    /**
     * 查找信息
     * 从第一个完整可见item逆序遍历，如果初始位置为0，则不执行方法内循环
     */
    private void computeBoundsBackward(int firstCompletelyVisiblePos) {
        for (int i = firstCompletelyVisiblePos; i < mAdapter.getCount(); i++) {
            View itemView = mGridLayoutManager.findViewByPosition(i);
            Rect bounds = new Rect();
            if (itemView != null) {
                ImageView imageView = itemView.findViewById(R.id.iv);
                imageView.getGlobalVisibleRect(bounds);
            }
            mAdapter.getItem(i).setBounds(bounds);
        }
    }

    private List<List<ImageViewInfo>> getMediaRes() {
        if (mIsVideo) {
            return DemoDataProvider.sVideos;
        } else {
            return DemoDataProvider.sPics;
        }
    }
}
