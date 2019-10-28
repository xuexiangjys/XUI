package com.xuexiang.xuidemo.fragment.components;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.components.imageview.ImageEditFragment;
import com.xuexiang.xuidemo.fragment.components.imageview.edit.ImageCropFragment;
import com.xuexiang.xuidemo.fragment.components.imageview.edit.ImageEnhanceFragment;
import com.xuexiang.xuidemo.fragment.components.imageview.PreviewFragment;
import com.xuexiang.xuidemo.fragment.components.imageview.RadiusImageViewFragment;
import com.xuexiang.xuidemo.fragment.components.imageview.pictureselector.PictureSelectorFragment;

/**
 * @author xuexiang
 * @since 2018/12/2 上午12:21
 */
@Page(name = "图片", extra = R.drawable.ic_widget_imageview)
public class ImageViewFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                RadiusImageViewFragment.class,
                PictureSelectorFragment.class,
                PreviewFragment.class,
                ImageEditFragment.class
        };
    }
}
