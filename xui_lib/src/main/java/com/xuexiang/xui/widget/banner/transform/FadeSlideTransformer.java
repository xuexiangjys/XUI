package com.xuexiang.xui.widget.banner.transform;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import android.view.View;

/**
 * 侧滑逐渐消失切换
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:10
 */
public class FadeSlideTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {

        page.setTranslationX(0);

        if (position <= -1.0F || position >= 1.0F) {
            page.setAlpha(0.0F);
        } else if (position == 0.0F) {
            page.setAlpha(1.0F);
        } else {
            // position is between -1.0F & 0.0F OR 0.0F & 1.0F
            page.setAlpha(1.0F - Math.abs(position));
        }
    }
}
