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
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.just.agentweb.core.client.MiddlewareWebClientBase;

/**
 * WebClient（WebViewClient 这个类主要帮助WebView处理各种通知、url加载，请求时间的）中间件
 *
 *
 * 方法的执行顺序，例如下面用了7个中间件一个 WebViewClient
 *
 * .useMiddlewareWebClient(getMiddlewareWebClient())  // 1
 * .useMiddlewareWebClient(getMiddlewareWebClient())  // 2
 * .useMiddlewareWebClient(getMiddlewareWebClient())  // 3
 * .useMiddlewareWebClient(getMiddlewareWebClient())  // 4
 * .useMiddlewareWebClient(getMiddlewareWebClient())  // 5
 * .useMiddlewareWebClient(getMiddlewareWebClient())  // 6
 * .useMiddlewareWebClient(getMiddlewareWebClient())  // 7
 *  DefaultWebClient                                  // 8
 *  .setWebViewClient(mWebViewClient)                 // 9
 *
 *
 * 典型的洋葱模型
 * 对象内部的方法执行顺序: 1->2->3->4->5->6->7->8->9->8->7->6->5->4->3->2->1
 *
 *
 * 中断中间件的执行， 删除super.methodName(...) 这行即可
 *
 */
public class MiddlewareWebViewClient extends MiddlewareWebClientBase {

    public MiddlewareWebViewClient() {
    }

    private static int count = 1;

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Log.i("Info", "MiddlewareWebViewClient -- >  shouldOverrideUrlLoading:" + request.getUrl().toString() + "  c:" + (count++));
        return super.shouldOverrideUrlLoading(view, request);

    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.i("Info", "MiddlewareWebViewClient -- >  shouldOverrideUrlLoading:" + url + "  c:" + (count++));
        return super.shouldOverrideUrlLoading(view, url);

    }
}
