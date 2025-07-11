package com.moutamid.unnepek;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MonthlyViewActivity extends AppCompatActivity {

    private int month;
    private int currentYear;

    private TextView headerText;
    private GridLayout dayGrid;
    List<FeastDay> feastDays = new ArrayList<>();
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_view);
        dbHelper = new DBHelper(this);

        headerText = findViewById(R.id.monthYearText);
        dayGrid = findViewById(R.id.monthlyDayGrid);

        ImageView prevBtn = findViewById(R.id.prevMonthBtn);
        ImageView nextBtn = findViewById(R.id.nextMonthBtn);
        ImageView calendarBtn = findViewById(R.id.claneder);

        ImageView addBtn = findViewById(R.id.add_event);
        addBtn.setOnClickListener(v -> showAddEventDialog());
        Intent intent = getIntent();
        month = intent.getIntExtra("month", 0);
        currentYear = intent.getIntExtra("currentYear", 2025);
        prevBtn.setOnClickListener(v -> {
            month--;
            if (month < 0) {
                month = 11;
                currentYear--;
            }
            populateCalendar();
        });

        nextBtn.setOnClickListener(v -> {
            month++;
            if (month > 11) {
                month = 0;
                currentYear++;
            }
            populateCalendar();
        });

        calendarBtn.setOnClickListener(v -> {
            finish();
        });
        feastDays = Arrays.asList(
                new FeastDay(2025, 0, 1, "Újév", " Az év első napja a megújulás, az újrakezdés és a remény szimbóluma.\n" +
                        " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az\n" +
                        " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy\n" +
                        " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és\n" +
                        " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a\n" +
                        " mondás. Legyen ez a nap a változás első lépése. Boldogságot,\n" +
                        " egészséget és kitartást kívánok az új év minden pillanatára!\n" +
                        " "),
                new FeastDay(2025, 0, 6, "Vízkereszt", " A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a\n" +
                        " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus\n" +
                        " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus\n" +
                        " megkeresztelkedésére és az első csodájára. A három királyok aranyat,\n" +
                        " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a\n" +
                        " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet\n" +
                        " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi\n" +
                        " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak\n" +
                        " házszenteléseket, Isten áldását kérve az otthonra és az új évre."),
                new FeastDay(2025, 1, 14, "Valentin nap", " \n" +
                        " A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint\n" +
                        " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században\n" +
                        " titokban esketett meg szerelmespárokat, mert II. Claudius császár\n" +
                        " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák.\n" +
                        " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az\n" +
                        " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a\n" +
                        " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal\n" +
                        " és üdvözlőkártyákkal ünneplik."),
                new FeastDay(2025, 2, 5, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja,\n" +
                        " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a\n" +
                        " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A\n" +
                        " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és\n" +
                        " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki\n" +
                        " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának\n" +
                        " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel,\n" +
                        " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére\n" +
                        " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A\n" +
                        " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap\n" +
                        " előtt tartanak. Általában február végére vagy március elejére esik."),
                new FeastDay(2025, 2, 8, "Nőnap", "Az első Nőnapot 1908 március 8.-án tartották. A női munkások\n" +
                        " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az\n" +
                        " ünnep célja a nők társadalmi, politikai és gazdasági jogainak\n" +
                        " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben\n" +
                        " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így\n" +
                        " 1911-ben már több európai országban is megünnepelték. Magyarországon\n" +
                        " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már\n" +
                        " a nők jogainak elismerésére és tiszteletére emelkedett."),
                new FeastDay(2025, 2, 15, "1848-as Forradalom", " Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a\n" +
                        " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti\n" +
                        " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a\n" +
                        " sajtószabadságért, a jobb életkörülményekért és a függetlenségért.\n" +
                        " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című\n" +
                        " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet\n" +
                        " függetlenségi törekvése a magyar történelem egyik legfontosabb\n" +
                        " eseményévé vált. Az események hatására megerősödött a nemzeti\n" +
                        " identitás és ma már a magyar szabadság és összetartozás napjaként\n" +
                        " ünnepeljük"),
                new FeastDay(2025, 2, 30, "Óraátállítás", "A tavaszi óraátállítás a nyári időszámítás kezdete. \n" +
                        " A tavaszi óraátállításkor az órákat hajnali 2:00-ról 3:00-ra állítjuk\n" +
                        " előre, hogy jobban kihasználjuk a nappali világosságot és csökkentsük a\n" +
                        " mesterséges világítás szükségességét. Magyarországon először\n" +
                        " 1916-ban az első világháború alatt vezették be az energiatakarékosság\n" +
                        " érdekében, de a háború után megszüntették. 1954 és 1957 között újra\n" +
                        " alkalmazták, mert az ipari munkahelyeken az esti energiafogyasztás\n" +
                        " csökkentésére volt szükség, de 1958-ban a lakossági ellenállás és az\n" +
                        " alacsony megtakarítás miatt eltörölték. 1980-ban ismét bevezették a\n" +
                        " villamosenergia-megtakarítás érdekében. Azóta évente kétszer állítjuk\n" +
                        " át az órákat és 1996 óta kormányrendelet szabályozza az\n" +
                        " óraátállítást Magyarországon")
//                new FeastDay(2025, 3, 1, "Bolondok napja"),
//                new FeastDay(2025, 3, 13, "Virágvasárnap"),
//                new FeastDay(2025, 3, 18, "Nagypéntek"),
//                new FeastDay(2025, 3, 20, "Húsvétvasárnap"),
//                new FeastDay(2025, 3, 21, "Húsvéthétfő"),
//                new FeastDay(2025, 3, 22, "A Föld napja"),
//                new FeastDay(2025, 3, 30, "Májusfa állítás"),
//                new FeastDay(2025, 4, 1, "A Munka ünnepe"),
//                new FeastDay(2025, 4, 4, "Anyák napja"),
//                new FeastDay(2025, 4, 25, "Gyermeknap"),
//                new FeastDay(2025, 5, 8, "Pünkösd"),
//                new FeastDay(2025, 5, 9, "Pünkösdhétfő"),
//                new FeastDay(2025, 5, 15, "Apák napja"),
//                new FeastDay(2025, 5, 21, "Nyári napforduló"),
//                new FeastDay(2025, 7, 20, "Szent István-nap"),
//                new FeastDay(2025, 9, 23, "1956-os Forradalom"),
//                new FeastDay(2025, 9, 26, "Óraátállítás"),
//                new FeastDay(2025, 10, 1, "Mindenszentek"),
//                new FeastDay(2025, 10, 2, "Halottak napja"),
//                new FeastDay(2025, 10, 30, "Advent első napja"),
//                new FeastDay(2025, 11, 6, "Mikulás"),
//                new FeastDay(2025, 11, 13, "Luca napja"),
//                new FeastDay(2025, 11, 21, "Téli napforduló")
        );
//List<FeastDay> feastDays2026 = Arrays.asList(
//    new FeastDay(2026, 0, 1, "Újév"),
//    new FeastDay(2026, 0, 6, "Vízkereszt"),
//    new FeastDay(2026, 1, 14, "Valentin nap"),
//    new FeastDay(2026, 1, 18, "Hamvazószerda"),
//    new FeastDay(2026, 2, 8, "Nőnap"),
//    new FeastDay(2026, 2, 15, "1848-as Forradalom"),
//    new FeastDay(2026, 2, 29, "Virágvasárnap"),
//    new FeastDay(2026, 2, 29, "Óraátállítás"),
//    new FeastDay(2026, 3, 1, "Bolondok napja"),
//    new FeastDay(2026, 3, 3, "Nagypéntek"),
//    new FeastDay(2026, 3, 5, "Húsvétvasárnap"),
//    new FeastDay(2026, 3, 6, "Húsvéthétfő"),
//    new FeastDay(2026, 3, 22, "A Föld napja"),
//    new FeastDay(2026, 3, 30, "Májusfa állítás"),
//    new FeastDay(2026, 4, 1, "A Munka ünnepe"),
//    new FeastDay(2026, 4, 3, "Anyák napja"),
//    new FeastDay(2026, 4, 24, "Pünkösd"),
//    new FeastDay(2026, 4, 25, "Pünkösdhétfő"),
//    new FeastDay(2026, 4, 31, "Gyermeknap"),
//    new FeastDay(2026, 5, 21, "Apák napja"),
//    new FeastDay(2026, 5, 21, "Nyári napforduló"),
//    new FeastDay(2026, 7, 20, "Szent István-nap"),
//    new FeastDay(2026, 9, 23, "1956-os Forradalom"),
//    new FeastDay(2026, 9, 25, "Óraátállítás"),
//    new FeastDay(2026, 10, 1, "Mindenszentek"),
//    new FeastDay(2026, 10, 2, "Halottak napja"),
//    new FeastDay(2026, 10, 29, "Advent első napja"),
//    new FeastDay(2026, 11, 6, "Mikulás"),
//    new FeastDay(2026, 11, 13, "Luca napja"),
//    new FeastDay(2026, 11, 21, "Téli napforduló"),
//    new FeastDay(2026, 11, 24, "Szenteste"),
//    new FeastDay(2026, 11, 25, "Karácsony"),
//    new FeastDay(2026, 11, 26, "Karácsony másnapja"),
//    new FeastDay(2026, 11, 31, "Szilveszter")
//);
//        List<FeastDay> feastDays2027 = Arrays.asList(
//    new FeastDay(2027, 0, 1, "Újév"),
//    new FeastDay(2027, 0, 6, "Vízkereszt"),
//    new FeastDay(2027, 1, 10, "Hamvazószerda"),
//    new FeastDay(2027, 1, 14, "Valentin nap"),
//    new FeastDay(2027, 2, 8, "Nőnap"),
//    new FeastDay(2027, 2, 15, "1848-as Forradalom"),
//    new FeastDay(2027, 2, 21, "Virágvasárnap"),
//    new FeastDay(2027, 2, 26, "Nagypéntek"),
//    new FeastDay(2027, 2, 28, "Húsvétvasárnap"),
//    new FeastDay(2027, 2, 28, "Óraátállítás"),
//    new FeastDay(2027, 2, 29, "Húsvéthétfő"),
//    new FeastDay(2027, 3, 1, "Bolondok napja"),
//    new FeastDay(2027, 3, 22, "A Föld napja"),
//    new FeastDay(2027, 3, 30, "Májusfa állítás"),
//    new FeastDay(2027, 4, 1, "A Munka ünnepe"),
//    new FeastDay(2027, 4, 2, "Anyák napja"),
//    new FeastDay(2027, 4, 16, "Pünkösd"),
//    new FeastDay(2027, 4, 17, "Pünkösdhétfő"),
//    new FeastDay(2027, 4, 30, "Gyermeknap"),
//    new FeastDay(2027, 5, 20, "Apák napja"),
//    new FeastDay(2027, 5, 21, "Nyári napforduló"),
//    new FeastDay(2027, 7, 20, "Szent István-nap"),
//    new FeastDay(2027, 9, 23, "1956-os Forradalom"),
//    new FeastDay(2027, 9, 31, "Óraátállítás"),
//    new FeastDay(2027, 10, 1, "Mindenszentek"),
//    new FeastDay(2027, 10, 2, "Halottak napja"),
//    new FeastDay(2027, 10, 28, "Advent első napja"),
//    new FeastDay(2027, 11, 6, "Mikulás"),
//    new FeastDay(2027, 11, 13, "Luca napja"),
//    new FeastDay(2027, 11, 22, "Téli napforduló"),
//    new FeastDay(2027, 11, 24, "Szenteste"),
//    new FeastDay(2027, 11, 25, "Karácsony"),
//    new FeastDay(2027, 11, 26, "Karácsony másnapja"),
//    new FeastDay(2027, 11, 31, "Szilveszter")
//);
//        List<FeastDay> feastDays2028 = Arrays.asList(
//    new FeastDay(2028, 0, 1, "Újév"),
//    new FeastDay(2028, 0, 6, "Vízkereszt"),
//    new FeastDay(2028, 1, 14, "Valentin nap"),
//    new FeastDay(2028, 2, 1, "Hamvazószerda"),
//    new FeastDay(2028, 2, 8, "Nőnap"),
//    new FeastDay(2028, 2, 15, "1848-as Forradalom"),
//    new FeastDay(2028, 2, 26, "Óraátállítás"),
//    new FeastDay(2028, 3, 1, "Bolondok napja"),
//    new FeastDay(2028, 3, 9, "Virágvasárnap"),
//    new FeastDay(2028, 3, 14, "Nagypéntek"),
//    new FeastDay(2028, 3, 16, "Húsvétvasárnap"),
//    new FeastDay(2028, 3, 17, "Húsvéthétfő"),
//    new FeastDay(2028, 3, 22, "A Föld napja"),
//    new FeastDay(2028, 3, 30, "Májusfa állítás"),
//    new FeastDay(2028, 4, 1, "A Munka ünnepe"),
//    new FeastDay(2028, 4, 7, "Anyák napja"),
//    new FeastDay(2028, 4, 28, "Gyermeknap"),
//    new FeastDay(2028, 5, 4, "Pünkösd"),
//    new FeastDay(2028, 5, 5, "Pünkösdhétfő"),
//    new FeastDay(2028, 5, 18, "Apák napja"),
//    new FeastDay(2028, 5, 20, "Nyári napforduló"),
//    new FeastDay(2028, 7, 20, "Szent István-nap"),
//    new FeastDay(2028, 9, 23, "1956-os Forradalom"),
//    new FeastDay(2028, 9, 29, "Óraátállítás"),
//    new FeastDay(2028, 10, 1, "Mindenszentek"),
//    new FeastDay(2028, 10, 2, "Halottak napja"),
//    new FeastDay(2028, 11, 3, "Advent első napja"),
//    new FeastDay(2028, 11, 6, "Mikulás"),
//    new FeastDay(2028, 11, 13, "Luca napja"),
//    new FeastDay(2028, 11, 21, "Téli napforduló"),
//    new FeastDay(2028, 11, 24, "Szenteste"),
//    new FeastDay(2028, 11, 25, "Karácsony"),
//    new FeastDay(2028, 11, 26, "Karácsony másnapja"),
//    new FeastDay(2028, 11, 31, "Szilveszter")
//);
        populateCalendar();

    }

    private void populateCalendar() {
        dayGrid.removeAllViews();
        String[] dayNames = {"H", "K", "Sze", "Cs", "P", "Szo", "V"}; // Hungarian: Hétfő, Kedd, Szerda, ...
        for (String dayName : dayNames) {
            TextView dayHeader = new TextView(this);
            dayHeader.setText(dayName);
            dayHeader.setTextSize(12);
            dayHeader.setTextColor(Color.WHITE);
            dayHeader.setGravity(Gravity.CENTER);
            dayHeader.setLayoutParams(new GridLayout.LayoutParams());
            dayHeader.setWidth(70);
            dayHeader.setHeight(70);
            if (dayName.equals("Szo")||dayName.equals("V")) {
                dayHeader.setTextColor(Color.RED);
            } else {
                dayHeader.setTextColor(Color.WHITE);
            }

            dayGrid.addView(dayHeader);
        }

        String[] monthNames = {
                "JANUÁR", "FEBRUÁR", "MÁRCIUS", "ÁPRILIS", "MÁJUS", "JÚNIUS",
                "JÚLIUS", "AUGUSZTUS", "SZEPTEMBER", "OKTÓBER", "NOVEMBER", "DECEMBER"
        };

        headerText.setText(monthNames[month]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, currentYear);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7;
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Empty cells
        for (int i = 0; i < firstDayOfWeek; i++) {
            TextView empty = new TextView(this);
            empty.setWidth(70);
            empty.setHeight(70);
            dayGrid.addView(empty);
        }

        for (int day = 1; day <= daysInMonth; day++) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
            int dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7;

            LinearLayout container = new LinearLayout(this);
            container.setOrientation(LinearLayout.VERTICAL);
            container.setGravity(Gravity.CENTER);
            container.setLayoutParams(new LinearLayout.LayoutParams(100, 100));

            TextView dayView = new TextView(this);
            dayView.setText(String.valueOf(day));
            dayView.setTextSize(14);
            dayView.setGravity(Gravity.CENTER);

            if (dayOfWeek == 5 || dayOfWeek == 6) {
                dayView.setTextColor(Color.RED);
            } else {
                dayView.setTextColor(Color.WHITE);
            }

            TextView fdLabel = new TextView(this);
            fdLabel.setText("Feast Day");
            fdLabel.setTextSize(9);
            fdLabel.setTextColor(Color.WHITE);
            fdLabel.setLines(1);

            fdLabel.setGravity(Gravity.CENTER);
            fdLabel.setPadding(9, 1, 9, 1);
            fdLabel.setBackgroundColor(Color.parseColor("#0B9F89"));
            LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            labelParams.topMargin = 2;
            fdLabel.setLayoutParams(labelParams);

            container.addView(dayView);

            List<FeastDay> allEvents = new ArrayList<>();
            allEvents.addAll(feastDays);
            allEvents.addAll(dbHelper.getAllEvents());

            for (FeastDay fd : allEvents) {
                if (fd.year == currentYear && fd.month == month && fd.day == day) {
//
                    container.addView(fdLabel);
                    fdLabel.setOnClickListener(v -> showFeastDialog(fd.name, fd.story));
                }
            }


            dayGrid.addView(container);


        }
    }

    private void showFeastDialog(String title, String message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.setCancelable(true);
        builder.show();
    }

    private void showAddEventDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_add_event, null);

        EditText nameInput = view.findViewById(R.id.eventNameInput);
        EditText storyInput = view.findViewById(R.id.eventStoryInput);
        TextView datePickerText = view.findViewById(R.id.datePickerText);

        final int[] selectedYear = {currentYear};
        final int[] selectedMonth = {month};
        final int[] selectedDay = {1};

        datePickerText.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            DatePickerDialog dp = new DatePickerDialog(this, (view1, year, monthOfYear, dayOfMonth) -> {
                selectedYear[0] = year;
                selectedMonth[0] = monthOfYear;
                selectedDay[0] = dayOfMonth;
                datePickerText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            dp.show();
        });

        new AlertDialog.Builder(this)
                .setTitle("Add New Event")
                .setView(view)
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = nameInput.getText().toString();
                    String story = storyInput.getText().toString();
                    dbHelper.addEvent(selectedYear[0], selectedMonth[0], selectedDay[0], name, story);
                    Toast.makeText(this, "Event Saved!", Toast.LENGTH_SHORT).show();
                    populateCalendar(); // Refresh view
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

}
