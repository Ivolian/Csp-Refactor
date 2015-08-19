package com.unicorn.csp.activity.main;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.unicorn.csp.MyApplication;
import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ButterKnifeActivity;
import com.unicorn.csp.greendao.Menu;
import com.unicorn.csp.other.TinyDB;
import com.unicorn.csp.utils.ConfigUtils;
import com.unicorn.csp.utils.JSONUtils;
import com.unicorn.csp.utils.ToastUtils;
import com.unicorn.csp.volley.MyVolley;

import org.json.JSONArray;
import org.json.JSONObject;


public class SplashActivity extends ButterKnifeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getRememberMe()){
            login();
        }else {
            gotoLoginActivity();
        }
    }

    private void gotoLoginActivity() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivityAndFinish(LoginActivity.class);
            }
        }, 2000);
    }

    private void login() {

        MyVolley.addRequest(new JsonObjectRequest(getLoginUrl(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        boolean result = JSONUtils.getBoolean(response, "result", false);
                        if (result) {
                            saveUserId(response);
                            saveMenu(response);
                            startActivityAndFinish(MainActivity.class);
                        } else {
                            ToastUtils.show("用户名或密码错误");
                        }
                    }
                },
                MyVolley.getDefaultErrorListener()));
    }

    private String getLoginUrl() {

        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/v1/user/login?").buildUpon();
        builder.appendQueryParameter("username", getUsername());
        builder.appendQueryParameter("password", getPassword());
        return builder.toString();
    }

    private void saveUserId(JSONObject response) {

        String userId = JSONUtils.getString(response, "userId", "");
        ConfigUtils.saveUserId(userId);
    }


    // ========================== 保存菜单到数据库 ==========================

    private void saveMenu(JSONObject response) {

        MyApplication.getMenuDao().deleteAll();

        JSONObject rootMenuItem = JSONUtils.getJSONObject(response, "rootMenuItem", null);
        Menu rootMenu = itemToMenu(rootMenuItem);
        rootMenu.setParent(null);
        MyApplication.getMenuDao().insert(rootMenu);

        copeChildMenuItems(rootMenuItem, rootMenu);
    }

    private void copeChildMenuItems(JSONObject menuItem, Menu menu) {

        JSONArray items = JSONUtils.getJSONArray(menuItem, "items", null);
        if (items != null) {
            for (int i = 0; i != items.length(); i++) {
                JSONObject subItem = JSONUtils.getJSONObject(items, i);
                Menu subMenu = itemToMenu(subItem);
                subMenu.setParent(menu);
                MyApplication.getMenuDao().insert(subMenu);
                copeChildMenuItems(subItem, subMenu);
            }
        }
    }

    private Menu itemToMenu(JSONObject item) {

        Menu menu = new Menu();
        menu.setId(JSONUtils.getString(item, "id", ""));
        menu.setName(JSONUtils.getString(item, "name", ""));
        menu.setType(JSONUtils.getString(item, "type", ""));
        menu.setOrderNo(JSONUtils.getInt(item, "orderNo", 0));
        return menu;
    }


    // ========================== 其他方法 ==========================

    private String getUsername() {

        TinyDB tinyDB = new TinyDB(this);
        return tinyDB.getString(LoginActivity.SF_USERNAME);
    }

    private String getPassword() {

        TinyDB tinyDB = new TinyDB(this);
        return tinyDB.getString(LoginActivity.SF_PASSWORD);
    }

    private boolean getRememberMe() {

        TinyDB tinyDB = new TinyDB(this);
        return tinyDB.getBoolean(LoginActivity.SF_REMEMBER_ME, false);
    }

}
