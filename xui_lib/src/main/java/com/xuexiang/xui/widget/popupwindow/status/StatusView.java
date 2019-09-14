/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.xui.widget.popupwindow.status;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import android.support.annotation.NonNull;

import com.xuexiang.xui.R;

/**
 * 状态控件
 *
 * @author xuexiang
 * @since 2018/12/27 下午5:35
 */
public class StatusView extends FrameLayout {

    private static final int DISMISS_ON_COMPLETE_DELAY = 1000;

    /**
     * 控件当前的状态
     */
    private Status mCurrentStatus;

    /**
     * 是否自动隐藏当是完成状态的时候，默认是true
     */
    private boolean mHideOnComplete = true;

    /**
     * 状态显示的控件
     */
    private View mCompleteView;
    private View mErrorView;
    private View mLoadingView;
    private View mCustomView;

    /**
     * Fade in out animations
     */
    private Animation mSlideOut;
    private Animation mSlideIn;

    /**
     * Handler
     */
    private Handler mHandler;

    /**
     * Auto dismiss on complete
     */
    private Runnable mAutoDismissOnComplete = new Runnable() {
        @Override
        public void run() {
            exitAnimation(getCurrentView(mCurrentStatus));
            mHandler.removeCallbacks(mAutoDismissOnComplete);
        }
    };

    public StatusView(Context context) {
        this(context, null);
    }

    public StatusView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.StatusViewStyle);
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
    }

    public StatusView(Context context, int completeLayout, int errorLayout, int loadingLayout, int customLayout) {
        this(context, null, completeLayout, errorLayout, loadingLayout, customLayout);
    }

    public StatusView(Context context, AttributeSet attrs, int completeLayout, int errorLayout, int loadingLayout, int customLayout) {
        this(context, attrs, R.attr.StatusViewStyle, completeLayout, errorLayout, loadingLayout, customLayout);
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr, int completeLayout, int errorLayout, int loadingLayout, int customLayout) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, completeLayout, errorLayout, loadingLayout, customLayout);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        init(context, attrs, defStyleAttr, 0, 0, 0, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int completeLayout, int errorLayout, int loadingLayout, int customLayout) {
        // Load initial values
        mCurrentStatus = Status.NONE;
        mSlideIn = AnimationUtils.loadAnimation(context, R.anim.sv_slide_in);
        mSlideOut = AnimationUtils.loadAnimation(context, R.anim.sv_slide_out);

        LayoutInflater inflate = LayoutInflater.from(context);
        mHandler = new Handler();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StatusView, defStyleAttr, 0);

        // get status layout ids
        int completeLayoutId = a.getResourceId(R.styleable.StatusView_sv_complete, 0);
        int errorLayoutId = a.getResourceId(R.styleable.StatusView_sv_error, 0);
        int loadingLayoutId = a.getResourceId(R.styleable.StatusView_sv_loading, 0);
        int customLayoutId = a.getResourceId(R.styleable.StatusView_sv_custom, 0);

        mHideOnComplete = a.getBoolean(R.styleable.StatusView_sv_dismissOnComplete, mHideOnComplete);

        // inflate layouts
        mCompleteView = inflate.inflate(completeLayout != 0 ? completeLayout : completeLayoutId, null);
        mErrorView = inflate.inflate(errorLayout != 0 ? errorLayout : errorLayoutId, null);
        mLoadingView = inflate.inflate(loadingLayout != 0 ? loadingLayout : loadingLayoutId, null);
        mCustomView = inflate.inflate(customLayout != 0 ? customLayout : customLayoutId, null);

        // Default layout params
        mCompleteView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mErrorView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mLoadingView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mCustomView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Add layout to root
        addView(mCompleteView);
        addView(mErrorView);
        addView(mLoadingView);
        addView(mCustomView);

        // set visibilities of childs
        mCompleteView.setVisibility(View.INVISIBLE);
        mErrorView.setVisibility(View.INVISIBLE);
        mLoadingView.setVisibility(View.INVISIBLE);
        mCustomView.setVisibility(View.INVISIBLE);

        a.recycle();
    }

    public void setOnErrorClickListener(@NonNull OnClickListener onErrorClickListener) {
        mErrorView.setOnClickListener(onErrorClickListener);
    }

    public void setOnCustomClickListener(@NonNull OnClickListener onErrorClickListener) {
        mCustomView.setOnClickListener(onErrorClickListener);
    }

    public void setOnLoadingClickListener(@NonNull OnClickListener onLoadingClickListener) {
        mLoadingView.setOnClickListener(onLoadingClickListener);
    }

    public void setOnCompleteClickListener(@NonNull OnClickListener onCompleteClickListener) {
        mCompleteView.setOnClickListener(onCompleteClickListener);
    }

    public View getErrorView() {
        return mErrorView;
    }

    public View getCompleteView() {
        return mCompleteView;
    }

    public View getLoadingView() {
        return mLoadingView;
    }

    public View getCustomView() {
        return mCustomView;
    }

    public StatusView setHideOnComplete(boolean hideOnComplete) {
        mHideOnComplete = hideOnComplete;
        return this;
    }

    /**
     * 设置自定义状态的标题
     *
     * @param id
     * @param title
     * @return
     */
    public View setCustomViewTitle(int id, String title) {
        View view = mCustomView.findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(title);
        }
        return view;
    }

    /**
     * 设置当前控件状态
     *
     * @param status
     */
    public void setStatus(final Status status) {
        setVisibility(VISIBLE);
        if (mCurrentStatus == Status.NONE) {
            mCurrentStatus = status;
            enterAnimation(getCurrentView(mCurrentStatus));
        } else if (status != Status.NONE) {
            switchAnimation(getCurrentView(mCurrentStatus), getCurrentView(status));
            mCurrentStatus = status;
        } else {
            exitAnimation(getCurrentView(mCurrentStatus));
        }

        mHandler.removeCallbacksAndMessages(null);
        if (status == Status.COMPLETE && mHideOnComplete) {
            mHandler.postDelayed(mAutoDismissOnComplete, DISMISS_ON_COMPLETE_DELAY);
        }
    }

    /**
     * 消失
     */
    public void dismiss() {
        setStatus(Status.NONE);
    }

    /**
     * 获取当前状态
     *
     * @return Status object
     */
    public Status getStatus() {
        return mCurrentStatus;
    }

    private View getCurrentView(Status status) {
        if (status == Status.NONE) {
            return null;
        } else if (status == Status.COMPLETE) {
            return mCompleteView;
        } else if (status == Status.ERROR) {
            return mErrorView;
        } else if (status == Status.LOADING) {
            return mLoadingView;
        } else if (status == Status.CUSTOM) {
            return mCustomView;
        } else {
            return null;
        }
    }

    private void switchAnimation(final View exitView, final View enterView) {
        clearAnimation();
        exitView.setVisibility(View.VISIBLE);
        exitView.startAnimation(mSlideOut);
        mSlideOut.setAnimationListener(new SimpleAnimListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mSlideOut.setAnimationListener(null);
                exitView.setVisibility(View.INVISIBLE);
                enterView.setVisibility(View.VISIBLE);
                enterView.startAnimation(mSlideIn);
            }
        });
    }

    private void enterAnimation(View enterView) {
        if (enterView == null) {
            return;
        }
        enterView.setVisibility(VISIBLE);
        enterView.startAnimation(mSlideIn);
    }

    private void exitAnimation(final View exitView) {
        if (exitView == null) {
            return;
        }

        exitView.startAnimation(mSlideOut);
        mSlideOut.setAnimationListener(new SimpleAnimListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mCurrentStatus = Status.NONE;
                exitView.setVisibility(INVISIBLE);
                mSlideOut.setAnimationListener(null);
                setVisibility(GONE);
            }
        });
    }
}
