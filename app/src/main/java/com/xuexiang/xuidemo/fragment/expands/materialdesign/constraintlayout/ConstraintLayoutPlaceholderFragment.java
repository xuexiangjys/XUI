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

package com.xuexiang.xuidemo.fragment.expands.materialdesign.constraintlayout;

import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Placeholder;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2020-01-09 11:36
 */
@Page(name = "占位符Placeholder使用")
public class ConstraintLayoutPlaceholderFragment extends BaseFragment {
    @BindView(R.id.constraint_layout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.place_holder)
    Placeholder placeHolder;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_constraint_placeholder;
    }

    @Override
    protected void initViews() {

    }

    @SingleClick
    @OnClick({R.id.tv_1, R.id.tv_2, R.id.tv_3})
    public void onViewClicked(View view) {
        //切换占位控件
        placeHolder.setContentId(view.getId());
        //切换动画
        TransitionManager.beginDelayedTransition(constraintLayout, new ChangeBounds()
                .setInterpolator(new OvershootInterpolator()).setDuration(1000));
    }
}
