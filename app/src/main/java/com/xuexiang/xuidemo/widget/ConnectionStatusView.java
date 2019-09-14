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

package com.xuexiang.xuidemo.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import com.xuexiang.xui.widget.popupwindow.status.StatusView;
import com.xuexiang.xuidemo.R;

/**
 * 连接状态
 *
 * @author xuexiang
 * @since 2018/12/27 下午5:58
 */
public class ConnectionStatusView extends StatusView {

    public ConnectionStatusView(Context context) {
        super(context, R.layout.sv_layout_complete, R.layout.sv_layout_error, R.layout.sv_layout_loading, R.layout.sv_layout_custom);
    }

    public ConnectionStatusView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.sv_layout_complete, R.layout.sv_layout_error, R.layout.sv_layout_loading,  R.layout.sv_layout_custom);
    }

    public ConnectionStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, R.layout.sv_layout_complete, R.layout.sv_layout_error, R.layout.sv_layout_loading,  R.layout.sv_layout_custom);
    }

}
