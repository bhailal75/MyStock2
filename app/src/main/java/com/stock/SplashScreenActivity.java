package com.stock;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stock.baseclass.BaseAppCompactActivity;
import com.stock.utility.MyPref;
import com.stock.utility.Utility;

public class SplashScreenActivity extends BaseAppCompactActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Utility.navigationIntent(SplashScreenActivity.this, LoginActivity.class);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
