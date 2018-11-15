package com.xuexiang.xui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;


/**
 * 可以监听滚动事件的 {@link ScrollView}，并能在滚动回调中获取每次滚动前后的偏移量。
 * <p>
 *  由于 {@link ScrollView} 没有类似于 addOnScrollChangedListener 的方法可以监听滚动事件，所以需要通过重写 {@link android.view.View#onScrollChanged}，来触发滚动监听
 * </p>
 * @author xuexiang
 * @since 2018/11/14 下午1:21
 */
public class XUIObservableScrollView extends ScrollView {

    private int mScrollOffset = 0;

    private List<OnScrollChangedListener> mOnScrollChangedListeners;

    public XUIObservableScrollView(Context context) {
        super(context);
    }

    public XUIObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XUIObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 添加滚动监听
     * @param onScrollChangedListener
     */
    public void addOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        if (mOnScrollChangedListeners == null) {
            mOnScrollChangedListeners = new ArrayList<>();
        }
        if (mOnScrollChangedListeners.contains(onScrollChangedListener)) {
            return;
        }
        mOnScrollChangedListeners.add(onScrollChangedListener);
    }

    public void removeOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        if (mOnScrollChangedListeners == null) {
            return;
        }
        mOnScrollChangedListeners.remove(onScrollChangedListener);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mScrollOffset = t;
        if (mOnScrollChangedListeners != null && !mOnScrollChangedListeners.isEmpty()) {
            for (OnScrollChangedListener listener : mOnScrollChangedListeners) {
                listener.onScrollChanged(this, l, t, oldl, oldt);
            }
        }
    }

    public int getScrollOffset() {
        return mScrollOffset;
    }

    public interface OnScrollChangedListener {
        /**
         * 滚动发生变化
         * @param scrollView
         * @param l 变化后的X轴位置
         * @param t 变化后的Y轴的位置
         * @param oldl 原先的X轴的位置
         * @param oldt 原先的Y轴的位置
         */
        void onScrollChanged(XUIObservableScrollView scrollView, int l, int t, int oldl, int oldt);
    }

}
