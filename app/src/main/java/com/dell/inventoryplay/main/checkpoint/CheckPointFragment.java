package com.dell.inventoryplay.main.checkpoint;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.dell.inventoryplay.AppConfig;
import com.dell.inventoryplay.InventoryPlayApp;
import com.dell.inventoryplay.R;
import com.dell.inventoryplay.base.BaseFragment;
import com.dell.inventoryplay.main.MainActivity;
import com.dell.inventoryplay.request.BaseGsonRequest;
import com.dell.inventoryplay.request.HttpRequestObject;
import com.dell.inventoryplay.request.RequestManager;
import com.dell.inventoryplay.response.InquirySvcTagResponse;
import com.dell.inventoryplay.utils.AppConstants;
import com.dell.inventoryplay.utils.Helper;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.view.View.GONE;

/**
 * Created by Sasikanta_Sahoo on 12/12/2017.
 * CheckPointFragment
 */

public class CheckPointFragment extends BaseFragment {
    private ViewGroup rootView;
    private MainActivity activity;
    private FrameLayout rowViewContainer, columnViewContainer;
    private ArrayList<ArrayList<String>> records;
    private ArrayList<String> columns;
    private CheckPointTableRowLayout rowViewTable;
    private CheckPointTableColumnLayout columnViewTable;
    private FrameLayout loader, noInternet;
    private TextView msg;
    private String checkPointId, checkPointTitle;

    @Override
    protected void setUp(View rootView) {
        TextView title = rootView.findViewById(R.id.title);
        rowViewTable = new CheckPointTableRowLayout(activity, columns, records);
        rowViewContainer.addView(rowViewTable);
        title.setText(checkPointTitle);
        FloatingActionButton switchView = rootView.findViewById(R.id.switchView);
        switchView.setTag(1);
        switchView.setOnClickListener(view -> {
            int tag = (int) view.getTag();
            if (tag == 0) {
                view.setTag(1);
                ((FloatingActionButton) view).setImageResource(R.drawable.ic_view_column);
                columnViewContainer.setVisibility(GONE);
                rowViewContainer.setVisibility(View.VISIBLE);
                if (rowViewTable == null) {
                    rowViewContainer.removeAllViews();
                    rowViewTable = new CheckPointTableRowLayout(activity, columns, records);
                    rowViewContainer.addView(rowViewTable);
                }
            } else {
                view.setTag(0);
                ((FloatingActionButton) view).setImageResource(R.drawable.ic_view_row);
                rowViewContainer.setVisibility(GONE);
                columnViewContainer.setVisibility(View.VISIBLE);
                if (columnViewTable == null) {
                    columnViewContainer.removeAllViews();
                    columnViewTable = new CheckPointTableColumnLayout(activity, columns, records);
                    columnViewTable.setGravity(Gravity.CENTER_HORIZONTAL);
                    columnViewContainer.addView(columnViewTable);
                }
            }


        });

        loader.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    public static Fragment newInstance() {
        return new CheckPointFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(
                R.layout.frag_check_point, container, false);
        setRetainInstance(false);
        setHasOptionsMenu(true);
        rowViewContainer = rootView.findViewById(R.id.rowViewContainer);
        columnViewContainer = rootView.findViewById(R.id.columnViewContainer);
        columnViewContainer.setVisibility(View.GONE);

        loader = rootView.findViewById(R.id.loader);
        msg = rootView.findViewById(R.id.msg);
        noInternet = rootView.findViewById(R.id.noInternet);
        Bundle b = getArguments();
        if (b != null) {
            checkPointId = b.getString("ID");
            checkPointTitle = b.getString("TITLE");
        }

        if (AppConfig.useMockJson) {
            String json = getString(R.string.api_check_point);
            Gson gson = new Gson();
            InquirySvcTagResponse res = gson.fromJson(json, InquirySvcTagResponse.class);
            records = res.getValueList();
            columns = res.getColumnList();
            setUp(rootView);
        } else {
            if (Helper.isConnected(activity)) {
                loadData();
            } else {

                noInternet.setVisibility(View.VISIBLE);
                noInternet.setOnClickListener(view -> {
                    if (Helper.isConnected(activity)) {
                        noInternet.setVisibility(View.GONE);
                        loadData();
                    } else {
                        Helper.getInstance(activity).showToast(getString(R.string.no_internet), 1, 3);
                    }
                });
            }
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.turnOffToolbarScrolling();
        activity.enableBackButtonViews(true);
        activity.turnOffBottomNavigationViewScrolling();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.nav_home);
            actionBar.setSubtitle(getString(R.string.health_check_alert));
        }

    }


    public void doInBg() {
        String url = AppConfig.REST_API_CHECK_POINT_HEALTH;
        String inputData = "{\"alertId\":\"" + checkPointId + "\"}";
        int apiCode = AppConstants.API_CODE_CHECK_POINT_HEALTH;


        try {
            HttpRequestObject reqObject = HttpRequestObject.getInstance();
            JSONObject jsonRequest = reqObject.getRequestBody(apiCode, inputData);
            BaseGsonRequest<InquirySvcTagResponse> gsonRequest = new BaseGsonRequest<>(Request.Method.POST, url, InquirySvcTagResponse.class, jsonRequest, InventoryPlayApp.getHeader(),
                    res -> {
                        if (res.getSuccess() && res.getColumnList() != null) {
                            records = res.getValueList();
                            columns = res.getColumnList();

                            setUp(rootView);
                        } else if (res.getColumnList() == null) {
                            loader.setVisibility(View.GONE);
                            msg.setText(R.string.no_alert_found);
                            noInternet.setVisibility(View.VISIBLE);
                        } else {
                            loader.setVisibility(View.GONE);
                            String msgStr = res.getMessage();
                            msg.setText(msgStr);
                            noInternet.setVisibility(View.VISIBLE);
                        }
                    }, error -> {
                loader.setVisibility(View.GONE);
                String msgStr = getString(R.string.unable_to_process);
                msg.setText(msgStr);
                noInternet.setVisibility(View.VISIBLE);
            });
            RequestManager.getRequestQueue().add(gsonRequest).setTag(AppConstants.CHECK_POINT_HEALTH_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadData() {
        loader.setVisibility(View.VISIBLE);
        Single.create((SingleOnSubscribe<Integer>) e -> doInBg())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();

    }


    void doShare(String fileName) {

        File dir = new File(activity.getFilesDir(), "images");
        File file = new File(dir, fileName);
        if (!file.exists()) {
            Helper.getInstance(activity).showToast(getString(R.string.unable_to_share), Toast.LENGTH_SHORT, 3);
            return;
        }
        Uri uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, checkPointTitle);
        intent.putExtra(Intent.EXTRA_TEXT, checkPointTitle);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.share)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, R.string.no_app_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.health_check, menu);
    }

    public void prepareShare() {
        View v;
        String fileName;
        int textTitleColor = getResources().getColor(R.color.headerTextLight);
        int textTitlePadding = Helper.getInstance(activity).dpToPx(10);
        if (rowViewContainer.getVisibility() == View.VISIBLE) {
            fileName = "alert_row_view.png";
            v = rowViewTable;
        } else {
            fileName = "alert_column_view.png";
            v = columnViewTable;
        }
        TextView tmpTv = new TextView(activity);
        tmpTv.setText(checkPointTitle);
        tmpTv.setPadding(textTitlePadding, textTitlePadding, textTitlePadding, textTitlePadding);
        tmpTv.setTextColor(textTitleColor);
        tmpTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f);
        tmpTv.setTypeface(Typeface.DEFAULT_BOLD);

        Bitmap bitmap1 = Helper.getInstance(activity).loadBitmapFromView(tmpTv);
        Bitmap bitmap2 = Helper.getInstance(activity).loadBitmapFromView(v);
        Helper.getInstance(activity).combineImages(bitmap1, bitmap2, fileName);
        doShare(fileName);
    }

    public void updateView() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_share:
                item.setEnabled(false);
                prepareShare();
                item.setEnabled(true);
                break;
            case R.id.action_help:
                showHelp();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
