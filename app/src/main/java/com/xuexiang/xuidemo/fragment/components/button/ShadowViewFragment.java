package com.xuexiang.xuidemo.fragment.components.button;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

/**
 * 可设置阴影效果的控件
 *
 * @author xuexiang
 * @since 2019/3/31 下午6:43
 */
@Page(name = "ShadowView\n可设置阴影效果的控件")
public class ShadowViewFragment extends BaseFragment {
    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shadow_view;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {

    }
}
