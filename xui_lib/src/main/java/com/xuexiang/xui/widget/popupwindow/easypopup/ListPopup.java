package com.xuexiang.xui.widget.popupwindow.easypopup;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.XUIWrapContentListView;

/**
 * 带条目的弹出框
 *
 * @author xuexiang
 * @date 2017/10/30 下午7:19
 */
public class ListPopup extends BaseCustomPopup {
    /**
     * 集合
     */
    private ListView mListView;
    /**
     * 适配器
     */
    private BaseAdapter mAdapter;

    private int mMaxHeight;

    private AdapterView.OnItemClickListener mOnItemClickListener;

    protected ListPopup(Context context) {
        super(context);
    }

    @Override
    protected void initAttributes() {
        mListView = new XUIWrapContentListView(getContext(), mMaxHeight);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(mWidth, mMaxHeight);
        mListView.setLayoutParams(lp);
        mListView.setAdapter(mAdapter);
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setOnItemClickListener(mOnItemClickListener);
        mListView.setDivider(ResUtils.getDrawable(R.drawable.xui_config_list_item_selector));
        mListView.setDividerHeight(DensityUtils.dp2px(getContext(), 1));

        setContentView(mListView);
    }

    @Override
    protected void initViews(View view) {

    }


    public BaseAdapter getAdapter() {
        return mAdapter;
    }

    public ListPopup setAdapter(BaseAdapter adapter) {
        mAdapter = adapter;
        return this;
    }

    public int getMaxHeight() {
        return mMaxHeight;
    }

    public ListPopup setMaxHeight(int maxHeight) {
        mMaxHeight = maxHeight;
        return this;
    }

    public ListPopup setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        return this;
    }


}
