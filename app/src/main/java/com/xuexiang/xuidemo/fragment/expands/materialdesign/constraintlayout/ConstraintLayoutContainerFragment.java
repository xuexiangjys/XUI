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

package com.xuexiang.xuidemo.fragment.expands.materialdesign.constraintlayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xrouter.annotation.AutoWired;
import com.xuexiang.xrouter.launcher.XRouter;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.base.BaseFragment;

import static com.xuexiang.xuidemo.fragment.expands.materialdesign.constraintlayout.ConstraintLayoutContainerFragment.KEY_LAYOUT_ID;
import static com.xuexiang.xuidemo.fragment.expands.materialdesign.constraintlayout.ConstraintLayoutContainerFragment.KEY_TITLE;

/**
 * @author xuexiang
 * @since 2020-01-08 10:19
 */
@Page(params = {KEY_TITLE, KEY_LAYOUT_ID})
public class ConstraintLayoutContainerFragment extends BaseFragment {
    public static final String KEY_TITLE = "key_title";
    public static final String KEY_LAYOUT_ID = "key_layout_id";
    @AutoWired(name = KEY_TITLE)
    String title;
    @AutoWired(name = KEY_LAYOUT_ID)
    int layoutId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        XRouter.getInstance().inject(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected TitleBar initTitle() {
        return super.initTitle().setTitle(title);
    }

    @Override
    protected int getLayoutId() {
        return layoutId;
    }

    @Override
    protected void initViews() {

    }
}
