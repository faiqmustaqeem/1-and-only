package com.codiansoft.oneandonly.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codiansoft.oneandonly.GlobalClass;
import com.codiansoft.oneandonly.R;
import com.codiansoft.oneandonly.adapter.RVAdapterMyActiveAds;
import com.codiansoft.oneandonly.model.MyAdsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Codiansoft on 8/1/2017.
 */

public class ActiveAdsFragment extends Fragment {
    RecyclerView rvActiveAds;
    public static ProgressBar progressBar;
    public static MyAdsModel selectedItemDataModel;
    private RVAdapterMyActiveAds mAdapter;

    public static ArrayList<MyAdsModel> dataModels;

    String userID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_active_ads, container, false);
        rvActiveAds = (RecyclerView) rootView.findViewById(R.id.rvActiveAds);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.bringToFront();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvActiveAds.setLayoutManager(mLayoutManager);
        rvActiveAds.setItemAnimator(new DefaultItemAnimator());
//        mAdapter = new RVAdapterMyActiveAds(getActivity(), dataModels);

        SharedPreferences settings = getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        userID = settings.getString("userID", "defaultValue");
        fetchAds();

        return rootView;
    }

    private void fetchAds() {
        dataModels = new ArrayList<MyAdsModel>();
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_MY_ADS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                JSONArray data = Jobject.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject adObj = data.getJSONObject(i);
                                    if (adObj.getString("user_id").equals(userID)) {
                                        if (adObj.getString("active").equals("0")) {
                                            ArrayList<String> adImages = new ArrayList<String>();
                                            JSONArray adImagesArr = adObj.getJSONArray("images");

                                            for (int j = 0; j < adImagesArr.length(); j++) {
                                                adImages.add(adImagesArr.getJSONObject(j).getString("path"));
                                            }
                                            ArrayList<String> adImagesTemp = adImages;
                                            dataModels.add(new MyAdsModel(adObj.getString("title"), adObj.getString("category_id"), adObj.getString("sub_cat_id"), adObj.getString("city"), adObj.getString("last_updated"), adObj.getString("id"), adObj.getString("description"), adObj.getString("mobile_number"), adObj.getString("phone_number"), adObj.getString("email"), "https://cdn.houseplans.com/product/o2d2ui14afb1sov3cnslpummre/w560x373.jpg?v=15", adObj.getString("latitude"), adObj.getString("longitude"), adObj.getString("price"), adObj.getString("currency_code"), adObj.getString("country_name"), adObj.getString("state_name"), adObj.getString("city_name"), adImagesTemp, adObj.getString("dis_1"), adObj.getString("dis_2"), adObj.getString("dis_3"), adObj.getString("dis_4")));
                                        }
                                    }
                                }
                                Collections.reverse(dataModels);
                                mAdapter = new RVAdapterMyActiveAds(getActivity(), dataModels);

                                rvActiveAds.setAdapter(mAdapter);

                                Toast.makeText(getActivity(), "Ads fetched successfully!", Toast.LENGTH_SHORT).show();

                            } else if (result.get("status").equals("error")) {
                                Toast.makeText(getActivity(), "Recheck and try again", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ee) {

                        }

                        if (progressBar.isShown()) {
                            progressBar.setVisibility(View.GONE);
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
                                    Toast.makeText(getActivity(), "Try again", Toast.LENGTH_SHORT).show();
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
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                SharedPreferences settings = getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}