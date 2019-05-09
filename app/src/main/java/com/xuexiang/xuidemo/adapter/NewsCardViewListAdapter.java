package com.xuexiang.xuidemo.adapter;

import com.scwang.smartrefresh.layout.adapter.SmartRecyclerAdapter;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xui.widget.imageview.preview.MediaLoader;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.entity.NewInfo;

/**
 * @author XUE
 * @since 2019/5/9 10:41
 */
public class NewsCardViewListAdapter extends SmartRecyclerAdapter<NewInfo>  {

    public NewsCardViewListAdapter() {
        super(R.layout.adapter_news_card_view_list_item);
    }

    @Override
    protected void onBindViewHolder(SmartViewHolder holder, NewInfo model, int position) {
        if (model != null) {
            holder.text(R.id.tv_user_name, model.getUserName());
            holder.text(R.id.tv_tag, model.getTag());
            holder.text(R.id.tv_title, model.getTitle());
            holder.text(R.id.tv_summary, model.getSummary());
            holder.text(R.id.tv_praise, model.getPraise() == 0 ? "点赞" : String.valueOf(model.getPraise()));
            holder.text(R.id.tv_comment, model.getComment() == 0 ? "评论" : String.valueOf(model.getComment()));
            holder.text(R.id.tv_read, "阅读量 " + model.getRead());

            RadiusImageView imageView = holder.findViewById(R.id.iv_image);
            MediaLoader.getInstance().getImageLoader().displayImage(imageView.getContext(), model.getImageUrl(), imageView);
        }
    }
}
