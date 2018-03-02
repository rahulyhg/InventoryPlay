package com.dell.inventoryplay.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.android.volley.Request;
import com.dell.inventoryplay.AppConfig;
import com.dell.inventoryplay.InventoryPlayApp;
import com.dell.inventoryplay.R;
import com.dell.inventoryplay.main.LoginActivity;
import com.dell.inventoryplay.request.BaseGsonRequest;
import com.dell.inventoryplay.request.HttpRequestObject;
import com.dell.inventoryplay.request.RequestManager;
import com.dell.inventoryplay.response.CheckPointResponse;
import com.dell.inventoryplay.utils.AppConstants;
import com.dell.inventoryplay.utils.AppLogger;

import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.NOTIFICATION_SERVICE;


/*
Remove this class once Push notification implemented
*/
public class AlarmReceiver extends WakefulBroadcastReceiver {
    private Context mContext;
    final private int notificationId = 10000;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            mContext = context;
            final PendingResult result = goAsync();
            Thread thread = new Thread() {
                public void run() {
                    int i = 1000;
                    doInBg();
                    result.setResultCode(i);
                    result.finish();
                }
            };
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendNotification(String title) {

        NotificationManager mNotifyMgr = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        if (mNotifyMgr != null) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, getChannelId());
            Intent notificationIntent1 = new Intent(mContext, LoginActivity.class);
            notificationIntent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent intent1 = PendingIntent.getActivity(mContext, 55, notificationIntent1,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            mBuilder.setSmallIcon(R.drawable.ic_launcher);
            mBuilder.setContentTitle("Inventory Play Alert");
            mBuilder.setContentText(title);
            mBuilder.setPriority(Notification.PRIORITY_MAX);
            mBuilder.setAutoCancel(true);
            mBuilder.setContentIntent(intent1);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                notifChannel(mNotifyMgr);
            }
            mNotifyMgr.notify(notificationId, mBuilder.build());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notifChannel(NotificationManager mNotificationManager) {
        String id = getChannelId();
        CharSequence name = "InventoryPlay";
        String description = "Inventory Alert";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(id, name, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.RED);
        mNotificationManager.createNotificationChannel(mChannel);
    }

    public String getChannelId() {
        return "inventory_channel_id";
    }


    public void setAlarm(Context mContext) {
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(mContext, 1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        if (alarmMgr != null) {
            alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                    AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);
        }

        ComponentName receiver = new ComponentName(mContext, BootReceiver.class);
        PackageManager pm = mContext.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);


    }

    public void doInBg() {
        String url = AppConfig.REST_API_CHECK_POINT_TITLE;
        String inputData = "{}";
        int apiCode = AppConstants.API_CODE_CHECK_POINT_TITLE;


        try {
            HttpRequestObject reqObject = HttpRequestObject.getInstance();
            JSONObject jsonRequest = reqObject.getRequestBody(apiCode, inputData);
            BaseGsonRequest<CheckPointResponse> gsonRequest = new BaseGsonRequest<>(Request.Method.POST, url, CheckPointResponse.class, jsonRequest, InventoryPlayApp.getHeader(),
                    res -> {
                        ArrayList<CheckPointResponse.TitleList> titleList = res.getTitleList();
                        if (res.getSuccess() && titleList != null && titleList.size() > 0) {
                            StringBuilder title = new StringBuilder();
                            for (CheckPointResponse.TitleList obj : titleList) {
                                title.append(obj.getTitle()).append("\n");
                            }
                            sendNotification(title.toString());

                        } else {
                            // clear existing notification from tray
                            NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                            if (mNotificationManager != null)
                                mNotificationManager.cancel(notificationId);
                        }
                    }, error -> AppLogger.e("ERROR:" + error.getMessage()));
            RequestManager.getRequestQueue().add(gsonRequest).setTag(AppConstants.CHECK_POINT_TITLE_TAG);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
