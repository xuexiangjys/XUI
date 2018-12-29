package com.xuexiang.xuidemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.xuexiang.xpage.base.XPageActivity;
import com.xuexiang.xui.XUI;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.fragment.ComponentsFragment;
import com.xuexiang.xuidemo.fragment.ExpandsFragment;
import com.xuexiang.xuidemo.fragment.UtilitysFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * 项目壳工程
 *
 * @author xuexiang
 * @since 2018/11/13 下午5:20
 */
public class MainActivity extends XPageActivity {

    Unbinder unbinder;

    @Override
    protected void attachBaseContext(Context newBase) {
        //注入字体
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(this);
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        openPage(ComponentsFragment.class);

        TabLayout.Tab component = mTabLayout.newTab();
        component.setText("组件");
        component.setIcon(R.drawable.selector_icon_tabbar_component);
        mTabLayout.addTab(component);

        TabLayout.Tab util = mTabLayout.newTab();
        util.setText("工具");
        util.setIcon(R.drawable.selector_icon_tabbar_util);
        mTabLayout.addTab(util);

        TabLayout.Tab expand = mTabLayout.newTab();
        expand.setText("拓展");
        expand.setIcon(R.drawable.selector_icon_tabbar_expand);
        mTabLayout.addTab(expand);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        openPage(ComponentsFragment.class);
                        break;
                    case 1:
                        openPage(UtilitysFragment.class);
                        break;
                    case 2:
                        openPage(ExpandsFragment.class);
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void switchTab(final boolean isShow) {
        Animation animation;
        if (isShow) {
            animation = new AlphaAnimation(0.2F, 1.0F);
        } else {
            animation = new AlphaAnimation(1.0F, 0.2F);
        }
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if (isShow) {
                    mTabLayout.setVisibility(View.VISIBLE);
                } else {
                    mTabLayout.setVisibility(View.GONE);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mTabLayout.startAnimation(animation);
    }

    @Override
    protected void onRelease() {
        unbinder.unbind();
        super.onRelease();
    }
}
