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

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.core.util.Pools.SynchronizedPool;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 模拟耗时的布局异步加载，copy from AsyncLayoutInflater
 *
 * @author xuexiang
 * @since 6/21/23 11:06 PM
 */
public final class MockLongTimeAsyncLayoutInflater {
    
    private static final String TAG = "AsyncLayoutInflater";

    LayoutInflater mInflater;
    Handler mHandler;
    InflateThread mInflateThread;

    public MockLongTimeAsyncLayoutInflater(@NonNull Context context) {
        mInflater = new BasicInflater(context);
        mHandler = new Handler(mHandlerCallback);
        mInflateThread = InflateThread.getInstance();
    }

    @UiThread
    public void inflate(@LayoutRes int resid, @Nullable ViewGroup parent,
                        @NonNull OnInflateFinishedListener callback) {
        if (callback == null) {
            throw new NullPointerException("callback argument may not be null!");
        }
        InflateRequest request = mInflateThread.obtainRequest();
        request.inflater = this;
        request.resid = resid;
        request.parent = parent;
        request.callback = callback;
        mInflateThread.enqueue(request);
    }

    private Callback mHandlerCallback = new Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            InflateRequest request = (InflateRequest) msg.obj;
            if (request.view == null) {
                request.view = mInflater.inflate(
                        request.resid, request.parent, false);
            }
            request.callback.onInflateFinished(
                    request.view, request.resid, request.parent);
            mInflateThread.releaseRequest(request);
            return true;
        }
    };

    public interface OnInflateFinishedListener {
        void onInflateFinished(@NonNull View view, @LayoutRes int resid,
                               @Nullable ViewGroup parent);
    }

    private static class InflateRequest {
        MockLongTimeAsyncLayoutInflater inflater;
        ViewGroup parent;
        int resid;
        View view;
        OnInflateFinishedListener callback;

        InflateRequest() {
        }
    }

    private static class BasicInflater extends LayoutInflater {
        private static final String[] sClassPrefixList = {
                "android.widget.",
                "android.webkit.",
                "android.app."
        };

        BasicInflater(Context context) {
            super(context);
        }

        @Override
        public LayoutInflater cloneInContext(Context newContext) {
            return new BasicInflater(newContext);
        }

        @Override
        protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
            for (String prefix : sClassPrefixList) {
                try {
                    View view = createView(name, prefix, attrs);
                    if (view != null) {
                        return view;
                    }
                } catch (ClassNotFoundException e) {
                    // In this case we want to let the base class take a crack
                    // at it.
                }
            }

            return super.onCreateView(name, attrs);
        }
    }

    private static class InflateThread extends Thread {
        private static final InflateThread sInstance;

        static {
            sInstance = new InflateThread();
            sInstance.start();
        }

        public static InflateThread getInstance() {
            return sInstance;
        }

        private ArrayBlockingQueue<InflateRequest> mQueue = new ArrayBlockingQueue<>(10);
        private SynchronizedPool<InflateRequest> mRequestPool = new SynchronizedPool<>(10);

        // Extracted to its own method to ensure locals have a constrained liveness
        // scope by the GC. This is needed to avoid keeping previous request references
        // alive for an indeterminate amount of time, see b/33158143 for details
        public void runInner() {
            InflateRequest request;
            try {
                request = mQueue.take();
            } catch (InterruptedException ex) {
                // Odd, just continue
                Log.w(TAG, ex);
                return;
            }
            try {
                // 模拟耗时加载
                request.view = InflateUtils.mockLongTimeLoad(request.inflater.mInflater,
                        request.parent, request.resid);
            } catch (RuntimeException ex) {
                // Probably a Looper failure, retry on the UI thread
                Log.w(TAG, "Failed to inflate resource in the background! Retrying on the UI"
                        + " thread", ex);
            }
            Message.obtain(request.inflater.mHandler, 0, request)
                    .sendToTarget();
        }

        @Override
        public void run() {
            while (true) {
                runInner();
            }
        }

        public InflateRequest obtainRequest() {
            InflateRequest obj = mRequestPool.acquire();
            if (obj == null) {
                obj = new InflateRequest();
            }
            return obj;
        }

        public void releaseRequest(InflateRequest obj) {
            obj.callback = null;
            obj.inflater = null;
            obj.parent = null;
            obj.resid = 0;
            obj.view = null;
            mRequestPool.release(obj);
        }

        public void enqueue(InflateRequest request) {
            try {
                mQueue.put(request);
            } catch (InterruptedException e) {
                throw new RuntimeException(
                        "Failed to enqueue async inflate request", e);
            }
        }
    }
}
