package com.dell.inventoryplay;

import android.app.Application;

import com.dell.inventoryplay.request.RequestManager;
import com.dell.inventoryplay.utils.AppLogger;

import java.util.Map;

/**
 * Created by sasikanta on 11/16/2017.
 * InventoryPlayApp
 */

public class InventoryPlayApp extends Application {


    public static Map<String, String> getHeader() {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppLogger.init();
        RequestManager.init(this);
    }

}