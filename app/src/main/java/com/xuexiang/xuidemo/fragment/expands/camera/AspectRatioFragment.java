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

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.cameraview.AspectRatio;

import java.util.Arrays;
import java.util.Set;


/**
 * 横纵比选择弹窗
 */
public class AspectRatioFragment extends DialogFragment {

    private static final String ARG_ASPECT_RATIOS = "aspect_ratios";
    private static final String ARG_CURRENT_ASPECT_RATIO = "current_aspect_ratio";

    private Listener mListener;

    public static AspectRatioFragment newInstance(Set<AspectRatio> ratios,
                                                  AspectRatio currentRatio) {
        final AspectRatioFragment fragment = new AspectRatioFragment();
        final Bundle args = new Bundle();
        args.putParcelableArray(ARG_ASPECT_RATIOS,
                ratios.toArray(new AspectRatio[ratios.size()]));
        args.putParcelable(ARG_CURRENT_ASPECT_RATIO, currentRatio);
        fragment.setArguments(args);
        return fragment;
    }


    public static AspectRatioFragment newInstance(Set<AspectRatio> ratios,
                                                  AspectRatio currentRatio,
                                                  Listener listener) {
        final AspectRatioFragment fragment = new AspectRatioFragment();
        final Bundle args = new Bundle();
        args.putParcelableArray(ARG_ASPECT_RATIOS,
                ratios.toArray(new AspectRatio[ratios.size()]));
        args.putParcelable(ARG_CURRENT_ASPECT_RATIO, currentRatio);
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public AspectRatioFragment setListener(Listener listener) {
        mListener = listener;
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle args = getArguments();
        final AspectRatio[] ratios = (AspectRatio[]) args.getParcelableArray(ARG_ASPECT_RATIOS);
        if (ratios == null) {
            throw new RuntimeException("No ratios");
        }
        Arrays.sort(ratios);
        final AspectRatio current = args.getParcelable(ARG_CURRENT_ASPECT_RATIO);
        final AspectRatioAdapter adapter = new AspectRatioAdapter(ratios, current);
        return new AlertDialog.Builder(getActivity())
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        mListener.onAspectRatioSelected(ratios[position]);
                    }
                })
                .create();
    }

    private static class AspectRatioAdapter extends BaseAdapter {

        private final AspectRatio[] mRatios;
        private final AspectRatio mCurrentRatio;

        AspectRatioAdapter(AspectRatio[] ratios, AspectRatio current) {
            mRatios = ratios;
            mCurrentRatio = current;
        }

        @Override
        public int getCount() {
            return mRatios.length;
        }

        @Override
        public AspectRatio getItem(int position) {
            return mRatios[position];
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).hashCode();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            AspectRatioAdapter.ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                holder = new AspectRatioAdapter.ViewHolder();
                holder.text = view.findViewById(android.R.id.text1);
                view.setTag(holder);
            } else {
                holder = (AspectRatioAdapter.ViewHolder) view.getTag();
            }
            AspectRatio ratio = getItem(position);
            StringBuilder sb = new StringBuilder(ratio.toString());
            if (ratio.equals(mCurrentRatio)) {
                sb.append(" *");
            }
            holder.text.setText(sb);
            return view;
        }

        private static class ViewHolder {
            TextView text;
        }

    }

    public interface Listener {
        void onAspectRatioSelected(@NonNull AspectRatio ratio);
    }

}
