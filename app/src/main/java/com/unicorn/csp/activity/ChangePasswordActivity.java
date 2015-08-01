package com.unicorn.csp.activity;

import android.net.Uri;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ivo.flatbutton.FlatButton;
import com.r0adkll.slidr.Slidr;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ToolbarActivity;
import com.unicorn.csp.utils.ConfigUtils;
import com.unicorn.csp.utils.JSONUtils;
import com.unicorn.csp.utils.ToastUtils;
import com.unicorn.csp.volley.MyVolley;
import com.unicorn.csp.volley.toolbox.VolleyErrorHelper;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;


public class ChangePasswordActivity extends ToolbarActivity {


    // ========================== views ==========================

    @Bind(R.id.et_old_password)
    MaterialEditText etOldPassword;

    @Bind(R.id.et_new_password)
    MaterialEditText etNewPassword;

    @Bind(R.id.et_confirm_password)
    MaterialEditText etConfirmPassword;

    @Bind(R.id.btn_confirm)
    FlatButton btnConfirm;


    // ========================== dialog handler ==========================

    MaterialDialog copyDialog;


    // ========================== onCreate ==========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initToolbar("修改密码", true);
        Slidr.attach(this);
    }


    // ========================== 确认密码 ==========================

    @OnClick(R.id.btn_confirm)
    public void changePassword() {

        if (!getNewPassword().equals(getConfirmPassword())) {
            ToastUtils.show("确认密码有误");
            return;
        }

        copyDialog = showCopeDialog();
        MyVolley.addRequest(new JsonObjectRequest(getUrl(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideCopeDialog();
                        boolean result = JSONUtils.getBoolean(response, "result", false);
                        if (result) {
                            ToastUtils.show("修改成功");
                            finish();
                        } else {
                            ToastUtils.show("旧密码错误");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideCopeDialog();
                        ToastUtils.show(VolleyErrorHelper.getErrorMessage(volleyError));
                    }
                }));
    }

    private String getUrl() {

        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/v1/user/changePassword?").buildUpon();
        builder.appendQueryParameter("userId", ConfigUtils.getUserId());
        builder.appendQueryParameter("oldPassword", getOldPassword());
        builder.appendQueryParameter("newPassword", getNewPassword());
        return builder.toString();
    }

    private MaterialDialog showCopeDialog() {

        return new MaterialDialog.Builder(this)
                .title("处理中")
                .content("请稍后...")
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .cancelable(false)
                .show();
    }

    private void hideCopeDialog() {

        if (copyDialog != null) {
            copyDialog.dismiss();
        }
    }


    // ========================== 确认按钮状态 ==========================

    @OnTextChanged(R.id.et_old_password)
    public void onOldPasswordChange() {

        onPasswordChange();
    }

    @OnTextChanged(R.id.et_new_password)
    public void onNewPasswordChange() {

        onPasswordChange();
    }


    @OnTextChanged(R.id.et_confirm_password)
    public void onConfirmPasswordChange() {

        onPasswordChange();
    }

    private void onPasswordChange() {

        btnConfirm.setEnabled(!isOldPasswordEmpty() && !isNewPasswordEmpty() && !isConfirmPasswordEmpty());
    }

    private boolean isOldPasswordEmpty() {

        return getOldPassword().equals("");
    }

    private boolean isNewPasswordEmpty() {

        return getNewPassword().equals("");
    }

    private boolean isConfirmPasswordEmpty() {

        return getConfirmPassword().equals("");
    }

    private String getOldPassword() {

        return etOldPassword.getText().toString().trim();
    }

    private String getNewPassword() {

        return etNewPassword.getText().toString().trim();
    }

    private String getConfirmPassword() {

        return etConfirmPassword.getText().toString().trim();
    }

}