package com.codiansoft.oneandonly;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class EditMyAdActivity extends AppCompatActivity {

    EditText etTitle, etDescription, etPrice;
    TextView tvCurrencyCode;
    Button bUpdate;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_ad);
        getSupportActionBar().setTitle("Edit Ad");
        initUI();

        bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validInputs()) {
                    updateAd();
                }
            }
        });
    }

    private boolean validInputs() {
        if (etTitle.getText().toString().equals("")) {
            etTitle.setError("Invalid title");
            return false;
        } else if (etDescription.getText().toString().equals("")) {
            etDescription.setError("Invalid description");
            return false;
        } else if (etPrice.getText().toString().equals("") & isNumeric(etPrice.getText().toString())) {
            etPrice.setError("Invalid price");
            return false;
        }
        return true;
    }

    private boolean isNumeric(String s) {
        try {
            double d = Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void updateAd() {
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(EditMyAdActivity.this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.EDIT_MY_AD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                Intent i = new Intent(EditMyAdActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    EditMyAdActivity.this.finishAffinity();
                                }
                                Toast.makeText(EditMyAdActivity.this, "Your ad is updated", Toast.LENGTH_SHORT).show();

                            } else if (result.get("status").equals("error")) {
                                Toast.makeText(EditMyAdActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ee) {
                            Toast.makeText(EditMyAdActivity.this, "error: " + ee.toString(), Toast.LENGTH_SHORT).show();
                        }

                        if (progressBar.isShown()) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 409:
//                                    utilities.dialog("Already Exist", act);
                                    break;
                                case 400:
                                    Toast.makeText(EditMyAdActivity.this, "Try again", Toast.LENGTH_SHORT).show();
//                                    utilities.dialog("Connection Problem", act);
                                    break;
                                default:
//                                    utilities.dialog("Connection Problem", act);
                                    break;
                            }
                        }
                        if (progressBar.isShown()) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String userID = settings.getString("userID", "defaultValue");
                String apiSecretKey = settings.getString("apiSecretKey", "");
                String email = settings.getString("email", "");
                String contactNum1 = settings.getString("contactNum1", "");
                String contactNum2 = settings.getString("contactNum2", "");

                Map<String, String> params = new HashMap<String, String>();
                params.put("api_secret", apiSecretKey);
                params.put("ad_id", GlobalClass.myAdEditID);
                params.put("title", etTitle.getText().toString());
                params.put("desc", etDescription.getText().toString());
                params.put("price", etPrice.getText().toString());

                return params;
            }
        };
        queue.add(postRequest);

    }

    private void initUI() {
        etTitle = (EditText) findViewById(R.id.etTitle);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etPrice = (EditText) findViewById(R.id.etPrice);
        tvCurrencyCode = (TextView) findViewById(R.id.tvCurrencyCode);
        bUpdate = (Button) findViewById(R.id.bUpdate);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.bringToFront();

        etTitle.setText(GlobalClass.myAdEditTitle);
        etDescription.setText(GlobalClass.myAdEditDescription);
        etPrice.setText(GlobalClass.myAdEditPrice);
        tvCurrencyCode.setText(GlobalClass.myAdEditCurrencyCode);
    }
}