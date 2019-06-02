package com.xuexiang.xuidemo.fragment.expands.materialdesign.behavior;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.NewsCardViewListAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.Utils;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import butterknife.BindView;

/**
 * @author XUE
 * @since 2019/5/9 11:54
 */
public class SimpleListFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private NewsCardViewListAdapter mAdapter;
    @Override
    protected TitleBar initTitle() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.include_recycler_view_refresh;
    }

    @Override
    protected void initViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter = new NewsCardViewListAdapter());
        mAdapter.refresh(DemoDataProvider.getDemoNewInfos());

        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    protected void initListeners() {
        mAdapter.setOnItemClickListener(new SmartViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Utils.goWeb(getContext(), mAdapter.getItem(position).getDetailUrl());
            }
        });
    }
}
