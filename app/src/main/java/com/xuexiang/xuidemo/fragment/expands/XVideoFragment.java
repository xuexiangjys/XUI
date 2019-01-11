/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.expands;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerController;
import com.xiao.nicevideoplayer.TxVideoPlayerController;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xaop.consts.PermissionConsts;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.imageview.preview.loader.GlideMediaLoader;
import com.xuexiang.xuidemo.MyApp;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xutil.app.PathUtils;
import com.xuexiang.xutil.app.SocialShareUtils;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.file.FileUtils;
import com.xuexiang.xvideo.MediaRecorderActivity;
import com.xuexiang.xvideo.XVideo;
import com.xuexiang.xvideo.model.MediaRecorderConfig;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * @author xuexiang
 * @since 2019/1/2 下午3:08
 */
@Page(name = "小视频录制", extra = R.drawable.ic_expand_video)
public class XVideoFragment extends BaseFragment implements TxVideoPlayerController.OnShareListener {

    /**
     * 小视频录制
     */
    private static final int REQUEST_CODE_VIDEO = 100;

    @BindView(R.id.video_player)
    NiceVideoPlayer videoPlayer;
    @BindView(R.id.btn_share)
    Button btnShare;

    private static boolean isInit = false;

    @Override
    protected void initArgs() {
        if (!isInit) {
            MyApp.initVideo();
            isInit = true;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_xvideo;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        btnShare.setEnabled(false);
    }

    /**
     * 初始化监听
     */
    @Override
    protected void initListeners() {

    }


    /**
     * 开始录制视频
     * @param requestCode 请求码
     */
    @Permission({PermissionConsts.CAMERA, PermissionConsts.STORAGE})
    public void startVideoRecorder(int requestCode) {
        MediaRecorderConfig mediaRecorderConfig = MediaRecorderConfig.newInstance();
        XVideo.startVideoRecorder(this, mediaRecorderConfig, requestCode);
    }

    @SingleClick
    @OnClick({R.id.btn_video, R.id.btn_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_video:
                startVideoRecorder(REQUEST_CODE_VIDEO);
                break;
            case R.id.btn_share:
                SocialShareUtils.shareVideo(PathUtils.getMediaContentUri(FileUtils.getFileByPath(videoPlayer.getUrl())), SocialShareUtils.ShareType.DEFAULT);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_VIDEO) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String videoUri = bundle.getString(MediaRecorderActivity.VIDEO_URI);
                String videoScreenshot = bundle.getString(MediaRecorderActivity.VIDEO_SCREENSHOT);
                videoPlayer.setUp(videoUri, null);
                videoPlayer.setController(buildPlayerController("这是XVideo拍摄的视频！", videoScreenshot));
                btnShare.setEnabled(true);
            }
        }
    }

    /**
     * 构建播放器的控制器
     *
     * @param title      标题
     * @param screenshot 截图
     * @return
     */
    private NiceVideoPlayerController buildPlayerController(String title, String screenshot) {
        TxVideoPlayerController controller = new TxVideoPlayerController(getContext(), this);
        controller.imageView().setBackgroundColor(Color.BLACK);
        controller.setTitle(title);
        if (!StringUtils.isEmpty(screenshot)) {
            RequestOptions options = GlideMediaLoader.getRequestOptions()
                    .placeholder(R.drawable.player_img_default);

            Glide.with(this)
                    .load(screenshot)
                    .apply(options)
                    .into(controller.imageView());
        }
        return controller;
    }

    /**
     * 分享
     */
    @Override
    public void onShare() {
        SocialShareUtils.shareVideo(PathUtils.getMediaContentUri(FileUtils.getFileByPath(videoPlayer.getUrl())), SocialShareUtils.ShareType.DEFAULT);
    }
}
