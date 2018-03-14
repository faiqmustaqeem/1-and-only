package com.codiansoft.oneandonly;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
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
import com.codiansoft.oneandonly.adapter.ListViewAdapter;
import com.codiansoft.oneandonly.model.PropertyListItemDataModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.codiansoft.oneandonly.GlobalClass.selectedItemDataModel;

public class FavouriteAddsListAcivity extends AppCompatActivity implements View.OnClickListener{
    public static ProgressBar progressBar;
    private ListView mListView;
    private ListViewAdapter mAdapter;
    private Context mContext = this;
    Button bAddProperty, bHome, bSettings;
    TextView tvCategory;

    public static ArrayList<PropertyListItemDataModel> dataModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        Log.e("listview" , "on create");

        initUI();

//        dataModels = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setTitle("Favourites");
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

        fetchAds();

//        mAdapter.setMode(Attributes.Mode.Single);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ((SwipeLayout) (mListView.getChildAt(position - mListView.getFirstVisiblePosition()))).open(true);

                selectedItemDataModel = dataModels.get(position);

                GlobalClass.selectedAdImages = selectedItemDataModel.getAdImages();

                GlobalClass.selectedPropertyName = selectedItemDataModel.getName();
                GlobalClass.selectedPropertyCity = selectedItemDataModel.getCity();
                GlobalClass.selectedPropertyUpdateTime = selectedItemDataModel.getLastUpdateTime();
                GlobalClass.selectedPropertyID = selectedItemDataModel.getID();
//                Toast.makeText(mContext, position+" "+GlobalClass.selectedPropertyID, Toast.LENGTH_SHORT).show();
                GlobalClass.selectedPropertyDetails = selectedItemDataModel.getDetails();
                GlobalClass.selectedPropertyContact1 = selectedItemDataModel.getContact1();
                GlobalClass.selectedPropertyContact2 = selectedItemDataModel.getContact2();
                GlobalClass.selectedPropertyEmail = selectedItemDataModel.getEmail();
                GlobalClass.selectedPropertyImageURL = selectedItemDataModel.getImageURL();
                GlobalClass.selectedPropertyCategory = selectedItemDataModel.getCategory();
                GlobalClass.selectedPropertyLatitude = selectedItemDataModel.getLatitude();
                GlobalClass.selectedPropertyLongitude = selectedItemDataModel.getLongitude();
                GlobalClass.selectedPropertyRefernceNumber=selectedItemDataModel.getReference_id();
                GlobalClass.otherAddsCount=selectedItemDataModel.getOtherAddsCount();

                AdDetailsActivity.isMyAd = false;
                GlobalClass.from="favourite";

                Intent i = new Intent(FavouriteAddsListAcivity.this, AdDetailsActivity.class);
                i.putExtra("restaurantName", selectedItemDataModel.getName());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("ListView", "OnTouch");
                return false;
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext, "OnItemLongClickListener", Toast.LENGTH_SHORT).show();
                return true;
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

    private void fetchAds() {
        dataModels = new ArrayList<PropertyListItemDataModel>();



        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_FAVOURITE_ADDS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.e("fetch_all_adds",response);
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                               // JSONObject dataObj = Jobject.getJSONObject("data");
                                JSONArray data=result.getJSONArray("data");

                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject adObj = data.getJSONObject(i).getJSONObject("data");
                                    {
                                        JSONObject obj=data.getJSONObject(i);
                                        ArrayList<String> adImages = new ArrayList<String>();

                                        JSONArray adImagesArr = data.getJSONObject(i).getJSONArray("images");

                                        if (adObj.getString("active").equals("0")) {

                                            for (int j = 0; j < adImagesArr.length(); j++) {
                                                adImages.add(adImagesArr.getJSONObject(j).getString("path"));
                                            }
                                            ArrayList<String> adImagesTemp = adImages;
                                            Log.e("title", adObj.getString("title"));
                                            dataModels.add(new PropertyListItemDataModel(adObj.getString("title"), "", adObj.getString("city"), adObj.getString("last_updated"), adObj.getString("id"), adObj.getString("description"), adObj.getString("mobile_number"), adObj.getString("phone_number"), adObj.getString("email"), "", adObj.getString("latitude"), adObj.getString("longitude"), adObj.getString("price"), adObj.getString("currency_code"), obj.getString("country_name"), obj.getString("state_name"), obj.getString("city_name"), adImagesTemp, adObj.getString("dis_1"), adObj.getString("dis_2"), adObj.getString("dis_3"), adObj.getString("dis_4"),adObj.getString("reference_id"), ""));
                                        }
                                    }
                                }
                                mAdapter = new ListViewAdapter(FavouriteAddsListAcivity.this, dataModels);
                                mListView.setAdapter(mAdapter);

                                Toast.makeText(mContext, "Ads fetched successfully!", Toast.LENGTH_SHORT).show();

                            } else if (result.get("status").equals("error")) {
                                Toast.makeText(mContext, "Recheck and try again", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception ee) {
                            Toast.makeText(mContext, "error: " + ee.toString(), Toast.LENGTH_SHORT).show();

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

                SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String userID = settings.getString("userID", "defaultValue");


                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userID);
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void initUI() {
        tvCategory = (TextView) findViewById(R.id.tvCategory);
        tvCategory.setText(GlobalClass.selectedSubCategory);
        mListView = (ListView) findViewById(R.id.listview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.bringToFront();
        bAddProperty = (Button) findViewById(R.id.bAddProperty);
        bAddProperty.setOnClickListener(this);
        bHome = (Button) findViewById(R.id.bHome);
        bHome.setOnClickListener(this);
        bSettings = (Button) findViewById(R.id.bSettings);
        bSettings.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_listview) {
//            startActivity(new Intent(this, AdListViewActivity.class));
//            finish();
//            return true;
//        } else if (id == R.id.action_gridview) {
////            startActivity(new Intent(this, GridViewExample.class));
////            finish();
//            return true;
//        } else if (id == R.id.action_recycler) {
////            startActivity(new Intent(this, RecyclerViewExample.class));
////            finish();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bAddProperty:
                Intent addPropertyIntent = new Intent(this, AddItemActivity.class);
                addPropertyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(addPropertyIntent);
                break;
            case R.id.bHome:
                finish();
                break;
            case R.id.bSettings:
                startActivity(new Intent(this, AccountInfoActivity.class));
                break;
        }
    }
}
