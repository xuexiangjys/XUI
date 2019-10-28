package com.xuexiang.xuidemo.fragment.components.statelayout;

import android.view.View;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.statelayout.CustomStateOptions;
import com.xuexiang.xui.widget.statelayout.StatefulLayout;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.XToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 有现成的模版使用
 *
 * @author xuexiang
 * @since 2018/11/26 上午12:25
 */
@Page(name = "StatefulLayout\n定制模版")
public class StatefulLayoutFragment extends BaseFragment {
    @BindView(R.id.ll_stateful)
    StatefulLayout mStatefulLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_statefullayout;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initListeners() {

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            XToastUtils.toast("点击重试按钮!");
        }
    };

    @OnClick(R.id.btn_content)
    public void content(View view) {
        mStatefulLayout.showContent();
    }

    @OnClick(R.id.btn_loading)
    public void loading(View view) {
        mStatefulLayout.showLoading();
        //mStatefulLayout.showLoading(R.string.testMessage);
        //mStatefulLayout.showLoading(getString(R.string.testMessage));
    }

    @OnClick(R.id.btn_empty)
    public void empty(View view) {
        mStatefulLayout.showEmpty();
        //mStatefulLayout.showEmpty(R.string.testMessage);
        //mStatefulLayout.showEmpty(getString(R.string.testMessage));
    }

    @OnClick(R.id.btn_error)
    public void error(View view) {
        mStatefulLayout.showError(clickListener);
        //mStatefulLayout.showError(R.string.testMessage, clickListener);
        //mStatefulLayout.showError(getString(R.string.testMessage), clickListener);
    }

    @OnClick(R.id.btn_offline)
    public void offline(View view) {
        mStatefulLayout.showOffline(clickListener);
        //mStatefulLayout.showOffline(R.string.testMessage, clickListener);
        //mStatefulLayout.showOffline(getString(R.string.testMessage), clickListener);
    }

    @OnClick(R.id.btn_locationOff)
    public void locationOff(View view) {
        mStatefulLayout.showLocationOff(clickListener);
        //mStatefulLayout.showLocationOff(R.string.testMessage, clickListener);
        //mStatefulLayout.showLocationOff(getString(R.string.testMessage), clickListener);
    }

    @OnClick(R.id.btn_custom)
    public void custom(View view) {
        //mStatefulLayout.showCustom(new CustomStateOptions());
        //mStatefulLayout.showCustom(new CustomStateOptions().image(R.drawable.ic_bluetooth_disabled_black_24dp));
        //mStatefulLayout.showCustom(new CustomStateOptions().image(R.drawable.ic_bluetooth_disabled_black_24dp).message("please open bluetooth"));
        //mStatefulLayout.showCustom(new CustomStateOptions().message("hey yow!"));
        //mStatefulLayout.showCustom(new CustomStateOptions().message("hey yow!").buttonAction(clickListener));
        mStatefulLayout.showCustom(new CustomStateOptions()
                .image(R.drawable.ic_bluetooth_disabled_black_24dp)
                .message("请打开蓝牙")
                .buttonText("设置")
                .buttonClickListener(clickListener));
    }
}
