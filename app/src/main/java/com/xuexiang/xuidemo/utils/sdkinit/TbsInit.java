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

/**
 * 腾讯X5 SDK初始化
 *
 * @author xuexiang
 * @since 2019-07-21 22:56
 */
public final class TbsInit {

    private TbsInit() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(Application application) {
        PreLoadX5Service.start(application);
    }
}
