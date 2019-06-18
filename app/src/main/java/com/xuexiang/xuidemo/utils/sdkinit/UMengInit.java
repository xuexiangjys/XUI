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

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

/**
 * UMeng 统计 SDK初始化
 * @author xuexiang
 * @since 2019-06-18 15:49
 */
public final class UMengInit {

    private static final String APP_ID_UMENG = "5d030d6f570df379b7000e70";

    private UMengInit() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化UmengSDK
     */
    public static void init(Application application) {
        //设置LOG开关，默认为false
        UMConfigure.setLogEnabled(true);
        //初始化组件化基础库, 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，UMConfigure.init调用中appkey和channel参数请置为null）。
        //第二个参数是appkey，最后一个参数是pushSecret
        UMConfigure.init(application, APP_ID_UMENG, "pgyer", UMConfigure.DEVICE_TYPE_PHONE,"");
        //统计SDK是否支持采集在子进程中打点的自定义事件，默认不支持
        UMConfigure.setProcessEvent(true);//支持多进程打点
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
    }
}
