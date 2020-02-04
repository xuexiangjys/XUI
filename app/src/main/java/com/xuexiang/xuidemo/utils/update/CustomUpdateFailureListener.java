package com.xuexiang.xuidemo.utils.update;

import com.xuexiang.xuidemo.utils.XToastUtils;
import com.xuexiang.xupdate.entity.UpdateError;
import com.xuexiang.xupdate.listener.OnUpdateFailureListener;

/**
 * 自定义版本更新提示
 *
 * @author xuexiang
 * @since 2019/4/15 上午12:01
 */
public class CustomUpdateFailureListener implements OnUpdateFailureListener {

    /**
     * 是否需要错误提示
     */
    private boolean mNeedErrorTip;

    public CustomUpdateFailureListener() {
        this(true);
    }

    public CustomUpdateFailureListener(boolean needErrorTip) {
        mNeedErrorTip = needErrorTip;
    }

    /**
     * 更新失败
     *
     * @param error 错误
     */
    @Override
    public void onFailure(UpdateError error) {
        if (mNeedErrorTip) {
            XToastUtils.toast(error.toString());
        }
        if (error.getCode() == UpdateError.ERROR.DOWNLOAD_FAILED) {
            UpdateTipDialog.show("Github被墙无法下载，是否考虑切换蒲公英下载[密码:xuexiangjys]？");
        }
    }
}
