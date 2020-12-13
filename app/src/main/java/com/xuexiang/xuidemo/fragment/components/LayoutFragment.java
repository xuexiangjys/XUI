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

package com.xuexiang.xuidemo.fragment.components;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.components.layout.AlphaViewFragment;
import com.xuexiang.xuidemo.fragment.components.layout.ExpandableLayoutFragment;
import com.xuexiang.xuidemo.fragment.components.layout.GroupListViewFragment;
import com.xuexiang.xuidemo.fragment.components.layout.LinkageScrollLayoutFragment;
import com.xuexiang.xuidemo.fragment.components.layout.XUILayoutFragment;

/**
 * @author xuexiang
 * @since 2019-06-02 16:46
 */
@Page(name = "通用布局", extra = R.drawable.ic_widget_layout)
public class LayoutFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                AlphaViewFragment.class,
                XUILayoutFragment.class,
                GroupListViewFragment.class,
                ExpandableLayoutFragment.class,
                LinkageScrollLayoutFragment.class,
        };
    }
}
