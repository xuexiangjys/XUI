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

package com.xuexiang.xuidemo.base;

import android.os.Bundle;

import com.xuexiang.xrouter.annotation.AutoWired;
import com.xuexiang.xrouter.annotation.Router;
import com.xuexiang.xrouter.launcher.XRouter;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.tip.ToastUtils;

/**
 * https://xuexiangjys.club/xpage/transfer?pageName=xxxxx&....
 * applink的中转
 *
 * @author xuexiang
 * @since 2019-07-06 9:37
 */
@Router(path = "/xpage/transfer")
public class XPageTransferActivity extends BaseActivity {

    @AutoWired(name = "pageName")
    String pageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XRouter.getInstance().inject(this);

        if (!StringUtils.isEmpty(pageName)) {
            if (openPage(pageName, getIntent().getExtras()) == null) {
                ToastUtils.toast("页面未找到！");
                finish();
            }
        } else {
            ToastUtils.toast("页面未找到！");
            finish();
        }
    }
}
