package com.xuexiang.xuidemo.fragment.expands.materialdesign.behavior;

import static com.google.android.material.tabs.TabLayout.MODE_FIXED;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.FragmentAdapter;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;

/**
 * @author XUE
 * @since 2019/5/9 11:43
 */
@Page(name = "TabLayout + ViewPager + AppBarLayout")
public class TabLayoutBehaviorFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected TitleBar initTitle() {
        toolbar.setTitle("TabLayout");
        toolbar.setNavigationOnClickListener(v -> popToBack());
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tablayout_behavior;
    }

    String[] titles = new String[]{"资讯", "娱乐", "教育"};

    @Override
    protected void initViews() {
        FragmentAdapter<BaseFragment> adapter = new FragmentAdapter<>(getChildFragmentManager());
        tabLayout.setTabMode(MODE_FIXED);
        for (String title : titles) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
            adapter.addFragment(new SimpleListFragment(), title);
        }
        viewPager.setOffscreenPageLimit(titles.length - 1);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        ViewUtils.setViewsFont(tabLayout);
    }

    @Override
    protected void initListeners() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    fab.show();
                } else {
                    fab.hide();
                }
            }
            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        fab.setOnClickListener(v -> SnackbarUtils.Long(v, "是否确认新建?")
                .setAction("是", v1 -> {

                }).show());
    }
}
