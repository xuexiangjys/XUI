package com.xuexiang.xuidemo.fragment.components.textview;

import android.widget.TextView;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.textview.ExpandableTextView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xutil.tip.ToastUtils;

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
        mExpandableTextView.setOnExpandStateChangeListener(new ExpandableTextView.OnExpandStateChangeListener() {
            @Override
            public void onExpandStateChanged(TextView textView, boolean isExpanded) {
                ToastUtils.toast(isExpanded ? "Expanded" : "Collapsed");
            }
        });

    }

    @Override
    protected void initListeners() {

    }
}
