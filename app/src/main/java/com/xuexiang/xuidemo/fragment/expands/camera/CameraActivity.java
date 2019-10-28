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
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.cameraview.CameraView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.xuexiang.xaop.annotation.IOThread;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xui.widget.alpha.XUIAlphaImageView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.utils.RotateSensorHelper;
import com.xuexiang.xuidemo.utils.Utils;
import com.xuexiang.xuidemo.utils.XToastUtils;
import com.xuexiang.xutil.common.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.xuexiang.xaop.consts.PermissionConsts.CAMERA;
import static com.xuexiang.xaop.consts.PermissionConsts.STORAGE;
import static com.xuexiang.xuidemo.fragment.expands.camera.PictureCropActivity.REQUEST_CODE_PICTURE_CROP;

/**
 * 简单的相机拍照界面
 *
 * @author xuexiang
 * @since 2019-09-29 13:58
 */
public class CameraActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_OPEN_CAMERA = 1245;

    private static final int[] FLASH_OPTIONS = {
            CameraView.FLASH_AUTO,
            CameraView.FLASH_OFF,
            CameraView.FLASH_ON,
    };

    private static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_auto,
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };
    private int mCurrentFlash;


    @BindView(R.id.iv_flash_light)
    XUIAlphaImageView ivFlashLight;
    @BindView(R.id.iv_camera_close)
    XUIAlphaImageView ivCameraClose;
    @BindView(R.id.iv_take_photo)
    XUIAlphaImageView ivTakePhoto;
    @BindView(R.id.iv_picture_select)
    XUIAlphaImageView ivPictureSelect;
    @BindView(R.id.camera_view)
    CameraView mCameraView;

    private Unbinder mUnbinder;

    private RotateSensorHelper mSensorHelper;


    @Permission({CAMERA, STORAGE})
    public static void open(@NonNull Activity activity) {
        activity.startActivityForResult(new Intent(activity, CameraActivity.class), REQUEST_CODE_OPEN_CAMERA);
    }

    @Permission({CAMERA, STORAGE})
    public static void open(@NonNull Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, CameraActivity.class), requestCode);
    }

    @Permission({CAMERA, STORAGE})
    public static void open(@NonNull Fragment fragment) {
        fragment.startActivityForResult(new Intent(fragment.getContext(), CameraActivity.class), REQUEST_CODE_OPEN_CAMERA);
    }

    @Permission({CAMERA, STORAGE})
    public static void open(@NonNull Fragment fragment, int requestCode) {
        fragment.startActivityForResult(new Intent(fragment.getContext(), CameraActivity.class), requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mUnbinder = ButterKnife.bind(this);

        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }

        List<View> views = new ArrayList<>();
        views.add(ivFlashLight);
        views.add(ivCameraClose);
        views.add(ivTakePhoto);
        views.add(ivPictureSelect);
        mSensorHelper = new RotateSensorHelper(this, views);
    }

    @OnClick({R.id.iv_camera_close, R.id.iv_flash_light, R.id.iv_take_photo, R.id.iv_picture_select})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_camera_close:
                finish();
                break;
            case R.id.iv_flash_light:
                if (mCameraView != null) {
                    mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.length;
                    ivFlashLight.setImageResource(FLASH_ICONS[mCurrentFlash]);
                    mCameraView.setFlash(FLASH_OPTIONS[mCurrentFlash]);
                }
                break;
            case R.id.iv_take_photo:
                if (mCameraView != null) {
                    mCameraView.takePicture();
                }
                break;
            case R.id.iv_picture_select:
                Utils.getPictureSelector(this)
                        .maxSelectNum(1)
                        .isCamera(false)
                        .compress(false)
                        .selectionMode(PictureConfig.SINGLE)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            default:
                break;
        }
    }

    /**
     * 拍照的回调
     */
    private CameraView.Callback mCallback = new CameraView.Callback() {
        @Override
        public void onCameraOpened(CameraView cameraView) {
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
        }

        @Override
        public void onPictureTaken(final CameraView cameraView, final byte[] data) {
            handlePictureTaken(data);
        }
    };

    @IOThread
    private void handlePictureTaken(byte[] data) {
        String picPath = Utils.handleOnPictureTaken(data);
        if (!StringUtils.isEmpty(picPath)) {
            PictureCropActivity.open(this, true, picPath);
        } else {
            XToastUtils.error("图片保存失败！");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择
                    List<LocalMedia> result = PictureSelector.obtainMultipleResult(data);
                    PictureCropActivity.open(this, false, result.get(0).getPath());
                    break;
                case REQUEST_CODE_PICTURE_CROP:
                    setResult(RESULT_OK, data);
                    finish();
                    break;
                default:
                    break;
            }
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        if (mCameraView != null) {
            mCameraView.start();
        }
    }

    @Override
    protected void onPause() {
        if (mCameraView != null) {
            mCameraView.stop();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        if (isFinishing()) {
            onRelease();
        }
        super.onStop();
    }

    /**
     * 资源释放
     */
    protected void onRelease() {
        mSensorHelper.recycle();
    }
}
