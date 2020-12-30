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

package com.xuexiang.xuidemo.fragment.components.textview;

import android.view.View;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.textview.LoggerTextView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2020/12/10 1:09 AM
 */
@Page(name = "LoggerTextView\n日志打印工具")
public class LoggerTextViewFragment extends BaseFragment {

    @BindView(R.id.logger)
    LoggerTextView logger;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_logger_textview;
    }

    @Override
    protected void initViews() {

    }

    @OnClick({R.id.btn_normal, R.id.btn_success, R.id.btn_error, R.id.btn_warning, R.id.btn_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_normal:
                logger.logNormal("这是一条普通日志！");
                break;
            case R.id.btn_success:
                logger.logSuccess("这是一条成功日志！");
                break;
            case R.id.btn_error:
                logger.logError("这是一条出错日志！");
                break;
            case R.id.btn_warning:
                logger.logWarning("这是一条警告日志！");
                break;
            case R.id.btn_clear:
                logger.clearLog();
                break;
            default:
                break;
        }
    }
}
