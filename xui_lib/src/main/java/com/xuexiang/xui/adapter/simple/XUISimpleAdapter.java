package com.xuexiang.xui.adapter.simple;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;

import com.xuexiang.xui.R;
import com.xuexiang.xui.adapter.listview.BaseListAdapter;
import com.xuexiang.xui.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单的适配器
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:07
 */
public class XUISimpleAdapter extends BaseListAdapter<AdapterItem, ViewHolder> {

    private int mPaddingStartPx;

    public XUISimpleAdapter(Context context) {
        super(context);
    }

    public XUISimpleAdapter(Context context, List<AdapterItem> data) {
        super(context, data);
    }

    public XUISimpleAdapter(Context context, AdapterItem[] data) {
        super(context, data);
    }

    @Override
    protected ViewHolder newViewHolder(View convertView) {
        ViewHolder holder = new ViewHolder();
        holder.mLLContentView = convertView.findViewById(R.id.ll_content);
        holder.mTvTitle = convertView.findViewById(R.id.tv_title);
        holder.mIvIcon = convertView.findViewById(R.id.iv_icon);

        if (mPaddingStartPx != 0) {
            holder.mLLContentView.setPaddingRelative(mPaddingStartPx, 0, 0, 0);
            holder.mLLContentView.setGravity(Gravity.CENTER_VERTICAL);
        } else {
            holder.mLLContentView.setGravity(Gravity.CENTER);
        }
        return holder;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.xui_adapter_listview_simple_item;
    }

    @Override
    protected void convert(ViewHolder holder, AdapterItem item, int position) {
        holder.mTvTitle.setText(item.getTitle());
        if (item.getIcon() != null) {
            holder.mIvIcon.setVisibility(View.VISIBLE);
            holder.mIvIcon.setImageDrawable(item.getIcon());
        } else {
            holder.mIvIcon.setVisibility(View.GONE);
        }

    }

    @Deprecated
    public XUISimpleAdapter setPaddingLeftPx(int paddingLeftPx) {
        mPaddingStartPx = paddingLeftPx;
        return this;
    }

    @Deprecated
    public XUISimpleAdapter setPaddingLeftDp(@NonNull Context context, int paddingLeftDp) {
        mPaddingStartPx = DensityUtils.dp2px(context, paddingLeftDp);
        return this;
    }

    public XUISimpleAdapter setPaddingStartPx(int paddingStartPx) {
        mPaddingStartPx = paddingStartPx;
        return this;
    }

    public XUISimpleAdapter setPaddingStartDp(@NonNull Context context, int paddingStartDp) {
        mPaddingStartPx = DensityUtils.dp2px(context, paddingStartDp);
        return this;
    }

    /**
     * 创建简单的适配器【不含图标】
     *
     * @param context
     * @param data
     * @return
     */
    public static XUISimpleAdapter create(Context context, String[] data) {
        if (data != null && data.length > 0) {
            List<AdapterItem> lists = new ArrayList<>();
            for (String datum : data) {
                lists.add(new AdapterItem(datum));
            }
            return new XUISimpleAdapter(context, lists);
        } else {
            return new XUISimpleAdapter(context);
        }
    }

    /**
     * 创建简单的适配器【不含图标】
     *
     * @param context
     * @param data
     * @return
     */
    public static XUISimpleAdapter create(Context context, List<String> data) {
        if (data != null && data.size() > 0) {
            List<AdapterItem> lists = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                lists.add(new AdapterItem(data.get(i)));
            }
            return new XUISimpleAdapter(context, lists);
        } else {
            return new XUISimpleAdapter(context);
        }
    }

}
