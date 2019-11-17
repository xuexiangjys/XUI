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

import android.content.Context;

import com.tencent.mmkv.MMKV;
import com.umeng.analytics.MobclickAgent;
import com.xuexiang.xuidemo.activity.LoginActivity;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.common.StringUtils;

/**
 * Token管理工具
 *
 * @author xuexiang
 * @since 2019-11-17 22:37
 */
public final class TokenUtils {

    private static String sToken;

    private static final String KEY_TOKEN = "com.xuexiang.xuidemo.utils.KEY_TOKEN";


    private TokenUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化Token信息
     */
    public static void init(Context context) {
        MMKV.initialize(context);
        sToken = MMKV.defaultMMKV().decodeString(KEY_TOKEN, "");
    }

    public static void setToken(String token) {
        sToken = token;
    }

    public static void clearToken() {
        sToken = null;
        MMKV.defaultMMKV().remove(KEY_TOKEN);
    }

    public static boolean hasToken() {
        return !StringUtils.isEmpty(sToken);
    }

    /**
     * 处理登录成功的事件
     *
     * @param token 账户信息
     */
    public static boolean handleLoginSuccess(String token) {
        if (!StringUtils.isEmpty(token)) {
            XToastUtils.success("登录成功！");
            MobclickAgent.onProfileSignIn("github", token);
            setToken(token);
            return true;
        } else {
            XToastUtils.error("登录失败！");
            return false;
        }
    }

    /**
     * 处理登出的事件
     */
    public static void handleLogoutSuccess() {
        MobclickAgent.onProfileSignOff();
        //登出时，清除账号信息
        clearToken();
        XToastUtils.success("登出成功！");
        //跳转到登录页
        ActivityUtils.startActivity(LoginActivity.class);
    }

}
