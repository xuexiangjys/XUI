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
import com.xuexiang.xuidemo.fragment.components.tabbar.tabsegment.TabSegmentFixModeFragment;
import com.xuexiang.xuidemo.fragment.components.tabbar.tabsegment.TabSegmentScrollableModeFragment;

/**
 * @author xuexiang
 * @since 2018/12/26 下午5:39
 */
@Page(name = "TabSegment\n扩展性极强的选项卡")
public class TabSegmentFragment extends ComponentContainerFragment {

    @Override
    protected Class[] getPagesClasses() {
        return new Class[] {
                TabSegmentFixModeFragment.class,
                TabSegmentScrollableModeFragment.class
        };
    }
}
