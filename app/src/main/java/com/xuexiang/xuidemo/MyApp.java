package com.xuexiang.xuidemo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.BuglyStrategy;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
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
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.system.DeviceUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.List;


/**
 * @author xuexiang
 * @since 2018/11/7 下午1:12
 */
public class MyApp extends Application {

    private static final String APP_ID_UMENG = "5d030d6f570df379b7000e70";
    private static final String APP_ID_BUGLY = "813d00d577";

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

        initUMeng();

        initBugly();
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


//    /**
//     * 初始化video的存放路径[xvideo项目太大，去除]
//     */
//    public static void initVideo() {
//        XVideo.setVideoCachePath(PathUtils.getExtDcimPath() + "/xvideo/");
//        // 初始化拍摄
//        XVideo.initialize(false, null);
//    }

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

    /**
     * 初始化UmengSDK
     */
    private void initUMeng() {
        long start = System.currentTimeMillis();
        //设置LOG开关，默认为false
        UMConfigure.setLogEnabled(true);
        //初始化组件化基础库, 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，UMConfigure.init调用中appkey和channel参数请置为null）。
        //第二个参数是appkey，最后一个参数是pushSecret
        UMConfigure.init(this, APP_ID_UMENG, "pgyer", UMConfigure.DEVICE_TYPE_PHONE,"");
        //统计SDK是否支持采集在子进程中打点的自定义事件，默认不支持
        UMConfigure.setProcessEvent(true);//支持多进程打点
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        long end = System.currentTimeMillis();
        Log.e("initUMeng time--->", end - start + "ms");
    }

    /**
     * 初始化BuglySDK
     */
    private void initBugly() {
        long start = System.currentTimeMillis();
        BuglyStrategy strategy = new BuglyStrategy();
        strategy.setEnableANRCrashMonitor(true)
                .setEnableNativeCrashMonitor(true)
                .setUploadProcess(true)
                .setDeviceID(DeviceUtils.getAndroidID())
                .setRecordUserInfoOnceADay(true);
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId,调试时将第三个参数设置为true
        Bugly.init(this, APP_ID_BUGLY, true, strategy);
        long end = System.currentTimeMillis();
        Log.e("initBugly time--->", end - start + "ms");
    }
}
