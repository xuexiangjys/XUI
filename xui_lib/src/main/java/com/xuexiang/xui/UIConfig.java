package com.xuexiang.xui;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.xuexiang.xui.utils.Utils;
import com.xuexiang.xui.widget.statelayout.StateLayoutConfig;

/**
 * UI动态配置
 *
 * @author xuexiang
 * @since 2018/11/26 上午12:03
 */
public class UIConfig {

    private static volatile UIConfig sInstance = null;

    /**
     * StatefulLayout的默认配置信息
     */
    private StateLayoutConfig mStateLayoutConfig;
    /**
     * 应用的图标
     */
    private Drawable mAppIcon;

    private UIConfig(@NonNull Context context) {
        mStateLayoutConfig = new StateLayoutConfig(context);
        mAppIcon = Utils.getAppIcon(context);
    }

    /**
     * 获取单例
     *
     * @param context 上下文
     * @return UI动态配置
     */
    public static UIConfig getInstance(@NonNull Context context) {
        if (sInstance == null) {
            synchronized (UIConfig.class) {
                if (sInstance == null) {
                    sInstance = new UIConfig(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    //==================多状态布局=====================//

    /**
     * 设置StatefulLayout的默认配置信息
     *
     * @param config 配置
     */
    public UIConfig setStatefulLayoutConfig(StateLayoutConfig config) {
        mStateLayoutConfig = config;
        return this;
    }

    public StateLayoutConfig getStateLayoutConfig() {
        return mStateLayoutConfig;
    }

    //==================应用=====================//

    public UIConfig setAppIcon(Drawable appIcon) {
        mAppIcon = appIcon;
        return this;
    }

    public Drawable getAppIcon() {
        return mAppIcon;
    }
}
