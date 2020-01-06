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
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ResUtils;

public class PorterShapeImageView extends PorterImageView {

    private Drawable mShapeDrawable;
    private Matrix mMatrix;
    private Matrix mDrawMatrix;

    public PorterShapeImageView(Context context) {
        this(context, null);
    }

    public PorterShapeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PorterShapeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PorterShapeImageView, defStyleAttr, 0);
            mShapeDrawable = ResUtils.getDrawableAttrRes(getContext(), array, R.styleable.PorterShapeImageView_psiv_shape_image);
            array.recycle();
        }
        mMatrix = new Matrix();
    }

    public void setShapeDrawable(Drawable drawable) {
        mShapeDrawable = drawable;
        invalidate();
    }

    @Override
    protected void paintMaskCanvas(Canvas maskCanvas, Paint maskPaint, int width, int height) {
        if (mShapeDrawable != null) {
            if (mShapeDrawable instanceof BitmapDrawable) {
                configureBitmapBounds(getWidth(), getHeight());
                if (mDrawMatrix != null) {
                    int drawableSaveCount = maskCanvas.getSaveCount();
                    maskCanvas.save();
                    maskCanvas.concat(mMatrix);
                    mShapeDrawable.draw(maskCanvas);
                    maskCanvas.restoreToCount(drawableSaveCount);
                    return;
                }
            }

            mShapeDrawable.setBounds(0, 0, getWidth(), getHeight());
            mShapeDrawable.draw(maskCanvas);
        }
    }

    private void configureBitmapBounds(int viewWidth, int viewHeight) {
        mDrawMatrix = null;
        int drawableWidth = mShapeDrawable.getIntrinsicWidth();
        int drawableHeight = mShapeDrawable.getIntrinsicHeight();
        boolean fits = viewWidth == drawableWidth && viewHeight == drawableHeight;

        if (drawableWidth > 0 && drawableHeight > 0 && !fits) {
            mShapeDrawable.setBounds(0, 0, drawableWidth, drawableHeight);
            float widthRatio = (float) viewWidth / (float) drawableWidth;
            float heightRatio = (float) viewHeight / (float) drawableHeight;
            float scale = Math.min(widthRatio, heightRatio);
            float dx = (int) ((viewWidth - drawableWidth * scale) * 0.5f + 0.5f);
            float dy = (int) ((viewHeight - drawableHeight * scale) * 0.5f + 0.5f);

            mMatrix.setScale(scale, scale);
            mMatrix.postTranslate(dx, dy);
        }
    }
}
