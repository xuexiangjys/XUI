package com.xuexiang.xuidemo.fragment.expands.materialdesign;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xutil.tip.ToastUtils;

import butterknife.BindView;

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
    }

    private void initToolbar1(){
        //设置NavigationIcon
        toolBar1.setNavigationIcon(R.drawable.ic_navigation_menu);
        // 设置 NavigationIcon 点击事件
        toolBar1.setNavigationOnClickListener(onClickListener);
        // 设置 toolbar 背景色
        toolBar1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        // 设置 Title
        toolBar1.setTitle(R.string.title_toolbar);
        //  设置Toolbar title文字颜色
        toolBar1.setTitleTextColor(getResources().getColor(R.color.white));
        // 设置Toolbar subTitle
        toolBar1.setSubtitle(R.string.title_toolbar_sub);
        toolBar1.setSubtitleTextColor(getResources().getColor(R.color.white));
        // 设置logo
        toolBar1.setLogo(R.mipmap.ic_launcher);

        //设置 Toolbar menu
        toolBar1.inflateMenu(R.menu.menu_setting);
        // 设置溢出菜单的图标
        toolBar1.setOverflowIcon(getResources().getDrawable(R.drawable.ic_navigation_more));
        // 设置menu item 点击事件
        toolBar1.setOnMenuItemClickListener(menuItemClickListener);
    }

    private void initToolbar2(){
        toolBar2.setNavigationOnClickListener(onClickListener);
        toolBar2.inflateMenu(R.menu.menu_setting);
        toolBar2.setOnMenuItemClickListener(menuItemClickListener);

    }

    private void initToolbar3(){
        toolBar3.setNavigationOnClickListener(onClickListener);
        toolBar3.inflateMenu(R.menu.menu_setting);
        toolBar3.setOnMenuItemClickListener(menuItemClickListener);
    }

    private void initToolbar4(){
        toolBar4.setNavigationOnClickListener(onClickListener);
        toolBar4.inflateMenu(R.menu.menu_search);
        toolBar4.setOnMenuItemClickListener(menuItemClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ToastUtils.toast("点击了NavigationIcon");
        }
    };

    Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            ToastUtils.toast("点击了:" + item.getTitle());
            switch (item.getItemId()){
                case R.id.item_setting:
                    //点击设置
                    break;
            }
            return false;
        }
    };
}
