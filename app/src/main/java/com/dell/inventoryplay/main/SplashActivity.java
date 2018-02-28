package com.dell.inventoryplay.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.utils.Helper;
import com.dell.inventoryplay.utils.PrefManager;


public class SplashActivity extends AppCompatActivity {

    private static int cnt = 0;
    // String[] textArr = {"I", "N", "V", "E", "N", "T", "O", "R", "Y", " ", "P", "L", "A", "Y"};
    String[] textArr = {"D", "E", "L", "L"};
    TextView tv;
    String tmpTitle = "";
    PrefManager mPref;

    //char[] myTitle="Inventory PLAY".toCharArray();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Hiding Title bar of this activity screen */
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        /*Making this activity, full screen */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mPref = PrefManager.getInstance(this);
        mPref.load();
        tv = findViewById(R.id.title);

        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.grow);
        findViewById(R.id.launcher).startAnimation(anim);
        final Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.grow1);

        tv.startAnimation(anim1);
        if (mPref.isFirstUse()) {
            mPref.setFirstUse(false);
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

    public void doAnim(TextView centerSplash) {
        Animation centerSplashAnim = AnimationUtils.loadAnimation(this, R.anim.growshrink);
        tmpTitle = tmpTitle + textArr[cnt];
        centerSplash.setText(textArr[cnt]);
        centerSplash.setTextSize(Helper.getInstance(SplashActivity.this).dpToPx(40));
        centerSplash.startAnimation(centerSplashAnim);

        centerSplashAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                centerSplash.setTextSize(Helper.getInstance(SplashActivity.this).dpToPx(10));
                if (cnt == textArr.length) {
                    centerSplash.setText(tmpTitle);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        cnt++;
    }
}
