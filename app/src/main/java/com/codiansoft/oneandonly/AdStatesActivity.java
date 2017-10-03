package com.codiansoft.oneandonly;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
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

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codiansoft.oneandonly.adapter.ListViewAdapterAdStates;
import com.codiansoft.oneandonly.model.StatesDataModel;
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
import static com.codiansoft.oneandonly.GlobalClass.selectedAddPostStateIDs;
import static com.codiansoft.oneandonly.GlobalClass.selectedAddPostStateNames;

public class AdStatesActivity extends AppCompatActivity implements View.OnClickListener {

    public static Activity adStatesActivity;

    private ListView mListView;
    private ListViewAdapterAdStates mAdapter;
    private Context mContext = this;
    TextView tvCategoryName, tvBack, tvCountryName;
    Button bSave, bSetCiies;
    ProgressBar progressBar;
    EditText etSearch;
    ImageView ivSearch;
    SwitchButton sbSelectAll;

    public static ArrayList<StatesDataModel> dataModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_states);
        adStatesActivity = this;
        initUI();
        dataModels = new ArrayList<StatesDataModel>();
        mListView = (ListView) findViewById(R.id.listview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
        getSupportActionBar().hide();
        initializeStatesList();

        etSearch.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mAdapter != null) mAdapter.getFilter().filter(s.toString());
            }
        });

        sbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    GlobalClass.isSelectAllStates = true;
                    bSave.setVisibility(View.VISIBLE);
                    GlobalClass.selectedAddPostStateID = "";
                    GlobalClass.selectedAddPostState = "";
                    selectedAddPostStateIDs.setLength(0);
                    selectedAddPostStateNames.setLength(0);

                    for (int i = 0; i < dataModels.size(); i++) {
                        GlobalClass.selectedAddPostStateID = GlobalClass.selectedAddPostStateID + dataModels.get(i).getID() + ",";
                        selectedAddPostStateIDs.append(dataModels.get(i).getID() + ",");
                        selectedAddPostStateNames.append(dataModels.get(i).getState() + ",");
                        GlobalClass.selectedAddPostState = dataModels.get(i).getID();

                        dataModels.get(i).setOnOff(true);
//                        tvSelectedAdStates.setText(GlobalClass.selectedAddPostCity);
                    }
                } else {
                    GlobalClass.isSelectAllStates = false;
                    bSave.setVisibility(View.GONE);
                    GlobalClass.selectedAddPostStateID = "";
                    GlobalClass.selectedAddPostState = "";
                    selectedAddPostStateIDs.setLength(0);
                    selectedAddPostStateNames.setLength(0);
                    for (int i = 0; i < dataModels.size(); i++) {
                        dataModels.get(i).setOnOff(false);
                    }
                }

                if (!selectedAddPostStateIDs.toString().equals("") & !selectedAddPostStateNames.toString().equals("")) {
//                    tvSelectedAdStates.setVisibility(View.VISIBLE);
//                    tvSelectedAdStates.setText(selectedAddPostStateNames.substring(0, selectedAddPostStateNames.length() - 1));
                } else {
//                    tvSelectedAdStates.setVisibility(View.GONE);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initializeStatesList() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_STATES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONArray StatesArr = new JSONArray(response);
                            for (int i = 0; i < StatesArr.length(); i++) {
                                JSONObject stateObj = StatesArr.getJSONObject(i);
                                dataModels.add(new StatesDataModel(GlobalClass.selectedLevel2Country, stateObj.getString("name"), "2:00", stateObj.getString("id"), "12"));
                            }

                            mAdapter = new ListViewAdapterAdStates(AdStatesActivity.this);
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
                params.put("country_id", GlobalClass.selectedAddPostCountryID);
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void initUI() {
        selectedAddPostStateIDs = new StringBuilder("");
        selectedAddPostStateNames = new StringBuilder("");
        GlobalClass.selectedAddPostStateID = "";
        GlobalClass.selectedAddPostState = "";

        sbSelectAll = (SwitchButton) findViewById(R.id.sbSelectAll);
        tvCategoryName = (TextView) findViewById(R.id.tvCategoryName);
        tvBack = (TextView) findViewById(R.id.tvBack);
        tvCountryName = (TextView) findViewById(R.id.tvCountryName);
        tvCountryName.setText(GlobalClass.selectedAddPostCountry);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        etSearch = (EditText) findViewById(R.id.etSearch);
        ivSearch = (ImageView) findViewById(R.id.ivSearch);
        progressBar.bringToFront();

        bSave = (Button) findViewById(R.id.bSave);
        bSetCiies = (Button) findViewById(R.id.bSetCities);
        bSave.setOnClickListener(this);
        bSetCiies.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bSave:
                /*if (!GlobalClass.selectedAddPostStateID.equals("")) {
                    savedAdLocations = true;
                    tvSaveAdLocations.setTextColor(getResources().getColor(R.color.text_color_save_config_done));
                    Drawable img = getResources().getDrawable(R.drawable.ic_done);
                    tvSaveAdLocations.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    AdCountriesActivity.adCountriesActivity.finish();
                    finish();
                } else {
                    Toast.makeText(mContext, "Turn on a state", Toast.LENGTH_SHORT).show();
                }*/

                if (!GlobalClass.selectedAddPostStateID.equals("")) {
                    /*savedAdLocations = true;
                    tvSaveAdLocations.setTextColor(getResources().getColor(R.color.text_color_save_config_done));
                    Drawable img = getResources().getDrawable(R.drawable.ic_done);
                    tvSaveAdLocations.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    AdStatesActivity.adStatesActivity.finish();
                    AdCountriesActivity.adCountriesActivity.finish();
                    finish();*/
                    progressBar.setVisibility(View.VISIBLE);
                    uploadAd(addItemAct);
                } else {
                    Toast.makeText(mContext, "Turn on a state", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bSetCities:
                if (!GlobalClass.selectedAddPostStateID.equals("")) {
                    savedAdLocations = false;
                    Intent i = new Intent(AdStatesActivity.this, AdCitiesActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(mContext, "Turn on a state", Toast.LENGTH_SHORT).show();
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