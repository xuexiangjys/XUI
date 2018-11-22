package com.xuexiang.xui.widget.textview.marqueen;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 负责解析提供数据及事件监听
 * @author XUE
 * @date 2017/9/13 9:39
 */
public abstract class MarqueeFactory<T extends View, E> {
    protected Context mContext;
    protected OnItemClickListener<T, E> mOnItemClickListener;
    protected List<T> mViews;
    protected List<E> mDatas;
    private boolean isOnItemClickRegistered;
    private MarqueeView mMarqueeView;

    public MarqueeFactory(Context context) {
        mContext = context;
    }

    public abstract T generateMarqueeItemView(E data);

    public void setData(List<E> datas) {
        if (datas == null || datas.size() == 0) {
            return;
        }
        mDatas = datas;
        mViews = new ArrayList<T>();
        for (int i = 0; i < datas.size(); i++) {
            E data = datas.get(i);
            T mView = generateMarqueeItemView(data);
            mViews.add(mView);
        }
        registerOnItemClick();
        if (mMarqueeView != null) {
            mMarqueeView.setMarqueeFactory(this);
        }
    }

    /**
     * 设置Item的监听
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener<T, E> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        registerOnItemClick();
    }

    public List<T> getMarqueeViews() {
        return mViews;
    }

    private void registerOnItemClick() {
        if (!isOnItemClickRegistered && mOnItemClickListener != null && mDatas != null) {
            for (int i = 0; i < mDatas.size(); i++) {
                T mView = mViews.get(i);
                E data = mDatas.get(i);
                mView.setTag(new ViewHolder(mView, data, i));
                mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.onItemClickListener((ViewHolder<T, E>) view.getTag());
                    }
                });
            }
            isOnItemClickRegistered = true;
        }
    }

    public interface OnItemClickListener<V extends View, E> {
        void onItemClickListener(ViewHolder<V, E> holder);
    }

    public static class ViewHolder<V extends View, P> {
        public V mView;
        public P mData;
        public int mPosition;

        public ViewHolder(V view, P data, int position) {
            mView = view;
            mData = data;
            mPosition = position;
        }

        public V getView() {
            return mView;
        }

        public ViewHolder setView(V view) {
            mView = view;
            return this;
        }

        public P getData() {
            return mData;
        }

        public ViewHolder setData(P data) {
            mData = data;
            return this;
        }

        public int getPosition() {
            return mPosition;
        }

        public ViewHolder setPosition(int position) {
            mPosition = position;
            return this;
        }
    }

    public void setAttachedToMarqueeView(MarqueeView marqueeView) {
       mMarqueeView = marqueeView;
    }
}