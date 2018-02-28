package com.dell.inventoryplay.base;

/*
  Created by sasikanta on 11/16/2017.
  BaseActivity
 */

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.utils.BottomNavigationViewBehavior;


public abstract class BaseActivity extends AppCompatActivity implements IView, BaseFragment.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected abstract void setUp();

    public void hideKeyboard() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

    }

    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    public boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        manageOrientation();
    }

    public void turnOffToolbarScrolling() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
        AppBarLayout.LayoutParams toolbarLayoutParams = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
        toolbarLayoutParams.setScrollFlags(0);
        mToolbar.setLayoutParams(toolbarLayoutParams);
        CoordinatorLayout.LayoutParams appBarLayoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        appBarLayoutParams.setBehavior(null);
        appBarLayout.setLayoutParams(appBarLayoutParams);
    }

    public void turnOffBottomNavigationViewScrolling() {

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) bottomNavigation.getLayoutParams();
        params.setBehavior(null);
        bottomNavigation.requestLayout();
        BottomNavigationViewBehavior bottomNavigationViewBehavior = new BottomNavigationViewBehavior();
        bottomNavigationViewBehavior.hideBottomNavigationView(bottomNavigation);

    }

    public void turnOnBottomNavigationViewScrolling() {


        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) bottomNavigation.getLayoutParams();
        params.setBehavior(new BottomNavigationViewBehavior());
        bottomNavigation.setLayoutParams(params);
        BottomNavigationViewBehavior bottomNavigationViewBehavior = new BottomNavigationViewBehavior();
        bottomNavigationViewBehavior.showBottomNavigationView(bottomNavigation);
    }

    /*
        public void turnOnToolbarScrolling() {

            Toolbar mToolbar = findViewById(R.id.toolbar);
            AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
            AppBarLayout.LayoutParams toolbarLayoutParams = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
            toolbarLayoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
            mToolbar.setLayoutParams(toolbarLayoutParams);
            CoordinatorLayout.LayoutParams appBarLayoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            appBarLayoutParams.setBehavior(new AppBarLayout.Behavior());
            appBarLayout.setLayoutParams(appBarLayoutParams);
        }
    */
    public void onFragmentDetached(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            fragmentManager
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .remove(fragment)
                    .commitNow();
        }
    }

    public void manageOrientation() {

        /*if (Helper.getInstance(this).getOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
         //   turnOffToolbarScrolling();
          //  turnOffBottomNavigationViewScrolling();
        } else {
           // turnOnToolbarScrolling();
           // turnOnBottomNavigationViewScrolling();
        }*/

    }
}
