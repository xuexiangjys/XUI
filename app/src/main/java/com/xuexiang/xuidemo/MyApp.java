package com.xuexiang.xuidemo;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.luck.picture.lib.tools.PictureFileUtils;
import com.xuexiang.xaop.XAOP;
import com.xuexiang.xaop.util.PermissionUtils;
import com.xuexiang.xpage.AppPageConfig;
import com.xuexiang.xpage.PageConfig;
import com.xuexiang.xpage.PageConfiguration;
import com.xuexiang.xpage.model.PageInfo;
import com.xuexiang.xui.XUI;
import com.xuexiang.xuidemo.base.BaseActivity;
import com.xuexiang.xuidemo.utils.LocationService;
import com.xuexiang.xuidemo.utils.sdkinit.BuglyInit;
import com.xuexiang.xuidemo.utils.sdkinit.UMengInit;
import com.xuexiang.xuidemo.utils.sdkinit.XUpdateInit;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.List;


/**
 * 应用初始化
 *
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

        initBasicLibs();

        initUI();

        //三方SDK初始化
        XUpdateInit.init(this);
        UMengInit.init(this);
        BuglyInit.init(this);
    }

    /**
     * 初始化XUI 框架
     */
    private void initUI() {
        XUI.init(this);
        XUI.debug(BuildConfig.DEBUG);
//        //设置默认字体为华文行楷
//        XUI.getInstance().initFontStyle("fonts/hwxk.ttf");

        PictureFileUtils.setAppName("xui");
    }


    /**
     * 初始化基础库
     */
    private void initBasicLibs() {
        XUtil.init(this);
        XUtil.debug(BuildConfig.DEBUG);
        //百度定位
        LocationService.get().init(this);

        //自动注册页面
        PageConfig.getInstance()
                .setPageConfiguration(new PageConfiguration() {
                    @Override
                    public List<PageInfo> registerPages(Context context) {
                        //自动注册页面,是编译时自动生成的，build一下就出来了
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


}
