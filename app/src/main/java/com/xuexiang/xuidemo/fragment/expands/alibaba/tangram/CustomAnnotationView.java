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
import com.tmall.wireless.tangram.structure.CellRender;
import com.tmall.wireless.tangram.support.ExposureSupport;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.utils.XToastUtils;

import java.util.Locale;

/**
 * 使用注解方式的自定义View
 *
 * @author xuexiang
 * @since 2020/4/7 1:16 AM
 */
public class CustomAnnotationView extends LinearLayout {
    private ImageView mImageView;
    private TextView mTextView;

    public CustomAnnotationView(Context context) {
        super(context);
        init();
    }

    public CustomAnnotationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomAnnotationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        int padding = DensityUtils.dp2px(getContext(), 10);
        setPadding(padding, padding, padding, padding);
        mImageView = new ImageView(getContext());
        addView(mImageView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mTextView = new TextView(getContext());
        mTextView.setPadding(0, padding, 0, 0);
        addView(mTextView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @CellRender
    public void cellInited(BaseCell cell) {
        if (cell.serviceManager != null) {
            ExposureSupport exposureSupport = cell.serviceManager.getService(ExposureSupport.class);
            if (exposureSupport != null) {
                exposureSupport.onTrace(this, cell, cell.type);
            }
        }
    }

    @CellRender
    public void postBindView(final BaseCell cell) {
        if (cell.pos % 2 == 0) {
            setBackgroundColor(0xff0000ff);
            mImageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            setBackgroundColor(0xff00ffff);
            mImageView.setImageResource(R.mipmap.ic_launcher_round);
        }
        mTextView.setText(String.format(Locale.CHINA, "%s%d: %s", getClass().getSimpleName(), cell.pos, cell.optParam("text")));
        setOnClickListener(v -> XToastUtils.toast("您点击了组件，type=" + cell.stringType + ", pos=" + cell.pos));
    }

    @CellRender
    public void postUnBindView(BaseCell cell) {

    }
}
