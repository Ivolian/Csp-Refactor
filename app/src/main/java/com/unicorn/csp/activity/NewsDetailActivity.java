package com.unicorn.csp.activity;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebViewClient;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.linroid.filtermenu.library.FilterMenu;
import com.linroid.filtermenu.library.FilterMenuLayout;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ToolbarActivity;
import com.unicorn.csp.model.News;
import com.unicorn.csp.other.greenmatter.ColorOverrider;

import butterknife.Bind;


public class NewsDetailActivity extends ToolbarActivity implements ObservableScrollViewCallbacks, FilterMenu.OnMenuChangeListener {


    @Bind(R.id.observable_webview)
    ObservableWebView observableWebView;

    @Bind(R.id.filter_menu_layout)
    FilterMenuLayout filterMenuLayout;


    // =============================== onCreate ===============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        initToolbar(getNews().getTitle(), true);
        initViews();
    }

    private void initViews() {

        initWebView();
        initFilterMenuLayout();
    }

    private void initWebView() {

        observableWebView.getSettings().setJavaScriptEnabled(true);
        // todo try remove
//        observableWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        // todo 考虑到节省流量的问题，news 应该在这再发一次请求去取
        observableWebView.loadData(getNews().getData(), "text/html; charset=UTF-8", null);
        observableWebView.setWebViewClient(new WebViewClient());
        observableWebView.setScrollViewCallbacks(this);
    }

    private News getNews() {

        return getIntent().getParcelableExtra("news");
    }

    private void initFilterMenuLayout() {

        int darkerColor = (int) new ArgbEvaluator().evaluate(0.7f, Color.parseColor("#000000"), ColorOverrider.getInstance(this).getColorAccent());
        filterMenuLayout.setPrimaryDarkColor(darkerColor);
        attachMenu(filterMenuLayout);
    }

    private FilterMenu attachMenu(FilterMenuLayout layout) {

        // 添加评论，查看评论，点赞，关注
        return new FilterMenu.Builder(this)
                .addItem(getIconDrawable(Iconify.IconValue.zmdi_star, 28))
                .addItem(getIconDrawable(Iconify.IconValue.zmdi_thumb_up, 24))
                .addItem(getIconDrawable(Iconify.IconValue.zmdi_comment_more, 25))
                .addItem(getIconDrawable(Iconify.IconValue.zmdi_comment_text_alt, 25))
                .attach(layout)
                .withListener(this)
                .build();
    }


    // =============================== FilterMenu 点击事件 ===============================

    @Override
    public void onMenuItemClick(View view, int position) {

        switch (position) {
            case 0:
                // todo
                break;
            case 1:
                // todo
                break;
            case 2:
                // todo
                startActivity(CommentActivity.class);
                break;
            case 3:
                startAddCommentActivity();
                break;
        }
    }

    @Override
    public void onMenuCollapse() {

    }

    @Override
    public void onMenuExpand() {

    }


    // =============================== 监听 WebView 滚动事件 ===============================

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        if (scrollState == ScrollState.UP) {
            filterMenuLayout.setVisibility(View.GONE);
        } else if (scrollState == ScrollState.DOWN) {
            filterMenuLayout.setVisibility(View.VISIBLE);
        }
    }


    // =============================== 底层方法 ===============================

    private Drawable getIconDrawable(Iconify.IconValue iconValue, int size) {

        return new IconDrawable(this, iconValue)
                .colorRes(android.R.color.white)
                .sizeDp(size);
    }


    private void startAddCommentActivity() {

        Intent intent = new Intent(this, AddCommentActivity.class);
        intent.putExtra("newsId", getNews().getId());
        startActivity(intent);
    }

}
