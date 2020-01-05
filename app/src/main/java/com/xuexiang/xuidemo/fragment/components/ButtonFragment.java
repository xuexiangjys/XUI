package com.xuexiang.xuidemo.fragment.components;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.components.button.ButtonStyleFragment;
import com.xuexiang.xuidemo.fragment.components.button.ButtonViewFragment;
import com.xuexiang.xuidemo.fragment.components.button.GoodViewFragment;
import com.xuexiang.xuidemo.fragment.components.button.RippleViewFragment;
import com.xuexiang.xuidemo.fragment.components.button.RoundButtonFragment;
import com.xuexiang.xuidemo.fragment.components.button.ShadowButtonFragment;
import com.xuexiang.xuidemo.fragment.components.button.ShadowViewFragment;
import com.xuexiang.xuidemo.fragment.components.button.SwitchButtonFragment;

/**
 * 按钮
 *
 * @author xuexiang
 * @since 2018/11/25 下午11:21
 */
@Page(name = "按钮", extra = R.drawable.ic_widget_button)
public class ButtonFragment extends ComponentContainerFragment {

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
                ShadowViewFragment.class,
                RoundButtonFragment.class,
                ButtonViewFragment.class,
                SwitchButtonFragment.class,
                RippleViewFragment.class,
                GoodViewFragment.class
        };
    }
}
