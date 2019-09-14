/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xuidemo.fragment.components.dialog;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.InputType;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.dialog.strategy.InputCallback;
import com.xuexiang.xui.widget.dialog.strategy.InputInfo;
import com.xuexiang.xui.widget.dialog.strategy.impl.AlertDialogStrategy;
import com.xuexiang.xui.widget.dialog.strategy.impl.MaterialDialogStrategy;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseSimpleListFragment;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.List;

/**
 * @author xuexiang
 * @since 2018/11/15 上午12:14
 */
@Page(name = "Dialog构建策略")
public class DialogStrategyFragment extends BaseSimpleListFragment {
    /**
     * 初始化例子
     *
     * @param lists
     * @return
     */
    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("使用MaterialDialog策略显示");
        lists.add("使用AlertDialog策略显示");
        lists.add("简单的提示性对话框");
        lists.add("简单的确认对话框");
        lists.add("带输入框的对话框");
        lists.add("类似系统的上下文菜单ContextMenu的Dialog");
        lists.add("带单选项的Dialog");
        return lists;
    }

    /**
     * 条目点击
     *
     * @param position
     */
    @Override
    protected void onItemClick(int position) {
        switch(position) {
            case 0:
                DialogLoader.getInstance().setIDialogStrategy(new MaterialDialogStrategy());
                break;
            case 1:
                DialogLoader.getInstance().setIDialogStrategy(new AlertDialogStrategy());
                break;
            case 2:
                DialogLoader.getInstance().showTipDialog(
                        getContext(),
                        getString(R.string.tip_infos),
                        getString(R.string.content_simple_confirm_dialog),
                        getString(R.string.lab_submit));
                break;
            case 3:
                DialogLoader.getInstance().showConfirmDialog(
                        getContext(),
                        getString(R.string.tip_bluetooth_permission),
                        getString(R.string.lab_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToastUtils.toast("同意打开蓝牙！");
                                dialog.dismiss();
                            }
                        },
                        getString(R.string.lab_no),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToastUtils.toast("不同意打开蓝牙！");
                                dialog.dismiss();
                            }
                        }
                );
                break;
            case 4:
                DialogLoader.getInstance().showInputDialog(
                        getContext(),
                        R.drawable.icon_warning,
                        getString(R.string.tip_warning),
                        getString(R.string.content_warning),
                        new InputInfo(InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                                | InputType.TYPE_TEXT_FLAG_CAP_WORDS,
                                getString(R.string.hint_please_input_password)),
                        new InputCallback() {
                            @Override
                            public void onInput(@NonNull DialogInterface dialog, CharSequence input) {
                                ToastUtils.toast(input.toString());
                            }
                        },
                        getString(R.string.lab_continue),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (dialog instanceof MaterialDialog) {
                                    ToastUtils.toast("你输入了:" + ((MaterialDialog)dialog).getInputEditText().getText().toString());
                                }
                            }
                        },
                        getString(R.string.lab_change),
                        null);
                break;
            case 5:
                DialogLoader.getInstance().showContextMenuDialog(getContext(),
                        getString(R.string.tip_options),
                        R.array.menu_values,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToastUtils.toast("选择了第" + (which + 1) + "个");
                            }
                        });
                break;
            case 6:
                DialogLoader.getInstance().showSingleChoiceDialog(
                        getContext(),
                        getString(R.string.tip_router_setting),
                        R.array.router_choice_entry,
                        0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToastUtils.toast("选择了第" + (which + 1) + "个");
                            }},
                        getString(R.string.lab_yes),
                        getString(R.string.lab_no));
                break;
            default:
                break;
        }
    }
}
