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

import com.xuexiang.xuidemo.fragment.components.refresh.sample.preload.adapter.async.AsyncInflater;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

/**
 * @author xuexiang
 * @since 6/21/23 1:41 AM
 */
public class PreInflateHelper {

    /**
     * 默认的预加载缓存池大小，默认是5，可根据需求设置
     */
    public static final int DEFAULT_PRELOAD_COUNT = 5;

    private final ViewCache mViewCache = new ViewCache();

    private IAsyncInflater mAsyncInflater = new AsyncInflater();

    public void preloadOnce(@NonNull ViewGroup parent, int layoutId) {
        preloadOnce(parent, layoutId, DEFAULT_PRELOAD_COUNT);
    }

    public void preloadOnce(@NonNull ViewGroup parent, int layoutId, int maxCount) {
        preload(parent, layoutId, maxCount, 1);
    }

    public void preload(@NonNull ViewGroup parent, int layoutId) {
        preload(parent, layoutId, DEFAULT_PRELOAD_COUNT, 0);
    }

    public void preload(@NonNull ViewGroup parent, int layoutId, int maxCount, int forcePreCount) {
        int viewsAvailableCount = mViewCache.getViewPoolAvailableCount(layoutId);
        if (viewsAvailableCount >= maxCount) {
            return;
        }
        int needPreloadCount = maxCount - viewsAvailableCount;
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

    public View getView(@NonNull ViewGroup parent, int layoutId) {
        return getView(parent, layoutId, DEFAULT_PRELOAD_COUNT);
    }

    public View getView(@NonNull ViewGroup parent, int layoutId, int maxCount) {
        View view = mViewCache.getView(layoutId);
        if (view != null) {
            preloadOnce(parent, layoutId, maxCount);
            return view;
        }
        final View[] inflateView = new View[1];
        final CountDownLatch latch = new CountDownLatch(1);
        mAsyncInflater.asyncInflateView(parent, layoutId, new InflateCallback() {
            @Override
            public void onInflateFinished(int layoutId, View view) {
                inflateView[0] = view;
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return inflateView[0];
    }

    public PreInflateHelper setAsyncInflater(IAsyncInflater asyncInflater) {
        mAsyncInflater = asyncInflater;
        return this;
    }

    private static class ViewCache {

        private final SparseArray<LinkedList<SoftReference<View>>> mViewPools = new SparseArray<>();

        @NonNull
        public LinkedList<SoftReference<View>> getViewPool(int layoutId) {
            LinkedList<SoftReference<View>> views = mViewPools.get(layoutId);
            if (views == null) {
                views = new LinkedList<>();
                mViewPools.put(layoutId, views);
            }
            return views;
        }

        public int getViewPoolAvailableCount(int layoutId) {
            LinkedList<SoftReference<View>> views = getViewPool(layoutId);
            Iterator<SoftReference<View>> it = views.iterator();
            int count = 0;
            while (it.hasNext()) {
                if (it.next().get() != null) {
                    count++;
                } else {
                    it.remove();
                }
            }
            return count;
        }

        public void putView(int layoutId, View view) {
            if (view == null) {
                return;
            }
            getViewPool(layoutId).offer(new SoftReference<>(view));
        }

        @Nullable
        public View getView(int layoutId) {
            return getViewFromPool(getViewPool(layoutId));
        }

        private View getViewFromPool(@NonNull LinkedList<SoftReference<View>> views) {
            if (views.isEmpty()) {
                return null;
            }
            View target = views.pop().get();
            if (target == null) {
                return getViewFromPool(views);
            }
            return target;
        }
    }

    public interface IAsyncInflater {

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
