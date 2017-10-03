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
import android.widget.TextView;
import android.widget.Toast;

import com.codiansoft.oneandonly.AdCitiesActivity;
import com.codiansoft.oneandonly.AdStatesActivity;
import com.codiansoft.oneandonly.GlobalClass;
import com.codiansoft.oneandonly.R;
import com.codiansoft.oneandonly.model.StatesDataModel;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;

import static com.codiansoft.oneandonly.AdStatesActivity.dataModels;

/**
 * Created by Codiansoft on 7/25/2017.
 */

public class ListViewAdapterAdStates extends BaseAdapter implements Filterable {
    ListViewAdapter adapter;
    private LayoutInflater inflater;

    private Context mContext;
    private ArrayList<StatesDataModel> mDisplayedValues;    // Values to be displayed

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

    public ListViewAdapterAdStates(Context mContext) {
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

        final ListViewAdapterAdStates.ViewHolder viewHolder;
        if (inflater == null)
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.ad_states_listview_item, null, true);

        viewHolder = new ListViewAdapterAdStates.ViewHolder();

        viewHolder.txtStateName = (TextView) view.findViewById(R.id.tvStateName);
        viewHolder.txtCity = (TextView) view.findViewById(R.id.tvCityName);
        viewHolder.txtLastUpdateTime = (TextView) view.findViewById(R.id.tvLastUpdateTime);
        viewHolder.txtID = (TextView) view.findViewById(R.id.tvID);
        viewHolder.txtQuantity = (TextView) view.findViewById(R.id.tvQuantity);
        viewHolder.t = (TextView) view.findViewById(R.id.position);
        viewHolder.sbOnOff = (SwitchButton) view.findViewById(R.id.sbOnOrOff);
        viewHolder.sbOnOff.setVisibility(View.VISIBLE);

        view.setTag(viewHolder);

        final StatesDataModel dataModel = mDisplayedValues.get(i);

        viewHolder.sbOnOff.setChecked(dataModel.getOnOff());
        viewHolder.txtStateName.setText(dataModel.getState());
        viewHolder.txtLastUpdateTime.setText(dataModel.getLastUpdateTime());
        viewHolder.txtID.setText(dataModel.getID());
        viewHolder.txtQuantity.setText(dataModel.getQuantity());

        viewHolder.sbOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    GlobalClass.selectedAddPostStateID = dataModel.getID();
                    GlobalClass.selectedAddPostState = dataModel.getState();

                    for (int j = 0; j < mDisplayedValues.size(); j++) {
                        if (i != j) {
                            mDisplayedValues.get(j).setOnOff(false);
                            notifyDataSetChanged();
                        } else {
                            mDisplayedValues.get(j).setOnOff(true);
                            viewHolder.sbOnOff.setChecked(true);
                        }
                    }
//                    Toast.makeText(mContext, GlobalClass.selectedAddPostState, Toast.LENGTH_SHORT).show();
                    if (!GlobalClass.selectedAddPostStateID.equals("")) {
                        Intent i = new Intent(mContext, AdCitiesActivity.class);
                        mContext.startActivity(i);

                    }
                } else {
                    GlobalClass.selectedAddPostStateID = "";
                    GlobalClass.selectedAddPostState = "";
                    for (int j = 0; j < mDisplayedValues.size(); j++) {
                        mDisplayedValues.get(j).setOnOff(false);
                        notifyDataSetChanged();
                    }
                }
            }
        });
        return view;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mDisplayedValues = (ArrayList<StatesDataModel>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<StatesDataModel> FilteredArrList = new ArrayList<StatesDataModel>();

                if (dataModels == null) {
                    dataModels = new ArrayList<StatesDataModel>(mDisplayedValues); // saves the original data in mOriginalValues
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
                        String data = dataModels.get(i).getState();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new StatesDataModel(dataModels.get(i).getCountry(), dataModels.get(i).getState(), dataModels.get(i).getLastUpdateTime(), dataModels.get(i).getID(), dataModels.get(i).getQuantity()));
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