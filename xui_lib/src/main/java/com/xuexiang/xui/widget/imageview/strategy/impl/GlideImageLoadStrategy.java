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

package com.xuexiang.xui.widget.imageview.strategy.impl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.xuexiang.xui.widget.imageview.strategy.DiskCacheStrategyEnum;
import com.xuexiang.xui.widget.imageview.strategy.IImageLoadStrategy;
import com.xuexiang.xui.widget.imageview.strategy.ILoadListener;
import com.xuexiang.xui.widget.imageview.strategy.LoadOption;

/**
 * Glide图片加载策略
 *
 * @author xuexiang
 * @since 2019-07-26 00:20
 */
public class GlideImageLoadStrategy implements IImageLoadStrategy {

    @Override
    public void loadImage(@NonNull ImageView imageView, Object path) {
        Glide.with(imageView.getContext())
                .load(path)
                .into(imageView);
    }

    /**
     * 加载图片【最常用】
     *
     * @param imageView 图片控件
     * @param path      图片资源的索引
     * @param listener  加载监听
     */
    @Override
    public void loadImage(@NonNull ImageView imageView, Object path, final @NonNull ILoadListener listener) {
        Glide.with(imageView.getContext())
                .load(path)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        listener.onLoadFailed(e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        listener.onLoadSuccess();
                        return false;
                    }
                })
                .into(imageView);
    }

    @Override
    public void loadGifImage(@NonNull ImageView imageView, Object path) {
        Glide.with(imageView.getContext())
                .asGif()
                .load(path)
                .into(imageView);
    }

    /**
     * 加载图片【最常用】
     *
     * @param imageView 图片控件
     * @param path      图片资源的索引
     * @param listener  加载监听
     */
    @Override
    public void loadGifImage(@NonNull ImageView imageView, Object path, final @NonNull ILoadListener listener) {
        Glide.with(imageView.getContext())
                .asGif()
                .load(path)
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        listener.onLoadFailed(e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        listener.onLoadSuccess();
                        return false;
                    }
                })
                .into(imageView);
    }

    @Override
    public void loadImage(@NonNull ImageView imageView, Object path, DiskCacheStrategyEnum strategy) {
        loadImage(imageView, path, LoadOption.of(strategy));
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
        loadImage(imageView, path, LoadOption.of(strategy), listener);
    }

    @Override
    public void loadGifImage(@NonNull ImageView imageView, Object path, DiskCacheStrategyEnum strategy) {
        loadGifImage(imageView, path, LoadOption.of(strategy));
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
        loadGifImage(imageView, path, LoadOption.of(strategy), listener);
    }

    @Override
    public void loadImage(@NonNull ImageView imageView, Object path, Drawable placeholder, DiskCacheStrategyEnum strategy) {
        loadImage(imageView, path, LoadOption.of(placeholder).setCacheStrategy(strategy));
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
        loadImage(imageView, path, LoadOption.of(placeholder).setCacheStrategy(strategy), listener);
    }

    @Override
    public void loadGifImage(@NonNull ImageView imageView, Object path, Drawable placeholder, DiskCacheStrategyEnum strategy) {
        loadGifImage(imageView, path, LoadOption.of(placeholder).setCacheStrategy(strategy));
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
        loadGifImage(imageView, path, LoadOption.of(placeholder).setCacheStrategy(strategy), listener);
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
        loadImage(imageView, path, loadOption, null);
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
        loadGifImage(imageView, path, loadOption, null);
    }

    /**
     * 加载图片
     *
     * @param imageView  图片控件
     * @param path       图片资源的索引
     * @param loadOption 加载选项
     */
    @SuppressLint("CheckResult")
    @Override
    public void loadImage(@NonNull ImageView imageView, Object path, LoadOption loadOption, final ILoadListener listener) {
        RequestBuilder<Drawable> builder = Glide.with(imageView.getContext())
                .load(path)
                .apply(getRequestOptions(loadOption));
        if (listener != null) {
            builder.listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    listener.onLoadFailed(e);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    listener.onLoadSuccess();
                    return false;
                }
            });
        }
        builder.into(imageView);
    }

    /**
     * 加载Gif图片
     *
     * @param imageView  图片控件
     * @param path       图片资源的索引
     * @param loadOption 加载选项
     */
    @SuppressLint("CheckResult")
    @Override
    public void loadGifImage(@NonNull ImageView imageView, Object path, LoadOption loadOption, final ILoadListener listener) {
        RequestBuilder<GifDrawable> builder = Glide.with(imageView.getContext())
                .asGif()
                .load(path)
                .apply(getRequestOptions(loadOption));
        if (listener != null) {
            builder.listener(new RequestListener<GifDrawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                    listener.onLoadFailed(e);
                    return false;
                }

                @Override
                public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                    listener.onLoadSuccess();
                    return false;
                }
            });
        }
        builder.into(imageView);
    }

    /**
     * loadOption转化为RequestOptions
     *
     * @param loadOption
     * @return
     */
    @SuppressLint("CheckResult")
    private RequestOptions getRequestOptions(LoadOption loadOption) {
        RequestOptions options = new RequestOptions();
        if (loadOption.hasSize()) {
            options.override(loadOption.getWidth(), loadOption.getHeight());
        }
        if (loadOption.placeholder != null) {
            options.placeholder(loadOption.placeholder);
        }
        if (loadOption.error != null) {
            options.error(loadOption.error);
        }
        if (loadOption.cacheStrategy != null) {
            options.diskCacheStrategy(toGlideStrategy(loadOption.cacheStrategy));
        }
        switch (loadOption.align) {
            case CENTER_CROP:
                options.centerCrop();
                break;
            case CIRCLE_CROP:
                options.circleCrop();
                break;
            case CENTER_INSIDE:
                options.centerInside();
                break;
            case FIT_CENTER:
                options.fitCenter();
                break;
            default:
                break;
        }
        options.timeout(loadOption.timeoutMs);
        return options;
    }

    @Override
    public void clearCache(Context context) {
        Glide.get(context).clearMemory();
        Glide.get(context).clearDiskCache();
    }

    @Override
    public void clearMemoryCache(Context context) {
        Glide.get(context).clearMemory();
    }

    @Override
    public void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }

    /**
     * 策略切换
     *
     * @param strategyEnum
     * @return
     */
    private DiskCacheStrategy toGlideStrategy(DiskCacheStrategyEnum strategyEnum) {
        switch (strategyEnum) {
            case ALL:
                return DiskCacheStrategy.ALL;
            case NONE:
                return DiskCacheStrategy.NONE;
            case DATA:
                return DiskCacheStrategy.DATA;
            case RESOURCE:
                return DiskCacheStrategy.RESOURCE;
            case AUTOMATIC:
                return DiskCacheStrategy.AUTOMATIC;
            default:
                return DiskCacheStrategy.AUTOMATIC;
        }
    }
}
