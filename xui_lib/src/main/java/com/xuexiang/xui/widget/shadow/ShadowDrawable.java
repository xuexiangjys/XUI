package com.xuexiang.xui.widget.shadow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ResUtils;

import static com.xuexiang.xui.widget.shadow.ShadowDrawable.Builder.DEFAULT_SHADOW_COLOR;

/**
 * 可以方便地生成圆角矩形/圆形的阴影
 *
 * @author XUE
 * @since 2019/3/30 15:31
 */
public class ShadowDrawable extends Drawable {

    private Paint mShadowPaint;
    private Paint mBgPaint;
    /**
     * 阴影圆角弧度
     */
    private int mShadowRadius;
    /**
     * 形状: SHAPE_RECTANGLE OR SHAPE_ROUND
     */
    private int mShape;
    /**
     * 圆角弧度
     */
    private int mShapeRadius;
    /**
     * X轴阴影偏移
     */
    private int mOffsetX;
    /**
     * Y轴阴影偏移
     */
    private int mOffsetY;
    /**
     * 背景颜色[大于1为渐变色]
     */
    private int mBgColor[];
    private RectF mRect;

    /**
     * 方形
     */
    public final static int SHAPE_RECTANGLE = 0;
    /**
     * 圆形
     */
    public final static int SHAPE_ROUND = 1;

    private ShadowDrawable(int shape, int[] bgColor, int shapeRadius, int shadowColor, int shadowRadius, int offsetX, int offsetY) {
        mShape = shape;
        mBgColor = bgColor;
        mShapeRadius = shapeRadius;
        mShadowRadius = shadowRadius;
        mOffsetX = offsetX;
        mOffsetY = offsetY;

        mShadowPaint = new Paint();
        mShadowPaint.setColor(Color.TRANSPARENT);
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setShadowLayer(shadowRadius, offsetX, offsetY, shadowColor);
        mShadowPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));

        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        mRect = new RectF(left + mShadowRadius - mOffsetX, top + mShadowRadius - mOffsetY, right - mShadowRadius - mOffsetX,
                bottom - mShadowRadius - mOffsetY);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mBgColor != null) {
            if (mBgColor.length == 1) {
                mBgPaint.setColor(mBgColor[0]);
            } else {
                mBgPaint.setShader(new LinearGradient(mRect.left, mRect.height() / 2, mRect.right,
                        mRect.height() / 2, mBgColor, null, Shader.TileMode.CLAMP));
            }
        }

        if (mShape == SHAPE_RECTANGLE) {
            canvas.drawRoundRect(mRect, mShapeRadius, mShapeRadius, mShadowPaint);
            canvas.drawRoundRect(mRect, mShapeRadius, mShapeRadius, mBgPaint);
        } else {
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), Math.min(mRect.width(), mRect.height()) / 2, mShadowPaint);
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), Math.min(mRect.width(), mRect.height()) / 2, mBgPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mShadowPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mShadowPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public static class Builder {
        public final static int DEFAULT_SHADOW_COLOR = Color.parseColor("#4d000000");

        private int mShape;
        private int mShapeRadius;
        private int mShadowColor;
        private int mShadowRadius;
        private int mOffsetX = 0;
        private int mOffsetY = 0;
        private int[] mBgColor;

        public Builder() {
            mShape = ShadowDrawable.SHAPE_RECTANGLE;
            mShapeRadius = 10;
            mShadowColor = DEFAULT_SHADOW_COLOR;
            mShadowRadius = 16;
            mOffsetX = 0;
            mOffsetY = 0;
            mBgColor = new int[1];
            mBgColor[0] = Color.TRANSPARENT;
        }

        public Builder setShape(int mShape) {
            this.mShape = mShape;
            return this;
        }

        public Builder setShapeRadius(int ShapeRadius) {
            this.mShapeRadius = ShapeRadius;
            return this;
        }

        public Builder setShadowColor(int shadowColor) {
            this.mShadowColor = shadowColor;
            return this;
        }

        public Builder setShadowRadius(int shadowRadius) {
            this.mShadowRadius = shadowRadius;
            return this;
        }

        public Builder setOffsetX(int OffsetX) {
            this.mOffsetX = OffsetX;
            return this;
        }

        public Builder setOffsetY(int OffsetY) {
            this.mOffsetY = OffsetY;
            return this;
        }

        public Builder setBgColor(int BgColor) {
            this.mBgColor[0] = BgColor;
            return this;
        }

        public Builder setBgColor(int[] BgColor) {
            this.mBgColor = BgColor;
            return this;
        }

        public ShadowDrawable build() {
            return new ShadowDrawable(mShape, mBgColor, mShapeRadius, mShadowColor, mShadowRadius, mOffsetX, mOffsetY);
        }
    }


    public static void setShadowDrawable(View view, Drawable drawable) {
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ViewCompat.setBackground(view, drawable);
    }

    public static void setShadowDrawable(View view, int shapeRadius, int shadowColor, int shadowRadius, int offsetX, int offsetY) {
        ShadowDrawable drawable = new Builder()
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .build();
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ViewCompat.setBackground(view, drawable);
    }

    public static void setShadowDrawable(View view, int bgColor, int shapeRadius, int shadowColor, int shadowRadius, int offsetX, int offsetY) {
        ShadowDrawable drawable = new Builder()
                .setBgColor(bgColor)
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .build();
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ViewCompat.setBackground(view, drawable);
    }

    public static void setShadowDrawable(View view, int shape, int bgColor, int shapeRadius, int shadowColor, int shadowRadius, int offsetX, int offsetY) {
        ShadowDrawable drawable = new Builder()
                .setShape(shape)
                .setBgColor(bgColor)
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .build();
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ViewCompat.setBackground(view, drawable);
    }

    public static void setShadowDrawable(View view, int[] bgColor, int shapeRadius, int shadowColor, int shadowRadius, int offsetX, int offsetY) {
        ShadowDrawable drawable = new Builder()
                .setBgColor(bgColor)
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .build();
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ViewCompat.setBackground(view, drawable);
    }

    /**
     * 根据 AttributeSet
     *
     * @param context
     * @param attrs
     * @return
     */
    public static ShadowDrawable fromAttributeSet(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShadowDrawable);
        int shadowRadius = typedArray.getDimensionPixelSize(R.styleable.ShadowDrawable_sd_shadowRadius, ResUtils.getDimensionPixelSize(R.dimen.default_sd_shadow_radius));
        int shadowColor = typedArray.getColor(R.styleable.ShadowDrawable_sd_shadowColor, DEFAULT_SHADOW_COLOR);

        int shape = typedArray.getInt(R.styleable.ShadowDrawable_sd_shapeType, SHAPE_RECTANGLE);
        int shapeRadius = typedArray.getDimensionPixelSize(R.styleable.ShadowDrawable_sd_shapeRadius, ResUtils.getDimensionPixelSize(R.dimen.default_sd_shape_radius));
        int offsetX = typedArray.getDimensionPixelSize(R.styleable.ShadowDrawable_sd_offsetX, 0);
        int offsetY = typedArray.getDimensionPixelSize(R.styleable.ShadowDrawable_sd_offsetY, 0);
        int bgColor = typedArray.getColor(R.styleable.ShadowDrawable_sd_bgColor, ResUtils.getColor(R.color.xui_config_color_white));
        int secondBgColor = typedArray.getColor(R.styleable.ShadowDrawable_sd_secondBgColor, -1);


        typedArray.recycle();

        Builder builder = new Builder()
                .setShape(shape)
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY);

        if (secondBgColor != -1) {
            builder.setBgColor(new int[]{bgColor, secondBgColor});
        } else {
            builder.setBgColor(bgColor);
        }
        return builder.build();
    }

}