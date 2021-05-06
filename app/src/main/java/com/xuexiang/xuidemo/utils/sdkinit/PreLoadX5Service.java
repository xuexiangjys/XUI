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

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xutil.common.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import static com.xuexiang.xaop.consts.PermissionConsts.STORAGE;

/**
 * 预加载X5
 *
 * @author xuexiang
 * @since 2019-07-31 9:29
 */
public class PreLoadX5Service extends IntentService {

    private static final String SERVICE_NAME = "PreLoadX5Service";

    public PreLoadX5Service() {
        super(SERVICE_NAME);
    }

    /**
     * 开始预加载
     *
     * @param context
     */
    public static void start(Context context) {
        Intent intent = new Intent(context, PreLoadX5Service.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        preInit();
    }

    /**
     * 预加载需要存储权限，否则插件加载失败
     */
    @Permission(STORAGE)
    private void preInit() {
        initSettings();
        preInitX5();
        preInitX5WebCore();
    }

    private void initSettings() {
        //TBS内核首次使用时加载卡顿ANR
        Map<String, Object> map = new HashMap<>();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
    }

    private void preInitX5() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Logger.dTag("TbsInit", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        QbSdk.initX5Environment(this, cb);
    }

    private void preInitX5WebCore() {
        if (!QbSdk.isTbsCoreInited()) {
            // 设置X5初始化完成的回调接口
            QbSdk.preInit(this, null);
        }
    }

}
