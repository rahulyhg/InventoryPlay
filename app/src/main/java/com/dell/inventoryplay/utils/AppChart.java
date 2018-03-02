package com.dell.inventoryplay.utils;

import android.app.Activity;
import android.graphics.Color;

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
    private String[] mAxis1;
    private ArrayList<String[]> mValues;
    private List<PieEntry> mEntries = new ArrayList<>();
    private PieDataSet mDataSet;
    private PieData data;

    public static AppChart newInstance() {
        return new AppChart();
    }

    private void createDataSet() {
        for (String[] valueArr : mValues) {
            mEntries = new ArrayList<>();
            int index = 0;
            for (String value : valueArr) {
                if (value.contains("NA"))
                    value = "0";
                float x = Float.parseFloat(value);
                mEntries.add(new PieEntry(x, mAxis1[index]));
                index++;
            }
        }
    }

    private void createPieChart() {
        ArrayList<Integer> colors = Helper.getInstance(mContext).getChartColor();
        mDataSet = new PieDataSet(mEntries, "");
        mDataSet.setSliceSpace(2f);
        mDataSet.setValueTextSize(15f);
        mDataSet.setSelectionShift(15f);

        mDataSet.setValueLinePart1OffsetPercentage(80.f);
        mDataSet.setValueLinePart1Length(1f);
        mDataSet.setValueLinePart2Length(0.9f);

        mDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        mDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        mDataSet.setValueLineColor(mContext.getResources().getColor(R.color.headerTextLight));
        mDataSet.setColors(colors);
    }

    private void setData() {
        data = new PieData(mDataSet);
        data.setValueTextColor(Color.WHITE);
        data.setDrawValues(true);
    }


    public PieData setUp(Activity context, String[] axis1, ArrayList<String[]> values) {
        mContext = context;
        mAxis1 = axis1;
        mValues = values;
        createDataSet();
        createPieChart();
        setData();
        return data;


    }
}
