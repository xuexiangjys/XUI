package com.xuexiang.xuidemo.base;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.xuexiang.xpage.base.XPageActivity;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.core.CoreSwitchBean;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.slideback.SlideBack;
import com.xuexiang.xuidemo.utils.Utils;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

/**
 * 基础容器Activity
 *
 * @author XUE
 * @since 2019/3/22 11:21
 */
public class BaseActivity extends XPageActivity {

    /**
     * 是否支持侧滑返回
     */
    public static final String KEY_SUPPORT_SLIDE_BACK = "key_support_slide_back";

    Unbinder mUnbinder;

    @Override
    protected void attachBaseContext(Context newBase) {
        //注入字体
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initAppTheme();
        initStatusBarStyle();
        initOthers();
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);

        registerSlideBack();
    }

    /**
     * 初始化应用的主题
     */
    protected void initAppTheme() {
        Utils.initTheme(this);
    }

    /**
     * 初始化状态栏的样式
     */
    protected void initStatusBarStyle() {

    }

    /**
     * 初始化其他事务
     */
    private void initOthers() {
        // 用于检测控件的加载速度
//        WidgetUtils.installLayoutInflaterLogger(this);
    }

    /**
     * 打开fragment
     *
     * @param clazz          页面类
     * @param addToBackStack 是否添加到栈中
     * @return 打开的fragment对象
     */
    public <T extends XPageFragment> T openPage(Class<T> clazz, boolean addToBackStack) {
        CoreSwitchBean page = new CoreSwitchBean(clazz)
                .setAddToBackStack(addToBackStack);
        return (T) openPage(page);
    }

    /**
     * 打开fragment
     *
     * @return 打开的fragment对象
     */
    public <T extends XPageFragment> T openNewPage(Class<T> clazz) {
        CoreSwitchBean page = new CoreSwitchBean(clazz)
                .setNewActivity(true);
        return (T) openPage(page);
    }


    /**
     * 切换fragment
     *
     * @param clazz 页面类
     * @return 打开的fragment对象
     */
    public <T extends XPageFragment> T switchPage(Class<T> clazz) {
        return changePage(clazz);
    }

    @Override
    protected void onRelease() {
        mUnbinder.unbind();
        unregisterSlideBack();
        super.onRelease();
    }

    /**
     * @return 是否支持侧滑返回
     */
    protected boolean isSupportSlideBack() {
        CoreSwitchBean page = getIntent().getParcelableExtra(CoreSwitchBean.KEY_SWITCH_BEAN);
        return page == null || page.getBundle() == null || page.getBundle().getBoolean(KEY_SUPPORT_SLIDE_BACK, true);
    }

    /**
     * 注册侧滑回调
     */
    protected void registerSlideBack() {
        if (isSupportSlideBack()) {
            SlideBack.withFixSize(this)
                    .haveScroll(true)
                    .edgeMode(ResUtils.isRtl(this) ? SlideBack.EDGE_RIGHT : SlideBack.EDGE_LEFT)
                    .callBack(this::popPage)
                    .register();
        }
    }

    /**
     * 注销侧滑回调
     */
    protected void unregisterSlideBack() {
        if (isSupportSlideBack()) {
            SlideBack.unregister(this);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        resetSlideBack();
    }

    private void resetSlideBack() {
        unregisterSlideBack();
        registerSlideBack();
    }
}
