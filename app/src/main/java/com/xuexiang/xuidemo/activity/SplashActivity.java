package com.xuexiang.xuidemo.activity;

import android.view.KeyEvent;

import com.xuexiang.xui.widget.activity.BaseSplashActivity;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.utils.SettingSPUtils;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.system.KeyboardUtils;

/**
 * 渐近式启动页
 *
 * @author xuexiang
 * @since 2018/11/27 下午5:24
 */
public class SplashActivity extends BaseSplashActivity {

    public final static String KEY_IS_DISPLAY = "is_display";

    private boolean isDisplay = false;

    @Override
    public void onCreateActivity() {
        isDisplay = getIntent().getBooleanExtra(KEY_IS_DISPLAY, isDisplay);
        SettingSPUtils spUtil = SettingSPUtils.getInstance();
        if (spUtil.isFirstOpen()) {
            spUtil.setIsFirstOpen(false);
            ActivityUtils.startActivity(UserGuideActivity.class);
            finish();

        }  else {
            initSplashView(R.drawable.xui_config_bg_splash);
            startSplash(false);
        }
    }

    @Override
    public void onSplashFinished() {
        if (!isDisplay) {
            ActivityUtils.startActivity(MainActivity.class);
        }
        finish();
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return KeyboardUtils.onDisableBackKeyDown(keyCode) && super.onKeyDown(keyCode, event);
    }
}
