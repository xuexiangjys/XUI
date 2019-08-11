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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.xuexiang.xui.widget.imageview.strategy.IImageLoadStrategy;

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

    @Override
    public void loadGifImage(@NonNull ImageView imageView, Object path) {
        Glide.with(imageView.getContext())
                .asGif()
                .load(path)
                .into(imageView);
    }

    @Override
    public void loadImage(@NonNull ImageView imageView, Object path, DiskCacheStrategy strategy) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(strategy);
        Glide.with(imageView.getContext())
                .load(path)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void loadGifImage(@NonNull ImageView imageView, Object path, DiskCacheStrategy strategy) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(strategy);
        Glide.with(imageView.getContext())
                .asGif()
                .load(path)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void loadImage(@NonNull ImageView imageView, Object path, Drawable placeholder, DiskCacheStrategy strategy) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholder)
                .diskCacheStrategy(strategy);
        Glide.with(imageView.getContext())
                .load(path)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void loadGifImage(@NonNull ImageView imageView, Object path, Drawable placeholder, DiskCacheStrategy strategy) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholder)
                .diskCacheStrategy(strategy);
        Glide.with(imageView.getContext())
                .asGif()
                .load(path)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void loadImage(@NonNull ImageView imageView, Object path, int width, int height, Drawable placeholder, DiskCacheStrategy strategy) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .override(width, height)
                .placeholder(placeholder)
                .diskCacheStrategy(strategy);
        Glide.with(imageView.getContext())
                .load(path)
                .apply(options)
                .into(imageView);
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
}
