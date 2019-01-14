package com.xuexiang.xui.widget.textview.marqueen;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.xuexiang.xui.R;

import java.util.List;

/**
 * 文字轮播控件【类似字幕、头条之类】
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:03
 */
public class MarqueeView extends ViewFlipper {
    /**
     * Item翻页时间间隔【mInterval 必须大于 mAnimDuration，否则视图将会重叠】
     */
    private int mInterval = 2500;
    /**
     * Item动画执行时间
     */
    private int mAnimDuration = 500;
    //进出动画
    private Animation mAnimIn, mAnimOut;
    private int mAnimInRes = R.anim.marquee_bottom_in;
    private int mAnimOutRes = R.anim.marquee_top_out;

    public MarqueeView(Context context) {
        super(context);
        init(null);
    }

    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MarqueeView, 0, 0);
        mInterval = a.getInt(R.styleable.MarqueeView_mq_interval, mInterval);
        mAnimInRes = a.getResourceId(R.styleable.MarqueeView_mq_animIn, mAnimInRes);
        mAnimOutRes = a.getResourceId(R.styleable.MarqueeView_mq_animOut, mAnimOutRes);
        mAnimDuration = a.getInt(R.styleable.MarqueeView_mq_animDuration, mAnimDuration);
        a.recycle();

        setFlipInterval(mInterval);
        mAnimIn = AnimationUtils.loadAnimation(getContext(), mAnimInRes);
        mAnimIn.setDuration(mAnimDuration);
        setInAnimation(mAnimIn);
        mAnimOut = AnimationUtils.loadAnimation(getContext(), mAnimOutRes);
        mAnimOut.setDuration(mAnimDuration);
        setOutAnimation(mAnimOut);
    }

    /**
     * 设置滚动数据的工厂
     * @param factory
     */
    public void setMarqueeFactory(MarqueeFactory factory) {
        factory.setAttachedToMarqueeView(this);
        removeAllViews();
        List<View> mViews = factory.getMarqueeViews();
        if (mViews != null) {
            for (int i = 0; i < mViews.size(); i++) {
                addView(mViews.get(i));
            }
        }
    }

    /**
     * 设置翻页时间间隔
     * @param interval
     */
    public void setInterval(int interval) {
        mInterval = interval;
        setFlipInterval(interval);
    }

    /**
     * 设置动画执行时间
     * @param animDuration
     */
    public void setAnimDuration(int animDuration) {
        mAnimDuration = animDuration;
        mAnimIn.setDuration(animDuration);
        setInAnimation(mAnimIn);
        mAnimOut.setDuration(animDuration);
        setOutAnimation(mAnimOut);
    }

    public void setAnimInAndOut(Animation animIn, Animation animOut) {
        mAnimIn = animIn;
        mAnimOut = animOut;
        setInAnimation(animIn);
        setOutAnimation(animOut);
    }

    public void setAnimInAndOut(int animInId, int animOutID) {
        mAnimIn = AnimationUtils.loadAnimation(getContext(), animInId);
        mAnimOut = AnimationUtils.loadAnimation(getContext(), animOutID);
        mAnimIn.setDuration(mAnimDuration);
        mAnimOut.setDuration(mAnimDuration);
        setInAnimation(mAnimIn);
        setOutAnimation(mAnimOut);

    }

    public Animation getAnimIn() {
        return mAnimIn;
    }

    public Animation getAnimOut() {
        return mAnimOut;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopFlipping();
    }
}
