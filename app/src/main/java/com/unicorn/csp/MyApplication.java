package com.unicorn.csp;

import android.app.Application;

import com.bugsnag.android.Bugsnag;


public class MyApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        Bugsnag.init(this);
    }

}
