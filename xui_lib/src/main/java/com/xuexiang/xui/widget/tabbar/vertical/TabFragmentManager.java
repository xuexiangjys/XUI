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

package com.xuexiang.xui.widget.tabbar.vertical;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xuexiang.xui.widget.tabbar.VerticalTabLayout;

import java.util.List;

/**
 * 选项卡的Fragment管理
 *
 * @author xuexiang
 * @since 2018/12/28 下午1:36
 */
public class TabFragmentManager {
    private FragmentManager mManager;
    private int mContainerResId;
    private List<Fragment> mFragments;
    private VerticalTabLayout mTabLayout;
    private VerticalTabLayout.OnTabSelectedListener mListener;

    public TabFragmentManager(FragmentManager manager, List<Fragment> fragments, VerticalTabLayout tabLayout) {
        mManager = manager;
        mFragments = fragments;
        mTabLayout = tabLayout;
        mListener = new OnFragmentTabSelectedListener();
        mTabLayout.addOnTabSelectedListener(mListener);
    }

    public TabFragmentManager(FragmentManager manager, int containerResid, List<Fragment> fragments, VerticalTabLayout tabLayout) {
        this(manager, fragments, tabLayout);
        mContainerResId = containerResid;
        changeFragment();
    }

    public void changeFragment() {
        FragmentTransaction ft = mManager.beginTransaction();
        int position = mTabLayout.getSelectedTabPosition();
        List<Fragment> addedFragments = mManager.getFragments();
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment fragment = mFragments.get(i);
            if ((addedFragments == null || !addedFragments.contains(fragment)) && mContainerResId != 0) {
                ft.add(mContainerResId, fragment);
            }
            if ((mFragments.size() > position && i == position)
                    || (mFragments.size() <= position && i == mFragments.size() - 1)) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
        }
        ft.commit();
        mManager.executePendingTransactions();
    }

    public void detach() {
        FragmentTransaction ft = mManager.beginTransaction();
        for (Fragment fragment : mFragments) {
            ft.remove(fragment);
        }
        ft.commit();
        mManager.executePendingTransactions();
        mManager = null;
        mFragments = null;
        mTabLayout.removeOnTabSelectedListener(mListener);
        mListener = null;
        mTabLayout = null;
    }


    private class OnFragmentTabSelectedListener implements VerticalTabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabView tab, int position) {
            changeFragment();
        }

        @Override
        public void onTabUnselected(TabView tab, int position) {

        }
        @Override
        public void onTabReselected(TabView tab, int position) {

        }
    }
}
