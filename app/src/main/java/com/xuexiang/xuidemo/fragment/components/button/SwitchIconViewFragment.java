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

package com.xuexiang.xuidemo.fragment.components.button;

import android.view.View;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.button.SwitchIconView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2020-01-05 23:47
 */
@Page(name = "SwitchIconView\n可切换图标的按钮")
public class SwitchIconViewFragment extends BaseFragment {

    @BindView(R.id.switchIconView1)
    SwitchIconView switchIconView1;
    @BindView(R.id.switchIconView2)
    SwitchIconView switchIconView2;
    @BindView(R.id.switchIconView3)
    SwitchIconView switchIconView3;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_switch_icon_view;
    }

    @Override
    protected void initViews() {

    }

    @OnClick({R.id.button1, R.id.button2, R.id.button3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button1:
                switchIconView1.switchState();
                break;
            case R.id.button2:
                switchIconView2.switchState();
                break;
            case R.id.button3:
                switchIconView3.switchState();
                break;
            default:
                break;
        }
    }
}
