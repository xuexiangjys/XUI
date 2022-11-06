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

package com.xuexiang.xuidemo.fragment.components.tabbar;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuexiang.xuidemo.R;

/**
 * @author xuexiang
 * @since 2018/12/26 下午2:17
 */
public class TestPageFragment extends Fragment {

    String content;

    public static TestPageFragment newInstance(String content) {
        return new TestPageFragment().setContent(content);
    }

    public TestPageFragment setContent(String content) {
        this.content = content;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getContext());
        textView.setTextAppearance(getContext(), R.style.TextStyle_Content_Match);
        textView.setGravity(Gravity.CENTER);
        textView.setText(String.format("这个是%s页面的内容", content));
        return textView;
    }
}
