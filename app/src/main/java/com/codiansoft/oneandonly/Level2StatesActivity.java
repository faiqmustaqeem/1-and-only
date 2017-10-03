package com.codiansoft.oneandonly;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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
import com.codiansoft.oneandonly.adapter.ListViewAdapterLevel2States;
import com.codiansoft.oneandonly.model.StatesDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.codiansoft.oneandonly.GlobalClass.alreadyLoggedIn;

public class Level2StatesActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView mListView;
    private ListViewAdapterLevel2States mAdapter;
    private Context mContext = this;
    Button bSave;
    TextView tvCountry, tvBack;

    public static ArrayList<StatesDataModel> states;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2_states);

        initUI();

        states = new ArrayList<>();

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

        initializeStatesList();

        if (!GlobalClass.postingAd) {
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ((SwipeLayout) (mListView.getChildAt(position - mListView.getFirstVisiblePosition()))).open(true);
                    StatesDataModel dataModel = states.get(position);

                    if (alreadyLoggedIn) {
                        GlobalClass.selectedLevel2State = dataModel.getState();
                        GlobalClass.selectedLevel2StateID = dataModel.getID();
                        Intent i = new Intent(Level2StatesActivity.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(i);
                    } else {

                        GlobalClass.selectedLevel2State = dataModel.getState();
                        GlobalClass.selectedLevel2StateID = dataModel.getID();

                        Intent i = new Intent(Level2StatesActivity.this, Level2CitiesActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                }
            });
        }
    }

    private void initializeStatesList() {

/*        JSONArray m_jArry;
        JSONObject obj;

        //Initialize States list
        try {
            obj = new JSONObject(loadJSONFromAsset());
            m_jArry = obj.getJSONArray(GlobalClass.selectedLevel2Country);
            states = new ArrayList<>();
            String state;

            for (int i = 0; i < m_jArry.length(); i++) {
                state = m_jArry.getString(i);
                if (state.length() > 0 && !states.contains(state)) {
                    states.add(new StatesDataModel(GlobalClass.selectedLevel2Country, state, "2:00", "1", "12"));
                }
            }
//            Collections.sort(states, String.CASE_INSENSITIVE_ORDER);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter = new ListViewAdapterLevel2States(this);
        mListView.setAdapter(mAdapter);*/
//        mAdapter.setMode(Attributes.Mode.Single);


        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_STATES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONArray statesArr = new JSONArray(response);
                            for (int i = 0; i < statesArr.length(); i++) {
                                JSONObject stateObj = statesArr.getJSONObject(i);
                                states.add(new StatesDataModel(GlobalClass.selectedLevel2Country, stateObj.getString("name"), "2:00", stateObj.getString("id"), "12"));
                            }

                            mAdapter = new ListViewAdapterLevel2States(Level2StatesActivity.this);
                            mListView.setAdapter(mAdapter);

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
                Map<String, String> params = new HashMap<String, String>();
                params.put("country_id", GlobalClass.selectedLevel2CountryID);
                return params;
            }
        };
        queue.add(postRequest);


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

    private void initUI() {
        tvBack = (TextView) findViewById(R.id.tvBack);
        bSave = (Button) findViewById(R.id.bSave);
        tvCountry = (TextView) findViewById(R.id.tvCountry);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.bringToFront();
        tvCountry.setText(GlobalClass.selectedLevel2Country);
        bSave.setOnClickListener(this);
        tvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bSave:
                Intent mainIntent = new Intent(Level2StatesActivity.this, MainActivity.class);
                startActivity(mainIntent);
                break;
            case R.id.tvBack:
                onBackPressed();
                break;
        }
    }

}
