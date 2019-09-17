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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.xui.XUI;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.utils.SettingSPUtils;

/**
 * 基础AppCompatActivity
 *
 * @author xuexiang
 * @since 2019-09-18 01:12
 */
public class BaseAppCompatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化主题
     */
    private void initTheme() {
        if (SettingSPUtils.getInstance().isUseCustomTheme()) {
            setTheme(R.style.CustomAppTheme);
        } else {
            XUI.initTheme(this);
        }
    }
}
