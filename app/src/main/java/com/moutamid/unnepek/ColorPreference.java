package com.moutamid.unnepek;

import android.content.Context;
import android.content.SharedPreferences;

public class ColorPreference {
    private static final String PREF_NAME = "app_colors";
    private static final String KEY_APP_COLOR = "app_color";
    private static final String KEY_FEAST_COLOR = "feast_color";
    private static final String KEY_REMINDER_COLOR = "reminder_color";
    private static final String KEY_NOTE_COLOR = "note_color";
    private static final String KEY_DIM = "dim_background"; // NEW


    public static void saveAppColor(Context context, int color) {
        saveColor(context, KEY_APP_COLOR, color);
    }

    public static void saveFeastColor(Context context, int color) {
        saveColor(context, KEY_FEAST_COLOR, color);
    }

    public static void saveReminderColor(Context context, int color) {
        saveColor(context, KEY_REMINDER_COLOR, color);
    }

    public static void saveNoteColor(Context context, int color) {
        saveColor(context, KEY_NOTE_COLOR, color);
    }

    public static int getAppColor(Context context) {
        return getColor(context, KEY_APP_COLOR, 0xFF171B24);
    }

    public static int getFeastColor(Context context) {
        return getColor(context, KEY_FEAST_COLOR, 0xff42919E);
    }

    public static int getReminderColor(Context context) {
        return getColor(context, KEY_REMINDER_COLOR, 0xff42919E);
    }

    public static int getNoteColor(Context context) {
        return getColor(context, KEY_NOTE_COLOR, 0xFF313743);
    }

    private static void saveColor(Context context, String key, int color) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(key, color).apply();
    }

    private static int getColor(Context context, String key, int defaultColor) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(key, defaultColor);
    }
    public static void setDimState(Context context, boolean isDimmed) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_DIM, isDimmed).apply();
    }

    public static boolean isDimmed(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_DIM, false);
    }
}
