package com.unicorn.csp.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.unicorn.csp.R;
import com.unicorn.csp.adapter.pager.ViewPagerAdapterL2;
import com.unicorn.csp.fragment.base.ButterKnifeFragment;

import butterknife.Bind;


public class ViewPagerFragmentL2 extends ButterKnifeFragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_viewpager_l2;
    }

    @Bind(R.id.viewpagertab)
    SmartTabLayout smartTabLayout;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        initViews();
        return rootView;
    }


    private void initViews() {

        final com.unicorn.csp.greendao.Menu menu = (com.unicorn.csp.greendao.Menu) getArguments().getSerializable("menu");
        viewPager.setAdapter(new ViewPagerAdapterL2(getChildFragmentManager(), menu));
        viewPager.setOffscreenPageLimit(menu.getChildren().size());
        smartTabLayout.setAlpha(0.8f);
        smartTabLayout.setViewPager(viewPager);
//        smartTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                for (int i = 0; i != menu.getChildren().size(); i++) {
//                    TextView textView = (TextView) smartTabLayout.getTabAt(i);
//                    if (i == viewPager.getCurrentItem()) {
//                        textView.setTextColor(getResources().getColor(android.R.color.white));
//                    } else {
//                        textView.setTextColor(ColorOverrider.getInstance(getActivity()).getColorAccent());
//                    }
//
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
////                if (state == 2) {
////                    for (int i = 0; i != menu.getChildren().size(); i++) {
////                        TextView textView = (TextView) smartTabLayout.getTabAt(i);
////                        if (i == viewPager.getCurrentItem()) {
////                            textView.setTextColor(getResources().getColor(android.R.color.white));
////                        } else {
////                            textView.setTextColor(ColorOverrider.getInstance(getActivity()).getColorAccent());
////                        }
////
////                    }
////                }
//            }
//        });
//        viewPager.setCurrentItem(0);

    }

}
