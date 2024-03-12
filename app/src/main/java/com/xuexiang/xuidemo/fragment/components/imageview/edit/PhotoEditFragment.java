/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xuidemo.fragment.components.imageview.edit;

import static android.app.Activity.RESULT_OK;
import static com.xuexiang.xaop.consts.PermissionConsts.STORAGE;
import static com.xuexiang.xuidemo.fragment.expands.XQRCodeFragment.REQUEST_IMAGE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;

import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.imageview.edit.OnPhotoEditorListener;
import com.xuexiang.xui.widget.imageview.edit.PhotoEditor;
import com.xuexiang.xui.widget.imageview.edit.PhotoEditorView;
import com.xuexiang.xui.widget.imageview.edit.PhotoFilter;
import com.xuexiang.xui.widget.imageview.edit.TextStyleBuilder;
import com.xuexiang.xui.widget.imageview.edit.ViewType;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.Utils;
import com.xuexiang.xutil.app.IntentUtils;
import com.xuexiang.xutil.app.PathUtils;
import com.xuexiang.xutil.display.ImageUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2019-10-28 10:56
 */
@Page(name = "图片编辑\n画笔、橡皮檫、文字、滤镜、保存")
public class PhotoEditFragment extends BaseFragment implements OnPhotoEditorListener {

    @BindView(R.id.photo_editor_view)
    PhotoEditorView photoEditorView;

    private PhotoEditor mPhotoEditor;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_photo_edit;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        mPhotoEditor = new PhotoEditor.Builder(getContext(), photoEditorView)
                .setPinchTextScalable(true)
                .build(); // build photo editor sdk

        mPhotoEditor.setOnPhotoEditorListener(this);
    }

    @SingleClick
    @OnClick({R.id.btn_select, R.id.btn_brush, R.id.btn_text, R.id.btn_rubber, R.id.iv_undo, R.id.iv_redo, R.id.btn_filter, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_select:
                selectImage();
                break;
            case R.id.iv_undo:
                mPhotoEditor.undo();
                break;
            case R.id.iv_redo:
                mPhotoEditor.redo();
                break;
            case R.id.btn_brush:
                mPhotoEditor.setBrushColor(ResUtils.getColor(getContext(), R.color.xui_config_color_white))
                        .setBrushSize(DensityUtils.dp2px(getContext(), 5))
                        .setBrushDrawingMode(true);
                break;
            case R.id.btn_text:
                final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                styleBuilder.withTextColor(ResUtils.getColor(getContext(), R.color.xui_config_color_white));
                mPhotoEditor.addText("XUI", styleBuilder);
                break;
            case R.id.btn_rubber:
                mPhotoEditor.brushEraser();
                break;
            case R.id.btn_filter:
                mPhotoEditor.setFilterEffect(PhotoFilter.GRAY_SCALE);
                break;
            case R.id.btn_save:
                saveImage();
                break;
            default:
                break;
        }
    }

    /**
     * 保存图片
     */
    @SuppressLint("MissingPermission")
    @Permission(STORAGE)
    private void saveImage() {
        getMessageLoader("保存中...").show();
        mPhotoEditor.saveAsFile(Utils.getImageSavePath(), new PhotoEditor.OnSaveListener() {
            @Override
            public void onSuccess(@NonNull String imagePath) {
                getMessageLoader().dismiss();
                if (photoEditorView != null) {
                    photoEditorView.getSource().setImageBitmap(ImageUtils.getBitmap(imagePath));
                }
            }
            @Override
            public void onFailure(@NonNull Exception exception) {
                getMessageLoader().dismiss();
                XToastUtils.error(exception);
            }
        });
    }

    @Permission(STORAGE)
    private void selectImage() {
        startActivityForResult(IntentUtils.getDocumentPickerIntent(IntentUtils.DocumentType.IMAGE), REQUEST_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选择系统图片并解析
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    mPhotoEditor.clearAllViews();
                    @SuppressLint("MissingPermission")
                    Bitmap bitmap = ImageUtils.getBitmap(PathUtils.getFilePathByUri(uri));
                    photoEditorView.getSource().setImageBitmap(bitmap);
                }
            }
        }
    }

    @Override
    public void onEditTextChangeListener(View rootView, String text, int colorCode) {

    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {

    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {

    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {

    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {

    }
}
