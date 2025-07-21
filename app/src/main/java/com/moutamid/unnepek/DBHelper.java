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

    // Insert event
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

    // Fetch all events (with ID)
    public List<FeastDay> getAllEvents() {
        List<FeastDay> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM events", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
                int month = cursor.getInt(cursor.getColumnIndexOrThrow("month"));
                int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String story = cursor.getString(cursor.getColumnIndexOrThrow("story"));

                FeastDay fd = new FeastDay(id, year, month, day, name, story);
                fd.isFromDB = true; // mark as DB event
                events.add(fd);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return events;
    }

    // Delete event by ID
    public void deleteEventById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("events", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteEventByDate(int year, int month, int day) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("events", "year = ? AND month = ? AND day = ?",
                new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(day)});
        db.close();
    }

    public void deleteAllEvents() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("events", null, null);
        db.close();
    }
}
