package com.unicorn.csp.activity;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.f2prateek.dart.InjectExtra;
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


public class AddAnswerActivity extends ToolbarActivity {


    @InjectExtra("questionId")
    String questionId;

    // ==================== view ====================

    @Bind(R.id.et_answer)
    EditText etQuestion;


    // ==================== onCreate ====================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answer);
        initToolbar("提交回答", true);
    }


    // ==================== 发送评论 ====================

    private void postQuestionToServer() {

        if (getAnswer().equals("")) {
            ToastUtils.show("回答不能为空");
            return;
        }
        MyVolley.addRequest(new JsonObjectRequest(getUrl(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        boolean result = JSONUtils.getBoolean(response, "result", false);
                        if (result) {
                            ToastUtils.show("提交成功");
                            finish();
                        } else {
                            ToastUtils.show("提交失败");
                        }
                    }
                },
                MyVolley.getDefaultErrorListener()));
    }

    private String getUrl() {

        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/v1/answer/create?").buildUpon();
        builder.appendQueryParameter("userId", ConfigUtils.getUserId());
        builder.appendQueryParameter("questionId", questionId);
        builder.appendQueryParameter("content", getAnswer());
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

        getMenuInflater().inflate(R.menu.add_question, menu);
        menu.findItem(R.id.add_question).setIcon(getMailSendDrawable());
        return super.onCreateOptionsMenu(menu);
    }

    private Drawable getMailSendDrawable() {

        return new IconDrawable(this, Iconify.IconValue.zmdi_mail_send)
                .colorRes(android.R.color.white)
                .actionBarSize();
    }

    private String getAnswer() {

        return etQuestion.getText().toString().trim();
    }

}
