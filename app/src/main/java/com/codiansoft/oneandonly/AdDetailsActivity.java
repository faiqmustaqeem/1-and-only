package com.codiansoft.oneandonly;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.codiansoft.oneandonly.adapter.RVAdapterAdImages;
import com.codiansoft.oneandonly.model.AdImagesModel;
import com.codiansoft.oneandonly.model.MyAdsModel;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codiansoft.oneandonly.GlobalClass.selectedItemDataModel;

public class AdDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    public static MyAdsModel selectedMyAdDataModel;
    public static boolean isMyAd;
    TextView tvDescription1Heading, tvDescription2Heading, tvDescription3Heading, tvDescription4Heading;
    TextView tvDescription1, tvDescription2, tvDescription3, tvDescription4;

    private SliderLayout mDemoSlider;

    TextView tvCityName;
    TextView tvPropertyName;
    TextView tvPropertyDetails;
    TextView tvContact1;
    TextView tvContact2;
    TextView tvEmail;
    TextView tvCurrencyCode;
    TextView tvPrice;
    Button bBack;
    Button bEmail;
    Button bCall;
    Button bMap;
    Button bReport;
    ImageView ivPropertyImage;

    RecyclerViewPager rvAdImages;
    RVAdapterAdImages adImagesAdapter;
    AdImagesModel adImagesModel;
    TextView refernce_number;
    TextView otherAddsCount;
    ImageView favourite;
    RelativeLayout favouriteLayout;

    private List<String> adImagesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);
        initUI();
        initSlider();
    }

    private void initSlider() {
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        final HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Picture 1", R.drawable.sample_property_pic2);
        file_maps.put("Picture 2", R.drawable.sample_property_pic1);
        file_maps.put("Picture 3", R.drawable.sample_property_pic2);

//        Glide.with(AdDetailsActivity.this).load(GlobalClass.selectedPropertyImageURL).into(ivPropertyImage);

        Glide.with(this)
                .load(GlobalClass.selectedPropertyImageURL)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        file_maps.put("Picture 4", resource.getGenerationId());
                    }
                });

        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {

                        }
                    });

/*            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);*/

            mDemoSlider.addSlider(textSliderView);
        }
    }

    private void initUI() {
        favourite=(ImageView)findViewById(R.id.favourite);

        tvCityName = (TextView) findViewById(R.id.tvCityName);
        tvPropertyName = (TextView) findViewById(R.id.tvPropertyName);
        tvPropertyDetails = (TextView) findViewById(R.id.tvPropertyDetails);
        tvContact1 = (TextView) findViewById(R.id.tvContact1);
        tvContact2 = (TextView) findViewById(R.id.tvContact2);
        tvCurrencyCode = (TextView) findViewById(R.id.tvCurrencyCode);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        bBack = (Button) findViewById(R.id.bBack);
        bEmail = (Button) findViewById(R.id.bEmail);
        bCall = (Button) findViewById(R.id.bCall);
        bMap = (Button) findViewById(R.id.bMap);
        bReport = (Button) findViewById(R.id.bReport);
        ivPropertyImage = (ImageView) findViewById(R.id.ivPropertyImage);

        tvDescription1 = (TextView) findViewById(R.id.tvDescription1);
        tvDescription1Heading = (TextView) findViewById(R.id.tvDescription1Heading);
        tvDescription2 = (TextView) findViewById(R.id.tvDescription2);
        tvDescription2Heading = (TextView) findViewById(R.id.tvDescription2Heading);
        tvDescription3 = (TextView) findViewById(R.id.tvDescription3);
        tvDescription3Heading = (TextView) findViewById(R.id.tvDescription3Heading);
        tvDescription4 = (TextView) findViewById(R.id.tvDescription4);
        tvDescription4Heading = (TextView) findViewById(R.id.tvDescription4Heading);
        refernce_number=(TextView)findViewById(R.id.refernce_number);
        otherAddsCount=(TextView)findViewById(R.id.otherAddsCount);

        tvDescription1Heading.setText(GlobalClass.selectedSubCatDes1Title);
        tvDescription2Heading.setText(GlobalClass.selectedSubCatDes2Title);
        tvDescription3Heading.setText(GlobalClass.selectedSubCatDes3Title);
        tvDescription4Heading.setText(GlobalClass.selectedSubCatDes4Title);

        favouriteLayout=(RelativeLayout)findViewById(R.id.favoriteLayout);

        bBack.setOnClickListener(this);
        bEmail.setOnClickListener(this);
        bCall.setOnClickListener(this);
        bMap.setOnClickListener(this);
        bReport.setOnClickListener(this);
        favourite.setOnClickListener(this);

//        Glide.with(this).load(GlobalClass.selectedPropertyImageURL).into(ivPropertyImage);
        Glide.with(AdDetailsActivity.this).load(GlobalClass.selectedPropertyImageURL).into(ivPropertyImage);

        if (isMyAd) {
            tvCityName.setText(selectedMyAdDataModel.getCountryName() + ":" + selectedMyAdDataModel.getStateName() + ":" + selectedMyAdDataModel.getCityname());
            tvCurrencyCode.setText(selectedMyAdDataModel.getCurrencyCode());
            tvPrice.setText(selectedMyAdDataModel.getPrice());
            tvDescription1.setText(selectedMyAdDataModel.getDes1());
            tvDescription2.setText(selectedMyAdDataModel.getDes2());
            tvDescription3.setText(selectedMyAdDataModel.getDes3());
            tvDescription4.setText(selectedMyAdDataModel.getDes4());

        } else {
            tvCityName.setText(selectedItemDataModel.getCountryName() + ":" + selectedItemDataModel.getStateName() + ":" + selectedItemDataModel.getCityname());
            tvCurrencyCode.setText(selectedItemDataModel.getCurrencyCode());
            tvPrice.setText(selectedItemDataModel.getPrice());
            tvDescription1.setText(selectedItemDataModel.getDes1());
            tvDescription2.setText(selectedItemDataModel.getDes2());
            tvDescription3.setText(selectedItemDataModel.getDes3());
            tvDescription4.setText(selectedItemDataModel.getDes4());
        }

        tvPropertyName.setText(GlobalClass.selectedPropertyName);
        tvPropertyDetails.setText(GlobalClass.selectedPropertyDetails);
        tvContact1.setText(GlobalClass.selectedPropertyContact1);
        tvContact2.setText(GlobalClass.selectedPropertyContact2);
        tvEmail.setText(GlobalClass.selectedPropertyEmail);
        tvContact1.setText(GlobalClass.selectedPropertyContact1);
        tvContact2.setText(GlobalClass.selectedPropertyContact2);
        refernce_number.setText(GlobalClass.selectedPropertyRefernceNumber);

        int int_otherAdds=0;
        try{
             int_otherAdds=Integer.parseInt(GlobalClass.otherAddsCount)-1;

            if(int_otherAdds==0)
            {
                otherAddsCount.setText("this person hos no other Ad");
            }
            else
            {
                otherAddsCount.setText("this person has "+int_otherAdds+" other Ads");
            }

        }catch(Exception e)
        {
            otherAddsCount.setVisibility(View.GONE);
        }

        if(GlobalClass.from.equals("favourite"))
        {
            favouriteLayout.setVisibility(View.GONE);
            otherAddsCount.setVisibility(View.GONE);
        }
        rvAdImages = (RecyclerViewPager) findViewById(R.id.rvAdImages);

//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        adImagesAdapter = new RVAdapterAdImages(AdDetailsActivity.this, adImagesList);
        rvAdImages.setLayoutManager(mLayoutManager);
//        rvAdImages.setItemAnimator(new DefaultItemAnimator());
        rvAdImages.setAdapter(adImagesAdapter);

        setAdImagesToRV();

    }

    private void setAdImagesToRV() {
        for (int i = 0; i < GlobalClass.selectedAdImages.size(); i++) {
            adImagesList.add(GlobalClass.selectedAdImages.get(i));
        }
        adImagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bBack:
                finish();
                break;
            case R.id.bEmail:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{GlobalClass.selectedPropertyEmail});
                i.putExtra(Intent.EXTRA_SUBJECT, "");
                i.putExtra(Intent.EXTRA_TEXT, "");

                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(AdDetailsActivity.this, "There are no email clients installed", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.bCall:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + GlobalClass.selectedPropertyContact1));
                if (ActivityCompat.checkSelfPermission(AdDetailsActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                }

                startActivity(callIntent);

                break;
            case R.id.bMap:
                Intent mapIntent = new Intent(AdDetailsActivity.this, MapsPropertyDetailsActivity.class);
                startActivity(mapIntent);
                break;
            case R.id.bReport:
                if (!isMyAd) {
                    startActivity(new Intent(this, ReportAdActivity.class));
                } else {
                    Toast.makeText(this, "Can not report your own add", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.favourite:
                addToFavourite();
                break;

        }
    }
    public void addToFavourite()
    {


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.ADD_TO_FAVOURITE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.e("item_added" , response);
                            Toast.makeText(AdDetailsActivity.this, "Add Added in favourite", Toast.LENGTH_SHORT).show();

                        } catch (Exception ee) {
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
                                    Toast.makeText(AdDetailsActivity.this, "Try again", Toast.LENGTH_SHORT).show();
//                                    utilities.dialog("Connection Problem", act);
                                    break;
                                default:
//                                    utilities.dialog("Connection Problem", act);
                                    break;
                            }
                        }

                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

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
                params.put("user_id", userID);
                params.put("add_id" ,GlobalClass.selectedPropertyID );

                return params;
            }
        };
        queue.add(postRequest);

    }

}
