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

package com.xuexiang.xui.widget.textview.autofit;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Editable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.SingleLineTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.xuexiang.xui.R;

import java.util.ArrayList;

/**
 * 字体自适应辅助类
 *
 * @author xuexiang
 * @since 2019-05-14 22:10
 */
public class AutoFitHelper {

    /**
     * 最小字体大小，默认是8sp
     */
    private static final int DEFAULT_MIN_TEXT_SIZE = 8;
    /**
     * 默认调整精度（精度值越小，越精准，耗时越长）
     */
    private static final float DEFAULT_PRECISION = 0.5f;

    /**
     * Creates a new instance of {@code AutofitHelper} that wraps a {@link TextView} and enables
     * automatically sizing the text to fit.
     */
    public static AutoFitHelper create(TextView view) {
        return create(view, null, 0);
    }

    /**
     * Creates a new instance of {@code AutofitHelper} that wraps a {@link TextView} and enables
     * automatically sizing the text to fit.
     */
    public static AutoFitHelper create(TextView view, AttributeSet attrs) {
        return create(view, attrs, 0);
    }

    /**
     * Creates a new instance of {@code AutofitHelper} that wraps a {@link TextView} and enables
     * automatically sizing the text to fit.
     */
    public static AutoFitHelper create(TextView view, AttributeSet attrs, int defStyle) {
        AutoFitHelper helper = new AutoFitHelper(view);
        boolean enableFit = true;
        Context context = view.getContext();
        int minTextSize = (int) helper.getMinTextSize();
        float precision = helper.getPrecision();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoFitTextView, defStyle, 0);
        enableFit = typedArray.getBoolean(R.styleable.AutoFitTextView_aftv_enable, enableFit);
        minTextSize = typedArray.getDimensionPixelSize(R.styleable.AutoFitTextView_aftv_minTextSize, minTextSize);
        precision = typedArray.getFloat(R.styleable.AutoFitTextView_aftv_precision, precision);
        typedArray.recycle();

        helper.setMinTextSize(TypedValue.COMPLEX_UNIT_PX, minTextSize).setPrecision(precision);
        helper.setEnabled(enableFit);
        return helper;
    }

    /**
     * 对字体大小进行调整
     */
    private static void autoFit(TextView view, TextPaint paint, float minTextSize, float maxTextSize,
                                int maxLines, float precision) {
        if (maxLines <= 0 || maxLines == Integer.MAX_VALUE) {
            // Don't auto-size since there's no limit on lines.
            return;
        }

        int targetWidth = view.getWidth() - view.getPaddingLeft() - view.getPaddingRight();
        if (targetWidth <= 0) {
            return;
        }

        CharSequence text = view.getText();
        TransformationMethod method = view.getTransformationMethod();
        if (method != null) {
            text = method.getTransformation(text, view);
        }

        Context context = view.getContext();
        Resources r = Resources.getSystem();
        DisplayMetrics displayMetrics;

        float size = maxTextSize;
        float high = size;
        float low = 0;

        if (context != null) {
            r = context.getResources();
        }
        displayMetrics = r.getDisplayMetrics();

        paint.set(view.getPaint());
        paint.setTextSize(size);

        if ((maxLines == 1 && paint.measureText(text, 0, text.length()) > targetWidth)
                || getLineCount(text, paint, size, targetWidth, displayMetrics) > maxLines) {
            size = getAutoFitTextSize(text, paint, targetWidth, maxLines, low, high, precision, displayMetrics);
        }

        if (size < minTextSize) {
            size = minTextSize;
        }

        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    /**
     * 计算获取最适合的字体大小
     *
     * @return
     */
    private static float getAutoFitTextSize(CharSequence text, TextPaint paint,
                                            float targetWidth, int maxLines, float low, float high, float precision,
                                            DisplayMetrics displayMetrics) {
        float mid = (low + high) / 2.0f;
        int lineCount = 1;
        StaticLayout layout = null;

        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, mid, displayMetrics));

        if (maxLines != 1) {
            layout = new StaticLayout(text, paint, (int) targetWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
            lineCount = layout.getLineCount();
        }

        if (lineCount > maxLines) {
            // For the case that `text` has more newline characters than `maxLines`.
            if ((high - low) < precision) {
                return low;
            }
            return getAutoFitTextSize(text, paint, targetWidth, maxLines, low, mid, precision, displayMetrics);
        } else if (lineCount < maxLines) {
            return getAutoFitTextSize(text, paint, targetWidth, maxLines, mid, high, precision, displayMetrics);
        } else {
            float maxLineWidth = 0;
            if (maxLines == 1) {
                maxLineWidth = paint.measureText(text, 0, text.length());
            } else {
                for (int i = 0; i < lineCount; i++) {
                    if (layout.getLineWidth(i) > maxLineWidth) {
                        maxLineWidth = layout.getLineWidth(i);
                    }
                }
            }

            if ((high - low) < precision) {
                return low;
            } else if (maxLineWidth > targetWidth) {
                return getAutoFitTextSize(text, paint, targetWidth, maxLines, low, mid, precision,
                        displayMetrics);
            } else if (maxLineWidth < targetWidth) {
                return getAutoFitTextSize(text, paint, targetWidth, maxLines, mid, high, precision,
                        displayMetrics);
            } else {
                return mid;
            }
        }
    }

    private static int getLineCount(CharSequence text, TextPaint paint, float size, float width,
                                    DisplayMetrics displayMetrics) {
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, size, displayMetrics));
        StaticLayout layout = new StaticLayout(text, paint, (int) width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        return layout.getLineCount();
    }

    private static int getMaxLines(TextView view) {
        int maxLines = -1;
        TransformationMethod method = view.getTransformationMethod();
        if (method instanceof SingleLineTransformationMethod) {
            maxLines = 1;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // setMaxLines() and getMaxLines() are only available on android-16+
            maxLines = view.getMaxLines();
        }
        return maxLines;
    }

    private TextView mTextView;
    private TextPaint mPaint;
    /**
     * 原始TextView的字体大小
     */
    private float mTextSize;
    /**
     * 字体最大的行数
     */
    private int mMaxLines;
    /**
     * 最小字体大小
     */
    private float mMinTextSize;
    /**
     * 最大字体大小
     */
    private float mMaxTextSize;
    /**
     * 字体大小调整精度
     */
    private float mPrecision;

    /**
     * 是否开启自适应大小
     */
    private boolean mEnabled;
    private boolean mIsAutoFitting;

    private ArrayList<OnTextSizeChangeListener> mListeners;

    private TextWatcher mTextWatcher = new AutofitTextWatcher();

    private View.OnLayoutChangeListener mOnLayoutChangeListener =
            new AutoFitOnLayoutChangeListener();

    private AutoFitHelper(TextView view) {
        final Context context = view.getContext();
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;

        mTextView = view;
        mPaint = new TextPaint();
        setRawTextSize(view.getTextSize());

        mMaxLines = getMaxLines(view);
        mMinTextSize = scaledDensity * DEFAULT_MIN_TEXT_SIZE;
        mMaxTextSize = mTextSize;
        mPrecision = DEFAULT_PRECISION;
    }

    /**
     * 添加字体大小变化的监听
     *
     * @param listener
     * @return
     */
    public AutoFitHelper addOnTextSizeChangeListener(OnTextSizeChangeListener listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }
        mListeners.add(listener);
        return this;
    }

    /**
     * 删除字体大小变化的监听
     *
     * @param listener
     * @return
     */
    public AutoFitHelper removeOnTextSizeChangeListener(OnTextSizeChangeListener listener) {
        if (mListeners != null) {
            mListeners.remove(listener);
        }
        return this;
    }

    /**
     * @return 字体大小调整精度
     */
    public float getPrecision() {
        return mPrecision;
    }

    /**
     * 设置自适应的调整精度（精度值越小，越精准，耗时越长）
     *
     * @param precision 字体调整精度
     * @return
     */
    public AutoFitHelper setPrecision(float precision) {
        if (mPrecision != precision) {
            mPrecision = precision;

            autoFit();
        }
        return this;
    }

    /**
     * 获取最小字体大小【单位： px】
     */
    public float getMinTextSize() {
        return mMinTextSize;
    }

    /**
     * 设置自适应的最小字体大小【单位：sp】
     *
     * @param size 字体大小
     * @return
     */
    public AutoFitHelper setMinTextSize(float size) {
        return setMinTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 设置自适应的最小字体大小
     *
     * @param unit 单位：px or sp
     * @param size 字体大小
     * @return
     */
    public AutoFitHelper setMinTextSize(int unit, float size) {
        Context context = mTextView.getContext();
        Resources r = Resources.getSystem();

        if (context != null) {
            r = context.getResources();
        }

        setRawMinTextSize(TypedValue.applyDimension(unit, size, r.getDisplayMetrics()));
        return this;
    }

    private void setRawMinTextSize(float size) {
        if (size != mMinTextSize) {
            mMinTextSize = size;

            autoFit();
        }
    }

    /**
     * 获取最大字体大小【单位： px】
     */
    public float getMaxTextSize() {
        return mMaxTextSize;
    }

    /**
     * 设置自适应的最大字体大小【单位： sp】
     *
     * @param size 字体大小
     * @return
     */
    public AutoFitHelper setMaxTextSize(float size) {
        return setMaxTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 设置自适应的最大字体大小
     *
     * @param unit 单位：px or sp
     * @param size 字体大小
     * @return
     */
    public AutoFitHelper setMaxTextSize(int unit, float size) {
        Context context = mTextView.getContext();
        Resources r = Resources.getSystem();

        if (context != null) {
            r = context.getResources();
        }

        setRawMaxTextSize(TypedValue.applyDimension(unit, size, r.getDisplayMetrics()));
        return this;
    }

    private void setRawMaxTextSize(float size) {
        if (size != mMaxTextSize) {
            mMaxTextSize = size;
            autoFit();
        }
    }

    /**
     * @see TextView#getMaxLines()
     */
    public int getMaxLines() {
        return mMaxLines;
    }

    /**
     * @see TextView#setMaxLines(int)
     */
    public AutoFitHelper setMaxLines(int lines) {
        if (mMaxLines != lines) {
            mMaxLines = lines;
            autoFit();
        }
        return this;
    }

    /**
     * @return 是否开启自适应字体大小的功能
     */
    public boolean isEnabled() {
        return mEnabled;
    }

    /**
     * 设置是否开启自适应字体大小的功能
     *
     * @param enabled 是否开启
     * @return
     */
    public AutoFitHelper setEnabled(boolean enabled) {
        if (mEnabled != enabled) {
            mEnabled = enabled;
            if (enabled) {
                mTextView.addTextChangedListener(mTextWatcher);
                mTextView.addOnLayoutChangeListener(mOnLayoutChangeListener);

                autoFit();
            } else {
                mTextView.removeTextChangedListener(mTextWatcher);
                mTextView.removeOnLayoutChangeListener(mOnLayoutChangeListener);

                mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            }
        }
        return this;
    }

    /**
     * Returns the original text size of the View.
     *
     * @see TextView#getTextSize()
     */
    public float getTextSize() {
        return mTextSize;
    }

    /**
     * Set the original text size of the View.
     *
     * @see TextView#setTextSize(float)
     */
    public void setTextSize(float size) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * Set the original text size of the View.
     *
     * @see TextView#setTextSize(int, float)
     */
    public void setTextSize(int unit, float size) {
        if (mIsAutoFitting) {
            // We don't want to update the TextView's actual textSize while we're autofitting
            // since it'd get set to the autofitTextSize
            return;
        }
        Context context = mTextView.getContext();
        Resources r = Resources.getSystem();

        if (context != null) {
            r = context.getResources();
        }
        setRawTextSize(TypedValue.applyDimension(unit, size, r.getDisplayMetrics()));
    }

    private void setRawTextSize(float size) {
        if (mTextSize != size) {
            mTextSize = size;
        }
    }

    private void autoFit() {
        float oldTextSize = mTextView.getTextSize();
        float textSize;

        mIsAutoFitting = true;
        autoFit(mTextView, mPaint, mMinTextSize, mMaxTextSize, mMaxLines, mPrecision);
        mIsAutoFitting = false;

        textSize = mTextView.getTextSize();
        if (textSize != oldTextSize) {
            sendTextSizeChange(textSize, oldTextSize);
        }
    }

    private void sendTextSizeChange(float textSize, float oldTextSize) {
        if (mListeners == null) {
            return;
        }

        for (OnTextSizeChangeListener listener : mListeners) {
            listener.onTextSizeChange(textSize, oldTextSize);
        }
    }

    private class AutofitTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            // do nothing
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            autoFit();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // do nothing
        }
    }

    private class AutoFitOnLayoutChangeListener implements View.OnLayoutChangeListener {
        @Override
        public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            autoFit();
        }
    }

    /**
     * 文字大小变化的监听
     */
    public interface OnTextSizeChangeListener {
        /**
         * 文字大小发生变化
         *
         * @param newTextSize 新的字体大小
         * @param oldTextSize 旧的字体大小
         */
        void onTextSizeChange(float newTextSize, float oldTextSize);
    }
}
