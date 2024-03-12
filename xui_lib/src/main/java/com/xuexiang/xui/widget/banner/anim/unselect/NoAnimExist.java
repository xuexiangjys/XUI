package com.xuexiang.xui.widget.banner.anim.unselect;

import android.animation.ObjectAnimator;
import android.view.View;

import com.xuexiang.xui.widget.banner.anim.BaseAnimator;

public class NoAnimExist extends BaseAnimator {

    public NoAnimExist() {
        this.mDuration = 200;
    }

    @Override
    public void setAnimation(View view) {
        this.mAnimatorSet.playTogether(ObjectAnimator.ofFloat(view, "alpha", 1, 1));
    }
}
