/*
 * Copyright (C) 2023 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.expands.materialdesign.motion.basic;

import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.motion.widget.MotionLayout;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;

/**
 * 在约束布局中使用 Keyframes，需要定义一个 KeyPosition 对象和一个 KeyAttribute 对象。
 * <p>
 * KeyPosition 对象定义了中间状态的位置，KeyAttribute 对象定义了中间状态的属性值。
 * <p>
 * 您可以将多个 KeyPosition 和 KeyAttribute 对象组合成一个 Keyframes 对象，并将其应用于约束布局中的任何属性。
 */
@Page(name = "KeyFrameSet\n设置动画中间状态")
public class KeyFrameSetFragment  extends BaseFragment {
    @BindView(R.id.motionLayout)
    MotionLayout motionLayout;

    private boolean mIsShowPath;

    private TextView mTextAction;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_motion_basic_keyframeset;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        mTextAction = (TextView) titleBar.addAction(new TitleBar.TextAction(getActionName()) {
            @Override
            public void performAction(View view) {
                mIsShowPath = !mIsShowPath;
                mTextAction.setText(getActionName());
                if (mIsShowPath) {
                    motionLayout.setDebugMode(MotionLayout.DEBUG_SHOW_PATH);
                } else {
                    motionLayout.setDebugMode(MotionLayout.DEBUG_SHOW_NONE);
                }
            }
        });
        return titleBar;
    }

    private String getActionName() {
        return mIsShowPath ? "隐藏路径" : "显示路径";
    }

    @Override
    protected void initViews() {

    }
}

