package com.example.tulasi.viewpager1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;

public class MyAdapter1 extends RecyclerView.Adapter<MyAdapter1.MyViewHolder>  {

    String baseUrl = "http://marvelwallpapers.000webhostapp.com/";
    private final Context mcontext;
    ArrayList<DataModel> dataModels;
    int privousposition = 0;

    public MyAdapter1(Context mcontext, ArrayList<DataModel> dataModels){
        this.mcontext = mcontext;
        this.dataModels = dataModels;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerlayout, parent, false);
        MyViewHolder ab = new MyViewHolder(view);
        return ab;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final DataModel product = dataModels.get(position);
        String img = product.getImgurl();
        String url =  baseUrl.concat(img);
        Glide.with(mcontext)
                .load(url)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.imageView);
        holder.textView.setText(product.getTxt());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,ImagesActivity.class);
                intent.putExtra("character",product.getTxt());
                mcontext.startActivity(intent);
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
        return dataModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView imageView;
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView =(ImageView)itemView.findViewById(R.id.img1);
            textView = (TextView)itemView.findViewById(R.id.title1);
            cardView = (CardView)itemView.findViewById(R.id.card);

        }
    }

}
