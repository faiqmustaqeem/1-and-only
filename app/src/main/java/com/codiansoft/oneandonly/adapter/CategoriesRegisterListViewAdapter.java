package com.codiansoft.oneandonly.adapter;

/**
 * Created by salal-khan on 4/26/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.codiansoft.oneandonly.GlobalClass;
import com.codiansoft.oneandonly.R;
import com.codiansoft.oneandonly.model.SetCategoriesStatusDataModel;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;


public class CategoriesRegisterListViewAdapter extends BaseAdapter {

    ListViewAdapter adapter;
    private LayoutInflater inflater;

    public static String ids = "";
    public static String statuses = "";
    private Context mContext;
    public static ArrayList<SetCategoriesStatusDataModel> savedCategoriesDataModels;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtLastUpdateTime;
        TextView txtID;
        TextView txtQuantity;
        TextView t;
        SwitchButton sbOnOrOff;
    }

    public CategoriesRegisterListViewAdapter(Context mContext, ArrayList<SetCategoriesStatusDataModel> dataModels) {
        this.mContext = mContext;
        this.savedCategoriesDataModels = dataModels;
    }

    @Override
    public int getViewTypeCount() {
        if (savedCategoriesDataModels.size() == 0) {
            return 1;
        }
        return savedCategoriesDataModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return savedCategoriesDataModels.size();
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


        final CategoriesRegisterListViewAdapter.ViewHolder viewHolder;
        if (inflater == null)
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {

            view = inflater.inflate(R.layout.categories_register_listview_item, null, true);

            viewHolder = new CategoriesRegisterListViewAdapter.ViewHolder();

            viewHolder.txtName = (TextView) view.findViewById(R.id.tvTitle);
            viewHolder.txtLastUpdateTime = (TextView) view.findViewById(R.id.tvLastUpdateTime);
            viewHolder.txtID = (TextView) view.findViewById(R.id.tvID);
            viewHolder.txtQuantity = (TextView) view.findViewById(R.id.tvQuantity);
            viewHolder.t = (TextView) view.findViewById(R.id.position);
            viewHolder.sbOnOrOff = (SwitchButton) view.findViewById(R.id.sbOnOrOff);


            view.setTag(viewHolder);

//        PropertyListItemDataModel dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view

            if (view == null) {

            /*viewHolder = new ListViewAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.properties_item_view, (ViewGroup) convertView.getParent(), false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.txtDescription = (TextView) convertView.findViewById(R.id.tvAddress);
            viewHolder.txtLastUpdateTime = (TextView) convertView.findViewById(R.id.tvLastUpdateTime);
            viewHolder.txtID = (TextView) convertView.findViewById(R.id.tvID);

            result = convertView;

            convertView.setTag(viewHolder);*/
            } else {
            /*viewHolder = (ListViewAdapter.ViewHolder) convertView.getTag();
            result = convertView;*/
            }

            final SetCategoriesStatusDataModel dataModel = savedCategoriesDataModels.get(i);

            if (dataModel.getStatus().equals("1")) {
                viewHolder.sbOnOrOff.setChecked(true);
            } else {
                viewHolder.sbOnOrOff.setChecked(false);
            }

            viewHolder.txtName.setText(dataModel.getCategoryName());
            viewHolder.txtLastUpdateTime.setText(dataModel.getLastUpdateTime());
            viewHolder.txtID.setText(dataModel.getID());
            viewHolder.txtQuantity.setText(dataModel.getQuantity());

            if (dataModel.getStatus().equals("1")) {
                viewHolder.sbOnOrOff.setChecked(true);
            } else if (dataModel.getStatus().equals("0")) {
                viewHolder.sbOnOrOff.setChecked(false);
            }
            /*ids = ids+dataModel.getID()+",";
            if(viewHolder.sbOnOrOff.isChecked()){
                statuses = statuses+"1"+",";
            }else{
                statuses = statuses+"0"+",";
            }*/

            viewHolder.sbOnOrOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    Toast.makeText(mContext, i + " " + b, Toast.LENGTH_SHORT).show();

                    //block adults category for saudi arabia
                    if (GlobalClass.userCountryID.equals("191") & i == 6) {
                        viewHolder.sbOnOrOff.setChecked(false);
                        Toast.makeText(mContext, "Not Allowed", Toast.LENGTH_SHORT).show();
                    } else {
                        if (b) {
                            dataModel.setStatus("1");
                        } else {
                            dataModel.setStatus("0");
                        }
                        notifyDataSetChanged();
                    }
                }
            });
        }
        return view;
    }
}