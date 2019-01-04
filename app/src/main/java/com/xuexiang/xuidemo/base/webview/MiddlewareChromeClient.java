/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.base.webview;

import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebView;

import com.just.agentweb.core.client.MiddlewareWebChromeBase;

/**
 * WebChrome（WebChromeClient主要辅助WebView处理JavaScript的对话框、网站图片、网站title、加载进度等）中间件
 *
 * @author xuexiang
 * @since 2019/1/4 下午11:31
 */
public class MiddlewareChromeClient extends MiddlewareWebChromeBase {

    public MiddlewareChromeClient() {

    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        Log.i("Info", "onJsAlert:" + url);
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        Log.i("Info", "onProgressChanged:");
    }
}
