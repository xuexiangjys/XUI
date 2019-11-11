package com.xuexiang.xui.widget.popupwindow.popup;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.UIConsts;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.adapter.simple.XUISimpleAdapter;
import com.xuexiang.xui.utils.ResUtils;

import java.util.List;

/**
 * 简单的弹窗
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:07
 */
public class XUISimplePopup<T extends XUISimplePopup> extends XUIListPopup {

    public XUISimplePopup(Context context, String[] listItems) {
        this(context, XUISimpleAdapter.create(context, listItems));
    }

    public XUISimplePopup(Context context, List<AdapterItem> listItems) {
        this(context, new XUISimpleAdapter(context, listItems));
    }

    public XUISimplePopup(Context context, AdapterItem[] listItems) {
        this(context, new XUISimpleAdapter(context, listItems));
    }

    public XUISimplePopup(Context context, XUISimpleAdapter adapter) {
        super(context, adapter);
    }

    /**
     * 创建弹窗
     *
     * @param maxHeight
     * @return
     */
    @Override
    public T create(int maxHeight) {
        create(getPopupWidth(), maxHeight);
        return (T) this;
    }

    /**
     * 创建弹窗
     *
     * @param maxHeight
     * @param onItemClickListener
     * @return
     */
    public T create(int maxHeight, final OnPopupItemClickListener onItemClickListener) {
        return create(getPopupWidth(), maxHeight, onItemClickListener);
    }

    /**
     * 创建弹窗
     *
     * @param onItemClickListener
     * @return
     */
    public T create(final OnPopupItemClickListener onItemClickListener) {
        create(getPopupWidth());
        setOnPopupItemClickListener(onItemClickListener);
        return (T) this;
    }

    /**
     * 创建弹窗
     *
     * @param width
     * @param maxHeight
     * @param onItemClickListener
     * @return
     */
    public T create(int width, int maxHeight, final OnPopupItemClickListener onItemClickListener) {
        create(width, maxHeight);
        setOnPopupItemClickListener(onItemClickListener);
        return (T) this;
    }

    /**
     * 设置条目点击监听
     *
     * @param onItemClickListener
     * @return
     */
    public T setOnPopupItemClickListener(final OnPopupItemClickListener onItemClickListener) {
        if (mListView != null) {
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapter(), getAdapter().getItem(position), position);
                    }
                    dismiss();
                }
            });
        }
        return (T) this;
    }

    /**
     * 条目点击监听
     */
    public interface OnPopupItemClickListener {
        /**
         * 条目点击
         *
         * @param adapter
         * @param item
         * @param position
         */
        void onItemClick(XUISimpleAdapter adapter, AdapterItem item, int position);
    }

    @Override
    public XUISimpleAdapter getAdapter() {
        return (XUISimpleAdapter) mAdapter;
    }


    @Override
    public T setHasDivider(boolean hasDivider) {
        super.setHasDivider(hasDivider);
        return (T) this;
    }
}
