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
import android.util.AttributeSet;
import android.util.TypedValue;

import android.support.v7.widget.AppCompatTextView;

/**
 * 能够自适应字体大小的TextView
 *
 * @author xuexiang
 * @since 2019-05-14 22:51
 */
public class AutoFitTextView extends AppCompatTextView implements AutoFitHelper.OnTextSizeChangeListener {

    private AutoFitHelper mHelper;

    public AutoFitTextView(Context context) {
        this(context, null);
    }

    public AutoFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public AutoFitTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        mHelper = AutoFitHelper.create(this, attrs, defStyle).addOnTextSizeChangeListener(this);
    }

    // Getters and Setters

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        if (mHelper != null) {
            mHelper.setTextSize(unit, size);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLines(int lines) {
        super.setLines(lines);
        if (mHelper != null) {
            mHelper.setMaxLines(lines);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMaxLines(int maxLines) {
        super.setMaxLines(maxLines);
        if (mHelper != null) {
            mHelper.setMaxLines(maxLines);
        }
    }

    /**
     * Returns the {@link AutoFitHelper} for this View.
     */
    public AutoFitHelper getAutofitHelper() {
        return mHelper;
    }

    /**
     * @return 是否开启自适应字体大小的功能
     */
    public boolean isEnableFit() {
        return mHelper.isEnabled();
    }

    /**
     * 开启自适应字体大小的功能
     */
    public void enableFit() {
        setEnableFit(true);
    }

    /**
     * 设置是否开启自适应字体大小的功能
     *
     * @param enableFit 是否开启
     * @return
     */
    public void setEnableFit(boolean enableFit) {
        mHelper.setEnabled(enableFit);
    }

    /**
     * 获取最大字体大小【单位： px】
     */
    public float getMaxTextSize() {
        return mHelper.getMaxTextSize();
    }

    /**
     * 设置自适应的最大字体大小【单位： sp】
     *
     * @param size 字体大小
     */
    public void setMaxTextSize(float size) {
        mHelper.setMaxTextSize(size);
    }

    /**
     * 设置自适应的最大字体大小
     *
     * @param unit 单位：px or sp
     * @param size 字体大小
     */
    public void setMaxTextSize(int unit, float size) {
        mHelper.setMaxTextSize(unit, size);
    }

    /**
     * 获取最小字体大小【单位： px】
     */
    public float getMinTextSize() {
        return mHelper.getMinTextSize();
    }

    /**
     * 设置自适应的最小字体大小【单位：sp】
     *
     * @param minSize 字体大小
     */
    public void setMinTextSize(int minSize) {
        mHelper.setMinTextSize(TypedValue.COMPLEX_UNIT_SP, minSize);
    }

    /**
     * 设置自适应的最小字体大小
     *
     * @param unit    单位：px or sp
     * @param minSize 字体大小
     * @return
     */
    public void setMinTextSize(int unit, float minSize) {
        mHelper.setMinTextSize(unit, minSize);
    }

    /**
     * @return 字体大小调整精度
     */
    public float getPrecision() {
        return mHelper.getPrecision();
    }

    /**
     * 设置自适应的调整精度（精度值越小，越精准，耗时越长）
     *
     * @param precision 字体调整精度
     * @return
     */
    public void setPrecision(float precision) {
        mHelper.setPrecision(precision);
    }

    @Override
    public void onTextSizeChange(float textSize, float oldTextSize) {
        // do nothing
    }
}
