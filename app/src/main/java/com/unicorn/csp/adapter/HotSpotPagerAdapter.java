package com.unicorn.csp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.unicorn.csp.fragment.TestFragment;


public class HotSpotPagerAdapter extends FragmentStatePagerAdapter {

    public HotSpotPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return new TestFragment();
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "司改动态";
            case 1:
                return "时政新闻";
            case 2:
                return "贵州要闻";
            case 3:
                return "法院动态";
            default:
                throw new RuntimeException("position 过大");
        }
    }

}
