/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xui.widget.banner.recycler;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.xuexiang.xui.R;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerViewBannerBase<L extends RecyclerView.LayoutManager, A extends RecyclerView.Adapter> extends FrameLayout {
    private final static int WHAT_AUTO_PLAY = 1000;


    protected RecyclerView mIndicatorContainer;
    protected IndicatorAdapter mIndicatorAdapter;
    protected int mIndicatorMargin;//指示器间距
    protected boolean mShowIndicator;//是否显示指示器
    protected Drawable mSelectedDrawable;
    protected Drawable mUnselectedDrawable;

    protected RecyclerView mRecyclerView;
    protected A mAdapter;
    protected L mLayoutManager;

    protected boolean mIsAutoPlaying;
    protected int mAutoPlayDuration = 4000;//刷新间隔时间
    protected boolean mHasInit;
    protected int mBannerSize = 1;
    protected int mCurrentIndex;
    protected boolean mIsPlaying;
    protected List<String> mTempUrlList = new ArrayList<>();

    protected Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == WHAT_AUTO_PLAY) {
                mRecyclerView.smoothScrollToPosition(++mCurrentIndex);
                refreshIndicator();
                mHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, mAutoPlayDuration);
            }
            return false;
        }
    });

    public RecyclerViewBannerBase(Context context) {
        this(context, null);
    }

    public RecyclerViewBannerBase(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewBannerBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    protected void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerViewBannerBase);
        mShowIndicator = a.getBoolean(R.styleable.RecyclerViewBannerBase_rvbb_showIndicator, true);
        mAutoPlayDuration = a.getInt(R.styleable.RecyclerViewBannerBase_rvbb_interval, 4000);
        mIsAutoPlaying = a.getBoolean(R.styleable.RecyclerViewBannerBase_rvbb_autoPlaying, true);
        mSelectedDrawable = a.getDrawable(R.styleable.RecyclerViewBannerBase_rvbb_indicatorSelectedSrc);
        mUnselectedDrawable = a.getDrawable(R.styleable.RecyclerViewBannerBase_rvbb_indicatorUnselectedSrc);
        if (mSelectedDrawable == null) {
            //绘制默认选中状态图形
            GradientDrawable selectedGradientDrawable = new GradientDrawable();
            selectedGradientDrawable.setShape(GradientDrawable.OVAL);
            selectedGradientDrawable.setColor(Color.RED);
            selectedGradientDrawable.setSize(dp2px(5), dp2px(5));
            selectedGradientDrawable.setCornerRadius(dp2px(5) / 2);
            mSelectedDrawable = new LayerDrawable(new Drawable[]{selectedGradientDrawable});
        }
        if (mUnselectedDrawable == null) {
            //绘制默认未选中状态图形
            GradientDrawable unSelectedGradientDrawable = new GradientDrawable();
            unSelectedGradientDrawable.setShape(GradientDrawable.OVAL);
            unSelectedGradientDrawable.setColor(Color.GRAY);
            unSelectedGradientDrawable.setSize(dp2px(5), dp2px(5));
            unSelectedGradientDrawable.setCornerRadius(dp2px(5) / 2);
            mUnselectedDrawable = new LayerDrawable(new Drawable[]{unSelectedGradientDrawable});
        }

        mIndicatorMargin = a.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_rvbb_indicatorSpace, dp2px(4));
        int marginLeft = a.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_rvbb_indicatorMarginLeft, dp2px(16));
        int marginRight = a.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_rvbb_indicatorMarginRight, dp2px(0));
        int marginBottom = a.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_rvbb_indicatorMarginBottom, dp2px(11));
        int g = a.getInt(R.styleable.RecyclerViewBannerBase_rvbb_indicatorGravity, 0);
        int gravity;
        if (g == 0) {
            gravity = GravityCompat.START;
        } else if (g == 2) {
            gravity = GravityCompat.END;
        } else {
            gravity = Gravity.CENTER;
        }
        int orientation = a.getInt(R.styleable.RecyclerViewBannerBase_rvbb_orientation, LinearLayoutManager.HORIZONTAL);
        a.recycle();

        //recyclerView部分
        mRecyclerView = new RecyclerView(context);
        new PagerSnapHelper().attachToRecyclerView(mRecyclerView);
        mLayoutManager = getLayoutManager(context, orientation);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                onBannerScrolled(recyclerView, dx, dy);
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                onBannerScrollStateChanged(recyclerView, newState);

            }
        });
        LayoutParams vpLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mRecyclerView, vpLayoutParams);
        //指示器部分
        mIndicatorContainer = new RecyclerView(context);

        LinearLayoutManager indicatorLayoutManager = new LinearLayoutManager(context, orientation, false);
        mIndicatorContainer.setLayoutManager(indicatorLayoutManager);
        mIndicatorAdapter = new IndicatorAdapter();
        mIndicatorContainer.setAdapter(mIndicatorAdapter);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | gravity;
        params.setMargins(marginLeft, 0, marginRight, marginBottom);
        addView(mIndicatorContainer, params);
        if (!mShowIndicator) {
            mIndicatorContainer.setVisibility(GONE);
        }
    }

    protected void onBannerScrolled(RecyclerView recyclerView, int dx, int dy) {

    }

    protected void onBannerScrollStateChanged(RecyclerView recyclerView, int newState) {

    }

    protected abstract L getLayoutManager(Context context, int orientation);

    protected abstract A getAdapter(Context context, List<String> list, OnBannerItemClickListener onBannerItemClickListener);

    /**
     * 设置轮播间隔时间
     *
     * @param millisecond 时间毫秒
     */
    public void setIndicatorInterval(int millisecond) {
        this.mAutoPlayDuration = millisecond;
    }

    /**
     * 设置是否自动播放（上锁）
     *
     * @param playing 开始播放
     */
    protected synchronized void setPlaying(boolean playing) {
        if (mIsAutoPlaying && mHasInit) {
            if (!mIsPlaying && playing) {
                mHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, mAutoPlayDuration);
                mIsPlaying = true;
            } else if (mIsPlaying && !playing) {
                mHandler.removeMessages(WHAT_AUTO_PLAY);
                mIsPlaying = false;
            }
        }
    }

    /**
     * 设置是否禁止滚动播放
     */
    public void setAutoPlaying(boolean isAutoPlaying) {
        mIsAutoPlaying = isAutoPlaying;
        setPlaying(this.mIsAutoPlaying);
    }

    public boolean isPlaying() {
        return mIsPlaying;
    }

    public void setShowIndicator(boolean showIndicator) {
        mShowIndicator = showIndicator;
        mIndicatorContainer.setVisibility(showIndicator ? VISIBLE : GONE);
    }

    /**
     * 设置轮播数据集
     */
    public void initBannerImageView(@NonNull List<String> newList, OnBannerItemClickListener onBannerItemClickListener) {
        //解决recyclerView嵌套问题
        if (compareListDifferent(newList, mTempUrlList)) {
            mHasInit = false;
            setVisibility(VISIBLE);
            setPlaying(false);
            mAdapter = getAdapter(getContext(), newList, onBannerItemClickListener);
            mRecyclerView.setAdapter(mAdapter);
            mTempUrlList = newList;
            mBannerSize = mTempUrlList.size();
            if (mBannerSize > 1) {
                mIndicatorContainer.setVisibility(VISIBLE);
                mCurrentIndex = mBannerSize * 10000;
                mRecyclerView.scrollToPosition(mCurrentIndex);
                mIndicatorAdapter.notifyDataSetChanged();
                setPlaying(true);
            } else {
                mIndicatorContainer.setVisibility(GONE);
                mCurrentIndex = 0;
            }
            mHasInit = true;
        }
        if (!mShowIndicator) {
            mIndicatorContainer.setVisibility(GONE);
        }
    }

    /**
     * 设置轮播数据集
     */
    public void initBannerImageView(@NonNull List<String> newList) {
        initBannerImageView(newList, null);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setPlaying(false);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setPlaying(true);
                break;
        }
        //解决recyclerView嵌套问题
        try {
            return super.dispatchTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    //解决recyclerView嵌套问题
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    //解决recyclerView嵌套问题
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setPlaying(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setPlaying(false);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            setPlaying(true);
        } else {
            setPlaying(false);
        }
    }

    /**
     * 标示点适配器
     */
    protected class IndicatorAdapter extends RecyclerView.Adapter {

        int currentPosition = 0;

        public void setPosition(int currentPosition) {
            this.currentPosition = currentPosition;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            ImageView bannerPoint = new ImageView(getContext());
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(mIndicatorMargin, mIndicatorMargin, mIndicatorMargin, mIndicatorMargin);
            bannerPoint.setLayoutParams(lp);
            return new RecyclerView.ViewHolder(bannerPoint) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImageView bannerPoint = (ImageView) holder.itemView;
            bannerPoint.setImageDrawable(currentPosition == position ? mSelectedDrawable : mUnselectedDrawable);

        }

        @Override
        public int getItemCount() {
            return mBannerSize;
        }
    }


    /**
     * 改变导航的指示点
     */
    protected synchronized void refreshIndicator() {
        if (mShowIndicator && mBannerSize > 1) {
            mIndicatorAdapter.setPosition(mCurrentIndex % mBannerSize);
            mIndicatorAdapter.notifyDataSetChanged();
        }
    }

    public interface OnBannerItemClickListener {
        void onItemClick(int position);
    }

    protected int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    /**
     * 获取颜色
     */
    protected int getColor(@ColorRes int color) {
        return ContextCompat.getColor(getContext(), color);
    }

    protected boolean compareListDifferent(List<String> newTabList, List<String> oldTabList) {
        if (oldTabList == null || oldTabList.isEmpty())
            return true;
        if (newTabList.size() != oldTabList.size())
            return true;
        for (int i = 0; i < newTabList.size(); i++) {
            if (TextUtils.isEmpty(newTabList.get(i)))
                return true;
            if (!newTabList.get(i).equals(oldTabList.get(i))) {
                return true;
            }
        }
        return false;
    }

}