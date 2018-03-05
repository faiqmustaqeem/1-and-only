package com.codiansoft.oneandonly;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codiansoft.oneandonly.adapter.ListViewAdapterAdCities;
import com.codiansoft.oneandonly.model.CitiesDataModel;
import com.kyleduo.switchbutton.SwitchButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.codiansoft.oneandonly.AddItemActivity.addItemAct;
import static com.codiansoft.oneandonly.AddItemActivity.tvSaveAdLocations;
import static com.codiansoft.oneandonly.AddItemActivity.savedAdLocations;
import static com.codiansoft.oneandonly.AddItemActivity.uploadAd;
import static com.codiansoft.oneandonly.GlobalClass.selectedAddPostCityIDs;
import static com.codiansoft.oneandonly.GlobalClass.selectedAddPostCityNames;
import static com.codiansoft.oneandonly.MainActivity.categories;

public class AdCitiesActivity extends AppCompatActivity implements View.OnClickListener {

    public static Activity adCitiesActivity;
    private ListView mListView;
    private ListViewAdapterAdCities mAdapter;
    private Context mContext = this;
    TextView tvCategoryName, tvBack, tvStateName;
    public static TextView tvSelectedAdCities;
    Button bSave;
    ProgressBar progressBar;
    EditText etSearch;
    ImageView ivSearch;
    SwitchButton sbSelectAll;

    public static ArrayList<CitiesDataModel> dataModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_cities);
        adCitiesActivity = this;
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
        initializeCitiesList();

        sbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    GlobalClass.selectedAddPostCityID = "";
                    GlobalClass.selectedAddPostCity = "";
                    GlobalClass.selectedAddPostCityIDs.setLength(0);
                    GlobalClass.selectedAddPostCityNames.setLength(0);

                    for (int i = 0; i < dataModels.size(); i++) {
                        GlobalClass.selectedAddPostCityID = GlobalClass.selectedAddPostCityID + dataModels.get(i).getID() + ",";
                        selectedAddPostCityIDs.append(dataModels.get(i).getID() + ",");
                        selectedAddPostCityNames.append(dataModels.get(i).getCity() + ",");
                        GlobalClass.selectedAddPostCity = dataModels.get(i).getID();

                        dataModels.get(i).setOnOff(true);
                        tvSelectedAdCities.setText(GlobalClass.selectedAddPostCity);
                    }
                } else {
                    GlobalClass.selectedAddPostCityID = "";
                    GlobalClass.selectedAddPostCity = "";
                    GlobalClass.selectedAddPostCityIDs.setLength(0);
                    GlobalClass.selectedAddPostCityNames.setLength(0);
                    for (int i = 0; i < dataModels.size(); i++) {
                        dataModels.get(i).setOnOff(false);
                    }
                }

                if (!selectedAddPostCityIDs.toString().equals("") & !selectedAddPostCityNames.toString().equals("")) {
                    tvSelectedAdCities.setVisibility(View.VISIBLE);
                    tvSelectedAdCities.setText(selectedAddPostCityNames.substring(0, selectedAddPostCityNames.length() - 1));
                } else {
                    tvSelectedAdCities.setVisibility(View.GONE);
                }
                mAdapter.notifyDataSetChanged();
            }
        });


        etSearch.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mAdapter != null) mAdapter.getFilter().filter(s.toString());
            }
        });
    }

    private void initializeCitiesList() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_CITIES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONArray CitiesArr = new JSONArray(response);
                            for (int i = 0; i < CitiesArr.length(); i++) {
                                JSONObject cityObj = CitiesArr.getJSONObject(i);
                                dataModels.add(new CitiesDataModel(GlobalClass.selectedLevel2StateID, cityObj.getString("name"), "2:00", cityObj.getString("id"), "12"));
                            }

                            mAdapter = new ListViewAdapterAdCities(AdCitiesActivity.this);
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
                params.put("state_id", GlobalClass.selectedAddPostStateID);
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void initUI() {
        GlobalClass.selectedAddPostCityIDs = new StringBuilder("");
        GlobalClass.selectedAddPostCityNames = new StringBuilder("");
        GlobalClass.selectedAddPostCityID = "";
        GlobalClass.selectedAddPostCity = "";

        tvSelectedAdCities = (TextView) findViewById(R.id.tvSelectedAdCities);
        tvCategoryName = (TextView) findViewById(R.id.tvCategoryName);
        tvBack = (TextView) findViewById(R.id.tvBack);
        tvStateName = (TextView) findViewById(R.id.tvStateName);
        tvStateName.setText(GlobalClass.selectedAddPostState);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.bringToFront();
        etSearch = (EditText) findViewById(R.id.etSearch);
        ivSearch = (ImageView) findViewById(R.id.ivSearch);
        sbSelectAll = (SwitchButton) findViewById(R.id.sbSelectAll);

        bSave = (Button) findViewById(R.id.bSave);
        bSave.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bSave:
                if (!GlobalClass.selectedAddPostCityID.equals("")) {
                    /*savedAdLocations = true;
                    tvSaveAdLocations.setTextColor(getResources().getColor(R.color.text_color_save_config_done));
                    Drawable img = getResources().getDrawable(R.drawable.ic_done);
                    tvSaveAdLocations.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    AdStatesActivity.adStatesActivity.finish();
                    AdCountriesActivity.adCountriesActivity.finish();
                    finish();*/
                    progressBar.setVisibility(View.VISIBLE);
                    new MaterialDialog.Builder(this)
                            .title("Select anotner country")
                            .content("do you want to select another country ?")
                            .positiveText("Yes")
                            .negativeText("No")
                            .canceledOnTouchOutside(false)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent intent=new Intent(mContext,AdCountriesActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    uploadAd(addItemAct);
                                }
                            })
                            .show();

                } else {
                    Toast.makeText(mContext, "Turn on a city", Toast.LENGTH_SHORT).show();
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
        if (etSearch.getVisibility() == View.VISIBLE) {
            etSearch.setVisibility(View.GONE);
            etSearch.clearFocus();
            etSearch.setText("");
        } else finish();
    }

}