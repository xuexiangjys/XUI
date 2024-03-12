package com.xuexiang.xui.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.xuexiang.xui.XUI;

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
     * @return 屏幕密度
     */
    @Deprecated
    public static DisplayMetrics getDisplayMetrics() {
        return ResUtils.getResources().getDisplayMetrics();
    }

    /**
     * DisplayMetrics
     *
     * @return 屏幕密度
     */
    public static DisplayMetrics getDisplayMetrics(@NonNull Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dpValue 尺寸dip
     * @return 像素值
     */
    @Deprecated
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
    @Deprecated
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
    @Deprecated
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
    @Deprecated
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
    @Deprecated
    public static int getScreenDpi() {
        return getDisplayMetrics().densityDpi;
    }

    /**
     * 获取屏幕分辨率
     *
     * @param context 上下文
     * @return 屏幕分辨率幕高度
     */
    public static int getScreenDpi(@NonNull Context context) {
        return getDisplayMetrics(context).densityDpi;
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

    /**
     * 获取应用窗口的尺寸
     *
     * @param activity 应用窗口
     * @param isReal   是否是真实的尺寸
     * @return 应用窗口的尺寸
     */
    public static Point getAppSize(Activity activity, boolean isReal) {
        return getDisplaySize(activity, isReal);
    }

    /**
     * 获取屏幕的尺寸
     *
     * @param isReal 是否是真实的尺寸
     * @return 屏幕的尺寸
     */
    public static Point getScreenSize(boolean isReal) {
        return getDisplaySize(XUI.getContext(), isReal);
    }

    /**
     * 获取上下文所在的尺寸
     *
     * @param context 上下文
     * @param isReal  是否是真实的尺寸
     * @return 上下文所在的尺寸
     */
    public static Point getDisplaySize(Context context, boolean isReal) {
        WindowManager windowManager;
        if (context instanceof Activity) {
            windowManager = ((Activity) context).getWindowManager();
        } else {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (windowManager == null) {
            return null;
        }
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        if (isReal) {
            display.getRealSize(point);
        } else {
            display.getSize(point);
        }
        return point;
    }

    /**
     * 获取上下文所在的宽度
     *
     * @param context 上下文
     * @param isReal  是否是真实的尺寸
     * @return 上下文所在的宽度
     */
    public static int getDisplayWidth(Context context, boolean isReal) {
        Point point = getDisplaySize(context, isReal);
        return point != null ? point.x : 0;
    }

    /**
     * 获取上下文所在的高度
     *
     * @param context 上下文
     * @param isReal  是否是真实的尺寸
     * @return 上下文所在的高度
     */
    public static int getDisplayHeight(Context context, boolean isReal) {
        Point point = getDisplaySize(context, isReal);
        return point != null ? point.y : 0;
    }

    /**
     * 获取应用窗口的度量信息
     *
     * @param activity 应用窗口
     * @param isReal   是否是真实的度量信息
     * @return 应用窗口的度量信息
     */
    public static DisplayMetrics getAppMetrics(Activity activity, boolean isReal) {
        return getDisplayMetrics(activity, isReal);
    }

    /**
     * 获取屏幕的度量信息
     *
     * @param isReal 是否是真实的度量信息
     * @return 屏幕的度量信息
     */
    public static DisplayMetrics getScreenMetrics(boolean isReal) {
        return getDisplayMetrics(XUI.getContext(), isReal);
    }

    /**
     * 获取上下文所在的度量信息
     *
     * @param context 上下文
     * @param isReal  是否是真实的度量信息
     * @return 上下文所在的度量信息
     */
    public static DisplayMetrics getDisplayMetrics(Context context, boolean isReal) {
        WindowManager windowManager;
        if (context instanceof Activity) {
            windowManager = ((Activity) context).getWindowManager();
        } else {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (windowManager == null) {
            return null;
        }
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        if (isReal) {
            display.getRealMetrics(metrics);
        } else {
            display.getMetrics(metrics);
        }
        return metrics;
    }


    /**
     * 底部导航条是否开启
     *
     * @param context 上下文
     * @return 底部导航条是否显示
     */
    public static boolean isNavigationBarExist(Context context) {
        return getNavigationBarHeight(context) > 0;
    }

    /**
     * 获取系统底部导航栏的高度
     *
     * @param context 上下文
     * @return 系统状态栏的高度
     */
    public static int getNavigationBarHeight(Context context) {
        WindowManager windowManager;
        if (context instanceof Activity) {
            windowManager = ((Activity) context).getWindowManager();
        } else {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (windowManager == null) {
            return 0;
        }
        Display defaultDisplay = windowManager.getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            defaultDisplay.getRealMetrics(realDisplayMetrics);
        }
        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        if (realHeight - displayHeight > 0) {
            return realHeight - displayHeight;
        }
        return Math.max(realWidth - displayWidth, 0);
    }


}
