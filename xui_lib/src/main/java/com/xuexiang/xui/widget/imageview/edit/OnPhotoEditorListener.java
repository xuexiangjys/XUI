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

import android.view.View;

/**
 * 图片编辑监听
 *
 * @author xuexiang
 * @since 2019-10-28 10:05
 */
public interface OnPhotoEditorListener {

    /**
     * When user long press the existing text this event will trigger implying that user want to
     * edit the current {@link android.widget.TextView}
     *
     * @param rootView  view on which the long press occurs
     * @param text      current text set on the view
     * @param colorCode current color value set on view
     */
    void onEditTextChangeListener(View rootView, String text, int colorCode);

    /**
     * This is a callback when user adds any view on the {@link PhotoEditorView} it can be
     * brush,text or sticker i.e bitmap on parent view
     *
     * @param viewType           enum which define type of view is added
     * @param numberOfAddedViews number of views currently added
     * @see ViewType
     */
    void onAddViewListener(ViewType viewType, int numberOfAddedViews);

    /**
     * This is a callback when user remove any view on the {@link PhotoEditorView} it happens when usually
     * undo and redo happens or text is removed
     *
     * @param viewType           enum which define type of view is added
     * @param numberOfAddedViews number of views currently added
     */
    void onRemoveViewListener(ViewType viewType, int numberOfAddedViews);

    /**
     * A callback when user start dragging a view which can be
     * any of {@link ViewType}
     *
     * @param viewType enum which define type of view is added
     */
    void onStartViewChangeListener(ViewType viewType);


    /**
     * A callback when user stop/up touching a view which can be
     * any of {@link ViewType}
     *
     * @param viewType enum which define type of view is added
     */
    void onStopViewChangeListener(ViewType viewType);
}
