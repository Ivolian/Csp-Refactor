package com.unicorn.csp.adapter.pager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.unicorn.csp.fragment.NewsFragment;
import com.unicorn.csp.fragment.ViewPagerFragmentL2;
import com.unicorn.csp.greendao.Menu;


public class ViewPagerAdapterL1 extends FragmentStatePagerAdapter {

    private Menu menu;

    public ViewPagerAdapterL1(FragmentManager fm, Menu menu) {

        super(fm);
        this.menu = menu;
    }

    @Override
    public Fragment getItem(int position) {

        // menu is 资讯热点，childMenu is 司改动态
        Menu childMenu = menu.getChildren().get(position);
        if (childMenu.getChildren().size() == 0) {
            NewsFragment newsFragment = new NewsFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("menu", childMenu);
            newsFragment.setArguments(bundle);
            return  newsFragment;
        }

        ViewPagerFragmentL2 viewPagerFragmentL2 = new ViewPagerFragmentL2();
        Bundle bundle = new Bundle();
        bundle.putSerializable("menu", childMenu);
        viewPagerFragmentL2.setArguments(bundle);
        return viewPagerFragmentL2;
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
