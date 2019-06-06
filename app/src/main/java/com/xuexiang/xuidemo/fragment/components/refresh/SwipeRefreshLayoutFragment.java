package com.xuexiang.xuidemo.fragment.components.refresh;

import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.SimpleRecyclerAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.Utils;

import butterknife.BindView;

/**
 * @author XUE
 * @since 2019/4/1 9:59
 */
@Page(name = "SwipeRefreshLayout\n谷歌官方下拉刷新控件")
public class SwipeRefreshLayoutFragment extends BaseFragment {
    private SimpleRecyclerAdapter mAdapter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_swipe_refresh_layout;
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
        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        refresh(); //第一次进入触发自动刷新，演示效果
    }

    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadData();
    }

    private void loadData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.refresh(DemoDataProvider.getDemoData());
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, 1000);
    }

}
