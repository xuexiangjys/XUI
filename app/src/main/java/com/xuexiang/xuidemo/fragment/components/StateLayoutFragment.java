package com.xuexiang.xuidemo.fragment.components;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.components.statelayout.MultipleStatusViewFragment;
import com.xuexiang.xuidemo.fragment.components.statelayout.StatefulLayoutFragment;
import com.xuexiang.xuidemo.fragment.components.statelayout.StatusLoaderFragment;
import com.xuexiang.xuidemo.fragment.components.statelayout.StatusViewFragment;

/**
 * 状态切换
 *
 * @author xuexiang
 * @since 2018/11/26 上午12:27
 */
@Page(name = "状态切换", extra = R.drawable.ic_widget_statelayout)
public class StateLayoutFragment extends ComponentContainerFragment {

    @Override
    public Class[] getPagesClasses() {
        return new Class[] {
                StatefulLayoutFragment.class,
                MultipleStatusViewFragment.class,
                StatusViewFragment.class,
                StatusLoaderFragment.class
        };
    }
}
