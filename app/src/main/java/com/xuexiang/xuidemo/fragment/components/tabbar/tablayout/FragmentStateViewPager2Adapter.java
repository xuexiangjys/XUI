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

package com.xuexiang.xuidemo.fragment.components.tabbar.tablayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xuexiang
 * @since 2020/5/21 1:27 AM
 */
public class FragmentStateViewPager2Adapter extends FragmentStateAdapter {

    private List<Fragment> mFragmentList = new ArrayList<>();

    private List<String> mTitleList = new ArrayList<>();

    private List<Long> mIds = new ArrayList<>();

    private AtomicLong mAtomicLong = new AtomicLong(0);

    public FragmentStateViewPager2Adapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragmentList.get(position);
    }

    public FragmentStateViewPager2Adapter addFragment(Fragment fragment, String title) {
        if (fragment != null) {
            mFragmentList.add(fragment);
            mTitleList.add(title);
            mIds.add(getAtomicGeneratedId());
        }
        return this;
    }

    public FragmentStateViewPager2Adapter addFragment(int index, Fragment fragment, String title) {
        if (fragment != null && index >= 0 && index <= mFragmentList.size()) {
            mFragmentList.add(index, fragment);
            mTitleList.add(index, title);
            mIds.add(index, getAtomicGeneratedId());
        }
        return this;
    }

    public FragmentStateViewPager2Adapter replaceFragment(int index, Fragment newFragment, String newTitle) {
        if (newFragment != null) {
            mFragmentList.set(index, newFragment);
            mTitleList.set(index, newTitle);
            mIds.set(index, getAtomicGeneratedId());
        }
        return this;
    }

    public int replaceFragment(Fragment oldFragment, Fragment newFragment, String newTitle) {
        if (oldFragment != null && newFragment != null) {
            int index = mFragmentList.indexOf(oldFragment);
            if (index != -1) {
                mFragmentList.set(index, newFragment);
                mTitleList.set(index, newTitle);
                mIds.set(index, getAtomicGeneratedId());
            }
            return index;
        }
        return -1;
    }

    public FragmentStateViewPager2Adapter removeFragment(int index) {
        if (index >= 0 && index < mFragmentList.size()) {
            mFragmentList.remove(index);
            mTitleList.remove(index);
            mIds.remove(index);
        }
        return this;
    }

    private long getAtomicGeneratedId() {
        return mAtomicLong.incrementAndGet();
    }

    @Override
    public int getItemCount() {
        return mFragmentList.size();
    }

    public void clear() {
        mFragmentList.clear();
        mTitleList.clear();
        mIds.clear();
        notifyDataSetChanged();
    }

    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mIds.get(position);
    }

    @Override
    public boolean containsItem(long itemId) {
        return mIds.contains(itemId);
    }
}
