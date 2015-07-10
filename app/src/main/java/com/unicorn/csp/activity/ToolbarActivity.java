package com.unicorn.csp.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.unicorn.csp.R;

import butterknife.Bind;


public abstract class ToolbarActivity extends ButterKnifeActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;


    // ========================== home键后退 ==========================

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // ========================== toolbar ==========================

    protected void initToolbar(String toolbarTitle, boolean displayHomeAsUpEnable) {

        // 隐藏默认的标题
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(displayHomeAsUpEnable);
        }
        tvToolbarTitle.setText(toolbarTitle);
    }

    protected void setToolbarTitle(String toolbarTitle) {

        tvToolbarTitle.setText(toolbarTitle);
    }

    protected boolean isToolbarHidden() {

        return toolbar.getVisibility() == View.GONE;
    }

    protected void hideToolbar() {

        toolbar.setVisibility(View.GONE);
    }

    protected void showToolbar() {

        toolbar.setVisibility(View.VISIBLE);
    }

    protected void toggleToolbar() {

        if (isToolbarHidden()) {
            showToolbar();
        } else {
            hideToolbar();
        }
    }

    protected Toolbar getToolbar() {

        return toolbar;
    }


    // ========================== 底层方法 ==========================

    protected void startActivity(Class activityClass) {

        startActivity(new Intent(this, activityClass));
    }

}
