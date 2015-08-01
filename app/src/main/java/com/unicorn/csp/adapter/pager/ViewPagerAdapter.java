package com.unicorn.csp.adapter.pager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.unicorn.csp.fragment.BookFragment;
import com.unicorn.csp.fragment.NewsFragment;
import com.unicorn.csp.fragment.OnlineStoreFragment;
import com.unicorn.csp.fragment.QuestionFragment;
import com.unicorn.csp.fragment.ViewPagerFragmentL2;
import com.unicorn.csp.greendao.Menu;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private Menu menu;

    public ViewPagerAdapter(FragmentManager fm, Menu menu) {

        super(fm);
        this.menu = menu;
    }

    @Override
    public Fragment getItem(int position) {

        // menu 资讯热点，childMenu 司改动态
        Menu childMenu = menu.getChildren().get(position);
        if (childMenu.getChildren().size() == 0) {
            Fragment fragment = getChildFragmentByType(childMenu.getType());
            Bundle bundle = new Bundle();
            bundle.putSerializable("menu", childMenu);
            fragment.setArguments(bundle);
            return fragment;
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

    static public Fragment getChildFragmentByType(String type) {

        switch (type) {
            case "news":
                return new NewsFragment();
            case "book":
                return new BookFragment();
            case "onlineStore":
                return new OnlineStoreFragment();
            case "question":
                return new QuestionFragment();
            default:
                throw new RuntimeException("未知的子菜单类型");
        }
    }

}
