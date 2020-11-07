package com.example.tulasi.viewpager1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

public class NoConnectionActivity extends AppCompatActivity {

    ImageView gif;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);
        gif = findViewById(R.id.gif);
        button = findViewById(R.id.tryagain);
        Glide.with(getApplicationContext())
                .load(R.drawable.nonet)
                .into(gif);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection(getApplicationContext())){
                    String activityclass = getIntent().getStringExtra("Activity");
                    Intent intent = null;
                    try {
                        intent = new Intent(getApplicationContext(), Class.forName(activityclass));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);

                }else {
                    Toast.makeText(getApplicationContext(),"No Connection",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static boolean checkConnection(Context context) {
        if (isNetworkAvailable(context)) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean isNetworkAvailable(Context ct) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ct.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getAllNetworkInfo();
        }
        return activeNetworkInfo != null;
    }
//    public static Boolean isOnline() {
//        try {
//            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
//            int returnVal = p1.waitFor();
//            boolean reachable = (returnVal==0);
//            return reachable;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
}
