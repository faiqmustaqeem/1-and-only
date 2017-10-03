package com.codiansoft.oneandonly.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.codiansoft.oneandonly.GlobalClass;
import com.codiansoft.oneandonly.R;

/**
 * Created by salal-khan on 7/13/2017.
 */

public class AddPostTypeDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity act;
    public Dialog d;
    Button bSubmit;
    RadioGroup rgAddPostType;
    RadioButton rbAlternateLoc, rbDefaultLoc, rbSelected;

    public AddPostTypeDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.act = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_post_type_dialog);
        setCanceledOnTouchOutside(false);

        initUI();
    }

    private void initUI() {
        rgAddPostType = (RadioGroup) findViewById(R.id.rgAddPostType);
        rbAlternateLoc = (RadioButton) findViewById(R.id.rbSendToAlternateLoc);
        rbDefaultLoc = (RadioButton) findViewById(R.id.rbSendToDefaultLoc);

        bSubmit = (Button) findViewById(R.id.bSubmit);
        bSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSubmit:
                int selectedRb = rgAddPostType.getCheckedRadioButtonId();
                rbSelected = (RadioButton) findViewById(selectedRb);
                GlobalClass.selectedAddPostType = rbSelected.getText().toString();
                dismiss();
                break;

            default:
                break;
        }
    }

}
