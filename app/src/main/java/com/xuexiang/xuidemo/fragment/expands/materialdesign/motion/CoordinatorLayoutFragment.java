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

package com.xuexiang.xuidemo.fragment.expands.materialdesign.motion;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.motion.coordinatorlayout.Coordinator1Fragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.motion.coordinatorlayout.Coordinator2Fragment;

/**
 * <a href="https://developer.android.com/develop/ui/views/animations/motionlayout">MotionLayout的Google官方文档</a>
 */
@Page(name = "结合CoordinatorLayout的使用案例")
public class CoordinatorLayoutFragment extends ComponentContainerFragment {
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                Coordinator1Fragment.class,
                Coordinator2Fragment.class
        };
    }
}
