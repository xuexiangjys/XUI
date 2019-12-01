package com.xuexiang.xuidemo.fragment.components;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.components.spinner.DropDownMenuFragment;
import com.xuexiang.xuidemo.fragment.components.spinner.SpinnerStyleFragment;

/**
 *
 *
 * @author xuexiang
 * @since 2018/11/26 下午3:19
 */
@Page(name = "下拉框", extra = R.drawable.ic_widget_spinner)
public class SpinnerFragment extends ComponentContainerFragment {

    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                SpinnerStyleFragment.class,
                DropDownMenuFragment.class
        };
    }
}
