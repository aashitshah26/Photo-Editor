package com.example.editor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class imageadapter extends RecyclerView.Adapter<imageadapter.MyViewHolder> {
   private int mThumbnails[];
   private Context context;
   int type;
   private ArrayList<String> paths = new ArrayList<>();

    public imageadapter(int abc[], Context context) {
        mThumbnails = abc;
        this.context=context;
        this.type=1;
    }

    public imageadapter(ArrayList img, Context context)
    {
        paths=img;
        this.context=context;
        this.type=2;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            if(type==1)
            image = (ImageView) view.findViewById(R.id.image);
            if(type==2)
                image = (ImageView) view.findViewById(R.id.img);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = null;
        if (type == 1) {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.images, viewGroup, false);
           
        } 
        if (type==2)
        {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.mycreation, viewGroup, false);
           
        }
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if (type==1) {
            Glide.with(context)
                    .load(mThumbnails[i])
                    .into(myViewHolder.image);
        }
        if (type==2){
            Glide.with(context)
                    .load(paths.get(i))
                    .into(myViewHolder.image);
        }
        }

    @Override
    public int getItemCount() {
        int length = 0;
        if (type==1)
        length = mThumbnails.length;
        if (type==2)
            length = paths.size();
        return length;
    }



}
