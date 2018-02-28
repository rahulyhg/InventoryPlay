package com.dell.inventoryplay.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dell.inventoryplay.main.asn.AsnMainFragment;
import com.dell.inventoryplay.main.healthcheck.HealthCheckMainFragment;
import com.dell.inventoryplay.main.home.HomeStaticFragment;
import com.dell.inventoryplay.main.inquiry.InquiryMainFragment;

/**
 * Created by sasikanta on 11/14/2017.
 * RootPagerAdapter
 */

public class RootPagerAdapter extends FragmentPagerAdapter {
    RootPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        //  return HealthCheckMainFragment.newInstance();

        switch (position) {
            case 0:
                // return DummyPagerFragment.newInstance();
                //    return AssistantPagerFragment.newInstance();
                //    return HomeMainFragment.newInstance();
                return HomeStaticFragment.newInstance();
            case 1:
                //  return AssistantPagerFragment.newInstance();
                // return SVGFragment.newInstance();
                return HealthCheckMainFragment.newInstance();
            case 2:
                //    return AssistantPagerFragment.newInstance();
                return InquiryMainFragment.newInstance();
            case 3:

                return AsnMainFragment.newInstance();
          /*  case 4:
                // return SVGFragment.newInstance();
                return AssistantPagerFragment.newInstance();
                */
        }
        return null;


    }

    @Override
    public int getCount() {
        return 4;
    }
}
