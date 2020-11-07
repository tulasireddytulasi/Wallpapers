package com.example.tulasi.viewpager1;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.tulasi.viewpager1.Fragments.Fragment1;
import com.example.tulasi.viewpager1.Fragments.Fragment2;
import com.example.tulasi.viewpager1.Fragments.Fragment3;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Boolean firstTime;
    ViewPager viewPager;
    ValueAnimator valueAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("mypref",MODE_PRIVATE);
        firstTime = sharedPreferences.getBoolean("firstTime",true);

        if(firstTime) {
            // ==============================================================
            SharedPreferences.Editor editor = sharedPreferences.edit();
            firstTime = false;
            editor.putBoolean("firstTime",firstTime);
            editor.apply();
            viewPager = findViewById(R.id.vp1);
            int color1 = ContextCompat.getColor(this, R.color.tulasi);
            int color2 = ContextCompat.getColor(this, R.color.silver);
            int color3 = ContextCompat.getColor(this, R.color.black);

            valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), color1, color2, color3);
            viewPager.setAdapter(new SamplePagerAdapter1(getSupportFragmentManager()));
            valueAnimator.setDuration((3 - 1) * 100000);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    valueAnimator.setCurrentPlayTime((long) ((positionOffset + position) * 100000));

                    Animation bigtosmall;
                    ImageView imageView;
                    imageView = findViewById(R.id.xman);
                    if (position == 0) {
                        bigtosmall = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bigtosmall);
                        imageView.startAnimation(bigtosmall);
                    }
                }

                @Override
                public void onPageSelected(int position) {
                    Animation smalltobig, bigtosmall;
                    ImageView imageView, imageView1, imageView2;
                    imageView1 = (ImageView) findViewById(R.id.shild);
                    imageView2 = (ImageView) findViewById(R.id.nickfury);

                    if (position == 1) {
                        bigtosmall = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bigtosmall);
                        imageView1.startAnimation(bigtosmall);
                    } else if (position == 2) {
                        smalltobig = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.smalltobig);
                        imageView2.startAnimation(smalltobig);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    viewPager.setBackgroundColor((Integer) animation.getAnimatedValue());
                }
            });
            // ==============================================================
        }else {
            Intent intent = new Intent(getApplicationContext(),Home.class);
            startActivity(intent);
            finish();
        }
    }
    private class SamplePagerAdapter1 extends FragmentPagerAdapter {
        public SamplePagerAdapter1(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new Fragment1();
            } else if(position == 1){
                return new Fragment2();
            }else
                return new Fragment3();

        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
