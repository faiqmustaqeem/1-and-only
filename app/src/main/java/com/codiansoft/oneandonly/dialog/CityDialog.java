package com.codiansoft.oneandonly.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
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
import com.codiansoft.oneandonly.AddItemActivity;
import com.codiansoft.oneandonly.GlobalClass;
import com.codiansoft.oneandonly.R;
import com.codiansoft.oneandonly.adapter.CityDialogAdapter;
import com.codiansoft.oneandonly.model.CitiesDataModel;
import com.codiansoft.oneandonly.model.CityModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Codiansoft on 3/9/2018.
 */

public class CityDialog extends Dialog {
    Activity activity;
    Dialog dialog;
    RecyclerView recyclerView;
    TextView ok;
    CityDialogAdapter adapter;
    List<CityModel> cityModelList;

    ProgressBar progressBar;
    String state_id;
    TextView state_name;
    String strStateName;

    public CityDialog(Activity activity , List<CityModel> list, String state_id , String state_name) {
        super(activity);

        this.activity=activity;
        this.cityModelList=list;
        this.dialog=this;
        this.state_id=state_id;
        this.strStateName=state_name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_city_dialog);

        recyclerView=(RecyclerView)findViewById(R.id.recycleview_city);
        ok=(TextView)findViewById(R.id.ok);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        state_name=(TextView)findViewById(R.id.state_name);
        state_name.setText(strStateName);





        adapter = new CityDialogAdapter(cityModelList , activity );
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        fetchCities(state_id);

    }
    private void fetchCities(final String state_id) {


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(activity);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_CITIES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("response_city" , response);
                        try {
                            cityModelList.clear();

                            JSONArray statesArray = new JSONArray(response);
                            for (int i = 0; i < statesArray.length(); i++) {
                                JSONObject stateObj = statesArray.getJSONObject(i);
                                cityModelList.add(new CityModel( stateObj.getString("name"), stateObj.getString("id")));

                            }

                           adapter.notifyDataSetChanged();
                        } catch (Exception ee) {
                            Toast.makeText(activity, "error: " + ee.toString(), Toast.LENGTH_SHORT).show();

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
                                    Toast.makeText(activity, "Try again", Toast.LENGTH_SHORT).show();
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
                params.put("state_id", state_id);

                return params;
            }
        };
        queue.add(postRequest);
    }

}
