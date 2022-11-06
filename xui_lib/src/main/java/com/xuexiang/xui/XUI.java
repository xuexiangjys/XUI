package com.xuexiang.xui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.xuexiang.xui.logs.UILog;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.utils.ViewUtils;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.calligraphy3.TypefaceUtils;
import io.github.inflationx.viewpump.ViewPump;

/**
 * UI全局设置
 *
 * @author xuexiang
 * @since 2018/11/14 上午11:40
 */
public class XUI {

    private static Application sContext;

    private static boolean sIsTabletChecked;

    private static int sScreenType;

    private static String sDefaultFontAssetPath;

    //=======================初始化设置===========================//

    /**
     * 初始化
     *
     * @param context 上下文
     */
    public static void init(Application context) {
        sContext = context;
    }

    /**
     * 设置默认字体
     */
    public static void initFontStyle(String defaultFontAssetPath) {
        if (!TextUtils.isEmpty(defaultFontAssetPath)) {
            sDefaultFontAssetPath = defaultFontAssetPath;
            ViewPump.init(ViewPump.builder()
                    .addInterceptor(new CalligraphyInterceptor(
                            new CalligraphyConfig.Builder()
                                    .setDefaultFontPath(defaultFontAssetPath)
                                    .setFontAttrId(R.attr.fontPath)
                                    .build()))
                    .build());
        }
    }

    public static Context getContext() {
        testInitialize();
        return sContext;
    }

    private static void testInitialize() {
        if (sContext == null) {
            throw new ExceptionInInitializerError("请先在全局Application中调用 XUI.init() 初始化！");
        }
    }

    //=======================日志调试===========================//

    /**
     * 设置调试模式
     *
     * @param tag 标志
     */
    public static void debug(String tag) {
        UILog.debug(tag);
    }

    /**
     * 设置调试模式
     *
     * @param isDebug 是否是调试模式
     */
    public static void debug(boolean isDebug) {
        UILog.debug(isDebug);
    }

    //=======================字体===========================//

    /**
     * 设置控件的字体【用于遗漏的控件字体设置】
     *
     * @param views 控件集合
     */
    public static void setViewsFont(View... views) {
        ViewUtils.setViewsFont(views);
    }

    /**
     * @return 获取默认字体
     */
    @Nullable
    public static Typeface getDefaultTypeface() {
        if (!TextUtils.isEmpty(sDefaultFontAssetPath)) {
            return TypefaceUtils.load(getContext().getAssets(), sDefaultFontAssetPath);
        }
        return null;
    }

    /**
     * @return 默认字体的存储位置
     */
    public static String getDefaultFontAssetPath() {
        return sDefaultFontAssetPath;
    }

    /**
     * @param fontPath 字体路径
     * @return 获取默认字体
     */
    @Nullable
    public static Typeface getDefaultTypeface(String fontPath) {
        if (TextUtils.isEmpty(fontPath)) {
            fontPath = sDefaultFontAssetPath;
        }
        if (!TextUtils.isEmpty(fontPath)) {
            return TypefaceUtils.load(getContext().getAssets(), fontPath);
        }
        return null;
    }

    //=======================屏幕尺寸===========================//

    /**
     * 检验设备屏幕的尺寸
     *
     * @param context 上下文
     * @return 屏幕的尺寸类型
     */
    private static int checkScreenSize(Context context) {
        int screenSize = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (screenSize >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            //证明是平板
            if (screenSize >= Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                return UIConsts.ScreenType.BIG_TABLET;
            } else {
                return UIConsts.ScreenType.SMALL_TABLET;
            }
        } else {
            return UIConsts.ScreenType.PHONE;
        }
    }

    /**
     * 判断是否平板设备
     *
     * @return true:平板,false:手机
     */
    public static int getScreenType() {
        if (sIsTabletChecked) {
            return sScreenType;
        }
        sScreenType = checkScreenSize(XUI.getContext());
        sIsTabletChecked = true;
        return sScreenType;
    }

    /**
     * 是否是平板
     *
     * @return
     */
    public static boolean isTablet() {
        return getScreenType() == UIConsts.ScreenType.SMALL_TABLET || getScreenType() == UIConsts.ScreenType.BIG_TABLET;
    }

    /**
     * 初始化主题
     *
     * @param activity
     */
    public static void initTheme(Activity activity) {
        int screenType = getScreenType();
        if (screenType == UIConsts.ScreenType.PHONE) {
            activity.setTheme(R.style.XUITheme_Phone);
        } else if (screenType == UIConsts.ScreenType.SMALL_TABLET) {
            activity.setTheme(R.style.XUITheme_Tablet_Small);
        } else {
            activity.setTheme(R.style.XUITheme_Tablet_Big);
        }
    }

    /**
     * 获取主题色
     *
     * @param context 上下文
     * @return 主题色
     */
    @ColorInt
    public static int getMainThemeColor(Context context) {
        return ThemeUtils.getMainThemeColor(context);
    }

}
