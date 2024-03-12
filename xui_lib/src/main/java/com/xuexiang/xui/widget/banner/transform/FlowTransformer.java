package com.xuexiang.xui.widget.banner.transform;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

/**
 * 翻转切换
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:12
 */
public class FlowTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(@NonNull View page, float position) {
        page.setRotationY(position * -30f);
    }
}
