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

package com.xuexiang.xui.widget.imageview.edit;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * 图片过滤处理
 *
 * @author xuexiang
 * @since 2019-10-28 9:59
 */
class FilterImageView extends AppCompatImageView {

    private OnImageChangedListener mOnImageChangedListener;

    public FilterImageView(Context context) {
        super(context);
    }

    public FilterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FilterImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnImageChangedListener(OnImageChangedListener onImageChangedListener) {
        mOnImageChangedListener = onImageChangedListener;
    }

    interface OnImageChangedListener {
        void onBitmapLoaded(@Nullable Bitmap sourceBitmap);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        if (mOnImageChangedListener != null) {
            mOnImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    @Override
    public void setImageIcon(@Nullable Icon icon) {
        super.setImageIcon(icon);
        if (mOnImageChangedListener != null) {
            mOnImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    @Override
    public void setImageMatrix(Matrix matrix) {
        super.setImageMatrix(matrix);
        if (mOnImageChangedListener != null) {
            mOnImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    @Override
    public void setImageState(int[] state, boolean merge) {
        super.setImageState(state, merge);
        if (mOnImageChangedListener != null) {
            mOnImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    @Override
    public void setImageTintList(@Nullable ColorStateList tint) {
        super.setImageTintList(tint);
        if (mOnImageChangedListener != null) {
            mOnImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    @Override
    public void setImageTintMode(@Nullable PorterDuff.Mode tintMode) {
        super.setImageTintMode(tintMode);
        if (mOnImageChangedListener != null) {
            mOnImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        if (mOnImageChangedListener != null) {
            mOnImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        if (mOnImageChangedListener != null) {
            mOnImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        super.setImageURI(uri);
        if (mOnImageChangedListener != null) {
            mOnImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    @Override
    public void setImageLevel(int level) {
        super.setImageLevel(level);
        if (mOnImageChangedListener != null) {
            mOnImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    @Nullable
    Bitmap getBitmap() {
        if (getDrawable() != null) {
            return ((BitmapDrawable) getDrawable()).getBitmap();
        }
        return null;
    }
}
