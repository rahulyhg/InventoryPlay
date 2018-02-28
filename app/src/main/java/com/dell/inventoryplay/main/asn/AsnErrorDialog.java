package com.dell.inventoryplay.main.asn;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.main.MainActivity;
import com.dell.inventoryplay.response.AsnAppErrorResponse;
import com.dell.inventoryplay.response.AsnResponse;
import com.dell.inventoryplay.utils.Helper;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AsnErrorDialog extends DialogFragment {

    MainActivity activity;
    AsnAppErrorResponse res;
    String inputAsnId, inputAppName, inputTransType, inputSubTransName;
    ArrayList<AsnAppErrorResponse.Records> recordsArrayList;
    ArrayList<AsnResponse.HeaderInfo> headerInfo;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static AsnErrorDialog newInstance() {
        return new AsnErrorDialog();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        LayoutInflater inflater = activity.getLayoutInflater();
        @SuppressLint("InflateParams") View rootView = inflater.inflate(R.layout.frag_asn_error, null);
        Bundle bundle = getArguments();
        inputAsnId = bundle != null ? bundle.getString("ASN_ID", "") : null;
        inputAppName = bundle != null ? bundle.getString("APP_NAME", "") : null;
        inputTransType = bundle != null ? bundle.getString("TRANS_TYPE", "") : null;
        inputSubTransName = bundle != null ? bundle.getString("SUB_TRANS_NAME", "") : null;

        //noinspection unchecked
        headerInfo = (ArrayList<AsnResponse.HeaderInfo>) (bundle != null ? bundle.getSerializable("HEADER") : null);

        recordsArrayList = new ArrayList<>();

        loadData();
        setUp(rootView);
        Dialog dialog = new Dialog(activity, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        //noinspection ConstantConditions
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.setContentView(rootView);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;

    }


    public void loadData() {
        String json = activity.getString(R.string.api_asn_search2);
        Gson gson = new Gson();
        res = gson.fromJson(json, AsnAppErrorResponse.class);
        recordsArrayList = res.getRecords();

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    protected void setUp(View rootView) {
        TextView appListTitle = rootView.findViewById(R.id.appListTitle);
        String title = inputSubTransName + " INFORMATION";
        appListTitle.setText(title);
        Helper.getInstance(activity).createShape(appListTitle);
        RecyclerView rv = rootView.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(new AsnErrorAdapter(recordsArrayList));
    }

}

