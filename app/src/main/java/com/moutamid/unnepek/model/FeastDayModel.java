package com.moutamid.unnepek.model;
public class FeastDayModel {
    public int id;
    public int year;
    public int month;
    public int day;
    public String name;
    public String story;
    public boolean isWeekend;
    public boolean isFeastDay;
    public String feastLabel;
    public String feastStory;
    public boolean isFromDB;
    public int displayDay;

    public FeastDayModel(int day, boolean isWeekend, boolean isFeastDay, String feastLabel) {
        this.day = day;
        this.isWeekend = isWeekend;
        this.isFeastDay = isFeastDay;
        this.feastLabel = feastLabel;
        this.isFromDB = false;
    }

    public FeastDayModel(int year, int month, int day, String name, String story) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.name = name;
        this.story = story;
        this.isFromDB = false;
    }

    public FeastDayModel(int id, int year, int month, int day, String name, String story) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.name = name;
        this.story = story;
        this.isFromDB = true;
    }
}
