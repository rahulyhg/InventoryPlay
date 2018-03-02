package com.dell.inventoryplay.utils;

/**
 * Created by Sasikanta Sahoo on 08/01/17.
 * AppConstants
 */

public final class AppConstants {

    public static final String CHECK_POINT_HEALTH_TAG = "CHECK_POINT_HEALTH_TAG";
    public static final String LOGIN_TAG = "LOGIN_TAG";
    public static final String HEALTH_CHECK_TAG = "HEALTH_CHECK_TAG";
    public static final String FRAG_MAIN_PAGER_TAG = "FRAG_MAIN_PAGER_TAG";
    public static final String FRAG_TRACK_ASN_DETAILS_TAG = "FRAG_TRACK_ASN_DETAILS_TAG";
    public static final String FRAG_INQUIRY_SVC_TAG = "FRAG_INQUIRY_SVC_TAG";
    public static final String FRAG_TRACK_ASN_APP_DETAILS_TAG = "FRAG_TRACK_ASN_APP_DETAILS_TAG";
    public static final String TRACK_ASN_APP_TAG = "TRACK_ASN_APP_TAG";
    public static final String FRAG_TRACK_ASN_APP_ERROR_TAG = "FRAG_TRACK_ASN_APP_ERROR_TAG";
    public static final String FRAG_CHECK_POINT_TAG = "FRAG_CHECK_POINT_TAG";
    public static final String CHECK_POINT_TITLE_TAG ="CHECK_POINT_TITLE_TAG";


    public static final int REQUEST_CODE_CAMERA_ASN = 1000;
    public static final int REQUEST_CODE_CAMERA_INQUIRY = 1001;
    public static final int API_CODE_LOGIN = 1;
    public static final int API_CODE_HEALTH_CHECK = 2;
    public static final int API_CODE_CHECK_POINT_HEALTH = 3;
    public static final int API_CODE_ASN_APP = 4;
    public static final int API_CODE_TRACK_ASN_APP_ERROR = 5;
    public static final int API_CODE_CHECK_POINT_TITLE = 6 ;


    public static int HOME = 0;
    public static int HEALTH_CHECK = 1;
    public static int INQUIRY = 2;
    public static int ASN = 3;

    public static int TOAST_SUCCESS = 1;
    public static int TOAST_WARNING = 2;
    public static int TOAST_ERROR = 3;
    public static int TOAST_INFO = 4;
    public static int TOAST_DEFAULT = 5;
    public static int TOAST_CONFUSING = 6;

    private AppConstants() {
        // This utility class is not publicly instantiable
    }

}