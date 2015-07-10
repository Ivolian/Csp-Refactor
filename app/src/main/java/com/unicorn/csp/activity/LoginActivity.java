package com.unicorn.csp.activity;

import android.os.Bundle;
import android.widget.CheckBox;

import com.ivo.flatbutton.FlatButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.unicorn.csp.R;
import com.unicorn.csp.utils.ToastUtils;

import net.grandcentrix.tray.TrayAppPreferences;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;


public class LoginActivity extends ToolbarActivity {


    // ========================== 常量 ==========================

    final String SF_ACCOUNT = "account";

    final String SF_PASSWORD = "password";

    final String SF_REMEMBER_ME = "remember_me";


    // ========================== Views ==========================

    @Bind(R.id.et_account)
    MaterialEditText etAccount;

    @Bind(R.id.et_password)
    MaterialEditText etPassword;

    @Bind(R.id.cb_remember_me)
    CheckBox cbRememberMe;

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

        restoreLoginInfo();
    }


    // ========================== 登录按钮状态 ==========================

    @OnTextChanged(R.id.et_account)
    public void onUsernameChange() {

        onUsernameOrPasswordChange();
    }

    @OnTextChanged(R.id.et_password)
    public void onPasswordChange() {

        onUsernameOrPasswordChange();
    }

    private void onUsernameOrPasswordChange() {

        btnLogin.setEnabled(!isAccountEmpty() && !isPasswordEmpty());
    }


    // ========================== 登录 ==========================

    @OnClick(R.id.btn_login)
    public void login() {

        // todo login
        ToastUtils.show(this, "登录成功！");
        storeLoginInfo();
    }


    // ========================== 记住密码 ==========================

    private void storeLoginInfo() {

        TrayAppPreferences trayAppPreferences = new TrayAppPreferences(this);
        trayAppPreferences.put(SF_ACCOUNT, getAccount());
        trayAppPreferences.put(SF_PASSWORD, getPassword());
        trayAppPreferences.put(SF_REMEMBER_ME, cbRememberMe.isChecked());
    }

    private void restoreLoginInfo() {

        try {
            TrayAppPreferences trayAppPreferences = new TrayAppPreferences(this);
            String account = trayAppPreferences.getString(SF_ACCOUNT);
            String password = trayAppPreferences.getString(SF_PASSWORD);
            boolean rememberMe = trayAppPreferences.getBoolean(SF_REMEMBER_ME, false);
            if (rememberMe) {
                etAccount.setText(account);
                etPassword.setText(password);
            }
        } catch (Exception e) {
            //
        }
    }


    // ========================== 基础方法 ==========================

    private boolean isAccountEmpty() {

        return getAccount().equals("");
    }

    private boolean isPasswordEmpty() {

        return getPassword().equals("");
    }

    private String getAccount() {

        return etAccount.getText().toString().trim();
    }

    private String getPassword() {

        return etPassword.getText().toString().trim();
    }

}
