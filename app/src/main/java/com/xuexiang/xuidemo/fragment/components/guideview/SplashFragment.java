package com.xuexiang.xuidemo.fragment.components.guideview;

import android.content.Intent;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.activity.SplashActivity;
import com.xuexiang.xuidemo.base.BaseSimpleListFragment;

import java.util.List;

import static com.xuexiang.xuidemo.activity.SplashActivity.KEY_ENABLE_ALPHA_ANIM;
import static com.xuexiang.xuidemo.activity.SplashActivity.KEY_IS_DISPLAY;

/**
 * 启动页演示
 *
 * @author xuexiang
 * @since 2018/11/30 上午12:56
 */
@Page(name = "启动页")
public class SplashFragment extends BaseSimpleListFragment {
    /**
     * 初始化例子
     *
     * @param lists
     * @return
     */
    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("渐近式启动页");
        lists.add("非渐近式启动页");
        return lists;
    }

    /**
     * 条目点击
     *
     * @param position
     */
    @Override
    protected void onItemClick(int position) {
        Intent i = new Intent();
        i.putExtra(KEY_IS_DISPLAY, true);
        i.setClass(getContext(), SplashActivity.class);
        switch (position) {
            case 0:
                i.putExtra(KEY_ENABLE_ALPHA_ANIM, true);
                break;
            case 1:
                i.putExtra(KEY_ENABLE_ALPHA_ANIM, false);
                break;
            default:
                break;
        }
        startActivity(i);
    }
}
