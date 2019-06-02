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
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xuexiang.xui.R;

import java.util.WeakHashMap;

/**
 * 能自动将子类中的TextView自适应大小
 *
 * @author xuexiang
 * @since 2019-05-14 22:38
 */
public class AutoFitLayout extends FrameLayout {

    private boolean mEnabled;
    private float mMinTextSize;
    private float mPrecision;
    private WeakHashMap<View, AutoFitHelper> mHelpers = new WeakHashMap<>();

    public AutoFitLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public AutoFitLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public AutoFitLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        boolean enableFit = true;
        int minTextSize = -1;
        float precision = -1;

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AutoFitTextView, defStyle, 0);
            enableFit = ta.getBoolean(R.styleable.AutoFitTextView_aftv_enable, enableFit);
            minTextSize = ta.getDimensionPixelSize(R.styleable.AutoFitTextView_aftv_minTextSize, minTextSize);
            precision = ta.getFloat(R.styleable.AutoFitTextView_aftv_precision, precision);
            ta.recycle();
        }
        mEnabled = enableFit;
        mMinTextSize = minTextSize;
        mPrecision = precision;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child instanceof TextView) {
            TextView textView = (TextView) child;
            AutoFitHelper helper = AutoFitHelper.create(textView).setEnabled(mEnabled);
            if (mPrecision > 0) {
                helper.setPrecision(mPrecision);
            }
            if (mMinTextSize > 0) {
                helper.setMinTextSize(TypedValue.COMPLEX_UNIT_PX, mMinTextSize);
            }
            mHelpers.put(textView, helper);
        }
    }

    /**
     * Returns the {@link AutoFitHelper} for this child View.
     */
    public AutoFitHelper getAutofitHelper(TextView textView) {
        return mHelpers.get(textView);
    }

    /**
     * Returns the {@link AutoFitHelper} for this child View.
     */
    public AutoFitHelper getAutofitHelper(int index) {
        return mHelpers.get(getChildAt(index));
    }
}
