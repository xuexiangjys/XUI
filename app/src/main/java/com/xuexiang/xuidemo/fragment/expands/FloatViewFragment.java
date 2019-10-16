package com.xuexiang.xuidemo.fragment.expands;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;

import com.xuexiang.xfloatview.permission.FloatWindowPermission;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseSimpleListFragment;
import com.xuexiang.xuidemo.fragment.expands.floatview.service.AppMonitorService;
import com.xuexiang.xuidemo.utils.Utils;

import java.util.List;

/**
 * @author xuexiang
 * @since 2019/1/21 上午11:32
 */
@Page(name = "悬浮窗", extra = R.drawable.ic_expand_floatview)
public class FloatViewFragment extends BaseSimpleListFragment {

    @Override
    protected void initArgs() {
        super.initArgs();
        FloatWindowPermission.getInstance().applyFloatWindowPermission(getContext());
    }

    /**
     * 初始化例子
     *
     * @param lists
     * @return
     */
    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("开启悬浮窗");
        lists.add("关闭悬浮窗");
        return lists;
    }

    /**
     * 条目点击
     *
     * @param position
     */
    @Override
    protected void onItemClick(int position) {
        Dialog dialog = FloatWindowPermission.getInstance().applyFloatWindowPermission(getContext());
        if  (dialog != null) {
            //需要申请权限
            return;
        }

        switch(position) {
            case 0:
                AppMonitorService.start(getContext(), null);
                gotoHome(getActivity());
                break;
            case 1:
                AppMonitorService.stop(getContext());
                break;
            default:
                break;
        }
    }

    /**
     * 防止华为机型未加入白名单时按返回键回到桌面再锁屏后几秒钟进程被杀
     */
    public static void gotoHome(Activity activity) {
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.addCategory(Intent.CATEGORY_HOME);
        activity.startActivity(launcherIntent);
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("Github") {
            @Override
            public void performAction(View view) {
                Utils.goWeb(getContext(), "https://github.com/xuexiangjys/XFloatView");
            }
        });
        return titleBar;
    }

}
