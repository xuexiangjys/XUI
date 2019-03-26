package com.xuexiang.xuidemo.fragment.components.popupwindow;

import android.support.annotation.NonNull;
import android.view.Gravity;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.textview.label.LabelView;
import com.xuexiang.xui.widget.toast.XToast;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseSimpleListFragment;

import java.util.List;

/**
 * @author XUE
 * @since 2019/3/26 10:06
 */
@Page(name = "XToast")
public class XToastFragment extends BaseSimpleListFragment {

    @Override
    protected void initArgs() {
        super.initArgs();
        XToast.Config.get()
                .setGravity(Gravity.CENTER)
                .setAlpha(150); //背景透明度
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
        switch(position) {
            case 0:
                XToast.error(getContext(), R.string.error_message).show();
                break;
            case 1:
                XToast.success(getContext(), R.string.success_message).show();
                break;
            case 2:
                XToast.info(getContext(), R.string.info_message).show();
                break;
            case 3:
                XToast.warning(getContext(), R.string.warning_message).show();
                break;
            case 4:
                XToast.normal(getContext(), R.string.normal_message_without_icon).show();
                break;
            default:
                break;
        }

    }
}
