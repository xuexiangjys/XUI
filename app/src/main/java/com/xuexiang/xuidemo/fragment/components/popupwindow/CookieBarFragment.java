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

import android.view.Gravity;
import android.view.View;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.popupwindow.bar.CookieBar;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xui.utils.XToastUtils;

import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2018/12/19 上午10:06
 */
@Page(name = "CookieBar\n顶部和底部信息显示条")
public class CookieBarFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cookiebar;
    }

    @Override
    protected void initViews() {

    }

    @OnClick({R.id.btn_top, R.id.btn_bottom, R.id.btn_top_with_icon, R.id.btn_custom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_top:
                CookieBar.builder(getActivity())
                        .setTitle(R.string.cookie_title)
                        .setMessage(R.string.cookie_message)
                        .show();
                break;
            case R.id.btn_bottom:
                CookieBar.builder(getActivity())
                        .setTitle(R.string.cookie_title)
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage(R.string.cookie_message)
                        .setLayoutGravity(Gravity.BOTTOM)
                        .setAction(R.string.cookie_action, view13 -> XToastUtils.toast("点击消失！"))
                        .show();
                break;
            case R.id.btn_top_with_icon:
                CookieBar.builder(getActivity())
                        .setMessage(R.string.cookie_message)
                        .setDuration(-1)
                        .setActionWithIcon(R.drawable.ic_action_close, view12 -> XToastUtils.toast("点击消失！"))
                        .show();
                break;
            case R.id.btn_custom:
                CookieBar.builder(getActivity())
                        .setTitle(R.string.cookie_title)
                        .setMessage(R.string.cookie_message)
                        .setDuration(3000)
                        .setBackgroundColor(R.color.colorPrimary)
                        .setActionColor(android.R.color.white)
                        .setTitleColor(android.R.color.white)
                        .setAction(R.string.cookie_action, view1 -> XToastUtils.toast("点击消失！"))
                        .show();
                break;
            default:
                break;
        }
    }
}
