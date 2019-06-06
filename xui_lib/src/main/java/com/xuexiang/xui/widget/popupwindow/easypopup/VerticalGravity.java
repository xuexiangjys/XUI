package com.xuexiang.xui.widget.popupwindow.easypopup;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 垂直布局方式
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:05
 */
@IntDef({
        VerticalGravity.CENTER,
        VerticalGravity.ABOVE,
        VerticalGravity.BELOW,
        VerticalGravity.ALIGN_TOP,
        VerticalGravity.ALIGN_BOTTOM,
})
@Retention(RetentionPolicy.SOURCE)
public @interface VerticalGravity {
    int CENTER = 0;
    int ABOVE = 1;
    int BELOW = 2;
    int ALIGN_TOP = 3;
    int ALIGN_BOTTOM = 4;
}
