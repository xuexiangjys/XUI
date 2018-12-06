package com.xuexiang.xui.widget.popupwindow.easypopup;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;
/**
 * 基础自定义弹出框
 *
 * @author xuexiang
 * @since 2018/12/6 下午2:48
 */
public abstract class BaseCustomPopup extends EasyPopup {

    protected BaseCustomPopup(Context context) {
        super(context);
    }

    @Override
    public void onPopupWindowCreated(PopupWindow popupWindow) {
        super.onPopupWindowCreated(popupWindow);
        //执行设置PopupWindow属性也可以通过Builder中设置
        //setContentView(x,x,x);
        //...
        initAttributes();
    }

    @Override
    public void onPopupWindowViewCreated(View contentView) {
        initViews(contentView);
    }

    @Override
    public void onPopupWindowDismiss() {

    }

    /**
     * 可以在此方法中设置PopupWindow需要的属性
     */
    protected abstract void initAttributes();

    /**
     * 初始化view
     *
     * @param view
     */
    protected abstract void initViews(View view);


}
