package com.dell.inventoryplay.main.inquiry;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.base.BaseFragment;
import com.dell.inventoryplay.main.MainActivity;
import com.dell.inventoryplay.response.InquirySvcTagResponse;
import com.dell.inventoryplay.utils.Helper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

import static android.view.View.GONE;

/**
 * Created by Sasikanta_Sahoo on 12/12/2017.
 * InquirySvcTagFragment
 */

public class InquirySvcTagFragment extends BaseFragment {
    private MainActivity activity;
    private String subTitle;
    private FrameLayout rowViewContainer, columnViewContainer;
    private ArrayList<ArrayList<String>> records;
    private ArrayList<String> columns;
    private TableRowLayout rowViewTable;
    private TableColumnLayout columnViewTable;
    private String param1;
    private String json;
    private RelativeLayout progressBarContainer;
    private boolean showRowViewFew, showColViewFew;

    @Override
    protected void setUp(View view) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    public static Fragment newInstance() {
        return new InquirySvcTagFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_inquiry_svc_tag, container, false);
        rowViewContainer = rootView.findViewById(R.id.rowViewContainer);
        columnViewContainer = rootView.findViewById(R.id.columnViewContainer);
        columnViewContainer.setVisibility(View.GONE);
        setRetainInstance(false);
        TextView svcTag = rootView.findViewById(R.id.svcTag);
        TextView reg = rootView.findViewById(R.id.reg);
        TextView dynParam = rootView.findViewById(R.id.dynParam);
        progressBarContainer = rootView.findViewById(R.id.progressBarContainer);
        FloatingActionButton switchView = rootView.findViewById(R.id.switchView);
        switchView.setTag(1);
        switchView.setOnClickListener(view -> {
            int tag = (int) view.getTag();
            if (tag == 0) {
                view.setTag(1);
                ((FloatingActionButton) view).setImageResource(R.drawable.ic_view_column);
                columnViewContainer.setVisibility(GONE);
                rowViewContainer.setVisibility(View.VISIBLE);
                if (rowViewTable == null) {
                    rowViewContainer.removeAllViews();
                    rowViewTable = new TableRowLayout(InquirySvcTagFragment.this, activity, columns, records);
                    rowViewContainer.addView(rowViewTable);
                }
            } else {
                view.setTag(0);
                ((FloatingActionButton) view).setImageResource(R.drawable.ic_view_row);
              /*  ArrayList<Integer> ids = new ArrayList<>();

                for (int i = 0; i < records.size(); i++) {
                    ids.add(i);
                }*/
                rowViewContainer.setVisibility(GONE);
                columnViewContainer.setVisibility(View.VISIBLE);
                if (columnViewTable == null) {
                    columnViewContainer.removeAllViews();
                    columnViewTable = new TableColumnLayout(activity, columns, records);
                    columnViewTable.setGravity(Gravity.CENTER_HORIZONTAL);
                    columnViewContainer.addView(columnViewTable);
                }
            }


        });
        FloatingActionButton fabCompare = rootView.findViewById(R.id.fabCompare);
        showRowViewFew = showColViewFew = false;
        fabCompare.setOnClickListener(view -> {
            if (((int) switchView.getTag()) == 1) {
                int cnt = rowViewTable.tableC.getChildCount();
                ArrayList<Integer> indexList = new ArrayList<>();
                // ArrayList<ArrayList<String>> viewRecords = new ArrayList<>();
                if (!showRowViewFew) {
                    for (int i = 0; i < cnt; i++) {
                        int tag = (int) ((ViewGroup) rowViewTable.tableC.getChildAt(i)).getChildAt(0).getTag();
                        if (tag == 1) {
                            indexList.add(i);
                            break;
                        }
                    }
                    if (indexList.size() > 0) {
                        showRowViewFew = true;
                        for (int i = 0; i < cnt; i++) {
                            int tag = (int) ((ViewGroup) rowViewTable.tableC.getChildAt(i)).getChildAt(0).getTag();
                            if (tag != 1) {

                                indexList.add(i);
                                (rowViewTable.tableC.getChildAt(i)).setVisibility(GONE);
                                (rowViewTable.tableD.getChildAt(i)).setVisibility(GONE);
                            }

                        }
                        Helper.getInstance(activity).showToast("Tap again to show all", 1, 4);
                    } else {
                        Helper.getInstance(activity).showToast("Select required rows to view", 2, 4);
                    }
                } else {
                    showRowViewFew = false;
                    for (int i = 0; i < cnt; i++) {
                        // int tag = (int) ((ViewGroup) rowViewTable.tableC.getChildAt(i)).getChildAt(0).getTag();
                        (rowViewTable.tableC.getChildAt(i)).setVisibility(View.VISIBLE);
                        (rowViewTable.tableD.getChildAt(i)).setVisibility(View.VISIBLE);
                    }
                }

               /* if (indexList.size() > 0) {
                    //open dialog
                    String tag = "INQUIRY_VIEW";
                    Bundle bundle = new Bundle();
                    bundle.putString("REGION", param2);
                    bundle.putString("TITLE", subTitle);
                    bundle.putString("DYN_PARAM", param1);
                    bundle.putIntegerArrayList("IDS", indexList);
                    bundle.putSerializable("RECORDS", viewRecords);
                    bundle.putStringArrayList("COLUMNS", columns);
                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    Fragment prev = activity.getSupportFragmentManager().findFragmentByTag(tag);
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    final InquiryViewDialog filterDialog = InquiryViewDialog.newInstance();
                    filterDialog.setArguments(bundle);
                    filterDialog.show(ft, tag);
                } else {
                    Helper.getInstance(activity).showToast("Select rows for column view", 1, 4);
                }*/
            } else {
                int cnt = columnViewTable.tableC.getChildCount();
                ArrayList<Integer> indexList = new ArrayList<>();
                //   ArrayList<ArrayList<String>> viewRecords = new ArrayList<>();
                if (!showColViewFew) {
                    for (int i = 0; i < cnt; i++) {
                        int tag = (int) ((ViewGroup) columnViewTable.tableC.getChildAt(i)).getChildAt(0).getTag();
                        if (tag == 1) {
                            indexList.add(i);
                            break;
                        }
                    }
                    if (indexList.size() > 0) {
                        showColViewFew = true;
                        for (int i = 0; i < cnt; i++) {
                            int tag = (int) ((ViewGroup) columnViewTable.tableC.getChildAt(i)).getChildAt(0).getTag();
                            if (tag != 1) {

                                indexList.add(i);
                                (columnViewTable.tableC.getChildAt(i)).setVisibility(GONE);
                                (columnViewTable.tableD.getChildAt(i)).setVisibility(GONE);
                            }
                        }
                        Helper.getInstance(activity).showToast("Tap again to show all", 1, 4);
                    } else {
                        Helper.getInstance(activity).showToast("Select required field(s) to view", 2, 4);
                    }
                } else {
                    showColViewFew = false;
                    for (int i = 0; i < cnt; i++) {
                        (columnViewTable.tableC.getChildAt(i)).setVisibility(View.VISIBLE);
                        (columnViewTable.tableD.getChildAt(i)).setVisibility(View.VISIBLE);
                    }
                }
            }

        });

        Bundle bundle = getArguments();


        String param2 = bundle != null ? bundle.getString("PARAM2", "") : "";
        reg.setText(param2);
        int inputTabNo = bundle != null ? bundle.getInt("TAB_NO", 0) : 0;

        switch (inputTabNo) {
            case 0:
                String inputSvcTag = bundle != null ? bundle.getString("PARAM1", "") : null;
                subTitle = activity.getResources().getString(R.string.title_sub_inquiry_svc_tag);
                param1 = inputSvcTag;

                json = activity.getString(R.string.api_inquiry_service_tag);
                break;
            case 1:
                String inputItem = bundle.getString("PARAM1", "");
                subTitle = activity.getResources().getString(R.string.title_sub_inquiry_item);
                param1 = inputItem;
                json = activity.getString(R.string.api_inquiry_item);
                break;
            case 2:
                String inputLocation = bundle.getString("PARAM1", "");
                subTitle = activity.getResources().getString(R.string.title_sub_inquiry_location);
                param1 = inputLocation;
                json = activity.getString(R.string.api_inquiry_location);
                break;
        }
        String dynParamText = subTitle + ":";
        svcTag.setText(param1);
        dynParam.setText(dynParamText);
        loadData();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.turnOffToolbarScrolling();
        activity.enableBackButtonViews(true);
        activity.turnOffBottomNavigationViewScrolling();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_inquiry);
            actionBar.setSubtitle(subTitle);
        }

    }

    public void showProgressBar() {
        progressBarContainer.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBarContainer.setVisibility(GONE);
    }

    public void sortCol(String sortType, String index, int posX, int posY) {

        Collections.sort(records, (t0, t1) -> {
            if (!sortType.contains("2")) {
                return (t0.get(Integer.parseInt(index))).compareTo((t1.get(Integer.parseInt(index))));
            } else {
                return -1;
            }


        });

        rowViewTable.regenerateTableD(records);
        Handler h = new Handler();
        h.postDelayed(() -> {
            rowViewTable.setScrollPos(posX);
            hideProgressBar();
        }, 100);

    }

    public void loadData() {

        //  String json = activity.getString(R.string.api_inquiry_service_tag);
        Gson gson = new Gson();
        InquirySvcTagResponse response = gson.fromJson(json, InquirySvcTagResponse.class);

        records = response.getValueList();
        columns = response.getColumnList();
/*        Timber.e("cuurrtimestamp: "+System.currentTimeMillis());
        Collections.swap(columns, 5, 1);
        Timber.e("cuurrtimestamp: "+System.currentTimeMillis());
        for (int i = 0; i < records.size(); i++) {
            Collections.swap(records.get(i), 5, 1);
        }
        Timber.e("cuurrtimestamp: "+System.currentTimeMillis());
        */
        rowViewTable = new TableRowLayout(InquirySvcTagFragment.this, activity, columns, records);
        // rowViewContainer.removeAllViews();
        rowViewContainer.addView(rowViewTable);

        //  TableMainLayout rowViewTable = new TableMainLayout(InquirySvcTagFragment.this, activity, res.getColumnList(), res.getValueList());
        // rowViewContainer.addView(rowViewTable);
        //   setUp(rootView);
/*
        Single.create((SingleOnSubscribe<Integer>) e -> doInBg())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(o -> updateView());
                */


    }

}
