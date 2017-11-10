package com.nganter.com.pesanan;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by aji on 10/6/2017.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> mTitles;
    private ArrayList<Fragment> mFragments;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        mTitles = new ArrayList<>(3);
        mFragments = new ArrayList<>(3);
    }

    public void add(Fragment fragment, String title) {
        mTitles.add(title);
        mFragments.add(fragment);
    }

    @Override public int getCount() {
        return mFragments.size();
    }

    @Override public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
