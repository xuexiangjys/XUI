package com.xuexiang.xui.widget.statelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AnimRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.UIConfig;

/**
 * Android layout to show most common state templates like loading, empty, error etc. To do that all you need to is
 * wrap the target area(view) with StatefulLayout. For more information about usage look
 * <a href="https://github.com/gturedi/StatefulLayout#usage">here</a>
 */
public class StatefulLayout extends LinearLayout {

    private static final String MSG_ONE_CHILD = "StatefulLayout must have one child!";

    /**
     * Indicates whether to place the animation on state changes
     */
    private boolean animationEnabled;
    /**
     * Animation started begin of state change
     */
    private Animation inAnimation;
    /**
     * Animation started end of state change
     */
    private Animation outAnimation;
    /**
     * to synchronize transition animations when animation duration shorter then request of state change
     */
    private int animCounter;

    private View content;
    private LinearLayout stContainer;
    private ProgressBar stProgress;
    private ImageView stImage;
    private TextView stMessage;
    private Button stButton;

    public StatefulLayout(Context context) {
        super(context);
        initAttrs(context, null);
    }

    public StatefulLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StatefulLayout, 0, 0);
        animationEnabled = array.getBoolean(R.styleable.StatefulLayout_stf_animationEnabled, UIConfig.getInstance().getStateLayoutConfig().animationEnabled);
        int inAnimationResId = array.getResourceId(R.styleable.StatefulLayout_stf_inAnimation, -1);
        if (inAnimationResId != -1) {
            inAnimation = anim(inAnimationResId);
        } else {
            inAnimation = UIConfig.getInstance().getStateLayoutConfig().inAnimation;
        }
        int outAnimationResId = array.getResourceId(R.styleable.StatefulLayout_stf_outAnimation, -1);
        if (inAnimationResId != -1) {
            outAnimation = anim(outAnimationResId);
        } else {
            outAnimation = UIConfig.getInstance().getStateLayoutConfig().outAnimation;
        }
        array.recycle();
    }

    public boolean isAnimationEnabled() {
        return animationEnabled;
    }

    public StatefulLayout setAnimationEnabled(boolean animationEnabled) {
        this.animationEnabled = animationEnabled;
        return this;
    }

    public Animation getInAnimation() {
        return inAnimation;
    }

    public StatefulLayout setInAnimation(Animation animation) {
        inAnimation = animation;
        return this;
    }

    public StatefulLayout setInAnimation(@AnimRes int anim) {
        inAnimation = anim(anim);
        return this;
    }

    public Animation getOutAnimation() {
        return outAnimation;
    }

    public StatefulLayout setOutAnimation(Animation animation) {
        outAnimation = animation;
        return this;
    }

    public StatefulLayout setOutAnimation(@AnimRes int anim) {
        outAnimation = anim(anim);
        return this;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) throw new IllegalStateException(MSG_ONE_CHILD);
        if (isInEditMode()) return; // hide state views in designer
        setOrientation(VERTICAL);
        content = getChildAt(0); // assume first child as content
        LayoutInflater.from(getContext()).inflate(R.layout.xui_layout_stateful_template, this, true);
        stContainer = (LinearLayout) findViewById(R.id.stContainer);
        stProgress = (ProgressBar) findViewById(R.id.stProgress);
        stImage = (ImageView) findViewById(R.id.stImage);
        stMessage = (TextView) findViewById(R.id.stMessage);
        stButton = (Button) findViewById(R.id.stButton);
    }

    // content //

    /**
     * 显示正文布局
     */
    public void showContent() {
        if (isAnimationEnabled()) {
            stContainer.clearAnimation();
            content.clearAnimation();
            final int animCounterCopy = ++animCounter;
            if (stContainer.getVisibility() == VISIBLE) {
                outAnimation.setAnimationListener(new CustomAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (animCounter != animCounterCopy) return;
                        stContainer.setVisibility(GONE);
                        content.setVisibility(VISIBLE);
                        content.startAnimation(inAnimation);
                    }
                });
                stContainer.startAnimation(outAnimation);
            }
        } else {
            stContainer.setVisibility(GONE);
            content.setVisibility(VISIBLE);
        }
    }

    // loading //

    /**
     * 显示加载中布局
     */
    public void showLoading() {
        showLoading(UIConfig.getInstance().getStateLayoutConfig().loadingMessageRes);
    }

    public void showLoading(@StringRes int resId) {
        showLoading(str(resId));
    }

    public void showLoading(String message) {
        showCustom(new CustomStateOptions()
                .message(message)
                .loading());
    }

    // empty //

    /**
     * 显示空内容布局
     */
    public void showEmpty() {
        showEmpty(UIConfig.getInstance().getStateLayoutConfig().emptyMessageRes);
    }

    public void showEmpty(@StringRes int resId) {
        showEmpty(str(resId));
    }

    public void showEmpty(String message) {
        showCustom(new CustomStateOptions()
                .message(message)
                .image(UIConfig.getInstance().getStateLayoutConfig().emptyImageRes));
    }

    // error //

    /**
     * 显示出错布局
     * @param clickListener
     */
    public void showError(OnClickListener clickListener) {
        showError(UIConfig.getInstance().getStateLayoutConfig().errorMessageRes, clickListener);
    }

    public void showError(@StringRes int resId, OnClickListener clickListener) {
        showError(str(resId), clickListener);
    }

    public void showError(String message, OnClickListener clickListener) {
        showError(message, str(UIConfig.getInstance().getStateLayoutConfig().retryMessageRes), clickListener);
    }

    public void showError(String message, String buttonText, OnClickListener clickListener) {
        showCustom(new CustomStateOptions()
                .message(message)
                .image(UIConfig.getInstance().getStateLayoutConfig().errorImageRes)
                .buttonText(buttonText)
                .buttonClickListener(clickListener));
    }

    // offline

    /**
     * 显示离线布局（网络异常）
     * @param clickListener
     */
    public void showOffline(OnClickListener clickListener) {
        showOffline(UIConfig.getInstance().getStateLayoutConfig().offlineMessageRes, clickListener);
    }

    public void showOffline(@StringRes int resId, OnClickListener clickListener) {
        showOffline(str(resId), clickListener);
    }

    public void showOffline(String message, OnClickListener clickListener) {
        showOffline(message, str(UIConfig.getInstance().getStateLayoutConfig().retryMessageRes), clickListener);
    }

    public void showOffline(String message, String buttonText, OnClickListener clickListener) {
        showCustom(new CustomStateOptions()
                .message(message)
                .image(UIConfig.getInstance().getStateLayoutConfig().offlineImageRes)
                .buttonText(buttonText)
                .buttonClickListener(clickListener));
    }

    // location off //

    /**
     * 显示定位未打开
     * @param clickListener
     */
    public void showLocationOff(OnClickListener clickListener) {
        showLocationOff(UIConfig.getInstance().getStateLayoutConfig().locationOffMessageRes, clickListener);
    }

    public void showLocationOff(@StringRes int resId, OnClickListener clickListener) {
        showLocationOff(str(resId), clickListener);
    }

    public void showLocationOff(String message, OnClickListener clickListener) {
        showLocationOff(message, str(UIConfig.getInstance().getStateLayoutConfig().retryMessageRes), clickListener);
    }

    public void showLocationOff(String message, String buttonText, OnClickListener clickListener) {
        showCustom(new CustomStateOptions()
                .message(message)
                .image(UIConfig.getInstance().getStateLayoutConfig().locationOffImageRes)
                .buttonText(buttonText)
                .buttonClickListener(clickListener));
    }

    // custom //

    /**
     * 显示自定义布局
     * Shows custom state for given options. If you do not set buttonClickListener, the button will not be shown
     *
     * @param options customization options
     * @see com.xuexiang.xui.widget.statelayout.CustomStateOptions
     */
    public void showCustom(final CustomStateOptions options) {
        if (isAnimationEnabled()) {
            stContainer.clearAnimation();
            content.clearAnimation();
            final int animCounterCopy = ++animCounter;
            if (stContainer.getVisibility() == GONE) {
                outAnimation.setAnimationListener(new CustomAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (animCounterCopy != animCounter) return;
                        content.setVisibility(GONE);
                        stContainer.setVisibility(VISIBLE);
                        stContainer.startAnimation(inAnimation);
                    }
                });
                content.startAnimation(outAnimation);
                state(options);
            } else {
                outAnimation.setAnimationListener(new CustomAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (animCounterCopy != animCounter)
                            return;
                        state(options);
                        stContainer.startAnimation(inAnimation);
                    }
                });
                stContainer.startAnimation(outAnimation);
            }
        } else {
            content.setVisibility(GONE);
            stContainer.setVisibility(VISIBLE);
            state(options);
        }
    }

    // helper methods //

    private void state(CustomStateOptions options) {
        if (!TextUtils.isEmpty(options.getMessage())) {
            stMessage.setVisibility(VISIBLE);
            stMessage.setText(options.getMessage());
        } else {
            stMessage.setVisibility(GONE);
        }

        if (options.isLoading()) {
            stProgress.setVisibility(VISIBLE);
            stImage.setVisibility(GONE);
            stButton.setVisibility(GONE);
        } else {
            stProgress.setVisibility(GONE);
            if (options.getImageRes() != 0) {
                stImage.setVisibility(VISIBLE);
                stImage.setImageResource(options.getImageRes());
            } else {
                stImage.setVisibility(GONE);
            }

            if (options.getClickListener() != null) {
                stButton.setVisibility(VISIBLE);
                stButton.setOnClickListener(options.getClickListener());
                if (!TextUtils.isEmpty(options.getButtonText())) {
                    stButton.setText(options.getButtonText());
                }
            } else {
                stButton.setVisibility(GONE);
            }
        }
    }

    private String str(@StringRes int resId) {
        return getContext().getString(resId);
    }

    private Animation anim(@AnimRes int resId) {
        return AnimationUtils.loadAnimation(getContext(), resId);
    }

}
