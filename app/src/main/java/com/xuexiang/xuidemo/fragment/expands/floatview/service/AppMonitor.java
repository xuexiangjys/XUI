package com.xuexiang.xuidemo.fragment.expands.floatview.service;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.xuexiang.xutil.app.AppUtils;

import java.util.List;

/**
 * 应用监听（使用 https://github.com/jaredrummler/AndroidProcesses )
 *
 * @author xuexiang
 * @since 2019/1/21 下午12:06
 */
public class AppMonitor extends Thread {

    private Context mContext;
    /**
     * 是否在运行
     */
    private boolean mIsRunning;

    /**
     * 检测间隔（单位：秒）
     */
    private int mKeepAliveInterval = 2;

    private OnAppListener mOnAppListener;

    private int mUid;

    /**
     * 构造器
     */
    public AppMonitor(Context context, int keepAliveInterval, OnAppListener listener) {
        mIsRunning = true;
        mContext = context.getApplicationContext();
        mKeepAliveInterval = keepAliveInterval;
        mOnAppListener = listener;
    }

    /**
     * 构造器
     */
    public AppMonitor(Context context, OnAppListener listener) {
        mIsRunning = true;
        mContext = context.getApplicationContext();
        mOnAppListener = listener;
    }

    @Override
    public void run() {
        while (mIsRunning) {
            try {
                // 休息一段时间
                Thread.sleep(mKeepAliveInterval * 1000);
                doAppCheck();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 对app的检测
     */
    private void doAppCheck() {
        //注意模拟器可能有问题
        List<AndroidAppProcess> processes = AndroidProcesses.getRunningForegroundApps(mContext);
        if (processes != null && processes.size() > 0) {
            AndroidAppProcess process = processes.get(processes.size() - 1);
            if (mUid != process.uid) {
                try {
                    mUid = process.uid;
                    PackageInfo packageInfo = process.getPackageInfo(mContext, 0);
                    String mAppName = packageInfo.applicationInfo.loadLabel(AppUtils.getPackageManager()).toString();
                    String mAppPackageName = process.getPackageName();

                    if (mOnAppListener != null) {
                        mOnAppListener.onAppChanged(mAppName, mAppPackageName);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 更新UID
     *
     * @param uid
     * @return
     */
    public AppMonitor updateUid(int uid) {
        mUid = uid;
        return this;
    }

    /**
     * 关闭线程
     */
    public void close() {
        mIsRunning = false;
        interrupt();
    }

    public AppMonitor setOnAppListener(OnAppListener listener) {
        mOnAppListener = listener;
        return this;
    }

    /**
     * 应用监听
     */
    public interface OnAppListener {
        /**
         * 应用变化
         *
         * @param appName     应用名
         * @param packageName 包名
         */
        void onAppChanged(String appName, String packageName);
    }

}
