package com.dell.inventoryplay.main.healthcheck;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.dell.inventoryplay.AppConfig;
import com.dell.inventoryplay.InventoryPlayApp;
import com.dell.inventoryplay.R;
import com.dell.inventoryplay.base.BaseFragment;
import com.dell.inventoryplay.main.MainActivity;
import com.dell.inventoryplay.main.common.ChartTableAdapter;
import com.dell.inventoryplay.request.BaseGsonRequest;
import com.dell.inventoryplay.request.HttpRequestObject;
import com.dell.inventoryplay.request.RequestManager;
import com.dell.inventoryplay.response.ChartTableResponse;
import com.dell.inventoryplay.utils.AppConstants;
import com.dell.inventoryplay.utils.AppLogger;
import com.dell.inventoryplay.utils.Helper;
import com.dell.inventoryplay.utils.MovableFloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sasikanta on 11/14/2017.
 * HealthCheckPagerFragment
 */

public class HealthCheckPagerFragment extends BaseFragment {
    public static String ARG_POSITION;
    private ViewGroup rootView;
    private ArrayList<ChartTableResponse.Chart> chartList;
    private MovableFloatingActionButton fab;
    private ChartTableAdapter chartTableAdapter;
    private RecyclerView recyclerView;
    private int num, pageType = 1;
    private MainActivity activity;
    private FrameLayout loader, noInternet;
    private TextView msg;
    private ArrayList<String> titleList;

    public static HealthCheckPagerFragment newInstance() {

        return new HealthCheckPagerFragment();
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
                R.layout.fragment_health_check_sub_page, container, false);
        setRetainInstance(false);
        setHasOptionsMenu(true);
        activity.turnOnBottomNavigationViewScrolling();
        loader = rootView.findViewById(R.id.loader);
        Bundle bundle = getArguments();
        num = bundle != null ? bundle.getInt(ARG_POSITION, 0) : 0;
        msg = rootView.findViewById(R.id.msg);
        noInternet = rootView.findViewById(R.id.noInternet);

        if (AppConfig.useMockJson) {
            String json;
            if (num == 0)
                json = activity.getString(R.string.api_health_check1);
            else
                json = activity.getString(R.string.api_health_check2);

            Gson gson = new Gson();
            ChartTableResponse res = gson.fromJson(json, ChartTableResponse.class);
            chartList = res.getChartList();
            setUp(rootView);
        } else {
            if (Helper.isConnected(activity)) {
                loadData();
            } else {

                noInternet.setVisibility(View.VISIBLE);
            }


            msg.setOnClickListener(view -> {
                if (Helper.isConnected(activity)) {
                    noInternet.setVisibility(View.GONE);
                    loadData();
                } else {
                    Helper.getInstance(activity).showToast(getString(R.string.no_internet), 1, 3);
                    activity.turnOnBottomNavigationViewScrolling();
                }
            });
        }


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (MainActivity.sCurrentPage == AppConstants.HEALTH_CHECK) {
            menu.clear();
            inflater.inflate(R.menu.health_check, menu);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (MainActivity.sCurrentPage == AppConstants.HEALTH_CHECK) {
            int id = item.getItemId();
            switch (id) {
                case R.id.action_share:
                    item.setEnabled(false);
                    AppLogger.e("setEnabled:false");
                    if (chartList != null && chartList.size() > 0) {
                        for (ChartTableResponse.Chart obj : chartList) {
                            obj.setMaximize(2);
                        }
                        recyclerView.setAdapter(new ChartTableAdapter(activity, pageType, chartList));
                        titleList = new ArrayList<>();
                        share();


                    } else {
                        Helper.getInstance(activity).showToast(getString(R.string.no_health_check_share), Toast.LENGTH_SHORT, 4);

                    }
                    AppLogger.e("setEnabled:true");
                    item.setEnabled(true);
                    break;
                case R.id.action_help:
                    showHelp();

                    break;
                case R.id.action_show_all:
                    for (ChartTableResponse.Chart obj : chartList) {
                        obj.setMaximize(2);
                    }
                    recyclerView.setAdapter(new ChartTableAdapter(activity, pageType, chartList));
                    Helper.getInstance(activity).showToast(getString(R.string.health_check_show_all), Toast.LENGTH_LONG, AppConstants.TOAST_SUCCESS);

                    break;
                case R.id.action_hide_all:


                    for (ChartTableResponse.Chart obj : chartList) {
                        obj.setMaximize(1);
                    }
                    recyclerView.setAdapter(new ChartTableAdapter(activity, pageType, chartList));

                    Helper.getInstance(activity).showToast(getString(R.string.health_check_hide_all), Toast.LENGTH_LONG, AppConstants.TOAST_SUCCESS);
                    break;
                case R.id.action_setting:

                    Helper.getInstance(activity).showToast(getString(R.string.in_progress), Toast.LENGTH_LONG, AppConstants.TOAST_SUCCESS);
                    break;
            }
            return super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        chartTableAdapter.notifyDataSetChanged();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public void loadData() {


        loader.setVisibility(View.VISIBLE);
        Single.create((SingleOnSubscribe<Integer>) e -> doInBg())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(o -> updateView());


    }


    public void updateView() {
        setUp(rootView);

    }

    public void doInBg() {
        int days = (num == 0) ? 1 : 7;
        String url = AppConfig.REST_API_HEALTH_CHECK;
        int apiCode = AppConstants.API_CODE_HEALTH_CHECK;
        String inputData = "{'days':'" + days + "'}";
        try {
            HttpRequestObject reqObject = HttpRequestObject.getInstance();
            JSONObject jsonRequest = reqObject.getRequestBody(apiCode, inputData);
            BaseGsonRequest<ChartTableResponse> gsonRequest = new BaseGsonRequest<>(Request.Method.POST, url, ChartTableResponse.class, jsonRequest, InventoryPlayApp.getHeader(),
                    res -> {
                        if (res.getSuccess() && res.getChartList() != null) {
                            chartList = res.getChartList();
                            setUp(rootView);
                        } else if (res.getSuccess() && res.getChartList() == null) {
                            loader.setVisibility(View.GONE);
                            msg.setText(R.string.try_again_health_check);
                            noInternet.setVisibility(View.VISIBLE);
                            activity.turnOnBottomNavigationViewScrolling();
                        } else {
                            loader.setVisibility(View.GONE);
                            String msgStr = res.getMessage();
                            msg.setText(msgStr);
                            noInternet.setVisibility(View.VISIBLE);
                            activity.turnOnBottomNavigationViewScrolling();
                        }
                    }, error -> {
                loader.setVisibility(View.GONE);
                String msgStr = getString(R.string.something_went_wrong);
                msg.setText(msgStr);
                activity.turnOnBottomNavigationViewScrolling();
                noInternet.setVisibility(View.VISIBLE);
            });
            RequestManager.getRequestQueue().add(gsonRequest).setTag(AppConstants.HEALTH_CHECK_TAG + num);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setUp(View view) {

        fab = view.findViewById(R.id.fab);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        chartTableAdapter = new ChartTableAdapter(activity, pageType, chartList);
        recyclerView.setAdapter(chartTableAdapter);
        recyclerView.setHasFixedSize(true);

        fab.setTag(pageType == 1 ? "GraphView" : "TableView");


        fab.setOnClickListener(v -> {
            if (v.getTag().toString().contains("GraphView")) {
                Helper.getInstance(activity).showToast("Tabular Mode", Toast.LENGTH_SHORT, AppConstants.TOAST_DEFAULT);
                fab.setImageResource(R.drawable.ic_toggle_chart);
                v.setTag("TableView");
                pageType = 0;
                recyclerView.setAdapter(new ChartTableAdapter(activity, pageType, chartList));
            } else {
                Helper.getInstance(activity).showToast("Graphical Mode", Toast.LENGTH_SHORT, AppConstants.TOAST_DEFAULT);

                fab.setImageResource(R.drawable.ic_toggle);
                v.setTag("GraphView");
                pageType = 1;
                recyclerView.setAdapter(new ChartTableAdapter(activity, pageType, chartList));
            }

        });
        loader.setVisibility(View.GONE);

    }

    public void getScreenshotFromRecyclerView(RecyclerView view) {
        RecyclerView.Adapter adapter = view.getAdapter();
        if (adapter != null) {
            int size = adapter.getItemCount();
            int textTitleColor = getResources().getColor(R.color.headerTextLight);
            int textTitlePadding = Helper.getInstance(activity).dpToPx(10);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                TextView titleText = holder.itemView.findViewById(R.id.title);

               // TableLayout tableLayout = holder.itemView.findViewById(R.id.tableLayout);
                String titleStr = titleText.getText().toString();
                titleList.add(titleStr);
                TextView tmpTv = new TextView(activity);

                tmpTv.setText(titleStr);
                tmpTv.setPadding(textTitlePadding, textTitlePadding, textTitlePadding, textTitlePadding);
                tmpTv.setTextColor(textTitleColor);
                tmpTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f);
                tmpTv.setTypeface(Typeface.DEFAULT_BOLD);
                Bitmap bitmap1 = Helper.getInstance(activity).loadBitmapFromView(tmpTv);
                titleText.setVisibility(View.GONE);
                String fileName = titleList.get(i).replace(" ", "_") + ".png";

                Bitmap bitmap2 = Helper.getInstance(activity).loadBitmapFromView(holder.itemView);
                Helper.getInstance(activity).combineImages(bitmap1, bitmap2, fileName);

                /*
                if (pageType == 0) {
                    Bitmap bitmap2 = Helper.getInstance(activity).loadBitmapFromView(holder.itemView);
                    Helper.getInstance(activity).combineImages(bitmap1, bitmap2, fileName);
                } else {
                    Bitmap bitmap2 = Helper.getInstance(activity).loadBitmapFromView(holder.itemView);
                    Helper.getInstance(activity).combineImages(bitmap1, bitmap2, fileName);

                }*/
                titleText.setVisibility(View.VISIBLE);

            }


        }

    }


    void share() {
        getScreenshotFromRecyclerView(recyclerView);
        String title = (num == 0) ? getString(R.string.health_check_24hr) : getString(R.string.health_check_7days);
        ArrayList<Uri> uriList = new ArrayList<>();
        for (int i = 0; i < titleList.size(); i++) {
            String fileName = titleList.get(i).replace(" ", "_") + ".png";
            File dir = new File(activity.getFilesDir(), "images");
            //  dir.mkdirs();
            File file = new File(dir, fileName);
            Uri uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", file);
            uriList.add(uri);
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, title);
        intent.putExtra(Intent.EXTRA_STREAM, uriList);
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.share)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, R.string.no_app_available, Toast.LENGTH_SHORT).show();
        }
    }
}
