package com.unicorn.csp.activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.CheckBox;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ivo.flatbutton.FlatButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.unicorn.csp.MyApplication;
import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ToolbarActivity;
import com.unicorn.csp.greendao.Menu;
import com.unicorn.csp.other.TinyDB;
import com.unicorn.csp.utils.ConfigUtils;
import com.unicorn.csp.utils.JSONUtils;
import com.unicorn.csp.utils.ToastUtils;
import com.unicorn.csp.volley.MyVolley;
import com.unicorn.csp.volley.toolbox.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;


public class LoginActivity extends ToolbarActivity {


    // ========================== SF 常量 ==========================

    // 一律使用 username，而不是 account
    final String SF_USERNAME = "username";

    final String SF_PASSWORD = "password";

    // key 一律驼峰命名
    final String SF_REMEMBER_ME = "rememberMe";


    // ========================== views ==========================

    @Bind(R.id.et_username)
    MaterialEditText etUsername;

    @Bind(R.id.et_password)
    MaterialEditText etPassword;

    @Bind(R.id.cb_remember_me)
    CheckBox cbRememberMe;

    @Bind(R.id.btn_login)
    FlatButton btnLogin;


    // ========================== dialog handler ==========================

    MaterialDialog loginDialog;

    MaterialDialog syncMenuDialog;


    // ========================== onCreate ==========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolbar("登录", false);
        restoreLoginInfo();
    }


    // ========================== 登录按钮状态 ==========================

    @OnTextChanged(R.id.et_username)
    public void onUsernameChange() {

        onUsernameOrPasswordChange();
    }

    @OnTextChanged(R.id.et_password)
    public void onPasswordChange() {

        onUsernameOrPasswordChange();
    }

    private void onUsernameOrPasswordChange() {

        btnLogin.setEnabled(!isUsernameEmpty() && !isPasswordEmpty());
    }


    // ========================== 登录 ==========================

    @OnClick(R.id.btn_login)
    public void onLoginBtnClick() {

        login();
    }

    private void login() {

        loginDialog = showLoginDialog();
        MyVolley.addRequest(new JsonObjectRequest(getLoginUrl(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loginDialog.dismiss();
                        boolean result = JSONUtils.getBoolean(response, "result", false);
                        if (result) {
                            String userId = JSONUtils.getString(response, "userId", "");
                            ConfigUtils.saveUserId(userId);
                            syncMenuFromServer();
                        } else {
                            ToastUtils.show("账号或密码错误");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loginDialog.dismiss();
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

    private String getLoginUrl() {

        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/v1/user/login?").buildUpon();
        builder.appendQueryParameter("username", getUsername());
        builder.appendQueryParameter("password", getPassword());
        return builder.toString();
    }


    // ========================== 同步菜单 ==========================

    private void syncMenuFromServer() {

        syncMenuDialog = showSyncMenuDialog();
        MyVolley.addRequest(new JsonObjectRequest(ConfigUtils.getBaseUrl() + "/api/v1/region/all2", // todo modify the url on server
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        persistMenu(response);
                        syncMenuDialog.dismiss();
                        storeLoginInfo();
                        startActivityAndFinish(MainActivity.class);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        syncMenuDialog.dismiss();
                        ToastUtils.show(VolleyErrorHelper.getErrorMessage(volleyError));
                    }
                }));
    }

    private MaterialDialog showSyncMenuDialog() {

        return new MaterialDialog.Builder(this)
                .title("同步菜单中")
                .content("请稍后...")
                .progress(true, 0)
                .cancelable(false)
                .show();
    }

    private void persistMenu(JSONObject rootItem) {

        MyApplication.getMenuDao().deleteAll();

        Menu rootMenu = itemToMenuSimple(rootItem);
        rootMenu.setParent(null);
        MyApplication.getMenuDao().insert(rootMenu);
        copyItems(rootItem, rootMenu);
    }

    private void copyItems(JSONObject item, Menu menu) {

        JSONArray items = JSONUtils.getJSONArray(item, "items", null);
        if (items != null) {
            for (int i = 0; i != items.length(); i++) {
                JSONObject subItem = JSONUtils.getJSONObject(items, i);
                Menu subMenu = itemToMenuSimple(subItem);
                subMenu.setParent(menu);
                MyApplication.getMenuDao().insert(subMenu);
                copyItems(subItem, subMenu);
            }
        }
    }

    private Menu itemToMenuSimple(JSONObject item) {

        Menu menu = new Menu();
        menu.setId(JSONUtils.getString(item, "id", ""));
        menu.setName(JSONUtils.getString(item, "name", ""));
        menu.setType(JSONUtils.getString(item, "code", ""));     // todo server change code => type
        menu.setOrderNo(JSONUtils.getInt(item, "orderNo", 0));
        return menu;
    }


    // ========================== 记住密码 ==========================

    private void restoreLoginInfo() {

        TinyDB tinyDB = new TinyDB(this);
        cbRememberMe.setChecked(tinyDB.getBoolean(SF_REMEMBER_ME, false));
        if (cbRememberMe.isChecked()) {
            etUsername.setText(tinyDB.getString(SF_USERNAME));
            etPassword.setText(tinyDB.getString(SF_PASSWORD));
        }
    }

    private void storeLoginInfo() {

        TinyDB tinyDB = new TinyDB(this);
        tinyDB.putString(SF_USERNAME, getUsername());
        tinyDB.putString(SF_PASSWORD, getPassword());
        tinyDB.putBoolean(SF_REMEMBER_ME, cbRememberMe.isChecked());
    }


    // ========================== 底层方法 ==========================

    private boolean isUsernameEmpty() {

        return getUsername().equals("");
    }

    private boolean isPasswordEmpty() {

        return getPassword().equals("");
    }

    private String getUsername() {

        return etUsername.getText().toString().trim();
    }

    private String getPassword() {

        return etPassword.getText().toString().trim();
    }

}
