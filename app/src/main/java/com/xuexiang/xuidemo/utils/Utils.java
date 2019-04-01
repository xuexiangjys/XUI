package com.xuexiang.xuidemo.utils;

import android.content.Context;
import android.content.Intent;

import com.xuexiang.xuidemo.base.webview.AgentWebActivity;

import static com.xuexiang.xuidemo.base.webview.AgentWebFragment.KEY_URL;

/**
 * @author XUE
 * @since 2019/4/1 11:25
 */
public final class Utils {

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 请求浏览器
     *
     * @param url
     */
    public static void goWeb(Context context, final String url) {
        Intent intent = new Intent(context, AgentWebActivity.class);
        intent.putExtra(KEY_URL, url);
        context.startActivity(intent);
    }
}
