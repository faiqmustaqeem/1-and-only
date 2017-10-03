package com.codiansoft.oneandonly.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codiansoft.oneandonly.R;
import com.codiansoft.oneandonly.model.AttributeKeysModel;
import com.codiansoft.oneandonly.model.AttributeValuesModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by salal-khan on 6/5/2017.
 */

public class ItemAttributeAdapter extends RecyclerView.Adapter<ItemAttributeAdapter.MyViewHolder> {

    private List<AttributeKeysModel> attributeKeys = new ArrayList<>();
    private List<AttributeValuesModel> attributeValues = new ArrayList<>();
    Context context;
    AttributeKeysModel attributeKeysModel;
    AttributeValuesModel attributeValuesModel;

    private String[] keys;
    private String[] values;

    public ItemAttributeAdapter(List<AttributeKeysModel> key, List<AttributeValuesModel> value, Context context) {
        this.attributeKeys = key;
        this.attributeValues = value;
        this.context = context;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView etKey, etValue;

        public MyViewHolder(View view) {
            super(view);

            etKey = (TextView) view.findViewById(R.id.etKey);
            etValue = (TextView) view.findViewById(R.id.etValue);
        }
    }


    // we make TextWatcher to be aware of the position it currently works with
    // this way, once a new item is attached in onBindViewHolder, it will
    // update current position MyCustomEditTextListener, reference to which is kept by ViewHolder
/*
    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            keys[position] = charSequence.toString();
            values[position] = charSequence.toString();
            attributeKeys.get(position);

        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
*/



/*
    public void addRow(String key, String value) {
        attributeKeysModel = new AttributeKeysModel(key);
        attributeValuesModel = new AttributeValuesModel(value);
        attributeKeys.add(attributeKeysModel);
        attributeValues.add(attributeValuesModel);
        notifyItemInserted(attributeKeys.size() - 1);
        notifyDataSetChanged();
    }*/

    @Override
    public ItemAttributeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attributes, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        attributeKeysModel = new AttributeKeysModel(holder.etKey.getText().toString());
        attributeValuesModel = new AttributeValuesModel(holder.etValue.getText().toString());
        /*attributeKeys.add(attributeKeysModel);
        attributeValues.add(attributeValuesModel);*/

        AttributeKeysModel keysModel = attributeKeys.get(position);
        AttributeValuesModel valuesModel = attributeValues.get(position);

        keysModel.setAttributeKey(holder.etKey.getText().toString());
        valuesModel.setAttributeKey(holder.etValue.getText().toString());

        holder.etKey.setText(keysModel.getAttributeKey());
        holder.etKey.setTag(position);

        holder.etValue.setText(valuesModel.getAttributeValue());
        holder.etValue.setTag(position);

    }

    @Override
    public int getItemCount() {
        return attributeKeys.size();
    }
}
