package com.xuexiang.xui.widget.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.xuexiang.xui.utils.Utils;

/**
 * 基础启动页
 *
 * @author xuexiang
 * @since 2018/11/27 下午4:49
 */
public abstract class BaseSplashActivity extends AppCompatActivity {
    /**
     * 默认启动页过渡时间
     */
    private static final int DEFAULT_SPLASH_DURATION_MILLIS = 2000;

    protected LinearLayout mWelcomeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initSplashView(getSplashImgResId());
        onCreateActivity();
    }

    private void initView() {
        mWelcomeLayout = new LinearLayout(this);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mWelcomeLayout.setLayoutParams(params);
        mWelcomeLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(mWelcomeLayout);
    }

    /**
     * 初始化启动界面
     *
     * @param splashImgResId 背景资源图片资源ID
     */
    protected void initSplashView(int splashImgResId) {
        if (splashImgResId != 0) {
            Utils.setBackground(this, mWelcomeLayout, splashImgResId);
        }
    }

    /**
     * 初始化启动界面背景图片
     *
     * @return 背景图片资源ID
     */
    protected int getSplashImgResId() {
        return 0;
    }

    /**
     * activity启动后的初始化
     */
    protected abstract void onCreateActivity();

    /**
     * 启动页结束后的动作
     */
    protected abstract void onSplashFinished();

    /**
     * @return 启动页持续的时间
     */
    protected long getSplashDurationMillis() {
        return DEFAULT_SPLASH_DURATION_MILLIS;
    }

    /**
     * 开启过渡
     *
     * @param enableAlphaAnim 是否启用渐近动画
     */
    protected void startSplash(boolean enableAlphaAnim) {
        if (enableAlphaAnim) {
            startSplashAnim(new AlphaAnimation(0.2F, 1.0F));
        } else {
            startSplashAnim(new AlphaAnimation(1.0F, 1.0F));
        }
    }

    /**
     * 开启引导过渡动画
     *
     * @param anim
     */
    private void startSplashAnim(Animation anim) {
        Utils.checkNull(anim, "Splash Animation can not be null");
        anim.setDuration(getSplashDurationMillis());
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                onSplashFinished();
            }
        });
        mWelcomeLayout.startAnimation(anim);
    }

    @Override
    protected void onDestroy() {
        Utils.recycleBackground(mWelcomeLayout);
        super.onDestroy();
    }
}
