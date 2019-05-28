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

package com.xuexiang.xuidemo.fragment.expands.webview;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.widget.FrameLayout;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.webview.BaseWebViewFragment;
import com.xuexiang.xuidemo.utils.Utils;
import com.xuexiang.xutil.tip.ToastUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2019-05-26 18:13
 */
@Page(name = "简单的JS通信")
public class JsWebViewFragment extends BaseWebViewFragment {

    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_js_webview;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        mAgentWeb = Utils.createAgentWeb(this, flContainer, "file:///android_asset/jsTest.html");

        //注入接口,供JS调用
        mAgentWeb.getJsInterfaceHolder().addJavaObject("Android", new AndroidInterface());

    }


    @SingleClick
    @OnClick({R.id.btn_js_no_param, R.id.btn_js_one_param, R.id.btn_js_more_param, R.id.btn_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_js_no_param:
                mAgentWeb.getJsAccessEntrace().quickCallJs("callByAndroidNoParam");
                break;
            case R.id.btn_js_one_param:
                mAgentWeb.getJsAccessEntrace().quickCallJs("callByAndroidOneParam", "Hello ! Agentweb");
                break;
            case R.id.btn_js_more_param:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mAgentWeb.getJsAccessEntrace().quickCallJs("callByAndroidMoreParams", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            Log.e("xuexiang", "这里是调用JS方法的返回值:" + value);
                        }
                    }, getJson(), "say！", " Hello! AgentWeb");
                }
                break;
            case R.id.btn_clear:
                mAgentWeb.getJsAccessEntrace().quickCallJs("clearLog");
                break;
            default:
                break;
        }
    }

    private String getJson() {
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", 1);
            jsonObject.put("name", "AgentWeb");
            jsonObject.put("age", 18);
            result = jsonObject.toString();
        } catch (Exception e) {

        }

        return result;
    }


    /**
     * 注入到JS里的对象接口
     */
    public class AndroidInterface {

        @JavascriptInterface
        public void callAndroid(final String msg) {
            ToastUtils.toast("这是Js调用Android的方法，内容:" + msg);
        }

    }

}
