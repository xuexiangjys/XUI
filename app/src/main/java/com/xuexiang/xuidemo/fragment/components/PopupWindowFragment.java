package com.xuexiang.xuidemo.fragment.components;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.components.popupwindow.CookieBarFragment;
import com.xuexiang.xuidemo.fragment.components.popupwindow.EasyPopFragment;
import com.xuexiang.xuidemo.fragment.components.popupwindow.PopupWindowStyleFragment;
import com.xuexiang.xuidemo.fragment.components.popupwindow.SnackbarFragment;
import com.xuexiang.xuidemo.fragment.components.popupwindow.ViewTipFragment;
import com.xuexiang.xuidemo.fragment.components.popupwindow.XUIPopupFragment;

/**
 * @author xuexiang
 * @date 2017/10/29 下午7:44
 */
@Page(name = "弹出窗", extra = R.drawable.ic_widget_popupwindow)
public class PopupWindowFragment extends ComponentContainerFragment {

    @Override
    public Class[] getPagesClasses() {
        return new Class[]{
                PopupWindowStyleFragment.class,
                ViewTipFragment.class,
                EasyPopFragment.class,
                XUIPopupFragment.class,
                SnackbarFragment.class,
                CookieBarFragment.class
        };
    }
}
