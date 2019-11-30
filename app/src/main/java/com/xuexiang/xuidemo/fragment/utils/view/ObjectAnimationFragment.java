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

package com.xuexiang.xuidemo.fragment.utils.view;

import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.TextView;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2019-11-30 17:29
 */
@Page(name = "属性动画")
public class ObjectAnimationFragment extends BaseFragment {

    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_object_animation;
    }

    @Override
    protected void initViews() {

    }

    @SingleClick
    @OnClick({R.id.btn_alpha, R.id.btn_translation, R.id.btn_scale, R.id.btn_rotation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_alpha:
                doAlphaAnimation(tvContent);
                break;
            case R.id.btn_translation:
                doTranslationAnimation(tvContent);
                break;
            case R.id.btn_scale:
                break;
            case R.id.btn_rotation:
                break;
            default:
                break;
        }
    }

    private void doAlphaAnimation(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1, 0);
        animator.setDuration(1000);
        animator.start();
    }

    private void doTranslationAnimation(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", 0, 200, 0, -200, 0);
        animator.setDuration(2000);
        animator.start();
    }
}
