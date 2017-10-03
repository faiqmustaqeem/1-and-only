package com.codiansoft.oneandonly.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.codiansoft.oneandonly.R;
import com.codiansoft.oneandonly.model.ImagesModel;

import java.util.List;

import static com.codiansoft.oneandonly.AddItemActivity.progressBar;
import static com.codiansoft.oneandonly.R.color.colorPrimary;

/**
 * Created by salal-khan on 6/1/2017.
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyViewHolder> {

    private List<ImagesModel> images;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRemoveImage;
        public ImageView ivImage;

        public MyViewHolder(View view) {
            super(view);

            tvRemoveImage = (TextView) view.findViewById(R.id.tvRemoveImage);
            ivImage = (ImageView) view.findViewById(R.id.ivImage);
        }
    }


    public ImagesAdapter(List<ImagesModel> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_images, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ImagesModel imagesModel = images.get(position);
        holder.tvRemoveImage.setTextColor(context.getResources().getColor(colorPrimary));

        holder.ivImage.setImageBitmap(imagesModel.getImageBitmap());

        progressBar.setVisibility(View.GONE);
        ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        holder.tvRemoveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                images.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, images.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}