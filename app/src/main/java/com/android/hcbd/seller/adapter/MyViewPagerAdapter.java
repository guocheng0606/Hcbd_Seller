package com.android.hcbd.seller.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guocheng on 2017/3/21.
 */
public class MyViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> fragmentTitles = new ArrayList<>();
    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public void addFragment(Fragment fragment,String title){
        mFragments.add(fragment);
        fragmentTitles.add(title);
    }
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
    @Override
    public int getCount() {
        return mFragments.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }
}
