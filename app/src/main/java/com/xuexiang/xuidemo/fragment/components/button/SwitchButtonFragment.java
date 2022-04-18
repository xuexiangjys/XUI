package com.xuexiang.xuidemo.fragment.components.button;

import android.view.View;
import android.widget.CompoundButton;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.button.switchbutton.SwitchButton;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xui.utils.XToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *
 * @author xuexiang
 * @date 2018/1/16 上午11:01
 */
@Page(name = "SwitchButton\n切换按钮")
public class SwitchButtonFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.sb_default)
    SwitchButton mSBDefault;
    @BindView(R.id.sb_md)
    SwitchButton mSBMD;
    @BindView(R.id.sb_ios)
    SwitchButton mSBIOS;
    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_switch_button;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
    }

    /**
     * 初始化监听
     */
    @Override
    protected void initListeners() {
        mSBDefault.setOnCheckedChangeListener(this);
        mSBMD.setOnCheckedChangeListener(this);
        mSBIOS.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        XToastUtils.toast("isChecked：" + isChecked);
    }

    @OnClick(R.id.btn_switch)
    void onClick(View v) {
        mSBDefault.toggle();
        mSBMD.toggle();
        mSBIOS.toggle();
    }
}
