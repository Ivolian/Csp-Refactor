package com.unicorn.csp.activity;


import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ButterKnifeActivity;

import butterknife.Bind;


public class WebViewActivity extends ButterKnifeActivity {

    @Bind(R.id.myProgressBar)
    ProgressBar progressBar;


    private static final String TAG = "MainActivity";
    private WebView mWebView;
    private WebChromeClient mWebChromeClient;
    private WebViewClient mWebViewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mWebView = (WebView) findViewById(R.id.webview);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
//        settings.setDatabaseEnabled(true);
//        settings.setDatabasePath(getDatabasePath("webview.db").getAbsolutePath());
//        settings.setAppCacheMaxSize(1024 * 1024 * 8);
//        // enableHTML5AppCache
//        settings.setDomStorageEnabled(true);
//        settings.setAppCacheEnabled(true);
//        settings.setAppCachePath(getCacheDir().getAbsolutePath());
//        settings.setAllowFileAccess(true);
//        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        settings.setSupportZoom(true);
//        settings.setBuiltInZoomControls(true);

        mWebChromeClient = new WebChromeClient() {
            @Override
            public void onExceededDatabaseQuota(String url, String databaseIdentifier,
                                                long currentQuota, long estimatedSize, long totalUsedQuota,
                                                WebStorage.QuotaUpdater quotaUpdater) {
                quotaUpdater.updateQuota(estimatedSize * 2);

            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                progressBar.setVisibility(newProgress==100? View.GONE:View.VISIBLE);
//                updateProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, final String title) {
                super.onReceivedTitle(view, title);
//                Log.d(TAG, "onReceivedTitle, url: " + view.getUrl() + ", title: " + title);
//
//                final String url = view.getUrl();
//
//                if (HOMEPAGE_URL.equals(url)) {
//                    mWebView.clearHistory();
//                }
//
//                if (url.equals(mLastUrl)) {
//                    return;
//                }
//                // url has changed, check if it's watch page
//                mLastUrl = url;
//
//                onPageChanged(url);
            }
        };

        mWebViewClient = new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "shouldOverrideUrlLoading, url: " + url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d(TAG, "onPageStarted, url: " + url);
//                onPageChanged(url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG, "onPageFinished, url: " + url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description,
                                        String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.d(TAG, "onReceivedError");
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                Log.d(TAG, "onReceivedSslError");
            }
        };
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setWebChromeClient(mWebChromeClient);

        mWebView.loadUrl(getIntent().getStringExtra("url"));
    }


}
