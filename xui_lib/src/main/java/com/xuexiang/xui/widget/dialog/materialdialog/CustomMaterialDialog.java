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

package com.xuexiang.xui.widget.dialog.materialdialog;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;

/**
 * 基础抽象的弹窗，可继承后自定义弹窗
 *
 * @author xuexiang
 * @since 2018/11/16 上午12:51
 */
public abstract class CustomMaterialDialog {

    /**
     * 弹窗窗体
     */
    protected MaterialDialog mDialog;

    /**
     * 构造窗体
     * @param context
     */
    public CustomMaterialDialog(Context context) {
        mDialog = getDialogBuilder(context).build();

        initViews(context);
    }

    /**
     * 获取弹窗的构建者
     * @param context
     * @return
     */
    protected abstract MaterialDialog.Builder getDialogBuilder(Context context);

    /**
     * 初始化控件
     * @param context
     */
    protected abstract void initViews(Context context);

    /**
     * 显示弹窗
     * @param <T>
     * @return
     */
    public <T extends CustomMaterialDialog> T show() {
        if (mDialog != null) {
            mDialog.show();
        }
        return (T) this;
    }

    /**
     * 隐藏弹窗
     * @param <T>
     * @return
     */
    public <T extends CustomMaterialDialog> T dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        return (T) this;
    }

    public MaterialDialog getDialog() {
        return mDialog;
    }

    public <T extends View> T findViewById(@IdRes int id) {
        return (T) mDialog.findViewById(id);
    }
}
