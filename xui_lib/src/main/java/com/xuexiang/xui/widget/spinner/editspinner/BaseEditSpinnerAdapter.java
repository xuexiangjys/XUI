package com.xuexiang.xui.widget.spinner.editspinner;

import android.widget.BaseAdapter;

/**
 * 基础可输入下拉框
 *
 * @author xuexiang
 * @since 2018/11/26 下午2:16
 */
public abstract class BaseEditSpinnerAdapter extends BaseAdapter {
    /**
     * editText输入监听
     *
     * @return
     */
    public abstract EditSpinnerFilter getEditSpinnerFilter();

    /**
     * 获取需要填入editText的字符串
     * @param position
     * @return
     */
    public abstract String getItemString(int position);

}
