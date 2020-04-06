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

package com.xuexiang.xuidemo.fragment.expands;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.expands.alibaba.TangramAndroidFragment;
import com.xuexiang.xuidemo.fragment.expands.alibaba.UltraViewPagerFragment;
import com.xuexiang.xuidemo.fragment.expands.alibaba.VLayoutFragment;

/**
 * @author xuexiang
 * @since 2020/3/19 11:24 PM
 */
@Page(name = "alibaba UIKit", extra = R.drawable.ic_expand_alibaba)
public class AlibabaUIFragment extends ComponentContainerFragment {
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                VLayoutFragment.class,
                TangramAndroidFragment.class,
                UltraViewPagerFragment.class
        };
    }
}
