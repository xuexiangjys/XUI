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

package com.xuexiang.xuidemo.fragment.expands.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.xrouter.annotation.AutoWired;
import com.xuexiang.xrouter.launcher.XRouter;
import com.xuexiang.xui.widget.imageview.crop.CropImageType;
import com.xuexiang.xui.widget.imageview.crop.CropImageView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.display.ImageUtils;
import com.xuexiang.xutil.file.FileUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.xuexiang.xuidemo.utils.Utils.JPEG;

/**
 * 图片裁剪页面
 *
 * @author xuexiang
 * @since 2019-09-29 16:33
 */
public class PictureCropActivity extends AppCompatActivity {

    public static final String KEY_PICTURE_PATH = "key_picture_path";
    public static final String KEY_IS_CAMERA = "key_is_camera";
    public static final int REQUEST_CODE_PICTURE_CROP = 1122;

    public static void open(@NonNull Activity activity, boolean isCamera, String imgPath) {
        Intent intent = new Intent(activity, PictureCropActivity.class);
        intent.putExtra(KEY_IS_CAMERA, isCamera);
        intent.putExtra(KEY_PICTURE_PATH, imgPath);
        activity.startActivityForResult(intent, REQUEST_CODE_PICTURE_CROP);
    }

    @BindView(R.id.crop_image_view)
    CropImageView mCropImageView;
    private Unbinder mUnbinder;

    @AutoWired(name = KEY_PICTURE_PATH)
    String mImgPath;
    /**
     * 是拍摄的图片
     */
    @AutoWired(name = KEY_IS_CAMERA)
    boolean mIsCamera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_crop);
        mUnbinder = ButterKnife.bind(this);
        XRouter.getInstance().inject(this);

        if (StringUtils.isEmpty(mImgPath)) {
            finish();
            return;
        }

        Bitmap bit = BitmapFactory.decodeFile(mImgPath);
        mCropImageView.setImageBitmap(bit);
        // 触摸时显示网格
        mCropImageView.setGuidelines(CropImageType.CROPIMAGE_GRID_ON);
        // 自由剪切
        mCropImageView.setFixedAspectRatio(false);
    }


    @OnClick({R.id.iv_close, R.id.iv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                handleBackPressed();
                break;
            case R.id.iv_submit:
                Bitmap bitmap = mCropImageView.getCroppedImage();
                ImageUtils.save(bitmap, mImgPath, Bitmap.CompressFormat.JPEG);

                handlePictureResult(mImgPath);
                break;
            default:
                break;
        }
    }

    private void handlePictureResult(String imgPath) {
        setResult(RESULT_OK, new Intent().putExtra(KEY_PICTURE_PATH, imgPath));
        finish();
    }

    private void handleBackPressed() {
        if (mIsCamera) {
            FileUtils.deleteFile(mImgPath);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        handleBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }
}
