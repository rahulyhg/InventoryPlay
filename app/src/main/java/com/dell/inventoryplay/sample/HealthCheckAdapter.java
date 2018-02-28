package com.dell.inventoryplay.sample;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.response.ChartTableResponse;
import com.github.mikephil.charting.charts.PieChart;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

/**
 * Created by Sasikanta_Sahoo on 11/21/2017.
 * HealthCheckAdapter
 */

class HealthCheckAdapter extends RecyclerView.Adapter<HealthCheckAdapter.CustomViewHolder> {
    private int pageType;
    private FragmentActivity context;
    int count = 0;
    ArrayList<Integer> colorList = new ArrayList<>();
    ArrayList<ChartTableResponse.Chart> chartList;
    int lastPosition = -1;
    HealthCheckAdapter(FragmentActivity activity, int type, ArrayList<ChartTableResponse.Chart> chartList) {
        pageType = type;
        context = activity;
        this.chartList = chartList;
        count = this.chartList.size();
        colorList.add(Color.RED);
        colorList.add(Color.GREEN);
        colorList.add(Color.BLUE);
        colorList.add(Color.YELLOW);
        colorList.add(Color.CYAN);
        colorList.add(Color.GRAY);
        colorList.add(Color.DKGRAY);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_health_check, parent, false);

        return new CustomViewHolder(v);

    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        ChartTableResponse.Chart chartObj = chartList.get(position);
        String name = chartObj.getName();
        String[] axis1 = chartObj.getAxis1();
        String[] axis2 = chartObj.getAxis2();
        ArrayList<String[]> values = chartObj.getValues();
        holder.title.setText(name);
        if (pageType == 1) {
            holder.tableLayout.setVisibility(View.GONE);
            holder.graphView.setVisibility(View.VISIBLE);
            createChart(holder.graphView, name, axis1, axis2, values);
        } else {
            holder.graphView.setVisibility(View.GONE);
            holder.tableLayout.setVisibility(View.VISIBLE);
            createTable(holder.tableLayout, name, axis1, axis2, values);
        }


    }
private void createPieChart(GraphView graphView, String name, String[] axis1, String[] axis2, ArrayList<String[]> values){
    PieChart pieChart = (PieChart)LayoutInflater.from(context).inflate(R.layout.inflate_chart_pie, null);
//mContainerView.addView(myView);
}
    private void createChart(GraphView graphView, String name, String[] axis1, String[] axis2, ArrayList<String[]> values) {
        int chartIndex = 0;
        for (String[] valArr : values) {

            int index = 0;
            int length = valArr.length;
            DataPoint[] dps = new DataPoint[length];

            for (String valStr : valArr) {
                valStr = valStr.contains("NA") ? "0" : valStr;
                DataPoint obj = new DataPoint(index + 1, Double.parseDouble(valStr));
                //  DataPoint obj = new DataPoint(index, 20);
                dps[index] = obj;
                index++;
            }
            BarGraphSeries<DataPoint> series1 = new BarGraphSeries<>(dps);

            series1.setOnDataPointTapListener((series, dataPoint) -> {

                Toast.makeText(context, series.getTitle() + ": " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
            });
            series1.setSpacing(10);
            series1.setColor(colorList.get(chartIndex));
            series1.setTitle(axis2[chartIndex]);
            series1.setAnimated(true);
            // series1.setDataWidth(1d);
            //series1.setDrawValuesOnTop(true);
            //series1.setValuesOnTopColor(colorList.get(chartIndex));
            graphView.addSeries(series1);
            chartIndex++;
        }
        //  graphView.getViewport().setMinX(1);
        // graphView.getViewport().setMinY(0);
        graphView.getViewport().setXAxisBoundsManual(true);
        //graphView.getViewport().setXAxisBoundsManual(false);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(axis1.length + 1);
        // graphView.getGridLabelRenderer().setNumHorizontalLabels(axis1.length+2);
        graphView.getLegendRenderer().setVisible(true);
        graphView.getViewport().setScrollable(false); // enables horizontal scrolling
        graphView.getViewport().setScrollableY(false); // enables vertical scrolling
        graphView.getViewport().setScalable(false); // enables horizontal zooming and scrolling
        graphView.getViewport().setScalableY(false); // enables vertical zooming and scrolling
        graphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);// It will remove the background grids
        // graphView.setBackgroundColor(Color.TRANSPARENT);
        graphView.getGridLabelRenderer().setHorizontalLabelsVisible(true);// remove horizontal x labels and line
        // graphView.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);

       /* int size = axis1.length + 1;
        String[] xLabel = new String[size];
        xLabel[0] = "x";
        for (int i = 0; i < axis1.length; i++) {
            xLabel[i + 1] = axis1[i];
        }
        xLabel[ axis1.length] = "Y";
        */
        // graphView.getGridLabelRenderer().setNumHorizontalLabels(12);
        graphView.getViewport().setScrollable(true);
        // graphView.getViewport().setMaxX(6);
        staticLabelsFormatter.setHorizontalLabels(axis1);
        // graphView.getGridLabelRenderer().setHorizontalLabelsVisible(true);
        //staticLabelsFormatter.setVerticalLabels(axis2);
        //graphView.getGridLabelRenderer().setHumanRounding(false);
        //graphView.getGridLabelRenderer().setNumVerticalLabels(7);
        graphView.getGridLabelRenderer().setNumHorizontalLabels(12);
        graphView.getGridLabelRenderer().setPadding(50);
        // graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        // graphView.getGridLabelRenderer().setvi
        //graphView.setTitle(name);
        graphView.getGridLabelRenderer().setLabelFormatter(
                new DefaultLabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {

                        if (isValueX) {
                            String xLable = "";
                            int value1 = (int) value;
                            if (value == value1 && value > 0 && value < axis1.length + 1)
                                xLable = axis1[value1 - 1];

                            return xLable;
                            // show normal x values
                            //  return super.formatLabel(value, isValueX)+"y";
                        } else {
                            // show currency for y values
                            return super.formatLabel(value, isValueX);
                        }
                    }
                }
        );
    }

    private void createTable(TableLayout tableLayout, String name, String[] axis1, String[] axis2, ArrayList<String[]> values) {

        TableRow tableRow;
        TextView textView;
        int columns = axis1.length;
        int rows = axis2.length;
        //   tableLayout.setLayoutParams(new HorizontalScrollView.LayoutParams(HorizontalScrollView.LayoutParams.MATCH_PARENT, HorizontalScrollView.LayoutParams.WRAP_CONTENT));

        try {
            for (int i = 0; i < rows + 1; i++) {
                tableRow = new TableRow(context);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1.0f));

                for (int j = 0; j < columns + 1; j++) {
                    textView = new TextView(context);

                    textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                    String dataVal = "";
                    if (i == 0 && j == 0) {

                    } else if (i == 0) {
                        textView.setGravity(Gravity.CENTER);
                        dataVal = axis1[j - 1];
                    } else if (j == 0) {
                        textView.setGravity(Gravity.LEFT);
                        dataVal = axis2[i - 1];
                    } else if (i != 0 && j != 0) {
                        textView.setGravity(Gravity.CENTER);
                        String[] valArr = values.get(i - 1);
                        dataVal = valArr[j - 1];
                    }

                    textView.setText(dataVal);
                    textView.setPadding(20, 20, 20, 20);
                    tableRow.addView(textView);
                }
                tableLayout.addView(tableRow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return count - 1;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TableLayout tableLayout;
        private GraphView graphView;
        private TextView title;

        CustomViewHolder(View itemView) {
            super(itemView);
            tableLayout = itemView.findViewById(R.id.tableLayout);
            graphView = itemView.findViewById(R.id.graph);
            title = itemView.findViewById(R.id.title);
        }
    }
}
