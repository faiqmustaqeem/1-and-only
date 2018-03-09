package com.codiansoft.oneandonly.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.codiansoft.oneandonly.adapter.CountryDialogAdapter;
import com.codiansoft.oneandonly.model.CountryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Codiansoft on 3/8/2018.
 */

public class CountryDialog extends Dialog {
    Activity activity;
    Dialog dialog;
    RecyclerView recyclerView;
    TextView ok;
    CountryDialogAdapter adapter;
    List<CountryModel> countryModelList;

    ProgressBar progressBar;


    public CountryDialog(Activity activity , List<CountryModel> list) {
        super(activity);

        this.activity=activity;
        this.countryModelList=list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.country_dialog_layout);

        recyclerView=(RecyclerView)findViewById(R.id.recycleview_country);
        ok=(TextView)findViewById(R.id.ok);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);




        adapter = new CountryDialogAdapter(countryModelList , activity );
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        fetchCountries();

    }

    public List<CountryModel> getFilteredCountries()
    {
        List<CountryModel> tempList=new ArrayList<>();
        for ( int i=0 ; i < countryModelList.size() ; i++  )
        {
            if(countryModelList.get(i).isFilterSelected())
            {
                tempList.add(countryModelList.get(i));
            }
        }
        return tempList;
    }

    private void fetchCountries() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(activity);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_COUNTRIES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONArray countriesArr = new JSONArray(response);
                            for (int i = 0; i < countriesArr.length(); i++) {
                                JSONObject countryObj = countriesArr.getJSONObject(i);
                                countryModelList.add(new CountryModel(countryObj.getString("name"),countryObj.getString("id")));

                            }

                            adapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (progressBar.isShown()) {
                            progressBar.setVisibility(View.INVISIBLE);
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
                            progressBar.setVisibility(View.INVISIBLE);
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

}
