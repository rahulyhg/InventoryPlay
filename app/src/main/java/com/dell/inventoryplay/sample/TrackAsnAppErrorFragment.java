package com.dell.inventoryplay.sample;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.dell.inventoryplay.main.asn.AsnErrorAdapter;
import com.dell.inventoryplay.request.BaseGsonRequest;
import com.dell.inventoryplay.request.HttpRequestObject;
import com.dell.inventoryplay.request.RequestManager;
import com.dell.inventoryplay.response.AsnAppErrorResponse;
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
 * TrackAsnAppErrorFragment
 */

public class TrackAsnAppErrorFragment extends BaseFragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    ViewGroup rootView;
    MainActivity mContext;
    MenuItem prevMenuItem;
    BottomNavigationViewHelper bottomNavigationViewHelper;
    BottomNavigationView bottomNavigationView;
    AsnAppErrorResponse res;
    String inputAsnId, inputAppName, inputTransType, inputSubTransName, inputSvcTag, inputRegion, inputTransFlag, inputDateTime, inputMsgType, inputSubTransDate;
    ArrayList<AsnAppErrorResponse.Records> recordsArrayList;
    ArrayList<AsnResponse.HeaderInfo> headerInfo;

    public static Fragment newInstance(MainActivity mainActivity) {
        return new TrackAsnAppErrorFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_track_asn_app_error, container, false);

        setRetainInstance(false);
        mContext = (MainActivity) getActivity();
        mContext.enableBackButtonViews(true);
        mContext.turnOffToolbarScrolling();
        mContext.turnOffBottomNavigationViewScrolling();
        mContext.getSupportActionBar().setTitle("Track AN ASN");
        mContext.getSupportActionBar().setSubtitle("App Error Report");
        bottomNavigationView = mContext.bottomNavigationView;
        bottomNavigationViewHelper = mContext.bottomNavigationViewHelper;

        Bundle bundle = getArguments();
        inputAsnId = bundle.getString("ASN_ID", "");
        inputAppName = bundle.getString("APP_NAME", "");
        inputTransType = bundle.getString("TRANS_TYPE", "");
        inputSubTransName = bundle.getString("SUB_TRANS_NAME", "");

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
        String json = mContext.getString(R.string.api_asn_search2);
        Gson gson = new Gson();
        res = gson.fromJson(json, AsnAppErrorResponse.class);

        recordsArrayList = ((AsnAppErrorResponse) res).getRecords();
        // AppLogger.e("reg reg %s", reg);

    }

    @Override
    protected void setUp(View rootView) {
        LinearLayout headerContainer = rootView.findViewById(R.id.headerContainer);
        Helper.getInstance(mContext).setHeaderInfo(headerContainer, headerInfo);

        TextView headerTitle = rootView.findViewById(R.id.headerTitle);
        TextView appListTitle = rootView.findViewById(R.id.appListTitle);
        appListTitle.setText(inputSubTransName + " INFORMATION");
        Helper.getInstance(mContext).createShape(headerTitle);
        Helper.getInstance(mContext).createShape(appListTitle);


        RecyclerView rv = rootView.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        //   rv.setItemAnimator(new DefaultItemAnimator());
        //  rv.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        rv.setAdapter(new AsnErrorAdapter(recordsArrayList));
    }

    public void doInBg() {

        String inputData = "{\"ASN_ID\": \"+inputAsnId+\",\"APP_NAME\": \"+inputAppName+\",\"TRANS_TYPE\": \"+inputTransType+\"}";
        String url = AppConfig.REST_API_TRACK_ASN_APP_ERROR;
        int apiCode = AppConstants.API_CODE_TRACK_ASN_APP_ERROR;
        try {
            HttpRequestObject reqObject = HttpRequestObject.getInstance();
            JSONObject jsonRequest = reqObject.getRequestBody(apiCode, inputData);
            BaseGsonRequest<AsnAppErrorResponse> gsonRequest = new BaseGsonRequest<>(Request.Method.POST, url, AsnAppErrorResponse.class, jsonRequest, DellApp.getHeader(),
                    res -> {
                    }, error -> {
                AppLogger.e("ERRORRR:" + error.getMessage());
            });
            RequestManager.getRequestQueue().add(gsonRequest).setTag(AppConstants.FRAG_TRACK_ASN_APP_ERROR_TAG);
       /* String jsonStr = "{\"ASN_ID\": \"+mInputAsnId+\",\"APP_NAME\": \"+mInputAppName+\",\"TRANS_TYPE\": \"+mInputTransType+\",\"SUB_TRANS_NAME\": \"+inputSubTransName+\"}";
        try {
            HttpRequestObject mReqobject = HttpRequestObject.getInstance();
            JSONObject jsonRequest = mReqobject.getRequestBody(AppConstants.TRACK_ASN_APP_ERROR_API, jsonStr);
            AsnAppErrorGsonRequest gsonRequest = new AsnAppErrorGsonRequest(Request.Method.POST, AppConstants.API_BASE_URL, jsonRequest, res -> {
            }, error -> {
            }, AsnAppErrorResponse.class, null);
            RequestManager.getRequestQueue().add(gsonRequest).setTag(AppConstants.FRAG_TRACK_ASN_APP_ERROR_TAG);
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

    }
}
