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

package com.xuexiang.xuidemo.widget.iconfont;

import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.mikepenz.iconics.Iconics;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.typeface.ITypeface;
import com.xuexiang.xaop.annotation.MemoryCache;
import com.xuexiang.xuidemo.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * XUI字体图标库，是使用 <a href="https://www.iconfont.cn/"> 平台自动生成的
 *
 * @author xuexiang
 * @since 2019-10-13 16:29
 */
public class XUIIconFont implements ITypeface {

    private static Map<String, Character> sChars;

    @NonNull
    @Override
    public String getAuthor() {
        return "xuexiangjys";
    }

    @NonNull
    @Override
    public Map<String, Character> getCharacters() {
        if (sChars == null) {
            Map<String, Character> aChars = new HashMap<>();
            for (Icon v : Icon.values()) {
                aChars.put(v.name(), v.character);
            }
            sChars = aChars;
        }
        return sChars;
    }

    @NonNull
    @Override
    public String getDescription() {
        return "XUI字体图标库";
    }

    @NonNull
    @Override
    public String getFontName() {
        return "XUIIconFont";
    }

    @Override
    public int getIconCount() {
        return Icon.values().length;
    }

    @NonNull
    @Override
    public List<String> getIcons() {
        List<String> icons = new LinkedList<>();
        for (Icon value : Icon.values()) {
            icons.add(value.name());
        }
        return icons;
    }

    @NonNull
    @Override
    public String getLicense() {
        return "";
    }

    @NonNull
    @Override
    public String getLicenseUrl() {
        return "";
    }

    @NonNull
    @Override
    public String getMappingPrefix() {
        return "xui";
    }

    @NonNull
    @Override
    public String getUrl() {
        return "https://github.com/xuexiangjys/XUI";
    }

    @NonNull
    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @NonNull
    @Override
    public IIcon getIcon(@NonNull String key) {
        return Icon.valueOf(key);
    }

    private static ITypeface sITypeface;

    @Override
    public int getFontRes() {
        return R.font.iconfont;
    }

    @NotNull
    @Override
    public Typeface getRawTypeface() {
        return ResourcesCompat.getFont(Iconics.getApplicationContext(), getFontRes());
    }

    /**
     * 图标枚举
     */
    public enum Icon implements IIcon {

        xui_file('\ue600'),
        xui_chat('\ue601'),
        xui_voice('\ue602'),
        xui_delete('\ue603'),
        xui_delete1('\ue613'),
        xui_delete2('\ue630'),
        xui_delete3('\ue658'),
        xui_back('\ue609'),
        xui_back1('\ue614'),
        xui_add('\ue612'),
        xui_add1('\ue615'),
        xui_add2('\ue631'),
        xui_reset('\ue616'),
        xui_complete('\ue650'),
        xui_complete1('\ue673'),
        xui_collect('\ue77f'),
        xui_emoj('\ue628');

        char character;

        Icon(char character) {
            this.character = character;
        }

        @Override
        public char getCharacter() {
            return character;
        }

        @Override
        public String getFormattedName() {
            return "{" + name() + "}";
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public ITypeface getTypeface() {
            if (sITypeface == null) {
                sITypeface = new XUIIconFont();
            }
            return sITypeface;
        }

        public static Icon get(String name) {
            try {
                return Icon.valueOf("xui_" + name);
            } catch (Exception e) {
            }
            return null;
        }

        public String getIconText() {
            return name().replace("xui_", "");
        }

        @MemoryCache
        public static List<String> getAllIconTexts() {
            List<String> icons = new LinkedList<>();
            for (Icon value : Icon.values()) {
                icons.add(value.getIconText());
            }
            return icons;
        }


    }
}
