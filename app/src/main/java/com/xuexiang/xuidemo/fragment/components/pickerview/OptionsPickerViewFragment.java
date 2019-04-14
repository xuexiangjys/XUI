package com.xuexiang.xuidemo.fragment.components.pickerview;

import android.view.View;
import android.widget.Button;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;
import com.xuexiang.xui.widget.picker.widget.listener.OnOptionsSelectListener;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author XUE
 * @since 2019/3/29 11:07
 */
@Page(name = "OptionsPickerView\n条件选择器--自定义选择条件")
public class OptionsPickerViewFragment extends BaseFragment {
    @BindView(R.id.btn_sex_picker)
    Button btnSexPicker;
    @BindView(R.id.btn_class_picker)
    Button btnClassPicker;
    private String[] mSexOption;
    private int sexSelectOption = 0;

    private String[] mGradeOption;
    private String[] mClassOption;
    private int gradeSelectOption = 0;
    private int classSelectOption = 0;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_options_pickerview;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        mSexOption = ResUtils.getStringArray(R.array.sex_option);
        mGradeOption = ResUtils.getStringArray(R.array.grade_option);
        mClassOption = new String[30];
        for (int i = 0; i < mClassOption.length; i++) {
            mClassOption[i] = (i + 1) + "班";
        }
    }

    @OnClick({R.id.btn_sex_picker, R.id.btn_class_picker})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sex_picker:
                showSexPickerView();
                break;
            case R.id.btn_class_picker:
                showClassPickerView();
                break;
        }
    }


    /**
     * 性别选择
     */
    private void showSexPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                btnSexPicker.setText(mSexOption[options1]);
                sexSelectOption = options1;
            }
        })
                .setTitleText(getString(R.string.title_sex_select))
                .setSelectOptions(sexSelectOption)
                .build();
        pvOptions.setPicker(mSexOption);
        pvOptions.show();
    }


    /**
     * 班级选择
     */
    private void showClassPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                btnClassPicker.setText(String.format("%s%s", mGradeOption[options1], mClassOption[options2]));
                gradeSelectOption = options1;
                classSelectOption = options2;
            }
        })
                .setTitleText(getString(R.string.title_grade_class_select))
                .setSelectOptions(gradeSelectOption, classSelectOption)
                .build();
        pvOptions.setNPicker(mGradeOption, mClassOption);
        pvOptions.show();
    }

}
