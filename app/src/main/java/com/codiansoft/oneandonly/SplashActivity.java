package com.codiansoft.oneandonly;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class SplashActivity extends AppCompatActivity {
    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // make fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                /*Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);*/

//                Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);

                SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String apiSecretKey = settings.getString("apiSecretKey", "");
                if (apiSecretKey.equals("")) {
                    GlobalClass.alreadyLoggedIn = false;
                    Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                } else {
                    GlobalClass.alreadyLoggedIn = true;
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    getUserCountry();
                }

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void getUserCountry() {

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                JSONObject user_data = result.getJSONObject("user_data");
                                GlobalClass.userCountryID = user_data.getString("country");
                                SplashActivity.this.finish();
                            }
                        } catch (Exception ee) {
                            Toast.makeText(getApplicationContext(), ee.toString(), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getApplicationContext(), "Error " + 409, Toast.LENGTH_SHORT).show();
//                                    utilities.dialog("Already Exist", act);
                                    break;
                                case 400:
//                                    utilities.dialog("Connection Problem", act);
                                    Toast.makeText(getApplicationContext(), "Error " + 400, Toast.LENGTH_SHORT).show();
                                    break;
                                default:
//                                    utilities.dialog("Connection Problem", act);
                                    Toast.makeText(getApplicationContext(), "Unexpected Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Unexpected Connection Error", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String userID = settings.getString("userID", "defaultValue");
                String apiSecretKey = settings.getString("apiSecretKey", "defaultValue");

                Map<String, String> params = new HashMap<String, String>();
                params.put("api_secret", apiSecretKey);
                return params;
            }
        };
        queue.add(postRequest);
    }
}