package com.xuexiang.xui.widget.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.xuexiang.xui.R;

import java.util.Locale;

/**
 * 倒计时按钮
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:10
 */
public class CountDownButton extends AppCompatButton {

    /**
     * 默认时间间隔1000ms
     */
    private static final int DEFAULT_INTERVAL = 1000;
    /**
     * 默认时长60s
     */
    private static final int DEFAULT_COUNTDOWN_TIME = 60 * 1000;
    /**
     * 默认倒计时文字格式(显示秒数)
     */
    private static final String DEFAULT_COUNT_FORMAT = "%ds";
    /**
     * 默认按钮文字 {@link #getText()}
     */
    private String mDefaultText;
    /**
     * 倒计时时长，单位为毫秒
     */
    private long mCountDownTime;
    /**
     * 时间间隔，单位为毫秒
     */
    private long mInterval;
    /**
     * 倒计时文字格式
     */
    private String mCountDownFormat;
    /**
     * 倒计时是否可用
     */
    private boolean mEnableCountDown = true;
    /**
     * 点击事件监听器
     */
    private OnClickListener mOnClickListener;

    /**
     * 倒计时
     */
    private CountDownTimer mCountDownTimer;

    /**
     * 是否正在执行倒计时
     */
    private boolean isCountDownNow;

    public CountDownButton(Context context) {
        super(context);
    }

    public CountDownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CountDownButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // 获取自定义属性值
        initAttr(context, attrs);

        // 初始化倒计时Timer
        if (mCountDownTimer == null) {
            mCountDownTimer = new CountDownTimer(mCountDownTime, mInterval) {
                @Override
                public void onTick(long millisUntilFinished) {
                    setText(String.format(Locale.CHINA, mCountDownFormat, millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    isCountDownNow = false;
                    setEnabled(true);
                    setText(mDefaultText);
                }
            };
        }
    }

    /**
     * 获取自定义属性值
     *
     * @param context
     * @param attrs
     */
    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountDownButton);
        if (typedArray != null) {
            mCountDownFormat = typedArray.getString(R.styleable.CountDownButton_cdbt_countDownFormat);
            if (TextUtils.isEmpty(mCountDownFormat)) {
                mCountDownFormat = DEFAULT_COUNT_FORMAT;
            }
            mCountDownTime = typedArray.getInteger(R.styleable.CountDownButton_cdbt_countDown, DEFAULT_COUNTDOWN_TIME);
            mInterval = typedArray.getInteger(R.styleable.CountDownButton_cdbt_countDownInterval, DEFAULT_INTERVAL);
            mEnableCountDown = (mCountDownTime > mInterval) && typedArray.getBoolean(R.styleable.CountDownButton_cdbt_enableCountDown, true);
            typedArray.recycle();
        }
    }


    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        super.setOnClickListener(onClickListener);
        mOnClickListener = onClickListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                Rect rect = new Rect();
                getGlobalVisibleRect(rect);
                if (mOnClickListener != null && rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    mOnClickListener.onClick(this);
                }
                if (mEnableCountDown && rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    startCountDown();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 开始计算
     */
    private void startCountDown() {
        mDefaultText = getText().toString();
        // 设置按钮不可点击
        setEnabled(false);
        // 开始倒计时
        mCountDownTimer.start();
        isCountDownNow = true;
    }

    public CountDownButton setEnableCountDown(boolean enableCountDown) {
        mEnableCountDown = (mCountDownTime > mInterval) && enableCountDown;
        return this;
    }

    public CountDownButton setCountDownFormat(String countDownFormat) {
        mCountDownFormat = countDownFormat;
        return this;
    }

    public CountDownButton setCountDownTime(long countDownTime) {
        mCountDownTime = countDownTime;
        return this;
    }

    public CountDownButton setInterval(long interval) {
        mInterval = interval;
        return this;
    }

    /**
     * 是否正在执行倒计时
     *
     * @return 倒计时期间返回true否则返回false
     */
    public boolean isCountDownNow() {
        return isCountDownNow;
    }

    /**
     * 设置倒计时数据
     *
     * @param count           时长
     * @param interval        间隔
     * @param countDownFormat 文字格式
     */
    public CountDownButton setCountDown(long count, long interval, String countDownFormat) {
        mCountDownTime = count;
        mCountDownFormat = countDownFormat;
        mInterval = interval;
        setEnableCountDown(true);
        return this;
    }

    /**
     * 取消倒计时
     */
    public void cancelCountDown() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        isCountDownNow = false;
        setText(mDefaultText);
        setEnabled(true);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (isCountDownNow()) {
            return;
        }
        super.setEnabled(enabled);
        setClickable(enabled);
    }
}
