package com.unicorn.csp.activity.main;

import android.os.Bundle;
import android.os.Handler;

import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ButterKnifeActivity;


public class SplashActivity extends ButterKnifeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivityAndFinish(LoginActivity.class);
            }
        }, 2000);
    }

}
