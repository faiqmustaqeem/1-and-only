package com.codiansoft.oneandonly;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.codiansoft.oneandonly.AdListViewActivity.selectedItemDataModel;

public class ReportAdActivity extends AppCompatActivity {

    RadioGroup rgReportType;
    Button reportBtn;
    ProgressBar progressBar;
    EditText etReportComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_ad);
        getSupportActionBar().hide();

        reportBtn = (Button) findViewById(R.id.bSubmitReport);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.bringToFront();
        rgReportType = (RadioGroup) findViewById(R.id.rgReportType);
        etReportComments = (EditText) findViewById(R.id.etReportComments);

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rgReportType.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(ReportAdActivity.this, "Select any 1 type", Toast.LENGTH_SHORT).show();
                } else {
                    reportAd();
                }
            }
        });
    }

    private void reportAd() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(ReportAdActivity.this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.REPORT_AD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {

                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                progressBar.setVisibility(View.GONE);

                                new android.support.v7.app.AlertDialog.Builder(ReportAdActivity.this)
                                        .setTitle("Report Submitted")
                                        .setMessage("Thank you for your cooperation. We will take a well founded action regarding your report ASAP.")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ReportAdActivity.this.finish();
                                            }
                                        }).show();


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
                                Toast.makeText(ReportAdActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ReportAdActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

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
                SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String apiSecretKey = settings.getString("apiSecretKey", "");

                params.put("api_secret", apiSecretKey);
                RadioButton rbSelected = (RadioButton) findViewById(rgReportType.getCheckedRadioButtonId());
                params.put("reason", rbSelected.getText().toString());
                params.put("ad_id", selectedItemDataModel.getID());
                params.put("comments", etReportComments.getText().toString());

                return params;
            }
        };
        queue.add(postRequest);
    }
}