package com.moutamid.unnepek;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;

public class DateChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Constants.TAG", "onReceive: DATE CHANGED "+new Date());
        String action = intent.getAction();
        if (Intent.ACTION_DATE_CHANGED.equals(action) || Intent.ACTION_TIME_CHANGED.equals(action) || Intent.ACTION_TIMEZONE_CHANGED.equals(action)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName widget = new ComponentName(context, WidgetProvider.class);
            int[] widgetIds = appWidgetManager.getAppWidgetIds(widget);

            for (int id : widgetIds) {
                WidgetProvider.updateAppWidget(context, appWidgetManager, id);
            }
        }
    }

    private void refreshWidget(Context context) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        ComponentName widget = new ComponentName(context, WidgetProvider.class);
        int[] ids = manager.getAppWidgetIds(widget);

        for (int id : ids) {
            WidgetProvider.updateAppWidget(context, manager, id);
        }
    }
}
