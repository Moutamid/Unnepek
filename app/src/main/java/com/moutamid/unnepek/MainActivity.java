package com.moutamid.unnepek;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.function.Consumer;



public class MainActivity extends AppCompatActivity {
    Integer appColor;
    Integer feastColor;
    Integer reminderColor;
    Integer noteColor;
    private TextView yearText;
    private GridView monthGrid;
    private int currentYear = 2025;

    private String[] monthNames = {
            "JANUÁR", "FEBRUÁR", "MÁRCIUS", "ÁPRILIS", "MÁJUS", "JÚNIUS",
            "JÚLIUS", "AUGUSZTUS", "SZEPTEMBER", "OKTÓBER", "NOVEMBER", "DECEMBER"
    };

    private BaseAdapter monthAdapter;
    View rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        checkApp(MainActivity.this);
        yearText = findViewById(R.id.yearText);
        monthGrid = findViewById(R.id.monthGrid);
        ImageView prev = findViewById(R.id.prevYearBtn);
        ImageView next = findViewById(R.id.nextYearBtn);
        ImageView monthView = findViewById(R.id.monthView);
        ImageView showPopupBtn = findViewById(R.id.menu);
        rootLayout = findViewById(R.id.yearLayout);
        boolean isDimmed = ColorPreference.isDimmed(this);
        applyDimEffect(isDimmed);
        applyColors();
        showPopupBtn.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
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
        monthView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MonthlyViewActivity.class);
                intent.putExtra("month", 0);
                intent.putExtra("currentYear", currentYear);
                startActivity(intent);

            }
        });
        monthAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return 12;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.month_item, parent, false);
                TextView monthName = view.findViewById(R.id.monthName);
                GridLayout dayGrid = view.findViewById(R.id.dayGrid);
                dayGrid.removeAllViews();

                monthName.setText(monthNames[position]);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, currentYear);
                calendar.set(Calendar.MONTH, position);
                calendar.set(Calendar.DAY_OF_MONTH, 1);

                int firstDayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7;
                int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                for (int i = 0; i < firstDayOfWeek; i++) {
                    TextView empty = new TextView(MainActivity.this);
                    empty.setWidth(17);
                    empty.setHeight(17);
                    dayGrid.addView(empty);
                }

                for (int day = 1; day <= daysInMonth; day++) {
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    int dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7;

                    TextView dayView = new TextView(MainActivity.this);
                    dayView.setText(String.valueOf(day));
                    dayView.setTextSize(6);
                    dayView.setWidth(17);
                    dayView.setHeight(17);
                    dayView.setGravity(Gravity.CENTER);

                    if (dayOfWeek == 5 || dayOfWeek == 6) {
                        dayView.setTextColor(Color.RED);
                    } else {
                        dayView.setTextColor(Color.WHITE);
                    }

                    dayGrid.addView(dayView);
                }

                return view;
            }
        };
        monthGrid.setAdapter(monthAdapter);

        prev.setOnClickListener(v -> {
                refreshWidget();
                currentYear--;
                updateUI();
        });

        next.setOnClickListener(v -> {
                refreshWidget();
                currentYear++;
                updateUI();
        });


        monthGrid.setOnItemClickListener((parent, view, position, id) ->

        {
            Intent intent = new Intent(MainActivity.this, MonthlyViewActivity.class);
            intent.putExtra("month", position);
            intent.putExtra("currentYear", currentYear);
            startActivity(intent);

        });

        updateUI();
    }

    private void updateUI() {
        yearText.setText(String.valueOf(currentYear));
        monthAdapter.notifyDataSetChanged();
    }

    private void showColorPickerDialog(String title, Consumer<Integer> colorSelected) {
        final int[] colors = {
                Color.parseColor("#171B24"), // Fekete
                Color.parseColor("#031D57"), // Sötétkék
                Color.parseColor("#4B0857"), // Lila
                Color.parseColor("#57082D"), // Borvörös
                Color.parseColor("#085721"), // Zöld
                Color.parseColor("#505708"), // Olíva
                Color.parseColor("#331F1E")  // Barna
        };

        final String[] colorNames = {
                "Fekete", "Sötétkék", "Lila", "Borvörös", "Zöld", "Olíva", "Barna"
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
                Color.parseColor("#FF2D61")  // Piros
        };

        final String[] colorNames = {
                "Rózsaszín", "Sárga", "Mentazöld", "Aqua", "Világoskék", "Levendula", "Piros"
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
        showColorPickerDialog (title, colorSelected); // same color palette for now
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
            Toast.makeText(this, "Heti számok megjelenítve", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Heti számok elrejtve", Toast.LENGTH_SHORT).show();
        }

    }
    private void applyDimEffect(boolean dim) {
        if (dim) {
            rootLayout.setForeground(new ColorDrawable(Color.parseColor("#88000000"))); // 53% black overlay
        } else {
            rootLayout.setForeground(null);
        }
    }
    public static void checkApp(Activity activity) {
        String appName = "Unnepek";

        new Thread(() -> {
            URL google = null;
            try {
                google = new URL("https://raw.githubusercontent.com/Moutamid/Moutamid/main/apps.txt");
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String input = null;
            StringBuilder stringBuffer = new StringBuilder();
            while (true) {
                try {
                    if ((input = in != null ? in.readLine() : null) == null) break;
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            try {
                JSONObject myAppObject = new JSONObject(htmlData).getJSONObject(appName);

                boolean value = myAppObject.getBoolean("value");
                String msg = myAppObject.getString("msg");

                if (value) {
                    activity.runOnUiThread(() -> {
                        new AlertDialog.Builder(activity)
                                .setMessage(msg)
                                .setCancelable(false)
                                .show();
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).start();
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
            WidgetProvider.updateAppWidgetWithDate(this, manager, id, year, month, day, hour, minute);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
