/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xui.widget.button.shinebutton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * @author xuexiang
 * @since 2020-01-06 11:36
 */
public abstract class PorterImageView extends AppCompatImageView {

    private static final PorterDuffXfermode PORTER_DUFF_XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

    private Canvas mMaskCanvas;
    private Bitmap mMaskBitmap;
    private Paint mMaskPaint;

    private Canvas mDrawableCanvas;
    private Bitmap mDrawableBitmap;
    private Paint mDrawablePaint;

    private int mPaintColor = Color.GRAY;

    private boolean mInvalidated = true;

    public PorterImageView(Context context) {
        super(context);
        init();
    }

    public PorterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PorterImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (getScaleType() == ScaleType.FIT_CENTER) {
            setScaleType(ScaleType.CENTER_CROP);
        }

        mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMaskPaint.setColor(Color.BLACK);
    }

    public void setTintColor(int color) {
        mPaintColor = color;
        setImageDrawable(new ColorDrawable(color));
        if (mDrawablePaint != null) {
            mDrawablePaint.setColor(color);
            invalidate();
        }
    }

    @Override
    public void invalidate() {
        mInvalidated = true;
        super.invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createMaskCanvas(w, h, oldw, oldh);
    }

    private void createMaskCanvas(int width, int height, int oldw, int oldh) {
        boolean sizeChanged = width != oldw || height != oldh;
        if (width > 0 && height > 0 && (mMaskCanvas == null || sizeChanged)) {
            mMaskCanvas = new Canvas();
            mMaskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            mMaskCanvas.setBitmap(mMaskBitmap);

            mMaskPaint.reset();
            paintMaskCanvas(mMaskCanvas, mMaskPaint, width, height);

            mDrawableCanvas = new Canvas();
            mDrawableBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            mDrawableCanvas.setBitmap(mDrawableBitmap);
            mDrawablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mDrawablePaint.setColor(mPaintColor);
            mInvalidated = true;
        }
    }

    protected abstract void paintMaskCanvas(Canvas maskCanvas, Paint maskPaint, int width, int height);

    @Override
    protected void onDraw(Canvas canvas) {
        int saveCount = canvas.saveLayer(0.0f, 0.0f, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        try {
            if (mInvalidated) {
                Drawable drawable = getDrawable();
                if (drawable != null) {
                    mInvalidated = false;
                    Matrix imageMatrix = getImageMatrix();
                    if (imageMatrix == null) {
                        drawable.draw(mDrawableCanvas);
                    } else {
                        int drawableSaveCount = mDrawableCanvas.getSaveCount();
                        mDrawableCanvas.save();
                        mDrawableCanvas.concat(imageMatrix);
                        drawable.draw(mDrawableCanvas);
                        mDrawableCanvas.restoreToCount(drawableSaveCount);
                    }

                    mDrawablePaint.reset();
                    mDrawablePaint.setFilterBitmap(false);
                    mDrawablePaint.setXfermode(PORTER_DUFF_XFERMODE);
                    mDrawableCanvas.drawBitmap(mMaskBitmap, 0.0f, 0.0f, mDrawablePaint);
                }
            }

            if (!mInvalidated) {
                mDrawablePaint.setXfermode(null);
                canvas.drawBitmap(mDrawableBitmap, 0.0f, 0.0f, mDrawablePaint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            canvas.restoreToCount(saveCount);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (widthMeasureSpec == 0) {
            widthMeasureSpec = 50;
        }
        if (heightMeasureSpec == 0) {
            heightMeasureSpec = 50;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}