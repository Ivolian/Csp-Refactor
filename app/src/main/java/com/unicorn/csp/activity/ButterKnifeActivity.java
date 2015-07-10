package com.unicorn.csp.activity;

import android.support.annotation.LayoutRes;

import butterknife.ButterKnife;

public class ButterKnifeActivity extends ColorActivity {

    @Override
    public void setContentView(@LayoutRes int layoutResId) {

        super.setContentView(layoutResId);
        ButterKnife.bind(this);
    }

}
