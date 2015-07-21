package com.unicorn.csp.activity;

import android.animation.ArgbEvaluator;
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


public class NewsDetailActivity extends ToolbarActivity implements ObservableScrollViewCallbacks {

    @Bind(R.id.observable_webview)
    ObservableWebView observableWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        initToolbar(getNews().getTitle(), true);
        initViews();
    }

    @Bind(R.id.filter_menu1)
    FilterMenuLayout layout1 ;

    // todo 考虑到节省流量的问题，news 应该在这再发一次请求去取
    private News getNews(){

       return getIntent().getParcelableExtra("news");
    }

    private void initViews() {

        initWebView();


        attachMenu1(layout1);
//
//
        int darkerColor = (int) new ArgbEvaluator().evaluate(0.7f, Color.parseColor("#000000"), ColorOverrider.getInstance(this).getColorAccent());
        layout1.setPrimaryDarkColor(darkerColor);
    }


    private FilterMenu attachMenu1(FilterMenuLayout layout){
        return new FilterMenu.Builder(this)
                .addItem(getHistoryDrawable())
                .addItem(getHistoryDrawable())
                .addItem(getHistoryDrawable())
                .addItem(getHistoryDrawable())
                .attach(layout)
                .build();

    }

    private void initWebView() {

     observableWebView.setScrollViewCallbacks(this);
        observableWebView.getSettings().setJavaScriptEnabled(true);
        observableWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        observableWebView.loadData(getNews().getData(), "text/html; charset=UTF-8", null);
        observableWebView.setWebViewClient(new WebViewClient());
    }





    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        if (ScrollState.UP == scrollState){
            layout1.setVisibility(View.GONE);
        }else if(ScrollState.DOWN == scrollState){
            layout1.setVisibility(View.VISIBLE);
        }
    }

    private Drawable getHistoryDrawable() {

        return new IconDrawable(this, Iconify.IconValue.zmdi_time)
                .colorRes(android.R.color.white)
                .sizeDp(28);
    }

}
