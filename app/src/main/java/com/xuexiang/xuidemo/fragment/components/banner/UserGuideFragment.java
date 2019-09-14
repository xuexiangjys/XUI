package com.xuexiang.xuidemo.fragment.components.banner;

import android.support.v4.view.ViewPager;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xrouter.annotation.AutoWired;
import com.xuexiang.xrouter.launcher.XRouter;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.banner.anim.select.ZoomInEnter;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleGuideBanner;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import static com.xuexiang.xuidemo.fragment.components.banner.UserGuideFragment.POSITION;


/**
 * 可使用Applink打开:https://xuexiangjys.club/xpage/transfer?pageName=UserGuide&position=2
 *
 * @author xuexiang
 * @since 2019-07-06 10:21
 */
@Page(name = "UserGuide", params = {POSITION})
public class UserGuideFragment extends BaseFragment {

    public final static String POSITION = "position";

    private Class<? extends ViewPager.PageTransformer> transformerClass;

    @AutoWired
    int position;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_guide;
    }

    @Override
    protected void initArgs() {
        XRouter.getInstance().inject(this);

        if (position >= 0 && position <= DemoDataProvider.transformers.length - 1) {
            transformerClass = DemoDataProvider.transformers[position];
        } else {
            transformerClass = DemoDataProvider.transformers[0];
        }
    }

    @Override
    protected TitleBar initTitle() {
        return null;
    }

    @Override
    protected void initViews() {
        sgb();
    }


    @Override
    protected void initListeners() {

    }


    private void sgb() {
        SimpleGuideBanner sgb = findViewById(R.id.sgb);

        sgb
                .setIndicatorWidth(6)
                .setIndicatorHeight(6)
                .setIndicatorGap(12)
                .setIndicatorCornerRadius(3.5f)
                .setSelectAnimClass(ZoomInEnter.class)
                .setTransformerClass(transformerClass)
                .barPadding(0, 10, 0, 10)
                .setSource(DemoDataProvider.getUsertGuides())
                .startScroll();

        sgb.setOnJumpClickListener(new SimpleGuideBanner.OnJumpClickListener() {
            @Override
            public void onJumpClick() {
                popToBack();
            }
        });
    }
}
