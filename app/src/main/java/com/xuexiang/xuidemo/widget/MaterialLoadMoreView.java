package com.xuexiang.xuidemo.widget;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.yanzhenjie.recyclerview.SwipeRecyclerView;

/**
 * 简单的Material Design风格的加载进度条
 *
 * @author XUE
 * @since 2019/5/9 9:37
 */
public class MaterialLoadMoreView extends ProgressBar implements SwipeRecyclerView.LoadMoreView {

    public MaterialLoadMoreView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        setVisibility(GONE);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        setLayoutParams(params);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onLoading() {
        setVisibility(VISIBLE);
    }

    @Override
    public void onLoadFinish(boolean dataEmpty, boolean hasMore) {
        setVisibility(GONE);
    }

    @Override
    public void onWaitToLoadMore(SwipeRecyclerView.LoadMoreListener loadMoreListener) {

    }

    @Override
    public void onLoadError(int errorCode, String errorMessage) {
        setVisibility(GONE);
    }
}
