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

package com.xuexiang.xuidemo.fragment.components.flowlayout;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.toast.XToast;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.FlexboxLayoutAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.XToastUtils;
import com.xuexiang.xutil.common.StringUtils;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019-11-23 01:23
 */
@Page(name = "FlexboxLayoutManager + RecyclerView\n流标签")
public class FlexboxLayoutFragment extends BaseFragment {

    @BindView(R.id.recycler_view_1)
    RecyclerView recyclerView1;
    @BindView(R.id.recycler_view_2)
    RecyclerView recyclerView2;
    @BindView(R.id.recycler_view_3)
    RecyclerView recyclerView3;
    @BindView(R.id.recycler_view_4)
    RecyclerView recyclerView4;

    private FlexboxLayoutAdapter mAdapter1;
    private FlexboxLayoutAdapter mAdapter2;
    private FlexboxLayoutAdapter mAdapter3;
    private FlexboxLayoutAdapter mAdapter4;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_flexbox_layout;
    }

    @Override
    protected void initViews() {
        String[] array = ResUtils.getStringArray(R.array.tags_values);

        recyclerView1.setLayoutManager(getFlexboxLayoutManager());
        recyclerView1.setAdapter(mAdapter1 = new FlexboxLayoutAdapter(array));

        recyclerView2.setLayoutManager(getFlexboxLayoutManager());
        recyclerView2.setAdapter(mAdapter2 = new FlexboxLayoutAdapter(array));

        recyclerView3.setLayoutManager(getFlexboxLayoutManager());
        recyclerView3.setAdapter(mAdapter3 = new FlexboxLayoutAdapter(array).setCancelable(true));

        recyclerView4.setLayoutManager(getFlexboxLayoutManager());
        recyclerView4.setItemAnimator(null);
        recyclerView4.setAdapter(mAdapter4 = new FlexboxLayoutAdapter(array).setIsMultiSelectMode(true));

        mAdapter2.select(2);
        mAdapter3.select(3);
        mAdapter4.multiSelect(1, 2, 3);
    }


    private FlexboxLayoutManager getFlexboxLayoutManager() {
        //设置布局管理器
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext());
        //flexDirection 属性决定主轴的方向（即项目的排列方向）。类似 LinearLayout 的 vertical 和 horizontal:
        // 主轴为水平方向，起点在左端。
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        //flexWrap 默认情况下 Flex 跟 LinearLayout 一样，都是不带换行排列的，但是flexWrap属性可以支持换行排列:
        // 按正常方向换行
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        //justifyContent 属性定义了项目在主轴上的对齐方式:
        // 交叉轴的起点对齐
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        return flexboxLayoutManager;
    }


    @Override
    protected void initListeners() {
        mAdapter1.setOnItemClickListener(new RecyclerViewHolder.OnItemClickListener<String>() {
            @Override
            public void onItemClick(View itemView, String item, int position) {
                XToastUtils.toast("点击了：" + item);
            }
        });

        mAdapter2.setOnItemClickListener(new RecyclerViewHolder.OnItemClickListener<String>() {
            @Override
            public void onItemClick(View itemView, String item, int position) {
                if (mAdapter2.select(position)) {
                    XToastUtils.toast("选中的内容：" + mAdapter2.getSelectContent());
                }
            }
        });

        mAdapter3.setOnItemClickListener(new RecyclerViewHolder.OnItemClickListener<String>() {
            @Override
            public void onItemClick(View itemView, String item, int position) {
                if (mAdapter3.select(position)) {
                    XToastUtils.toast("选中的内容：" + mAdapter3.getSelectContent());
                }
            }
        });

        mAdapter4.setOnItemClickListener(new RecyclerViewHolder.OnItemClickListener<String>() {
            @Override
            public void onItemClick(View itemView, String item, int position) {
                mAdapter4.select(position);
                XToastUtils.toast("选中的内容：" + StringUtils.listToString(mAdapter4.getMultiContent(), ","));
            }
        });
    }
}
