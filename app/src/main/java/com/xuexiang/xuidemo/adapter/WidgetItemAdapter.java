package com.xuexiang.xuidemo.adapter;

import android.content.Context;

import com.xuexiang.xpage.model.PageInfo;
import com.xuexiang.xuidemo.R;

import java.util.List;

/**
 * @author XUE
 * @date 2017/9/10 15:28
 */
public class WidgetItemAdapter extends BaseRecyclerAdapter<PageInfo> {

    public WidgetItemAdapter(Context ctx, List<PageInfo> list) {
        super(ctx, list);
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
