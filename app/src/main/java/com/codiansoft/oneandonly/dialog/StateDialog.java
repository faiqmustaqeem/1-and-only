package com.codiansoft.oneandonly.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.codiansoft.oneandonly.adapter.CountryDialogAdapter;
import com.codiansoft.oneandonly.adapter.StateDialogAdapter;
import com.codiansoft.oneandonly.model.CountryModel;
import com.codiansoft.oneandonly.model.StateModel;
import com.codiansoft.oneandonly.model.StatesDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Codiansoft on 3/8/2018.
 */

public class StateDialog extends Dialog {
    Activity activity;
    Dialog dialog;
    RecyclerView recyclerView;
    TextView ok;
    StateDialogAdapter adapter;
    List<StateModel> stateModelList;

    ProgressBar progressBar;
    String country_id;
    TextView country_name;
    String strCountryName;

    public StateDialog(Activity activity , List<StateModel> list, String country_id , String country_name) {
        super(activity);

        this.activity=activity;
        this.stateModelList=list;
        this.dialog=this;
        this.country_id=country_id;
        this.strCountryName=country_name;
    }
    public List<StateModel> getFilteredList()
    {
        List<StateModel> tempList=new ArrayList<>();
        for (int i= 0 ; i < stateModelList.size() ; i++)
        {
            if(stateModelList.get(i).isFilterSelected())
            {
                tempList.add(stateModelList.get(i));
            }
        }
        return tempList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.state_dialog_layout);

        recyclerView=(RecyclerView)findViewById(R.id.recycleview_state);
        ok=(TextView)findViewById(R.id.ok);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        country_name=(TextView)findViewById(R.id.country_name);
        country_name.setText(strCountryName);





        adapter = new StateDialogAdapter(stateModelList , activity );
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        fetchStates(country_id);

    }

    private void fetchStates(final String countryID) {

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(activity);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_STATES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            stateModelList.clear();

                            JSONArray statesArray = new JSONArray(response);
                            for (int i = 0; i < statesArray.length(); i++) {
                                JSONObject stateObj = statesArray.getJSONObject(i);
                                stateModelList.add(new StateModel( stateObj.getString("name") , stateObj.getString("id")));

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
                params.put("country_id", country_id);

                return params;
            }
        };
        queue.add(postRequest);


    }

}
