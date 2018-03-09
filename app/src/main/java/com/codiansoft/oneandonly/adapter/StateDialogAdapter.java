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
import com.codiansoft.oneandonly.model.CountryModel;
import com.codiansoft.oneandonly.model.StateModel;

import java.util.List;

/**
 * Created by Codiansoft on 3/8/2018.
 */

public class StateDialogAdapter extends RecyclerView.Adapter<StateDialogAdapter.MyViewHolder> {

    List<StateModel> list;
    public StateDialogAdapter(List<StateModel> list , Context context)
    {
        this.list=list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.state_row_dialog, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final StateModel model=list.get(position);

        holder.state_name.setText(model.getName());
        holder.select_full.setChecked(model.isAllSelected());
        holder.filter.setChecked(model.isFilterSelected());


        holder.filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    model.setFilterSelected(true);
                    holder.filter.setChecked(true);
                }
                else {
                    model.setFilterSelected(false);
                    holder.filter.setChecked(false);
                }
            }
        });

        holder.select_full.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    model.setAllSelected(true);
                    holder.select_full.setChecked(true);
                }
                else {
                    model.setAllSelected(false);
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
        RadioButton filter;
        public MyViewHolder(View itemView) {
            super(itemView);

            state_name= (TextView) itemView.findViewById(R.id.state_name);
            select_full=(RadioButton)itemView.findViewById(R.id.select_full);
            filter=(RadioButton)itemView.findViewById(R.id.filter);

        }
    }
}
