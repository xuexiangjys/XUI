package com.xuexiang.xui.widget.banner.transform;

import androidx.viewpager.widget.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * 侧滑缩小切换
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:13
 */
public class ZoomOutSlideTransformer implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.9f;

    @Override
    public void transformPage(View page, float position) {
        if (position >= -1 || position <= 1) {
            // Modify the default slide transition to shrink the page as well
            final float height = page.getHeight();
            final float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            final float vertMargin = height * (1 - scaleFactor) / 2;
            final float horzMargin = page.getWidth() * (1 - scaleFactor) / 2;

            // Center vertically
            ViewHelper.setPivotY(page, 0.5f * height);


            if (position < 0) {
                ViewHelper.setTranslationX(page, horzMargin - vertMargin / 2);
            } else {
                ViewHelper.setTranslationX(page, -horzMargin + vertMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            ViewHelper.setScaleX(page, scaleFactor);
            ViewHelper.setScaleY(page, scaleFactor);

            // Fade the page relative to its size.
            ViewHelper.setAlpha(page, MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
        }
    }
}
