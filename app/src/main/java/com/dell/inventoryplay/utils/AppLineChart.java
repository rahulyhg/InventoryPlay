package com.dell.inventoryplay.utils;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sasikanta_Sahoo on 11/25/2017.
 * AppChart
 */

public class AppLineChart {
    private Activity mContext;
    ViewGroup mRootView;
    String mName;
    String[] mAxis1;
    String[] mAxis2;
    ArrayList<String[]> mValues;
    int mType;
    View mChartView;
    List<BarEntry> mEntries = new ArrayList<BarEntry>();
    ArrayList<String> mLabel = new ArrayList<String>();
    List<IBarDataSet> mDataSet;
    BarData data;

    public static AppLineChart newInstance() {
        return new AppLineChart();
    }

    public void createDataSet() {
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(0,10,230));
        colors.add(Color.rgb(159,0,235));
        colors.add(Color.rgb(143,200,0));
        colors.add(Color.rgb(10,0,136));
        colors.add(Color.rgb(100,10,10));
        colors.add(Color.rgb(176,100,100));
        colors.add(Color.rgb(10,100,253));


        mDataSet = new ArrayList<IBarDataSet>();
        int index1 = 0;
        for (String[] valueArr : mValues) {
            mEntries = new ArrayList<BarEntry>();
            int index = 0;
            for (String value : valueArr) {
                if (value.contains("NA"))
                    value = "0";
                float x = Float.parseFloat(value);
                float y = index;
                mEntries.add(new BarEntry(index, x));
                index++;
            }


            BarDataSet set1 = new BarDataSet(mEntries, mAxis2[index1]);
            set1.setColor(colors.get(index1));
            mDataSet.add(set1);

            index1++;

        }


/*
        BarDataSet set1 = new BarDataSet(valuesMen, "Men");
        set1.setColor(Color.BLUE);
        BarDataSet set2 = new BarDataSet(valuesWomen, "Women");
        set2.setColor(Color.RED);

        List<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);

        BarData data = new BarData(dataSets);
*/

    }

    public void setData() {
        data = new BarData(mDataSet);
        data.setValueTextColor(Color.BLACK);/* only YValue color changes, Xvalues remains white*/
        data.setDrawValues(true);
        data.setBarWidth(1f/(mAxis2.length));
    }


    public BarData setUp(Activity context, int type, String name, String[] axis1, String[] axis2, ArrayList<String[]> values) {
        mContext = context;
        mName = name;
        mAxis1 = axis1;
        mAxis2 = axis2;
        mValues = values;
        mType = type;
        createDataSet();
        setData();
       // testBar();
        return data;

    }
}
