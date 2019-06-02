package com.xuexiang.xuidemo.fragment.components;

import com.xuexiang.xuidemo.fragment.components.progress.ArcLoadingViewFragment;
import com.xuexiang.xuidemo.fragment.components.progress.BeautifulProgressBarFragment;
import com.xuexiang.xuidemo.fragment.components.progress.DeterminateCircularFragment;
import com.xuexiang.xuidemo.fragment.components.progress.MaterialProgressBarFragment;
import com.xuexiang.xuidemo.fragment.components.progress.RatingBarFragment;
import com.xuexiang.xuidemo.fragment.components.progress.RotateLoadingViewFragment;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;

/**
 * @author xuexiang
 * @since 2018/11/26 下午1:47
 */
@Page(name = "进度条", extra = R.drawable.ic_widget_loading)
public class ProgressBarFragment extends ComponentContainerFragment {
    @Override
    public Class[] getPagesClasses() {
        return new Class[]{
                ArcLoadingViewFragment.class,
                RotateLoadingViewFragment.class,
                MaterialProgressBarFragment.class,
                DeterminateCircularFragment.class,
                RatingBarFragment.class,
                BeautifulProgressBarFragment.class
        };
    }
}
