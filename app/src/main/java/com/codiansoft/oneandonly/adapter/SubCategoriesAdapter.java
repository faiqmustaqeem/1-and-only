package com.codiansoft.oneandonly.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.codiansoft.oneandonly.R;

import java.util.ArrayList;

/**
 * Created by salal-khan on 6/22/2017.
 */

public class SubCategoriesAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context mContext;
    ArrayList<String> sub_categories_name = new ArrayList<String>();
    ArrayList<String> sub_categories_last_update_time = new ArrayList<String>();
    ArrayList<String> sub_categories_ads_quantity = new ArrayList<String>();

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView tvLastUpdateTime;
        TextView tvAdsQuantity;
    }

    public SubCategoriesAdapter(Context mContext, ArrayList<String> sub_categories_names, ArrayList<String> sub_categories_last_update_time, ArrayList<String> sub_ategories_ads_quantity) {
        this.mContext = mContext;
        this.sub_categories_name = sub_categories_names;
        this.sub_categories_last_update_time = sub_categories_last_update_time;
        this.sub_categories_ads_quantity = sub_ategories_ads_quantity;
    }

    @Override
    public int getViewTypeCount() {
        if (sub_categories_name.size() == 0) {
            return 1;
        }
        return sub_categories_name.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return sub_categories_name.size();
    }

    @Override
    public Object getItem(int position) {
        return sub_categories_name.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final SubCategoriesAdapter.ViewHolder viewHolder;
        if (inflater == null)
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = inflater.inflate(R.layout.sub_category_listview_item, null, true);

            viewHolder = new SubCategoriesAdapter.ViewHolder();

            viewHolder.txtName = (TextView) view.findViewById(R.id.tvSubCategoryName);
            viewHolder.tvAdsQuantity = (TextView) view.findViewById(R.id.tvQuantity);
            viewHolder.tvLastUpdateTime = (TextView) view.findViewById(R.id.tvLastUpdateTime);

            view.setTag(viewHolder);

            String subCategoryName = sub_categories_name.get(i);
            String sub_ategories_last_update_time = sub_categories_last_update_time.get(i);
            String sub_ategories_ads_quantity = sub_categories_ads_quantity.get(i);

            // Check if an existing view is being reused, otherwise inflate the view
            if (view == null) {

            } else {

            }

            viewHolder.txtName.setText(subCategoryName);
            viewHolder.tvLastUpdateTime.setText(sub_ategories_last_update_time);
            viewHolder.tvAdsQuantity.setText(sub_ategories_ads_quantity);
        }
        return view;
    }
}