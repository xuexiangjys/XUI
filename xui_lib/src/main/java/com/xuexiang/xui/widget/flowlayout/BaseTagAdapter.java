package com.xuexiang.xui.widget.flowlayout;

import android.content.Context;

import com.xuexiang.xui.adapter.BaseListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 流布局基础适配器
 *
 * @author xuexiang
 * @date 2017/11/20 下午3:00
 */
public abstract class BaseTagAdapter<T, H> extends BaseListAdapter<T, H> implements FlowTagLayout.OnInitSelectedPosition {
    /**
     * 初始化选中的位置
     */
    private List<Integer> mPositions = new ArrayList<>();

    public BaseTagAdapter(Context context) {
        super(context);
    }

    /**
     * 增加标签数据
     *
     * @param data
     */
    public void addTag(T data) {
        addData(data);
    }

    /**
     * 增加标签数据
     *
     * @param datas
     */
    public void addTags(List<T> datas) {
        addData(datas);
    }

    /**
     * 增加标签数据
     *
     * @param datas
     */
    public void addTags(T[] datas) {
        addData(datas);
    }

    /**
     * 清除并增加标签数据
     *
     * @param datas
     */
    public void clearAndAddTags(List<T> datas) {
        clearNotNotify();
        addTags(datas);
    }

    /**
     * 清除并增加标签数据
     *
     * @param datas
     */
    public void clearAndAddTags(T[] datas) {
        clearNotNotify();
        addTags(datas);
    }

    /**
     * 设置初始化选中的标签索引
     *
     * @param p
     * @return
     */
    public BaseTagAdapter setSelectedPosition(Integer p) {
        mPositions.clear();
        mPositions.add(p);
        notifyDataSetChanged();
        return this;
    }

    /**
     * 设置初始化选中的标签索引
     *
     * @param ps
     * @return
     */
    public BaseTagAdapter setSelectedPositions(Integer... ps) {
        if (ps != null && ps.length > 0) {
            mPositions.clear();
            mPositions.addAll(Arrays.asList(ps));
            notifyDataSetChanged();
        }
        return this;
    }

    /**
     * 设置初始化选中的标签索引
     *
     * @param ps
     * @return
     */
    public BaseTagAdapter setSelectedPositions(List<Integer> ps) {
        if (ps != null && ps.size() > 0) {
            mPositions.clear();
            mPositions.addAll(ps);
            notifyDataSetChanged();
        }
        return this;
    }

    /**
     * 设置初始化选中的标签索引
     *
     * @param ps
     * @return
     */
    public BaseTagAdapter setSelectedPositions(int[] ps) {
        mPositions.clear();
        for (int i = 0; i < ps.length; i++) {
            mPositions.add(ps[i]);
        }
        notifyDataSetChanged();
        return this;
    }

    /**
     * 初始化选择
     *
     * @param poi
     * @return
     */
    @Override
    public boolean isSelectedPosition(int poi) {
        for (int i = 0; i < mPositions.size(); i++) {
            if (mPositions.get(i) == poi) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取初始选中的索引的集合
     *
     * @return
     */
    public List<Integer> getInitSelectedPositions() {
        return mPositions;
    }
}
