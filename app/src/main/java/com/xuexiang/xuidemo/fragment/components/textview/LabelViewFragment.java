package com.xuexiang.xuidemo.fragment.components.textview;

import android.view.View;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.textview.label.LabelButtonView;
import com.xuexiang.xui.widget.textview.label.LabelImageView;
import com.xuexiang.xui.widget.textview.label.LabelTextView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2018/12/3 上午12:54
 */
@Page(name = "LabelView\n标签")
public class LabelViewFragment extends BaseFragment {


    @BindView(R.id.btn_label)
    LabelButtonView btnLabel;
    @BindView(R.id.iv_label)
    LabelImageView ivLabel;
    @BindView(R.id.tv_label)
    LabelTextView tvLabel;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_label_view;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {

    }

    @OnClick({R.id.btn_label, R.id.iv_label, R.id.tv_label})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_label:
                btnLabel.setLabelVisual(!btnLabel.isLabelVisual());
                break;
            case R.id.iv_label:
                ivLabel.setLabelDistance((int) (Math.random() * 20 + 30));
                break;
            case R.id.tv_label:
                tvLabel.setLabelOrientation((int) (Math.random() * 4 + 1));
                break;
            default:
                break;
        }
    }
}
