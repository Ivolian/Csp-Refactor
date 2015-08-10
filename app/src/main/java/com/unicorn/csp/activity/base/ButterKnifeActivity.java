package com.unicorn.csp.activity.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;

import com.f2prateek.dart.Dart;

import butterknife.ButterKnife;


// clear
public abstract class ButterKnifeActivity extends ColorActivity {

    @Override
    public void setContentView(@LayoutRes int layoutResId) {

        super.setContentView(layoutResId);
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Dart.inject(this);
    }

}
