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

package com.xuexiang.xuidemo.fragment.expands.webview;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageActivity;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.core.PageOption;
import com.xuexiang.xrouter.annotation.AutoWired;
import com.xuexiang.xrouter.launcher.XRouter;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.base.webview.x5.FileReaderView;

import butterknife.BindView;

import static com.xuexiang.xuidemo.fragment.expands.webview.TbsWebFileReaderFragment.KEY_FILE_PATH;

/**
 * @author xuexiang
 * @since 2019-07-21 23:01
 */
@Page(name = "腾讯X5文件浏览器", params = {KEY_FILE_PATH})
public class TbsWebFileReaderFragment extends BaseFragment {

    static final String KEY_FILE_PATH = "key_file_path";

    @BindView(R.id.file_reader_view)
    FileReaderView fileReaderView;

    @AutoWired(name = KEY_FILE_PATH)
    String filePath;

    @Override
    protected void initArgs() {
        super.initArgs();
        XRouter.getInstance().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tbs_web_file_reader;
    }

    @Override
    protected void initViews() {
        fileReaderView.show(filePath);
    }


    @Override
    public void onDestroyView() {
        if (fileReaderView != null) {
            fileReaderView.stop();
        }
        super.onDestroyView();
    }


    public static void show(XPageFragment fragment, String path) {
        PageOption.to(TbsWebFileReaderFragment.class)
                .setNewActivity(true)
                .putString(KEY_FILE_PATH, path)
                .open(fragment);
    }

    public static void show(XPageActivity activity, String path) {
        PageOption.to(TbsWebFileReaderFragment.class)
                .setNewActivity(true)
                .putString(KEY_FILE_PATH, path)
                .open(activity);
    }
}
