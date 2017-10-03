package com.codiansoft.oneandonly;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.HashMap;

public class Level4ItemDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private SliderLayout mDemoSlider;

    TextView tvCityName;
    TextView tvPropertyName;
    TextView tvPropertyDetails;
    TextView tvContact1;
    TextView tvContact2;
    TextView tvEmail;
    Button bBack;
    Button bEmail;
    Button bCall;
    Button bMap;
    Button bDelete;
    ImageView ivPropertyImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);

        initUI();
        initSlider();

    }

    private void initSlider() {
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        final HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Picture 1", R.drawable.sample_property_pic2);
        file_maps.put("Picture 2", R.drawable.sample_property_pic1);
        file_maps.put("Picture 3", R.drawable.sample_property_pic2);
//        Glide.with(AdDetailsActivity.this).load(GlobalClass.selectedPropertyImageURL).into(ivPropertyImage);

        Glide.with(this)
                .load(GlobalClass.selectedLevel3ItemImageURL)
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
        tvCityName = (TextView) findViewById(R.id.tvCityName);
        tvPropertyName = (TextView) findViewById(R.id.tvPropertyName);
        tvPropertyDetails = (TextView) findViewById(R.id.tvPropertyDetails);
        tvContact1 = (TextView) findViewById(R.id.tvContact1);
        tvContact2 = (TextView) findViewById(R.id.tvContact2);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        bBack = (Button) findViewById(R.id.bBack);
        bEmail = (Button) findViewById(R.id.bEmail);
        bCall = (Button) findViewById(R.id.bCall);
        bMap = (Button) findViewById(R.id.bMap);
        bDelete = (Button) findViewById(R.id.bDelete);
        ivPropertyImage = (ImageView) findViewById(R.id.ivPropertyImage);

        bBack.setOnClickListener(this);
        bEmail.setOnClickListener(this);
        bCall.setOnClickListener(this);
        bMap.setOnClickListener(this);
        bDelete.setOnClickListener(this);

//        Glide.with(this).load(GlobalClass.selectedPropertyImageURL).into(ivPropertyImage);
        Glide.with(Level4ItemDetailsActivity.this).load(GlobalClass.selectedLevel3ItemImageURL).into(ivPropertyImage);

        tvCityName.setText(GlobalClass.selectedLevel2City);
        tvPropertyName.setText(GlobalClass.selectedLevel3ItemName);
        tvPropertyDetails.setText(GlobalClass.selectedLevel3ItemDescription);
        tvContact1.setText(GlobalClass.selectedLevel3ContactNumber1);
        tvContact2.setText(GlobalClass.selectedLevel3ContactNumber2);
        tvEmail.setText(GlobalClass.selectedLevel3ItemEmail);
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
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{GlobalClass.selectedLevel3ItemEmail});
                i.putExtra(Intent.EXTRA_SUBJECT, "");
                i.putExtra(Intent.EXTRA_TEXT, "");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Level4ItemDetailsActivity.this, "There are no email clients installed", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bCall:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + GlobalClass.selectedLevel3ContactNumber1));
                if (ActivityCompat.checkSelfPermission(Level4ItemDetailsActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
                Intent mapIntent = new Intent(Level4ItemDetailsActivity.this,MapItemLocationActivity.class);
                startActivity(mapIntent);
                break;
            case R.id.bDelete:

                break;
        }
    }
}