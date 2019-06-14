package com.xuexiang.xuidemo.base;

import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.umeng.analytics.MobclickAgent;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.core.PageOption;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.actionbar.TitleUtils;
import com.xuexiang.xutil.net.JsonUtil;

/**
 * @author xuexiang
 * @since 2018/5/25 下午3:44
 */
public abstract class BaseFragment extends XPageFragment {

    @Override
    protected void initPage() {
        initTitle();
        initViews();
        initListeners();
    }

    protected TitleBar initTitle() {
        return TitleUtils.addTitleBarDynamic((ViewGroup) getRootView(), getPageTitle(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popToBack();
            }
        });
    }

    @Override
    protected void initListeners() {

    }

    /**
     * 打开一个新的页面
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends XPageFragment> Fragment openNewPage(Class<T> clazz) {
        return new PageOption(clazz)
                .setNewActivity(true)
                .open(this);
    }

    /**
     * 打开一个新的页面
     *
     * @param name
     * @param <T>
     * @return
     */
    public <T extends XPageFragment> Fragment openNewPage(String name) {
        return new PageOption(name)
                .setAnim(CoreAnim.slide)
                .setNewActivity(true)
                .open(this);
    }

    /**
     * 打开一个新的页面
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends XPageFragment> Fragment openNewPage(Class<T> clazz, String key, Object value) {
        return new PageOption(clazz)
                .setNewActivity(true)
                .putString(key, JsonUtil.toJson(value))
                .open(this);
    }

    /**
     * 打开页面,需要结果返回
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends XPageFragment> Fragment openPageForResult(Class<T> clazz, String key, Object value, int requestCode) {
        return new PageOption(clazz)
                .setRequestCode(requestCode)
                .putString(key, JsonUtil.toJson(value))
                .open(this);
    }

    @Override
    public void onDestroyView() {
//        KeyboardUtils.fixSoftInputLeaks(getContext());
        super.onDestroyView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //屏幕旋转时刷新一下title
        super.onConfigurationChanged(newConfig);
        ViewGroup root = (ViewGroup) getRootView();
        if (root.getChildAt(0) instanceof TitleBar) {
            root.removeViewAt(0);
            initTitle();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getPageTitle());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getPageTitle());
    }
}
