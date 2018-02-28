package com.dell.inventoryplay.main.asn;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.main.MainActivity;
import com.dell.inventoryplay.main.common.ChartTableAdapter;
import com.dell.inventoryplay.response.ChartTableResponse;
import com.dell.inventoryplay.utils.MovableFloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by sasikanta on 11/14/2017.
 * HealthCheckPagerFragment
 */

public class AsnStatisticsFragment extends Fragment {

    public static String ARG_POSITION;
    ViewGroup rootView;
    ArrayList<ChartTableResponse.Chart> chartList;
    RecyclerView recyclerView;
    MainActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    public static AsnStatisticsFragment newInstance() {
        return new AsnStatisticsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_track_asn_sub_page, container, false);
        setRetainInstance(true);
        loadData();
        return rootView;
    }

    public void loadData() {
        String json = activity.getString(R.string.api_asn1);
        Gson gson = new Gson();
        ChartTableResponse res = gson.fromJson(json, ChartTableResponse.class);
        chartList = res.getChartList();
        setUp(rootView);
        /*
        Single.create((SingleOnSubscribe<Integer>) e -> doInBg())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(o -> updateView());
    */
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
                    chartList = ((ChartTableResponse) res).getChartList();

                    setUp(rootView);
                }, error -> {
                    AppLogger.e("ChartList %s", error.getMessage());
                }, ChartTableResponse.class, null);
                RequestManager.getRequestQueue().add(gsonRequest).setTag(AppConstants.HEALTH_CHECK_TAG);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    public void getLegendInfo() {
        //  recyclerView
    }
 */
    protected void setUp(View view) {
       /* SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.swipeRefresh);
        swipeRefresh.setEnabled(false);
        swipeRefresh.setRefreshing(false);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefresh.setRefreshing(true);
                Toast.makeText(getActivity(), "Refreshing..", Toast.LENGTH_SHORT).show();
                loadData();

            }
        });*/
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ChartTableAdapter(getActivity(), 1, chartList));
        recyclerView.setHasFixedSize(true);
        MovableFloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setTag("GraphView");
        fab.setOnClickListener(v -> {
            if (v.getTag().toString().contains("GraphView")) {
                fab.setImageResource(R.drawable.ic_toggle_chart);
                v.setTag("TableView");
                recyclerView.setAdapter(new ChartTableAdapter(getActivity(), 0, chartList));
            } else {
                fab.setImageResource(R.drawable.ic_toggle);
                v.setTag("GraphView");
                recyclerView.setAdapter(new ChartTableAdapter(getActivity(), 1, chartList));
            }
        });
    }
}
