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

package com.xuexiang.xuidemo.widget;

import android.content.Context;

import com.xuexiang.xui.widget.button.shinebutton.ShineButton;
import com.xuexiang.xui.widget.dialog.materialdialog.CustomMaterialDialog;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xuidemo.R;

/**
 * @author xuexiang
 * @since 2020-01-06 23:39
 */
public class ShineButtonDialog extends CustomMaterialDialog {
    ShineButton shineButton;
    ShineButton shineButton1;
    ShineButton shineButton2;
    ShineButton shineButton3;

    /**
     * 构造窗体
     *
     * @param context
     */
    public ShineButtonDialog(Context context) {
        super(context);
    }

    @Override
    protected MaterialDialog.Builder getDialogBuilder(Context context) {
        return new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_shine_button, true)
                .title("自定义对话框")
                .positiveText(R.string.lab_submit)
                .negativeText(R.string.lab_cancel);
    }

    @Override
    protected void initViews(Context context) {
        shineButton = findViewById(R.id.shine_button);
        shineButton1 = findViewById(R.id.shine_button_1);
        shineButton2 = findViewById(R.id.shine_button_2);
        shineButton3 = findViewById(R.id.shine_button_3);

        //修复在对话框中的显示问题
        shineButton.fitDialog(mDialog);
        shineButton1.fitDialog(mDialog);
        shineButton2.fitDialog(mDialog);
        shineButton3.fitDialog(mDialog);
    }
}
