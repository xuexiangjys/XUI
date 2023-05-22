/*
 * Copyright (C) 2023 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.expands.materialdesign;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.recyclerview.XGridLayoutManager;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.itemtouch.DragAdapter;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.itemtouch.DragItemCallBack;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.itemtouch.GridSpaceItemDecoration;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 使用ItemTouchHelper，可以自定义拖拽，滑动等功能，还是非常有用的。
 *
 * 具体参见 DragItemCallBack 的实现
 *
 *
 * @author xuexiang
 * @since 5/16/23 12:36 AM
 */
@Page(name = "ItemTouchHelper+RecyclerView\n实现列表拖拽")
public class ItemTouchHelperFragment extends BaseFragment {

    private static final String TAG = "DragAdapter";

    public static final int SPAN_COUNT = 4;

    @BindView(R.id.recycleView)
    RecyclerView recycleView;

    private DragAdapter mDragAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_item_touch_helper;
    }

    @Override
    protected void initViews() {
        recycleView.setLayoutManager(new XGridLayoutManager(getContext(), SPAN_COUNT));
        recycleView.addItemDecoration(new GridSpaceItemDecoration(SPAN_COUNT));
        recycleView.setAdapter(mDragAdapter = new DragAdapter(ResUtils.getStringArray(getContext(), R.array.menu_entry)));

        // 设置拖拽/滑动
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new DragItemCallBack(mDragAdapter));
        itemTouchHelper.attachToRecyclerView(recycleView);

        mDragAdapter.setItemLongClickListener((viewHolder, item, position) -> {
            if (position != mDragAdapter.getFixedPosition()) {
                itemTouchHelper.startDrag(viewHolder);
            }
        });

        mDragAdapter.setOnItemClickListener((itemView, item, position) -> XToastUtils.toast(item));
    }

    @Override
    protected void initListeners() {
        mDragAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                Log.e(TAG, "onItemRangeMoved:" + fromPosition + " -> " + toPosition);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                Log.e(TAG, "onItemRangeRemoved:" + positionStart);
            }

            @Override
            public void onChanged() {
                super.onChanged();
                Log.e(TAG, "onChanged");
            }
        });
    }

    @OnClick(R.id.tv_switch)
    public void onViewClicked(View view) {
        RecyclerView.LayoutManager layoutManager = recycleView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            recycleView.setLayoutManager(new XLinearLayoutManager(getContext()));
        } else {
            recycleView.setLayoutManager(new XGridLayoutManager(getContext(), SPAN_COUNT));
        }
    }
}
