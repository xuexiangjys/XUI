package com.xuexiang.xui.widget.banner.anim.select;

import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;
import com.xuexiang.xui.widget.banner.anim.BaseAnimator;

/**
 *
 *
 * @author xuexiang
 * @since 2018/11/25 下午6:51
 */
public class RotateEnter extends BaseAnimator {
    public RotateEnter() {
        this.mDuration = 200;
    }

    @Override
    public void setAnimation(View view) {
        this.mAnimatorSet.playTogether(ObjectAnimator.ofFloat(view, "rotation", 0, 180));
    }
}
