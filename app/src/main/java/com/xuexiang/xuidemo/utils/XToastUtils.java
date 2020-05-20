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

package com.xuexiang.xuidemo.utils;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.xuexiang.xui.XUI;
import com.xuexiang.xui.widget.toast.XToast;

/**
 * xtoast 工具类
 *
 * @author xuexiang
 * @since 2019-06-30 19:04
 */
public final class XToastUtils {

    private XToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    static {
        XToast.Config.get()
                .setAlpha(200)
                .setToastTypeface(XUI.getDefaultTypeface())
                .allowQueue(false);
    }


    @MainThread
    public static void toast(@NonNull CharSequence message) {
        XToast.normal(XUI.getContext(), message).show();
    }

    @MainThread
    public static void toast(@StringRes int message) {
        XToast.normal(XUI.getContext(), message).show();
    }

    @MainThread
    public static void toast(@NonNull CharSequence message, int duration) {
        XToast.normal(XUI.getContext(), message, duration).show();
    }

    @MainThread
    public static void toast(@StringRes int message, int duration) {
        XToast.normal(XUI.getContext(), message, duration).show();
    }

    //=============//

    @MainThread
    public static void error(@NonNull CharSequence message) {
        XToast.error(XUI.getContext(), message).show();
    }

    @MainThread
    public static void error(@NonNull Exception error) {
        XToast.error(XUI.getContext(), error.getMessage()).show();
    }

    @MainThread
    public static void error(@StringRes int message) {
        XToast.error(XUI.getContext(), message).show();
    }

    @MainThread
    public static void error(@NonNull CharSequence message, int duration) {
        XToast.error(XUI.getContext(), message, duration).show();
    }

    @MainThread
    public static void error(@StringRes int message, int duration) {
        XToast.error(XUI.getContext(), message, duration).show();
    }

    //=============//

    @MainThread
    public static void success(@NonNull CharSequence message) {
        XToast.success(XUI.getContext(), message).show();
    }

    @MainThread
    public static void success(@StringRes int message) {
        XToast.success(XUI.getContext(), message).show();
    }

    @MainThread
    public static void success(@NonNull CharSequence message, int duration) {
        XToast.success(XUI.getContext(), message, duration).show();
    }

    @MainThread
    public static void success(@StringRes int message, int duration) {
        XToast.success(XUI.getContext(), message, duration).show();
    }

    //=============//

    @MainThread
    public static void info(@NonNull CharSequence message) {
        XToast.info(XUI.getContext(), message).show();
    }

    @MainThread
    public static void info(@StringRes int message) {
        XToast.info(XUI.getContext(), message).show();
    }

    @MainThread
    public static void info(@NonNull CharSequence message, int duration) {
        XToast.info(XUI.getContext(), message, duration).show();
    }

    @MainThread
    public static void info(@StringRes int message, int duration) {
        XToast.info(XUI.getContext(), message, duration).show();
    }

    //=============//

    @MainThread
    public static void warning(@NonNull CharSequence message) {
        XToast.warning(XUI.getContext(), message).show();
    }

    @MainThread
    public static void warning(@StringRes int message) {
        XToast.warning(XUI.getContext(), message).show();
    }

    @MainThread
    public static void warning(@NonNull CharSequence message, int duration) {
        XToast.warning(XUI.getContext(), message, duration).show();
    }

    @MainThread
    public static void warning(@StringRes int message, int duration) {
        XToast.warning(XUI.getContext(), message, duration).show();
    }

}
