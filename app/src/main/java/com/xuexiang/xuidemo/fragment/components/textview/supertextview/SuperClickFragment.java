package com.xuexiang.xuidemo.fragment.components.textview.supertextview;

import android.view.Gravity;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.layout.ExpandableLayout;
import com.xuexiang.xui.widget.textview.badge.Badge;
import com.xuexiang.xui.widget.textview.badge.BadgeView;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xui.utils.XToastUtils;

import butterknife.BindView;

/**
 *
 *
 * @author xuexiang
 * @since 2020/5/13 8:32 PM
 */
@Page(name = "SuperTextView点击事件")
public class SuperClickFragment extends BaseFragment {
    @BindView(R.id.super_tv)
    SuperTextView superTextView;
    @BindView(R.id.super_cb_tv)
    SuperTextView superTextView_cb;
    @BindView(R.id.super_switch_tv)
    SuperTextView superTextView_switch;
    @BindView(R.id.super_message_tv)
    SuperTextView stvMessage;
    @BindView(R.id.stv_expandable)
    SuperTextView stvExpandable;
    @BindView(R.id.expandable_layout)
    ExpandableLayout mExpandableLayout;
    @BindView(R.id.stv_name)
    SuperTextView stvName;
    @BindView(R.id.stv_phone)
    SuperTextView stvPhone;

    private Badge mBadge;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_super_click;
    }

    @Override
    protected void initViews() {
        //设置空字符串用于占位
        stvMessage.setRightString("      ");
        mBadge = new BadgeView(getContext()).bindTarget(stvMessage.getRightTextView())
                .setBadgeGravity(Gravity.END | Gravity.CENTER)
                .setBadgePadding(3, true)
                .setBadgeTextSize(9, true)
                .setBadgeNumber(3);

    }

    @Override
    protected void initListeners() {
        /**
         * 根据实际需求对需要的View设置点击事件
         */
        superTextView.setOnSuperTextViewClickListener(superTextView -> XToastUtils.toast("整个item的点击事件")).setLeftTopTvClickListener(textView -> XToastUtils.toast(superTextView.getLeftTopString())).setLeftTvClickListener(textView -> XToastUtils.toast(superTextView.getLeftString())).setLeftBottomTvClickListener(textView -> XToastUtils.toast(superTextView.getLeftBottomString())).setCenterTopTvClickListener(textView -> XToastUtils.toast(superTextView.getCenterTopString())).setCenterTvClickListener(textView -> XToastUtils.toast(superTextView.getCenterString())).setCenterBottomTvClickListener(textView -> XToastUtils.toast(superTextView.getCenterBottomString())).setRightTopTvClickListener(textView -> XToastUtils.toast(superTextView.getRightTopString())).setRightTvClickListener(textView -> XToastUtils.toast(superTextView.getRightString())).setRightBottomTvClickListener(textView -> XToastUtils.toast(superTextView.getRightBottomString())).setLeftImageViewClickListener(imageView -> {
        }).setRightImageViewClickListener(imageView -> {
        });


        superTextView_cb.setOnSuperTextViewClickListener(superTextView -> superTextView.setCheckBoxChecked(!superTextView.getCheckBoxIsChecked())).setCheckBoxCheckedChangeListener((buttonView, isChecked) -> XToastUtils.toast("isChecked : " + isChecked));

        superTextView_switch.setOnSuperTextViewClickListener(superTextView -> superTextView.setSwitchIsChecked(!superTextView.getSwitchIsChecked(), false)).setSwitchCheckedChangeListener((buttonView, isChecked) -> XToastUtils.toast("isChecked : " + isChecked));

        stvMessage.setOnSuperTextViewClickListener(superTextView -> {
            mBadge.hide(true);
            stvName.requestFocus();
        });

        stvName.setCenterEditTextFocusChangeListener((v, hasFocus) -> XToastUtils.toast("聚焦变化：" + hasFocus));
        stvPhone.setCenterEditTextFocusChangeListener((v, hasFocus) -> XToastUtils.toast("聚焦变化：" + hasFocus));

        stvPhone.setCenterEditTextClickListener(v -> XToastUtils.toast("点击监听"));

        mExpandableLayout.setOnExpansionChangedListener((expansion, state) -> {
            if (stvExpandable != null && stvExpandable.getRightIconIV() != null) {
                stvExpandable.getRightIconIV().setRotation(expansion * 90);
            }
        });
        stvExpandable.setOnSuperTextViewClickListener(superTextView -> {
            if (mExpandableLayout != null) {
                mExpandableLayout.toggle();
            }
        });
    }

}
