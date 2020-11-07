package com.example.tulasi.viewpager1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder>{

    String baseUrl = "http://marvelwallpapers.000webhostapp.com/";
    private final Context context;
    private String character;
    int privousposition =0;
    private  ArrayList<DataModel2> dataModel2s;
    public ImageAdapter(Context context,ArrayList<DataModel2> dataModel2s,String character){
        this.context = context;
        this.character = character;
        this.dataModel2s = dataModel2s;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewlayout2,parent,false);
        MyViewHolder ab = new MyViewHolder(view);
        return ab;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,final int position) {
    DataModel2 product = dataModel2s.get(position);
        String img = product.getImg();
        String url = baseUrl.concat(img);
//        Picasso.get()
//                .load(baseUrl.concat(img))
//                .placeholder(R.drawable.image)
//                .into(holder.imageView);
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.imageView);
       holder.imageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context,Activity3.class);
               intent.putExtra("character",character);
               intent.putExtra("positionId",position);
               context.startActivity(intent);
           }
       });

        if(position > privousposition){
            AnimationUtil.animate(holder,true);
        }else{
            AnimationUtil.animate(holder,false);
        }
        privousposition = position;
    }

    @Override
    public int getItemCount() {
        return dataModel2s.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView =(ImageView)itemView.findViewById(R.id.img2);
        }
    }
}


