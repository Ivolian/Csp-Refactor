package com.unicorn.csp.adapter.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.unicorn.csp.fragment.HotSpotFragment;
import com.unicorn.csp.fragment.TestFragment;
import com.unicorn.csp.greendao.Menu;


public class HotSpotPagerAdapter extends FragmentStatePagerAdapter {

    private Menu menu;

    public HotSpotPagerAdapter(FragmentManager fm,Menu menu) {

        super(fm);
        this.menu = menu;
    }

    @Override
    public Fragment getItem(int position) {

        Menu subMenu = menu.getChildren().get(position);
            if (subMenu.getChildren().size()!=0){
                HotSpotFragment hotSpotFragment =new HotSpotFragment();
                hotSpotFragment.setMenu(subMenu);
                return  hotSpotFragment;
            }else {
                return new TestFragment();
            }



//        if (position != 0) {

//        }
//        return new NewsFragment();
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
