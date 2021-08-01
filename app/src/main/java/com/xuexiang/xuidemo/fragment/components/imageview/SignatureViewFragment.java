/*
 * Copyright (C) 2021 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.components.imageview;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.imageview.SignatureView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.XToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2021/5/27 1:19 AM
 */
@Page(name = "SignatureView\n电子签名工具")
public class SignatureViewFragment extends BaseFragment {

    @BindView(R.id.signature_view)
    SignatureView signatureView;
    @BindView(R.id.iv_signature)
    ImageView ivSignature;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_signatureview;
    }

    @Override
    protected void initViews() {
    }

    @OnClick({R.id.btn_clear, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_clear:
                signatureView.clear();
                break;
            case R.id.btn_submit:
                onSubmit(signatureView);
                break;
            default:
                break;
        }
    }

    private void onSubmit(SignatureView signatureView) {
        Bitmap bitmap = signatureView.getSnapshot();
        if (bitmap != null) {
            showSignatureImg(bitmap);
        } else {
            XToastUtils.warning("您未签名~");
        }

    }

    private void showSignatureImg(Bitmap bitmap) {
        ivSignature.setImageBitmap(bitmap);
    }

}
