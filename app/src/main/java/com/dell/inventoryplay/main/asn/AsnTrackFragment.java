package com.dell.inventoryplay.main.asn;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.main.MainActivity;
import com.dell.inventoryplay.response.AsnResponse;
import com.dell.inventoryplay.utils.AppConstants;
import com.dell.inventoryplay.utils.Helper;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Created by sasikanta on 11/14/2017.
 * HomeMainFragment
 */

public class AsnTrackFragment extends Fragment {
    public static final String ARG_POSITION = "ARG_POSITION";
    private MainActivity activity;
    private String[] transTypeArr;
    private TextView titleAsnNo, titleServiceTag;
    private EditText editTextAsnNo, editServiceTag;
    private ListView listView;
    private AsnResponse res;
    private String inputAsnId, inputSvcTag, inputTransType;

    public static AsnTrackFragment newInstance() {
        return new AsnTrackFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_track_an_asn, container, false);

        activity.turnOffToolbarScrolling();
        setRetainInstance(true);
        setUp(rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    protected void setUp(View rootView) {
        titleAsnNo = rootView.findViewById(R.id.titleAsnNo);
        editTextAsnNo = rootView.findViewById(R.id.editTextAsnNo);
        titleServiceTag = rootView.findViewById(R.id.titleServiceTag);
        editServiceTag = rootView.findViewById(R.id.editServiceTag);
        Button search = rootView.findViewById(R.id.search);
        listView = rootView.findViewById(R.id.spinner);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ImageView scan = rootView.findViewById(R.id.scan);
        TextView titleFlow = rootView.findViewById(R.id.titleFlow);
        Helper.getInstance(activity).createShape(titleFlow);

        transTypeArr = activity.getResources().getStringArray(R.array.asn_dropdown_array);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                R.array.asn_dropdown_array, android.R.layout.simple_list_item_single_choice);
        listView.setAdapter(adapter);
        editTextAsnNo.addTextChangedListener(generalTextWatcher);
        editTextAsnNo.setOnEditorActionListener(generalEditorAction);
        editServiceTag.addTextChangedListener(generalTextWatcher);
        editServiceTag.setOnEditorActionListener(generalEditorAction);
        scan.setOnClickListener(v -> scanNow());

        search.setOnClickListener(v -> {
            // AppLogger.e("INPUT %s %s %s", transType, editTextAsnNo.getText(), editServiceTag.getText());
            performSearch();


        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Toast.makeText(activity, requestCode + "::" + resultCode, Toast.LENGTH_SHORT).show();
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            // display it on screen
            Toast.makeText(activity, scanContent + "FORMATTTT: " + scanFormat, Toast.LENGTH_SHORT).show();
            if (scanContent != null)
                editServiceTag.setText(scanContent);
        } else {
            Toast toast = Toast.makeText(activity, "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == AppConstants.REQUEST_CODE_CAMERA_ASN && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            scanNow();
        }
    }

    public void scanNow() {
        if (!activity.hasPermission(Manifest.permission.CAMERA)) {
            activity.requestPermissionsSafely(new String[]{Manifest.permission.CAMERA}, AppConstants.REQUEST_CODE_CAMERA_ASN);
        } else {
            IntentIntegrator integrator = new IntentIntegrator(activity);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
            integrator.setPrompt("Scan a barcode");
            integrator.setResultDisplayDuration(0);
            //integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
            integrator.setCameraId(0);  // Use a specific camera of the device
            integrator.initiateScan();
        }
    }


    private TextWatcher generalTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if ((editTextAsnNo.getText().hashCode() == s.hashCode())) {
                if (count > 0) {
                    titleAsnNo.setVisibility(View.VISIBLE);
                } else {
                    titleAsnNo.setVisibility(View.INVISIBLE);
                }
            } else if ((editServiceTag.getText().hashCode() == s.hashCode())) {
                if (count > 0) {
                    titleServiceTag.setVisibility(View.VISIBLE);
                } else {
                    titleServiceTag.setVisibility(View.INVISIBLE);
                }
            }


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private TextView.OnEditorActionListener generalEditorAction = (v, actionId, event) -> {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            performSearch();
            return true;
        }
        return false;
    };

    private void performSearch() {
        int position = listView.getCheckedItemPosition();
        inputTransType = position != -1 ? transTypeArr[position] : null;
        inputAsnId = editTextAsnNo.getText().toString().trim();
        inputSvcTag = editServiceTag.getText().toString().trim();
        if (inputTransType != null && (inputAsnId.length() > 0 || inputSvcTag.length() > 0)) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                activity.hideKeyboard();
                //InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            loadData();

        } else {
            Helper.getInstance(activity).showToast("Invalid Input", Toast.LENGTH_SHORT, 3);
            // Toast.makeText(activity, "Invalid Input", Toast.LENGTH_SHORT).show();
        }
    }

    public void resultPage() {
        Bundle bundle = new Bundle();
        bundle.putString("ASN_ID", inputAsnId);
        bundle.putString("SVC_TAG", inputSvcTag);
        bundle.putString("TRANS_TYPE", inputTransType);

        bundle.putSerializable("RES", res);
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment frag = fm.findFragmentByTag(AppConstants.FRAG_TRACK_ASN_DETAILS_TAG);
        FragmentTransaction ft = fm.beginTransaction();
        if (frag == null)
            frag = AsnTrackResultFragment.newInstance();
        frag.setArguments(bundle);
        ft.add(R.id.frameContainer, frag, AppConstants.FRAG_TRACK_ASN_DETAILS_TAG)
                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .addToBackStack(AppConstants.FRAG_TRACK_ASN_DETAILS_TAG)
                .commit();
    }

    public void loadData() {
        String json = activity.getString(R.string.api_asn_search0);

        Gson gson = new Gson();
        res = gson.fromJson(json, AsnResponse.class);


        resultPage();
/*
        Single.create((SingleOnSubscribe<Integer>) e -> doInBg())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(o -> updateView());
                */

    }
/*
    public void updateView() {
        //setUp(rootView);
    }

    public void doInBg() {
        String jsonStr = "{\"ASN_ID\": \"100000001\",\"SVC_TAG\": \"tag_service\",\"TRANS_TYPE\": \"3PL\"}";
        try {
            HttpRequestObject mReqobject = HttpRequestObject.getInstance();
            JSONObject jsonRequest = mReqobject.getRequestBody(AppConstants.ASN_TRACK_SEARCH_API, jsonStr);
            AsnGsonRequest gsonRequest = new AsnGsonRequest(Request.Method.POST, AppConstants.API_BASE_URL, jsonRequest, res -> {

            }, error -> {
            }, AsnResponse.class, null);
            RequestManager.getRequestQueue().add(gsonRequest).setTag(AppConstants.TRACK_ASN_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}