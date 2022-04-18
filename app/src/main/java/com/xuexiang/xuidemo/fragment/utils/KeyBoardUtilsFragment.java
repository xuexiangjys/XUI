/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xuidemo.fragment.utils;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.button.switchbutton.SwitchButton;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xui.utils.XToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2020/12/15 12:29 AM
 */
@Page(name = "KeyBoardUtils", extra = R.drawable.ic_util_keyboard)
public class KeyBoardUtilsFragment extends BaseFragment implements KeyboardUtils.SoftKeyboardToggleListener {

    @BindView(R.id.switch_status)
    SwitchButton switchStatus;
    @BindView(R.id.btn_show_soft)
    Button btnShowSoft;
    @BindView(R.id.btn_hide_soft)
    Button btnHideSoft;
    @BindView(R.id.et_1)
    EditText et1;
    @BindView(R.id.et_2)
    EditText et2;

    private boolean isFullScreen;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_util_keyboard;
    }

    @Override
    protected void initViews() {
        switchStatus.setClickable(false);

    }

    @Override
    protected void initListeners() {
        KeyboardUtils.addKeyboardToggleListener(getActivity(), this);
    }

    @Override
    public void onToggleSoftKeyboard(boolean isVisible) {
        if (switchStatus != null) {
            switchStatus.setChecked(isVisible);
        }
    }

    @SingleClick
    @OnClick({R.id.btn_get_status, R.id.btn_switch_screen, R.id.btn_show_soft, R.id.btn_hide_soft})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_status:
                XToastUtils.toast("键盘显示状态:" + KeyboardUtils.isSoftInputShow(getActivity()));
                break;
            case R.id.btn_switch_screen:
                if (isFullScreen) {
                    StatusBarUtils.cancelFullScreen(getActivity());
                } else {
                    StatusBarUtils.fullScreen(getActivity());
                }
                isFullScreen = !isFullScreen;
                break;
            case R.id.btn_show_soft:
                KeyboardUtils.showSoftInputForce(getActivity());
                break;
            case R.id.btn_hide_soft:
                KeyboardUtils.hideSoftInput(view);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onTouchDownAction(MotionEvent ev) {
        // 不处理隐藏键盘的操作
    }

    private EditText getFocusEditText() {
        if (getActivity() != null) {
            View view = getActivity().getCurrentFocus();
            if (view instanceof EditText) {
                return (EditText) view;
            }
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        KeyboardUtils.removeKeyboardToggleListener(this);
        super.onDestroyView();
    }

}
