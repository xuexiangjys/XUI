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

import android.content.Intent;

import com.xuexiang.xaop.cache.XMemoryCache;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.activity.MainActivity;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.SettingSPUtils;
import com.xuexiang.xutil.app.AppUtils;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019-09-17 17:51
 */
@Page(name = "设置")
public class SettingFragment extends BaseFragment {
    @BindView(R.id.stv_switch_custom_theme)
    SuperTextView stvSwitchCustomTheme;
    @BindView(R.id.stv_switch_custom_font)
    SuperTextView stvSwitchCustomFont;

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
        stvSwitchCustomTheme.setOnSuperTextViewClickListener(superTextView -> stvSwitchCustomTheme.setSwitchIsChecked(!stvSwitchCustomTheme.getSwitchIsChecked(), false));
        stvSwitchCustomTheme.setSwitchCheckedChangeListener((buttonView, isChecked) -> {
            SettingSPUtils.getInstance().setIsUseCustomTheme(isChecked);
            XMemoryCache.getInstance().clear();
            popToBack();
            //重启主页面
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        stvSwitchCustomFont.setSwitchIsChecked(SettingSPUtils.getInstance().isUseCustomFont());
        stvSwitchCustomFont.setOnSuperTextViewClickListener(superTextView -> stvSwitchCustomFont.setSwitchIsChecked(!stvSwitchCustomFont.getSwitchIsChecked(), false));
        stvSwitchCustomFont.setSwitchCheckedChangeListener((buttonView, isChecked) -> {
            DialogLoader.getInstance().showTipDialog(getContext(), -1, "切换字体", "切换字体需重启App后生效, 点击“重启”应用将自动重启！", "重启", (dialog, which) -> {
                SettingSPUtils.getInstance().setIsUseCustomFont(isChecked);
                //重启app
                AppUtils.rebootApp(500);
            });
        });
    }
}
