package com.dell.inventoryplay.main.healthcheck;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dell.inventoryplay.R;

/**
 * Created by sasikanta on 11/14/2017.
 * HealthCheckPagerAdapter
 */

class HealthCheckPagerAdapter extends FragmentPagerAdapter {
    private String[] pageTitle;

    private HealthCheckPagerAdapter(FragmentManager fm, FragmentActivity activity) {
        super(fm);
        pageTitle = activity.getResources().getStringArray(R.array.tab_health_check);
    }
    public static HealthCheckPagerAdapter newInstance(FragmentManager fm, FragmentActivity activity) {

        return new HealthCheckPagerAdapter( fm, activity);
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = HealthCheckPagerFragment.newInstance();
        Bundle args = new Bundle();
        args.putInt(HealthCheckPagerFragment.ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitle[position];

    }

    @Override
    public int getCount() {
        return pageTitle.length;
    }
}
