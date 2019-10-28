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

package com.xuexiang.xui.widget.imageview.edit;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * 文字编辑
 *
 * @author xuexiang
 * @since 2019-10-28 10:28
 */
public class TextStyleBuilder {

    private Map<TextStyle, Object> values = new HashMap<>();

    protected Map<TextStyle, Object> getValues() {
        return values;
    }

    /**
     * Set this textSize style
     *
     * @param size Size to apply on text
     */
    public void withTextSize(@NonNull float size) {
        values.put(TextStyle.SIZE, size);
    }

    /**
     * Set this color style
     *
     * @param color Color to apply on text
     */
    public void withTextColor(@NonNull int color) {
        values.put(TextStyle.COLOR, color);
    }

    /**
     * Set this {@link Typeface} style
     *
     * @param textTypeface TypeFace to apply on text
     */
    public void withTextFont(@NonNull Typeface textTypeface) {
        values.put(TextStyle.FONT_FAMILY, textTypeface);
    }

    /**
     * Set this gravity style
     *
     * @param gravity Gravity style to apply on text
     */
    public void withGravity(@NonNull int gravity) {
        values.put(TextStyle.GRAVITY, gravity);
    }

    /**
     * Set this background color
     *
     * @param background Background color to apply on text, this method overrides the preview set on {@link TextStyleBuilder#withBackgroundDrawable(Drawable)}
     */
    public void withBackgroundColor(@NonNull int background) {
        values.put(TextStyle.BACKGROUND, background);
    }

    /**
     * Set this background {@link Drawable}, this method overrides the preview set on {@link TextStyleBuilder#withBackgroundColor(int)}
     *
     * @param bgDrawable Background drawable to apply on text
     */
    public void withBackgroundDrawable(@NonNull Drawable bgDrawable) {
        values.put(TextStyle.BACKGROUND, bgDrawable);
    }

    /**
     * Set this textAppearance style
     *
     * @param textAppearance Text style to apply on text
     */
    public void withTextAppearance(@NonNull int textAppearance) {
        values.put(TextStyle.TEXT_APPEARANCE, textAppearance);
    }

    /**
     * Method to apply all the style setup on this Builder}
     *
     * @param textView TextView to apply the style
     */
    void applyStyle(@NonNull TextView textView) {
        for (Map.Entry<TextStyle, Object> entry : values.entrySet()) {
            switch (entry.getKey()) {
                case SIZE: {
                    final float size = (float) entry.getValue();
                    applyTextSize(textView, size);
                }
                break;

                case COLOR: {
                    final int color = (int) entry.getValue();
                    applyTextColor(textView, color);
                }
                break;

                case FONT_FAMILY: {
                    final Typeface typeface = (Typeface) entry.getValue();
                    applyFontFamily(textView, typeface);
                }
                break;

                case GRAVITY: {
                    final int gravity = (int) entry.getValue();
                    applyGravity(textView, gravity);
                }
                break;

                case BACKGROUND: {
                    if (entry.getValue() instanceof Drawable) {
                        final Drawable bg = (Drawable) entry.getValue();
                        applyBackgroundDrawable(textView, bg);

                    } else if (entry.getValue() instanceof Integer) {
                        final int color = (Integer) entry.getValue();
                        applyBackgroundColor(textView, color);
                    }
                }
                break;

                case TEXT_APPEARANCE: {
                    if (entry.getValue() instanceof Integer) {
                        final int styleAppearance = (Integer) entry.getValue();
                        applyTextAppearance(textView, styleAppearance);
                    }
                }
                break;
            }
        }
    }

    protected void applyTextSize(TextView textView, float size) {
        textView.setTextSize(size);
    }

    protected void applyTextColor(TextView textView, int color) {
        textView.setTextColor(color);
    }

    protected void applyFontFamily(TextView textView, Typeface typeface) {
        textView.setTypeface(typeface);
    }

    protected void applyGravity(TextView textView, int gravity) {
        textView.setGravity(gravity);
    }

    protected void applyBackgroundColor(TextView textView, int color) {
        textView.setBackgroundColor(color);
    }

    protected void applyBackgroundDrawable(TextView textView, Drawable bg) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.setBackground(bg);
        } else {
            textView.setBackgroundDrawable(bg);
        }
    }

    protected void applyTextAppearance(TextView textView, int styleAppearance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(styleAppearance);
        } else {
            textView.setTextAppearance(textView.getContext(), styleAppearance);
        }
    }

    /**
     * Enum to maintain current supported style properties used on on {@link PhotoEditor#addText(String, TextStyleBuilder)} and {@link PhotoEditor#editText(View, String, TextStyleBuilder)}
     */
    protected enum TextStyle {
        SIZE("TextSize"),
        COLOR("TextColor"),
        GRAVITY("Gravity"),
        FONT_FAMILY("FontFamily"),
        BACKGROUND("Background"),
        TEXT_APPEARANCE("TextAppearance");

        TextStyle(String property) {
            this.property = property;
        }

        private String property;

        public String getProperty() {
            return property;
        }
    }
}