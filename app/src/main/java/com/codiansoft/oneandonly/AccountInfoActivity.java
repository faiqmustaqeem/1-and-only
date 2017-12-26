package com.codiansoft.oneandonly;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.codiansoft.oneandonly.model.CitiesDataModel;
import com.codiansoft.oneandonly.model.CountriesDataModel;
import com.codiansoft.oneandonly.model.StatesDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccountInfoActivity extends AppCompatActivity implements View.OnClickListener {

    String currentCountry;
    Spinner citizenship, spStates, cityName;
    EditText etUsername, etEmail, etPhoneNumber, etMobileNumber, etWebsite;
    ImageView ivProfilePic;
    Bitmap bitmapProfilePic;
    ArrayAdapter countriesAdapter;
    ArrayAdapter statesAdapter;
    ArrayAdapter adapterCities;
    ProgressBar progressBar;
    Button bSave;
    String imageBase64;
    ArrayList<CountriesDataModel> countries = new ArrayList<CountriesDataModel>();
    ArrayList<String> countriesNames = new ArrayList<String>();
    CountriesDataModel countriesDataModel;
    ArrayList<StatesDataModel> states = new ArrayList<StatesDataModel>();
    ArrayList<String> statesNames = new ArrayList<String>();
    ArrayList<CitiesDataModel> cities = new ArrayList<CitiesDataModel>();
    ArrayList<String> citiesNames = new ArrayList<String>();
    int countriesLoadedCount = 0;
    int statesLoadedCount = 0;
    int citiesLoadedCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_account_info);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        countriesLoadedCount = 0;
        initUI();

        citizenship.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
//                setStatesInSpinner(countries.get(position).getID(),states.get(position).getID(),"");

                countriesLoadedCount++;

                if (countriesLoadedCount > 1) {
//                    Toast.makeText(AccountInfoActivity.this, "on item selected spCountry", Toast.LENGTH_SHORT).show();
                    setStatesInSpinner(countries.get(position).getID(), "", "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spStates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
//                setStatesInSpinner(countries.get(position).getID(),states.get(position).getID(),"");
                statesLoadedCount++;
                if (statesLoadedCount > 1) {
//                    Toast.makeText(AccountInfoActivity.this, "on item selected states", Toast.LENGTH_SHORT).show();
                    setCitiesInSpinner(states.get(position).getID(), "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etUsername:
                etUsername.setEnabled(true);
                break;
            case R.id.ivProfilePic:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                break;
            case R.id.bSave:
                if (validInputs()) {
                    updateUserInfo();
                }
                break;
        }
    }

    private boolean validInputs() {
        boolean check = false;
        if (etUsername.getText().toString().equals("")) {
            etUsername.setError("This field is required");
            check = false;
        } else if (etEmail.getText().toString().equals("")) {
            etEmail.setError("This field is required");
            check = false;
        } else if (!etPhoneNumber.getText().toString().substring(0, 1).equals("+")) {
            etPhoneNumber.setError("Enter number with calling code");
            check = false;
        } else if (!etMobileNumber.getText().toString().substring(0, 1).equals("+")) {
            etMobileNumber.setError("Enter number with calling code");
            check = false;
        } else if (etPhoneNumber.getText().toString().length() < 9) {
            etPhoneNumber.setError("Enter a valid contact number");
            check = false;
        } else if (etMobileNumber.getText().toString().length() < 9) {
            etMobileNumber.setError("Enter a valid contact number");
            check = false;
        }
        return check;
    }

    private void updateUserInfo() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.UPDATE_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                JSONObject userData = result.getJSONObject("data");
                                /*etUsername.setText(userData.getString("name"));
                                etEmail.setText(userData.getString("email"));
                                etPhoneNumber.setText(userData.getString("phone_number"));
                                etMobileNumber.setText(userData.getString("mobile_number"));
                                citizenship.setSelection(countriesAdapter.getPosition(userData.getString("country")));
                                cityName.setSelection(countriesAdapter.getPosition(userData.getString("city")));*/

                                GlobalClass.userCountryID = userData.getString("country");
                                SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("contactNum1", userData.getString("phone_number"));
                                editor.putString("contactNum2", userData.getString("mobile_number"));
                                editor.putString("country", userData.getString("country"));
                                editor.putString("state", userData.getString("state_id"));
                                editor.putString("city", userData.getString("city"));
                                editor.commit();


                                Toast.makeText(AccountInfoActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        } catch (Exception ee) {
                            Toast.makeText(AccountInfoActivity.this, "error: " + ee.toString(), Toast.LENGTH_SHORT).show();
                        }

                        if (progressBar.isShown()) {
                            progressBar.setVisibility(View.GONE);
                        }
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
                                    Toast.makeText(AccountInfoActivity.this, "Try again", Toast.LENGTH_SHORT).show();
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
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
                params.put("name", etUsername.getText().toString());
                params.put("email", etEmail.getText().toString());
                params.put("website", etWebsite.getText().toString());
                params.put("phone_number", etPhoneNumber.getText().toString());
                params.put("mobile_number", etMobileNumber.getText().toString());

                for (int i = 0; i < countries.size(); i++) {
                    if (countries.get(i).getCountry().equals(citizenship.getSelectedItem().toString())) {
                        params.put("country", countries.get(i).getID());
                        break;
                    }
                }
                for (int i = 0; i < states.size(); i++) {
                    if (states.get(i).getState().equals(spStates.getSelectedItem().toString())) {
                        params.put("state_id", states.get(i).getID());
                        break;
                    }
                }
                for (int i = 0; i < cities.size(); i++) {
                    if (cities.get(i).getCity().equals(cityName.getSelectedItem().toString())) {
                        params.put("city", cities.get(i).getID());
                        break;
                    }
                }

                if (imageBase64.equals("")) {
                    params.put("image", "");
                }

                params.put("image", imageBase64);

                return params;
            }
        };
        queue.add(postRequest);
    }


    private void initUI() {
        imageBase64 = "";
        etUsername = (EditText) findViewById(R.id.etUsername);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        etWebsite = (EditText) findViewById(R.id.etWebsite);
        ivProfilePic = (ImageView) findViewById(R.id.ivProfilePic);
        citizenship = (Spinner) findViewById(R.id.spCountry);
        spStates = (Spinner) findViewById(R.id.spState);
        cityName = (Spinner) findViewById(R.id.spCity);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.bringToFront();
        bSave = (Button) findViewById(R.id.bSave);

        ivProfilePic.setOnClickListener(this);
        etUsername.setOnClickListener(this);
        etEmail.setOnClickListener(this);
        etPhoneNumber.setOnClickListener(this);
        etMobileNumber.setOnClickListener(this);
        etWebsite.setOnClickListener(this);
        bSave.setOnClickListener(this);

        etEmail.setFocusable(false);
/*
        //Initialize countries list
        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for (Locale loc : locale) {
            country = loc.getDisplayCountry();
            if (country.length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        countriesAdapter = new ArrayAdapter<String>(AccountInfoActivity.this, android.R.layout.simple_spinner_item, countries);
        citizenship.setAdapter(countriesAdapter);
//        currentCountry = Locale.getDefault().getCountry();
//        citizenship.setSelection(countriesAdapter.getPosition(currentCountry));
        */
        fetchProfile();
    }

    private void fetchCountriesInSpinner(final String userCountryID, final String userStateID, final String userCityID) {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_COUNTRIES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONArray countriesArr = new JSONArray(response);
                            for (int i = 0; i < countriesArr.length(); i++) {
                                JSONObject countryObj = countriesArr.getJSONObject(i);
                                countries.add(new CountriesDataModel(countryObj.getString("name"), countryObj.getString("sortname").toLowerCase(), "New York", countryObj.getString("last_update"), countryObj.getString("id"), countryObj.getString("ads_num")));
                                countriesNames.add(countryObj.getString("name"));
                            }
                            countriesAdapter = new ArrayAdapter<String>(AccountInfoActivity.this, android.R.layout.simple_spinner_item, countriesNames);
                            citizenship.setAdapter(countriesAdapter);

                            for (int j = 0; j < countries.size(); j++) {
                                if (countries.get(j).getID().equals(userCountryID)) {
                                    citizenship.setSelection(countriesAdapter.getPosition(countries.get(j).getCountry()));
                                    setStatesInSpinner(countries.get(citizenship.getSelectedItemPosition()).getID(), userStateID, userCityID);
                                    break;
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (progressBar.isShown()) {
                            progressBar.setVisibility(View.GONE);
                        }
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

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
                                    Toast.makeText(AccountInfoActivity.this, "Try again", Toast.LENGTH_SHORT).show();
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
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }
        };
        queue.add(postRequest);

    }

    private void fetchProfile() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

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
                                JSONObject userData = result.getJSONObject("user_data");
                                etUsername.setText(userData.getString("name"));
                                etEmail.setText(userData.getString("email"));
                                etPhoneNumber.setText(userData.getString("phone_number"));
                                etMobileNumber.setText(userData.getString("mobile_number"));
//                                citizenship.setSelection(countriesAdapter.getPosition(userData.getString("country")));
//                                setStatesInSpinner(userData.getString("country_id"));
//                                cityName.setSelection(adapterCities.getPosition(userData.getString("city")));

                                Glide.with(AccountInfoActivity.this).load(userData.getString("user_image")).into(ivProfilePic);
                                fetchCountriesInSpinner(userData.getString("country"), userData.getString("state_id"), userData.getString("city"));


                            } else {
                                finish();
                            }


                        } catch (Exception ee) {
                            Toast.makeText(AccountInfoActivity.this, "error: " + ee.toString(), Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        if (progressBar.isShown()) {
                            progressBar.setVisibility(View.GONE);
                        }
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

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
                                    Toast.makeText(AccountInfoActivity.this, "Try again", Toast.LENGTH_SHORT).show();
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
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        finish();
                    }
                }
        )
        {
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

                return params;
            }
        };
        queue.add(postRequest);
    }

    private void setStatesInSpinner(final String countryID, final String stateID, final String cityID) {
/*
        ArrayList<String> cities = null;
        JSONArray m_jArry;
        JSONObject obj;

        //Initialize cities list
        try {
            obj = new JSONObject(loadJSONFromAsset());
            m_jArry = obj.getJSONArray(userCountryID);
            cities = new ArrayList<String>();
            String city;

            for (int i = 0; i < m_jArry.length(); i++) {
                city = m_jArry.getString(i);
                if (city.length() > 0 && !cities.contains(city)) {
                    cities.add(city);
                }
            }
            Collections.sort(cities, String.CASE_INSENSITIVE_ORDER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapterCities = new ArrayAdapter<String>(AccountInfoActivity.this, android.R.layout.simple_spinner_item, cities);
        cityName.setAdapter(adapterCities);

*/


        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_STATES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            states.clear();
                            statesNames.clear();
                            JSONArray statesArray = new JSONArray(response);
                            for (int i = 0; i < statesArray.length(); i++) {
                                JSONObject stateObj = statesArray.getJSONObject(i);
                                states.add(new StatesDataModel(stateObj.getString("country_id"), stateObj.getString("name"), "2:00", stateObj.getString("id"), "14"));
                                statesNames.add(stateObj.getString("name"));
                            }

                            statesAdapter = new ArrayAdapter<String>(AccountInfoActivity.this, android.R.layout.simple_spinner_item, statesNames);
                            spStates.setAdapter(statesAdapter);

                            if (!stateID.equals("")) {
                                for (int j = 0; j < states.size(); j++) {
                                    if (states.get(j).getID().equals(stateID)) {
                                        spStates.setSelection(statesAdapter.getPosition(states.get(j).getState()));
                                        setCitiesInSpinner(states.get(spStates.getSelectedItemPosition()).getID(), cityID);
                                        break;
                                    }
                                }
                            } else {
                                setCitiesInSpinner(states.get(spStates.getSelectedItemPosition()).getID(), "");
                            }

                        } catch (Exception ee) {
                            Toast.makeText(AccountInfoActivity.this, "error: " + ee.toString(), Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        if (progressBar.isShown()) {
                            progressBar.setVisibility(View.GONE);
                        }
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

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
                                    Toast.makeText(AccountInfoActivity.this, "Try again", Toast.LENGTH_SHORT).show();
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
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        finish();
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
                params.put("country_id", countryID);

                return params;
            }
        };
        queue.add(postRequest);


    }

    private void setCitiesInSpinner(final String userStateID, final String userCityID) {

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_CITIES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            cities.clear();
                            citiesNames.clear();
                            JSONArray statesArray = new JSONArray(response);
                            for (int i = 0; i < statesArray.length(); i++) {
                                JSONObject stateObj = statesArray.getJSONObject(i);
                                cities.add(new CitiesDataModel(stateObj.getString("state_id"), stateObj.getString("name"), "2:00", stateObj.getString("id"), "14"));
                                citiesNames.add(stateObj.getString("name"));
                            }

                            adapterCities = new ArrayAdapter<String>(AccountInfoActivity.this, android.R.layout.simple_spinner_item, citiesNames);
                            cityName.setAdapter(adapterCities);

                            if (!userCityID.equals("")) {
                                for (int j = 0; j < cities.size(); j++) {
                                    if (cities.get(j).getID().equals(userCityID)) {
                                        cityName.setSelection(adapterCities.getPosition(cities.get(j).getCity()));
                                        break;
                                    }
                                }
                            } else {

                            }

                        } catch (Exception ee) {
                            Toast.makeText(AccountInfoActivity.this, "error: " + ee.toString(), Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        if (progressBar.isShown()) {
                            progressBar.setVisibility(View.GONE);
                        }
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
                                    Toast.makeText(AccountInfoActivity.this, "Try again", Toast.LENGTH_SHORT).show();
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
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        finish();
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
                params.put("state_id", userStateID);

                return params;
            }
        };
        queue.add(postRequest);
    }

    private String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getResources().openRawResource(R.raw.countries_to_cities);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                // get uri from Intent
                Uri uri = data.getData();
                // get bitmap from uri
                bitmapProfilePic = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                // store bitmap to file
                File filename = new File(Environment.getExternalStorageDirectory(), "imageName.jpg");
                FileOutputStream out = new FileOutputStream(filename);
                bitmapProfilePic.compress(Bitmap.CompressFormat.JPEG, 60, out);

                Glide.clear(ivProfilePic);
                ivProfilePic.setImageBitmap(bitmapProfilePic);

                out.flush();
                out.close();
                // get base64 string from file
                imageBase64 = getStringImage(filename);
                // use base64 for your next step.
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getStringImage(File file) {
        try {
            FileInputStream fin = new FileInputStream(file);
            byte[] imageBytes = new byte[(int) file.length()];
            fin.read(imageBytes, 0, imageBytes.length);
            fin.close();
            ivProfilePic.setImageBitmap(bitmapProfilePic);
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception ex) {
            Toast.makeText(this, "Image size is too large to upload", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}