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

package com.xuexiang.xuidemo.fragment.components.imageview.edit;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.imageview.crop.CropImageType;
import com.xuexiang.xui.widget.imageview.crop.CropImageView;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xutil.app.IntentUtils;
import com.xuexiang.xutil.app.PathUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.xuexiang.xaop.consts.PermissionConsts.STORAGE;
import static com.xuexiang.xuidemo.fragment.expands.XQRCodeFragment.REQUEST_IMAGE;

/**
 * @author xuexiang
 * @since 2019-10-19 17:29
 */
@Page(name = "图片裁剪")
public class ImageCropFragment extends BaseFragment {

    @BindView(R.id.crop_image_view)
    CropImageView mCropImageView;
    @BindView(R.id.btn_rotate)
    SuperButton btnRotate;
    @BindView(R.id.btn_crop)
    SuperButton btnCrop;


    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_image_crop;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        // 触摸时显示网格
        mCropImageView.setGuidelines(CropImageType.CROPIMAGE_GRID_ON);
        // 自由剪切
        mCropImageView.setFixedAspectRatio(false);

        btnRotate.setEnabled(false);
        btnCrop.setEnabled(false);
    }

    @SingleClick
    @OnClick({R.id.btn_select, R.id.btn_crop, R.id.btn_rotate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_select:
                selectImage();
                break;
            case R.id.btn_rotate:
                mCropImageView.rotateImage(90);
                break;
            case R.id.btn_crop:
                mCropImageView.cropImage();
                //使用getCroppedImage获取裁剪的图片

                btnRotate.setEnabled(false);
                btnCrop.setEnabled(false);
                break;
            default:
                break;
        }
    }

    @Permission(STORAGE)
    private void selectImage() {
        startActivityForResult(IntentUtils.getDocumentPickerIntent(IntentUtils.DocumentType.IMAGE), REQUEST_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选择系统图片并解析
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    mCropImageView.setImagePath(PathUtils.getFilePathByUri(uri));
                    btnRotate.setEnabled(true);
                    btnCrop.setEnabled(true);
                }
            }
        }
    }


}
