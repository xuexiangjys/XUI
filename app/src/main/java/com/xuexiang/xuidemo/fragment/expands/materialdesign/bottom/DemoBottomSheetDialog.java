/*
 * Copyright (C) 2021 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.expands.materialdesign.bottom;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.xuexiang.xpage.config.AppPageConfig;
import com.xuexiang.xpage.model.PageInfo;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.WidgetItemAdapter;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 底部弹窗
 *
 * @author xuexiang
 * @since 2021/3/28 1:30 PM
 */
public class DemoBottomSheetDialog extends BottomSheetDialogFragment {

    private static final String TAG = "BottomSheetDialog";

    public static DemoBottomSheetDialog newInstance() {
        Bundle args = new Bundle();
        DemoBottomSheetDialog fragment = new DemoBottomSheetDialog();
        fragment.setArguments(args);
        return fragment;
    }

    private Unbinder mUnbinder;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_bottom_sheet, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // 这里可以自定义弹窗的样式
        Dialog dialog = new BottomSheetDialog(getContext(), R.style.XUITheme_BottomSheetDialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        initView(view);
    }

    private void initView(@NonNull View view) {
        WidgetUtils.initGridRecyclerView(recyclerView, 3, DensityUtils.dp2px(2));

        WidgetItemAdapter widgetItemAdapter = new WidgetItemAdapter(sortPageInfo(AppPageConfig.getInstance().getComponents()));
        recyclerView.setAdapter(widgetItemAdapter);
    }

    /**
     * 进行排序
     *
     * @param pageInfoList
     * @return
     */
    private List<PageInfo> sortPageInfo(List<PageInfo> pageInfoList) {
        Collections.sort(pageInfoList, (o1, o2) -> o1.getClassPath().compareTo(o2.getClassPath()));
        return pageInfoList;
    }

    public void show(@NonNull FragmentManager manager) {
        super.show(manager, TAG);
    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

}
