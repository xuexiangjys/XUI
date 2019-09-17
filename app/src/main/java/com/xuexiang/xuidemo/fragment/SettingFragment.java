/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment;

import android.widget.CompoundButton;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.SettingSPUtils;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019-09-17 17:51
 */
@Page(name = "设置")
public class SettingFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.stv_switch_custom_theme)
    SuperTextView stvSwitchCustomTheme;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        stvSwitchCustomTheme.setSwitchIsChecked(SettingSPUtils.getInstance().isUseCustomTheme());
        stvSwitchCustomTheme.setSwitchCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SettingSPUtils.getInstance().setIsUseCustomTheme(isChecked);
    }
}
