package com.dell.inventoryplay.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.main.MainActivity;
import com.dell.inventoryplay.main.asn.AsnErrorAdapter;
import com.dell.inventoryplay.response.AsnAppErrorResponse;
import com.dell.inventoryplay.response.AsnResponse;
import com.google.gson.Gson;

import java.util.ArrayList;

public class HelpDialog extends DialogFragment {

    MainActivity activity;
    private static HelpDialog helpDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static HelpDialog newInstance() {
        if (helpDialog == null)
            helpDialog = new HelpDialog();
        return helpDialog;
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
        @SuppressLint("InflateParams")
        View rootView = inflater.inflate(R.layout.frag_help, null);
        Bundle bundle = getArguments();

        setUp(rootView);
        Dialog dialog = new Dialog(activity, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        //noinspection ConstantConditions
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
       // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.alphaBlack)));

        dialog.show();
        dialog.setContentView(rootView);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;

    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    protected void setUp(View rootView) {
        ImageButton btn = rootView.findViewById(R.id.btn);
        btn.setOnClickListener(view -> {
            helpDialog.dismiss();
        });
    }

}

