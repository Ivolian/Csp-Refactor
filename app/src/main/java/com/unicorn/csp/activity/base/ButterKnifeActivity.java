package com.unicorn.csp.activity.base;

import android.support.annotation.LayoutRes;

import butterknife.ButterKnife;

public abstract class ButterKnifeActivity extends ColorActivity {

    @Override
    public void setContentView(@LayoutRes int layoutResId) {

        super.setContentView(layoutResId);
        ButterKnife.bind(this);
    }

}
