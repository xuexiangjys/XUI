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

package com.xuexiang.xui.widget.imageview.preview.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.text.TextUtils;
import android.widget.Toast;
import android.widget.VideoView;

import com.xuexiang.xui.R;

/**
 * 视频播放界面
 *
 * @author xuexiang
 * @since 2018/12/5 上午11:49
 */
public class VideoPlayerActivity extends FragmentActivity {

    public static final String KEY_URL = "com.xuexiang.xui.widget.preview.KEY_URL";

    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_activity_video_player);
        mVideoView = findViewById(R.id.video);

        String videoPath = getIntent().getStringExtra(KEY_URL);
        if (TextUtils.isEmpty(videoPath)) {
            Toast.makeText(VideoPlayerActivity.this, R.string.xui_preview_video_path_error, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mVideoView.setVideoPath(videoPath);
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(VideoPlayerActivity.this, R.string.xui_preview_play_failed, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        mVideoView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mVideoView.isPlaying()) {
            mVideoView.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
    }

    /***
     * 启动播放视频
     * @param fragment context
     * @param url url
     **/
    public static void start(Fragment fragment, String url) {
        Intent intent = new Intent(fragment.getContext(), VideoPlayerActivity.class);
        intent.putExtra(KEY_URL, url);
        fragment.startActivity(intent);
    }
}
