package com.codiansoft.oneandonly.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codiansoft.oneandonly.GlobalClass;
import com.codiansoft.oneandonly.Level2StatesActivity;
import com.codiansoft.oneandonly.R;
import com.codiansoft.oneandonly.model.CountriesDataModel;
import com.kyleduo.switchbutton.SwitchButton;

import static com.codiansoft.oneandonly.Level1CountriesActivity.dataModels;
import static com.codiansoft.oneandonly.R.id.clCountry;

/**
 * Created by salal-khan on 4/26/2017.
 */

public class ListViewAdapterRegisterCountries extends BaseAdapter {

    ListViewAdapter adapter;
    private LayoutInflater inflater;

    private Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtCountryName;
        TextView txtCity;
        TextView txtLastUpdateTime;
        TextView txtID;
        TextView txtQuantity;
        TextView t;
        ImageView ivFlag;
        ConstraintLayout clCountry;
        SwitchButton sbOnOff;
    }

    public ListViewAdapterRegisterCountries(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return dataModels.size();
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

        final ListViewAdapterRegisterCountries.ViewHolder viewHolder;
        if (inflater == null)
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.level_one_listview_item, null, true);

        viewHolder = new ListViewAdapterRegisterCountries.ViewHolder();

        viewHolder.txtCountryName = (TextView) view.findViewById(R.id.tvCountryName);
        viewHolder.txtCity = (TextView) view.findViewById(R.id.tvCityName);
        viewHolder.txtLastUpdateTime = (TextView) view.findViewById(R.id.tvLastUpdateTime);
        viewHolder.txtID = (TextView) view.findViewById(R.id.tvID);
        viewHolder.txtQuantity = (TextView) view.findViewById(R.id.tvQuantity);
        viewHolder.t = (TextView) view.findViewById(R.id.position);
        viewHolder.ivFlag = (ImageView) view.findViewById(R.id.ivFlag);
        viewHolder.clCountry = (ConstraintLayout) view.findViewById(clCountry);
        viewHolder.sbOnOff = (SwitchButton) view.findViewById(R.id.sbOnOrOff);
        if (GlobalClass.postingAd) viewHolder.sbOnOff.setVisibility(View.GONE);

        view.setTag(viewHolder);

//        PropertyListItemDataModel dataModel = getItem(position);
        final CountriesDataModel dataModel = dataModels.get(i);

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
        if (!GlobalClass.postingAd) viewHolder.sbOnOff.setVisibility(View.GONE);

        viewHolder.ivFlag.setImageResource(mContext.getResources().getIdentifier("drawable/" + dataModel.getStateCode(), null, mContext.getPackageName()));

        if (GlobalClass.postingAd) {
            viewHolder.sbOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b == true) {
                        GlobalClass.selectedAddPostCountry = dataModel.getID();

                        for (int j = 0; j < dataModels.size(); j++) {
                            if (i != j) {
                                dataModels.get(j).setOnOff(false);
                                notifyDataSetChanged();
                            } else {
                                dataModels.get(j).setOnOff(true);
                                viewHolder.sbOnOff.setChecked(true);
                            }
                        }
                        Toast.makeText(mContext, dataModel.getCountry(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (GlobalClass.postingAd) {
            viewHolder.txtCountryName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewHolder.sbOnOff.isChecked()) {
                        Intent i = new Intent(mContext, Level2StatesActivity.class);
                        mContext.startActivity(i);
                        GlobalClass.selectedLevel2Country = viewHolder.txtCountryName.getText().toString();
                        GlobalClass.selectedLevel2CountryID = dataModel.getID();
                    }
                }
            });
        }

        return view;
    }
}