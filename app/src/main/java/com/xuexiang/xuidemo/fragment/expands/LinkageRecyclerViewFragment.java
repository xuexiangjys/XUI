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

package com.xuexiang.xuidemo.fragment.expands;

import android.view.View;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.expands.linkage.LinkageRecyclerViewCustomFragment;
import com.xuexiang.xuidemo.fragment.expands.linkage.LinkageRecyclerViewElemeFragment;
import com.xuexiang.xuidemo.fragment.expands.linkage.LinkageRecyclerViewSimpleFragment;
import com.xuexiang.xuidemo.utils.Utils;

/**
 * @author xuexiang
 * @since 2019-11-25 11:24
 */
@Page(name = "双列表联动", extra = R.drawable.ic_expand_linkage_list)
public class LinkageRecyclerViewFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                LinkageRecyclerViewSimpleFragment.class,
                LinkageRecyclerViewCustomFragment.class,
                LinkageRecyclerViewElemeFragment.class
        };
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("Github") {
            @Override
            public void performAction(View view) {
                Utils.goWeb(getContext(), "https://github.com/KunMinX/Linkage-RecyclerView");
            }
        });
        return titleBar;
    }
}
