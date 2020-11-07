package com.example.tulasi.viewpager1;

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

public class Home extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<DataModel> dataModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (NoConnectionActivity.checkConnection(getApplicationContext())){
            setRecyclerView();
        }else {
            Toast.makeText(getApplicationContext(),"No Connection",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(),NoConnectionActivity.class);
            String activity = "com.example.tulasi.viewpager1.Home";
            intent.putExtra("Activity",activity);
            startActivity(intent);
        }
    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.recyclerview1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        dataModels = new ArrayList<>();
        loadImages();
    }

    private void loadImages() {
        AndroidNetworking.get("http://marvelwallpapers.000webhostapp.com/getallimages.php")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject product = response.getJSONObject(i);
                                dataModels.add(new DataModel(
                                        product.getString( "name" ),
                                        product.getString("category")
                                ));
                            }
                            MyAdapter1   myAdapter1 = new MyAdapter1(Home.this,dataModels);

                            recyclerView.setAdapter(myAdapter1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(Home.this, "No Data Please Check The Code", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),NoConnectionActivity.class);
                        String activity = "com.example.tulasi.viewpager1.Home";
                        intent.putExtra("Activity",activity);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
