package com.xuexiang.xuidemo.fragment.components;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.components.guideview.GuideCaseViewQueueFragment;
import com.xuexiang.xuidemo.fragment.components.guideview.GuideCaseViewStyleFragment;
import com.xuexiang.xuidemo.fragment.components.guideview.SplashFragment;

/**
 * 引导页
 *
 * @author xuexiang
 * @since 2018/11/30 上午12:57
 */
@Page(name = "引导页", extra = R.drawable.ic_widget_guideview)
public class GuideViewFragment extends ComponentContainerFragment {

    @Override
    public Class[] getPagesClasses() {
        return new Class[]{
                GuideCaseViewQueueFragment.class,
                GuideCaseViewStyleFragment.class,
                SplashFragment.class
        };
    }
}

