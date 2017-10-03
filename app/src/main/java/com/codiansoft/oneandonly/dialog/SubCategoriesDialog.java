package com.codiansoft.oneandonly.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import com.codiansoft.oneandonly.GlobalClass;
import com.codiansoft.oneandonly.AdListViewActivity;
import com.codiansoft.oneandonly.R;
import com.codiansoft.oneandonly.adapter.SubCategoriesAdapter;
import com.codiansoft.oneandonly.model.CategoriesModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by salal-khan on 6/15/2017.
 */

public class SubCategoriesDialog extends Dialog {

    ProgressBar progressBar;
    public Activity act;
    ListView lvSubCategories;
    ArrayList<String> sub_categories = new ArrayList<String>();
    public static ArrayList<CategoriesModel> categoriesDataModel;
    private SubCategoriesAdapter mAdapter;

    public SubCategoriesDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.act = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.subcategories_dialog);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        setCanceledOnTouchOutside(false);

        initUI();

        lvSubCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                TextView textView = (TextView) view.findViewById(R.id.tvSubCategoryName);
                GlobalClass.selectedSubCategory = ((TextView)textView).getText().toString();

                Intent electronicsIntent = new Intent(act, AdListViewActivity.class);
                act.startActivity(electronicsIntent);

            }

        });
    }

    private void initUI() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        lvSubCategories = (ListView) findViewById(R.id.lvSubCategories);

        String[] values = new String[]{
                "Audio and Hi-Fi",
                "Cameras and Photography",
                "Computing",
                "Consoles and Computer Games",
                "Kitchen appliances",
                "Mobile Phones & phones",
                "Satellite, Cable",
                "Small Electricals",
                "Televisions",
                "Video Cameras and Accessories",
                "Media All types",
                "E Books",
                "Solar equipment"
        };

        getSubCategories();

    }

    private void getSubCategories() {
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(act);

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

                                for (int i = 0; i < Data.length(); i++) {
                                    JSONObject categoryObj = Data.getJSONObject(i);

                                    if (categoryObj.getString("category_Id").equals(GlobalClass.selectedCategoryID)) {
                                        JSONArray subCatArray = categoryObj.getJSONArray("sub_category");
                                        for (int j = 0; j < subCatArray.length(); j++) {
                                            sub_categories.add(subCatArray.getJSONObject(j).getString("sub_cat_name"));
                                        }
                                    }


//                                    categoriesDataModel.add(new CategoriesModel(categoryObj.getString("category_Id"), categoryObj.getString("name"), categoryObj.getString("description"), sub_categories));

                                }

                                /*mAdapter = new SubCategoriesAdapter(act, sub_categories_names, sub_categories_last_update_time, sub_categories);
                                lvSubCategories.setAdapter(mAdapter);*/
                                progressBar.setVisibility(View.GONE);

                            }

                        } catch (Exception ee) {
                            Toast.makeText(act, ee.toString(), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        queue.add(postRequest);

    }

}
