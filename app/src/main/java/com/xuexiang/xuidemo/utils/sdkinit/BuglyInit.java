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

import com.tencent.bugly.Bugly;
import com.tencent.bugly.BuglyStrategy;
import com.xuexiang.xuidemo.BuildConfig;
import com.xuexiang.xutil.system.DeviceUtils;

/**
 * Bugly SDK 初始化
 *
 * @author xuexiang
 * @since 2019-06-18 15:44
 */
public final class BuglyInit {

    private BuglyInit() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化BuglySDK
     */
    public static void init(Application application) {
        BuglyStrategy strategy = new BuglyStrategy();
        strategy.setEnableANRCrashMonitor(true)
                .setEnableNativeCrashMonitor(true)
                .setUploadProcess(true)
                .setDeviceID(DeviceUtils.getAndroidID())
                .setRecordUserInfoOnceADay(true);
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId,调试时将第三个参数设置为true
        //这里BuildConfig.APP_ID_BUGLY是根据local.properties中定义的APP_ID_BUGLY生成的，只是运行看效果的话，可以不初始化该SDK
        Bugly.init(application, BuildConfig.APP_ID_BUGLY, true, strategy);
    }

}
