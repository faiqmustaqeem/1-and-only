package com.codiansoft.oneandonly;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codiansoft.oneandonly.model.CitiesDataModel;
import com.codiansoft.oneandonly.model.CountriesDataModel;
import com.codiansoft.oneandonly.model.StatesDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.codiansoft.oneandonly.AddItemActivity.cities;
import static com.codiansoft.oneandonly.AddItemActivity.countries;
import static com.codiansoft.oneandonly.AddItemActivity.states;

public class SearchFiltersActivity extends AppCompatActivity {
    Spinner spCountry, spStates, spCity;
    Button bSearch;
    EditText etSearchTitle;
    ArrayList<String> countriesNames = new ArrayList<String>();
    ArrayList<String> statesNames = new ArrayList<String>();
    ArrayList<String> citiesNames = new ArrayList<String>();
    ArrayAdapter countriesAdapter;
    ArrayAdapter statesAdapter;
    ArrayAdapter adapterCities;

    int countriesLoadedCount = 0;
    int statesLoadedCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filters);
        getSupportActionBar().setTitle("Filter Ads Search");
        etSearchTitle = (EditText) findViewById(R.id.etSearchTitle);
        bSearch = (Button) findViewById(R.id.bSearch);
        spCountry = (Spinner) findViewById(R.id.spCountry);
        spStates = (Spinner) findViewById(R.id.spState);
        spCity = (Spinner) findViewById(R.id.spCity);
        fetchProfile();

        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("searchName", etSearchTitle.getText().toString());
                intent.putExtra("searchCountry", spCountry.getSelectedItem().toString());
                intent.putExtra("searchState", spStates.getSelectedItem().toString());
                intent.putExtra("searchCity", spCity.getSelectedItem().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void fetchProfile() {

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

                                fetchCountriesInSpinner(userData.getString("country"), userData.getString("state_id"), userData.getString("city"));
                            } else {
                                finish();
                            }
                        } catch (Exception ee) {
                            Toast.makeText(SearchFiltersActivity.this, "error: " + ee.toString(), Toast.LENGTH_SHORT).show();
                            finish();
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
                                    Toast.makeText(SearchFiltersActivity.this, "Try again", Toast.LENGTH_SHORT).show();
//                                    utilities.dialog("Connection Problem", act);
                                    break;
                                default:
//                                    utilities.dialog("Connection Problem", act);
                                    break;
                            }
                        }
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
                params.put("api_secret", apiSecretKey);

                return params;
            }
        };
        queue.add(postRequest);
    }

    private void fetchCountriesInSpinner(final String userCountryID, final String userStateID, final String userCityID) {

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
                            countriesAdapter = new ArrayAdapter<String>(SearchFiltersActivity.this, android.R.layout.simple_spinner_item, countriesNames);
                            spCountry.setAdapter(countriesAdapter);

                            for (int j = 0; j < countries.size(); j++) {
                                if (countries.get(j).getID().equals(userCountryID)) {
                                    spCountry.setSelection(countriesAdapter.getPosition(countries.get(j).getCountry()));
                                    setStatesInSpinner(countries.get(spCountry.getSelectedItemPosition()).getID(), userStateID, userCityID);
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                                    Toast.makeText(SearchFiltersActivity.this, "Try again", Toast.LENGTH_SHORT).show();
//                                    utilities.dialog("Connection Problem", act);
                                    break;
                                default:
//                                    utilities.dialog("Connection Problem", act);
                                    break;
                            }
                        }
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

    private void setStatesInSpinner(final String countryID, final String stateID, final String cityID) {
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

                            statesAdapter = new ArrayAdapter<String>(SearchFiltersActivity.this, android.R.layout.simple_spinner_item, statesNames);
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
                            Toast.makeText(SearchFiltersActivity.this, "error: " + ee.toString(), Toast.LENGTH_SHORT).show();
                            finish();
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
                                    Toast.makeText(SearchFiltersActivity.this, "Try again", Toast.LENGTH_SHORT).show();
//                                    utilities.dialog("Connection Problem", act);
                                    break;
                                default:
//                                    utilities.dialog("Connection Problem", act);
                                    break;
                            }
                        }
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

                            adapterCities = new ArrayAdapter<String>(SearchFiltersActivity.this, android.R.layout.simple_spinner_item, citiesNames);
                            spCity.setAdapter(adapterCities);

                            if (!userCityID.equals("")) {
                                for (int j = 0; j < cities.size(); j++) {
                                    if (cities.get(j).getID().equals(userCityID)) {
                                        spCity.setSelection(adapterCities.getPosition(cities.get(j).getCity()));
                                        break;
                                    }
                                }
                            } else {

                            }

                        } catch (Exception ee) {
                            Toast.makeText(SearchFiltersActivity.this, "error: " + ee.toString(), Toast.LENGTH_SHORT).show();
                            finish();
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
                                    Toast.makeText(SearchFiltersActivity.this, "Try again", Toast.LENGTH_SHORT).show();
//                                    utilities.dialog("Connection Problem", act);
                                    break;
                                default:
//                                    utilities.dialog("Connection Problem", act);
                                    break;
                            }
                        }
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
}
