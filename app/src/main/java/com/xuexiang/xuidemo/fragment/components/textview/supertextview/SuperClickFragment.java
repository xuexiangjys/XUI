package com.xuexiang.xuidemo.fragment.components.textview.supertextview;

import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.textview.badge.Badge;
import com.xuexiang.xui.widget.textview.badge.BadgeView;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xutil.tip.ToastUtils;

import butterknife.BindView;

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
    @BindView(R.id.stv_name)
    SuperTextView stvName;
    @BindView(R.id.stv_phone)
    SuperTextView stvPhone;

    Badge mBadge;

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
        superTextView.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                ToastUtils.toast("整个item的点击事件");
            }
        }).setLeftTopTvClickListener(new SuperTextView.OnLeftTopTvClickListener() {
            @Override
            public void onClickListener() {
                ToastUtils.toast(superTextView.getLeftTopString());
            }
        }).setLeftTvClickListener(new SuperTextView.OnLeftTvClickListener() {
            @Override
            public void onClickListener() {
                ToastUtils.toast(superTextView.getLeftString());
            }
        }).setLeftBottomTvClickListener(new SuperTextView.OnLeftBottomTvClickListener() {
            @Override
            public void onClickListener() {
                ToastUtils.toast(superTextView.getLeftBottomString());
            }
        }).setCenterTopTvClickListener(new SuperTextView.OnCenterTopTvClickListener() {
            @Override
            public void onClickListener() {
                ToastUtils.toast(superTextView.getCenterTopString());
            }
        }).setCenterTvClickListener(new SuperTextView.OnCenterTvClickListener() {
            @Override
            public void onClickListener() {
                ToastUtils.toast(superTextView.getCenterString());
            }
        }).setCenterBottomTvClickListener(new SuperTextView.OnCenterBottomTvClickListener() {
            @Override
            public void onClickListener() {
                ToastUtils.toast(superTextView.getCenterBottomString());
            }
        }).setRightTopTvClickListener(new SuperTextView.OnRightTopTvClickListener() {
            @Override
            public void onClickListener() {
                ToastUtils.toast(superTextView.getRightTopString());
            }
        }).setRightTvClickListener(new SuperTextView.OnRightTvClickListener() {
            @Override
            public void onClickListener() {
                ToastUtils.toast(superTextView.getRightString());
            }
        }).setRightBottomTvClickListener(new SuperTextView.OnRightBottomTvClickListener() {
            @Override
            public void onClickListener() {
                ToastUtils.toast(superTextView.getRightBottomString());
            }
        }).setLeftImageViewClickListener(new SuperTextView.OnLeftImageViewClickListener() {
            @Override
            public void onClickListener(ImageView imageView) {
            }
        }).setRightImageViewClickListener(new SuperTextView.OnRightImageViewClickListener() {
            @Override
            public void onClickListener(ImageView imageView) {
            }
        });


        superTextView_cb.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                superTextView.setCheckBoxChecked(!superTextView.getCheckBoxIsChecked());
            }
        }).setCheckBoxCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ToastUtils.toast("isChecked : " + isChecked);
            }
        });

        superTextView_switch.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                superTextView.setSwitchIsChecked(!superTextView.getSwitchIsChecked());
            }
        }).setSwitchCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ToastUtils.toast("isChecked : " + isChecked);
            }
        });

        stvMessage.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                mBadge.hide(true);
            }
        });

        stvName.setCenterEditTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ToastUtils.toast("聚焦变化：" + hasFocus);
            }
        });

        stvPhone.setCenterEditTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.toast("点击监听");
            }
        });
    }

}
