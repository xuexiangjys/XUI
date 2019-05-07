package com.xuexiang.xuidemo.fragment.components.edittext;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.validation.RegexpValidator;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

/**
 * @author XUE
 * @date 2017/9/29 11:09
 */
@Page(name = "MaterialEditText\nMaterial Design风格的输入框")
public class MaterialEditTextFragment extends BaseFragment {

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
        enableBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basicEt.setEnabled(!basicEt.isEnabled());
                enableBt.setText(basicEt.isEnabled() ? "DISABLE" : "ENABLE");
            }
        });
    }

    private void initSingleLineEllipsisEt() {
        EditText singleLineEllipsisEt = findViewById(R.id.singleLineEllipsisEt);
        singleLineEllipsisEt.setSelection(singleLineEllipsisEt.getText().length());
    }

    private void initSetErrorEt() {
        final EditText bottomTextEt = findViewById(R.id.bottomTextEt);
        final Button setErrorBt = findViewById(R.id.setErrorBt);
        setErrorBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomTextEt.setError("1-line Error!");
            }
        });
        final Button setError2Bt = findViewById(R.id.setError2Bt);
        setError2Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomTextEt.setError("2-line\nError!");
            }
        });
        final Button setError3Bt = findViewById(R.id.setError3Bt);
        setError3Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomTextEt.setError("So Many Errors! So Many Errors! So Many Errors! So Many Errors! So Many Errors! So Many Errors! So Many Errors! So Many Errors!");
            }
        });
    }

    private void initValidationEt() {
        final MaterialEditText validationEt = findViewById(R.id.validationEt);
        final MaterialEditText et_phone_number = findViewById(R.id.et_phone_number);
        validationEt.addValidator(new RegexpValidator("Only Integer Valid!", "\\d+"));
        final Button validateBt = findViewById(R.id.validateBt);
        validateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationEt.validate();
                et_phone_number.validate();
            }
        });
    }
}
