package com.dell.inventoryplay.main;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.dell.inventoryplay.R;
import com.dell.inventoryplay.base.BaseFragment;
import com.dell.inventoryplay.utils.AppConstants;
import com.dell.inventoryplay.utils.AppLogger;
import com.dell.inventoryplay.utils.BottomNavigationViewHelper;
import com.dell.inventoryplay.utils.PrefManager;

/**
 * Created by Sasikanta_Sahoo on 12/1/2017.
 * RootFragment
 */

public class RootFragment extends BaseFragment {
    ViewGroup rootView;
    MenuItem prevMenuItem;
    BottomNavigationViewHelper bottomNavigationViewHelper;
    BottomNavigationView bottomNavigationView;
    ViewPager mPager;
    MainActivity activity;
    ImageView mSwipeStart, mSwipeEnd;
    PrefManager mPref;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    public static Fragment newInstance() {
        return new RootFragment();
    }

    public void selectPager(int page) {
        mPager.setCurrentItem(page);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_root_page, container, false);

        setRetainInstance(false);
        mPref = PrefManager.getInstance(activity);
        mPref.load();
        bottomNavigationView = activity.bottomNavigationView;
        bottomNavigationViewHelper = activity.bottomNavigationViewHelper;
        setUp(rootView);
        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Fragment page = activity.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + mPager.getCurrentItem());
        if (requestCode == AppConstants.REQUEST_CODE_CAMERA_ASN) {
            page.onRequestPermissionsResult(AppConstants.REQUEST_CODE_CAMERA_ASN, permissions, grantResults);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        Fragment page = activity.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + mPager.getCurrentItem());
        if (null != page) {
            page.onActivityResult(requestCode, resultCode, intent);
        }

    }

    @Override
    protected void setUp(View view) {
        mPager = rootView.findViewById(R.id.pager);
        mSwipeStart = rootView.findViewById(R.id.start);
        mSwipeEnd = rootView.findViewById(R.id.end);
        RootPagerAdapter mPagerAdapter = new RootPagerAdapter(activity.getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new DepthPageTransformer());

      /*  activity.invalidateFragmentMenus(mPager.getCurrentItem());
        for(int i = 0; i < mPagerAdapter.getCount(); i++){
            mPagerAdapter.getItem(i).setHasOptionsMenu(i == mPager.getCurrentItem());
        }
*/
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                String[] pageTitleArr = getResources().getStringArray(R.array.page_title);


                if (activity.getSupportActionBar() != null) {
                    activity.getSupportActionBar().setTitle(pageTitleArr[MainActivity.currentPage]);
                    activity.getSupportActionBar().setSubtitle("");
                }
                //  activity.turnOnToolbarScrolling();
                // activity.turnOnBottomNavigationViewScrolling();

                activity.manageOrientation();
            }

            @Override
            public void onPageSelected(int position) {
                AppLogger.e("HEALThCHECK:onPageSelected" + position);
                ActionBar actionBar = activity.getSupportActionBar();
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
                MainActivity.currentPage = position;
                if (actionBar != null) {
                    actionBar.setTitle(activity.getResources().getStringArray(R.array.page_title)[MainActivity.currentPage]);
                    actionBar.setSubtitle("");
                }
                //   Fragment page = activity.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + mPager.getCurrentItem());
                // if (null != page) {
                // page.setHasOptionsMenu(true);
                // page.onResume();
                //}
                //  activity.invalidateOptionsMenu();
                // invalidateFragmentMenus(position);
              /*  for(int i = 0; i < mPagerAdapter.getCount(); i++){
                    mPagerAdapter.getItem(i).setHasOptionsMenu(i == position);
                }*/
                //swipeHelp(position);
                activity.invalidateOptionsMenu();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //  mPager.setCurrentItem(3);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_menu1:
                    mPager.setCurrentItem(0);
                    break;
                case R.id.nav_menu2:
                    mPager.setCurrentItem(1);
                    break;
                case R.id.nav_menu3:
                    mPager.setCurrentItem(2);
                    break;
                case R.id.nav_menu4:
                    mPager.setCurrentItem(3);
                    break;
               /* case R.id.nav_menu5:
                    mPager.setCurrentItem(4);
                    break;*/
            }
            return false;
        });
        if (mPref.isFirstTimeHelp()) {
            mPref.setFirstTimeHelp(false);
            new Handler().postDelayed(() -> {

                showHelp();
            }, 2000);
        }

    }


}
