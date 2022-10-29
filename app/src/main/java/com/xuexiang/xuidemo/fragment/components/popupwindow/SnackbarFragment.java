/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.xuidemo.fragment.components.popupwindow;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xui.utils.XToastUtils;

import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2018/12/18 上午12:31
 */
@Page(name = "Snackbar\n使用详解")
public class SnackbarFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_util_snack_bar;
    }

    @Override
    protected void initViews() {

    }

    /**
     * 显示时长
     *
     * @param view
     */
    @OnClick({R.id.btn_short, R.id.btn_long, R.id.btn_indefinite, R.id.btn_length_custom})
    public void onClickDuring(View view) {
        switch (view.getId()) {
            case R.id.btn_short:
                SnackbarUtils.Short(view, "显示时长:短 + info").info().show();
                break;
            case R.id.btn_long:
                SnackbarUtils.Long(view, "显示时长:长 + info").info().show();
                break;
            case R.id.btn_indefinite:
                SnackbarUtils.Indefinite(view, "显示时长:无限 + info").info()
                        .actionColor(ResUtils.getColor(getContext(), R.color.xui_config_color_white))
                        .setAction("确定", v -> XToastUtils.toast("点击了确定！")).show();
                break;
            case R.id.btn_length_custom:
                SnackbarUtils.Custom(view, "显示时长:自定义 3秒 + info", 3 * 1000)
                        .info()
                        .show();
                break;
            default:
                break;
        }
    }

    /**
     * 背景样式
     *
     * @param view
     */
    @OnClick({R.id.btn_info, R.id.btn_confirm, R.id.btn_warn, R.id.btn_danger})
    public void onClickColorStyle(View view) {
        switch (view.getId()) {
            case R.id.btn_info:
                SnackbarUtils.Short(view, "背景样式:info")
                        .info().show();
                break;
            case R.id.btn_confirm:
                SnackbarUtils.Short(view, "背景样式:confirm")
                        .confirm().show();
                break;
            case R.id.btn_warn:
                SnackbarUtils.Short(view, "背景样式:warning")
                        .warning().show();
                break;
            case R.id.btn_danger:
                SnackbarUtils.Short(view, "背景样式:danger")
                        .danger().show();
                break;
            default:
                break;
        }
    }

    /**
     * 颜色设置
     *
     * @param view
     */
    @OnClick({R.id.btn_background_color, R.id.btn_message_color, R.id.btn_action_color})
    public void onClickColorSetting(View view) {
        switch (view.getId()) {
            case R.id.btn_background_color:
                SnackbarUtils.Short(view, "设置背景颜色: 绿色")
                        .backColor(Color.GREEN)
                        .show();
                break;
            case R.id.btn_message_color:
                SnackbarUtils.Short(view, "设置提示文字的颜色:红色")
                        .messageColor(Color.RED)
                        .show();
                break;
            case R.id.btn_action_color:
                SnackbarUtils.Short(view, "设置按钮文字的颜色:白色")
                        .actionColor(Color.WHITE)
                        .setAction("确定", v -> {

                        })
                        .show();
                break;
            default:
                break;
        }
    }

    /**
     * 监听设置
     *
     * @param view
     */
    @OnClick({R.id.btn_action, R.id.btn_callback})
    public void onClickSettingListener(View view) {
        switch (view.getId()) {
            case R.id.btn_action:
                SnackbarUtils.Long(view, "设置按钮文字及点击监听")
                        .setAction("按钮文字", v -> XToastUtils.toast("点击了按钮!")).show();
                break;
            case R.id.btn_callback:
                SnackbarUtils.Short(view, "设置显示及隐藏监听")
                        .setCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                                XToastUtils.toast("onDismissed!");
                            }

                            @Override
                            public void onShown(Snackbar snackbar) {
                                super.onShown(snackbar);
                                XToastUtils.toast("onShown!");
                            }
                        }).show();
                break;
            default:
                break;
        }
    }

    /**
     * 文字对齐方式
     *
     * @param view
     */
    @OnClick({R.id.btn_default, R.id.btn_center, R.id.btn_right})
    public void onClickTextGravity(View view) {
        switch (view.getId()) {
            case R.id.btn_default:
                SnackbarUtils.Short(view, "文字位置:默认").show();
                break;
            case R.id.btn_center:
                SnackbarUtils.Short(view, "文字位置:居中").messageCenter().show();
                break;
            case R.id.btn_right:
                SnackbarUtils.Short(view, "文字位置:居右").messageRight().show();
                break;
            default:
                break;
        }
    }


    /**
     * 文字对齐方式
     *
     * @param view
     */
    @OnClick({R.id.btn_gravity_bottom, R.id.btn_gravity_center, R.id.btn_gravity_top})
    public void onClickSnackbarGravity(View view) {
        switch (view.getId()) {
            case R.id.btn_gravity_bottom:
                SnackbarUtils.Short(view, "Snackbar位置:默认").show();
                break;
            case R.id.btn_gravity_center:
                SnackbarUtils.Short(view, "Snackbar位置:居中")
                        .gravityFrameLayout(Gravity.CENTER).show();
                break;
            case R.id.btn_gravity_top:
                SnackbarUtils.Short(view, "Snackbar位置:顶部")
                        .gravityFrameLayout(Gravity.TOP).show();
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.btn_message_left_right_drawable, R.id.btn_add_view, R.id.btn_margin, R.id.btn_radius_stroke})
    public void onClickOther(View view) {
        switch (view.getId()) {
            case R.id.btn_message_left_right_drawable:
                SnackbarUtils.Short(view, "设置文字位置左右两侧图片")
                        .leftAndRightDrawable(R.drawable.icon_avatar1, R.drawable.icon_avatar2).show();
                break;
            case R.id.btn_add_view:
                ImageView imageView = new ImageView(getContext());
                imageView.setImageResource(R.mipmap.ic_launcher);
                imageView.setPadding(10, 10, 10, 10);
                SnackbarUtils.Short(view, "向Snackbar布局中添加View")
                        .addView(imageView, 0)
                        .show();
                break;
            case R.id.btn_margin:
                SnackbarUtils.Short(view, "设置Snackbar布局的外边距").margins(16).show();
                break;
            case R.id.btn_radius_stroke:
                SnackbarUtils.Short(view, "设置圆角半径值及边框")
                        .radius(30, 4, Color.GREEN)
                        .show();
                break;
            default:
                break;
        }
    }

}
