package com.xuexiang.xuidemo.base;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.actionbar.TitleUtils;

/**
 * @author xuexiang
 * @since 2018/5/25 下午3:44
 */
public abstract class BaseFragment extends XPageFragment {

    @Override
    protected void initPage() {
        initTitle();
        initViews();
        initListeners();
    }

    protected TitleBar initTitle() {
        return TitleUtils.addTitleBarDynamic((ViewGroup) getRootView(), getPageTitle(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popToBack();
            }
        });
    }

    @Override
    protected void initListeners() {

    }

    @Override
    public void onDestroyView() {
//        KeyboardUtils.fixSoftInputLeaks(getContext());
        super.onDestroyView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig); //屏幕旋转时刷新一下title
        ((ViewGroup) getRootView()).removeViewAt(0);
        initTitle();
    }
}
