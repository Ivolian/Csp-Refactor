package com.unicorn.csp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebViewClient;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.jauker.widget.BadgeView;
import com.malinskiy.materialicons.widget.IconTextView;
import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ToolbarActivity;
import com.unicorn.csp.model.News;
import com.unicorn.csp.other.greenmatter.ColorOverrider;
import com.unicorn.csp.utils.ToastUtils;

import butterknife.Bind;
import butterknife.OnClick;

public class NewsContentActivity extends ToolbarActivity implements ObservableScrollViewCallbacks {

    @Bind(R.id.webView)
    ObservableWebView webView;

    @Bind(R.id.itv_thumb)
    IconTextView itvThumb;

    @Bind(R.id.itv_star)
    IconTextView itvStar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initToolbar(getNews().getTitle(), true);
        initViews();

    }

    private News getNews(){

       return getIntent().getParcelableExtra("news");
    }

    private void initViews() {

        initWebView();

        BadgeView badgeView = new BadgeView(this);
        badgeView.setTargetView(findViewById(R.id.comment_badge));
        badgeView.setBadgeCount(22);
    }

    private void initWebView() {

     webView.setScrollViewCallbacks(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.loadData(getNews().getData(), "text/html; charset=UTF-8", null);
        webView.setWebViewClient(new WebViewClient());
    }

    @OnClick(R.id.itv_thumb)
    public void thumb() {

        itvThumb.setTextColor(ColorOverrider.getInstance(this).getColorAccent());
        ToastUtils.show(this, "已赞");
    }

    boolean star = false;

    @OnClick(R.id.itv_star)
    public void onStarClick() {

        if (star) {
            unStar();
            star = false;
        } else {
            star();
            star = true;
        }
    }

    public void star() {

        itvStar.setTextColor(ColorOverrider.getInstance(this).getColorAccent());
        ToastUtils.show(this, "已关注");
    }

    public void unStar() {

        itvStar.setTextColor(Color.parseColor("#cccccc"));
        ToastUtils.show(this, "已取消关注");
    }

    @OnClick(R.id.itv_comment)
    public void comment() {

        startActivity(CommentActivity.class);
    }

    @OnClick(R.id.add_comment)
    public void startAddCommentActivity() {

        startActivity(AddCommentActivity.class);
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        ToastUtils.show(scrollState+"");
    }
}
