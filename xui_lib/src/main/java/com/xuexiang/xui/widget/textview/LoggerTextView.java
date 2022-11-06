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
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
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

    /**
     * 日志格式化接口
     */
    private ILogFormatter mLogFormatter = new DefaultLogFormatter();

    /**
     * 日志装饰接口
     */
    private ILogDecorator mLogDecorator = new DefaultLogDecorator();

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
     * 设置日志格式化接口
     *
     * @param logFormatter 日志格式化接口
     * @return 日志打印显示控件
     */
    public LoggerTextView setLogFormatter(@NonNull ILogFormatter logFormatter) {
        mLogFormatter = logFormatter;
        return this;
    }


    /**
     * 设置日志装饰接口
     *
     * @param logDecorator 日志装饰接口
     * @return 日志打印显示控件
     */
    public LoggerTextView setLogDecorator(@NonNull ILogDecorator logDecorator) {
        mLogDecorator = logDecorator;
        return this;
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
     * 添加自定义等级日志
     *
     * @param logContent 日志内容
     */

    public void logCustom(String logContent) {
        addLog(logContent, LogType.CUSTOM);
    }

    /**
     * 添加日志
     *
     * @param logContent 日志内容
     * @param logType    日志类型
     */
    public void addLog(String logContent, LogType logType) {
        SpannableString spannableString = getLogDecorator().decorate(getContext(), getLogFormatter().format(logContent, logType), logType);
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


    /**
     * 获取日志装饰接口
     *
     * @return 日志装饰接口
     */
    public ILogDecorator getLogDecorator() {
        if (mLogDecorator == null) {
            mLogDecorator = new DefaultLogDecorator();
        }
        return mLogDecorator;
    }

    /**
     * 获取日志格式化接口
     *
     * @return 日志格式化接口
     */
    public ILogFormatter getLogFormatter() {
        if (mLogFormatter == null) {
            mLogFormatter = new DefaultLogFormatter();
        }
        return mLogFormatter;
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
        Layout layout = getLayout();
        return (layout != null ? layout.getLineTop(getLineCount()) : 0) + getCompoundPaddingTop() + getCompoundPaddingBottom();
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
     * 默认日志格式化接口
     *
     * @author xuexiang
     * @since 2021/4/14 1:48 AM
     */
    public static class DefaultLogFormatter implements ILogFormatter {

        @Override
        public String format(String logContent, LogType logType) {
            return logContent;
        }
    }

    /**
     * 日志格式化接口
     *
     * @author xuexiang
     * @since 2021/4/14 1:30 AM
     */
    public interface ILogFormatter {
        /**
         * 格式化日志内容
         *
         * @param logContent 日志内容
         * @param logType    日志类型
         * @return 格式化后的日志
         */
        String format(String logContent, LogType logType);
    }


    /**
     * 默认日志装饰接口
     *
     * @author xuexiang
     * @since 2021/4/14 1:49 AM
     */
    public static class DefaultLogDecorator implements ILogDecorator {

        @Override
        public SpannableString decorate(@NonNull Context context, String logContent, LogType logType) {
            SpannableString spannableString = new SpannableString(logContent);
            switch (logType) {
                case ERROR:
                    spannableString.setSpan(new ForegroundColorSpan(ResUtils.getColor(context, R.color.xui_config_color_error)),
                            0,
                            logContent.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                case SUCCESS:
                    spannableString.setSpan(new ForegroundColorSpan(ResUtils.getColor(context, R.color.xui_config_color_success)),
                            0,
                            logContent.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                case WARNING:
                    spannableString.setSpan(new ForegroundColorSpan(ResUtils.getColor(context, R.color.xui_config_color_waring)),
                            0,
                            logContent.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                default:
                    break;
            }
            return spannableString;
        }
    }

    /**
     * 日志装饰接口
     *
     * @author xuexiang
     * @since 2021/4/14 1:30 AM
     */
    public interface ILogDecorator {
        /**
         * 装饰日志内容
         *
         * @param context    上下文
         * @param logContent 日志内容
         * @param logType    日志类型
         * @return 装饰后的日志
         */
        SpannableString decorate(Context context, String logContent, LogType logType);
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
        WARNING,
        /**
         * 自定义等级日志
         */
        CUSTOM
    }


}
