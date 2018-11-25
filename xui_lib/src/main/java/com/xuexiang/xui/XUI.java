package com.xuexiang.xui;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.text.TextUtils;

import com.xuexiang.xui.logs.UILog;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * UI全局设置
 *
 * @author xuexiang
 * @since 2018/11/14 上午11:40
 */
public class XUI {

    private static volatile XUI sInstance = null;

    private static Application sContext;

    private static boolean sIsTabletChecked;

    private static int sScreenType;

    private XUI() {

    }

    /**
     * 获取单例
     *
     * @return
     */
    public static XUI getInstance() {
        if (sInstance == null) {
            synchronized (XUI.class) {
                if (sInstance == null) {
                    sInstance = new XUI();
                }
            }
        }
        return sInstance;
    }

    //=======================初始化设置===========================//
    /**
     * 初始化
     *
     * @param context
     */
    public static void init(Application context) {
        sContext = context;
    }

    /**
     * 设置默认字体
     */
    public XUI initFontStyle(String defaultFontAssetPath) {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(defaultFontAssetPath)
                .setFontAttrId(R.attr.fontPath)
                .build());
        return this;
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
     * @param tag
     * @return
     */
    public static void debug(String tag) {
        UILog.debug(tag);
    }

    /**
     * 设置调试模式
     *
     * @param isDebug
     * @return
     */
    public static void debug(boolean isDebug) {
        UILog.debug(isDebug);
    }

    //=======================字体===========================//
    /**
     * @return 获取默认字体
     */
    public static Typeface getDefaultTypeface() {
        return TypefaceUtils.load(getContext().getAssets(), CalligraphyConfig.get().getFontPath());
    }

    /**
     * @param fontPath 字体路径
     * @return 获取默认字体
     */
    public static Typeface getDefaultTypeface(String fontPath) {
        if (TextUtils.isEmpty(fontPath)) {
            fontPath = CalligraphyConfig.get().getFontPath();
        }
        if (!TextUtils.isEmpty(fontPath)) {
            return TypefaceUtils.load(getContext().getAssets(), fontPath);
        }
        return null;
    }

    //=======================屏幕尺寸===========================//

    /**
     * 检验设备屏幕的尺寸
     * @param context
     * @return
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
     * @return
     */
    public static boolean isTablet() {
        return getScreenType() == UIConsts.ScreenType.SMALL_TABLET || getScreenType() == UIConsts.ScreenType.BIG_TABLET;
    }


}
