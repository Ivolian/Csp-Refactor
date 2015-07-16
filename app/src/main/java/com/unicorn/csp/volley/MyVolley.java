package com.unicorn.csp.volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.unicorn.csp.utils.ToastUtils;
import com.unicorn.csp.volley.toolbox.BitmapLruCache;
import com.unicorn.csp.volley.toolbox.VolleyErrorHelper;


public class MyVolley {

    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;

    private MyVolley() {
        // no instances
    }

    static public void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(cacheSize));
    }


    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader != null) {
            return mImageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }

    public static Response.ErrorListener getDefaultErrorListener() {

        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.show(VolleyErrorHelper.getErrorMessage(volleyError));
            }
        };
    }

}