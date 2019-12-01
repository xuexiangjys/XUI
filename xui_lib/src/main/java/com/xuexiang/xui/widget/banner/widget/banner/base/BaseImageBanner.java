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

package com.xuexiang.xui.widget.banner.widget.banner.base;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem;
import com.xuexiang.xui.widget.imageview.ImageLoader;
import com.xuexiang.xui.widget.imageview.strategy.DiskCacheStrategyEnum;
import com.xuexiang.xui.widget.imageview.strategy.LoadOption;

import java.lang.ref.WeakReference;

/**
 * @author xuexiang
 * @since 2019-09-24 9:04
 */
public abstract class BaseImageBanner<T extends BaseImageBanner<T>> extends BaseIndicatorBanner<BannerItem, T> {

    /**
     * 默认加载图片
     */
    private Drawable mPlaceHolder;
    /**
     * 是否允许进行缓存
     */
    private boolean mEnableCache;
    /**
     * 加载图片的高／宽比率
     */
    private double mScale;

    public BaseImageBanner(Context context) {
        super(context);
        initImageBanner(context);
    }

    public BaseImageBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initImageBanner(context);
    }

    public BaseImageBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initImageBanner(context);
    }

    /**
     * 初始化ImageBanner
     *
     * @param context
     */
    protected void initImageBanner(Context context) {
        mPlaceHolder = new ColorDrawable(ResUtils.getColor(R.color.default_image_banner_placeholder_color));
        mEnableCache = true;
        mScale = getContainerScale();
    }

    @Override
    public void onTitleSelect(TextView tv, int position) {
        final BannerItem item = getItem(position);
        if (item != null) {
            tv.setText(item.title);
        }
    }

    /**
     * @return 轮播布局的ID
     */
    protected abstract int getItemLayoutId();

    /**
     * @return 图片控件的ID
     */
    protected abstract int getImageViewId();

    /**
     * 创建ViewPager的Item布局
     *
     * @param position
     */
    @Override
    public View onCreateItemView(int position) {
        View inflate = View.inflate(getContext(), getItemLayoutId(), null);
        ImageView iv = inflate.findViewById(getImageViewId());

        //解决Glide资源释放的问题，详细见http://blog.csdn.net/shangmingchao/article/details/51125554
        WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(iv);
        ImageView target = imageViewWeakReference.get();

        BannerItem item = getItem(position);
        if (item != null && target != null) {
            loadingImageView(target, item);
        }
        return inflate;
    }

    /**
     * 默认加载图片的方法，可以重写
     *
     * @param iv
     * @param item
     */
    protected void loadingImageView(ImageView iv, BannerItem item) {
        String imgUrl = item.imgUrl;
        if (!TextUtils.isEmpty(imgUrl)) {
            if (mScale > 0) {
                int itemWidth = getItemWidth();
                int itemHeight = (int) (itemWidth * mScale);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iv.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));

                LoadOption option = LoadOption.of(mPlaceHolder)
                        .setSize(itemWidth, itemHeight)
                        .setCacheStrategy(mEnableCache ? DiskCacheStrategyEnum.RESOURCE : DiskCacheStrategyEnum.NONE);
                ImageLoader.get().loadImage(iv, imgUrl, option);
            } else {
                ImageLoader.get().loadImage(iv, imgUrl, mPlaceHolder, mEnableCache ? DiskCacheStrategyEnum.RESOURCE : DiskCacheStrategyEnum.NONE);
            }
        } else {
            iv.setImageDrawable(mPlaceHolder);
        }
    }

    /**
     * 设置是否允许缓存
     *
     * @param enableCache
     * @return
     */
    public T enableCache(boolean enableCache) {
        mEnableCache = enableCache;
        return (T) this;
    }

    /**
     * 获取是否允许缓存
     *
     * @return
     */
    public boolean getEnableCache() {
        return mEnableCache;
    }

    public Drawable getPlaceHolderDrawable() {
        return mPlaceHolder;
    }

    public T setPlaceHolderDrawable(Drawable placeHolder) {
        mPlaceHolder = placeHolder;
        return (T) this;
    }

    public double getScale() {
        return mScale;
    }

    public T setScale(double scale) {
        mScale = scale;
        return (T) this;
    }

    @Override
    protected void onDetachedFromWindow() {
        //解决内存泄漏的问题
        pauseScroll();
        super.onDetachedFromWindow();
    }
}
