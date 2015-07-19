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
        return R.layout.fragment_child_viewpager;
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

        com.unicorn.csp.greendao.Menu menu =(com.unicorn.csp.greendao.Menu) getArguments().getSerializable("menu");
        viewPager.setAdapter(new ViewPagerAdapterL2(getChildFragmentManager(), menu));
        viewPager.setOffscreenPageLimit(menu.getChildren().size());
        smartTabLayout.setViewPager(viewPager);
        smartTabLayout.setAlpha(0.8f);
    }

}
