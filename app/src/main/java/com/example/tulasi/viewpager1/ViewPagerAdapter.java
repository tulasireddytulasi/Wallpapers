package com.example.tulasi.viewpager1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    private final Context context;
    private ArrayList<DataModel2> datamodels;

    ViewPagerAdapter(Context context, ArrayList<DataModel2> datamodels ){
        this.context = context;
        this.datamodels = datamodels;
    }

    @Override
    public int getCount() {
        return datamodels.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        final DataModel2 product = datamodels.get(position);
        ImageView imageView = new ImageView(context);
        imageView.setId(R.id.closeId);
        String baseurl = "http://marvelwallpapers.000webhostapp.com/";
        String img = product.getImg();
        String url = baseurl.concat(img);
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .fitCenter()
                        .centerCrop()
                        .placeholder(R.drawable.image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(imageView);
        container.addView(imageView);
        return  imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       container.removeView((View) object);
    }
}
