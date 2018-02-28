package com.dell.inventoryplay.main.home;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.dell.inventoryplay.AppConfig;
import com.dell.inventoryplay.DellApp;
import com.dell.inventoryplay.R;
import com.dell.inventoryplay.base.BaseFragment;
import com.dell.inventoryplay.main.MainActivity;
import com.dell.inventoryplay.main.checkpoint.CheckPointFragment;
import com.dell.inventoryplay.request.BaseGsonRequest;
import com.dell.inventoryplay.request.HttpRequestObject;
import com.dell.inventoryplay.request.RequestManager;
import com.dell.inventoryplay.response.CheckPointResponse;
import com.dell.inventoryplay.utils.AppConstants;
import com.dell.inventoryplay.utils.Helper;
import com.dell.inventoryplay.utils.MovableFloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sasikanta on 11/14/2017.
 * HomeStaticFragment
 */

public class HomeStaticFragment extends BaseFragment {
    public static final String ARG_POSITION = "ARG_POSITION";
    ViewGroup rootView;
    MainActivity activity;
    TextView alertHeader;
    LinearLayout alertContainer;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    public static HomeStaticFragment newInstance() {
        return new HomeStaticFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_home_static, container, false);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        setUp(rootView);

        return rootView;
    }

    @Override
    protected void setUp(View rootView) {
        ImageView gifImageView = rootView.findViewById(R.id.gif);
        TextView desc = rootView.findViewById(R.id.desc);
        desc.setText(Html.fromHtml(getResources().getString(R.string.home_desc)));
       /* Glide.with(activity)
                .load(R.drawable.home_anim)
                .into(gifImageView);
                */
        alertHeader = rootView.findViewById(R.id.alertHeader);
        alertHeader.setOnClickListener(view -> {
            Helper.getInstance(activity).showToast("Please wait..", 1, 4);
            alertHeader.setEnabled(false);
            alertHeader.setAlpha(0.3f);
            loadAlert();

        });
        alertContainer = rootView.findViewById(R.id.alertContainer);

        if (AppConfig.useMockJson) {
            String json = getString(R.string.api_check_point_list);
            Gson gson = new Gson();
            CheckPointResponse res = gson.fromJson(json, CheckPointResponse.class);
            makeAlertList(res);
        } else {
            loadAlert();

        }

    }

    void loadAlert() {
        if (Helper.isConnected(activity)) {
            Single.create((SingleOnSubscribe<Integer>) e -> doInBg())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe();
        } else {
            addTextView("No internet connection");
        }
    }

    public void doInBg() {
        String url = AppConfig.REST_API_CHECK_POINT_TITLE;
        String inputData = "{}";
        int apiCode = AppConstants.API_CODE_CHECK_POINT_TITLE;


        try {
            HttpRequestObject reqObject = HttpRequestObject.getInstance();
            JSONObject jsonRequest = reqObject.getRequestBody(apiCode, inputData);
            BaseGsonRequest<CheckPointResponse> gsonRequest = new BaseGsonRequest<>(Request.Method.POST, url, CheckPointResponse.class, jsonRequest, DellApp.getHeader(),
                    res -> {
                        alertHeader.setEnabled(true);
                        alertHeader.setAlpha(1f);
                        if (res.getSuccess()) {
                            makeAlertList(res);
                        }
                    }, error -> {
                alertHeader.setEnabled(true);
                alertHeader.setAlpha(1f);
            });
            RequestManager.getRequestQueue().add(gsonRequest).setTag(AppConstants.CHECK_POINT_TITLE_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeAlertList(CheckPointResponse res) {
        alertContainer.removeAllViews();
        ArrayList<CheckPointResponse.TitleList> titleList = res.getTitleList();
        if (titleList.size() > 0) {
            alertHeader.setTextColor(Color.RED);
        }
        alertHeader.setText(titleList.size() + " alert(s) found.");

        Resources resources = getResources();
        Drawable drawable = resources.getDrawable(R.drawable.ic_navigate_next_black);
        int px = Helper.getInstance(activity).dpToPx(5);
        for (CheckPointResponse.TitleList obj : titleList) {
            TextView tv = new TextView(activity);
            tv.setText(obj.getTitle());
            tv.setTag(obj);
            tv.setPadding(px, px, px, px);
            tv.setTypeface(Typeface.DEFAULT_BOLD);
            tv.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            tv.setOnClickListener(view -> {
                CheckPointResponse.TitleList tag = (CheckPointResponse.TitleList) view.getTag();
                Bundle bundle = new Bundle();
                bundle.putString("ID", tag.getId());
                bundle.putString("TITLE", tag.getTitle());
                FragmentManager fm = activity.getSupportFragmentManager();
                Fragment frag = fm.findFragmentByTag(AppConstants.FRAG_CHECK_POINT_TAG);
                FragmentTransaction ft = fm.beginTransaction();
                if (frag == null)
                    frag = CheckPointFragment.newInstance();
                frag.setArguments(bundle);
                ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_INQUIRY_SVC_TAG)
                        .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                        .addToBackStack(AppConstants.FRAG_INQUIRY_SVC_TAG)
                        .commit();
            });

            alertContainer.addView(tv);
        }
    }

    public void addTextView(String msg) {
        int px = Helper.getInstance(activity).dpToPx(5);
        alertContainer.removeAllViews();
        TextView tv = new TextView(activity);
        tv.setText(msg);
        tv.setPadding(px, px, px, px);
        // tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
        alertContainer.addView(tv);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (MainActivity.currentPage == AppConstants.HOME) {
            menu.clear();
            inflater.inflate(R.menu.home, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (MainActivity.currentPage == AppConstants.HOME) {
            int id = item.getItemId();

            switch (id) {
                case R.id.action_help:
                    showHelp();
                    // Helper.getInstance(activity).showToast("In progress", Toast.LENGTH_LONG, AppConstants.TOAST_SUCCESS);
                    break;



            }
            return super.onOptionsItemSelected(item);
        }
        return false;
    }

}
