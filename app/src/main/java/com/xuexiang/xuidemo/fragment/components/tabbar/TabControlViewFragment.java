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

package com.xuexiang.xuidemo.fragment.components.tabbar;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.tabbar.MultiTabControlView;
import com.xuexiang.xui.widget.tabbar.TabControlView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xutil.tip.ToastUtils;

import butterknife.BindView;

/**
 *
 *
 * @author xuexiang
 * @since 2019/1/3 上午12:41
 */
@Page(name = "TabControlView\n选项卡控制滑块")
public class TabControlViewFragment extends BaseFragment {
    @BindView(R.id.tcv_select)
    TabControlView mTabControlView;
    @BindView(R.id.tcv_multi_select)
    MultiTabControlView mMultiTabControlView;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tabcontrolview;
    }

    @Override
    protected void initViews() {
        initTabControlView();
        initMultiTabControlView();

    }

    private void initTabControlView() {
        try {
            mTabControlView.setItems(ResUtils.getStringArray(R.array.course_param_option), ResUtils.getStringArray(R.array.course_param_value));
            mTabControlView.setDefaultSelection(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTabControlView.setOnTabSelectionChangedListener(new TabControlView.OnTabSelectionChangedListener() {
            @Override
            public void newSelection(String title, String value) {
                ToastUtils.toast("选中了：" + title + ", 选中的值为：" + value);
            }
        });
    }

    private void initMultiTabControlView() {
        try {
            mMultiTabControlView.setItems(ResUtils.getStringArray(R.array.course_param_option), ResUtils.getStringArray(R.array.course_param_value));
            mMultiTabControlView.setDefaultSelection(1, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMultiTabControlView.setOnMultiTabSelectionChangedListener(new MultiTabControlView.OnMultiTabSelectionChangedListener() {
            @Override
            public void newSelection(String title, String value, boolean isChecked) {
                ToastUtils.toast("选中了：" + title + ", 选中的值为：" + value + ", isChecked：" + isChecked);
            }
        });
    }

}
