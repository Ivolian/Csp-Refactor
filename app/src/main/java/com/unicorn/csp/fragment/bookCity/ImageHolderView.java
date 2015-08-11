package com.unicorn.csp.fragment.bookCity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.unicorn.csp.activity.bookCity.WebViewActivity;


public class ImageHolderView implements CBPageAdapter.Holder<Integer> {

    Activity activity;

    public ImageHolderView(Activity activity) {
        this.activity = activity;
    }

    private ImageView imageView;

    @Override
    public View createView(Context context) {

        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    private String[] urls = {
            "http://i.cnki.net/Mobile/Home/Index/",
            "http://www.dz.cnki.net/kns55/index.html?username=sygzgf&password=sygzgf123",
            "http://kepu.cnki.net/KNetWeb/Nonacad/nonacad/OutHref/cjft?type=in&username=sygzgf&password=sygzgf123",
            "http://wenhua.cnki.net/KNetWeb/Nonacad/nonacad/OutHref/CJFU?type=in&username=sygzgf&password=sygzgf123",
            "http://wenyi.cnki.net//KNetWeb/Nonacad/nonacad/OutHref/cjfv?type=in&username=sygzgf&password=sygzgf123"
    };

    @Override
    public void UpdateUI(Context context, final int position, Integer data) {
        imageView.setImageResource(data);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, WebViewActivity.class);
                intent.putExtra("url", urls[position]);
                activity.startActivity(intent);
            }
        });
    }

}
