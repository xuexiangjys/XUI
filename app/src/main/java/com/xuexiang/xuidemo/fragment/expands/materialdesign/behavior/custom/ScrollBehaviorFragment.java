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

package com.xuexiang.xuidemo.fragment.expands.materialdesign.behavior.custom;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

/**
 * 某个view监听CoordinatorLayout里的滑动状态
 *
 * 1.onStartNestedScroll方法: 返回值设置需要关注什么样的滑动，是水平还是垂直方向上的滑动？
 * 2.onNestedPreScroll方法: 处理滑动
 *
 * @author xuexiang
 * @since 11/28/22 10:29 PM
 */
@Page(name = "监听CoordinatorLayout里的滑动状态")
public class ScrollBehaviorFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_scroll_behavior;
    }

    @Override
    protected void initViews() {

    }
}
