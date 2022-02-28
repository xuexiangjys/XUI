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

package com.xuexiang.xuidemo.fragment.utils.shortcut;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xuexiang.xuidemo.utils.XToastUtils;
import com.xuexiang.xutil.common.logger.Logger;

/**
 * 快捷方式创建接收广播
 *
 * @author xuexiang
 * @since 2021/10/6 4:43 PM
 */
public class ShortcutReceiver extends BroadcastReceiver {

    private static final String TAG = "ShortcutReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.dTag(TAG, "onReceive:" + intent.getAction());
        XToastUtils.toast("开始创建快捷方式");
    }
}
