package com.dell.inventoryplay.main.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.base.BaseFragment;
import com.dell.inventoryplay.main.MainActivity;
import com.dell.inventoryplay.utils.AppConstants;
import com.dell.inventoryplay.utils.Helper;

/**
 * Created by sasikanta on 11/14/2017.
 * HomeMainFragment
 */


public class HomeMainFragment extends BaseFragment {
    public static final String ARG_POSITION = "ARG_POSITION";
    public HomePagerAdapter viewPagerAdapter;
    public ViewGroup rootView;
    @SuppressWarnings("unused")
    MainActivity activity;

    @Override
    protected void setUp(View view) {
        ViewPager innerPager = rootView.findViewById(R.id.innerPager);
        ImageView home_indicator1 = rootView.findViewById(R.id.home_indicator1);
        ImageView home_indicator2 = rootView.findViewById(R.id.home_indicator2);

        viewPagerAdapter = new HomePagerAdapter(getChildFragmentManager(), getActivity());
        innerPager.setAdapter(viewPagerAdapter);
        innerPager.setOffscreenPageLimit(0);
        innerPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        home_indicator1.setImageResource(R.drawable.ic_home_indicator_sel);
                        home_indicator2.setImageResource(R.drawable.ic_home_indicator_unsel);
                        break;

                    case 1:
                        home_indicator2.setImageResource(R.drawable.ic_home_indicator_sel);
                        home_indicator1.setImageResource(R.drawable.ic_home_indicator_unsel);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    public static HomeMainFragment newInstance() {
        return new HomeMainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_main_page, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        setUp(rootView);
        return rootView;
    }

    /*
        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                setHasOptionsMenu(true);
            } else {
                setHasOptionsMenu(false);
            }
        }
    */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (MainActivity.currentPage == AppConstants.HOME) {
            menu.clear();
            inflater.inflate(R.menu.home, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (MainActivity.currentPage == AppConstants.HOME) {
            int id = item.getItemId();

            switch (id) {
                case R.id.action_help:
                    showHelp();
                    // Helper.getInstance(activity).showToast("In progress", Toast.LENGTH_LONG, AppConstants.TOAST_SUCCESS);
                    break;



            }
            return super.onOptionsItemSelected(item);
        }
        return false;
    }


/*
    private void invalidateFragmentMenus(int position) {
        for (int i = 0; i < viewPagerAdapter.getCount(); i++) {
            viewPagerAdapter.getItem(i).setHasOptionsMenu(i == position);
        }
        activity.invalidateOptionsMenu(); //or respectively its support method.
    }*/
}
