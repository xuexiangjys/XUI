package com.xuexiang.xuidemo.fragment.components.edittext;

import android.widget.Button;
import android.widget.EditText;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.validation.RegexpValidator;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;

/**
 * @author XUE
 * @date 2017/9/29 11:09
 */
@Page(name = "MaterialEditText\nMaterial Design风格的输入框")
public class MaterialEditTextFragment extends BaseFragment {

    @BindView(R.id.et_not_allow_empty1)
    MaterialEditText etNotAllowEmpty1;
    @BindView(R.id.et_not_allow_empty2)
    MaterialEditText etNotAllowEmpty2;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_material_edittext;
    }

    @Override
    protected void initViews() {
        initEnableBt();
        initSingleLineEllipsisEt();
        initSetErrorEt();
        initValidationEt();
    }

    @Override
    protected void initListeners() {

    }

    private void initEnableBt() {
        final EditText basicEt = findViewById(R.id.basicEt);
        final Button enableBt = findViewById(R.id.enableBt);
        enableBt.setOnClickListener(v -> {
            basicEt.setEnabled(!basicEt.isEnabled());
            enableBt.setText(basicEt.isEnabled() ? "DISABLE" : "ENABLE");
        });
    }

    private void initSingleLineEllipsisEt() {
        EditText singleLineEllipsisEt = findViewById(R.id.singleLineEllipsisEt);
        singleLineEllipsisEt.setSelection(singleLineEllipsisEt.getText().length());
    }

    private void initSetErrorEt() {
        final EditText bottomTextEt = findViewById(R.id.bottomTextEt);
        final Button setErrorBt = findViewById(R.id.setErrorBt);
        setErrorBt.setOnClickListener(v -> bottomTextEt.setError("1-line Error!"));
        final Button setError2Bt = findViewById(R.id.setError2Bt);
        setError2Bt.setOnClickListener(v -> bottomTextEt.setError("2-line\nError!"));
        final Button setError3Bt = findViewById(R.id.setError3Bt);
        setError3Bt.setOnClickListener(v -> bottomTextEt.setError("So Many Errors! So Many Errors! So Many Errors! So Many Errors! So Many Errors! So Many Errors! So Many Errors! So Many Errors!"));
    }

    private void initValidationEt() {
        final MaterialEditText validationEt = findViewById(R.id.validationEt);
        final MaterialEditText et_phone_number = findViewById(R.id.et_phone_number);
        etNotAllowEmpty2.setAllowEmpty(false, "your name not allow empty！");
        validationEt.addValidator(new RegexpValidator("Only Integer Valid!", "\\d+"));
        final Button validateBt = findViewById(R.id.validateBt);
        validateBt.setOnClickListener(v -> {
            validationEt.validate();
            et_phone_number.validate();
            etNotAllowEmpty1.validate();
            etNotAllowEmpty2.validate();
        });
    }

}
