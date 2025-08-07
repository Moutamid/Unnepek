package com.moutamid.unnepek;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context context;
    private final List<FeastDay> items = new ArrayList<>();
    private int currentMonth;
    private int currentYear;
    private DBHelper dbHelper;

    public GridRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        dbHelper = new DBHelper(context);
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        currentMonth = prefs.getInt("saved_month", Calendar.getInstance().get(Calendar.MONTH));
        currentYear = prefs.getInt("saved_year", Calendar.getInstance().get(Calendar.YEAR));
    }


    @Override
    public void onCreate() {
        populateCalendar();
    }

    private void populateCalendar() {
        items.clear();

        // Add weekday headers (Hungarian)
        String[] dayNames = {"H", "K", "Sze", "Cs", "P", "Szo", "V"};
        for (String name : dayNames) {
            FeastDay header = new FeastDay(-1, false, false, name);
            items.add(header);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, currentYear);
        calendar.set(Calendar.MONTH, currentMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7; // Monday=0, Sunday=6
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Load feast days
        List<FeastDay> feastDays = new ArrayList<>();

        feastDays.add(new FeastDay(2025, 0, 6, "Vízkereszt", " A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a\n" +
                " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus\n" +
                " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus\n" +
                " megkeresztelkedésére és az első csodájára. A három királyok aranyat,\n" +
                " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a\n" +
                " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet\n" +
                " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi\n" +
                " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak\n" +
                " házszenteléseket, Isten áldását kérve az otthonra és az új évre."));
        feastDays.add(new FeastDay(2025, 1, 14, "Valentin nap", " \n" +
                " A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint\n" +
                " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században\n" +
                " titokban esketett meg szerelmespárokat, mert II. Claudius császár\n" +
                " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák.\n" +
                " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az\n" +
                " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a\n" +
                " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal\n" +
                " és üdvözlőkártyákkal ünneplik."));

        feastDays.addAll(dbHelper.getAllEvents());

        Calendar prevMonth = (Calendar) calendar.clone();
        prevMonth.add(Calendar.MONTH, -1);
        int daysInPrevMonth = prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH);

// ===== PREVIOUS MONTH DAYS =====
        for (int i = 0; i < firstDayOfWeek; i++) {
            int prevDay = (daysInPrevMonth - firstDayOfWeek + 1) + i;
            FeastDay prevItem = new FeastDay(-2, false, false, null);
            prevItem.displayDay = prevDay;
            items.add(prevItem);
        }

// ===== CURRENT MONTH DAYS =====
        for (int day = 1; day <= daysInMonth; day++) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
            int dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7;
            boolean isWeekend = (dayOfWeek == 5 || dayOfWeek == 6);

            StringBuilder feastLabelBuilder = new StringBuilder();
            StringBuilder feastStoryBuilder = new StringBuilder();
            boolean isFeastDay = false;
            boolean isFromDB = false;

            for (FeastDay fd : feastDays) {
                if (fd.year == currentYear && fd.month == currentMonth && fd.day == day) {
                    String trimmedName = fd.name.length() > 4 ? fd.name.substring(0, 4) : fd.name;

                    if (feastLabelBuilder.length() > 0) feastLabelBuilder.append("\n");
                    feastLabelBuilder.append(trimmedName);

                    isFeastDay = true;
                    if (fd.isFromDB) isFromDB = true;
                }
            }

            String feastLabel = feastLabelBuilder.toString();
            String feastStory = feastStoryBuilder.toString();
            FeastDay item = new FeastDay(day, isWeekend, isFeastDay, feastLabel);
            item.feastStory = feastStory;
            item.isFromDB = isFromDB;   // <--- Save DB flag
            item.displayDay = day;
            items.add(item);
        }

// ===== NEXT MONTH DAYS =====
        int totalCells = items.size() % 7;
        if (totalCells != 0) {
            int extraDays = 7 - totalCells;
            for (int i = 1; i <= extraDays; i++) {
                FeastDay nextItem = new FeastDay(-2, false, false, null);
                nextItem.displayDay = i;
                items.add(nextItem);
            }
        }

    }

    @Override
    public void onDataSetChanged() {
        populateCalendar();
    }

    @Override
    public void onDestroy() {
        items.clear();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        FeastDay item = items.get(position);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.calendar_cell);

        int feastColor = ColorPreference.getFeastColor(context);
        int reminderColor = ColorPreference.getReminderColor(context);

        Calendar today = Calendar.getInstance();
        int todayYear = today.get(Calendar.YEAR);
        int todayMonth = today.get(Calendar.MONTH);
        int todayDay = today.get(Calendar.DAY_OF_MONTH);
        boolean isToday = (currentYear == todayYear && currentMonth == todayMonth && item.day == todayDay);

        rv.setViewVisibility(R.id.feastLabel, View.INVISIBLE);

        if (item.day == -1 && item.feastLabel != null) {
            rv.setTextViewText(R.id.dayText, item.feastLabel);
            rv.setTextColor(R.id.dayText,
                    (item.feastLabel.equals("Szo") || item.feastLabel.equals("V")) ? Color.RED : Color.WHITE);
            rv.setTextViewTextSize(R.id.dayText, TypedValue.COMPLEX_UNIT_SP, 13);

            // --- Previous/Next Month Cells ---
        } else if (item.day == -2) {
            rv.setTextViewText(R.id.dayText, String.valueOf(item.displayDay));
            rv.setTextColor(R.id.dayText, Color.parseColor("#666666"));
            rv.setInt(R.id.calendar_cell_root, "setBackgroundColor", Color.TRANSPARENT);

            // --- Normal Day Cell ---
        } else {
            rv.setTextViewText(R.id.dayText, String.valueOf(item.day));

            // Highlight current day
            if (isToday) {
                rv.setTextColor(R.id.dayText, Color.YELLOW);
                rv.setInt(R.id.calendar_cell_root, "setBackgroundColor", Color.parseColor(context.getString(R.color.app_color))); // Blue highlight
            } else {
                rv.setTextColor(R.id.dayText, item.isWeekend ? Color.RED : Color.WHITE);
                rv.setInt(R.id.calendar_cell_root, "setBackgroundColor", Color.TRANSPARENT);
            }

            if (item.isFeastDay) {
                rv.setViewVisibility(R.id.feastLabel, View.VISIBLE);
                rv.setTextViewText(R.id.feastLabel, item.feastLabel);
                int color = item.isFromDB ? reminderColor : feastColor;
                rv.setInt(R.id.feastLabel, "setBackgroundColor", color);
            }
        }

        // --- Click intent ---
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("clicked_day", (item.day == -2) ? item.displayDay : item.day);
        fillInIntent.putExtra("clicked_feast", item.feastLabel);
        fillInIntent.putExtra("feast_story", item.feastStory);
        fillInIntent.putExtra("month", currentMonth);
        fillInIntent.putExtra("currentYear", currentYear);
        rv.setOnClickFillInIntent(R.id.calendar_cell_root, fillInIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.calendar_cell);
        rv.setTextViewText(R.id.dayText, "");
        rv.setViewVisibility(R.id.feastLabel, View.INVISIBLE);
        return rv;
    }


    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    private boolean isWeekendDay(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        int weekDay = cal.get(Calendar.DAY_OF_WEEK);
        return (weekDay == Calendar.SATURDAY || weekDay == Calendar.SUNDAY);
    }
}
