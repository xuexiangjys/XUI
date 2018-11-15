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

package com.xuexiang.xui.widget.dialog.strategy.impl;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.xuexiang.xui.widget.dialog.strategy.IDialogStrategy;
import com.xuexiang.xui.widget.dialog.strategy.InputCallback;
import com.xuexiang.xui.widget.dialog.strategy.InputInfo;

/**
 * AlertDialog 策略
 *
 * @author xuexiang
 * @since 2018/11/15 上午12:24
 */
public class AlertDialogStrategy implements IDialogStrategy {

    /**
     * 显示简要的提示对话框
     *
     * @param context
     * @param icon       图标
     * @param title      标题
     * @param content    正文
     * @param submitText 确认按钮
     * @param listener   确认按钮点击监听
     */
    @Override
    public Dialog showTipDialog(Context context, int icon, String title, String content, String submitText, DialogInterface.OnClickListener listener) {
        return new AlertDialog.Builder(context)
                .setIcon(icon)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton(submitText, listener)
                .setCancelable(false)
                .show();
    }

    /**
     * 显示简要的提示对话框
     *
     * @param context
     * @param title      标题
     * @param content    正文
     * @param submitText 确认按钮
     */
    @Override
    public Dialog showTipDialog(Context context, String title, String content, String submitText) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton(submitText, null)
                .show();
    }

    /**
     * 显示简要的确认对话框
     *
     * @param context
     * @param title          标题
     * @param content        正文
     * @param submitText     确认按钮
     * @param submitListener 确认按钮点击监听
     * @param cancelText     取消按钮
     * @param cancelListener 取消按钮点击监听
     * @return 对话框
     */
    @Override
    public Dialog showConfirmDialog(Context context, String title, String content, String submitText, DialogInterface.OnClickListener submitListener, String cancelText, DialogInterface.OnClickListener cancelListener) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton(submitText, submitListener)
                .setNegativeButton(cancelText, cancelListener)
                .setCancelable(false)
                .show();
    }

    /**
     * 显示简要的确认对话框
     *
     * @param context
     * @param content        正文
     * @param submitText     确认按钮
     * @param submitListener 确认按钮点击监听
     * @param cancelText     取消按钮
     * @param cancelListener 取消按钮点击监听
     */
    @Override
    public Dialog showConfirmDialog(Context context, String content, String submitText, DialogInterface.OnClickListener submitListener, String cancelText, DialogInterface.OnClickListener cancelListener) {
        return new AlertDialog.Builder(context)
                .setMessage(content)
                .setPositiveButton(submitText, submitListener)
                .setNegativeButton(cancelText, cancelListener)
                .setCancelable(false)
                .show();
    }

    /**
     * 显示简要的确认对话框
     *
     * @param context
     * @param content        正文
     * @param submitText     确认按钮
     * @param submitListener 确认按钮点击监听
     * @param cancelText     取消按钮
     */
    @Override
    public Dialog showConfirmDialog(Context context, String content, String submitText, DialogInterface.OnClickListener submitListener, String cancelText) {
        return new AlertDialog.Builder(context)
                .setMessage(content)
                .setPositiveButton(submitText, submitListener)
                .setNegativeButton(cancelText, null)
                .setCancelable(false)
                .show();
    }

    /**
     * 显示带输入框的对话框
     *
     * @param context        正文
     * @param icon           图标
     * @param title          标题
     * @param content        正文
     * @param inputInfo      输入框信息描述
     * @param inputCallback  输入的回调
     * @param submitText     确认按钮
     * @param submitListener 确认按钮点击监听
     * @param cancelText     取消按钮
     * @param cancelListener 取消按钮点击监听
     * @return
     */
    @Override
    public Dialog showInputDialog(Context context, int icon, String title, String content, InputInfo inputInfo, InputCallback inputCallback, String submitText, DialogInterface.OnClickListener submitListener, String cancelText, DialogInterface.OnClickListener cancelListener) {
        return null;
    }
}
