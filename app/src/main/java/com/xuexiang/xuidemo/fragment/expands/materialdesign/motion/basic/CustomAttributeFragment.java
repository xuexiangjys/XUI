/*
 * Copyright (C) 2023 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.expands.materialdesign.motion.basic;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;


/**
 * <CustomAttribute> contains two attributes of its own:
 * <p>
 * motion:attributeName is required and must match an object with getter and setter methods.
 * The getter and setter must match a specific pattern. For example, backgroundColor is supported, since our view has underlying getBackgroundColor() and setBackgroundColor() methods.
 * The other attribute you must provide is based on the value type. Choose from the following supported types:
 * <p>
 * motion:customColorValue for colors
 * motion:customIntegerValue for integers
 * motion:customFloatValue for floats
 * motion:customStringValue for strings
 * motion:customDimension for dimensions
 * motion:customBoolean for booleans
 */
@Page(name = "自定义属性动画切换")
public class CustomAttributeFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_motion_basic_custom_attribute;
    }

    @Override
    protected void initViews() {

    }
}

