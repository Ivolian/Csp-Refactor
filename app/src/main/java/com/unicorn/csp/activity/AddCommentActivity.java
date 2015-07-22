package com.unicorn.csp.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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


public class AddCommentActivity extends ToolbarActivity {

    @Bind(R.id.et_comment)
    EditText etComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        initToolbar("发表评论", true);
    }

    private void addComment() {

        if (getComment().equals("")) {
            ToastUtils.show("评论不能为空");
            return;
        }
        MyVolley.addRequest(new JsonObjectRequest(getUrl(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        boolean result = JSONUtils.getBoolean(response, "result", false);
                        if (result) {
                            ToastUtils.show("发表评论成功");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startCommentActivity();
                                }
                            }, 600);

                        } else {
                            ToastUtils.show("发表评论失败");
                        }
                    }
                },
                MyVolley.getDefaultErrorListener()));
    }

    private String getUrl() {

        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/v1/comment/create?").buildUpon();
        builder.appendQueryParameter("userId", ConfigUtils.getUserId());
        builder.appendQueryParameter("contentId", getIntent().getStringExtra("newsId"));
        builder.appendQueryParameter("words", getComment());
        return builder.toString();
    }

    private void startCommentActivity() {

        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra("newsId", getIntent().getStringExtra("newsId"));
        startActivity(intent);
    }


    // ====================== 菜单 ======================

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_comment:
                addComment();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_add_comment, menu);
        menu.findItem(R.id.add_comment).setIcon(getMailSendDrawable());
        return super.onCreateOptionsMenu(menu);
    }


    // ====================== 底层方法 ======================

    private Drawable getMailSendDrawable() {

        return new IconDrawable(this, Iconify.IconValue.zmdi_mail_send)
                .colorRes(android.R.color.white)
                .actionBarSize();
    }

    private String getComment() {

        return etComment.getText().toString().trim();
    }

}
