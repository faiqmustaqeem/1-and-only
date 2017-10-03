package com.codiansoft.oneandonly;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codiansoft.oneandonly.adapter.CategoriesRegisterListViewAdapter;
import com.codiansoft.oneandonly.dialog.RegisterDialog;
import com.codiansoft.oneandonly.model.SetCategoriesStatusDataModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.codiansoft.oneandonly.MainActivity.categories;
import static com.codiansoft.oneandonly.dialog.RegisterDialog.tvSaveConfigurations;

public class Level2Activity extends AppCompatActivity implements View.OnClickListener {

    public static ListView mListView;
    public static CategoriesRegisterListViewAdapter mAdapter;
    private Context mContext = this;
    Button bSave;
    ProgressBar progressBar;

    public static ArrayList<SetCategoriesStatusDataModel> categoriesRegisterDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2);

//        getUserCountry();
        initUI();

        categoriesRegisterDataModel = new ArrayList<>();

        mListView = (ListView) findViewById(R.id.listview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setTitle("ListView");
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

        /*cities.add(new SetCategoriesStatusDataModel("Bungalow ideal for big family", "For Rent", "Peshawar", "6:54", "1", "", "03001234567", "03112345678", "xyz@abc.com", "", "32.173942", "-110.820111", "12"));
        cities.add(new SetCategoriesStatusDataModel("Golf Villa", "For Sale", "Quetta", "3:56", "1", "", "03001234567", "03112345678", "xyz@abc.com", "", "32.173942", "-110.820111", "12"));
        cities.add(new SetCategoriesStatusDataModel("Basement For Rent", "For Rent", "Sargodha", "5:56", "1", "", "03001234567", "03112345678", "xyz@abc.com", "", "32.173942", "-110.820111", "12"));
        cities.add(new SetCategoriesStatusDataModel("Property #10", "For Sale", "Sialkot", "5:34", "1", "", "03001234567", "03112345678", "xyz@abc.com", "", "32.173942", "-110.820111", "12"));
        cities.add(new SetCategoriesStatusDataModel("Property #11", "For Sale", "Karachi", "21:21", "1", "", "03001234567", "03112345678", "xyz@abc.com", "", "32.173942", "-110.820111", "12"));
        cities.add(new SetCategoriesStatusDataModel("Property #12", "For Sale", "Lahore", "12:01", "1", "", "03001234567", "03112345678", "xyz@abc.com", "", "32.173942", "-110.820111", "12"));
        cities.add(new SetCategoriesStatusDataModel("Property #13", "For Sale", "Lahore", "23:07", "1", "", "03001234567", "03112345678", "xyz@abc.com", "", "32.173942", "-110.820111", "12"));
*/

        SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        String userID = settings.getString("userID", "defaultValue");
        String apiSecretKey = settings.getString("apiSecretKey", "defaultValue");

        if (GlobalClass.userNotRegisteredYet | apiSecretKey.equals("defaultValue") | apiSecretKey.equals("")) {
            getCategories();
        } else {
            for (int i = 0; i < categories.size(); i++) {
                categoriesRegisterDataModel.add(new SetCategoriesStatusDataModel(categories.get(i).getName(), categories.get(i).getLastUpdated(), categories.get(i).getCategory_Id(), categories.get(i).getQuantity(), categories.get(i).getCategorySatus()));
            }
            mAdapter = new CategoriesRegisterListViewAdapter(Level2Activity.this, categoriesRegisterDataModel);
            mListView.setAdapter(mAdapter);
        }


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ((SwipeLayout) (mListView.getChildAt(position - mListView.getFirstVisiblePosition()))).open(true);

               /* Intent i = new Intent(Level2Activity.this, Level3Activity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);*/

            }
        });

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("ListView", "OnTouch");
                return false;
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.e("ListView", "onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ListView", "onItemSelected:" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("ListView", "onNothingSelected:");
            }
        });
    }

    private void getUserCountry() {

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                JSONObject user_data = result.getJSONObject("user_data");
                                GlobalClass.userCountryID = user_data.getString("country");
                            }
                        } catch (Exception ee) {
                            Toast.makeText(Level2Activity.this, ee.toString(), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(Level2Activity.this, "Error " + 409, Toast.LENGTH_SHORT).show();
//                                    utilities.dialog("Already Exist", act);
                                    break;
                                case 400:
//                                    utilities.dialog("Connection Problem", act);
                                    Toast.makeText(Level2Activity.this, "Error " + 400, Toast.LENGTH_SHORT).show();
                                    break;
                                default:
//                                    utilities.dialog("Connection Problem", act);
                                    Toast.makeText(Level2Activity.this, "Unexpected Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            Toast.makeText(Level2Activity.this, "Unexpected Connection Error", Toast.LENGTH_SHORT).show();
                            finish();
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
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void getCategories() {
        categoriesRegisterDataModel.clear();

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.CATEGORIES_URL,
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
                                    JSONObject category = data.getJSONObject(i);
                                    categoriesRegisterDataModel.add(new SetCategoriesStatusDataModel(category.getString("name"), category.getString("last_updated"), category.getString("category_Id"), category.getString("ads_nums"), "" + category.getInt("status")));
                                }
                                mAdapter = new CategoriesRegisterListViewAdapter(Level2Activity.this, categoriesRegisterDataModel);
                                mListView.setAdapter(mAdapter);
                            }

                        } catch (Exception ee) {
                            Toast.makeText(mContext, ee.toString(), Toast.LENGTH_SHORT).show();
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
//                                    utilities.dialog("Connection Problem", act);
                                    break;
                                default:
                                    Toast.makeText(mContext, "Unexpected Error " + response.statusCode, Toast.LENGTH_SHORT).show();
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

    private void initUI() {
        bSave = (Button) findViewById(R.id.bSave);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        bSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bSave:
                SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String userID = settings.getString("userID", "defaultValue");
                String apiSecretKey = settings.getString("apiSecretKey", "defaultValue");
                if (GlobalClass.userNotRegisteredYet | apiSecretKey.equals("defaultValue") | apiSecretKey.equals("")) {
                    RegisterDialog.savedConfigurations = true;
                    tvSaveConfigurations.setTextColor(getResources().getColor(R.color.text_color_save_config_done));
                    Drawable img = getResources().getDrawable(R.drawable.ic_done);
                    tvSaveConfigurations.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    finish();

                } else {
                    setCategoriesStatusForUser();
                }
                break;
        }
    }

    private void setCategoriesStatusForUser() {

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.CATEGORIES_STATUS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {

                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("updated")) {

                                Toast.makeText(Level2Activity.this, "Configurations Saved", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Level2Activity.this, SplashActivity.class);
                                startActivity(i);
                                ActivityCompat.finishAffinity(Level2Activity.this);

                            }

                        } catch (Exception ee) {
                            Toast.makeText(mContext, ee.toString(), Toast.LENGTH_SHORT).show();
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
//                                    utilities.dialog("Connection Problem", act);
                                    break;
                                default:
                                    Toast.makeText(mContext, "Unexpected Error " + response.statusCode, Toast.LENGTH_SHORT).show();
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

                SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String userID = settings.getString("userID", "defaultValue");
                String apiSecretKey = settings.getString("apiSecretKey", "defaultValue");

                Map<String, String> params = new HashMap<String, String>();

                params.put("api_secret", apiSecretKey);

                String id = "";
                String status = "";
                for (int i = 0; i < categoriesRegisterDataModel.size(); i++) {
                    id = id + categoriesRegisterDataModel.get(i).getID() + ",";
                    status = status + categoriesRegisterDataModel.get(i).getStatus() + ",";
                }

                params.put("cat_id", "" + id.substring(0, id.length() - 1) + "");
                params.put("status", "" + status.substring(0, status.length() - 1) + "");

                return params;
            }
        };
        queue.add(postRequest);

    }
}