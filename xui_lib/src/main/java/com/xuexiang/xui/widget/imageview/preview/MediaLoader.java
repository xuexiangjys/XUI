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

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.xuexiang.xui.widget.imageview.preview.loader.GlideMediaLoader;
import com.xuexiang.xui.widget.imageview.preview.loader.IMediaLoader;
import com.xuexiang.xui.widget.imageview.preview.loader.ISimpleTarget;

/**
 * 图片加载管理器
 *
 * @author xuexiang
 * @since 2018/12/5 上午11:27
 */
public class MediaLoader implements IMediaLoader{

    private static volatile MediaLoader sInstance = null;

    private volatile IMediaLoader mIMediaLoader;

    private MediaLoader() {
        mIMediaLoader = new GlideMediaLoader();
    }

    /**
     * 设置IMediaLoader
     *
     * @param loader
     */
    public MediaLoader setIMediaLoader(@NonNull IMediaLoader loader) {
        mIMediaLoader = loader;
        return this;
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static MediaLoader get() {
        if (sInstance == null) {
            synchronized (MediaLoader.class) {
                if (sInstance == null) {
                    sInstance = new MediaLoader();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void displayImage(@NonNull Fragment context, @NonNull String path, ImageView imageView, @NonNull ISimpleTarget simpleTarget) {
        mIMediaLoader.displayImage(context, path, imageView, simpleTarget);
    }

    @Override
    public void displayGifImage(@NonNull Fragment context, @NonNull String path, ImageView imageView, @NonNull ISimpleTarget simpleTarget) {
        mIMediaLoader.displayGifImage(context, path, imageView, simpleTarget);
    }

    @Override
    public void onStop(@NonNull Fragment context) {
        mIMediaLoader.onStop(context);
    }

    @Override
    public void clearMemory(@NonNull Context context) {
        mIMediaLoader.clearMemory(context);
    }
}
