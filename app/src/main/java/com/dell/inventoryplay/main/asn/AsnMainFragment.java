package com.dell.inventoryplay.main.asn;

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
 * Created by Sasikanta Sahoo on 11/18/2017.
 * AsnMainFragment
 */

public class AsnMainFragment extends BaseFragment {
    ViewGroup rootView;
    MainActivity activity;
    ViewPager tabViewPager;

    public static AsnMainFragment newInstance() {
        return new AsnMainFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_track_asn, container, false);
        activity = (MainActivity) getActivity();
        setRetainInstance(true);
        setHasOptionsMenu(true);
        setUp(rootView);
        return rootView;
    }

  /*  public void loadData() {
        Single.create((SingleOnSubscribe<Integer>) e -> doInBg())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(o -> updateView());

    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.tabViewPager + ":" + tabViewPager.getCurrentItem());
        if (requestCode == AppConstants.REQUEST_CODE_CAMERA_ASN) {
            page.onRequestPermissionsResult(AppConstants.REQUEST_CODE_CAMERA_ASN, permissions, grantResults);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.tabViewPager + ":" + tabViewPager.getCurrentItem());
        if (null != page) {
            page.onActivityResult(requestCode, resultCode, intent);
        }

    }

    /*
        public void updateView() {
            setUp(rootView);
        }

        public void doInBg() {
            String jsonStr = "{'DAYS':'" + 10 + "'}";
            try {
                HttpRequestObject mReqobject = HttpRequestObject.getInstance();
                JSONObject jsonRequest = mReqobject.getRequestBody(AppConstants.HEALTH_CHECK_API, jsonStr);

                ChartTableGsonRequest gsonRequest = new ChartTableGsonRequest(Request.Method.POST, AppConstants.API_BASE_URL, jsonRequest, res -> {

                }, error -> {
                }, ChartTableResponse.class, null);
                RequestManager.getRequestQueue().add(gsonRequest).setTag(AppConstants.TRACK_ASN_TAG);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (MainActivity.currentPage == AppConstants.ASN) {
            menu.clear();
            inflater.inflate(R.menu.home, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (MainActivity.currentPage == AppConstants.ASN) {
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
    @Override
    protected void setUp(View rootView) {
        tabViewPager = rootView.findViewById(R.id.tabViewPager);
        TabLayout tabLayout = rootView.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(tabViewPager);
        tabViewPager.setAdapter(new AsnPagerAdapter(getChildFragmentManager(), getActivity()));
        tabViewPager.setPageTransformer(true, new AccordionTransformer());
        /*
        tabViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ActionBar actionBar = activity.getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(activity.getResources().getStringArray(R.array.page_title)[MainActivity.currentPage]);
                    actionBar.setSubtitle("");
                }

            }

            @Override
            public void onPageSelected(int position) {
                activity.manageOrientation();

                ActionBar actionBar = activity.getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(activity.getResources().getStringArray(R.array.page_title)[MainActivity.currentPage]);
                    actionBar.setSubtitle("");
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        */
    }


}