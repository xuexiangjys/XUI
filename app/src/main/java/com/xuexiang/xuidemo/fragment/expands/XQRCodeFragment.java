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

package com.xuexiang.xuidemo.fragment.expands;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.xuexiang.xaop.annotation.IOThread;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xaop.enums.ThreadType;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.utils.TitleBar;
import com.xuexiang.xqrcode.XQRCode;
import com.xuexiang.xqrcode.ui.CaptureActivity;
import com.xuexiang.xqrcode.util.QRCodeAnalyzeUtils;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseSimpleListFragment;
import com.xuexiang.xuidemo.fragment.expands.qrcode.CustomCaptureFragment;
import com.xuexiang.xuidemo.fragment.expands.qrcode.QRCodeProduceFragment;
import com.xuexiang.xutil.app.IntentUtils;
import com.xuexiang.xutil.app.PathUtils;
import com.xuexiang.xutil.common.ClickUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.xuexiang.xaop.consts.PermissionConsts.CAMERA;
import static com.xuexiang.xaop.consts.PermissionConsts.STORAGE;

/**
 * 二维码扫描
 *
 * @author xuexiang
 * @since 2018/12/29 下午12:37
 */
@Page(name = "二维码", extra = R.drawable.ic_expand_qrcode)
public class XQRCodeFragment extends BaseSimpleListFragment {
    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;
    /**
     * 选择系统图片Request Code
     */
    public static final int REQUEST_IMAGE = 112;
    /**
     * 定制化扫描界面Request Code
     */
    public static final int REQUEST_CUSTOM_SCAN = 113;


    @Override
    protected void initArgs() {
        initPermission();
    }

    /**
     * 初始化例子
     *
     * @param lists
     * @return
     */
    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("默认扫描界面");
        lists.add("定制化扫描界面");
        lists.add("远程扫描界面");
        lists.add("生成二维码图片");
        lists.add("选择二维码进行解析");
        return lists;
    }

    /**
     * 条目点击
     *
     * @param position
     */
    @Override
    protected void onItemClick(int position) {
        switch (position) {
            case 0:
                startScan(ScanType.DEFAULT);
                break;
            case 1:
                startScan(ScanType.CUSTOM);
                break;
            case 2:
                startScan(ScanType.REMOTE);
                break;
            case 3:
                openPage(QRCodeProduceFragment.class);
                break;
            case 4:
                selectQRCode();
                break;
            default:
                break;
        }
    }

    @Permission(STORAGE)
    private void selectQRCode() {
        startActivityForResult(IntentUtils.getDocumentPickerIntent(IntentUtils.DocumentType.IMAGE), REQUEST_IMAGE);
    }

    /**
     * 开启二维码扫描
     */
    @Permission(CAMERA)
    @IOThread(ThreadType.Single)
    private void startScan(ScanType scanType) {
        switch (scanType) {
            case CUSTOM:
                openPageForResult(CustomCaptureFragment.class, null, REQUEST_CUSTOM_SCAN);
                break;
            case DEFAULT:
                startActivityForResult(new Intent(getActivity(), CaptureActivity.class), REQUEST_CODE);
                break;
            case REMOTE:
                Intent intent = new Intent(XQRCode.ACTION_DEFAULT_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CUSTOM_SCAN && resultCode == RESULT_OK) {
            handleScanResult(data);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理二维码扫描结果
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            //处理扫描结果（在界面上显示）
            handleScanResult(data);
        }

        //选择系统图片并解析
        else if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                getAnalyzeQRCodeResult(uri);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getAnalyzeQRCodeResult(Uri uri) {
        XQRCode.analyzeQRCode(PathUtils.getFilePathByUri(getContext(), uri), new QRCodeAnalyzeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                ToastUtils.toast("解析结果:" + result, Toast.LENGTH_LONG);
            }

            @Override
            public void onAnalyzeFailed() {
                ToastUtils.toast("解析二维码失败", Toast.LENGTH_LONG);
            }
        });
    }


    /**
     * 处理二维码扫描结果
     *
     * @param data
     */
    private void handleScanResult(Intent data) {
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                if (bundle.getInt(XQRCode.RESULT_TYPE) == XQRCode.RESULT_SUCCESS) {
                    String result = bundle.getString(XQRCode.RESULT_DATA);
                    ToastUtils.toast("解析结果:" + result, Toast.LENGTH_LONG);
                } else if (bundle.getInt(XQRCode.RESULT_TYPE) == XQRCode.RESULT_FAILED) {
                    ToastUtils.toast("解析二维码失败", Toast.LENGTH_LONG);
                }
            }
        }
    }

    @Permission(CAMERA)
    private void initPermission() {
        ToastUtils.toast("相机权限已获取！");
    }

    /**
     * 二维码扫描类型
     */
    public enum ScanType {
        /**
         * 默认
         */
        DEFAULT,
        /**
         * 远程
         */
        REMOTE,
        /**
         * 自定义
         */
        CUSTOM,
    }


}
