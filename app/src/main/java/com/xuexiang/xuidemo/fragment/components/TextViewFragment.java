package com.xuexiang.xuidemo.fragment.components;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.components.textview.AutoFitTextViewFragment;
import com.xuexiang.xuidemo.fragment.components.textview.AutoHyphenationTextViewFragment;
import com.xuexiang.xuidemo.fragment.components.textview.BadgeViewFragment;
import com.xuexiang.xuidemo.fragment.components.textview.ExpandableTextViewFragment;
import com.xuexiang.xuidemo.fragment.components.textview.LabelViewFragment;
import com.xuexiang.xuidemo.fragment.components.textview.LoggerTextViewFragment;
import com.xuexiang.xuidemo.fragment.components.textview.SuperTextViewFragment;

/**
 * TextView
 *
 * @author xuexiang
 * @since 2018/11/28 下午11:42
 */
@Page(name = "文字", extra = R.drawable.ic_widget_textview)
public class TextViewFragment extends ComponentContainerFragment {

    @Override
    public Class[] getPagesClasses() {
        return new Class[]{
                SuperTextViewFragment.class,
                ExpandableTextViewFragment.class,
                LabelViewFragment.class,
                BadgeViewFragment.class,
                AutoFitTextViewFragment.class,
                AutoHyphenationTextViewFragment.class,
                LoggerTextViewFragment.class
        };
    }

}
