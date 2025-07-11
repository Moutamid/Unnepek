package com.moutamid.unnepek;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView yearText;
    private GridView monthGrid;
    private int currentYear = 2025;

    private String[] monthNames = {
            "JANUÁR", "FEBRUÁR", "MÁRCIUS", "ÁPRILIS", "MÁJUS", "JÚNIUS",
            "JÚLIUS", "AUGUSZTUS", "SZEPTEMBER", "OKTÓBER", "NOVEMBER", "DECEMBER"
    };

    private BaseAdapter monthAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        yearText = findViewById(R.id.yearText);
        monthGrid = findViewById(R.id.monthGrid);
        ImageView prev = findViewById(R.id.prevYearBtn);
        ImageView next = findViewById(R.id.nextYearBtn);
        ImageView monthView = findViewById(R.id.monthView);
        monthView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentYear = calendar.get(Calendar.YEAR);
                Intent i = new Intent(MainActivity.this, MonthlyViewActivity.class);
                i.putExtra("month", currentMonth);
                i.putExtra("currentYear", currentYear);
                startActivity(i);
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
            if (currentYear > 2025) {
                currentYear--;
                updateUI();
            }
        });

        next.setOnClickListener(v -> {
            if (currentYear < 2035) {
                currentYear++;
                updateUI();
            }
        });


        monthGrid.setOnItemClickListener((parent, view, position, id) ->

        {
            Intent i = new Intent(MainActivity.this, MonthlyViewActivity.class);
            i.putExtra("month", position);
            i.putExtra("currentYear", currentYear);
            startActivity(i);
        });

        updateUI();
    }

    private void updateUI() {
        yearText.setText(String.valueOf(currentYear));
        monthAdapter.notifyDataSetChanged();
    }
}
