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
 * 注意,这里直接指定的layout,必须以ConstraintLayout作为父布局
 * <p>
 * ConstraintSet自动生效的属性有以下几个:
 * alpha
 * visibility
 * elevation
 * rotation, rotationX, rotationY
 * translationX, translationY, translationZ
 * scaleX, scaleY
 */
@Page(name = "ConstraintSet直接指定layout资源进行动画切换")
public class ConstraintSetLayoutFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_motion_basic_constraintset_layout;
    }

    @Override
    protected void initViews() {

    }
}
