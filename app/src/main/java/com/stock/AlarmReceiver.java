package com.stock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.stock.retrofit.model.IPOData;
import com.stock.retrofit.model.IPODataEntityManager;
import com.stock.retrofit.model.SmeIPOData;
import com.stock.retrofit.model.SmeIPODataEntityManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class AlarmReceiver extends BroadcastReceiver {

    private String TAG = "AlarmReceiver";
    private List<IPOData> notificationArray;
    private List<SmeIPOData> notificationArray1;
    private String currentDate;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        Calendar calender = Calendar.getInstance();
        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                NotificationScheduler.setReminder(context, AlarmReceiver.class);
                return;
            }
        }

        //Trigger the notification
//        NotificationScheduler. showNotification(context, MainActivity.class,
//                "You have 5 unwatched videos", "Watch them now?");

        notificationArray = new ArrayList<>();
        IPODataEntityManager ipoDataEntityManager = new IPODataEntityManager();
        notificationArray = ipoDataEntityManager.select().asList();
        for (int i = 0; i < ipoDataEntityManager.select().count(); i++) {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date today = Calendar.getInstance().getTime();
            String startDate[] = notificationArray.get(i).getIpoDate().trim().split("-");
            currentDate = df.format(today);

            if (currentDate.equals(startDate[0].trim())) {
                NotificationScheduler.showNotification(context, MainActivity.class,
                        "IPO", "Watch them now!");
            }
        }

        notificationArray1 = new ArrayList<>();
        SmeIPODataEntityManager ipoDataEntityManager1 = new SmeIPODataEntityManager();
        notificationArray1 = ipoDataEntityManager1.select().asList();
        for (int i = 0; i < ipoDataEntityManager1.select().count(); i++) {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date today = Calendar.getInstance().getTime();
            String startDate[] = notificationArray1.get(i).getIpoDate().trim().split("-");
            currentDate = df.format(today);

            if (currentDate.equals(startDate[0].trim())) {
                NotificationScheduler.showNotification(context, MainActivity.class,
                        "SME IPO", "Watch them now!");
            }
        }
    }
}


