package com.unicorn.csp.adapter.pager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.unicorn.csp.fragment.NewsFragment;
import com.unicorn.csp.greendao.Menu;


public class ViewPagerAdapterL2 extends FragmentStatePagerAdapter {

    private Menu menu;

    public ViewPagerAdapterL2(FragmentManager fm, Menu menu) {

        super(fm);
        this.menu = menu;
    }

    @Override
    public Fragment getItem(int position) {

        NewsFragment newsFragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("menu", menu.getChildren().get(position));
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public int getCount() {
        return menu.getChildren().size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return menu.getChildren().get(position).getName();
    }

}
