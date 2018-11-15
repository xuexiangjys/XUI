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

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.dialog.strategy.IDialogStrategy;
import com.xuexiang.xui.widget.dialog.strategy.InputCallback;
import com.xuexiang.xui.widget.dialog.strategy.InputInfo;

/**
 * MaterialDialog 策略
 *
 * @author xuexiang
 * @since 2018/11/14 下午11:46
 */
public class MaterialDialogStrategy implements IDialogStrategy {

    /**
     * 显示简要的提示对话框
     *
     * @param context
     * @param icon       图标
     * @param title      标题
     * @param content    正文
     * @param submitText 确认按钮
     * @param listener 确认按钮点击监听
     */
    @Override
    public Dialog showTipDialog(Context context, int icon, String title, String content, String submitText, final DialogInterface.OnClickListener listener) {
        return new MaterialDialog.Builder(context)
                .iconRes(icon)
                .title(title)
                .content(content)
                .positiveText(submitText)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (listener != null) {
                            listener.onClick(dialog, which.ordinal());
                        } else {
                            dialog.dismiss();
                        }
                    }
                })
                .cancelable(false)
                .autoDismiss(false)
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
        return new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(submitText)
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
    public Dialog showConfirmDialog(Context context, String title, String content, String submitText, final DialogInterface.OnClickListener submitListener, String cancelText, final DialogInterface.OnClickListener cancelListener) {
        return new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(submitText)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (submitListener != null) {
                            submitListener.onClick(dialog, which.ordinal());
                        } else {
                            dialog.dismiss();
                        }
                    }
                })
                .negativeText(cancelText)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (cancelListener != null) {
                            cancelListener.onClick(dialog, which.ordinal());
                        } else {
                            dialog.dismiss();
                        }
                    }
                })
                .cancelable(false)
                .autoDismiss(false)
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
    public Dialog showConfirmDialog(Context context, String content, String submitText, final DialogInterface.OnClickListener submitListener, String cancelText, final DialogInterface.OnClickListener cancelListener) {
        return new MaterialDialog.Builder(context)
                .content(content)
                .positiveText(submitText)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (submitListener != null) {
                            submitListener.onClick(dialog, which.ordinal());
                        } else {
                            dialog.dismiss();
                        }
                    }
                })
                .negativeText(cancelText)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (cancelListener != null) {
                            cancelListener.onClick(dialog, which.ordinal());
                        } else {
                            dialog.dismiss();
                        }
                    }
                })
                .cancelable(false)
                .autoDismiss(false)
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
    public Dialog showConfirmDialog(Context context, String content, String submitText, final DialogInterface.OnClickListener submitListener, String cancelText) {
        return new MaterialDialog.Builder(context)
                .content(content)
                .positiveText(submitText)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (submitListener != null) {
                            submitListener.onClick(dialog, which.ordinal());
                        } else {
                            dialog.dismiss();
                        }
                    }
                })
                .negativeText(cancelText)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .cancelable(false)
                .autoDismiss(false)
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
    public Dialog showInputDialog(Context context, int icon, String title, String content, @NonNull InputInfo inputInfo, final InputCallback inputCallback, String submitText, final DialogInterface.OnClickListener submitListener, String cancelText, final DialogInterface.OnClickListener cancelListener) {
        return new MaterialDialog.Builder(context)
                .iconRes(icon)
                .title(title)
                .content(content)
                .inputType(inputInfo.getInputType())
                .input(inputInfo.getHint(), inputInfo.getPreFill(), inputInfo.isAllowEmptyInput(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if (inputCallback != null) {
                            inputCallback.onInput(dialog, input);
                        }
                    }
                })
                .positiveText(submitText)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (submitListener != null) {
                            submitListener.onClick(dialog, which.ordinal());
                        } else {
                            dialog.dismiss();
                        }
                    }
                })
                .negativeText(cancelText)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (cancelListener != null) {
                            cancelListener.onClick(dialog, which.ordinal());
                        } else {
                            dialog.dismiss();
                        }
                    }
                })
                .cancelable(false)
                .autoDismiss(false)
                .show();

    }


}
