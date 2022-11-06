package com.xuexiang.xuidemo.fragment.components.refresh.swipe;

import android.os.Handler;
import android.view.View;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.SimpleRecyclerAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xui.utils.XToastUtils;
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
    private SimpleImageBanner banner;

    @BindView(R.id.recycler_view)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private Handler mHandler = new Handler();

    private boolean mEnableLoadMore;

    private int mIndex = 0;
    MaterialLoadMoreView mLoadMoreView;
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
        WidgetUtils.initRecyclerView(recyclerView);

        // HeaderView，必须在setAdapter之前调用
        View headerView = getLayoutInflater().inflate(R.layout.include_head_view_banner, recyclerView, false);

        banner = headerView.findViewById(R.id.sib_simple_usage);
        banner.setSource(DemoDataProvider.getBannerList())
                .setOnItemClickListener((view, item, position) -> XToastUtils.toast("headBanner position--->" + position)).startScroll();
        recyclerView.addHeaderView(headerView);

        recyclerView.setAdapter(mAdapter = new SimpleRecyclerAdapter());

        swipeRefreshLayout.setColorSchemeColors(0xff0099cc, 0xffff4444, 0xff669900, 0xffaa66cc, 0xffff8800);
    }


    @Override
    protected void initListeners() {
        // 刷新监听。
        swipeRefreshLayout.setOnRefreshListener(mRefreshListener);

        autoRefresh();
    }

    /**
     * 刷新。
     */
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = this::refreshData;

    private void autoRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        refreshData();
    }

    private void refreshData() {
        mIndex = 0;
        mHandler.postDelayed(() -> {
            mAdapter.refresh(DemoDataProvider.getDemoData());
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
            }
            enableLoadMore();
        }, 1000);
    }

    /**
     * 确保有数据加载了才开启加载更多
     */
    private void enableLoadMore() {
        if (recyclerView != null && !mEnableLoadMore) {
            mEnableLoadMore = true;
            useMaterialLoadMore();
            // 加载更多的监听。
            recyclerView.setLoadMoreListener(mLoadMoreListener);
            recyclerView.loadMoreFinish(false, true);
        }
    }

    private void useMaterialLoadMore() {
        if (mLoadMoreView == null) {
            mLoadMoreView = new MaterialLoadMoreView(getContext());
        }
        recyclerView.addFooterView(mLoadMoreView);
        recyclerView.setLoadMoreView(mLoadMoreView);
    }

    /**
     * 确保有数据加载了才开启加载更多
     */
    private void disEnableLoadMore() {
        if (recyclerView != null && mEnableLoadMore) {
            mEnableLoadMore = false;
            disableMaterialLoadMore();
            // 加载更多的监听。
            recyclerView.setLoadMoreListener(null);
            recyclerView.loadMoreFinish(false, false);
        }
    }

    private void disableMaterialLoadMore() {
        if (mLoadMoreView == null) {
            mLoadMoreView = new MaterialLoadMoreView(getContext());
        }
        recyclerView.removeFooterView(mLoadMoreView);
        recyclerView.setLoadMoreView(null);
    }

    /**
     * 加载更多。
     */
    private SwipeRecyclerView.LoadMoreListener mLoadMoreListener = new SwipeRecyclerView.LoadMoreListener() {
        @Override
        public void onLoadMore() {
            mIndex ++;
            mHandler.postDelayed(() -> {
                mAdapter.loadMore(DemoDataProvider.getDemoData());
                // 数据完更多数据，一定要掉用这个方法。
                // 第一个参数：表示此次数据是否为空。
                // 第二个参数：表示是否还有更多数据。
                if (recyclerView != null) {
                    recyclerView.loadMoreFinish(false, true);
                }
                if (mIndex >= 2) {
                    disEnableLoadMore();
                }
                // 如果加载失败调用下面的方法，传入errorCode和errorMessage。
                // errorCode随便传，你自定义LoadMoreView时可以根据errorCode判断错误类型。
                // errorMessage是会显示到loadMoreView上的，用户可以看到。
                // mRecyclerView.loadMoreError(0, "请求网络失败");
            }, 1000);
        }
    };


    @Override
    public void onDestroyView() {
        banner.recycle();
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

}




