package com.dell.inventoryplay.main.healthcheck;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.dell.inventoryplay.AppConfig;
import com.dell.inventoryplay.DellApp;
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
import java.io.FileOutputStream;
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
    ArrayList<Bitmap> bitmapList;
    ViewGroup rootView;
    ArrayList<ChartTableResponse.Chart> chartList;
    MovableFloatingActionButton fab;
    ChartTableAdapter chartTableAdapter;
    RecyclerView recyclerView;
    int num;
    int pageType = 1;
    MainActivity activity;
    FrameLayout loader, noInternet;
    TextView msg;
    ArrayList<String> titleList;

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
               /* noInternet.setOnClickListener(view -> {
                    if (Helper.isConnected(activity)) {
                        noInternet.setVisibility(View.GONE);
                        loadData();
                    } else {
                        Helper.getInstance(activity).showToast("No internet connection", 1, 3);
                    }
                });
                */
            }


            msg.setOnClickListener(view -> {
                if (Helper.isConnected(activity)) {
                    noInternet.setVisibility(View.GONE);
                    loadData();
                } else {
                    Helper.getInstance(activity).showToast("No internet connection", 1, 3);
                    activity.turnOnBottomNavigationViewScrolling();
                }
            });
        }


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (MainActivity.currentPage == AppConstants.HEALTH_CHECK) {
            menu.clear();
            inflater.inflate(R.menu.health_check, menu);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (MainActivity.currentPage == AppConstants.HEALTH_CHECK) {
            int id = item.getItemId();
            switch (id) {
                case R.id.action_share:
                    item.setEnabled(false);
                    AppLogger.e("setEnabled:false");
                    // Helper.getInstance(activity).showToast("Starting share app..", Toast.LENGTH_LONG, 4);
                    if (chartList != null && chartList.size() > 0) {
                        for (ChartTableResponse.Chart obj : chartList) {
                            obj.setMaximize(2);
                        }
                        recyclerView.setAdapter(new ChartTableAdapter(activity, pageType, chartList));
                        bitmapList = new ArrayList<>();


                        titleList = new ArrayList<>();

                      //  Helper.getInstance(activity).showToast("Opening Share Widget",Toast.LENGTH_LONG,4);
                        share();



                    } else {
                        Helper.getInstance(activity).showToast("No health check found to share", Toast.LENGTH_SHORT, 4);

                    }
                    AppLogger.e("setEnabled:true");
                    item.setEnabled(true);
                    break;
                case R.id.action_help:
                    showHelp();

                    break;
                case R.id.action_show_all:

                    //  loadImageFromStorage();
                    for (ChartTableResponse.Chart obj : chartList) {
                        obj.setMaximize(2);
                    }
                    recyclerView.setAdapter(new ChartTableAdapter(activity, pageType, chartList));
                    Helper.getInstance(activity).showToast("Maximized all health check.", Toast.LENGTH_LONG, AppConstants.TOAST_SUCCESS);

                    break;
                case R.id.action_hide_all:


                    for (ChartTableResponse.Chart obj : chartList) {
                        obj.setMaximize(1);
                    }
                    recyclerView.setAdapter(new ChartTableAdapter(activity, pageType, chartList));

                    Helper.getInstance(activity).showToast("Minimized all health check.", Toast.LENGTH_LONG, AppConstants.TOAST_SUCCESS);
                    break;
                case R.id.action_setting:

                    Helper.getInstance(activity).showToast("In progress", Toast.LENGTH_LONG, AppConstants.TOAST_SUCCESS);
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
        //  String url = AppConstants.API_BASE_URL + "healthcheck";
        int days = (num == 0) ? 1 : 7;
        String url = AppConfig.REST_API_HEALTH_CHECK;
        int apiCode = AppConstants.API_CODE_HEALTH_CHECK;
        String inputData = "{'days':'" + days + "'}";
        try {
            HttpRequestObject reqObject = HttpRequestObject.getInstance();
            JSONObject jsonRequest = reqObject.getRequestBody(apiCode, inputData);
            BaseGsonRequest<ChartTableResponse> gsonRequest = new BaseGsonRequest<>(Request.Method.POST, url, ChartTableResponse.class, jsonRequest, DellApp.getHeader(),
                    res -> {
                        if (res.getSuccess() && res.getChartList() != null) {
                            chartList = res.getChartList();
                            setUp(rootView);
                        } else if (res.getSuccess() && res.getChartList() == null) {
                            loader.setVisibility(View.GONE);
                            String msgStr = res.getMessage();
                            msg.setText("No health check found.try again later");
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
                String msgStr = "Something went wrong, try again in few seconds";
                AppLogger.e("HEALTHCHECK:" + msgStr);
                msg.setText(msgStr);
                activity.turnOnBottomNavigationViewScrolling();
                noInternet.setVisibility(View.VISIBLE);
            });
            RequestManager.getRequestQueue().add(gsonRequest).setTag(AppConstants.HEALTH_CHECK_TAG + num);

/*
            try {

            HttpRequestObject reqObject = HttpRequestObject.getInstance();
            JSONObject jsonRequest = reqObject.getRequestBody(apiCode, inputData);
            ChartTableGsonRequest gsonRequest = new ChartTableGsonRequest(Request.Method.POST, url, jsonRequest, res -> {
                if (((ChartTableResponse) res).getSuccess()) {
                    chartList = ((ChartTableResponse) res).getChartList();
                    setUp(rootView);
                } else {
                    loader.setVisibility(View.GONE);
                    String msgStr = ((ChartTableResponse) res).getMessage();
                    msg.setText("" + msgStr);
                    noInternet.setVisibility(View.VISIBLE);
                }
            }, error -> {
                loader.setVisibility(View.GONE);
                String msgStr = "Sorry, unable to process response now";
                msg.setText(msgStr);
                noInternet.setVisibility(View.VISIBLE);
            }, ChartTableResponse.class, DellApp.getHeader());
            RequestManager.getRequestQueue().add(gsonRequest).setTag(AppConstants.HEALTH_CHECK_TAG + num);

            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            AppLogger.e("HEALThCHECK:setUserVisibleHint"+isVisibleToUser+MainActivity.currentPage);
            if (isVisibleToUser) {
               setHasOptionsMenu(true);
            } else {
                setHasOptionsMenu(false);
            }
        }
    */
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


    public void takeScreenshot() {
        // return getScreenBitmap(); // Get the bitmap
        //  return getRecyclerViewScreenshot();
      //  return getScreenshotFromRecyclerView(recyclerView);
        // Save it to the external storage device.
    }

    public Bitmap getScreenBitmap() {
        View v = rootView.findViewById(R.id.constraintLayout);
        v.setDrawingCacheEnabled(true);
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false); // clear drawing cache
        return b;
    }


    public Bitmap getRecyclerViewScreenshot() {
        RecyclerView view = rootView.findViewById(R.id.recyclerView);
        int size = view.getAdapter().getItemCount();
        RecyclerView.ViewHolder holder = view.getAdapter().createViewHolder(view, 0);
        view.getAdapter().onBindViewHolder(holder, 0);
        holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());

        //  Bitmap bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), holder.itemView.getMeasuredHeight()*size ,
        //        Bitmap.Config.ARGB_8888);
        Bitmap bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), holder.itemView.getMeasuredHeight() * size,
                Bitmap.Config.ARGB_8888);


        Canvas bigCanvas = new Canvas(bigBitmap);
        bigCanvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        int iHeight = 0;
        holder.itemView.setDrawingCacheEnabled(true);
        holder.itemView.buildDrawingCache();
        bigCanvas.drawBitmap(holder.itemView.getDrawingCache(), 0f, iHeight, paint);
        holder.itemView.setDrawingCacheEnabled(false);
        holder.itemView.destroyDrawingCache();
        iHeight += holder.itemView.getMeasuredHeight();
      /* for (int i = 1; i < size; i++) {
            holder = view.getAdapter().createViewHolder(view, 0);
            view.getAdapter().onBindViewHolder(holder, i);
            holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());

            holder.itemView.setDrawingCacheEnabled(true);
            holder.itemView.buildDrawingCache();
            bigCanvas.drawBitmap(holder.itemView.getDrawingCache(), 0f, iHeight, paint);
            holder.itemView.setDrawingCacheEnabled(false);
            holder.itemView.destroyDrawingCache();
            iHeight += holder.itemView.getMeasuredHeight();
        }*/

        return bigBitmap;
    }

    void share(ArrayList<Bitmap> bitmapList) {
        int i = 0;
        String title = (num == 0) ? "LAST 24 HOURS HEALTH CHECK REPORT" : "LAST 7 DAYS HEALTH CHECK REPORT";
        ArrayList<Uri> uriList = new ArrayList<>();

        for (Bitmap bitmap : bitmapList) {

            String fileName = titleList.get(i).replace(" ", "_") + "_report.png";
            File dir = new File(activity.getFilesDir(), "images");
            dir.mkdirs();
            File file = new File(dir, fileName);
            try {
                FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Uri uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", file);
            uriList.add(uri);
            i++;
        }
      /*  String fileName = "share.png";
        File dir = new File(activity.getFilesDir(), "images");
        dir.mkdirs();
        File file = new File(dir, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();

        String fname = "HEALTHCHECK_2.jpg";
        File file1 = new File(myDir, fname);
        try {
            FileOutputStream fOut = new FileOutputStream(file1);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        int file_size = Integer.parseInt(String.valueOf(file1.length() / 1024));
        AppLogger.e("ByteCountKB::" + file_size);
        */
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        //intent.setDataAndType(uri, activity.getContentResolver().getType(uri));
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, title);
        intent.putExtra(android.content.Intent.EXTRA_CC, new String[]{"Reddy_Chandrasekhar@Dell.com", "Raghavendra_Ganapath@Dell.com", "D_Gokulakrishnan@Dell.com", "Anuradha_Hariharasub@Dell.com", "Pardhasaradhi_Vajja@Dell.com", "Sabu_Philip@DellTeam.com"});
        //intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"sasikanta_sahoo@dellteam.com"});

        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
        try {
            startActivity(Intent.createChooser(intent, "Share"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "No App Available", Toast.LENGTH_SHORT).show();
        }


       /* Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        //intent.setDataAndType(uri, activity.getContentResolver().getType(uri));
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        //  intent.putExtra(Intent.EXTRA_TEXT, title);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"sasikanta_sahoo@dellteam.com"});
*/


    }

    public void getScreenshotFromRecyclerView(RecyclerView view) {
        RecyclerView.Adapter adapter = view.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null) {
            int size = adapter.getItemCount();
            //  int height = 0;
            // Paint paint = new Paint();
            // int iHeight = 0;
            // final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            // final int cacheSize = maxMemory / 8;
            //LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            // ArrayList<Integer> heightList = new ArrayList<>();
            // String pageTitle = (pageType == 0) ? " Tabular Report" : " Graphical Report";
            int textTitleColor = getResources().getColor(R.color.headerTextLight);
            int textTitlePadding = Helper.getInstance(activity).dpToPx(10);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                TextView titleText = holder.itemView.findViewById(R.id.title);

                HorizontalScrollView tableLayout = holder.itemView.findViewById(R.id.horizontalView);
                String titleStr = titleText.getText().toString();
                titleList.add(titleStr);
                TextView tmpTv = new TextView(activity);
                String title = titleStr;// + pageTitle;
                tmpTv.setText(title);
                tmpTv.setPadding(textTitlePadding, textTitlePadding, textTitlePadding, textTitlePadding);
                tmpTv.setTextColor(textTitleColor);
                tmpTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f);
                tmpTv.setTypeface(Typeface.DEFAULT_BOLD);
                Bitmap bitmap1 = Helper.getInstance(activity).loadBitmapFromView(tmpTv);
                titleText.setVisibility(View.GONE);
                String fileName = titleList.get(i).replace(" ", "_") + ".png";
                if (pageType == 0) {
                    Bitmap bitmap2 = Helper.getInstance(activity).loadBitmapFromView(tableLayout);
                    Helper.getInstance(activity).combineImages(bitmap1, bitmap2, fileName);
                    // singleImages(bitmap2,i);
                } else {
                    Bitmap bitmap2 = Helper.getInstance(activity).loadBitmapFromView(holder.itemView);
                    Helper.getInstance(activity).combineImages(bitmap1, bitmap2, fileName);
                    //singleImages(bitmap2,i);
                }
                titleText.setVisibility(View.VISIBLE);

/*
                holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = holder.itemView.getDrawingCache();


                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }
                heightList.add(holder.itemView.getMeasuredHeight());
                height += holder.itemView.getMeasuredHeight();
                */
            }


          /*  for (int i = 0; i < size; i++) {
                bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), heightList.get(i), Bitmap.Config.ARGB_8888);
                Canvas bigCanvas = new Canvas(bigBitmap);
                bigCanvas.drawColor(Color.WHITE);

                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                bitmapList.add(bigBitmap);
                //  iHeight += bitmap.getHeight();
                bitmap.recycle();
            }*/

        }
        //  AppLogger.e("ByteCount:" + bigBitmap.getByteCount());
        //return bitmapList;
        // return bigBitmap;
    }


    void share() {
        getScreenshotFromRecyclerView(recyclerView);
        String title = (num == 0) ? "LAST 24 HOURS HEALTH CHECK REPORT" : "LAST 7 DAYS  HEALTH CHECK REPORT";
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
        intent.putExtra(android.content.Intent.EXTRA_CC, new String[]{"Reddy_Chandrasekhar@Dell.com", "Raghavendra_Ganapath@Dell.com", "D_Gokulakrishnan@Dell.com", "Anuradha_Hariharasub@Dell.com", "Pardhasaradhi_Vajja@Dell.com", "Sabu_Philip@DellTeam.com"});
        intent.putExtra(Intent.EXTRA_TEXT, title);
        intent.putExtra(Intent.EXTRA_STREAM, uriList);
        try {
            startActivity(Intent.createChooser(intent, "Share"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "No App Available", Toast.LENGTH_SHORT).show();
        }
    }
}
