package com.xuexiang.xuidemo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.xuexiang.xaop.XAOP;
import com.xuexiang.xaop.util.PermissionUtils;
import com.xuexiang.xpage.AppPageConfig;
import com.xuexiang.xpage.PageConfig;
import com.xuexiang.xpage.PageConfiguration;
import com.xuexiang.xpage.model.PageInfo;
import com.xuexiang.xui.XUI;
import com.xuexiang.xuidemo.base.BaseActivity;
import com.xuexiang.xuidemo.utils.LocationService;
import com.xuexiang.xuidemo.utils.update.OKHttpUpdateHttpService;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.utils.UpdateUtils;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.app.PathUtils;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.tip.ToastUtils;
import com.xuexiang.xvideo.XVideo;

import java.util.List;

/**
 * @author xuexiang
 * @since 2018/11/7 下午1:12
 */
public class MyApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //解决4.x运行崩溃的问题
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initLibs();

        initUI();

        initUpdate();
    }

    private void initUI() {
        XUI.init(this);
        XUI.debug(BuildConfig.DEBUG);
//        //设置默认字体为华文行楷
//        XUI.getInstance().initFontStyle("fonts/hwxk.ttf");
    }


    /**
     * 初始化基础库
     */
    private void initLibs() {
        XUtil.init(this);
        XUtil.debug(BuildConfig.DEBUG);
        //百度定位
        LocationService.get().init(this);

        //自动注册页面
        PageConfig.getInstance()
                .setPageConfiguration(new PageConfiguration() {
                    @Override
                    public List<PageInfo> registerPages(Context context) {
                        return AppPageConfig.getInstance().getPages();
                    }
                })
                .debug("PageLog")
                .setContainActivityClazz(BaseActivity.class)
                .enableWatcher(false)
                .init(this);

        //初始化插件
        XAOP.init(this);
        //日志打印切片开启
        XAOP.debug(BuildConfig.DEBUG);
        //设置动态申请权限切片 申请权限被拒绝的事件响应监听
        XAOP.setOnPermissionDeniedListener(new PermissionUtils.OnPermissionDeniedListener() {
            @Override
            public void onDenied(List<String> permissionsDenied) {
                ToastUtils.toast("权限申请被拒绝:" + StringUtils.listToString(permissionsDenied, ","));
            }

        });
    }


    /**
     * 初始化video的存放路径
     */
    public static void initVideo() {
        XVideo.setVideoCachePath(PathUtils.getExtDcimPath() + "/xvideo/");
        // 初始化拍摄
        XVideo.initialize(false, null);
    }

    private void initUpdate() {
        XUpdate.get()
                .debug(BuildConfig.DEBUG)
                //默认设置只在wifi下检查版本更新
                .isWifiOnly(false)
                //默认设置使用get请求检查版本
                .isGet(true)
                //默认设置非自动模式，可根据具体使用配置
                .isAutoMode(false)
                //设置默认公共请求参数
                .param("versionCode", UpdateUtils.getVersionCode(this))
                .param("appKey", getPackageName())
                //这个必须设置！实现网络请求功能。
                .setIUpdateHttpService(new OKHttpUpdateHttpService())
                //这个必须初始化
                .init(this);
    }
}
