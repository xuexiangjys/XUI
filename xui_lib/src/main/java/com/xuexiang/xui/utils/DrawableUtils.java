/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.xui.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.NestedScrollView;

import java.security.InvalidParameterException;

/**
 * Drawable工具类
 *
 * @author xuexiang
 * @since 2019/1/3 下午3:47
 */
public final class DrawableUtils {

    private DrawableUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static final String TAG = DrawableUtils.class.getSimpleName();

    /**
     * 节省每次创建时产生的开销，但要注意多线程操作synchronized
     */
    private static final Canvas CANVAS = new Canvas();

    /**
     * 从一个view创建Bitmap。
     * 注意点：绘制之前要清掉 View 的焦点，因为焦点可能会改变一个 View 的 UI 状态。
     * 来源：https://github.com/tyrantgit/ExplosionField
     *
     * @param view  传入一个 View，会获取这个 View 的内容创建 Bitmap。
     * @param scale 缩放比例，对创建的 Bitmap 进行缩放，数值支持从 0 到 1。
     */
    public static Bitmap createBitmapFromView(View view, float scale) {
        if (view instanceof ImageView) {
            Drawable drawable = ((ImageView) view).getDrawable();
            if (drawable != null && drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }
        }
        view.clearFocus();
        int viewHeight = 0;
        if (view instanceof ScrollView) {
            for (int i = 0; i < ((ScrollView) view).getChildCount(); i++) {
                viewHeight += ((ScrollView) view).getChildAt(i).getHeight();
            }
        } else if (view instanceof NestedScrollView) {
            for (int i = 0; i < ((NestedScrollView) view).getChildCount(); i++) {
                viewHeight += ((NestedScrollView) view).getChildAt(i).getHeight();
            }
        } else {
            viewHeight = view.getHeight();
        }

        Bitmap bitmap = createBitmapSafely((int) (view.getWidth() * scale),
                (int) (viewHeight * scale), Bitmap.Config.ARGB_8888, 1);
        if (bitmap != null) {
            synchronized (CANVAS) {
                Canvas canvas = CANVAS;
                canvas.setBitmap(bitmap);
                canvas.save();
                // 防止 View 上面有些区域空白导致最终 Bitmap 上有些区域变黑
                canvas.drawColor(Color.WHITE);
                canvas.scale(scale, scale);
                view.draw(canvas);
                canvas.restore();
                canvas.setBitmap(null);
            }
        }
        return bitmap;
    }


    public static Bitmap createBitmapFromWebView(WebView view) {
        return createBitmapFromWebView(view, 1f);
    }

    public static Bitmap createBitmapFromWebView(WebView view, float scale) {
        view.clearFocus();
        int viewHeight = (int) (view.getContentHeight() * view.getScale());
        Bitmap bitmap = createBitmapSafely((int) (view.getWidth() * scale), (int) (viewHeight * scale), Bitmap.Config.ARGB_8888, 1);

        int unitHeight = view.getHeight();
        int bottom = viewHeight;

        if (bitmap != null) {
            synchronized (CANVAS) {
                Canvas canvas = CANVAS;
                canvas.setBitmap(bitmap);
                // 防止 View 上面有些区域空白导致最终 Bitmap 上有些区域变黑
                canvas.drawColor(Color.WHITE);
                canvas.scale(scale, scale);
                while (bottom > 0) {
                    if (bottom < unitHeight) {
                        bottom = 0;
                    } else {
                        bottom -= unitHeight;
                    }
                    canvas.save();
                    canvas.clipRect(0, bottom, canvas.getWidth(), bottom + unitHeight);
                    view.scrollTo(0, bottom);
                    view.draw(canvas);
                    canvas.restore();
                }
                canvas.setBitmap(null);
            }
        }
        return bitmap;
    }


    public static Bitmap createBitmapFromView(View view) {
        return createBitmapFromView(view, 1f);
    }

    /**
     * 从一个view创建Bitmap。把view的区域截掉leftCrop/topCrop/rightCrop/bottomCrop
     */
    public static Bitmap createBitmapFromView(View view, int leftCrop, int topCrop, int rightCrop, int bottomCrop) {
        Bitmap originBitmap = DrawableUtils.createBitmapFromView(view);
        if (originBitmap == null) {
            return null;
        }
        Bitmap cutBitmap = createBitmapSafely(view.getWidth() - rightCrop - leftCrop, view.getHeight() - topCrop - bottomCrop, Bitmap.Config.ARGB_8888, 1);
        if (cutBitmap == null) {
            return null;
        }
        Canvas canvas = new Canvas(cutBitmap);
        Rect src = new Rect(leftCrop, topCrop, view.getWidth() - rightCrop, view.getHeight() - bottomCrop);
        Rect dest = new Rect(0, 0, view.getWidth() - rightCrop - leftCrop, view.getHeight() - topCrop - bottomCrop);
        // 防止 View 上面有些区域空白导致最终 Bitmap 上有些区域变黑
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(originBitmap, src, dest, null);
        originBitmap.recycle();
        return cutBitmap;
    }

    /**
     * 安全的创建bitmap。
     * 如果新建 Bitmap 时产生了 OOM，可以主动进行一次 GC - System.gc()，然后再次尝试创建。
     *
     * @param width      Bitmap 宽度。
     * @param height     Bitmap 高度。
     * @param config     传入一个 Bitmap.Config。
     * @param retryCount 创建 Bitmap 时产生 OOM 后，主动重试的次数。
     * @return 返回创建的 Bitmap。
     */
    public static Bitmap createBitmapSafely(int width, int height, Bitmap.Config config, int retryCount) {
        //width and height must be > 0
        if (width <= 0 || height <= 0) {
            return null;
        }
        try {
            return Bitmap.createBitmap(width, height, config);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            if (retryCount > 0) {
                System.gc();
                return createBitmapSafely(width, height, config, retryCount - 1);
            }
            return null;
        }
    }

    /**
     * 创建一张指定大小的纯色图片，支持圆角
     *
     * @param resources    Resources对象，用于创建BitmapDrawable
     * @param width        图片的宽度
     * @param height       图片的高度
     * @param cornerRadius 图片的圆角，不需要则传0
     * @param filledColor  图片的填充色
     * @return 指定大小的纯色图片
     */
    public static BitmapDrawable createDrawableWithSize(Resources resources, int width, int height, int cornerRadius, @ColorInt int filledColor) {
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        if (filledColor == 0) {
            filledColor = Color.TRANSPARENT;
        }
        if (cornerRadius > 0) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(filledColor);
            canvas.drawRoundRect(new RectF(0, 0, width, height), cornerRadius, cornerRadius, paint);
        } else {
            canvas.drawColor(filledColor);
        }
        return new BitmapDrawable(resources, output);
    }

    /**
     * 设置Drawable的颜色
     * <b>这里不对Drawable进行mutate()，会影响到所有用到这个Drawable的地方，如果要避免，请先自行mutate()</b>
     */
    public static ColorFilter setDrawableTintColor(Drawable drawable, @ColorInt int tintColor) {
        LightingColorFilter colorFilter = new LightingColorFilter(Color.argb(255, 0, 0, 0), tintColor);
        if (drawable != null) {
            drawable.setColorFilter(colorFilter);
        }
        return colorFilter;
    }

    /**
     * 创建一张渐变图片，支持韵脚。
     *
     * @param startColor 渐变开始色
     * @param endColor   渐变结束色
     * @param radius     圆角大小
     * @param centerX    渐变中心点 X 轴坐标
     * @param centerY    渐变中心点 Y 轴坐标
     * @return 返回所创建的渐变图片。
     */
    public static GradientDrawable createCircleGradientDrawable(@ColorInt int startColor,
                                                                @ColorInt int endColor, int radius,
                                                                @FloatRange(from = 0f, to = 1f) float centerX,
                                                                @FloatRange(from = 0f, to = 1f) float centerY) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColors(new int[]{
                startColor,
                endColor
        });
        gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        gradientDrawable.setGradientRadius(radius);
        gradientDrawable.setGradientCenter(centerX, centerY);
        return gradientDrawable;
    }


    /**
     * 动态创建带上分隔线或下分隔线的Drawable。
     *
     * @param separatorColor 分割线颜色。
     * @param bgColor        Drawable 的背景色。
     * @param top            true 则分割线为上分割线，false 则为下分割线。
     * @return 返回所创建的 Drawable。
     */
    public static LayerDrawable createItemSeparatorBg(@ColorInt int separatorColor, @ColorInt int bgColor, int separatorHeight, boolean top) {

        ShapeDrawable separator = new ShapeDrawable();
        separator.getPaint().setStyle(Paint.Style.FILL);
        separator.getPaint().setColor(separatorColor);

        ShapeDrawable bg = new ShapeDrawable();
        bg.getPaint().setStyle(Paint.Style.FILL);
        bg.getPaint().setColor(bgColor);

        Drawable[] layers = {separator, bg};
        LayerDrawable layerDrawable = new LayerDrawable(layers);

        layerDrawable.setLayerInset(1, 0, top ? separatorHeight : 0, 0, top ? 0 : separatorHeight);
        return layerDrawable;
    }


    /**
     * 创建一张指定大小的圆形图片，并附带文字
     *
     * @param resources   Resources对象，用于创建BitmapDrawable
     * @param size        图片的宽度
     * @param filledColor 图片的填充色
     * @param text        文字
     * @param textSize    文字大小(px)
     * @param textColor   文字颜色
     * @return 指定大小的纯色图片
     */
    public static BitmapDrawable createCircleDrawableWithText(Resources resources, int size, @ColorInt int filledColor, String text, float textSize, @ColorInt int textColor) {
        if (size <= 0) {
            throw new InvalidParameterException("bitmap size must be > 0!");
        }
        if (textSize <= 0) {
            throw new InvalidParameterException("text size must be > 0!");
        }
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawColor(Color.TRANSPARENT);
        // 画圆
        int radius = size / 2;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(filledColor);
        canvas.drawCircle(radius, radius, radius, paint);
        // 画文字
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        float baseline = radius + getBaselineDistance(paint);
        canvas.drawText(text, radius, baseline, paint);
        return new BitmapDrawable(resources, output);
    }

    /**
     * 获取画笔的基线距离
     *
     * @param paint 画笔
     * @return 基线距离
     */
    public static float getBaselineDistance(@NonNull Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
    }

    /////////////// StateListDrawable /////////////////////

    /**
     * 实体 得到随机色 状态选择器
     *
     * @param cornerRadius 圆角半径
     * @return 状态选择器
     */
    public static StateListDrawable getDrawable(int cornerRadius) {
        return getDrawable(cornerRadius, ColorUtils.getRandomColor());
    }

    /**
     * 实体 按下的颜色加深
     *
     * @param cornerRadius 圆角半径
     * @param normalColor  正常的颜色
     * @return 状态选择器
     */

    public static StateListDrawable getDrawable(int cornerRadius, int normalColor) {
        return getDrawable(cornerRadius, ColorUtils.darker(normalColor, 0.8F), normalColor);
    }

    /**
     * 实体  状态选择器
     *
     * @param cornerRadius 圆角半径
     * @param pressedColor 按下颜色
     * @param normalColor  正常的颜色
     * @return 状态选择器
     */
    public static StateListDrawable getDrawable(int cornerRadius, int pressedColor, int normalColor) {
        return getStateListDrawable(getSolidRectDrawable(cornerRadius, pressedColor), getSolidRectDrawable(cornerRadius, normalColor));
    }

    /**
     * 背景选择器
     *
     * @param pressedDrawable 按下状态的Drawable
     * @param normalDrawable  正常状态的Drawable
     * @return 状态选择器
     */
    public static StateListDrawable getStateListDrawable(Drawable pressedDrawable, Drawable normalDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed}, pressedDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, normalDrawable);
        //设置不能用的状态
        //默认其他状态背景
        GradientDrawable gray = getSolidRectDrawable(10, Color.GRAY);
        stateListDrawable.addState(new int[]{}, gray);
        return stateListDrawable;
    }

    /**
     * 得到实心的drawable, 一般作为选中，点中的效果
     *
     * @param cornerRadius 圆角半径
     * @param solidColor   实心颜色
     * @return 得到实心效果
     */
    public static GradientDrawable getSolidRectDrawable(int cornerRadius, int solidColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        // 设置矩形的圆角半径
        gradientDrawable.setCornerRadius(cornerRadius);
        // 设置绘画图片色值
        gradientDrawable.setColor(solidColor);
        // 绘画的是矩形
        gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        return gradientDrawable;
    }

    /////////////// VectorDrawable /////////////////////

    @Nullable
    public static Drawable getVectorDrawable(Context context, @DrawableRes int resVector) {
        try {
            return AppCompatResources.getDrawable(context, resVector);
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap vectorDrawableToBitmap(Context context, @DrawableRes int resVector) {
        Drawable drawable = getVectorDrawable(context, resVector);
        if (drawable != null) {
            return drawable2Bitmap(drawable);
        }
        return null;
    }

    /////////////// VectorDrawable /////////////////////

    /**
     * 获取支持RTL布局的drawable【如果是RTL布局就旋转180度】
     *
     * @param src 原drawable
     * @return
     */
    public static Drawable getSupportRTLDrawable(Drawable src) {
        return getSupportRTLDrawable(src, false);
    }

    /**
     * 获取支持RTL布局的drawable【如果是RTL布局就旋转180度】
     *
     * @param src 原drawable
     * @return
     */
    public static Drawable getSupportRTLDrawable(Drawable src, boolean recycle) {
        if (ResUtils.isRtl()) {
            return rotate(src, 180, 0, 0, recycle);
        }
        return src;
    }

    /**
     * Return the rotated drawable.
     *
     * @param src     The source of drawable.
     * @param degrees The number of degrees.
     * @param px      The x coordinate of the pivot point.
     * @param py      The y coordinate of the pivot point.
     * @param recycle True to recycle the source of drawable, false otherwise.
     * @return the rotated drawable
     */
    public static Drawable rotate(final Drawable src,
                                  final int degrees,
                                  final float px,
                                  final float py,
                                  final boolean recycle) {
        return bitmap2Drawable(rotate(drawable2Bitmap(src), degrees, px, py, recycle));
    }

    /**
     * Return the rotated bitmap.
     *
     * @param src     The source of bitmap.
     * @param degrees The number of degrees.
     * @param px      The x coordinate of the pivot point.
     * @param py      The y coordinate of the pivot point.
     * @param recycle True to recycle the source of bitmap, false otherwise.
     * @return the rotated bitmap
     */
    public static Bitmap rotate(final Bitmap src,
                                final int degrees,
                                final float px,
                                final float py,
                                final boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        if (src.isRecycled()) {
            return null;
        }
        if (degrees == 0) {
            return src;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, px, py);
        Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        if (recycle && !src.isRecycled()) {
            src.recycle();
        }
        return ret;
    }

    private static boolean isEmptyBitmap(final Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    /**
     * 获取图片
     *
     * @param context 上下文
     * @param resId   图片资源
     * @return 图片
     */
    public static Bitmap getBitmapByDrawableId(Context context, @DrawableRes int resId) {
        return drawable2Bitmap(ResUtils.getDrawable(context, resId));
    }

    /**
     * Drawable to bitmap.
     *
     * @param drawable The drawable.
     * @return bitmap
     */
    public static Bitmap drawable2Bitmap(final Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1,
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Bitmap to drawable.
     *
     * @param bitmap The bitmap.
     * @return drawable
     */
    public static Drawable bitmap2Drawable(final Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(ResUtils.getResources(), bitmap);
    }
}
