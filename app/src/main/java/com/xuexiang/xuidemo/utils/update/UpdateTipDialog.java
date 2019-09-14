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

package com.xuexiang.xuidemo.utils.update;

import android.content.DialogInterface;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xuidemo.utils.Utils;

/**
 * 版本更新提示弹窗
 *
 * @author xuexiang
 * @since 2019-06-15 00:06
 */
public class UpdateTipDialog extends AppCompatActivity implements DialogInterface.OnDismissListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        DialogLoader.getInstance().showConfirmDialog(this, "Github下载速度太慢了，是否考虑切换蒲公英下载？", "是", (dialog, which) -> {
            dialog.dismiss();
            Utils.goWeb(UpdateTipDialog.this, "https://www.pgyer.com/XUIDemo");
        }, "否")
                .setOnDismissListener(this);

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        finish();
    }
}
