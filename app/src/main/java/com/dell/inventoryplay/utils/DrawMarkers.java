package com.dell.inventoryplay.utils;

import android.content.Context;

import com.github.mikephil.charting.charts.Chart;

/**
 * Created by Sasikanta_Sahoo on 12/6/2017.
 */

public class DrawMarkers extends Chart {
    public DrawMarkers(Context context) {
        super(context);
    }

    @Override
    public void notifyDataSetChanged() {

    }

    @Override
    protected void calculateOffsets() {

    }

    @Override
    protected void calcMinMax() {

    }

    @Override
    public float getYChartMin() {
        return 0;
    }

    @Override
    public float getYChartMax() {
        return 0;
    }

    @Override
    public int getMaxVisibleCount() {
        return 0;
    }
}
