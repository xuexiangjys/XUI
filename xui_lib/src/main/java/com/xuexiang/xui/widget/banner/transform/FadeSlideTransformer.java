package com.xuexiang.xui.widget.banner.transform;

import androidx.viewpager.widget.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * 侧滑逐渐消失切换
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:10
 */
public class FadeSlideTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {

        ViewHelper.setTranslationX(page, 0);

        if (position <= -1.0F || position >= 1.0F) {
            ViewHelper.setAlpha(page, 0.0F);
        } else if (position == 0.0F) {
            ViewHelper.setAlpha(page, 1.0F);
        } else {
            // position is between -1.0F & 0.0F OR 0.0F & 1.0F
            ViewHelper.setAlpha(page, 1.0F - Math.abs(position));
        }
    }
}
