package com.dgu.table.univ.univtable;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class IntroActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;
    private Handler h;
    private int delayTime = 1000;
    private ImageView iv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        pref = getSharedPreferences("Univtable", MODE_PRIVATE);
        prefEditor = pref.edit();

        iv = (ImageView)findViewById(R.id.imageView);
        iv.setDrawingCacheEnabled(true);

        AnimationSet animset = new AnimationSet(false);
        Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_anim);
        animset.addAnimation(anim2);
        iv.startAnimation(animset);
        h = new Handler();

        animset.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                h.postDelayed(intro, delayTime);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }

    private Runnable intro = new Runnable() {
        public void run() {
            if(pref.getBoolean("auto", false)){
                Intent i = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }else{
                Intent i = new Intent(IntroActivity.this, PrevActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        h.removeCallbacks(intro);
    }
}
