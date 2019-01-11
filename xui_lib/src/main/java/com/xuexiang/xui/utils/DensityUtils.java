package com.xuexiang.xui.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * 屏幕密度工具类
 *
 * @author xuexiang
 * @since 2018/12/18 上午12:15
 */
public final class DensityUtils {

    private DensityUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * DisplayMetrics
     *
     * @return
     */
    public static DisplayMetrics getDisplayMetrics() {
        return ResUtils.getResources().getDisplayMetrics();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dpValue 尺寸dip
     * @return 像素值
     */
    public static int dp2px(float dpValue) {
        final float scale = ResUtils.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context 上下文
     * @param dpValue 尺寸dip
     * @return 像素值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param pxValue 尺寸像素
     * @return DIP值
     */
    public static int px2dp(float pxValue) {
        final float scale = ResUtils.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context 上下文
     * @param pxValue 尺寸像素
     * @return DIP值
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     *
     * @param pxValue 尺寸像素
     * @return SP值
     */
    public static int px2sp(float pxValue) {
        float fontScale = ResUtils.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     *
     * @param pxValue 尺寸像素
     * @return SP值
     */
    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px
     *
     * @param spValue SP值
     * @return 像素值
     */
    public static int sp2px(float spValue) {
        float fontScale = ResUtils.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px
     *
     * @param spValue SP值
     * @return 像素值
     */
    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕分辨率
     *
     * @return 屏幕分辨率幕高度
     */
    public static int getScreenDpi() {
        return getDisplayMetrics().densityDpi;
    }

    /**
     * 获取真实屏幕密度
     *
     * @param context 上下文【注意，Application和Activity的屏幕密度是不一样的】
     * @return
     */
    public static int getRealDpi(Context context) {
        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        float xdpi = metric.xdpi;
        float ydpi = metric.ydpi;

        return (int) (((xdpi + ydpi) / 2.0F) + 0.5F);
    }

}
