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

package com.xuexiang.xuidemo.fragment.components.refresh.sample.preload.adapter;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedList;

/**
 * @author xuexiang
 * @since 6/21/23 1:41 AM
 */
public class PreInflateHelper {

    public static final int DEFAULT_PRELOAD_COUNT = 5;

    private ViewCache mViewCache = new ViewCache();

    private AsyncInflater mAsyncInflater;

    public void preloadOnce(@NonNull ViewGroup parent, int layoutId, int maxCount) {
        preload(parent, layoutId, maxCount, 1);
    }

    public void preload(@NonNull ViewGroup parent, int layoutId, int maxCount, int forcePreCount) {
        LinkedList<View> views = mViewCache.getViewPool(layoutId);
        if (views.size() >= maxCount) {
            return;
        }
        int needPreloadCount = maxCount - views.size();
        if (forcePreCount > 0) {
            needPreloadCount = Math.min(forcePreCount, needPreloadCount);
        }
        for (int i = 0; i < needPreloadCount; i++) {
            // 异步加载View
            asyncInflateView(parent, layoutId);
        }
    }

    private void asyncInflateView(@NonNull ViewGroup parent, int layoutId) {
        mAsyncInflater.asyncInflateView(parent, layoutId, new InflateCallback() {
            @Override
            public void onInflateFinished(int layoutId, View view) {
                mViewCache.putView(layoutId, view);
            }
        });
    }

//    public View getView(@NonNull ViewGroup parent, int layoutId, int maxCount) {
//        View view = mViewCache.getView(layoutId);
//        if (view != null) {
//            preloadOnce(parent, layoutId, maxCount);
//            return view;
//        }
//    }

    public PreInflateHelper setAsyncInflater(AsyncInflater asyncInflater) {
        mAsyncInflater = asyncInflater;
        return this;
    }


    private static class ViewCache {

        private SparseArray<LinkedList<View>> mViewPools = new SparseArray<>();

        @NonNull
        public LinkedList<View> getViewPool(int layoutId) {
            LinkedList<View> views = mViewPools.get(layoutId);
            if (views == null) {
                views = new LinkedList<>();
                mViewPools.put(layoutId, views);
            }
            return views;
        }

        public void putView(int layoutId, View view) {
            if (view == null) {
                return;
            }
            getViewPool(layoutId).offer(view);
        }

        @Nullable
        public View getView(int layoutId) {
            LinkedList<View> views = getViewPool(layoutId);
            if (views.isEmpty()) {
                return null;
            }
            return views.pop();
        }
    }

    public interface AsyncInflater {

        /**
         * 异步加载View
         *
         * @param parent
         * @param layoutId
         * @param callback 加载回调
         */
        void asyncInflateView(@NonNull ViewGroup parent, int layoutId, InflateCallback callback);
    }


    public interface InflateCallback {

        void onInflateFinished(int layoutId, View view);
    }


}
