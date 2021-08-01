package com.xuexiang.xui.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.AnimRes;
import androidx.annotation.ArrayRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.annotation.StyleableRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.xuexiang.xui.XUI;

/**
 * 获取res中的资源
 *
 * @author xuexiang
 * @since 2018/12/18 上午12:14
 */
public final class ResUtils {

    private ResUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取resources对象
     *
     * @return resources对象
     */
    public static Resources getResources() {
        return XUI.getContext().getResources();
    }

    /**
     * 获取字符串
     *
     * @param resId 资源id
     * @return 字符串
     */
    public static String getString(@StringRes int resId) {
        return getResources().getString(resId);
    }

    /**
     * 获取资源图片
     *
     * @param resId 图片资源id
     * @return 资源图片
     */
    public static Drawable getDrawable(@DrawableRes int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return XUI.getContext().getDrawable(resId);
        }
        return getResources().getDrawable(resId);
    }

    /**
     * 获取资源图片【和主题有关】
     *
     * @param resId 图片资源id
     * @return 资源图片
     */
    public static Drawable getDrawable(Context context, @DrawableRes int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(resId);
        }
        return AppCompatResources.getDrawable(context, resId);
    }

    /**
     * 获取svg资源图片
     *
     * @param context 上下文
     * @param resId   图片资源id
     * @return svg资源图片
     */
    public static Drawable getVectorDrawable(Context context, @DrawableRes int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(resId);
        }
        return AppCompatResources.getDrawable(context, resId);
    }

    /**
     * 获取Drawable属性（兼容VectorDrawable）
     *
     * @param context        上下文
     * @param typedArray     样式属性数组
     * @param styleableResId 样式资源ID
     * @return Drawable
     */
    public static Drawable getDrawableAttrRes(Context context, TypedArray typedArray, @StyleableRes int styleableResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return typedArray.getDrawable(styleableResId);
        } else {
            int resourceId = typedArray.getResourceId(styleableResId, -1);
            if (resourceId != -1) {
                return AppCompatResources.getDrawable(context, resourceId);
            }
        }
        return null;
    }

    /**
     * 获取ColorStateList属性（兼容?attr属性）
     *
     * @param context        上下文
     * @param typedArray     样式属性数组
     * @param styleableResId 样式资源ID
     * @return ColorStateList
     */
    public static ColorStateList getColorStateListAttrRes(Context context, TypedArray typedArray, @StyleableRes int styleableResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return typedArray.getColorStateList(styleableResId);
        } else {
            int resourceId = typedArray.getResourceId(styleableResId, -1);
            if (resourceId != -1) {
                return AppCompatResources.getColorStateList(context, resourceId);
            }
        }
        return null;
    }

    /**
     * 获取dimes值，返回的是精确的值
     *
     * @param resId 资源id
     * @return dimes值，返回的是精确的值
     */
    public static float getDimens(@DimenRes int resId) {
        return getResources().getDimension(resId);
    }

    /**
     * 获取Color值
     *
     * @param resId 资源id
     * @return Color值
     */
    public static int getColor(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 获取ColorStateList值
     *
     * @param resId 资源id
     * @return ColorStateList值
     */
    public static ColorStateList getColors(@ColorRes int resId) {
        return getResources().getColorStateList(resId);
    }

    /**
     * 获取dimes值，返回的是【去余取整】的值
     *
     * @param resId 资源id
     * @return dimes值【去余取整】
     */
    public static int getDimensionPixelOffset(@DimenRes int resId) {
        return getResources().getDimensionPixelOffset(resId);
    }

    /**
     * 获取dimes值，返回的是【4舍5入取整】的值
     *
     * @param resId 资源id
     * @return dimes值【4舍5入取整】
     */
    public static int getDimensionPixelSize(@DimenRes int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获取字符串的数组
     *
     * @param resId 资源id
     * @return 字符串的数组
     */
    public static String[] getStringArray(@ArrayRes int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 获取Drawable的数组
     *
     * @param context context
     * @param resId   资源id
     * @return Drawable的数组
     */
    public static Drawable[] getDrawableArray(Context context, @ArrayRes int resId) {
        TypedArray ta = getResources().obtainTypedArray(resId);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(context, id);
            }
        }
        ta.recycle();
        return icons;
    }

    /**
     * 获取数字的数组
     *
     * @param resId 数组资源id
     * @return 数字的数组
     */
    public static int[] getIntArray(@ArrayRes int resId) {
        return getResources().getIntArray(resId);
    }

    /**
     * 获取动画
     *
     * @param resId 动画资源id
     * @return 动画
     */
    public static Animation getAnim(@AnimRes int resId) {
        return AnimationUtils.loadAnimation(XUI.getContext(), resId);
    }

    /**
     * Check if layout direction is RTL
     *
     * @return {@code true} if the layout direction is right-to-left
     */
    public static boolean isRtl() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }


    /**
     * Check if layout direction is RTL
     *
     * @param context context
     * @return {@code true} if the layout direction is right-to-left
     */
    public static boolean isRtl(@NonNull Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    /**
     * Darkens a color by a given factor.
     *
     * @param color  the color to darken
     * @param factor The factor to darken the color.
     * @return darker version of specified color.
     */
    public static int darker(int color, float factor) {
        return Color.argb(Color.alpha(color), Math.max((int) (Color.red(color) * factor), 0),
                Math.max((int) (Color.green(color) * factor), 0),
                Math.max((int) (Color.blue(color) * factor), 0));
    }

    /**
     * Lightens a color by a given factor.
     *
     * @param color  The color to lighten
     * @param factor The factor to lighten the color. 0 will make the color unchanged. 1 will make the
     *               color white.
     * @return lighter version of the specified color.
     */
    public static int lighter(int color, float factor) {
        int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
        int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
        int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
        return Color.argb(Color.alpha(color), red, green, blue);
    }

}
