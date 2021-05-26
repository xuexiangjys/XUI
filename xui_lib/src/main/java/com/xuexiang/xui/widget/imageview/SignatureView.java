/*
 * Copyright (C) 2021 xuexiangjys(xuexiangjys@163.com)
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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * 自定义签名画板
 *
 * @author xuexiang
 * @since 2021/5/27 1:07 AM
 */
public class SignatureView extends View {
    /**
     * 默认签名图片的宽
     */
    public final static int DEFAULT_IMG_WIDTH = 300;
    /**
     * 默认签名图片的高
     */
    public final static int DEFAULT_IMG_HEIGHT = 150;
    /**
     * 默认画笔的粗细
     */
    public final static float DEFAULT_PEN_SIZE = 10F;

    /**
     * 笔画X坐标起点
     */
    private float mX;
    /**
     * 笔画Y坐标起点
     */
    private float mY;
    /**
     * 手写画笔
     */
    private Paint mGesturePaint = new Paint();
    /**
     * 路径
     */
    private Path mPath = new Path();
    /**
     * 背景画布
     */
    private Canvas mCacheCanvas;
    /**
     * 背景Bitmap缓存
     */
    private Bitmap mCacheBitmap;

    /**
     * 保存图片的宽(px)
     */
    private int mPictureWidth = DEFAULT_IMG_WIDTH;

    /**
     * 保存图片的高(px)
     */
    private int mPictureHeight = DEFAULT_IMG_HEIGHT;

    /**
     * 是否已经签名
     */
    private boolean mIsTouched = false;

    /**
     * 画笔宽度 px；
     */
    private float mPenSize = DEFAULT_PEN_SIZE;

    /**
     * 画笔颜色
     */
    private int mPenColor = Color.BLACK;

    /**
     * 画板背景底色
     */
    private int mBackColor = Color.TRANSPARENT;

    /**
     * 边框画笔
     */
    private Paint mBorderPaint;
    /**
     * 边框宽度
     */
    private int mBorderWidth;

    public SignatureView(Context context) {
        super(context);
        initAttrs(context, null);
    }

    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public SignatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SignatureView);
        mPenSize = typedArray.getDimension(R.styleable.SignatureView_stv_penSize, mPenSize);
        mPenColor = typedArray.getColor(R.styleable.SignatureView_stv_penColor, Color.BLACK);
        mBackColor = typedArray.getColor(R.styleable.SignatureView_stv_backColor, Color.TRANSPARENT);
        mPictureWidth = typedArray.getInteger(R.styleable.SignatureView_stv_imgWidth, mPictureWidth);
        mPictureHeight = typedArray.getInteger(R.styleable.SignatureView_stv_imgHeight, mPictureHeight);
        boolean hasBorder = typedArray.getBoolean(R.styleable.SignatureView_stv_hasBorder, false);
        updateBorderStyle(hasBorder);
        typedArray.recycle();

        initPaint();
    }

    /**
     * 更新边框样式
     *
     * @param hasBorder 是否有边框
     */
    private void updateBorderStyle(boolean hasBorder) {
        if (hasBorder) {
            mBorderPaint = new Paint();
            mBorderWidth = DensityUtils.dp2px(1);
            initBorderPaintStyle();
        } else {
            mBorderPaint = null;
            mBorderWidth = 0;
        }
    }

    private void initPaint() {
        //抗锯齿
        mGesturePaint.setAntiAlias(true);
        //防抖动
        mGesturePaint.setDither(true);
        //空心画笔
        mGesturePaint.setStyle(Style.STROKE);
        mGesturePaint.setStrokeCap(Paint.Cap.ROUND);
        mGesturePaint.setStrokeWidth(mPenSize);
        mGesturePaint.setColor(mPenColor);
    }

    private void initBorderPaintStyle() {
        if (mBorderPaint != null) {
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setStyle(Style.STROKE);
            mBorderPaint.setStrokeCap(Paint.Cap.ROUND);
            mBorderPaint.setStrokeWidth(mBorderWidth);
            mBorderPaint.setColor(Color.BLACK);
            mBorderPaint.setColor(ThemeUtils.resolveColor(getContext(), R.attr.xui_config_color_separator_dark));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateCacheCanvasSize();
    }

    /**
     * 更新背景画布的尺寸
     */
    private void updateCacheCanvasSize() {
        if (getWidth() > 0 && getHeight() > 0) {
            mCacheBitmap = Bitmap.createBitmap(getWidth() - 2 * mBorderWidth, getHeight() - 2 * mBorderWidth, Config.ARGB_8888);
            mCacheCanvas = new Canvas(mCacheBitmap);
            mCacheCanvas.drawColor(mBackColor);
            mIsTouched = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                mIsTouched = true;
                touchMove(event);
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                break;
            default:
                break;
        }
        // 更新绘制
        invalidate();
        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBorder(canvas);
        canvas.drawBitmap(mCacheBitmap, mBorderWidth, mBorderWidth, mGesturePaint);
        // 通过画布绘制多点形成的图形
        canvas.drawPath(mPath, mGesturePaint);
    }

    /**
     * 画边框
     *
     * @param canvas 画布
     */
    private void drawBorder(Canvas canvas) {
        if (mBorderPaint != null) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), mBorderPaint);
        }
    }

    /**
     * 手指点下屏幕时调用
     *
     * @param event 点击事件
     */
    private void touchDown(MotionEvent event) {
        // mPath.rewind();
        // 重置绘制路线，即隐藏之前绘制的轨迹
        mPath.reset();
        float x = event.getX();
        float y = event.getY();

        mX = x;
        mY = y;
        // mPath绘制的绘制起点
        mPath.moveTo(x, y);
    }

    /**
     * 手指在屏幕上滑动时调用
     *
     * @param event 滑动事件
     */
    private void touchMove(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();

        final float previousX = mX;
        final float previousY = mY;

        final float dx = Math.abs(x - previousX);
        final float dy = Math.abs(y - previousY);

        // 两点之间的距离大于等于3时，生成贝塞尔绘制曲线
        if (dx >= 3 || dy >= 3) {
            // 设置贝塞尔曲线的操作点为起点和终点的一半
            float cX = (x + previousX) / 2;
            float cY = (y + previousY) / 2;

            // 二次贝塞尔，实现平滑曲线；previousX, previousY为操作点，cX, cY为终点
            mPath.quadTo(previousX, previousY, cX, cY);

            // 第二次执行时，第一次结束调用的坐标值将作为第二次调用的初始坐标值
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        mCacheCanvas.translate(-mBorderWidth, -mBorderWidth);
        mCacheCanvas.drawPath(mPath, mGesturePaint);
        mCacheCanvas.translate(mBorderWidth, mBorderWidth);
        mPath.reset();
    }

    /**
     * 清除画板
     */
    public void clear() {
        if (mCacheCanvas != null) {
            mIsTouched = false;
            mGesturePaint.setColor(mPenColor);
            if (mBackColor == Color.TRANSPARENT) {
                mCacheCanvas.drawColor(mBackColor, PorterDuff.Mode.CLEAR);
            } else {
                mCacheCanvas.drawColor(mBackColor);
            }
            mGesturePaint.setColor(mPenColor);
            invalidate();
        }
    }


    /**
     * 保存签名
     *
     * @param path
     * @return
     * @throws IOException
     */
    public Bitmap saveSignature(String path) throws IOException {
        Bitmap bitmap = null;
        if (getTouched()) {
            bitmap = save(path);
            clear();
        }
        return bitmap;
    }

    /**
     * 保存画板
     *
     * @param path 保存到路劲
     */

    public Bitmap save(String path) throws IOException {
        return save(path, false, 0);
    }

    /**
     * 保存画板
     *
     * @param path       保存的路径
     * @param clearBlank 是否清除空白区域
     * @param blank      边缘空白区域
     */
    public Bitmap save(String path, boolean clearBlank, int blank) throws IOException {
        Bitmap bitmap = Utils.zoom(mCacheBitmap, mPictureWidth, mPictureHeight);
        if (clearBlank) {
            bitmap = clearBlank(bitmap, blank);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] buffer = bos.toByteArray();
        if (buffer != null) {
            File file = new File(path);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(buffer);
            outputStream.close();
        }
        return bitmap;
    }

    /**
     * 获取画板的bitmap
     *
     * @return
     */
    public Bitmap getBitMap() {
        setDrawingCacheEnabled(true);
        buildDrawingCache();
        Bitmap bitmap = getDrawingCache();
        setDrawingCacheEnabled(false);
        return bitmap;
    }

    /**
     * 逐行扫描 清楚边界空白。
     *
     * @param bp
     * @param blank 边距留多少个像素
     * @return
     */
    private Bitmap clearBlank(Bitmap bp, int blank) {
        int bmpHeight = bp.getHeight();
        int bmpWidth = bp.getWidth();
        int top = 0, left = 0, right = 0, bottom = 0;
        int[] pixs = new int[bmpWidth];
        boolean isStop;
        for (int y = 0; y < bmpHeight; y++) {
            bp.getPixels(pixs, 0, bmpWidth, 0, y, bmpWidth, 1);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBackColor) {
                    top = y;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        for (int y = bmpHeight - 1; y >= 0; y--) {
            bp.getPixels(pixs, 0, bmpWidth, 0, y, bmpWidth, 1);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBackColor) {
                    bottom = y;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        pixs = new int[bmpHeight];
        for (int x = 0; x < bmpWidth; x++) {
            bp.getPixels(pixs, 0, 1, x, 0, 1, bmpHeight);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBackColor) {
                    left = x;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        for (int x = bmpWidth - 1; x > 0; x--) {
            bp.getPixels(pixs, 0, 1, x, 0, 1, bmpHeight);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBackColor) {
                    right = x;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        if (blank < 0) {
            blank = 0;
        }
        left = Math.max(left - blank, 0);
        top = Math.max(top - blank, 0);
        right = Math.min(right + blank, bmpWidth - 1);
        bottom = Math.min(bottom + blank, bmpHeight - 1);
        return Bitmap.createBitmap(bp, left, top, right - left, bottom - top);
    }

    /**
     * 设置画笔宽度 默认宽度为10px
     *
     * @param penSize 单位：px
     */
    public SignatureView setPenSize(float penSize) {
        penSize = penSize > 0 ? penSize : 10F;
        mPenSize = penSize;
        if (mGesturePaint != null) {
            mGesturePaint.setStrokeWidth(penSize);
        }
        return this;
    }

    /**
     * 设置是否画边框
     *
     * @param hasBorder 是否画边框
     */
    public SignatureView enableBorder(boolean hasBorder) {
        updateBorderStyle(hasBorder);
        updateCacheCanvasSize();
        return this;
    }

    /**
     * 设置签名板的背景颜色
     *
     * @param backColor 签名板的背景颜色
     */
    public SignatureView setBackColor(int backColor) {
        mBackColor = backColor;
        if (mCacheCanvas != null) {
            mCacheCanvas.drawColor(mBackColor);
        }
        return this;
    }

    /**
     * 设置画笔颜色
     *
     * @param penColor 画笔颜色
     */
    public SignatureView setPenColor(int penColor) {
        mPenColor = penColor;
        if (mGesturePaint != null) {
            mGesturePaint.setColor(penColor);
        }
        return this;
    }

    /**
     * 是否有签名
     *
     * @return 是否有签名
     */
    public boolean getTouched() {
        return mIsTouched;
    }


    /**
     * 资源回收
     */
    public void recycle() {
        if (mCacheBitmap != null) {
            mCacheBitmap.recycle();
            mCacheBitmap = null;
        }
        if (mCacheCanvas != null) {
            mCacheCanvas.drawColor(mBackColor, PorterDuff.Mode.CLEAR);
            mCacheCanvas = null;
        }
        mGesturePaint = null;
        mPath = null;
    }

    /**
     * 设置保存图片的尺寸
     *
     * @param pictureWidth
     * @param pictureHeight
     */
    public SignatureView setPictureSize(int pictureWidth, int pictureHeight) {
        mPictureWidth = pictureWidth;
        mPictureHeight = pictureHeight;
        return this;
    }

    public int getPictureWidth() {
        return mPictureWidth;
    }

    public SignatureView setPictureWidth(int pictureWidth) {
        mPictureWidth = pictureWidth;
        return this;
    }

    public int getPictureHeight() {
        return mPictureHeight;
    }

    public SignatureView setPictureHeight(int pictureHeight) {
        mPictureHeight = pictureHeight;
        return this;
    }

    /**
     * 设置签名板的尺寸
     *
     * @param width  宽
     * @param height 高
     * @return
     */
    public SignatureView setViewSize(int width, int height) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        setLayoutParams(params);
        return this;
    }

    /**
     * 设置签名板的宽度
     *
     * @param width
     * @return
     */
    public SignatureView setViewWidth(int width) {
        setViewSize(width, width / 2);
        return this;
    }

    /**
     * 设置签名板的高度
     *
     * @param height
     * @return
     */
    public SignatureView setViewHeight(int height) {
        setViewSize(height * 2, height);
        return this;
    }

}
