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

package com.xuexiang.xuidemo.fragment.expands.iconfont;

import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.IconFontGridAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.widget.iconfont.XUIIconFont;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019-10-13 18:55
 */
@Page(name = "XUIIconFont展示")
public class XUIIconFontDisplayFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_xui_iconfont_display;
    }

    @Override
    protected void initViews() {
        WidgetUtils.initGridRecyclerView(recyclerView, 3, DensityUtils.dp2px(2));

        recyclerView.setAdapter(new IconFontGridAdapter(XUIIconFont.Icon.values()));
    }

}
