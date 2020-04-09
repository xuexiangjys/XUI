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

package com.xuexiang.xuidemo.fragment.expands.alibaba.tangram.support;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.tmall.wireless.tangram.dataparser.concrete.Card;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.support.ExposureSupport;

/**
 * @author SunQiang
 * @since 2019-04-22
 */

/**
 * 自定义曝光事件
 *
 * @author xuexiang
 * @since 2020/4/10 1:13 AM
 */
public class CustomExposureSupport extends ExposureSupport {

    private static final String TAG = "CustomExposureSupport";

    public CustomExposureSupport() {
        setOptimizedMode(true);
    }

    /**
     * 布局的整体曝光
     *
     * @param card
     * @param offset
     * @param position
     */
    @Override
    public void onExposure(@NonNull Card card, int offset, int position) {
        Log.e(TAG, "onExposure: card=" + card.getClass().getSimpleName() + ", offset=" + offset + ", position=" + position);
    }

    /**
     * 布局的整体曝光
     *
     * @param targetView
     * @param cell
     * @param type
     */
    @Override
    public void defaultExposureCell(@NonNull View targetView, @NonNull BaseCell cell, int type) {
        Log.e(TAG, "defaultExposureCell: targetView=" + targetView.getClass().getSimpleName() + ", pos=" + cell.pos + ", type=" + type);
    }

    /**
     * 组件的局部区域曝光
     *
     * @param targetView
     * @param cell
     * @param type
     */
    @Override
    public void defaultTrace(@NonNull View targetView, @NonNull BaseCell cell, int type) {
        Log.e(TAG, "defaultTrace: targetView=" + targetView.getClass().getSimpleName() + ", pos=" + cell.pos + ", type=" + type);
    }
}
