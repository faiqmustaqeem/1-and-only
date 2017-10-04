package com.codiansoft.oneandonly.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.codiansoft.oneandonly.GlobalClass;
import com.codiansoft.oneandonly.Level1CountriesActivity;
import com.codiansoft.oneandonly.MainActivity;
import com.codiansoft.oneandonly.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.codiansoft.oneandonly.GlobalClass.alreadyLoggedIn;
import static com.codiansoft.oneandonly.adapter.CategoriesRegisterListViewAdapter.savedCategoriesDataModels;

/**
 * Created by salal-khan on 4/20/2017.
 */

public class RegisterDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity act;
    public Dialog d;
    Button bSubmit;
    EditText etName, etEmail, etPassword, etWebsite, etContact1, etContact2;
    ProgressBar progressBar;
    public static TextView tvSaveConfigurations;
    public static boolean savedConfigurations;

    String newUserAPIsecret, newUserUserID, newUserEmail, newUserContact1, newUserContact2, newUserCountry, newUserState, newUserCity;

    public RegisterDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.act = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_dialog);
        setCanceledOnTouchOutside(false);

        Window window = getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        GlobalClass.postingAd = false;

        initUI();
    }

    private void initUI() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.bringToFront();
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etWebsite = (EditText) findViewById(R.id.etWebsite);
        etContact1 = (EditText) findViewById(R.id.etContact1);
        etContact2 = (EditText) findViewById(R.id.etContact2);

        bSubmit = (Button) findViewById(R.id.bSubmit);
        bSubmit.setOnClickListener(this);
        tvSaveConfigurations = (TextView) findViewById(R.id.tvSaveConfigurations);
        tvSaveConfigurations.setOnClickListener(this);
        savedConfigurations = false;

//        bSubmit.setOnClickListener(new RegisterDialogCustomListener(d, act));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSubmit:

                if (savedConfigurations) {
                    if (validInputs()) {
                        /*Intent i = new Intent(act, MainActivity.class);
                        act.startActivity(i);*/
                        registerUser();
                    } else {
                        Toast.makeText(act, "Check fields", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(act, "No configurations saved", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tvSaveConfigurations:
                GlobalClass.userNotRegisteredYet = true;
                Intent level1Intent = new Intent(act, Level1CountriesActivity.class);
                act.startActivity(level1Intent);
                break;

            default:
                break;
        }
    }

    private void registerUser() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(act);

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://codiansoft.com:80/salal/OneAndOnly/index.php/api/user/register",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {

                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                JSONObject Data = Jobject.getJSONObject("data");
                                JSONObject userData = Data.getJSONObject("user_data");

                                GlobalClass.userNotRegisteredYet = false;
                                alreadyLoggedIn = true;

                                newUserAPIsecret = Data.getString("api_secret");
                                newUserUserID = userData.getString("user_id");
                                newUserEmail = userData.getString("email");
                                newUserContact1 = userData.getString("mobile_number");
                                newUserContact2 = userData.getString("phone_number");
                                newUserCountry = userData.getString("country");
                                newUserState = userData.getString("state_id");
                                newUserCity = userData.getString("city");

                                setCategoriesStatusForUser();

                            } else if (result.get("status").equals("error")) {
                                if (result.getString("response").equals("Invalid Email Address.")) {
                                    etEmail.setError("Please enter a valid email address");
                                    Toast.makeText(act, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                                } else if (result.getString("response").equals("Email Already Exists.")) {
                                    etEmail.setError("This email is already registered");
                                    Toast.makeText(act, "This email is already registered", Toast.LENGTH_SHORT).show();
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
                params.put("name", etName.getText().toString());
                params.put("email", etEmail.getText().toString());
                params.put("password", etPassword.getText().toString());
                params.put("phone_number", etContact1.getText().toString());
                params.put("mobile_number", etContact2.getText().toString());
                params.put("website", etWebsite.getText().toString() + "");
                params.put("country", GlobalClass.selectedLevel2CountryID);
                params.put("city", GlobalClass.selectedLevel2CityID);
                params.put("state_id", GlobalClass.selectedLevel2StateID);

                return params;
            }
        };
        queue.add(postRequest);
    }

    private void setCategoriesStatusForUser() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(act);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.CATEGORIES_STATUS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {

                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {

                                SharedPreferences settings = act.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = settings.edit();

                                editor.putString("apiSecretKey", newUserAPIsecret);
                                editor.putString("userID", newUserUserID);
                                editor.putString("email", newUserEmail);
                                if (newUserContact1 != null)
                                    editor.putString("contactNum1", newUserContact1);
                                else editor.putString("contactNum1", "");
                                if (newUserContact2 != null)
                                    editor.putString("contactNum2", newUserContact2);
                                else editor.putString("contactNum2", "");
                                editor.putString("country", newUserCountry);
                                editor.putString("country", newUserState);
                                editor.putString("city", newUserCity);
                                editor.commit();

                                Intent i = new Intent(act, MainActivity.class);
                                act.startActivity(i);
                                act.finish();

                            } else if (result.get("status").equals("error")) {
                                Toast.makeText(act, "Unexpected Connection Error", Toast.LENGTH_SHORT).show();
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

                String ids = "";
                String statuses = "";

                SharedPreferences settings = act.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String userID = settings.getString("userID", "defaultValue");
                String apiSecretKey = settings.getString("apiSecretKey", "defaultValue");

                Map<String, String> params = new HashMap<String, String>();

                for (int i = 0; i < savedCategoriesDataModels.size(); i++) {
                    ids = ids + savedCategoriesDataModels.get(i).getID() + ",";
                    statuses = statuses + savedCategoriesDataModels.get(i).getStatus() + ",";
                }

                ids = ids.substring(0, ids.length() - 1) + "";
                statuses = statuses.substring(0, statuses.length() - 1) + "";

                params.put("api_secret", newUserAPIsecret);
                params.put("cat_id", ids);
                params.put("status", statuses);

                return params;
            }
        };
        queue.add(postRequest);

    }

    private boolean validInputs() {
        boolean check = false;

        if (etName.getText().toString().equals("")) {
            etName.setError("This field is required");
            check = false;
        } else if (etEmail.getText().toString().equals("")) {
            etEmail.setError("This field is required");
            check = false;
        } else if (etPassword.getText().toString().equals("")) {
            etPassword.setError("This field is required");
            check = false;
        } else if (etContact1.getText().toString().equals("")) {
            etContact1.setError("This field is required");
            check = false;
        } else if (etContact2.getText().toString().equals("")) {
            etContact2.setError("This field is required");
            check = false;
        } else if (!etContact1.getText().toString().substring(0, 1).equals("+")) {
            etContact1.setError("Enter number with calling code");
            check = false;
        } else if (!etContact2.getText().toString().substring(0, 1).equals("+")) {
            etContact2.setError("Enter number with calling code");
            check = false;
        } else if (etContact1.getText().toString().length() < 9) {
            etContact1.setError("Enter a valid contact number");
            check = false;
        } else if (etContact2.getText().toString().length() < 9) {
            etContact2.setError("Enter a valid contact number");
            check = false;
        } else if (etPassword.getText().toString().length() < 6) {
            etPassword.setError("Minimum 6 characters allowed");
            check = false;
        } else {
            check = true;
        }
        return check;
    }
}