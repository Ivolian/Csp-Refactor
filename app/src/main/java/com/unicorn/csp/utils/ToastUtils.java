package com.unicorn.csp.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.unicorn.csp.MyApplication;

public class ToastUtils {

    static Toast toast;

    public static void show(String msg) {

        if (toast == null) {
            toast = Toast.makeText(MyApplication.getInstance(), "", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.setText(msg);
        toast.show();
    }

}
