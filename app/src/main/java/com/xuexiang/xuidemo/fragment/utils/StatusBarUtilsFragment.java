package com.xuexiang.xuidemo.fragment.utils;

import android.view.View;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.grouplist.XUIGroupListView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.activity.TranslucentActivity;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019/4/8 下午11:32
 */
@Page(name = "StatusBarUtils", extra = R.drawable.ic_util_status_bar)
public class StatusBarUtilsFragment extends BaseFragment {

    @BindView(R.id.groupListView)
    XUIGroupListView groupListView;

    boolean isFullScreen;
    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_util_statusbar;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        XUIGroupListView.newSection(getContext())
                .setDescription("支持 4.4 以上版本的 MIUI 和 Flyme，以及 5.0 以上版本的其他 Android")
                .addItemView(groupListView.createItemView("沉浸式状态栏"), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityUtils.startActivity(TranslucentActivity.class);
                    }
                })
                .addTo(groupListView);

        XUIGroupListView.newSection(getContext())
                .setDescription("支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android")
                .addItemView(groupListView.createItemView("设置状态栏黑色字体与图标"), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StatusBarUtils.setStatusBarLightMode(getActivity());
                    }
                })
                .addItemView(groupListView.createItemView("设置状态栏白色字体与图标"), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StatusBarUtils.setStatusBarDarkMode(getActivity());
                    }
                })
                .addTo(groupListView);

        XUIGroupListView.newSection(getContext())
                .setDescription("不同机型下状态栏高度可能略有差异，并不是固定值，可以通过这个方法获取实际高度")
                .addItemView(groupListView.createItemView("获取状态栏的实际高度"), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.toast("状态栏的实际高度：" + StatusBarUtils.getStatusBarHeight(getContext()));
                    }
                })
                .addTo(groupListView);

        XUIGroupListView.newSection(getContext())
                .addItemView(groupListView.createItemView("切换全屏"), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFullScreen) {
                            StatusBarUtils.cancelFullScreen(getActivity());
                        } else {
                            StatusBarUtils.fullScreen(getActivity());
                        }
                        isFullScreen = !isFullScreen;
                    }
                })
                .addTo(groupListView);
    }

}
