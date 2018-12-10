/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xui.widget.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.Utils;

/**
 * 可在ImageView上设置图标的ImageView
 *
 * @author xuexiang
 * @since 2018/12/10 上午11:58
 */
public class IconImageView extends AppCompatImageView {
    /**
     * 图标
     */
    private Bitmap mIconBitmap;
    /**
     * 图标缩放比例
     */
    private float mIconScale = 0.3F;
    /**
     * 绘制图标的画笔
     */
    private Paint mIconPaint;
    /**
     * 是否显示
     */
    private boolean mIsShow = true;

    public IconImageView(Context context) {
        super(context);
        initAttrs(context, null);
    }

    public IconImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public IconImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    /**
     * 初始化属性
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconImageView);

            Drawable icon = typedArray.getDrawable(R.styleable.IconImageView_iiv_icon);
            if (icon != null) {
                mIconBitmap = Utils.getBitmapFromDrawable(icon);
            }
            mIconScale = typedArray.getFloat(R.styleable.IconImageView_iiv_icon_scale, mIconScale);
            mIsShow = typedArray.getBoolean(R.styleable.IconImageView_iiv_is_show, mIsShow);

            typedArray.recycle();
        }

        initPaint();
    }

    private void initPaint() {
        mIconPaint = new Paint();
        mIconPaint.setColor(Color.parseColor("#299EE3"));
        mIconPaint.setAntiAlias(true);
        mIconPaint.setFilterBitmap(true);
        mIconPaint.setStyle(Paint.Style.STROKE);
        mIconPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mIconBitmap != null && mIsShow) {
            canvas.drawBitmap(mIconBitmap, null, getIconBitmapRectF(mIconBitmap), mIconPaint);
        }
    }

    /**
     * 获取中心图标所在的区域
     *
     * @return
     */
    private RectF getIconBitmapRectF(Bitmap bitmap) {
        float width = bitmap.getWidth(), height = bitmap.getHeight();
        if (width >= height) {
            height = getWidth() / width * height;
            width = getWidth();
        } else {
            width = getHeight() / height * width;
            height = getHeight();
        }
        float left = (getWidth() - width * mIconScale) / 2;
        float top = (getHeight() - height * mIconScale) / 2;
        float right = getWidth() - left;
        float bottom = getHeight() - top;
        return new RectF(left, top, right, bottom);
    }

    /**
     * 设置是否显示图标
     *
     * @param isShowIcon
     * @return
     */
    public IconImageView setIsShowIcon(boolean isShowIcon) {
        mIsShow = isShowIcon;
        invalidate();
        return this;
    }

    /**
     * 设置图标
     *
     * @param iconBitmap
     * @return
     */
    public IconImageView setIconBitmap(@NonNull Bitmap iconBitmap) {
        mIconBitmap = iconBitmap;
        invalidate();
        return this;
    }

    /**
     * 设置图标
     *
     * @param iconDrawable
     * @return
     */
    public IconImageView setIconDrawable(@NonNull Drawable iconDrawable) {
        mIconBitmap = Utils.getBitmapFromDrawable(iconDrawable);
        invalidate();
        return this;
    }

    /**
     * 设置图标的缩放比例
     *
     * @param iconScale
     * @return
     */
    public IconImageView setIconScale(float iconScale) {
        mIconScale = iconScale;
        invalidate();
        return this;
    }

    /**
     * 资源释放
     */
    public void recycle() {
        if (mIconBitmap != null) {
            mIconBitmap.recycle();
            mIconBitmap = null;
        }
        mIconPaint = null;
    }

}
