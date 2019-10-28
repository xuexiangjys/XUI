/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.components.textview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.core.content.ContextCompat;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.widget.grouplist.XUICommonListItemView;
import com.xuexiang.xui.widget.grouplist.XUIGroupListView;
import com.xuexiang.xui.widget.progress.loading.MiniLoadingView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.XToastUtils;

import butterknife.BindView;

/**
 * {@link XUIGroupListView} 的使用示例
 *
 * @author xuexiang
 * @since 2019/1/3 上午11:38
 */
@Page(name = "XUIGroupListView\n通用的GroupListView，注意它不是ListView")
public class GroupListViewFragment extends BaseFragment {

    @BindView(R.id.groupListView)
    XUIGroupListView mGroupListView;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_grouplistview;
    }

    @Override
    protected void initViews() {
        initGroupListView();
    }

    private void initGroupListView() {
        XUICommonListItemView normalItem = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.ic_launcher),
                "Item 1",
                null,
                XUICommonListItemView.HORIZONTAL,
                XUICommonListItemView.ACCESSORY_TYPE_NONE);
        normalItem.setOrientation(XUICommonListItemView.VERTICAL);

        XUICommonListItemView itemWithDetail = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.drawable.icon_avatar1),
                "Item 2",
                null,
                XUICommonListItemView.HORIZONTAL,
                XUICommonListItemView.ACCESSORY_TYPE_NONE);
        itemWithDetail.setDetailText("在右方的详细信息");

        XUICommonListItemView itemWithDetailBelow = mGroupListView.createItemView("Item 3");
        itemWithDetailBelow.setOrientation(XUICommonListItemView.VERTICAL);
        itemWithDetailBelow.setDetailText("在标题下方的详细信息");

        XUICommonListItemView itemWithChevron = mGroupListView.createItemView("Item 4");
        itemWithChevron.setAccessoryType(XUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        XUICommonListItemView itemWithSwitch = mGroupListView.createItemView("Item 5");
        itemWithSwitch.setAccessoryType(XUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        itemWithSwitch.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                XToastUtils.toast("checked = " + isChecked);
            }
        });

        XUICommonListItemView itemWithCustom = mGroupListView.createItemView("Item 6");
        itemWithCustom.setAccessoryType(XUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        MiniLoadingView loadingView = new MiniLoadingView(getActivity());
        itemWithCustom.addAccessoryCustomView(loadingView);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof XUICommonListItemView) {
                    CharSequence text = ((XUICommonListItemView) v).getText();
                    XToastUtils.toast(text + " is Clicked");
                }
            }
        };

        int size = DensityUtils.dp2px(getContext(), 20);
        XUIGroupListView.newSection(getContext())
                .setTitle("Section 1: 默认提供的样式")
                .setDescription("Section 1 的描述")
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(normalItem, onClickListener)
                .addItemView(itemWithDetail, onClickListener)
                .addItemView(itemWithDetailBelow, onClickListener)
                .addItemView(itemWithChevron, onClickListener)
                .addItemView(itemWithSwitch, onClickListener)
                .addTo(mGroupListView);

        XUIGroupListView.newSection(getContext())
                .setTitle("Section 2: 自定义右侧 View")
                .addItemView(itemWithCustom, onClickListener)
                .addTo(mGroupListView);
    }

}
