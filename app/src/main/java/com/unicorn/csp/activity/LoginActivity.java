package com.unicorn.csp.activity;

import android.os.Bundle;

import com.unicorn.csp.R;


public class LoginActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolbar("登录",false);
    }

}
