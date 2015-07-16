package com.unicorn.csp.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.unicorn.csp.MyApplication;

public class ToastUtils {

    public static void show(Context context, String message) {

        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void show(String msg){

        show(MyApplication.getInstance(),msg);
    }

}
