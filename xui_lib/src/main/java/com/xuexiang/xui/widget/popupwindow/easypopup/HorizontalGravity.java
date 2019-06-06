package com.xuexiang.xui.widget.popupwindow.easypopup;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 水平布局方式
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:07
 */
@IntDef({
        HorizontalGravity.CENTER,
        HorizontalGravity.LEFT,
        HorizontalGravity.RIGHT,
        HorizontalGravity.ALIGN_LEFT,
        HorizontalGravity.ALIGN_RIGHT,
})
@Retention(RetentionPolicy.SOURCE)
public @interface HorizontalGravity {
    int CENTER = 0;
    int LEFT = 1;
    int RIGHT = 2;
    int ALIGN_LEFT = 3;
    int ALIGN_RIGHT = 4;
}