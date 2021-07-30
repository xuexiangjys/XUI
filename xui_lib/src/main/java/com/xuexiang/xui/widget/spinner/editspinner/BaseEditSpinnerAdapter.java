package com.xuexiang.xui.widget.spinner.editspinner;

import android.widget.BaseAdapter;

import com.xuexiang.xui.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 基础可输入下拉框
 *
 * @author xuexiang
 * @since 2018/11/26 下午2:16
 */
public abstract class BaseEditSpinnerAdapter<T> extends BaseAdapter {

    /**
     * 可选项集合数据源
     */
    protected final List<T> mDataSource;
    /**
     * 输入后匹配相关联的选项（展示）
     */
    protected List<String> mDisplayData;

    protected final int[] mIndexs;

    /**
     * 构造方法
     *
     * @param data 选项数据
     */
    public BaseEditSpinnerAdapter(List<T> data) {
        mDataSource = data;
        mDisplayData = new ArrayList<>();
        initDisplayData(data);
        mIndexs = new int[mDataSource.size()];
    }

    /**
     * 构造方法
     *
     * @param data 选项数据
     */
    public BaseEditSpinnerAdapter(T[] data) {
        mDataSource = new ArrayList<>();
        mDataSource.addAll(Arrays.asList(data));
        mDisplayData = new ArrayList<>();
        initDisplayData(mDataSource);
        mIndexs = new int[mDataSource.size()];
    }

    protected void initDisplayData(List<T> data) {
        if (!CollectionUtils.isEmpty(data)) {
            for (int i = 0; i < data.size(); i++) {
                mDisplayData.add(data.get(i).toString());
            }
        }
    }

    @Override
    public int getCount() {
        return CollectionUtils.getSize(mDisplayData);
    }

    @Override
    public String getItem(int position) {
        return CollectionUtils.getListItem(mDisplayData, position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 获取editText输入监听
     *
     * @return editText输入监听
     */
    public abstract EditSpinnerFilter getEditSpinnerFilter();

    /**
     * 获取需要填入editText的字符串
     *
     * @param position 位置
     * @return 需要填入editText的字符串
     */
    protected String getItemString(int position) {
        return mDataSource.get(mIndexs[position]).toString();
    }

    protected String getDataSourceString(int i) {
        T data = CollectionUtils.getListItem(mDataSource, i);
        return data != null ? data.toString() : "";
    }


}
