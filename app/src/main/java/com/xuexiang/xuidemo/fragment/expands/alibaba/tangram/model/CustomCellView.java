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

package com.xuexiang.xuidemo.fragment.expands.alibaba.tangram.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.xuexiang.xui.widget.imageview.ImageLoader;
import com.xuexiang.xutil.display.DensityUtils;

/**
 * 使用自定义model方式的自定义View
 *
 * @author xuexiang
 * @since 2020/4/9 12:48 AM
 */
public class CustomCellView extends LinearLayout {
    private ImageView mImageView;
    private TextView mTextView;

    public CustomCellView(Context context) {
        super(context);
        init();
    }

    public CustomCellView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCellView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        int padding = DensityUtils.dip2px(getContext(), 10);
        setPadding(padding, padding, padding, padding);
        mImageView = new ImageView(getContext());
        addView(mImageView, DensityUtils.dip2px(getContext(), 110), DensityUtils.dip2px(getContext(), 72));
        mTextView = new TextView(getContext());
        mTextView.setPadding(0, padding, 0, 0);
        addView(mTextView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    public void setImageUrl(String url) {
        ImageLoader.get().loadImage(mImageView, url);
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    public void setTextColor(@ColorInt int color) {
        mTextView.setTextColor(color);
    }
}
