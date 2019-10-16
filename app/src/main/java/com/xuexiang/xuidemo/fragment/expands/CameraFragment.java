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

package com.xuexiang.xuidemo.fragment.expands;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.fragment.expands.camera.CameraActivity;
import com.xuexiang.xutil.app.PathUtils;
import com.xuexiang.xutil.file.FileUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.xuexiang.xuidemo.fragment.expands.camera.CameraActivity.REQUEST_CODE_OPEN_CAMERA;
import static com.xuexiang.xuidemo.fragment.expands.camera.PictureCropActivity.KEY_PICTURE_PATH;

/**
 * @author xuexiang
 * @since 2019-10-16 10:25
 */
@Page(name = "照相机", extra = R.drawable.ic_expand_camera)
public class CameraFragment extends BaseFragment {

    @BindView(R.id.iv_content)
    AppCompatImageView ivContent;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_camera;
    }

    @Override
    protected void initViews() {

    }

    @SingleClick
    @OnClick(R.id.btn_camera)
    public void onViewClicked(View view) {
        switch(view.getId()) {
            case R.id.btn_camera:
                CameraActivity.open(this);
                break;
            default:
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_OPEN_CAMERA && resultCode == RESULT_OK) {
            if (data != null) {
                String imgPath = data.getStringExtra(KEY_PICTURE_PATH);
                ivContent.setImageURI(PathUtils.getUriForFile(FileUtils.getFileByPath(imgPath)));
            }
        }
    }
}
