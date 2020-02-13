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

package com.xuexiang.xuidemo.fragment.expands.camera;

import android.graphics.Bitmap;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;
import com.xuexiang.xaop.annotation.MainThread;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.alpha.XUIAlphaImageView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.wonderkiln.camerakit.CameraKit.Constants.FLASH_AUTO;
import static com.wonderkiln.camerakit.CameraKit.Constants.FLASH_OFF;
import static com.wonderkiln.camerakit.CameraKit.Constants.FLASH_ON;
import static com.wonderkiln.camerakit.CameraKit.Constants.FLASH_TORCH;

/**
 * CameraKit 相机工具
 *
 * @author xuexiang
 * @since 2020-02-13 10:47
 */
@Page(name = "CameraKit")
public class CameraKitFragment extends BaseFragment {

    @BindView(R.id.camera_view)
    CameraView cameraView;
    @BindView(R.id.iv_photo)
    AppCompatImageView ivPhoto;
    @BindView(R.id.iv_flash_light)
    XUIAlphaImageView ivFlashLight;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_camerakit;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("Github") {
            @Override
            public void performAction(View view) {
                Utils.goWeb(getContext(), "https://github.com/CameraKit/camerakit-android");
            }
        });
        return titleBar;
    }

    @Override
    protected void initViews() {
        switchFlashIcon(cameraView.getFlash());
        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @MainThread
            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                Bitmap bitmap = cameraKitImage.getBitmap();
                ivPhoto.setImageBitmap(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });
    }

    @SingleClick
    @OnClick({R.id.iv_flash_light, R.id.iv_face, R.id.iv_take_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_flash_light:
                switchFlashIcon(cameraView.toggleFlash());
                break;
            case R.id.iv_face:
                cameraView.toggleFacing();
                break;
            case R.id.iv_take_photo:
                cameraView.captureImage();
                break;
            default:
                break;
        }
    }

    private void switchFlashIcon(int flash) {
        switch (flash) {
            case FLASH_OFF:
                ivFlashLight.setImageResource(R.drawable.ic_flash_off);
                break;
            case FLASH_ON:
                ivFlashLight.setImageResource(R.drawable.ic_flash_on);
                break;
            case FLASH_AUTO:
                ivFlashLight.setImageResource(R.drawable.ic_flash_auto);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    public void onPause() {
        cameraView.stop();
        super.onPause();
    }
}
