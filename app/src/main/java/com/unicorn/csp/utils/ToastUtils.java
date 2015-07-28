package com.unicorn.csp.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.unicorn.csp.MyApplication;

public class ToastUtils {

    static Toast toast = Toast.makeText(MyApplication.getInstance(), "", Toast.LENGTH_SHORT);

    public static void show(String msg) {

        toast.setText(msg);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
