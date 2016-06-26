package com.example.srin.warisanbudaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Afkar on 4/24/2016.
 */
public class SplashScreenActivity extends AppCompatActivity {
    final String PREF_NAME = "WBI_pref";
    final String PREF_LOGIN = "isLogin";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        SharedPreferences pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        final boolean isLoggedIn = pref.getBoolean(PREF_LOGIN, false);

        Handler h = new Handler();

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLoggedIn){
                    startActivity(new Intent(SplashScreenActivity.this, MemberAreaActivity.class));
                } else {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                }
                finish();
            }
        }, 3000);
    }
}
