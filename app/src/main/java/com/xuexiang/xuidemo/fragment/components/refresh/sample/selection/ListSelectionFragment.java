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

package com.xuexiang.xuidemo.fragment.components.refresh.sample.selection;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.XToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2020/9/2 8:55 PM
 */
@Page(name = "列表选择案例")
public class ListSelectionFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_selection_total1)
    TextView tvSelectionTotal1;
    @BindView(R.id.tv_selection_total2)
    TextView tvSelectionTotal2;

    private SelectionListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_selection;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("保存") {
            @Override
            public void performAction(View view) {
                List<SelectionItem> result = mAdapter.getSelectionResult();
                StringBuilder sb = new StringBuilder("选择结果:");
                for (SelectionItem item : result) {
                    sb.append("\n")
                            .append(item.subjectName)
                            .append(":")
                            .append(getSelectionName(item.selection));
                }
                XToastUtils.toast(sb.toString());
            }
        });
        return titleBar;
    }


    public String getSelectionName(int selection) {
        if (selection == 0) {
            return "符合";
        } else if (selection == 1) {
            return "不符合";
        } else {
            return "未选择";
        }
    }

    @Override
    protected void initViews() {
        WidgetUtils.initRecyclerView(recyclerView);
        recyclerView.setAdapter(mAdapter = new SelectionListAdapter());
        mAdapter.refresh(getSelectionItems());
        updateSelectResult();
    }

    @Override
    protected void initListeners() {
        mAdapter.setOnSelectionChangedListener(item -> {
            updateSelectResult();
        });
    }

    private void updateSelectResult() {
        int[] count = mAdapter.getSelectionCount();
        tvSelectionTotal1.setText(String.valueOf(count[0]));
        tvSelectionTotal2.setText(String.valueOf(count[1]));
    }

    private List<SelectionItem> getSelectionItems() {
        List<SelectionItem> items = new ArrayList<>();
        items.add(new SelectionItem("项目名称", "符合", "不符合"));
        // TODO: 2020/9/2 模拟接口返回的数据 
        for (int i = 1; i <= 5; i++) {
            items.add(new SelectionItem("项目" + i));
        }
        return items;
    }
}
