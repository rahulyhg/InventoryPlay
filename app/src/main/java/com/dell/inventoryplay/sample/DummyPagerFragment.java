package com.dell.inventoryplay.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.main.MainActivity;

/**
 * Created by sasikanta on 11/14/2017.
 * AssistantPagerFragment
 */

public class DummyPagerFragment extends Fragment {
    public static final String ARG_POSITION = "ARG_POSITION";
    ViewGroup rootView;
    MainActivity activity;

    public static DummyPagerFragment newInstance() {
        return new DummyPagerFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_dummy_page, container, false);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        // setUp(rootView);
        return rootView;
    }


}
