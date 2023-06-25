/*
 * Copyright (C) 2023 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.components.refresh.sample.preload.adapter.mock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xuexiang.xui.logs.UILog;

import java.util.concurrent.TimeUnit;

/**
 * @author xuexiang
 * @since 6/21/23 1:24 AM
 */
public final class InflateUtils {

    private InflateUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 模拟耗时加载
     */
    public static View mockLongTimeLoad(@NonNull ViewGroup parent, int layoutId) {
        try {
            // 模拟耗时
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getInflateView(parent, layoutId);
    }

    public static View getInflateView(@NonNull ViewGroup parent, int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    /**
     * 模拟耗时加载
     */
    public static View mockLongTimeLoad(LayoutInflater inflater, ViewGroup parent, int layoutId) {
        long startNanos = System.nanoTime();
        try {
            // 模拟耗时
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        View view = inflater.inflate(layoutId, parent, false);
        long cost = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
        UILog.dTag("InflateUtils", "mockLongTimeLoad cost:" + cost + " ms");
        return view;
    }
}
