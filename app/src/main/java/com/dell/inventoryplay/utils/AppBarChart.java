package com.dell.inventoryplay.utils;

import android.app.Activity;

import com.dell.inventoryplay.R;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Sasikanta_Sahoo on 11/25/2017.
 * AppChart
 */

public class AppBarChart {
    private Activity mContext;
    private String[] mAxis2;
    private boolean[] mShowList;
    private ArrayList<String[]> mValues;
    private List<IBarDataSet> mDataSet;
    private BarData data;

    public static AppBarChart newInstance() {
        return new AppBarChart();
    }

    private void createDataSet() {
        try {
            ArrayList<Integer> colors = Helper.getInstance(mContext).getChartColor();
            mDataSet = new ArrayList<>();
            int index1 = 0;
            for (String[] valueArr : mValues) {
                List<BarEntry> mEntries = new ArrayList<>();
                int index = 0;
                for (String value : valueArr) {
                    if (value.contains("|")) {
                        value = value.split(Pattern.quote("|"))[0];
                    }
                    float graphVal = Float.parseFloat(value);
                    if (!mShowList[index1]) {
                        graphVal = 0f;
                    }
                    BarEntry entry = new BarEntry(index, graphVal);
                    mEntries.add(entry);
                    index++;
                }
                BarDataSet set1 = new BarDataSet(mEntries, mAxis2[index1]);
                set1.setColor(colors.get(index1));
                mDataSet.add(set1);
                index1++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData() {
        try {
            data = new BarData(mDataSet);
            data.setValueTextColor(mContext.getResources().getColor(R.color.carban));/* only YValue color changes, Xvalues remains white*/
            data.setBarWidth(1f / (mAxis2.length) - 0.1f);
            data.setHighlightEnabled(true);
            data.setDrawValues(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public BarData setUp(Activity context, String[] axis2, ArrayList<String[]> values, boolean[] showList) {
        mContext = context;
        mAxis2 = axis2;
        mValues = values;
        mShowList = showList;
        createDataSet();
        setData();
        return data;

    }
}
