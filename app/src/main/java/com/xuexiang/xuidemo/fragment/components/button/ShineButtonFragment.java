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

package com.xuexiang.xuidemo.fragment.components.button;

import android.view.View;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.button.shinebutton.ShineButton;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.XToastUtils;
import com.xuexiang.xuidemo.widget.ShineButtonDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2020-01-06 17:24
 */
@Page(name = "ShineButton\n增强效果的按钮，自带闪烁的特效")
public class ShineButtonFragment extends BaseFragment implements ShineButton.OnCheckedChangeListener {
    @BindView(R.id.shine_button_1)
    ShineButton shineButton1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shine_button;
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initListeners() {
        shineButton1.setOnCheckStateChangeListener(this);
    }

    @SingleClick
    @OnClick(R.id.btn_dialog)
    public void onViewClicked(View view) {
        new ShineButtonDialog(getContext()).show();
    }

    @Override
    public void onCheckedChanged(ShineButton shineButton, boolean isChecked) {
        XToastUtils.toast("checked:" + isChecked);
    }
}
