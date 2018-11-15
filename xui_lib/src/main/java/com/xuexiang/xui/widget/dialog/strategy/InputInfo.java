/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xui.widget.dialog.strategy;

/**
 * 输入信息
 *
 * @author xuexiang
 * @since 2018/11/16 上午12:18
 */
public class InputInfo {

    /**
     * 输入类型
     */
    private int mInputType;
    /**
     * 输入的提示
     */
    private String mHint;
    /**
     * 预先输入的内容
     */
    private String mPreFill;
    /**
     * 是否允许输入为空
     */
    private boolean mAllowEmptyInput;

    public InputInfo(int inputType) {
        mInputType = inputType;
        mHint = "";
        mPreFill = "";
        mAllowEmptyInput = false;
    }

    public InputInfo(int inputType, String hint) {
        mInputType = inputType;
        mHint = hint;
        mPreFill = "";
        mAllowEmptyInput = false;
    }

    public InputInfo(int inputType, String hint, String preFill, boolean allowEmptyInput) {
        mInputType = inputType;
        mHint = hint;
        mPreFill = preFill;
        mAllowEmptyInput = allowEmptyInput;
    }

    public int getInputType() {
        return mInputType;
    }

    public InputInfo setInputType(int inputType) {
        mInputType = inputType;
        return this;
    }

    public String getHint() {
        return mHint;
    }

    public InputInfo setHint(String hint) {
        mHint = hint;
        return this;
    }

    public String getPreFill() {
        return mPreFill;
    }

    public InputInfo setPreFill(String preFill) {
        mPreFill = preFill;
        return this;
    }

    public boolean isAllowEmptyInput() {
        return mAllowEmptyInput;
    }

    public InputInfo setAllowEmptyInput(boolean allowEmptyInput) {
        mAllowEmptyInput = allowEmptyInput;
        return this;
    }

    @Override
    public String toString() {
        return "InputInfo{" +
                "mInputType=" + mInputType +
                ", mHint='" + mHint + '\'' +
                ", mPreFill='" + mPreFill + '\'' +
                ", mAllowEmptyInput=" + mAllowEmptyInput +
                '}';
    }
}
