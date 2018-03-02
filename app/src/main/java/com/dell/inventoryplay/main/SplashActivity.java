package com.dell.inventoryplay.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.utils.PrefManager;


public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        PrefManager mPref = PrefManager.getInstance(this);
        mPref.load();
        TextView tv = findViewById(R.id.title);
        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.grow);
        findViewById(R.id.launcher).startAnimation(anim);
        final Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.grow1);

        tv.startAnimation(anim1);
        if (mPref.isFirstUse()) {
            mPref.setFirstUse();
            long SPLASH_TIME_OUT = 1000;
            new Handler().postDelayed(() -> {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }, SPLASH_TIME_OUT);
        } else {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}
