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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Fragment的适配器,详情参考{@link FragmentStatePagerAdapter}
 *
 * @author xuexiang
 * @since 2018/12/26 下午2:11
 */
public class FragmentStateAdapter<T extends Fragment> extends FragmentStatePagerAdapter {

    private List<T> mFragmentList = new ArrayList<>();

    private List<String> mTitleList = new ArrayList<>();

    public FragmentStateAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public FragmentStateAdapter(@NonNull FragmentManager fm, T[] fragments) {
        this(fm, Arrays.asList(fragments));
    }

    public FragmentStateAdapter(@NonNull FragmentManager fm, List<T> fragments) {
        super(fm);
        setFragments(fragments);
    }

    public FragmentStateAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public FragmentStateAdapter(@NonNull FragmentManager fm, int behavior, T[] fragments) {
        this(fm, behavior, Arrays.asList(fragments));
    }

    public FragmentStateAdapter(@NonNull FragmentManager fm, int behavior, List<T> fragments) {
        super(fm, behavior);
        setFragments(fragments);
    }

    public FragmentStateAdapter setFragments(List<T> fragments) {
        if (fragments != null && fragments.size() > 0) {
            mFragmentList.clear();
            mFragmentList.addAll(fragments);
        }
        return this;
    }

    public FragmentStateAdapter addFragments(List<T> fragments) {
        if (fragments != null && fragments.size() > 0) {
            mFragmentList.addAll(fragments);
        }
        return this;
    }

    public FragmentStateAdapter setTitles(List<String> titles) {
        if (titles != null && titles.size() > 0) {
            mTitleList.clear();
            mTitleList.addAll(titles);
        }
        return this;
    }

    public FragmentStateAdapter addTitles(List<String> titles) {
        if (titles != null && titles.size() > 0) {
            mTitleList.addAll(titles);
        }
        return this;
    }

    public FragmentStateAdapter addFragment(T fragment, String title) {
        if (fragment != null) {
            mFragmentList.add(fragment);
            mTitleList.add(title);
        }
        return this;
    }

    @NonNull
    @Override
    public T getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }

    public List<T> getFragmentList() {
        return mFragmentList;
    }

    public List<String> getTitleList() {
        return mTitleList;
    }
}
