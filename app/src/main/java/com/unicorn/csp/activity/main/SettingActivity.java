package com.unicorn.csp.activity.main;

import android.os.Bundle;

import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ToolbarActivity;


public class SettingActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initToolbar("更多设置", true);
    }

}
