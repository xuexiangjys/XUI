package com.xuexiang.xuidemo.fragment.components.refresh;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.components.refresh.smartrefresh.RefreshBasicFragment;
import com.xuexiang.xuidemo.fragment.components.refresh.smartrefresh.RefreshStatusLayoutFragment;
import com.xuexiang.xuidemo.fragment.components.refresh.smartrefresh.RefreshStyleFragment;

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
}
