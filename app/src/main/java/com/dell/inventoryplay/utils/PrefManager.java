package com.dell.inventoryplay.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PrefManager {

    private static final String PROPERTY_FIRST_USE = "FIRST_USE";
    private static final String PROPERTY_FIRST_TIME_HELP = "FIRST_TIME_HELP";
    private static final String PROPERTY_USER_NAME = "USER_NAME";
    private static final String PROPERTY_PASSWORD = "PASSWORD";
    private Context mContext;
    private SharedPreferences pref;
    private Editor editor;
    private static PrefManager instance;
    private String userName,password;
    private boolean isFirstUse,isFirstTimeHelp;


    private PrefManager(Context ctx) {
        mContext = ctx;
        pref = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        editor = pref.edit();
    }

    public static PrefManager getInstance(Context ctx) {
        if (instance == null)
            instance = new PrefManager(ctx);
        return instance;
    }

    public void load() {
        isFirstUse = pref.getBoolean(PROPERTY_FIRST_USE, true);
        isFirstTimeHelp = pref.getBoolean(PROPERTY_FIRST_TIME_HELP, true);
        userName = pref.getString(PROPERTY_USER_NAME, null);
        password = pref.getString(PROPERTY_PASSWORD, null);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String mUserName) {
        userName = mUserName;
        editor.putString(PROPERTY_USER_NAME,userName);
        editor.commit();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String mPassword) {
        password = mPassword;
        editor.putString(PROPERTY_PASSWORD,password);
        editor.commit();

    }

    public boolean isFirstUse() {
        return isFirstUse;
    }

    public void setFirstUse(boolean firstUse) {
        isFirstUse = firstUse;
        editor.putBoolean(PROPERTY_FIRST_USE,isFirstUse);
        editor.commit();
    }
    public boolean isFirstTimeHelp() {
        return isFirstTimeHelp;
    }

    public void setFirstTimeHelp(boolean firstTimeHelp) {
        isFirstTimeHelp= firstTimeHelp;
        editor.putBoolean(PROPERTY_FIRST_TIME_HELP,isFirstTimeHelp);
        editor.commit();
    }
}
