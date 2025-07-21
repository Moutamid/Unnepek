package com.moutamid.unnepek;
public class FeastDay {
    public int id;
    public int year;
    public int month;
    public int day;
    public String name;
    public String story;
    public boolean isFromDB;

    public FeastDay(int year, int month, int day, String name, String story) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.name = name;
        this.story = story;
        this.isFromDB = false;
    }

    public FeastDay(int id, int year, int month, int day, String name, String story) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.name = name;
        this.story = story;
        this.isFromDB = true;
    }
}
