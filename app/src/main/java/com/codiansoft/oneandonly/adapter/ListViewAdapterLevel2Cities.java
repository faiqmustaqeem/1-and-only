package com.codiansoft.oneandonly.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.codiansoft.oneandonly.R;
import com.codiansoft.oneandonly.model.CitiesDataModel;

import static com.codiansoft.oneandonly.Level2CitiesActivity.cities;

/**
 * Created by salal-khan on 5/24/2017.
 */

public class ListViewAdapterLevel2Cities extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;

    boolean liked;

    public ListViewAdapterLevel2Cities(Context context) {
        this.context = context;

    }


    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public Object getItem(int position) {
        return cities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ListViewAdapterLevel2Cities.ViewHolder viewHolder;
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.level_two_cities_listview_item, null, true);

            // view lookup cache stored in tag

            viewHolder = new ListViewAdapterLevel2Cities.ViewHolder();

            viewHolder.tvCityName = (TextView) convertView.findViewById(R.id.tvTitle);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }




        final CitiesDataModel cityDataModel = cities.get(position);

        try{
            viewHolder.tvCityName.setText(cityDataModel.getCity());

        } catch (Exception e) {
            e.printStackTrace();
        }




        return convertView;
    }

    private static class ViewHolder {
        TextView tvCityName;
    }

}