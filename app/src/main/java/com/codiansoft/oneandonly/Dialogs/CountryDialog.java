package com.codiansoft.oneandonly.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.Button;

import com.codiansoft.oneandonly.R;

/**
 * Created by Codiansoft on 3/7/2018.
 */

public class CountryDialog extends Dialog{
    public Activity activity;
    public Dialog dialog;
    public Button ok;
    public CountryDialog(Activity activity) {
        super(activity);

        this.activity=activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(R.layout.custom_dialog);


    }
}
