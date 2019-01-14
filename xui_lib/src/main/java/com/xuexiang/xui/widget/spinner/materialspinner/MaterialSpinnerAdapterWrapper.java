package com.xuexiang.xui.widget.spinner.materialspinner;

import android.content.Context;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Spinner适配器代理
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:16
 */
final class MaterialSpinnerAdapterWrapper extends MaterialSpinnerBaseAdapter {

    private final ListAdapter listAdapter;

    public MaterialSpinnerAdapterWrapper(Context context, ListAdapter toWrap) {
        super(context);
        listAdapter = toWrap;
    }

    @Override
    public int getCount() {
        return listAdapter.getCount() - 1;
    }

    @Override
    public Object getItem(int position) {
        if (position >= getSelectedIndex()) {
            return listAdapter.getItem(position + 1);
        } else {
            return listAdapter.getItem(position);
        }
    }

    @Override
    public Object get(int position) {
        return listAdapter.getItem(position);
    }

    @Override
    public List<Object> getItems() {
        List<Object> items = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            items.add(getItem(i));
        }
        return items;
    }

}