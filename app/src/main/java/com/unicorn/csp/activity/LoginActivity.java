package com.unicorn.csp.activity;

import android.os.Bundle;

import com.ivo.flatbutton.FlatButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.unicorn.csp.R;

import net.grandcentrix.tray.TrayAppPreferences;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;


public class LoginActivity extends ToolbarActivity {

    // ========================== 常量 ==========================

    final String ACCOUNT = "admin";

    final String PASSWORD = "123456";

    // ========================== Views ==========================

    @Bind(R.id.et_account)
    MaterialEditText etAccount;

    @Bind(R.id.et_password)
    MaterialEditText etPassword;

    @Bind(R.id.btn_login)
    FlatButton btnLogin;

    // ========================== onCreate ==========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolbar("登录", false);
        initViews();
    }

    private void initViews() {

        final TrayAppPreferences appPreferences = new TrayAppPreferences(this);

        etAccount.setText(appPreferences.getString("account","default"));
        etPassword.setText(PASSWORD);
    }

    // ========================== onTextChanged ==========================

    @OnTextChanged(R.id.et_account)
    public void onUsernameChange() {

        onUsernameOrPasswordChange();
    }

    @OnTextChanged(R.id.et_password)
    public void onPasswordChange() {

        onUsernameOrPasswordChange();
    }

    private void onUsernameOrPasswordChange() {

        String account = etAccount.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        btnLogin.setEnabled(!account.equals("") && !password.equals(""));
    }

    // ========================== login ==========================

    @OnClick(R.id.btn_login)
    public void login() {

        final TrayAppPreferences appPreferences = new TrayAppPreferences(this);
        appPreferences.put("account", etAccount.getText().toString().trim());
    }


}
