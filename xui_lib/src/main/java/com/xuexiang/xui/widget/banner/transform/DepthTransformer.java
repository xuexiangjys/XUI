package com.xuexiang.xui.widget.banner.transform;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * 3D旋转切换
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:11
 */
public class DepthTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE_DEPTH = 0.75f;

    @Override
    public void transformPage(View page, float position) {
        float alpha, scale, translationX;
        if (position > 0 && position < 1) {
            // moving to the right
            alpha = (1 - position);
            scale = MIN_SCALE_DEPTH + (1 - MIN_SCALE_DEPTH) * (1 - Math.abs(position));
            translationX = (page.getWidth() * -position);
        } else {
            // use default for all other cases
            alpha = 1;
            scale = 1;
            translationX = 0;
        }

        ViewHelper.setAlpha(page, alpha);
        ViewHelper.setTranslationX(page, translationX);
        ViewHelper.setScaleX(page, scale);
        ViewHelper.setScaleY(page, scale);
    }
}
