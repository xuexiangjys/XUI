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
package com.xuexiang.xui.widget.imageview.photoview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 可放大缩小的图片控件
 *
 * @author xuexiang
 * @since 2020/5/3 9:49 AM
 */
public class PhotoView extends AppCompatImageView implements IPhotoView {

    private PhotoViewAttacher mAttacher;

    private ScaleType mPendingScaleType;

    public PhotoView(Context context) {
        this(context, null);
    }

    public PhotoView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public PhotoView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        super.setScaleType(ScaleType.MATRIX);
        init();
    }

    protected void init() {
        if (mAttacher == null || mAttacher.getImageView() == null) {
            mAttacher = new PhotoViewAttacher(this);
        }

        if (mPendingScaleType != null) {
            setScaleType(mPendingScaleType);
            mPendingScaleType = null;
        }
    }

    @Override
    public void setRotationTo(float rotationDegree) {
        if (mAttacher != null) {
            mAttacher.setRotationTo(rotationDegree);
        }
    }

    @Override
    public void setRotationBy(float rotationDegree) {
        if (mAttacher != null) {
            mAttacher.setRotationBy(rotationDegree);
        }
    }

    @Override
    public boolean canZoom() {
        return mAttacher != null && mAttacher.canZoom();
    }

    @Override
    public RectF getDisplayRect() {
        return mAttacher != null ? mAttacher.getDisplayRect() : null;
    }

    @Override
    public void getDisplayMatrix(Matrix matrix) {
        if (mAttacher != null) {
            mAttacher.getDisplayMatrix(matrix);
        }
    }

    @Override
    public boolean setDisplayMatrix(Matrix finalRectangle) {
        return mAttacher != null && mAttacher.setDisplayMatrix(finalRectangle);
    }

    @Override
    public float getMinimumScale() {
        return mAttacher != null ? mAttacher.getMinimumScale() : DEFAULT_MIN_SCALE;
    }

    @Override
    public float getMediumScale() {
        return mAttacher != null ? mAttacher.getMediumScale() : DEFAULT_MID_SCALE;
    }

    @Override
    public float getMaximumScale() {
        return mAttacher != null ? mAttacher.getMaximumScale() : DEFAULT_MAX_SCALE;
    }

    @Override
    public float getScale() {
        return mAttacher != null ? mAttacher.getScale() : DEFAULT_MIN_SCALE;
    }

    @Override
    public ScaleType getScaleType() {
        return mAttacher != null ? mAttacher.getScaleType() : null;
    }

    @Override
    public Matrix getImageMatrix() {
        return mAttacher != null ? mAttacher.getImageMatrix() : null;
    }

    @Override
    public void setAllowParentInterceptOnEdge(boolean allow) {
        if (mAttacher != null) {
            mAttacher.setAllowParentInterceptOnEdge(allow);
        }
    }

    @Override
    public void setMinimumScale(float minimumScale) {
        if (mAttacher != null) {
            mAttacher.setMinimumScale(minimumScale);
        }
    }

    @Override
    public void setMediumScale(float mediumScale) {
        if (mAttacher != null) {
            mAttacher.setMediumScale(mediumScale);
        }
    }

    @Override
    public void setMaximumScale(float maximumScale) {
        if (mAttacher != null) {
            mAttacher.setMaximumScale(maximumScale);
        }
    }

    @Override
    public void setScaleLevels(float minimumScale, float mediumScale, float maximumScale) {
        if (mAttacher != null) {
            mAttacher.setScaleLevels(minimumScale, mediumScale, maximumScale);
        }
    }

    @Override
    // setImageBitmap calls through to this method
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if (mAttacher != null) {
            mAttacher.update();
        }
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        if (mAttacher != null) {
            mAttacher.update();
        }
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        if (mAttacher != null) {
            mAttacher.update();
        }
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        boolean changed = super.setFrame(l, t, r, b);
        if (mAttacher != null) {
            mAttacher.update();
        }
        return changed;
    }

    @Override
    public void setOnMatrixChangeListener(PhotoViewAttacher.OnMatrixChangedListener listener) {
        if (mAttacher != null) {
            mAttacher.setOnMatrixChangeListener(listener);
        }
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        if (mAttacher != null) {
            mAttacher.setOnLongClickListener(l);
        }
    }

    @Override
    public void setOnPhotoTapListener(PhotoViewAttacher.OnPhotoTapListener listener) {
        if (mAttacher != null) {
            mAttacher.setOnPhotoTapListener(listener);
        }
    }

    @Override
    public void setOnViewTapListener(PhotoViewAttacher.OnViewTapListener listener) {
        if (mAttacher != null) {
            mAttacher.setOnViewTapListener(listener);
        }
    }

    @Override
    public void setScale(float scale) {
        if (mAttacher != null) {
            mAttacher.setScale(scale);
        }
    }

    @Override
    public void setScale(float scale, boolean animate) {
        if (mAttacher != null) {
            mAttacher.setScale(scale, animate);
        }
    }

    @Override
    public void setScale(float scale, float focalX, float focalY, boolean animate) {
        if (mAttacher != null) {
            mAttacher.setScale(scale, focalX, focalY, animate);
        }
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (mAttacher != null) {
            mAttacher.setScaleType(scaleType);
        } else {
            mPendingScaleType = scaleType;
        }
    }

    @Override
    public void setZoomable(boolean zoomable) {
        if (mAttacher != null) {
            mAttacher.setZoomable(zoomable);
        }
    }

    @Override
    public Bitmap getVisibleRectangleBitmap() {
        return mAttacher != null ? mAttacher.getVisibleRectangleBitmap() : null;
    }

    @Override
    public void setZoomTransitionDuration(int milliseconds) {
        if (mAttacher != null) {
            mAttacher.setZoomTransitionDuration(milliseconds);
        }
    }

    @Override
    public IPhotoView getIPhotoViewImplementation() {
        return mAttacher;
    }

    @Override
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener newOnDoubleTapListener) {
        if (mAttacher != null) {
            mAttacher.setOnDoubleTapListener(newOnDoubleTapListener);
        }
    }

    @Override
    public void setOnScaleChangeListener(PhotoViewAttacher.OnScaleChangeListener onScaleChangeListener) {
        if (mAttacher != null) {
            mAttacher.setOnScaleChangeListener(onScaleChangeListener);
        }
    }

    @Override
    public void setOnSingleFlingListener(PhotoViewAttacher.OnSingleFlingListener onSingleFlingListener) {
        if (mAttacher != null) {
            mAttacher.setOnSingleFlingListener(onSingleFlingListener);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        resetMatrix();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        init();
        super.onAttachedToWindow();
    }

    public void resetMatrix() {
        if (mAttacher != null) {
            mAttacher.cleanup();
            mAttacher.resetMatrix();
        }
    }
}
