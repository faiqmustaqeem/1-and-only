package com.codiansoft.oneandonly;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import static com.codiansoft.oneandonly.AdDetailsActivity.isMyAd;
import static com.codiansoft.oneandonly.GlobalClass.selectedItemDataModel;

public class AdListViewActivity extends AppCompatActivity implements View.OnClickListener {

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

        initUI();

//        dataModels = new ArrayList<>();

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

                isMyAd = false;

                Intent i = new Intent(AdListViewActivity.this, AdDetailsActivity.class);
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

/*        if (GlobalClass.selectedCategory.equals("Property")) {
            categoriesRegisterDataModel.add(new PropertyListItemDataModel("Villa with 3 bedrooms", "For Rent", "Karachi", "2:00", "1", "Its at 2nd floor . Only three rooms one kitchen n one washroom with open roof. U can use it as washing area . Separate electric n gas meters. Rent is negotiable.", "03001234567", "03112345678", "xyz@abc.com", "https://cdn.houseplans.com/product/o2d2ui14afb1sov3cnslpummre/w560x373.jpg?v=15", "24.917785", "67.097034"));
            categoriesRegisterDataModel.add(new PropertyListItemDataModel("House 200 sq. yards", "For Rent", "Lahore", "6:45", "1", "E 11/3 one kanal 3 bed room with bath dd Living servant room store Meter separate Gate Separate V.Good Location ", "03001234567", "03112345678", "xyz@abc.com", "https://photos-a.propertyimages.ie/media/6/0/9/3778906/5a21c496-24d6-4c9c-b26b-0d2c44ac11f3_l.jpg", "31.586486", "74.311647"));
            categoriesRegisterDataModel.add(new PropertyListItemDataModel("Beautiful House for sale", "For Sale", "Islamabad", "12:55", "1", "", "03001234567", "03112345678", "xyz@abc.com", "", "33.726910", "73.039066"));
            categoriesRegisterDataModel.add(new PropertyListItemDataModel(" 1 Kanal Bungalow", "For Sale", "Faisalabad", "23:00", "1", "", "03001234567", "03112345678", "xyz@abc.com", "", "31.407442", "73.112550"));
            categoriesRegisterDataModel.add(new PropertyListItemDataModel("Ground portion", "For Rent", "Rawalpindi", "21:54", "1", "", "03001234567", "03112345678", "xyz@abc.com", "", "32.173942", "-110.820111"));
            categoriesRegisterDataModel.add(new PropertyListItemDataModel(" New house at prime location", "For Sale", "Hyderabad", "5:23", "1", "", "03001234567", "03112345678", "xyz@abc.com", "", "32.173942", "-110.820111"));
            categoriesRegisterDataModel.add(new PropertyListItemDataModel("Bungalow ideal for big family", "For Rent", "Peshawar", "6:54", "1", "", "03001234567", "03112345678", "xyz@abc.com", "", "32.173942", "-110.820111"));
            categoriesRegisterDataModel.add(new PropertyListItemDataModel("Golf Villa", "For Sale", "Quetta", "3:56", "1", "", "03001234567", "03112345678", "xyz@abc.com", "", "32.173942", "-110.820111"));
            categoriesRegisterDataModel.add(new PropertyListItemDataModel("Basement For Rent", "For Rent", "Sargodha", "5:56", "1", "", "03001234567", "03112345678", "xyz@abc.com", "", "32.173942", "-110.820111"));
            categoriesRegisterDataModel.add(new PropertyListItemDataModel("Property #10", "For Sale", "Sialkot", "5:34", "1", "", "03001234567", "03112345678", "xyz@abc.com", "", "32.173942", "-110.820111"));
            categoriesRegisterDataModel.add(new PropertyListItemDataModel("Property #11", "For Sale", "Karachi", "21:21", "1", "", "03001234567", "03112345678", "xyz@abc.com", "", "32.173942", "-110.820111"));
            categoriesRegisterDataModel.add(new PropertyListItemDataModel("Property #12", "For Sale", "Lahore", "12:01", "1", "", "03001234567", "03112345678", "xyz@abc.com", "", "32.173942", "-110.820111"));
            categoriesRegisterDataModel.add(new PropertyListItemDataModel("Property #13", "For Sale", "Lahore", "23:07", "1", "", "03001234567", "03112345678", "xyz@abc.com", "", "32.173942", "-110.820111"));
        } else if (GlobalClass.selectedCategory.equals("Vehicle")) {
            categoriesRegisterDataModel.add(new PropertyListItemDataModel("Beautiful Daihatsu Car", "For Rent", "Karachi", "2:00", "1", "Mira Esi L Smart Edition", "03009999999", "03118887777", "test@test.com", "https://cdn.houseplans.com/product/o2d2ui14afb1sov3cnslpummre/w560x373.jpg?v=15", "24.917785", "67.097034"));
        } else if (GlobalClass.selectedCategory.equals("Electronics")) {
            categoriesRegisterDataModel.add(new PropertyListItemDataModel("Mini Camera", "For Sale", "Karachi", "2:00", "1", "Hidden Mini Camera SQ8 with Night Vision ", "03001234567", "03112345678", "xyz@abc.com", "https://cdn.houseplans.com/product/o2d2ui14afb1sov3cnslpummre/w560x373.jpg?v=15", "24.917785", "67.097034"));
            categoriesRegisterDataModel.add(new PropertyListItemDataModel("Dell Laptop", "For Sale", "Islamabad", "12:55", "1", "DELL Latitude D630 ,,Core2 duo,, 120GB Hard disk,, 2 gb RAM", "03001234567", "03112345678", "xyz@abc.com", "https://i.ytimg.com/vi/NSF7Y-05q0E/hqdefault.jpg", "33.726910", "73.039066"));
            categoriesRegisterDataModel.add(new PropertyListItemDataModel("Electricity Generator", "For Rent", "Rawalpindi", "21:54", "1", "Box pack 3.0 kva & All company's generator available", "03001234567", "03112345678", "xyz@abc.com", "https://sc02.alicdn.com/kf/HTB1cLQcLFXXXXcSXpXXq6xXFXXXv/BISON-CHINA-Economical-Fuel-Use-1-5kw.jpg", "32.173942", "-110.820111"));
            categoriesRegisterDataModel.add(new PropertyListItemDataModel("DV Camera", "For Rent", "Lahore", "6:45", "1", "8x Lens motion action at 1080 which is at the highest real level until ", "03001234567", "03112345678", "xyz@abc.com", "https://pisces.bbystatic.com/image2/BestBuy_US/en_US/images/abn/2015/cam/pr/140514-camcorders-cat-conv/140514-learn-3things-camcorders.jpg;canvasHeight=316;canvasWidth=572", "31.586486", "74.311647"));
            categoriesRegisterDataModel.add(new PropertyListItemDataModel("HP Laptop", "For Sale", "Faisalabad", "23:00", "1", "Hp Pro-book Core i5 Laptop Condition Good With WarrantyEID OFFER", "03001234567", "03112345678", "xyz@abc.com", "https://i.ebayimg.com/00/s/NjUwWDU3Ng==/z/-jQAAOSw8w1X5UC3/$_86.JPG", "31.407442", "73.112550"));
        }*/


        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_ALL_ADDS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.e("fetch_all_adds",response);
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                JSONArray data = Jobject.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject adObj = data.getJSONObject(i);
                                    if (adObj.getString("sub_cat_id").equals(GlobalClass.selectedSubCategoryID)) {
                                        ArrayList<String> adImages = new ArrayList<String>();
                                        JSONArray adImagesArr = adObj.getJSONArray("images");

                                        if (adObj.getString("active").equals("a")) {

                                            for (int j = 0; j < adImagesArr.length(); j++) {
                                                adImages.add(adImagesArr.getJSONObject(j).getString("path"));
                                            }
                                            ArrayList<String> adImagesTemp = adImages;
                                            dataModels.add(new PropertyListItemDataModel(adObj.getString("title"), "For Rent", adObj.getString("city"), adObj.getString("last_updated"), adObj.getString("addv_id"), adObj.getString("description"), adObj.getString("mobile_number"), adObj.getString("phone_number"), adObj.getString("email"), "https://cdn.houseplans.com/product/o2d2ui14afb1sov3cnslpummre/w560x373.jpg?v=15", adObj.getString("ad_latitude"), adObj.getString("ad_longitude"), adObj.getString("price"), adObj.getString("currency_code"), adObj.getString("country_name"), adObj.getString("state_name"), adObj.getString("city_name"), adImagesTemp, adObj.getString("dis_1"), adObj.getString("dis_2"), adObj.getString("dis_3"), adObj.getString("dis_4")));
                                        }
                                    }
                                }
                                mAdapter = new ListViewAdapter(AdListViewActivity.this, dataModels);
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