package com.xuexiang.xuidemo.base;

import android.view.View;
import android.view.ViewGroup;

import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xui.utils.KeyboardUtils;
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
        KeyboardUtils.fixSoftInputLeaks(getContext());
        super.onDestroyView();
    }
}
