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

package com.xuexiang.xui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.xuexiang.xui.widget.dialog.strategy.IDialogStrategy;
import com.xuexiang.xui.widget.dialog.strategy.InputCallback;
import com.xuexiang.xui.widget.dialog.strategy.InputInfo;
import com.xuexiang.xui.widget.dialog.strategy.impl.MaterialDialogStrategy;

/**
 * 对话框加载者
 *
 * @author xuexiang
 * @since 2018/11/15 上午12:16
 */
public class DialogLoader implements IDialogStrategy {

    private static volatile DialogLoader sInstance = null;

    /**
     * 加载策略
     */
    private IDialogStrategy mStrategy;


    private DialogLoader() {
        mStrategy = new MaterialDialogStrategy();
    }

    /**
     * 设置图片加载的策略
     *
     * @param strategy
     */
    public DialogLoader setIDialogStrategy(IDialogStrategy strategy) {
        mStrategy = strategy;
        return this;
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static DialogLoader getInstance() {
        if (sInstance == null) {
            synchronized (DialogLoader.class) {
                if (sInstance == null) {
                    sInstance = new DialogLoader();
                }
            }
        }
        return sInstance;
    }

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
        return mStrategy.showTipDialog(context, icon, title, content, submitText, listener);
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
        return mStrategy.showTipDialog(context, title, content, submitText);
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
        return mStrategy.showConfirmDialog(context, title, content, submitText, submitListener, cancelText, cancelListener);
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
        return mStrategy.showConfirmDialog(context, content, submitText, submitListener, cancelText, cancelListener);
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
        return mStrategy.showConfirmDialog(context, content, submitText, submitListener, cancelText);
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
        return mStrategy.showInputDialog(context, icon, title, content, inputInfo, inputCallback, submitText, submitListener, cancelText, cancelListener);
    }

    /**
     * 显示类似上下文菜单的对话框
     *
     * @param context
     * @param title    标题
     * @param items    选项集合
     * @param listener 点击监听
     * @return
     */
    @Override
    public Dialog showContextMenuDialog(Context context, String title, String[] items, DialogInterface.OnClickListener listener) {
        return mStrategy.showContextMenuDialog(context, title, items, listener);
    }

    /**
     * 显示类似上下文菜单的对话框
     *
     * @param context
     * @param title    标题
     * @param itemsId  选项集合的资源id
     * @param listener 点击监听
     * @return
     */
    @Override
    public Dialog showContextMenuDialog(Context context, String title, int itemsId, DialogInterface.OnClickListener listener) {
        return mStrategy.showContextMenuDialog(context, title, itemsId, listener);
    }

    /**
     * 显示带单选项的Dialog
     *
     * @param context
     * @param title          标题
     * @param items          选项集合
     * @param selectedIndex  默认已选项
     * @param listener       选项点击监听
     * @param submitText     确认按钮
     * @param cancelText     取消按钮
     * @return
     */
    @Override
    public Dialog showSingleChoiceDialog(Context context, String title, String[] items, int selectedIndex, DialogInterface.OnClickListener listener, String submitText, String cancelText) {
        return mStrategy.showSingleChoiceDialog(context, title, items, selectedIndex, listener, submitText, cancelText);
    }

    /**
     * 显示带单选项的Dialog
     *
     * @param context
     * @param title          标题
     * @param itemsId        选项集合的资源id
     * @param selectedIndex  默认已选项
     * @param listener       选项点击监听
     * @param submitText     确认按钮
     * @param cancelText     取消按钮
     * @return
     */
    @Override
    public Dialog showSingleChoiceDialog(Context context, String title, int itemsId, int selectedIndex, DialogInterface.OnClickListener listener, String submitText, String cancelText) {
        return mStrategy.showSingleChoiceDialog(context, title, itemsId, selectedIndex, listener, submitText, cancelText);
    }
}
