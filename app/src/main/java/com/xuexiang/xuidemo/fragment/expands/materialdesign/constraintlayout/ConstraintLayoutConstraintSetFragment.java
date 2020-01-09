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

import android.os.Build;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2020-01-09 13:38
 */
@Page(name = "ConstraintSet实现切换动画")
public class ConstraintLayoutConstraintSetFragment extends BaseFragment {
    private boolean mShowBigImage = false;

    @BindView(R.id.constraint_layout)
    ConstraintLayout constraintLayout;

    private ConstraintSet mConstraintSetNormal = new ConstraintSet();
    private ConstraintSet mConstraintSetBig = new ConstraintSet();

    @Override
    protected int getLayoutId() {
        return R.layout.layout_constraintset_normal;
    }

    @Override
    protected void initViews() {
        mConstraintSetNormal.clone(constraintLayout);
        mConstraintSetBig.load(getContext(), R.layout.layout_constraintset_big);
    }

    @SingleClick
    @OnClick(R.id.iv_content)
    public void onViewClicked(View view) {
        toggleMode();
    }

    public void toggleMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(constraintLayout);
        }
        mShowBigImage = !mShowBigImage;
        applyConfig();
    }

    private void applyConfig() {
        if (mShowBigImage) {
            mConstraintSetBig.applyTo(constraintLayout);
        } else {
            mConstraintSetNormal.applyTo(constraintLayout);
        }
    }
}
