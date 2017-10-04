package com.codiansoft.oneandonly.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.codiansoft.oneandonly.R;
import com.codiansoft.oneandonly.model.StatesDataModel;

import static com.codiansoft.oneandonly.Level2StatesActivity.states;

/**
 * Created by salal-khan on 7/14/2017.
 */

public class ListViewAdapterLevel2States extends BaseAdapter {


    private LayoutInflater inflater;
    private Context context;

    boolean liked;

    public ListViewAdapterLevel2States(Context context) {
        this.context = context;

    }


    @Override
    public int getCount() {
        return states.size();
    }

    @Override
    public Object getItem(int position) {
        return states.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ListViewAdapterLevel2States.ViewHolder viewHolder;
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.level_two_states_listview_item, null, true);

            // view lookup cache stored in tag

            viewHolder = new ListViewAdapterLevel2States.ViewHolder();

            viewHolder.tvCityName = (TextView) convertView.findViewById(R.id.tvTitle);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ListViewAdapterLevel2States.ViewHolder) convertView.getTag();
        }




        final StatesDataModel cityDataModel = states.get(position);

        try{
            viewHolder.tvCityName.setText(cityDataModel.getState());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView tvCityName;
    }
}
