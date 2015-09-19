package com.unicorn.csp.activity.main;

import android.os.Bundle;

import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ToolbarActivity;
import com.unicorn.csp.activity.other.ChangePasswordActivity;

import butterknife.OnClick;


public class SettingActivity extends ToolbarActivity {

//    @Bind(R.id.tv_select_color)
//    TextView tvSelectColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initToolbar("更多设置", true);
    }

    @OnClick(R.id.tv_change_password)
    public void startChangePasswordActivity(){

        startActivity(ChangePasswordActivity.class);
    }

}
