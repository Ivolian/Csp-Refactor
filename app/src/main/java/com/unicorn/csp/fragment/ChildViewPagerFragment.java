package com.unicorn.csp.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.unicorn.csp.R;
import com.unicorn.csp.adapter.pager.HotSpotPagerAdapter;
import com.unicorn.csp.fragment.base.ButterKnifeFragment;

import butterknife.Bind;


public class ChildViewPagerFragment extends ButterKnifeFragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_child_viewpager;
    }

    @Bind(R.id.viewpagertab)
    SmartTabLayout smartTabLayout;

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

        viewPager.setAdapter(new HotSpotPagerAdapter(getChildFragmentManager(), this.menu));
        smartTabLayout.setViewPager(viewPager);
        smartTabLayout.setAlpha(0.8f);
    }

}
