package com.unicorn.csp.activity;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ToolbarActivity;
import com.unicorn.csp.utils.ConfigUtils;
import com.unicorn.csp.utils.JSONUtils;
import com.unicorn.csp.utils.ToastUtils;
import com.unicorn.csp.volley.MyVolley;

import org.json.JSONObject;

import butterknife.Bind;


public class AddQuestionActivity extends ToolbarActivity {


    // ==================== view ====================

    @Bind(R.id.et_question)
    EditText etQuestion;


    // ==================== onCreate ====================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        initToolbar("我要提问", true);
    }


    // ==================== 发送评论 ====================

    private void postQuestionToServer() {

        if (getQuestion().equals("")) {
            ToastUtils.show("提问不能为空");
            return;
        }
        MyVolley.addRequest(new JsonObjectRequest(getUrl(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        boolean result = JSONUtils.getBoolean(response, "result", false);
                        if (result) {
                            ToastUtils.show("提问成功");
                            finish();
                            // TODO MAYBE 添加提问后的刷新问题
                        } else {
                            ToastUtils.show("提问失败");
                        }
                    }
                },
                MyVolley.getDefaultErrorListener()));
    }

    private String getUrl() {

        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/v1/question/create?").buildUpon();
        builder.appendQueryParameter("userId", ConfigUtils.getUserId());
        builder.appendQueryParameter("content", getQuestion());
        return builder.toString();
    }


    // ====================== 菜单 ======================e

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_question:
                postQuestionToServer();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_add_question, menu);
        menu.findItem(R.id.add_question).setIcon(getMailSendDrawable());
        return super.onCreateOptionsMenu(menu);
    }

    private Drawable getMailSendDrawable() {

        return new IconDrawable(this, Iconify.IconValue.zmdi_mail_send)
                .colorRes(android.R.color.white)
                .actionBarSize();
    }

    private String getQuestion() {

        return etQuestion.getText().toString().trim();
    }

}
