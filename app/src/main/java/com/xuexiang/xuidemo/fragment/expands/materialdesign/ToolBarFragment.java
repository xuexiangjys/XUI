package com.xuexiang.xuidemo.fragment.expands.materialdesign;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.widget.popupwindow.popup.XUISimplePopup;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xutil.display.DensityUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2019-05-08 00:13
 */
@Page(name = "ToolBar使用")
public class ToolBarFragment extends BaseFragment {
    @BindView(R.id.tool_bar1)
    Toolbar toolBar1;
    @BindView(R.id.tool_bar_2)
    Toolbar toolBar2;
    @BindView(R.id.tool_bar_3)
    Toolbar toolBar3;
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.tool_bar_4)
    Toolbar toolBar4;
    @BindView(R.id.ll_action)
    LinearLayout llAction;
    @BindView(R.id.tool_bar_5)
    Toolbar toolBar5;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_sub_title)
    TextView tvSubTitle;


    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_toolbar;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        initToolbar1();
        initToolbar2();
        initToolbar3();
        initToolbar4();
        initToolbar5();
    }

    private void initToolbar1() {
        //设置NavigationIcon
        toolBar1.setNavigationIcon(R.drawable.ic_navigation_menu);
        // 设置 NavigationIcon 点击事件
        toolBar1.setNavigationOnClickListener(onClickListener);
        toolBar1.setContentInsetStartWithNavigation(0);
        // 设置 toolbar 背景色
        toolBar1.setBackgroundColor(ResUtils.getColor(getContext(), R.color.colorPrimary));
        // 设置 Title
        toolBar1.setTitle(R.string.title_toolbar);
        //  设置Toolbar title文字颜色
        toolBar1.setTitleTextColor(ResUtils.getColor(getContext(), R.color.white));
        // 设置Toolbar subTitle
        toolBar1.setSubtitle(R.string.title_toolbar_sub);
        toolBar1.setSubtitleTextColor(ResUtils.getColor(getContext(), R.color.white));
        // 设置logo
        toolBar1.setLogo(R.mipmap.ic_launcher);

        //设置 Toolbar menu
        toolBar1.inflateMenu(R.menu.menu_custom);
        // 设置溢出菜单的图标
        toolBar1.setOverflowIcon(ResUtils.getDrawable(getContext(), R.drawable.ic_navigation_more));
        // 设置menu item 点击事件
        toolBar1.setOnMenuItemClickListener(menuItemClickListener);
    }

    private void initToolbar2() {
        toolBar2.setNavigationOnClickListener(onClickListener);
        toolBar2.inflateMenu(R.menu.menu_setting);
        toolBar2.setOnMenuItemClickListener(menuItemClickListener);

    }

    private void initToolbar3() {
        toolBar3.setNavigationOnClickListener(onClickListener);
        toolBar3.inflateMenu(R.menu.menu_setting);
        toolBar3.setOnMenuItemClickListener(menuItemClickListener);
    }

    private void initToolbar4() {
        toolBar4.setNavigationOnClickListener(onClickListener);
        toolBar4.inflateMenu(R.menu.menu_search);
        toolBar4.setOnMenuItemClickListener(menuItemClickListener);
    }

    private void initToolbar5() {
        toolBar5.setNavigationOnClickListener(onClickListener);
        toolBar5.setOnMenuItemClickListener(menuItemClickListener);
        tvTitle.setText("主页");
    }


    private View.OnClickListener onClickListener = v -> XToastUtils.toast("点击了NavigationIcon");

    Toolbar.OnMenuItemClickListener menuItemClickListener = item -> {
        XToastUtils.toast("点击了:" + item.getTitle());
        if (item.getItemId() == R.id.item_setting) {
            //点击设置
        }
        return false;
    };

    @OnClick(R.id.ll_action)
    public void onViewClicked(View view) {
        if (view.getId() == R.id.ll_action) {
            showSelectPopWindow(view);
        }
    }

    private void showSelectPopWindow(View view) {
        new XUISimplePopup<>(getContext(), ResUtils.getStringArray(getContext(), R.array.grid_titles_entry))
                .create(DensityUtils.dip2px(getContext(), 170), (adapter, item, position) -> {
                    ViewUtils.setText(tvSubTitle, String.format("<%s>", item));
                })
                .setHasDivider(true)
                .showDown(view);
    }
}
