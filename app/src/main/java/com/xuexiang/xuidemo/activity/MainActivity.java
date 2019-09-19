package com.xuexiang.xuidemo.activity;

import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.umeng.analytics.MobclickAgent;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.menu.DrawerAdapter;
import com.xuexiang.xuidemo.adapter.menu.DrawerItem;
import com.xuexiang.xuidemo.adapter.menu.SimpleItem;
import com.xuexiang.xuidemo.adapter.menu.SpaceItem;
import com.xuexiang.xuidemo.base.BaseActivity;
import com.xuexiang.xuidemo.fragment.AboutFragment;
import com.xuexiang.xuidemo.fragment.ComponentsFragment;
import com.xuexiang.xuidemo.fragment.ExpandsFragment;
import com.xuexiang.xuidemo.fragment.QRCodeFragment;
import com.xuexiang.xuidemo.fragment.SettingFragment;
import com.xuexiang.xuidemo.fragment.UtilitysFragment;
import com.xuexiang.xuidemo.utils.SettingSPUtils;
import com.xuexiang.xuidemo.utils.Utils;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.common.ClickUtils;
import com.xuexiang.xutil.system.DeviceUtils;
import com.xuexiang.xutil.tip.ToastUtils;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.callback.DragStateListener;

import java.util.Arrays;

import butterknife.BindView;

/**
 * 项目壳工程
 *
 * @author xuexiang
 * @since 2018/11/13 下午5:20
 */
public class MainActivity extends BaseActivity implements DrawerAdapter.OnItemSelectedListener, ClickUtils.OnClick2ExitListener {
    private static final int POS_COMPONENTS = 0;
    private static final int POS_UTILITYS = 1;
    private static final int POS_EXPANDS = 2;
    private static final int POS_ABOUT = 3;
    private static final int POS_LOGOUT = 5;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;


    private SlidingRootNav mSlidingRootNav;
    private LinearLayout mLLMenu;
    private String[] mMenuTitles;
    private Drawable[] mMenuIcons;
    private DrawerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //登记一下
        MobclickAgent.onProfileSignIn(DeviceUtils.getAndroidID());

        initSlidingMenu(savedInstanceState);

        initViews();
    }

    @Override
    protected boolean isSupportSlideBack() {
        return false;
    }

    private void initViews() {
        initTab();

        //静默检查版本更新
        Utils.checkUpdate(this, false);
    }

    /**
     * 初始化Tab
     */
    private void initTab() {
        TabLayout.Tab component = mTabLayout.newTab();
        component.setText("组件");
        component.setIcon(SettingSPUtils.getInstance().isUseCustomTheme() ? R.drawable.custom_selector_icon_tabbar_component : R.drawable.selector_icon_tabbar_component);
        mTabLayout.addTab(component);

        TabLayout.Tab util = mTabLayout.newTab();
        util.setText("工具");
        util.setIcon(SettingSPUtils.getInstance().isUseCustomTheme() ? R.drawable.custom_selector_icon_tabbar_util : R.drawable.selector_icon_tabbar_util);
        mTabLayout.addTab(util);

        TabLayout.Tab expand = mTabLayout.newTab();
        expand.setText("拓展");
        expand.setIcon(SettingSPUtils.getInstance().isUseCustomTheme() ? R.drawable.custom_selector_icon_tabbar_expand : R.drawable.selector_icon_tabbar_expand);
        mTabLayout.addTab(expand);

        switchPage(ComponentsFragment.class);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mAdapter.setSelected(tab.getPosition());
                switch (tab.getPosition()) {
                    case POS_COMPONENTS:
                        switchPage(ComponentsFragment.class);
                        break;
                    case 1:
                        switchPage(UtilitysFragment.class);
                        break;
                    case 2:
                        switchPage(ExpandsFragment.class);
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


    public void openMenu() {
        if (mSlidingRootNav != null) {
            mSlidingRootNav.openMenu();
        }
    }

    public void closeMenu() {
        if (mSlidingRootNav != null) {
            mSlidingRootNav.closeMenu();
        }
    }

    public boolean isMenuOpen() {
        if (mSlidingRootNav != null) {
            return mSlidingRootNav.isMenuOpened();
        }
        return false;
    }

    private void initSlidingMenu(Bundle savedInstanceState) {
        mMenuTitles = loadMenuTitles();
        mMenuIcons = loadMenuIcons();

        mSlidingRootNav = new SlidingRootNavBuilder(this)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        mLLMenu = mSlidingRootNav.getLayout().findViewById(R.id.ll_menu);
        mSlidingRootNav.getLayout().findViewById(R.id.iv_qrcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewPage(QRCodeFragment.class);
            }
        });
        mSlidingRootNav.getLayout().findViewById(R.id.iv_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewPage(SettingFragment.class);
            }
        });

        mAdapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_COMPONENTS).setChecked(true),
                createItemFor(POS_UTILITYS),
                createItemFor(POS_EXPANDS),
                createItemFor(POS_ABOUT),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)));
        mAdapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(mAdapter);

        mAdapter.setSelected(POS_COMPONENTS);
        mSlidingRootNav.setMenuLocked(false);
        mSlidingRootNav.getLayout().addDragStateListener(new DragStateListener() {
            @Override
            public void onDragStart() {

            }

            @Override
            public void onDragEnd(boolean isMenuOpened) {

            }
        });
    }

    @Override
    public void onItemSelected(int position) {
        switch (position) {
            case POS_COMPONENTS:
            case POS_UTILITYS:
            case POS_EXPANDS:
                if (mTabLayout != null) {
                    TabLayout.Tab tab = mTabLayout.getTabAt(position);
                    if (tab != null) {
                        tab.select();
                    }
                }
                mSlidingRootNav.closeMenu();
                break;
            case POS_ABOUT:
                openNewPage(AboutFragment.class);
                break;
            case POS_LOGOUT:
                DialogLoader.getInstance().showConfirmDialog(
                        this,
                        getString(R.string.lab_logout_confirm),
                        getString(R.string.lab_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                MobclickAgent.onProfileSignOff();
                                finish();
                            }
                        },
                        getString(R.string.lab_no),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );
                break;
            default:
                break;
        }
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(mMenuIcons[position], mMenuTitles[position])
                .withIconTint(ThemeUtils.resolveColor(this, R.attr.xui_config_color_content_text))
                .withTextTint(ThemeUtils.resolveColor(this, R.attr.xui_config_color_content_text))
                .withSelectedIconTint(ThemeUtils.resolveColor(this, R.attr.colorAccent))
                .withSelectedTextTint(ThemeUtils.resolveColor(this, R.attr.colorAccent));
    }

    private String[] loadMenuTitles() {
        return getResources().getStringArray(R.array.menu_titles);
    }

    private Drawable[] loadMenuIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.menu_icons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isMenuOpen()) {
                closeMenu();
            } else {
                ClickUtils.exitBy2Click(2000, this);
            }
        }
        return true;
    }

    /**
     * 再点击一次
     */
    @Override
    public void onRetry() {
        ToastUtils.toast("再按一次退出程序");
    }

    /**
     * 退出
     */
    @Override
    public void onExit() {
        moveTaskToBack(true);
    }
}
