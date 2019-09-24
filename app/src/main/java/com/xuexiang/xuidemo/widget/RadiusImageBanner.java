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

package com.xuexiang.xuidemo.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.widget.banner.widget.banner.base.BaseImageBanner;
import com.xuexiang.xuidemo.R;

/**
 * 带圆角的图片轮播
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:07
 */
public class RadiusImageBanner extends BaseImageBanner<RadiusImageBanner> {

    public RadiusImageBanner(Context context) {
        super(context);
    }

    public RadiusImageBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadiusImageBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @return 轮播布局的ID
     */
    @Override
    protected int getItemLayoutId() {
        return R.layout.xui_adapter_radius_image;
    }

    /**
     * @return 图片控件的ID
     */
    @Override
    protected int getImageViewId() {
        return R.id.riv;
    }

    @Override
    public int getItemWidth() {
        //需要距离边一点距离
        return super.getItemWidth() - DensityUtils.dp2px(getContext(), 32);
    }
}
