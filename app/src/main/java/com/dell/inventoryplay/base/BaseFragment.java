package com.dell.inventoryplay.base;

/*
  Created by sasikanta on 11/16/2017.
  BaseFragment
 */
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.dell.inventoryplay.utils.HelpDialog;


public abstract class BaseFragment extends Fragment implements IView {
    private BaseActivity mActivity;

    protected abstract void setUp(View view);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(false);
        mActivity=(BaseActivity)getActivity();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // setUp(view);
    }
    public void showHelp() {
        Bundle bundle = new Bundle();
        bundle.putString("PAGE", "ROOT_PAGE");
        String tag = "root";
        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
        Fragment prev = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
        if (prev != null) {
            ft.remove(prev);
        }
        final HelpDialog filterDialog = HelpDialog.newInstance();
        filterDialog.setArguments(bundle);
        filterDialog.show(ft, tag);
    }
    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
            activity.onFragmentAttached();
        }
    }


    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }

}
