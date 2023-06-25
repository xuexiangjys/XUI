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

package com.xuexiang.xuidemo.fragment.expands.materialdesign;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.edittext.materialedittext.validation.RegexpValidator;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;

/**
 * 输入框，不是非常好用
 *
 * @author xuexiang
 * @since 5/16/23 12:30 AM
 */
@Page(name = "TextInputLayout")
public class TextInputLayoutFragment extends BaseFragment {

    @BindView(R.id.et_phone)
    TextInputEditText etPhone;
    @BindView(R.id.til_phone)
    TextInputLayout tilPhone;

    private RegexpValidator mValidator;

    private TextWatcher mTextWatcher;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_textinput_layout;
    }

    @Override
    protected void initViews() {
        mValidator = new RegexpValidator(getString(R.string.tip_phone_number_error), getString(R.string.regex_phone_number));

        etPhone.addTextChangedListener(mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (mValidator.isValid(input, input.isEmpty())) {
                    tilPhone.setError(null);
                } else {
                    tilPhone.setError(mValidator.getErrorMessage());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        etPhone.removeTextChangedListener(mTextWatcher);
        super.onDestroyView();
    }
}
