package com.dell.inventoryplay.main.common;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.response.ChartTableResponse;
import com.dell.inventoryplay.utils.AppBarChart;
import com.dell.inventoryplay.utils.AppChart;
import com.dell.inventoryplay.utils.AppConstants;
import com.dell.inventoryplay.utils.AppLogger;
import com.dell.inventoryplay.utils.CustomMarkerView;
import com.dell.inventoryplay.utils.Helper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Sasikanta_Sahoo on 11/25/2017.
 * ChartTableAdapter
 */

public class ChartTableAdapter extends RecyclerView.Adapter<ChartTableAdapter.CustomViewHolder> {
    private int pageType;
    private FragmentActivity mContext;
    private int mItems = 0;
    private ArrayList<ChartTableResponse.Chart> mChartList;
    private ArrayList<Integer> mColor;

    public ChartTableAdapter(FragmentActivity activity, int type, ArrayList<ChartTableResponse.Chart> chartList) {
        pageType = type;
        mContext = activity;
        mChartList = chartList;
        mItems = mChartList.size();
        mColor = Helper.getInstance(mContext).getChartColor();
    }

    /*
        public void blinkText(View v) {
            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(100); //You can manage the time of the blink with this parameter
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            v.startAnimation(anim);
        }
    */
    @Override
    public ChartTableAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_chart_table, parent, false);
        return new ChartTableAdapter.CustomViewHolder(v);
    }

    public static class LegendTag {
        int position;
        String title;
        Boolean isShow;
        int colIndex;
        ChartTableResponse.Chart chartObj;
    }

    private void addLegend(LinearLayout legendContainer, int position, String[] axis2, ChartTableResponse.Chart chartObj) {
        try {

            int index = 0;
            LinearLayout linearLayout = new LinearLayout(mContext);
            FrameLayout frameLayout;
            int countLegend = 6;
            if (mContext.getResources().getConfiguration().orientation == 1) {
                countLegend = 3;
            }
            for (String legend : axis2) {
                if (index % countLegend == 0) {
                    frameLayout = new FrameLayout(mContext);
                    LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    frameLayout.setLayoutParams(params0);

                    linearLayout = new LinearLayout(mContext);
                    FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    params1.gravity = Gravity.CENTER;

                    linearLayout.setLayoutParams(params1);

                    int px = Helper.getInstance(mContext).dpToPx(5);
                    linearLayout.setPadding(px, px, px, px);
                    frameLayout.addView(linearLayout);
                    legendContainer.addView(frameLayout);
                }


                CardView card = new CardView(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                card.setLayoutParams(params);
                card.setContentPadding(0, 0, 0, 0);
                card.setCardBackgroundColor(mColor.get(index));
                card.setMaxCardElevation(15);
                card.setCardElevation(9);
                card.setUseCompatPadding(true);
                TextView txt = new TextView(mContext);
                //  LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                txt.setText(legend);
                txt.setTextColor(Color.WHITE);
                // txt.setBackground(mContext.getResources().getDrawable(R.drawable.bg_shadow));
                txt.setBackgroundColor(mColor.get(index));
                txt.setShadowLayer(1.5f, -1, 1, Color.WHITE);
                txt.setSingleLine(true);
                if (!chartObj.getShowList()[index]) {
                    txt.setAlpha(0.3f);
                } else {
                    txt.setAlpha(1f);
                }
                int px = Helper.getInstance(mContext).dpToPx(5);
                int px1 = Helper.getInstance(mContext).dpToPx(2);
                txt.setPadding(px1, 0, px1, 0);
                params.setMargins(px, 0, px, 0);
                txt.setLayoutParams(params);
                LegendTag lt = new LegendTag();
                lt.position = position;
                lt.colIndex = index;
                lt.title = legend;
                lt.isShow = chartObj.getShowList()[index];
                lt.chartObj = chartObj;
                txt.setTag(lt);

                txt.setTypeface(Typeface.DEFAULT_BOLD);
                card.addView(txt);
                linearLayout.addView(card);

                txt.setOnClickListener(v -> {
                    LegendTag tagObj = (LegendTag) v.getTag();
                    if (tagObj.isShow = !tagObj.isShow) {
                        v.setAlpha(1f);
                        tagObj.chartObj.getShowList()[lt.colIndex] = tagObj.isShow;
                    } else {
                        tagObj.chartObj.getShowList()[lt.colIndex] = tagObj.isShow;
                        v.setAlpha(0.2f);
                    }
                    ChartTableAdapter.this.notifyItemChanged(tagObj.position);

                });
                index++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBindViewHolder(ChartTableAdapter.CustomViewHolder holder, int position) {
        try {
            ChartTableResponse.Chart chartObj = mChartList.get(position);
            String name = chartObj.getName();
            String[] axis1 = chartObj.getAxis1();
            String[] axis2 = chartObj.getAxis2();
            ArrayList<String[]> values = chartObj.getValues();
            boolean[] showList = chartObj.getShowList();
            chartObj.setPosition(position);
            int isMaximize = chartObj.isMaximize();
            if (position == 0 && isMaximize == 0) {
                chartObj.setMaximize(2);
                holder.showHide.setVisibility(View.VISIBLE);
                holder.title.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.arrow_down_float, 0, 0, 0);
            } else {
                holder.title.setCompoundDrawablesWithIntrinsicBounds(isMaximize == 2 ? android.R.drawable.arrow_down_float : android.R.drawable.arrow_up_float, 0, 0, 0);
                chartObj.setMaximize(isMaximize == 2 ? 2 : 1);
                holder.showHide.setVisibility(isMaximize == 2 ? View.VISIBLE : View.GONE);
            }
            AppLogger.e("INPUT %d", isMaximize);
            holder.title.setText(name);
            holder.title.setTag(chartObj);
            Helper.getInstance(mContext).createShape(holder.title);
            holder.title.setOnClickListener(v -> {
                ChartTableResponse.Chart tagObj = (ChartTableResponse.Chart) v.getTag();
                tagObj.setMaximize(chartObj.isMaximize() != 2 ? 2 : 1);
                // ChartTableAdapter.this.notifyDataSetChanged();
                ChartTableAdapter.this.notifyItemChanged(tagObj.getPosition());
                holder.title.setTag(tagObj);

            });
            if (pageType == 1) {
                holder.tableLayout.setVisibility(View.GONE);
                // we are clearing so that no previous value exist during show/hide graph bar
                holder.barChart.clear();
                holder.pieChart.clear();
                holder.pieChart.setVisibility(View.VISIBLE);
                holder.barChart.setVisibility(View.VISIBLE);

                if (values.size() == 1) {
                    holder.legendContainer.setVisibility(View.GONE);
                    holder.barChart.setVisibility(View.GONE);
                    holder.pieChart.setVisibility(View.VISIBLE);
                    createChart(holder.pieChart, name, axis1, axis2, values);
                    holder.legendContainer.removeAllViews();
                } else {
                    holder.legendContainer.removeAllViews();
                    holder.legendContainer.setVisibility(View.VISIBLE);
                    holder.pieChart.setVisibility(View.GONE);
                    holder.barChart.setVisibility(View.VISIBLE);
                    createBarChart(holder.barChart, name, axis1, axis2, values, showList);
                    addLegend(holder.legendContainer, position, axis2, chartObj);
                }
            } else {
                holder.pieChart.setVisibility(View.GONE);
                holder.barChart.setVisibility(View.GONE);
                holder.legendContainer.setVisibility(View.GONE);
                holder.tableLayout.setVisibility(View.VISIBLE);
                holder.tableLayout.removeAllViews();
                createTable(holder.tableLayout, axis1, axis2, values);
                holder.legendContainer.removeAllViews();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createBarChart(BarChart barChart, String name, String[] axis1, String[] axis2, ArrayList<String[]> values, boolean[] showList) {
        try {

            AppBarChart chart = AppBarChart.newInstance();

            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormatter(axis1));
            xAxis.setTextColor(mContext.getResources().getColor(R.color.headerTextLight));
            barChart.getAxisLeft().setTextColor(mContext.getResources().getColor(R.color.headerTextLight));
            barChart.getAxisRight().setTextColor(mContext.getResources().getColor(R.color.headerTextLight));
            barChart.setDrawMarkers(false);
            xAxis.setAxisMinimum(0f);
            xAxis.setAxisMaximum(axis1.length);
            xAxis.setGranularity(1f);
            xAxis.setCenterAxisLabels(true);
            barChart.getAxisLeft().setAxisMinimum(0f);
            barChart.getAxisRight().setAxisMinimum(0f);
            barChart.setDrawBarShadow(false);
            barChart.setDrawValueAboveBar(true);
            barChart.setPinchZoom(true);
            barChart.setDrawGridBackground(false);
            barChart.getLegend().setEnabled(false);


            float groupSpace = 0.0f;
            float barSpace = 0.1f;
            barChart.setData(chart.setUp(mContext, name, axis1, axis2, values, showList));
            barChart.groupBars(0f, groupSpace, barSpace);
            barChart.getDescription().setEnabled(false);
            CustomMarkerView marker = new CustomMarkerView(mContext, R.layout.custom_marker_view);
            marker.setChartView(barChart);
            barChart.setMarker(marker);

            barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {

                    AppLogger.e(h.toString() + "VALUE BAR: %d ::: %f :: %f ", h.getDataIndex(), e.getY(), e.getX());
                    // dont remove setDrawMarkers(true); , some issue in chart library, intermittently crash
                    if (e.getY() != 0)
                        barChart.setDrawMarkers(true);
                }

                @Override
                public void onNothingSelected() {

                    barChart.setDrawMarkers(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createChart(PieChart pieChart, String name, String[] axis1, String[] axis2, ArrayList<String[]> values) {
        try {


            AppChart chart = AppChart.newInstance();
            pieChart.setData(chart.setUp(mContext, name, axis1, axis2, values));
            Legend cLegend = pieChart.getLegend();
            cLegend.setEnabled(false);
            int entryLabelColor = mContext.getResources().getColor(R.color.headerTextLight);
            pieChart.setEntryLabelColor(entryLabelColor);
            pieChart.setRotationAngle(90);
            pieChart.setRotationEnabled(true);
            pieChart.animateY(5000);
            pieChart.getDescription().setEnabled(false);
            pieChart.setHoleRadius(0f);
            pieChart.setTransparentCircleRadius(0f);
            pieChart.invalidate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable(TableLayout tableLayout, String[] axis1, String[] axis2, ArrayList<String[]> values) {
        try {
            TableRow tableRow;
            TextView textView;
            int columns = axis1.length;
            int rows = axis2.length;
            tableLayout.setStretchAllColumns(true);
            for (int i = 0; i < rows + 1; i++) {
                tableRow = new TableRow(mContext);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT));

                for (int j = 0; j < columns + 1; j++) {
                    textView = new TextView(mContext);

                    textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                    String dataVal;
                    if (i == 0 && j == 0) {
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextColor(mContext.getResources().getColor(R.color.headerTextLight));
                        textView.setTypeface(textView.getTypeface(), Typeface.NORMAL);
                        dataVal = "";
                    } else if (i == 0) {
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextColor(mContext.getResources().getColor(R.color.headerTextLight));
                        textView.setTypeface(textView.getTypeface(), Typeface.NORMAL);
                        dataVal = axis1[j - 1];
                    } else if (j == 0) {
                        textView.setGravity(Gravity.START);
                        textView.setTypeface(textView.getTypeface(), Typeface.NORMAL);
                        textView.setTextColor(mContext.getResources().getColor(R.color.tblColumnText));
                        dataVal = axis2[i - 1];
                    } else {
                        textView.setTextColor(mContext.getResources().getColor(R.color.carban));
                        textView.setGravity(Gravity.CENTER);
                        String[] valArr = values.get(i - 1);
                        dataVal = valArr[j - 1];
                    }
                    if (i == 0) {
                        Helper.getInstance(mContext).createTableHeaderShape(textView);

                        //textView.setBackgroundColor(mContext.getResources().getColor(R.color.grey_top));
                        textView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));

                    } else if (i % 2 == 0) {
                        textView.setBackgroundResource(R.drawable.border1);
                    } else {
                        textView.setBackgroundResource(R.drawable.border2);
                    }

                    textView.setText(dataVal);
                    if (dataVal.contains("|")) {
                        dataVal = dataVal.split(Pattern.quote("|"))[0];
                        String val = "<span style='color:red;text-decoration:line-through'><u><b>" + dataVal + "</b></u></span>";
                        textView.setText(Html.fromHtml(val));
                        textView.setTextColor(Color.RED);
                        //textView.setTypeface(Typeface.DEFAULT_BOLD);
                        // blinkText(textView);
                        textView.setOnClickListener(v -> {
                            Helper.getInstance(mContext).showToast("ASN Recd & Ack are not matching.", Toast.LENGTH_SHORT, AppConstants.TOAST_DEFAULT);
                            //  v.clearAnimation();
                        });
                    }
                    //  blinkText(textView);
                    //  textView.setTextSize(Helper.getInstance(mContext).dpToPx(5));
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
        return mItems;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        private PieChart pieChart;
        private BarChart barChart;
        private TableLayout tableLayout;
        private TextView title;
        private LinearLayout legendContainer;
        private RelativeLayout showHide;

        CustomViewHolder(View itemView) {
            super(itemView);
            pieChart = itemView.findViewById(R.id.pieChart);
            barChart = itemView.findViewById(R.id.barChart);
            tableLayout = itemView.findViewById(R.id.tableLayout);
            title = itemView.findViewById(R.id.title);
            legendContainer = itemView.findViewById(R.id.legendContainer);
            showHide = itemView.findViewById(R.id.showHide);


        }
    }

    private class MyXAxisValueFormatter implements IAxisValueFormatter {
        private String[] mValues;

        MyXAxisValueFormatter(String[] values) {
            mValues = values;
        }

        @Override
        public String getFormattedValue(float v, AxisBase axisBase) {
            try {
                return mValues[(int) v];
            } catch (Exception e) {
                return "0";
            }

        }
    }
}
