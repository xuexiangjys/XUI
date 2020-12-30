/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xui.widget.textview;

import android.content.Context;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ResUtils;

/**
 * 日志打印显示控件
 *
 * @author xuexiang
 * @since 2020/12/10 12:32 AM
 */
public class LoggerTextView extends AppCompatTextView {

    public LoggerTextView(Context context) {
        this(context, null);
    }

    public LoggerTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.LoggerTextViewStyle);
    }

    public LoggerTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setMovementMethod(new ScrollingMovementMethod());
    }

    /**
     * 添加普通日志
     *
     * @param logContent 日志内容
     */
    public void logNormal(String logContent) {
        addLog(logContent, LogType.NORMAL);
    }

    /**
     * 添加成功日志
     *
     * @param logContent 日志内容
     */
    public void logSuccess(String logContent) {
        addLog(logContent, LogType.SUCCESS);
    }

    /**
     * 添加错误日志
     *
     * @param logContent 日志内容
     */
    public void logError(String logContent) {
        addLog(logContent, LogType.ERROR);
    }


    /**
     * 添加警告日志
     *
     * @param logContent 日志内容
     */

    public void logWarning(String logContent) {
        addLog(logContent, LogType.WARNING);
    }

    /**
     * 添加日志
     *
     * @param logContent 日志内容
     * @param logType    日志类型
     */
    public void addLog(String logContent, LogType logType) {
        SpannableString spannableString = new SpannableString(logContent);
        switch (logType) {
            case ERROR:
                spannableString.setSpan(new ForegroundColorSpan(ResUtils.getColor(R.color.xui_config_color_error)),
                        0,
                        logContent.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case SUCCESS:
                spannableString.setSpan(new ForegroundColorSpan(ResUtils.getColor(R.color.xui_config_color_success)),
                        0,
                        logContent.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case WARNING:
                spannableString.setSpan(new ForegroundColorSpan(ResUtils.getColor(R.color.xui_config_color_waring)),
                        0,
                        logContent.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            default:
                break;
        }
        appendLogInMainThread(spannableString);
    }

    private void appendLogInMainThread(final SpannableString spannableString) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            appendLog(spannableString);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    appendLog(spannableString);
                }
            });
        }
    }

    private void appendLog(SpannableString spannableString) {
        append(spannableString);
        append("\r\n");
        scrollToEnd();
    }

    /**
     * 滑动至最后一行
     */
    private void scrollToEnd() {
        int offset = getTextRealHeight();
        if (offset > getHeight()) {
            scrollTo(0, offset - getHeight());
        }
    }

    /**
     * @return 获取当前TextView文字的真实高度
     */
    private int getTextRealHeight() {
        return getLayout().getLineTop(getLineCount()) + getCompoundPaddingTop() + getCompoundPaddingBottom();
    }

    /**
     * 清除日志
     */
    public void clearLog() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            setText("");
            scrollTo(0, 0);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    setText("");
                    scrollTo(0, 0);
                }
            });
        }
    }

    /**
     * 日志类型
     */
    public enum LogType {
        /**
         * 普通日志
         */
        NORMAL,
        /**
         * 成功日志
         */
        SUCCESS,
        /**
         * 出错日志
         */
        ERROR,
        /**
         * 警告日志
         */
        WARNING
    }


}
