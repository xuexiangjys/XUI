package com.xuexiang.xuidemo.fragment.components.popupwindow;

import android.view.Gravity;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.toast.XToast;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseSimpleListFragment;

import java.util.List;

/**
 * @author XUE
 * @since 2019/3/26 10:06
 */
@Page(name = "XToast\n多重样式的Toast显示")
public class XToastFragment extends BaseSimpleListFragment {

    @Override
    protected void initArgs() {
        super.initArgs();
        XToast.Config.get()
                //位置设置为居中
                .setGravity(Gravity.CENTER);
    }

    /**
     * 初始化例子
     *
     * @param lists
     * @return
     */
    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("ERROR_TOAST");
        lists.add("SUCCESS_TOAST");
        lists.add("INFO_TOAST");
        lists.add("WARNING_TOAST");
        lists.add("NORMAL_TOAST");
        return lists;
    }

    /**
     * 条目点击
     *
     * @param position
     */
    @Override
    protected void onItemClick(int position) {
        switch (position) {
            case 0:
                XToastUtils.error(R.string.error_message);
                break;
            case 1:
                XToastUtils.success(R.string.success_message);
                break;
            case 2:
                XToastUtils.info(R.string.info_message);
                break;
            case 3:
                XToastUtils.warning(R.string.warning_message);
                break;
            case 4:
                XToastUtils.toast(R.string.normal_message_without_icon);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        XToast.Config.get()
                //位置还原
                .resetGravity();
        super.onDestroyView();
    }
}
