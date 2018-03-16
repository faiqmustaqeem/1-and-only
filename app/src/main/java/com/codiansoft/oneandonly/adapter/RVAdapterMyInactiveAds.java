package com.codiansoft.oneandonly.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.codiansoft.oneandonly.AdDetailsActivity;
import com.codiansoft.oneandonly.EditMyAdActivity;
import com.codiansoft.oneandonly.GlobalClass;
import com.codiansoft.oneandonly.R;
import com.codiansoft.oneandonly.model.MyAdsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.codiansoft.oneandonly.AdDetailsActivity.isMyAd;
import static com.codiansoft.oneandonly.AdDetailsActivity.selectedMyAdDataModel;
import static com.codiansoft.oneandonly.fragment.InactiveAdsFragment.progressBar;

/**
 * Created by Codiansoft on 8/2/2017.
 */

public class RVAdapterMyInactiveAds extends RecyclerView.Adapter<RVAdapterMyInactiveAds.MyViewHolder> {

    ArrayList<MyAdsModel> dataModels;
    Context c;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtName;
        TextView txtDescription;
        TextView txtLastUpdateTime;
        TextView tvPrice;
        TextView tvCurrencyCode;
        TextView txtID;
        TextView t;
        ImageView ivAdPic, ivOptions;

        public MyViewHolder(View view)
        {
            super(view);
            txtName = (TextView) view.findViewById(R.id.tvTitle);
            txtDescription = (TextView) view.findViewById(R.id.tvDescription);
            txtLastUpdateTime = (TextView) view.findViewById(R.id.tvLastUpdateTime);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            tvCurrencyCode = (TextView) view.findViewById(R.id.tvCurrencyCode);
            txtID = (TextView) view.findViewById(R.id.tvID);
            t = (TextView) view.findViewById(R.id.position);
            ivAdPic = (ImageView) view.findViewById(R.id.ivAdImage);
            ivOptions = (ImageView) view.findViewById(R.id.ivOptions);


            ivOptions.setOnClickListener(this);
            ivAdPic.setOnClickListener(this);
            txtDescription.setOnClickListener(this);
            txtName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ivOptions:
                    //Creating the instance of PopupMenu
                    PopupMenu popup = new PopupMenu(c, ivOptions);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.my_ads_inactive_popup_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {

                                case R.id.my_ad_activate:
                                    new android.support.v7.app.AlertDialog.Builder(c)
                                            .setTitle("Activate ad " + dataModels.get(getAdapterPosition()).getName() + "?")
                                            .setCancelable(false)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    updateAdStatus("a", dataModels.get(getAdapterPosition()).getID(), getAdapterPosition());
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // do nothing
                                                }
                                            }).show();
                                    break;

                                case R.id.my_ad_view:
                                    
                                    selectedMyAdDataModel = dataModels.get(getAdapterPosition());
                                    GlobalClass.selectedAdImages = selectedMyAdDataModel.getAdImages();

                                    GlobalClass.selectedPropertyName = selectedMyAdDataModel.getName();
                                    GlobalClass.selectedPropertyCity = selectedMyAdDataModel.getCity();
                                    GlobalClass.selectedPropertyUpdateTime = selectedMyAdDataModel.getLastUpdateTime();
                                    GlobalClass.selectedPropertyID = selectedMyAdDataModel.getID();
                                    GlobalClass.selectedPropertyDetails = selectedMyAdDataModel.getDetails();
                                    GlobalClass.selectedPropertyContact1 = selectedMyAdDataModel.getContact1();
                                    GlobalClass.selectedPropertyContact2 = selectedMyAdDataModel.getContact2();
                                    GlobalClass.selectedPropertyEmail = selectedMyAdDataModel.getEmail();
                                    GlobalClass.selectedPropertyImageURL = selectedMyAdDataModel.getImageURL();
                                    GlobalClass.selectedPropertyCategory = selectedMyAdDataModel.getCategory();
                                    GlobalClass.selectedPropertyLatitude = selectedMyAdDataModel.getLatitude();
                                    GlobalClass.selectedPropertyLongitude = selectedMyAdDataModel.getLongitude();

                                    isMyAd = true;

                                    fetchDescriptionHeadings();

                                    break;

                                case R.id.my_ad_delete:
                                    new android.support.v7.app.AlertDialog.Builder(c)
                                            .setTitle("Are you sure to delete this ad " + dataModels.get(getAdapterPosition()).getName() + "?")
                                            .setCancelable(false)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    updateAdStatus("d", dataModels.get(getAdapterPosition()).getID(), getAdapterPosition());
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // do nothing
                                                }
                                            }).show();
                                    break;

                                case R.id.my_ad_edit:
                                    GlobalClass.myAdEditID = dataModels.get(getAdapterPosition()).getID();
                                    GlobalClass.myAdEditTitle = dataModels.get(getAdapterPosition()).getName();
                                    GlobalClass.myAdEditDescription = dataModels.get(getAdapterPosition()).getDetails();
                                    GlobalClass.myAdEditCurrencyCode = dataModels.get(getAdapterPosition()).getCurrencyCode();
                                    GlobalClass.myAdEditPrice = dataModels.get(getAdapterPosition()).getPrice();

                                    Intent editAdIntent = new Intent(c, EditMyAdActivity.class);
                                    c.startActivity(editAdIntent);
                                    break;
                            }
                            return true;
                        }
                    });

                    popup.show(); //showing popup menu

                    break;
                case R.id.tvName:
                case R.id.tvTitle:
                case R.id.tvDescription:
                case R.id.ivAdImage:
                    selectedMyAdDataModel = dataModels.get(getAdapterPosition());
                    GlobalClass.selectedAdImages = selectedMyAdDataModel.getAdImages();

                    GlobalClass.selectedPropertyName = selectedMyAdDataModel.getName();
                    GlobalClass.selectedPropertyCity = selectedMyAdDataModel.getCity();
                    GlobalClass.selectedPropertyUpdateTime = selectedMyAdDataModel.getLastUpdateTime();
                    GlobalClass.selectedPropertyID = selectedMyAdDataModel.getID();
                    GlobalClass.selectedPropertyDetails = selectedMyAdDataModel.getDetails();
                    GlobalClass.selectedPropertyContact1 = selectedMyAdDataModel.getContact1();
                    GlobalClass.selectedPropertyContact2 = selectedMyAdDataModel.getContact2();
                    GlobalClass.selectedPropertyEmail = selectedMyAdDataModel.getEmail();
                    GlobalClass.selectedPropertyImageURL = selectedMyAdDataModel.getImageURL();
                    GlobalClass.selectedPropertyCategory = selectedMyAdDataModel.getCategory();
                    GlobalClass.selectedPropertyLatitude = selectedMyAdDataModel.getLatitude();
                    GlobalClass.selectedPropertyLongitude = selectedMyAdDataModel.getLongitude();

                    isMyAd = true;

                    fetchDescriptionHeadings();
                    break;
            }
        }

        private void fetchDescriptionHeadings() {
            progressBar.setVisibility(View.VISIBLE);

            RequestQueue queue = Volley.newRequestQueue(c);

            StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.CATEGORIES_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            try {
                                JSONObject Jobject = new JSONObject(response);
                                JSONObject result = Jobject.getJSONObject("result");
                                if (result.get("status").equals("success")) {
                                    JSONArray Data = Jobject.getJSONArray("data");
                                    int count;
                                    for (int i = 0; i < Data.length(); i++) {
                                        JSONObject categoryObj = Data.getJSONObject(i);

                                        count = 0;

                                        if (selectedMyAdDataModel.getCategory().equals(categoryObj.getString("category_Id"))) {
                                            JSONArray subCatArr = categoryObj.getJSONArray("sub_category");
                                            for (int j = 0; j < subCatArr.length(); j++) {
                                                if (selectedMyAdDataModel.getSubCategory().equals(subCatArr.getJSONObject(j).getString("id"))) {
                                                    GlobalClass.selectedSubCatDes1Title = subCatArr.getJSONObject(j).getString("dis_1");
                                                    GlobalClass.selectedSubCatDes2Title = subCatArr.getJSONObject(j).getString("dis_2");
                                                    GlobalClass.selectedSubCatDes3Title = subCatArr.getJSONObject(j).getString("dis_3");
                                                    GlobalClass.selectedSubCatDes4Title = subCatArr.getJSONObject(j).getString("dis_4");
                                                    count = 1;
                                                    break;
                                                }
                                            }
                                            if (count == 1) break;
                                        }
                                    }
                                    Intent i = new Intent(c, AdDetailsActivity.class);
                                    i.putExtra("restaurantName", selectedMyAdDataModel.getName());
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    c.startActivity(i);
                                    progressBar.setVisibility(View.GONE);
                                }
                            } catch (Exception ee) {
                                Toast.makeText(c, ee.toString(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                switch (response.statusCode) {
                                    case 409:
                                        Toast.makeText(c, "Error " + 409, Toast.LENGTH_SHORT).show();
//                                    utilities.dialog("Already Exist", act);
                                        break;
                                    case 400:
//                                    utilities.dialog("Connection Problem", act);
                                        Toast.makeText(c, "Error " + 400, Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
//                                    utilities.dialog("Connection Problem", act);
                                        Toast.makeText(c, "Unexpected Server Error", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            } else {
                                Toast.makeText(c, "Unexpected Connection Error", Toast.LENGTH_SHORT).show();
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

        private void updateAdStatus(final String adStatus, final String adID, final int adapterPosition) {

            progressBar.setVisibility(View.VISIBLE);

            RequestQueue queue = Volley.newRequestQueue(c);

            StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.MY_AD_STATUS_CHANGE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            try {
                                JSONObject Jobject = new JSONObject(response);
                                JSONObject result = Jobject.getJSONObject("result");
                                if (result.get("status").equals("success")) {
                                    dataModels.remove(adapterPosition);
                                    notifyDataSetChanged();
                                } else if (result.get("status").equals("error")) {
                                    Toast.makeText(c, "Try again", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception ee) {
                                Toast.makeText(c, "error: " + ee.toString(), Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(c, "Try again", Toast.LENGTH_SHORT).show();
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

                    SharedPreferences settings = c.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                    String userID = settings.getString("userID", "defaultValue");
                    String apiSecretKey = settings.getString("apiSecretKey", "");
                    String email = settings.getString("email", "");
                    String contactNum1 = settings.getString("contactNum1", "");
                    String contactNum2 = settings.getString("contactNum2", "");

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_secret", apiSecretKey);
                    params.put("ad_id", adID);
                    params.put("status", adStatus);

                    return params;
                }
            };
            queue.add(postRequest);

        }
    }


    public RVAdapterMyInactiveAds(Context c, ArrayList<MyAdsModel> dataModels) {
        this.dataModels = dataModels;
        this.c = c;
    }

    @Override
    public RVAdapterMyInactiveAds.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_my_ad_item, parent, false);

        return new RVAdapterMyInactiveAds.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RVAdapterMyInactiveAds.MyViewHolder holder, int position) {

        MyAdsModel dataModel = dataModels.get(position);

        holder.txtName.setText(dataModel.getName());
        holder.txtDescription.setText(dataModel.getDetails());
        holder.txtLastUpdateTime.setText(dataModel.getLastUpdateTime());
        holder.tvPrice.setText(dataModel.getPrice());
        holder.tvCurrencyCode.setText(dataModel.getCurrencyCode());
        holder.txtID.setText(dataModel.getID());
//        Glide.with(mContext).load(dataModel.getImageURL()).into(viewHolder.ivAdPic);
        try {

            Glide.with(c).
                    load(dataModel.getAdImages().get(0))
                    .placeholder(R.drawable.sample_property_pic2)
                    .error(R.drawable.sample_property_pic2)
                    .into(holder.ivAdPic);

        } catch (IndexOutOfBoundsException e) {

            Glide.with(c).
                    load(R.drawable.sample_property_pic2)
                    .into(holder.ivAdPic);
        }
    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }
}