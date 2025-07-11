package com.moutamid.unnepek;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "calendar_events.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE events (id INTEGER PRIMARY KEY AUTOINCREMENT, year INTEGER, month INTEGER, day INTEGER, name TEXT, story TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS events");
        onCreate(db);
    }

    public void addEvent(int year, int month, int day, String name, String story) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("year", year);
        values.put("month", month);
        values.put("day", day);
        values.put("name", name);
        values.put("story", story);
        db.insert("events", null, values);
        db.close();
    }

    public List<FeastDay> getAllEvents() {
        List<FeastDay> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM events", null);

        if (cursor.moveToFirst()) {
            do {
                int year = cursor.getInt(1);
                int month = cursor.getInt(2);
                int day = cursor.getInt(3);
                String name = cursor.getString(4);
                String story = cursor.getString(5);
                events.add(new FeastDay(year, month, day, name, story));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return events;
    }
}
