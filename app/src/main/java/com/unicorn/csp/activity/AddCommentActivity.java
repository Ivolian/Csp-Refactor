package com.unicorn.csp.activity;

import android.os.Bundle;
import android.view.Menu;

import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ToolbarActivity;


public class AddCommentActivity extends ToolbarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        initToolbar("发表评论", true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_add_comment,menu);
        menu.findItem(R.id.add_comment).setIcon(
                new IconDrawable(this, Iconify.IconValue.zmdi_mail_send)
                        .colorRes(android.R.color.white)
                        .actionBarSize());

        return super.onCreateOptionsMenu(menu);
    }

}
