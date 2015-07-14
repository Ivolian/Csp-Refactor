package com.unicorn.csp.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ButterKnifeActivity;

import butterknife.Bind;

public class WebViewActivity extends ButterKnifeActivity {

    @Bind(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initViews();
    }

    private void initViews() {

        initWebView();
    }

    private void initWebView() {

        String url = "http://www.sc.xinhuanet.com/content/2015-07/08/c_1115856116.htm";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient());
    }

}
