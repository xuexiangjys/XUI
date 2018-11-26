package com.xuexiang.xuidemo.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Snackbar工具类
 *
 * @author xuexiang
 * @since 2018/11/26 下午3:12
 */
public class SnackbarUtils {

    private static Snackbar.SnackbarLayout getSnackbarLayout(Snackbar snackbar) {
        final Snackbar.SnackbarLayout snackbarView = (Snackbar.SnackbarLayout) snackbar.getView();
        return snackbarView;
    }


    public static Snackbar shortMake(View view, CharSequence message) {
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        return snackbar;
    }

    public static Snackbar shortMake(View view, int messageId) {
        final Snackbar snackbar = Snackbar.make(view, messageId, Snackbar.LENGTH_LONG);
        return snackbar;
    }

    public static Snackbar longMake(View view, CharSequence message) {
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        return snackbar;
    }

    public static Snackbar longMake(View view, int messageId) {
        final Snackbar snackbar = Snackbar.make(view, messageId, Snackbar.LENGTH_LONG);
        return snackbar;
    }

    public static Snackbar indefiniteMake(View view, CharSequence message) {
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        return snackbar;
    }

    public static Snackbar indefiniteMake(View view, int messageId) {
        final Snackbar snackbar = Snackbar.make(view, messageId, Snackbar.LENGTH_INDEFINITE);
        return snackbar;
    }

    /**
     * 方法描述：设置Snackbar的透明度
     *
     * @param snackbar
     * @param alpha
     */
    public static void setSnackbarAlpha(Snackbar snackbar, float alpha) {
        final Snackbar.SnackbarLayout snackbarView = getSnackbarLayout(snackbar);
        if (snackbarView == null) return;
        snackbarView.setAlpha(alpha);
    }

    /**
     * 方法描述：设置Snackbar的背景颜色
     *
     * @param snackbar
     * @param color
     */
    public static void setSnackbarBackgroundColor(Snackbar snackbar, int color) {
        final Snackbar.SnackbarLayout snackbarView = getSnackbarLayout(snackbar);
        if (snackbarView == null) return;
        snackbarView.setBackgroundColor(color);
    }

    /**
     * 方法描述：设置Snackbar的背景图片资源
     *
     * @param snackbar
     * @param resId
     */
    public static void setSnackbarBackgroudResource(Snackbar snackbar, int resId) {
        final Snackbar.SnackbarLayout snackbarView = getSnackbarLayout(snackbar);
        if (snackbarView == null) return;
        snackbarView.setBackgroundResource(resId);
    }

    /**
     * 方法描述：设置Snackbar中Action的字体颜色
     *
     * @param snackbar
     * @param color
     */
    public static void setActionTextColor(Snackbar snackbar, int color) {
        //snackbar.setActionTextColor(color);
        final Snackbar.SnackbarLayout snackbarView = getSnackbarLayout(snackbar);
        if (snackbarView == null) return;
        final Button snackbar_action = (Button) snackbarView.findViewById(android.support.design.R.id.snackbar_action);
        snackbar_action.setTextColor(color);
    }

    /**
     * 方法描述：设置Snackbar中Action的字体大小
     *
     * @param snackbar
     * @param size     Action的字体大小(单位为sp)
     */
    public static void setActionTextSize(Snackbar snackbar, float size) {
        final Snackbar.SnackbarLayout snackbarView = getSnackbarLayout(snackbar);
        if (snackbarView == null) return;
        final Button snackbar_action = (Button) snackbarView.findViewById(android.support.design.R.id.snackbar_action);
        snackbar_action.setTextSize(sp2px(snackbarView.getContext(), size));
    }

    /**
     * 方法描述：设置SnackBar左侧TextView控件的文字颜色和大小
     *
     * @param snackbar
     * @param color    TextView控件的文字颜色
     * @param size     TextView控件的文字大小
     */
    public static void setTextColorAndSize(Snackbar snackbar, @Nullable int color, @Nullable float size) {
        final Snackbar.SnackbarLayout snackbarView = getSnackbarLayout(snackbar);
        if (snackbarView == null) return;
        final TextView snackbar_text = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        if (color != 0) {
            snackbar_text.setTextColor(color);
        }
        if (size != 0) {
            snackbar_text.setTextSize(size);
        }
    }


    /**
     * 方法描述：在Snackbar左侧添加icon
     *
     * @param snackbar      Snackbar实例
     * @param drawableResId 添加的icon资源ID
     * @param sizeDp        icon的宽度与高度值
     */
    public static void setIconLeft(Snackbar snackbar, int drawableResId, float sizeDp) {
        final Snackbar.SnackbarLayout snackbarView = (Snackbar.SnackbarLayout) getSnackbarLayout(snackbar);
        if (snackbarView == null) return;
        //snackbar不同于Toast，snackbar依赖于Activity而存在
        final Context mContext = snackbarView.getContext();
        if (mContext == null) {
            return;
        }
        final TextView snackbar_text = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);

        Drawable drawable = ContextCompat.getDrawable(mContext, drawableResId);
        if (drawable != null) {
            drawable = fitDrawable(mContext.getResources(), drawable, (int) convertDpToPixel(sizeDp, mContext));
        } else {
            throw new IllegalArgumentException("resource_id is not a valid drawable!");
        }

        final Drawable[] compoundDrawables = snackbar_text.getCompoundDrawables();
        snackbar_text.setCompoundDrawables(drawable, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]);
    }

    /**
     * 方法描述：将drawable压缩为指定宽高的drawable
     *
     * @param resources
     * @param drawable  原始drawable
     * @param sizePx    指定的drawable压缩宽高
     * @return
     */
    private static Drawable fitDrawable(Resources resources, Drawable drawable, int sizePx) {
        if (drawable.getIntrinsicWidth() != sizePx || drawable.getIntrinsicHeight() != sizePx) {
            if (drawable instanceof BitmapDrawable) {
                drawable = new BitmapDrawable(resources, Bitmap.createScaledBitmap(getBitmap(drawable), sizePx, sizePx, true));
            }
        }
        drawable.setBounds(0, 0, sizePx, sizePx);

        return drawable;
    }

    /**
     * 方法描述：将Drawable转化为Bitmap
     *
     * @param drawable
     * @return
     */
    private static Bitmap getBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    /**
     * 方法描述：将VectorDrawable转化为Bitmap
     *
     * @param vectorDrawable
     * @return
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    /**
     * 方法描述：dp转化为px
     *
     * @param dpValue
     * @param context
     * @return
     */
    private static float convertDpToPixel(float dpValue, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int convertSpToPixel(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
