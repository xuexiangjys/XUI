package com.xuexiang.xui.widget.spinner.materialspinner;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.xuexiang.xui.R;
import com.xuexiang.xui.logs.UILog;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.utils.Utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义下拉框，使用AppCompatTextView + PopupWindow组合实现
 *
 * @author xuexiang
 * @since 2018/12/7 下午4:24
 */
public class MaterialSpinner extends AppCompatTextView {

    private OnNothingSelectedListener onNothingSelectedListener;
    private OnItemSelectedListener onItemSelectedListener;
    private OnNoMoreChoiceListener onNoMoreChoiceListener;
    private MaterialSpinnerBaseAdapter adapter;
    private PopupWindow popupWindow;
    private ListView listView;
    private Drawable arrowDrawable;
    private boolean hideArrow;
    private boolean nothingSelected;
    private int popupWindowMaxHeight;
    private int popupWindowHeight;
    private int selectedIndex;
    private int backgroundColor;
    private int backgroundSelector;
    private int arrowColor;
    private int arrowColorDisabled;
    private int textColor;
    private int entriesID;
    private Drawable dropDownBg;
    private boolean isInDialog;

    private int dropDownOffset;

    public MaterialSpinner(Context context) {
        super(context);
        init(context, null);
    }

    public MaterialSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MaterialSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MaterialSpinner);
        int defaultColor = getTextColors().getDefaultColor();
        boolean rtl = ResUtils.isRtl();

        try {
            backgroundColor = ta.getColor(R.styleable.MaterialSpinner_ms_background_color, Color.WHITE);
            backgroundSelector = ta.getResourceId(R.styleable.MaterialSpinner_ms_background_selector, 0);
            textColor = ta.getColor(R.styleable.MaterialSpinner_ms_text_color, defaultColor);
            arrowColor = ta.getColor(R.styleable.MaterialSpinner_ms_arrow_tint, textColor);
            hideArrow = ta.getBoolean(R.styleable.MaterialSpinner_ms_hide_arrow, false);
            popupWindowMaxHeight = ta.getDimensionPixelSize(R.styleable.MaterialSpinner_ms_dropdown_max_height, 0);
            popupWindowHeight = ta.getLayoutDimension(R.styleable.MaterialSpinner_ms_dropdown_height, WindowManager.LayoutParams.WRAP_CONTENT);
            arrowColorDisabled = ResUtils.lighter(arrowColor, 0.8f);
            entriesID = ta.getResourceId(R.styleable.MaterialSpinner_ms_entries, 0);
            dropDownBg = ta.getDrawable(R.styleable.MaterialSpinner_ms_dropdown_bg);
            isInDialog = ta.getBoolean(R.styleable.MaterialSpinner_ms_in_dialog, false);

        } finally {
            ta.recycle();
        }

        int left, right, bottom, top;
        left = right = bottom = top = ThemeUtils.resolveDimension(getContext(), R.attr.ms_padding_top_size);
        if (rtl) {
            right = ThemeUtils.resolveDimension(getContext(), R.attr.ms_padding_left_size);
        } else {
            left = ThemeUtils.resolveDimension(getContext(), R.attr.ms_padding_left_size);
        }

        dropDownOffset = ThemeUtils.resolveDimension(getContext(), R.attr.ms_dropdown_offset);

        setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        setClickable(true);
        setPadding(left, top, right, bottom);
        setBackgroundResource(R.drawable.ms_background_selector);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && rtl) {
            setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            setTextDirection(View.TEXT_DIRECTION_RTL);
        }

        if (!hideArrow) {
            arrowDrawable = ResUtils.getDrawable(getContext(), R.drawable.ms_ic_arrow_up).mutate();
            arrowDrawable.setColorFilter(arrowColor, PorterDuff.Mode.SRC_IN);
            int arrowSize = ThemeUtils.resolveDimension(getContext(), R.attr.ms_arrow_size);
            arrowDrawable.setBounds(0, 0, arrowSize, arrowSize);
            if (rtl) {
                setCompoundDrawablesWithIntrinsicBounds(arrowDrawable, null, null, null);
            } else {
                setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDrawable, null);
            }
        }

        listView = new ListView(context);
        listView.setId(getId());
        listView.setDivider(null);
        listView.setItemsCanFocus(true);
        int padding = ThemeUtils.resolveDimension(getContext(), R.attr.ms_dropdown_offset);
        listView.setPadding(padding, padding, padding, padding);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= selectedIndex && position < adapter.getCount()) {
                    position++;
                }
                selectedIndex = position;
                nothingSelected = false;
                Object item = adapter.get(position);
                adapter.notifyItemSelected(position);
                setText(item.toString());
                collapse();
                if (onItemSelectedListener != null) {
                    //noinspection unchecked
                    onItemSelectedListener.onItemSelected(MaterialSpinner.this, position, id, item);
                }
            }
        });
        if (entriesID != 0) {
            setItems(ResUtils.getStringArray(entriesID));
        }

        popupWindow = new PopupWindow(context);
        popupWindow.setContentView(listView);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(16);
        }

        if (dropDownBg != null) {
            popupWindow.setBackgroundDrawable(dropDownBg);
        } else {
            popupWindow.setBackgroundDrawable(ResUtils.getDrawable(getContext(), R.drawable.ms_drop_down_bg));
        }

        if (backgroundColor != Color.WHITE) { // default color is white
            setBackgroundColor(backgroundColor);
        } else if (backgroundSelector != 0) {
            setBackgroundResource(backgroundSelector);
        }
        if (textColor != defaultColor) {
            setTextColor(textColor);
        }

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                if (nothingSelected && onNothingSelectedListener != null) {
                    onNothingSelectedListener.onNothingSelected(MaterialSpinner.this);
                }
                if (!hideArrow) {
                    animateArrow(false);
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        popupWindow.setHeight(calculatePopupWindowHeight());
        if (adapter != null) {
            CharSequence currentText = getText();
            String longestItem = currentText.toString();
            for (int i = 0; i < adapter.getCount(); i++) {
                String itemText = adapter.getItemText(i);
                if (itemText.length() > longestItem.length()) {
                    longestItem = itemText;
                }
            }
            setText(longestItem);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setText(currentText);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        popupWindow.setWidth(getMeasuredWidth());
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isEnabled() && isClickable()) {
                if (!popupWindow.isShowing()) {
                    expand();
                } else {
                    collapse();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setBackgroundColor(int color) {
        backgroundColor = color;
        Drawable background = getBackground();
        if (background instanceof StateListDrawable) { // pre-L
            try {
                Method getStateDrawable = StateListDrawable.class.getDeclaredMethod("getStateDrawable", int.class);
                if (!getStateDrawable.isAccessible()) getStateDrawable.setAccessible(true);
                int[] colors = {ResUtils.darker(color, 0.85f), color};
                for (int i = 0; i < colors.length; i++) {
                    ColorDrawable drawable = (ColorDrawable) getStateDrawable.invoke(background, i);
                    drawable.setColor(colors[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                UILog.e(e);
            }
        } else if (background != null) { // 21+ (RippleDrawable)
            background.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
        popupWindow.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void setTextColor(int color) {
        textColor = color;
        super.setTextColor(color);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("state", super.onSaveInstanceState());
        bundle.putInt("selected_index", selectedIndex);
        if (popupWindow != null) {
            bundle.putBoolean("is_popup_showing", popupWindow.isShowing());
            collapse();
        } else {
            bundle.putBoolean("is_popup_showing", false);
        }
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable savedState) {
        if (savedState instanceof Bundle) {
            Bundle bundle = (Bundle) savedState;
            selectedIndex = bundle.getInt("selected_index");
            if (adapter != null) {
                setText(adapter.get(selectedIndex).toString());
                adapter.notifyItemSelected(selectedIndex);
            }
            if (bundle.getBoolean("is_popup_showing")) {
                if (popupWindow != null) {
                    // Post the show request into the looper to avoid bad token exception
                    post(new Runnable() {

                        @Override
                        public void run() {
                            expand();
                        }
                    });
                }
            }
            savedState = bundle.getParcelable("state");
        }
        super.onRestoreInstanceState(savedState);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (arrowDrawable != null) {
            arrowDrawable.setColorFilter(enabled ? arrowColor : arrowColorDisabled, PorterDuff.Mode.SRC_IN);
        }
    }

    /**
     * @return the selected item position
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Set the default spinner item using its index
     *
     * @param position the item's position
     */
    public void setSelectedIndex(int position) {
        if (adapter != null) {
            if (position >= 0 && position <= adapter.getCount()) {
                adapter.notifyItemSelected(position);
                selectedIndex = position;
                setText(adapter.get(position).toString());
            } else {
                throw new IllegalArgumentException("Position must be lower than adapter count!");
            }
        }
    }

    /**
     * 设置选中的内容
     *
     * @param item 选中的内容
     * @param <T>
     */
    public <T> void setSelectedItem(@NonNull T item) {
        if (adapter != null && item != null) {
            setSelectedIndex(getSpinnerPosition(item, adapter.getItems()));
        }
    }

    /**
     * 获取选中内容在Spinner中的位置
     *
     * @param item  选中的内容
     * @param items Spinner中选项的集合
     * @return
     */
    public <T> int getSpinnerPosition(T item, List<T> items) {
        int position = 0;
        if (item != null && items != null && items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                if (item.equals(items.get(i))) {
                    position = i;
                    break;
                }
            }
        }
        return position;
    }

    /**
     * Register a callback to be invoked when an item in the dropdown is selected.
     *
     * @param onItemSelectedListener The callback that will run
     */
    public void setOnItemSelectedListener(@Nullable OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    /**
     * Register a callback to be invoked when the {@link PopupWindow} is shown but the user didn't select an item.
     *
     * @param onNothingSelectedListener the callback that will run
     */
    public void setOnNothingSelectedListener(@Nullable OnNothingSelectedListener onNothingSelectedListener) {
        this.onNothingSelectedListener = onNothingSelectedListener;
    }

    /**
     * 设置无更多选择的监听
     *
     * @param onNoMoreChoiceListener
     * @return
     */
    public MaterialSpinner setOnNoMoreChoiceListener(OnNoMoreChoiceListener onNoMoreChoiceListener) {
        this.onNoMoreChoiceListener = onNoMoreChoiceListener;
        return this;
    }

    /**
     * Set the dropdown items
     *
     * @param items A list of items
     * @param <T>   The item type
     */
    public <T> void setItems(@NonNull T... items) {
        setItems(Arrays.asList(items));
    }

    /**
     * Set the dropdown items
     *
     * @param items A list of items
     * @param <T>   The item type
     */
    public <T> void setItems(@NonNull List<T> items) {
        adapter = new MaterialSpinnerAdapter<>(getContext(), items)
                .setTextColor(textColor).setTextSize(getTextSize());
        setAdapterInternal(adapter);
    }

    /**
     * Get the list of items in the adapter
     *
     * @param <T> The item type
     * @return A list of items or {@code null} if no items are set.
     */
    public <T> List<T> getItems() {
        if (adapter == null) {
            return null;
        }
        //noinspection unchecked
        return adapter.getItems();
    }

    /**
     * Get the Selected Item in the adapter
     *
     * @param <T> The item type
     * @return A list of items or {@code null} if no items are set.
     */
    public <T> T getSelectedItem() {
        return adapter != null ? (T) adapter.get(selectedIndex) : null;
    }

    /**
     * Set a custom adapter for the dropdown items
     *
     * @param adapter The list adapter
     */
    public void setAdapter(@NonNull ListAdapter adapter) {
        this.adapter = new MaterialSpinnerAdapterWrapper(getContext(), adapter)
                .setTextColor(textColor).setTextSize(getTextSize());
        setAdapterInternal(this.adapter);
    }

    /**
     * Set the custom adapter for the dropdown items
     *
     * @param adapter The adapter
     * @param <T>     The type
     */
    public <T> void setAdapter(MaterialSpinnerAdapter<T> adapter) {
        this.adapter = adapter;
        this.adapter.setTextColor(textColor);
        this.adapter.setTextSize(getTextSize());
        setAdapterInternal(adapter);
    }

    /**
     * 获取适配器
     *
     * @return
     */
    public MaterialSpinnerBaseAdapter getAdapter() {
        return adapter;
    }

    public MaterialSpinner setDropDownBackgroundSelector(@DrawableRes int backgroundSelector) {
        if (adapter != null) {
            adapter.setBackgroundSelector(backgroundSelector);
        }
        return this;
    }

    private void setAdapterInternal(@NonNull MaterialSpinnerBaseAdapter adapter) {
        listView.setAdapter(adapter);
        if (selectedIndex >= adapter.getCount()) {
            selectedIndex = 0;
        }
        if (adapter.getCount() >= 0) {
            setText(adapter.get(selectedIndex).toString());
        } else {
            setText("");
        }
    }

    /**
     * @return 是否有内容可以下拉选择
     */
    private boolean hasMoreChoice() {
        return adapter != null && adapter.getCount() > 0;
    }

    /**
     * Show the dropdown menu
     */
    public void expand() {
        if (!hasMoreChoice()) {
            if (onNoMoreChoiceListener != null) {
                onNoMoreChoiceListener.OnNoMoreChoice(this);
            }
            return;
        }

        if (!hideArrow) {
            animateArrow(true);
        }
        nothingSelected = true;
        showPopupWindow();
    }

    /**
     * Closes the dropdown menu
     */
    public void collapse() {
        if (!hideArrow) {
            animateArrow(false);
        }
        popupWindow.dismiss();
    }

    private void showPopupWindow() {
        if (isInDialog) {
            popupWindow.showAsDropDown(this);
        } else {
            popupWindow.showAsDropDown(this, 0, calculatePopWindowYOffset(this));
        }
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView 呼出window的view
     * @return window显示的左上角的xOff, yOff坐标
     */
    private int[] calculatePopWindowPos(final View anchorView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = Utils.getScreenHeight(getContext());
        // 计算ListView的高宽
        final int listViewHeight = Utils.getListViewHeightBasedOnChildren(listView);
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < listViewHeight);
        if (isNeedShowUp) {
            windowPos[0] = anchorLoc[0];
            windowPos[1] = anchorLoc[1] - listViewHeight - dropDownOffset;
        } else {
            windowPos[0] = anchorLoc[0];
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView 呼出window的view
     * @return window显示的左上角的xOff, yOff坐标
     */
    private int calculatePopWindowYOffset(final View anchorView) {
        int windowYOffset;
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = Utils.getScreenHeight(getContext());
        // 计算ListView的高宽
        final int listViewHeight = Utils.getListViewHeightBasedOnChildren(listView);
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] < listViewHeight + anchorHeight);
        if (isNeedShowUp) {
            windowYOffset = - (listViewHeight + dropDownOffset + anchorHeight);
        } else {
            windowYOffset = 0;
        }
        return windowYOffset;
    }

    /**
     * Set the tint color for the dropdown arrow
     *
     * @param color the color value
     */
    public void setArrowColor(@ColorInt int color) {
        arrowColor = color;
        arrowColorDisabled = ResUtils.lighter(arrowColor, 0.8f);
        if (arrowDrawable != null) {
            arrowDrawable.setColorFilter(arrowColor, PorterDuff.Mode.SRC_IN);
        }
    }

    private void animateArrow(boolean shouldRotateUp) {
        int start = shouldRotateUp ? 0 : 10000;
        int end = shouldRotateUp ? 10000 : 0;
        ObjectAnimator animator = ObjectAnimator.ofInt(arrowDrawable, "level", start, end);
        animator.start();
    }

    /**
     * Set the maximum height of the dropdown menu.
     *
     * @param height the height in pixels
     */
    public void setDropdownMaxHeight(int height) {
        popupWindowMaxHeight = height;
        popupWindow.setHeight(calculatePopupWindowHeight());
    }

    /**
     * Set the height of the dropdown menu
     *
     * @param height the height in pixels
     */
    public void setDropdownHeight(int height) {
        popupWindowHeight = height;
        popupWindow.setHeight(calculatePopupWindowHeight());
    }

    private int calculatePopupWindowHeight() {
        if (adapter == null) {
            return WindowManager.LayoutParams.WRAP_CONTENT;
        }
        float listViewHeight = adapter.getCount() * ThemeUtils.resolveDimension(getContext(), R.attr.ms_item_height_size);
        if (popupWindowMaxHeight > 0 && listViewHeight > popupWindowMaxHeight) {
            return popupWindowMaxHeight;
        } else if (popupWindowHeight != WindowManager.LayoutParams.MATCH_PARENT
                && popupWindowHeight != WindowManager.LayoutParams.WRAP_CONTENT
                && popupWindowHeight <= listViewHeight) {
            return popupWindowHeight;
        }
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    /**
     * Get the {@link PopupWindow}.
     *
     * @return The {@link PopupWindow} that is displayed when the view has been clicked.
     */
    public PopupWindow getPopupWindow() {
        return popupWindow;
    }

    /**
     * Get the {@link ListView} that is used in the dropdown menu
     *
     * @return the ListView shown in the PopupWindow.
     */
    public ListView getListView() {
        return listView;
    }

    /**
     * Interface definition for a callback to be invoked when an item in this view has been selected.
     *
     * @param <T> Adapter item type
     */
    public interface OnItemSelectedListener<T> {

        /**
         * <p>Callback method to be invoked when an item in this view has been selected. This callback is invoked only when
         * the newly selected position is different from the previously selected position or if there was no selected
         * item.</p>
         *
         * @param view     The {@link MaterialSpinner} view
         * @param position The position of the view in the adapter
         * @param id       The row id of the item that is selected
         * @param item     The selected item
         */
        void onItemSelected(MaterialSpinner view, int position, long id, T item);
    }

    /**
     * Interface definition for a callback to be invoked when the dropdown is dismissed and no item was selected.
     */
    public interface OnNothingSelectedListener {

        /**
         * Callback method to be invoked when the {@link PopupWindow} is dismissed and no item was selected.
         *
         * @param spinner the {@link MaterialSpinner}
         */
        void onNothingSelected(MaterialSpinner spinner);
    }

    /**
     * 无更多选择的监听
     */
    public interface OnNoMoreChoiceListener {
        /**
         * 无更多选择
         *
         * @param spinner
         */
        void OnNoMoreChoice(MaterialSpinner spinner);
    }

}
