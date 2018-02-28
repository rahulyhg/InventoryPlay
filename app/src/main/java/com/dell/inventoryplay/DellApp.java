package com.dell.inventoryplay;

import android.app.Application;
import android.os.StrictMode;

import com.dell.inventoryplay.request.RequestManager;
import com.dell.inventoryplay.utils.AppLogger;

import java.util.Map;

/**
 * Created by sasikanta on 11/16/2017.
 * DellApp
 */

public class DellApp extends Application {

    static Map<String, String> mHeaders;
    private boolean DEVELOPER_MODE = false;

    public static void setHeader(Map<String, String> headers) {
        mHeaders = headers;
    }

    public static Map<String, String> getHeader() {
        // return mHeaders;
        // no use of custom header now for data power.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppLogger.init();
        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectNetwork()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(
                    new StrictMode.VmPolicy.Builder()
                            .detectLeakedSqlLiteObjects()
                            .detectLeakedClosableObjects()
                            .penaltyLog()
                            .penaltyDeath()
                            .detectAll()
                            .build());
        }
        RequestManager.init(this);
    }

}