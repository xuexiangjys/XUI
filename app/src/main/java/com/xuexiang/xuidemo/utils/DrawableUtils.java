package com.xuexiang.xuidemo.utils;

import android.annotation.TargetApi;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;

/**
 * @author xuexiang
 * @since 2019/4/7 下午12:57
 */
public class DrawableUtils {

    private DrawableUtils() {
        throw new UnsupportedOperationException("Can not be instantiated.");
    }

    /**
     * 矩形
     * @param color
     * @param cornerRadius
     * @return
     */
    public static GradientDrawable createRectangleDrawable(int color, float cornerRadius) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(cornerRadius);
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }

    /**
     * 矩形
     * @param colors
     * @param cornerRadius
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static GradientDrawable createRectangleDrawable(int[] colors, float cornerRadius) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(cornerRadius);
        gradientDrawable.setColors(colors);
        return gradientDrawable;
    }

    /**
     * 圆形
     * @param color
     * @return
     */
    public static GradientDrawable createOvalDrawable(int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }

    /**
     * 圆形
     * @param colors
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static GradientDrawable createOvalDrawable(int[] colors) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setColors(colors);
        return gradientDrawable;
    }
}
