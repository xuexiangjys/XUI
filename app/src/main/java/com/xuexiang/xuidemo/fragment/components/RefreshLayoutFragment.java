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

package com.xuexiang.xuidemo.fragment.components;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.components.refresh.BroccoliFragment;
import com.xuexiang.xuidemo.fragment.components.refresh.RefreshHeadViewFragment;
import com.xuexiang.xuidemo.fragment.components.refresh.SmartRefreshLayoutFragment;
import com.xuexiang.xuidemo.fragment.components.refresh.StickyRecyclerViewFragment;
import com.xuexiang.xuidemo.fragment.components.refresh.SwipeRecyclerViewFragment;
import com.xuexiang.xuidemo.fragment.components.refresh.SwipeRefreshLayoutFragment;
import com.xuexiang.xuidemo.fragment.components.refresh.diffutil.DiffUtilRefreshFragment;

/**
 * @author xuexiang
 * @since 2018/12/6 下午6:09
 */
@Page(name = "列表刷新", extra = R.drawable.ic_widget_refresh)
public class RefreshLayoutFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                SmartRefreshLayoutFragment.class,
                SwipeRecyclerViewFragment.class,
                SwipeRefreshLayoutFragment.class,
                RefreshHeadViewFragment.class,
                BroccoliFragment.class,
                StickyRecyclerViewFragment.class,
                DiffUtilRefreshFragment.class
        };
    }
}
