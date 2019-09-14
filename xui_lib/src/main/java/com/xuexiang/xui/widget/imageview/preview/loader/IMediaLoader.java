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

package com.xuexiang.xui.widget.imageview.preview.loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

/**
 * 加载器接口
 *
 * @author xuexiang
 * @since 2018/12/5 上午11:07
 */
public interface IMediaLoader {

    /**
     * 加载图片
     *
     * @param context
     * @param path         图片你的路径
     * @param imageView
     * @param simpleTarget 图片加载状态回调
     */
    void displayImage(@NonNull Fragment context, @NonNull String path, ImageView imageView, @NonNull ISimpleTarget simpleTarget);

    /**
     * 加载gif 图
     *
     * @param context
     * @param path         图片你的路径
     * @param imageView
     * @param simpleTarget 图片加载状态回调
     */
    void displayGifImage(@NonNull Fragment context, @NonNull String path, ImageView imageView, @NonNull ISimpleTarget simpleTarget);

    /**
     * 停止
     *
     * @param context 容器
     **/
    void onStop(@NonNull Fragment context);

    /**
     * 停止
     *
     * @param context 容器
     **/
    void clearMemory(@NonNull Context context);
}
