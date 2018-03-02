package com.dell.inventoryplay.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.main.MainActivity;

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

        setUp(rootView);
        Dialog dialog = new Dialog(activity, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        //noinspection ConstantConditions
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

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
        btn.setOnClickListener(view -> helpDialog.dismiss());
    }

}

