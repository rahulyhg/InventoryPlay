package com.dell.inventoryplay.main.healthcheck;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.dell.inventoryplay.R;
import com.dell.inventoryplay.base.BaseFragment;
import com.dell.inventoryplay.main.MainActivity;

/**
 * Created by Sasikanta Sahoo on 11/18/2017.
 * HealthCheckMainFragment
 */

public class HealthCheckMainFragment extends BaseFragment {
    MainActivity activity;
    Resources res;
    ViewPager tabViewPager;

    public static HealthCheckMainFragment newInstance() {
        return new HealthCheckMainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_health_check, container, false);
        activity = (MainActivity) getActivity();
        res = activity != null ? activity.getResources() : null;
        setRetainInstance(true);
        setUp(rootView);
        return rootView;
    }
/*
    public void scrollPage() {
        int currPage = tabViewPager.getCurrentItem();
        HealthCheckPagerFragment page1 = (HealthCheckPagerFragment)activity.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.tabViewPager + ":0");
        HealthCheckPagerFragment page2 = (HealthCheckPagerFragment)activity.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.tabViewPager + ":1");

        if (currPage == 0) {
            page2.scrollPage();
        } else {
            page2.scrollPage();
        }


    }
    */

    @Override
    protected void setUp(View rootView) {
        tabViewPager = rootView.findViewById(R.id.tabViewPager);
        TabLayout tabLayout = rootView.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(tabViewPager);
        tabViewPager.setAdapter(HealthCheckPagerAdapter.newInstance(getChildFragmentManager(), activity));
        tabViewPager.setPageTransformer(true, new AccordionTransformer());
    }

}