package com.unicorn.csp.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ToolbarActivity;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.Arrays;

import butterknife.Bind;


public class QuestionActivity extends ToolbarActivity {

    @Bind(R.id.sp)
    MaterialBetterSpinner materialBetterSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        initToolbar("我要提问", true);
        initViews();
    }

    private void initViews() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                Arrays.asList("问题分类1", "问题分类2", "问题分类3"));
        materialBetterSpinner.setAdapter(adapter);
    }

}
