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

package com.xuexiang.xuidemo.fragment.components.refresh.sample;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.components.refresh.sample.diffutil.DiffUtilRefreshFragment;
import com.xuexiang.xuidemo.fragment.components.refresh.sample.edit.NewsListEditFragment;
import com.xuexiang.xuidemo.fragment.components.refresh.sample.selection.ListSelectionFragment;
import com.xuexiang.xuidemo.fragment.components.refresh.sample.sortedlist.SortedListRefreshFragment;

/**
 * @author xuexiang
 * @since 2020/9/2 8:46 PM
 */
@Page(name = "列表使用案例集合")
public class SampleListFragment extends ComponentContainerFragment {
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                DiffUtilRefreshFragment.class,
                SortedListRefreshFragment.class,
                ListSelectionFragment.class,
                NewsListEditFragment.class
        };
    }
}
