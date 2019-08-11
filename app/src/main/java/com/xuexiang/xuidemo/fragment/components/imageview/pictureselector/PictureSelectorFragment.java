/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.xuidemo.fragment.components.imageview.pictureselector;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2018/12/5 下午11:51
 */
@Page(name = "PictureSelector\n图片选择")
public class PictureSelectorFragment extends BaseFragment implements ImageSelectGridAdapter.OnAddPicClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ImageSelectGridAdapter mAdapter;

    private List<LocalMedia> mSelectList = new ArrayList<>();

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_photo_picker;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter = new ImageSelectGridAdapter(getActivity(), this));
        mAdapter.setSelectList(mSelectList);
        mAdapter.setSelectMax(8);
        mAdapter.setOnItemClickListener(new ImageSelectGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                PictureSelector.create(PictureSelectorFragment.this).themeStyle(R.style.XUIPictureStyle).openExternalPreview(position, mSelectList);
            }
        });
    }

    @OnClick({R.id.button, R.id.button_no_camera, R.id.button_one_photo, R.id.button_photo_gif})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button:
                Utils.getPictureSelector(this)
                        .selectionMedia(mSelectList)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case R.id.button_no_camera:
                Utils.getPictureSelector(this)
                        .selectionMedia(mSelectList)
                        .previewImage(false)
                        .isCamera(false)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case R.id.button_one_photo:
                Utils.getPictureSelector(this)
                        .selectionMedia(mSelectList)
                        .maxSelectNum(1)
                        .selectionMode(PictureConfig.SINGLE)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case R.id.button_photo_gif:
                Utils.getPictureSelector(this)
                        .selectionMedia(mSelectList)
                        .isGif(true)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择
                    mSelectList = PictureSelector.obtainMultipleResult(data);
                    mAdapter.setSelectList(mSelectList);
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onAddPicClick() {
        Utils.getPictureSelector(this)
                .selectionMedia(mSelectList)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
}
