package com.dell.inventoryplay.main.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by sasikanta on 11/14/2017.
 * HomePagerAdapter
 */

public class HomePagerAdapter extends FragmentPagerAdapter {


    public HomePagerAdapter(FragmentManager fm, FragmentActivity activity) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle args;
        switch (position) {
            case 0:
                fragment = HomeStaticFragment.newInstance();
                args = new Bundle();
                args.putInt(HomeStaticFragment.ARG_POSITION, position);
                fragment.setArguments(args);
                break;
            case 1:
                fragment = HomeGraphFragment.newInstance();
                args = new Bundle();
                args.putInt(HomeGraphFragment.ARG_POSITION, position);
                fragment.setArguments(args);
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "";

    }

    @Override
    public int getCount() {
        return 1;
    }
}
