package com.xuexiang.xuidemo.fragment.expands;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.AppBarLayoutFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.ToolBarFragment;

/**
 * @author xuexiang
 * @since 2019-05-07 23:30
 */
@Page(name = "Material Design", extra = R.drawable.ic_expand_material_design)
public class MaterialDesignFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                ToolBarFragment.class,
                AppBarLayoutFragment.class
        };
    }

    /**
     * 条目点击
     *
     * @param position
     */
    @Override
    protected void onItemClick(int position) {
        if (position == 1) {
            openNewPage(getSimpleDataItem(position));
        } else {
            openPage(getSimpleDataItem(position));
        }
    }
}
