package com.xuexiang.xuidemo.fragment.expands.materialdesign.behavior;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xutil.display.Colors;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019-05-08 23:15
 */
@Page(name = "CoordinatorLayout + AppBarLayout\n详情页常用组合")
public class ToolbarBehaviorFragment extends BaseFragment {
    @BindView(R.id.appbar_layout_toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapse_layout)
    CollapsingToolbarLayout collapseLayout;
    @BindView(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @BindView(R.id.fab_scrolling)
    FloatingActionButton fabScrolling;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_behavior_toolbar;
    }

    @Override
    protected void initArgs() {
        super.initArgs();
        StatusBarUtils.translucent(getActivity(), Colors.TRANSPARENT);
        StatusBarUtils.setStatusBarLightMode(getActivity());

    }

    @Override
    protected TitleBar initTitle() {
        return null;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        toolbar.inflateMenu(R.menu.menu_search);
        toolbar.setNavigationOnClickListener(v -> popToBack());
        ViewUtils.setToolbarLayoutTextFont(collapseLayout);
        fabScrolling.setOnClickListener(v -> XToastUtils.toast("分享"));

        appbarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                StatusBarUtils.setStatusBarDarkMode(getActivity());
            } else {
                StatusBarUtils.setStatusBarLightMode(getActivity());
            }
        });
    }

}
