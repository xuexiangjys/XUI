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

import com.xuexiang.xui.widget.imageview.strategy.DiskCacheStrategyEnum;
import com.xuexiang.xui.widget.imageview.strategy.IImageLoadStrategy;
import com.xuexiang.xui.widget.imageview.strategy.ILoadListener;
import com.xuexiang.xui.widget.imageview.strategy.LoadOption;
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

    /**
     * 加载图片【最常用】
     *
     * @param imageView 图片控件
     * @param path      图片资源的索引
     * @param listener  加载监听
     */
    @Override
    public void loadImage(@NonNull ImageView imageView, Object path, @NonNull ILoadListener listener) {
        mStrategy.loadImage(imageView, path, listener);
    }

    @Override
    public void loadGifImage(@NonNull ImageView imageView, Object path) {
        mStrategy.loadGifImage(imageView, path);
    }

    /**
     * 加载图片【最常用】
     *
     * @param imageView 图片控件
     * @param path      图片资源的索引
     * @param listener  加载监听
     */
    @Override
    public void loadGifImage(@NonNull ImageView imageView, Object path, @NonNull ILoadListener listener) {
        mStrategy.loadGifImage(imageView, path, listener);
    }

    @Override
    public void loadImage(@NonNull ImageView imageView, Object path, DiskCacheStrategyEnum strategy) {
        mStrategy.loadImage(imageView, path, strategy);
    }

    /**
     * 加载图片
     *
     * @param imageView 图片控件
     * @param path      图片资源的索引
     * @param strategy  磁盘缓存策略
     * @param listener  加载监听
     */
    @Override
    public void loadImage(@NonNull ImageView imageView, Object path, DiskCacheStrategyEnum strategy, ILoadListener listener) {
        mStrategy.loadImage(imageView, path, strategy, listener);
    }

    @Override
    public void loadGifImage(@NonNull ImageView imageView, Object path, DiskCacheStrategyEnum strategy) {
        mStrategy.loadGifImage(imageView, path, strategy);
    }

    /**
     * 加载Gif图片
     *
     * @param imageView 图片控件
     * @param path      图片资源的索引
     * @param strategy  磁盘缓存策略
     * @param listener  加载监听
     */
    @Override
    public void loadGifImage(@NonNull ImageView imageView, Object path, DiskCacheStrategyEnum strategy, ILoadListener listener) {
        mStrategy.loadGifImage(imageView, path, strategy, listener);
    }

    @Override
    public void loadImage(@NonNull ImageView imageView, Object path, Drawable placeholder, DiskCacheStrategyEnum strategy) {
        mStrategy.loadImage(imageView, path, placeholder, strategy);
    }

    /**
     * 加载图片
     *
     * @param imageView   图片控件
     * @param path        图片资源的索引
     * @param placeholder 占位图片
     * @param strategy    磁盘缓存策略
     * @param listener    加载监听
     */
    @Override
    public void loadImage(@NonNull ImageView imageView, Object path, Drawable placeholder, DiskCacheStrategyEnum strategy, ILoadListener listener) {
        mStrategy.loadImage(imageView, path, placeholder, strategy, listener);
    }

    @Override
    public void loadGifImage(@NonNull ImageView imageView, Object path, Drawable placeholder, DiskCacheStrategyEnum strategy) {
        mStrategy.loadGifImage(imageView, path, placeholder, strategy);
    }

    /**
     * 加载Gif图片
     *
     * @param imageView   图片控件
     * @param path        图片资源的索引
     * @param placeholder 占位图片
     * @param strategy    磁盘缓存策略
     * @param listener    加载监听
     */
    @Override
    public void loadGifImage(@NonNull ImageView imageView, Object path, Drawable placeholder, DiskCacheStrategyEnum strategy, ILoadListener listener) {
        mStrategy.loadGifImage(imageView, path, placeholder, strategy, listener);
    }

    /**
     * 加载图片
     *
     * @param imageView  图片控件
     * @param path       图片资源的索引
     * @param loadOption 加载选项
     */
    @Override
    public void loadImage(@NonNull ImageView imageView, Object path, LoadOption loadOption) {
        mStrategy.loadImage(imageView, path, loadOption);
    }

    /**
     * 加载图片
     *
     * @param imageView  图片控件
     * @param path       图片资源的索引
     * @param loadOption 加载选项
     * @param listener   加载监听
     */
    @Override
    public void loadImage(@NonNull ImageView imageView, Object path, LoadOption loadOption, ILoadListener listener) {
        mStrategy.loadImage(imageView, path, loadOption, listener);
    }

    /**
     * 加载Gif图片
     *
     * @param imageView  图片控件
     * @param path       图片资源的索引
     * @param loadOption 加载选项
     */
    @Override
    public void loadGifImage(@NonNull ImageView imageView, Object path, LoadOption loadOption) {
        mStrategy.loadGifImage(imageView, path, loadOption);
    }

    /**
     * 加载Gif图片
     *
     * @param imageView  图片控件
     * @param path       图片资源的索引
     * @param loadOption 加载选项
     * @param listener   加载监听
     */
    @Override
    public void loadGifImage(@NonNull ImageView imageView, Object path, LoadOption loadOption, ILoadListener listener) {
        mStrategy.loadGifImage(imageView, path, loadOption, listener);
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
