package com.xuexiang.xuidemo.fragment.components.refresh;

import android.view.View;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.components.refresh.smartrefresh.RefreshBasicFragment;
import com.xuexiang.xuidemo.fragment.components.refresh.smartrefresh.RefreshStatusLayoutFragment;
import com.xuexiang.xuidemo.fragment.components.refresh.smartrefresh.RefreshStyleFragment;
import com.xuexiang.xuidemo.utils.Utils;

/**
 * @author XUE
 * @since 2019/4/1 9:47
 */
@Page(name = "SmartRefreshLayout\nAndroid智能下拉刷新框架")
public class SmartRefreshLayoutFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[] {
                RefreshBasicFragment.class,
                RefreshStatusLayoutFragment.class,
                RefreshStyleFragment.class,
                SwipeRefreshLayoutFragment.class
        };
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("Github") {
            @Override
            public void performAction(View view) {
                Utils.goWeb(getContext(), "https://github.com/scwang90/SmartRefreshLayout");
            }
        });
        return titleBar;
    }
}
