package com.codiansoft.oneandonly.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.codiansoft.oneandonly.R;
import com.codiansoft.oneandonly.model.CityModel;

import java.util.List;

/**
 * Created by Codiansoft on 3/9/2018.
 */

public class CityDialogAdapter extends RecyclerView.Adapter<CityDialogAdapter.MyViewHolder> {

    List<CityModel> list;
   public CityDialogAdapter(List<CityModel> list , Context context)
   {
       this.list=list;
   }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_dialog_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CityModel model=list.get(position);

        holder.state_name.setText(model.getName());
        holder.select_full.setChecked(model.isSelected());


        holder.select_full.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    model.setSelected(true);
                    holder.select_full.setChecked(true);
                }
                else {
                    model.setSelected(false);
                    holder.select_full.setChecked(false);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView state_name;
        RadioButton select_full;

        public MyViewHolder(View itemView) {
            super(itemView);

            state_name= (TextView) itemView.findViewById(R.id.city_name);
            select_full=(RadioButton)itemView.findViewById(R.id.select);


        }
    }

}
