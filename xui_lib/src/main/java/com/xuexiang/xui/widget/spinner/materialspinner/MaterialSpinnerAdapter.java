package com.xuexiang.xui.widget.spinner.materialspinner;

import android.content.Context;

import java.util.List;

/**
 * Spinner适配器
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:08
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

    /**
     * 获取列表项
     *
     * @param position
     * @return
     */
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