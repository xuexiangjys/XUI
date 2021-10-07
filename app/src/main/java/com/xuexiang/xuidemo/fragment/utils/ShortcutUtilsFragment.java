/*
 * Copyright (C) 2021 xuexiangjys(xuexiangjys@163.com)
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

import android.view.View;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ShortcutUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.grouplist.XUIGroupListView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.activity.SearchComponentActivity;
import com.xuexiang.xuidemo.activity.SettingsActivity;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.fragment.utils.shortcut.ShortcutReceiver;
import com.xuexiang.xuidemo.utils.Utils;

import butterknife.BindView;

/**
 * 快捷方式创建工具类
 *
 * @author xuexiang
 * @since 2021/10/6 3:33 PM
 */
@Page(name = "ShortcutUtils", extra = R.drawable.ic_util_shortcut)
public class ShortcutUtilsFragment extends BaseFragment {

    @BindView(R.id.groupListView)
    XUIGroupListView groupListView;

    @Override
    protected TitleBar initTitle() {
        return super.initTitle().setLeftClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                popToBack();
                Utils.syncMainPageStatus();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shortcututils;
    }

    @Override
    protected void initViews() {
        XUIGroupListView.newSection(getContext())
                .setDescription("支持7.0以上版本的桌面快捷方式的创建")
                .addItemView(groupListView.createItemView("增加搜索至桌面快捷方式"), v -> createSearchShortcut())
                .addItemView(groupListView.createItemView("增加设置至桌面快捷方式"), v -> createSettingShortcut())
                .addTo(groupListView);
    }

    /**
     * 创建搜索桌面快捷方式
     */
    private void createSearchShortcut() {
        ShortcutUtils.addPinShortcut(getContext(),
                SearchComponentActivity.class,
                "shortcut_search_id",
                R.drawable.ic_action_search, ResUtils.getString(R.string.shortcut_label_search), ShortcutReceiver.class);
    }

    /**
     * 创建搜索桌面快捷方式s
     */
    private void createSettingShortcut() {
        ShortcutUtils.addPinShortcut(getContext(),
                SettingsActivity.class,
                "shortcut_setting_id",
                R.drawable.ic_action_setting, ResUtils.getString(R.string.shortcut_label_setting), ShortcutReceiver.class);
    }
}
