package com.xuexiang.xui.adapter;

/**
 * 集合条目监听
 * @author XUE
 *
 * @param <T>
 */
public interface OnListItemListener<T> {

    /**
     * 点击
     * @param position
     * @param model
     * @param tag
     */
    void onItemClick(int position, T model, int tag);

    /**
     * 长按
     * @param position
     * @param model
     * @param tag
     */
    void onItemLongClick(int position, T model, int tag);
}
