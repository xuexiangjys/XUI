package com.xuexiang.xui.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.xuexiang.xui.R;

/**
 * 倒计时Button帮助类
 *
 * @author xuexiang
 * @since 2018/11/22 上午12:38
 */
public class CountDownButtonHelper {

    /**
     * 倒计时timer
     */
    private CountDownTimer mCountDownTimer;
    private OnCountDownListener mListener;

    private TextView mButton;
    /**
     * 倒计时总时间
     */
    private int mCountDownTime;
    /**
     * 倒计时间期
     */
    private int mInterval;

    /**
     * 构造方法
     * @param button        需要显示倒计时的Button
     * @param countDownTime 倒计时总时间,单位是秒
     */
    public CountDownButtonHelper(TextView button, int countDownTime) {
        this(button, countDownTime, 1);
    }

    /**
     * 构造方法
     * @param button        需要显示倒计时的Button
     * @param countDownTime 需要进行倒计时的最大值,单位是秒
     * @param interval      倒计时的间隔，单位是秒
     */
    public CountDownButtonHelper(TextView button, int countDownTime, int interval) {
        mButton = button;
        mCountDownTime = countDownTime;
        mInterval = interval;
        initCountDownTimer();
    }

    /**
     * 初始化倒计时器
     */
    private void initCountDownTimer() {
        // 由于CountDownTimer并不是准确计时，在onTick方法调用的时候，time会有1-10ms左右的误差，这会导致最后一秒不会调用onTick()
        // 因此，设置间隔的时候，默认减去了10ms，从而减去误差。
        // 经过以上的微调，最后一秒的显示时间会由于10ms延迟的积累，导致显示时间比1s长max*10ms的时间，其他时间的显示正常,总时间正常
        if (mCountDownTimer == null) {
            mCountDownTimer = new CountDownTimer(mCountDownTime * 1000, mInterval * 1000 - 10) {

                @Override
                public void onTick(long time) {
                    // 第一次调用会有1-10ms的误差，因此需要+15ms，防止第一个数不显示，第二个数显示2s
                    int surplusTime = (int)((time + 15) / 1000);
                    if (mListener != null) {
                        mListener.onCountDown(surplusTime);
                    } else {
                        mButton.setText(surplusTime + "s");
                    }
                }

                @Override
                public void onFinish() {
                    mButton.setEnabled(true);
                    if (mListener != null) {
                        mListener.onFinished();
                    } else {
                        mButton.setText(mButton.getResources().getString(R.string.xui_count_down_finish));
                    }
                }
            };
        }
    }

    /**
     * 开始倒计时
     */
    public void start() {
        initCountDownTimer();
        mButton.setEnabled(false);
        mCountDownTimer.start();
    }

    /**
     * 设置倒计时的监听器
     *
     * @param listener
     */
    public CountDownButtonHelper setOnCountDownListener(OnCountDownListener listener) {
        mListener = listener;
        return this;
    }

    /**
     * 计时时监听接口
     *
     * @author xx
     */
    public interface OnCountDownListener {
        /**
         * 正在倒计时
         * @param time 剩余的时间
         */
        void onCountDown(int time);

        /**
         * 倒计时结束
         */
        void onFinished();


    }


    /**
     * 取消倒计时
     */
    public void cancel() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    /**
     * 资源回收
     */
    public void recycle() {
        cancel();
        mListener = null;
        mButton = null;
    }
}
