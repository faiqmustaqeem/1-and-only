package com.codiansoft.oneandonly.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codiansoft.oneandonly.R;
import com.codiansoft.oneandonly.model.PropertyListItemDataModel;

import java.util.ArrayList;

/**
 * Created by salal-khan on 4/20/2017.
 */

public class PropertiesListViewAdapter extends ArrayAdapter<PropertyListItemDataModel> {

    private ArrayList<PropertyListItemDataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtCategory;
        TextView txtLastUpdateTime;
        TextView txtID;
    }

    public PropertiesListViewAdapter(ArrayList<PropertyListItemDataModel> data, Context context) {
        super(context, R.layout.properties_item_view, data);
        this.dataSet = data;
        this.mContext = context;

    }

/*    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        PropertyListItemDataModel dataModel=(PropertyListItemDataModel) object;

        switch (v.getId())
        {
            case R.id.bOpen:

                *//*Intent i = new Intent(getContext(), RestaurantOffersAcvivity.class);
                i.putExtra("restaurantName",dataModel.getName());
                GlobalClass.selectedRestaurant = dataModel.getName();
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(i);*//*
                break;
        }
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PropertyListItemDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.properties_item_view, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.txtCategory = (TextView) convertView.findViewById(R.id.tvAddress);
            viewHolder.txtLastUpdateTime = (TextView) convertView.findViewById(R.id.tvLastUpdateTime);
            viewHolder.txtID = (TextView) convertView.findViewById(R.id.tvID);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtCategory.setText(dataModel.getCategory());
        viewHolder.txtLastUpdateTime.setText(dataModel.getLastUpdateTime());
        viewHolder.txtID.setText(dataModel.getID());
        // Return the completed view to render on screen
        return convertView;
    }

}
