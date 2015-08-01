package com.unicorn.csp.activity;

import android.content.Intent;
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
import com.r0adkll.slidr.Slidr;
import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ToolbarActivity;
import com.unicorn.csp.utils.ConfigUtils;
import com.unicorn.csp.utils.JSONUtils;
import com.unicorn.csp.utils.ToastUtils;
import com.unicorn.csp.volley.MyVolley;

import org.json.JSONObject;

import butterknife.Bind;


public class AddCommentActivity extends ToolbarActivity {


    // ==================== view ====================

    @Bind(R.id.et_comment)
    EditText etComment;


    // ==================== 必要参数，新闻 Id ====================

    // 可能来自 NewsDetailActivity，可能来自 CommentActivity。
    @InjectExtra("newsId")
    String newsId;


    // ==================== onCreate ====================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        initToolbar("发表评论", true);
        Slidr.attach(this);
    }


    // ==================== 发送评论 ====================

    private void postCommentToServer() {

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
                            startCommentActivityAndFinish();
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
        builder.appendQueryParameter("contentId", newsId);
        builder.appendQueryParameter("words", getComment());
        return builder.toString();
    }

    /*
        有两种可能: 1.打开评论列表 2.回到评论列表界面
        1. 传递 newsId
        2. 评论界面已获得 newsId
      */
    private void startCommentActivityAndFinish() {

        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra("newsId", newsId);
        startActivity(intent);
        finish();
    }


    // ====================== 菜单 ======================

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_comment:
                postCommentToServer();
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

    private Drawable getMailSendDrawable() {

        return new IconDrawable(this, Iconify.IconValue.zmdi_mail_send)
                .colorRes(android.R.color.white)
                .actionBarSize();
    }

    private String getComment() {

        return etComment.getText().toString().trim();
    }

}
