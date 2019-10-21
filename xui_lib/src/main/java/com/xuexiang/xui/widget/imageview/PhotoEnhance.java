/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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
package com.xuexiang.xui.widget.imageview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import com.xuexiang.xui.utils.DrawableUtils;

/**
 * 图片增强处理
 *
 * @author xuexiang
 * @since 2019-10-21 11:32
 */
public class PhotoEnhance {
    /**
     * 饱和度
     */
    public final static int ENHANCE_SATURATION = 0;
    /**
     * 亮度
     */
    public final static int ENHANCE_BRIGHTNESS = 1;
    /**
     * 对比度
     */
    public final static int ENHANCE_CONTRAST = 2;

    private Bitmap mBitmap;

    private float mSaturationNum = 1.0f;
    private float mBrightNum = 0.0f;
    private float mContrastNum = 1.0f;

    private ColorMatrix mAllMatrix = null;
    private ColorMatrix mSaturationMatrix = null;
    private ColorMatrix mContrastMatrix = null;
    private ColorMatrix mBrightnessMatrix = null;

    public PhotoEnhance() {

    }

    public PhotoEnhance(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public float getSaturation() {
        return mSaturationNum;
    }

    /**
     * 设置饱和度 ( 0 ~ 2)
     *
     * @param saturationNum (范围 :0 ~ 255)
     */
    public PhotoEnhance setSaturation(int saturationNum) {
        mSaturationNum = saturationNum * 1.0f / 128;
        return this;
    }

    public float getBrightness() {
        return mBrightNum;
    }

    /**
     * 设置亮度 (-128 ~ 128 )
     *
     * @param brightNum (范围：0 ~ 255)
     */
    public PhotoEnhance setBrightness(int brightNum) {
        mBrightNum = brightNum - 128;
        return this;
    }

    public float getContrast() {
        return mContrastNum;
    }

    /**
     * 设置对比度 (0.5 ~ 1.5)
     *
     * @param contrastNum (范围 : 0 ~ 255)
     */
    public PhotoEnhance setContrast(int contrastNum) {
        mContrastNum = (float) ((contrastNum / 2 + 64) / 128.0);
        return this;
    }

    public PhotoEnhance setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        return this;
    }

    public boolean hasSetBitmap() {
        return mBitmap != null;
    }

    /**
     * 处理图片
     *
     * @param type
     * @return
     */
    public Bitmap handleImage(int type) {
        if (mBitmap == null) {
            return null;
        }
        Bitmap bitmap = DrawableUtils.createBitmapSafely(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888, 1);
        if (bitmap == null) {
            return null;
        }

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        if (mAllMatrix == null) {
            mAllMatrix = new ColorMatrix();
        }

        /* 饱和度矩阵 */
        if (mSaturationMatrix == null) {
            mSaturationMatrix = new ColorMatrix();
        }

        /* 对比度矩阵 */
        if (mContrastMatrix == null) {
            mContrastMatrix = new ColorMatrix();
        }

        /* 亮度矩阵 */
        if (mBrightnessMatrix == null) {
            mBrightnessMatrix = new ColorMatrix();
        }

        switch (type) {
            case ENHANCE_SATURATION:
                mSaturationMatrix.reset();
                mSaturationMatrix.setSaturation(mSaturationNum);
                break;

            case ENHANCE_BRIGHTNESS:
                mBrightnessMatrix.reset();
                mBrightnessMatrix.set(new float[]{1, 0, 0, 0, mBrightNum, 0, 1, 0, 0, mBrightNum, 0, 0, 1, 0, mBrightNum, 0, 0, 0, 1, 0});
                break;
            case ENHANCE_CONTRAST:
                /* 在亮度不变的情况下，提高对比度必定要降低亮度 */
                float regulateBright = 0;
                regulateBright = (1 - mContrastNum) * 128;

                mContrastMatrix.reset();
                mContrastMatrix.set(new float[]{mContrastNum, 0, 0, 0, regulateBright, 0, mContrastNum, 0, 0, regulateBright, 0, 0, mContrastNum, 0, regulateBright, 0, 0, 0, 1, 0});
                break;

            default:
                break;
        }

        mAllMatrix.reset();
        mAllMatrix.postConcat(mSaturationMatrix);
        mAllMatrix.postConcat(mBrightnessMatrix);
        mAllMatrix.postConcat(mContrastMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));
        canvas.drawBitmap(mBitmap, 0, 0, paint);
        return bitmap;
    }

}
