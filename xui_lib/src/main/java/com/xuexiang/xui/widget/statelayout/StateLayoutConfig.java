package com.xuexiang.xui.widget.statelayout;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.animation.Animation;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ResUtils;

/**
 * 多状态布局的配置信息
 *
 * @author xuexiang
 * @since 2018/11/26 上午12:12
 */
public class StateLayoutConfig {
    private static final boolean DEFAULT_ANIM_ENABLED = true;
    private static final int DEFAULT_IN_ANIM = android.R.anim.fade_in;
    private static final int DEFAULT_OUT_ANIM = android.R.anim.fade_out;
    /**
     * 页面状态切换是否启用动画
     */
    public boolean animationEnabled;
    /**
     * 进入动画
     */
    public Animation inAnimation;
    /**
     * 退出动画
     */
    public Animation outAnimation;

    /**
     * 暂无数据
     */
    public int emptyImageRes;
    public int emptyMessageRes;

    /**
     * 出错
     */
    public int errorImageRes;
    public int errorMessageRes;

    /**
     * 断网
     */
    public int offlineImageRes;
    public int offlineMessageRes;

    /**
     * 定位关闭
     */
    public int locationOffImageRes;
    public int locationOffMessageRes;

    /**
     * 重试按钮提示
     */
    public int retryMessageRes;
    /**
     * loading加载提示文字
     */
    public int loadingMessageRes;


    public StateLayoutConfig() {
        animationEnabled = DEFAULT_ANIM_ENABLED;
        inAnimation = ResUtils.getAnim(DEFAULT_IN_ANIM);
        outAnimation = ResUtils.getAnim(DEFAULT_OUT_ANIM);

        emptyImageRes = R.drawable.stf_ic_empty;
        emptyMessageRes = R.string.stfEmptyMessage;

        errorImageRes = R.drawable.stf_ic_error;
        errorMessageRes = R.string.stfErrorMessage;

        offlineImageRes = R.drawable.stf_ic_offline;
        offlineMessageRes = R.string.stfOfflineMessage;

        locationOffImageRes = R.drawable.stf_ic_location_off;
        locationOffMessageRes = R.string.stfLocationOffMessage;

        retryMessageRes = R.string.stfRetryButtonText;
        loadingMessageRes = R.string.stfLoadingMessage;

    }

    public boolean isAnimationEnabled() {
        return animationEnabled;
    }

    public StateLayoutConfig setAnimationEnabled(boolean animationEnabled) {
        this.animationEnabled = animationEnabled;
        return this;
    }

    public Animation getInAnimation() {
        return inAnimation;
    }

    public StateLayoutConfig setInAnimation(Animation inAnimation) {
        this.inAnimation = inAnimation;
        return this;
    }

    public Animation getOutAnimation() {
        return outAnimation;
    }

    public StateLayoutConfig setOutAnimation(Animation outAnimation) {
        this.outAnimation = outAnimation;
        return this;
    }

    public int getEmptyImageRes() {
        return emptyImageRes;
    }

    public StateLayoutConfig setEmptyImageRes(@DrawableRes int emptyImageRes) {
        this.emptyImageRes = emptyImageRes;
        return this;
    }

    public int getEmptyMessageRes() {
        return emptyMessageRes;
    }

    public StateLayoutConfig setEmptyMessageRes(@StringRes int emptyMessageRes) {
        this.emptyMessageRes = emptyMessageRes;
        return this;
    }

    public int getErrorImageRes() {
        return errorImageRes;
    }

    public StateLayoutConfig setErrorImageRes(@DrawableRes int errorImageRes) {
        this.errorImageRes = errorImageRes;
        return this;
    }

    public int getErrorMessageRes() {
        return errorMessageRes;
    }

    public StateLayoutConfig setErrorMessageRes(@StringRes int errorMessageRes) {
        this.errorMessageRes = errorMessageRes;
        return this;
    }

    public int getOfflineImageRes() {
        return offlineImageRes;
    }

    public StateLayoutConfig setOfflineImageRes(@DrawableRes int offlineImageRes) {
        this.offlineImageRes = offlineImageRes;
        return this;
    }

    public int getOfflineMessageRes() {
        return offlineMessageRes;
    }

    public StateLayoutConfig setOfflineMessageRes(@StringRes int offlineMessageRes) {
        this.offlineMessageRes = offlineMessageRes;
        return this;
    }

    public int getLocationOffImageRes() {
        return locationOffImageRes;
    }

    public StateLayoutConfig setLocationOffImageRes(@DrawableRes int locationOffImageRes) {
        this.locationOffImageRes = locationOffImageRes;
        return this;
    }

    public int getLocationOffMessageRes() {
        return locationOffMessageRes;
    }

    public StateLayoutConfig setLocationOffMessageRes(@StringRes int locationOffMessageRes) {
        this.locationOffMessageRes = locationOffMessageRes;
        return this;
    }

    public int getRetryMessageRes() {
        return retryMessageRes;
    }

    public StateLayoutConfig setRetryMessageRes(@StringRes int retryMessageRes) {
        this.retryMessageRes = retryMessageRes;
        return this;
    }

    public int getLoadingMessageRes() {
        return loadingMessageRes;
    }

    public StateLayoutConfig setLoadingMessageRes(@StringRes int loadingMessageRes) {
        this.loadingMessageRes = loadingMessageRes;
        return this;
    }
}
