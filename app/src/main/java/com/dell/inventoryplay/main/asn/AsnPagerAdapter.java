package com.dell.inventoryplay.main.asn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dell.inventoryplay.R;

/**
 * Created by sasikanta on 11/14/2017.
 * AsnPagerAdapter
 */

class AsnPagerAdapter extends FragmentPagerAdapter {
    private String[] pageTitle;

    AsnPagerAdapter(FragmentManager fm, FragmentActivity activity) {
        super(fm);
        pageTitle = activity.getResources().getStringArray(R.array.tab_track_asn);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle args;
        switch (position) {
            case 0:
                fragment = AsnStatisticsFragment.newInstance();
                args = new Bundle();
                args.putInt(AsnStatisticsFragment.ARG_POSITION, position);
                fragment.setArguments(args);
                break;
            case 1:
                fragment = AsnTrackFragment.newInstance();
                args = new Bundle();
                args.putInt(AsnTrackFragment.ARG_POSITION, position);
                fragment.setArguments(args);
                break;
        }
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
