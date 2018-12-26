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

package com.xuexiang.xui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Fragment的适配器
 *
 * @author xuexiang
 * @since 2018/12/26 下午2:11
 */
public class FragmentAdapter<T extends Fragment> extends FragmentPagerAdapter {

    private List<T> mFragmentList = new ArrayList<>();

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public FragmentAdapter(FragmentManager fm, List<T> fragments) {
        super(fm);
        setFragments(fragments);
    }

    public FragmentAdapter(FragmentManager fm, T[] fragments) {
        super(fm);
        setFragments(Arrays.asList(fragments));
    }

    public void addFragment(T fragment) {
        if (fragment != null) {
            mFragmentList.add(fragment);
        }
    }

    public void setFragments(List<T> fragments) {
        if (fragments != null && fragments.size() > 0) {
            mFragmentList.clear();
            mFragmentList.addAll(fragments);
        }
    }

    public void addFragments(List<T> fragments) {
        if (fragments != null && fragments.size() > 0) {
            mFragmentList.addAll(fragments);
        }
    }

    @Override
    public T getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public List<T> getFragmentLists() {
        return mFragmentList;
    }
}
