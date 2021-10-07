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

package com.xuexiang.xui.utils;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

/**
 * Shortcut应用快捷方式创建工具类
 *
 * @author xuexiang
 * @since 2021/10/6 2:50 PM
 */
public final class ShortcutUtils {

    private static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";

    private ShortcutUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 增加桌面快捷方式
     *
     * @param context        上下文
     * @param targetClass    快捷方式跳转的目标页面
     * @param shortcutId     快捷方式id
     * @param iconResourceId 快捷方式的图标
     * @param shortLabel     快捷方式的标题
     * @param resultReceiver 添加结果的接收器
     * @return 是否添加成功
     */
    public static boolean addPinShortcut(@NonNull Context context, @NonNull Class<?> targetClass, @NonNull String shortcutId, @AnyRes int iconResourceId, @NonNull CharSequence shortLabel, Class<? extends BroadcastReceiver> resultReceiver) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return addPinShortcutAboveN(context, targetClass, shortcutId, IconCompat.createWithResource(context, iconResourceId), shortLabel, resultReceiver);
        } else {
            return addShortcutBelowAndroidN(context, targetClass, iconResourceId, shortLabel);
        }
    }

    /**
     * 增加桌面快捷方式
     *
     * @param context        上下文
     * @param targetClass    快捷方式跳转的目标页面
     * @param shortcutId     快捷方式id
     * @param icon           快捷方式的图标
     * @param shortLabel     快捷方式的标题
     * @param resultReceiver 添加结果的接收器
     * @return 是否添加成功
     */
    public static boolean addPinShortcutAboveN(@NonNull Context context, @NonNull Class<?> targetClass, @NonNull String shortcutId, @NonNull IconCompat icon, @NonNull CharSequence shortLabel, Class<? extends BroadcastReceiver> resultReceiver) {
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            Intent shortcutInfoIntent = new Intent(context, targetClass);
            shortcutInfoIntent.setAction(Intent.ACTION_VIEW);
            ShortcutInfoCompat info = new ShortcutInfoCompat.Builder(context, shortcutId)
                    .setIcon(icon)
                    .setShortLabel(shortLabel)
                    .setIntent(shortcutInfoIntent)
                    .build();
            // 当添加快捷方式的确认弹框弹出来时，将被回调
            Intent resultIntent = resultReceiver != null ? new Intent(context, resultReceiver) : ShortcutManagerCompat.createShortcutResultIntent(context, info);
            PendingIntent shortcutCallbackIntent = PendingIntent.getBroadcast(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            return ShortcutManagerCompat.requestPinShortcut(context, info, shortcutCallbackIntent.getIntentSender());
        }
        return false;
    }


    /**
     * 增加桌面快捷方式
     *
     * @param context        上下文
     * @param targetClass    快捷方式跳转的目标页面
     * @param shortLabel     快捷方式的标题
     * @param iconResourceId 快捷方式的图标
     * @return 是否添加成功
     */
    public static boolean addShortcutBelowAndroidN(@NonNull Context context, @NonNull Class<?> targetClass, @AnyRes int iconResourceId, @NonNull CharSequence shortLabel) {
        try {
            Intent addShortcutIntent = new Intent(ACTION_ADD_SHORTCUT);
            // 不允许重复创建，不是根据快捷方式的名字判断重复的
            addShortcutIntent.putExtra("duplicate", false);
            // 快捷方式的图标
            addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, iconResourceId));
            // 快捷方式的名称
            addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortLabel);
            // 设置关联程序
            Intent launcherIntent = new Intent();
            launcherIntent.setClass(context, targetClass);
            addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);
            // 发送广播
            context.sendBroadcast(addShortcutIntent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
