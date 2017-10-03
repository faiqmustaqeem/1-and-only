package com.codiansoft.oneandonly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class ChooseCountryAndCityActivity extends AppCompatActivity {
    Button bSubmit;
    Spinner citizenship, cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_country_and_city);

        initUI();

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChooseCountryAndCityActivity.this, AdListViewActivity.class);
                startActivity(i);
            }
        });

        citizenship.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                setCitiesInSpinner(citizenship.getSelectedItem() + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

    }

    private void setCitiesInSpinner(String s) {
        ArrayList<String> cities = null;
        JSONArray m_jArry;
        JSONObject obj;

        //Initialize cities list
        try {
            obj = new JSONObject(loadJSONFromAsset());
            m_jArry = obj.getJSONArray(s);
            cities = new ArrayList<String>();
            String city;

            for (int i = 0; i < m_jArry.length(); i++) {
                city = m_jArry.getString(i);
                if (city.length() > 0 && !cities.contains(city)) {
                    cities.add(city);
                }
            }
            Collections.sort(cities, String.CASE_INSENSITIVE_ORDER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ChooseCountryAndCityActivity.this, android.R.layout.simple_spinner_item, cities);
        cityName.setAdapter(adapter);
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
        bSubmit = (Button) findViewById(R.id.bSubmit);
        cityName = (Spinner)findViewById(R.id.spCity);
        citizenship = (Spinner)findViewById(R.id.spCountry);

        //Initialize countries list
        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for (Locale loc : locale) {
            country = loc.getDisplayCountry();
            if (country.length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ChooseCountryAndCityActivity.this, android.R.layout.simple_spinner_item, countries);
        citizenship.setAdapter(adapter);
        citizenship.setSelection(adapter.getPosition("Saudi Arabia"));
    }
}
