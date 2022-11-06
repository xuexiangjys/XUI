package com.xuexiang.xuidemo.fragment.components.textview;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.textview.ExpandableTextView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xui.utils.XToastUtils;

import butterknife.BindView;

/**
 * @author xuexiang
 * @date 2017/10/27 下午3:33
 */
@Page(name = "可伸缩折叠的TextView")
public class ExpandableTextViewFragment extends BaseFragment {
    @BindView(R.id.expand_text_view)
    ExpandableTextView mExpandableTextView;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_expandabletextview;
    }

    @Override
    protected void initViews() {
        mExpandableTextView.setText(getString(R.string.etv_content_demo1));
        mExpandableTextView.setOnExpandStateChangeListener((textView, isExpanded) -> XToastUtils.toast(isExpanded ? "Expanded" : "Collapsed"));

    }

    @Override
    protected void initListeners() {

    }
}
