package com.xuexiang.xuidemo.fragment.components.progress;

import android.view.View;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.progress.ratingbar.RatingBar;
import com.xuexiang.xui.widget.progress.ratingbar.RotationRatingBar;
import com.xuexiang.xui.widget.progress.ratingbar.ScaleRatingBar;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xutil.tip.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2019/3/26 下午11:24
 */
@Page(name = "RatingBar\n星级评分控件")
public class RatingBarFragment extends BaseFragment {
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.scale_rating_bar)
    ScaleRatingBar scaleRatingBar;
    @BindView(R.id.rrb_custom)
    RotationRatingBar rrbCustom;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ratingbar;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {

    }

    @Override
    protected void initListeners() {
        ratingBar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(RatingBar ratingBar, float rating) {
                ToastUtils.toast("当前星级：" + rating);
            }
        });
        scaleRatingBar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(RatingBar ratingBar, float rating) {

            }
        });
    }

    @SingleClick
    @OnClick(R.id.btn_add_rating)
    public void onViewClicked(View view) {

        float currentRating = ratingBar.getRating();
        ratingBar.setRating(currentRating + 0.25f);

        currentRating = scaleRatingBar.getRating();
        scaleRatingBar.setRating(currentRating + 0.25f);

        currentRating = rrbCustom.getRating();
        rrbCustom.setRating(currentRating + 0.25f);

    }
}
