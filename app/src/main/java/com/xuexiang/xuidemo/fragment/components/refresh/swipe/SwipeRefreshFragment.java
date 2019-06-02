package com.xuexiang.xuidemo.fragment.components.refresh.swipe;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.SimpleRecyclerAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.Utils;
import com.xuexiang.xuidemo.widget.MaterialLoadMoreView;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019/4/6 下午6:57
 */
@Page(name = "下拉刷新和加载更多\n拓展SwipeRefreshLayout的功能")
public class SwipeRefreshFragment extends BaseFragment {

    private SimpleRecyclerAdapter mAdapter;

    @BindView(R.id.recycler_view)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private Handler mHandler = new Handler();

    private boolean mEnableLoadMore;
    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.layout_swipe_recycler_view;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        Utils.initRecyclerView(recyclerView);

        recyclerView.setAdapter(mAdapter = new SimpleRecyclerAdapter());

        swipeRefreshLayout.setColorSchemeColors(0xff0099cc, 0xffff4444, 0xff669900, 0xffaa66cc, 0xffff8800);
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
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadData();
        }
    };

    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadData();
    }

    private void loadData() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.refresh(DemoDataProvider.getDemoData());
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                enableLoadMore();
            }
        }, 1000);
    }

    /**
     * 确保有数据加载了才开启加载更多
     */
    private void enableLoadMore() {
        if (recyclerView != null && !mEnableLoadMore) {
            mEnableLoadMore = true;
            //SwipeRefreshLayout不支持加载更多
//            recyclerView.useDefaultLoadMore();
            useMaterialLoadMore();
            // 加载更多的监听。
            recyclerView.setLoadMoreListener(mLoadMoreListener);
            recyclerView.loadMoreFinish(false, true);
        }
    }

    private void useMaterialLoadMore() {
        MaterialLoadMoreView loadMoreView = new MaterialLoadMoreView(getContext());
        recyclerView.addFooterView(loadMoreView);
        recyclerView.setLoadMoreView(loadMoreView);
    }

    /**
     * 加载更多。
     */
    private SwipeRecyclerView.LoadMoreListener mLoadMoreListener = new SwipeRecyclerView.LoadMoreListener() {
        @Override
        public void onLoadMore() {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mAdapter.loadMore(DemoDataProvider.getDemoData());
                    // 数据完更多数据，一定要掉用这个方法。
                    // 第一个参数：表示此次数据是否为空。
                    // 第二个参数：表示是否还有更多数据。
                    if (recyclerView != null) {
                        recyclerView.loadMoreFinish(false, true);
                    }
                    // 如果加载失败调用下面的方法，传入errorCode和errorMessage。
                    // errorCode随便传，你自定义LoadMoreView时可以根据errorCode判断错误类型。
                    // errorMessage是会显示到loadMoreView上的，用户可以看到。
                    // mRecyclerView.loadMoreError(0, "请求网络失败");
                }
            }, 1000);
        }
    };


    @Override
    public void onDestroyView() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }
}




