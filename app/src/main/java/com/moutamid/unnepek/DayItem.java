package com.moutamid.unnepek;
public class DayItem {
    public int day;
    public boolean isWeekend;
    public boolean isFeastDay;
    public String feastLabel;
    public String feastStory;

        public int displayDay; // For prev/next month day numbers

    public DayItem(int day, boolean isWeekend, boolean isFeastDay, String feastLabel) {
        this.day = day;
        this.isWeekend = isWeekend;
        this.isFeastDay = isFeastDay;
        this.feastLabel = feastLabel;
    }
    public DayItem(int day, String feastLabel, String feastStory, boolean isWeekend, boolean isFeastDay, int displayDay) {
        this.day = day;
        this.feastLabel = feastLabel;
        this.feastStory = feastStory;
        this.isWeekend = isWeekend;
        this.isFeastDay = isFeastDay;
        this.displayDay = displayDay;
    }
}
