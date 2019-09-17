package com.xuexiang.xui.widget.popupwindow.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.XUIWrapContentListView;

/**
 * 继承自 {@link XUIPopup}，在 {@link XUIPopup} 的基础上，支持显示一个列表。
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:10
 */
public class XUIListPopup<T extends XUIListPopup> extends XUIPopup {
    protected ListView mListView;
    protected BaseAdapter mAdapter;
    private boolean mHasDivider;

    /**
     * Constructor.
     *
     * @param context   Context
     * @param direction
     */
    public XUIListPopup(Context context, int direction, BaseAdapter adapter) {
        super(context, direction);
        mAdapter = adapter;
    }

    public XUIListPopup(Context context, BaseAdapter adapter) {
        super(context);
        mAdapter = adapter;
    }

    /**
     * 创建弹窗
     *
     * @param width               弹窗的宽度
     * @param maxHeight           弹窗最大的高度
     * @param onItemClickListener 列表点击的监听
     * @return
     */
    public T create(int width, int maxHeight, AdapterView.OnItemClickListener onItemClickListener) {
        create(width, maxHeight);
        mListView.setOnItemClickListener(onItemClickListener);
        return (T) this;
    }

    /**
     * 创建弹窗
     *
     * @param width     弹窗的宽度
     * @param maxHeight 弹窗最大的高度
     * @return
     */
    protected T create(int width, int maxHeight) {
        int margin = DensityUtils.dp2px(getContext(), 5);
        if (maxHeight != 0) {
            mListView = new XUIWrapContentListView(getContext(), maxHeight);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, maxHeight);
            lp.setMargins(0, margin, 0, margin);
            mListView.setLayoutParams(lp);
        } else {
            mListView = new XUIWrapContentListView(getContext());
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, FrameLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, margin, 0, margin);
            mListView.setLayoutParams(lp);
        }
        mListView.setPadding(margin, 0, margin, 0);
        mListView.setAdapter(mAdapter);
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        updateListViewDivider(mListView);
        setContentView(mListView);
        return (T) this;
    }

    /**
     * 创建弹窗
     *
     * @param width 弹窗的宽度
     * @return
     */
    protected T create(int width) {
        return create(width, 0);
    }

    /**
     * 设置是否有分割线
     *
     * @param hasDivider
     * @return
     */
    public T setHasDivider(boolean hasDivider) {
        mHasDivider = hasDivider;
        if (mListView != null) {
            updateListViewDivider(mListView);
        }
        return (T) this;
    }

    private void updateListViewDivider(ListView listView) {
        if (mHasDivider) {
            listView.setDivider(new ColorDrawable(ThemeUtils.resolveColor(getContext(), R.attr.xui_config_color_separator_light)));
            if (XUI.isTablet()) {
                listView.setDividerHeight(DensityUtils.dp2px(getContext(), 1));
            } else {
                listView.setDividerHeight(DensityUtils.dp2px(getContext(), 0.5F));
            }
        } else {
            listView.setDivider(null);
        }
    }

    /**
     * 设置分割线的资源
     *
     * @param divider
     * @return
     */
    public T setDivider(Drawable divider) {
        mListView.setDivider(divider);
        return (T) this;
    }

    /**
     * 设置分割线的高度
     *
     * @param dividerHeight
     * @return
     */
    public T setDividerHeight(int dividerHeight) {
        mListView.setDividerHeight(dividerHeight);
        return (T) this;
    }

    public BaseAdapter getAdapter() {
        return mAdapter;
    }

}
