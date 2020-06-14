/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.utils;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.utils.view.ObjectAnimationFragment;
import com.xuexiang.xuidemo.fragment.utils.view.ViewAnimationFragment;
import com.xuexiang.xuidemo.fragment.utils.view.ViewCustomAnimationFragment;
import com.xuexiang.xuidemo.fragment.utils.view.ViewPaddingFragment;

/**
 * @author xuexiang
 * @since 2019/1/3 下午2:01
 */
@Page(name = "ViewUtils", extra = R.drawable.ic_util_view)
public class ViewUtilsFragment extends ComponentContainerFragment {
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                ViewAnimationFragment.class,
                ViewPaddingFragment.class,
                ObjectAnimationFragment.class,
                ViewCustomAnimationFragment.class
        };
    }
}
