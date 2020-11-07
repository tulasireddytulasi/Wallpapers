package com.example.tulasi.viewpager1;

import android.Manifest;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class Activity3 extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    ViewPager viewPager;
    ArrayList<DataModel2> data;
    String message;
    TextView textView;
    int value, no;
    boolean isFABOpen;
    String ironman;
    FloatingActionButton fab1, fab2, fab3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        textView = findViewById(R.id.urltext);
        permissioncheck();
        viewPager = findViewById(R.id.viewpager1);
        data = new ArrayList<>();
        no = getIntent().getIntExtra("positionId", 0);
        ironman = getIntent().getStringExtra("character");
        loadImages();
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                value = position;
                closeFABMenu();
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try {
                   downloadImages();
               }catch (Exception e){
                   e.printStackTrace();
                   Toast.makeText(getApplicationContext(),"Download Canceled",Toast.LENGTH_SHORT).show();
               }
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    setWallpaper();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"No Wallpaper to set",Toast.LENGTH_SHORT).show();
                }
            }
        });
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();
            }
        });
    }


    private void permissioncheck() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                //  return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void setWallpaper() {
        DataModel2 num = data.get(value);
        String baseurl = "http://marvelwallpapers.000webhostapp.com/";
        String imgname = num.getImg();
        message = baseurl.concat(imgname);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        WallpaperManager wpm = WallpaperManager.getInstance(getApplicationContext());
        InputStream ins; // = null;
        if(message != null){
            try {
                ins = new URL(message).openStream();
                wpm.setStream(ins);
                Toast.makeText(getApplicationContext(), "Wallpaper Seted", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(getApplicationContext(), "No Data Received To Set Wallpaper", Toast.LENGTH_LONG).show();
        }
    }

    private void shareImage() {
        try {
            DataModel2 num = data.get(value);
            String baseurl = "http://marvelwallpapers.000webhostapp.com/";
            String imgname = num.getImg();
            String uri = baseurl.concat(imgname);
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(uri)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            String path = MediaStore.Images.Media.insertImage(getContentResolver(), resource, "SomeText", null);
                            Log.d("Path", path);
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, "Hey view/download this image");
                            Uri screenshotUri = Uri.parse(path);
                            intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                            intent.setType("image/*");
                            startActivity(Intent.createChooser(intent, "Share image via..."));
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
           Toast.makeText(getApplicationContext(),"Can't Share Image",Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadImages() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        } else {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                downloadmanager();
            } else {
                downloadimagefile();
            }
        }
    }

    private void downloadmanager() {
        DataModel2 num = data.get(value);
        String baseurl = "http://marvelwallpapers.000webhostapp.com/";
        String imgname = num.getImg();
        String uri = baseurl.concat(imgname);
        if(uri != null){
            final String filename = uri.substring(52);
            File dirpath = new File(Environment.getExternalStorageDirectory()
                    + "/Image/".concat(ironman));
            if (!dirpath.exists()) {
                dirpath.mkdirs();
            }
            DownloadManager downloadManager;
            downloadManager = (DownloadManager) getSystemService(getApplicationContext().DOWNLOAD_SERVICE);
            Uri urls = Uri.parse(uri);
            DownloadManager.Request request = new DownloadManager.Request(urls);
            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false).setTitle("Demo")
                    .setDescription("Something useful. No, really.")  // "ImageName.jpg"
                    .setDestinationInExternalPublicDir(dirpath.toString(), filename)
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            downloadManager.enqueue(request);
        }else {
            Toast.makeText(getApplicationContext(),"No Data Received To Download Image",Toast.LENGTH_LONG).show();
        }
    }

    private void downloadimagefile() {
        try {
            DataModel2 num = data.get(value);
            String baseurl = "http://marvelwallpapers.000webhostapp.com/";
            String imgname = num.getImg();
            String uri = baseurl.concat(imgname);
            final String filename = uri.substring(52);
            File dirpath = new File(Environment.getExternalStorageDirectory()
                    + "/Image/".concat(ironman));
            if (!dirpath.exists()) {
                dirpath.mkdirs();
            }
            final File file = new File(dirpath, filename);
            if(uri != null){
                AndroidNetworking.download(uri, dirpath.toString(), filename)
                        .setTag("downloadTest")
                        .setPriority(Priority.HIGH)
                        .build()
                        .setDownloadProgressListener(new DownloadProgressListener() {
                            @Override
                            public void onProgress(long bytesDownloaded, long totalBytes) {
                                // do anything with progress
                            }
                        })
                        .startDownload(new DownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                Toast.makeText(getApplicationContext(), filename + " Downloaded", Toast.LENGTH_SHORT).show();
                                Log.d("Tag", "Scan finished. You can view the image in the DDDDDDD now.");
                                try {
                                    MediaScannerConnection.scanFile(getApplicationContext(),
                                            new String[]{file.toString()}, null,
                                            new MediaScannerConnection.OnScanCompletedListener() {
                                                public void onScanCompleted(String path, Uri uri) {
                                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                                    Log.i("ExternalStorage", "-> uri=" + uri);
                                                }
                                            });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError error) {
                                // handle error
                                Toast.makeText(getApplicationContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                            }
                        }); // === end download ===
            }else {
                Toast.makeText(getApplicationContext(),"No Data Received To Download Image",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject product = response.getJSONObject(i);
                                data.add(new DataModel2(
                                        product.getString("name")

                                ));
                            }
                            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(Activity3.this, data);
                            viewPager.setAdapter(viewPagerAdapter);
                            viewPager.setCurrentItem(no);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(Activity3.this, "No Data Please Check The Code", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), NoConnectionActivity.class);
                        String activity = "com.example.tulasi.viewpager1.Activity3";
                        intent.putExtra("Activity",activity);
                        startActivity(intent);
                    }
                });
    }

    private void showFABMenu() {
        isFABOpen = true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_185));
    }

    private void closeFABMenu() {
        isFABOpen = false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        fab3.animate().translationY(0);
    }
}
