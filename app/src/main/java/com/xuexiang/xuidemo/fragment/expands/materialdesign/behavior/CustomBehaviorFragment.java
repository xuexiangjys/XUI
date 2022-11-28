/*
 * Copyright (C) 2022 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.expands.materialdesign.behavior;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.behavior.custom.DependViewBehaviorFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.behavior.custom.ScrollBehaviorFragment;

/**
 * @author xuexiang
 * @since 11/28/22 10:22 PM
 */
@Page(name = "自定义Behavior的使用")
public class CustomBehaviorFragment extends ComponentContainerFragment {
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                DependViewBehaviorFragment.class,
                ScrollBehaviorFragment.class
        };
    }
}
