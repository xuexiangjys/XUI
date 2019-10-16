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

package com.xuexiang.xuidemo.fragment.expands.camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 图片裁剪页面
 *
 * @author xuexiang
 * @since 2019-09-29 16:33
 */
public class PictureCropActivity extends AppCompatActivity {

    public static final String KEY_PICTURE_PATH = "key_picture_path";
    public static final String KEY_IS_CAMERA = "key_is_camera";

    public static final int REQUEST_CODE_PICTURE_CROP = 1122;

    public static void open(@NonNull Activity activity, boolean isCamera, String imgPath) {
        Intent intent = new Intent(activity, PictureCropActivity.class);
        intent.putExtra(KEY_IS_CAMERA, isCamera);
        intent.putExtra(KEY_PICTURE_PATH, imgPath);
        activity.startActivityForResult(intent, REQUEST_CODE_PICTURE_CROP);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }





}
