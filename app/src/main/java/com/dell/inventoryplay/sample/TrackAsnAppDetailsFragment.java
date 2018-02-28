package com.dell.inventoryplay.sample;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.dell.inventoryplay.AppConfig;
import com.dell.inventoryplay.DellApp;
import com.dell.inventoryplay.R;
import com.dell.inventoryplay.base.BaseFragment;
import com.dell.inventoryplay.main.MainActivity;
import com.dell.inventoryplay.main.RecyclerItemTouchHelper;
import com.dell.inventoryplay.request.BaseGsonRequest;
import com.dell.inventoryplay.request.HttpRequestObject;
import com.dell.inventoryplay.request.RequestManager;
import com.dell.inventoryplay.response.AsnAppDetailsResponse;
import com.dell.inventoryplay.response.AsnResponse;
import com.dell.inventoryplay.utils.AppConstants;
import com.dell.inventoryplay.utils.AppLogger;
import com.dell.inventoryplay.utils.BottomNavigationViewHelper;
import com.dell.inventoryplay.utils.Helper;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sasikanta_Sahoo on 12/12/2017.
 */

public class TrackAsnAppDetailsFragment extends BaseFragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    ViewGroup rootView;
    MainActivity mContext;
    MenuItem prevMenuItem;
    BottomNavigationViewHelper bottomNavigationViewHelper;
    BottomNavigationView bottomNavigationView;
    AsnAppDetailsResponse res;
    String inputAsnId, inputAppName, inputTransType, inputMsgType, inputDateTime, inputRegion, inputTransFlag, inputSvcTag;
    ArrayList<AsnAppDetailsResponse.Records> recordsArrayList;
    ArrayList<AsnResponse.HeaderInfo> headerInfo;

    public static Fragment newInstance(MainActivity mainActivity) {
        return new TrackAsnAppDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_track_asn_app_details, container, false);

        setRetainInstance(false);
        mContext = (MainActivity) getActivity();
        mContext.enableBackButtonViews(true);
        mContext.turnOffToolbarScrolling();
        mContext.turnOffBottomNavigationViewScrolling();
        mContext.getSupportActionBar().setTitle("Track AN ASN");
        mContext.getSupportActionBar().setSubtitle("App Details Report");
        bottomNavigationView = mContext.bottomNavigationView;
        bottomNavigationViewHelper = mContext.bottomNavigationViewHelper;

        Bundle bundle = getArguments();
        inputAsnId = bundle.getString("ASN_ID", "");
        inputAppName = bundle.getString("APP_NAME", "");
        inputTransType = bundle.getString("TRANS_TYPE", "");


        headerInfo = (ArrayList<AsnResponse.HeaderInfo>) bundle.getSerializable("HEADER");
        recordsArrayList = new ArrayList<>();
        setUp(rootView);
        loadData();
/*    Single.create((SingleOnSubscribe<Integer>) e -> doInBg())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(o -> updateView());
                */
        return rootView;
    }

    public void loadData() {
        String json = mContext.getString(R.string.api_asn_search1);
        Gson gson = new Gson();
        res = gson.fromJson(json, AsnAppDetailsResponse.class);

        recordsArrayList = ((AsnAppDetailsResponse) res).getRecords();
        // AppLogger.e("reg reg %s", reg);

    }

    @Override
    protected void setUp(View rootView) {


        LinearLayout headerContainer = rootView.findViewById(R.id.headerContainer);
        Helper.getInstance(mContext).setHeaderInfo(headerContainer, headerInfo);
        TextView headerTitle = rootView.findViewById(R.id.headerTitle);
        TextView appListTitle = rootView.findViewById(R.id.appListTitle);
        appListTitle.setText(inputAppName+" INFORMATION");

        Helper.getInstance(mContext).createShape(headerTitle);
        Helper.getInstance(mContext).createShape(appListTitle);
        RecyclerView rv = rootView.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        //   rv.setItemAnimator(new DefaultItemAnimator());
        //  rv.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv);
        rv.setAdapter(new TrackAsnAppDetailsAdapter(mContext, recordsArrayList, inputAsnId, inputAppName, inputTransType, headerInfo));
    }

    public void doInBg() {
        String inputData = "{\"ASN_ID\": \"+inputAsnId+\",\"APP_NAME\": \"+inputAppName+\",\"TRANS_TYPE\": \"+inputTransType+\"}";
        String url = AppConfig.REST_API_ASN_APP;
        int apiCode = AppConstants.API_CODE_ASN_APP;

        try {
            HttpRequestObject reqObject = HttpRequestObject.getInstance();
            JSONObject jsonRequest = reqObject.getRequestBody(apiCode, inputData);
            BaseGsonRequest<AsnAppDetailsResponse> gsonRequest = new BaseGsonRequest<>(Request.Method.POST, url, AsnAppDetailsResponse.class, jsonRequest, DellApp.getHeader(),
                    res -> {
                    }, error -> {
                AppLogger.e("ERRORRR:" + error.getMessage());
            });
            RequestManager.getRequestQueue().add(gsonRequest).setTag(AppConstants.TRACK_ASN_APP_TAG);
       /* try {
            HttpRequestObject mReqobject = HttpRequestObject.getInstance();
            JSONObject jsonRequest = mReqobject.getRequestBody(AppConstants.TRACK_ASN_APP_DETAILS_API, jsonStr);
            AsnAppDetailsGsonRequest gsonRequest = new AsnAppDetailsGsonRequest(Request.Method.POST, AppConstants.API_BASE_URL, jsonRequest, res -> {
            }, error -> {
            }, AsnAppDetailsResponse.class, null);
            RequestManager.getRequestQueue().add(gsonRequest).setTag(AppConstants.TRACK_ASN_APP_TAG);
          */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

    }
}
