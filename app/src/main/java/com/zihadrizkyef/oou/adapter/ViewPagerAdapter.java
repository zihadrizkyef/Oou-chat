package com.zihadrizkyef.oou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 05/07/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragment;
    private List<String> title;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragment, List<String> title) {
        super(fm);

        this.fragment = fragment;
        this.title = title;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragment.get(position);
    }

    @Override
    public int getCount() {
        return fragment.size();
    }
}
