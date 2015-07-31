package com.unicorn.csp.utils;


import com.unicorn.csp.MyApplication;
import com.unicorn.csp.other.TinyDB;

public class ConfigUtils {

    public static String getBaseUrl() {

        return "http://192.168.1.100:3000/withub";
    }


    final static String SF_USER_ID = "user_id";

    public static void saveUserId(String userId) {

        new TinyDB(MyApplication.getInstance().getApplicationContext()).putString(SF_USER_ID, userId);
    }

    public static String getUserId() {

        return new TinyDB(MyApplication.getInstance().getApplicationContext()).getString(SF_USER_ID);
    }

}
