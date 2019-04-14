package com.xuexiang.xuidemo.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xuexiang.xuidemo.base.webview.AgentWebActivity;
import com.xuexiang.xuidemo.utils.update.CustomUpdateFailureListener;
import com.xuexiang.xupdate.XUpdate;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static com.xuexiang.xuidemo.base.webview.AgentWebFragment.KEY_URL;

/**
 * @author XUE
 * @since 2019/4/1 11:25
 */
public final class Utils {

    public final static String mUpdateUrl = "https://raw.githubusercontent.com/xuexiangjys/XUI/master/jsonapi/update_api.json";

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

    public static void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 进行版本更新检查
     *
     * @param context
     */
    public static void checkUpdate(Context context, boolean needErrorTip) {
        XUpdate.newBuild(context).updateUrl(mUpdateUrl).update();
        XUpdate.get().setOnUpdateFailureListener(new CustomUpdateFailureListener(needErrorTip));

    }
}
