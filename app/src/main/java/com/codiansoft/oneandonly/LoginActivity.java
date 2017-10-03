package com.codiansoft.oneandonly;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
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
import com.codiansoft.oneandonly.dialog.RegisterDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.codiansoft.oneandonly.GlobalClass.alreadyLoggedIn;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail, etPassword;
    Button bLogin;
    TextView tvRegister;
    public static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
    }

    private void initUI() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.bringToFront();
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        bLogin = (Button) findViewById(R.id.bLogin);
        bLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bLogin:
/*
                alreadyLoggedIn = true;
                Intent i = new Intent(this,MainActivity.class);
                startActivity(i);*/

                if (validInputs()) {
                    loginUser();
                }


                break;
            case R.id.tvRegister:
                RegisterDialog registerDialog = new RegisterDialog(this);
                registerDialog.show();
                break;
        }
    }

    private boolean validInputs() {
        boolean check = false;
        if (etEmail.getText().toString().equals("")) {
            etEmail.setError("This field is required");
            check = false;
        } else if (etPassword.getText().toString().equals("")) {
            etPassword.setError("This field is required");
            check = false;
        } else if (etPassword.getText().toString().length() < 6) {
            etPassword.setError("Minimum 6 characters allowed");
            check = false;
        } else {
            check = true;
        }
        return check;
    }

    private void loginUser() {

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                JSONObject Data = Jobject.getJSONObject("data");
                                JSONObject userData = Data.getJSONObject("user data");

                                SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("apiSecretKey", Data.getString("api_secret"));
                                editor.putString("userID", userData.getString("user_id"));
                                editor.putString("email", userData.getString("email"));
                                if(userData.getString("mobile_number") != null) editor.putString("contactNum1", userData.getString("mobile_number"));
                                else editor.putString("contactNum1", "");
                                if(userData.getString("phone_number") != null) editor.putString("contactNum2", userData.getString("phone_number"));
                                else editor.putString("contactNum2", "");
                                editor.putString("country", userData.getString("country"));
                                editor.putString("state", userData.getString("state_id"));
                                editor.putString("city", userData.getString("city"));
                                editor.commit();

                                GlobalClass.userNotRegisteredYet = false;
                                alreadyLoggedIn = true;
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();

                            } else if (result.get("status").equals("error")) {
                                if (result.getString("response").equals("Invalid Email Address.")) {
                                    etEmail.setError("Please enter a valid email address");
                                }else if (result.getString("response").equals("Invalid Crediantials.")) {
                                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                }
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
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 409:
//                                    utilities.dialog("Already Exist", act);
                                    break;
                                case 400:
                                    Toast.makeText(LoginActivity.this, "Try again", Toast.LENGTH_SHORT).show();
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
                params.put("email", etEmail.getText().toString());
                params.put("password", etPassword.getText().toString());
                return params;
            }
        };
        queue.add(postRequest);
    }
}
