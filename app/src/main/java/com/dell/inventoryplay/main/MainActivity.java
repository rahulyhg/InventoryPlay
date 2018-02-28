package com.dell.inventoryplay.main;

import android.app.SearchManager;
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
import com.dell.inventoryplay.utils.Connectivity;
import com.dell.inventoryplay.utils.HelpDialog;
import com.dell.inventoryplay.utils.Helper;

import timber.log.Timber;

public class MainActivity extends BaseActivity
        implements IMainView {
    CharSequence aTitle = "", aSubTitle = "";
    public BottomNavigationViewHelper bottomNavigationViewHelper;
    public BottomNavigationView bottomNavigationView;
    public MainPresenter mPresenter;
    public static int currentPage = 0;
    private static long sBackPressed;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    Fragment frag;
    private boolean mToolBarNavigationListenerIsRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = MainPresenter.newInstance();
        //noinspection unchecked
        mPresenter.onAttach(MainActivity.this);
        setUp();
        setupNavMenu();
        setupBottomNavMenuWithPager();
        handleIntent(getIntent());


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
        frag = fm.findFragmentByTag(AppConstants.FRAG_MAIN_PAGER_TAG);
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
        if (Connectivity.isConnected(this) && !Connectivity.isConnectedFast(this)) {
            userName.setText(i.getStringExtra("USER_NAME") + ":slow connection");
        } else if (!Connectivity.isConnected(this)) {
            userName.setText(i.getStringExtra("USER_NAME") + ":no connection");
        } else {
            userName.setText(i.getStringExtra("USER_NAME"));
        }

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
            aSubTitle = actionBar != null ? actionBar.getSubtitle() : null;
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
                // actionBar.setSubtitle(aSubTitle);
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

        //  DrawerLayout drawer = findViewById(R.id.drawerLayout);
        int fragCnt = getSupportFragmentManager().getBackStackEntryCount();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else if (fragCnt > 1) {
            onFragmentDetached(getSupportFragmentManager().getBackStackEntryAt(fragCnt - 1).getName());

            if (fragCnt == 2) {
                enableBackButtonViews(false);
                //  drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                turnOnBottomNavigationViewScrolling();
            } else {
                //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                enableBackButtonViews(true);
            }
            manageOrientation();
            getSupportFragmentManager().popBackStack();
        } else {

            if (sBackPressed + 2000 > System.currentTimeMillis()) {
                // super.onBackPressed();
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
        handleIntent(intent);
    }
/*
    public void doAnimate() {
        final Dialog diHomeChart_instr = new Dialog(this);
        diHomeChart_instr.requestWindowFeature(Window.FEATURE_NO_TITLE);
        diHomeChart_instr.setCancelable(true);
        diHomeChart_instr.setContentView(R.layout.dialog_instruc_scanbar);
        LottieAnimationView laCartDialog = (LottieAnimationView) diHomeChart_instr.findViewById(R.id.animation_view);
        laCartDialog.setAnimation("animated_graph.json");
        laCartDialog.playAnimation();
        laCartDialog.loop(true);
        Button btnletme = diHomeChart_instr.findViewById(R.id.btnletme);
        btnletme.setOnClickListener(v -> diHomeChart_instr.dismiss());
        diHomeChart_instr.show();

    }*/

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //  Toast.makeText(this, "hi" + query, Toast.LENGTH_SHORT).show();
            //doMySearch(query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return false;
    }
    /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
           switch (currentPage){
               case 0:
                   inflater.inflate(R.menu.main, menu);
                   search(menu);
                   break;
               case 1:
                   inflater.inflate(R.menu.health_check, menu);
                   break;
               case 2:
                   inflater.inflate(R.menu.health_check, menu);
                   break;
               case 3:
                   inflater.inflate(R.menu.health_check, menu);
                   break;
               case 4:
                   inflater.inflate(R.menu.main, menu);
                   search(menu);
                   break;
           }
            AppLogger.e("POSITION %d",currentPage);

            return true;
        }
    private void search(Menu menu){
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
    }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // int id = item.getItemId();
            return super.onOptionsItemSelected(item);
        }
    */

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
            Timber.e("MENUID::" + item.getItemId());
            switch (item.getItemId()) {
                case R.id.nav_menu1:
                    item.setChecked(true);
                    Timber.e("MENUID::HOME" + item.getItemId());
                    drawer.setSelected(true);
                    navigatePage(0);


                    break;
                case R.id.nav_menu2:
                    item.setChecked(true);
                    Timber.e("MENUID::INQUIRY" + item.getItemId());
                    navigatePage(1);
                    break;
                case R.id.nav_menu3:
                    item.setChecked(true);
                    Timber.e("MENUID::ASN" + item.getItemId());
                    navigatePage(2);
                    break;
                case R.id.nav_menu4:
                    item.setChecked(true);
                    Timber.e("MENUID::HEALTHCHECK" + item.getItemId());
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
                    Timber.e("MENUID::FEEDBACK" + item.getItemId());
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