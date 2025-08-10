package com.moutamid.unnepek.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.moutamid.unnepek.R;
import com.moutamid.unnepek.adapter.ColorAdapter;
import com.moutamid.unnepek.model.FeastDayModel;
import com.moutamid.unnepek.receiver.UpdateReceiver;
import com.moutamid.unnepek.utils.ColorPreference;
import com.moutamid.unnepek.utils.Constants;
import com.moutamid.unnepek.utils.DBHelper;
import com.moutamid.unnepek.utils.FeastBrain;
import com.moutamid.unnepek.widget.WidgetProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.function.Consumer;

public class MonthlyViewActivity extends AppCompatActivity {
    Integer appColor;
    Integer feastColor;
    Integer reminderColor;
    Integer noteColor;
    int month;
    int currentYear;
    TextView headerText;
    GridLayout dayGrid;
    List<FeastDayModel> feastDays = new ArrayList<>();
    DBHelper dbHelper;
    View rootLayout;
    ImageView currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_monthly_view_main);
        Log.d("Constants.TAG", "onCreate: started");
        dbHelper = new DBHelper(this);
        ImageView showPopupBtn = findViewById(R.id.menu);
        rootLayout = findViewById(R.id.main_layout);
        boolean isDimmed = ColorPreference.isDimmed(this);
        applyDimEffect(isDimmed);
        applyColors();
        showPopupBtn.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(MonthlyViewActivity.this, v);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.change_app_color) {
                    showColorPickerDialog("Válassz alkalmazás színt", color -> {
                        appColor = color;
                        ColorPreference.saveAppColor(this, appColor);
                        rootLayout.setBackgroundColor(appColor);
                        refreshWidget();
                    });
                    return true;
                } else if (itemId == R.id.change_feast_color) {
                    showFeastColorPickerDialog("Válassz ünnepnap színt", color -> {
                        feastColor = color;
                        ColorPreference.saveFeastColor(this, feastColor);
                        refreshWidget();
                    });
                    return true;
                } else if (itemId == R.id.change_reminder_color) {
                    showReminderColorPickerDialog("Válassz emlékeztető színt", color -> {
                        reminderColor = color;
                        ColorPreference.saveReminderColor(this, reminderColor);
                        refreshWidget();
                    });
                    return true;
                } else if (itemId == R.id.change_note_color) {
                    showNoteColorPickerDialog("Válassz jegyzet színt", color -> {
                        noteColor = color;
                        ColorPreference.saveNoteColor(this, noteColor);
                        refreshWidget();
                    });
                    return true;
                } else if (itemId == R.id.toggle_dim) {
                    boolean currentState = ColorPreference.isDimmed(this);
                    boolean newState = !currentState;
                    ColorPreference.setDimState(this, newState);
                    applyDimEffect(newState);
                    refreshWidget();
                    return true;
                } else if (itemId == R.id.toggle_week_numbers) {
                    toggleWeekNumbers();
                    refreshWidget();
                    return true;
                }
                return false;
            });

            popupMenu.show();
        });
        headerText = findViewById(R.id.monthYearText);
        dayGrid = findViewById(R.id.monthlyDayGrid);
        currentDay = findViewById(R.id.currentDayBtn);
        ImageView prevBtn = findViewById(R.id.prevMonthBtn);
        ImageView nextBtn = findViewById(R.id.nextMonthBtn);
        ImageView calendarBtn = findViewById(R.id.claneder);
        ImageView addBtn = findViewById(R.id.add_event);
        currentDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month = Calendar.getInstance().get(Calendar.MONTH);
                currentYear = Calendar.getInstance().get(Calendar.YEAR);
                populateCalendar();
            }
        });
        addBtn.setOnClickListener(v -> showAddEventDialog());
        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        month = prefs.getInt("saved_month", Calendar.getInstance().get(Calendar.MONTH));
        currentYear = prefs.getInt("saved_year", Calendar.getInstance().get(Calendar.YEAR));
        prevBtn.setOnClickListener(v -> {
            month--;
            if (month < 0) {
                month = 11;
                currentYear--;
            }
            refreshWidget();
            populateCalendar();
        });

        nextBtn.setOnClickListener(v -> {
            month++;
            if (month > 11) {
                month = 0;
                currentYear++;
            }
            refreshWidget();
            populateCalendar();
        });

        calendarBtn.setOnClickListener(v -> {
            Intent intenti = new Intent(this, MainActivity.class);
            intenti.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intenti);

        });
        feastDays = FeastBrain.feastDayModelList();

        populateCalendar();

    }

    private void populateCalendar() {
        dayGrid.removeAllViews();

        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean showWeek = prefs.getBoolean("show_week_numbers", false);

        int columns = showWeek ? 8 : 7;
        dayGrid.setColumnCount(columns);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int cellSize = screenWidth / columns;

        // --- Day headers ---
        if (showWeek) {
            dayGrid.addView(createHeader("Hét", cellSize));
        }
        String[] dayNames = {"H", "K", "Sze", "Cs", "P", "Szo", "V"}; // Monday - Sunday
        for (String dayName : dayNames) {
            TextView dayHeader = createHeader(dayName, cellSize);
            if (dayName.equals("Szo") || dayName.equals("V"))
                dayHeader.setTextColor(Color.RED);
            else
                dayHeader.setTextColor(Color.WHITE);
            dayGrid.addView(dayHeader);
        }

        // --- Month title ---
        String[] monthNames = {"JANUÁR", "FEBRUÁR", "MÁRCIUS", "ÁPRILIS", "MÁJUS", "JÚNIUS",
                "JÚLIUS", "AUGUSZTUS", "SZEPTEMBER", "OKTÓBER", "NOVEMBER", "DECEMBER"};
        headerText.setText(currentYear + " / " + monthNames[month]);

        // --- Set calendar to 1st of month ---
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.YEAR, currentYear);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = Calendar.MONDAY; // Always start week on Monday
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int offset = (dayOfWeek + 5) % 7; // Shift Sunday to end

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Create temp calendar to loop all visible cells
        Calendar drawCal = (Calendar) calendar.clone();
        drawCal.add(Calendar.DAY_OF_MONTH, -offset); // go to first visible cell

        int totalCells = ((offset + daysInMonth + 6) / 7) * 7;

        Calendar today = Calendar.getInstance();
        int todayYear = today.get(Calendar.YEAR);
        int todayMonth = today.get(Calendar.MONTH);
        int todayDay = today.get(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < totalCells; i++) {
            Log.d(Constants.TAG, "populateCalendar: totalCells: "+totalCells);
            if (showWeek && i % 7 == 0) {
                TextView weekNum = createWeekNum(drawCal.get(Calendar.WEEK_OF_YEAR), cellSize);
                dayGrid.addView(weekNum);
            }

            int day = drawCal.get(Calendar.DAY_OF_MONTH);
            int cellMonth = drawCal.get(Calendar.MONTH);
            int cellYear = drawCal.get(Calendar.YEAR);

            boolean isCurrentMonth = (cellMonth == month && cellYear == currentYear);
            boolean isWeekend = drawCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                    drawCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;

            String textColor = isCurrentMonth ? (isWeekend ? "RED" : "WHITE") : "#666666";
            LinearLayout container = createDayCell(day, textColor, cellSize);

            if (cellYear == todayYear && cellMonth == todayMonth && day == todayDay) {
                container.setBackgroundColor(getColor(R.color.app_color)); // Highlight today
            }

            // Add feast events
            for (FeastDayModel fd : feastDays) {
                if (fd.year == cellYear && fd.month == cellMonth && fd.day == day) {
                    TextView label = createEventLabel(fd.name, ColorPreference.getFeastColor(this), cellSize);
                    label.setOnClickListener(v -> showFeastDialog(fd));
                    container.addView(label);
                }
            }

            // Add reminder events
            for (FeastDayModel fd : dbHelper.getAllEvents()) {
                if (fd.year == cellYear && fd.month == cellMonth && fd.day == day) {
                    TextView label = createEventLabel(fd.name, ColorPreference.getReminderColor(this), cellSize);
                    label.setOnClickListener(v -> showFeastDialog(fd));
                    container.addView(label);
                }
            }

            dayGrid.addView(container);
            drawCal.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private TextView createHeader(String text, int size) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(12);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = size;
        params.height = size;
        tv.setLayoutParams(params);
        return tv;
    }

    private TextView createWeekNum(int weekNum, int size) {
        TextView tv = new TextView(this);
        tv.setText(String.valueOf(weekNum));
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.RED);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = size;
        params.height = size;
        tv.setLayoutParams(params);
        return tv;
    }

    private LinearLayout createDayCell(int day, String color, int size) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = size;
        params.height = (int) (size * 1.4); // 40% taller
        layout.setLayoutParams(params);

        TextView tv = new TextView(this);
        tv.setText(String.valueOf(day));
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(color.equals("RED") ? Color.RED : color.equals("WHITE") ? Color.WHITE : Color.parseColor(color));
        layout.addView(tv);
        return layout;
    }

    private TextView createEventLabel(String text, int bgColor, int size) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(7);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(2, 1, 1, 2);
        tv.setBackgroundColor(bgColor);
        tv.setTextColor(Color.BLACK);
        tv.setMaxLines(1);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams
                (
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
        int margin = 2;
        params.setMargins(margin, margin, margin, margin);
        tv.setLayoutParams(params);
        return tv;
    }

    private void showFeastDialog(FeastDayModel fd) {
        View eventView = getLayoutInflater().inflate(R.layout.item_event, null);

        TextView nameView = eventView.findViewById(R.id.eventName);
        TextView dateView = eventView.findViewById(R.id.eventDate);
        TextView storyView = eventView.findViewById(R.id.eventStory);
        ImageView btnClose = eventView.findViewById(R.id.btnClose);
        ImageView btnDelete = eventView.findViewById(R.id.btnDelete);

        nameView.setText(fd.name);
        dateView.setText(String.format("%04d/%02d/%02d", fd.year, fd.month + 1, fd.day));
        storyView.setText(fd.story);

        eventView.setBackgroundColor(ColorPreference.getNoteColor(this));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(eventView)
                .create();
        btnDelete.setVisibility(!fd.isFromDB ? View.GONE : View.VISIBLE);

        btnClose.setOnClickListener(v -> {
            dialog.dismiss();
            populateCalendar(); // refresh
        });

        btnDelete.setOnClickListener(v -> {
            dialog.dismiss();
            showDeleteConfirmation(fd);
        });

        dialog.show();
    }
    private void applyDimEffect(boolean dim) {
        if (dim) {
            rootLayout.setForeground(new ColorDrawable(Color.parseColor("#88000000"))); // 53% black overlay
        } else {
            rootLayout.setForeground(null);
        }
    }

    private void showAddEventDialog() {
        if (isFinishing()) return; // prevent crash

        View view = getLayoutInflater().inflate(R.layout.dialog_add_event, null);
        EditText nameInput = view.findViewById(R.id.eventNameInput);
        EditText storyInput = view.findViewById(R.id.eventStoryInput);
        TextView datePickerText = view.findViewById(R.id.datePickerText);
        Switch addForAllYearsSwitch = view.findViewById(R.id.addForAllYearsSwitch);

        final int[] selectedYear = {currentYear};
        final int[] selectedMonth = {month};
        final int[] selectedDay = {1};

        datePickerText.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            DatePickerDialog dp = new DatePickerDialog(MonthlyViewActivity.this,
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        selectedYear[0] = year;
                        selectedMonth[0] = monthOfYear;
                        selectedDay[0] = dayOfMonth;

                        String[] monthsHu = {"január", "február", "március", "április", "május", "június",
                                "július", "augusztus", "szeptember", "október", "november", "december"};
                        String formattedDate = dayOfMonth + ". " + monthsHu[monthOfYear] + " " + year;
                        datePickerText.setText(formattedDate);
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH));

            if (!isFinishing()) {
                dp.show();
            }
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            ComponentName widget = new ComponentName(this, WidgetProvider.class);
            int[] ids = manager.getAppWidgetIds(widget);
            manager.notifyAppWidgetViewDataChanged(ids, R.id.calendarGrid);

        });

        AlertDialog dialog = new AlertDialog.Builder(MonthlyViewActivity.this)
                .setTitle("Új esemény hozzáadása")
                .setView(view)
                .setPositiveButton("Mentés", null) // We will override this later
                .setNegativeButton("Mégse", null)
                .create();

        dialog.setOnShowListener(dlg -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String name = nameInput.getText().toString().trim();
                String story = storyInput.getText().toString().trim();

                // Validate fields
                if (name.isEmpty()) {
                    nameInput.setError("Kérlek, add meg az esemény nevét!");
                    return;
                }
                if (story.isEmpty()) {
                    storyInput.setError("Kérlek, add meg a történetet!");
                    return;
                }

                // Save event
                if (addForAllYearsSwitch.isChecked()) {
                    for (int year = 2000; year <= 2100; year++) {
                        dbHelper.addEvent(year, selectedMonth[0], selectedDay[0], name, story);
                    }
                    Toast.makeText(this, "Esemény mentve minden évre!", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.addEvent(selectedYear[0], selectedMonth[0], selectedDay[0], name, story);
                    Toast.makeText(this, "Esemény mentve!", Toast.LENGTH_SHORT).show();
                }

                populateCalendar();
                refreshWidget();

                dialog.dismiss(); // Close dialog after saving
            });
        });

        dialog.show();
    }




    private void showDeleteConfirmation(FeastDayModel fd) {
        new AlertDialog.Builder(this)
                .setTitle("Esemény törlése")
                .setMessage("Biztosan törölni szeretnéd ezt az eseményt?\n\n" +
                        "Töröljem az összes évben?")
                .setPositiveButton("Csak ezt", (dialog, which) -> {
                    dbHelper.deleteEventById(fd.id);
//                    dbHelper.deleteEventByDate(fd.year, fd.month, fd.day);
                    Toast.makeText(this, "Esemény törölve!", Toast.LENGTH_SHORT).show();
                    populateCalendar();
                })
                .setNeutralButton("Minden év", (dialog, which) -> {
                    dbHelper.deleteEventByMonthDay(fd.month, fd.day);
                    Toast.makeText(this, "Esemény minden évből törölve!", Toast.LENGTH_SHORT).show();
                    populateCalendar();
                    refreshWidget();

                })
                .setNegativeButton("Mégse", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            finish();
        } else {
            super.onBackPressed();
            finish();
        }
    }

    private void refreshWidget() {
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        ComponentName widget = new ComponentName(this, WidgetProvider.class);
        int[] ids = manager.getAppWidgetIds(widget);

        // Get current date and time
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        for (int id : ids) {
            WidgetProvider.updateAppWidgetWithDate(this, manager, id, year, month, day
//                    , hour, minute
            );
        }

        Intent intent = new Intent(this, UpdateReceiver.class);
        sendBroadcast(intent);
    }

    private void showColorPickerDialog(String title, Consumer<Integer> colorSelected) {
        final int[] colors = {
                Color.parseColor("#171B24"), // Fekete
                Color.parseColor("#031D57"), // Sötétkék
                Color.parseColor("#4B0857"), // Lila
                Color.parseColor("#57082D"), // Borvörös
                Color.parseColor("#085721"), // Zöld
                Color.parseColor("#505708"), // Olíva
                Color.parseColor("#331F1E"), // Barna
                Color.parseColor("#98092C"), // Red
                Color.parseColor("#36923A"), // Green
        };

        final String[] colorNames = {
                "Fekete", "Sötétkék", "Lila", "Borvörös", "Zöld", "Olíva", "Barna",
                "Red", "Green"
        };

        ColorAdapter adapter = new ColorAdapter(this, colors, colorNames);

        new MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setAdapter(adapter, (dialog, which) -> colorSelected.accept(colors[which]))
                .show();
    }

    private void showFeastColorPickerDialog(String title, Consumer<Integer> colorSelected) {
        final int[] colors = {
                Color.parseColor("#F8BCBC"), // Rózsaszín
                Color.parseColor("#F9E9AD"), // Sárga
                Color.parseColor("#C4F9AD"), // Mentazöld
                Color.parseColor("#ADF9DD"), // Aqua
                Color.parseColor("#ADC9F9"), // Világoskék
                Color.parseColor("#F1B2F8"), // Levendula
                Color.parseColor("#FF2D61"), // Piros
                Color.parseColor("#98092C"), // Red
                Color.parseColor("#36923A"), // Green
                 

        };

        final String[] colorNames = {
                "Rózsaszín", "Sárga", "Mentazöld", "Aqua", "Világoskék", "Levendula", "Piros",
                "Red", "Green"
        };

        ColorAdapter adapter = new ColorAdapter(this, colors, colorNames);

        new MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setAdapter(adapter, (dialog, which) -> colorSelected.accept(colors[which]))
                .show();
    }

    private void showReminderColorPickerDialog(String title, Consumer<Integer> colorSelected) {
        showFeastColorPickerDialog(title, colorSelected); // same color palette for now
    }

    private void showNoteColorPickerDialog(String title, Consumer<Integer> colorSelected) {
        showColorPickerDialog(title, colorSelected); // same color palette for now
    }

    private void applyColors() {

        rootLayout.setBackgroundColor(ColorPreference.getAppColor(this));
    }

    private void toggleWeekNumbers() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean showWeek = prefs.getBoolean("show_week_numbers", false);

        showWeek = !showWeek; // Toggle state
        prefs.edit().putBoolean("show_week_numbers", showWeek).apply();

        if (showWeek) {
            refreshWidget();
            populateCalendar();
            Toast.makeText(this, "Heti számok megjelenítve", Toast.LENGTH_SHORT).show();
        } else {
            refreshWidget();
            populateCalendar();

            Toast.makeText(this, "Heti számok elrejtve", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ActivityLifecycle", "onDestroy called");
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("ActivityLifecycle", "onPause called");
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        month = prefs.getInt("saved_month", Calendar.getInstance().get(Calendar.MONTH));
        currentYear = prefs.getInt("saved_year", Calendar.getInstance().get(Calendar.YEAR));
        Log.d("ActivityLifecycle", "onResume called   " + month);
    }
}
