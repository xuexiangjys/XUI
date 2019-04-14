package com.xuexiang.xuidemo.fragment.components.refresh.swipe;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ViewGroup;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.SimpleRecyclerAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.Utils;
import com.xuexiang.xutil.tip.ToastUtils;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import butterknife.BindView;

/**
 * @author XUE
 * @since 2019/4/1 11:21
 */
@Page(name = "SwipeMenuItem\nItem侧滑菜单")
public class SwipeMenuItemFragment extends BaseFragment {
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private SimpleRecyclerAdapter mAdapter;

    @BindView(R.id.recycler_view)
    SwipeRecyclerView recyclerView;

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

        //必须在setAdapter之前调用
        recyclerView.setSwipeMenuCreator(swipeMenuCreator);
        //必须在setAdapter之前调用
        recyclerView.setOnItemMenuClickListener(mMenuItemClickListener);
        recyclerView.setAdapter(mAdapter = new SimpleRecyclerAdapter());

        refreshLayout.setColorSchemeColors(0xff0099cc, 0xffff4444, 0xff669900, 0xffaa66cc, 0xffff8800);
    }

    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int position) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_70);

            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加左侧的，如果不添加，则左侧不会出现菜单。
            {
                SwipeMenuItem addItem = new SwipeMenuItem(getContext()).setBackground(R.drawable.menu_selector_green)
                        .setImage(R.drawable.ic_swipe_menu_add)
                        .setWidth(width)
                        .setHeight(height);
                swipeLeftMenu.addMenuItem(addItem); // 添加菜单到左侧。

                SwipeMenuItem closeItem = new SwipeMenuItem(getContext()).setBackground(R.drawable.menu_selector_red)
                        .setImage(R.drawable.ic_swipe_menu_close)
                        .setWidth(width)
                        .setHeight(height);
                swipeLeftMenu.addMenuItem(closeItem); // 添加菜单到左侧。
            }

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getContext()).setBackground(R.drawable.menu_selector_red)
                        .setImage(R.drawable.ic_swipe_menu_delete)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

                SwipeMenuItem addItem = new SwipeMenuItem(getContext()).setBackground(R.drawable.menu_selector_green)
                        .setText("添加")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(addItem); // 添加菜单到右侧。
            }
        }
    };

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private OnItemMenuClickListener mMenuItemClickListener = new OnItemMenuClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                ToastUtils.toast("list第" + position + "; 右侧菜单第" + menuPosition);
            } else if (direction == SwipeRecyclerView.LEFT_DIRECTION) {
                ToastUtils.toast("list第" + position + "; 左侧菜单第" + menuPosition);
            }
        }
    };

    @Override
    protected void initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        refresh(); //第一次进入触发自动刷新，演示效果
    }

    private void refresh() {
        refreshLayout.setRefreshing(true);
        loadData();
    }

    private void loadData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.refresh(DemoDataProvider.getDemoData());
                if (refreshLayout != null) {
                    refreshLayout.setRefreshing(false);
                }
            }
        }, 1000);
    }

}
