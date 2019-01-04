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

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;

import com.just.agentweb.core.web.AgentWebUIControllerImplBase;

/**
 * 如果你需要修改某一个AgentWeb 内部的某一个弹窗 ，请看下面的例子
 * 注意写法一定要参照 DefaultUIController 的写法 ，因为UI自由定制，但是回调的方式是固定的，并且一定要回调。
 */
public class UIController extends AgentWebUIControllerImplBase {

    private Activity mActivity;

    public UIController(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onShowMessage(String message, String from) {
        super.onShowMessage(message, from);
        Log.i(TAG, "message:" + message);
    }

    @Override
    public void onSelectItemsPrompt(WebView view, String url, String[] items, Handler.Callback callback) {
        super.onSelectItemsPrompt(view, url, items, callback); // 使用默认的UI
    }

    /**
     * 修改文件选择的弹窗
     */
   /* @Override
    public void onSelectItemsPrompt(WebView view, String mUrl, String[] ways, final Handler.Callback callback) {
        //super.onSelectItemsPrompt(view,mUrl,ways,callback); //这行应该注释或者删除掉
        final AlertDialog mAlertDialog = new AlertDialog.Extra(mActivity)//
                .setSingleChoiceItems(ways, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (callback != null) {
                            Message mMessage = Message.obtain();
                            mMessage.what = which;  //mMessage.what 必须等于ways的index
                            callback.handleMessage(mMessage); //最后callback一定要回调
                        }

                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                        if (callback != null) {
                            callback.handleMessage(Message.obtain(null, -1)); //-1表示取消  //最后callback一定要回调
                        }
                    }
                }).create();
        mAlertDialog.show();

    }*/
}
