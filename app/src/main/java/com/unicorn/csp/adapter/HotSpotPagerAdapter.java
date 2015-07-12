package com.unicorn.csp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.unicorn.csp.fragment.TestFragment;


public class HotSpotPagerAdapter extends FragmentStatePagerAdapter {

    public HotSpotPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private String[] titles = {"司改动态", "时政新闻", "贵州要闻", "法院动态"};

    @Override
    public Fragment getItem(int position) {

        return new TestFragment();
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return titles[position];
    }

}
