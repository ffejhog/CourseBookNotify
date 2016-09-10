package com.jeffreyneer.coursebooknotify;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Project: CourseBook Notify
 * Author: Jeffrey Neer
 */

//Boot Reciever to rest alarms at boot time
public class BootReceiver extends BroadcastReceiver {

    public final String DATABASE = "database";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SharedPreferences database = context.getSharedPreferences(DATABASE, 0);
            boolean AlarmSet = database.getBoolean("AlarmOn", false);
            int AlarmTimeSet = database.getInt("AlarmTime", 0);

            if (AlarmSet) {
                Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                alarmIntent.setAction("com.jeffreyneer.coursebooknotify.alarm");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 11, alarmIntent, 0);


                //Find the required interval between alarms
                long interval;
                switch (AlarmTimeSet) {
                    case 0:
                        interval = AlarmManager.INTERVAL_HALF_HOUR;
                        break;
                    case 1:
                        interval = AlarmManager.INTERVAL_HOUR;
                        break;
                    case 2:
                        interval = AlarmManager.INTERVAL_HOUR * 3;
                        break;
                    case 3:
                        interval = AlarmManager.INTERVAL_HOUR * 6;
                        break;
                    case 4:
                        interval = AlarmManager.INTERVAL_DAY;
                        break;
                    default:
                        interval = AlarmManager.INTERVAL_DAY;
                        break;
                }
                AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, interval,  interval, pendingIntent);
            }
        }

    }
}