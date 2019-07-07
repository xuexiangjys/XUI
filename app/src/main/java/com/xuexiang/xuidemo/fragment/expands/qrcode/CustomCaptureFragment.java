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

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xqrcode.XQRCode;
import com.xuexiang.xqrcode.camera.CameraManager;
import com.xuexiang.xqrcode.ui.CaptureActivity;
import com.xuexiang.xqrcode.ui.CaptureFragment;
import com.xuexiang.xqrcode.util.QRCodeAnalyzeUtils;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xutil.tip.ToastUtils;

import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * <pre>
 *     desc   : 自定义二维码扫描界面
 *     author : xuexiang
 *     time   : 2018/5/6 下午11:48
 * </pre>
 */
@Page(name = "自定义二维码扫描", anim = CoreAnim.none)
public class CustomCaptureFragment extends XPageFragment {
    public static boolean isOpen = false;
    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_custom_capture;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        // 为二维码扫描界面设置定制化界面
        CaptureFragment captureFragment = XQRCode.getCaptureFragment(R.layout.layout_custom_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        captureFragment.setCameraInitCallBack(new CaptureFragment.CameraInitCallBack() {
            @Override
            public void callBack(Exception e) {
                if (e != null) {
                    CaptureActivity.showNoPermissionTip(getActivity(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            popToBack();
                        }
                    });
                }
            }
        });
        getChildFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }

    /**
     * 二维码解析回调函数
     */
    QRCodeAnalyzeUtils.AnalyzeCallback analyzeCallback = new QRCodeAnalyzeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(XQRCode.RESULT_TYPE, XQRCode.RESULT_SUCCESS);
            bundle.putString(XQRCode.RESULT_DATA, result);
            resultIntent.putExtras(bundle);
            setFragmentResult(RESULT_OK, resultIntent);
            popToBack();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(XQRCode.RESULT_TYPE, XQRCode.RESULT_FAILED);
            bundle.putString(XQRCode.RESULT_DATA, "");
            resultIntent.putExtras(bundle);
            setFragmentResult(RESULT_OK, resultIntent);
            popToBack();
        }
    };

    /**
     * 初始化监听
     */
    @Override
    protected void initListeners() {

    }

    @OnClick(R.id.ll_flash_light)
    @SingleClick
    void onClickFlashLight(View v) {
        isOpen = !isOpen;
        try {
            XQRCode.enableFlashLight(isOpen);
        } catch (RuntimeException e) {
            e.printStackTrace();
            ToastUtils.toast("设备不支持闪光灯!");
        }
    }

    @Override
    public void onDestroyView() {
        //恢复设置
        CameraManager.FRAME_MARGIN_TOP = -1;
        super.onDestroyView();
    }
}
