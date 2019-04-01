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

package com.xuexiang.xuidemo.fragment.components.refresh.smartrefresh;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.components.refresh.smartrefresh.style.RefreshAllStyleFragment;
import com.xuexiang.xuidemo.fragment.components.refresh.smartrefresh.style.RefreshClassicsStyleFragment;
import com.xuexiang.xuidemo.fragment.components.refresh.smartrefresh.style.RefreshMaterialStyleFragment;

/**
 * @author xuexiang
 * @since 2018/12/7 上午12:44
 */
@Page(name = "下拉刷新样式\n包含10多种下拉样式")
public class RefreshStyleFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[] {
                RefreshClassicsStyleFragment.class,
                RefreshMaterialStyleFragment.class,
                RefreshAllStyleFragment.class,
        };
    }
}
