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

package com.xuexiang.xuidemo.utils.sdkinit;

import android.app.Application;

import com.xuexiang.xuidemo.MyApp;
import com.xuexiang.xuidemo.utils.update.CustomUpdateDownloader;
import com.xuexiang.xuidemo.utils.update.OKHttpUpdateHttpService;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.utils.UpdateUtils;

/**
 * XUpdate 版本更新 SDK 初始化
 * @author xuexiang
 * @since 2019-06-18 15:51
 */
public final class XUpdateInit {

    private XUpdateInit() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(Application application) {
        XUpdate.get()
                .debug(MyApp.isDebug())
                //默认设置只在wifi下检查版本更新
                .isWifiOnly(false)
                //默认设置使用get请求检查版本
                .isGet(true)
                //默认设置非自动模式，可根据具体使用配置
                .isAutoMode(false)
                //设置默认公共请求参数
                .param("versionCode", UpdateUtils.getVersionCode(application))
                .param("appKey", application.getPackageName())
                //这个必须设置！实现网络请求功能。
                .setIUpdateHttpService(new OKHttpUpdateHttpService())
                .setIUpdateDownLoader(new CustomUpdateDownloader())
                //这个必须初始化
                .init(application);
    }
}
