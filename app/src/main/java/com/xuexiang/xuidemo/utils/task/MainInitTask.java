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

package com.xuexiang.xuidemo.utils.task;

import android.app.Application;

import androidx.annotation.NonNull;

import com.mikepenz.iconics.Iconics;
import com.xuexiang.xtask.api.step.SimpleTaskStep;
import com.xuexiang.xtask.core.ThreadType;
import com.xuexiang.xui.XUI;
import com.xuexiang.xuidemo.MyApp;
import com.xuexiang.xuidemo.utils.SettingSPUtils;
import com.xuexiang.xuidemo.utils.sdkinit.XBasicLibInit;
import com.xuexiang.xuidemo.utils.sdkinit.XUpdateInit;
import com.xuexiang.xuidemo.widget.iconfont.XUIIconFont;

/**
 * 主要初始化任务，放在第一位执行
 *
 * @author xuexiang
 * @since 2/17/22 12:43 AM
 */
public class MainInitTask extends SimpleTaskStep {

    private Application mApplication;

    /**
     * 构造方法
     *
     * @param application 应用上下文
     */
    public MainInitTask(Application application) {
        mApplication = application;
    }

    @Override
    public void doTask() throws Exception {
        // 初始化基础库
        XBasicLibInit.init(mApplication);
        // 初始化UI框架
        initUi();
        // XUpdate版本更新
        XUpdateInit.init(mApplication);
    }

    /**
     * 初始化UI框架
     */
    private void initUi() {
        XUI.debug(MyApp.isDebug());
        if (SettingSPUtils.getInstance().isUseCustomFont()) {
            //设置默认字体为华文行楷
            XUI.initFontStyle("fonts/hwxk.ttf");
        }
        //字体图标库
        Iconics.init(mApplication);
        //这是自己定义的图标库
        Iconics.registerFont(new XUIIconFont());
    }

    @Override
    public String getName() {
        return "MainInitTask";
    }

    @NonNull
    @Override
    public ThreadType getThreadType() {
        return ThreadType.SYNC;
    }
}
