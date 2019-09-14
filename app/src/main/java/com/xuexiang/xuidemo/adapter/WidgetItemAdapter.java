package com.xuexiang.xuidemo.adapter;

import android.support.annotation.NonNull;

import com.xuexiang.xpage.model.PageInfo;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xuidemo.R;

import java.util.List;

/**
 * @author XUE
 * @date 2017/9/10 15:28
 */
public class WidgetItemAdapter extends BaseRecyclerAdapter<PageInfo> {

    public WidgetItemAdapter(List<PageInfo> list) {
        super(list);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.layout_widget_item;
    }

    @Override
    public void bindData(@NonNull RecyclerViewHolder holder, int position, PageInfo item) {
        holder.text(R.id.item_name, item.getName());
        if (item.getExtra() != 0) {
            holder.image(R.id.item_icon, item.getExtra());
        }
    }

}
