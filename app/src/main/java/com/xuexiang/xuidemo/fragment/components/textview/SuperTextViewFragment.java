package com.xuexiang.xuidemo.fragment.components.textview;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.components.textview.supertextview.SuperButtonFragment;
import com.xuexiang.xuidemo.fragment.components.textview.supertextview.SuperClickFragment;
import com.xuexiang.xuidemo.fragment.components.textview.supertextview.SuperNetPictureLoadingFragment;
import com.xuexiang.xuidemo.fragment.components.textview.supertextview.SuperTextCommonUseFragment;

/**
 * SuperTextView演示
 *
 * @author xuexiang
 * @since 2018/11/29 上午12:29
 */
@Page(name = "SuperTextView")
public class SuperTextViewFragment extends ComponentContainerFragment {
    @Override
    public Class[] getPagesClasses() {
        return new Class[]{
                SuperClickFragment.class,
                SuperButtonFragment.class,
                SuperTextCommonUseFragment.class,
                SuperNetPictureLoadingFragment.class
        };
    }

}
