package com.xuexiang.xuidemo.fragment.components.spinner;

import android.content.Context;
import android.view.View;
import android.widget.Spinner;

import com.xuexiang.xpage.PageConfig;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.actionbar.TitleUtils;
import com.xuexiang.xui.widget.spinner.editspinner.EditSpinner;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.SnackbarUtils;
import com.xuexiang.xuidemo.widget.EditSpinnerDialog;
import com.xuexiang.xutil.tip.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2018/11/26 下午3:17
 */
@Page(name = "统一的下拉框样式")
public class SpinnerStyleFragment extends BaseFragment {
    @BindView(R.id.titlebar)
    TitleBar mTitleBar;

    @BindView(R.id.spinner_system_fit_offset)
    Spinner mSpinnerFitOffset;

    @BindView(R.id.spinner_system)
    Spinner mSpinnerSystem;

    @BindView(R.id.spinner)
    MaterialSpinner mMaterialSpinner;

    @BindView(R.id.spinner_one)
    MaterialSpinner mMaterialSpinnerOne;

    @BindView(R.id.editSpinner)
    EditSpinner mEditSpinner;

    @BindView(R.id.btn_enable)
    SuperButton mBtEnable;

    private boolean mWidgetEnable = true;

    String data = "";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_spinner_style;
    }

    @Override
    protected TitleBar initTitle() {
        mTitleBar = TitleUtils.initTitleBarStyle(mTitleBar, PageConfig.getPageInfo(getClass()).getName(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popToBack();
            }
        });
        return mTitleBar;
    }

    @Override
    protected void initViews() {
        WidgetUtils.setSpinnerDropDownVerticalOffset(mSpinnerFitOffset);
        WidgetUtils.initSpinnerStyle(mSpinnerSystem, ResUtils.getStringArray(R.array.sort_mode_entry));

        mMaterialSpinner.setItems(ResUtils.getStringArray(R.array.sort_mode_entry));
        mMaterialSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner spinner, int position, long id, Object item) {
                SnackbarUtils.Long(spinner, "Clicked " + item).show();
            }
        });
        mMaterialSpinner.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                SnackbarUtils.Long(spinner, "Nothing selected").show();
            }
        });
//        mMaterialSpinner.setSelectedIndex(1);

        mMaterialSpinner.setSelectedItem("根据床号降序排序");

        mMaterialSpinnerOne.setOnNoMoreChoiceListener(new MaterialSpinner.OnNoMoreChoiceListener() {
            @Override
            public void OnNoMoreChoice(MaterialSpinner spinner) {
                ToastUtils.toast("没有更多的选项！");
            }
        });

    }

    @Override
    protected void initListeners() {
        mBtEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWidgetEnable = !mWidgetEnable;
                mSpinnerFitOffset.setEnabled(mWidgetEnable);
                mSpinnerSystem.setEnabled(mWidgetEnable);
                mMaterialSpinner.setEnabled(mWidgetEnable);
                mEditSpinner.setEnabled(mWidgetEnable);

            }
        });
    }

    @OnClick(R.id.btn_dialog)
    void onClick(View v) {
        showEditSpinnerDialog(getContext(), "排序顺序", data, ResUtils.getStringArray(R.array.sort_mode_entry), new EditSpinnerDialog.OnEditListener() {
            @Override
            public void OnEdit(String value) {
                data = value;
            }
        });
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
}
