package com.xuexiang.xuidemo.fragment.expands.floatview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.xuexiang.xaop.annotation.IOThread;
import com.xuexiang.xfloatview.XFloatView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xutil.app.PackageUtils;

/**
 * 应用切换悬浮窗
 *
 * @author xuexiang
 * @since 2019/1/21 上午11:53
 */
public class AppSwitchView extends XFloatView {
    /**
     * 应用名
     */
    private TextView mTvAppName;
    /**
     * 包名
     */
    private TextView mTvPackageName;

    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    /**
     * 构造器
     *
     * @param context
     */
    public AppSwitchView(Context context) {
        super(context);
    }

    /**
     * @return 获取根布局的ID
     */
    @Override
    protected int getLayoutId() {
        return R.layout.layout_float_view;
    }

    /**
     * @return 能否移动或者触摸响应
     */
    @Override
    protected boolean canMoveOrTouch() {
        return true;
    }

    /**
     * 初始化悬浮控件
     */
    @Override
    protected void initFloatView() {
        mTvAppName = findViewById(R.id.tv_app_name);
        mTvPackageName = findViewById(R.id.tv_package_name);
    }

    /**
     * 初始化监听
     */
    @Override
    protected void initListener() {
        setOnFloatViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageUtils.openApp(getContext());
            }
        });
    }

    /**
     * 更新包名
     *
     * @param appName
     * @param packageName
     */
    @IOThread
    public void updateAppInfo(final String appName, final String packageName) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            mTvAppName.setText(String.format("应用：%s", appName));
            mTvPackageName.setText(String.format("包名：%s", packageName));
        } else {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    mTvAppName.setText(String.format("应用：%s", appName));
                    mTvPackageName.setText(String.format("包名：%s", packageName));
                }
            });
        }
    }

    /**
     * @return 设置悬浮框是否吸附在屏幕边缘
     */
    @Override
    protected boolean isAdsorbView() {
        return true;
    }

    @Override
    public void clear() {
        super.clear();
        mMainHandler.removeCallbacksAndMessages(null);
    }
}
