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

package com.xuexiang.xui.widget.edittext;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

/**
 * ‘****’号密码输入样式
 *
 * @author xuexiang
 * @since 2019-07-05 9:34
 */
public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {

    private static AsteriskPasswordTransformationMethod sInstance;

    public static PasswordTransformationMethod getInstance() {
        if (sInstance != null) {
            return sInstance;
        }
        sInstance = new AsteriskPasswordTransformationMethod();
        return sInstance;
    }

    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new AsteriskPasswordCharSequence(source);
    }

    private static class AsteriskPasswordCharSequence implements CharSequence {
        private CharSequence mSource;

        AsteriskPasswordCharSequence(CharSequence source) {
            mSource = source; // Store char sequence
        }

        @Override
        public char charAt(int index) {
            return '*'; // This is the important part
        }

        @Override
        public int length() {
            return mSource.length(); // Return default
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end); // Return default
        }
    }

}
