/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.xuidemo.fragment.components.tabbar;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.components.tabbar.tablayout.TabLayoutCacheFragment;
import com.xuexiang.xuidemo.fragment.components.tabbar.tablayout.TabLayoutSimpleFragment;
import com.xuexiang.xuidemo.fragment.components.tabbar.tablayout.TabLayoutViewPager2Fragment;

/**
 * @author xuexiang
 * @since 2018/12/27 上午11:45
 */
@Page(name = "TabLayout\nMaterial Design 组件")
public class TabLayoutFragment extends ComponentContainerFragment {

    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                TabLayoutSimpleFragment.class,
                TabLayoutCacheFragment.class,
                TabLayoutViewPager2Fragment.class
        };
    }
}
