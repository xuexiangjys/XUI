package com.xuexiang.xuidemo.adapter;

import com.xuexiang.xpage.model.PageInfo;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.base.BaseRecyclerAdapter;
import com.xuexiang.xuidemo.adapter.base.RecyclerViewHolder;

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
    public void bindData(RecyclerViewHolder holder, int position, PageInfo item) {
        holder.getTextView(R.id.item_name).setText(item.getName());
        if (item.getExtra() != 0) {
            holder.getImageView(R.id.item_icon).setImageResource(item.getExtra());
        }
    }

}
