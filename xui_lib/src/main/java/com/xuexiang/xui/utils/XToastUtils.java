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
    public static void toast(@NonNull final CharSequence message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.normal(XUI.getContext(), message).show();
            }
        });
    }

    @MainThread
    public static void toast(@StringRes final int message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.normal(XUI.getContext(), message).show();
            }
        });
    }

    @MainThread
    public static void toast(@NonNull final CharSequence message, final int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.normal(XUI.getContext(), message, duration).show();
            }
        });
    }

    @MainThread
    public static void toast(@StringRes final int message, final int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.normal(XUI.getContext(), message, duration).show();
            }
        });
    }

    //=============//

    @MainThread
    public static void error(@NonNull final CharSequence message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.error(XUI.getContext(), message).show();
            }
        });
    }

    @MainThread
    public static void error(@NonNull Throwable error) {
        final String message = error.getMessage() != null ? error.getMessage() : "";
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.error(XUI.getContext(), message).show();
            }
        });
    }

    @MainThread
    public static void error(@StringRes final int message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.error(XUI.getContext(), message).show();
            }
        });
    }

    @MainThread
    public static void error(@NonNull final CharSequence message, final int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.error(XUI.getContext(), message, duration).show();
            }
        });
    }

    @MainThread
    public static void error(@StringRes final int message, final int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.error(XUI.getContext(), message, duration).show();
            }
        });
    }

    //=============//

    @MainThread
    public static void success(@NonNull final CharSequence message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.success(XUI.getContext(), message).show();
            }
        });
    }

    @MainThread
    public static void success(@StringRes final int message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.success(XUI.getContext(), message).show();
            }
        });
    }

    @MainThread
    public static void success(@NonNull final CharSequence message, final int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.success(XUI.getContext(), message, duration).show();
            }
        });
    }

    @MainThread
    public static void success(@StringRes final int message, final int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.success(XUI.getContext(), message, duration).show();
            }
        });
    }

    //=============//

    @MainThread
    public static void info(@NonNull final CharSequence message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.info(XUI.getContext(), message).show();
            }
        });
    }

    @MainThread
    public static void info(@StringRes final int message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.info(XUI.getContext(), message).show();
            }
        });
    }

    @MainThread
    public static void info(@NonNull final CharSequence message, final int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.info(XUI.getContext(), message, duration).show();
            }
        });
    }

    @MainThread
    public static void info(@StringRes final int message, final int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.info(XUI.getContext(), message, duration).show();
            }
        });
    }

    //=============//

    @MainThread
    public static void warning(@NonNull final CharSequence message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.warning(XUI.getContext(), message).show();
            }
        });
    }

    @MainThread
    public static void warning(@StringRes final int message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.warning(XUI.getContext(), message).show();
            }
        });
    }

    @MainThread
    public static void warning(@NonNull final CharSequence message, final int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.warning(XUI.getContext(), message, duration).show();
            }
        });
    }

    @MainThread
    public static void warning(@StringRes final int message, final int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.warning(XUI.getContext(), message, duration).show();
            }
        });
    }

    private static void runOnUiThread(@NonNull final Runnable runnable) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            runnable.run();
        } else {
            MAIN_HANDLER.post(runnable);
        }
    }

}
