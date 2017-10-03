package com.codiansoft.oneandonly;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.codiansoft.oneandonly.adapter.ListViewAdapterRegisterCountries;
import com.codiansoft.oneandonly.model.CountriesDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.codiansoft.oneandonly.GlobalClass.alreadyLoggedIn;

public class Level1CountriesActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView mListView;
    private ListViewAdapterRegisterCountries mAdapter;
    private Context mContext = this;
    TextView tvCategoryName, tvBack;
    Button bSave;
    ProgressBar progressBar;

    public static ArrayList<CountriesDataModel> dataModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level1_countries);

        initUI();

        dataModels = new ArrayList<>();

        mListView = (ListView) findViewById(R.id.listview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
        getSupportActionBar().hide();

        /**
         * The following comment is the sample usage of ArraySwipeAdapter.
         */
//        String[] adapterData = new String[]{"Activity", "Service", "Content Provider", "Intent", "BroadcastReceiver", "ADT", "Sqlite3", "HttpClient",
//                "DDMS", "Android Studio", "Fragment", "Loader", "Activity", "Service", "Content Provider", "Intent",
//                "BroadcastReceiver", "ADT", "Sqlite3", "HttpClient", "Activity", "Service", "Content Provider", "Intent",
//                "BroadcastReceiver", "ADT", "Sqlite3", "HttpClient"};
//        mListView.setAdapter(new ArraySwipeAdapterSample<String>(this, R.layout.listview_item, R.id.position, adapterData));

        initializeCountriesList();

        if (!GlobalClass.postingAd) {
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CountriesDataModel dataModel = dataModels.get(position);

                    if (alreadyLoggedIn) {
                        GlobalClass.selectedLevel2Country = dataModel.getCountry();
                        GlobalClass.selectedLevel2CountryID = dataModel.getID();
                        Intent i = new Intent(Level1CountriesActivity.this, Level2StatesActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } else {

//                ((SwipeLayout) (mListView.getChildAt(position - mListView.getFirstVisiblePosition()))).open(true);

                        GlobalClass.selectedLevel2Country = dataModel.getCountry();
                        GlobalClass.selectedLevel2UpdateTime = dataModel.getLastUpdateTime();
                        GlobalClass.selectedLevel2CountryID = dataModel.getID();
                        GlobalClass.selectedLevel2Quantity = dataModel.getQuantity();

                        Intent i = new Intent(Level1CountriesActivity.this, Level2StatesActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                }
            });
        }
    }

    private void initializeCountriesList() {
       /* categoriesRegisterDataModel = new ArrayList<>();

        categoriesRegisterDataModel.add(new CountriesDataModel("United States", "us", "New York", "2:00", "1", "12"));
        categoriesRegisterDataModel.add(new CountriesDataModel("Pakistan", "pk", "Karachi", "12:55", "1", "12"));
        categoriesRegisterDataModel.add(new CountriesDataModel("Germany", "gr", "Berlin", "23:00", "1", "12"));
        categoriesRegisterDataModel.add(new CountriesDataModel("Australia", "au", "Melbourne", "21:54", "1", "12"));
        categoriesRegisterDataModel.add(new CountriesDataModel("Spain", "es", "Mahaloof", "5:23", "1", "12"));

        mAdapter = new ListViewAdapterRegisterCountries(this);
        mListView.setAdapter(mAdapter);*/


        /*JSONArray m_jArry;

        //Initialize cities list
        try {
            m_jArry = new JSONArray(loadJSONFromAsset());

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject countryObj = m_jArry.getJSONObject(i);
                categoriesRegisterDataModel.add(new CountriesDataModel(countryObj.getString("name"), countryObj.getString("alpha-2").toLowerCase(), "New York", "2:00", "1", "12"));

            }
//            Collections.sort(categoriesRegisterDataModel, String.CASE_INSENSITIVE_ORDER);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter = new ListViewAdapterRegisterCountries(this);
        mListView.setAdapter(mAdapter);*/







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

                            mAdapter = new ListViewAdapterRegisterCountries(Level1CountriesActivity.this);
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

    private String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getResources().openRawResource(R.raw.all_countries);
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

    private void initUI() {
        tvCategoryName = (TextView) findViewById(R.id.tvCategoryName);
        tvBack = (TextView) findViewById(R.id.tvBack);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.bringToFront();

        bSave = (Button) findViewById(R.id.bSave);
        bSave.setOnClickListener(this);
        tvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bSave:
                Intent mainIntent = new Intent(Level1CountriesActivity.this, MainActivity.class);
                startActivity(mainIntent);
                break;
            case R.id.tvBack:
                onBackPressed();
                break;
        }
    }
}
