package com.xuexiang.xui.widget.banner.transform;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * 向上旋转切换
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:15
 */
public class RotateUpTransformer implements ViewPager.PageTransformer {

	private static final float ROT_MOD = -15f;

	@Override
	public void transformPage(View page, float position) {
		final float width = page.getWidth();
		final float rotation = ROT_MOD * position;

		ViewHelper.setPivotX(page,width * 0.5f);
        ViewHelper.setPivotY(page,0f);
        ViewHelper.setTranslationX(page,0f);
        ViewHelper.setRotation(page,rotation);
	}
}
