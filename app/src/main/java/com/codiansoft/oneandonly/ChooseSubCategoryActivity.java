package com.codiansoft.oneandonly;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.codiansoft.oneandonly.adapter.SubCategoriesAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.codiansoft.oneandonly.MainActivity.categories;

public class ChooseSubCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressBar progressBar;
    private SubCategoriesAdapter mAdapter;
    Button button;
    ListView lvSubCategories;
    ArrayList<String> sub_categories_names = new ArrayList<String>();
    ArrayList<String> sub_categories_last_update_time = new ArrayList<String>();
    ArrayList<String> sub_categories_ads_quantity = new ArrayList<String>();
    TextView tvCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subcategories_dialog);

        initUI();

        lvSubCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                /*TextView textView = (TextView) view.findViewById(R.id.tvSubCategoryName);
                GlobalClass.selectedSubCategory = ((TextView)textView).getText().toString();*/
                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) lvSubCategories.getItemAtPosition(position);
                GlobalClass.selectedSubCategory = itemValue;

                outerLoop:

                for (int i = 0; i < categories.get(Integer.parseInt(GlobalClass.selectedCategoryID) - 1).getSubCategoriesIDs().size(); i++) {
                    if (categories.get(Integer.parseInt(GlobalClass.selectedCategoryID) - 1).getSubCategoriesNames().get(i).equals(GlobalClass.selectedSubCategory)) {
                        GlobalClass.selectedSubCategoryID = categories.get(Integer.parseInt(GlobalClass.selectedCategoryID) - 1).getSubCategoriesIDs().get(i);
                        break outerLoop;
                    }
                }

                GlobalClass.selectedSubCatDes1Title = categories.get(Integer.parseInt(GlobalClass.selectedCategoryID) - 1).getSubCatDesTitles().get(position).get(0);
                GlobalClass.selectedSubCatDes2Title = categories.get(Integer.parseInt(GlobalClass.selectedCategoryID) - 1).getSubCatDesTitles().get(position).get(1);
                GlobalClass.selectedSubCatDes3Title = categories.get(Integer.parseInt(GlobalClass.selectedCategoryID) - 1).getSubCatDesTitles().get(position).get(2);
                GlobalClass.selectedSubCatDes4Title = categories.get(Integer.parseInt(GlobalClass.selectedCategoryID) - 1).getSubCatDesTitles().get(position).get(3);

//                Toast.makeText(ChooseSubCategoryActivity.this, GlobalClass.selectedSubCatDes1Title + GlobalClass.selectedSubCatDes2Title + GlobalClass.selectedSubCatDes3Title + GlobalClass.selectedSubCatDes4Title, Toast.LENGTH_LONG).show();

                Intent i = new Intent(ChooseSubCategoryActivity.this, AdListViewActivity.class);
                startActivity(i);

            }

        });
    }

    private void initUI() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        lvSubCategories = (ListView) findViewById(R.id.lvSubCategories);
        tvCategory = (TextView) findViewById(R.id.tvCategory);
        tvCategory.setText(GlobalClass.selectedCategory);

        getSubCategories();

    }

    private void getSubCategories() {
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(ChooseSubCategoryActivity.this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.CATEGORIES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {

                            JSONObject Jobject = new JSONObject(response);
                           // JSONObject result = Jobject.getJSONObject("result");
                           // if (result.get("status").equals("success"))
                            {
                                JSONObject Data = Jobject.getJSONObject("data");

                                sub_categories_names.clear();
                                sub_categories_last_update_time.clear();
                                sub_categories_ads_quantity.clear();

                                for (int i = 1; i <= 12; i++) {

                                    JSONArray categoryObj = Data.getJSONArray(String.valueOf(i));


                                    if (categoryObj.getJSONObject(0).getString("category_Id").equals(GlobalClass.selectedCategoryID)) {

                                        Log.e("selected_category_id" , GlobalClass.selectedCategoryID);
                                       // Log.e("category_Id",categoryObj.getString("category_Id"));

                                        sub_categories_last_update_time.add("");
                                        sub_categories_ads_quantity.add("");
                                     //   JSONArray subCatArray = categoryObj.getJSONArray("sub_category");

                                        Log.e("size_sub_cty" , categoryObj.length()+"");
                                        for (int j = 0; j < categoryObj.length(); j++) {
                                            sub_categories_names.add(categoryObj.getJSONObject(j).getString("sub_cat_name"));

                                            if(j == categoryObj.length()-1)
                                            {

                                                mAdapter = new SubCategoriesAdapter(ChooseSubCategoryActivity.this, sub_categories_names, sub_categories_last_update_time, sub_categories_ads_quantity);
                                                lvSubCategories.setAdapter(mAdapter);
                                                progressBar.setVisibility(View.GONE);
                                                break;
                                            }


                                        }
                                    }

//                                    categoriesDataModel.add(new CategoriesModel(categoryObj.getString("category_Id"), categoryObj.getString("name"), categoryObj.getString("description"), sub_categories_names));

                                }
                                Log.e("size" , sub_categories_names.size()+"");


                            }

                        } catch (Exception ee) {
                            Toast.makeText(ChooseSubCategoryActivity.this, ee.toString(), Toast.LENGTH_SHORT).show();
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
//                                    utilities.dialog("Already Exist", act);
                                    break;
                                case 400:
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
                SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String userID = settings.getString("userID", "defaultValue");
                String apiSecretKey = settings.getString("apiSecretKey", "defaultValue");

                Map<String, String> params = new HashMap<String, String>();
                params.put("api_secret", apiSecretKey);
                Log.e("params" , params.toString());
                return params;
            }
        };
        queue.add(postRequest);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }
}
