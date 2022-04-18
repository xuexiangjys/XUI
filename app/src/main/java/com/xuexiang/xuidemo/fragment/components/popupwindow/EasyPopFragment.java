package com.xuexiang.xuidemo.fragment.components.popupwindow;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.popupwindow.easypopup.EasyPopup;
import com.xuexiang.xui.widget.popupwindow.easypopup.HorizontalGravity;
import com.xuexiang.xui.widget.popupwindow.easypopup.VerticalGravity;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xui.utils.XToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @date 2017/10/30 上午11:49
 */
@Page(name = "EasyPopup\n可自定义的弹出窗")
public class EasyPopFragment extends BaseFragment {

    private EasyPopup mCirclePop;
    @BindView(R.id.btn_circle_comment)
    Button mBtnCircleComment;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_easypop;
    }

    @Override
    protected void initViews() {
        initCirclePop();
    }

    @Override
    protected void initListeners() {

    }

    public void initCirclePop() {
        mCirclePop = new EasyPopup(getContext())
                .setContentView(R.layout.layout_friend_circle_comment)
//                .setAnimationStyle(R.style.CirclePopAnim)
                .setFocusAndOutsideEnable(true)
                .createPopup();

        TextView tvZan = mCirclePop.getView(R.id.tv_zan);
        TextView tvComment = mCirclePop.getView(R.id.tv_comment);
        tvZan.setOnClickListener(v -> {
            XToastUtils.toast("点赞");
            mCirclePop.dismiss();
        });

        tvComment.setOnClickListener(v -> {
            XToastUtils.toast("评论");
            mCirclePop.dismiss();
        });
    }

    @OnClick(R.id.btn_circle_comment_left)
    public void showCirclePopLeft(View view) {
        mCirclePop.showAtAnchorView(mBtnCircleComment, VerticalGravity.CENTER, HorizontalGravity.LEFT, 0, 0);
    }

    @OnClick(R.id.btn_circle_comment_right)
    public void showCirclePopRight(View view) {
        mCirclePop.showAtAnchorView(mBtnCircleComment, VerticalGravity.CENTER, HorizontalGravity.RIGHT, 0, 0);
    }

    @OnClick(R.id.btn_circle_comment_top)
    public void showCirclePopTop(View view) {
        mCirclePop.showAtAnchorView(mBtnCircleComment, VerticalGravity.ABOVE, HorizontalGravity.CENTER, 0, 0);
    }

    @OnClick(R.id.btn_circle_comment_bottom)
    public void showCirclePopBottom(View view) {
        mCirclePop.showAtAnchorView(mBtnCircleComment, VerticalGravity.BELOW, HorizontalGravity.CENTER, 0, 0);
    }

}
