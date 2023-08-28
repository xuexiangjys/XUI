package com.xuexiang.xuidemo.fragment.components.button;

import android.widget.FrameLayout;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;

/**
 * @author XUE
 * @date 2017/9/20 18:42
 */
@Page(name = "ButtonView\n通用按钮")
public class ButtonViewFragment extends BaseFragment {

    @BindView(R.id.button_container)
    FrameLayout buttonContainer;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_buttonview;
    }

    @Override
    protected void initViews() {
        buttonContainer.addView(createButtonView());
    }

    private ButtonView createButtonView() {
        ButtonView buttonView = new ButtonView(getContext());
        buttonView.setText("动态按钮");
        buttonView.setPadding(40, 20, 40, 20);
        return buttonView;
    }

    @Override
    protected void initListeners() {

    }
}
