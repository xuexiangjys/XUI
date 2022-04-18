package com.xuexiang.xuidemo.fragment.components.refresh.swipe;

import android.view.View;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.SimpleRecyclerAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xui.utils.XToastUtils;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019/4/6 下午6:12
 */
@Page(name = "动态添加Head、FootView")
public class SwipeHeadFootViewFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    SwipeRecyclerView recyclerView;

    private SimpleImageBanner banner;
    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_swipe_recycler_view;
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

        View footerView = getLayoutInflater().inflate(R.layout.include_foot_view, recyclerView, false);
        recyclerView.addFooterView(footerView);

        final SimpleRecyclerAdapter adapter = new SimpleRecyclerAdapter(DemoDataProvider.getDemoData1());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((itemView, item, position) -> {
            //需要注意的是，因为加了一个HeaderView，所以position都被自动加了1,因此获取内容时需要减1
            XToastUtils.toast("点击了第" + position + "个条目：" + adapter.getItem(position - 1));
        });
    }


    @Override
    public void onDestroyView() {
        banner.recycle();
        super.onDestroyView();
    }
}
