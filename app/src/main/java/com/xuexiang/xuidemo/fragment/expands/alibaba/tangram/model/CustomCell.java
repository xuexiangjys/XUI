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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tmall.wireless.tangram.MVHelper;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.support.ExposureSupport;
import com.xuexiang.xui.utils.ColorUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 自定义 model 开发组件
 * 主要重写以下
 * 1.parseWith
 * 2.parseStyle(可选)
 * 2.bindView
 * 2.unbindView(可选)
 * 2.isValid(可选)
 *
 * @author xuexiang
 * @since 2020/4/9 12:53 AM
 */
public class CustomCell extends BaseCell<CustomCellView> {
    private String imageUrl;
    private String text;
    private boolean isRandomTextColor;

    /**
     * 解析数据样式数据，可以将解析值缓存到成员变量里
     *
     * @param data
     * @param resolver
     */
    @Override
    public void parseWith(@NonNull JSONObject data, @NonNull MVHelper resolver) {
        try {
            if (data.has("imageUrl")) {
                imageUrl = data.getString("imageUrl");
            }
            if (data.has("text")) {
                text = data.getString("text");
            }
            if (data.has("randomColor")) {
                isRandomTextColor = data.getBoolean("randomColor");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析数据样式数据，可以将解析值缓存到成员变量里(可选)
     *
     * @param data
     */
    @Override
    public void parseStyle(@Nullable JSONObject data) {
        super.parseStyle(data);
    }

    /**
     * 绑定数据到自定义
     *
     * @param view
     */
    @Override
    public void bindView(@NonNull CustomCellView view) {
        view.setBackgroundColor(ColorUtils.getRandomColor());
        view.setImageUrl(imageUrl);
        if (isRandomTextColor) {
            view.setTextColor(ColorUtils.getRandomColor());
        }
        view.setText(view.getClass().getSimpleName() + pos + ": " + text);
        view.setOnClickListener(this);
        if (serviceManager != null) {
            ExposureSupport exposureSupport = serviceManager.getService(ExposureSupport.class);
            if (exposureSupport != null) {
                exposureSupport.onTrace(view, this, type);
            }
        }
    }

    /**
     * 绑定数据到 View 之后(可选)
     *
     * @param view
     */
    @Override
    public void postBindView(@NonNull CustomCellView view) {
        super.postBindView(view);
    }

    /**
     * 控件解除绑定(可选)
     *
     * @param view
     */
    @Override
    public void unbindView(@NonNull CustomCellView view) {
        super.unbindView(view);
    }

    /**
     * 校验原始数据，检查组件的合法性(可选)
     *
     * @return 组件是否合法
     */
    @Override
    public boolean isValid() {
        return super.isValid();
    }
}
