package com.unicorn.csp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.CheckBox;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ivo.flatbutton.FlatButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ToolbarActivity;
import com.unicorn.csp.other.TinyDB;
import com.unicorn.csp.utils.ToastUtils;
import com.unicorn.csp.volley.MyVolley;
import com.unicorn.csp.volley.toolbox.VolleyErrorHelper;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;


public class LoginActivity extends ToolbarActivity {


    // ========================== SF 常量 ==========================

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

        final MaterialDialog loginDialog = showLoginDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loginDialog.dismiss();

                // todo login

//                test();
                ToastUtils.show(LoginActivity.this, "登录成功");
                storeLoginInfo();
                startActivityAndFinish(MainActivity.class);
            }
        }, 1500);
    }

    private void test(){

        String url = "http://192.168.1.101:3002/withub/api/v1/news?pageNo=1&pageSize=10";
        MyVolley.getRequestQueue().add(new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ToastUtils.show(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ToastUtils.show(VolleyErrorHelper.getErrorMessage(volleyError));
                    }
                }));
    }

    private MaterialDialog showLoginDialog() {

        return new MaterialDialog.Builder(this)
                .title("登录中")
                .content("请稍后...")
                .progress(true, 0)
                .cancelable(false)
                .show();
    }


    // ========================== 记住密码 ==========================

    private void storeLoginInfo() {

        TinyDB tinyDB = new TinyDB(this);
        tinyDB.putString(SF_ACCOUNT, getAccount());
        tinyDB.putString(SF_PASSWORD, getPassword());
        tinyDB.putBoolean(SF_REMEMBER_ME, cbRememberMe.isChecked());
    }

    private void restoreLoginInfo() {

        TinyDB tinyDB = new TinyDB(this);
        cbRememberMe.setChecked(tinyDB.getBoolean(SF_REMEMBER_ME, false));
        if (cbRememberMe.isChecked()) {
            etAccount.setText(tinyDB.getString(SF_ACCOUNT));
            etPassword.setText(tinyDB.getString(SF_PASSWORD));
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
