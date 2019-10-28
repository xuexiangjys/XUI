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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import java.util.Stack;

/**
 * 画板，自定义绘图视图，用于在用户触摸事件上进行绘图
 *
 * @author xuexiang
 * @since 2019-10-28 9:52
 */
public class BrushDrawingView extends View {

    static final float DEFAULT_BRUSH_SIZE = 25.0f;
    static final float DEFAULT_ERASER_SIZE = 50.0f;
    static final int DEFAULT_OPACITY = 255;

    private float mBrushSize = DEFAULT_BRUSH_SIZE;
    private float mBrushEraserSize = DEFAULT_ERASER_SIZE;
    private int mOpacity = DEFAULT_OPACITY;

    private final Stack<LinePath> mDrawnPaths = new Stack<>();
    private final Stack<LinePath> mRedoPaths = new Stack<>();
    private final Paint mDrawPaint = new Paint();

    private Canvas mDrawCanvas;
    private boolean mBrushDrawMode;

    private Path mPath;
    private float mTouchX, mTouchY;
    private static final float TOUCH_TOLERANCE = 4;

    private BrushViewChangeListener mBrushViewChangeListener;

    public BrushDrawingView(Context context) {
        this(context, null);
    }

    public BrushDrawingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BrushDrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupBrushDrawing();
    }

    private void setupBrushDrawing() {
        //Caution: This line is to disable hardware acceleration to make eraser feature work properly
        setLayerType(LAYER_TYPE_HARDWARE, null);
        mDrawPaint.setColor(Color.BLACK);
        setupPathAndPaint();
        setVisibility(View.GONE);
    }

    private void setupPathAndPaint() {
        mPath = new Path();
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setDither(true);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
        mDrawPaint.setStrokeWidth(mBrushSize);
        mDrawPaint.setAlpha(mOpacity);
        mDrawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
    }

    private void refreshBrushDrawing() {
        mBrushDrawMode = true;
        setupPathAndPaint();
    }

    void brushEraser() {
        mBrushDrawMode = true;
        mDrawPaint.setStrokeWidth(mBrushEraserSize);
        mDrawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    void setBrushDrawingMode(boolean brushDrawMode) {
        mBrushDrawMode = brushDrawMode;
        if (brushDrawMode) {
            this.setVisibility(View.VISIBLE);
            refreshBrushDrawing();
        }
    }

    void setOpacity(@IntRange(from = 0, to = 255) int opacity) {
        this.mOpacity = opacity;
        setBrushDrawingMode(true);
    }

    int getOpacity() {
        return mOpacity;
    }

    boolean getBrushDrawingMode() {
        return mBrushDrawMode;
    }

    void setBrushSize(float size) {
        mBrushSize = size;
        setBrushDrawingMode(true);
    }

    void setBrushColor(@ColorInt int color) {
        mDrawPaint.setColor(color);
        setBrushDrawingMode(true);
    }

    void setBrushEraserSize(float brushEraserSize) {
        this.mBrushEraserSize = brushEraserSize;
        setBrushDrawingMode(true);
    }

    void setBrushEraserColor(@ColorInt int color) {
        mDrawPaint.setColor(color);
        setBrushDrawingMode(true);
    }

    float getEraserSize() {
        return mBrushEraserSize;
    }

    float getBrushSize() {
        return mBrushSize;
    }

    int getBrushColor() {
        return mDrawPaint.getColor();
    }

    void clearAll() {
        mDrawnPaths.clear();
        mRedoPaths.clear();
        if (mDrawCanvas != null) {
            mDrawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        }
        invalidate();
    }

    void setBrushViewChangeListener(BrushViewChangeListener brushViewChangeListener) {
        mBrushViewChangeListener = brushViewChangeListener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Bitmap canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mDrawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (LinePath linePath : mDrawnPaths) {
            canvas.drawPath(linePath.getDrawPath(), linePath.getDrawPaint());
        }
        canvas.drawPath(mPath, mDrawPaint);
    }

    /**
     * Handle touch event to draw paint on canvas i.e brush drawing
     *
     * @param event points having touch info
     * @return true if handling touch events
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (mBrushDrawMode) {
            float touchX = event.getX();
            float touchY = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchStart(touchX, touchY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchMove(touchX, touchY);
                    break;
                case MotionEvent.ACTION_UP:
                    touchUp();
                    break;
                default:
                    break;
            }
            invalidate();
            return true;
        } else {
            return false;
        }
    }

    boolean undo() {
        if (!mDrawnPaths.empty()) {
            mRedoPaths.push(mDrawnPaths.pop());
            invalidate();
        }
        if (mBrushViewChangeListener != null) {
            mBrushViewChangeListener.onViewRemoved(this);
        }
        return !mDrawnPaths.empty();
    }

    boolean redo() {
        if (!mRedoPaths.empty()) {
            mDrawnPaths.push(mRedoPaths.pop());
            invalidate();
        }

        if (mBrushViewChangeListener != null) {
            mBrushViewChangeListener.onViewAdd(this);
        }
        return !mRedoPaths.empty();
    }


    private void touchStart(float x, float y) {
        mRedoPaths.clear();
        mPath.reset();
        mPath.moveTo(x, y);
        mTouchX = x;
        mTouchY = y;
        if (mBrushViewChangeListener != null) {
            mBrushViewChangeListener.onStartDrawing();
        }
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mTouchX);
        float dy = Math.abs(y - mTouchY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mTouchX, mTouchY, (x + mTouchX) / 2, (y + mTouchY) / 2);
            mTouchX = x;
            mTouchY = y;
        }
    }

    private void touchUp() {
        mPath.lineTo(mTouchX, mTouchY);
        // Commit the path to our offscreen
        mDrawCanvas.drawPath(mPath, mDrawPaint);
        // kill this so we don't double draw
        mDrawnPaths.push(new LinePath(mPath, mDrawPaint));
        mPath = new Path();
        if (mBrushViewChangeListener != null) {
            mBrushViewChangeListener.onStopDrawing();
            mBrushViewChangeListener.onViewAdd(this);
        }
    }

    @VisibleForTesting
    Paint getDrawingPaint() {
        return mDrawPaint;
    }

    @VisibleForTesting
    Pair<Stack<LinePath>, Stack<LinePath>> getDrawingPath() {
        return new Pair<>(mDrawnPaths, mRedoPaths);
    }
}