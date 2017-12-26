package com.codiansoft.oneandonly.adapter;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codiansoft.oneandonly.AdCountriesActivity;
import com.codiansoft.oneandonly.AdStatesActivity;
import com.codiansoft.oneandonly.GlobalClass;
import com.codiansoft.oneandonly.R;
import com.codiansoft.oneandonly.model.CountriesDataModel;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;

import static com.codiansoft.oneandonly.AdCountriesActivity.dataModels;

/**
 * Created by Codiansoft on 7/24/2017.
 */

public class ListViewAdapterAdCountries extends BaseAdapter implements Filterable {

    ListViewAdapter adapter;
    private LayoutInflater inflater;

    private Context mContext;

    private ArrayList<CountriesDataModel> mDisplayedValues;    // Values to be displayed

    // View lookup cache
    private static class ViewHolder {
        TextView txtCountryName;
        TextView txtCity;
        TextView txtLastUpdateTime;
        TextView txtID;
        TextView txtQuantity;
        TextView t;
        ImageView ivFlag;
        SwitchButton sbOnOff;
    }

    public ListViewAdapterAdCountries(Context mContext) {
        this.mContext = mContext;
        this.mDisplayedValues = dataModels;
    }

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final ListViewAdapterAdCountries.ViewHolder viewHolder;
        if (inflater == null)
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.ad_countries_listview_item, null, true);

        viewHolder = new ListViewAdapterAdCountries.ViewHolder();

        viewHolder.txtCountryName = (TextView) view.findViewById(R.id.tvCountryName);
        viewHolder.txtCity = (TextView) view.findViewById(R.id.tvCityName);
        viewHolder.txtLastUpdateTime = (TextView) view.findViewById(R.id.tvLastUpdateTime);
        viewHolder.txtID = (TextView) view.findViewById(R.id.tvID);
        viewHolder.txtQuantity = (TextView) view.findViewById(R.id.tvQuantity);
        viewHolder.t = (TextView) view.findViewById(R.id.position);
        viewHolder.ivFlag = (ImageView) view.findViewById(R.id.ivFlag);
        viewHolder.sbOnOff = (SwitchButton) view.findViewById(R.id.sbOnOrOff);
        viewHolder.sbOnOff.setVisibility(View.VISIBLE);

        view.setTag(viewHolder);

//        PropertyListItemDataModel dataModel = getItem(position);
        final CountriesDataModel dataModel = mDisplayedValues.get(i);

            /*String[] values = mContext.getResources().getStringArray(R.array.CountryCodes);

            String[] g = values[i].split(",");

            Locale loc = new Locale("", g[1]);

            loc.getDisplayCountry().trim();

            String pngName = g[1].trim().toLowerCase();*/

        viewHolder.sbOnOff.setChecked(dataModel.getOnOff());
        viewHolder.txtCountryName.setText(dataModel.getCountry());
        viewHolder.txtLastUpdateTime.setText(dataModel.getLastUpdateTime());
        viewHolder.txtID.setText(dataModel.getID());
        viewHolder.txtQuantity.setText(dataModel.getQuantity());

        viewHolder.ivFlag.setImageResource(mContext.getResources().getIdentifier("drawable/" + dataModel.getStateCode(), null, mContext.getPackageName()));


        viewHolder.sbOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    GlobalClass.selectedAddPostCountry = dataModel.getCountry();
                    GlobalClass.selectedAddPostCountryID = dataModel.getID();

                    for (int j = 0; j < mDisplayedValues.size(); j++) {
                        if (i != j) {
                            mDisplayedValues.get(j).setOnOff(false);
                            notifyDataSetChanged();
                        } else {
                            mDisplayedValues.get(j).setOnOff(true);
                            viewHolder.sbOnOff.setChecked(true);
                        }
                    }
//                    Toast.makeText(mContext, GlobalClass.selectedAddPostCountry, Toast.LENGTH_SHORT).show();if (!GlobalClass.selectedAddPostCountry.equals("")) {
                // yahan p chng karna hai
                    Intent i = new Intent(mContext, AdStatesActivity.class);
                    mContext.startActivity(i);

                } else {
                    GlobalClass.selectedAddPostCountry = "";
                    GlobalClass.selectedAddPostCountryID = "";
                    for (int j = 0; j < mDisplayedValues.size(); j++) {
                        mDisplayedValues.get(j).setOnOff(false);
                        notifyDataSetChanged();
                    }
                }
            }
        });

/*
        if (GlobalClass.postingAd) {
            viewHolder.txtStateName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewHolder.sbOnOff.isChecked()) {
                        Intent i = new Intent(mContext, AdStatesActivity.class);
                        mContext.startActivity(i);
                        GlobalClass.selectedLevel2Country = viewHolder.txtStateName.getText().toString();
                        GlobalClass.selectedLevel2CountryID = dataModel.getID();
                    }
                }
            });
        }
*/

        return view;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mDisplayedValues = (ArrayList<CountriesDataModel>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<CountriesDataModel> FilteredArrList = new ArrayList<CountriesDataModel>();

                if (dataModels == null) {
                    dataModels = new ArrayList<CountriesDataModel>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = dataModels.size();
                    results.values = dataModels;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < dataModels.size(); i++) {
                        String data = dataModels.get(i).getCountry();
                        CountriesDataModel tempModel = null;
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            tempModel = new CountriesDataModel(dataModels.get(i).getCountry(), dataModels.get(i).getStateCode(), dataModels.get(i).getCity(), dataModels.get(i).getLastUpdateTime(), dataModels.get(i).getID(), dataModels.get(i).getQuantity());
                            tempModel.setOnOff(dataModels.get(i).getOnOff());
                            FilteredArrList.add(tempModel);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}