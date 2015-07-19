package com.unicorn.csp.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.unicorn.csp.R;
import com.unicorn.csp.adapter.pager.HotSpotPagerAdapter;
import com.unicorn.csp.fragment.base.ButterKnifeFragment;
import com.unicorn.csp.other.greenmatter.ColorOverrider;

import butterknife.Bind;


public class ViewPagerFragment extends ButterKnifeFragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_viewpager;
    }

    @Bind(R.id.tabs)
    PagerSlidingTabStrip pagerSlidingTabStrip;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    private com.unicorn.csp.greendao.Menu menu;

    public void setMenu(com.unicorn.csp.greendao.Menu menu) {
        this.menu = menu;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        initViews();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

//        inflater.inflate(R.menu.a_menu_general_fragment, menu);
//        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setQueryHint("请输入查询内容");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void initViews() {

//        boolean result = menu.getDepth() == 2;

        com.unicorn.csp.greendao.Menu menu = (com.unicorn.csp.greendao.Menu)getArguments().getSerializable("menu");
        viewPager.setAdapter(new HotSpotPagerAdapter(menu.getDepth()==2?getChildFragmentManager():getActivity().getSupportFragmentManager(),menu));
        pagerSlidingTabStrip.setViewPager(viewPager);
        pagerSlidingTabStrip.setIndicatorColor(ColorOverrider.getInstance(getActivity()).getColorAccent());
    }

}
