package com.xuexiang.xui;

import android.graphics.drawable.Drawable;

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


    private UIConfig() {
        mStateLayoutConfig = new StateLayoutConfig();
        mAppIcon = Utils.getAppIcon(XUI.getContext());
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static UIConfig getInstance() {
        if (sInstance == null) {
            synchronized (UIConfig.class) {
                if (sInstance == null) {
                    sInstance = new UIConfig();
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
