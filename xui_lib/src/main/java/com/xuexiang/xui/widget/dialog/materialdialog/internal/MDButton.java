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

package com.xuexiang.xui.widget.dialog.materialdialog.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.GravityEnum;

/**
 * @author Kevin Barry (teslacoil) 4/02/2015
 */
@SuppressLint("AppCompatCustomView")
public class MDButton extends TextView {

    private boolean stacked = false;
    private GravityEnum stackedGravity;

    private int stackedEndPadding;
    private Drawable stackedBackground;
    private Drawable defaultBackground;

    public MDButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MDButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        stackedEndPadding = ThemeUtils.resolveDimension(context, R.attr.md_dialog_frame_margin, R.dimen.default_md_dialog_frame_margin);
        stackedGravity = GravityEnum.END;
    }

    /**
     * Set if the button should be displayed in stacked mode. This should only be called from
     * MDRootLayout's onMeasure, and we must be measured after calling this.
     */
  /* package */ void setStacked(boolean stacked, boolean force) {
        if (this.stacked != stacked || force) {

            setGravity(
                    stacked ? (Gravity.CENTER_VERTICAL | stackedGravity.getGravityInt()) : Gravity.CENTER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                //noinspection ResourceType
                setTextAlignment(stacked ? stackedGravity.getTextAlignment() : TEXT_ALIGNMENT_CENTER);
            }

            ThemeUtils.setBackgroundCompat(this, stacked ? stackedBackground : defaultBackground);
            if (stacked) {
                setPadding(stackedEndPadding, getPaddingTop(), stackedEndPadding, getPaddingBottom());
            } /* Else the padding was properly reset by the drawable */

            this.stacked = stacked;
        }
    }

    public void setStackedGravity(GravityEnum gravity) {
        stackedGravity = gravity;
    }

    public void setStackedSelector(Drawable d) {
        stackedBackground = d;
        if (stacked) {
            setStacked(true, true);
        }
    }

    public void setDefaultSelector(Drawable d) {
        defaultBackground = d;
        if (!stacked) {
            setStacked(false, true);
        }
    }

    public void setAllCapsCompat(boolean allCaps) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setAllCaps(allCaps);
        } else {
            if (allCaps) {
                setTransformationMethod(new AllCapsTransformationMethod(getContext()));
            } else {
                setTransformationMethod(null);
            }
        }
    }
}
