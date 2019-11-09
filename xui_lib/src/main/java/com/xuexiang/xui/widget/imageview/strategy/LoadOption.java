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

package com.xuexiang.xui.widget.imageview.strategy;

import android.graphics.drawable.Drawable;

/**
 * 加载选项
 *
 * @author xuexiang
 * @since 2019-11-09 11:12
 */
public class LoadOption {
    /**
     * 磁盘缓存策略
     */
    public DiskCacheStrategyEnum cacheStrategy;
    /**
     * 占位图
     */
    public Drawable placeholder;
    /**
     * 宽度
     */
    public int width;
    /**
     * 高度
     */
    public int height;

    public static LoadOption of(DiskCacheStrategyEnum cacheStrategy) {
        return new LoadOption(cacheStrategy);
    }

    public static LoadOption of(Drawable placeholder) {
        return new LoadOption(placeholder);
    }

    public LoadOption() {
    }

    public LoadOption(DiskCacheStrategyEnum cacheStrategy) {
        this.cacheStrategy = cacheStrategy;
    }

    public LoadOption(Drawable placeholder) {
        this.placeholder = placeholder;
    }

    public DiskCacheStrategyEnum getCacheStrategy() {
        return cacheStrategy;
    }

    public LoadOption setCacheStrategy(DiskCacheStrategyEnum cacheStrategy) {
        this.cacheStrategy = cacheStrategy;
        return this;
    }

    public Drawable getPlaceholder() {
        return placeholder;
    }

    public LoadOption setPlaceholder(Drawable placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    /**
     * 设置加载图片的宽高
     *
     * @param width  宽
     * @param height 高
     * @return
     */
    public LoadOption setSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public boolean hasSize() {
        return width != 0 && height != 0;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public String toString() {
        return "LoadOption{" +
                "cacheStrategy=" + cacheStrategy +
                ", placeholder=" + placeholder +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
