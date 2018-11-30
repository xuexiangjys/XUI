package com.xuexiang.xui.widget.alpha;

import android.view.View;

/**
 * 透明度辅助工具
 *
 * @author xuexiang
 * @since 2018/11/30 下午1:42
 */
public interface IAlphaViewHelper {

    /**
     * 处理setPressed
     * @param current
     * @param pressed
     */
    void onPressedChanged(View current, boolean pressed);

    /**
     * 处理setEnabled
     * @param current
     * @param enabled
     */
    void onEnabledChanged(View current, boolean enabled);

    /**
     * 设置是否要在 press 时改变透明度
     *
     * @param changeAlphaWhenPress 是否要在 press 时改变透明度
     */
    void setChangeAlphaWhenPress(boolean changeAlphaWhenPress);

    /**
     * 设置是否要在 disabled 时改变透明度
     *
     * @param changeAlphaWhenDisable 是否要在 disabled 时改变透明度
     */
    void setChangeAlphaWhenDisable(boolean changeAlphaWhenDisable);
}
