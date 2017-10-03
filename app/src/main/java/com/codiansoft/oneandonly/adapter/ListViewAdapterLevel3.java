package com.codiansoft.oneandonly.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codiansoft.oneandonly.GlobalClass;
import com.codiansoft.oneandonly.R;
import com.codiansoft.oneandonly.model.Level3ListItemDataModel;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import static com.codiansoft.oneandonly.Level3Activity.dataModels;

/**
 * Created by salal-khan on 4/27/2017.
 */

public class ListViewAdapterLevel3 extends BaseSwipeAdapter {

    ListViewAdapter adapter;

    private Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtDescription;
        TextView txtLastUpdateTime;
        TextView txtID;
        TextView t;
        ImageView ivItemImage;
    }

    public ListViewAdapterLevel3(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.level_three_listview_item, null);
        final SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
/*
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
*/
            }
        });
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
//                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Toast.makeText(mContext, "click delete", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
                adb.setTitle("Are You Sure?");
                adb.setMessage("You will not see this item again:\n" + dataModels.get(position).getName());
                final int positionToRemove = position;
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dataModels.remove(positionToRemove);
                        notifyDataSetChanged();
                        swipeLayout.close();
                    }
                });
                adb.show();
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {

        ListViewAdapterLevel3.ViewHolder viewHolder; // view lookup cache stored in tag

        viewHolder = new ListViewAdapterLevel3.ViewHolder();

        viewHolder.txtName = (TextView) convertView.findViewById(R.id.tvName);
        viewHolder.txtDescription = (TextView) convertView.findViewById(R.id.tvDescription);
        viewHolder.txtLastUpdateTime = (TextView) convertView.findViewById(R.id.tvCost);
        viewHolder.txtID = (TextView) convertView.findViewById(R.id.tvID);
        viewHolder.t = (TextView) convertView.findViewById(R.id.position);
        viewHolder.ivItemImage = (ImageView) convertView.findViewById(R.id.ivItemImage);

        convertView.setTag(viewHolder);


//        PropertyListItemDataModel dataModel = getItem(position);
        Level3ListItemDataModel dataModel = dataModels.get(position);
        // Check if an existing view is being reused, otherwise inflate the view


        if (convertView == null) {

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

        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtDescription.setText(dataModel.getDescription());
        viewHolder.txtLastUpdateTime.setText(dataModel.getCost());
        viewHolder.txtID.setText(dataModel.getID());
        Glide.with(mContext).load(dataModel.getItemImageURL()).into(viewHolder.ivItemImage);

//        viewHolder.t.setText((position + 1) + ".");

        // Return the completed view to render on screen
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
}