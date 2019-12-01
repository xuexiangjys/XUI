package com.xuexiang.xuidemo.activity;

import android.app.Activity;

import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.widget.activity.BaseGuideActivity;
import com.xuexiang.xuidemo.DemoDataProvider;

import java.util.List;

/**
 *  启动引导页
 *
 * @author xuexiang
 * @since 2018/11/28 上午12:52
 */
public class UserGuideActivity extends BaseGuideActivity {
    @Override
    protected List<Object> getGuideResourceList() {
        return DemoDataProvider.getUsertGuides();
    }

    @Override
    protected Class<? extends Activity> getSkipClass() {
        return LoginActivity.class;
    }

}
