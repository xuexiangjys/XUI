/*
 * Copyright (C) 2023 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.activity;

import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseActivity;

/**
 * 使用Theme.MaterialComponents作为主题的Activity
 *
 * @author xuexiang
 * @since 5/14/23 10:30 PM
 */
public class MaterialDesignThemeActivity extends BaseActivity {

    @Override
    protected void initAppTheme() {
        setTheme(R.style.MaterialDesignTheme);
    }
}
