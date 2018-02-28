package com.dell.inventoryplay.utils;

import android.app.Activity;
import android.graphics.Color;
import android.view.ViewGroup;

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
    ViewGroup mRootView;
    String mName;
    String[] mAxis1;
    String[] mAxis2;
    boolean[] mShowList;
    ArrayList<String[]> mValues;
    List<BarEntry> mEntries = new ArrayList<BarEntry>();
    ArrayList<String> mLabel = new ArrayList<String>();
    List<IBarDataSet> mDataSet;
    BarData data;

    public static AppBarChart newInstance() {
        return new AppBarChart();
    }

    public void createDataSet() {
        try {
            ArrayList<Integer> colors = Helper.getInstance(mContext).getChartColor();
            mDataSet = new ArrayList<IBarDataSet>();
            int index1 = 0;
            for (String[] valueArr : mValues) {
               // List<Integer> customColors = new ArrayList<Integer>();
                mEntries = new ArrayList<BarEntry>();
                int index = 0;
                for (String value : valueArr) {
                    if (value.contains("|")) {
                 //       customColors.add(Color.CYAN);
                        value = value.split(Pattern.quote("|"))[0];
                    }/*else{
                      customColors.add(colors.get(index1));
                    }*/
                    float graphVal = Float.parseFloat(value);
                    if (!mShowList[index1]) {
                        graphVal = 0f;
                    }
                    BarEntry entry = new BarEntry(index, graphVal);
                    mEntries.add(entry);
                    index++;
                }
                BarDataSet set1 = new BarDataSet(mEntries, mAxis2[index1]);
               // set1.setColors(customColors);
                set1.setColor(colors.get(index1));
              //  set1.setHighlightEnabled(false);
              //  set1.setHighLightColor(Color.TRANSPARENT);

                mDataSet.add(set1);

                index1++;


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setData() {
        try {
            data = new BarData(mDataSet);
            data.setValueTextColor(mContext.getResources().getColor(R.color.carban));/* only YValue color changes, Xvalues remains white*/
            //  data.setDrawValues(false);
            data.setBarWidth(1f / (mAxis2.length) - 0.1f);
            data.setHighlightEnabled(true);

            // allow highlighting for DataSet
            //   data.setValueTextSize(Helper.getInstance(mContext).dpToPx(10));
            // set this to false to disable the drawing of highlight indicator (lines)
            data.setDrawValues(false);




        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public BarData setUp(Activity context, String name, String[] axis1, String[] axis2, ArrayList<String[]> values, boolean[] showList) {
        mContext = context;
        mName = name;
        mAxis1 = axis1;
        mAxis2 = axis2;
        mValues = values;
        mShowList = showList;
        createDataSet();
        setData();
        // testBar();
        return data;

    }
}
