package com.codiansoft.oneandonly;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codiansoft.oneandonly.adapter.ListViewAdapter;
import com.codiansoft.oneandonly.dialog.DeletePeriodDialog;
import com.codiansoft.oneandonly.model.CategoriesModel;
import com.codiansoft.oneandonly.model.PropertyListItemDataModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.codiansoft.oneandonly.GlobalClass.alreadyLoggedIn;
import static com.codiansoft.oneandonly.GlobalClass.selectedItemDataModel;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    private static final int PERMISSIONS_CODE = 11;
    private static final int REQUEST_CODE_FILTER_ACTIVITY = 22;
    Button bProperty, bVehicle, bElectronics, bTravel, bLeisure, bBusiness, bAdults, bFood, bCareer, bLifestyle, bJobsAndTraining, bHomeAndGarden;
    public static ArrayList<CategoriesModel> categories;
    CategoriesModel categoriesModel;
    ProgressDialog progressDialog;

    //for search list
    RecyclerView rvSearchedAds;
    public static ProgressBar progressBar;
    public static PropertyListItemDataModel searchedAdsModel;
    private ListViewAdapter mAdapter;
    ArrayList<PropertyListItemDataModel> dataModels = new ArrayList<PropertyListItemDataModel>();
    private ListView mListView;
    private Button bFilter;

    ConstraintLayout clMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make it fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("1 AND ONLY");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_change_password).setVisible(false);

        initUI();
        requestPermissions();
        fetchCategories();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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

                Intent i = new Intent(MainActivity.this, AdDetailsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String[] permissions = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET
            };
            ActivityCompat.requestPermissions(MainActivity.this, permissions, PERMISSIONS_CODE);

/*            if ( (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    | (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    | (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    | (checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)
                    | (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    | (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    | (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    | (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
                    ) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
*/


        }
    }

    private void fetchCategories() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgressNumberFormat(null);
        progressDialog.setProgressPercentFormat(null);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.CATEGORIES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.e("fetch_categories",response);
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                JSONArray Data = Jobject.getJSONArray("data");
                                categories = new ArrayList<CategoriesModel>();

                                for (int i = 0; i < Data.length(); i++) {
                                    JSONObject categoryObj = Data.getJSONObject(i);
                                    JSONArray subCatArr = categoryObj.getJSONArray("sub_category");

                                    ArrayList<String> sub_category_IDs = new ArrayList<String>();
                                    ArrayList<String> sub_category_names = new ArrayList<String>();
                                    ArrayList<String> sub_category_last_update_time = new ArrayList<String>();
                                    ArrayList<String> sub_category_ads_quantity = new ArrayList<String>();
                                    ArrayList<ArrayList<String>> descriptionHeadings = new ArrayList<ArrayList<String>>();

                                    for (int j = 0; j < subCatArr.length(); j++) {
                                        String id = subCatArr.getJSONObject(j).getString("id");
                                        sub_category_IDs.add(id);

                                        String name = subCatArr.getJSONObject(j).getString("sub_cat_name");
                                        sub_category_names.add(name);

                                        String lastUpdateTime = subCatArr.getJSONObject(j).getString("last_updated");
                                        sub_category_last_update_time.add(lastUpdateTime);

                                        String adsQuantity = subCatArr.getJSONObject(j).getString("subCat_adsNum");
                                        sub_category_ads_quantity.add(adsQuantity);

                                        ArrayList<String> subCatHeadings = new ArrayList<String>();
                                        subCatHeadings.clear();
                                        for (int k = 1; k <= 4; k++) {
                                            subCatHeadings.add(subCatArr.getJSONObject(j).getString("dis_" + k));
                                        }

                                        descriptionHeadings.add(subCatHeadings);

                                        /*ArrayList<String> desHeading = new ArrayList<String>();
                                        for (int k = 1; k <= 4; k++) {
                                            desHeading.add(subCatArr.getJSONObject(j).getString("des" + k));
                                        }
                                        descriptionHeadings.add(desHeading);*/

//                                    categoriesDataModel.add(new CategoriesModel(categoryObj.getString("category_Id"), categoryObj.getString("name"), categoryObj.getString("description"), sub_categories_names));
                                    }
                                    categoriesModel = new CategoriesModel(categoryObj.getString("category_Id"), categoryObj.getString("name"), categoryObj.getString("last_updated"), sub_category_IDs, sub_category_names, sub_category_last_update_time, sub_category_ads_quantity, categoryObj.getString("status"), categoryObj.getString("ads_nums"), descriptionHeadings);
                                    categories.add(categoriesModel);
                                }

                                setAdsQtyInCategories();

                                progressDialog.hide();
                            }

                        } catch (Exception ee) {
                            Toast.makeText(MainActivity.this, ee.toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                        }

                        if (categories == null) {
                            Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                MainActivity.this.finishAffinity();
                            } else finish();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 409:
                                    Toast.makeText(MainActivity.this, "Error " + 409, Toast.LENGTH_SHORT).show();
//                                    utilities.dialog("Already Exist", act);
                                    break;
                                case 400:
//                                    utilities.dialog("Connection Problem", act);
                                    Toast.makeText(MainActivity.this, "Error " + 400, Toast.LENGTH_SHORT).show();
                                    break;
                                default:
//                                    utilities.dialog("Connection Problem", act);
                                    Toast.makeText(MainActivity.this, "Unexpected Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Unexpected Connection Error", Toast.LENGTH_SHORT).show();
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
                Log.e("api_secret",apiSecretKey);
                return params;
            }
        };
        queue.add(postRequest);

    }

    private void setAdsQtyInCategories() {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getName().equals("Property")) {
                bProperty.setText(categories.get(i).getQuantity());
                continue;
            }
            if (categories.get(i).getName().equals("Travel")) {
                bTravel.setText(categories.get(i).getQuantity());
                continue;
            }
            if (categories.get(i).getName().equals("Electrical")) {
                bElectronics.setText(categories.get(i).getQuantity());
                continue;
            }
            if (categories.get(i).getName().equals("Motoring")) {
                bVehicle.setText(categories.get(i).getQuantity());
                continue;
            }

            if (categories.get(i).getName().equals("Leisure")) {
                bLeisure.setText(categories.get(i).getQuantity());
                continue;
            }
            if (categories.get(i).getName().equals("Business")) {
                bBusiness.setText(categories.get(i).getQuantity());
                continue;
            }
            if (categories.get(i).getName().equals("Adults")) {
                bAdults.setText(categories.get(i).getQuantity());
                continue;
            }
            if (categories.get(i).getName().equals("What's On")) {
                bFood.setText(categories.get(i).getQuantity());
                continue;
            }
            if (categories.get(i).getName().equals("Career Center")) {
                bCareer.setText(categories.get(i).getQuantity());
                continue;
            }
            if (categories.get(i).getName().equals("Lifestyle")) {
                bLifestyle.setText(categories.get(i).getQuantity());
                continue;
            }
            if (categories.get(i).getName().equals("Jobs And Training")) {
                bJobsAndTraining.setText(categories.get(i).getQuantity());
                continue;
            }
            if (categories.get(i).getName().equals("Home And Garden")) {
                bHomeAndGarden.setText(categories.get(i).getQuantity());
                continue;
            }
        }
    }

    private void initUI() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.bringToFront();

        bProperty = (Button) findViewById(R.id.bProperty);
        bProperty.setOnClickListener(this);
        bVehicle = (Button) findViewById(R.id.bVehicle);
        bVehicle.setOnClickListener(this);
        bElectronics = (Button) findViewById(R.id.bElectronics);
        bElectronics.setOnClickListener(this);
        bTravel = (Button) findViewById(R.id.bTravel);
        bTravel.setOnClickListener(this);
        bLeisure = (Button) findViewById(R.id.bLeisure);
        bLeisure.setOnClickListener(this);
        bBusiness = (Button) findViewById(R.id.bBusiness);
        bBusiness.setOnClickListener(this);
        bAdults = (Button) findViewById(R.id.bAdults);
        bAdults.setOnClickListener(this);
        bFood = (Button) findViewById(R.id.bFood);
        bFood.setOnClickListener(this);
        bCareer = (Button) findViewById(R.id.bCareer);
        bCareer.setOnClickListener(this);
        bLifestyle = (Button) findViewById(R.id.bLifestyle);
        bLifestyle.setOnClickListener(this);
        bJobsAndTraining = (Button) findViewById(R.id.bJobsAndTraining);
        bJobsAndTraining.setOnClickListener(this);
        bHomeAndGarden = (Button) findViewById(R.id.bHomeAndGarden);
        bHomeAndGarden.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.lvSearchedAds);
        bFilter = (Button) findViewById(R.id.bFilter);
        bFilter.setOnClickListener(this);
        clMain = (ConstraintLayout) findViewById(R.id.clMain);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (clMain.getVisibility() == View.GONE) {
                clMain.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
                bFilter.setVisibility(View.GONE);
            } else super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                clMain.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                bFilter.setVisibility(View.VISIBLE);
                dataModels.clear();
                progressDialog.dismiss();
                fetchSearchedAds(s);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return true;
            }
        });

        return true;
    }

    private void fetchSearchedAds(final String s) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Searching...");
        progressDialog.setProgressNumberFormat(null);
        progressDialog.setProgressPercentFormat(null);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_ALL_ADDS_URL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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

                                    if (adObj.getString("title").toLowerCase().contains(s.toLowerCase())
                                            || adObj.getString("city").toLowerCase().contains(s.toLowerCase())
                                            || adObj.getString("description").toLowerCase().contains(s.toLowerCase())
                                            || adObj.getString("country_name").toLowerCase().contains(s.toLowerCase())
                                            || adObj.getString("state_name").toLowerCase().contains(s.toLowerCase())
                                            || adObj.getString("city_name").toLowerCase().contains(s.toLowerCase())
                                            || adObj.getString("dis_1").toLowerCase().contains(s.toLowerCase())
                                            || adObj.getString("dis_1").toLowerCase().contains(s.toLowerCase())
                                            || adObj.getString("dis_1").toLowerCase().contains(s.toLowerCase())
                                            || adObj.getString("dis_1").toLowerCase().contains(s.toLowerCase())) {

                                        ArrayList<String> adImages = new ArrayList<String>();
                                        JSONArray adImagesArr = adObj.getJSONArray("images");

                                        for (int j = 0; j < adImagesArr.length(); j++) {
                                            adImages.add(adImagesArr.getJSONObject(j).getString("path"));
                                        }
                                        ArrayList<String> adImagesTemp = adImages;
                                        dataModels.add(new PropertyListItemDataModel(adObj.getString("title"), "For Rent", adObj.getString("city"), adObj.getString("last_updated"), adObj.getString("addv_id"), adObj.getString("description"), adObj.getString("mobile_number"), adObj.getString("phone_number"), adObj.getString("email"), "https://cdn.houseplans.com/product/o2d2ui14afb1sov3cnslpummre/w560x373.jpg?v=15", adObj.getString("latitude"), adObj.getString("longitude"), adObj.getString("price"), adObj.getString("currency_code"), adObj.getString("country_name"), adObj.getString("state_name"), adObj.getString("city_name"), adImagesTemp, adObj.getString("dis_1"), adObj.getString("dis_2"), adObj.getString("dis_3"), adObj.getString("dis_4"),"",""));
                                    }
                                    else
                                    {
                                        Toast.makeText(MainActivity.this, "No Records Found.",Toast.LENGTH_LONG).show();

                                    }
                                }
                                progressDialog.dismiss();

                                if (dataModels.size() < 1) {
                                    mListView.setBackground(getResources().getDrawable(R.drawable.ic_not_found));
                                }

                                mAdapter = new ListViewAdapter(MainActivity.this, dataModels);
                                mListView.setAdapter(mAdapter);

//                                Toast.makeText(MainActivity.this, "Ads fetched successfully!", Toast.LENGTH_SHORT).show();

                            } else if (result.get("status").equals("error")) {
                                Toast.makeText(MainActivity.this, "Recheck and try again", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        } catch (Exception ee) {
                            Toast.makeText(MainActivity.this, "error: " + ee.toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }

                        progressDialog.dismiss();

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        progressDialog.dismiss();
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 409:
//                                    utilities.dialog("Already Exist", act);
                                    break;
                                case 400:
                                    Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_SHORT).show();
//                                    utilities.dialog("Connection Problem", act);
                                    break;
                                default:
//                                    utilities.dialog("Connection Problem", act);
                                    break;
                            }
                        }
                    }
                }
        )
        {
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

                Log.e("params" , params.toString());

                return params;
            }
        };

        queue.add(postRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
///*//            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
//            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 32.173942, -110.820111);
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//            startActivity(intent);*/
//
//            return true;
//        }
//
//        if (id == R.id.action_set_my_location) {
///*//            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
//            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 32.173942, -110.820111);
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//            startActivity(intent);*/
//
//
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account_info_or_register) {
            /*RegisterDialog registerDialog = new RegisterDialog(this);
            registerDialog.show();*/
            Intent infoIntent = new Intent(MainActivity.this, AccountInfoActivity.class);
            startActivity(infoIntent);
        } else if (id == R.id.nav_dashboard) {
            Intent level1Intent = new Intent(MainActivity.this, Level2Activity.class);
            startActivity(level1Intent);
        } else if (id == R.id.nav_my_ads) {
            Intent myAdsIntent = new Intent(MainActivity.this, MyAdsActivity.class);
            startActivity(myAdsIntent);

        } else if (id == R.id.nav_change_password) {

        } /*else if (id == R.id.nav_delete_period) {

            DeletePeriodDialog deletePeriodDialog = new DeletePeriodDialog(this);
            deletePeriodDialog.show();

        }*/
        else if (id == R.id.nav_share_app) {
            onClickWhatsApp();
        }
        else if (id == R.id.nav_logout) {

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Are you sure to log out?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alreadyLoggedIn = false;
                            SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = settings.edit();

                            editor.putString("apiSecretKey", "");
                            editor.putString("userID", "");
                            editor.putString("email", "");
                            editor.putString("contactNum1", "");
                            editor.putString("contactNum2", "");

                            editor.commit();

                            Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
                            ActivityCompat.finishAffinity(MainActivity.this);
                            logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(logoutIntent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Whatever...
                        }
                    }).show();


        } else if (id == R.id.nav_sell_something)
        {
            Intent addItemIntent = new Intent(MainActivity.this, AddItemActivity.class);
            startActivity(addItemIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public ArrayList<String> getWhatsappContacts()
    {
        ContentResolver cr = getContentResolver();

//RowContacts for filter Account Types
        Cursor contactCursor = cr.query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[]{ContactsContract.RawContacts._ID,
                        ContactsContract.RawContacts.CONTACT_ID},
                ContactsContract.RawContacts.ACCOUNT_TYPE + "= ?",
                new String[]{"com.whatsapp"},
                null);

//ArrayList for Store Whatsapp Contact
        ArrayList<String> myWhatsappContacts = new ArrayList<>();

        if (contactCursor != null) {
            if (contactCursor.getCount() > 0) {
                if (contactCursor.moveToFirst()) {
                    do {
                        //whatsappContactId for get Number,Name,Id ect... from  ContactsContract.CommonDataKinds.Phone
                        String whatsappContactId = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));

                        if (whatsappContactId != null) {
                            //Get Data from ContactsContract.CommonDataKinds.Phone of Specific CONTACT_ID
                            Cursor whatsAppContactCursor = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{whatsappContactId}, null);

                            if (whatsAppContactCursor != null) {
                                whatsAppContactCursor.moveToFirst();
                                String id = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                                String name = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                String number = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                whatsAppContactCursor.close();

                                //Add Number to ArrayList
                                myWhatsappContacts.add(number);

                             //   showLogI(TAG, " WhatsApp contact id  :  " + id);
                              //  showLogI(TAG, " WhatsApp contact name :  " + name);
                                //showLogI(TAG, " WhatsApp contact number :  " + number);
                                Log.e("name" , name);
                                Log.e("number" ,number);
                            }
                        }
                    } while (contactCursor.moveToNext());
                    contactCursor.close();
                }
            }
        }

       // showLogI(TAG, " WhatsApp contact size :  " + myWhatsappContacts.size());

        return myWhatsappContacts;
    }
    public void onClickWhatsApp() {
//        getWhatsappContacts();

        PackageManager pm=getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = "hey , download this app from playstore .";

            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }

    }
    @Override
    public void onClick(View view) {

        if (categories == null) {
            Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show();
        } else {

            switch (view.getId()) {
                case R.id.bProperty:
                    for (int i = 0; i < categories.size(); i++) {
                        if (categories.get(i).getName().equals("Property")) {
                            GlobalClass.selectedCategory = categories.get(i).getName();
                            GlobalClass.selectedCategoryID = categories.get(i).getCategory_Id();
                            if (categories.get(i).getCategorySatus().equals("0")) {
                                Toast.makeText(this, "Enable it from dashboard", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent electronicsIntent = new Intent(MainActivity.this, ChooseSubCategoryActivity.class);
                                startActivity(electronicsIntent);
                            }
                            break;
                        }
                    }
                    break;
                case R.id.bTravel:
                    /*Intent travelIntent = new Intent(MainActivity.this, AdListViewActivity.class);
                    GlobalClass.selectedCategory = "Travel";
                    GlobalClass.selectedCategoryID = "2";
                    startActivity(travelIntent);*/

                    for (int i = 0; i < categories.size(); i++) {
                        if (categories.get(i).getName().equals("Travel")) {
                            GlobalClass.selectedCategory = categories.get(i).getName();
                            GlobalClass.selectedCategoryID = categories.get(i).getCategory_Id();
                            if (categories.get(i).getCategorySatus().equals("0")) {
                                Toast.makeText(this, "Enable it from dashboard", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent electronicsIntent = new Intent(MainActivity.this, ChooseSubCategoryActivity.class);
                                startActivity(electronicsIntent);
                            }
                            break;
                        }
                    }
                    break;
                case R.id.bElectronics:

                    for (int i = 0; i < categories.size(); i++) {
                        if (categories.get(i).getName().equals("Electrical")) {
                            GlobalClass.selectedCategory = categories.get(i).getName();
                            GlobalClass.selectedCategoryID = categories.get(i).getCategory_Id();
                            if (categories.get(i).getCategorySatus().equals("0")) {
                                Toast.makeText(this, "Enable it from dashboard", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent electronicsIntent = new Intent(MainActivity.this, ChooseSubCategoryActivity.class);
                                startActivity(electronicsIntent);
                            }
                            break;
                        }
                    }

                    /*SubCategoriesDialog subCategoriesDialog = new SubCategoriesDialog(this);
                    subCategoriesDialog.show();*/

                /*
                Intent electronicsIntent = new Intent(MainActivity.this, AdListViewActivity.class);
                GlobalClass.selectedCategory = "Electronic";
                startActivity(electronicsIntent);*/
                    break;
                case R.id.bVehicle:
                    for (int i = 0; i < categories.size(); i++) {
                        if (categories.get(i).getName().equals("Motoring")) {
                            GlobalClass.selectedCategory = categories.get(i).getName();
                            GlobalClass.selectedCategoryID = categories.get(i).getCategory_Id();
                            if (categories.get(i).getCategorySatus().equals("0")) {
                                Toast.makeText(this, "Enable it from dashboard", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent electronicsIntent = new Intent(MainActivity.this, ChooseSubCategoryActivity.class);
                                startActivity(electronicsIntent);
                            }
                            break;
                        }
                    }
                    break;
                case R.id.bLeisure:
                    /*Intent leisureIntent = new Intent(MainActivity.this, AdListViewActivity.class);
                    GlobalClass.selectedCategory = "Leisure";
                    GlobalClass.selectedCategoryID = "4";
                    startActivity(leisureIntent);*/

                    for (int i = 0; i < categories.size(); i++) {
                        if (categories.get(i).getName().equals("Leisure")) {
                            GlobalClass.selectedCategory = categories.get(i).getName();
                            GlobalClass.selectedCategoryID = categories.get(i).getCategory_Id();
                            if (categories.get(i).getCategorySatus().equals("0")) {
                                Toast.makeText(this, "Enable it from dashboard", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent electronicsIntent = new Intent(MainActivity.this, ChooseSubCategoryActivity.class);
                                startActivity(electronicsIntent);
                            }
                            break;
                        }
                    }
                    break;
                case R.id.bBusiness:
                    for (int i = 0; i < categories.size(); i++) {
                        if (categories.get(i).getName().equals("Business")) {
                            GlobalClass.selectedCategory = categories.get(i).getName();
                            GlobalClass.selectedCategoryID = categories.get(i).getCategory_Id();
                            if (categories.get(i).getCategorySatus().equals("0")) {
                                Toast.makeText(this, "Enable it from dashboard", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent electronicsIntent = new Intent(MainActivity.this, ChooseSubCategoryActivity.class);
                                startActivity(electronicsIntent);
                            }
                            break;
                        }
                    }
                    break;
                case R.id.bAdults:
                    //block adults category for saudi arabia

                    new MaterialDialog.Builder(this)
                            .title("warning")
                            .content("Disclaimer\n" +
                                    "This section may contain adult material, sexual content, including pictorial nudity and adult language. 1andOnly is not responsible for any material posted on this site; it is the responsibility of the user to ensure no unauthorized material is submitted. The section of 1and only site is to be accessed only by persons who are 18 years of age or older (and is not considered to be a minor in his/her state of residence) and who live in a community or local jurisdiction where nude pictures and explicit adult materials are prohibited by law. By accessing this website, you are representing to us that you meet the above qualifications. A false representation may be a criminal offense.\n" +
                                    "I confirm and represent that I am 18 years of age or older (and am not considered to be a minor in my state of residence) and that I am not located in a community or local jurisdiction where nude pictures or explicit adult materials are prohibited by any law. I agree to report any illegal services or activities which violate the Terms of Use. I also agree to report suspected exploitation of minors and/or human trafficking to the appropriate authorities.\n" +
                                    "I have read and agree to this disclaimer as well as the Terms of Use.\n")
                            .positiveText("Agree")
                            .negativeText("don't agree")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        if (GlobalClass.userCountryID.equals("191")) {
                        Toast.makeText(MainActivity.this, "Not Allowed", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < categories.size(); i++) {
                            if (categories.get(i).getName().equals("Adults")) {

                                GlobalClass.selectedCategory = categories.get(i).getName();
                                GlobalClass.selectedCategoryID = categories.get(i).getCategory_Id();
                                if (categories.get(i).getCategorySatus().equals("0")) {
                                    Toast.makeText(MainActivity.this, "Enable it from dashboard", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent electronicsIntent = new Intent(MainActivity.this, ChooseSubCategoryActivity.class);
                                    startActivity(electronicsIntent);
                                }
                                break;
                            }
                        }
                    }
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    // do nothing , dont open adults page
                                }
                            })
                            .show();
                break;
                case R.id.bFood:
                    for (int i = 0; i < categories.size(); i++) {
                        if (categories.get(i).getName().equals("What's On")) {
                            GlobalClass.selectedCategory = categories.get(i).getName();
                            GlobalClass.selectedCategoryID = categories.get(i).getCategory_Id();
                            if (categories.get(i).getCategorySatus().equals("0")) {
                                Toast.makeText(this, "Enable it from dashboard", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent electronicsIntent = new Intent(MainActivity.this, ChooseSubCategoryActivity.class);
                                startActivity(electronicsIntent);
                            }
                            break;
                        }
                    }
                    break;
                case R.id.bCareer:
                    for (int i = 0; i < categories.size(); i++) {
                        if (categories.get(i).getName().equals("Career Center")) {
                            GlobalClass.selectedCategory = categories.get(i).getName();
                            GlobalClass.selectedCategoryID = categories.get(i).getCategory_Id();
                            if (categories.get(i).getCategorySatus().equals("0")) {
                                Toast.makeText(this, "Enable it from dashboard", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent electronicsIntent = new Intent(MainActivity.this, ChooseSubCategoryActivity.class);
                                startActivity(electronicsIntent);
                            }
                            break;
                        }
                    }
                    break;
                case R.id.bLifestyle:
                    for (int i = 0; i < categories.size(); i++) {
                        if (categories.get(i).getName().equals("Lifestyle")) {
                            GlobalClass.selectedCategory = categories.get(i).getName();
                            GlobalClass.selectedCategoryID = categories.get(i).getCategory_Id();
                            if (categories.get(i).getCategorySatus().equals("0")) {
                                Toast.makeText(this, "Enable it from dashboard", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent electronicsIntent = new Intent(MainActivity.this, ChooseSubCategoryActivity.class);
                                startActivity(electronicsIntent);
                            }
                            break;
                        }
                    }
                    break;
                case R.id.bJobsAndTraining:
                    for (int i = 0; i < categories.size(); i++) {
                        if (categories.get(i).getName().equals("Jobs And Training")) {
                            GlobalClass.selectedCategory = categories.get(i).getName();
                            GlobalClass.selectedCategoryID = categories.get(i).getCategory_Id();
                            if (categories.get(i).getCategorySatus().equals("0")) {
                                Toast.makeText(this, "Enable it from dashboard", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent electronicsIntent = new Intent(MainActivity.this, ChooseSubCategoryActivity.class);
                                startActivity(electronicsIntent);
                            }
                            break;
                        }
                    }
                    break;
                case R.id.bHomeAndGarden:
                    for (int i = 0; i < categories.size(); i++) {
                        if (categories.get(i).getName().equals("Home And Garden")) {
                            GlobalClass.selectedCategory = categories.get(i).getName();
                            GlobalClass.selectedCategoryID = categories.get(i).getCategory_Id();
                            if (categories.get(i).getCategorySatus().equals("0")) {
                                Toast.makeText(this, "Enable it from dashboard", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent electronicsIntent = new Intent(MainActivity.this, ChooseSubCategoryActivity.class);
                                startActivity(electronicsIntent);
                            }
                            break;
                        }
                    }
                    break;
                case R.id.bFilter:
                    Intent intent = new Intent(this, SearchFiltersActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_FILTER_ACTIVITY);
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.SEND_SMS)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_CODE);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_FILTER_ACTIVITY) {

            if (resultCode == Activity.RESULT_OK) {
                String searchName = data.getStringExtra("searchName");
                String searchCountry = data.getStringExtra("searchCountry");
                String searchState = data.getStringExtra("searchState");
                String searchCity = data.getStringExtra("searchCity");
                // do something with the result

                fetchSearchedAds(searchName, searchCountry, searchState, searchCity);

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // some stuff that will happen if there's no result
            }
        }
    }

    private void fetchSearchedAds(final String searchName, final String searchCountry, final String searchState, final String searchCity) {
        dataModels.clear();
        mAdapter.notifyDataSetChanged();


        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Searching...");
        progressDialog.setProgressNumberFormat(null);
        progressDialog.setProgressPercentFormat(null);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_ALL_ADDS_URL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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

                                    if (
                                            (adObj.getString("title").toLowerCase().contains(searchName.toLowerCase())
                                                    | adObj.getString("dis_1").toLowerCase().contains(searchName.toLowerCase())
                                                    | adObj.getString("dis_1").toLowerCase().contains(searchName.toLowerCase())
                                                    | adObj.getString("dis_1").toLowerCase().contains(searchName.toLowerCase())
                                                    | adObj.getString("dis_1").toLowerCase().contains(searchName.toLowerCase())
                                                    | adObj.getString("description").toLowerCase().contains(searchName.toLowerCase())
                                            )
                                                    & (adObj.getString("country_name").toLowerCase().contains(searchCountry.toLowerCase())
                                                    & adObj.getString("state_name").toLowerCase().contains(searchState.toLowerCase())
                                                    & adObj.getString("city_name").toLowerCase().contains(searchCity.toLowerCase())
                                            )
                                            ) {

                                        ArrayList<String> adImages = new ArrayList<String>();
                                        JSONArray adImagesArr = adObj.getJSONArray("images");

                                        for (int j = 0; j < adImagesArr.length(); j++) {
                                            adImages.add(adImagesArr.getJSONObject(j).getString("path"));
                                        }
                                        ArrayList<String> adImagesTemp = adImages;
                                        dataModels.add(new PropertyListItemDataModel(adObj.getString("title"), "For Rent", adObj.getString("city"), adObj.getString("last_updated"), adObj.getString("addv_id"), adObj.getString("description"), adObj.getString("mobile_number"), adObj.getString("phone_number"), adObj.getString("email"), "https://cdn.houseplans.com/product/o2d2ui14afb1sov3cnslpummre/w560x373.jpg?v=15", adObj.getString("latitude"), adObj.getString("longitude"), adObj.getString("price"), adObj.getString("currency_code"), adObj.getString("country_name"), adObj.getString("state_name"), adObj.getString("city_name"), adImagesTemp, adObj.getString("dis_1"), adObj.getString("dis_2"), adObj.getString("dis_3"), adObj.getString("dis_4"),"",""));
                                    }
                                }
                                progressDialog.dismiss();

                                if (dataModels.size() < 1) {
                                    mListView.setBackground(getResources().getDrawable(R.drawable.ic_not_found));
                                }

                                mAdapter = new ListViewAdapter(MainActivity.this, dataModels);
                                mListView.setAdapter(mAdapter);

//                                Toast.makeText(MainActivity.this, "Ads fetched successfully!", Toast.LENGTH_SHORT).show();

                            } else if (result.get("status").equals("error")) {
                                Toast.makeText(MainActivity.this, "Recheck and try again", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        } catch (Exception ee) {
                            Toast.makeText(MainActivity.this, "error: " + ee.toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }

                        progressDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        progressDialog.dismiss();
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 409:
//                                    utilities.dialog("Already Exist", act);
                                    break;
                                case 400:
                                    Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_SHORT).show();
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
}
