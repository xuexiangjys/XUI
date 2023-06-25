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

import android.view.View;

import androidx.core.view.ViewCompat;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.OnClick;

/**
 * 某个view监听另一个view的状态变化
 * <p>
 * 1.layoutDependsOn方法：返回值设置监听哪个View
 * 2.onDependentViewChanged方法：返回值设置该view状态发生变化时我们的view要做出一些变化
 *
 * @author xuexiang
 * @since 11/28/22 10:29 PM
 */
@Page(name = "监听另一个view的状态变化")
public class DependViewBehaviorFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dependview_behavior;
    }

    @Override
    protected void initViews() {

    }

    @OnClick(R.id.depend_view)
    public void onViewClicked(View view) {
        // 被监听的view点击后向下移动5px
        ViewCompat.offsetTopAndBottom(view, 5);
    }
}
