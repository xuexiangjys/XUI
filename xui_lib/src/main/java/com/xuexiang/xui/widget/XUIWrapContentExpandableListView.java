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

package com.xuexiang.xui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

import com.xuexiang.xui.R;

/**
 * @author xuexiang
 * @since 2019-11-11 11:27
 */
public class XUIWrapContentExpandableListView extends ExpandableListView {

    private int mMaxHeight = Integer.MAX_VALUE >> 2;

    public XUIWrapContentExpandableListView(Context context) {
        super(context);
    }

    public XUIWrapContentExpandableListView(Context context, int maxHeight) {
        super(context);
        mMaxHeight = maxHeight;
    }

    public XUIWrapContentExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public XUIWrapContentExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    /**
     * 初始化属性
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.XUIWrapContentExpandableListView);
        if (ta != null) {
            mMaxHeight = ta.getDimensionPixelSize(R.styleable.XUIWrapContentExpandableListView_wcelv_max_height, mMaxHeight);
            ta.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    /**
     * 设置最大高度
     *
     * @param maxHeight 最大高度[px]
     */
    public void setMaxHeight(int maxHeight) {
        if (mMaxHeight != maxHeight) {
            mMaxHeight = maxHeight;
            requestLayout();
        }
    }
}
