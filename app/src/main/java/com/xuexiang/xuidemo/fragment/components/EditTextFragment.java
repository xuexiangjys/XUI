package com.xuexiang.xuidemo.fragment.components;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.components.edittext.CustomEditTextFragment;
import com.xuexiang.xuidemo.fragment.components.edittext.EditTextStyleFragment;
import com.xuexiang.xuidemo.fragment.components.edittext.MaterialEditTextFragment;

/**
 * 输入框组件
 *
 * @author xuexiang
 * @since 2018/11/26 下午5:48
 */
@Page(name = "输入框", extra = R.drawable.ic_widget_edittext)
public class EditTextFragment extends ComponentContainerFragment {

    @Override
    public Class[] getPagesClasses() {
        return new Class[]{
                EditTextStyleFragment.class,
                CustomEditTextFragment.class,
                MaterialEditTextFragment.class
        };
    }
}
