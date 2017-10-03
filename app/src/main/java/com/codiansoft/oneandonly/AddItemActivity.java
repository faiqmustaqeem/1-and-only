package com.codiansoft.oneandonly;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codiansoft.oneandonly.adapter.ImagesAdapter;
import com.codiansoft.oneandonly.adapter.ItemAttributeAdapter;
import com.codiansoft.oneandonly.model.AttributeKeysModel;
import com.codiansoft.oneandonly.model.AttributeValuesModel;
import com.codiansoft.oneandonly.model.CitiesDataModel;
import com.codiansoft.oneandonly.model.CountriesDataModel;
import com.codiansoft.oneandonly.model.ImagesModel;
import com.codiansoft.oneandonly.model.StatesDataModel;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codiansoft.oneandonly.AdCitiesActivity.adCitiesActivity;
import static com.codiansoft.oneandonly.AdCountriesActivity.adCountriesActivity;
import static com.codiansoft.oneandonly.AdStatesActivity.adStatesActivity;
import static com.codiansoft.oneandonly.MainActivity.categories;

public class AddItemActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 11;
//    private static AdapterView spCategory;

    public static Activity addItemAct;

    int backCount;
    int count = 1;
    static Spinner spCountry;
    static Spinner spStates;
    static Spinner spCity;
    static Spinner spCategory;
    static Spinner spSubCategory;
    ImageView ivGallery, ivCamera;
    ImageView ivImage1, ivImage2, ivImage3, ivImage4;
    Bitmap bitmap;
    RecyclerView rvImages;
    static ImagesAdapter imagesAdapter;
    RecyclerView rvItemAttributes;
    private static List<ImagesModel> images = new ArrayList<>();
    private List<AttributeKeysModel> attributeKeys = new ArrayList<>();
    private List<AttributeValuesModel> attributeValues = new ArrayList<>();
    AttributeKeysModel attributeKeysModel;
    AttributeValuesModel attributeValuesModel;

    ImagesModel imagesModel;
    public static ProgressBar progressBar;
    Button bAddAttribute;
    ItemAttributeAdapter itemAttributeAdapter;

    Button bSubmit, bSetLoc;
    static EditText etTitle;
    static EditText etDescription;
    static EditText etPrice;
    static EditText etDes1;
    static EditText etDes2;
    static EditText etDes3;
    static EditText etDes4;
    TextView tvLatLog;
    static TextView tvCurrencyCode;
    TextView tvDes1;
    TextView tvDes2;
    TextView tvDes3;
    TextView tvDes4;

    static String locLat;
    static String locLog;
    static int adImagePosition;

    RadioButton rbDefault, rbAlternate;
    public static TextView tvSaveAdLocations;
    public static boolean savedAdLocations;

    int countriesLoadedCount = 0;
    static ArrayList<CountriesDataModel> countries = new ArrayList<CountriesDataModel>();
    static ArrayList<StatesDataModel> states = new ArrayList<StatesDataModel>();
    ArrayList<String> statesNames = new ArrayList<String>();
    ArrayAdapter statesAdapter;
    int statesLoadedCount = 0;

    static ArrayList<CitiesDataModel> cities = new ArrayList<CitiesDataModel>();
    ArrayList<String> citiesNames = new ArrayList<String>();
    ArrayAdapter adapterCities;

    ArrayList<String> countriesNames = new ArrayList<String>();
    ArrayAdapter countriesAdapter;


    ContentValues values;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);
        addItemAct = this;

        getSupportActionBar().setTitle("Submit An Ad");
        backCount = 0;
        initUI();
        images.clear();
        fetchProfile();

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imagesAdapter.getItemCount() < 10) {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 1);
//                ivGallery.setBackgroundColor(0x00000000);
                } else {
                    Toast.makeText(AddItemActivity.this, "Max 10 images allowed\nRemove an image to add new one", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imagesAdapter.getItemCount() < 10) {
                    /*Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i, 2);*/

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(AddItemActivity.this,new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                        } else {
                            values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "New Picture");
                            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                            imageUri = getContentResolver().insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, 2);
                        }
                    } else {
                        values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "New Picture");
                        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                        imageUri = getContentResolver().insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, 2);
                    }

                } else {
                    Toast.makeText(AddItemActivity.this, "Max 10 images allowed\nRemove an image to add new one", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
//                setStatesInSpinner(countries.get(position).getID(),states.get(position).getID(),"");

                setCurrencyCode(getCountryCode(spCountry.getSelectedItem() + ""));

                countriesLoadedCount++;

                if (countriesLoadedCount > 1) {
//                    Toast.makeText(AccountInfoActivity.this, "on item selected spCountry", Toast.LENGTH_SHORT).show();
                    setStatesInSpinner(countries.get(position).getID(), "", "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spStates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
//                setStatesInSpinner(countries.get(position).getID(),states.get(position).getID(),"");
                statesLoadedCount++;
                if (statesLoadedCount > 1) {
//                    Toast.makeText(AccountInfoActivity.this, "on item selected states", Toast.LENGTH_SHORT).show();
                    setCitiesInSpinner(states.get(position).getID(), "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                GlobalClass.selectedCategoryID = categories.get(position).getCategory_Id();

                setSubCategoriesInSpinner(spCategory.getSelectedItem() + "");

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        spSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                GlobalClass.selectedSubCatDes1Title = categories.get(Integer.parseInt(GlobalClass.selectedCategoryID) - 1).getSubCatDesTitles().get(position).get(0);
                GlobalClass.selectedSubCatDes2Title = categories.get(Integer.parseInt(GlobalClass.selectedCategoryID) - 1).getSubCatDesTitles().get(position).get(1);
                GlobalClass.selectedSubCatDes3Title = categories.get(Integer.parseInt(GlobalClass.selectedCategoryID) - 1).getSubCatDesTitles().get(position).get(2);
                GlobalClass.selectedSubCatDes4Title = categories.get(Integer.parseInt(GlobalClass.selectedCategoryID) - 1).getSubCatDesTitles().get(position).get(3);

                tvDes1.setText(GlobalClass.selectedSubCatDes1Title);
                tvDes2.setText(GlobalClass.selectedSubCatDes2Title);
                tvDes3.setText(GlobalClass.selectedSubCatDes3Title);
                tvDes4.setText(GlobalClass.selectedSubCatDes4Title);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        bAddAttribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*attributeKeysModel = new AttributeKeysModel("");
                attributeValuesModel = new AttributeValuesModel("");
                attributeKeys.add(attributeKeysModel);
                attributeValues.add(attributeValuesModel);
                itemAttributeAdapter.notifyDataSetChanged();*/

//                Toast.makeText(AddItemActivity.this, "Row added", Toast.LENGTH_SHORT).show();

                attributeKeysModel = new AttributeKeysModel("a");
                attributeValuesModel = new AttributeValuesModel("b");
                attributeKeys.add(attributeKeysModel);
                attributeValues.add(attributeValuesModel);
                itemAttributeAdapter.notifyDataSetChanged();
                rvItemAttributes.setItemViewCacheSize(attributeKeys.size());
            }
        });

        bSetLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(AddItemActivity.this), 3);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        tvLatLog.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                new AlertDialog.Builder(AddItemActivity.this)
                        .setTitle("Remove location?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tvLatLog.setVisibility(View.GONE);
                                locLat = "";
                                locLog = "";
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Whatever...
                            }
                        }).show();

                return false;
            }
        });

        rbAlternate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
//                    tvSaveAdLocations.setVisibility(View.VISIBLE);
                } else {
                    tvSaveAdLocations.setVisibility(View.GONE);
                    GlobalClass.selectedAddPostCountry = "";
                    savedAdLocations = false;
                    tvSaveAdLocations.setTextColor(Color.parseColor("#000000"));
                    Drawable img = getResources().getDrawable(R.drawable.ic_not_done);
                    tvSaveAdLocations.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                }
            }
        });
        rbDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) savedAdLocations = false;
            }
        });

        tvSaveAdLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddItemActivity.this, AdCountriesActivity.class);
                startActivity(i);
            }
        });

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbDefault.isChecked()) {
                    GlobalClass.selectedAddPostType = "default";
                } else if (rbAlternate.isChecked()) {
                    GlobalClass.selectedAddPostType = "alternate";
                }

                if (checkFields()) {
                    GlobalClass.postingAd = true;
//                    AddPostTypeDialog registerDialog = new AddPostTypeDialog(AddItemActivity.this);
//                    registerDialog.show();

                    if (GlobalClass.selectedAddPostType.equals("default")) {
                        uploadAd(AddItemActivity.this);
                    } else if (GlobalClass.selectedAddPostType.equals("alternate")) {
                        /*if (savedAdLocations) {
                            uploadAd();
                        } else {
                            Toast.makeText(AddItemActivity.this, "No ad locations saved", Toast.LENGTH_SHORT).show();
                        }*/
                        Intent i = new Intent(AddItemActivity.this, AdCountriesActivity.class);
                        startActivity(i);
                    }
                }
            }
        });
    }


    private void fetchProfile() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

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
                                JSONObject userData = result.getJSONObject("user_data");

                                fetchCountriesInSpinner(userData.getString("country"), userData.getString("state_id"), userData.getString("city"));
                            } else {
                                finish();
                            }

                        } catch (Exception ee) {
                            Toast.makeText(AddItemActivity.this, "error: " + ee.toString(), Toast.LENGTH_SHORT).show();
                            finish();
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
                                    Toast.makeText(AddItemActivity.this, "Try again", Toast.LENGTH_SHORT).show();
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
                        finish();
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

    private void fetchCountriesInSpinner(final String userCountryID, final String userStateID, final String userCityID) {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_COUNTRIES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONArray countriesArr = new JSONArray(response);
                            for (int i = 0; i < countriesArr.length(); i++) {
                                JSONObject countryObj = countriesArr.getJSONObject(i);
                                countries.add(new CountriesDataModel(countryObj.getString("name"), countryObj.getString("sortname").toLowerCase(), "New York", countryObj.getString("last_update"), countryObj.getString("id"), countryObj.getString("ads_num")));
                                countriesNames.add(countryObj.getString("name"));
                            }
                            countriesAdapter = new ArrayAdapter<String>(AddItemActivity.this, android.R.layout.simple_spinner_item, countriesNames);
                            spCountry.setAdapter(countriesAdapter);

                            for (int j = 0; j < countries.size(); j++) {
                                if (countries.get(j).getID().equals(userCountryID)) {
                                    spCountry.setSelection(countriesAdapter.getPosition(countries.get(j).getCountry()));
                                    setStatesInSpinner(countries.get(spCountry.getSelectedItemPosition()).getID(), userStateID, userCityID);
                                    break;
                                }
                            }

                            setCurrencyCode(getCountryCode(spCountry.getSelectedItem() + ""));

                        } catch (JSONException e) {
                            e.printStackTrace();
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
                                    Toast.makeText(AddItemActivity.this, "Try again", Toast.LENGTH_SHORT).show();
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

                return params;
            }
        };
        queue.add(postRequest);

    }

    private void setStatesInSpinner(final String countryID, final String stateID, final String cityID) {

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_STATES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            states.clear();
                            statesNames.clear();
                            JSONArray statesArray = new JSONArray(response);
                            for (int i = 0; i < statesArray.length(); i++) {
                                JSONObject stateObj = statesArray.getJSONObject(i);
                                states.add(new StatesDataModel(stateObj.getString("country_id"), stateObj.getString("name"), "2:00", stateObj.getString("id"), "14"));
                                statesNames.add(stateObj.getString("name"));
                            }

                            statesAdapter = new ArrayAdapter<String>(AddItemActivity.this, android.R.layout.simple_spinner_item, statesNames);
                            spStates.setAdapter(statesAdapter);

                            if (!stateID.equals("")) {
                                for (int j = 0; j < states.size(); j++) {
                                    if (states.get(j).getID().equals(stateID)) {
                                        spStates.setSelection(statesAdapter.getPosition(states.get(j).getState()));
                                        setCitiesInSpinner(states.get(spStates.getSelectedItemPosition()).getID(), cityID);
                                        break;
                                    }
                                }
                            } else {
                                setCitiesInSpinner(states.get(spStates.getSelectedItemPosition()).getID(), "");
                            }

                        } catch (Exception ee) {
                            Toast.makeText(AddItemActivity.this, "error: " + ee.toString(), Toast.LENGTH_SHORT).show();
                            finish();
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
                                    Toast.makeText(AddItemActivity.this, "Try again", Toast.LENGTH_SHORT).show();
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
                        finish();
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
                params.put("country_id", countryID);

                return params;
            }
        };
        queue.add(postRequest);


    }

    private boolean checkFields() {
        if (etTitle.getText().toString().equals("")) {
            etTitle.setError("Title is required");
            Toast.makeText(this, "Check fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etDescription.getText().toString().equals("")) {
            etDescription.setError("Description is required");
            Toast.makeText(this, "Check fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etPrice.getText().toString().equals("") & isNumeric(etPrice.getText().toString())) {
            etPrice.setError("Price is required");
            Toast.makeText(this, "Check fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (rvImages.getChildCount() < 1) {
            Toast.makeText(this, "Add at least one photo", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isNumeric(String s) {
        try {
            double d = Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static void uploadAd(final Activity context) {

        progressBar.setVisibility(View.VISIBLE);
        context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.UPLOAD_AD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
//                                Toast.makeText(AddItemActivity.this, "Add posted successfully!", Toast.LENGTH_SHORT).show();
                                GlobalClass.uploadingAdID = result.get("add_id") + "";
                                if (imagesAdapter.getItemCount() > 0) {
                                    adImagePosition = 0;
                                    uploadAdImages(context);
                                } else {
                                    adCountriesActivity.finish();
                                    adStatesActivity.finish();
                                    adCitiesActivity.finish();
                                    Toast.makeText(context, "Ad posted successfully", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(context, MainActivity.class);
                                    context.startActivity(i);
                                    context.finish();
                                    ActivityCompat.finishAffinity(context);

                                }

                            } else if (result.get("status").equals("error")) {
                                Toast.makeText(context, "Recheck and try again", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception ee) {
                            Toast.makeText(context, "error: " + ee.toString(), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show();
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
                        context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                SharedPreferences settings = context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String userID = settings.getString("userID", "defaultValue");
                String apiSecretKey = settings.getString("apiSecretKey", "");
                String email = settings.getString("email", "");
                String contactNum1 = settings.getString("contactNum1", "");
                String contactNum2 = settings.getString("contactNum2", "");

                Map<String, String> params = new HashMap<String, String>();
                params.put("api_secret", apiSecretKey);
                params.put("title", etTitle.getText().toString());

                String catID = "";
                String SubCatID = "";
                for (int i = 0; i < categories.size(); i++) {
                    if (categories.get(i).getName().equals(spCategory.getSelectedItem().toString())) {
                        catID = categories.get(i).getCategory_Id();
                        for (int j = 0; j < categories.get(i).getSubCategoriesNames().size(); j++) {
                            if (categories.get(i).getSubCategoriesNames().get(j).equals(spSubCategory.getSelectedItem().toString())) {
                                SubCatID = categories.get(i).getSubCategoriesIDs().get(j);
                            }
                        }
                    }
                }

                params.put("category_id", catID);
                params.put("sub_cat_id", SubCatID);
                params.put("description", etDescription.getText().toString());
                params.put("email", email);

                if (locLat == null) params.put("latitude", "");
                else params.put("latitude", locLat);

                if (locLog == null) params.put("longitude", "");
                else params.put("longitude", locLog);

                params.put("mobile_number", contactNum1);
                params.put("phone_number", contactNum2);
                params.put("price", etPrice.getText().toString());

//                String pictures = setPictures();
//                params.put("picture_array", pictures);
//                params.put("customize_column_array", "");
                /*params.put("country", spCountry.getSelectedItem().toString());
                params.put("city", spCity.getSelectedItem().toString());*/

                params.put("dis_1", etDes1.getText().toString());
                params.put("dis_2", etDes2.getText().toString());
                params.put("dis_3", etDes3.getText().toString());
                params.put("dis_4", etDes4.getText().toString());

                params.put("currency_code", tvCurrencyCode.getText().toString());

                if (GlobalClass.selectedAddPostType.equals("default")) {
                    params.put("country_id", settings.getString("country", ""));
                    params.put("state_id", settings.getString("state", ""));
                    params.put("city_id", settings.getString("city", ""));
                    params.put("ad_type", "2");

                } else {
                    GlobalClass.selectedAddPostCityID = GlobalClass.selectedAddPostCityID.substring(0, GlobalClass.selectedAddPostCityID.length() - 1);
                    if (GlobalClass.isSelectAllStates) {
                        GlobalClass.selectedAddPostStateID = GlobalClass.selectedAddPostStateID.substring(0, GlobalClass.selectedAddPostStateID.length() - 1);
                    }

                    params.put("country_id", GlobalClass.selectedAddPostCountryID);
                    params.put("state_id", GlobalClass.selectedAddPostStateID);
                    params.put("city_id", GlobalClass.selectedAddPostCityID);

                    params.put("ad_type", "1");
                }

                String adCountryID = "", adStateID = "", adCityID = "";
                for (int i = 0; i < countries.size(); i++) {
                    if (spCountry.getSelectedItem().toString().equals(countries.get(i).getCountry())) {
                        adCountryID = countries.get(i).getID();
                    }
                }
                for (int i = 0; i < states.size(); i++) {
                    if (spStates.getSelectedItem().toString().equals(states.get(i).getState())) {
                        adStateID = states.get(i).getID();
                    }
                }
                for (int i = 0; i < cities.size(); i++) {
                    if (spCity.getSelectedItem().toString().equals(cities.get(i).getCity())) {
                        adCityID = cities.get(i).getID();
                    }
                }
                params.put("country", adCountryID);
                params.put("state", adStateID);
                params.put("city", adCityID);

                return params;
            }
        };
        queue.add(postRequest);
    }

    private static void uploadAdImages(final Activity context) {

        progressBar.setVisibility(View.VISIBLE);
        context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.UPLOAD_AD_IMAGE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
//                                Toast.makeText(AddItemActivity.this, adImagePosition + "th. image posted successfully!", Toast.LENGTH_SHORT).show();

                                adImagePosition = adImagePosition + 1;
                                if (adImagePosition < imagesAdapter.getItemCount() & imagesAdapter.getItemCount() > 0) {
                                    uploadAdImages(context);
                                } else {
                                    if (progressBar.isShown()) {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    if (GlobalClass.selectedAddPostType.equals("alternate")){
                                        adCountriesActivity.finish();
                                        adStatesActivity.finish();
                                        adCitiesActivity.finish();
                                    }
                                    Intent i = new Intent(context, MainActivity.class);
                                    context.startActivity(i);
                                    context.finish();
                                    context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    ActivityCompat.finishAffinity(context);
                                    Toast.makeText(context, "Ad posted successfully", Toast.LENGTH_SHORT).show();
                                }

                            } else if (result.get("status").equals("error")) {
                                Toast.makeText(context, "Recheck and try again", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception ee) {
                            Toast.makeText(context, "error: " + ee.toString(), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show();
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
                        context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                SharedPreferences settings = context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String userID = settings.getString("userID", "defaultValue");
                String apiSecretKey = settings.getString("apiSecretKey", "");
                String email = settings.getString("email", "");
                String contactNum1 = settings.getString("contactNum1", "");
                String contactNum2 = settings.getString("contactNum2", "");

                Map<String, String> params = new HashMap<String, String>();
                params.put("api_secret", apiSecretKey);
                params.put("ad_id", GlobalClass.uploadingAdID);
                String image = getPicture(adImagePosition);
                params.put("image", image);

                return params;
            }
        };
        queue.add(postRequest);

    }

    private String setPictures() {

        String picturesArray = "[";
        for (int i = 0; i < imagesAdapter.getItemCount(); i++) {
            Bitmap itemImage = images.get(i).getImageBitmap();
            // store bitmap to file
            File filename = new File(Environment.getExternalStorageDirectory(), "imageName.jpg");
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(filename);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            itemImage.compress(Bitmap.CompressFormat.JPEG, 60, out);
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // get base64 string from file
            String base64 = getStringImage(filename);
            /*if (i > 0) {
                picturesArray = picturesArray + ",{\"path\":\"" + base64 + "\",\"detail\":\"" + "" + "\"}";
            } else {
                picturesArray = picturesArray + "{\"path\":\"" + base64 + "\",\"detail\":\"" + "" + "\"}";
            }*/

            picturesArray = picturesArray + "\'" + base64 + "\'" + ",";

//                    ,{\"path\":\"title\",\"detail\":\"dddd\"}]"
        }
        picturesArray = picturesArray.substring(0, picturesArray.length() - 1) + "]";

        return picturesArray;
    }

    private static String getPicture(int position) {
        String picture = "";
        for (int i = 0; i < imagesAdapter.getItemCount(); i++) {
            if (i == position) {

/*                Bitmap itemImage = images.get(i).getImageBitmap();
                // store bitmap to file
                File filename = new File(Environment.getExternalStorageDirectory(), "imageName.jpg");
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(filename);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                itemImage.compress(Bitmap.CompressFormat.JPEG, 60, out);
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // get base64 string from file
                String base64 = getStringImage(filename);
            *//*if (i > 0) {
                picturesArray = picturesArray + ",{\"path\":\"" + base64 + "\",\"detail\":\"" + "" + "\"}";
            } else {
                picturesArray = picturesArray + "{\"path\":\"" + base64 + "\",\"detail\":\"" + "" + "\"}";
            }*//*


            */

                Bitmap itemImage = images.get(i).getImageBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                itemImage.compress(Bitmap.CompressFormat.PNG, 90, stream);
//                itemImage.compress(Bitmap.CompressFormat.PNG, 60, stream);
                byte[] image = stream.toByteArray();
//                System.out.println("byte array:" + image);

                String base64 = Base64.encodeToString(image, 0);

                picture = base64;
                break;
            }

//                    ,{\"path\":\"title\",\"detail\":\"dddd\"}]"
        }
        return picture;
    }

    private String getStringImage(File file) {
        try {
            FileInputStream fin = new FileInputStream(file);
            byte[] imageBytes = new byte[(int) file.length()];
            fin.read(imageBytes, 0, imageBytes.length);
            fin.close();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception ex) {
            Toast.makeText(this, "Image size is too large to upload", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private void setSubCategoriesInSpinner(String selectedCategory) {
        ArrayList<String> subCategories = new ArrayList<String>();
        JSONArray m_jArry;
        JSONObject obj;

/*
        //Initialize sub-categories list
        try {
            obj = new JSONObject(loadJSONFromAsset());
            m_jArry = obj.getJSONArray(selectedCategory);
            subCategories = new ArrayList<String>();
            String city;

            for (int i = 0; i < m_jArry.length(); i++) {
                city = m_jArry.getString(i);
                if (city.length() > 0 && !subCategories.contains(city)) {
                    subCategories.add(city);
                }
            }
            Collections.sort(subCategories, String.CASE_INSENSITIVE_ORDER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */

        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getName().equals(selectedCategory)) {
                subCategories = categories.get(i).getSubCategoriesNames();
            }
        }

        /*subCategories.add("Audio and Hi-Fi");
        subCategories.add("Cameras and Photography");
        subCategories.add("Computing");
        subCategories.add("Consoles and Computer Games");
        subCategories.add("Kitchen appliances");
        subCategories.add("Mobile Phones & phones");
        subCategories.add("Satellite, Cable");
        subCategories.add("Small Electricals");*/

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddItemActivity.this, android.R.layout.simple_spinner_item, subCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spSubCategory.setAdapter(adapter);
    }

    private void setCitiesInSpinner(final String userStateID, final String userCityID) {

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_CITIES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            cities.clear();
                            citiesNames.clear();
                            JSONArray statesArray = new JSONArray(response);
                            for (int i = 0; i < statesArray.length(); i++) {
                                JSONObject stateObj = statesArray.getJSONObject(i);
                                cities.add(new CitiesDataModel(stateObj.getString("state_id"), stateObj.getString("name"), "2:00", stateObj.getString("id"), "14"));
                                citiesNames.add(stateObj.getString("name"));
                            }

                            adapterCities = new ArrayAdapter<String>(AddItemActivity.this, android.R.layout.simple_spinner_item, citiesNames);
                            spCity.setAdapter(adapterCities);

                            if (!userCityID.equals("")) {
                                for (int j = 0; j < cities.size(); j++) {
                                    if (cities.get(j).getID().equals(userCityID)) {
                                        spCity.setSelection(adapterCities.getPosition(cities.get(j).getCity()));
                                        break;
                                    }
                                }
                            } else {

                            }

                        } catch (Exception ee) {
                            Toast.makeText(AddItemActivity.this, "error: " + ee.toString(), Toast.LENGTH_SHORT).show();
                            finish();
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
                                    Toast.makeText(AddItemActivity.this, "Try again", Toast.LENGTH_SHORT).show();
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
                        finish();
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
                params.put("state_id", userStateID);

                return params;
            }
        };
        queue.add(postRequest);
    }


    private void setCurrencyCode(String selectedCountryCode) {
        JSONObject obj;
        try {
            obj = new JSONObject(loadCurrencyCodeJSONFromAsset());
            String currencyCode = obj.getString(selectedCountryCode);
            tvCurrencyCode.setText(currencyCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadCurrencyCodeJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.country_code_to_currency_code);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.countries_to_cities);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private String getCountryCode(String countryName) {
        JSONArray m_jArry;
        String countryCode = null;
        try {
            m_jArry = new JSONArray(loadAllCountriesJSONFromAsset());

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject countryObj = m_jArry.getJSONObject(i);
                if (countryObj.getString("name").contains(countryName)) {
                    countryCode = countryObj.getString("alpha-2");
                    break;
                } else {
                    countryCode = countryName + " Currency";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return countryCode;
    }

    private String loadAllCountriesJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getResources().openRawResource(R.raw.all_countries);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void initUI() {
        savedAdLocations = false;
        spCountry = (Spinner) findViewById(R.id.spCountry);
        spCity = (Spinner) findViewById(R.id.spCity);
        spStates = (Spinner) findViewById(R.id.spState);
        spCategory = (Spinner) findViewById(R.id.spCategory);
        spSubCategory = (Spinner) findViewById(R.id.spSubCategory);
        bSubmit = (Button) findViewById(R.id.bSubmit);
        bSetLoc = (Button) findViewById(R.id.bSetLoc);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etPrice = (EditText) findViewById(R.id.etPrice);
        etDes1 = (EditText) findViewById(R.id.etDescription1);
        etDes2 = (EditText) findViewById(R.id.etDescription2);
        etDes3 = (EditText) findViewById(R.id.etDescription3);
        etDes4 = (EditText) findViewById(R.id.etDescription4);

        tvDes1 = (TextView) findViewById(R.id.tvDescription1);
        tvDes2 = (TextView) findViewById(R.id.tvDescription2);
        tvDes3 = (TextView) findViewById(R.id.tvDescription3);
        tvDes4 = (TextView) findViewById(R.id.tvDescription4);

        tvSaveAdLocations = (TextView) findViewById(R.id.tvSaveAdLocations);

        rbAlternate = (RadioButton) findViewById(R.id.rbSendToAlternateLoc);
        rbDefault = (RadioButton) findViewById(R.id.rbSendToDefaultLoc);

//        tvDes1.setText(categories.get(i).get);

        tvCurrencyCode = (TextView) findViewById(R.id.tvCurrencyCode);
        tvLatLog = (TextView) findViewById(R.id.tvLatLog);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.bringToFront();

        rvImages = (RecyclerView) findViewById(R.id.rvImages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvImages.setLayoutManager(layoutManager);

        rvItemAttributes = (RecyclerView) findViewById(R.id.rvItemAttributes);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        rvItemAttributes.setLayoutManager(layoutManager2);

        bAddAttribute = (Button) findViewById(R.id.bAddItemAttribute);

        itemAttributeAdapter = new ItemAttributeAdapter(attributeKeys, attributeValues, AddItemActivity.this);
        rvItemAttributes.setAdapter(itemAttributeAdapter);

        imagesAdapter = new ImagesAdapter(images, AddItemActivity.this);
        rvImages.setAdapter(imagesAdapter);

        ivGallery = (ImageView) findViewById(R.id.ivGallery2);
        ivCamera = (ImageView) findViewById(R.id.ivCamera2);

        initCategories();

/*        //Initialize countries list
        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for (Locale loc : locale) {
            country = loc.getDisplayCountry();
            if (country.length() > 0 && !countries.contains(country)) {
                if (country.contains("World")) continue;
                countries.add(country);
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spCountry.setAdapter(adapter);
        String currentCountry = getResources().getConfiguration().locale.getDisplayCountry();
        spCountry.setSelection(adapter.getPosition(currentCountry));
        setCurrencyCode(getCountryCode(spCountry.getSelectedItem() + ""));*/
    }

    private void initCategories() {

        String[] categoriesArray = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            categoriesArray[i] = categories.get(i).getName();
        }
        // Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddItemActivity.this, android.R.layout.simple_spinner_item, categoriesArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spCategory.setAdapter(spinnerArrayAdapter);

        /*// Array of categories
        String categories[] = {"Property", "Vehicle", "Electronics", "Food", "Career Center", "Life Style", "Adults", "Orange", "Business"};

        // Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spCategory.setAdapter(spinnerArrayAdapter);*/


        /*progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

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
                                String[] categories = new String[data.length()];
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject catObj = data.getJSONObject(i);
                                    categories[i] = catObj.getString("name");
                                }
                                // Application of the Array to the Spinner
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddItemActivity.this, android.R.layout.simple_spinner_item, categories);
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                                spCategory.setAdapter(spinnerArrayAdapter);
                            } else if (result.get("status").equals("error")) {

                            }
                        } catch (Exception ee) {

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
                                    Toast.makeText(AddItemActivity.this, "Try again", Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };
        queue.add(postRequest);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        //For gallery
        if (requestCode == 1 & resultCode != Activity.RESULT_CANCELED) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap = decodeSampledBitmapFromUri(AddItemActivity.this, filePath, 200, 200);
                bitmap = getPhoto(filePath);
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, false);

                //Setting the Bitmap to ImageView

                imagesModel = new ImagesModel(bitmap);
                images.add(imagesModel);
                imagesAdapter.notifyDataSetChanged();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //For camera
        if (requestCode == 2 & resultCode != Activity.RESULT_CANCELED) {
/*

            Bundle extras = data.getExtras();
            final ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bitmap = (Bitmap) extras.get("data");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
*/


            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, false);


                imagesModel = new ImagesModel(bitmap);
                images.add(imagesModel);
                imagesAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        //For place picker
        if (requestCode == 3 & resultCode != Activity.RESULT_CANCELED) {

            Place selectedPlace = PlacePicker.getPlace(this, data);
            String name = (String) selectedPlace.getName();
            LatLng latLang = selectedPlace.getLatLng();
            locLat = latLang.latitude + "";
            locLog = latLang.longitude + "";
            tvLatLog.setText("Location: " + locLat + ", " + locLog);
            tvLatLog.setVisibility(View.VISIBLE);
        }

        progressBar.setVisibility(View.GONE);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    public static Bitmap decodeSampledBitmapFromUri(Context context, Uri imageUri, int reqWidth, int reqHeight) throws FileNotFoundException {

        // Get input stream of the image
        final BitmapFactory.Options options = new BitmapFactory.Options();
        InputStream iStream = context.getContentResolver().openInputStream(imageUri);

        // First decode with inJustDecodeBounds=true to check dimensions
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(iStream, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(iStream, null, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    Bitmap getPhoto(Uri selectedImage) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        Bitmap photoBitmap = null;
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, bitmapOptions);
        int imageWidth = bitmapOptions.outWidth;
        int imageHeight = bitmapOptions.outHeight;

        @SuppressWarnings("unused")
        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        float scale = 1.0f;

        if (imageWidth < imageHeight) {
            if (imageHeight > width * 1.0f) {
                scale = width * 1.0f / (imageHeight * 1.0f);
            }

        } else {
            if (imageWidth > width * 1.0f) {
                scale = width * 1.0f / (imageWidth * 1.0f);
            }

        }

        photoBitmap = decodeSampledBitmapFromResource(this,
                selectedImage, (int) (imageWidth * scale),
                (int) (imageHeight * scale));
        return photoBitmap;
    }

    public static Bitmap decodeSampledBitmapFromResource(Context context, Uri uri, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        BitmapFactory.decodeStream(is, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode editBitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        InputStream inputs = null;
        try {
            inputs = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return BitmapFactory.decodeStream(inputs, null, options);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
                Toast.makeText(this, "Permission granted for camera", Toast.LENGTH_SHORT).show();
            }
            else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close
                Toast.makeText(this, "No permission granted for camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        backCount += 1;
        if (backCount == 2) finish();
        else Toast.makeText(this, "Press back again", Toast.LENGTH_SHORT).show();
    }
}