package com.xuexiang.xuidemo.adapter;

import androidx.annotation.NonNull;

import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xuidemo.R;

import java.util.Collection;

/**
 * 基于simple_list_item_2简单的适配器
 *
 * @author XUE
 * @since 2019/4/1 11:04
 */
public class SimpleRecyclerAdapter extends BaseRecyclerAdapter<String> {

    public SimpleRecyclerAdapter() {
    }

    public SimpleRecyclerAdapter(Collection<String> list) {
        super(list);
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, String item) {
        holder.text(android.R.id.text1, ResUtils.getResources().getString(R.string.item_example_number_title, position));
        holder.text(android.R.id.text2, ResUtils.getResources().getString(R.string.item_example_number_abstract, position));
        holder.textColorId(android.R.id.text2, R.color.xui_config_color_light_blue_gray);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return android.R.layout.simple_list_item_2;
    }

}
