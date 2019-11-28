package com.xuexiang.xuidemo.adapter;

import android.view.View;

import androidx.annotation.NonNull;

import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;
import com.xuexiang.xui.widget.banner.widget.banner.base.BaseBanner;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.utils.XToastUtils;

import java.util.List;

/**
 * 演示如何增加HeadView和FootView
 *
 * @author xuexiang
 * @since 2019/3/31 下午11:45
 */
public class RefreshHeadViewAdapter extends BaseRecyclerAdapter<String> {

    private static final int TYPE_BANNER_HEAD = 0;
    private static final int TYPE_COMMON = 1;
    private static final int TYPE_BANNER_FOOT = 2;

    private List<BannerItem> mData;
    private SimpleImageBanner headBanner;
    private SimpleImageBanner footBanner;

    public RefreshHeadViewAdapter(List<BannerItem> bannerData) {
        super();
        mData = bannerData;
    }

    /**
     * 适配的布局
     *
     * @param viewType
     * @return
     */
    @Override
    protected int getItemLayoutId(int viewType) {
        if (viewType == TYPE_BANNER_HEAD || viewType == TYPE_BANNER_FOOT) {
            return R.layout.include_head_view_banner;
        } else {
            return android.R.layout.simple_list_item_2;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BANNER_HEAD;
        } else if (position == getItemCount() - 1) {
            return TYPE_BANNER_FOOT;
        } else {
            return TYPE_COMMON;
        }
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position 索引
     * @param item     列表项
     */
    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, String item) {
        if (getItemViewType(position) == TYPE_BANNER_HEAD) {
            headBanner = holder.findViewById(R.id.sib_simple_usage);
            headBanner.setSource(mData)
                    .setOnItemClickListener(new BaseBanner.OnItemClickListener<BannerItem>() {
                        @Override
                        public void onItemClick(View view, BannerItem item, int position) {
                            XToastUtils.toast("headBanner position--->" + position);
                        }
                    }).startScroll();

        } else if (getItemViewType(position) == TYPE_BANNER_FOOT) {
            footBanner = holder.findViewById(R.id.sib_simple_usage);
            footBanner.setSource(mData)
                    .setOnItemClickListener(new BaseBanner.OnItemClickListener<BannerItem>() {
                        @Override
                        public void onItemClick(View view, BannerItem item, int position) {
                            XToastUtils.toast("headBanner position--->" + position);
                        }
                    }).startScroll();
        } else {
            holder.text(android.R.id.text1, ResUtils.getResources().getString(R.string.item_example_number_title, position));
            holder.text(android.R.id.text2, ResUtils.getResources().getString(R.string.item_example_number_abstract, position) + "：" + item);
            holder.textColorId(android.R.id.text2, R.color.xui_config_color_light_blue_gray);
        }
    }

    /**
     * 资源释放
     */
    public void recycle() {
        if (headBanner != null) {
            headBanner.recycle();
        }
        if (footBanner != null) {
            footBanner.recycle();
        }
        clear();
    }


}
