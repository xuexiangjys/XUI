/*
 * Copyright (C) 2022 xuexiangjys(xuexiangjys@163.com)
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

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.xuexiang.xui.XUI;
import com.xuexiang.xui.widget.toast.XToast;

/**
 * xtoast 工具类
 *
 * @author xuexiang
 * @since 4/19/22 1:04 AM
 */
public final class XToastUtils {

    private static final int DEFAULT_ALPHA = 200;

    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    private XToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    static {
        XToast.Config.get()
                .setAlpha(DEFAULT_ALPHA)
                .setToastTypeface(XUI.getDefaultTypeface())
                .allowQueue(false);
    }

    @MainThread
    public static void toast(@NonNull CharSequence message) {
        showToast(XToast.normal(XUI.getContext(), message));
    }

    @MainThread
    public static void toast(@StringRes int message) {
        showToast(XToast.normal(XUI.getContext(), message));
    }

    @MainThread
    public static void toast(@NonNull CharSequence message, int duration) {
        showToast(XToast.normal(XUI.getContext(), message, duration));
    }

    @MainThread
    public static void toast(@StringRes int message, int duration) {
        showToast(XToast.normal(XUI.getContext(), message, duration));
    }

    //=============//

    @MainThread
    public static void error(@NonNull CharSequence message) {
        showToast(XToast.error(XUI.getContext(), message));
    }

    @MainThread
    public static void error(@NonNull Exception error) {
        String message = error.getMessage() != null ? error.getMessage() : "";
        showToast(XToast.error(XUI.getContext(), message));
    }

    @MainThread
    public static void error(@StringRes int message) {
        showToast(XToast.error(XUI.getContext(), message));
    }

    @MainThread
    public static void error(@NonNull CharSequence message, int duration) {
        showToast(XToast.error(XUI.getContext(), message, duration));
    }

    @MainThread
    public static void error(@StringRes int message, int duration) {
        showToast(XToast.error(XUI.getContext(), message, duration));
    }

    //=============//

    @MainThread
    public static void success(@NonNull CharSequence message) {
        showToast(XToast.success(XUI.getContext(), message));
    }

    @MainThread
    public static void success(@StringRes int message) {
        showToast(XToast.success(XUI.getContext(), message));
    }

    @MainThread
    public static void success(@NonNull CharSequence message, int duration) {
        showToast(XToast.success(XUI.getContext(), message, duration));
    }

    @MainThread
    public static void success(@StringRes int message, int duration) {
        showToast(XToast.success(XUI.getContext(), message, duration));
    }

    //=============//

    @MainThread
    public static void info(@NonNull CharSequence message) {
        showToast(XToast.info(XUI.getContext(), message));
    }

    @MainThread
    public static void info(@StringRes int message) {
        showToast(XToast.info(XUI.getContext(), message));
    }

    @MainThread
    public static void info(@NonNull CharSequence message, int duration) {
        showToast(XToast.info(XUI.getContext(), message, duration));
    }

    @MainThread
    public static void info(@StringRes int message, int duration) {
        showToast(XToast.info(XUI.getContext(), message, duration));
    }

    //=============//

    @MainThread
    public static void warning(@NonNull CharSequence message) {
        showToast(XToast.warning(XUI.getContext(), message));
    }

    @MainThread
    public static void warning(@StringRes int message) {
        showToast(XToast.warning(XUI.getContext(), message));
    }

    @MainThread
    public static void warning(@NonNull CharSequence message, int duration) {
        showToast(XToast.warning(XUI.getContext(), message, duration));
    }

    @MainThread
    public static void warning(@StringRes int message, int duration) {
        showToast(XToast.warning(XUI.getContext(), message, duration));
    }

    private static void showToast(@NonNull final Toast toast) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            toast.show();
        } else {
            MAIN_HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    toast.show();
                }
            });
        }
    }

}
