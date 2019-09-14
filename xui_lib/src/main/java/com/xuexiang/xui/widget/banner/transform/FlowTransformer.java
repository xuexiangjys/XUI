package com.xuexiang.xui.widget.banner.transform;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

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
