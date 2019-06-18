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
import android.util.Log;

import com.tencent.tinker.entry.ApplicationLike;
import com.tencent.tinker.lib.service.PatchResult;
import com.tinkerpatch.sdk.TinkerPatch;
import com.tinkerpatch.sdk.loader.TinkerPatchApplicationLike;
import com.tinkerpatch.sdk.server.callback.ConfigRequestCallback;
import com.tinkerpatch.sdk.tinker.callback.ResultCallBack;
import com.umeng.analytics.MobclickAgent;
import com.xuexiang.xuidemo.BuildConfig;
import com.xuexiang.xutil.net.JsonUtil;

import java.util.HashMap;

/**
 * Tinker 热更新初始化
 *
 * @author xuexiang
 * @since 2019-06-18 15:46
 */
public final class TinkerInit {

    private TinkerInit() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 我们需要确保至少对主进程跟patch进程初始化 TinkerPatch
     */
    public static void init(final Application application) {
        if (BuildConfig.TINKER_ENABLE) {
            // 我们可以从这里获得Tinker加载过程的信息
            ApplicationLike tinkerApplicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike();

            // 初始化TinkerPatch SDK, 更多配置可参照API章节中的,初始化SDK
            TinkerPatch.init(tinkerApplicationLike)
                    .reflectPatchLibrary()
                    //向后台获取是否有补丁包更新,默认的访问间隔为3个小时，若参数为true,即每次调用都会真正的访问后台配置
                    //你也可以在用户登录或者APP启动等一些关键路径，使用fetchPatchUpdate(true)强制检查更新
                    .fetchPatchUpdate(true)
                    //设置访问后台补丁包更新配置的时间间隔,默认为3个小时
                    .setFetchPatchIntervalByHours(1)
                    //若参数为true,即每次调用都会真正的访问后台配置
                    .fetchDynamicConfig(new ConfigRequestCallback() {
                        @Override
                        public void onSuccess(HashMap<String, String> hashMap) {
                            Log.e("xuexiang", "参数:" + JsonUtil.toJson(hashMap));
                        }

                        @Override
                        public void onFail(Exception e) {
                        }
                    }, true)
                    //设置访问后台动态配置的时间间隔,默认为3个小时
                    .setFetchDynamicConfigIntervalByHours(3)
                    //设置收到后台回退要求时,锁屏清除补丁,默认是等主进程重启时自动清除
                    .setPatchRollbackOnScreenOff(true)
                    //设置补丁合成成功后,锁屏重启程序,默认是等应用自然重启
                    .setPatchRestartOnSrceenOff(true)
                    .setPatchResultCallback(new ResultCallBack() {
                        @Override
                        public void onPatchResult(PatchResult patchResult) {
                            MobclickAgent.onEvent(application, "Event-HotFix", "热更新" + (patchResult.isSuccess ? "成功" : "失败"));
                        }
                    });
            // 每隔3个小时(通过setFetchPatchIntervalByHours设置)去访问后台时候有更新,通过handler实现轮训的效果
            TinkerPatch.with().fetchPatchUpdateAndPollWithInterval();
        }
    }
}
