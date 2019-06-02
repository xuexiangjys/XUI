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

package com.xuexiang.xuidemo.fragment.components.textview;

import android.widget.TextView;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.textview.autofit.AutoFitTextView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xutil.common.RandomUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2019-05-14 23:22
 */
@Page(name = "AutoFitTextView\n自适应字体大小的文字")
public class AutoFitTextViewFragment extends BaseFragment {
    @BindView(R.id.tv_in_auto_fit)
    TextView tvInAutoFit;
    @BindView(R.id.aftv)
    AutoFitTextView aftv;
    @BindView(R.id.tv)
    TextView tv;

    StringBuilder sb = new StringBuilder();

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_autofit_textview;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        setText(sb);
    }


    @OnClick(R.id.btn_add)
    public void onViewClicked() {
        sb.append(RandomUtils.getRandomNumbersAndLetters(2));
        setText(sb);
    }


    private void setText(StringBuilder sb) {
        tvInAutoFit.setText(sb);
        aftv.setText(sb);
        tv.setText(sb);
    }
}
