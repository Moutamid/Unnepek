package com.moutamid.unnepek;
public class DayItem {
    public int day;
    public boolean isWeekend;
    public boolean isFeastDay;
    public String feastLabel;
    public String feastStory;

    public DayItem(int day, boolean isWeekend, boolean isFeastDay, String feastLabel) {
        this.day = day;
        this.isWeekend = isWeekend;
        this.isFeastDay = isFeastDay;
        this.feastLabel = feastLabel;
    }
}
