package com.dell.inventoryplay.utils;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import com.dell.inventoryplay.R;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sasikanta_Sahoo on 11/25/2017.
 * AppChart
 */

public class AppChart {
    private Activity mContext;
    ViewGroup mRootView;
    String mName;
    String[] mAxis1;
    String[] mAxis2;
    ArrayList<String[]> mValues;
    View mChartView;
    List<PieEntry> mEntries = new ArrayList<PieEntry>();
    ArrayList<String> mLabel = new ArrayList<String>();
    PieDataSet mDataSet;
    PieData data;

    public static AppChart newInstance() {
        return new AppChart();
    }

    public void createDataSet() {
        for (String[] valueArr : mValues) {
            mEntries = new ArrayList<PieEntry>();
            int index = 0;
            for (String value : valueArr) {
                if (value.contains("NA"))
                    value = "0";
                float x = Float.parseFloat(value);
                float y = index;
                mEntries.add(new PieEntry(x, mAxis1[index]));
                index++;
            }
        }
    }

    public void createPieChart() {
        ArrayList<Integer> colors = Helper.getInstance(mContext).getChartColor();
        mDataSet = new PieDataSet(mEntries, "");
        mDataSet.setSliceSpace(2f);
        mDataSet.setValueTextSize(15f);
     //   mDataSet.setValueTextColor(Color.RED);/* this line not working */
        mDataSet.setSelectionShift(15f);

        mDataSet.setValueLinePart1OffsetPercentage(80.f);
        mDataSet.setValueLinePart1Length(1f);
        mDataSet.setValueLinePart2Length(0.9f);

        mDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        mDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
         mDataSet.setValueLineColor(mContext.getResources().getColor(R.color.headerTextLight));
     //  mDataSet.setColor(Color.GREEN);
        mDataSet.setColors(colors);
    }

    public void setData() {
        data = new PieData(mDataSet);
        data.setValueTextColor(Color.WHITE);/* only YValue color changes, Xvalues remains white*/
        data.setDrawValues(true);
    }


    public PieData setUp(Activity context, String name, String[] axis1, String[] axis2, ArrayList<String[]> values) {
        mContext = context;
        mName = name;
        mAxis1 = axis1;
        mAxis2 = axis2;
        mValues = values;

        createDataSet();
        createPieChart();
        setData();
        return data;


    }
}
