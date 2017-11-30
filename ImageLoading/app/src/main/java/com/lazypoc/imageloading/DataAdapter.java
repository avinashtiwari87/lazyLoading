package com.lazypoc.imageloading;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lazypoc.imageloading.modle.ImageDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<ImageDetails> imageDetailses;
    private Context context;

    public DataAdapter(Context context, ArrayList<ImageDetails> imageDetailses) {
        this.context = context;
        this.imageDetailses = imageDetailses;

    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.imageTitle.setText(imageDetailses.get(i).getTitle());
        viewHolder.imageDescription.setText(imageDetailses.get(i).getDescription());
        Picasso.with(context).load(imageDetailses.get(i).getImagePath()).into(viewHolder.img_android);
    }

    @Override
    public int getItemCount() {
        return imageDetailses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView imageTitle, imageDescription;
        ImageView img_android;

        public ViewHolder(View view) {
            super(view);

            imageTitle = (TextView) view.findViewById(R.id.image_title);
            imageDescription = (TextView) view.findViewById(R.id.image_description);
            img_android = (ImageView) view.findViewById(R.id.img_android);
        }
    }
}