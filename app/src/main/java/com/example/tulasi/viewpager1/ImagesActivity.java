package com.example.tulasi.viewpager1;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ImagesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<DataModel2> imgdata;
    String ironman ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        recyclerView = findViewById(R.id.recyclerview2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        imgdata = new ArrayList<>();
        ironman = getIntent().getStringExtra("character");
        loadImages();
    }

    private void loadImages() {

        AndroidNetworking.get("https://marvelwallpapers.000webhostapp.com/getimages.php?category={ironman}")
                .addPathParameter("ironman", ironman)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                      if(response != null){
                          try {
                              for (int i = 0; i < response.length(); i++) {
                                  JSONObject product = response.getJSONObject(i);
                                  imgdata.add(new DataModel2(
                                          product.getString( "name" )
                                  ));
                              }
                              ImageAdapter   myAdapter1 = new ImageAdapter( ImagesActivity.this,imgdata, ironman);
                              recyclerView.setAdapter(myAdapter1);
                          } catch (JSONException e) {
                              e.printStackTrace();
                          }
                      }else {
                          Intent intent = new Intent(getApplicationContext(),NoConnectionActivity.class);
                          startActivity(intent);
                      }
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(ImagesActivity.this, error + "No Data Please Check The Code", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), NoConnectionActivity.class);
                        String activity = "com.example.tulasi.viewpager1.ImagesActivity";
                        intent.putExtra("Activity",activity);
                        startActivity(intent);
                    }
                });
    }
}
