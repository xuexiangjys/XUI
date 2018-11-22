package com.xuexiang.xuidemo.fragment.components;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.components.flowlayout.FlowTagLayoutFragment;
import com.xuexiang.xuidemo.fragment.components.flowlayout.NormalFlowLayoutFragment;

/**
 * @author xuexiang
 * @date 2017/11/20 下午4:08
 */
@Page(name = "流布局", extra = R.drawable.ic_widget_flowlayout)
public class FlowLayoutFragment extends ComponentContainerFragment {

    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                FlowTagLayoutFragment.class,
                NormalFlowLayoutFragment.class
        };
    }
}
