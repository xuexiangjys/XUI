package com.xuexiang.xuidemo.fragment.components.textview.supertextview;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2018/11/29 上午12:09
 */
@Page(name = "使用SuperTextView写一个用户中心界面\n仿苹果界面拖动回弹效果")
public class SuperTextCommonUseFragment extends BaseFragment {

    @BindView(R.id.stv_notification)
    SuperTextView stvNotification;

    private boolean mIsSelected;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_supertextview_common_use;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initListeners() {
        stvNotification.setOnSuperTextViewClickListener(superTextView -> {
            mIsSelected = !mIsSelected;
            int color = ResUtils.getColor(getContext(), mIsSelected ? R.color.app_color_theme_1 :  R.color.xui_config_color_separator_light_phone);
            stvNotification.setDividerLineColor(color);
        });
    }
}
