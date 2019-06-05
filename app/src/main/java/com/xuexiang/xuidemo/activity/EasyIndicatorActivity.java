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

package com.xuexiang.xuidemo.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.xuexiang.xui.adapter.FragmentAdapter;
import com.xuexiang.xui.widget.tabbar.EasyIndicator;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.fragment.components.tabbar.ContentPage;
import com.xuexiang.xuidemo.fragment.components.tabbar.TestPageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuexiang
 * @since 2018/12/26 下午3:38
 */
public class EasyIndicatorActivity extends AppCompatActivity {
    EasyIndicator mEasyIndicator;
    ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_easy_indicator);

        mEasyIndicator = findViewById(R.id.easy_indicator);
        mViewPager = findViewById(R.id.view_pager);

        List<TestPageFragment> list = new ArrayList<>();
        String[] pages = ContentPage.getPageNames();
        for (String page : pages) {
            list.add(TestPageFragment.newInstance(page));
        }
        mEasyIndicator.setTabTitles(ContentPage.getPageNames());
        mEasyIndicator.setViewPager(mViewPager, new FragmentAdapter<>(getSupportFragmentManager(), list));
        mViewPager.setOffscreenPageLimit(ContentPage.size() - 1);

    }
}
