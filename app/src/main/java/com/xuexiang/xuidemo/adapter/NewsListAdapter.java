package com.xuexiang.xuidemo.adapter;

import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.xuexiang.xui.widget.imageview.ImageLoader;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.base.BroccoliRecyclerAdapter;
import com.xuexiang.xuidemo.adapter.entity.NewInfo;
import com.xuexiang.xuidemo.utils.PlaceholderHelper;

import me.samlss.broccoli.Broccoli;

/**
 * @author xuexiang
 * @since 2019/4/7 下午12:06
 */
public class NewsListAdapter extends BroccoliRecyclerAdapter<NewInfo> {
    /**
     * 是否是加载占位
     */
    private boolean mIsAnim;

    public NewsListAdapter(boolean isAnim) {
        super(DemoDataProvider.getEmptyNewInfo(), R.layout.adapter_news_list_item);
        mIsAnim = isAnim;
    }

    /**
     * 绑定控件
     *
     * @param holder
     * @param model
     * @param position
     */
    @Override
    protected void onBindData(SmartViewHolder holder, NewInfo model, int position) {
        holder.text(R.id.tv_user_name, model.getUserName());
        holder.text(R.id.tv_tag, model.getTag());
        holder.text(R.id.tv_title, model.getTitle());
        holder.text(R.id.tv_summary, model.getSummary());
        holder.text(R.id.tv_praise, model.getPraise() == 0 ? "点赞" : String.valueOf(model.getPraise()));
        holder.text(R.id.tv_comment, model.getComment() == 0 ? "评论" : String.valueOf(model.getComment()));
        holder.text(R.id.tv_read, "阅读量 " + model.getRead());

        RadiusImageView imageView = holder.findViewById(R.id.iv_image);
        ImageLoader.get().loadImage(imageView, model.getImageUrl());
    }

    /**
     * 绑定占位控件
     *
     * @param holder
     * @param broccoli
     */
    @Override
    protected void onBindBroccoli(SmartViewHolder holder, Broccoli broccoli) {
        if (mIsAnim) {
            broccoli.addPlaceholder(PlaceholderHelper.getParameter(holder.findView(R.id.iv_avatar)))
                    .addPlaceholder(PlaceholderHelper.getParameter(holder.findView(R.id.tv_user_name)))
                    .addPlaceholder(PlaceholderHelper.getParameter(holder.findView(R.id.tv_tag)))
                    .addPlaceholder(PlaceholderHelper.getParameter(holder.findView(R.id.tv_title)))
                    .addPlaceholder(PlaceholderHelper.getParameter(holder.findView(R.id.tv_summary)))
                    .addPlaceholder(PlaceholderHelper.getParameter(holder.findView(R.id.iv_image)))
                    .addPlaceholder(PlaceholderHelper.getParameter(holder.findView(R.id.iv_praise)))
                    .addPlaceholder(PlaceholderHelper.getParameter(holder.findView(R.id.iv_comment)))
                    .addPlaceholder(PlaceholderHelper.getParameter(holder.findView(R.id.tv_praise)))
                    .addPlaceholder(PlaceholderHelper.getParameter(holder.findView(R.id.tv_comment)))
                    .addPlaceholder(PlaceholderHelper.getParameter(holder.findView(R.id.tv_read)));
        } else {
            broccoli.addPlaceholders(
                    holder.findView(R.id.iv_avatar),
                    holder.findView(R.id.tv_user_name),
                    holder.findView(R.id.tv_tag),
                    holder.findView(R.id.tv_title),
                    holder.findView(R.id.tv_summary),
                    holder.findView(R.id.iv_image),
                    holder.findView(R.id.iv_praise),
                    holder.findView(R.id.tv_praise),
                    holder.findView(R.id.iv_comment),
                    holder.findView(R.id.tv_comment),
                    holder.findView(R.id.tv_read)
            );
        }
    }

}
