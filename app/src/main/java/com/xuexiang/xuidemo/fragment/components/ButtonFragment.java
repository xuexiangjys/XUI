package com.xuexiang.xuidemo.fragment.components;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageContainerListFragment;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.fragment.components.button.ButtonStyleFragment;
import com.xuexiang.xuidemo.fragment.components.button.ButtonViewFragment;
import com.xuexiang.xuidemo.fragment.components.button.RoundButtonFragment;
import com.xuexiang.xuidemo.fragment.components.button.ShadowButtonFragment;
import com.xuexiang.xuidemo.fragment.components.button.SwitchButtonFragment;

/**
 * @author XUE
 * @date 2017/9/13 16:03
 */
@Page(name = "按钮", extra = R.drawable.ic_widget_button)
public class ButtonFragment extends XPageContainerListFragment {

    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                ButtonStyleFragment.class,
                ShadowButtonFragment.class,
                RoundButtonFragment.class,
                ButtonViewFragment.class,
                SwitchButtonFragment.class
        };
    }
}
