/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.xui.widget.imageview.preview;

import com.xuexiang.xui.widget.imageview.preview.loader.GlideMediaLoader;
import com.xuexiang.xui.widget.imageview.preview.loader.IMediaLoader;

/**
 * 图片加载管理器
 *
 * @author xuexiang
 * @since 2018/12/5 上午11:27
 */
public class MediaLoader {

    private static volatile MediaLoader sInstance = null;

    private volatile IMediaLoader mLoader;

    private MediaLoader() {

    }

    /**
     * 获取单例
     *
     * @return
     */
    public static MediaLoader getInstance() {
        if (sInstance == null) {
            synchronized (MediaLoader.class) {
                if (sInstance == null) {
                    sInstance = new MediaLoader();
                }
            }
        }
        return sInstance;
    }

    public static IMediaLoader get() {
        return getInstance().getLoader();
    }


    /**
     * 初始化加载图片类
     *
     * @param loader
     */
    public void initLoader(IMediaLoader loader) {
        mLoader = loader;
    }

    public IMediaLoader getLoader() {
        if (mLoader == null) {
            mLoader = new GlideMediaLoader();
        }
        return mLoader;
    }

}
