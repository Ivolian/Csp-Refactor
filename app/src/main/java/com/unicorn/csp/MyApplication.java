package com.unicorn.csp;

import android.app.Application;

import com.bugsnag.android.Bugsnag;
import com.unicorn.csp.volley.MyVolley;


public class MyApplication extends Application {

    private static MyApplication instance;

    public static MyApplication getInstance() {

        return instance;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
        Bugsnag.init(instance);
        MyVolley.init(instance);
    }

}
