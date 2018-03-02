package com.dell.inventoryplay.main.asn;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.base.BaseFragment;
import com.dell.inventoryplay.main.MainActivity;
import com.dell.inventoryplay.response.AsnAppDetailsResponse;
import com.dell.inventoryplay.response.AsnResponse;
import com.dell.inventoryplay.utils.Helper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sasikanta_Sahoo on 12/12/2017.
 * AsnTrackResultFragment
 */

public class AsnTrackResultFragment extends BaseFragment {
    private ViewGroup rootView;
    private MainActivity activity;
    private AsnResponse res;
    private String inputAsnId;
    private String inputTransType;
    private ExpandableListView expListView;
    private List<AsnResponse.Records> listDataHeader;
    private ArrayMap<AsnResponse.Records, ArrayList<AsnAppDetailsResponse.Records>> listDataChild;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    public static Fragment newInstance() {
        return new AsnTrackResultFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_track_asn_details, container, false);

        setRetainInstance(false);

        Bundle bundle = getArguments();
        inputAsnId = bundle != null ? bundle.getString("ASN_ID", "") : null;
        inputTransType = bundle != null ? bundle.getString("TRANS_TYPE", "") : null;
        res = (AsnResponse) (bundle != null ? bundle.getSerializable("RES") : null);
        setUp(rootView);

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
            actionBar.setTitle(R.string.title_asn_track);
            actionBar.setSubtitle(getString(R.string.title_sub_asn_track));
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ArrayList<AsnResponse.HeaderInfo> headerInfo = res.getHeaderInfo();
        if (headerInfo != null) {
            LinearLayout headerContainer = rootView.findViewById(R.id.headerContainer);
            Helper.getInstance(activity).setHeaderInfo(headerContainer, headerInfo);
        }
    }

    protected void setUp(View rootView) {
        ArrayList<AsnResponse.HeaderInfo> headerInfo = res.getHeaderInfo();
        LinearLayout headerContainer = rootView.findViewById(R.id.headerContainer);
        Helper.getInstance(activity).setHeaderInfo(headerContainer, headerInfo);
        TextView headerTitle = rootView.findViewById(R.id.headerTitle);
        TextView appListTitle = rootView.findViewById(R.id.appListTitle);
        Helper.getInstance(activity).createShape(headerTitle);
        Helper.getInstance(activity).createShape(appListTitle);
        String title = inputTransType + " " + getString(R.string.title_asn_track_header);
        appListTitle.setText(title);
        prepareListData();
        ExpandableListAdapter listAdapter = new AsnAppExpandableAdapter(activity, listDataHeader, listDataChild);
        expListView = rootView.findViewById(R.id.appList);
        expListView.setAdapter(listAdapter);
        expListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            ViewGroup vg = ((ViewGroup) v);
            try {
                if (expListView.isGroupExpanded(groupPosition)) {
                    expListView.collapseGroup(groupPosition);
                    vg.findViewById(R.id.progress).setVisibility(View.GONE);
                } else {

                    vg.findViewById(R.id.progress).setVisibility(View.VISIBLE);
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        //Do something after 100ms
                        vg.findViewById(R.id.progress).setVisibility(View.GONE);
                        //   String inputAppName = listDataHeader.get(groupPosition).getAppName();
                        //  String jsonStr = "{\"ASN_ID\": \"+inputAsnId+\",\"APP_NAME\": \"+inputAppName+\",\"TRANS_TYPE\": \"+inputTransType+\"}";
                        String json = activity.getString(R.string.api_asn_search1);
                        Gson gson = new Gson();
                        AsnAppDetailsResponse resChild = gson.fromJson(json, AsnAppDetailsResponse.class);
                        ArrayList<AsnAppDetailsResponse.Records> recordsChildArrayList = resChild.getRecords();
                        listDataChild.put(listDataHeader.get(groupPosition), recordsChildArrayList);
                        expListView.expandGroup(groupPosition);
                    }, 2000);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return true;
        });

        expListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {


            AsnResponse.Records appObj = listDataHeader.get(groupPosition);
            AsnAppDetailsResponse.Records obj = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);

            if (obj.getMsgType().contains("ERROR")) {
                String tag = "ASN_ERROR";
                Bundle bundle = new Bundle();
                bundle.putString("ASN_ID", inputAsnId);
                bundle.putString("APP_NAME", appObj.getAppName());
                bundle.putString("TRANS_TYPE", inputTransType);
                bundle.putString("SUB_TRANS_NAME", obj.getSubTransName());
                bundle.putSerializable("HEADER", headerInfo);
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                Fragment prev = activity.getSupportFragmentManager().findFragmentByTag(tag);
                if (prev != null) {
                    ft.remove(prev);
                }
                final AsnErrorDialog filterDialog = AsnErrorDialog.newInstance();
                filterDialog.setArguments(bundle);
                filterDialog.show(ft, tag);
            }
            return true;
        });


    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new ArrayMap<>();
        ArrayList<AsnResponse.Records> dataList = res.getRecords();
        listDataHeader.addAll(dataList);


    }
}
