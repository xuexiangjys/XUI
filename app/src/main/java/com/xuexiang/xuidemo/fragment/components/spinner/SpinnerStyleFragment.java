package com.xuexiang.xuidemo.fragment.components.spinner;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.xuexiang.xaop.annotation.MemoryCache;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.spinner.editspinner.EditSpinner;
import com.xuexiang.xui.widget.spinner.editspinner.EditSpinnerAdapter;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.widget.EditSpinnerDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2018/11/26 下午3:17
 */
@Page(name = "统一的下拉框样式")
public class SpinnerStyleFragment extends BaseFragment {
    @BindView(R.id.spinner_system_fit_offset)
    Spinner mSpinnerFitOffset;

    @BindView(R.id.spinner_system)
    Spinner mSpinnerSystem;

    @BindView(R.id.spinner)
    MaterialSpinner mMaterialSpinner;

    @BindView(R.id.ms_custom)
    MaterialSpinner mMaterialSpinnerCustom;

    @BindView(R.id.spinner_one)
    MaterialSpinner mMaterialSpinnerOne;

    @BindView(R.id.editSpinner)
    EditSpinner mEditSpinner;

    @BindView(R.id.editSpinner1)
    EditSpinner mEditSpinner1;

    @BindView(R.id.btn_enable)
    SuperButton mBtEnable;

    private boolean mWidgetEnable = true;

    String data = "";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_spinner_style;
    }

    @Override
    protected void initViews() {
        KeyboardUtils.setSoftInputAdjustResize(getActivity()); //修改输入法模式

        WidgetUtils.setSpinnerDropDownVerticalOffset(mSpinnerFitOffset);
        WidgetUtils.initSpinnerStyle(mSpinnerSystem, ResUtils.getStringArray(getContext(), R.array.sort_mode_entry));

        mMaterialSpinner.setItems(ResUtils.getStringArray(getContext(), R.array.sort_mode_entry));
        mMaterialSpinner.setOnItemSelectedListener((spinner, position, id, item) -> SnackbarUtils.Long(spinner, "Clicked " + item).show());
        mMaterialSpinner.setOnNothingSelectedListener(spinner -> SnackbarUtils.Long(spinner, "Nothing selected").show());
//        mMaterialSpinner.setSelectedIndex(1);
        mMaterialSpinner.setSelectedItem("综合排序");

        //注意自定义实体，需要重写对象的toString方法
        List<AdapterItem> list = getAdapterItems();
        mMaterialSpinnerCustom.setItems(list);
        mMaterialSpinnerCustom.setSelectedItem(list.get(1));

        mMaterialSpinnerOne.setOnNoMoreChoiceListener(spinner -> XToastUtils.toast("没有更多的选项！"));

        //注意自定义实体，需要重写对象的toString方法
        mEditSpinner1.setAdapter(new EditSpinnerAdapter<>(list)
                .setTextColor(ResUtils.getColor(getContext(), R.color.color_green))
                .setTextSize(mEditSpinner1.getEditText().getTextSize())
                .setIsFilterKey(true)
                .setFilterColor("#FFFF00")
                .setBackgroundSelector(R.drawable.selector_custom_spinner_bg)
        );
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        WidgetUtils.initSpinnerStyle(mSpinnerSystem);
    }

    @MemoryCache
    public List<AdapterItem> getAdapterItems() {
        List<AdapterItem> list = new ArrayList<>();
        String[] array = ResUtils.getStringArray(getContext(), R.array.sort_mode_entry);
        for (String s : array) {
            list.add(new AdapterItem(s));
        }
        return list;
    }

    @Override
    protected void initListeners() {
        mBtEnable.setOnClickListener(v -> {
            mWidgetEnable = !mWidgetEnable;
            mSpinnerFitOffset.setEnabled(mWidgetEnable);
            mSpinnerSystem.setEnabled(mWidgetEnable);
            mMaterialSpinner.setEnabled(mWidgetEnable);
            mEditSpinner.setEnabled(mWidgetEnable);

        });
    }

    @OnClick(R.id.btn_dialog)
    void onClick(View v) {
        showEditSpinnerDialog(getContext(), "排序顺序", data, ResUtils.getStringArray(getContext(), R.array.sort_mode_entry), value -> data = value);
    }

    /**
     * 显示spinner编辑弹窗
     *
     * @param context
     * @param title
     * @param defaultItems
     * @param listener
     * @return
     */
    public static EditSpinnerDialog showEditSpinnerDialog(Context context, String title, String data, String[] defaultItems, EditSpinnerDialog.OnEditListener listener) {
        return EditSpinnerDialog.newBuilder(context)
                .setTitle(title).setText(data)
                .setDefaultItems(defaultItems)
                .setOnEditListener(listener)
                .show();
    }

    @Override
    public void onDestroyView() {
        KeyboardUtils.setSoftInputAdjustPan(getActivity());
        super.onDestroyView();
    }

}
