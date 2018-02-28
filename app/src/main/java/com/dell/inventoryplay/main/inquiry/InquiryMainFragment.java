package com.dell.inventoryplay.main.inquiry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.dell.inventoryplay.R;
import com.dell.inventoryplay.base.BaseFragment;
import com.dell.inventoryplay.main.MainActivity;
import com.dell.inventoryplay.utils.AppConstants;
import com.dell.inventoryplay.utils.Helper;

/**
 * Created by sasikanta on 11/14/2017.
 * SvcPagerFragment
 */

public class InquiryMainFragment extends BaseFragment {
    public static final String ARG_POSITION = "ARG_POSITION";

    ViewGroup rootView;
    MainActivity activity;
    ViewPager tabViewPager;

    public static InquiryMainFragment newInstance() {
        return new InquiryMainFragment();
    }

    @Override
    protected void setUp(View rootView) {
        tabViewPager = rootView.findViewById(R.id.tabViewPager);
        TabLayout tabLayout = rootView.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(tabViewPager);
        tabViewPager.setAdapter(new InquiryPagerAdapter(getChildFragmentManager(), activity));
        tabViewPager.setPageTransformer(true, new AccordionTransformer());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_inquiry_page, container, false);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        setUp(rootView);


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (MainActivity.currentPage == AppConstants.INQUIRY) {
            menu.clear();
            inflater.inflate(R.menu.home, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (MainActivity.currentPage == AppConstants.INQUIRY) {
            int id = item.getItemId();

            switch (id) {
                case R.id.action_help:
                    showHelp();
                    break;

                default:
                    Helper.getInstance(activity).showToast("In progress", Toast.LENGTH_LONG, AppConstants.TOAST_SUCCESS);


            }
            return super.onOptionsItemSelected(item);
        }
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.tabViewPager + ":" + tabViewPager.getCurrentItem());
        if (null != page) {
            page.onActivityResult(requestCode, resultCode, intent);
        }

    }

}
