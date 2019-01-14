package com.xuexiang.xui.widget.banner.widget.banner;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xuexiang.xui.R;
import com.xuexiang.xui.widget.banner.widget.banner.base.BaseIndicatorBanner;
import com.xuexiang.xui.widget.banner.widget.banner.base.GlideImageLoader;
import com.xuexiang.xui.widget.banner.widget.banner.base.ImageLoader;

import java.lang.ref.WeakReference;

/**
 * 简单的图片轮播
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:07
 */
public class SimpleImageBanner extends BaseIndicatorBanner<BannerItem, SimpleImageBanner> {
    /**
     * 默认加载图片
     */
    private ColorDrawable mColorDrawable;
    /**
     * 是否允许进行缓存
     */
    private boolean mEnableCache = true;
    /**
     * 高／宽比率
     */
    private double mScale = 0.5625D;

    private ImageLoader mImageLoader;

    public SimpleImageBanner(Context context) {
        super(context);
        mColorDrawable = new ColorDrawable(Color.parseColor("#555555"));
    }

    public SimpleImageBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        mColorDrawable = new ColorDrawable(Color.parseColor("#555555"));
    }

    public SimpleImageBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mColorDrawable = new ColorDrawable(Color.parseColor("#555555"));
    }

    @Override
    public void onTitleSelect(TextView tv, int position) {
        final BannerItem item = getItem(position);
        if (item != null) {
            tv.setText(item.title);
        }
    }

    @Override
    public View onCreateItemView(int position) {
        View inflate = View.inflate(mContext, R.layout.xui_adapter_simple_image, null);
        ImageView iv = inflate.findViewById(R.id.iv);

        //解决Glide资源释放的问题，详细见http://blog.csdn.net/shangmingchao/article/details/51125554
        WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(iv);
        ImageView target = imageViewWeakReference.get();

        BannerItem item = getItem(position);
        if (item != null && target != null) {
            loadingImageView(target, item);
        }
        return inflate;
    }

    private ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = new GlideImageLoader();
        }
        return  mImageLoader;
    }

    /**
     * 加载图片
     *
     * @param iv
     * @param item
     */
    protected void loadingImageView(ImageView iv, BannerItem item) {
        int itemWidth = mDisplayMetrics.widthPixels;
        int itemHeight = (int) (itemWidth * mScale);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));

        String imgUrl = item.imgUrl;

        if (!TextUtils.isEmpty(imgUrl)) {
            getImageLoader().displayImage(mContext, imgUrl, iv,
                    itemWidth, itemHeight, mColorDrawable,
                    mEnableCache ? DiskCacheStrategy.RESOURCE : DiskCacheStrategy.NONE);
        } else {
            iv.setImageDrawable(mColorDrawable);
        }
    }

    /**
     * 设置是否允许缓存
     *
     * @param enableCache
     * @return
     */
    public SimpleImageBanner enableCache(boolean enableCache) {
        mEnableCache = enableCache;
        return this;
    }

    /**
     * 获取是否允许缓存
     *
     * @return
     */
    public boolean getEnableCache() {
        return mEnableCache;
    }

    public ColorDrawable getColorDrawable() {
        return mColorDrawable;
    }

    public SimpleImageBanner setColorDrawable(ColorDrawable colorDrawable) {
        mColorDrawable = colorDrawable;
        return this;
    }

    public double getScale() {
        return mScale;
    }

    public SimpleImageBanner setScale(double scale) {
        mScale = scale;
        return this;
    }

    public SimpleImageBanner setImageLoader(ImageLoader imageLoader) {
        mImageLoader = imageLoader;
        return this;
    }

    //解决内存泄漏的问题
    @Override
    protected void onDetachedFromWindow() {
        pauseScroll();
        super.onDetachedFromWindow();
    }
}
