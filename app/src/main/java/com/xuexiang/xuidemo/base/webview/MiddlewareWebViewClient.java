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

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import android.support.annotation.RequiresApi;

import com.just.agentweb.core.client.MiddlewareWebClientBase;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.tip.ToastUtils;

import java.net.URISyntaxException;

/**
 * 【网络请求、加载】
 * WebClient（WebViewClient 这个类主要帮助WebView处理各种通知、url加载，请求时间的）中间件
 * <p>
 * <p>
 * 方法的执行顺序，例如下面用了7个中间件一个 WebViewClient
 * <p>
 * .useMiddlewareWebClient(getMiddlewareWebClient())  // 1
 * .useMiddlewareWebClient(getMiddlewareWebClient())  // 2
 * .useMiddlewareWebClient(getMiddlewareWebClient())  // 3
 * .useMiddlewareWebClient(getMiddlewareWebClient())  // 4
 * .useMiddlewareWebClient(getMiddlewareWebClient())  // 5
 * .useMiddlewareWebClient(getMiddlewareWebClient())  // 6
 * .useMiddlewareWebClient(getMiddlewareWebClient())  // 7
 * DefaultWebClient                                  // 8
 * .setWebViewClient(mWebViewClient)                 // 9
 * <p>
 * <p>
 * 典型的洋葱模型
 * 对象内部的方法执行顺序: 1->2->3->4->5->6->7->8->9->8->7->6->5->4->3->2->1
 * <p>
 * <p>
 * 中断中间件的执行， 删除super.methodName(...) 这行即可
 *
 * 这里主要是做去广告的工作
 */
public class MiddlewareWebViewClient extends MiddlewareWebClientBase {

    public MiddlewareWebViewClient() {
    }

    private static int count = 1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Log.i("Info", "MiddlewareWebViewClient -- >  shouldOverrideUrlLoading:" + request.getUrl().toString() + "  c:" + (count++));
        if (shouldOverrideUrlLoadingByApp(view, request.getUrl().toString())) {
            return true;
        }
        return super.shouldOverrideUrlLoading(view, request);

    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.i("Info", "MiddlewareWebViewClient -- >  shouldOverrideUrlLoading:" + url + "  c:" + (count++));
        if (shouldOverrideUrlLoadingByApp(view, url)) {
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        url = url.toLowerCase();
        if (!hasAd(url)) {
            //正常加载
            return super.shouldInterceptRequest(view, url);
        } else {
            //含有广告资源屏蔽请求
            return new WebResourceResponse(null, null, null);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString().toLowerCase();
        if (!hasAd(url)) {
            //正常加载
            return super.shouldInterceptRequest(view, request);
        } else {
            //含有广告资源屏蔽请求
            return new WebResourceResponse(null, null, null);
        }
    }

    /**
     * 判断是否存在广告的链接
     *
     * @param url
     * @return
     */
    public static boolean hasAd(String url) {
        String[] adUrls = ResUtils.getStringArray(R.array.adBlockUrl);
        for (String adUrl : adUrls) {
            if (url.contains(adUrl)) {
                return true;
            }
        }
        return false;
    }

    public static final String APP_LINK_HOST = "xuexiangjys.club";
    public static final String APP_LINK_ACTION = "com.xuexiang.xui.applink";


    /**
     * 根据url的scheme处理跳转第三方app的业务
     */
    private boolean shouldOverrideUrlLoadingByApp(WebView webView, final String url) {

        if (url.startsWith("http") || url.startsWith("https") || url.startsWith("ftp")) {
            //不处理http, https, ftp的请求,除了host = xuexiangjys.club的情况，主要是用于处理AppLink
            Uri uri = Uri.parse(url);
            if (uri != null && !(APP_LINK_HOST.equals(uri.getHost())
                    //防止xui官网被拦截
                    && url.contains("xpage"))) {
                return false;
            }
        }

        DialogLoader.getInstance().showConfirmDialog(
                webView.getContext(),
                getOpenTitle(url),
                ResUtils.getString(R.string.lab_yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (isAppLink(url)) {
                            openAppLink(webView.getContext(), url);
                        } else {
                            openApp(url);
                        }
                    }
                },
                ResUtils.getString(R.string.lab_no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );
        return true;
    }

    private String getOpenTitle(String url) {
        String scheme = getScheme(url);
        if ("mqqopensdkapi".equals(scheme)) {
            return "是否允许页面打开\"QQ\"?";
        } else {
            return ResUtils.getString(R.string.lab_open_third_app);
        }
    }

    private String getScheme(String url) {
        try {
            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            return intent.getScheme();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "";
    }

    private boolean isAppLink(String url) {
        Uri uri = Uri.parse(url);
        return uri != null
                && APP_LINK_HOST.equals(uri.getHost())
                && (url.startsWith("http") || url.startsWith("https"));
    }


    private void openApp(String url) {
        Intent intent;
        try {
            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            XUtil.getContext().startActivity(intent);
        } catch (Exception e) {
            ToastUtils.toast("您所打开的第三方App未安装！");
        }
    }

    private void openAppLink(Context context, String url) {
        try {
            Intent intent = new Intent(APP_LINK_ACTION);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtils.toast("您所打开的第三方App未安装！");
        }
    }

}
