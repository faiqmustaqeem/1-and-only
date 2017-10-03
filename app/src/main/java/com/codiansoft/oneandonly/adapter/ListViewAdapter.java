package com.codiansoft.oneandonly.adapter;

/**
 * Created by salal-khan on 4/25/2017.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.codiansoft.oneandonly.GlobalClass;
import com.codiansoft.oneandonly.R;
import com.codiansoft.oneandonly.model.PropertyListItemDataModel;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.codiansoft.oneandonly.AdListViewActivity.dataModels;
import static com.codiansoft.oneandonly.AdListViewActivity.progressBar;

public class ListViewAdapter extends BaseSwipeAdapter {

    private ArrayList<PropertyListItemDataModel> adsDataModels = new ArrayList<PropertyListItemDataModel>();

    ListViewAdapter adapter;

    private Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtDescription;
        TextView txtLastUpdateTime;
        TextView tvPrice;
        TextView tvCurrencyCode;
        TextView txtID;
        TextView t;
        ImageView ivAdPic;
        Button delete;
        SwipeLayout swipeLayout;
    }

    public ListViewAdapter(Context mContext, ArrayList<PropertyListItemDataModel> adsDataModels) {
        this.mContext = mContext;
        this.adsDataModels = adsDataModels;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null);
        final SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));

        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.delete));
            }
        });

        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private void removeAdForUser(final SwipeLayout swipeLayout, final int positionToRemove) {

        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(mContext);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.REMOVE_AD_FOR_USER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                adsDataModels.remove(positionToRemove);
                                notifyDataSetChanged();
                                swipeLayout.close();

                            } else if (result.get("status").equals("error")) {
                                Toast.makeText(mContext, "Recheck and try again", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception ee) {
                            Toast.makeText(mContext, "error: " + ee.toString(), Toast.LENGTH_SHORT).show();

                        }

                        if (progressBar.isShown()) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 409:
//                                    utilities.dialog("Already Exist", act);
                                    break;
                                case 400:
                                    Toast.makeText(mContext, "Try again", Toast.LENGTH_SHORT).show();
//                                    utilities.dialog("Connection Problem", act);
                                    break;
                                default:
//                                    utilities.dialog("Connection Problem", act);
                                    break;
                            }
                        }
                        if (progressBar.isShown()) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                SharedPreferences settings = mContext.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String userID = settings.getString("userID", "defaultValue");
                String apiSecretKey = settings.getString("apiSecretKey", "");
                String email = settings.getString("email", "");
                String contactNum1 = settings.getString("contactNum1", "");
                String contactNum2 = settings.getString("contactNum2", "");

                Map<String, String> params = new HashMap<String, String>();
                params.put("api_secret", apiSecretKey);

                PropertyListItemDataModel selectedItemDataModel = dataModels.get(positionToRemove);
                params.put("ad_id", selectedItemDataModel.getID());

                return params;
            }
        };
        queue.add(postRequest);
    }

    @Override
    public void fillValues(final int position, View convertView) {

        final ListViewAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        viewHolder = new ListViewAdapter.ViewHolder();

        viewHolder.swipeLayout = (SwipeLayout) convertView.findViewById(getSwipeLayoutResourceId(position));

        viewHolder.delete = (Button) convertView.findViewById(R.id.delete);
        viewHolder.txtName = (TextView) convertView.findViewById(R.id.tvTitle);
        viewHolder.txtDescription = (TextView) convertView.findViewById(R.id.tvDescription);
        viewHolder.txtLastUpdateTime = (TextView) convertView.findViewById(R.id.tvLastUpdateTime);
        viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
        viewHolder.tvCurrencyCode = (TextView) convertView.findViewById(R.id.tvCurrencyCode);
        viewHolder.txtID = (TextView) convertView.findViewById(R.id.tvID);
        viewHolder.t = (TextView) convertView.findViewById(R.id.position);
        viewHolder.ivAdPic = (ImageView) convertView.findViewById(R.id.ivAdImage);

        convertView.setTag(viewHolder);

//        PropertyListItemDataModel dataModel = getItem(position);
        PropertyListItemDataModel dataModel = adsDataModels.get(position);
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
        viewHolder.txtDescription.setText(dataModel.getDetails());
        viewHolder.txtLastUpdateTime.setText(dataModel.getLastUpdateTime());
        viewHolder.tvPrice.setText(dataModel.getPrice());
        viewHolder.tvCurrencyCode.setText(dataModel.getCurrencyCode());
        viewHolder.txtID.setText(dataModel.getID());
//        Glide.with(mContext).load(dataModel.getImageURL()).into(viewHolder.ivAdPic);
        try {
            Glide.with(mContext).load(dataModel.getAdImages().get(0)).into(viewHolder.ivAdPic);
        }catch (IndexOutOfBoundsException i){

        }


        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
//                Toast.makeText(mContext, "click delete", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
                adb.setTitle("Are You Sure?");
                adb.setMessage("You will not see this item again:\n" + adsDataModels.get(position).getName());
                final int positionToRemove = position;
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

//                        Toast.makeText(mContext, positionToRemove+" "+dataModels.get(positionToRemove).getID(), Toast.LENGTH_SHORT).show();
                        removeAdForUser(viewHolder.swipeLayout, positionToRemove);
/*

                        adsDataModels.remove(positionToRemove);
                        notifyDataSetChanged();
                        swipeLayout.close();
*/

                    }
                });
                adb.show();
            }
        });


//        viewHolder.t.setText((position + 1) + ".");

        // Return the completed view to render on screen
    }

    @Override
    public int getCount() {
        return adsDataModels.size();
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