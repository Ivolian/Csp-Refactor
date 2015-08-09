package com.unicorn.csp.activity;


import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.f2prateek.dart.InjectExtra;
import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ToolbarActivity;
import com.unicorn.csp.model.Question;
import com.unicorn.csp.utils.ConfigUtils;
import com.unicorn.csp.utils.JSONUtils;
import com.unicorn.csp.utils.ToastUtils;
import com.unicorn.csp.volley.MyVolley;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

public class QuestionDetailActivity extends ToolbarActivity{

    @Bind(R.id.et_question)
    EditText etQuestion;

    @InjectExtra("question")
    Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        initToolbar("问答详情", true);

        etQuestion.setText(question.getContent());
    }

    @OnClick(R.id.btn_send)
    public void send(){

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
                            ToastUtils.show("回答成功");
                            finish();
                        } else {
                            ToastUtils.show("回答失败");
                        }
                    }
                },
                MyVolley.getDefaultErrorListener()));
    }

    private String getUrl() {

        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/v1/answer/create?").buildUpon();
        builder.appendQueryParameter("userId", ConfigUtils.getUserId());
        builder.appendQueryParameter("questionId", question.getId());
        builder.appendQueryParameter("content", getQuestion());
        return builder.toString();
    }

    private String getQuestion() {

        return etQuestion.getText().toString().trim();
    }

}
