package com.unicorn.csp.activity;


import android.os.Bundle;
import android.widget.EditText;

import com.f2prateek.dart.InjectExtra;
import com.r0adkll.slidr.Slidr;
import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ToolbarActivity;

import butterknife.Bind;

public class QuestionDetailActivity extends ToolbarActivity{

    @Bind(R.id.et_question)
    EditText etQuestion;

    @InjectExtra("content")
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        initToolbar("问答详情", true);
        Slidr.attach(this);

        etQuestion.setText(content);
    }

}
