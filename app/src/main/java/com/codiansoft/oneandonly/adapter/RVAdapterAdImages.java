package com.codiansoft.oneandonly.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.codiansoft.oneandonly.R;
import com.codiansoft.oneandonly.SelectedAdPicActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.codiansoft.oneandonly.GlobalClass.selectedAdPic;

/**
 * Created by Codiansoft on 7/21/2017.
 */

public class RVAdapterAdImages extends RecyclerView.Adapter<RVAdapterAdImages.MyViewHolder> {

    private List<String> imagesList;
    private Context c;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivAdImage;

        public MyViewHolder(View view) {
            super(view);
            ivAdImage = (ImageView) view.findViewById(R.id.ivAdImage);
        }
    }

    public RVAdapterAdImages(Context c, List<String> imagesList) {
        this.imagesList = imagesList;
        this.c = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_ad_images_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final String image = imagesList.get(position);
//        Glide.with(c).load(image).into(holder.ivAdImage);
        Picasso.with(c).load(image).into(holder.ivAdImage);

        holder.ivAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(c, ""+image, Toast.LENGTH_SHORT).show();
                selectedAdPic = image;
                Intent i = new Intent(c, SelectedAdPicActivity.class);
                c.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }
}