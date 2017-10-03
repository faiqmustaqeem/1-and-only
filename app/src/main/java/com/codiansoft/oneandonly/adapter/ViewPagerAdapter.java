package com.codiansoft.oneandonly.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codiansoft.oneandonly.fragment.ActiveAdsFragment;
import com.codiansoft.oneandonly.fragment.InactiveAdsFragment;
import com.codiansoft.oneandonly.fragment.PendingAdsFragment;

/**
 * Created by Codiansoft on 8/1/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new ActiveAdsFragment();
        }
        else if (position == 1)
        {
            fragment = new PendingAdsFragment();
        }
        else if (position == 2)
        {
            fragment = new InactiveAdsFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "ACTIVE";
        }
        else if (position == 1)
        {
            title = "PENDING";
        }
        else if (position == 2)
        {
            title = "INACTIVE";
        }
        return title;
    }
}