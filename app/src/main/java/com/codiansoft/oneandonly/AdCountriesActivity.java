package com.codiansoft.oneandonly;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.codiansoft.oneandonly.adapter.ListViewAdapterAdCountries;
import com.codiansoft.oneandonly.model.CountriesDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.codiansoft.oneandonly.AddItemActivity.savedAdLocations;
import static com.codiansoft.oneandonly.AddItemActivity.tvSaveAdLocations;
import static com.codiansoft.oneandonly.dialog.RegisterDialog.tvSaveConfigurations;

public class AdCountriesActivity extends AppCompatActivity implements View.OnClickListener {

    public static Activity adCountriesActivity;

    private ListView mListView;
    private ListViewAdapterAdCountries mAdapter;
    private Context mContext = this;
    TextView tvCategoryName, tvBack;
    Button bSave, bSetStates;
    ProgressBar progressBar;
    EditText etSearch;
    ImageView ivSearch;

    public static ArrayList<CountriesDataModel> dataModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_countries);
        adCountriesActivity = this;

        initUI();

        dataModels = new ArrayList<CountriesDataModel>();
        mListView = (ListView) findViewById(R.id.listview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
        getSupportActionBar().hide();
        initializeCountriesList();


        etSearch.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mAdapter != null) mAdapter.getFilter().filter(s.toString());
            }
        });

    }

    private void initializeCountriesList() {
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
                                dataModels.add(new CountriesDataModel(countryObj.getString("name"), countryObj.getString("sortname").toLowerCase(), "New York", countryObj.getString("last_update"), countryObj.getString("id"), countryObj.getString("ads_num")));
                            }

                            mAdapter = new ListViewAdapterAdCountries(AdCountriesActivity.this);
                            mListView.setAdapter(mAdapter);

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
                                    Toast.makeText(mContext, "Try again", Toast.LENGTH_SHORT).show();
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

    private void initUI() {
        GlobalClass.selectedAddPostCountry = "";
        savedAdLocations = false;
        tvSaveAdLocations.setTextColor(Color.parseColor("#000000"));
        Drawable img = getResources().getDrawable(R.drawable.ic_not_done);
        tvSaveAdLocations.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);

        tvCategoryName = (TextView) findViewById(R.id.tvCategoryName);
        tvBack = (TextView) findViewById(R.id.tvBack);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        etSearch = (EditText) findViewById(R.id.etSearch);
        ivSearch = (ImageView) findViewById(R.id.ivSearch);
        progressBar.bringToFront();
        etSearch.bringToFront();

        bSave = (Button) findViewById(R.id.bSave);
        bSetStates = (Button) findViewById(R.id.bSetStates);
        bSave.setOnClickListener(this);
        bSetStates.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bSave:
                if (!GlobalClass.selectedAddPostCountry.equals(""))
                {
                    savedAdLocations = true;
                    tvSaveAdLocations.setTextColor(getResources().getColor(R.color.text_color_save_config_done));
                    Drawable img = getResources().getDrawable(R.drawable.ic_done);
                    tvSaveAdLocations.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    finish();
                }
                else {
                    Toast.makeText(mContext, "Turn on a country", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bSetStates:
                if (!GlobalClass.selectedAddPostCountry.equals("")) {
                    savedAdLocations = false;
                    Intent i = new Intent(AdCountriesActivity.this, AdStatesActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(mContext, "Turn on a country", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ivSearch:
                etSearch.setVisibility(View.VISIBLE);
                break;
            case R.id.tvBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if (etSearch.getVisibility() == View.VISIBLE){
            etSearch.setVisibility(View.GONE);
            etSearch.clearFocus();
            etSearch.setText("");
        } else finish();
    }
}