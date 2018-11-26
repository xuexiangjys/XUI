package com.xuexiang.xui.widget.spinner.materialspinner;

import android.content.Context;

import java.util.List;

/**
 * Spinner适配器
 * @author xuexiang
 * @date 2017/11/30 下午4:22
 */
public class MaterialSpinnerAdapter<T> extends MaterialSpinnerBaseAdapter {

    private final List<T> mItems;

    public MaterialSpinnerAdapter(Context context, List<T> items) {
        super(context);
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems != null ? mItems.size() - 1 : 0;
    }

    @Override
    public T getItem(int position) {
        if (position >= getSelectedIndex()) {
            return mItems.get(position + 1);
        } else {
            return mItems.get(position);
        }
    }

    @Override
    public T get(int position) {
        if (mItems != null && position >= 0 && position <= mItems.size() - 1) {
            return mItems.get(position);
        } else {
            return null;
        }
    }

    @Override
    public List<T> getItems() {
        return mItems;
    }

}