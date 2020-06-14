package com.xuexiang.xuidemo.utils;

import android.content.Context;

import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.data.BaseSPUtil;

/**
 * SharedPreferences管理工具基类
 *
 * @author xuexiang
 * @since 2018/11/27 下午5:16
 */
public class SettingSPUtils extends BaseSPUtil {

    private static volatile SettingSPUtils sInstance = null;

    private SettingSPUtils(Context context) {
        super(context);
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static SettingSPUtils getInstance() {
        if (sInstance == null) {
            synchronized (SettingSPUtils.class) {
                if (sInstance == null) {
                    sInstance = new SettingSPUtils(XUtil.getContext());
                }
            }
        }
        return sInstance;
    }


    private final String IS_FIRST_OPEN_KEY = "is_first_open_key";

    private final String IS_AGREE_PRIVACY_KEY = "is_agree_privacy_key";

    private final String IS_USE_CUSTOM_THEME_KEY = "is_use_custom_theme_key";

    private final String IS_USE_CUSTOM_FONT_KEY = "is_use_custom_font_key";

    /**
     * 是否是第一次启动
     */
    public boolean isFirstOpen() {
        return getBoolean(IS_FIRST_OPEN_KEY, true);
    }

    /**
     * 设置是否是第一次启动
     */
    public void setIsFirstOpen(boolean isFirstOpen) {
        putBoolean(IS_FIRST_OPEN_KEY, isFirstOpen);
    }

    /**
     * @return 是否同意隐私政策
     */
    public boolean isAgreePrivacy() {
        return getBoolean(IS_AGREE_PRIVACY_KEY, false);
    }

    public void setIsAgreePrivacy(boolean isAgreePrivacy) {
        putBoolean(IS_AGREE_PRIVACY_KEY, isAgreePrivacy);
    }

    public boolean isUseCustomTheme() {
        return getBoolean(IS_USE_CUSTOM_THEME_KEY, false);
    }

    public void setIsUseCustomTheme(boolean isUseCustomTheme) {
        putBoolean(IS_USE_CUSTOM_THEME_KEY, isUseCustomTheme);
    }

    public boolean isUseCustomFont() {
        return getBoolean(IS_USE_CUSTOM_FONT_KEY, false);
    }

    public void setIsUseCustomFont(boolean isUseCustomFont) {
        putBoolean(IS_USE_CUSTOM_FONT_KEY, isUseCustomFont);
    }



}
