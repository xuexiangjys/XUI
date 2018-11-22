package com.xuexiang.xui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 简单的集合列表适配器
 * @author XUE
 *
 * @param <T>
 * @param <H>
 */
public abstract class BaseListAdapter<T, H> extends XListAdapter<T> {

    public BaseListAdapter(Context context) {
        super(context);
    }

    public BaseListAdapter(Context context, OnListItemListener<T> listener) {
        super(context, listener);
    }

    public BaseListAdapter(Context context, List<T> data) {
        super(context, data);
    }

    public BaseListAdapter(Context context, T[] data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        H holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), getLayoutId(), null);
            holder = newViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (H) convertView.getTag();
        }
        
        T item = getItem(position);
        if (item != null) {
        	convert(holder, item, position);
        }
        return convertView;
    }

    /**
     * 创建ViewHolder
     * @param convertView
     * @return
     */
    protected abstract H newViewHolder(View convertView);

    /**
     * 获取适配的布局ID
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 转换布局
     * @param holder
     * @param item
     * @param position
     */
    protected abstract void convert(H holder, T item, int position);
}
