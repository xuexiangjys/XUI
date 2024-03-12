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
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ThemeUtils;

import java.lang.reflect.Field;

/**
 * Tints widgets
 */
@SuppressLint("PrivateResource")
public class MDTintHelper {

    public static void setTint(@NonNull RadioButton radioButton, @NonNull ColorStateList colors) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            radioButton.setButtonTintList(colors);
        } else {
            Drawable radioDrawable =
                    ContextCompat.getDrawable(radioButton.getContext(), R.drawable.abc_btn_radio_material);
            Drawable d = DrawableCompat.wrap(radioDrawable);
            DrawableCompat.setTintList(d, colors);
            radioButton.setButtonDrawable(d);
        }
    }

    public static void setTint(@NonNull RadioButton radioButton, @ColorInt int color) {
        final int disabledColor = ThemeUtils.getDisabledColor(radioButton.getContext());
        ColorStateList sl =
                new ColorStateList(
                        new int[][]{
                                new int[]{android.R.attr.state_enabled, -android.R.attr.state_checked},
                                new int[]{android.R.attr.state_enabled, android.R.attr.state_checked},
                                new int[]{-android.R.attr.state_enabled, -android.R.attr.state_checked},
                                new int[]{-android.R.attr.state_enabled, android.R.attr.state_checked}
                        },
                        new int[]{
                                ThemeUtils.resolveColor(radioButton.getContext(), R.attr.colorControlNormal),
                                color,
                                disabledColor,
                                disabledColor
                        });
        setTint(radioButton, sl);
    }

    public static void setTint(@NonNull CheckBox box, @NonNull ColorStateList colors) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            box.setButtonTintList(colors);
        } else {
            Drawable checkDrawable =
                    ContextCompat.getDrawable(box.getContext(), R.drawable.abc_btn_check_material);
            Drawable drawable = DrawableCompat.wrap(checkDrawable);
            DrawableCompat.setTintList(drawable, colors);
            box.setButtonDrawable(drawable);
        }
    }

    public static void setTint(@NonNull CheckBox box, @ColorInt int color) {
        final int disabledColor = ThemeUtils.getDisabledColor(box.getContext());
        ColorStateList sl =
                new ColorStateList(
                        new int[][]{
                                new int[]{android.R.attr.state_enabled, -android.R.attr.state_checked},
                                new int[]{android.R.attr.state_enabled, android.R.attr.state_checked},
                                new int[]{-android.R.attr.state_enabled, -android.R.attr.state_checked},
                                new int[]{-android.R.attr.state_enabled, android.R.attr.state_checked}
                        },
                        new int[]{
                                ThemeUtils.resolveColor(box.getContext(), R.attr.colorControlNormal),
                                color,
                                disabledColor,
                                disabledColor
                        });
        setTint(box, sl);
    }

    public static void setTint(@NonNull SeekBar seekBar, @ColorInt int color) {
        ColorStateList s1 = ColorStateList.valueOf(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            seekBar.setThumbTintList(s1);
            seekBar.setProgressTintList(s1);
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            Drawable progressDrawable = DrawableCompat.wrap(seekBar.getProgressDrawable());
            seekBar.setProgressDrawable(progressDrawable);
            DrawableCompat.setTintList(progressDrawable, s1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Drawable thumbDrawable = DrawableCompat.wrap(seekBar.getThumb());
                DrawableCompat.setTintList(thumbDrawable, s1);
                seekBar.setThumb(thumbDrawable);
            }
        } else {
            PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
                mode = PorterDuff.Mode.MULTIPLY;
            }
            if (seekBar.getIndeterminateDrawable() != null) {
                seekBar.getIndeterminateDrawable().setColorFilter(color, mode);
            }
            if (seekBar.getProgressDrawable() != null) {
                seekBar.getProgressDrawable().setColorFilter(color, mode);
            }
        }
    }

    public static void setTint(@NonNull ProgressBar progressBar, @ColorInt int color) {
        setTint(progressBar, color, false);
    }

    private static void setTint(
            @NonNull ProgressBar progressBar, @ColorInt int color, boolean skipIndeterminate) {
        ColorStateList sl = ColorStateList.valueOf(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setProgressTintList(sl);
            progressBar.setSecondaryProgressTintList(sl);
            if (!skipIndeterminate) {
                progressBar.setIndeterminateTintList(sl);
            }
        } else {
            PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
                mode = PorterDuff.Mode.MULTIPLY;
            }
            if (!skipIndeterminate && progressBar.getIndeterminateDrawable() != null) {
                progressBar.getIndeterminateDrawable().setColorFilter(color, mode);
            }
            if (progressBar.getProgressDrawable() != null) {
                progressBar.getProgressDrawable().setColorFilter(color, mode);
            }
        }
    }

    private static ColorStateList createEditTextColorStateList(
            @NonNull Context context, @ColorInt int color) {
        int[][] states = new int[3][];
        int[] colors = new int[3];
        int i = 0;
        states[i] = new int[]{-android.R.attr.state_enabled};
        colors[i] = ThemeUtils.resolveColor(context, R.attr.colorControlNormal);
        i++;
        states[i] = new int[]{-android.R.attr.state_pressed, -android.R.attr.state_focused};
        colors[i] = ThemeUtils.resolveColor(context, R.attr.colorControlNormal);
        i++;
        states[i] = new int[]{};
        colors[i] = color;
        return new ColorStateList(states, colors);
    }

    @SuppressLint("RestrictedApi")
    public static void setTint(@NonNull EditText editText, @ColorInt int color) {
        ColorStateList editTextColorStateList =
                createEditTextColorStateList(editText.getContext(), color);
        if (editText instanceof AppCompatEditText) {
            //noinspection RestrictedApi
            ((AppCompatEditText) editText).setSupportBackgroundTintList(editTextColorStateList);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setBackgroundTintList(editTextColorStateList);
        }
        setCursorTint(editText, color);
    }

    private static void setCursorTint(@NonNull EditText editText, @ColorInt int color) {
        try {
            Field fCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);
            int mCursorDrawableRes = fCursorDrawableRes.getInt(editText);
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(editText);
            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);
            Drawable[] drawables = new Drawable[2];
            drawables[0] = ContextCompat.getDrawable(editText.getContext(), mCursorDrawableRes);
            drawables[1] = ContextCompat.getDrawable(editText.getContext(), mCursorDrawableRes);
            if (drawables[0] != null) {
                drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
            if (drawables[1] != null) {
                drawables[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
            fCursorDrawable.set(editor, drawables);
        } catch (NoSuchFieldException e1) {
            Log.d("MDTintHelper", "Device issue with cursor tinting: " + e1.getMessage());
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
