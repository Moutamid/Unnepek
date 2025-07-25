package com.moutamid.unnepek;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

public class WidgetProvider extends AppWidgetProvider {
    static RemoteViews views;
    public static final String ACTION_CURRENT_DAY = "com.moutamid.unnepek.CURRENT_DAY";
    public static final String ACTION_PREV = "com.moutamid.unnepek.PREV_MONTH";
    public static final String ACTION_NEXT = "com.moutamid.unnepek.NEXT_MONTH";
    public static final String ACTION_CELL_CLICK = "com.moutamid.unnepek.CELL_CLICK";
    public static final String ACTION_TOGGLE_LAYOUT = "com.moutamid.unnepek.TOGGLE_LAYOUT";
    private static int month = Calendar.getInstance().get(Calendar.MONTH);
    private static int year = Calendar.getInstance().get(Calendar.YEAR);
    private static boolean isLayoutVisible = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction() == null) return;
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        ComponentName widget = new ComponentName(context, WidgetProvider.class);
        switch (intent.getAction()) {
            case ACTION_PREV:
                month--;
                if (month < 0) {
                    month = 11;
                    year--;
                }
                break;
            case ACTION_NEXT:
                month++;
                if (month > 11) {
                    month = 0;
                    year++;
                }
                break;
            case ACTION_CURRENT_DAY:
                Calendar calendar = Calendar.getInstance();
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);

                // Force refresh widget list
                AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(
                        AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, WidgetProvider.class)),
                        R.id.calendarGrid
                );
                break;

            case ACTION_TOGGLE_LAYOUT:
                isLayoutVisible = !isLayoutVisible;
                RemoteViews updatedViews = new RemoteViews(context.getPackageName(), R.layout.activity_monthly_view);
                updatedViews.setViewVisibility(R.id.monthYearText, isLayoutVisible ? View.VISIBLE : View.GONE);
                manager.updateAppWidget(widget, updatedViews);
                break;
            case ACTION_CELL_CLICK:
                Intent openDetail = new Intent(context, MonthlyViewActivity.class);
                context.startActivity(openDetail);
                break;
        }
        int[] ids = manager.getAppWidgetIds(widget);
        for (int id : ids) {
            updateAppWidget(context, manager, id);
        }
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        for (int id : ids) {
            updateAppWidget(context, manager, id);
        }
    }
    static void updateAppWidget(Context context, AppWidgetManager manager, int appWidgetId) {
        views = new RemoteViews(context.getPackageName(), R.layout.activity_monthly_view);
        views.setInt(R.id.widgetRoot, "setBackgroundColor", ColorPreference.getAppColor(context)); // Example: Red color
        DateFormatSymbols dfs = new DateFormatSymbols(new Locale("hu", "HU"));
        String monthName = dfs.getMonths()[month];
        views.setTextViewText(R.id.monthYearText, year+" "+ monthName);
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("saved_month", month);
        editor.putInt("saved_year", year);
        editor.apply();
        Intent prevIntent = new Intent(context, WidgetProvider.class);
        prevIntent.setAction(ACTION_PREV);
        prevIntent.setData(Uri.parse("widget://prev" + appWidgetId));
        PendingIntent prevPending = PendingIntent.getBroadcast(context, appWidgetId, prevIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.prevMonthBtn, prevPending);

        Intent prevIntent1 = new Intent(context, WidgetProvider.class);
        prevIntent1.setAction(ACTION_CURRENT_DAY);
        prevIntent1.setData(Uri.parse("widget://current" + appWidgetId));
        PendingIntent prevIntent2 = PendingIntent.getBroadcast(context, appWidgetId, prevIntent1,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.currentDay, prevIntent2);

        Intent nextIntent = new Intent(context, WidgetProvider.class);
        nextIntent.setAction(ACTION_NEXT);
        nextIntent.setData(Uri.parse("widget://next" + appWidgetId));
        PendingIntent nextPending = PendingIntent.getBroadcast(context, appWidgetId + 1, nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.nextMonthBtn, nextPending);
        Intent openIntent = new Intent(context, MainActivity.class);
        openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent openPending = PendingIntent.getActivity(context, appWidgetId, openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.claneder, openPending);
        Intent currentDayIntent = new Intent(context, WidgetProvider.class);
        currentDayIntent.setAction(ACTION_CURRENT_DAY);
        currentDayIntent.setData(Uri.parse("widget://currentday" + appWidgetId));
        PendingIntent currentDayPending = PendingIntent.getBroadcast(context, appWidgetId + 2, currentDayIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.currentDayBtn, currentDayPending);
        Intent serviceIntent = new Intent(context, WidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.putExtra("month", month);
        serviceIntent.putExtra("year", year);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.calendarGrid, serviceIntent);
        Intent clickIntentTemplate = new Intent(context, MonthlyViewActivity.class);
        clickIntentTemplate.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clickIntentTemplate,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setPendingIntentTemplate(R.id.calendarGrid, pendingIntent);
        manager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.calendarGrid);
        manager.updateAppWidget(appWidgetId, views);
    }
    static void updateAppWidgetWithDate(Context context, AppWidgetManager manager, int appWidgetId,
                                        int year, int month, int day, int hour, int minute) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_monthly_view);
        String dateTime = String.format("%04d/%02d", year, month + 1);
        views.setTextViewText(R.id.monthYearText, dateTime); // Ensure this TextView exists
        updateAppWidget(context, manager, appWidgetId);
    }

}
