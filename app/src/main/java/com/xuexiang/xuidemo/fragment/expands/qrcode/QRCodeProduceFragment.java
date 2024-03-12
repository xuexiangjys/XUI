/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.expands.qrcode;

import static android.app.Activity.RESULT_OK;
import static com.xuexiang.xaop.consts.PermissionConsts.STORAGE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.SwitchCompat;

import com.xuexiang.xaop.annotation.IOThread;
import com.xuexiang.xaop.annotation.MainThread;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xaop.enums.ThreadType;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xqrcode.XQRCode;
import com.xuexiang.xqrcode.util.QRCodeProduceUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xutil.app.IntentUtils;
import com.xuexiang.xutil.app.PathUtils;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.data.DateUtils;
import com.xuexiang.xutil.display.ImageUtils;
import com.xuexiang.xutil.file.FileUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 *     desc   : 二维码生成界面
 *     author : xuexiang
 *     time   : 2018/5/5 下午11:06
 * </pre>
 */
@Page(name = "二维码生成器")
public class QRCodeProduceFragment extends BaseFragment {
    private final int SELECT_FILE_REQUEST_CODE = 700;

    /**
     * 二维码背景图片
     */
    private Bitmap backgroundImage = null;

    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.iv_qrcode)
    ImageView mIvQrcode;

    @BindView(R.id.sc_change)
    SwitchCompat mScChange;

    @BindView(R.id.ll_normal_create)
    LinearLayout mLLNormalCreate;

    @BindView(R.id.ll_complex_create)
    LinearLayout mLLComplexCreate;
    @BindView(R.id.et_size)
    EditText mEtSize;
    @BindView(R.id.et_margin)
    EditText mEtMargin;
    @BindView(R.id.et_dotScale)
    EditText mEtDotScale;

    @BindView(R.id.cb_autoColor)
    CheckBox mCbAutoColor;
    @BindView(R.id.et_colorDark)
    EditText mEtColorDark;
    @BindView(R.id.et_colorLight)
    EditText mEtColorLight;

    @BindView(R.id.cb_whiteMargin)
    CheckBox mCbWhiteMargin;
    @BindView(R.id.cb_binarize)
    CheckBox mCbBinarize;
    @BindView(R.id.et_binarizeThreshold)
    EditText mEtBinarizeThreshold;


    private boolean isQRCodeCreated = false;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_qrcode_produce;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {

    }

    /**
     * 初始化监听
     */
    @Override
    protected void initListeners() {
        mScChange.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mLLComplexCreate.setVisibility(View.VISIBLE);
                mLLNormalCreate.setVisibility(View.GONE);
            } else {
                mLLComplexCreate.setVisibility(View.GONE);
                mLLNormalCreate.setVisibility(View.VISIBLE);
            }
        });

        mCbAutoColor.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mEtColorDark.setEnabled(!isChecked);
            mEtColorLight.setEnabled(!isChecked);
        });

        mCbBinarize.setOnCheckedChangeListener((buttonView, isChecked) -> mEtBinarizeThreshold.setEnabled(isChecked));
    }

    @OnClick({R.id.btn_save, R.id.btn_create_no_logo, R.id.btn_create_with_logo, R.id.btn_background_image, R.id.btn_remove_background_image, R.id.btn_create})
    void OnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                saveQRCode();
                break;
            case R.id.btn_create_no_logo:
                if (StringUtils.isSpace(mEtInput.getEditableText().toString())) {
                    XToastUtils.warning("请输入二维码内容!");
                    return;
                }

                createQRCodeWithLogo(null);

                break;
            case R.id.btn_create_with_logo:
                if (StringUtils.isSpace(mEtInput.getEditableText().toString())) {
                    XToastUtils.toast("请输入二维码内容!");
                    return;
                }

                createQRCodeWithLogo(ImageUtils.getBitmap(R.mipmap.ic_launcher));

                break;

            case R.id.btn_background_image:
                startActivityForResult(IntentUtils.getDocumentPickerIntent(IntentUtils.DocumentType.IMAGE), SELECT_FILE_REQUEST_CODE);
                break;
            case R.id.btn_remove_background_image:
                backgroundImage = null;
                XToastUtils.toast("背景图片已被去除！");
                break;
            case R.id.btn_create:
                if (StringUtils.isSpace(mEtInput.getEditableText().toString())) {
                    XToastUtils.warning("请输入二维码内容!");
                    return;
                }

                createQRCodeWithBackgroundImage();

                break;
            default:
                break;
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FILE_REQUEST_CODE && resultCode == RESULT_OK && data.getData() != null) {
            try {
                Uri imageUri = data.getData();
                backgroundImage = BitmapFactory.decodeFile(PathUtils.getFilePathByUri(getContext(), imageUri));
                XToastUtils.toast("成功添加背景图片！");
            } catch (Exception e) {
                e.printStackTrace();
                XToastUtils.error("添加背景图片失败！");
            }
        }
    }

    @IOThread(ThreadType.Disk)
    @Permission(STORAGE)
    private void saveQRCode() {
        if (isQRCodeCreated) {
            boolean result = ImageUtils.save(ImageUtils.view2Bitmap(mIvQrcode), FileUtils.getDiskCacheDir() + File.separator + "XQRCode_" + DateUtils.getNowMills() + ".png", Bitmap.CompressFormat.PNG);
            XToastUtils.toast("二维码保存" + (result ? "成功" : "失败") + "!");
        } else {
            XToastUtils.toast("请先生成二维码!");
        }
    }

    /**
     * 生成简单的带logo的二维码
     * @param logo
     */
    @IOThread(ThreadType.Single)
    private void createQRCodeWithLogo(Bitmap logo) {
        showQRCode(XQRCode.createQRCodeWithLogo(mEtInput.getText().toString(), 400, 400, logo));
        isQRCodeCreated = true;
    }

    @MainThread
    private void showQRCode(Bitmap QRCode) {
        mIvQrcode.setImageBitmap(QRCode);
    }

    /**
     * 生成复杂的带背景图案的二维码
     */
    @IOThread(ThreadType.Single)
    private void createQRCodeWithBackgroundImage() {
        QRCodeProduceUtils.Builder builder = XQRCode.newQRCodeBuilder(mEtInput.getText().toString())
                .setAutoColor(mCbAutoColor.isChecked())
                .setWhiteMargin(mCbWhiteMargin.isChecked())
                .setBinarize(mCbBinarize.isChecked())
                .setBackgroundImage(backgroundImage);
        if (mEtSize.getText().length() != 0) {
            builder.setSize(StringUtils.toInt(mEtSize.getText().toString(), 400));
        }
        if (mEtMargin.getText().length() != 0) {
            builder.setMargin(StringUtils.toInt(mEtMargin.getText().toString(), 20));
        }
        if (mEtDotScale.getText().length() != 0) {
            builder.setDataDotScale(StringUtils.toFloat(mEtDotScale.getText().toString(), 0.3F));
        }
        if (mEtDotScale.getText().length() != 0) {
            builder.setDataDotScale(StringUtils.toFloat(mEtDotScale.getText().toString(), 0.3F));
        }
        if (!mCbAutoColor.isChecked()) {
            try {
                builder.setColorDark(Color.parseColor(mEtColorDark.getText().toString()));
                builder.setColorLight(Color.parseColor(mEtColorLight.getText().toString()));
            } catch (Exception e) {
                e.printStackTrace();
                XToastUtils.error("色值填写出错!");
            }
        }
        if (mEtBinarizeThreshold.getText().length() != 0) {
            builder.setBinarizeThreshold(StringUtils.toInt(mEtBinarizeThreshold.getText().toString(), 128));
        }

        showQRCode(builder.build());
        isQRCodeCreated = true;
    }

}
