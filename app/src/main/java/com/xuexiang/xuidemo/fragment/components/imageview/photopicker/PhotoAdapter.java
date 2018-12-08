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

package com.xuexiang.xuidemo.fragment.components.imageview.photopicker;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xuexiang.xuidemo.R;

import java.io.File;
import java.util.ArrayList;

import me.iwf.photopicker.utils.AndroidLifecycleUtils;

/**
 *
 *
 * @author xuexiang
 * @since 2018/12/6 上午12:02
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private ArrayList<String> photoPaths = new ArrayList<String>();
    private LayoutInflater inflater;

    private Context mContext;

    final static int TYPE_ADD = 1;
    final static int TYPE_PHOTO = 2;

    final static int MAX = 9;

    public PhotoAdapter(Context mContext, ArrayList<String> photoPaths) {
        this.photoPaths = photoPaths;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);

    }


    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType) {
            case TYPE_ADD:
                itemView = inflater.inflate(R.layout.adapter_picker_item_add, parent, false);
                break;
            case TYPE_PHOTO:
                itemView = inflater.inflate(R.layout.__picker_item_photo, parent, false);
                break;
        }
        return new PhotoViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {

        if (getItemViewType(position) == TYPE_PHOTO) {
            Uri uri = Uri.fromFile(new File(photoPaths.get(position)));

            boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(holder.ivPhoto.getContext());

            if (canLoadImage) {
                final RequestOptions options = new RequestOptions();
                options.centerCrop()
                        .placeholder(R.drawable.__picker_ic_photo_black_48dp)
                        .error(R.drawable.__picker_ic_broken_image_black_48dp);
                Glide.with(mContext)
                        .load(uri)
                        .apply(options)
                        .thumbnail(0.1f)
                        .into(holder.ivPhoto);
            }
        }
    }


    @Override
    public int getItemCount() {
        int count = photoPaths.size() + 1;
        if (count > MAX) {
            count = MAX;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == photoPaths.size() && position != MAX) ? TYPE_ADD : TYPE_PHOTO;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private View vSelected;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(me.iwf.photopicker.R.id.iv_photo);
            vSelected = itemView.findViewById(me.iwf.photopicker.R.id.v_selected);
            if (vSelected != null) vSelected.setVisibility(View.GONE);
        }
    }

}
