package com.moutamid.unnepek.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.moutamid.unnepek.widget.WidgetProvider;

import java.util.Calendar;
import java.util.Date;

public class DateChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("DateChangeReceiver", "onReceive: " + action + " at " + new Date());

//        if (Intent.ACTION_TIME_TICK.equals(action)
//                || Intent.ACTION_DATE_CHANGED.equals(action)
//                || Intent.ACTION_TIMEZONE_CHANGED.equals(action)
//                || AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)
//                || "MIDNIGHT_UPDATE".equals(action)) {

            refreshWidget(context);
//        }
    }

    private void refreshWidget(Context context) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        ComponentName widget = new ComponentName(context, WidgetProvider.class);
        int[] ids = manager.getAppWidgetIds(widget);

        // Set month/year to today's values
        Calendar calendar = Calendar.getInstance();
        WidgetProvider.month = calendar.get(Calendar.MONTH);
        WidgetProvider.year = calendar.get(Calendar.YEAR);

        for (int id : ids) {
            WidgetProvider.updateAppWidget(context, manager, id);
        }
    }

    public static void scheduleMidnightUpdate(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, DateChangeReceiver.class);
        intent.setAction("MIDNIGHT_UPDATE");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Calculate next midnight
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
        );

        Log.d("DateChangeReceiver", "Midnight update scheduled for " + calendar.getTime());
    }
}
