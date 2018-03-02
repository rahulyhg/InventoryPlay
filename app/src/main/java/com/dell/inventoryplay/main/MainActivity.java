package com.dell.inventoryplay.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.alarm.AlarmReceiver;
import com.dell.inventoryplay.base.BaseActivity;
import com.dell.inventoryplay.utils.AppConstants;
import com.dell.inventoryplay.utils.AppLogger;
import com.dell.inventoryplay.utils.BottomNavigationViewBehavior;
import com.dell.inventoryplay.utils.BottomNavigationViewHelper;
import com.dell.inventoryplay.utils.HelpDialog;
import com.dell.inventoryplay.utils.Helper;

public class MainActivity extends BaseActivity
        implements IMainView {
    private CharSequence aTitle;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private boolean mToolBarNavigationListenerIsRegistered = false;
    public BottomNavigationViewHelper bottomNavigationViewHelper;
    public BottomNavigationView bottomNavigationView;
    private static long sBackPressed;
    public static int sCurrentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainPresenter mPresenter = MainPresenter.newInstance();
        //noinspection unchecked
        mPresenter.onAttach(MainActivity.this);
        setUp();
        setupNavMenu();
        setupBottomNavMenuWithPager();

        AlarmReceiver alarm = new AlarmReceiver();
        alarm.setAlarm(getApplicationContext());
    }


    private void setupBottomNavMenuWithPager() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationViewHelper = BottomNavigationViewHelper.newInstance();
        bottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());
        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentByTag(AppConstants.FRAG_MAIN_PAGER_TAG);
        FragmentTransaction ft = fm.beginTransaction();
        if (frag == null)
            frag = RootFragment.newInstance();
        ft.replace(R.id.constraintLayout, frag, AppConstants.FRAG_MAIN_PAGER_TAG);
        ft.addToBackStack(AppConstants.FRAG_MAIN_PAGER_TAG);
        ft.commit();

    }

    @Override
    protected void setUp() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navView = findViewById(R.id.navView);
        View headerLayout =
                navView.inflateHeaderView(R.layout.nav_header_main);
        //panel = headerLayout.findViewById(R.id.viewId);
        TextView userName = headerLayout.findViewById(R.id.userName);
        Intent i = getIntent();
        AppLogger.e("USER_NAME" + i.getStringExtra("USER_NAME"));
        String userNameStr = i.getStringExtra("USER_NAME");
        userName.setText(userNameStr);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideKeyboard();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }

    public void enableBackButtonViews(boolean enable) {
        ActionBar actionBar = getSupportActionBar();
        if (enable) {
            aTitle = actionBar != null ? actionBar.getTitle() : null;
            toggle.setDrawerIndicatorEnabled(false);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            if (!mToolBarNavigationListenerIsRegistered) {
                toggle.setToolbarNavigationClickListener(v -> onBackPressed());
                mToolBarNavigationListenerIsRegistered = true;
            }

        } else {
            if (actionBar != null) {
                actionBar.setTitle(aTitle);
                actionBar.setSubtitle("");
                actionBar.setDisplayHomeAsUpEnabled(false);
            }


            toggle.setDrawerIndicatorEnabled(true);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toggle.setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;
        }

    }

    @Override
    public void onBackPressed() {
        int fragCnt = getSupportFragmentManager().getBackStackEntryCount();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else if (fragCnt > 1) {
            onFragmentDetached(getSupportFragmentManager().getBackStackEntryAt(fragCnt - 1).getName());

            if (fragCnt == 2) {
                enableBackButtonViews(false);
                turnOnBottomNavigationViewScrolling();
            } else {
                enableBackButtonViews(true);
            }
            manageOrientation();
            getSupportFragmentManager().popBackStack();
        } else {

            if (sBackPressed + 2000 > System.currentTimeMillis()) {
                finish();
            } else {
                Helper.getInstance(this).showToast(getString(R.string.press_again), Toast.LENGTH_SHORT, AppConstants.TOAST_ERROR);
            }
            sBackPressed = System.currentTimeMillis();
        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return false;
    }

    public void navigatePage(int page) {
        FragmentManager fm = getSupportFragmentManager();
        RootFragment frag = (RootFragment) fm.findFragmentByTag(AppConstants.FRAG_MAIN_PAGER_TAG);
        if (frag != null)
            frag.selectPager(page);
    }

    void setupNavMenu() {
        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(item -> {
            // DrawerLayout drawer = findViewById(R.id.drawerLayout);
            drawer.closeDrawer(GravityCompat.START);
            switch (item.getItemId()) {
                case R.id.nav_menu1:
                    item.setChecked(true);
                    drawer.setSelected(true);
                    navigatePage(0);


                    break;
                case R.id.nav_menu2:
                    item.setChecked(true);
                    navigatePage(1);
                    break;
                case R.id.nav_menu3:
                    item.setChecked(true);
                    navigatePage(2);
                    break;
                case R.id.nav_menu4:
                    item.setChecked(true);
                    navigatePage(3);
                    break;
                case R.id.navHelp:
                    Bundle bundle = new Bundle();
                    bundle.putString("PAGE", "HELP_PAGE");
                    String tag = "help";
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    final HelpDialog filterDialog = HelpDialog.newInstance();
                    filterDialog.setArguments(bundle);
                    filterDialog.show(ft, tag);
                    break;
                case R.id.navFeedBack:
                    item.setChecked(true);
                    break;
            }
            return false;
        });


    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Fragment page = getSupportFragmentManager().findFragmentByTag(AppConstants.FRAG_MAIN_PAGER_TAG);

        if (null != page) {
            page.onActivityResult(requestCode, resultCode, intent);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Fragment page = getSupportFragmentManager().findFragmentByTag(AppConstants.FRAG_MAIN_PAGER_TAG);
        if (requestCode == AppConstants.REQUEST_CODE_CAMERA_ASN) {
            page.onRequestPermissionsResult(AppConstants.REQUEST_CODE_CAMERA_ASN, permissions, grantResults);
        }
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {
        super.onFragmentDetached(tag);
    }
}