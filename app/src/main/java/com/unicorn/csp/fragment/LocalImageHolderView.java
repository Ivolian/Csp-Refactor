package com.unicorn.csp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.unicorn.csp.activity.WebViewActivity;


public class LocalImageHolderView implements CBPageAdapter.Holder<Integer>{
    private ImageView imageView;
    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    Activity activity;

    public LocalImageHolderView(Activity activity) {
        this.activity = activity;
    }

    private String[] urls = {
//            "http://i.cnki.net/Mobile/Home/Index/",
            "http://kepu.cnki.net/KNetWeb/Nonacad/nonacad/detail/CJFT?code=SWJJ201507002&tablename=CJFTTEMP",
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
                //点击事件
                Intent intent =new Intent(activity, WebViewActivity.class);
                intent.putExtra("url",urls[position]);
                activity.startActivity(intent);
//                Toast.makeText(view.getContext(), "点击了第" + (position + 1) + "图片", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
