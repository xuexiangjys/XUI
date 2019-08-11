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

package com.xuexiang.xui.widget.imageview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xuexiang.xui.widget.imageview.strategy.IImageLoadStrategy;
import com.xuexiang.xui.widget.imageview.strategy.impl.GlideImageLoadStrategy;

/**
 * 图片加载策略管理
 *
 * @author xuexiang
 * @since 2019-07-26 00:27
 */
public class ImageLoader implements IImageLoadStrategy {


    private static volatile ImageLoader sInstance = null;

    /**
     * 图片加载策略
     */
    private IImageLoadStrategy mStrategy;


    private ImageLoader() {
        mStrategy = new GlideImageLoadStrategy();
    }

    /**
     * 设置图片加载的策略
     *
     * @param strategy
     */
    public ImageLoader setImageLoadStrategy(@NonNull IImageLoadStrategy strategy) {
        mStrategy = strategy;
        return this;
    }

    public IImageLoadStrategy getStrategy() {
        return mStrategy;
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static ImageLoader get() {
        if (sInstance == null) {
            synchronized (ImageLoader.class) {
                if (sInstance == null) {
                    sInstance = new ImageLoader();
                }
            }
        }
        return sInstance;
    }


    @Override
    public void loadImage(@NonNull ImageView imageView, Object path) {
        mStrategy.loadImage(imageView, path);
    }

    @Override
    public void loadGifImage(@NonNull ImageView imageView, Object path) {
        mStrategy.loadGifImage(imageView, path);
    }

    @Override
    public void loadImage(@NonNull ImageView imageView, Object path, DiskCacheStrategy strategy) {
        mStrategy.loadImage(imageView, path, strategy);
    }

    @Override
    public void loadGifImage(@NonNull ImageView imageView, Object path, DiskCacheStrategy strategy) {
        mStrategy.loadGifImage(imageView, path, strategy);
    }

    @Override
    public void loadImage(@NonNull ImageView imageView, Object path, Drawable placeholder, DiskCacheStrategy strategy) {
        mStrategy.loadImage(imageView, path, placeholder, strategy);
    }

    @Override
    public void loadGifImage(@NonNull ImageView imageView, Object path, Drawable placeholder, DiskCacheStrategy strategy) {
        mStrategy.loadGifImage(imageView, path, placeholder, strategy);
    }

    @Override
    public void loadImage(@NonNull ImageView imageView, Object path, int width, int height, Drawable placeholder, DiskCacheStrategy strategy) {
        mStrategy.loadImage(imageView, path, width, height, placeholder, strategy);
    }

    @Override
    public void clearCache(Context context) {
        mStrategy.clearCache(context);
    }

    @Override
    public void clearMemoryCache(Context context) {
        mStrategy.clearMemoryCache(context);
    }

    @Override
    public void clearDiskCache(Context context) {
        mStrategy.clearDiskCache(context);
    }
}
