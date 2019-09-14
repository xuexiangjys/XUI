package com.xuexiang.xuidemo.adapter;

import android.support.annotation.NonNull;

import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.entity.NewInfo;

/**
 * @author XUE
 * @since 2019/5/9 10:41
 */
public class NewsCardViewListAdapter extends BaseRecyclerAdapter<NewInfo> {

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.adapter_news_card_view_list_item;
    }

    @Override
    public void bindData(@NonNull RecyclerViewHolder holder, int position, NewInfo model) {
        if (model != null) {
            holder.text(R.id.tv_user_name, model.getUserName());
            holder.text(R.id.tv_tag, model.getTag());
            holder.text(R.id.tv_title, model.getTitle());
            holder.text(R.id.tv_summary, model.getSummary());
            holder.text(R.id.tv_praise, model.getPraise() == 0 ? "点赞" : String.valueOf(model.getPraise()));
            holder.text(R.id.tv_comment, model.getComment() == 0 ? "评论" : String.valueOf(model.getComment()));
            holder.text(R.id.tv_read, "阅读量 " + model.getRead());
            holder.image(R.id.iv_image, model.getImageUrl());
        }
    }

}
