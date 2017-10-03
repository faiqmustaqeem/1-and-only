package com.codiansoft.oneandonly.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.codiansoft.oneandonly.GlobalClass;
import com.codiansoft.oneandonly.R;
import com.codiansoft.oneandonly.model.CitiesDataModel;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;

import static com.codiansoft.oneandonly.AdCitiesActivity.dataModels;
import static com.codiansoft.oneandonly.AdCitiesActivity.tvSelectedAdCities;
import static com.codiansoft.oneandonly.GlobalClass.selectedAddPostCityIDs;
import static com.codiansoft.oneandonly.GlobalClass.selectedAddPostCityNames;

/**
 * Created by Codiansoft on 7/26/2017.
 */

public class ListViewAdapterAdCities extends BaseAdapter implements Filterable {

    ListViewAdapter adapter;
    private LayoutInflater inflater;

    private Context mContext;
    private ArrayList<CitiesDataModel> mDisplayedValues;    // Values to be displayed


    // View lookup cache
    private static class ViewHolder {
        TextView txtStateName;
        TextView txtCity;
        TextView txtLastUpdateTime;
        TextView txtID;
        TextView txtQuantity;
        TextView t;
        SwitchButton sbOnOff;
    }

    public ListViewAdapterAdCities(Context mContext) {
        this.mContext = mContext;
        mDisplayedValues = dataModels;
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

        final CitiesDataModel dataModel = mDisplayedValues.get(i);

        final ListViewAdapterAdCities.ViewHolder viewHolder;
        if (inflater == null)
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.ad_states_listview_item, null, true);

        viewHolder = new ListViewAdapterAdCities.ViewHolder();

        viewHolder.txtStateName = (TextView) view.findViewById(R.id.tvStateName);
        viewHolder.txtCity = (TextView) view.findViewById(R.id.tvCityName);
        viewHolder.txtLastUpdateTime = (TextView) view.findViewById(R.id.tvLastUpdateTime);
        viewHolder.txtID = (TextView) view.findViewById(R.id.tvID);
        viewHolder.txtQuantity = (TextView) view.findViewById(R.id.tvQuantity);
        viewHolder.t = (TextView) view.findViewById(R.id.position);
        viewHolder.sbOnOff = (SwitchButton) view.findViewById(R.id.sbOnOrOff);
        viewHolder.sbOnOff.setVisibility(View.VISIBLE);
        viewHolder.sbOnOff.setChecked(dataModel.getOnOff());

        view.setTag(viewHolder);

        viewHolder.sbOnOff.setChecked(dataModel.getOnOff());
        viewHolder.txtStateName.setText(dataModel.getCity());
        viewHolder.txtLastUpdateTime.setText(dataModel.getLastUpdateTime());
        viewHolder.txtID.setText(dataModel.getID());
        viewHolder.txtQuantity.setText(dataModel.getQuantity());

        viewHolder.sbOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    GlobalClass.selectedAddPostCityID = GlobalClass.selectedAddPostCityID + dataModel.getID() + ",";
                    selectedAddPostCityIDs.append(dataModel.getID() + ",");
                    selectedAddPostCityNames.append(dataModel.getCity() + ",");
                    GlobalClass.selectedAddPostCity = dataModel.getID();

                    dataModels.get(i).setOnOff(true);
                    viewHolder.sbOnOff.setChecked(true);
                    tvSelectedAdCities.setText(GlobalClass.selectedAddPostCity);

//                    Toast.makeText(mContext, dataModel.getCity(), Toast.LENGTH_SHORT).show();
                } else {
                    GlobalClass.selectedAddPostCityID = "";
                    GlobalClass.selectedAddPostCity = "";
                    replaceString(selectedAddPostCityIDs, dataModel.getID() + ",", "");
                    replaceString(selectedAddPostCityNames, dataModel.getCity() + ",", "");

                    dataModels.get(i).setOnOff(false);
                }
                if (!selectedAddPostCityIDs.toString().equals("") & !selectedAddPostCityNames.toString().equals("")) {
                    tvSelectedAdCities.setVisibility(View.VISIBLE);
                    tvSelectedAdCities.setText(selectedAddPostCityNames.substring(0, selectedAddPostCityNames.length() - 1));
                } else {
                    tvSelectedAdCities.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }

    private void replaceString(StringBuilder selectedAddPostCityIDs, String toReplace, String replacement) {
        int index = -1;
        while ((index = selectedAddPostCityIDs.lastIndexOf(toReplace)) != -1) {
            selectedAddPostCityIDs.replace(index, index + toReplace.length(), replacement);
        }
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mDisplayedValues = (ArrayList<CitiesDataModel>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<CitiesDataModel> FilteredArrList = new ArrayList<CitiesDataModel>();

                if (dataModels == null) {
                    dataModels = new ArrayList<CitiesDataModel>(mDisplayedValues); // saves the original data in mOriginalValues
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
                        String data = dataModels.get(i).getCity();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new CitiesDataModel(dataModels.get(i).getState(), dataModels.get(i).getCity(), dataModels.get(i).getLastUpdateTime(), dataModels.get(i).getID(), dataModels.get(i).getQuantity()));
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
