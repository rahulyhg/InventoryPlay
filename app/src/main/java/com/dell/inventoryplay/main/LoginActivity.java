package com.dell.inventoryplay.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.dell.inventoryplay.AppConfig;
import com.dell.inventoryplay.DellApp;
import com.dell.inventoryplay.R;
import com.dell.inventoryplay.request.BaseGsonRequest;
import com.dell.inventoryplay.request.HttpRequestObject;
import com.dell.inventoryplay.request.RequestManager;
import com.dell.inventoryplay.response.LoginResponse;
import com.dell.inventoryplay.utils.AppConstants;
import com.dell.inventoryplay.utils.Helper;
import com.dell.inventoryplay.utils.PrefManager;

import org.json.JSONObject;

import java.util.Calendar;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Sasikanta_Sahoo on 2/8/2018.
 * LoginActivity
 */

public class LoginActivity extends AppCompatActivity {
    EditText userName, password;
    FrameLayout loader;
    TextView remember;
    String userNameStr, passwordStr;
    CheckBox mock;
    PrefManager mPref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPref = PrefManager.getInstance(this);
        mPref.load();
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        Button submit = findViewById(R.id.submit);
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        remember = findViewById(R.id.remember);
        loader = findViewById(R.id.loader);
        mock = findViewById(R.id.mock);
        userName.setOnEditorActionListener(generalEditorAction);
        password.setOnEditorActionListener(generalEditorAction);
        remember.setOnClickListener(view -> {
            if (mock.isChecked())
                mock.setChecked(false);
            else
                mock.setChecked(true);
        });
        submit.setOnClickListener(view -> {
            if (!AppConfig.useMockJson)
                performSearch();
            else
                setUp();
        });
        if (mPref.getUserName() != null) {
            mock.setChecked(true);
            userName.setText(mPref.getUserName());
            password.setText(mPref.getPassword());
        }


    }

    public void hideKeyboard() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

    }

    private void performSearch() {

        Calendar cal = Calendar.getInstance();
        int MONTH = cal.get(Calendar.MONTH);
        int YEAR = cal.get(Calendar.YEAR);
        int DAY_OF_MONTH = cal.get(Calendar.DAY_OF_MONTH);


        hideKeyboard();
        userNameStr = userName.getText().toString().trim();
        passwordStr = password.getText().toString().trim();


        if (userNameStr.length() == 0 || passwordStr.length() == 0) {
            Helper.getInstance(this).showToast("Invalid credential.", 1, 3);

        } else if (Helper.isConnected(this)) {
            loadData();
        } else {

            Helper.getInstance(this).showToast("Connect internet & tap here.", 1, 3);

        }


    }

    public void loadData() {
        loader.setVisibility(View.VISIBLE);
        Single.create((SingleOnSubscribe<Integer>) e -> doInBg())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(o -> updateView());
    }

    private void updateView() {
    }

    public void doInBg() {
        String url = AppConfig.REST_API_LOGIN;
        String inputData = "{'userName':'" + userNameStr + "','password':'" + passwordStr + "'}";
        int apiCode = AppConstants.API_CODE_LOGIN;
        try {
            HttpRequestObject reqObject = HttpRequestObject.getInstance();
            JSONObject jsonRequest = reqObject.getRequestBody(apiCode, inputData);
            BaseGsonRequest<LoginResponse> gsonRequest = new BaseGsonRequest<>(Request.Method.POST, url, LoginResponse.class, jsonRequest, DellApp.getHeader(),
                    res -> {
                        if (res.getSuccess() && res.getValidUser()) {
                            setUp();
                        } else if (res.getSuccess() && !res.getValidUser()) {
                            loader.setVisibility(View.GONE);
                            String msgStr = "Sorry, Invalid user";
                            Helper.getInstance(this).showToast("Invalid credential.", 1, 3);
                        } else {
                            loader.setVisibility(View.GONE);
                            String msgStr = res.getMessage();
                        }
                    }, error -> {
                loader.setVisibility(View.GONE);
                String msgStr = "Sorry, unable to process response now";
                //  msg.setText(msgStr);
                Helper.getInstance(this).showToast(msgStr, 1, 3);
            });
            RequestManager.getRequestQueue().add(gsonRequest).setTag(AppConstants.LOGIN_TAG);




           /* HttpRequestObject reqObject = HttpRequestObject.getInstance();
            JSONObject jsonRequest = reqObject.getRequestBody(apiCode, inputData);




            LoginGsonRequest gsonRequest = new LoginGsonRequest(Request.Method.POST, url, jsonRequest, res -> {
                if (((LoginResponse) res).getSuccess() && ((LoginResponse) res).getValidUser()) {
                    setUp();
                } else {
                    loader.setVisibility(View.GONE);
                    String msgStr = ((LoginResponse) res).getMessage();
                    msg.setText("" + msgStr);
                    noInternet.setVisibility(View.VISIBLE);
                }
            }, error -> {
                loader.setVisibility(View.GONE);
                String msgStr = "Sorry, unable to process response now";
                msg.setText(msgStr);
                AppLogger.e("ERRORRR:" + error.getMessage());
                noInternet.setVisibility(View.VISIBLE);
            }, LoginResponse.class, DellApp.getHeader());
            RequestManager.getRequestQueue().add(gsonRequest).setTag(AppConstants.LOGIN_TAG);
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUp() {
        if (mock.isChecked()) {
            mPref.setPassword(passwordStr);
            mPref.setUserName(userNameStr);
        } else {
            mPref.setPassword(null);
            mPref.setUserName(null);
        }

        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        i.putExtra("USER_NAME", userNameStr);
        startActivity(i);
        finish();
    }


    private TextView.OnEditorActionListener generalEditorAction = (v, actionId, event) -> {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            performSearch();
            return true;
        }
        return false;
    };

}
