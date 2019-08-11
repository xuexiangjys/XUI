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

package com.xuexiang.xuidemo.base.webview.x5;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.tencent.smtt.sdk.TbsReaderView;
import com.xuexiang.xutil.app.PathUtils;
import com.xuexiang.xutil.file.FileUtils;
import com.xuexiang.xutil.security.EncryptUtils;

import static com.tencent.smtt.sdk.TbsReaderView.KEY_FILE_PATH;
import static com.tencent.smtt.sdk.TbsReaderView.KEY_TEMP_PATH;

/**
 * 腾讯X5文件阅读器  文件打开核心类
 *
 * @author xuexiang
 * @since 2019-07-21 22:50
 */
public class FileReaderView extends FrameLayout implements TbsReaderView.ReaderCallback {

    private TbsReaderView mTbsReaderView;
    private Context mContext;

    private String mLoadFilePath;

    public FileReaderView(Context context) {
        this(context, null, 0);
    }

    public FileReaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FileReaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTbsReaderView = getTbsReaderView(context);
        addView(mTbsReaderView, new LinearLayout.LayoutParams(-1, -1));
        mContext = context;
    }


    private TbsReaderView getTbsReaderView(Context context) {
        return new TbsReaderView(context, this);
    }

    /**
     * 初始化完布局调用此方法浏览文件
     *
     * @param filePath 文件路径
     */
    public void show(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            //加载文件
            Bundle localBundle = new Bundle();
            localBundle.putString(KEY_FILE_PATH, filePath);
            localBundle.putString(KEY_TEMP_PATH, Environment.getExternalStorageDirectory() + "/" + "TbsReaderTemp");
            if (mTbsReaderView == null) {
                mTbsReaderView = getTbsReaderView(mContext);
            }
            boolean bool = mTbsReaderView.preOpen(getFileType(filePath), false);
            if (bool) {
                mLoadFilePath = filePath;
                mTbsReaderView.openFile(localBundle);
            }
        } else {
            Log.e("TAG", "文件路径无效！");
        }
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {
    }

    /**
     * 务必在onDestroy方法中调用此方法，否则第二次打开无法浏览
     */
    public void stop() {
        if (mTbsReaderView != null) {
            mTbsReaderView.onStop();
        }
    }

    /**
     * @return 加载文件的路径
     */
    public String getLoadFilePath() {
        return mLoadFilePath;
    }

    /**
     * @return 加载文件的路径
     */
    @Nullable
    public Uri getLoadFileUri() {
        return PathUtils.getUriForFile(FileUtils.getFileByPath(mLoadFilePath));
    }

    /**
     * 文件下载保存的目录
     *
     * @return
     */
    public String getCacheFileDir() {
        return FileUtils.getDiskCacheDir("x5");
    }

    /**
     * 根据下载地址获取文件名
     *
     * @param url
     * @return
     */
    public String getFileNameByUrl(String url) {
        return EncryptUtils.encryptMD5ToString(url) + "." + getFileType(url);
    }

    /***
     * 获取文件类型
     */
    private String getFileType(String paramString) {
        String str = "";

        if (TextUtils.isEmpty(paramString)) {
            return str;
        }
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            return str;
        }
        str = paramString.substring(i + 1);
        return str;
    }

}
