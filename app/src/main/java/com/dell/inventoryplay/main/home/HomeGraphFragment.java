package com.dell.inventoryplay.main.home;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.main.MainActivity;
import com.dell.inventoryplay.response.ChartTableResponse;
import com.dell.inventoryplay.utils.AppBarChart;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by sasikanta on 11/14/2017.
 * HomeGraphFragment
 */

public class HomeGraphFragment extends Fragment {
    public static final String ARG_POSITION = "ARG_POSITION";
    ViewGroup rootView;
    ArrayList<Integer> mColorList = new ArrayList<>();
    ArrayList<ChartTableResponse.Chart> mChartList;
    MainActivity activity;

    public static HomeGraphFragment newInstance() {
        return new HomeGraphFragment();
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
                R.layout.fragment_home_graph, container, false);
        setRetainInstance(true);
        setUp();
        return rootView;
    }

    protected void setUp() {
        BarChart barChart = rootView.findViewById(R.id.barChart);
        mColorList.add(Color.RED);
        mColorList.add(Color.GREEN);
        mColorList.add(Color.BLUE);
        mColorList.add(Color.YELLOW);
        mColorList.add(Color.CYAN);
        mColorList.add(Color.GRAY);
        mColorList.add(Color.DKGRAY);
        String json = activity.getString(R.string.api_asn1);
        Gson gson = new Gson();
        ChartTableResponse res = gson.fromJson(json, ChartTableResponse.class);
        mChartList = res.getChartList();
        ChartTableResponse.Chart chartObj = mChartList.get(1);
        String name = chartObj.getName();
        String[] axis1 = chartObj.getAxis1();
        String[] axis2 = chartObj.getAxis2();
        ArrayList<String[]> values = chartObj.getValues();
        boolean[] showList = chartObj.getShowList();
        createBarChart(barChart, name, axis1, axis2, values, showList);
    }


    private void createBarChart(BarChart barChart, String name, String[] axis1, String[] axis2, ArrayList<String[]> values, boolean[] showList) {
        AppBarChart chart = AppBarChart.newInstance();
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new HomeGraphFragment.MyXAxisValueFormatter(axis1));
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(axis1.length);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setPinchZoom(true);
        barChart.setDrawGridBackground(false);
        float groupSpace = 0.0f;
        float barSpace = 0.0f; // x2 dataset
        barChart.setData(chart.setUp(getActivity(), name, axis1, axis2, values, showList));
        barChart.groupBars(0f, groupSpace, barSpace); // perform the "explicit" grouping
        barChart.getDescription().setEnabled(false);
        barChart.invalidate(); // refresh


    }

    private class MyXAxisValueFormatter implements IAxisValueFormatter {
        private String[] mValues;

        MyXAxisValueFormatter(String[] values) {
            mValues = values;
        }

        @Override
        public String getFormattedValue(float v, AxisBase axisBase) {
            String val = "";
            try {
                val = mValues[(int) v];
            } catch (Exception e) {
                e.printStackTrace();
            }
            return val;

        }
    }
}
