package com.unicorn.csp.adapter.viewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.unicorn.csp.fragment.BookCityFragment;
import com.unicorn.csp.fragment.BookFragment;
import com.unicorn.csp.fragment.ChartFragment;
import com.unicorn.csp.fragment.MyShelfFragment;
import com.unicorn.csp.fragment.news.NewsFragment;
import com.unicorn.csp.fragment.question.QuestionFragment;
import com.unicorn.csp.fragment.viewpager.ViewPagerFragmentL1;
import com.unicorn.csp.fragment.viewpager.ViewPagerFragmentL2;
import com.unicorn.csp.greendao.Menu;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private Menu menu;

    public ViewPagerAdapter(FragmentManager fm, Menu menu) {

        super(fm);
        this.menu = menu;
    }

    @Override
    public Fragment getItem(int position) {

        Menu childMenu = menu.getChildren().get(position);
        return getFragmentByMenu(childMenu, false);
    }

    @Override
    public int getCount() {
        return menu.getChildren().size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return menu.getChildren().get(position).getName();
    }

    static public Fragment getFragmentByMenu(Menu menu, boolean inMainActivity) {

        Fragment fragment;
        if (menu.getChildren().size() != 0) {
            fragment = inMainActivity ? new ViewPagerFragmentL1() : new ViewPagerFragmentL2();
        } else {
            fragment = getSimpleFragmentByMenu(menu);
        }
        addMenuArgForFragment(menu, fragment);
        return fragment;
    }

    static private Fragment getSimpleFragmentByMenu(Menu menu) {

        switch (menu.getType()) {
            case "news":
                return new NewsFragment();
            case "book":
                return new BookFragment();
            case "onlineStore":
                return new BookCityFragment();
            case "question":
                return new QuestionFragment();
            // todo 确认这个，在服务端
            case "myShelf":
                return new MyShelfFragment();
            case"chart":
                return new ChartFragment();
            default:
                throw new RuntimeException("未知的菜单类型");
        }
    }

    static private void addMenuArgForFragment(Menu menu, Fragment fragment) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("menu", menu);
        fragment.setArguments(bundle);
    }

}