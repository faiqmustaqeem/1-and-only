package com.codiansoft.oneandonly;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codiansoft.oneandonly.adapter.ListViewAdapterLevel3;
import com.codiansoft.oneandonly.dialog.RegisterDialog;
import com.codiansoft.oneandonly.model.Level3ListItemDataModel;
import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;

public class Level3Activity extends AppCompatActivity implements View.OnClickListener {

    private ListView mListView;
    private ListViewAdapterLevel3 mAdapter;
    private Context mContext = this;
    TextView tvCountryName,tvCityName;
    Button bSave;

    public static ArrayList<Level3ListItemDataModel> dataModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level3);

        initUI();

        dataModels = new ArrayList<>();

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

        dataModels = new ArrayList<>();

        if(GlobalClass.selectedLevel1CategoryName.equals("Vehicles")) {
            dataModels.add(new Level3ListItemDataModel("Daihatsu ", "Mira Esi L Smart Edition", "45,000", "1", "12", "https://www.whatcar.com/car-leasing/images/vehicles/medium/100892.jpg","0","0","030000000","031111111","a@a.com"));
            dataModels.add(new Level3ListItemDataModel("Honda", "Vezel XL Leather Edition verifiable 4 Grade fresh import April 2017", "6:45", "1", "120", "https://cnet1.cbsistatic.com/img/gPJixVYMEH7E1iTArc-2sSYkR2Y=/300x225/2017/03/15/27b63bdf-42ea-4095-a22c-e7bb9ba2f87a/2017-chrysler-pacifica-limited-4.jpg","0","0","030000000","031111111","a@a.com"));
            dataModels.add(new Level3ListItemDataModel("Mercedes", "Mercedes-Benz S320 CDI", "12:55", "1", "12", "http://media.caranddriver.com/images/17q2/678295/2017-mercedes-amg-cla45-and-2018-gla45-first-drive-review-car-and-driver-photo-679297-s-340x208.jpg","0","0","030000000","031111111","a@a.com"));
            dataModels.add(new Level3ListItemDataModel("Toyota", "Prius Pearl White new shape Prius S package", "23:00", "1", "12", "http://media.caranddriver.com/images/media/672263/2017-kia-soul-in-depth-model-review-car-and-driver-photo-679357-s-450x274.jpg","0","0","030000000","031111111","a@a.com"));
            dataModels.add(new Level3ListItemDataModel("Toyota", "Passo mdl 2010 Rgr 2015", "21:54", "1", "12", "https://cnet1.cbsistatic.com/img/qJ3R5F7H2JLXUF28GMEwVEPxkdg=/300x225/2017/04/19/678a9d51-0918-4641-a2f5-e9d878747434/2018-lexus-nx-19.jpg","0","0","030000000","031111111","a@a.com"));
            dataModels.add(new Level3ListItemDataModel("Toyota", "Aqua model 2014 import 2017", "5:23", "1", "12", "http://app.canada.com/chrome/get.svc/image/388922?resize=compare:search","0","0","030000000","031111111","a@a.com"));
        }
        mAdapter = new ListViewAdapterLevel3(this);
        mListView.setAdapter(mAdapter);
        mAdapter.setMode(Attributes.Mode.Single);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ((SwipeLayout) (mListView.getChildAt(position - mListView.getFirstVisiblePosition()))).open(true);
                Level3ListItemDataModel dataModel = dataModels.get(position);

                GlobalClass.selectedLevel3ItemName = dataModel.getName();
                GlobalClass.selectedLevel3ItemCost = dataModel.getCost();
                GlobalClass.selectedLevel3ItemID = dataModel.getID();
                GlobalClass.selectedLevel3ItemDescription= dataModel.getDescription();
                GlobalClass.selectedLevel3ItemImageURL= dataModel.getItemImageURL();
                GlobalClass.selectedLevel3ContactNumber1 = dataModel.getContact1();
                GlobalClass.selectedLevel3ContactNumber2 = dataModel.getContact2();
                GlobalClass.selectedLevel3ItemEmail = dataModel.getEmail();
                GlobalClass.selectedLevel3Latitude = dataModel.getLatitude();
                GlobalClass.selectedLevel3Longitude = dataModel.getLongitude();

                Intent i = new Intent(Level3Activity.this, Level4ItemDetailsActivity.class);
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

    private void initUI() {
        tvCountryName = (TextView) findViewById(R.id.tvCountryName);
        tvCountryName.setText(GlobalClass.selectedLevel2Country);
        tvCityName = (TextView) findViewById(R.id.tvCityName);
        tvCityName.setText(GlobalClass.selectedLevel2City);

        bSave = (Button) findViewById(R.id.bSave);
        bSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bSave:
                Intent mainIntent = new Intent(Level3Activity.this,MainActivity.class);
                startActivity(mainIntent);
                break;
        }
    }
}