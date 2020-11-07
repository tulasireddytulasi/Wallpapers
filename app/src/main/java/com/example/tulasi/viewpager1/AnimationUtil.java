package com.example.tulasi.viewpager1;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;

public class AnimationUtil {
    public static void animate(RecyclerView.ViewHolder viewHolder, boolean goes){

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(viewHolder.itemView,"translationY",goes ? 200 : -200,0);
        objectAnimator.setDuration(1000);
        animatorSet.playTogether(objectAnimator);
        animatorSet.start();
    }
}
