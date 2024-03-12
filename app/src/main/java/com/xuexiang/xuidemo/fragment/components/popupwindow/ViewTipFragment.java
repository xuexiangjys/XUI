package com.xuexiang.xuidemo.fragment.components.popupwindow;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.popupwindow.ViewTooltip;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @date 2017/10/29 下午11:13
 */
@Page(name = "控件提示工具")
public class ViewTipFragment extends BaseFragment {
    @BindView(R.id.editText)
    EditText mEditText;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_viewtip;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initListeners() {

    }

    @OnClick(R.id.btn_left)
    public void tipOnLeft(View view) {
        ViewTooltip
                .on(mEditText)
                .color(Color.BLACK)
                .position(ViewTooltip.Position.LEFT)
                .text("Some tooltip with long text")
                .clickToHide(true)
                .autoHide(false, 0)
                .animation(new ViewTooltip.FadeTooltipAnimation(500))
                .onDisplay(view12 -> XToastUtils.toast("onDisplay"))
                .onHide(view1 -> XToastUtils.toast("onHide"))
                .show();
    }

    @OnClick(R.id.btn_right)
    public void tipOnRight(View view) {
        ViewTooltip
                .on(mEditText)
                .autoHide(true, 1000)
                .position(ViewTooltip.Position.RIGHT)
                .text("Some tooltip with long text")
                .show();
    }

    @OnClick(R.id.btn_top)
    public void tipOnTop(View view) {
        ViewTooltip
                .on(mEditText)
                .position(ViewTooltip.Position.TOP)
                .text("Popup可以设置其位置以及显示和隐藏的动画")
                .show();
    }


    @OnClick(R.id.btn_bottom)
    public void tipOnBotton(View view) {
        ViewTooltip
                .on(mEditText)
                .color(Color.BLACK)
//                .padding(20, 20, 20, 20)
                .position(ViewTooltip.Position.BOTTOM)
                .align(ViewTooltip.ALIGN.START)
                .text("abcdefg")
                .show();
    }

    @OnClick(R.id.bottomRight)
    public void tipOnBottomRight(View view) {
        ViewTooltip
                .on(view)
                .position(ViewTooltip.Position.TOP)
                .text("bottomRight bottomRight bottomRight")
                .show();
    }

    @OnClick(R.id.bottomLeft)
    public void tipOnBottomLeft(View view) {
        ViewTooltip
                .on(view)
                .position(ViewTooltip.Position.TOP)
                .text("bottomLeft bottomLeft bottomLeft")
                .show();
    }
}
