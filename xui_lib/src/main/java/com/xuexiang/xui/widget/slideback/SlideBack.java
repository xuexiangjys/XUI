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

package com.xuexiang.xui.widget.slideback;

import android.app.Activity;

import androidx.annotation.IntDef;

import com.xuexiang.xui.widget.slideback.callback.SlideBackCallBack;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.WeakHashMap;

/**
 * SlideBack使用类
 *
 * @author xuexiang
 * @since 2019-08-30 9:30
 */
public class SlideBack {
    // 使用WeakHashMap防止内存泄漏
    private static WeakHashMap<Activity, SlideBackManager> sSideMap = new WeakHashMap<>();

    /**
     * 注册
     *
     * @param activity 目标Act
     * @param callBack 回调
     */
    public static void register(Activity activity, SlideBackCallBack callBack) {
        register(activity, false, callBack);
    }

    /**
     * 注册
     *
     * @param activity   目标Act
     * @param haveScroll 页面是否有滑动
     * @param callBack   回调
     */
    public static void register(Activity activity, boolean haveScroll, SlideBackCallBack callBack) {
        with(activity).haveScroll(haveScroll).callBack(callBack).register();
    }

    /**
     * 注销
     *
     * @param activity 目标Act
     */
    public static void unregister(Activity activity) {
        SlideBackManager slideBack = sSideMap.get(activity);
        if (null != slideBack) {
            slideBack.unregister();
        }
        sSideMap.remove(activity);
    }

    /**
     * 构建侧滑管理器 - 用于更丰富的自定义配置
     *
     * @param activity 目标Act
     * @return 构建管理器
     */
    public static SlideBackManager with(Activity activity) {
        SlideBackManager manager = new SlideBackManager(activity);
        sSideMap.put(activity, manager);
        return manager;
    }

    /**
     * 侧滑返回模式 左
     */
    public static final int EDGE_LEFT = 0x0000;

    /**
     * 侧滑返回模式 右
     */
    public static final int EDGE_RIGHT = 0x0001;

    /**
     * 侧滑返回模式 左右皆可
     */
    public static final int EDGE_BOTH = 0x0002;

    @IntDef({EDGE_LEFT, EDGE_RIGHT, EDGE_BOTH})
    @Retention(RetentionPolicy.SOURCE)
    @interface EdgeMode {
    }
}