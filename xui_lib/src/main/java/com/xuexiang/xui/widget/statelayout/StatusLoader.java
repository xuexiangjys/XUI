package com.xuexiang.xui.widget.statelayout;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.xuexiang.xui.logs.UILog;

/**
 * 多状态布局加载者
 *
 * @author xuexiang
 * @since 2020/4/29 12:12 AM
 */
public class StatusLoader {
    /**
     * 加载中状态
     */
    public static final int STATUS_LOADING = 1;
    /**
     * 加载成功状态
     */
    public static final int STATUS_LOAD_SUCCESS = 2;
    /**
     * 加载失败状态
     */
    public static final int STATUS_LOAD_FAILED = 3;
    /**
     * 空数据状态
     */
    public static final int STATUS_EMPTY_DATA = 4;
    /**
     * 自定义状态
     */
    public static final int STATUS_CUSTOM = 5;

    private static volatile StatusLoader sDefault;
    private Adapter mAdapter;

    /**
     * 状态适配器
     */
    public interface Adapter {
        /**
         * 根据不同状态加载不同类型的布局
         *
         * @param holder      Holder
         * @param convertView 上一个状态的布局，可以复用
         * @param status      当前状态
         * @return 需要显示的状态布局。可以是convertView复用.
         * @see Holder
         */
        View getView(Holder holder, View convertView, int status);
    }

    private StatusLoader() {
    }

    /**
     * Create a new StatusLoader different from the default one
     *
     * @param adapter another adapter different from the default one
     * @return StatusLoader
     */
    public static StatusLoader from(Adapter adapter) {
        StatusLoader statusLoader = new StatusLoader();
        statusLoader.mAdapter = adapter;
        return statusLoader;
    }

    /**
     * get default StatusLoader object for global usage in whole app
     *
     * @return default StatusLoader object
     */
    public static StatusLoader getDefault() {
        if (sDefault == null) {
            synchronized (StatusLoader.class) {
                if (sDefault == null) {
                    sDefault = new StatusLoader();
                }
            }
        }
        return sDefault;
    }

    /**
     * init the default loading status view creator ({@link Adapter})
     *
     * @param adapter adapter to create all status views
     */
    public static void initDefault(Adapter adapter) {
        getDefault().mAdapter = adapter;
    }

    /**
     * StatusLoader(loading status view) wrap the whole activity
     * wrapper is android.R.id.content
     *
     * @param activity current activity object
     * @return holder of StatusLoader
     */
    public Holder wrap(Activity activity) {
        ViewGroup wrapper = activity.findViewById(android.R.id.content);
        return new Holder(mAdapter, activity, wrapper);
    }

    /**
     * StatusLoader(loading status view) wrap the specific view.
     *
     * @param view view to be wrapped
     * @return Holder
     */
    public Holder wrap(View view) {
        FrameLayout wrapper = new FrameLayout(view.getContext());
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp != null) {
            wrapper.setLayoutParams(lp);
        }
        if (view.getParent() != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            int index = parent.indexOfChild(view);
            parent.removeView(view);
            parent.addView(wrapper, index);
        }
        LayoutParams newLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        wrapper.addView(view, newLp);
        return new Holder(mAdapter, view.getContext(), wrapper);
    }

    /**
     * loadingStatusView shows cover the view with the same LayoutParams object
     * this method is useful with RelativeLayout and ConstraintLayout
     *
     * @param view the view which needs show loading status
     * @return Holder
     */
    public Holder cover(View view) {
        ViewParent parent = view.getParent();
        if (parent == null) {
            throw new RuntimeException("view has no parent to show StatusLoader as cover!");
        }
        ViewGroup viewGroup = (ViewGroup) parent;
        FrameLayout wrapper = new FrameLayout(view.getContext());
        viewGroup.addView(wrapper, view.getLayoutParams());
        return new Holder(mAdapter, view.getContext(), wrapper);
    }

    /**
     * StatusLoader holder<br>
     * create by {@link StatusLoader#wrap(Activity)} or {@link StatusLoader#wrap(View)}<br>
     * the core API for showing all status view
     */
    public static class Holder {
        private Adapter mAdapter;
        private Context mContext;
        private View.OnClickListener mRetryListener;
        private View mCurStatusView;
        private ViewGroup mWrapper;
        private int curState;
        private SparseArray<View> mStatusViews = new SparseArray<>(5);
        private Object mData;

        private Holder(Adapter adapter, Context context, ViewGroup wrapper) {
            this.mAdapter = adapter;
            this.mContext = context;
            this.mWrapper = wrapper;
        }

        /**
         * 设置重试监听
         *
         * @param listener 重试监听
         * @return this
         */
        public Holder withRetry(View.OnClickListener listener) {
            mRetryListener = listener;
            return this;
        }

        /**
         * set extension data
         *
         * @param data extension data
         * @return this
         */
        public Holder withData(Object data) {
            this.mData = data;
            return this;
        }

        public int getCurState() {
            return curState;
        }

        /**
         * show UI for status: {@link #STATUS_LOADING}
         */
        public void showLoading() {
            showLoadingStatus(STATUS_LOADING);
        }

        /**
         * show UI for status: {@link #STATUS_LOAD_SUCCESS}
         */
        public void showLoadSuccess() {
            showLoadingStatus(STATUS_LOAD_SUCCESS);
        }

        /**
         * show UI for status: {@link #STATUS_LOAD_FAILED}
         */
        public void showLoadFailed() {
            showLoadingStatus(STATUS_LOAD_FAILED);
        }

        /**
         * show UI for status: {@link #STATUS_EMPTY_DATA}
         */
        public void showEmpty() {
            showLoadingStatus(STATUS_EMPTY_DATA);
        }

        /**
         * show UI for status: {@link #STATUS_CUSTOM}
         */
        public void showCustom() {
            showLoadingStatus(STATUS_CUSTOM);
        }


        /**
         * Show specific status UI
         *
         * @param status status
         * @see #showLoading()
         * @see #showLoadFailed()
         * @see #showLoadSuccess()
         * @see #showEmpty()
         */
        public void showLoadingStatus(int status) {
            if (curState == status || !validate()) {
                return;
            }
            curState = status;
            //first try to reuse status view
            View convertView = mStatusViews.get(status);
            if (convertView == null) {
                //secondly try to reuse current status view
                convertView = mCurStatusView;
            }
            try {
                //call customer adapter to get UI for specific status. convertView can be reused
                View view = mAdapter.getView(this, convertView, status);
                if (view == null) {
                    UILog.e(mAdapter.getClass().getName() + ".getView returns null");
                    return;
                }
                if (view != mCurStatusView || mWrapper.indexOfChild(view) < 0) {
                    if (mCurStatusView != null) {
                        mWrapper.removeView(mCurStatusView);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.setElevation(Float.MAX_VALUE);
                    }
                    mWrapper.addView(view);
                    ViewGroup.LayoutParams lp = view.getLayoutParams();
                    if (lp != null) {
                        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    }
                } else if (mWrapper.indexOfChild(view) != mWrapper.getChildCount() - 1) {
                    // make sure loading status view at the front
                    view.bringToFront();
                }
                mCurStatusView = view;
                mStatusViews.put(status, view);
            } catch (Exception e) {
                UILog.e(e);
            }
        }

        private boolean validate() {
            if (mAdapter == null) {
                UILog.e("StatusLoader.Adapter is not specified！");
            }
            if (mContext == null) {
                UILog.e("Context is null！");
            }
            if (mWrapper == null) {
                UILog.e("The mWrapper of loading status view is null！");
            }
            return mAdapter != null && mContext != null && mWrapper != null;
        }

        public Context getContext() {
            return mContext;
        }

        /**
         * get wrapper
         *
         * @return container of StatusLoader
         */
        public ViewGroup getWrapper() {
            return mWrapper;
        }

        /**
         * get retry task
         *
         * @return retry task
         */
        public View.OnClickListener getRetryListener() {
            return mRetryListener;
        }

        /**
         * get extension data
         *
         * @param <T> return type
         * @return data
         */
        public <T> T getData() {
            try {
                return (T) mData;
            } catch (Exception e) {
                UILog.e(e);
            }
            return null;
        }
    }

}
