package com.xuexiang.xuidemo.fragment.components.textview.supertextview;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;

/**
 *
 *
 * @author xuexiang
 * @since 2018/11/29 上午12:09
 */
@Page(name = "带网络图片的SuperTextView")
public class SuperNetPictureLoadingFragment extends BaseFragment {
    @BindView(R.id.super_tv1)
    SuperTextView superTextView;
    @BindView(R.id.super_tv2)
    SuperTextView superTextView2;
    @BindView(R.id.super_tv3)
    SuperTextView superTextView3;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_super_picture_loading;
    }

    @Override
    protected void initViews() {
        String url1 = "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3860616424,1789830124&fm=80&w=179&h=119&img.PNG";
        String url2 = "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=219781665,3032880226&fm=80&w=179&h=119&img.JPEG";
        String url3 = "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3860616424,1789830124&fm=80&w=179&h=119&img.PNG";

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.icon_head_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(this)
                .load(url1)
                .apply(options)
                .into(superTextView.getLeftIconIV());
        Glide.with(this)
                .load(url2)
                .apply(options)
                .into(superTextView2.getRightIconIV());
        Glide.with(this)
                .load(url3)
                .apply(options)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        superTextView3.setRightTvDrawableRight(resource);
                    }
                });
    }

    @Override
    protected void initListeners() {

    }
}
