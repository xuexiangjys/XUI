package com.xuexiang.xui.widget.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.banner.anim.select.ZoomInEnter;
import com.xuexiang.xui.widget.banner.transform.DepthTransformer;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleGuideBanner;

import java.util.ArrayList;
import java.util.List;

/**
 * 启动引导页
 *
 * @author xuexiang
 * @since 2018/11/27 下午4:49
 */
public abstract class BaseGuideActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WidgetUtils.requestFullScreen(this);

        setContentView(R.layout.xui_activity_guide);
        initGuideView(getGuidesResIdList(), getSkipClass());
        onCreateActivity();
    }

    /**
     * activity启动后的初始化
     */
    protected void onCreateActivity() {

    }

    /**
     * 获取引导页资源的ID集合
     *
     * @return
     */
    protected abstract List<Integer> getGuidesResIdList();

    /**
     * 获取跳转activity的类
     *
     * @return
     */
    protected abstract Class<? extends Activity> getSkipClass();

    /**
     * 初始化引导页动画
     *
     * @param guidesResIdList 引导图片
     * @param cls             点击后跳转的Activity类
     */
    public void initGuideView(List<Integer> guidesResIdList, final Class<?> cls) {
        initGuideView(guidesResIdList, DepthTransformer.class, cls);
    }

    /**
     * 初始化引导页动画
     *
     * @param guidesResIdList  引导图片
     * @param transformerClass 引导图片切换的效果
     * @param cls              点击后跳转的Activity类
     */
    public void initGuideView(List<Integer> guidesResIdList, Class<? extends ViewPager.PageTransformer> transformerClass, final Class<?> cls) {
        SimpleGuideBanner sgb = (SimpleGuideBanner) findViewById(R.id.sgb);

        sgb.setIndicatorWidth(6).setIndicatorHeight(6).setIndicatorGap(12)
                .setIndicatorCornerRadius(3.5f)
                .setSelectAnimClass(ZoomInEnter.class)
                .setTransformerClass(transformerClass).barPadding(0, 10, 0, 10)
                .setSource(guidesResIdList).startScroll();

        sgb.setOnJumpClickL(new SimpleGuideBanner.OnJumpClickL() {
            @Override
            public void onJumpClick() {
                startActivity(new Intent(BaseGuideActivity.this, cls));
                finish();
            }
        });
    }

    /**
     * 初始化引导页动画
     *
     * @param guidesResIdList  引导图片
     * @param transformerClass 引导图片切换的效果
     */
    public void initGuideView(ArrayList<Integer> guidesResIdList, Class<? extends ViewPager.PageTransformer> transformerClass, SimpleGuideBanner.OnJumpClickL onJumpClickListener) {
        SimpleGuideBanner sgb = (SimpleGuideBanner) findViewById(R.id.sgb);

        sgb.setIndicatorWidth(6).setIndicatorHeight(6).setIndicatorGap(12)
                .setIndicatorCornerRadius(3.5f)
                .setSelectAnimClass(ZoomInEnter.class)
                .setTransformerClass(transformerClass).barPadding(0, 10, 0, 10)
                .setSource(guidesResIdList).startScroll();

        sgb.setOnJumpClickL(onJumpClickListener);
    }
}
