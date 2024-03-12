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

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.button.SmoothCheckBox;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2020-01-06 10:05
 */
@Page(name = "SmoothCheckBox\n自带切换动画的CheckBox")
public class SmoothCheckBoxFragment extends BaseFragment implements SmoothCheckBox.OnCheckedChangeListener {
    @BindView(R.id.scb)
    SmoothCheckBox scb;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_smooth_checkbox;
    }

    @Override
    protected void initViews() {
        scb.setChecked(true);
    }

    @Override
    protected void initListeners() {
        scb.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
        XToastUtils.toast("isChecked：" + isChecked);
    }
}
