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

package com.xuexiang.xuidemo.fragment.expands.alibaba.tangram;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;
import com.tmall.wireless.tangram.support.ExposureSupport;
import com.xuexiang.xui.utils.ColorUtils;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xutil.display.DensityUtils;

import java.util.Locale;

/**
 * 使用接口方式的自定义View
 * <p>
 * 需要实现ITangramViewLifeCycle接口方法，也就是下面三个方法
 * <p>
 * 1、void cellInited(BaseCell cell);
 * 2、void postBindView(BaseCell cell);
 * 3、void postUnBindView(BaseCell cell);
 *
 * @author xuexiang
 * @since 2020/4/9 12:30 AM
 */
public class CustomInterfaceView extends LinearLayout implements ITangramViewLifeCycle {
    private ImageView mImageView;
    private TextView mTextView;

    public CustomInterfaceView(Context context) {
        super(context);
        init();
    }

    public CustomInterfaceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomInterfaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        int padding = DensityUtils.dip2px(getContext(), 10);
        setPadding(padding, padding, padding, padding);
        mImageView = new ImageView(getContext());
        addView(mImageView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mTextView = new TextView(getContext());
        mTextView.setPadding(0, padding, 0, 0);
        addView(mTextView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        ViewUtils.setViewsFont(this);
    }

    @Override
    public void cellInited(BaseCell cell) {
        //设置点击监听
        setOnClickListener(cell);
        if (cell.serviceManager != null) {
            ExposureSupport exposureSupport = cell.serviceManager.getService(ExposureSupport.class);
            if (exposureSupport != null) {
                exposureSupport.onTrace(this, cell, cell.type);
            }
        }
    }

    @Override
    public void postBindView(BaseCell cell) {
        if (cell.pos % 2 == 0) {
            mImageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            mImageView.setImageResource(R.mipmap.ic_launcher_round);
        }
        setBackgroundColor(ColorUtils.getRandomColor());
        mTextView.setText(String.format(Locale.CHINA, "%s%d: %s", getClass().getSimpleName(), cell.pos, cell.optParam("text")));
    }

    @Override
    public void postUnBindView(BaseCell cell) {

    }
}
