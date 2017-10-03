package com.codiansoft.oneandonly.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codiansoft.oneandonly.GlobalClass;
import com.codiansoft.oneandonly.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Codiansoft on 8/3/2017.
 */

public class DeletePeriodDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity act;
    public Dialog d;
    Button bSubmit;
    EditText etDeletePeriod;
    ProgressBar progressBar;

    String newUserAPIsecret, newUserUserID, newUserEmail, newUserContact1, newUserContact2, newUserCountry, newUserState, newUserCity;

    public DeletePeriodDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.act = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.delete_period_dialog);
        setCanceledOnTouchOutside(false);

        Window window = getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        initUI();
        fetchActiveDays();
    }

    private void fetchActiveDays() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(act);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.AD_ACTIVE_DAYS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {

                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                etDeletePeriod.setText(result.getString("expire_days"));

                            } else if (result.get("status").equals("error")) {
                                Toast.makeText(act, "Error", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception ee) {

                        }

                        if (progressBar.isShown()) {
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        error.printStackTrace();

                        Log.e("VOLLEY", error.getMessage());
                        Toast.makeText(act, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 409:
//                                    utilities.dialog("Already Exist", act);
                                    break;
                                case 400:
//                                    utilities.dialog("Connection Problem", act);
                                    break;
                                default:
//                                    utilities.dialog("Connection Problem", act);
                                    break;
                            }
                        }
                        if (progressBar.isShown()) {
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences settings = act.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String apiSecretKey = settings.getString("apiSecretKey", "");

                params.put("api_secret", apiSecretKey);

                return params;
            }
        };
        queue.add(postRequest);
    }

    private void initUI() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.bringToFront();
        etDeletePeriod = (EditText) findViewById(R.id.etDeletePeriod);

        bSubmit = (Button) findViewById(R.id.bSave);
        bSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSubmit:
                if (validInputs()) {
                    updateActiveDays();
                } else {
                    Toast.makeText(act, "Invalid active period", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    private void updateActiveDays() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(act);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.AD_ACTIVE_DAYS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {

                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                dismiss();
                                Toast.makeText(act, "Days Updated", Toast.LENGTH_SHORT).show();

//                                JSONObject Data = Jobject.getJSONObject("data");
//                                JSONObject userData = Data.getJSONObject("user_data");
//
//                                GlobalClass.userNotRegisteredYet = false;
//                                alreadyLoggedIn = true;
//
//                                newUserAPIsecret = Data.getString("api_secret");
//                                newUserUserID = userData.getString("user_id");
//                                newUserEmail = userData.getString("email");
//                                newUserContact1 = userData.getString("mobile_number");
//                                newUserContact2 = userData.getString("phone_number");
//                                newUserCountry = userData.getString("country");
//                                newUserState = userData.getString("state_id");
//                                newUserCity = userData.getString("city");

                            } else if (result.get("status").equals("error")) {
                                Toast.makeText(act, "Error", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception ee) {

                        }

                        if (progressBar.isShown()) {
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        error.printStackTrace();

                        Log.e("VOLLEY", error.getMessage());
                        Toast.makeText(act, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 409:
//                                    utilities.dialog("Already Exist", act);
                                    break;
                                case 400:
//                                    utilities.dialog("Connection Problem", act);
                                    break;
                                default:
//                                    utilities.dialog("Connection Problem", act);
                                    break;
                            }
                        }
                        if (progressBar.isShown()) {
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences settings = act.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String apiSecretKey = settings.getString("apiSecretKey", "");

                params.put("api_secret", apiSecretKey);
                params.put("days", etDeletePeriod.getText().toString());

                return params;
            }
        };
        queue.add(postRequest);
    }

    private boolean validInputs() {
        boolean check = false;

        if (etDeletePeriod.getText().toString().equals("")) {
            etDeletePeriod.setError("Enter active days");
            check = false;
        } else if (isNumeric(etDeletePeriod.getText().toString())) {
            etDeletePeriod.setError("Invalid active days");
            check = false;
        } else {
            check = true;
        }
        return check;
    }

    private boolean isNumeric(String s) {
        try {
            double d = Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}