package com.moutamid.unnepek;

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
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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
    View rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_monthly_view_main);
        dbHelper = new DBHelper(this);
        rootLayout = findViewById(R.id.main_layout);
        boolean isDimmed = ColorPreference.isDimmed(this);
        applyDimEffect(isDimmed);
        applyColors();
        headerText = findViewById(R.id.monthYearText);
        dayGrid = findViewById(R.id.monthlyDayGrid);
        ImageView prevBtn = findViewById(R.id.prevMonthBtn);
        ImageView nextBtn = findViewById(R.id.nextMonthBtn);
        ImageView calendarBtn = findViewById(R.id.claneder);
        ImageView addBtn = findViewById(R.id.add_event);
        addBtn.setOnClickListener(v -> showAddEventDialog());
        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        month = prefs.getInt("saved_month", 0);
        currentYear = prefs.getInt("saved_year", 2025);

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
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

        });
        feastDays = Arrays.asList(
                new FeastDay(2025, 0, 6, "Vízkereszt", " A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a " +
                        " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus " +
                        " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus " +
                        " megkeresztelkedésére és az első csodájára. A három királyok aranyat, " +
                        " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a " +
                        " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet " +
                        " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi " +
                        " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak " +
                        " házszenteléseket, Isten áldását kérve az otthonra és az új évre."),
                new FeastDay(2025, 1, 14, "Valentin nap", "  " +
                        " A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint " +
                        " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században " +
                        " titokban esketett meg szerelmespárokat, mert II. Claudius császár " +
                        " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák. " +
                        " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az " +
                        " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a " +
                        " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal " +
                        " és üdvözlőkártyákkal ünneplik."),
                new FeastDay(2025, 2, 5, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja, " +
                        " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a " +
                        " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A " +
                        " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és " +
                        " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki " +
                        " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának " +
                        " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel, " +
                        " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére " +
                        " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A " +
                        " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap " +
                        " előtt tartanak. Általában február végére vagy március elejére esik."),
                new FeastDay(2025, 2, 8, "Nőnap", "Az első Nőnapot 1908 március 8.-án tartották. A női munkások " +
                        " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az " +
                        " ünnep célja a nők társadalmi, politikai és gazdasági jogainak " +
                        " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben " +
                        " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így " +
                        " 1911-ben már több európai országban is megünnepelték. Magyarországon " +
                        " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már " +
                        " a nők jogainak elismerésére és tiszteletére emelkedett."),
                new FeastDay(2025, 2, 15, "1848-as Forradalom", " Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a " +
                        " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti " +
                        " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a " +
                        " sajtószabadságért, a jobb életkörülményekért és a függetlenségért. " +
                        " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című " +
                        " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet " +
                        " függetlenségi törekvése a magyar történelem egyik legfontosabb " +
                        " eseményévé vált. Az események hatására megerősödött a nemzeti " +
                        " identitás és ma már a magyar szabadság és összetartozás napjaként " +
                        " ünnepeljük"),
                new FeastDay(2025, 2, 30, "Óraátállítás", "A tavaszi óraátállítás a nyári időszámítás kezdete.  " +
                        " A tavaszi óraátállításkor az órákat hajnali 2:00-ról 3:00-ra állítjuk " +
                        " előre, hogy jobban kihasználjuk a nappali világosságot és csökkentsük a " +
                        " mesterséges világítás szükségességét. Magyarországon először " +
                        " 1916-ban az első világháború alatt vezették be az energiatakarékosság " +
                        " érdekében, de a háború után megszüntették. 1954 és 1957 között újra " +
                        " alkalmazták, mert az ipari munkahelyeken az esti energiafogyasztás " +
                        " csökkentésére volt szükség, de 1958-ban a lakossági ellenállás és az " +
                        " alacsony megtakarítás miatt eltörölték. 1980-ban ismét bevezették a " +
                        " villamosenergia-megtakarítás érdekében. Azóta évente kétszer állítjuk " +
                        " át az órákat és 1996 óta kormányrendelet szabályozza az " +
                        " óraátállítást Magyarországon"),
                new FeastDay(2025, 3, 1, "Bolondok napja", " Április elseje a bolondok napja, a bolondos tréfák és ártalmatlan " +
                        " átverések napja. Az emberek apró csínyekkel, például hamis hírekkel vagy " +
                        " kitalált történetekkel szórakoztatják egymást. Az internet " +
                        " elterjedésével az álhírek és vírusos videók is népszerűvé váltak. A " +
                        " nap célja, hogy jókedvet és nevetést hozzon, miközben erősíti a " +
                        " közösségi szellemet. Eredete több elméletre vezethető vissza. A " +
                        " legnépszerűbb magyarázat szerint a XVI. században, a Gergely-naptár " +
                        " bevezetésekor azok, akik áprilisban ünnepelték az újévet, " +
                        " Franciaországban bolondnak számítottak, így tréfák céltáblájává " +
                        " váltak. Hasonló szokások figyelhetők meg a kelta tavaszi ünnepekben és az " +
                        " ókori római Hilaria ünnepén is, ahol a bolondos viselkedés a tavaszi " +
                        " megújulást jelezte."),
                new FeastDay(2025, 3, 13, "Virágvasárnap", "A Virágvasárnap a húsvét előtti vasárnap, Jézus jeruzsálemi " +
                        " bevonulásának emlékére van. Az evangéliumok szerint Jézus szamárháton " +
                        " vonult be a városba, miközben a hívek pálmaágakat lengettek, üdvözölve " +
                        " őt, mint a Megváltót. A virágok, a pálmák és a zöld ágak használata " +
                        " az ünnep jelképei, melyek a békét és az üdvözítőt hirdetik. A " +
                        " keresztény hagyományban Virágvasárnap a nagyböjt végét és a húsvéti " +
                        " ünnepekre való lelki felkészülést jelzi. Az ünnep hivatalosan a 4. " +
                        " század óta van jelen az egyházi naptárban és azóta része a keresztény " +
                        " hagyományoknak világszerte."),
                new FeastDay(2025, 3, 18, "Nagypéntek", "A Nagypéntek a keresztény húsvéti ünnepkör legfontosabb napja, amikor " +
                        " Jézus Krisztus kereszthalálát és szenvedését emlékezzük meg. Az ünnep " +
                        " célja, hogy a hívők átgondolják a megváltásra tett áldozat " +
                        " jelentőségét. E napot a 4. század óta ünneplik, amikor a keresztény " +
                        " egyház hivatalosan is elismerte Jézus kereszthalálának fontosságát. " +
                        " Magyarországon a 20. század elejétől munkaszüneti nap. A keresztény " +
                        " hagyományokban sötét napként van jelen, amikor sokan böjtölnek és " +
                        " csendes megemlékezéseket tartanak. Az egyházi szertartások mellett a " +
                        " keresztény közösség számára Nagypéntek a bűnbánat és a lelki " +
                        " megújulás napja is.  Nagypénteket a húsvét előtti pénteken ünneplik, " +
                        " tehát ez a dátum évente változik."),
                new FeastDay(2025, 3, 20, "Húsvétvasárnap", "Húsvétvasárnap a kereszténység legfontosabb ünnepe, Jézus Krisztus " +
                        " feltámadásának napja, amely a halál feletti győzelmet és az örök élet " +
                        " ígéretét hirdeti. Magyarországon 1991 óta munkaszüneti nap, de " +
                        " jelentősége évszázadok óta meghatározó. Húsvét a 40 napos nagyböjt " +
                        " végét jelzi, amely a bűnbánat és a lelki megújulás időszaka. A gyerekek " +
                        " az ajándékaikat hagyományosan ezen a napon kapják meg, édességeket és " +
                        " tojásokat, amelyeket a húsvéti nyúl hoz. A húsvéti nyúl a 16. századi " +
                        " Németországból ered és a termékenység, valamint az újjászületés " +
                        " jelképe. Eredete pogány hagyományokhoz köthető, ahol a nyúl és a tojás " +
                        " a tavaszi újjászületés szimbóluma volt, melyek később beépültek a " +
                        " keresztény húsvéti szokásokba. A Húsvétot évente változó dátumon " +
                        " ünneplik, mivel a tavaszi napéjegyenlőség utáni első teliholdat követő " +
                        " vasárnapra esik."),
                new FeastDay(2025, 3, 21, "Húsvéthétfő", "Húsvéthétfő a húsvéti ünnepkör egyik legfontosabb napja, amely Jézus " +
                        " Krisztus feltámadását követően az új élet és a megújulás " +
                        " szimbólumává vált. Magyarországon a húsvéti locsolás hagyománya a 17. " +
                        " századtól terjedt el. A férfiak vízzel vagy illatos kölnivel locsolják " +
                        " meg a nőket, kívánva nekik egészséget, boldogságot és termékenységet. " +
                        " A locsolás eredete valószínűleg a megtisztulás és a tavaszi megújulás " +
                        " ősi hiedelmeiből származik. A húsvéti nyúl a tavasz, az új élet és a " +
                        " termékenység jelképe, míg a bárány Krisztus áldozatát, ártatlanságát " +
                        " és a megváltást szimbolizálja. Magyarországon a húsvétot a " +
                        " kereszténység elterjedése óta ünneplik és a népszokások a 19. " +
                        " századtól erősödtek meg."),
                new FeastDay(2025, 3, 22, "A Föld napja", "A Föld napja 1970-ben indult el, amikor az Egyesült Államokban Gaylord " +
                        " Nelson szenátor kezdeményezésére több millió ember gyűlt össze, hogy " +
                        " felhívja a figyelmet a környezetszennyezésre és a természeti erőforrások " +
                        " védelmére. A nap célja, hogy globálisan tudatosítsa az emberekben a " +
                        " környezetvédelem fontosságát és ösztönözze a fenntartható életmódra " +
                        " való áttérést. Azóta minden évben április 22.-én világszerte " +
                        " megemlékeznek róla. Számos esemény, akció és kampány hívja fel a " +
                        " figyelmet a bolygó védelmére és a környezeti problémák megoldására. " +
                        " "),
                new FeastDay(2025, 3, 30, "Májusfa állítás", " A májusfa felállításának legjellemzőbb hagyomány szerinti időpontja a " +
                        " május 1-jét megelőző nap délután/este volt.  Magyarországon a középkor " +
                        " óta ismert, tehát legalább a 14. századtól ünneplik. A szokás az ősi " +
                        " tavaszi rituálékra épül, melyek a természet megújulását és az élet " +
                        " körforgását szimbolizálták. A fiatal legények a lányok háza elé " +
                        " állították a feldíszített fákat, hogy kifejezzék szerelmi szándékukat. " +
                        " A májusfát szalagokkal, virágokkal díszítették és május végén vagy " +
                        " pünkösdkor táncos mulatság keretében bontották le, amit kitáncolásnak " +
                        " neveztek. A hagyomány a szerelem és a termékenység ünnepe volt, később " +
                        " pedig összefonódott a munka ünnepével (május 1.). Mára már kevésbé " +
                        " elterjedt szokás, de sok helyen igyekeznek még fenntartani."),
                new FeastDay(2025, 4, 1, "A Munka ünnepe", "Május 1.-e a munka ünnepe. A munkások jogaiért és szolidaritásáért " +
                        " folytatott küzdelem emléknapja világszerte. Az ünnep gyökerei az 1886-os " +
                        " chicagói tüntetésekhez nyúlnak vissza, ahol a munkások a nyolcórás " +
                        " munkaidő bevezetéséért demonstráltak. Az események hatására 1890-től " +
                        " május 1.-ét a munkások nemzetközi szolidaritási napjává " +
                        " nyilvánították.  Magyarországon először 1890-ben tartották meg, de " +
                        " hivatalos állami ünnep csak 1946-ban lett. A szocializmus idején " +
                        " nagyszabású felvonulásokkal és tömegrendezvényekkel ünnepelték. A " +
                        " rendszerváltás után a jelentése átalakult, de továbbra is a munka " +
                        " tiszteletét és a munkavállalók jogainak fontosságát hirdeti."),
                new FeastDay(2025, 4, 4, "Anyák napja", " Az édesanyák napja a szeretet, a tisztelet és a hála ünnepe, amelyet " +
                        " világszerte az édesanyák tiszteletére tartanak. Gyökerei az ókori " +
                        " Görögországba nyúlnak vissza, ahol Rheát, az istenek anyját ünnepelték. " +
                        " A modern anyák napja az Egyesült Államokból indult 1908-ban, amikor Anna " +
                        " Jarvis kampányt indított édesanyja emlékére. Magyarországon először " +
                        " 1925-ben ünnepelték és a májusi Mária-tisztelet hagyományaival is " +
                        " összekapcsolódott. Ez a nap a gondoskodó és szerető édesanyák, valamint " +
                        " minden női gondviselő iránti megbecsülés kifejezésére szolgál. " +
                        " Virágokkal, ajándékokkal és figyelmességgel fejezik ki hálájukat a " +
                        " gyerekek. Bár világszerte eltérő időpontokban ünneplik, mindenhol az " +
                        " anyák iránti szeretet és tisztelet áll a középpontban. Anyák napját " +
                        " Magyarországon minden év május első vasárnapján ünneplik. "),
                new FeastDay(2025, 4, 25, "Gyermeknap", " A gyermeknap a gyerekek jogainak és jólétének fontosságát hirdető " +
                        " ünnep, melyet világszerte különböző időpontokban tartanak. Eredete " +
                        " 1925-re nyúlik vissza, amikor a genfi Gyermekjóléti Konferencián " +
                        " elhatározták, hogy kiemelik a gyermekek védelmének fontosságát. " +
                        " Magyarországon 1931-ben tartották meg először, majd 1950 óta május " +
                        " utolsó vasárnapján ünneplik. Ez a nap a gyerekekről szól, akik ilyenkor " +
                        " különféle programokon vehetnek részt, ahol játékokkal és szórakoztató " +
                        " rendezvényekkel ünneplik őket. Az ünnep célja, hogy felhívja a figyelmet " +
                        " a gyermekek jogaira és boldogságuk fontosságára. Magyarországon a " +
                        " Gyermeknapot minden évben május utolsó vasárnapján ünneplik."),
                new FeastDay(2025, 5, 8, "Pünkösd", "Pünkösd a kereszténység egyik legősibb ünnepe, melyet már a 4. " +
                        " századtól kezdve ünnepelnek a keresztény egyházban. Az ünnep Jézus " +
                        " mennybemenetele után ötven nappal emlékezik meg a Szentlélek " +
                        " eljöveteléről, amely erőt adott az apostoloknak a keresztény hit " +
                        " hirdetéséhez. Ez az esemény az egyház születésnapjaként is ismert. A " +
                        " „pünkösd” elnevezés a görög „pentékoszté” szóból ered, ami " +
                        " ötvenediket jelent. Magyarországon a középkor óta tartják számon és " +
                        " számos népi hagyomány kapcsolódik hozzá, például a pünkösdi király " +
                        " és királyné választása.  Pünkösd vasárnapját a húsvét utáni 50. " +
                        " napon, tehát 50 nappal húsvétvasárnap után ünneplik. "),
                new FeastDay(2025, 5, 9, "Pünkösdhétfő", " A Pünkösdhétfő a keresztény egyház egyik legfontosabb ünnepének, a " +
                        " pünkösdnek a második napja. Pünkösd a húsvét utáni 50. napon van, és a " +
                        " Szentlélek eljövetelét ünnepli, amikor Jézus tanítványai erőt és " +
                        " útmutatást kaptak küldetésükhöz. Az esemény a keresztény egyház " +
                        " születésnapjának is tekinthető. Az ünnep gyökerei a zsidó pünkösdi " +
                        " ünnepre (Sávuót) vezethetők vissza, amely az aratási hálaadás napja " +
                        " volt. Magyarországon pünkösdhétfő munkaszüneti nap és hivatalosan 1993 " +
                        " óta ünnepeljük újra. Az ünnep a megújulást és a közösség " +
                        " összetartozását jelképezi."),
                new FeastDay(2025, 5, 15, "Apák napja", " Apák napja világszerte az édesapák tiszteletére szolgál, elismerve a " +
                        " családban és a gyermeknevelésben betöltött szerepüket. Az ünnep eredete " +
                        " az Egyesült Államokhoz köthető, ahol először 1910-ben tartották meg " +
                        " Sonora Smart Dodd kezdeményezésére , aki édesapját akarta tiszteletben " +
                        " részesíteni . Magyarországon az apák napja az 1990-es években kezdett " +
                        " elterjedni  és 1994 óta ünnepeljük minden év június harmadik " +
                        " vasárnapján. Célja, hogy elismerje az apák és az apaszerepet betöltő " +
                        " férfiak szeretetét, áldozatvállalását és a családban betöltött " +
                        " szerepét. Ez a nap lehetőséget ad arra, hogy köszönetet mondjunk az apák " +
                        " szeretetéért, támogatásáért és áldozatos munkájáért, amelyet " +
                        " családjukért tesznek. "),
                new FeastDay(2025, 5, 21, "Nyári napforduló", " Az év leghosszabb napja.  " +
                        " Napkelte: Körülbelül 4:42 " +
                        " Napnyugta: Körülbelül 20:19 " +
                        " Világos időtartam: Körülbelül 15 óra és 37 " +
                        " A nyári napforduló a leghosszabb nap az évben, amikor a Nap a legmagasabbra " +
                        " emelkedik az égbolton. Ez a jelenség a Föld tengelyferdesége és a " +
                        " keringése miatt következik be. Az északi féltekén a napforduló " +
                        " környékén a Nap több mint 15 órán keresztül a horizont felett " +
                        " tartózkodik. Ezen a napon a Föld az északi féltekén éri el a leghosszabb " +
                        " nappali órákat, így ez a legrövidebb éjszaka."),
                new FeastDay(2025, 7, 20, "Szent István-nap", "Szent István király koronázása körülbelül 1000-ben történt, ezzel " +
                        " megalapozva a keresztény Magyarországot. 1083 augusztus 20.-án szentté " +
                        " avatták a Szent Péter-bazilikában. Az Árpád-korban az István-kultusz az " +
                        " uralkodó dinasztiát támogatta és egységet teremtett a népcsoportok " +
                        " között. 1686-ban XI. Ince pápa elrendelte, hogy az egész katolikus világ " +
                        " emlékezzen meg róla, majd 1771-ben Mária Terézia országos ünneppé tette. " +
                        " Az 1848–49-es forradalom és szabadságharc leverése után egy időre " +
                        " szünetelt az ünneplés, de 1891-ben Ferenc József munkaszüneti nappá " +
                        " nyilvánította. A két világháború között a nemzeti egység " +
                        " szimbólumává vált. 1950-től az új államrendszer ideológiája szerint " +
                        " alakították át az ünnep jelentését, majd 1991-ben ismét állami " +
                        " ünneppé nyilvánították."),
                new FeastDay(2025, 9, 23, "1956-os Forradalom", "Magyarország nemzeti ünnepe, amely az 1956-os forradalom és szabadságharc " +
                        " eseményeire emlékezik. Az ünnep hivatalosan 1989 október 23.-án vált " +
                        " nemzeti ünneppé a rendszerváltás után, amikor a forradalom hőseit " +
                        " ünnepelve tisztelegtek a szabadságért és a demokráciáért folytatott " +
                        " küzdelmük előtt. Az 1956-os forradalom a szovjet megszállás elleni " +
                        " népfelkelés volt, amely bár véresen végződött, alapvető szerepet " +
                        " játszott Magyarország függetlenségi törekvéseiben és a későbbi " +
                        " rendszerváltásban. Az október 23.-i események máig a szabadság és a " +
                        " nemzeti szuverenitás fontos szimbólumai."),
                new FeastDay(2025, 9, 26, "Óraátállítás", "Az őszi óraátállítás (a téli időszámítás kezdete) – (vasárnap), " +
                        " 3:00 → 2:00 " +
                        " Ekkor kell 1 órát visszaállítani az órákat. " +
                        " Az őszi óraátállítás célja, hogy reggel világosabb legyen, így a " +
                        " természetes fényt jobban kihasználjuk. Magyarországon először 1916-ban " +
                        " vezették be az óraátállítást energiatakarékossági okokból. Bár több " +
                        " alkalommal szünetelt, végül 1980-ban újra bevezették, hogy csökkentsék a " +
                        " villamosenergia-felhasználást. A téli időszámítás október utolsó " +
                        " vasárnapján kezdődik, amikor az órákat visszaállítjuk. Ezáltal reggel " +
                        " hamarabb világosodik, de este hamarabb sötétedik. 1996 óta kormányrendelet " +
                        " szabályozza az óraátállítást."),
                new FeastDay(2025, 10, 1, "Mindenszentek", "Mindenszentek ünnepe november 1.-én van, amikor a katolikus egyház " +
                        " megemlékezik minden üdvözült lélekről, akik elnyerték a mennyországot, " +
                        " de nevüket nem tartják számon a naptárban. Az ünnep eredete a " +
                        " kereszténység előtti halotti kultuszokra is visszavezethető. Hivatalosan a " +
                        " 8. században III. Gergely pápa idején vezették be, aki a Szent " +
                        " Péter-bazilikában kápolnát szentelt minden szent tiszteletére.  " +
                        " Magyarországon munkaszüneti nap. Sokan virágokkal és gyertyagyújtással " +
                        " tisztelegnek elhunyt szeretteik emléke előtt. Ez a nap a hit és a " +
                        " megemlékezés ünnepe."),
                new FeastDay(2025, 10, 2, "Halottak napja", "Halottak napja november 2.-án van, amikor a katolikus egyház megemlékezik az " +
                        " elhunyt hívekről. Az ünnep célja, hogy imádságokkal és jó " +
                        " cselekedetekkel segítsük a halottak lelki üdvét és tisztelegjünk " +
                        " szeretteink emléke előtt. Bár gyökerei a kereszténység előtti halotti " +
                        " kultuszokig nyúlnak vissza, mai formáját a 11. században Cluny-i Szent " +
                        " Odiló apát vezette be. Ő rendelte el, hogy a kolostorok imádkozzanak a " +
                        " tisztítótűzben szenvedő lelkekért. A gyertyagyújtás és a virágok " +
                        " elhelyezése máig az emlékezés fontos része."),
                new FeastDay(2025, 10, 30, "Advent első napja", "Advent a karácsony előtti négyhetes időszak, mely Jézus Krisztus " +
                        " születésére és második eljövetelére való várakozást jelenti. Az " +
                        " advent származása a 4. századra nyúlik vissza, amikor bűnbánattal és " +
                        " lelki felkészüléssel várták a karácsonyt. Kezdetben hosszabb böjti " +
                        " időszak volt, de fokozatosan négyhetesre csökkent. Az adventi koszorú " +
                        " hagyománya a 19. században terjedt el, az örökzöld növények a " +
                        " halhatatlanságot és az újjászületést szimbolizálták. A négy gyertya a " +
                        " hitet, a reményt, az örömöt és a szeretetet jelképezi. Minden vasárnap " +
                        " egy újabb gyertya meggyújtásával közelebb kerülünk a karácsonyhoz. " +
                        " Advent első vasárnapját a karácsonyt megelőző négy vasárnap egyikeként " +
                        " ünneplik. Ez a nap általában november végére vagy december elejére esik.  " +
                        " "),
                new FeastDay(2025, 11, 6, "Mikulás", "Mikuláskor december 5.-én éjszaka, vagy 6.-án reggel töltik meg a gyerekek " +
                        " csizmáit édességgel és játékokkal.  Szent Miklós püspök emlék napja. " +
                        " A 4. században élt szent, aki a gyermekek, a hajósok és a szegények " +
                        " védőszentje lett, híres volt jótékonyságáról. Egy legenda szerint " +
                        " titokban segített egy szegény ember három lányán, akiket így férjhez " +
                        " tudott adni.  Ebből a történetből született a hagyomány, hogy Szent " +
                        " Miklós éjjelente ajándékokat hoz a jó gyerekeknek. Ma már világszerte " +
                        " ismert a piros ruhás, fehér szakállú Mikulás.  A Mikulás-hagyományok " +
                        " országonként eltérőek. Egyes helyeken a Mikulás hozza az ajándékokat, " +
                        " máshol a Télapó. A lényeg azonban mindenhol ugyanaz: a szeretet az " +
                        " adakozás és a gyermeki öröm ünnepe."),
                new FeastDay(2025, 11, 13, "Luca napja", " Luca napja a keresztény és a népi hagyományok keveréke. Szent Luca a 3. " +
                        " századi vértanú, a látás védőszentje, de a népi hitvilágban a " +
                        " termékenység, a jólét és a védelem szimbóluma. A hagyomány szerint ezen " +
                        " a napon kezdődött a tél leghosszabb éjszakája, így Luca napja a fény és " +
                        " a sötétség határán áll. Ezen a napon a nők nem dolgoztak, hogy bőséges " +
                        " legyen a következő év termése. Szokás volt Luca-széket készíteni, " +
                        " búzát ültetni és jóslásokat végezni. Luca napja a megújulás és a " +
                        " remény ünnepe, a fény győzelme a sötétség felett és a következő év " +
                        " jólétének biztosítása."),
                new FeastDay(2025, 11, 21, "Téli napforduló", "Az év legrövidebb napja nap.  " +
                        " Napkelte: Körülbelül  7:30 " +
                        " Napnyugta:  Körülbelül 16:00 " +
                        " Világos időtartam: Körülbelül 8 óra 30 perc " +
                        " A téli napforduló a legrövidebb nap az évben, amikor a Nap a " +
                        " legalacsonyabbra emelkedik az égbolton. Ez a jelenség a Föld " +
                        " tengelyferdesége és keringése miatt következik be. Az északi féltekén a " +
                        " napforduló környékén a Nap csak 8 órán keresztül tartózkodik a horizont " +
                        " felett, így ez a leghosszabb éjszaka. Ezen a napon kezdődik a nappali fény " +
                        " fokozatos növekedése, így a következő hónapokban egyre hosszabbak lesznek " +
                        " a nappalok."),
                new FeastDay(2025, 11, 24, "Szenteste", "Szenteste a karácsonyi ünnepkör egyik legfontosabb napja, amely Jézus " +
                        " Krisztus születésének előestéje. Bár Jézus pontos születési dátuma " +
                        " nem ismert, a keresztény egyház a 4. században december 25.-ét jelölte meg " +
                        " születésének napjaként, összekapcsolva azt a téli napfordulóval. " +
                        " December 24.-én este a családok összegyűlnek, közösen vacsoráznak, " +
                        " ajándékokat adnak egymásnak és karácsonyfát állítanak. Magyarországon " +
                        " a hagyomány szerint a Jézuska hozza az ajándékokat, ezzel is megemlékezve " +
                        " Jézus születésének csodájáról. A fenyőfa, amely a 16. századtól vált " +
                        " a karácsony szimbólumává, az örök életet jelképezi. Az ajándékozás a " +
                        " szeretet és a megbecsülés kifejezése, amely a három királyok " +
                        " ajándékozására utal. Karácsony, bár elsősorban keresztény ünnep, " +
                        " világszerte a szeretet, a béke és a család ünnepe lett."),
                new FeastDay(2025, 11, 25, "Karácsony", "A keresztények számára Jézus Krisztus születésének ünnepe, melyet a 4. " +
                        " század óta tartanak. Az ünnep a téli napfordulóhoz kapcsolódik, " +
                        " szimbolizálva a fény győzelmét a sötétség felett. Karácsony a remény, " +
                        " a szeretet és a megváltás ünnepe, amely a családi együttlét, az öröm " +
                        " és a vallási szertartások ideje. Karácsonyfa, betlehem és csillag a " +
                        " szimbólumok, amelyek Jézus születését, az örök életet és az " +
                        " útmutatást képviselik. Az ünneplés világszerte eltérő, de mindegyik " +
                        " kultúrában a szeretet és a béke fontosságát hangsúlyozza. Karácsony " +
                        " lehetőséget ad az emberi kapcsolatok és az élet értelmének " +
                        " átgondolására."),
                new FeastDay(2025, 11, 26, "Karácsony másnapja", "A keresztény hagyományban Szent István, az első vértanú emléknapja, " +
                        " azonban Magyarországon elsősorban Szent István királyra, az " +
                        " államalapítóra emlékeznek. A 4. századi eredetű ünnep során Jézus " +
                        " születése után következik Szent István napja, aki kulcsszerepet játszott " +
                        " a kereszténység elterjesztésében Magyarországon. Bár Szent István a 11. " +
                        " században élt, az egyház később tette december 26.-át emlékévé. " +
                        " Magyarországon nemzeti hősként is tisztelik, mivel ő alapította az " +
                        " államot. Ezen a napon az emberek szentmisére mennek, megemlékeznek " +
                        " királyukról és különböző ünnepi rendezvényeken is részt vesznek. "),
                new FeastDay(2025, 11, 31, "Szilveszter", "A Szilveszter az év utolsó napja, amikor búcsút veszünk az óévtől és " +
                        " ünnepeljük az új év kezdetét. Az ünnep neve Szent Szilveszter pápától " +
                        " származik, aki 335 körül halt meg, de az újév ünneplésének hagyományai " +
                        " sokkal régebbre nyúlnak vissza. Szilveszter éjszakáján családok és " +
                        " barátok együtt ünnepelnek, vacsoráznak, tűzijátékot néznek és " +
                        " jókívánságokat mondanak. A tűzijáték és a zajkeltés az ősi " +
                        " hagyományokból ered. Az ünnep a megújulás, az összetartozás és a " +
                        " remény kifejezője, miközben gazdaságilag is fontos szerepet játszik a " +
                        " fogyasztói társadalomban. " +
                        "  " +
                        " Boldog Új Évet kívánok! Legyen tele örömmel, egészséggel és sikerrel. " +
                        " Hozzon az új év boldog pillanatokat, új lehetőségeket és boldog együtt " +
                        " töltött időt!"),
                new FeastDay(2026, 0, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma. " +
                        " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az " +
                        " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy " +
                        " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és " +
                        " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a " +
                        " mondás. Legyen ez a nap a változás első lépése. Boldogságot, " +
                        " egészséget és kitartást kívánok az új év minden pillanatára!\nAz év első napja a megújulás, az újrakezdés és a remény szimbóluma. Az emberek világszerte különféle szokásokkal és ünnepségekkel köszöntik az új esztendőt, jókívánságokat osztanak, fogadalmakat tesznek, és bíznak abban, hogy az új év jobb lesz, mint az előző."),
                new FeastDay(2026, 0, 6, "Vízkereszt", "A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a " +
                        " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus " +
                        " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus " +
                        " megkeresztelkedésére és az első csodájára. A három királyok aranyat, " +
                        " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a " +
                        " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet " +
                        " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi " +
                        " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak " +
                        " házszenteléseket, Isten áldását kérve az otthonra és az új évre."),
                new FeastDay(2026, 1, 14, "Valentin nap", "A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint " +
                        " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században " +
                        " titokban esketett meg szerelmespárokat, mert II. Claudius császár " +
                        " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák. " +
                        " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az " +
                        " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a " +
                        " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal " +
                        " és üdvözlőkártyákkal ünneplik."),
                new FeastDay(2026, 1, 18, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja, " +
                        " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a " +
                        " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A " +
                        " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és " +
                        " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki " +
                        " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának " +
                        " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel, " +
                        " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére " +
                        " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A " +
                        " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap " +
                        " előtt tartanak. Általában február végére vagy március elejére esik."),
                new FeastDay(2026, 2, 8, "Nőnap", "Az első Nőnapot 1908 március 8.-án tartották. A női munkások " +
                        " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az " +
                        " ünnep célja a nők társadalmi, politikai és gazdasági jogainak " +
                        " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben " +
                        " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így " +
                        " 1911-ben már több európai országban is megünnepelték. Magyarországon " +
                        " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már " +
                        " a nők jogainak elismerésére és tiszteletére emelkedett."),
                new FeastDay(2026, 2, 15, "1848-as Forradalom", "Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a " +
                        " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti " +
                        " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a " +
                        " sajtószabadságért, a jobb életkörülményekért és a függetlenségért. " +
                        " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című " +
                        " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet " +
                        " függetlenségi törekvése a magyar történelem egyik legfontosabb " +
                        " eseményévé vált. Az események hatására megerősödött a nemzeti " +
                        " identitás és ma már a magyar szabadság és összetartozás napjaként " +
                        " ünnepeljük."),
                new FeastDay(2026, 2, 29, "Virágvasárnap", "A Virágvasárnap a húsvét előtti vasárnap, Jézus jeruzsálemi " +
                        " bevonulásának emlékére van. Az evangéliumok szerint Jézus szamárháton " +
                        " vonult be a városba, miközben a hívek pálmaágakat lengettek, üdvözölve " +
                        " őt, mint a Megváltót. A virágok, a pálmák és a zöld ágak használata " +
                        " az ünnep jelképei, melyek a békét és az üdvözítőt hirdetik. A " +
                        " keresztény hagyományban Virágvasárnap a nagyböjt végét és a húsvéti " +
                        " ünnepekre való lelki felkészülést jelzi. Az ünnep hivatalosan a 4. " +
                        " század óta van jelen az egyházi naptárban és azóta része a keresztény " +
                        " hagyományoknak világszerte."),
                new FeastDay(2026, 2, 29, "Óraátállítás", "A tavaszi óraátállítás a nyári időszámítás kezdete.  " +
                        " A tavaszi óraátállításkor az órákat hajnali 2:00-ról 3:00-ra állítjuk " +
                        " előre, hogy jobban kihasználjuk a nappali világosságot és csökkentsük a " +
                        " mesterséges világítás szükségességét. Magyarországon először " +
                        " 1916-ban az első világháború alatt vezették be az energiatakarékosság " +
                        " érdekében, de a háború után megszüntették. 1954 és 1957 között újra " +
                        " alkalmazták, mert az ipari munkahelyeken az esti energiafogyasztás " +
                        " csökkentésére volt szükség, de 1958-ban a lakossági ellenállás és az " +
                        " alacsony megtakarítás miatt eltörölték. 1980-ban ismét bevezették a " +
                        " villamosenergia-megtakarítás érdekében. Azóta évente kétszer állítjuk " +
                        " át az órákat és 1996 óta kormányrendelet szabályozza az " +
                        " óraátállítást Magyarországon."),
                new FeastDay(2026, 3, 1, "Bolondok napja", "Április elseje a bolondok napja, a bolondos tréfák és ártalmatlan " +
                        " átverések napja. Az emberek apró csínyekkel, például hamis hírekkel vagy " +
                        " kitalált történetekkel szórakoztatják egymást. Az internet " +
                        " elterjedésével az álhírek és vírusos videók is népszerűvé váltak. A " +
                        " nap célja, hogy jókedvet és nevetést hozzon, miközben erősíti a " +
                        " közösségi szellemet. Eredete több elméletre vezethető vissza. A " +
                        " legnépszerűbb magyarázat szerint a XVI. században, a Gergely-naptár " +
                        " bevezetésekor azok, akik áprilisban ünnepelték az újévet, " +
                        " Franciaországban bolondnak számítottak, így tréfák céltáblájává " +
                        " váltak. Hasonló szokások figyelhetők meg a kelta tavaszi ünnepekben és az " +
                        " ókori római Hilaria ünnepén is, ahol a bolondos viselkedés a tavaszi " +
                        " megújulást jelezte. "),
                new FeastDay(2026, 3, 3, "Nagypéntek", "A Nagypéntek a keresztény húsvéti ünnepkör legfontosabb napja, amikor " +
                        " Jézus Krisztus kereszthalálát és szenvedését emlékezzük meg. Az ünnep " +
                        " célja, hogy a hívők átgondolják a megváltásra tett áldozat " +
                        " jelentőségét. E napot a 4. század óta ünneplik, amikor a keresztény " +
                        " egyház hivatalosan is elismerte Jézus kereszthalálának fontosságát. " +
                        " Magyarországon a 20. század elejétől munkaszüneti nap. A keresztény " +
                        " hagyományokban sötét napként van jelen, amikor sokan böjtölnek és " +
                        " csendes megemlékezéseket tartanak. Az egyházi szertartások mellett a " +
                        " keresztény közösség számára Nagypéntek a bűnbánat és a lelki " +
                        " megújulás napja is. Nagypénteket a húsvét előtti pénteken ünneplik, " +
                        " tehát ez a dátum évente változik."),
                new FeastDay(2026, 3, 5, "Húsvétvasárnap", "Húsvétvasárnap a kereszténység legfontosabb ünnepe, Jézus Krisztus " +
                        " feltámadásának napja, amely a halál feletti győzelmet és az örök élet " +
                        " ígéretét hirdeti. Magyarországon 1991 óta munkaszüneti nap, de " +
                        " jelentősége évszázadok óta meghatározó. Húsvét a 40 napos nagyböjt " +
                        " végét jelzi, amely a bűnbánat és a lelki megújulás időszaka. A gyerekek " +
                        " az ajándékaikat hagyományosan ezen a napon kapják meg, édességeket és " +
                        " tojásokat, amelyeket a húsvéti nyúl hoz. A húsvéti nyúl a 16. századi " +
                        " Németországból ered és a termékenység, valamint az újjászületés " +
                        " jelképe. Eredete pogány hagyományokhoz köthető, ahol a nyúl és a tojás " +
                        " a tavaszi újjászületés szimbóluma volt, melyek később beépültek a " +
                        " keresztény húsvéti szokásokba.  A Húsvétot évente változó dátumon " +
                        " ünneplik, mivel a tavaszi napéjegyenlőség utáni első teliholdat követő " +
                        " vasárnapra esik."),
                new FeastDay(2026, 3, 6, "Húsvéthétfő", "Húsvéthétfő a húsvéti ünnepkör egyik legfontosabb napja, amely Jézus " +
                        " Krisztus feltámadását követően az új élet és a megújulás " +
                        " szimbólumává vált. Magyarországon a húsvéti locsolás hagyománya a 17. " +
                        " századtól terjedt el. A férfiak vízzel vagy illatos kölnivel locsolják " +
                        " meg a nőket, kívánva nekik egészséget, boldogságot és termékenységet. " +
                        " A locsolás eredete valószínűleg a megtisztulás és a tavaszi megújulás " +
                        " ősi hiedelmeiből származik.  A húsvéti nyúl a tavasz, az új élet és a " +
                        " termékenység jelképe, míg a bárány Krisztus áldozatát, ártatlanságát " +
                        " és a megváltást szimbolizálja. Magyarországon a húsvétot a " +
                        " kereszténység elterjedése óta ünneplik és a népszokások a 19. " +
                        " századtól erősödtek meg."),
                new FeastDay(2026, 3, 22, "A Föld napja", "A Föld napja 1970-ben indult el, amikor az Egyesült Államokban Gaylord " +
                        " Nelson szenátor kezdeményezésére több millió ember gyűlt össze, hogy " +
                        " felhívja a figyelmet a környezetszennyezésre és a természeti erőforrások " +
                        " védelmére. A nap célja, hogy globálisan tudatosítsa az emberekben a " +
                        " környezetvédelem fontosságát és ösztönözze a fenntartható életmódra " +
                        " való áttérést. Azóta minden évben április 22.-én világszerte " +
                        " megemlékeznek róla. Számos esemény, akció és kampány hívja fel a " +
                        " figyelmet a bolygó védelmére és a környezeti problémák megoldására."),
                new FeastDay(2026, 3, 30, "Májusfa állítás", "A májusfa felállításának legjellemzőbb hagyomány szerinti időpontja a " +
                        " május 1-jét megelőző nap délután/este volt.   Magyarországon a " +
                        " középkor óta ismert, tehát legalább a 14. századtól ünneplik. A szokás " +
                        " az ősi tavaszi rituálékra épül, melyek a természet megújulását és az " +
                        " élet körforgását szimbolizálták. A fiatal legények a lányok háza elé " +
                        " állították a feldíszített fákat, hogy kifejezzék szerelmi szándékukat. " +
                        " A májusfát szalagokkal, virágokkal díszítették és május végén vagy " +
                        " pünkösdkor táncos mulatság keretében bontották le, amit kitáncolásnak " +
                        " neveztek. A hagyomány a szerelem és a termékenység ünnepe volt, később " +
                        " pedig összefonódott a munka ünnepével (május 1.). Mára már kevésbé " +
                        " elterjedt szokás, de sok helyen igyekeznek még fenntartani."),
                new FeastDay(2026, 4, 1, "A Munka ünnepe", "Május 1.-e a munka ünnepe. A munkások jogaiért és szolidaritásáért " +
                        " folytatott küzdelem emléknapja világszerte. Az ünnep gyökerei az 1886-os " +
                        " chicagói tüntetésekhez nyúlnak vissza, ahol a munkások a nyolcórás " +
                        " munkaidő bevezetéséért demonstráltak. Az események hatására 1890-től " +
                        " május 1.-ét a munkások nemzetközi szolidaritási napjává " +
                        " nyilvánították.  Magyarországon először 1890-ben tartották meg, de " +
                        " hivatalos állami ünnep csak 1946-ban lett. A szocializmus idején " +
                        " nagyszabású felvonulásokkal és tömegrendezvényekkel ünnepelték. A " +
                        " rendszerváltás után a jelentése átalakult, de továbbra is a munka " +
                        " tiszteletét és a munkavállalók jogainak fontosságát hirdeti."),
                new FeastDay(2026, 4, 3, "Anyák napja", "Az édesanyák napja a szeretet, a tisztelet és a hála ünnepe, amelyet " +
                        " világszerte az édesanyák tiszteletére tartanak. Gyökerei az ókori " +
                        " Görögországba nyúlnak vissza, ahol Rheát, az istenek anyját ünnepelték. " +
                        " A modern anyák napja az Egyesült Államokból indult 1908-ban, amikor Anna " +
                        " Jarvis kampányt indított édesanyja emlékére. Magyarországon először " +
                        " 1925-ben ünnepelték és a májusi Mária-tisztelet hagyományaival is " +
                        " összekapcsolódott. Ez a nap a gondoskodó és szerető édesanyák, valamint " +
                        " minden női gondviselő iránti megbecsülés kifejezésére szolgál. " +
                        " Virágokkal, ajándékokkal és figyelmességgel fejezik ki hálájukat a " +
                        " gyerekek. Bár világszerte eltérő időpontokban ünneplik, mindenhol az " +
                        " anyák iránti szeretet és tisztelet áll a középpontban. Anyák napját " +
                        " Magyarországon minden év május első vasárnapján ünneplik."),
                new FeastDay(2026, 4, 24, "Pünkösd", "Pünkösd a kereszténység egyik legősibb ünnepe, melyet már a 4. " +
                        " századtól kezdve ünnepelnek a keresztény egyházban. Az ünnep Jézus " +
                        " mennybemenetele után ötven nappal emlékezik meg a Szentlélek " +
                        " eljöveteléről, amely erőt adott az apostoloknak a keresztény hit " +
                        " hirdetéséhez. Ez az esemény az egyház születésnapjaként is ismert. A " +
                        " „pünkösd” elnevezés a görög „pentékoszté” szóból ered, ami " +
                        " ötvenediket jelent. Magyarországon a középkor óta tartják számon és " +
                        " számos népi hagyomány kapcsolódik hozzá, például a pünkösdi király " +
                        " és királyné választása. Pünkösd vasárnapját a húsvét utáni 50. " +
                        " napon, tehát 50 nappal húsvétvasárnap után ünneplik."),
                new FeastDay(2026, 4, 25, "Pünkösdhétfő", "A Pünkösdhétfő a keresztény egyház egyik legfontosabb ünnepének, a " +
                        " pünkösdnek a második napja. Pünkösd a húsvét utáni 50. napon van, és a " +
                        " Szentlélek eljövetelét ünnepli, amikor Jézus tanítványai erőt és " +
                        " útmutatást kaptak küldetésükhöz. Az esemény a keresztény egyház " +
                        " születésnapjának is tekinthető. Az ünnep gyökerei a zsidó pünkösdi " +
                        " ünnepre (Sávuót) vezethetők vissza, amely az aratási hálaadás napja " +
                        " volt. Magyarországon pünkösdhétfő munkaszüneti nap és hivatalosan 1993 " +
                        " óta ünnepeljük újra. Az ünnep a megújulást és a közösség " +
                        " összetartozását jelképezi."),
                new FeastDay(2026, 4, 31, "Gyermeknap", "A gyermeknap a gyerekek jogainak és jólétének fontosságát hirdető " +
                        " ünnep, melyet világszerte különböző időpontokban tartanak. Eredete " +
                        " 1925-re nyúlik vissza, amikor a genfi Gyermekjóléti Konferencián " +
                        " elhatározták, hogy kiemelik a gyermekek védelmének fontosságát. " +
                        " Magyarországon 1931-ben tartották meg először, majd 1950 óta május " +
                        " utolsó vasárnapján ünneplik. Ez a nap a gyerekekről szól, akik ilyenkor " +
                        " különféle programokon vehetnek részt, ahol játékokkal és szórakoztató " +
                        " rendezvényekkel ünneplik őket. Az ünnep célja, hogy felhívja a figyelmet " +
                        " a gyermekek jogaira és boldogságuk fontosságára.  Magyarországon a " +
                        " Gyermeknapot minden évben május utolsó vasárnapján ünneplik."),
                new FeastDay(2026, 5, 21, "Apák napja", "Apák napja világszerte az édesapák tiszteletére szolgál, elismerve a " +
                        " családban és a gyermeknevelésben betöltött szerepüket. Az ünnep eredete " +
                        " az Egyesült Államokhoz köthető, ahol először 1910-ben tartották meg " +
                        " Sonora Smart Dodd kezdeményezésére , aki édesapját akarta tiszteletben " +
                        " részesíteni . Magyarországon az apák napja az 1990-es években kezdett " +
                        " elterjedni  és 1994 óta ünnepeljük minden év június harmadik " +
                        " vasárnapján. Célja, hogy elismerje az apák és az apaszerepet betöltő " +
                        " férfiak szeretetét, áldozatvállalását és a családban betöltött " +
                        " szerepét. Ez a nap lehetőséget ad arra, hogy köszönetet mondjunk az apák " +
                        " szeretetéért, támogatásáért és áldozatos munkájáért, amelyet " +
                        " családjukért tesznek. "),
                new FeastDay(2026, 5, 21, "Nyári napforduló", "Az év leghosszabb napja.  " +
                        " Napkelte: Körülbelül 05:07 " +
                        " Napnyugta: Körülbelül 22:07 " +
                        " Világos időtartam: Körülbelül 17 óra " +
                        " A nyári napforduló a leghosszabb nap az évben, amikor a Nap a legmagasabbra " +
                        " emelkedik az égbolton. Ez a jelenség a Föld tengelyferdesége és a " +
                        " keringése miatt következik be. Az északi féltekén a napforduló " +
                        " környékén a Nap több mint 15 órán keresztül a horizont felett " +
                        " tartózkodik. Ezen a napon a Föld az északi féltekén éri el a leghosszabb " +
                        " nappali órákat, így ez a legrövidebb éjszaka."),
                new FeastDay(2026, 7, 20, "Szent István-nap", "Szent István király koronázása körülbelül 1000-ben történt, ezzel " +
                        " megalapozva a keresztény Magyarországot. 1083 augusztus 20.-án szentté " +
                        " avatták a Szent Péter-bazilikában. Az Árpád-korban az István-kultusz az " +
                        " uralkodó dinasztiát támogatta és egységet teremtett a népcsoportok " +
                        " között. 1686-ban XI. Ince pápa elrendelte, hogy az egész katolikus világ " +
                        " emlékezzen meg róla, majd 1771-ben Mária Terézia országos ünneppé tette. " +
                        " Az 1848–49-es forradalom és szabadságharc leverése után egy időre " +
                        " szünetelt az ünneplés, de 1891-ben Ferenc József munkaszüneti nappá " +
                        " nyilvánította. A két világháború között a nemzeti egység " +
                        " szimbólumává vált. 1950-től az új államrendszer ideológiája szerint " +
                        " alakították át az ünnep jelentését, majd 1991-ben ismét állami " +
                        " ünneppé nyilvánították."),
                new FeastDay(2026, 9, 23, "1956-os Forradalom", "Magyarország nemzeti ünnepe, amely az 1956-os forradalom és szabadságharc " +
                        " eseményeire emlékezik. Az ünnep hivatalosan 1989 október 23.-án vált " +
                        " nemzeti ünneppé a rendszerváltás után, amikor a forradalom hőseit " +
                        " ünnepelve tisztelegtek a szabadságért és a demokráciáért folytatott " +
                        " küzdelmük előtt. Az 1956-os forradalom a szovjet megszállás elleni " +
                        " népfelkelés volt, amely bár véresen végződött, alapvető szerepet " +
                        " játszott Magyarország függetlenségi törekvéseiben és a későbbi " +
                        " rendszerváltásban. Az október 23.-i események máig a szabadság és a " +
                        " nemzeti szuverenitás fontos szimbólumai."),
                new FeastDay(2026, 9, 25, "Óraátállítás", "Az őszi óraátállítás (a téli időszámítás kezdete) – (vasárnap), " +
                        " 3:00 → 2:00 " +
                        " Ekkor kell 1 órát visszaállítani az órákat. " +
                        " Az őszi óraátállítás célja, hogy reggel világosabb legyen, így a " +
                        " természetes fényt jobban kihasználjuk. Magyarországon először 1916-ban " +
                        " vezették be az óraátállítást energiatakarékossági okokból. Bár több " +
                        " alkalommal szünetelt, végül 1980-ban újra bevezették, hogy csökkentsék a " +
                        " villamosenergia-felhasználást. A téli időszámítás október utolsó " +
                        " vasárnapján kezdődik, amikor az órákat visszaállítjuk. Ezáltal reggel " +
                        " hamarabb világosodik, de este hamarabb sötétedik. 1996 óta kormányrendelet " +
                        " szabályozza az óraátállítást."),
                new FeastDay(2026, 10, 1, "Mindenszentek", "Mindenszentek ünnepe november 1.-én van, amikor a katolikus egyház " +
                        " megemlékezik minden üdvözült lélekről, akik elnyerték a mennyországot, " +
                        " de nevüket nem tartják számon a naptárban. Az ünnep eredete a " +
                        " kereszténység előtti halotti kultuszokra is visszavezethető. Hivatalosan a " +
                        " 8. században III. Gergely pápa idején vezették be, aki a Szent " +
                        " Péter-bazilikában kápolnát szentelt minden szent tiszteletére. " +
                        " Magyarországon munkaszüneti nap. Sokan virágokkal és gyertyagyújtással " +
                        " tisztelegnek elhunyt szeretteik emléke előtt. Ez a nap a hit és a " +
                        " megemlékezés ünnepe."),
                new FeastDay(2026, 10, 2, "Halottak napja", "Halottak napja november 2.-án van, amikor a katolikus egyház megemlékezik az " +
                        " elhunyt hívekről. Az ünnep célja, hogy imádságokkal és jó " +
                        " cselekedetekkel segítsük a halottak lelki üdvét és tisztelegjünk " +
                        " szeretteink emléke előtt. Bár gyökerei a kereszténység előtti halotti " +
                        " kultuszokig nyúlnak vissza, mai formáját a 11. században Cluny-i Szent " +
                        " Odiló apát vezette be. Ő rendelte el, hogy a kolostorok imádkozzanak a " +
                        " tisztítótűzben szenvedő lelkekért. A gyertyagyújtás és a virágok " +
                        " elhelyezése máig az emlékezés fontos része."),
                new FeastDay(2026, 10, 29, "Advent első napja", "Advent a karácsony előtti négyhetes időszak, mely Jézus Krisztus " +
                        " születésére és második eljövetelére való várakozást jelenti. Az " +
                        " advent származása a 4. századra nyúlik vissza, amikor bűnbánattal és " +
                        " lelki felkészüléssel várták a karácsonyt. Kezdetben hosszabb böjti " +
                        " időszak volt, de fokozatosan négyhetesre csökkent. Az adventi koszorú " +
                        " hagyománya a 19. században terjedt el, az örökzöld növények a " +
                        " halhatatlanságot és az újjászületést szimbolizálták. A négy gyertya a " +
                        " hitet, a reményt, az örömöt és a szeretetet jelképezi. Minden vasárnap " +
                        " egy újabb gyertya meggyújtásával közelebb kerülünk a karácsonyhoz.  " +
                        " Advent első vasárnapját a karácsonyt megelőző négy vasárnap egyikeként " +
                        " ünneplik. Ez a nap általában november végére vagy december elejére esik."),
                new FeastDay(2026, 11, 6, "Mikulás", "Mikuláskor december 5.-én éjszaka, vagy 6.-án reggel töltik meg a gyerekek " +
                        " csizmáit édességgel és játékokkal.  Szent Miklós püspök emlék napja. " +
                        " A 4. században élt szent, aki a gyermekek, a hajósok és a szegények " +
                        " védőszentje lett, híres volt jótékonyságáról. Egy legenda szerint " +
                        " titokban segített egy szegény ember három lányán, akiket így férjhez " +
                        " tudott adni. Ebből a történetből született a hagyomány, hogy Szent " +
                        " Miklós éjjelente ajándékokat hoz a jó gyerekeknek. Ma már világszerte " +
                        " ismert a piros ruhás ,fehér szakállú Mikulás.  A Mikulás-hagyományok " +
                        " országonként eltérőek. Egyes helyeken a Mikulás hozza az ajándékokat, " +
                        " máshol a Télapó. A lényeg azonban mindenhol ugyanaz: a szeretet az " +
                        " adakozás és a gyermeki öröm ünnepe."),
                new FeastDay(2026, 11, 13, "Luca napja", "Luca napja a keresztény és a népi hagyományok keveréke. Szent Luca a 3. " +
                        " századi vértanú, a látás védőszentje, de a népi hitvilágban a " +
                        " termékenység, a jólét és a védelem szimbóluma. A hagyomány szerint ezen " +
                        " a napon kezdődött a tél leghosszabb éjszakája, így Luca napja a fény és " +
                        " a sötétség határán áll. Ezen a napon a nők nem dolgoztak, hogy bőséges " +
                        " legyen a következő év termése. Szokás volt Luca-széket készíteni, " +
                        " búzát ültetni és jóslásokat végezni. Luca napja a megújulás és a " +
                        " remény ünnepe, a fény győzelme a sötétség felett és a következő év " +
                        " jólétének biztosítása."),
                new FeastDay(2026, 11, 21, "Téli napforduló", "Az év legrövidebb napja nap.  " +
                        " Napkelte: Körülbelül 08:49 " +
                        " Napnyugta: Körülbelül 16:29 " +
                        " Világos időtartam: Körülbelül 7 óra 40 perc " +
                        " A téli napforduló a legrövidebb nap az évben, amikor a Nap a " +
                        " legalacsonyabbra emelkedik az égbolton. Ez a jelenség a Föld " +
                        " tengelyferdesége és keringése miatt következik be. Az északi féltekén a " +
                        " napforduló környékén a Nap csak 8 órán keresztül tartózkodik a horizont " +
                        " felett, így ez a leghosszabb éjszaka. Ezen a napon kezdődik a nappali fény " +
                        " fokozatos növekedése, így a következő hónapokban egyre hosszabbak lesznek. " +
                        " a nappalok."),
                new FeastDay(2026, 11, 24, "Szenteste", "Szenteste a karácsonyi ünnepkör egyik legfontosabb napja, amely Jézus " +
                        " Krisztus születésének előestéje. Bár Jézus pontos születési dátuma " +
                        " nem ismert, a keresztény egyház a 4. században december 25.-ét jelölte meg " +
                        " születésének napjaként, összekapcsolva azt a téli napfordulóval. " +
                        " December 24.-én este a családok összegyűlnek, közösen vacsoráznak, " +
                        " ajándékokat adnak egymásnak és karácsonyfát állítanak. Magyarországon " +
                        " a hagyomány szerint a Jézuska hozza az ajándékokat, ezzel is megemlékezve " +
                        " Jézus születésének csodájáról. A fenyőfa, amely a 16. századtól vált " +
                        " a karácsony szimbólumává, az örök életet jelképezi. Az ajándékozás a " +
                        " szeretet és a megbecsülés kifejezése, amely a három királyok " +
                        " ajándékozására utal. Karácsony, bár elsősorban keresztény ünnep, " +
                        " világszerte a szeretet, a béke és a család ünnepe lett."),
                new FeastDay(2026, 11, 25, "Karácsony", "A keresztények számára Jézus Krisztus születésének ünnepe, melyet a 4. " +
                        " század óta tartanak. Az ünnep a téli napfordulóhoz kapcsolódik, " +
                        " szimbolizálva a fény győzelmét a sötétség felett. Karácsony a remény, " +
                        " a szeretet és a megváltás ünnepe, amely a családi együttlét, az öröm " +
                        " és a vallási szertartások ideje. Karácsonyfa, betlehem és csillag a " +
                        " szimbólumok, amelyek Jézus születését, az örök életet és az " +
                        " útmutatást képviselik. Az ünneplés világszerte eltérő, de mindegyik " +
                        " kultúrában a szeretet és a béke fontosságát hangsúlyozza. Karácsony " +
                        " lehetőséget ad az emberi kapcsolatok és az élet értelmének " +
                        " átgondolására."),
                new FeastDay(2026, 11, 26, "Karácsony másnapja", "A keresztény hagyományban Szent István, az első vértanú emléknapja, " +
                        " azonban Magyarországon elsősorban Szent István királyra, az " +
                        " államalapítóra emlékeznek. A 4. századi eredetű ünnep során Jézus " +
                        " születése után következik Szent István napja, aki kulcsszerepet játszott " +
                        " a kereszténység elterjesztésében Magyarországon. Bár Szent István a 11. " +
                        " században élt, az egyház később tette december 26.-át emlékévé. " +
                        " Magyarországon nemzeti hősként is tisztelik, mivel ő alapította az " +
                        " államot. Ezen a napon az emberek szentmisére mennek, megemlékeznek " +
                        " királyukról és különböző ünnepi rendezvényeken is részt vesznek."),
                new FeastDay(2026, 11, 31, "Szilveszter", "A Szilveszter az év utolsó napja, amikor búcsút veszünk az óévtől és " +
                        " ünnepeljük az új év kezdetét. Az ünnep neve Szent Szilveszter pápától " +
                        " származik, aki 335 körül halt meg, de az újév ünneplésének hagyományai " +
                        " sokkal régebbre nyúlnak vissza. Szilveszter éjszakáján családok és " +
                        " barátok együtt ünnepelnek, vacsoráznak, tűzijátékot néznek és " +
                        " jókívánságokat mondanak. A tűzijáték és a zajkeltés az ősi " +
                        " hagyományokból ered. Az ünnep a megújulás, az összetartozás és a " +
                        " remény kifejezője, miközben gazdaságilag is fontos szerepet játszik a " +
                        " fogyasztói társadalomban. " +
                        " Boldog Új Évet kívánok! Legyen tele örömmel, egészséggel és sikerrel. " +
                        " Hozzon az új év boldog pillanatokat, új lehetőségeket és boldog együtt " +
                        " töltött időt!"),
                new FeastDay(2027, 0, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma. " +
                        " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az " +
                        " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy " +
                        " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és " +
                        " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a " +
                        " mondás. Legyen ez a nap a változás első lépése. Boldogságot, " +
                        " egészséget és kitartást kívánok az új év minden pillanatára!"),
                new FeastDay(2027, 0, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma. " +
                        " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az " +
                        " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy " +
                        " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és " +
                        " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a " +
                        " mondás. Legyen ez a nap a változás első lépése. Boldogságot, " +
                        " egészséget és kitartást kívánok az új év minden pillanatára!\nAz év első napja a megújulás, az újrakezdés és a remény szimbóluma. Az emberek világszerte különféle szokásokkal és ünnepségekkel köszöntik az új esztendőt, jókívánságokat osztanak, fogadalmakat tesznek, és bíznak abban, hogy az új év jobb lesz, mint az előző."),
                new FeastDay(2027, 0, 6, "Vízkereszt", "A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a " +
                        " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus " +
                        " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus " +
                        " megkeresztelkedésére és az első csodájára. A három királyok aranyat, " +
                        " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a " +
                        " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet " +
                        " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi " +
                        " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak " +
                        " házszenteléseket, Isten áldását kérve az otthonra és az új évre."),
                new FeastDay(2027, 1, 14, "Valentin nap", "A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint " +
                        " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században " +
                        " titokban esketett meg szerelmespárokat, mert II. Claudius császár " +
                        " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák. " +
                        " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az " +
                        " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a " +
                        " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal " +
                        " és üdvözlőkártyákkal ünneplik."),
                new FeastDay(2027, 1, 18, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja, " +
                        " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a " +
                        " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A " +
                        " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és " +
                        " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki " +
                        " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának " +
                        " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel, " +
                        " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére " +
                        " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A " +
                        " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap " +
                        " előtt tartanak. Általában február végére vagy március elejére esik."),
                new FeastDay(2027, 2, 8, "Nőnap", "Az első Nőnapot 1908 március 8.-án tartották. A női munkások " +
                        " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az " +
                        " ünnep célja a nők társadalmi, politikai és gazdasági jogainak " +
                        " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben " +
                        " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így " +
                        " 1911-ben már több európai országban is megünnepelték. Magyarországon " +
                        " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már " +
                        " a nők jogainak elismerésére és tiszteletére emelkedett."),
                new FeastDay(2027, 2, 15, "1848-as Forradalom", "Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a " +
                        " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti " +
                        " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a " +
                        " sajtószabadságért, a jobb életkörülményekért és a függetlenségért. " +
                        " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című " +
                        " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet " +
                        " függetlenségi törekvése a magyar történelem egyik legfontosabb " +
                        " eseményévé vált. Az események hatására megerősödött a nemzeti " +
                        " identitás és ma már a magyar szabadság és összetartozás napjaként " +
                        " ünnepeljük."),
                new FeastDay(2027, 2, 29, "Virágvasárnap", "A Virágvasárnap a húsvét előtti vasárnap, Jézus jeruzsálemi " +
                        " bevonulásának emlékére van. Az evangéliumok szerint Jézus szamárháton " +
                        " vonult be a városba, miközben a hívek pálmaágakat lengettek, üdvözölve " +
                        " őt, mint a Megváltót. A virágok, a pálmák és a zöld ágak használata " +
                        " az ünnep jelképei, melyek a békét és az üdvözítőt hirdetik. A " +
                        " keresztény hagyományban Virágvasárnap a nagyböjt végét és a húsvéti " +
                        " ünnepekre való lelki felkészülést jelzi. Az ünnep hivatalosan a 4. " +
                        " század óta van jelen az egyházi naptárban és azóta része a keresztény " +
                        " hagyományoknak világszerte."),
                new FeastDay(2027, 2, 29, "Óraátállítás", "A tavaszi óraátállítás a nyári időszámítás kezdete.  " +
                        " A tavaszi óraátállításkor az órákat hajnali 2:00-ról 3:00-ra állítjuk " +
                        " előre, hogy jobban kihasználjuk a nappali világosságot és csökkentsük a " +
                        " mesterséges világítás szükségességét. Magyarországon először " +
                        " 1916-ban az első világháború alatt vezették be az energiatakarékosság " +
                        " érdekében, de a háború után megszüntették. 1954 és 1957 között újra " +
                        " alkalmazták, mert az ipari munkahelyeken az esti energiafogyasztás " +
                        " csökkentésére volt szükség, de 1958-ban a lakossági ellenállás és az " +
                        " alacsony megtakarítás miatt eltörölték. 1980-ban ismét bevezették a " +
                        " villamosenergia-megtakarítás érdekében. Azóta évente kétszer állítjuk " +
                        " át az órákat és 1996 óta kormányrendelet szabályozza az " +
                        " óraátállítást Magyarországon."),
                new FeastDay(2027, 3, 1, "Bolondok napja", "Április elseje a bolondok napja, a bolondos tréfák és ártalmatlan " +
                        " átverések napja. Az emberek apró csínyekkel, például hamis hírekkel vagy " +
                        " kitalált történetekkel szórakoztatják egymást. Az internet " +
                        " elterjedésével az álhírek és vírusos videók is népszerűvé váltak. A " +
                        " nap célja, hogy jókedvet és nevetést hozzon, miközben erősíti a " +
                        " közösségi szellemet. Eredete több elméletre vezethető vissza. A " +
                        " legnépszerűbb magyarázat szerint a XVI. században, a Gergely-naptár " +
                        " bevezetésekor azok, akik áprilisban ünnepelték az újévet, " +
                        " Franciaországban bolondnak számítottak, így tréfák céltáblájává " +
                        " váltak. Hasonló szokások figyelhetők meg a kelta tavaszi ünnepekben és az " +
                        " ókori római Hilaria ünnepén is, ahol a bolondos viselkedés a tavaszi " +
                        " megújulást jelezte. "),
                new FeastDay(2027, 3, 3, "Nagypéntek", "A Nagypéntek a keresztény húsvéti ünnepkör legfontosabb napja, amikor " +
                        " Jézus Krisztus kereszthalálát és szenvedését emlékezzük meg. Az ünnep " +
                        " célja, hogy a hívők átgondolják a megváltásra tett áldozat " +
                        " jelentőségét. E napot a 4. század óta ünneplik, amikor a keresztény " +
                        " egyház hivatalosan is elismerte Jézus kereszthalálának fontosságát. " +
                        " Magyarországon a 20. század elejétől munkaszüneti nap. A keresztény " +
                        " hagyományokban sötét napként van jelen, amikor sokan böjtölnek és " +
                        " csendes megemlékezéseket tartanak. Az egyházi szertartások mellett a " +
                        " keresztény közösség számára Nagypéntek a bűnbánat és a lelki " +
                        " megújulás napja is. Nagypénteket a húsvét előtti pénteken ünneplik, " +
                        " tehát ez a dátum évente változik."),
                new FeastDay(2027, 3, 5, "Húsvétvasárnap", "Húsvétvasárnap a kereszténység legfontosabb ünnepe, Jézus Krisztus " +
                        " feltámadásának napja, amely a halál feletti győzelmet és az örök élet " +
                        " ígéretét hirdeti. Magyarországon 1991 óta munkaszüneti nap, de " +
                        " jelentősége évszázadok óta meghatározó. Húsvét a 40 napos nagyböjt " +
                        " végét jelzi, amely a bűnbánat és a lelki megújulás időszaka. A gyerekek " +
                        " az ajándékaikat hagyományosan ezen a napon kapják meg, édességeket és " +
                        " tojásokat, amelyeket a húsvéti nyúl hoz. A húsvéti nyúl a 16. századi " +
                        " Németországból ered és a termékenység, valamint az újjászületés " +
                        " jelképe. Eredete pogány hagyományokhoz köthető, ahol a nyúl és a tojás " +
                        " a tavaszi újjászületés szimbóluma volt, melyek később beépültek a " +
                        " keresztény húsvéti szokásokba.  A Húsvétot évente változó dátumon " +
                        " ünneplik, mivel a tavaszi napéjegyenlőség utáni első teliholdat követő " +
                        " vasárnapra esik."),
                new FeastDay(2027, 3, 6, "Húsvéthétfő", "Húsvéthétfő a húsvéti ünnepkör egyik legfontosabb napja, amely Jézus " +
                        " Krisztus feltámadását követően az új élet és a megújulás " +
                        " szimbólumává vált. Magyarországon a húsvéti locsolás hagyománya a 17. " +
                        " századtól terjedt el. A férfiak vízzel vagy illatos kölnivel locsolják " +
                        " meg a nőket, kívánva nekik egészséget, boldogságot és termékenységet. " +
                        " A locsolás eredete valószínűleg a megtisztulás és a tavaszi megújulás " +
                        " ősi hiedelmeiből származik.  A húsvéti nyúl a tavasz, az új élet és a " +
                        " termékenység jelképe, míg a bárány Krisztus áldozatát, ártatlanságát " +
                        " és a megváltást szimbolizálja. Magyarországon a húsvétot a " +
                        " kereszténység elterjedése óta ünneplik és a népszokások a 19. " +
                        " századtól erősödtek meg."),
                new FeastDay(2027, 3, 22, "A Föld napja", "A Föld napja 1970-ben indult el, amikor az Egyesült Államokban Gaylord " +
                        " Nelson szenátor kezdeményezésére több millió ember gyűlt össze, hogy " +
                        " felhívja a figyelmet a környezetszennyezésre és a természeti erőforrások " +
                        " védelmére. A nap célja, hogy globálisan tudatosítsa az emberekben a " +
                        " környezetvédelem fontosságát és ösztönözze a fenntartható életmódra " +
                        " való áttérést. Azóta minden évben április 22.-én világszerte " +
                        " megemlékeznek róla. Számos esemény, akció és kampány hívja fel a " +
                        " figyelmet a bolygó védelmére és a környezeti problémák megoldására."),
                new FeastDay(2027, 3, 30, "Májusfa állítás", "A májusfa felállításának legjellemzőbb hagyomány szerinti időpontja a " +
                        " május 1-jét megelőző nap délután/este volt.   Magyarországon a " +
                        " középkor óta ismert, tehát legalább a 14. századtól ünneplik. A szokás " +
                        " az ősi tavaszi rituálékra épül, melyek a természet megújulását és az " +
                        " élet körforgását szimbolizálták. A fiatal legények a lányok háza elé " +
                        " állították a feldíszített fákat, hogy kifejezzék szerelmi szándékukat. " +
                        " A májusfát szalagokkal, virágokkal díszítették és május végén vagy " +
                        " pünkösdkor táncos mulatság keretében bontották le, amit kitáncolásnak " +
                        " neveztek. A hagyomány a szerelem és a termékenység ünnepe volt, később " +
                        " pedig összefonódott a munka ünnepével (május 1.). Mára már kevésbé " +
                        " elterjedt szokás, de sok helyen igyekeznek még fenntartani."),
                new FeastDay(2027, 4, 1, "A Munka ünnepe", "Május 1.-e a munka ünnepe. A munkások jogaiért és szolidaritásáért " +
                        " folytatott küzdelem emléknapja világszerte. Az ünnep gyökerei az 1886-os " +
                        " chicagói tüntetésekhez nyúlnak vissza, ahol a munkások a nyolcórás " +
                        " munkaidő bevezetéséért demonstráltak. Az események hatására 1890-től " +
                        " május 1.-ét a munkások nemzetközi szolidaritási napjává " +
                        " nyilvánították.  Magyarországon először 1890-ben tartották meg, de " +
                        " hivatalos állami ünnep csak 1946-ban lett. A szocializmus idején " +
                        " nagyszabású felvonulásokkal és tömegrendezvényekkel ünnepelték. A " +
                        " rendszerváltás után a jelentése átalakult, de továbbra is a munka " +
                        " tiszteletét és a munkavállalók jogainak fontosságát hirdeti."),
                new FeastDay(2027, 4, 3, "Anyák napja", "Az édesanyák napja a szeretet, a tisztelet és a hála ünnepe, amelyet " +
                        " világszerte az édesanyák tiszteletére tartanak. Gyökerei az ókori " +
                        " Görögországba nyúlnak vissza, ahol Rheát, az istenek anyját ünnepelték. " +
                        " A modern anyák napja az Egyesült Államokból indult 1908-ban, amikor Anna " +
                        " Jarvis kampányt indított édesanyja emlékére. Magyarországon először " +
                        " 1925-ben ünnepelték és a májusi Mária-tisztelet hagyományaival is " +
                        " összekapcsolódott. Ez a nap a gondoskodó és szerető édesanyák, valamint " +
                        " minden női gondviselő iránti megbecsülés kifejezésére szolgál. " +
                        " Virágokkal, ajándékokkal és figyelmességgel fejezik ki hálájukat a " +
                        " gyerekek. Bár világszerte eltérő időpontokban ünneplik, mindenhol az " +
                        " anyák iránti szeretet és tisztelet áll a középpontban. Anyák napját " +
                        " Magyarországon minden év május első vasárnapján ünneplik."),
                new FeastDay(2027, 4, 24, "Pünkösd", "Pünkösd a kereszténység egyik legősibb ünnepe, melyet már a 4. " +
                        " századtól kezdve ünnepelnek a keresztény egyházban. Az ünnep Jézus " +
                        " mennybemenetele után ötven nappal emlékezik meg a Szentlélek " +
                        " eljöveteléről, amely erőt adott az apostoloknak a keresztény hit " +
                        " hirdetéséhez. Ez az esemény az egyház születésnapjaként is ismert. A " +
                        " „pünkösd” elnevezés a görög „pentékoszté” szóból ered, ami " +
                        " ötvenediket jelent. Magyarországon a középkor óta tartják számon és " +
                        " számos népi hagyomány kapcsolódik hozzá, például a pünkösdi király " +
                        " és királyné választása. Pünkösd vasárnapját a húsvét utáni 50. " +
                        " napon, tehát 50 nappal húsvétvasárnap után ünneplik."),
                new FeastDay(2027, 4, 25, "Pünkösdhétfő", "A Pünkösdhétfő a keresztény egyház egyik legfontosabb ünnepének, a " +
                        " pünkösdnek a második napja. Pünkösd a húsvét utáni 50. napon van, és a " +
                        " Szentlélek eljövetelét ünnepli, amikor Jézus tanítványai erőt és " +
                        " útmutatást kaptak küldetésükhöz. Az esemény a keresztény egyház " +
                        " születésnapjának is tekinthető. Az ünnep gyökerei a zsidó pünkösdi " +
                        " ünnepre (Sávuót) vezethetők vissza, amely az aratási hálaadás napja " +
                        " volt. Magyarországon pünkösdhétfő munkaszüneti nap és hivatalosan 1993 " +
                        " óta ünnepeljük újra. Az ünnep a megújulást és a közösség " +
                        " összetartozását jelképezi."),
                new FeastDay(2027, 4, 31, "Gyermeknap", "A gyermeknap a gyerekek jogainak és jólétének fontosságát hirdető " +
                        " ünnep, melyet világszerte különböző időpontokban tartanak. Eredete " +
                        " 1925-re nyúlik vissza, amikor a genfi Gyermekjóléti Konferencián " +
                        " elhatározták, hogy kiemelik a gyermekek védelmének fontosságát. " +
                        " Magyarországon 1931-ben tartották meg először, majd 1950 óta május " +
                        " utolsó vasárnapján ünneplik. Ez a nap a gyerekekről szól, akik ilyenkor " +
                        " különféle programokon vehetnek részt, ahol játékokkal és szórakoztató " +
                        " rendezvényekkel ünneplik őket. Az ünnep célja, hogy felhívja a figyelmet " +
                        " a gyermekek jogaira és boldogságuk fontosságára.  Magyarországon a " +
                        " Gyermeknapot minden évben május utolsó vasárnapján ünneplik."),
                new FeastDay(2027, 5, 21, "Apák napja", "Apák napja világszerte az édesapák tiszteletére szolgál, elismerve a " +
                        " családban és a gyermeknevelésben betöltött szerepüket. Az ünnep eredete " +
                        " az Egyesült Államokhoz köthető, ahol először 1910-ben tartották meg " +
                        " Sonora Smart Dodd kezdeményezésére , aki édesapját akarta tiszteletben " +
                        " részesíteni . Magyarországon az apák napja az 1990-es években kezdett " +
                        " elterjedni  és 1994 óta ünnepeljük minden év június harmadik " +
                        " vasárnapján. Célja, hogy elismerje az apák és az apaszerepet betöltő " +
                        " férfiak szeretetét, áldozatvállalását és a családban betöltött " +
                        " szerepét. Ez a nap lehetőséget ad arra, hogy köszönetet mondjunk az apák " +
                        " szeretetéért, támogatásáért és áldozatos munkájáért, amelyet " +
                        " családjukért tesznek. "),
                new FeastDay(2027, 5, 21, "Nyári napforduló", "Az év leghosszabb napja.  " +
                        " Napkelte: Körülbelül 05:07 " +
                        " Napnyugta: Körülbelül 22:07 " +
                        " Világos időtartam: Körülbelül 17 óra " +
                        " A nyári napforduló a leghosszabb nap az évben, amikor a Nap a legmagasabbra " +
                        " emelkedik az égbolton. Ez a jelenség a Föld tengelyferdesége és a " +
                        " keringése miatt következik be. Az északi féltekén a napforduló " +
                        " környékén a Nap több mint 15 órán keresztül a horizont felett " +
                        " tartózkodik. Ezen a napon a Föld az északi féltekén éri el a leghosszabb " +
                        " nappali órákat, így ez a legrövidebb éjszaka."),
                new FeastDay(2027, 7, 20, "Szent István-nap", "Szent István király koronázása körülbelül 1000-ben történt, ezzel " +
                        " megalapozva a keresztény Magyarországot. 1083 augusztus 20.-án szentté " +
                        " avatták a Szent Péter-bazilikában. Az Árpád-korban az István-kultusz az " +
                        " uralkodó dinasztiát támogatta és egységet teremtett a népcsoportok " +
                        " között. 1686-ban XI. Ince pápa elrendelte, hogy az egész katolikus világ " +
                        " emlékezzen meg róla, majd 1771-ben Mária Terézia országos ünneppé tette. " +
                        " Az 1848–49-es forradalom és szabadságharc leverése után egy időre " +
                        " szünetelt az ünneplés, de 1891-ben Ferenc József munkaszüneti nappá " +
                        " nyilvánította. A két világháború között a nemzeti egység " +
                        " szimbólumává vált. 1950-től az új államrendszer ideológiája szerint " +
                        " alakították át az ünnep jelentését, majd 1991-ben ismét állami " +
                        " ünneppé nyilvánították."),
                new FeastDay(2027, 9, 23, "1956-os Forradalom", "Magyarország nemzeti ünnepe, amely az 1956-os forradalom és szabadságharc " +
                        " eseményeire emlékezik. Az ünnep hivatalosan 1989 október 23.-án vált " +
                        " nemzeti ünneppé a rendszerváltás után, amikor a forradalom hőseit " +
                        " ünnepelve tisztelegtek a szabadságért és a demokráciáért folytatott " +
                        " küzdelmük előtt. Az 1956-os forradalom a szovjet megszállás elleni " +
                        " népfelkelés volt, amely bár véresen végződött, alapvető szerepet " +
                        " játszott Magyarország függetlenségi törekvéseiben és a későbbi " +
                        " rendszerváltásban. Az október 23.-i események máig a szabadság és a " +
                        " nemzeti szuverenitás fontos szimbólumai."),
                new FeastDay(2027, 9, 25, "Óraátállítás", "Az őszi óraátállítás (a téli időszámítás kezdete) – (vasárnap), " +
                        " 3:00 → 2:00 " +
                        " Ekkor kell 1 órát visszaállítani az órákat. " +
                        " Az őszi óraátállítás célja, hogy reggel világosabb legyen, így a " +
                        " természetes fényt jobban kihasználjuk. Magyarországon először 1916-ban " +
                        " vezették be az óraátállítást energiatakarékossági okokból. Bár több " +
                        " alkalommal szünetelt, végül 1980-ban újra bevezették, hogy csökkentsék a " +
                        " villamosenergia-felhasználást. A téli időszámítás október utolsó " +
                        " vasárnapján kezdődik, amikor az órákat visszaállítjuk. Ezáltal reggel " +
                        " hamarabb világosodik, de este hamarabb sötétedik. 1996 óta kormányrendelet " +
                        " szabályozza az óraátállítást."),
                new FeastDay(2027, 10, 1, "Mindenszentek", "Mindenszentek ünnepe november 1.-én van, amikor a katolikus egyház " +
                        " megemlékezik minden üdvözült lélekről, akik elnyerték a mennyországot, " +
                        " de nevüket nem tartják számon a naptárban. Az ünnep eredete a " +
                        " kereszténység előtti halotti kultuszokra is visszavezethető. Hivatalosan a " +
                        " 8. században III. Gergely pápa idején vezették be, aki a Szent " +
                        " Péter-bazilikában kápolnát szentelt minden szent tiszteletére. " +
                        " Magyarországon munkaszüneti nap. Sokan virágokkal és gyertyagyújtással " +
                        " tisztelegnek elhunyt szeretteik emléke előtt. Ez a nap a hit és a " +
                        " megemlékezés ünnepe."),
                new FeastDay(2027, 10, 2, "Halottak napja", "Halottak napja november 2.-án van, amikor a katolikus egyház megemlékezik az " +
                        " elhunyt hívekről. Az ünnep célja, hogy imádságokkal és jó " +
                        " cselekedetekkel segítsük a halottak lelki üdvét és tisztelegjünk " +
                        " szeretteink emléke előtt. Bár gyökerei a kereszténység előtti halotti " +
                        " kultuszokig nyúlnak vissza, mai formáját a 11. században Cluny-i Szent " +
                        " Odiló apát vezette be. Ő rendelte el, hogy a kolostorok imádkozzanak a " +
                        " tisztítótűzben szenvedő lelkekért. A gyertyagyújtás és a virágok " +
                        " elhelyezése máig az emlékezés fontos része."),
                new FeastDay(2027, 10, 29, "Advent első napja", "Advent a karácsony előtti négyhetes időszak, mely Jézus Krisztus " +
                        " születésére és második eljövetelére való várakozást jelenti. Az " +
                        " advent származása a 4. századra nyúlik vissza, amikor bűnbánattal és " +
                        " lelki felkészüléssel várták a karácsonyt. Kezdetben hosszabb böjti " +
                        " időszak volt, de fokozatosan négyhetesre csökkent. Az adventi koszorú " +
                        " hagyománya a 19. században terjedt el, az örökzöld növények a " +
                        " halhatatlanságot és az újjászületést szimbolizálták. A négy gyertya a " +
                        " hitet, a reményt, az örömöt és a szeretetet jelképezi. Minden vasárnap " +
                        " egy újabb gyertya meggyújtásával közelebb kerülünk a karácsonyhoz.  " +
                        " Advent első vasárnapját a karácsonyt megelőző négy vasárnap egyikeként " +
                        " ünneplik. Ez a nap általában november végére vagy december elejére esik."),
                new FeastDay(2027, 11, 6, "Mikulás", "Mikuláskor december 5.-én éjszaka, vagy 6.-án reggel töltik meg a gyerekek " +
                        " csizmáit édességgel és játékokkal.  Szent Miklós püspök emlék napja. " +
                        " A 4. században élt szent, aki a gyermekek, a hajósok és a szegények " +
                        " védőszentje lett, híres volt jótékonyságáról. Egy legenda szerint " +
                        " titokban segített egy szegény ember három lányán, akiket így férjhez " +
                        " tudott adni. Ebből a történetből született a hagyomány, hogy Szent " +
                        " Miklós éjjelente ajándékokat hoz a jó gyerekeknek. Ma már világszerte " +
                        " ismert a piros ruhás ,fehér szakállú Mikulás.  A Mikulás-hagyományok " +
                        " országonként eltérőek. Egyes helyeken a Mikulás hozza az ajándékokat, " +
                        " máshol a Télapó. A lényeg azonban mindenhol ugyanaz: a szeretet az " +
                        " adakozás és a gyermeki öröm ünnepe."),
                new FeastDay(2027, 11, 13, "Luca napja", "Luca napja a keresztény és a népi hagyományok keveréke. Szent Luca a 3. " +
                        " századi vértanú, a látás védőszentje, de a népi hitvilágban a " +
                        " termékenység, a jólét és a védelem szimbóluma. A hagyomány szerint ezen " +
                        " a napon kezdődött a tél leghosszabb éjszakája, így Luca napja a fény és " +
                        " a sötétség határán áll. Ezen a napon a nők nem dolgoztak, hogy bőséges " +
                        " legyen a következő év termése. Szokás volt Luca-széket készíteni, " +
                        " búzát ültetni és jóslásokat végezni. Luca napja a megújulás és a " +
                        " remény ünnepe, a fény győzelme a sötétség felett és a következő év " +
                        " jólétének biztosítása."),
                new FeastDay(2027, 11, 21, "Téli napforduló", "Az év legrövidebb napja nap.  " +
                        " Napkelte: Körülbelül 08:49 " +
                        " Napnyugta: Körülbelül 16:29 " +
                        " Világos időtartam: Körülbelül 7 óra 40 perc " +
                        " A téli napforduló a legrövidebb nap az évben, amikor a Nap a " +
                        " legalacsonyabbra emelkedik az égbolton. Ez a jelenség a Föld " +
                        " tengelyferdesége és keringése miatt következik be. Az északi féltekén a " +
                        " napforduló környékén a Nap csak 8 órán keresztül tartózkodik a horizont " +
                        " felett, így ez a leghosszabb éjszaka. Ezen a napon kezdődik a nappali fény " +
                        " fokozatos növekedése, így a következő hónapokban egyre hosszabbak lesznek. " +
                        " a nappalok."),
                new FeastDay(2027, 11, 24, "Szenteste", "Szenteste a karácsonyi ünnepkör egyik legfontosabb napja, amely Jézus " +
                        " Krisztus születésének előestéje. Bár Jézus pontos születési dátuma " +
                        " nem ismert, a keresztény egyház a 4. században december 25.-ét jelölte meg " +
                        " születésének napjaként, összekapcsolva azt a téli napfordulóval. " +
                        " December 24.-én este a családok összegyűlnek, közösen vacsoráznak, " +
                        " ajándékokat adnak egymásnak és karácsonyfát állítanak. Magyarországon " +
                        " a hagyomány szerint a Jézuska hozza az ajándékokat, ezzel is megemlékezve " +
                        " Jézus születésének csodájáról. A fenyőfa, amely a 16. századtól vált " +
                        " a karácsony szimbólumává, az örök életet jelképezi. Az ajándékozás a " +
                        " szeretet és a megbecsülés kifejezése, amely a három királyok " +
                        " ajándékozására utal. Karácsony, bár elsősorban keresztény ünnep, " +
                        " világszerte a szeretet, a béke és a család ünnepe lett."),
                new FeastDay(2027, 11, 25, "Karácsony", "A keresztények számára Jézus Krisztus születésének ünnepe, melyet a 4. " +
                        " század óta tartanak. Az ünnep a téli napfordulóhoz kapcsolódik, " +
                        " szimbolizálva a fény győzelmét a sötétség felett. Karácsony a remény, " +
                        " a szeretet és a megváltás ünnepe, amely a családi együttlét, az öröm " +
                        " és a vallási szertartások ideje. Karácsonyfa, betlehem és csillag a " +
                        " szimbólumok, amelyek Jézus születését, az örök életet és az " +
                        " útmutatást képviselik. Az ünneplés világszerte eltérő, de mindegyik " +
                        " kultúrában a szeretet és a béke fontosságát hangsúlyozza. Karácsony " +
                        " lehetőséget ad az emberi kapcsolatok és az élet értelmének " +
                        " átgondolására."),
                new FeastDay(2027, 11, 26, "Karácsony másnapja", "A keresztény hagyományban Szent István, az első vértanú emléknapja, " +
                        " azonban Magyarországon elsősorban Szent István királyra, az " +
                        " államalapítóra emlékeznek. A 4. századi eredetű ünnep során Jézus " +
                        " születése után következik Szent István napja, aki kulcsszerepet játszott " +
                        " a kereszténység elterjesztésében Magyarországon. Bár Szent István a 11. " +
                        " században élt, az egyház később tette december 26.-át emlékévé. " +
                        " Magyarországon nemzeti hősként is tisztelik, mivel ő alapította az " +
                        " államot. Ezen a napon az emberek szentmisére mennek, megemlékeznek " +
                        " királyukról és különböző ünnepi rendezvényeken is részt vesznek."),
                new FeastDay(2027, 11, 31, "Szilveszter", "A Szilveszter az év utolsó napja, amikor búcsút veszünk az óévtől és " +
                        " ünnepeljük az új év kezdetét. Az ünnep neve Szent Szilveszter pápától " +
                        " származik, aki 335 körül halt meg, de az újév ünneplésének hagyományai " +
                        " sokkal régebbre nyúlnak vissza. Szilveszter éjszakáján családok és " +
                        " barátok együtt ünnepelnek, vacsoráznak, tűzijátékot néznek és " +
                        " jókívánságokat mondanak. A tűzijáték és a zajkeltés az ősi " +
                        " hagyományokból ered. Az ünnep a megújulás, az összetartozás és a " +
                        " remény kifejezője, miközben gazdaságilag is fontos szerepet játszik a " +
                        " fogyasztói társadalomban. " +
                        " Boldog Új Évet kívánok! Legyen tele örömmel, egészséggel és sikerrel. " +
                        " Hozzon az új év boldog pillanatokat, új lehetőségeket és boldog együtt " +
                        " töltött időt!"),
                new FeastDay(2028, 0, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma. " +
                        " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az " +
                        " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy " +
                        " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és " +
                        " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a " +
                        " mondás. Legyen ez a nap a változás első lépése. Boldogságot, " +
                        " egészséget és kitartást kívánok az új év minden pillanatára! "
                ),
                new FeastDay(2028, 0, 6, "Vízkereszt", " A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a " +
                        " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus " +
                        " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus " +
                        " megkeresztelkedésére és az első csodájára. A három királyok aranyat, " +
                        " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a " +
                        " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet " +
                        " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi " +
                        " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak " +
                        " házszenteléseket, Isten áldását kérve az otthonra és az új évre."),
                new FeastDay(2028, 1, 14, "Valentin nap", "A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint " +
                        " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században " +
                        " titokban esketett meg szerelmespárokat, mert II. Claudius császár " +
                        " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák. " +
                        " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az " +
                        " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a " +
                        " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal " +
                        " és üdvözlőkártyákkal ünneplik."),
                new FeastDay(2028, 2, 1, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja, " +
                        " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a " +
                        " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A " +
                        " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és " +
                        " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki " +
                        " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának " +
                        " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel, " +
                        " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére " +
                        " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A " +
                        " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap " +
                        " előtt tartanak. Általában február végére vagy március elejére esik."),
                new FeastDay(2028, 2, 8, "Nőnap", " Az első Nőnapot 1908 március 8.-án tartották. A női munkások " +
                        " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az " +
                        " ünnep célja a nők társadalmi, politikai és gazdasági jogainak " +
                        " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben " +
                        " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így " +
                        " 1911-ben már több európai országban is megünnepelték. Magyarországon " +
                        " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már " +
                        " a nők jogainak elismerésére és tiszteletére emelkedett."),
                new FeastDay(2028, 2, 15, "1848-as Forradalom", "Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a " +
                        " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti " +
                        " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a " +
                        " sajtószabadságért, a jobb életkörülményekért és a függetlenségért. " +
                        " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című " +
                        " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet " +
                        " függetlenségi törekvése a magyar történelem egyik legfontosabb " +
                        " eseményévé vált. Az események hatására megerősödött a nemzeti " +
                        " identitás és ma már a magyar szabadság és összetartozás napjaként " +
                        " ünnepeljük. " +
                        " "),
                new FeastDay(2028, 2, 26, "Óraátállítás", " A tavaszi óraátállítás a nyári időszámítás kezdete.  " +
                        " A tavaszi óraátállításkor az órákat hajnali 2:00-ról 3:00-ra állítjuk " +
                        " előre, hogy jobban kihasználjuk a nappali világosságot és csökkentsük a " +
                        " mesterséges világítás szükségességét. Magyarországon először " +
                        " 1916-ban az első világháború alatt vezették be az energiatakarékosság " +
                        " érdekében, de a háború után megszüntették. 1954 és 1957 között újra " +
                        " alkalmazták, mert az ipari munkahelyeken az esti energiafogyasztás " +
                        " csökkentésére volt szükség, de 1958-ban a lakossági ellenállás és az " +
                        " alacsony megtakarítás miatt eltörölték. 1980-ban ismét bevezették a " +
                        " villamosenergia-megtakarítás érdekében. Azóta évente kétszer állítjuk " +
                        " át az órákat és 1996 óta kormányrendelet szabályozza az " +
                        " óraátállítást Magyarországon."),
                new FeastDay(2028, 3, 1, "Bolondok napja", "Április elseje a bolondok napja, a bolondos tréfák és ártalmatlan " +
                        " átverések napja. Az emberek apró csínyekkel, például hamis hírekkel vagy " +
                        " kitalált történetekkel szórakoztatják egymást. Az internet " +
                        " elterjedésével az álhírek és vírusos videók is népszerűvé váltak. A " +
                        " nap célja, hogy jókedvet és nevetést hozzon, miközben erősíti a " +
                        " közösségi szellemet. Eredete több elméletre vezethető vissza. A " +
                        " legnépszerűbb magyarázat szerint a XVI. században, a Gergely-naptár " +
                        " bevezetésekor azok, akik áprilisban ünnepelték az újévet, " +
                        " Franciaországban bolondnak számítottak, így tréfák céltáblájává " +
                        " váltak. Hasonló szokások figyelhetők meg a kelta tavaszi ünnepekben és az " +
                        " ókori római Hilaria ünnepén is, ahol a bolondos viselkedés a tavaszi " +
                        " megújulást jelezte. "),
                new FeastDay(2028, 3, 9, "Virágvasárnap", " A Virágvasárnap a húsvét előtti vasárnap, Jézus jeruzsálemi " +
                        " bevonulásának emlékére van. Az evangéliumok szerint Jézus szamárháton " +
                        " vonult be a városba, miközben a hívek pálmaágakat lengettek, üdvözölve " +
                        " őt, mint a Megváltót. A virágok, a pálmák és a zöld ágak használata " +
                        " az ünnep jelképei, melyek a békét és az üdvözítőt hirdetik. A " +
                        " keresztény hagyományban Virágvasárnap a nagyböjt végét és a húsvéti " +
                        " ünnepekre való lelki felkészülést jelzi. Az ünnep hivatalosan a 4. " +
                        " század óta van jelen az egyházi naptárban és azóta része a keresztény " +
                        " hagyományoknak világszerte."),
                new FeastDay(2028, 3, 14, "Nagypéntek", "A Nagypéntek a keresztény húsvéti ünnepkör legfontosabb napja, amikor " +
                        " Jézus Krisztus kereszthalálát és szenvedését emlékezzük meg. Az ünnep " +
                        " célja, hogy a hívők átgondolják a megváltásra tett áldozat " +
                        " jelentőségét. E napot a 4. század óta ünneplik, amikor a keresztény " +
                        " egyház hivatalosan is elismerte Jézus kereszthalálának fontosságát. " +
                        " Magyarországon a 20. század elejétől munkaszüneti nap. A keresztény " +
                        " hagyományokban sötét napként van jelen, amikor sokan böjtölnek és " +
                        " csendes megemlékezéseket tartanak. Az egyházi szertartások mellett a " +
                        " keresztény közösség számára Nagypéntek a bűnbánat és a lelki " +
                        " megújulás napja is.  Nagypénteket a húsvét előtti pénteken ünneplik, " +
                        " tehát ez a dátum évente változik.  " +
                        " "),
                new FeastDay(2028, 3, 16, "Húsvétvasárnap", "Húsvétvasárnap a kereszténység legfontosabb ünnepe, Jézus Krisztus " +
                        " feltámadásának napja, amely a halál feletti győzelmet és az örök élet " +
                        " ígéretét hirdeti. Magyarországon 1991 óta munkaszüneti nap, de " +
                        " jelentősége évszázadok óta meghatározó. Húsvét a 40 napos nagyböjt " +
                        " végét jelzi, amely a bűnbánat és a lelki megújulás időszaka. A gyerekek " +
                        " az ajándékaikat hagyományosan ezen a napon kapják meg, édességeket és " +
                        " tojásokat, amelyeket a húsvéti nyúl hoz.  A húsvéti nyúl a 16. századi " +
                        " Németországból ered és a termékenység, valamint az újjászületés " +
                        " jelképe. Eredete pogány hagyományokhoz köthető, ahol a nyúl és a tojás " +
                        " a tavaszi újjászületés szimbóluma volt, melyek később beépültek a " +
                        " keresztény húsvéti szokásokba. " +
                        " A Húsvétot évente változó dátumon ünneplik, mivel a tavaszi " +
                        " napéjegyenlőség utáni első teliholdat követő vasárnapra esik."),
                new FeastDay(2028, 3, 17, "Húsvéthétfő", "Húsvéthétfő a húsvéti ünnepkör egyik legfontosabb napja, amely Jézus " +
                        " Krisztus feltámadását követően az új élet és a megújulás " +
                        " szimbólumává vált. Magyarországon a húsvéti locsolás hagyománya a 17. " +
                        " századtól terjedt el. A férfiak vízzel vagy illatos kölnivel locsolják " +
                        " meg a nőket, kívánva nekik egészséget, boldogságot és termékenységet. " +
                        " A locsolás eredete valószínűleg a megtisztulás és a tavaszi megújulás " +
                        " ősi hiedelmeiből származik. A húsvéti nyúl a tavasz, az új élet és a " +
                        " termékenység jelképe, míg a bárány Krisztus áldozatát, ártatlanságát " +
                        " és a megváltást szimbolizálja. Magyarországon a húsvétot a " +
                        " kereszténység elterjedése óta ünneplik és a népszokások a 19. " +
                        " századtól erősödtek meg."),
                new FeastDay(2028, 3, 22, "A Föld napja", "A Föld napja 1970-ben indult el, amikor az Egyesült Államokban Gaylord " +
                        " Nelson szenátor kezdeményezésére több millió ember gyűlt össze, hogy " +
                        " felhívja a figyelmet a környezetszennyezésre és a természeti erőforrások " +
                        " védelmére. A nap célja, hogy globálisan tudatosítsa az emberekben a " +
                        " környezetvédelem fontosságát és ösztönözze a fenntartható életmódra " +
                        " való áttérést. Azóta minden évben április 22.-én világszerte " +
                        " megemlékeznek róla. Számos esemény, akció és kampány hívja fel a " +
                        " figyelmet a bolygó védelmére és a környezeti problémák megoldására. " +
                        "  " +
                        "  " +
                        " "),
                new FeastDay(2028, 3, 30, "Májusfa állítás", "A májusfa felállításának legjellemzőbb hagyomány szerinti időpontja a " +
                        " május 1-jét megelőző nap délután/este volt.  " +
                        " Magyarországon a középkor óta ismert, tehát legalább a 14. századtól " +
                        " ünneplik. A szokás az ősi tavaszi rituálékra épül, melyek a természet " +
                        " megújulását és az élet körforgását szimbolizálták. A fiatal legények " +
                        " a lányok háza elé állították a feldíszített fákat, hogy kifejezzék " +
                        " szerelmi szándékukat. A májusfát szalagokkal, virágokkal díszítették " +
                        " és május végén vagy pünkösdkor táncos mulatság keretében bontották " +
                        " le, amit kitáncolásnak neveztek. A hagyomány a szerelem és a termékenység " +
                        " ünnepe volt, később pedig összefonódott a munka ünnepével (május 1.). " +
                        " Mára már kevésbé elterjedt szokás, de sok helyen igyekeznek még " +
                        " fenntartani."),
                new FeastDay(2028, 4, 1, "A Munka ünnepe", "Május 1.-e a munka ünnepe. A munkások jogaiért és szolidaritásáért " +
                        " folytatott küzdelem emléknapja világszerte. Az ünnep gyökerei az 1886-os " +
                        " chicagói tüntetésekhez nyúlnak vissza, ahol a munkások a nyolcórás " +
                        " munkaidő bevezetéséért demonstráltak. Az események hatására 1890-től " +
                        " május 1.-ét a munkások nemzetközi szolidaritási napjává " +
                        " nyilvánították.  Magyarországon először 1890-ben tartották meg, de " +
                        " hivatalos állami ünnep csak 1946-ban lett. A szocializmus idején " +
                        " nagyszabású felvonulásokkal és tömegrendezvényekkel ünnepelték. A " +
                        " rendszerváltás után a jelentése átalakult, de továbbra is a munka " +
                        " tiszteletét és a munkavállalók jogainak fontosságát hirdeti. "),
                new FeastDay(2028, 4, 7, "Anyák napja", "Az édesanyák napja a szeretet, a tisztelet és a hála ünnepe, amelyet " +
                        " világszerte az édesanyák tiszteletére tartanak. Gyökerei az ókori " +
                        " Görögországba nyúlnak vissza, ahol Rheát, az istenek anyját ünnepelték. " +
                        " A modern anyák napja az Egyesült Államokból indult 1908-ban, amikor Anna " +
                        " Jarvis kampányt indított édesanyja emlékére. Magyarországon először " +
                        " 1925-ben ünnepelték és a májusi Mária-tisztelet hagyományaival is " +
                        " összekapcsolódott. Ez a nap a gondoskodó és szerető édesanyák, valamint " +
                        " minden női gondviselő iránti megbecsülés kifejezésére szolgál. " +
                        " Virágokkal, ajándékokkal és figyelmességgel fejezik ki hálájukat a " +
                        " gyerekek. Bár világszerte eltérő időpontokban ünneplik, mindenhol az " +
                        " anyák iránti szeretet és tisztelet áll a középpontban. Anyák napját " +
                        " Magyarországon minden év május első vasárnapján ünneplik.  " +
                        " "),
                new FeastDay(2028, 4, 28, "Gyermeknap", "A gyermeknap a gyerekek jogainak és jólétének fontosságát hirdető " +
                        " ünnep, melyet világszerte különböző időpontokban tartanak. Eredete " +
                        " 1925-re nyúlik vissza, amikor a genfi Gyermekjóléti Konferencián " +
                        " elhatározták, hogy kiemelik a gyermekek védelmének fontosságát. " +
                        " Magyarországon 1931-ben tartották meg először, majd 1950 óta május " +
                        " utolsó vasárnapján ünneplik. Ez a nap a gyerekekről szól, akik ilyenkor " +
                        " különféle programokon vehetnek részt, ahol játékokkal és szórakoztató " +
                        " rendezvényekkel ünneplik őket. Az ünnep célja, hogy felhívja a figyelmet " +
                        " a gyermekek jogaira és boldogságuk fontosságára. Magyarországon a " +
                        " Gyermeknapot minden évben május utolsó vasárnapján ünneplik."),
                new FeastDay(2028, 5, 4, "Pünkösd", "Pünkösd a kereszténység egyik legősibb ünnepe, melyet már a 4. " +
                        " századtól kezdve ünnepelnek a keresztény egyházban. Az ünnep Jézus " +
                        " mennybemenetele után ötven nappal emlékezik meg a Szentlélek " +
                        " eljöveteléről, amely erőt adott az apostoloknak a keresztény hit " +
                        " hirdetéséhez. Ez az esemény az egyház születésnapjaként is ismert. A " +
                        " „pünkösd” elnevezés a görög „pentékoszté” szóból ered, ami " +
                        " ötvenediket jelent. Magyarországon a középkor óta tartják számon és " +
                        " számos népi hagyomány kapcsolódik hozzá, például a pünkösdi király " +
                        " és királyné választása. Pünkösd vasárnapját a húsvét utáni 50. " +
                        " napon, tehát 50 nappal húsvétvasárnap után ünneplik. "),
                new FeastDay(2028, 5, 5, "Pünkösdhétfő", "A Pünkösdhétfő a keresztény egyház egyik legfontosabb ünnepének, a " +
                        " pünkösdnek a második napja. Pünkösd a húsvét utáni 50. napon van, és a " +
                        " Szentlélek eljövetelét ünnepli, amikor Jézus tanítványai erőt és " +
                        " útmutatást kaptak küldetésükhöz. Az esemény a keresztény egyház " +
                        " születésnapjának is tekinthető. Az ünnep gyökerei a zsidó pünkösdi " +
                        " ünnepre (Sávuót) vezethetők vissza, amely az aratási hálaadás napja " +
                        " volt. Magyarországon pünkösdhétfő munkaszüneti nap és hivatalosan 1993 " +
                        " óta ünnepeljük újra. Az ünnep a megújulást és a közösség " +
                        " összetartozását jelképezi."),
                new FeastDay(2028, 5, 18, "Apák napja", "Apák napja világszerte az édesapák tiszteletére szolgál, elismerve a " +
                        " családban és a gyermeknevelésben betöltött szerepüket. Az ünnep eredete " +
                        " az Egyesült Államokhoz köthető, ahol először 1910-ben tartották meg " +
                        " Sonora Smart Dodd kezdeményezésére , aki édesapját akarta tiszteletben " +
                        " részesíteni . Magyarországon az apák napja az 1990-es években kezdett " +
                        " elterjedni  és 1994 óta ünnepeljük minden év június harmadik " +
                        " vasárnapján. Célja, hogy elismerje az apák és az apaszerepet betöltő " +
                        " férfiak szeretetét, áldozatvállalását és a családban betöltött " +
                        " szerepét. Ez a nap lehetőséget ad arra, hogy köszönetet mondjunk az apák " +
                        " szeretetéért, támogatásáért és áldozatos munkájáért, amelyet " +
                        " családjukért tesznek. "),
                new FeastDay(2028, 5, 20, "Nyári napforduló", "Az év leghosszabb napja.  " +
                        " Napkelte:  " +
                        " Napnyugta:  " +
                        " Világos időtartam:  " +
                        " A nyári napforduló a leghosszabb nap az évben, amikor a Nap a legmagasabbra " +
                        " emelkedik az égbolton. Ez a jelenség a Föld tengelyferdesége és a " +
                        " keringése miatt következik be. Az északi féltekén a napforduló " +
                        " környékén a Nap több mint 15 órán keresztül a horizont felett " +
                        " tartózkodik. Ezen a napon a Föld az északi féltekén éri el a leghosszabb " +
                        " nappali órákat, így ez a legrövidebb éjszaka."),
                new FeastDay(2028, 7, 20, "Szent István-nap", "Szent István király koronázása körülbelül 1000-ben történt, ezzel " +
                        " megalapozva a keresztény Magyarországot. 1083 augusztus 20.-án szentté " +
                        " avatták a Szent Péter-bazilikában. Az Árpád-korban az István-kultusz az " +
                        " uralkodó dinasztiát támogatta és egységet teremtett a népcsoportok " +
                        " között. 1686-ban XI. Ince pápa elrendelte, hogy az egész katolikus világ " +
                        " emlékezzen meg róla, majd 1771-ben Mária Terézia országos ünneppé tette. " +
                        " Az 1848–49-es forradalom és szabadságharc leverése után egy időre " +
                        " szünetelt az ünneplés, de 1891-ben Ferenc József munkaszüneti nappá " +
                        " nyilvánította. A két világháború között a nemzeti egység " +
                        " szimbólumává vált. 1950-től az új államrendszer ideológiája szerint " +
                        " alakították át az ünnep jelentését, majd 1991-ben ismét állami " +
                        " ünneppé nyilvánították."),
                new FeastDay(2028, 9, 23, "1956-os Forradalom", "Magyarország nemzeti ünnepe, amely az 1956-os forradalom és szabadságharc " +
                        " eseményeire emlékezik. Az ünnep hivatalosan 1989 október 23.-án vált " +
                        " nemzeti ünneppé a rendszerváltás után, amikor a forradalom hőseit " +
                        " ünnepelve tisztelegtek a szabadságért és a demokráciáért folytatott " +
                        " küzdelmük előtt. Az 1956-os forradalom a szovjet megszállás elleni " +
                        " népfelkelés volt, amely bár véresen végződött, alapvető szerepet " +
                        " játszott Magyarország függetlenségi törekvéseiben és a későbbi " +
                        " rendszerváltásban. Az október 23.-i események máig a szabadság és a " +
                        " nemzeti szuverenitás fontos szimbólumai."),
                new FeastDay(2028, 9, 29, "Óraátállítás", "Az őszi óraátállítás (a téli időszámítás kezdete) – (vasárnap), " +
                        " 3:00 → 2:00 " +
                        " Ekkor kell 1 órát visszaállítani az órákat. " +
                        " Az őszi óraátállítás célja, hogy reggel világosabb legyen, így a " +
                        " természetes fényt jobban kihasználjuk. Magyarországon először 1916-ban " +
                        " vezették be az óraátállítást energiatakarékossági okokból. Bár több " +
                        " alkalommal szünetelt, végül 1980-ban újra bevezették, hogy csökkentsék a " +
                        " villamosenergia-felhasználást. A téli időszámítás október utolsó " +
                        " vasárnapján kezdődik, amikor az órákat visszaállítjuk. Ezáltal reggel " +
                        " hamarabb világosodik, de este hamarabb sötétedik.  1996 óta " +
                        " kormányrendelet szabályozza az óraátállítást. " +
                        " "),
                new FeastDay(2028, 10, 1, "Mindenszentek", "Mindenszentek ünnepe november 1.-én van, amikor a katolikus egyház " +
                        " megemlékezik minden üdvözült lélekről, akik elnyerték a mennyországot, " +
                        " de nevüket nem tartják számon a naptárban. Az ünnep eredete a " +
                        " kereszténység előtti halotti kultuszokra is visszavezethető. Hivatalosan a " +
                        " 8. században III. Gergely pápa idején vezették be, aki a Szent " +
                        " Péter-bazilikában kápolnát szentelt minden szent tiszteletére. " +
                        " Magyarországon munkaszüneti nap. Sokan virágokkal és gyertyagyújtással " +
                        " tisztelegnek elhunyt szeretteik emléke előtt. Ez a nap a hit és a " +
                        " megemlékezés ünnepe."),
                new FeastDay(2028, 10, 2, "Halottak napja", "Halottak napja november 2.-án van, amikor a katolikus egyház megemlékezik az " +
                        " elhunyt hívekről. Az ünnep célja, hogy imádságokkal és jó " +
                        " cselekedetekkel segítsük a halottak lelki üdvét és tisztelegjünk " +
                        " szeretteink emléke előtt. Bár gyökerei a kereszténység előtti halotti " +
                        " kultuszokig nyúlnak vissza, mai formáját a 11. században Cluny-i Szent " +
                        " Odiló apát vezette be. Ő rendelte el, hogy a kolostorok imádkozzanak a " +
                        " tisztítótűzben szenvedő lelkekért. A gyertyagyújtás és a virágok " +
                        " elhelyezése máig az emlékezés fontos része. "),
                new FeastDay(2028, 11, 3, "Advent első napja", "Advent a karácsony előtti négyhetes időszak, mely Jézus Krisztus " +
                        " születésére és második eljövetelére való várakozást jelenti. Az " +
                        " advent származása a 4. századra nyúlik vissza, amikor bűnbánattal és " +
                        " lelki felkészüléssel várták a karácsonyt. Kezdetben hosszabb böjti " +
                        " időszak volt, de fokozatosan négyhetesre csökkent. Az adventi koszorú " +
                        " hagyománya a 19. században terjedt el, az örökzöld növények a " +
                        " halhatatlanságot és az újjászületést szimbolizálták. A négy gyertya a " +
                        " hitet, a reményt, az örömöt és a szeretetet jelképezi. Minden vasárnap " +
                        " egy újabb gyertya meggyújtásával közelebb kerülünk a karácsonyhoz.  " +
                        " Advent első vasárnapját a karácsonyt megelőző négy vasárnap egyikeként " +
                        " ünneplik. Ez a nap általában november végére vagy december elejére esik.  " +
                        " "),
                new FeastDay(2028, 11, 6, "Mikulás", " Mikuláskor december 5.-én éjszaka, vagy 6.-án reggel töltik meg a gyerekek " +
                        " csizmáit édességgel és játékokkal. Szent Miklós püspök emlék napja. A " +
                        " 4. században élt szent, aki a gyermekek, a hajósok és a szegények " +
                        " védőszentje lett, híres volt jótékonyságáról. Egy legenda szerint " +
                        " titokban segített egy szegény ember három lányán, akiket így férjhez " +
                        " tudott adni. " +
                        " Ebből a történetből született a hagyomány, hogy Szent Miklós éjjelente " +
                        " ajándékokat hoz a jó gyerekeknek. Ma már világszerte ismert a piros " +
                        " ruhás, fehér szakállú Mikulás.  A Mikulás-hagyományok országonként " +
                        " eltérőek. Egyes helyeken a Mikulás hozza az ajándékokat, máshol a " +
                        " Télapó. A lényeg azonban mindenhol ugyanaz: a szeretet az adakozás és a " +
                        " gyermeki öröm ünnepe"),
                new FeastDay(2028, 11, 13, "Luca napja", "Luca napja a keresztény és a népi hagyományok keveréke. Szent Luca a 3. " +
                        " századi vértanú, a látás védőszentje, de a népi hitvilágban a " +
                        " termékenység, a jólét és a védelem szimbóluma. A hagyomány szerint ezen " +
                        " a napon kezdődött a tél leghosszabb éjszakája, így Luca napja a fény és " +
                        " a sötétség határán áll. Ezen a napon a nők nem dolgoztak, hogy bőséges " +
                        " legyen a következő év termése. Szokás volt Luca-széket készíteni, " +
                        " búzát ültetni és jóslásokat végezni. Luca napja a megújulás és a " +
                        " remény ünnepe, a fény győzelme a sötétség felett és a következő év " +
                        " jólétének biztosítása. " +
                        " "),
                new FeastDay(2028, 11, 21, "Téli napforduló", "Az év legrövidebb napja nap.  " +
                        " Napkelte:  " +
                        " Napnyugta:  " +
                        " Világos időtartam:  " +
                        " A téli napforduló a legrövidebb nap az évben, amikor a Nap a " +
                        " legalacsonyabbra emelkedik az égbolton. Ez a jelenség a Föld " +
                        " tengelyferdesége és keringése miatt következik be. Az északi féltekén a " +
                        " napforduló környékén a Nap csak 8 órán keresztül tartózkodik a horizont " +
                        " felett, így ez a leghosszabb éjszaka. Ezen a napon kezdődik a nappali fény " +
                        " fokozatos növekedése, így a következő hónapokban egyre hosszabbak lesznek " +
                        " a nappalok."),
                new FeastDay(2028, 11, 24, "Szenteste", "Szenteste a karácsonyi ünnepkör egyik legfontosabb napja, amely Jézus " +
                        " Krisztus születésének előestéje. Bár Jézus pontos születési dátuma " +
                        " nem ismert, a keresztény egyház a 4. században december 25.-ét jelölte meg " +
                        " születésének napjaként, összekapcsolva azt a téli napfordulóval. " +
                        " December 24.-én este a családok összegyűlnek, közösen vacsoráznak, " +
                        " ajándékokat adnak egymásnak és karácsonyfát állítanak. Magyarországon " +
                        " a hagyomány szerint a Jézuska hozza az ajándékokat, ezzel is megemlékezve " +
                        " Jézus születésének csodájáról. A fenyőfa, amely a 16. századtól vált " +
                        " a karácsony szimbólumává, az örök életet jelképezi. Az ajándékozás a " +
                        " szeretet és a megbecsülés kifejezése, amely a három királyok " +
                        " ajándékozására utal. Karácsony, bár elsősorban keresztény ünnep, " +
                        " világszerte a szeretet, a béke és a család ünnepe lett."),
                new FeastDay(2028, 11, 25, "Karácsony", "A keresztények számára Jézus Krisztus születésének ünnepe, melyet a 4. " +
                        " század óta tartanak. Az ünnep a téli napfordulóhoz kapcsolódik, " +
                        " szimbolizálva a fény győzelmét a sötétség felett. Karácsony a remény, " +
                        " a szeretet és a megváltás ünnepe, amely a családi együttlét, az öröm " +
                        " és a vallási szertartások ideje. Karácsonyfa, betlehem és csillag a " +
                        " szimbólumok, amelyek Jézus születését, az örök életet és az " +
                        " útmutatást képviselik. Az ünneplés világszerte eltérő, de mindegyik " +
                        " kultúrában a szeretet és a béke fontosságát hangsúlyozza. Karácsony " +
                        " lehetőséget ad az emberi kapcsolatok és az élet értelmének " +
                        " átgondolására"),
                new FeastDay(2028, 11, 26, "Karácsony másnapja", "A keresztény hagyományban Szent István, az első vértanú emléknapja, " +
                        " azonban Magyarországon elsősorban Szent István királyra, az " +
                        " államalapítóra emlékeznek. A 4. századi eredetű ünnep során Jézus " +
                        " születése után következik Szent István napja, aki kulcsszerepet játszott " +
                        " a kereszténység elterjesztésében Magyarországon. Bár Szent István a 11. " +
                        " században élt, az egyház később tette december 26.-át emlékévé. " +
                        " Magyarországon nemzeti hősként is tisztelik, mivel ő alapította az " +
                        " államot. Ezen a napon az emberek szentmisére mennek, megemlékeznek " +
                        " királyukról és különböző ünnepi rendezvényeken is részt vesznek"),
                new FeastDay(2028, 11, 31, "Szilveszter", "A Szilveszter az év utolsó napja, amikor búcsút veszünk az óévtől és " +
                        " ünnepeljük az új év kezdetét. Az ünnep neve Szent Szilveszter pápától " +
                        " származik, aki 335 körül halt meg, de az újév ünneplésének hagyományai " +
                        " sokkal régebbre nyúlnak vissza. Szilveszter éjszakáján családok és " +
                        " barátok együtt ünnepelnek, vacsoráznak, tűzijátékot néznek és " +
                        " jókívánságokat mondanak. A tűzijáték és a zajkeltés az ősi " +
                        " hagyományokból ered. Az ünnep a megújulás, az összetartozás és a " +
                        " remény kifejezője, miközben gazdaságilag is fontos szerepet játszik a " +
                        " fogyasztói társadalomban. " +
                        "  " +
                        " Boldog Új Évet kívánok! Legyen tele örömmel, egészséggel és sikerrel. " +
                        " Hozzon az új év boldog pillanatokat, új lehetőségeket és boldog együtt " +
                        " töltött időt!"),
                new FeastDay(2029, 0, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma. " +
                        " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az " +
                        " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy " +
                        " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és " +
                        " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a " +
                        " mondás. Legyen ez a nap a változás első lépése. Boldogságot, " +
                        " egészséget és kitartást kívánok az új év minden pillanatára! "
                ),
                new FeastDay(2029, 0, 6, "Vízkereszt", " A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a " +
                        " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus " +
                        " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus " +
                        " megkeresztelkedésére és az első csodájára. A három királyok aranyat, " +
                        " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a " +
                        " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet " +
                        " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi " +
                        " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak " +
                        " házszenteléseket, Isten áldását kérve az otthonra és az új évre."),
                new FeastDay(2029, 1, 14, "Valentin nap", "A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint " +
                        " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században " +
                        " titokban esketett meg szerelmespárokat, mert II. Claudius császár " +
                        " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák. " +
                        " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az " +
                        " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a " +
                        " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal " +
                        " és üdvözlőkártyákkal ünneplik."),
                new FeastDay(2029, 2, 1, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja, " +
                        " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a " +
                        " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A " +
                        " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és " +
                        " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki " +
                        " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának " +
                        " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel, " +
                        " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére " +
                        " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A " +
                        " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap " +
                        " előtt tartanak. Általában február végére vagy március elejére esik."),
                new FeastDay(2029, 2, 8, "Nőnap", " Az első Nőnapot 1908 március 8.-án tartották. A női munkások " +
                        " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az " +
                        " ünnep célja a nők társadalmi, politikai és gazdasági jogainak " +
                        " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben " +
                        " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így " +
                        " 1911-ben már több európai országban is megünnepelték. Magyarországon " +
                        " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már " +
                        " a nők jogainak elismerésére és tiszteletére emelkedett."),
                new FeastDay(2029, 2, 15, "1848-as Forradalom", "Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a " +
                        " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti " +
                        " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a " +
                        " sajtószabadságért, a jobb életkörülményekért és a függetlenségért. " +
                        " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című " +
                        " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet " +
                        " függetlenségi törekvése a magyar történelem egyik legfontosabb " +
                        " eseményévé vált. Az események hatására megerősödött a nemzeti " +
                        " identitás és ma már a magyar szabadság és összetartozás napjaként " +
                        " ünnepeljük. " +
                        " "),
                new FeastDay(2029, 2, 26, "Óraátállítás", " A tavaszi óraátállítás a nyári időszámítás kezdete.  " +
                        " A tavaszi óraátállításkor az órákat hajnali 2:00-ról 3:00-ra állítjuk " +
                        " előre, hogy jobban kihasználjuk a nappali világosságot és csökkentsük a " +
                        " mesterséges világítás szükségességét. Magyarországon először " +
                        " 1916-ban az első világháború alatt vezették be az energiatakarékosság " +
                        " érdekében, de a háború után megszüntették. 1954 és 1957 között újra " +
                        " alkalmazták, mert az ipari munkahelyeken az esti energiafogyasztás " +
                        " csökkentésére volt szükség, de 1958-ban a lakossági ellenállás és az " +
                        " alacsony megtakarítás miatt eltörölték. 1980-ban ismét bevezették a " +
                        " villamosenergia-megtakarítás érdekében. Azóta évente kétszer állítjuk " +
                        " át az órákat és 1996 óta kormányrendelet szabályozza az " +
                        " óraátállítást Magyarországon."),
                new FeastDay(2029, 3, 1, "Bolondok napja", "Április elseje a bolondok napja, a bolondos tréfák és ártalmatlan " +
                        " átverések napja. Az emberek apró csínyekkel, például hamis hírekkel vagy " +
                        " kitalált történetekkel szórakoztatják egymást. Az internet " +
                        " elterjedésével az álhírek és vírusos videók is népszerűvé váltak. A " +
                        " nap célja, hogy jókedvet és nevetést hozzon, miközben erősíti a " +
                        " közösségi szellemet. Eredete több elméletre vezethető vissza. A " +
                        " legnépszerűbb magyarázat szerint a XVI. században, a Gergely-naptár " +
                        " bevezetésekor azok, akik áprilisban ünnepelték az újévet, " +
                        " Franciaországban bolondnak számítottak, így tréfák céltáblájává " +
                        " váltak. Hasonló szokások figyelhetők meg a kelta tavaszi ünnepekben és az " +
                        " ókori római Hilaria ünnepén is, ahol a bolondos viselkedés a tavaszi " +
                        " megújulást jelezte. "),
                new FeastDay(2029, 3, 9, "Virágvasárnap", " A Virágvasárnap a húsvét előtti vasárnap, Jézus jeruzsálemi " +
                        " bevonulásának emlékére van. Az evangéliumok szerint Jézus szamárháton " +
                        " vonult be a városba, miközben a hívek pálmaágakat lengettek, üdvözölve " +
                        " őt, mint a Megváltót. A virágok, a pálmák és a zöld ágak használata " +
                        " az ünnep jelképei, melyek a békét és az üdvözítőt hirdetik. A " +
                        " keresztény hagyományban Virágvasárnap a nagyböjt végét és a húsvéti " +
                        " ünnepekre való lelki felkészülést jelzi. Az ünnep hivatalosan a 4. " +
                        " század óta van jelen az egyházi naptárban és azóta része a keresztény " +
                        " hagyományoknak világszerte."),
                new FeastDay(2029, 3, 14, "Nagypéntek", "A Nagypéntek a keresztény húsvéti ünnepkör legfontosabb napja, amikor " +
                        " Jézus Krisztus kereszthalálát és szenvedését emlékezzük meg. Az ünnep " +
                        " célja, hogy a hívők átgondolják a megváltásra tett áldozat " +
                        " jelentőségét. E napot a 4. század óta ünneplik, amikor a keresztény " +
                        " egyház hivatalosan is elismerte Jézus kereszthalálának fontosságát. " +
                        " Magyarországon a 20. század elejétől munkaszüneti nap. A keresztény " +
                        " hagyományokban sötét napként van jelen, amikor sokan böjtölnek és " +
                        " csendes megemlékezéseket tartanak. Az egyházi szertartások mellett a " +
                        " keresztény közösség számára Nagypéntek a bűnbánat és a lelki " +
                        " megújulás napja is.  Nagypénteket a húsvét előtti pénteken ünneplik, " +
                        " tehát ez a dátum évente változik.  " +
                        " "),
                new FeastDay(2029, 3, 16, "Húsvétvasárnap", "Húsvétvasárnap a kereszténység legfontosabb ünnepe, Jézus Krisztus " +
                        " feltámadásának napja, amely a halál feletti győzelmet és az örök élet " +
                        " ígéretét hirdeti. Magyarországon 1991 óta munkaszüneti nap, de " +
                        " jelentősége évszázadok óta meghatározó. Húsvét a 40 napos nagyböjt " +
                        " végét jelzi, amely a bűnbánat és a lelki megújulás időszaka. A gyerekek " +
                        " az ajándékaikat hagyományosan ezen a napon kapják meg, édességeket és " +
                        " tojásokat, amelyeket a húsvéti nyúl hoz.  A húsvéti nyúl a 16. századi " +
                        " Németországból ered és a termékenység, valamint az újjászületés " +
                        " jelképe. Eredete pogány hagyományokhoz köthető, ahol a nyúl és a tojás " +
                        " a tavaszi újjászületés szimbóluma volt, melyek később beépültek a " +
                        " keresztény húsvéti szokásokba. " +
                        " A Húsvétot évente változó dátumon ünneplik, mivel a tavaszi " +
                        " napéjegyenlőség utáni első teliholdat követő vasárnapra esik."),
                new FeastDay(2029, 3, 17, "Húsvéthétfő", "Húsvéthétfő a húsvéti ünnepkör egyik legfontosabb napja, amely Jézus " +
                        " Krisztus feltámadását követően az új élet és a megújulás " +
                        " szimbólumává vált. Magyarországon a húsvéti locsolás hagyománya a 17. " +
                        " századtól terjedt el. A férfiak vízzel vagy illatos kölnivel locsolják " +
                        " meg a nőket, kívánva nekik egészséget, boldogságot és termékenységet. " +
                        " A locsolás eredete valószínűleg a megtisztulás és a tavaszi megújulás " +
                        " ősi hiedelmeiből származik. A húsvéti nyúl a tavasz, az új élet és a " +
                        " termékenység jelképe, míg a bárány Krisztus áldozatát, ártatlanságát " +
                        " és a megváltást szimbolizálja. Magyarországon a húsvétot a " +
                        " kereszténység elterjedése óta ünneplik és a népszokások a 19. " +
                        " századtól erősödtek meg."),
                new FeastDay(2029, 3, 22, "A Föld napja", "A Föld napja 1970-ben indult el, amikor az Egyesült Államokban Gaylord " +
                        " Nelson szenátor kezdeményezésére több millió ember gyűlt össze, hogy " +
                        " felhívja a figyelmet a környezetszennyezésre és a természeti erőforrások " +
                        " védelmére. A nap célja, hogy globálisan tudatosítsa az emberekben a " +
                        " környezetvédelem fontosságát és ösztönözze a fenntartható életmódra " +
                        " való áttérést. Azóta minden évben április 22.-én világszerte " +
                        " megemlékeznek róla. Számos esemény, akció és kampány hívja fel a " +
                        " figyelmet a bolygó védelmére és a környezeti problémák megoldására. " +
                        "  " +
                        "  " +
                        " "),
                new FeastDay(2029, 3, 30, "Májusfa állítás", "A májusfa felállításának legjellemzőbb hagyomány szerinti időpontja a " +
                        " május 1-jét megelőző nap délután/este volt.  " +
                        " Magyarországon a középkor óta ismert, tehát legalább a 14. századtól " +
                        " ünneplik. A szokás az ősi tavaszi rituálékra épül, melyek a természet " +
                        " megújulását és az élet körforgását szimbolizálták. A fiatal legények " +
                        " a lányok háza elé állították a feldíszített fákat, hogy kifejezzék " +
                        " szerelmi szándékukat. A májusfát szalagokkal, virágokkal díszítették " +
                        " és május végén vagy pünkösdkor táncos mulatság keretében bontották " +
                        " le, amit kitáncolásnak neveztek. A hagyomány a szerelem és a termékenység " +
                        " ünnepe volt, később pedig összefonódott a munka ünnepével (május 1.). " +
                        " Mára már kevésbé elterjedt szokás, de sok helyen igyekeznek még " +
                        " fenntartani."),
                new FeastDay(2029, 4, 1, "A Munka ünnepe", "Május 1.-e a munka ünnepe. A munkások jogaiért és szolidaritásáért " +
                        " folytatott küzdelem emléknapja világszerte. Az ünnep gyökerei az 1886-os " +
                        " chicagói tüntetésekhez nyúlnak vissza, ahol a munkások a nyolcórás " +
                        " munkaidő bevezetéséért demonstráltak. Az események hatására 1890-től " +
                        " május 1.-ét a munkások nemzetközi szolidaritási napjává " +
                        " nyilvánították.  Magyarországon először 1890-ben tartották meg, de " +
                        " hivatalos állami ünnep csak 1946-ban lett. A szocializmus idején " +
                        " nagyszabású felvonulásokkal és tömegrendezvényekkel ünnepelték. A " +
                        " rendszerváltás után a jelentése átalakult, de továbbra is a munka " +
                        " tiszteletét és a munkavállalók jogainak fontosságát hirdeti. "),
                new FeastDay(2029, 4, 7, "Anyák napja", "Az édesanyák napja a szeretet, a tisztelet és a hála ünnepe, amelyet " +
                        " világszerte az édesanyák tiszteletére tartanak. Gyökerei az ókori " +
                        " Görögországba nyúlnak vissza, ahol Rheát, az istenek anyját ünnepelték. " +
                        " A modern anyák napja az Egyesült Államokból indult 1908-ban, amikor Anna " +
                        " Jarvis kampányt indított édesanyja emlékére. Magyarországon először " +
                        " 1925-ben ünnepelték és a májusi Mária-tisztelet hagyományaival is " +
                        " összekapcsolódott. Ez a nap a gondoskodó és szerető édesanyák, valamint " +
                        " minden női gondviselő iránti megbecsülés kifejezésére szolgál. " +
                        " Virágokkal, ajándékokkal és figyelmességgel fejezik ki hálájukat a " +
                        " gyerekek. Bár világszerte eltérő időpontokban ünneplik, mindenhol az " +
                        " anyák iránti szeretet és tisztelet áll a középpontban. Anyák napját " +
                        " Magyarországon minden év május első vasárnapján ünneplik.  " +
                        " "),
                new FeastDay(2029, 4, 28, "Gyermeknap", "A gyermeknap a gyerekek jogainak és jólétének fontosságát hirdető " +
                        " ünnep, melyet világszerte különböző időpontokban tartanak. Eredete " +
                        " 1925-re nyúlik vissza, amikor a genfi Gyermekjóléti Konferencián " +
                        " elhatározták, hogy kiemelik a gyermekek védelmének fontosságát. " +
                        " Magyarországon 1931-ben tartották meg először, majd 1950 óta május " +
                        " utolsó vasárnapján ünneplik. Ez a nap a gyerekekről szól, akik ilyenkor " +
                        " különféle programokon vehetnek részt, ahol játékokkal és szórakoztató " +
                        " rendezvényekkel ünneplik őket. Az ünnep célja, hogy felhívja a figyelmet " +
                        " a gyermekek jogaira és boldogságuk fontosságára. Magyarországon a " +
                        " Gyermeknapot minden évben május utolsó vasárnapján ünneplik."),
                new FeastDay(2029, 5, 4, "Pünkösd", "Pünkösd a kereszténység egyik legősibb ünnepe, melyet már a 4. " +
                        " századtól kezdve ünnepelnek a keresztény egyházban. Az ünnep Jézus " +
                        " mennybemenetele után ötven nappal emlékezik meg a Szentlélek " +
                        " eljöveteléről, amely erőt adott az apostoloknak a keresztény hit " +
                        " hirdetéséhez. Ez az esemény az egyház születésnapjaként is ismert. A " +
                        " „pünkösd” elnevezés a görög „pentékoszté” szóból ered, ami " +
                        " ötvenediket jelent. Magyarországon a középkor óta tartják számon és " +
                        " számos népi hagyomány kapcsolódik hozzá, például a pünkösdi király " +
                        " és királyné választása. Pünkösd vasárnapját a húsvét utáni 50. " +
                        " napon, tehát 50 nappal húsvétvasárnap után ünneplik. "),
                new FeastDay(2029, 5, 5, "Pünkösdhétfő", "A Pünkösdhétfő a keresztény egyház egyik legfontosabb ünnepének, a " +
                        " pünkösdnek a második napja. Pünkösd a húsvét utáni 50. napon van, és a " +
                        " Szentlélek eljövetelét ünnepli, amikor Jézus tanítványai erőt és " +
                        " útmutatást kaptak küldetésükhöz. Az esemény a keresztény egyház " +
                        " születésnapjának is tekinthető. Az ünnep gyökerei a zsidó pünkösdi " +
                        " ünnepre (Sávuót) vezethetők vissza, amely az aratási hálaadás napja " +
                        " volt. Magyarországon pünkösdhétfő munkaszüneti nap és hivatalosan 1993 " +
                        " óta ünnepeljük újra. Az ünnep a megújulást és a közösség " +
                        " összetartozását jelképezi."),
                new FeastDay(2029, 5, 18, "Apák napja", "Apák napja világszerte az édesapák tiszteletére szolgál, elismerve a " +
                        " családban és a gyermeknevelésben betöltött szerepüket. Az ünnep eredete " +
                        " az Egyesült Államokhoz köthető, ahol először 1910-ben tartották meg " +
                        " Sonora Smart Dodd kezdeményezésére , aki édesapját akarta tiszteletben " +
                        " részesíteni . Magyarországon az apák napja az 1990-es években kezdett " +
                        " elterjedni  és 1994 óta ünnepeljük minden év június harmadik " +
                        " vasárnapján. Célja, hogy elismerje az apák és az apaszerepet betöltő " +
                        " férfiak szeretetét, áldozatvállalását és a családban betöltött " +
                        " szerepét. Ez a nap lehetőséget ad arra, hogy köszönetet mondjunk az apák " +
                        " szeretetéért, támogatásáért és áldozatos munkájáért, amelyet " +
                        " családjukért tesznek. "),
                new FeastDay(2029, 5, 20, "Nyári napforduló", "Az év leghosszabb napja.  " +
                        " Napkelte:  " +
                        " Napnyugta:  " +
                        " Világos időtartam:  " +
                        " A nyári napforduló a leghosszabb nap az évben, amikor a Nap a legmagasabbra " +
                        " emelkedik az égbolton. Ez a jelenség a Föld tengelyferdesége és a " +
                        " keringése miatt következik be. Az északi féltekén a napforduló " +
                        " környékén a Nap több mint 15 órán keresztül a horizont felett " +
                        " tartózkodik. Ezen a napon a Föld az északi féltekén éri el a leghosszabb " +
                        " nappali órákat, így ez a legrövidebb éjszaka."),
                new FeastDay(2029, 7, 20, "Szent István-nap", "Szent István király koronázása körülbelül 1000-ben történt, ezzel " +
                        " megalapozva a keresztény Magyarországot. 1083 augusztus 20.-án szentté " +
                        " avatták a Szent Péter-bazilikában. Az Árpád-korban az István-kultusz az " +
                        " uralkodó dinasztiát támogatta és egységet teremtett a népcsoportok " +
                        " között. 1686-ban XI. Ince pápa elrendelte, hogy az egész katolikus világ " +
                        " emlékezzen meg róla, majd 1771-ben Mária Terézia országos ünneppé tette. " +
                        " Az 1848–49-es forradalom és szabadságharc leverése után egy időre " +
                        " szünetelt az ünneplés, de 1891-ben Ferenc József munkaszüneti nappá " +
                        " nyilvánította. A két világháború között a nemzeti egység " +
                        " szimbólumává vált. 1950-től az új államrendszer ideológiája szerint " +
                        " alakították át az ünnep jelentését, majd 1991-ben ismét állami " +
                        " ünneppé nyilvánították."),
                new FeastDay(2029, 9, 23, "1956-os Forradalom", "Magyarország nemzeti ünnepe, amely az 1956-os forradalom és szabadságharc " +
                        " eseményeire emlékezik. Az ünnep hivatalosan 1989 október 23.-án vált " +
                        " nemzeti ünneppé a rendszerváltás után, amikor a forradalom hőseit " +
                        " ünnepelve tisztelegtek a szabadságért és a demokráciáért folytatott " +
                        " küzdelmük előtt. Az 1956-os forradalom a szovjet megszállás elleni " +
                        " népfelkelés volt, amely bár véresen végződött, alapvető szerepet " +
                        " játszott Magyarország függetlenségi törekvéseiben és a későbbi " +
                        " rendszerváltásban. Az október 23.-i események máig a szabadság és a " +
                        " nemzeti szuverenitás fontos szimbólumai."),
                new FeastDay(2029, 9, 29, "Óraátállítás", "Az őszi óraátállítás (a téli időszámítás kezdete) – (vasárnap), " +
                        " 3:00 → 2:00 " +
                        " Ekkor kell 1 órát visszaállítani az órákat. " +
                        " Az őszi óraátállítás célja, hogy reggel világosabb legyen, így a " +
                        " természetes fényt jobban kihasználjuk. Magyarországon először 1916-ban " +
                        " vezették be az óraátállítást energiatakarékossági okokból. Bár több " +
                        " alkalommal szünetelt, végül 1980-ban újra bevezették, hogy csökkentsék a " +
                        " villamosenergia-felhasználást. A téli időszámítás október utolsó " +
                        " vasárnapján kezdődik, amikor az órákat visszaállítjuk. Ezáltal reggel " +
                        " hamarabb világosodik, de este hamarabb sötétedik.  1996 óta " +
                        " kormányrendelet szabályozza az óraátállítást. " +
                        " "),
                new FeastDay(2029, 10, 1, "Mindenszentek", "Mindenszentek ünnepe november 1.-én van, amikor a katolikus egyház " +
                        " megemlékezik minden üdvözült lélekről, akik elnyerték a mennyországot, " +
                        " de nevüket nem tartják számon a naptárban. Az ünnep eredete a " +
                        " kereszténység előtti halotti kultuszokra is visszavezethető. Hivatalosan a " +
                        " 8. században III. Gergely pápa idején vezették be, aki a Szent " +
                        " Péter-bazilikában kápolnát szentelt minden szent tiszteletére. " +
                        " Magyarországon munkaszüneti nap. Sokan virágokkal és gyertyagyújtással " +
                        " tisztelegnek elhunyt szeretteik emléke előtt. Ez a nap a hit és a " +
                        " megemlékezés ünnepe."),
                new FeastDay(2029, 10, 2, "Halottak napja", "Halottak napja november 2.-án van, amikor a katolikus egyház megemlékezik az " +
                        " elhunyt hívekről. Az ünnep célja, hogy imádságokkal és jó " +
                        " cselekedetekkel segítsük a halottak lelki üdvét és tisztelegjünk " +
                        " szeretteink emléke előtt. Bár gyökerei a kereszténység előtti halotti " +
                        " kultuszokig nyúlnak vissza, mai formáját a 11. században Cluny-i Szent " +
                        " Odiló apát vezette be. Ő rendelte el, hogy a kolostorok imádkozzanak a " +
                        " tisztítótűzben szenvedő lelkekért. A gyertyagyújtás és a virágok " +
                        " elhelyezése máig az emlékezés fontos része. "),
                new FeastDay(2029, 11, 3, "Advent első napja", "Advent a karácsony előtti négyhetes időszak, mely Jézus Krisztus " +
                        " születésére és második eljövetelére való várakozást jelenti. Az " +
                        " advent származása a 4. századra nyúlik vissza, amikor bűnbánattal és " +
                        " lelki felkészüléssel várták a karácsonyt. Kezdetben hosszabb böjti " +
                        " időszak volt, de fokozatosan négyhetesre csökkent. Az adventi koszorú " +
                        " hagyománya a 19. században terjedt el, az örökzöld növények a " +
                        " halhatatlanságot és az újjászületést szimbolizálták. A négy gyertya a " +
                        " hitet, a reményt, az örömöt és a szeretetet jelképezi. Minden vasárnap " +
                        " egy újabb gyertya meggyújtásával közelebb kerülünk a karácsonyhoz.  " +
                        " Advent első vasárnapját a karácsonyt megelőző négy vasárnap egyikeként " +
                        " ünneplik. Ez a nap általában november végére vagy december elejére esik.  " +
                        " "),
                new FeastDay(2029, 11, 6, "Mikulás", " Mikuláskor december 5.-én éjszaka, vagy 6.-án reggel töltik meg a gyerekek " +
                        " csizmáit édességgel és játékokkal. Szent Miklós püspök emlék napja. A " +
                        " 4. században élt szent, aki a gyermekek, a hajósok és a szegények " +
                        " védőszentje lett, híres volt jótékonyságáról. Egy legenda szerint " +
                        " titokban segített egy szegény ember három lányán, akiket így férjhez " +
                        " tudott adni. " +
                        " Ebből a történetből született a hagyomány, hogy Szent Miklós éjjelente " +
                        " ajándékokat hoz a jó gyerekeknek. Ma már világszerte ismert a piros " +
                        " ruhás, fehér szakállú Mikulás.  A Mikulás-hagyományok országonként " +
                        " eltérőek. Egyes helyeken a Mikulás hozza az ajándékokat, máshol a " +
                        " Télapó. A lényeg azonban mindenhol ugyanaz: a szeretet az adakozás és a " +
                        " gyermeki öröm ünnepe"),
                new FeastDay(2029, 11, 13, "Luca napja", "Luca napja a keresztény és a népi hagyományok keveréke. Szent Luca a 3. " +
                        " századi vértanú, a látás védőszentje, de a népi hitvilágban a " +
                        " termékenység, a jólét és a védelem szimbóluma. A hagyomány szerint ezen " +
                        " a napon kezdődött a tél leghosszabb éjszakája, így Luca napja a fény és " +
                        " a sötétség határán áll. Ezen a napon a nők nem dolgoztak, hogy bőséges " +
                        " legyen a következő év termése. Szokás volt Luca-széket készíteni, " +
                        " búzát ültetni és jóslásokat végezni. Luca napja a megújulás és a " +
                        " remény ünnepe, a fény győzelme a sötétség felett és a következő év " +
                        " jólétének biztosítása. " +
                        " "),
                new FeastDay(2029, 11, 21, "Téli napforduló", "Az év legrövidebb napja nap.  " +
                        " Napkelte:  " +
                        " Napnyugta:  " +
                        " Világos időtartam:  " +
                        " A téli napforduló a legrövidebb nap az évben, amikor a Nap a " +
                        " legalacsonyabbra emelkedik az égbolton. Ez a jelenség a Föld " +
                        " tengelyferdesége és keringése miatt következik be. Az északi féltekén a " +
                        " napforduló környékén a Nap csak 8 órán keresztül tartózkodik a horizont " +
                        " felett, így ez a leghosszabb éjszaka. Ezen a napon kezdődik a nappali fény " +
                        " fokozatos növekedése, így a következő hónapokban egyre hosszabbak lesznek " +
                        " a nappalok."),
                new FeastDay(2029, 11, 24, "Szenteste", "Szenteste a karácsonyi ünnepkör egyik legfontosabb napja, amely Jézus " +
                        " Krisztus születésének előestéje. Bár Jézus pontos születési dátuma " +
                        " nem ismert, a keresztény egyház a 4. században december 25.-ét jelölte meg " +
                        " születésének napjaként, összekapcsolva azt a téli napfordulóval. " +
                        " December 24.-én este a családok összegyűlnek, közösen vacsoráznak, " +
                        " ajándékokat adnak egymásnak és karácsonyfát állítanak. Magyarországon " +
                        " a hagyomány szerint a Jézuska hozza az ajándékokat, ezzel is megemlékezve " +
                        " Jézus születésének csodájáról. A fenyőfa, amely a 16. századtól vált " +
                        " a karácsony szimbólumává, az örök életet jelképezi. Az ajándékozás a " +
                        " szeretet és a megbecsülés kifejezése, amely a három királyok " +
                        " ajándékozására utal. Karácsony, bár elsősorban keresztény ünnep, " +
                        " világszerte a szeretet, a béke és a család ünnepe lett."),
                new FeastDay(2029, 11, 25, "Karácsony", "A keresztények számára Jézus Krisztus születésének ünnepe, melyet a 4. " +
                        " század óta tartanak. Az ünnep a téli napfordulóhoz kapcsolódik, " +
                        " szimbolizálva a fény győzelmét a sötétség felett. Karácsony a remény, " +
                        " a szeretet és a megváltás ünnepe, amely a családi együttlét, az öröm " +
                        " és a vallási szertartások ideje. Karácsonyfa, betlehem és csillag a " +
                        " szimbólumok, amelyek Jézus születését, az örök életet és az " +
                        " útmutatást képviselik. Az ünneplés világszerte eltérő, de mindegyik " +
                        " kultúrában a szeretet és a béke fontosságát hangsúlyozza. Karácsony " +
                        " lehetőséget ad az emberi kapcsolatok és az élet értelmének " +
                        " átgondolására"),
                new FeastDay(2029, 11, 26, "Karácsony másnapja", "A keresztény hagyományban Szent István, az első vértanú emléknapja, " +
                        " azonban Magyarországon elsősorban Szent István királyra, az " +
                        " államalapítóra emlékeznek. A 4. századi eredetű ünnep során Jézus " +
                        " születése után következik Szent István napja, aki kulcsszerepet játszott " +
                        " a kereszténység elterjesztésében Magyarországon. Bár Szent István a 11. " +
                        " században élt, az egyház később tette december 26.-át emlékévé. " +
                        " Magyarországon nemzeti hősként is tisztelik, mivel ő alapította az " +
                        " államot. Ezen a napon az emberek szentmisére mennek, megemlékeznek " +
                        " királyukról és különböző ünnepi rendezvényeken is részt vesznek"),
                new FeastDay(2029, 11, 31, "Szilveszter", "A Szilveszter az év utolsó napja, amikor búcsút veszünk az óévtől és " +
                        " ünnepeljük az új év kezdetét. Az ünnep neve Szent Szilveszter pápától " +
                        " származik, aki 335 körül halt meg, de az újév ünneplésének hagyományai " +
                        " sokkal régebbre nyúlnak vissza. Szilveszter éjszakáján családok és " +
                        " barátok együtt ünnepelnek, vacsoráznak, tűzijátékot néznek és " +
                        " jókívánságokat mondanak. A tűzijáték és a zajkeltés az ősi " +
                        " hagyományokból ered. Az ünnep a megújulás, az összetartozás és a " +
                        " remény kifejezője, miközben gazdaságilag is fontos szerepet játszik a " +
                        " fogyasztói társadalomban. " +
                        "  " +
                        " Boldog Új Évet kívánok! Legyen tele örömmel, egészséggel és sikerrel. " +
                        " Hozzon az új év boldog pillanatokat, új lehetőségeket és boldog együtt " +
                        " töltött időt!"),

                new FeastDay(2030, 0, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma. " +
                        " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az " +
                        " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy " +
                        " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és " +
                        " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a " +
                        " mondás. Legyen ez a nap a változás első lépése. Boldogságot, " +
                        " egészséget és kitartást kívánok az új év minden pillanatára! "
                ),
                new FeastDay(2030, 0, 6, "Vízkereszt", " A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a " +
                        " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus " +
                        " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus " +
                        " megkeresztelkedésére és az első csodájára. A három királyok aranyat, " +
                        " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a " +
                        " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet " +
                        " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi " +
                        " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak " +
                        " házszenteléseket, Isten áldását kérve az otthonra és az új évre."),
                new FeastDay(2030, 1, 14, "Valentin nap", "A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint " +
                        " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században " +
                        " titokban esketett meg szerelmespárokat, mert II. Claudius császár " +
                        " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák. " +
                        " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az " +
                        " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a " +
                        " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal " +
                        " és üdvözlőkártyákkal ünneplik."),
                new FeastDay(2030, 2, 1, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja, " +
                        " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a " +
                        " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A " +
                        " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és " +
                        " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki " +
                        " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának " +
                        " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel, " +
                        " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére " +
                        " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A " +
                        " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap " +
                        " előtt tartanak. Általában február végére vagy március elejére esik."),
                new FeastDay(2030, 2, 8, "Nőnap", " Az első Nőnapot 1908 március 8.-án tartották. A női munkások " +
                        " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az " +
                        " ünnep célja a nők társadalmi, politikai és gazdasági jogainak " +
                        " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben " +
                        " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így " +
                        " 1911-ben már több európai országban is megünnepelték. Magyarországon " +
                        " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már " +
                        " a nők jogainak elismerésére és tiszteletére emelkedett."),
                new FeastDay(2030, 2, 15, "1848-as Forradalom", "Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a " +
                        " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti " +
                        " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a " +
                        " sajtószabadságért, a jobb életkörülményekért és a függetlenségért. " +
                        " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című " +
                        " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet " +
                        " függetlenségi törekvése a magyar történelem egyik legfontosabb " +
                        " eseményévé vált. Az események hatására megerősödött a nemzeti " +
                        " identitás és ma már a magyar szabadság és összetartozás napjaként " +
                        " ünnepeljük. " +
                        " "),
                new FeastDay(2030, 2, 26, "Óraátállítás", " A tavaszi óraátállítás a nyári időszámítás kezdete.  " +
                        " A tavaszi óraátállításkor az órákat hajnali 2:00-ról 3:00-ra állítjuk " +
                        " előre, hogy jobban kihasználjuk a nappali világosságot és csökkentsük a " +
                        " mesterséges világítás szükségességét. Magyarországon először " +
                        " 1916-ban az első világháború alatt vezették be az energiatakarékosság " +
                        " érdekében, de a háború után megszüntették. 1954 és 1957 között újra " +
                        " alkalmazták, mert az ipari munkahelyeken az esti energiafogyasztás " +
                        " csökkentésére volt szükség, de 1958-ban a lakossági ellenállás és az " +
                        " alacsony megtakarítás miatt eltörölték. 1980-ban ismét bevezették a " +
                        " villamosenergia-megtakarítás érdekében. Azóta évente kétszer állítjuk " +
                        " át az órákat és 1996 óta kormányrendelet szabályozza az " +
                        " óraátállítást Magyarországon."),
                new FeastDay(2030, 3, 1, "Bolondok napja", "Április elseje a bolondok napja, a bolondos tréfák és ártalmatlan " +
                        " átverések napja. Az emberek apró csínyekkel, például hamis hírekkel vagy " +
                        " kitalált történetekkel szórakoztatják egymást. Az internet " +
                        " elterjedésével az álhírek és vírusos videók is népszerűvé váltak. A " +
                        " nap célja, hogy jókedvet és nevetést hozzon, miközben erősíti a " +
                        " közösségi szellemet. Eredete több elméletre vezethető vissza. A " +
                        " legnépszerűbb magyarázat szerint a XVI. században, a Gergely-naptár " +
                        " bevezetésekor azok, akik áprilisban ünnepelték az újévet, " +
                        " Franciaországban bolondnak számítottak, így tréfák céltáblájává " +
                        " váltak. Hasonló szokások figyelhetők meg a kelta tavaszi ünnepekben és az " +
                        " ókori római Hilaria ünnepén is, ahol a bolondos viselkedés a tavaszi " +
                        " megújulást jelezte. "),
                new FeastDay(2030, 3, 9, "Virágvasárnap", " A Virágvasárnap a húsvét előtti vasárnap, Jézus jeruzsálemi " +
                        " bevonulásának emlékére van. Az evangéliumok szerint Jézus szamárháton " +
                        " vonult be a városba, miközben a hívek pálmaágakat lengettek, üdvözölve " +
                        " őt, mint a Megváltót. A virágok, a pálmák és a zöld ágak használata " +
                        " az ünnep jelképei, melyek a békét és az üdvözítőt hirdetik. A " +
                        " keresztény hagyományban Virágvasárnap a nagyböjt végét és a húsvéti " +
                        " ünnepekre való lelki felkészülést jelzi. Az ünnep hivatalosan a 4. " +
                        " század óta van jelen az egyházi naptárban és azóta része a keresztény " +
                        " hagyományoknak világszerte."),
                new FeastDay(2030, 3, 14, "Nagypéntek", "A Nagypéntek a keresztény húsvéti ünnepkör legfontosabb napja, amikor " +
                        " Jézus Krisztus kereszthalálát és szenvedését emlékezzük meg. Az ünnep " +
                        " célja, hogy a hívők átgondolják a megváltásra tett áldozat " +
                        " jelentőségét. E napot a 4. század óta ünneplik, amikor a keresztény " +
                        " egyház hivatalosan is elismerte Jézus kereszthalálának fontosságát. " +
                        " Magyarországon a 20. század elejétől munkaszüneti nap. A keresztény " +
                        " hagyományokban sötét napként van jelen, amikor sokan böjtölnek és " +
                        " csendes megemlékezéseket tartanak. Az egyházi szertartások mellett a " +
                        " keresztény közösség számára Nagypéntek a bűnbánat és a lelki " +
                        " megújulás napja is.  Nagypénteket a húsvét előtti pénteken ünneplik, " +
                        " tehát ez a dátum évente változik.  " +
                        " "),
                new FeastDay(2030, 3, 16, "Húsvétvasárnap", "Húsvétvasárnap a kereszténység legfontosabb ünnepe, Jézus Krisztus " +
                        " feltámadásának napja, amely a halál feletti győzelmet és az örök élet " +
                        " ígéretét hirdeti. Magyarországon 1991 óta munkaszüneti nap, de " +
                        " jelentősége évszázadok óta meghatározó. Húsvét a 40 napos nagyböjt " +
                        " végét jelzi, amely a bűnbánat és a lelki megújulás időszaka. A gyerekek " +
                        " az ajándékaikat hagyományosan ezen a napon kapják meg, édességeket és " +
                        " tojásokat, amelyeket a húsvéti nyúl hoz.  A húsvéti nyúl a 16. századi " +
                        " Németországból ered és a termékenység, valamint az újjászületés " +
                        " jelképe. Eredete pogány hagyományokhoz köthető, ahol a nyúl és a tojás " +
                        " a tavaszi újjászületés szimbóluma volt, melyek később beépültek a " +
                        " keresztény húsvéti szokásokba. " +
                        " A Húsvétot évente változó dátumon ünneplik, mivel a tavaszi " +
                        " napéjegyenlőség utáni első teliholdat követő vasárnapra esik."),
                new FeastDay(2030, 3, 17, "Húsvéthétfő", "Húsvéthétfő a húsvéti ünnepkör egyik legfontosabb napja, amely Jézus " +
                        " Krisztus feltámadását követően az új élet és a megújulás " +
                        " szimbólumává vált. Magyarországon a húsvéti locsolás hagyománya a 17. " +
                        " századtól terjedt el. A férfiak vízzel vagy illatos kölnivel locsolják " +
                        " meg a nőket, kívánva nekik egészséget, boldogságot és termékenységet. " +
                        " A locsolás eredete valószínűleg a megtisztulás és a tavaszi megújulás " +
                        " ősi hiedelmeiből származik. A húsvéti nyúl a tavasz, az új élet és a " +
                        " termékenység jelképe, míg a bárány Krisztus áldozatát, ártatlanságát " +
                        " és a megváltást szimbolizálja. Magyarországon a húsvétot a " +
                        " kereszténység elterjedése óta ünneplik és a népszokások a 19. " +
                        " századtól erősödtek meg."),
                new FeastDay(2030, 3, 22, "A Föld napja", "A Föld napja 1970-ben indult el, amikor az Egyesült Államokban Gaylord " +
                        " Nelson szenátor kezdeményezésére több millió ember gyűlt össze, hogy " +
                        " felhívja a figyelmet a környezetszennyezésre és a természeti erőforrások " +
                        " védelmére. A nap célja, hogy globálisan tudatosítsa az emberekben a " +
                        " környezetvédelem fontosságát és ösztönözze a fenntartható életmódra " +
                        " való áttérést. Azóta minden évben április 22.-én világszerte " +
                        " megemlékeznek róla. Számos esemény, akció és kampány hívja fel a " +
                        " figyelmet a bolygó védelmére és a környezeti problémák megoldására. " +
                        "  " +
                        "  " +
                        " "),
                new FeastDay(2030, 3, 30, "Májusfa állítás", "A májusfa felállításának legjellemzőbb hagyomány szerinti időpontja a " +
                        " május 1-jét megelőző nap délután/este volt.  " +
                        " Magyarországon a középkor óta ismert, tehát legalább a 14. századtól " +
                        " ünneplik. A szokás az ősi tavaszi rituálékra épül, melyek a természet " +
                        " megújulását és az élet körforgását szimbolizálták. A fiatal legények " +
                        " a lányok háza elé állították a feldíszített fákat, hogy kifejezzék " +
                        " szerelmi szándékukat. A májusfát szalagokkal, virágokkal díszítették " +
                        " és május végén vagy pünkösdkor táncos mulatság keretében bontották " +
                        " le, amit kitáncolásnak neveztek. A hagyomány a szerelem és a termékenység " +
                        " ünnepe volt, később pedig összefonódott a munka ünnepével (május 1.). " +
                        " Mára már kevésbé elterjedt szokás, de sok helyen igyekeznek még " +
                        " fenntartani."),
                new FeastDay(2030, 4, 1, "A Munka ünnepe", "Május 1.-e a munka ünnepe. A munkások jogaiért és szolidaritásáért " +
                        " folytatott küzdelem emléknapja világszerte. Az ünnep gyökerei az 1886-os " +
                        " chicagói tüntetésekhez nyúlnak vissza, ahol a munkások a nyolcórás " +
                        " munkaidő bevezetéséért demonstráltak. Az események hatására 1890-től " +
                        " május 1.-ét a munkások nemzetközi szolidaritási napjává " +
                        " nyilvánították.  Magyarországon először 1890-ben tartották meg, de " +
                        " hivatalos állami ünnep csak 1946-ban lett. A szocializmus idején " +
                        " nagyszabású felvonulásokkal és tömegrendezvényekkel ünnepelték. A " +
                        " rendszerváltás után a jelentése átalakult, de továbbra is a munka " +
                        " tiszteletét és a munkavállalók jogainak fontosságát hirdeti. "),
                new FeastDay(2030, 4, 7, "Anyák napja", "Az édesanyák napja a szeretet, a tisztelet és a hála ünnepe, amelyet " +
                        " világszerte az édesanyák tiszteletére tartanak. Gyökerei az ókori " +
                        " Görögországba nyúlnak vissza, ahol Rheát, az istenek anyját ünnepelték. " +
                        " A modern anyák napja az Egyesült Államokból indult 1908-ban, amikor Anna " +
                        " Jarvis kampányt indított édesanyja emlékére. Magyarországon először " +
                        " 1925-ben ünnepelték és a májusi Mária-tisztelet hagyományaival is " +
                        " összekapcsolódott. Ez a nap a gondoskodó és szerető édesanyák, valamint " +
                        " minden női gondviselő iránti megbecsülés kifejezésére szolgál. " +
                        " Virágokkal, ajándékokkal és figyelmességgel fejezik ki hálájukat a " +
                        " gyerekek. Bár világszerte eltérő időpontokban ünneplik, mindenhol az " +
                        " anyák iránti szeretet és tisztelet áll a középpontban. Anyák napját " +
                        " Magyarországon minden év május első vasárnapján ünneplik.  " +
                        " "),
                new FeastDay(2030, 4, 28, "Gyermeknap", "A gyermeknap a gyerekek jogainak és jólétének fontosságát hirdető " +
                        " ünnep, melyet világszerte különböző időpontokban tartanak. Eredete " +
                        " 1925-re nyúlik vissza, amikor a genfi Gyermekjóléti Konferencián " +
                        " elhatározták, hogy kiemelik a gyermekek védelmének fontosságát. " +
                        " Magyarországon 1931-ben tartották meg először, majd 1950 óta május " +
                        " utolsó vasárnapján ünneplik. Ez a nap a gyerekekről szól, akik ilyenkor " +
                        " különféle programokon vehetnek részt, ahol játékokkal és szórakoztató " +
                        " rendezvényekkel ünneplik őket. Az ünnep célja, hogy felhívja a figyelmet " +
                        " a gyermekek jogaira és boldogságuk fontosságára. Magyarországon a " +
                        " Gyermeknapot minden évben május utolsó vasárnapján ünneplik."),
                new FeastDay(2030, 5, 4, "Pünkösd", "Pünkösd a kereszténység egyik legősibb ünnepe, melyet már a 4. " +
                        " századtól kezdve ünnepelnek a keresztény egyházban. Az ünnep Jézus " +
                        " mennybemenetele után ötven nappal emlékezik meg a Szentlélek " +
                        " eljöveteléről, amely erőt adott az apostoloknak a keresztény hit " +
                        " hirdetéséhez. Ez az esemény az egyház születésnapjaként is ismert. A " +
                        " „pünkösd” elnevezés a görög „pentékoszté” szóból ered, ami " +
                        " ötvenediket jelent. Magyarországon a középkor óta tartják számon és " +
                        " számos népi hagyomány kapcsolódik hozzá, például a pünkösdi király " +
                        " és királyné választása. Pünkösd vasárnapját a húsvét utáni 50. " +
                        " napon, tehát 50 nappal húsvétvasárnap után ünneplik. "),
                new FeastDay(2030, 5, 5, "Pünkösdhétfő", "A Pünkösdhétfő a keresztény egyház egyik legfontosabb ünnepének, a " +
                        " pünkösdnek a második napja. Pünkösd a húsvét utáni 50. napon van, és a " +
                        " Szentlélek eljövetelét ünnepli, amikor Jézus tanítványai erőt és " +
                        " útmutatást kaptak küldetésükhöz. Az esemény a keresztény egyház " +
                        " születésnapjának is tekinthető. Az ünnep gyökerei a zsidó pünkösdi " +
                        " ünnepre (Sávuót) vezethetők vissza, amely az aratási hálaadás napja " +
                        " volt. Magyarországon pünkösdhétfő munkaszüneti nap és hivatalosan 1993 " +
                        " óta ünnepeljük újra. Az ünnep a megújulást és a közösség " +
                        " összetartozását jelképezi."),
                new FeastDay(2030, 5, 18, "Apák napja", "Apák napja világszerte az édesapák tiszteletére szolgál, elismerve a " +
                        " családban és a gyermeknevelésben betöltött szerepüket. Az ünnep eredete " +
                        " az Egyesült Államokhoz köthető, ahol először 1910-ben tartották meg " +
                        " Sonora Smart Dodd kezdeményezésére , aki édesapját akarta tiszteletben " +
                        " részesíteni . Magyarországon az apák napja az 1990-es években kezdett " +
                        " elterjedni  és 1994 óta ünnepeljük minden év június harmadik " +
                        " vasárnapján. Célja, hogy elismerje az apák és az apaszerepet betöltő " +
                        " férfiak szeretetét, áldozatvállalását és a családban betöltött " +
                        " szerepét. Ez a nap lehetőséget ad arra, hogy köszönetet mondjunk az apák " +
                        " szeretetéért, támogatásáért és áldozatos munkájáért, amelyet " +
                        " családjukért tesznek. "),
                new FeastDay(2030, 5, 20, "Nyári napforduló", "Az év leghosszabb napja.  " +
                        " Napkelte:  " +
                        " Napnyugta:  " +
                        " Világos időtartam:  " +
                        " A nyári napforduló a leghosszabb nap az évben, amikor a Nap a legmagasabbra " +
                        " emelkedik az égbolton. Ez a jelenség a Föld tengelyferdesége és a " +
                        " keringése miatt következik be. Az északi féltekén a napforduló " +
                        " környékén a Nap több mint 15 órán keresztül a horizont felett " +
                        " tartózkodik. Ezen a napon a Föld az északi féltekén éri el a leghosszabb " +
                        " nappali órákat, így ez a legrövidebb éjszaka."),
                new FeastDay(2030, 7, 20, "Szent István-nap", "Szent István király koronázása körülbelül 1000-ben történt, ezzel " +
                        " megalapozva a keresztény Magyarországot. 1083 augusztus 20.-án szentté " +
                        " avatták a Szent Péter-bazilikában. Az Árpád-korban az István-kultusz az " +
                        " uralkodó dinasztiát támogatta és egységet teremtett a népcsoportok " +
                        " között. 1686-ban XI. Ince pápa elrendelte, hogy az egész katolikus világ " +
                        " emlékezzen meg róla, majd 1771-ben Mária Terézia országos ünneppé tette. " +
                        " Az 1848–49-es forradalom és szabadságharc leverése után egy időre " +
                        " szünetelt az ünneplés, de 1891-ben Ferenc József munkaszüneti nappá " +
                        " nyilvánította. A két világháború között a nemzeti egység " +
                        " szimbólumává vált. 1950-től az új államrendszer ideológiája szerint " +
                        " alakították át az ünnep jelentését, majd 1991-ben ismét állami " +
                        " ünneppé nyilvánították."),
                new FeastDay(2030, 9, 23, "1956-os Forradalom", "Magyarország nemzeti ünnepe, amely az 1956-os forradalom és szabadságharc " +
                        " eseményeire emlékezik. Az ünnep hivatalosan 1989 október 23.-án vált " +
                        " nemzeti ünneppé a rendszerváltás után, amikor a forradalom hőseit " +
                        " ünnepelve tisztelegtek a szabadságért és a demokráciáért folytatott " +
                        " küzdelmük előtt. Az 1956-os forradalom a szovjet megszállás elleni " +
                        " népfelkelés volt, amely bár véresen végződött, alapvető szerepet " +
                        " játszott Magyarország függetlenségi törekvéseiben és a későbbi " +
                        " rendszerváltásban. Az október 23.-i események máig a szabadság és a " +
                        " nemzeti szuverenitás fontos szimbólumai."),
                new FeastDay(2030, 9, 29, "Óraátállítás", "Az őszi óraátállítás (a téli időszámítás kezdete) – (vasárnap), " +
                        " 3:00 → 2:00 " +
                        " Ekkor kell 1 órát visszaállítani az órákat. " +
                        " Az őszi óraátállítás célja, hogy reggel világosabb legyen, így a " +
                        " természetes fényt jobban kihasználjuk. Magyarországon először 1916-ban " +
                        " vezették be az óraátállítást energiatakarékossági okokból. Bár több " +
                        " alkalommal szünetelt, végül 1980-ban újra bevezették, hogy csökkentsék a " +
                        " villamosenergia-felhasználást. A téli időszámítás október utolsó " +
                        " vasárnapján kezdődik, amikor az órákat visszaállítjuk. Ezáltal reggel " +
                        " hamarabb világosodik, de este hamarabb sötétedik.  1996 óta " +
                        " kormányrendelet szabályozza az óraátállítást. " +
                        " "),
                new FeastDay(2030, 10, 1, "Mindenszentek", "Mindenszentek ünnepe november 1.-én van, amikor a katolikus egyház " +
                        " megemlékezik minden üdvözült lélekről, akik elnyerték a mennyországot, " +
                        " de nevüket nem tartják számon a naptárban. Az ünnep eredete a " +
                        " kereszténység előtti halotti kultuszokra is visszavezethető. Hivatalosan a " +
                        " 8. században III. Gergely pápa idején vezették be, aki a Szent " +
                        " Péter-bazilikában kápolnát szentelt minden szent tiszteletére. " +
                        " Magyarországon munkaszüneti nap. Sokan virágokkal és gyertyagyújtással " +
                        " tisztelegnek elhunyt szeretteik emléke előtt. Ez a nap a hit és a " +
                        " megemlékezés ünnepe."),
                new FeastDay(2030, 10, 2, "Halottak napja", "Halottak napja november 2.-án van, amikor a katolikus egyház megemlékezik az " +
                        " elhunyt hívekről. Az ünnep célja, hogy imádságokkal és jó " +
                        " cselekedetekkel segítsük a halottak lelki üdvét és tisztelegjünk " +
                        " szeretteink emléke előtt. Bár gyökerei a kereszténység előtti halotti " +
                        " kultuszokig nyúlnak vissza, mai formáját a 11. században Cluny-i Szent " +
                        " Odiló apát vezette be. Ő rendelte el, hogy a kolostorok imádkozzanak a " +
                        " tisztítótűzben szenvedő lelkekért. A gyertyagyújtás és a virágok " +
                        " elhelyezése máig az emlékezés fontos része. "),
                new FeastDay(2030, 11, 3, "Advent első napja", "Advent a karácsony előtti négyhetes időszak, mely Jézus Krisztus " +
                        " születésére és második eljövetelére való várakozást jelenti. Az " +
                        " advent származása a 4. századra nyúlik vissza, amikor bűnbánattal és " +
                        " lelki felkészüléssel várták a karácsonyt. Kezdetben hosszabb böjti " +
                        " időszak volt, de fokozatosan négyhetesre csökkent. Az adventi koszorú " +
                        " hagyománya a 19. században terjedt el, az örökzöld növények a " +
                        " halhatatlanságot és az újjászületést szimbolizálták. A négy gyertya a " +
                        " hitet, a reményt, az örömöt és a szeretetet jelképezi. Minden vasárnap " +
                        " egy újabb gyertya meggyújtásával közelebb kerülünk a karácsonyhoz.  " +
                        " Advent első vasárnapját a karácsonyt megelőző négy vasárnap egyikeként " +
                        " ünneplik. Ez a nap általában november végére vagy december elejére esik.  " +
                        " "),
                new FeastDay(2030, 11, 6, "Mikulás", " Mikuláskor december 5.-én éjszaka, vagy 6.-án reggel töltik meg a gyerekek " +
                        " csizmáit édességgel és játékokkal. Szent Miklós püspök emlék napja. A " +
                        " 4. században élt szent, aki a gyermekek, a hajósok és a szegények " +
                        " védőszentje lett, híres volt jótékonyságáról. Egy legenda szerint " +
                        " titokban segített egy szegény ember három lányán, akiket így férjhez " +
                        " tudott adni. " +
                        " Ebből a történetből született a hagyomány, hogy Szent Miklós éjjelente " +
                        " ajándékokat hoz a jó gyerekeknek. Ma már világszerte ismert a piros " +
                        " ruhás, fehér szakállú Mikulás.  A Mikulás-hagyományok országonként " +
                        " eltérőek. Egyes helyeken a Mikulás hozza az ajándékokat, máshol a " +
                        " Télapó. A lényeg azonban mindenhol ugyanaz: a szeretet az adakozás és a " +
                        " gyermeki öröm ünnepe"),
                new FeastDay(2030, 11, 13, "Luca napja", "Luca napja a keresztény és a népi hagyományok keveréke. Szent Luca a 3. " +
                        " századi vértanú, a látás védőszentje, de a népi hitvilágban a " +
                        " termékenység, a jólét és a védelem szimbóluma. A hagyomány szerint ezen " +
                        " a napon kezdődött a tél leghosszabb éjszakája, így Luca napja a fény és " +
                        " a sötétség határán áll. Ezen a napon a nők nem dolgoztak, hogy bőséges " +
                        " legyen a következő év termése. Szokás volt Luca-széket készíteni, " +
                        " búzát ültetni és jóslásokat végezni. Luca napja a megújulás és a " +
                        " remény ünnepe, a fény győzelme a sötétség felett és a következő év " +
                        " jólétének biztosítása. " +
                        " "),
                new FeastDay(2030, 11, 21, "Téli napforduló", "Az év legrövidebb napja nap.  " +
                        " Napkelte:  " +
                        " Napnyugta:  " +
                        " Világos időtartam:  " +
                        " A téli napforduló a legrövidebb nap az évben, amikor a Nap a " +
                        " legalacsonyabbra emelkedik az égbolton. Ez a jelenség a Föld " +
                        " tengelyferdesége és keringése miatt következik be. Az északi féltekén a " +
                        " napforduló környékén a Nap csak 8 órán keresztül tartózkodik a horizont " +
                        " felett, így ez a leghosszabb éjszaka. Ezen a napon kezdődik a nappali fény " +
                        " fokozatos növekedése, így a következő hónapokban egyre hosszabbak lesznek " +
                        " a nappalok."),
                new FeastDay(2030, 11, 24, "Szenteste", "Szenteste a karácsonyi ünnepkör egyik legfontosabb napja, amely Jézus " +
                        " Krisztus születésének előestéje. Bár Jézus pontos születési dátuma " +
                        " nem ismert, a keresztény egyház a 4. században december 25.-ét jelölte meg " +
                        " születésének napjaként, összekapcsolva azt a téli napfordulóval. " +
                        " December 24.-én este a családok összegyűlnek, közösen vacsoráznak, " +
                        " ajándékokat adnak egymásnak és karácsonyfát állítanak. Magyarországon " +
                        " a hagyomány szerint a Jézuska hozza az ajándékokat, ezzel is megemlékezve " +
                        " Jézus születésének csodájáról. A fenyőfa, amely a 16. századtól vált " +
                        " a karácsony szimbólumává, az örök életet jelképezi. Az ajándékozás a " +
                        " szeretet és a megbecsülés kifejezése, amely a három királyok " +
                        " ajándékozására utal. Karácsony, bár elsősorban keresztény ünnep, " +
                        " világszerte a szeretet, a béke és a család ünnepe lett."),
                new FeastDay(2030, 11, 25, "Karácsony", "A keresztények számára Jézus Krisztus születésének ünnepe, melyet a 4. " +
                        " század óta tartanak. Az ünnep a téli napfordulóhoz kapcsolódik, " +
                        " szimbolizálva a fény győzelmét a sötétség felett. Karácsony a remény, " +
                        " a szeretet és a megváltás ünnepe, amely a családi együttlét, az öröm " +
                        " és a vallási szertartások ideje. Karácsonyfa, betlehem és csillag a " +
                        " szimbólumok, amelyek Jézus születését, az örök életet és az " +
                        " útmutatást képviselik. Az ünneplés világszerte eltérő, de mindegyik " +
                        " kultúrában a szeretet és a béke fontosságát hangsúlyozza. Karácsony " +
                        " lehetőséget ad az emberi kapcsolatok és az élet értelmének " +
                        " átgondolására"),
                new FeastDay(2030, 11, 26, "Karácsony másnapja", "A keresztény hagyományban Szent István, az első vértanú emléknapja, " +
                        " azonban Magyarországon elsősorban Szent István királyra, az " +
                        " államalapítóra emlékeznek. A 4. századi eredetű ünnep során Jézus " +
                        " születése után következik Szent István napja, aki kulcsszerepet játszott " +
                        " a kereszténység elterjesztésében Magyarországon. Bár Szent István a 11. " +
                        " században élt, az egyház később tette december 26.-át emlékévé. " +
                        " Magyarországon nemzeti hősként is tisztelik, mivel ő alapította az " +
                        " államot. Ezen a napon az emberek szentmisére mennek, megemlékeznek " +
                        " királyukról és különböző ünnepi rendezvényeken is részt vesznek"),
                new FeastDay(2030, 11, 31, "Szilveszter", "A Szilveszter az év utolsó napja, amikor búcsút veszünk az óévtől és " +
                        " ünnepeljük az új év kezdetét. Az ünnep neve Szent Szilveszter pápától " +
                        " származik, aki 335 körül halt meg, de az újév ünneplésének hagyományai " +
                        " sokkal régebbre nyúlnak vissza. Szilveszter éjszakáján családok és " +
                        " barátok együtt ünnepelnek, vacsoráznak, tűzijátékot néznek és " +
                        " jókívánságokat mondanak. A tűzijáték és a zajkeltés az ősi " +
                        " hagyományokból ered. Az ünnep a megújulás, az összetartozás és a " +
                        " remény kifejezője, miközben gazdaságilag is fontos szerepet játszik a " +
                        " fogyasztói társadalomban. " +
                        "  " +
                        " Boldog Új Évet kívánok! Legyen tele örömmel, egészséggel és sikerrel. " +
                        " Hozzon az új év boldog pillanatokat, új lehetőségeket és boldog együtt " +
                        " töltött időt!"),
                new FeastDay(2031, 0, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma. " +
                        " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az " +
                        " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy " +
                        " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és " +
                        " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a " +
                        " mondás. Legyen ez a nap a változás első lépése. Boldogságot, " +
                        " egészséget és kitartást kívánok az új év minden pillanatára! "
                ),
                new FeastDay(2031, 0, 6, "Vízkereszt", " A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a " +
                        " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus " +
                        " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus " +
                        " megkeresztelkedésére és az első csodájára. A három királyok aranyat, " +
                        " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a " +
                        " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet " +
                        " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi " +
                        " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak " +
                        " házszenteléseket, Isten áldását kérve az otthonra és az új évre."),
                new FeastDay(2031, 1, 14, "Valentin nap", "A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint " +
                        " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században " +
                        " titokban esketett meg szerelmespárokat, mert II. Claudius császár " +
                        " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák. " +
                        " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az " +
                        " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a " +
                        " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal " +
                        " és üdvözlőkártyákkal ünneplik."),
                new FeastDay(2031, 2, 1, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja, " +
                        " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a " +
                        " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A " +
                        " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és " +
                        " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki " +
                        " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának " +
                        " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel, " +
                        " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére " +
                        " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A " +
                        " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap " +
                        " előtt tartanak. Általában február végére vagy március elejére esik."),
                new FeastDay(2031, 2, 8, "Nőnap", " Az első Nőnapot 1908 március 8.-án tartották. A női munkások " +
                        " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az " +
                        " ünnep célja a nők társadalmi, politikai és gazdasági jogainak " +
                        " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben " +
                        " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így " +
                        " 1911-ben már több európai országban is megünnepelték. Magyarországon " +
                        " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már " +
                        " a nők jogainak elismerésére és tiszteletére emelkedett."),
                new FeastDay(2031, 2, 15, "1848-as Forradalom", "Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a " +
                        " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti " +
                        " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a " +
                        " sajtószabadságért, a jobb életkörülményekért és a függetlenségért. " +
                        " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című " +
                        " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet " +
                        " függetlenségi törekvése a magyar történelem egyik legfontosabb " +
                        " eseményévé vált. Az események hatására megerősödött a nemzeti " +
                        " identitás és ma már a magyar szabadság és összetartozás napjaként " +
                        " ünnepeljük. " +
                        " "),
                new FeastDay(2031, 2, 26, "Óraátállítás", " A tavaszi óraátállítás a nyári időszámítás kezdete.  " +
                        " A tavaszi óraátállításkor az órákat hajnali 2:00-ról 3:00-ra állítjuk " +
                        " előre, hogy jobban kihasználjuk a nappali világosságot és csökkentsük a " +
                        " mesterséges világítás szükségességét. Magyarországon először " +
                        " 1916-ban az első világháború alatt vezették be az energiatakarékosság " +
                        " érdekében, de a háború után megszüntették. 1954 és 1957 között újra " +
                        " alkalmazták, mert az ipari munkahelyeken az esti energiafogyasztás " +
                        " csökkentésére volt szükség, de 1958-ban a lakossági ellenállás és az " +
                        " alacsony megtakarítás miatt eltörölték. 1980-ban ismét bevezették a " +
                        " villamosenergia-megtakarítás érdekében. Azóta évente kétszer állítjuk " +
                        " át az órákat és 1996 óta kormányrendelet szabályozza az " +
                        " óraátállítást Magyarországon."),
                new FeastDay(2031, 3, 1, "Bolondok napja", "Április elseje a bolondok napja, a bolondos tréfák és ártalmatlan " +
                        " átverések napja. Az emberek apró csínyekkel, például hamis hírekkel vagy " +
                        " kitalált történetekkel szórakoztatják egymást. Az internet " +
                        " elterjedésével az álhírek és vírusos videók is népszerűvé váltak. A " +
                        " nap célja, hogy jókedvet és nevetést hozzon, miközben erősíti a " +
                        " közösségi szellemet. Eredete több elméletre vezethető vissza. A " +
                        " legnépszerűbb magyarázat szerint a XVI. században, a Gergely-naptár " +
                        " bevezetésekor azok, akik áprilisban ünnepelték az újévet, " +
                        " Franciaországban bolondnak számítottak, így tréfák céltáblájává " +
                        " váltak. Hasonló szokások figyelhetők meg a kelta tavaszi ünnepekben és az " +
                        " ókori római Hilaria ünnepén is, ahol a bolondos viselkedés a tavaszi " +
                        " megújulást jelezte. "),
                new FeastDay(2031, 3, 9, "Virágvasárnap", " A Virágvasárnap a húsvét előtti vasárnap, Jézus jeruzsálemi " +
                        " bevonulásának emlékére van. Az evangéliumok szerint Jézus szamárháton " +
                        " vonult be a városba, miközben a hívek pálmaágakat lengettek, üdvözölve " +
                        " őt, mint a Megváltót. A virágok, a pálmák és a zöld ágak használata " +
                        " az ünnep jelképei, melyek a békét és az üdvözítőt hirdetik. A " +
                        " keresztény hagyományban Virágvasárnap a nagyböjt végét és a húsvéti " +
                        " ünnepekre való lelki felkészülést jelzi. Az ünnep hivatalosan a 4. " +
                        " század óta van jelen az egyházi naptárban és azóta része a keresztény " +
                        " hagyományoknak világszerte."),
                new FeastDay(2031, 3, 14, "Nagypéntek", "A Nagypéntek a keresztény húsvéti ünnepkör legfontosabb napja, amikor " +
                        " Jézus Krisztus kereszthalálát és szenvedését emlékezzük meg. Az ünnep " +
                        " célja, hogy a hívők átgondolják a megváltásra tett áldozat " +
                        " jelentőségét. E napot a 4. század óta ünneplik, amikor a keresztény " +
                        " egyház hivatalosan is elismerte Jézus kereszthalálának fontosságát. " +
                        " Magyarországon a 20. század elejétől munkaszüneti nap. A keresztény " +
                        " hagyományokban sötét napként van jelen, amikor sokan böjtölnek és " +
                        " csendes megemlékezéseket tartanak. Az egyházi szertartások mellett a " +
                        " keresztény közösség számára Nagypéntek a bűnbánat és a lelki " +
                        " megújulás napja is.  Nagypénteket a húsvét előtti pénteken ünneplik, " +
                        " tehát ez a dátum évente változik.  " +
                        " "),
                new FeastDay(2031, 3, 16, "Húsvétvasárnap", "Húsvétvasárnap a kereszténység legfontosabb ünnepe, Jézus Krisztus " +
                        " feltámadásának napja, amely a halál feletti győzelmet és az örök élet " +
                        " ígéretét hirdeti. Magyarországon 1991 óta munkaszüneti nap, de " +
                        " jelentősége évszázadok óta meghatározó. Húsvét a 40 napos nagyböjt " +
                        " végét jelzi, amely a bűnbánat és a lelki megújulás időszaka. A gyerekek " +
                        " az ajándékaikat hagyományosan ezen a napon kapják meg, édességeket és " +
                        " tojásokat, amelyeket a húsvéti nyúl hoz.  A húsvéti nyúl a 16. századi " +
                        " Németországból ered és a termékenység, valamint az újjászületés " +
                        " jelképe. Eredete pogány hagyományokhoz köthető, ahol a nyúl és a tojás " +
                        " a tavaszi újjászületés szimbóluma volt, melyek később beépültek a " +
                        " keresztény húsvéti szokásokba. " +
                        " A Húsvétot évente változó dátumon ünneplik, mivel a tavaszi " +
                        " napéjegyenlőség utáni első teliholdat követő vasárnapra esik."),
                new FeastDay(2031, 3, 17, "Húsvéthétfő", "Húsvéthétfő a húsvéti ünnepkör egyik legfontosabb napja, amely Jézus " +
                        " Krisztus feltámadását követően az új élet és a megújulás " +
                        " szimbólumává vált. Magyarországon a húsvéti locsolás hagyománya a 17. " +
                        " századtól terjedt el. A férfiak vízzel vagy illatos kölnivel locsolják " +
                        " meg a nőket, kívánva nekik egészséget, boldogságot és termékenységet. " +
                        " A locsolás eredete valószínűleg a megtisztulás és a tavaszi megújulás " +
                        " ősi hiedelmeiből származik. A húsvéti nyúl a tavasz, az új élet és a " +
                        " termékenység jelképe, míg a bárány Krisztus áldozatát, ártatlanságát " +
                        " és a megváltást szimbolizálja. Magyarországon a húsvétot a " +
                        " kereszténység elterjedése óta ünneplik és a népszokások a 19. " +
                        " századtól erősödtek meg."),
                new FeastDay(2031, 3, 22, "A Föld napja", "A Föld napja 1970-ben indult el, amikor az Egyesült Államokban Gaylord " +
                        " Nelson szenátor kezdeményezésére több millió ember gyűlt össze, hogy " +
                        " felhívja a figyelmet a környezetszennyezésre és a természeti erőforrások " +
                        " védelmére. A nap célja, hogy globálisan tudatosítsa az emberekben a " +
                        " környezetvédelem fontosságát és ösztönözze a fenntartható életmódra " +
                        " való áttérést. Azóta minden évben április 22.-én világszerte " +
                        " megemlékeznek róla. Számos esemény, akció és kampány hívja fel a " +
                        " figyelmet a bolygó védelmére és a környezeti problémák megoldására. " +
                        "  " +
                        "  " +
                        " "),
                new FeastDay(2031, 3, 30, "Májusfa állítás", "A májusfa felállításának legjellemzőbb hagyomány szerinti időpontja a " +
                        " május 1-jét megelőző nap délután/este volt.  " +
                        " Magyarországon a középkor óta ismert, tehát legalább a 14. századtól " +
                        " ünneplik. A szokás az ősi tavaszi rituálékra épül, melyek a természet " +
                        " megújulását és az élet körforgását szimbolizálták. A fiatal legények " +
                        " a lányok háza elé állították a feldíszített fákat, hogy kifejezzék " +
                        " szerelmi szándékukat. A májusfát szalagokkal, virágokkal díszítették " +
                        " és május végén vagy pünkösdkor táncos mulatság keretében bontották " +
                        " le, amit kitáncolásnak neveztek. A hagyomány a szerelem és a termékenység " +
                        " ünnepe volt, később pedig összefonódott a munka ünnepével (május 1.). " +
                        " Mára már kevésbé elterjedt szokás, de sok helyen igyekeznek még " +
                        " fenntartani."),
                new FeastDay(2031, 4, 1, "A Munka ünnepe", "Május 1.-e a munka ünnepe. A munkások jogaiért és szolidaritásáért " +
                        " folytatott küzdelem emléknapja világszerte. Az ünnep gyökerei az 1886-os " +
                        " chicagói tüntetésekhez nyúlnak vissza, ahol a munkások a nyolcórás " +
                        " munkaidő bevezetéséért demonstráltak. Az események hatására 1890-től " +
                        " május 1.-ét a munkások nemzetközi szolidaritási napjává " +
                        " nyilvánították.  Magyarországon először 1890-ben tartották meg, de " +
                        " hivatalos állami ünnep csak 1946-ban lett. A szocializmus idején " +
                        " nagyszabású felvonulásokkal és tömegrendezvényekkel ünnepelték. A " +
                        " rendszerváltás után a jelentése átalakult, de továbbra is a munka " +
                        " tiszteletét és a munkavállalók jogainak fontosságát hirdeti. "),
                new FeastDay(2031, 4, 7, "Anyák napja", "Az édesanyák napja a szeretet, a tisztelet és a hála ünnepe, amelyet " +
                        " világszerte az édesanyák tiszteletére tartanak. Gyökerei az ókori " +
                        " Görögországba nyúlnak vissza, ahol Rheát, az istenek anyját ünnepelték. " +
                        " A modern anyák napja az Egyesült Államokból indult 1908-ban, amikor Anna " +
                        " Jarvis kampányt indított édesanyja emlékére. Magyarországon először " +
                        " 1925-ben ünnepelték és a májusi Mária-tisztelet hagyományaival is " +
                        " összekapcsolódott. Ez a nap a gondoskodó és szerető édesanyák, valamint " +
                        " minden női gondviselő iránti megbecsülés kifejezésére szolgál. " +
                        " Virágokkal, ajándékokkal és figyelmességgel fejezik ki hálájukat a " +
                        " gyerekek. Bár világszerte eltérő időpontokban ünneplik, mindenhol az " +
                        " anyák iránti szeretet és tisztelet áll a középpontban. Anyák napját " +
                        " Magyarországon minden év május első vasárnapján ünneplik.  " +
                        " "),
                new FeastDay(2031, 4, 28, "Gyermeknap", "A gyermeknap a gyerekek jogainak és jólétének fontosságát hirdető " +
                        " ünnep, melyet világszerte különböző időpontokban tartanak. Eredete " +
                        " 1925-re nyúlik vissza, amikor a genfi Gyermekjóléti Konferencián " +
                        " elhatározták, hogy kiemelik a gyermekek védelmének fontosságát. " +
                        " Magyarországon 1931-ben tartották meg először, majd 1950 óta május " +
                        " utolsó vasárnapján ünneplik. Ez a nap a gyerekekről szól, akik ilyenkor " +
                        " különféle programokon vehetnek részt, ahol játékokkal és szórakoztató " +
                        " rendezvényekkel ünneplik őket. Az ünnep célja, hogy felhívja a figyelmet " +
                        " a gyermekek jogaira és boldogságuk fontosságára. Magyarországon a " +
                        " Gyermeknapot minden évben május utolsó vasárnapján ünneplik."),
                new FeastDay(2031, 5, 4, "Pünkösd", "Pünkösd a kereszténység egyik legősibb ünnepe, melyet már a 4. " +
                        " századtól kezdve ünnepelnek a keresztény egyházban. Az ünnep Jézus " +
                        " mennybemenetele után ötven nappal emlékezik meg a Szentlélek " +
                        " eljöveteléről, amely erőt adott az apostoloknak a keresztény hit " +
                        " hirdetéséhez. Ez az esemény az egyház születésnapjaként is ismert. A " +
                        " „pünkösd” elnevezés a görög „pentékoszté” szóból ered, ami " +
                        " ötvenediket jelent. Magyarországon a középkor óta tartják számon és " +
                        " számos népi hagyomány kapcsolódik hozzá, például a pünkösdi király " +
                        " és királyné választása. Pünkösd vasárnapját a húsvét utáni 50. " +
                        " napon, tehát 50 nappal húsvétvasárnap után ünneplik. "),
                new FeastDay(2031, 5, 5, "Pünkösdhétfő", "A Pünkösdhétfő a keresztény egyház egyik legfontosabb ünnepének, a " +
                        " pünkösdnek a második napja. Pünkösd a húsvét utáni 50. napon van, és a " +
                        " Szentlélek eljövetelét ünnepli, amikor Jézus tanítványai erőt és " +
                        " útmutatást kaptak küldetésükhöz. Az esemény a keresztény egyház " +
                        " születésnapjának is tekinthető. Az ünnep gyökerei a zsidó pünkösdi " +
                        " ünnepre (Sávuót) vezethetők vissza, amely az aratási hálaadás napja " +
                        " volt. Magyarországon pünkösdhétfő munkaszüneti nap és hivatalosan 1993 " +
                        " óta ünnepeljük újra. Az ünnep a megújulást és a közösség " +
                        " összetartozását jelképezi."),
                new FeastDay(2031, 5, 18, "Apák napja", "Apák napja világszerte az édesapák tiszteletére szolgál, elismerve a " +
                        " családban és a gyermeknevelésben betöltött szerepüket. Az ünnep eredete " +
                        " az Egyesült Államokhoz köthető, ahol először 1910-ben tartották meg " +
                        " Sonora Smart Dodd kezdeményezésére , aki édesapját akarta tiszteletben " +
                        " részesíteni . Magyarországon az apák napja az 1990-es években kezdett " +
                        " elterjedni  és 1994 óta ünnepeljük minden év június harmadik " +
                        " vasárnapján. Célja, hogy elismerje az apák és az apaszerepet betöltő " +
                        " férfiak szeretetét, áldozatvállalását és a családban betöltött " +
                        " szerepét. Ez a nap lehetőséget ad arra, hogy köszönetet mondjunk az apák " +
                        " szeretetéért, támogatásáért és áldozatos munkájáért, amelyet " +
                        " családjukért tesznek. "),
                new FeastDay(2031, 5, 20, "Nyári napforduló", "Az év leghosszabb napja.  " +
                        " Napkelte:  " +
                        " Napnyugta:  " +
                        " Világos időtartam:  " +
                        " A nyári napforduló a leghosszabb nap az évben, amikor a Nap a legmagasabbra " +
                        " emelkedik az égbolton. Ez a jelenség a Föld tengelyferdesége és a " +
                        " keringése miatt következik be. Az északi féltekén a napforduló " +
                        " környékén a Nap több mint 15 órán keresztül a horizont felett " +
                        " tartózkodik. Ezen a napon a Föld az északi féltekén éri el a leghosszabb " +
                        " nappali órákat, így ez a legrövidebb éjszaka."),
                new FeastDay(2031, 7, 20, "Szent István-nap", "Szent István király koronázása körülbelül 1000-ben történt, ezzel " +
                        " megalapozva a keresztény Magyarországot. 1083 augusztus 20.-án szentté " +
                        " avatták a Szent Péter-bazilikában. Az Árpád-korban az István-kultusz az " +
                        " uralkodó dinasztiát támogatta és egységet teremtett a népcsoportok " +
                        " között. 1686-ban XI. Ince pápa elrendelte, hogy az egész katolikus világ " +
                        " emlékezzen meg róla, majd 1771-ben Mária Terézia országos ünneppé tette. " +
                        " Az 1848–49-es forradalom és szabadságharc leverése után egy időre " +
                        " szünetelt az ünneplés, de 1891-ben Ferenc József munkaszüneti nappá " +
                        " nyilvánította. A két világháború között a nemzeti egység " +
                        " szimbólumává vált. 1950-től az új államrendszer ideológiája szerint " +
                        " alakították át az ünnep jelentését, majd 1991-ben ismét állami " +
                        " ünneppé nyilvánították."),
                new FeastDay(2031, 9, 23, "1956-os Forradalom", "Magyarország nemzeti ünnepe, amely az 1956-os forradalom és szabadságharc " +
                        " eseményeire emlékezik. Az ünnep hivatalosan 1989 október 23.-án vált " +
                        " nemzeti ünneppé a rendszerváltás után, amikor a forradalom hőseit " +
                        " ünnepelve tisztelegtek a szabadságért és a demokráciáért folytatott " +
                        " küzdelmük előtt. Az 1956-os forradalom a szovjet megszállás elleni " +
                        " népfelkelés volt, amely bár véresen végződött, alapvető szerepet " +
                        " játszott Magyarország függetlenségi törekvéseiben és a későbbi " +
                        " rendszerváltásban. Az október 23.-i események máig a szabadság és a " +
                        " nemzeti szuverenitás fontos szimbólumai."),
                new FeastDay(2031, 9, 29, "Óraátállítás", "Az őszi óraátállítás (a téli időszámítás kezdete) – (vasárnap), " +
                        " 3:00 → 2:00 " +
                        " Ekkor kell 1 órát visszaállítani az órákat. " +
                        " Az őszi óraátállítás célja, hogy reggel világosabb legyen, így a " +
                        " természetes fényt jobban kihasználjuk. Magyarországon először 1916-ban " +
                        " vezették be az óraátállítást energiatakarékossági okokból. Bár több " +
                        " alkalommal szünetelt, végül 1980-ban újra bevezették, hogy csökkentsék a " +
                        " villamosenergia-felhasználást. A téli időszámítás október utolsó " +
                        " vasárnapján kezdődik, amikor az órákat visszaállítjuk. Ezáltal reggel " +
                        " hamarabb világosodik, de este hamarabb sötétedik.  1996 óta " +
                        " kormányrendelet szabályozza az óraátállítást. " +
                        " "),
                new FeastDay(2031, 10, 1, "Mindenszentek", "Mindenszentek ünnepe november 1.-én van, amikor a katolikus egyház " +
                        " megemlékezik minden üdvözült lélekről, akik elnyerték a mennyországot, " +
                        " de nevüket nem tartják számon a naptárban. Az ünnep eredete a " +
                        " kereszténység előtti halotti kultuszokra is visszavezethető. Hivatalosan a " +
                        " 8. században III. Gergely pápa idején vezették be, aki a Szent " +
                        " Péter-bazilikában kápolnát szentelt minden szent tiszteletére. " +
                        " Magyarországon munkaszüneti nap. Sokan virágokkal és gyertyagyújtással " +
                        " tisztelegnek elhunyt szeretteik emléke előtt. Ez a nap a hit és a " +
                        " megemlékezés ünnepe."),
                new FeastDay(2031, 10, 2, "Halottak napja", "Halottak napja november 2.-án van, amikor a katolikus egyház megemlékezik az " +
                        " elhunyt hívekről. Az ünnep célja, hogy imádságokkal és jó " +
                        " cselekedetekkel segítsük a halottak lelki üdvét és tisztelegjünk " +
                        " szeretteink emléke előtt. Bár gyökerei a kereszténység előtti halotti " +
                        " kultuszokig nyúlnak vissza, mai formáját a 11. században Cluny-i Szent " +
                        " Odiló apát vezette be. Ő rendelte el, hogy a kolostorok imádkozzanak a " +
                        " tisztítótűzben szenvedő lelkekért. A gyertyagyújtás és a virágok " +
                        " elhelyezése máig az emlékezés fontos része. "),
                new FeastDay(2031, 11, 3, "Advent első napja", "Advent a karácsony előtti négyhetes időszak, mely Jézus Krisztus " +
                        " születésére és második eljövetelére való várakozást jelenti. Az " +
                        " advent származása a 4. századra nyúlik vissza, amikor bűnbánattal és " +
                        " lelki felkészüléssel várták a karácsonyt. Kezdetben hosszabb böjti " +
                        " időszak volt, de fokozatosan négyhetesre csökkent. Az adventi koszorú " +
                        " hagyománya a 19. században terjedt el, az örökzöld növények a " +
                        " halhatatlanságot és az újjászületést szimbolizálták. A négy gyertya a " +
                        " hitet, a reményt, az örömöt és a szeretetet jelképezi. Minden vasárnap " +
                        " egy újabb gyertya meggyújtásával közelebb kerülünk a karácsonyhoz.  " +
                        " Advent első vasárnapját a karácsonyt megelőző négy vasárnap egyikeként " +
                        " ünneplik. Ez a nap általában november végére vagy december elejére esik.  " +
                        " "),
                new FeastDay(2031, 11, 6, "Mikulás", " Mikuláskor december 5.-én éjszaka, vagy 6.-án reggel töltik meg a gyerekek " +
                        " csizmáit édességgel és játékokkal. Szent Miklós püspök emlék napja. A " +
                        " 4. században élt szent, aki a gyermekek, a hajósok és a szegények " +
                        " védőszentje lett, híres volt jótékonyságáról. Egy legenda szerint " +
                        " titokban segített egy szegény ember három lányán, akiket így férjhez " +
                        " tudott adni. " +
                        " Ebből a történetből született a hagyomány, hogy Szent Miklós éjjelente " +
                        " ajándékokat hoz a jó gyerekeknek. Ma már világszerte ismert a piros " +
                        " ruhás, fehér szakállú Mikulás.  A Mikulás-hagyományok országonként " +
                        " eltérőek. Egyes helyeken a Mikulás hozza az ajándékokat, máshol a " +
                        " Télapó. A lényeg azonban mindenhol ugyanaz: a szeretet az adakozás és a " +
                        " gyermeki öröm ünnepe"),
                new FeastDay(2031, 11, 13, "Luca napja", "Luca napja a keresztény és a népi hagyományok keveréke. Szent Luca a 3. " +
                        " századi vértanú, a látás védőszentje, de a népi hitvilágban a " +
                        " termékenység, a jólét és a védelem szimbóluma. A hagyomány szerint ezen " +
                        " a napon kezdődött a tél leghosszabb éjszakája, így Luca napja a fény és " +
                        " a sötétség határán áll. Ezen a napon a nők nem dolgoztak, hogy bőséges " +
                        " legyen a következő év termése. Szokás volt Luca-széket készíteni, " +
                        " búzát ültetni és jóslásokat végezni. Luca napja a megújulás és a " +
                        " remény ünnepe, a fény győzelme a sötétség felett és a következő év " +
                        " jólétének biztosítása. " +
                        " "),
                new FeastDay(2031, 11, 21, "Téli napforduló", "Az év legrövidebb napja nap.  " +
                        " Napkelte:  " +
                        " Napnyugta:  " +
                        " Világos időtartam:  " +
                        " A téli napforduló a legrövidebb nap az évben, amikor a Nap a " +
                        " legalacsonyabbra emelkedik az égbolton. Ez a jelenség a Föld " +
                        " tengelyferdesége és keringése miatt következik be. Az északi féltekén a " +
                        " napforduló környékén a Nap csak 8 órán keresztül tartózkodik a horizont " +
                        " felett, így ez a leghosszabb éjszaka. Ezen a napon kezdődik a nappali fény " +
                        " fokozatos növekedése, így a következő hónapokban egyre hosszabbak lesznek " +
                        " a nappalok."),
                new FeastDay(2031, 11, 24, "Szenteste", "Szenteste a karácsonyi ünnepkör egyik legfontosabb napja, amely Jézus " +
                        " Krisztus születésének előestéje. Bár Jézus pontos születési dátuma " +
                        " nem ismert, a keresztény egyház a 4. században december 25.-ét jelölte meg " +
                        " születésének napjaként, összekapcsolva azt a téli napfordulóval. " +
                        " December 24.-én este a családok összegyűlnek, közösen vacsoráznak, " +
                        " ajándékokat adnak egymásnak és karácsonyfát állítanak. Magyarországon " +
                        " a hagyomány szerint a Jézuska hozza az ajándékokat, ezzel is megemlékezve " +
                        " Jézus születésének csodájáról. A fenyőfa, amely a 16. századtól vált " +
                        " a karácsony szimbólumává, az örök életet jelképezi. Az ajándékozás a " +
                        " szeretet és a megbecsülés kifejezése, amely a három királyok " +
                        " ajándékozására utal. Karácsony, bár elsősorban keresztény ünnep, " +
                        " világszerte a szeretet, a béke és a család ünnepe lett."),
                new FeastDay(2031, 11, 25, "Karácsony", "A keresztények számára Jézus Krisztus születésének ünnepe, melyet a 4. " +
                        " század óta tartanak. Az ünnep a téli napfordulóhoz kapcsolódik, " +
                        " szimbolizálva a fény győzelmét a sötétség felett. Karácsony a remény, " +
                        " a szeretet és a megváltás ünnepe, amely a családi együttlét, az öröm " +
                        " és a vallási szertartások ideje. Karácsonyfa, betlehem és csillag a " +
                        " szimbólumok, amelyek Jézus születését, az örök életet és az " +
                        " útmutatást képviselik. Az ünneplés világszerte eltérő, de mindegyik " +
                        " kultúrában a szeretet és a béke fontosságát hangsúlyozza. Karácsony " +
                        " lehetőséget ad az emberi kapcsolatok és az élet értelmének " +
                        " átgondolására"),
                new FeastDay(2031, 11, 26, "Karácsony másnapja", "A keresztény hagyományban Szent István, az első vértanú emléknapja, " +
                        " azonban Magyarországon elsősorban Szent István királyra, az " +
                        " államalapítóra emlékeznek. A 4. századi eredetű ünnep során Jézus " +
                        " születése után következik Szent István napja, aki kulcsszerepet játszott " +
                        " a kereszténység elterjesztésében Magyarországon. Bár Szent István a 11. " +
                        " században élt, az egyház később tette december 26.-át emlékévé. " +
                        " Magyarországon nemzeti hősként is tisztelik, mivel ő alapította az " +
                        " államot. Ezen a napon az emberek szentmisére mennek, megemlékeznek " +
                        " királyukról és különböző ünnepi rendezvényeken is részt vesznek"),
                new FeastDay(2031, 11, 31, "Szilveszter", "A Szilveszter az év utolsó napja, amikor búcsút veszünk az óévtől és " +
                        " ünnepeljük az új év kezdetét. Az ünnep neve Szent Szilveszter pápától " +
                        " származik, aki 335 körül halt meg, de az újév ünneplésének hagyományai " +
                        " sokkal régebbre nyúlnak vissza. Szilveszter éjszakáján családok és " +
                        " barátok együtt ünnepelnek, vacsoráznak, tűzijátékot néznek és " +
                        " jókívánságokat mondanak. A tűzijáték és a zajkeltés az ősi " +
                        " hagyományokból ered. Az ünnep a megújulás, az összetartozás és a " +
                        " remény kifejezője, miközben gazdaságilag is fontos szerepet játszik a " +
                        " fogyasztói társadalomban. " +
                        "  " +
                        " Boldog Új Évet kívánok! Legyen tele örömmel, egészséggel és sikerrel. " +
                        " Hozzon az új év boldog pillanatokat, új lehetőségeket és boldog együtt " +
                        " töltött időt!"),
                new FeastDay(2032, 0, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma. " +
                        " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az " +
                        " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy " +
                        " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és " +
                        " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a " +
                        " mondás. Legyen ez a nap a változás első lépése. Boldogságot, " +
                        " egészséget és kitartást kívánok az új év minden pillanatára! "
                ),
                new FeastDay(2032, 0, 6, "Vízkereszt", " A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a " +
                        " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus " +
                        " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus " +
                        " megkeresztelkedésére és az első csodájára. A három királyok aranyat, " +
                        " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a " +
                        " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet " +
                        " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi " +
                        " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak " +
                        " házszenteléseket, Isten áldását kérve az otthonra és az új évre."),
                new FeastDay(2032, 1, 14, "Valentin nap", "A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint " +
                        " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században " +
                        " titokban esketett meg szerelmespárokat, mert II. Claudius császár " +
                        " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák. " +
                        " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az " +
                        " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a " +
                        " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal " +
                        " és üdvözlőkártyákkal ünneplik."),
                new FeastDay(2032, 2, 1, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja, " +
                        " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a " +
                        " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A " +
                        " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és " +
                        " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki " +
                        " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának " +
                        " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel, " +
                        " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére " +
                        " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A " +
                        " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap " +
                        " előtt tartanak. Általában február végére vagy március elejére esik."),
                new FeastDay(2032, 2, 8, "Nőnap", " Az első Nőnapot 1908 március 8.-án tartották. A női munkások " +
                        " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az " +
                        " ünnep célja a nők társadalmi, politikai és gazdasági jogainak " +
                        " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben " +
                        " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így " +
                        " 1911-ben már több európai országban is megünnepelték. Magyarországon " +
                        " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már " +
                        " a nők jogainak elismerésére és tiszteletére emelkedett."),
                new FeastDay(2032, 2, 15, "1848-as Forradalom", "Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a " +
                        " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti " +
                        " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a " +
                        " sajtószabadságért, a jobb életkörülményekért és a függetlenségért. " +
                        " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című " +
                        " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet " +
                        " függetlenségi törekvése a magyar történelem egyik legfontosabb " +
                        " eseményévé vált. Az események hatására megerősödött a nemzeti " +
                        " identitás és ma már a magyar szabadság és összetartozás napjaként " +
                        " ünnepeljük. " +
                        " "),
                new FeastDay(2032, 2, 26, "Óraátállítás", " A tavaszi óraátállítás a nyári időszámítás kezdete.  " +
                        " A tavaszi óraátállításkor az órákat hajnali 2:00-ról 3:00-ra állítjuk " +
                        " előre, hogy jobban kihasználjuk a nappali világosságot és csökkentsük a " +
                        " mesterséges világítás szükségességét. Magyarországon először " +
                        " 1916-ban az első világháború alatt vezették be az energiatakarékosság " +
                        " érdekében, de a háború után megszüntették. 1954 és 1957 között újra " +
                        " alkalmazták, mert az ipari munkahelyeken az esti energiafogyasztás " +
                        " csökkentésére volt szükség, de 1958-ban a lakossági ellenállás és az " +
                        " alacsony megtakarítás miatt eltörölték. 1980-ban ismét bevezették a " +
                        " villamosenergia-megtakarítás érdekében. Azóta évente kétszer állítjuk " +
                        " át az órákat és 1996 óta kormányrendelet szabályozza az " +
                        " óraátállítást Magyarországon."),
                new FeastDay(2032, 3, 1, "Bolondok napja", "Április elseje a bolondok napja, a bolondos tréfák és ártalmatlan " +
                        " átverések napja. Az emberek apró csínyekkel, például hamis hírekkel vagy " +
                        " kitalált történetekkel szórakoztatják egymást. Az internet " +
                        " elterjedésével az álhírek és vírusos videók is népszerűvé váltak. A " +
                        " nap célja, hogy jókedvet és nevetést hozzon, miközben erősíti a " +
                        " közösségi szellemet. Eredete több elméletre vezethető vissza. A " +
                        " legnépszerűbb magyarázat szerint a XVI. században, a Gergely-naptár " +
                        " bevezetésekor azok, akik áprilisban ünnepelték az újévet, " +
                        " Franciaországban bolondnak számítottak, így tréfák céltáblájává " +
                        " váltak. Hasonló szokások figyelhetők meg a kelta tavaszi ünnepekben és az " +
                        " ókori római Hilaria ünnepén is, ahol a bolondos viselkedés a tavaszi " +
                        " megújulást jelezte. "),
                new FeastDay(2032, 3, 9, "Virágvasárnap", " A Virágvasárnap a húsvét előtti vasárnap, Jézus jeruzsálemi " +
                        " bevonulásának emlékére van. Az evangéliumok szerint Jézus szamárháton " +
                        " vonult be a városba, miközben a hívek pálmaágakat lengettek, üdvözölve " +
                        " őt, mint a Megváltót. A virágok, a pálmák és a zöld ágak használata " +
                        " az ünnep jelképei, melyek a békét és az üdvözítőt hirdetik. A " +
                        " keresztény hagyományban Virágvasárnap a nagyböjt végét és a húsvéti " +
                        " ünnepekre való lelki felkészülést jelzi. Az ünnep hivatalosan a 4. " +
                        " század óta van jelen az egyházi naptárban és azóta része a keresztény " +
                        " hagyományoknak világszerte."),
                new FeastDay(2032, 3, 14, "Nagypéntek", "A Nagypéntek a keresztény húsvéti ünnepkör legfontosabb napja, amikor " +
                        " Jézus Krisztus kereszthalálát és szenvedését emlékezzük meg. Az ünnep " +
                        " célja, hogy a hívők átgondolják a megváltásra tett áldozat " +
                        " jelentőségét. E napot a 4. század óta ünneplik, amikor a keresztény " +
                        " egyház hivatalosan is elismerte Jézus kereszthalálának fontosságát. " +
                        " Magyarországon a 20. század elejétől munkaszüneti nap. A keresztény " +
                        " hagyományokban sötét napként van jelen, amikor sokan böjtölnek és " +
                        " csendes megemlékezéseket tartanak. Az egyházi szertartások mellett a " +
                        " keresztény közösség számára Nagypéntek a bűnbánat és a lelki " +
                        " megújulás napja is.  Nagypénteket a húsvét előtti pénteken ünneplik, " +
                        " tehát ez a dátum évente változik.  " +
                        " "),
                new FeastDay(2032, 3, 16, "Húsvétvasárnap", "Húsvétvasárnap a kereszténység legfontosabb ünnepe, Jézus Krisztus " +
                        " feltámadásának napja, amely a halál feletti győzelmet és az örök élet " +
                        " ígéretét hirdeti. Magyarországon 1991 óta munkaszüneti nap, de " +
                        " jelentősége évszázadok óta meghatározó. Húsvét a 40 napos nagyböjt " +
                        " végét jelzi, amely a bűnbánat és a lelki megújulás időszaka. A gyerekek " +
                        " az ajándékaikat hagyományosan ezen a napon kapják meg, édességeket és " +
                        " tojásokat, amelyeket a húsvéti nyúl hoz.  A húsvéti nyúl a 16. századi " +
                        " Németországból ered és a termékenység, valamint az újjászületés " +
                        " jelképe. Eredete pogány hagyományokhoz köthető, ahol a nyúl és a tojás " +
                        " a tavaszi újjászületés szimbóluma volt, melyek később beépültek a " +
                        " keresztény húsvéti szokásokba. " +
                        " A Húsvétot évente változó dátumon ünneplik, mivel a tavaszi " +
                        " napéjegyenlőség utáni első teliholdat követő vasárnapra esik."),
                new FeastDay(2032, 3, 17, "Húsvéthétfő", "Húsvéthétfő a húsvéti ünnepkör egyik legfontosabb napja, amely Jézus " +
                        " Krisztus feltámadását követően az új élet és a megújulás " +
                        " szimbólumává vált. Magyarországon a húsvéti locsolás hagyománya a 17. " +
                        " századtól terjedt el. A férfiak vízzel vagy illatos kölnivel locsolják " +
                        " meg a nőket, kívánva nekik egészséget, boldogságot és termékenységet. " +
                        " A locsolás eredete valószínűleg a megtisztulás és a tavaszi megújulás " +
                        " ősi hiedelmeiből származik. A húsvéti nyúl a tavasz, az új élet és a " +
                        " termékenység jelképe, míg a bárány Krisztus áldozatát, ártatlanságát " +
                        " és a megváltást szimbolizálja. Magyarországon a húsvétot a " +
                        " kereszténység elterjedése óta ünneplik és a népszokások a 19. " +
                        " századtól erősödtek meg."),
                new FeastDay(2032, 3, 22, "A Föld napja", "A Föld napja 1970-ben indult el, amikor az Egyesült Államokban Gaylord " +
                        " Nelson szenátor kezdeményezésére több millió ember gyűlt össze, hogy " +
                        " felhívja a figyelmet a környezetszennyezésre és a természeti erőforrások " +
                        " védelmére. A nap célja, hogy globálisan tudatosítsa az emberekben a " +
                        " környezetvédelem fontosságát és ösztönözze a fenntartható életmódra " +
                        " való áttérést. Azóta minden évben április 22.-én világszerte " +
                        " megemlékeznek róla. Számos esemény, akció és kampány hívja fel a " +
                        " figyelmet a bolygó védelmére és a környezeti problémák megoldására. " +
                        "  " +
                        "  " +
                        " "),
                new FeastDay(2032, 3, 30, "Májusfa állítás", "A májusfa felállításának legjellemzőbb hagyomány szerinti időpontja a " +
                        " május 1-jét megelőző nap délután/este volt.  " +
                        " Magyarországon a középkor óta ismert, tehát legalább a 14. századtól " +
                        " ünneplik. A szokás az ősi tavaszi rituálékra épül, melyek a természet " +
                        " megújulását és az élet körforgását szimbolizálták. A fiatal legények " +
                        " a lányok háza elé állították a feldíszített fákat, hogy kifejezzék " +
                        " szerelmi szándékukat. A májusfát szalagokkal, virágokkal díszítették " +
                        " és május végén vagy pünkösdkor táncos mulatság keretében bontották " +
                        " le, amit kitáncolásnak neveztek. A hagyomány a szerelem és a termékenység " +
                        " ünnepe volt, később pedig összefonódott a munka ünnepével (május 1.). " +
                        " Mára már kevésbé elterjedt szokás, de sok helyen igyekeznek még " +
                        " fenntartani."),
                new FeastDay(2032, 4, 1, "A Munka ünnepe", "Május 1.-e a munka ünnepe. A munkások jogaiért és szolidaritásáért " +
                        " folytatott küzdelem emléknapja világszerte. Az ünnep gyökerei az 1886-os " +
                        " chicagói tüntetésekhez nyúlnak vissza, ahol a munkások a nyolcórás " +
                        " munkaidő bevezetéséért demonstráltak. Az események hatására 1890-től " +
                        " május 1.-ét a munkások nemzetközi szolidaritási napjává " +
                        " nyilvánították.  Magyarországon először 1890-ben tartották meg, de " +
                        " hivatalos állami ünnep csak 1946-ban lett. A szocializmus idején " +
                        " nagyszabású felvonulásokkal és tömegrendezvényekkel ünnepelték. A " +
                        " rendszerváltás után a jelentése átalakult, de továbbra is a munka " +
                        " tiszteletét és a munkavállalók jogainak fontosságát hirdeti. "),
                new FeastDay(2032, 4, 7, "Anyák napja", "Az édesanyák napja a szeretet, a tisztelet és a hála ünnepe, amelyet " +
                        " világszerte az édesanyák tiszteletére tartanak. Gyökerei az ókori " +
                        " Görögországba nyúlnak vissza, ahol Rheát, az istenek anyját ünnepelték. " +
                        " A modern anyák napja az Egyesült Államokból indult 1908-ban, amikor Anna " +
                        " Jarvis kampányt indított édesanyja emlékére. Magyarországon először " +
                        " 1925-ben ünnepelték és a májusi Mária-tisztelet hagyományaival is " +
                        " összekapcsolódott. Ez a nap a gondoskodó és szerető édesanyák, valamint " +
                        " minden női gondviselő iránti megbecsülés kifejezésére szolgál. " +
                        " Virágokkal, ajándékokkal és figyelmességgel fejezik ki hálájukat a " +
                        " gyerekek. Bár világszerte eltérő időpontokban ünneplik, mindenhol az " +
                        " anyák iránti szeretet és tisztelet áll a középpontban. Anyák napját " +
                        " Magyarországon minden év május első vasárnapján ünneplik.  " +
                        " "),
                new FeastDay(2032, 4, 28, "Gyermeknap", "A gyermeknap a gyerekek jogainak és jólétének fontosságát hirdető " +
                        " ünnep, melyet világszerte különböző időpontokban tartanak. Eredete " +
                        " 1925-re nyúlik vissza, amikor a genfi Gyermekjóléti Konferencián " +
                        " elhatározták, hogy kiemelik a gyermekek védelmének fontosságát. " +
                        " Magyarországon 1931-ben tartották meg először, majd 1950 óta május " +
                        " utolsó vasárnapján ünneplik. Ez a nap a gyerekekről szól, akik ilyenkor " +
                        " különféle programokon vehetnek részt, ahol játékokkal és szórakoztató " +
                        " rendezvényekkel ünneplik őket. Az ünnep célja, hogy felhívja a figyelmet " +
                        " a gyermekek jogaira és boldogságuk fontosságára. Magyarországon a " +
                        " Gyermeknapot minden évben május utolsó vasárnapján ünneplik."),
                new FeastDay(2032, 5, 4, "Pünkösd", "Pünkösd a kereszténység egyik legősibb ünnepe, melyet már a 4. " +
                        " századtól kezdve ünnepelnek a keresztény egyházban. Az ünnep Jézus " +
                        " mennybemenetele után ötven nappal emlékezik meg a Szentlélek " +
                        " eljöveteléről, amely erőt adott az apostoloknak a keresztény hit " +
                        " hirdetéséhez. Ez az esemény az egyház születésnapjaként is ismert. A " +
                        " „pünkösd” elnevezés a görög „pentékoszté” szóból ered, ami " +
                        " ötvenediket jelent. Magyarországon a középkor óta tartják számon és " +
                        " számos népi hagyomány kapcsolódik hozzá, például a pünkösdi király " +
                        " és királyné választása. Pünkösd vasárnapját a húsvét utáni 50. " +
                        " napon, tehát 50 nappal húsvétvasárnap után ünneplik. "),
                new FeastDay(2032, 5, 5, "Pünkösdhétfő", "A Pünkösdhétfő a keresztény egyház egyik legfontosabb ünnepének, a " +
                        " pünkösdnek a második napja. Pünkösd a húsvét utáni 50. napon van, és a " +
                        " Szentlélek eljövetelét ünnepli, amikor Jézus tanítványai erőt és " +
                        " útmutatást kaptak küldetésükhöz. Az esemény a keresztény egyház " +
                        " születésnapjának is tekinthető. Az ünnep gyökerei a zsidó pünkösdi " +
                        " ünnepre (Sávuót) vezethetők vissza, amely az aratási hálaadás napja " +
                        " volt. Magyarországon pünkösdhétfő munkaszüneti nap és hivatalosan 1993 " +
                        " óta ünnepeljük újra. Az ünnep a megújulást és a közösség " +
                        " összetartozását jelképezi."),
                new FeastDay(2032, 5, 18, "Apák napja", "Apák napja világszerte az édesapák tiszteletére szolgál, elismerve a " +
                        " családban és a gyermeknevelésben betöltött szerepüket. Az ünnep eredete " +
                        " az Egyesült Államokhoz köthető, ahol először 1910-ben tartották meg " +
                        " Sonora Smart Dodd kezdeményezésére , aki édesapját akarta tiszteletben " +
                        " részesíteni . Magyarországon az apák napja az 1990-es években kezdett " +
                        " elterjedni  és 1994 óta ünnepeljük minden év június harmadik " +
                        " vasárnapján. Célja, hogy elismerje az apák és az apaszerepet betöltő " +
                        " férfiak szeretetét, áldozatvállalását és a családban betöltött " +
                        " szerepét. Ez a nap lehetőséget ad arra, hogy köszönetet mondjunk az apák " +
                        " szeretetéért, támogatásáért és áldozatos munkájáért, amelyet " +
                        " családjukért tesznek. "),
                new FeastDay(2032, 5, 20, "Nyári napforduló", "Az év leghosszabb napja.  " +
                        " Napkelte:  " +
                        " Napnyugta:  " +
                        " Világos időtartam:  " +
                        " A nyári napforduló a leghosszabb nap az évben, amikor a Nap a legmagasabbra " +
                        " emelkedik az égbolton. Ez a jelenség a Föld tengelyferdesége és a " +
                        " keringése miatt következik be. Az északi féltekén a napforduló " +
                        " környékén a Nap több mint 15 órán keresztül a horizont felett " +
                        " tartózkodik. Ezen a napon a Föld az északi féltekén éri el a leghosszabb " +
                        " nappali órákat, így ez a legrövidebb éjszaka."),
                new FeastDay(2032, 7, 20, "Szent István-nap", "Szent István király koronázása körülbelül 1000-ben történt, ezzel " +
                        " megalapozva a keresztény Magyarországot. 1083 augusztus 20.-án szentté " +
                        " avatták a Szent Péter-bazilikában. Az Árpád-korban az István-kultusz az " +
                        " uralkodó dinasztiát támogatta és egységet teremtett a népcsoportok " +
                        " között. 1686-ban XI. Ince pápa elrendelte, hogy az egész katolikus világ " +
                        " emlékezzen meg róla, majd 1771-ben Mária Terézia országos ünneppé tette. " +
                        " Az 1848–49-es forradalom és szabadságharc leverése után egy időre " +
                        " szünetelt az ünneplés, de 1891-ben Ferenc József munkaszüneti nappá " +
                        " nyilvánította. A két világháború között a nemzeti egység " +
                        " szimbólumává vált. 1950-től az új államrendszer ideológiája szerint " +
                        " alakították át az ünnep jelentését, majd 1991-ben ismét állami " +
                        " ünneppé nyilvánították."),
                new FeastDay(2032, 9, 23, "1956-os Forradalom", "Magyarország nemzeti ünnepe, amely az 1956-os forradalom és szabadságharc " +
                        " eseményeire emlékezik. Az ünnep hivatalosan 1989 október 23.-án vált " +
                        " nemzeti ünneppé a rendszerváltás után, amikor a forradalom hőseit " +
                        " ünnepelve tisztelegtek a szabadságért és a demokráciáért folytatott " +
                        " küzdelmük előtt. Az 1956-os forradalom a szovjet megszállás elleni " +
                        " népfelkelés volt, amely bár véresen végződött, alapvető szerepet " +
                        " játszott Magyarország függetlenségi törekvéseiben és a későbbi " +
                        " rendszerváltásban. Az október 23.-i események máig a szabadság és a " +
                        " nemzeti szuverenitás fontos szimbólumai."),
                new FeastDay(2032, 9, 29, "Óraátállítás", "Az őszi óraátállítás (a téli időszámítás kezdete) – (vasárnap), " +
                        " 3:00 → 2:00 " +
                        " Ekkor kell 1 órát visszaállítani az órákat. " +
                        " Az őszi óraátállítás célja, hogy reggel világosabb legyen, így a " +
                        " természetes fényt jobban kihasználjuk. Magyarországon először 1916-ban " +
                        " vezették be az óraátállítást energiatakarékossági okokból. Bár több " +
                        " alkalommal szünetelt, végül 1980-ban újra bevezették, hogy csökkentsék a " +
                        " villamosenergia-felhasználást. A téli időszámítás október utolsó " +
                        " vasárnapján kezdődik, amikor az órákat visszaállítjuk. Ezáltal reggel " +
                        " hamarabb világosodik, de este hamarabb sötétedik.  1996 óta " +
                        " kormányrendelet szabályozza az óraátállítást. " +
                        " "),
                new FeastDay(2032, 10, 1, "Mindenszentek", "Mindenszentek ünnepe november 1.-én van, amikor a katolikus egyház " +
                        " megemlékezik minden üdvözült lélekről, akik elnyerték a mennyországot, " +
                        " de nevüket nem tartják számon a naptárban. Az ünnep eredete a " +
                        " kereszténység előtti halotti kultuszokra is visszavezethető. Hivatalosan a " +
                        " 8. században III. Gergely pápa idején vezették be, aki a Szent " +
                        " Péter-bazilikában kápolnát szentelt minden szent tiszteletére. " +
                        " Magyarországon munkaszüneti nap. Sokan virágokkal és gyertyagyújtással " +
                        " tisztelegnek elhunyt szeretteik emléke előtt. Ez a nap a hit és a " +
                        " megemlékezés ünnepe."),
                new FeastDay(2032, 10, 2, "Halottak napja", "Halottak napja november 2.-án van, amikor a katolikus egyház megemlékezik az " +
                        " elhunyt hívekről. Az ünnep célja, hogy imádságokkal és jó " +
                        " cselekedetekkel segítsük a halottak lelki üdvét és tisztelegjünk " +
                        " szeretteink emléke előtt. Bár gyökerei a kereszténység előtti halotti " +
                        " kultuszokig nyúlnak vissza, mai formáját a 11. században Cluny-i Szent " +
                        " Odiló apát vezette be. Ő rendelte el, hogy a kolostorok imádkozzanak a " +
                        " tisztítótűzben szenvedő lelkekért. A gyertyagyújtás és a virágok " +
                        " elhelyezése máig az emlékezés fontos része. "),
                new FeastDay(2032, 11, 3, "Advent első napja", "Advent a karácsony előtti négyhetes időszak, mely Jézus Krisztus " +
                        " születésére és második eljövetelére való várakozást jelenti. Az " +
                        " advent származása a 4. századra nyúlik vissza, amikor bűnbánattal és " +
                        " lelki felkészüléssel várták a karácsonyt. Kezdetben hosszabb böjti " +
                        " időszak volt, de fokozatosan négyhetesre csökkent. Az adventi koszorú " +
                        " hagyománya a 19. században terjedt el, az örökzöld növények a " +
                        " halhatatlanságot és az újjászületést szimbolizálták. A négy gyertya a " +
                        " hitet, a reményt, az örömöt és a szeretetet jelképezi. Minden vasárnap " +
                        " egy újabb gyertya meggyújtásával közelebb kerülünk a karácsonyhoz.  " +
                        " Advent első vasárnapját a karácsonyt megelőző négy vasárnap egyikeként " +
                        " ünneplik. Ez a nap általában november végére vagy december elejére esik.  " +
                        " "),
                new FeastDay(2032, 11, 6, "Mikulás", " Mikuláskor december 5.-én éjszaka, vagy 6.-án reggel töltik meg a gyerekek " +
                        " csizmáit édességgel és játékokkal. Szent Miklós püspök emlék napja. A " +
                        " 4. században élt szent, aki a gyermekek, a hajósok és a szegények " +
                        " védőszentje lett, híres volt jótékonyságáról. Egy legenda szerint " +
                        " titokban segített egy szegény ember három lányán, akiket így férjhez " +
                        " tudott adni. " +
                        " Ebből a történetből született a hagyomány, hogy Szent Miklós éjjelente " +
                        " ajándékokat hoz a jó gyerekeknek. Ma már világszerte ismert a piros " +
                        " ruhás, fehér szakállú Mikulás.  A Mikulás-hagyományok országonként " +
                        " eltérőek. Egyes helyeken a Mikulás hozza az ajándékokat, máshol a " +
                        " Télapó. A lényeg azonban mindenhol ugyanaz: a szeretet az adakozás és a " +
                        " gyermeki öröm ünnepe"),
                new FeastDay(2032, 11, 13, "Luca napja", "Luca napja a keresztény és a népi hagyományok keveréke. Szent Luca a 3. " +
                        " századi vértanú, a látás védőszentje, de a népi hitvilágban a " +
                        " termékenység, a jólét és a védelem szimbóluma. A hagyomány szerint ezen " +
                        " a napon kezdődött a tél leghosszabb éjszakája, így Luca napja a fény és " +
                        " a sötétség határán áll. Ezen a napon a nők nem dolgoztak, hogy bőséges " +
                        " legyen a következő év termése. Szokás volt Luca-széket készíteni, " +
                        " búzát ültetni és jóslásokat végezni. Luca napja a megújulás és a " +
                        " remény ünnepe, a fény győzelme a sötétség felett és a következő év " +
                        " jólétének biztosítása. " +
                        " "),
                new FeastDay(2032, 11, 21, "Téli napforduló", "Az év legrövidebb napja nap.  " +
                        " Napkelte:  " +
                        " Napnyugta:  " +
                        " Világos időtartam:  " +
                        " A téli napforduló a legrövidebb nap az évben, amikor a Nap a " +
                        " legalacsonyabbra emelkedik az égbolton. Ez a jelenség a Föld " +
                        " tengelyferdesége és keringése miatt következik be. Az északi féltekén a " +
                        " napforduló környékén a Nap csak 8 órán keresztül tartózkodik a horizont " +
                        " felett, így ez a leghosszabb éjszaka. Ezen a napon kezdődik a nappali fény " +
                        " fokozatos növekedése, így a következő hónapokban egyre hosszabbak lesznek " +
                        " a nappalok."),
                new FeastDay(2032, 11, 24, "Szenteste", "Szenteste a karácsonyi ünnepkör egyik legfontosabb napja, amely Jézus " +
                        " Krisztus születésének előestéje. Bár Jézus pontos születési dátuma " +
                        " nem ismert, a keresztény egyház a 4. században december 25.-ét jelölte meg " +
                        " születésének napjaként, összekapcsolva azt a téli napfordulóval. " +
                        " December 24.-én este a családok összegyűlnek, közösen vacsoráznak, " +
                        " ajándékokat adnak egymásnak és karácsonyfát állítanak. Magyarországon " +
                        " a hagyomány szerint a Jézuska hozza az ajándékokat, ezzel is megemlékezve " +
                        " Jézus születésének csodájáról. A fenyőfa, amely a 16. századtól vált " +
                        " a karácsony szimbólumává, az örök életet jelképezi. Az ajándékozás a " +
                        " szeretet és a megbecsülés kifejezése, amely a három királyok " +
                        " ajándékozására utal. Karácsony, bár elsősorban keresztény ünnep, " +
                        " világszerte a szeretet, a béke és a család ünnepe lett."),
                new FeastDay(2032, 11, 25, "Karácsony", "A keresztények számára Jézus Krisztus születésének ünnepe, melyet a 4. " +
                        " század óta tartanak. Az ünnep a téli napfordulóhoz kapcsolódik, " +
                        " szimbolizálva a fény győzelmét a sötétség felett. Karácsony a remény, " +
                        " a szeretet és a megváltás ünnepe, amely a családi együttlét, az öröm " +
                        " és a vallási szertartások ideje. Karácsonyfa, betlehem és csillag a " +
                        " szimbólumok, amelyek Jézus születését, az örök életet és az " +
                        " útmutatást képviselik. Az ünneplés világszerte eltérő, de mindegyik " +
                        " kultúrában a szeretet és a béke fontosságát hangsúlyozza. Karácsony " +
                        " lehetőséget ad az emberi kapcsolatok és az élet értelmének " +
                        " átgondolására"),
                new FeastDay(2032, 11, 26, "Karácsony másnapja", "A keresztény hagyományban Szent István, az első vértanú emléknapja, " +
                        " azonban Magyarországon elsősorban Szent István királyra, az " +
                        " államalapítóra emlékeznek. A 4. századi eredetű ünnep során Jézus " +
                        " születése után következik Szent István napja, aki kulcsszerepet játszott " +
                        " a kereszténység elterjesztésében Magyarországon. Bár Szent István a 11. " +
                        " században élt, az egyház később tette december 26.-át emlékévé. " +
                        " Magyarországon nemzeti hősként is tisztelik, mivel ő alapította az " +
                        " államot. Ezen a napon az emberek szentmisére mennek, megemlékeznek " +
                        " királyukról és különböző ünnepi rendezvényeken is részt vesznek"),
                new FeastDay(2032, 11, 31, "Szilveszter", "A Szilveszter az év utolsó napja, amikor búcsút veszünk az óévtől és " +
                        " ünnepeljük az új év kezdetét. Az ünnep neve Szent Szilveszter pápától " +
                        " származik, aki 335 körül halt meg, de az újév ünneplésének hagyományai " +
                        " sokkal régebbre nyúlnak vissza. Szilveszter éjszakáján családok és " +
                        " barátok együtt ünnepelnek, vacsoráznak, tűzijátékot néznek és " +
                        " jókívánságokat mondanak. A tűzijáték és a zajkeltés az ősi " +
                        " hagyományokból ered. Az ünnep a megújulás, az összetartozás és a " +
                        " remény kifejezője, miközben gazdaságilag is fontos szerepet játszik a " +
                        " fogyasztói társadalomban. " +
                        "  " +
                        " Boldog Új Évet kívánok! Legyen tele örömmel, egészséggel és sikerrel. " +
                        " Hozzon az új év boldog pillanatokat, új lehetőségeket és boldog együtt " +
                        " töltött időt!"),
                new FeastDay(2033, 0, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma. " +
                        " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az " +
                        " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy " +
                        " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és " +
                        " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a " +
                        " mondás. Legyen ez a nap a változás első lépése. Boldogságot, " +
                        " egészséget és kitartást kívánok az új év minden pillanatára! "
                ),
                new FeastDay(2033, 0, 6, "Vízkereszt", " A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a " +
                        " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus " +
                        " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus " +
                        " megkeresztelkedésére és az első csodájára. A három királyok aranyat, " +
                        " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a " +
                        " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet " +
                        " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi " +
                        " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak " +
                        " házszenteléseket, Isten áldását kérve az otthonra és az új évre."),
                new FeastDay(2033, 1, 14, "Valentin nap", "A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint " +
                        " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században " +
                        " titokban esketett meg szerelmespárokat, mert II. Claudius császár " +
                        " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák. " +
                        " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az " +
                        " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a " +
                        " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal " +
                        " és üdvözlőkártyákkal ünneplik."),
                new FeastDay(2033, 2, 1, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja, " +
                        " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a " +
                        " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A " +
                        " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és " +
                        " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki " +
                        " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának " +
                        " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel, " +
                        " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére " +
                        " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A " +
                        " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap " +
                        " előtt tartanak. Általában február végére vagy március elejére esik."),
                new FeastDay(2033, 2, 8, "Nőnap", " Az első Nőnapot 1908 március 8.-án tartották. A női munkások " +
                        " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az " +
                        " ünnep célja a nők társadalmi, politikai és gazdasági jogainak " +
                        " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben " +
                        " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így " +
                        " 1911-ben már több európai országban is megünnepelték. Magyarországon " +
                        " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már " +
                        " a nők jogainak elismerésére és tiszteletére emelkedett."),
                new FeastDay(2033, 2, 15, "1848-as Forradalom", "Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a " +
                        " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti " +
                        " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a " +
                        " sajtószabadságért, a jobb életkörülményekért és a függetlenségért. " +
                        " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című " +
                        " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet " +
                        " függetlenségi törekvése a magyar történelem egyik legfontosabb " +
                        " eseményévé vált. Az események hatására megerősödött a nemzeti " +
                        " identitás és ma már a magyar szabadság és összetartozás napjaként " +
                        " ünnepeljük. " +
                        " "),
                new FeastDay(2033, 2, 26, "Óraátállítás", " A tavaszi óraátállítás a nyári időszámítás kezdete.  " +
                        " A tavaszi óraátállításkor az órákat hajnali 2:00-ról 3:00-ra állítjuk " +
                        " előre, hogy jobban kihasználjuk a nappali világosságot és csökkentsük a " +
                        " mesterséges világítás szükségességét. Magyarországon először " +
                        " 1916-ban az első világháború alatt vezették be az energiatakarékosság " +
                        " érdekében, de a háború után megszüntették. 1954 és 1957 között újra " +
                        " alkalmazták, mert az ipari munkahelyeken az esti energiafogyasztás " +
                        " csökkentésére volt szükség, de 1958-ban a lakossági ellenállás és az " +
                        " alacsony megtakarítás miatt eltörölték. 1980-ban ismét bevezették a " +
                        " villamosenergia-megtakarítás érdekében. Azóta évente kétszer állítjuk " +
                        " át az órákat és 1996 óta kormányrendelet szabályozza az " +
                        " óraátállítást Magyarországon."),
                new FeastDay(2033, 3, 1, "Bolondok napja", "Április elseje a bolondok napja, a bolondos tréfák és ártalmatlan " +
                        " átverések napja. Az emberek apró csínyekkel, például hamis hírekkel vagy " +
                        " kitalált történetekkel szórakoztatják egymást. Az internet " +
                        " elterjedésével az álhírek és vírusos videók is népszerűvé váltak. A " +
                        " nap célja, hogy jókedvet és nevetést hozzon, miközben erősíti a " +
                        " közösségi szellemet. Eredete több elméletre vezethető vissza. A " +
                        " legnépszerűbb magyarázat szerint a XVI. században, a Gergely-naptár " +
                        " bevezetésekor azok, akik áprilisban ünnepelték az újévet, " +
                        " Franciaországban bolondnak számítottak, így tréfák céltáblájává " +
                        " váltak. Hasonló szokások figyelhetők meg a kelta tavaszi ünnepekben és az " +
                        " ókori római Hilaria ünnepén is, ahol a bolondos viselkedés a tavaszi " +
                        " megújulást jelezte. "),
                new FeastDay(2033, 3, 9, "Virágvasárnap", " A Virágvasárnap a húsvét előtti vasárnap, Jézus jeruzsálemi " +
                        " bevonulásának emlékére van. Az evangéliumok szerint Jézus szamárháton " +
                        " vonult be a városba, miközben a hívek pálmaágakat lengettek, üdvözölve " +
                        " őt, mint a Megváltót. A virágok, a pálmák és a zöld ágak használata " +
                        " az ünnep jelképei, melyek a békét és az üdvözítőt hirdetik. A " +
                        " keresztény hagyományban Virágvasárnap a nagyböjt végét és a húsvéti " +
                        " ünnepekre való lelki felkészülést jelzi. Az ünnep hivatalosan a 4. " +
                        " század óta van jelen az egyházi naptárban és azóta része a keresztény " +
                        " hagyományoknak világszerte."),
                new FeastDay(2033, 3, 14, "Nagypéntek", "A Nagypéntek a keresztény húsvéti ünnepkör legfontosabb napja, amikor " +
                        " Jézus Krisztus kereszthalálát és szenvedését emlékezzük meg. Az ünnep " +
                        " célja, hogy a hívők átgondolják a megváltásra tett áldozat " +
                        " jelentőségét. E napot a 4. század óta ünneplik, amikor a keresztény " +
                        " egyház hivatalosan is elismerte Jézus kereszthalálának fontosságát. " +
                        " Magyarországon a 20. század elejétől munkaszüneti nap. A keresztény " +
                        " hagyományokban sötét napként van jelen, amikor sokan böjtölnek és " +
                        " csendes megemlékezéseket tartanak. Az egyházi szertartások mellett a " +
                        " keresztény közösség számára Nagypéntek a bűnbánat és a lelki " +
                        " megújulás napja is.  Nagypénteket a húsvét előtti pénteken ünneplik, " +
                        " tehát ez a dátum évente változik.  " +
                        " "),
                new FeastDay(2033, 3, 16, "Húsvétvasárnap", "Húsvétvasárnap a kereszténység legfontosabb ünnepe, Jézus Krisztus " +
                        " feltámadásának napja, amely a halál feletti győzelmet és az örök élet " +
                        " ígéretét hirdeti. Magyarországon 1991 óta munkaszüneti nap, de " +
                        " jelentősége évszázadok óta meghatározó. Húsvét a 40 napos nagyböjt " +
                        " végét jelzi, amely a bűnbánat és a lelki megújulás időszaka. A gyerekek " +
                        " az ajándékaikat hagyományosan ezen a napon kapják meg, édességeket és " +
                        " tojásokat, amelyeket a húsvéti nyúl hoz.  A húsvéti nyúl a 16. századi " +
                        " Németországból ered és a termékenység, valamint az újjászületés " +
                        " jelképe. Eredete pogány hagyományokhoz köthető, ahol a nyúl és a tojás " +
                        " a tavaszi újjászületés szimbóluma volt, melyek később beépültek a " +
                        " keresztény húsvéti szokásokba. " +
                        " A Húsvétot évente változó dátumon ünneplik, mivel a tavaszi " +
                        " napéjegyenlőség utáni első teliholdat követő vasárnapra esik."),
                new FeastDay(2033, 3, 17, "Húsvéthétfő", "Húsvéthétfő a húsvéti ünnepkör egyik legfontosabb napja, amely Jézus " +
                        " Krisztus feltámadását követően az új élet és a megújulás " +
                        " szimbólumává vált. Magyarországon a húsvéti locsolás hagyománya a 17. " +
                        " századtól terjedt el. A férfiak vízzel vagy illatos kölnivel locsolják " +
                        " meg a nőket, kívánva nekik egészséget, boldogságot és termékenységet. " +
                        " A locsolás eredete valószínűleg a megtisztulás és a tavaszi megújulás " +
                        " ősi hiedelmeiből származik. A húsvéti nyúl a tavasz, az új élet és a " +
                        " termékenység jelképe, míg a bárány Krisztus áldozatát, ártatlanságát " +
                        " és a megváltást szimbolizálja. Magyarországon a húsvétot a " +
                        " kereszténység elterjedése óta ünneplik és a népszokások a 19. " +
                        " századtól erősödtek meg."),
                new FeastDay(2033, 3, 22, "A Föld napja", "A Föld napja 1970-ben indult el, amikor az Egyesült Államokban Gaylord " +
                        " Nelson szenátor kezdeményezésére több millió ember gyűlt össze, hogy " +
                        " felhívja a figyelmet a környezetszennyezésre és a természeti erőforrások " +
                        " védelmére. A nap célja, hogy globálisan tudatosítsa az emberekben a " +
                        " környezetvédelem fontosságát és ösztönözze a fenntartható életmódra " +
                        " való áttérést. Azóta minden évben április 22.-én világszerte " +
                        " megemlékeznek róla. Számos esemény, akció és kampány hívja fel a " +
                        " figyelmet a bolygó védelmére és a környezeti problémák megoldására. " +
                        "  " +
                        "  " +
                        " "),
                new FeastDay(2033, 3, 30, "Májusfa állítás", "A májusfa felállításának legjellemzőbb hagyomány szerinti időpontja a " +
                        " május 1-jét megelőző nap délután/este volt.  " +
                        " Magyarországon a középkor óta ismert, tehát legalább a 14. századtól " +
                        " ünneplik. A szokás az ősi tavaszi rituálékra épül, melyek a természet " +
                        " megújulását és az élet körforgását szimbolizálták. A fiatal legények " +
                        " a lányok háza elé állították a feldíszített fákat, hogy kifejezzék " +
                        " szerelmi szándékukat. A májusfát szalagokkal, virágokkal díszítették " +
                        " és május végén vagy pünkösdkor táncos mulatság keretében bontották " +
                        " le, amit kitáncolásnak neveztek. A hagyomány a szerelem és a termékenység " +
                        " ünnepe volt, később pedig összefonódott a munka ünnepével (május 1.). " +
                        " Mára már kevésbé elterjedt szokás, de sok helyen igyekeznek még " +
                        " fenntartani."),
                new FeastDay(2033, 4, 1, "A Munka ünnepe", "Május 1.-e a munka ünnepe. A munkások jogaiért és szolidaritásáért " +
                        " folytatott küzdelem emléknapja világszerte. Az ünnep gyökerei az 1886-os " +
                        " chicagói tüntetésekhez nyúlnak vissza, ahol a munkások a nyolcórás " +
                        " munkaidő bevezetéséért demonstráltak. Az események hatására 1890-től " +
                        " május 1.-ét a munkások nemzetközi szolidaritási napjává " +
                        " nyilvánították.  Magyarországon először 1890-ben tartották meg, de " +
                        " hivatalos állami ünnep csak 1946-ban lett. A szocializmus idején " +
                        " nagyszabású felvonulásokkal és tömegrendezvényekkel ünnepelték. A " +
                        " rendszerváltás után a jelentése átalakult, de továbbra is a munka " +
                        " tiszteletét és a munkavállalók jogainak fontosságát hirdeti. "),
                new FeastDay(2033, 4, 7, "Anyák napja", "Az édesanyák napja a szeretet, a tisztelet és a hála ünnepe, amelyet " +
                        " világszerte az édesanyák tiszteletére tartanak. Gyökerei az ókori " +
                        " Görögországba nyúlnak vissza, ahol Rheát, az istenek anyját ünnepelték. " +
                        " A modern anyák napja az Egyesült Államokból indult 1908-ban, amikor Anna " +
                        " Jarvis kampányt indított édesanyja emlékére. Magyarországon először " +
                        " 1925-ben ünnepelték és a májusi Mária-tisztelet hagyományaival is " +
                        " összekapcsolódott. Ez a nap a gondoskodó és szerető édesanyák, valamint " +
                        " minden női gondviselő iránti megbecsülés kifejezésére szolgál. " +
                        " Virágokkal, ajándékokkal és figyelmességgel fejezik ki hálájukat a " +
                        " gyerekek. Bár világszerte eltérő időpontokban ünneplik, mindenhol az " +
                        " anyák iránti szeretet és tisztelet áll a középpontban. Anyák napját " +
                        " Magyarországon minden év május első vasárnapján ünneplik.  " +
                        " "),
                new FeastDay(2033, 4, 28, "Gyermeknap", "A gyermeknap a gyerekek jogainak és jólétének fontosságát hirdető " +
                        " ünnep, melyet világszerte különböző időpontokban tartanak. Eredete " +
                        " 1925-re nyúlik vissza, amikor a genfi Gyermekjóléti Konferencián " +
                        " elhatározták, hogy kiemelik a gyermekek védelmének fontosságát. " +
                        " Magyarországon 1931-ben tartották meg először, majd 1950 óta május " +
                        " utolsó vasárnapján ünneplik. Ez a nap a gyerekekről szól, akik ilyenkor " +
                        " különféle programokon vehetnek részt, ahol játékokkal és szórakoztató " +
                        " rendezvényekkel ünneplik őket. Az ünnep célja, hogy felhívja a figyelmet " +
                        " a gyermekek jogaira és boldogságuk fontosságára. Magyarországon a " +
                        " Gyermeknapot minden évben május utolsó vasárnapján ünneplik."),
                new FeastDay(2033, 5, 4, "Pünkösd", "Pünkösd a kereszténység egyik legősibb ünnepe, melyet már a 4. " +
                        " századtól kezdve ünnepelnek a keresztény egyházban. Az ünnep Jézus " +
                        " mennybemenetele után ötven nappal emlékezik meg a Szentlélek " +
                        " eljöveteléről, amely erőt adott az apostoloknak a keresztény hit " +
                        " hirdetéséhez. Ez az esemény az egyház születésnapjaként is ismert. A " +
                        " „pünkösd” elnevezés a görög „pentékoszté” szóból ered, ami " +
                        " ötvenediket jelent. Magyarországon a középkor óta tartják számon és " +
                        " számos népi hagyomány kapcsolódik hozzá, például a pünkösdi király " +
                        " és királyné választása. Pünkösd vasárnapját a húsvét utáni 50. " +
                        " napon, tehát 50 nappal húsvétvasárnap után ünneplik. "),
                new FeastDay(2033, 5, 5, "Pünkösdhétfő", "A Pünkösdhétfő a keresztény egyház egyik legfontosabb ünnepének, a " +
                        " pünkösdnek a második napja. Pünkösd a húsvét utáni 50. napon van, és a " +
                        " Szentlélek eljövetelét ünnepli, amikor Jézus tanítványai erőt és " +
                        " útmutatást kaptak küldetésükhöz. Az esemény a keresztény egyház " +
                        " születésnapjának is tekinthető. Az ünnep gyökerei a zsidó pünkösdi " +
                        " ünnepre (Sávuót) vezethetők vissza, amely az aratási hálaadás napja " +
                        " volt. Magyarországon pünkösdhétfő munkaszüneti nap és hivatalosan 1993 " +
                        " óta ünnepeljük újra. Az ünnep a megújulást és a közösség " +
                        " összetartozását jelképezi."),
                new FeastDay(2033, 5, 18, "Apák napja", "Apák napja világszerte az édesapák tiszteletére szolgál, elismerve a " +
                        " családban és a gyermeknevelésben betöltött szerepüket. Az ünnep eredete " +
                        " az Egyesült Államokhoz köthető, ahol először 1910-ben tartották meg " +
                        " Sonora Smart Dodd kezdeményezésére , aki édesapját akarta tiszteletben " +
                        " részesíteni . Magyarországon az apák napja az 1990-es években kezdett " +
                        " elterjedni  és 1994 óta ünnepeljük minden év június harmadik " +
                        " vasárnapján. Célja, hogy elismerje az apák és az apaszerepet betöltő " +
                        " férfiak szeretetét, áldozatvállalását és a családban betöltött " +
                        " szerepét. Ez a nap lehetőséget ad arra, hogy köszönetet mondjunk az apák " +
                        " szeretetéért, támogatásáért és áldozatos munkájáért, amelyet " +
                        " családjukért tesznek. "),
                new FeastDay(2033, 5, 20, "Nyári napforduló", "Az év leghosszabb napja.  " +
                        " Napkelte:  " +
                        " Napnyugta:  " +
                        " Világos időtartam:  " +
                        " A nyári napforduló a leghosszabb nap az évben, amikor a Nap a legmagasabbra " +
                        " emelkedik az égbolton. Ez a jelenség a Föld tengelyferdesége és a " +
                        " keringése miatt következik be. Az északi féltekén a napforduló " +
                        " környékén a Nap több mint 15 órán keresztül a horizont felett " +
                        " tartózkodik. Ezen a napon a Föld az északi féltekén éri el a leghosszabb " +
                        " nappali órákat, így ez a legrövidebb éjszaka."),
                new FeastDay(2033, 7, 20, "Szent István-nap", "Szent István király koronázása körülbelül 1000-ben történt, ezzel " +
                        " megalapozva a keresztény Magyarországot. 1083 augusztus 20.-án szentté " +
                        " avatták a Szent Péter-bazilikában. Az Árpád-korban az István-kultusz az " +
                        " uralkodó dinasztiát támogatta és egységet teremtett a népcsoportok " +
                        " között. 1686-ban XI. Ince pápa elrendelte, hogy az egész katolikus világ " +
                        " emlékezzen meg róla, majd 1771-ben Mária Terézia országos ünneppé tette. " +
                        " Az 1848–49-es forradalom és szabadságharc leverése után egy időre " +
                        " szünetelt az ünneplés, de 1891-ben Ferenc József munkaszüneti nappá " +
                        " nyilvánította. A két világháború között a nemzeti egység " +
                        " szimbólumává vált. 1950-től az új államrendszer ideológiája szerint " +
                        " alakították át az ünnep jelentését, majd 1991-ben ismét állami " +
                        " ünneppé nyilvánították."),
                new FeastDay(2033, 9, 23, "1956-os Forradalom", "Magyarország nemzeti ünnepe, amely az 1956-os forradalom és szabadságharc " +
                        " eseményeire emlékezik. Az ünnep hivatalosan 1989 október 23.-án vált " +
                        " nemzeti ünneppé a rendszerváltás után, amikor a forradalom hőseit " +
                        " ünnepelve tisztelegtek a szabadságért és a demokráciáért folytatott " +
                        " küzdelmük előtt. Az 1956-os forradalom a szovjet megszállás elleni " +
                        " népfelkelés volt, amely bár véresen végződött, alapvető szerepet " +
                        " játszott Magyarország függetlenségi törekvéseiben és a későbbi " +
                        " rendszerváltásban. Az október 23.-i események máig a szabadság és a " +
                        " nemzeti szuverenitás fontos szimbólumai."),
                new FeastDay(2033, 9, 29, "Óraátállítás", "Az őszi óraátállítás (a téli időszámítás kezdete) – (vasárnap), " +
                        " 3:00 → 2:00 " +
                        " Ekkor kell 1 órát visszaállítani az órákat. " +
                        " Az őszi óraátállítás célja, hogy reggel világosabb legyen, így a " +
                        " természetes fényt jobban kihasználjuk. Magyarországon először 1916-ban " +
                        " vezették be az óraátállítást energiatakarékossági okokból. Bár több " +
                        " alkalommal szünetelt, végül 1980-ban újra bevezették, hogy csökkentsék a " +
                        " villamosenergia-felhasználást. A téli időszámítás október utolsó " +
                        " vasárnapján kezdődik, amikor az órákat visszaállítjuk. Ezáltal reggel " +
                        " hamarabb világosodik, de este hamarabb sötétedik.  1996 óta " +
                        " kormányrendelet szabályozza az óraátállítást. " +
                        " "),
                new FeastDay(2033, 10, 1, "Mindenszentek", "Mindenszentek ünnepe november 1.-én van, amikor a katolikus egyház " +
                        " megemlékezik minden üdvözült lélekről, akik elnyerték a mennyországot, " +
                        " de nevüket nem tartják számon a naptárban. Az ünnep eredete a " +
                        " kereszténység előtti halotti kultuszokra is visszavezethető. Hivatalosan a " +
                        " 8. században III. Gergely pápa idején vezették be, aki a Szent " +
                        " Péter-bazilikában kápolnát szentelt minden szent tiszteletére. " +
                        " Magyarországon munkaszüneti nap. Sokan virágokkal és gyertyagyújtással " +
                        " tisztelegnek elhunyt szeretteik emléke előtt. Ez a nap a hit és a " +
                        " megemlékezés ünnepe."),
                new FeastDay(2033, 10, 2, "Halottak napja", "Halottak napja november 2.-án van, amikor a katolikus egyház megemlékezik az " +
                        " elhunyt hívekről. Az ünnep célja, hogy imádságokkal és jó " +
                        " cselekedetekkel segítsük a halottak lelki üdvét és tisztelegjünk " +
                        " szeretteink emléke előtt. Bár gyökerei a kereszténység előtti halotti " +
                        " kultuszokig nyúlnak vissza, mai formáját a 11. században Cluny-i Szent " +
                        " Odiló apát vezette be. Ő rendelte el, hogy a kolostorok imádkozzanak a " +
                        " tisztítótűzben szenvedő lelkekért. A gyertyagyújtás és a virágok " +
                        " elhelyezése máig az emlékezés fontos része. "),
                new FeastDay(2033, 11, 3, "Advent első napja", "Advent a karácsony előtti négyhetes időszak, mely Jézus Krisztus " +
                        " születésére és második eljövetelére való várakozást jelenti. Az " +
                        " advent származása a 4. századra nyúlik vissza, amikor bűnbánattal és " +
                        " lelki felkészüléssel várták a karácsonyt. Kezdetben hosszabb böjti " +
                        " időszak volt, de fokozatosan négyhetesre csökkent. Az adventi koszorú " +
                        " hagyománya a 19. században terjedt el, az örökzöld növények a " +
                        " halhatatlanságot és az újjászületést szimbolizálták. A négy gyertya a " +
                        " hitet, a reményt, az örömöt és a szeretetet jelképezi. Minden vasárnap " +
                        " egy újabb gyertya meggyújtásával közelebb kerülünk a karácsonyhoz.  " +
                        " Advent első vasárnapját a karácsonyt megelőző négy vasárnap egyikeként " +
                        " ünneplik. Ez a nap általában november végére vagy december elejére esik.  " +
                        " "),
                new FeastDay(2033, 11, 6, "Mikulás", " Mikuláskor december 5.-én éjszaka, vagy 6.-án reggel töltik meg a gyerekek " +
                        " csizmáit édességgel és játékokkal. Szent Miklós püspök emlék napja. A " +
                        " 4. században élt szent, aki a gyermekek, a hajósok és a szegények " +
                        " védőszentje lett, híres volt jótékonyságáról. Egy legenda szerint " +
                        " titokban segített egy szegény ember három lányán, akiket így férjhez " +
                        " tudott adni. " +
                        " Ebből a történetből született a hagyomány, hogy Szent Miklós éjjelente " +
                        " ajándékokat hoz a jó gyerekeknek. Ma már világszerte ismert a piros " +
                        " ruhás, fehér szakállú Mikulás.  A Mikulás-hagyományok országonként " +
                        " eltérőek. Egyes helyeken a Mikulás hozza az ajándékokat, máshol a " +
                        " Télapó. A lényeg azonban mindenhol ugyanaz: a szeretet az adakozás és a " +
                        " gyermeki öröm ünnepe"),
                new FeastDay(2033, 11, 13, "Luca napja", "Luca napja a keresztény és a népi hagyományok keveréke. Szent Luca a 3. " +
                        " századi vértanú, a látás védőszentje, de a népi hitvilágban a " +
                        " termékenység, a jólét és a védelem szimbóluma. A hagyomány szerint ezen " +
                        " a napon kezdődött a tél leghosszabb éjszakája, így Luca napja a fény és " +
                        " a sötétség határán áll. Ezen a napon a nők nem dolgoztak, hogy bőséges " +
                        " legyen a következő év termése. Szokás volt Luca-széket készíteni, " +
                        " búzát ültetni és jóslásokat végezni. Luca napja a megújulás és a " +
                        " remény ünnepe, a fény győzelme a sötétség felett és a következő év " +
                        " jólétének biztosítása. " +
                        " "),
                new FeastDay(2033, 11, 21, "Téli napforduló", "Az év legrövidebb napja nap.  " +
                        " Napkelte:  " +
                        " Napnyugta:  " +
                        " Világos időtartam:  " +
                        " A téli napforduló a legrövidebb nap az évben, amikor a Nap a " +
                        " legalacsonyabbra emelkedik az égbolton. Ez a jelenség a Föld " +
                        " tengelyferdesége és keringése miatt következik be. Az északi féltekén a " +
                        " napforduló környékén a Nap csak 8 órán keresztül tartózkodik a horizont " +
                        " felett, így ez a leghosszabb éjszaka. Ezen a napon kezdődik a nappali fény " +
                        " fokozatos növekedése, így a következő hónapokban egyre hosszabbak lesznek " +
                        " a nappalok."),
                new FeastDay(2033, 11, 24, "Szenteste", "Szenteste a karácsonyi ünnepkör egyik legfontosabb napja, amely Jézus " +
                        " Krisztus születésének előestéje. Bár Jézus pontos születési dátuma " +
                        " nem ismert, a keresztény egyház a 4. században december 25.-ét jelölte meg " +
                        " születésének napjaként, összekapcsolva azt a téli napfordulóval. " +
                        " December 24.-én este a családok összegyűlnek, közösen vacsoráznak, " +
                        " ajándékokat adnak egymásnak és karácsonyfát állítanak. Magyarországon " +
                        " a hagyomány szerint a Jézuska hozza az ajándékokat, ezzel is megemlékezve " +
                        " Jézus születésének csodájáról. A fenyőfa, amely a 16. századtól vált " +
                        " a karácsony szimbólumává, az örök életet jelképezi. Az ajándékozás a " +
                        " szeretet és a megbecsülés kifejezése, amely a három királyok " +
                        " ajándékozására utal. Karácsony, bár elsősorban keresztény ünnep, " +
                        " világszerte a szeretet, a béke és a család ünnepe lett."),
                new FeastDay(2033, 11, 25, "Karácsony", "A keresztények számára Jézus Krisztus születésének ünnepe, melyet a 4. " +
                        " század óta tartanak. Az ünnep a téli napfordulóhoz kapcsolódik, " +
                        " szimbolizálva a fény győzelmét a sötétség felett. Karácsony a remény, " +
                        " a szeretet és a megváltás ünnepe, amely a családi együttlét, az öröm " +
                        " és a vallási szertartások ideje. Karácsonyfa, betlehem és csillag a " +
                        " szimbólumok, amelyek Jézus születését, az örök életet és az " +
                        " útmutatást képviselik. Az ünneplés világszerte eltérő, de mindegyik " +
                        " kultúrában a szeretet és a béke fontosságát hangsúlyozza. Karácsony " +
                        " lehetőséget ad az emberi kapcsolatok és az élet értelmének " +
                        " átgondolására"),
                new FeastDay(2033, 11, 26, "Karácsony másnapja", "A keresztény hagyományban Szent István, az első vértanú emléknapja, " +
                        " azonban Magyarországon elsősorban Szent István királyra, az " +
                        " államalapítóra emlékeznek. A 4. századi eredetű ünnep során Jézus " +
                        " születése után következik Szent István napja, aki kulcsszerepet játszott " +
                        " a kereszténység elterjesztésében Magyarországon. Bár Szent István a 11. " +
                        " században élt, az egyház később tette december 26.-át emlékévé. " +
                        " Magyarországon nemzeti hősként is tisztelik, mivel ő alapította az " +
                        " államot. Ezen a napon az emberek szentmisére mennek, megemlékeznek " +
                        " királyukról és különböző ünnepi rendezvényeken is részt vesznek"),
                new FeastDay(2033, 11, 31, "Szilveszter", "A Szilveszter az év utolsó napja, amikor búcsút veszünk az óévtől és " +
                        " ünnepeljük az új év kezdetét. Az ünnep neve Szent Szilveszter pápától " +
                        " származik, aki 335 körül halt meg, de az újév ünneplésének hagyományai " +
                        " sokkal régebbre nyúlnak vissza. Szilveszter éjszakáján családok és " +
                        " barátok együtt ünnepelnek, vacsoráznak, tűzijátékot néznek és " +
                        " jókívánságokat mondanak. A tűzijáték és a zajkeltés az ősi " +
                        " hagyományokból ered. Az ünnep a megújulás, az összetartozás és a " +
                        " remény kifejezője, miközben gazdaságilag is fontos szerepet játszik a " +
                        " fogyasztói társadalomban. " +
                        "  " +
                        " Boldog Új Évet kívánok! Legyen tele örömmel, egészséggel és sikerrel. " +
                        " Hozzon az új év boldog pillanatokat, új lehetőségeket és boldog együtt " +
                        " töltött időt!"),
                new FeastDay(2034, 0, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma. " +
                        " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az " +
                        " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy " +
                        " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és " +
                        " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a " +
                        " mondás. Legyen ez a nap a változás első lépése. Boldogságot, " +
                        " egészséget és kitartást kívánok az új év minden pillanatára! "
                ),
                new FeastDay(2034, 0, 6, "Vízkereszt", " A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a " +
                        " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus " +
                        " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus " +
                        " megkeresztelkedésére és az első csodájára. A három királyok aranyat, " +
                        " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a " +
                        " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet " +
                        " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi " +
                        " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak " +
                        " házszenteléseket, Isten áldását kérve az otthonra és az új évre."),
                new FeastDay(2034, 1, 14, "Valentin nap", "A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint " +
                        " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században " +
                        " titokban esketett meg szerelmespárokat, mert II. Claudius császár " +
                        " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák. " +
                        " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az " +
                        " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a " +
                        " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal " +
                        " és üdvözlőkártyákkal ünneplik."),
                new FeastDay(2034, 2, 1, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja, " +
                        " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a " +
                        " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A " +
                        " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és " +
                        " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki " +
                        " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának " +
                        " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel, " +
                        " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére " +
                        " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A " +
                        " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap " +
                        " előtt tartanak. Általában február végére vagy március elejére esik."),
                new FeastDay(2034, 2, 8, "Nőnap", " Az első Nőnapot 1908 március 8.-án tartották. A női munkások " +
                        " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az " +
                        " ünnep célja a nők társadalmi, politikai és gazdasági jogainak " +
                        " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben " +
                        " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így " +
                        " 1911-ben már több európai országban is megünnepelték. Magyarországon " +
                        " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már " +
                        " a nők jogainak elismerésére és tiszteletére emelkedett."),
                new FeastDay(2034, 2, 15, "1848-as Forradalom", "Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a " +
                        " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti " +
                        " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a " +
                        " sajtószabadságért, a jobb életkörülményekért és a függetlenségért. " +
                        " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című " +
                        " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet " +
                        " függetlenségi törekvése a magyar történelem egyik legfontosabb " +
                        " eseményévé vált. Az események hatására megerősödött a nemzeti " +
                        " identitás és ma már a magyar szabadság és összetartozás napjaként " +
                        " ünnepeljük. " +
                        " "),
                new FeastDay(2034, 2, 26, "Óraátállítás", " A tavaszi óraátállítás a nyári időszámítás kezdete.  " +
                        " A tavaszi óraátállításkor az órákat hajnali 2:00-ról 3:00-ra állítjuk " +
                        " előre, hogy jobban kihasználjuk a nappali világosságot és csökkentsük a " +
                        " mesterséges világítás szükségességét. Magyarországon először " +
                        " 1916-ban az első világháború alatt vezették be az energiatakarékosság " +
                        " érdekében, de a háború után megszüntették. 1954 és 1957 között újra " +
                        " alkalmazták, mert az ipari munkahelyeken az esti energiafogyasztás " +
                        " csökkentésére volt szükség, de 1958-ban a lakossági ellenállás és az " +
                        " alacsony megtakarítás miatt eltörölték. 1980-ban ismét bevezették a " +
                        " villamosenergia-megtakarítás érdekében. Azóta évente kétszer állítjuk " +
                        " át az órákat és 1996 óta kormányrendelet szabályozza az " +
                        " óraátállítást Magyarországon."),
                new FeastDay(2034, 3, 1, "Bolondok napja", "Április elseje a bolondok napja, a bolondos tréfák és ártalmatlan " +
                        " átverések napja. Az emberek apró csínyekkel, például hamis hírekkel vagy " +
                        " kitalált történetekkel szórakoztatják egymást. Az internet " +
                        " elterjedésével az álhírek és vírusos videók is népszerűvé váltak. A " +
                        " nap célja, hogy jókedvet és nevetést hozzon, miközben erősíti a " +
                        " közösségi szellemet. Eredete több elméletre vezethető vissza. A " +
                        " legnépszerűbb magyarázat szerint a XVI. században, a Gergely-naptár " +
                        " bevezetésekor azok, akik áprilisban ünnepelték az újévet, " +
                        " Franciaországban bolondnak számítottak, így tréfák céltáblájává " +
                        " váltak. Hasonló szokások figyelhetők meg a kelta tavaszi ünnepekben és az " +
                        " ókori római Hilaria ünnepén is, ahol a bolondos viselkedés a tavaszi " +
                        " megújulást jelezte. "),
                new FeastDay(2034, 3, 9, "Virágvasárnap", " A Virágvasárnap a húsvét előtti vasárnap, Jézus jeruzsálemi " +
                        " bevonulásának emlékére van. Az evangéliumok szerint Jézus szamárháton " +
                        " vonult be a városba, miközben a hívek pálmaágakat lengettek, üdvözölve " +
                        " őt, mint a Megváltót. A virágok, a pálmák és a zöld ágak használata " +
                        " az ünnep jelképei, melyek a békét és az üdvözítőt hirdetik. A " +
                        " keresztény hagyományban Virágvasárnap a nagyböjt végét és a húsvéti " +
                        " ünnepekre való lelki felkészülést jelzi. Az ünnep hivatalosan a 4. " +
                        " század óta van jelen az egyházi naptárban és azóta része a keresztény " +
                        " hagyományoknak világszerte."),
                new FeastDay(2034, 3, 14, "Nagypéntek", "A Nagypéntek a keresztény húsvéti ünnepkör legfontosabb napja, amikor " +
                        " Jézus Krisztus kereszthalálát és szenvedését emlékezzük meg. Az ünnep " +
                        " célja, hogy a hívők átgondolják a megváltásra tett áldozat " +
                        " jelentőségét. E napot a 4. század óta ünneplik, amikor a keresztény " +
                        " egyház hivatalosan is elismerte Jézus kereszthalálának fontosságát. " +
                        " Magyarországon a 20. század elejétől munkaszüneti nap. A keresztény " +
                        " hagyományokban sötét napként van jelen, amikor sokan böjtölnek és " +
                        " csendes megemlékezéseket tartanak. Az egyházi szertartások mellett a " +
                        " keresztény közösség számára Nagypéntek a bűnbánat és a lelki " +
                        " megújulás napja is.  Nagypénteket a húsvét előtti pénteken ünneplik, " +
                        " tehát ez a dátum évente változik.  " +
                        " "),
                new FeastDay(2034, 3, 16, "Húsvétvasárnap", "Húsvétvasárnap a kereszténység legfontosabb ünnepe, Jézus Krisztus " +
                        " feltámadásának napja, amely a halál feletti győzelmet és az örök élet " +
                        " ígéretét hirdeti. Magyarországon 1991 óta munkaszüneti nap, de " +
                        " jelentősége évszázadok óta meghatározó. Húsvét a 40 napos nagyböjt " +
                        " végét jelzi, amely a bűnbánat és a lelki megújulás időszaka. A gyerekek " +
                        " az ajándékaikat hagyományosan ezen a napon kapják meg, édességeket és " +
                        " tojásokat, amelyeket a húsvéti nyúl hoz.  A húsvéti nyúl a 16. századi " +
                        " Németországból ered és a termékenység, valamint az újjászületés " +
                        " jelképe. Eredete pogány hagyományokhoz köthető, ahol a nyúl és a tojás " +
                        " a tavaszi újjászületés szimbóluma volt, melyek később beépültek a " +
                        " keresztény húsvéti szokásokba. " +
                        " A Húsvétot évente változó dátumon ünneplik, mivel a tavaszi " +
                        " napéjegyenlőség utáni első teliholdat követő vasárnapra esik."),
                new FeastDay(2034, 3, 17, "Húsvéthétfő", "Húsvéthétfő a húsvéti ünnepkör egyik legfontosabb napja, amely Jézus " +
                        " Krisztus feltámadását követően az új élet és a megújulás " +
                        " szimbólumává vált. Magyarországon a húsvéti locsolás hagyománya a 17. " +
                        " századtól terjedt el. A férfiak vízzel vagy illatos kölnivel locsolják " +
                        " meg a nőket, kívánva nekik egészséget, boldogságot és termékenységet. " +
                        " A locsolás eredete valószínűleg a megtisztulás és a tavaszi megújulás " +
                        " ősi hiedelmeiből származik. A húsvéti nyúl a tavasz, az új élet és a " +
                        " termékenység jelképe, míg a bárány Krisztus áldozatát, ártatlanságát " +
                        " és a megváltást szimbolizálja. Magyarországon a húsvétot a " +
                        " kereszténység elterjedése óta ünneplik és a népszokások a 19. " +
                        " századtól erősödtek meg."),
                new FeastDay(2034, 3, 22, "A Föld napja", "A Föld napja 1970-ben indult el, amikor az Egyesült Államokban Gaylord " +
                        " Nelson szenátor kezdeményezésére több millió ember gyűlt össze, hogy " +
                        " felhívja a figyelmet a környezetszennyezésre és a természeti erőforrások " +
                        " védelmére. A nap célja, hogy globálisan tudatosítsa az emberekben a " +
                        " környezetvédelem fontosságát és ösztönözze a fenntartható életmódra " +
                        " való áttérést. Azóta minden évben április 22.-én világszerte " +
                        " megemlékeznek róla. Számos esemény, akció és kampány hívja fel a " +
                        " figyelmet a bolygó védelmére és a környezeti problémák megoldására. " +
                        "  " +
                        "  " +
                        " "),
                new FeastDay(2034, 3, 30, "Májusfa állítás", "A májusfa felállításának legjellemzőbb hagyomány szerinti időpontja a " +
                        " május 1-jét megelőző nap délután/este volt.  " +
                        " Magyarországon a középkor óta ismert, tehát legalább a 14. századtól " +
                        " ünneplik. A szokás az ősi tavaszi rituálékra épül, melyek a természet " +
                        " megújulását és az élet körforgását szimbolizálták. A fiatal legények " +
                        " a lányok háza elé állították a feldíszített fákat, hogy kifejezzék " +
                        " szerelmi szándékukat. A májusfát szalagokkal, virágokkal díszítették " +
                        " és május végén vagy pünkösdkor táncos mulatság keretében bontották " +
                        " le, amit kitáncolásnak neveztek. A hagyomány a szerelem és a termékenység " +
                        " ünnepe volt, később pedig összefonódott a munka ünnepével (május 1.). " +
                        " Mára már kevésbé elterjedt szokás, de sok helyen igyekeznek még " +
                        " fenntartani."),
                new FeastDay(2034, 4, 1, "A Munka ünnepe", "Május 1.-e a munka ünnepe. A munkások jogaiért és szolidaritásáért " +
                        " folytatott küzdelem emléknapja világszerte. Az ünnep gyökerei az 1886-os " +
                        " chicagói tüntetésekhez nyúlnak vissza, ahol a munkások a nyolcórás " +
                        " munkaidő bevezetéséért demonstráltak. Az események hatására 1890-től " +
                        " május 1.-ét a munkások nemzetközi szolidaritási napjává " +
                        " nyilvánították.  Magyarországon először 1890-ben tartották meg, de " +
                        " hivatalos állami ünnep csak 1946-ban lett. A szocializmus idején " +
                        " nagyszabású felvonulásokkal és tömegrendezvényekkel ünnepelték. A " +
                        " rendszerváltás után a jelentése átalakult, de továbbra is a munka " +
                        " tiszteletét és a munkavállalók jogainak fontosságát hirdeti. "),
                new FeastDay(2034, 4, 7, "Anyák napja", "Az édesanyák napja a szeretet, a tisztelet és a hála ünnepe, amelyet " +
                        " világszerte az édesanyák tiszteletére tartanak. Gyökerei az ókori " +
                        " Görögországba nyúlnak vissza, ahol Rheát, az istenek anyját ünnepelték. " +
                        " A modern anyák napja az Egyesült Államokból indult 1908-ban, amikor Anna " +
                        " Jarvis kampányt indított édesanyja emlékére. Magyarországon először " +
                        " 1925-ben ünnepelték és a májusi Mária-tisztelet hagyományaival is " +
                        " összekapcsolódott. Ez a nap a gondoskodó és szerető édesanyák, valamint " +
                        " minden női gondviselő iránti megbecsülés kifejezésére szolgál. " +
                        " Virágokkal, ajándékokkal és figyelmességgel fejezik ki hálájukat a " +
                        " gyerekek. Bár világszerte eltérő időpontokban ünneplik, mindenhol az " +
                        " anyák iránti szeretet és tisztelet áll a középpontban. Anyák napját " +
                        " Magyarországon minden év május első vasárnapján ünneplik.  " +
                        " "),
                new FeastDay(2034, 4, 28, "Gyermeknap", "A gyermeknap a gyerekek jogainak és jólétének fontosságát hirdető " +
                        " ünnep, melyet világszerte különböző időpontokban tartanak. Eredete " +
                        " 1925-re nyúlik vissza, amikor a genfi Gyermekjóléti Konferencián " +
                        " elhatározták, hogy kiemelik a gyermekek védelmének fontosságát. " +
                        " Magyarországon 1931-ben tartották meg először, majd 1950 óta május " +
                        " utolsó vasárnapján ünneplik. Ez a nap a gyerekekről szól, akik ilyenkor " +
                        " különféle programokon vehetnek részt, ahol játékokkal és szórakoztató " +
                        " rendezvényekkel ünneplik őket. Az ünnep célja, hogy felhívja a figyelmet " +
                        " a gyermekek jogaira és boldogságuk fontosságára. Magyarországon a " +
                        " Gyermeknapot minden évben május utolsó vasárnapján ünneplik."),
                new FeastDay(2034, 5, 4, "Pünkösd", "Pünkösd a kereszténység egyik legősibb ünnepe, melyet már a 4. " +
                        " századtól kezdve ünnepelnek a keresztény egyházban. Az ünnep Jézus " +
                        " mennybemenetele után ötven nappal emlékezik meg a Szentlélek " +
                        " eljöveteléről, amely erőt adott az apostoloknak a keresztény hit " +
                        " hirdetéséhez. Ez az esemény az egyház születésnapjaként is ismert. A " +
                        " „pünkösd” elnevezés a görög „pentékoszté” szóból ered, ami " +
                        " ötvenediket jelent. Magyarországon a középkor óta tartják számon és " +
                        " számos népi hagyomány kapcsolódik hozzá, például a pünkösdi király " +
                        " és királyné választása. Pünkösd vasárnapját a húsvét utáni 50. " +
                        " napon, tehát 50 nappal húsvétvasárnap után ünneplik. "),
                new FeastDay(2034, 5, 5, "Pünkösdhétfő", "A Pünkösdhétfő a keresztény egyház egyik legfontosabb ünnepének, a " +
                        " pünkösdnek a második napja. Pünkösd a húsvét utáni 50. napon van, és a " +
                        " Szentlélek eljövetelét ünnepli, amikor Jézus tanítványai erőt és " +
                        " útmutatást kaptak küldetésükhöz. Az esemény a keresztény egyház " +
                        " születésnapjának is tekinthető. Az ünnep gyökerei a zsidó pünkösdi " +
                        " ünnepre (Sávuót) vezethetők vissza, amely az aratási hálaadás napja " +
                        " volt. Magyarországon pünkösdhétfő munkaszüneti nap és hivatalosan 1993 " +
                        " óta ünnepeljük újra. Az ünnep a megújulást és a közösség " +
                        " összetartozását jelképezi."),
                new FeastDay(2034, 5, 18, "Apák napja", "Apák napja világszerte az édesapák tiszteletére szolgál, elismerve a " +
                        " családban és a gyermeknevelésben betöltött szerepüket. Az ünnep eredete " +
                        " az Egyesült Államokhoz köthető, ahol először 1910-ben tartották meg " +
                        " Sonora Smart Dodd kezdeményezésére , aki édesapját akarta tiszteletben " +
                        " részesíteni . Magyarországon az apák napja az 1990-es években kezdett " +
                        " elterjedni  és 1994 óta ünnepeljük minden év június harmadik " +
                        " vasárnapján. Célja, hogy elismerje az apák és az apaszerepet betöltő " +
                        " férfiak szeretetét, áldozatvállalását és a családban betöltött " +
                        " szerepét. Ez a nap lehetőséget ad arra, hogy köszönetet mondjunk az apák " +
                        " szeretetéért, támogatásáért és áldozatos munkájáért, amelyet " +
                        " családjukért tesznek. "),
                new FeastDay(2034, 5, 20, "Nyári napforduló", "Az év leghosszabb napja.  " +
                        " Napkelte:  " +
                        " Napnyugta:  " +
                        " Világos időtartam:  " +
                        " A nyári napforduló a leghosszabb nap az évben, amikor a Nap a legmagasabbra " +
                        " emelkedik az égbolton. Ez a jelenség a Föld tengelyferdesége és a " +
                        " keringése miatt következik be. Az északi féltekén a napforduló " +
                        " környékén a Nap több mint 15 órán keresztül a horizont felett " +
                        " tartózkodik. Ezen a napon a Föld az északi féltekén éri el a leghosszabb " +
                        " nappali órákat, így ez a legrövidebb éjszaka."),
                new FeastDay(2034, 7, 20, "Szent István-nap", "Szent István király koronázása körülbelül 1000-ben történt, ezzel " +
                        " megalapozva a keresztény Magyarországot. 1083 augusztus 20.-án szentté " +
                        " avatták a Szent Péter-bazilikában. Az Árpád-korban az István-kultusz az " +
                        " uralkodó dinasztiát támogatta és egységet teremtett a népcsoportok " +
                        " között. 1686-ban XI. Ince pápa elrendelte, hogy az egész katolikus világ " +
                        " emlékezzen meg róla, majd 1771-ben Mária Terézia országos ünneppé tette. " +
                        " Az 1848–49-es forradalom és szabadságharc leverése után egy időre " +
                        " szünetelt az ünneplés, de 1891-ben Ferenc József munkaszüneti nappá " +
                        " nyilvánította. A két világháború között a nemzeti egység " +
                        " szimbólumává vált. 1950-től az új államrendszer ideológiája szerint " +
                        " alakították át az ünnep jelentését, majd 1991-ben ismét állami " +
                        " ünneppé nyilvánították."),
                new FeastDay(2034, 9, 23, "1956-os Forradalom", "Magyarország nemzeti ünnepe, amely az 1956-os forradalom és szabadságharc " +
                        " eseményeire emlékezik. Az ünnep hivatalosan 1989 október 23.-án vált " +
                        " nemzeti ünneppé a rendszerváltás után, amikor a forradalom hőseit " +
                        " ünnepelve tisztelegtek a szabadságért és a demokráciáért folytatott " +
                        " küzdelmük előtt. Az 1956-os forradalom a szovjet megszállás elleni " +
                        " népfelkelés volt, amely bár véresen végződött, alapvető szerepet " +
                        " játszott Magyarország függetlenségi törekvéseiben és a későbbi " +
                        " rendszerváltásban. Az október 23.-i események máig a szabadság és a " +
                        " nemzeti szuverenitás fontos szimbólumai."),
                new FeastDay(2034, 9, 29, "Óraátállítás", "Az őszi óraátállítás (a téli időszámítás kezdete) – (vasárnap), " +
                        " 3:00 → 2:00 " +
                        " Ekkor kell 1 órát visszaállítani az órákat. " +
                        " Az őszi óraátállítás célja, hogy reggel világosabb legyen, így a " +
                        " természetes fényt jobban kihasználjuk. Magyarországon először 1916-ban " +
                        " vezették be az óraátállítást energiatakarékossági okokból. Bár több " +
                        " alkalommal szünetelt, végül 1980-ban újra bevezették, hogy csökkentsék a " +
                        " villamosenergia-felhasználást. A téli időszámítás október utolsó " +
                        " vasárnapján kezdődik, amikor az órákat visszaállítjuk. Ezáltal reggel " +
                        " hamarabb világosodik, de este hamarabb sötétedik.  1996 óta " +
                        " kormányrendelet szabályozza az óraátállítást. " +
                        " "),
                new FeastDay(2034, 10, 1, "Mindenszentek", "Mindenszentek ünnepe november 1.-én van, amikor a katolikus egyház " +
                        " megemlékezik minden üdvözült lélekről, akik elnyerték a mennyországot, " +
                        " de nevüket nem tartják számon a naptárban. Az ünnep eredete a " +
                        " kereszténység előtti halotti kultuszokra is visszavezethető. Hivatalosan a " +
                        " 8. században III. Gergely pápa idején vezették be, aki a Szent " +
                        " Péter-bazilikában kápolnát szentelt minden szent tiszteletére. " +
                        " Magyarországon munkaszüneti nap. Sokan virágokkal és gyertyagyújtással " +
                        " tisztelegnek elhunyt szeretteik emléke előtt. Ez a nap a hit és a " +
                        " megemlékezés ünnepe."),
                new FeastDay(2034, 10, 2, "Halottak napja", "Halottak napja november 2.-án van, amikor a katolikus egyház megemlékezik az " +
                        " elhunyt hívekről. Az ünnep célja, hogy imádságokkal és jó " +
                        " cselekedetekkel segítsük a halottak lelki üdvét és tisztelegjünk " +
                        " szeretteink emléke előtt. Bár gyökerei a kereszténység előtti halotti " +
                        " kultuszokig nyúlnak vissza, mai formáját a 11. században Cluny-i Szent " +
                        " Odiló apát vezette be. Ő rendelte el, hogy a kolostorok imádkozzanak a " +
                        " tisztítótűzben szenvedő lelkekért. A gyertyagyújtás és a virágok " +
                        " elhelyezése máig az emlékezés fontos része. "),
                new FeastDay(2034, 11, 3, "Advent első napja", "Advent a karácsony előtti négyhetes időszak, mely Jézus Krisztus " +
                        " születésére és második eljövetelére való várakozást jelenti. Az " +
                        " advent származása a 4. századra nyúlik vissza, amikor bűnbánattal és " +
                        " lelki felkészüléssel várták a karácsonyt. Kezdetben hosszabb böjti " +
                        " időszak volt, de fokozatosan négyhetesre csökkent. Az adventi koszorú " +
                        " hagyománya a 19. században terjedt el, az örökzöld növények a " +
                        " halhatatlanságot és az újjászületést szimbolizálták. A négy gyertya a " +
                        " hitet, a reményt, az örömöt és a szeretetet jelképezi. Minden vasárnap " +
                        " egy újabb gyertya meggyújtásával közelebb kerülünk a karácsonyhoz.  " +
                        " Advent első vasárnapját a karácsonyt megelőző négy vasárnap egyikeként " +
                        " ünneplik. Ez a nap általában november végére vagy december elejére esik.  " +
                        " "),
                new FeastDay(2034, 11, 6, "Mikulás", " Mikuláskor december 5.-én éjszaka, vagy 6.-án reggel töltik meg a gyerekek " +
                        " csizmáit édességgel és játékokkal. Szent Miklós püspök emlék napja. A " +
                        " 4. században élt szent, aki a gyermekek, a hajósok és a szegények " +
                        " védőszentje lett, híres volt jótékonyságáról. Egy legenda szerint " +
                        " titokban segített egy szegény ember három lányán, akiket így férjhez " +
                        " tudott adni. " +
                        " Ebből a történetből született a hagyomány, hogy Szent Miklós éjjelente " +
                        " ajándékokat hoz a jó gyerekeknek. Ma már világszerte ismert a piros " +
                        " ruhás, fehér szakállú Mikulás.  A Mikulás-hagyományok országonként " +
                        " eltérőek. Egyes helyeken a Mikulás hozza az ajándékokat, máshol a " +
                        " Télapó. A lényeg azonban mindenhol ugyanaz: a szeretet az adakozás és a " +
                        " gyermeki öröm ünnepe"),
                new FeastDay(2034, 11, 13, "Luca napja", "Luca napja a keresztény és a népi hagyományok keveréke. Szent Luca a 3. " +
                        " századi vértanú, a látás védőszentje, de a népi hitvilágban a " +
                        " termékenység, a jólét és a védelem szimbóluma. A hagyomány szerint ezen " +
                        " a napon kezdődött a tél leghosszabb éjszakája, így Luca napja a fény és " +
                        " a sötétség határán áll. Ezen a napon a nők nem dolgoztak, hogy bőséges " +
                        " legyen a következő év termése. Szokás volt Luca-széket készíteni, " +
                        " búzát ültetni és jóslásokat végezni. Luca napja a megújulás és a " +
                        " remény ünnepe, a fény győzelme a sötétség felett és a következő év " +
                        " jólétének biztosítása. " +
                        " "),
                new FeastDay(2034, 11, 21, "Téli napforduló", "Az év legrövidebb napja nap.  " +
                        " Napkelte:  " +
                        " Napnyugta:  " +
                        " Világos időtartam:  " +
                        " A téli napforduló a legrövidebb nap az évben, amikor a Nap a " +
                        " legalacsonyabbra emelkedik az égbolton. Ez a jelenség a Föld " +
                        " tengelyferdesége és keringése miatt következik be. Az északi féltekén a " +
                        " napforduló környékén a Nap csak 8 órán keresztül tartózkodik a horizont " +
                        " felett, így ez a leghosszabb éjszaka. Ezen a napon kezdődik a nappali fény " +
                        " fokozatos növekedése, így a következő hónapokban egyre hosszabbak lesznek " +
                        " a nappalok."),
                new FeastDay(2034, 11, 24, "Szenteste", "Szenteste a karácsonyi ünnepkör egyik legfontosabb napja, amely Jézus " +
                        " Krisztus születésének előestéje. Bár Jézus pontos születési dátuma " +
                        " nem ismert, a keresztény egyház a 4. században december 25.-ét jelölte meg " +
                        " születésének napjaként, összekapcsolva azt a téli napfordulóval. " +
                        " December 24.-én este a családok összegyűlnek, közösen vacsoráznak, " +
                        " ajándékokat adnak egymásnak és karácsonyfát állítanak. Magyarországon " +
                        " a hagyomány szerint a Jézuska hozza az ajándékokat, ezzel is megemlékezve " +
                        " Jézus születésének csodájáról. A fenyőfa, amely a 16. századtól vált " +
                        " a karácsony szimbólumává, az örök életet jelképezi. Az ajándékozás a " +
                        " szeretet és a megbecsülés kifejezése, amely a három királyok " +
                        " ajándékozására utal. Karácsony, bár elsősorban keresztény ünnep, " +
                        " világszerte a szeretet, a béke és a család ünnepe lett."),
                new FeastDay(2034, 11, 25, "Karácsony", "A keresztények számára Jézus Krisztus születésének ünnepe, melyet a 4. " +
                        " század óta tartanak. Az ünnep a téli napfordulóhoz kapcsolódik, " +
                        " szimbolizálva a fény győzelmét a sötétség felett. Karácsony a remény, " +
                        " a szeretet és a megváltás ünnepe, amely a családi együttlét, az öröm " +
                        " és a vallási szertartások ideje. Karácsonyfa, betlehem és csillag a " +
                        " szimbólumok, amelyek Jézus születését, az örök életet és az " +
                        " útmutatást képviselik. Az ünneplés világszerte eltérő, de mindegyik " +
                        " kultúrában a szeretet és a béke fontosságát hangsúlyozza. Karácsony " +
                        " lehetőséget ad az emberi kapcsolatok és az élet értelmének " +
                        " átgondolására"),
                new FeastDay(2034, 11, 26, "Karácsony másnapja", "A keresztény hagyományban Szent István, az első vértanú emléknapja, " +
                        " azonban Magyarországon elsősorban Szent István királyra, az " +
                        " államalapítóra emlékeznek. A 4. századi eredetű ünnep során Jézus " +
                        " születése után következik Szent István napja, aki kulcsszerepet játszott " +
                        " a kereszténység elterjesztésében Magyarországon. Bár Szent István a 11. " +
                        " században élt, az egyház később tette december 26.-át emlékévé. " +
                        " Magyarországon nemzeti hősként is tisztelik, mivel ő alapította az " +
                        " államot. Ezen a napon az emberek szentmisére mennek, megemlékeznek " +
                        " királyukról és különböző ünnepi rendezvényeken is részt vesznek"),
                new FeastDay(2034, 11, 31, "Szilveszter", "A Szilveszter az év utolsó napja, amikor búcsút veszünk az óévtől és " +
                        " ünnepeljük az új év kezdetét. Az ünnep neve Szent Szilveszter pápától " +
                        " származik, aki 335 körül halt meg, de az újév ünneplésének hagyományai " +
                        " sokkal régebbre nyúlnak vissza. Szilveszter éjszakáján családok és " +
                        " barátok együtt ünnepelnek, vacsoráznak, tűzijátékot néznek és " +
                        " jókívánságokat mondanak. A tűzijáték és a zajkeltés az ősi " +
                        " hagyományokból ered. Az ünnep a megújulás, az összetartozás és a " +
                        " remény kifejezője, miközben gazdaságilag is fontos szerepet játszik a " +
                        " fogyasztói társadalomban. " +
                        "  " +
                        " Boldog Új Évet kívánok! Legyen tele örömmel, egészséggel és sikerrel. " +
                        " Hozzon az új év boldog pillanatokat, új lehetőségeket és boldog együtt " +
                        " töltött időt!"),
                new FeastDay(2035, 0, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma. " +
                        " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az " +
                        " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy " +
                        " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és " +
                        " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a " +
                        " mondás. Legyen ez a nap a változás első lépése. Boldogságot, " +
                        " egészséget és kitartást kívánok az új év minden pillanatára! "
                ),
                new FeastDay(2035, 0, 6, "Vízkereszt", " A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a " +
                        " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus " +
                        " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus " +
                        " megkeresztelkedésére és az első csodájára. A három királyok aranyat, " +
                        " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a " +
                        " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet " +
                        " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi " +
                        " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak " +
                        " házszenteléseket, Isten áldását kérve az otthonra és az új évre."),
                new FeastDay(2035, 1, 14, "Valentin nap", "A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint " +
                        " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században " +
                        " titokban esketett meg szerelmespárokat, mert II. Claudius császár " +
                        " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák. " +
                        " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az " +
                        " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a " +
                        " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal " +
                        " és üdvözlőkártyákkal ünneplik."),
                new FeastDay(2035, 2, 1, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja, " +
                        " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a " +
                        " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A " +
                        " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és " +
                        " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki " +
                        " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának " +
                        " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel, " +
                        " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére " +
                        " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A " +
                        " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap " +
                        " előtt tartanak. Általában február végére vagy március elejére esik."),
                new FeastDay(2035, 2, 8, "Nőnap", " Az első Nőnapot 1908 március 8.-án tartották. A női munkások " +
                        " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az " +
                        " ünnep célja a nők társadalmi, politikai és gazdasági jogainak " +
                        " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben " +
                        " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így " +
                        " 1911-ben már több európai országban is megünnepelték. Magyarországon " +
                        " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már " +
                        " a nők jogainak elismerésére és tiszteletére emelkedett."),
                new FeastDay(2035, 2, 15, "1848-as Forradalom", "Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a " +
                        " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti " +
                        " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a " +
                        " sajtószabadságért, a jobb életkörülményekért és a függetlenségért. " +
                        " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című " +
                        " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet " +
                        " függetlenségi törekvése a magyar történelem egyik legfontosabb " +
                        " eseményévé vált. Az események hatására megerősödött a nemzeti " +
                        " identitás és ma már a magyar szabadság és összetartozás napjaként " +
                        " ünnepeljük. " +
                        " "),
                new FeastDay(2035, 2, 26, "Óraátállítás", " A tavaszi óraátállítás a nyári időszámítás kezdete.  " +
                        " A tavaszi óraátállításkor az órákat hajnali 2:00-ról 3:00-ra állítjuk " +
                        " előre, hogy jobban kihasználjuk a nappali világosságot és csökkentsük a " +
                        " mesterséges világítás szükségességét. Magyarországon először " +
                        " 1916-ban az első világháború alatt vezették be az energiatakarékosság " +
                        " érdekében, de a háború után megszüntették. 1954 és 1957 között újra " +
                        " alkalmazták, mert az ipari munkahelyeken az esti energiafogyasztás " +
                        " csökkentésére volt szükség, de 1958-ban a lakossági ellenállás és az " +
                        " alacsony megtakarítás miatt eltörölték. 1980-ban ismét bevezették a " +
                        " villamosenergia-megtakarítás érdekében. Azóta évente kétszer állítjuk " +
                        " át az órákat és 1996 óta kormányrendelet szabályozza az " +
                        " óraátállítást Magyarországon."),
                new FeastDay(2035, 3, 1, "Bolondok napja", "Április elseje a bolondok napja, a bolondos tréfák és ártalmatlan " +
                        " átverések napja. Az emberek apró csínyekkel, például hamis hírekkel vagy " +
                        " kitalált történetekkel szórakoztatják egymást. Az internet " +
                        " elterjedésével az álhírek és vírusos videók is népszerűvé váltak. A " +
                        " nap célja, hogy jókedvet és nevetést hozzon, miközben erősíti a " +
                        " közösségi szellemet. Eredete több elméletre vezethető vissza. A " +
                        " legnépszerűbb magyarázat szerint a XVI. században, a Gergely-naptár " +
                        " bevezetésekor azok, akik áprilisban ünnepelték az újévet, " +
                        " Franciaországban bolondnak számítottak, így tréfák céltáblájává " +
                        " váltak. Hasonló szokások figyelhetők meg a kelta tavaszi ünnepekben és az " +
                        " ókori római Hilaria ünnepén is, ahol a bolondos viselkedés a tavaszi " +
                        " megújulást jelezte. "),
                new FeastDay(2035, 3, 9, "Virágvasárnap", " A Virágvasárnap a húsvét előtti vasárnap, Jézus jeruzsálemi " +
                        " bevonulásának emlékére van. Az evangéliumok szerint Jézus szamárháton " +
                        " vonult be a városba, miközben a hívek pálmaágakat lengettek, üdvözölve " +
                        " őt, mint a Megváltót. A virágok, a pálmák és a zöld ágak használata " +
                        " az ünnep jelképei, melyek a békét és az üdvözítőt hirdetik. A " +
                        " keresztény hagyományban Virágvasárnap a nagyböjt végét és a húsvéti " +
                        " ünnepekre való lelki felkészülést jelzi. Az ünnep hivatalosan a 4. " +
                        " század óta van jelen az egyházi naptárban és azóta része a keresztény " +
                        " hagyományoknak világszerte."),
                new FeastDay(2035, 3, 14, "Nagypéntek", "A Nagypéntek a keresztény húsvéti ünnepkör legfontosabb napja, amikor " +
                        " Jézus Krisztus kereszthalálát és szenvedését emlékezzük meg. Az ünnep " +
                        " célja, hogy a hívők átgondolják a megváltásra tett áldozat " +
                        " jelentőségét. E napot a 4. század óta ünneplik, amikor a keresztény " +
                        " egyház hivatalosan is elismerte Jézus kereszthalálának fontosságát. " +
                        " Magyarországon a 20. század elejétől munkaszüneti nap. A keresztény " +
                        " hagyományokban sötét napként van jelen, amikor sokan böjtölnek és " +
                        " csendes megemlékezéseket tartanak. Az egyházi szertartások mellett a " +
                        " keresztény közösség számára Nagypéntek a bűnbánat és a lelki " +
                        " megújulás napja is.  Nagypénteket a húsvét előtti pénteken ünneplik, " +
                        " tehát ez a dátum évente változik.  " +
                        " "),
                new FeastDay(2035, 3, 16, "Húsvétvasárnap", "Húsvétvasárnap a kereszténység legfontosabb ünnepe, Jézus Krisztus " +
                        " feltámadásának napja, amely a halál feletti győzelmet és az örök élet " +
                        " ígéretét hirdeti. Magyarországon 1991 óta munkaszüneti nap, de " +
                        " jelentősége évszázadok óta meghatározó. Húsvét a 40 napos nagyböjt " +
                        " végét jelzi, amely a bűnbánat és a lelki megújulás időszaka. A gyerekek " +
                        " az ajándékaikat hagyományosan ezen a napon kapják meg, édességeket és " +
                        " tojásokat, amelyeket a húsvéti nyúl hoz.  A húsvéti nyúl a 16. századi " +
                        " Németországból ered és a termékenység, valamint az újjászületés " +
                        " jelképe. Eredete pogány hagyományokhoz köthető, ahol a nyúl és a tojás " +
                        " a tavaszi újjászületés szimbóluma volt, melyek később beépültek a " +
                        " keresztény húsvéti szokásokba. " +
                        " A Húsvétot évente változó dátumon ünneplik, mivel a tavaszi " +
                        " napéjegyenlőség utáni első teliholdat követő vasárnapra esik."),
                new FeastDay(2035, 3, 17, "Húsvéthétfő", "Húsvéthétfő a húsvéti ünnepkör egyik legfontosabb napja, amely Jézus " +
                        " Krisztus feltámadását követően az új élet és a megújulás " +
                        " szimbólumává vált. Magyarországon a húsvéti locsolás hagyománya a 17. " +
                        " századtól terjedt el. A férfiak vízzel vagy illatos kölnivel locsolják " +
                        " meg a nőket, kívánva nekik egészséget, boldogságot és termékenységet. " +
                        " A locsolás eredete valószínűleg a megtisztulás és a tavaszi megújulás " +
                        " ősi hiedelmeiből származik. A húsvéti nyúl a tavasz, az új élet és a " +
                        " termékenység jelképe, míg a bárány Krisztus áldozatát, ártatlanságát " +
                        " és a megváltást szimbolizálja. Magyarországon a húsvétot a " +
                        " kereszténység elterjedése óta ünneplik és a népszokások a 19. " +
                        " századtól erősödtek meg."),
                new FeastDay(2035, 3, 22, "A Föld napja", "A Föld napja 1970-ben indult el, amikor az Egyesült Államokban Gaylord " +
                        " Nelson szenátor kezdeményezésére több millió ember gyűlt össze, hogy " +
                        " felhívja a figyelmet a környezetszennyezésre és a természeti erőforrások " +
                        " védelmére. A nap célja, hogy globálisan tudatosítsa az emberekben a " +
                        " környezetvédelem fontosságát és ösztönözze a fenntartható életmódra " +
                        " való áttérést. Azóta minden évben április 22.-én világszerte " +
                        " megemlékeznek róla. Számos esemény, akció és kampány hívja fel a " +
                        " figyelmet a bolygó védelmére és a környezeti problémák megoldására. " +
                        "  " +
                        "  " +
                        " "),
                new FeastDay(2035, 3, 30, "Májusfa állítás", "A májusfa felállításának legjellemzőbb hagyomány szerinti időpontja a " +
                        " május 1-jét megelőző nap délután/este volt.  " +
                        " Magyarországon a középkor óta ismert, tehát legalább a 14. századtól " +
                        " ünneplik. A szokás az ősi tavaszi rituálékra épül, melyek a természet " +
                        " megújulását és az élet körforgását szimbolizálták. A fiatal legények " +
                        " a lányok háza elé állították a feldíszített fákat, hogy kifejezzék " +
                        " szerelmi szándékukat. A májusfát szalagokkal, virágokkal díszítették " +
                        " és május végén vagy pünkösdkor táncos mulatság keretében bontották " +
                        " le, amit kitáncolásnak neveztek. A hagyomány a szerelem és a termékenység " +
                        " ünnepe volt, később pedig összefonódott a munka ünnepével (május 1.). " +
                        " Mára már kevésbé elterjedt szokás, de sok helyen igyekeznek még " +
                        " fenntartani."),
                new FeastDay(2035, 4, 1, "A Munka ünnepe", "Május 1.-e a munka ünnepe. A munkások jogaiért és szolidaritásáért " +
                        " folytatott küzdelem emléknapja világszerte. Az ünnep gyökerei az 1886-os " +
                        " chicagói tüntetésekhez nyúlnak vissza, ahol a munkások a nyolcórás " +
                        " munkaidő bevezetéséért demonstráltak. Az események hatására 1890-től " +
                        " május 1.-ét a munkások nemzetközi szolidaritási napjává " +
                        " nyilvánították.  Magyarországon először 1890-ben tartották meg, de " +
                        " hivatalos állami ünnep csak 1946-ban lett. A szocializmus idején " +
                        " nagyszabású felvonulásokkal és tömegrendezvényekkel ünnepelték. A " +
                        " rendszerváltás után a jelentése átalakult, de továbbra is a munka " +
                        " tiszteletét és a munkavállalók jogainak fontosságát hirdeti. "),
                new FeastDay(2035, 4, 7, "Anyák napja", "Az édesanyák napja a szeretet, a tisztelet és a hála ünnepe, amelyet " +
                        " világszerte az édesanyák tiszteletére tartanak. Gyökerei az ókori " +
                        " Görögországba nyúlnak vissza, ahol Rheát, az istenek anyját ünnepelték. " +
                        " A modern anyák napja az Egyesült Államokból indult 1908-ban, amikor Anna " +
                        " Jarvis kampányt indított édesanyja emlékére. Magyarországon először " +
                        " 1925-ben ünnepelték és a májusi Mária-tisztelet hagyományaival is " +
                        " összekapcsolódott. Ez a nap a gondoskodó és szerető édesanyák, valamint " +
                        " minden női gondviselő iránti megbecsülés kifejezésére szolgál. " +
                        " Virágokkal, ajándékokkal és figyelmességgel fejezik ki hálájukat a " +
                        " gyerekek. Bár világszerte eltérő időpontokban ünneplik, mindenhol az " +
                        " anyák iránti szeretet és tisztelet áll a középpontban. Anyák napját " +
                        " Magyarországon minden év május első vasárnapján ünneplik.  " +
                        " "),
                new FeastDay(2035, 4, 28, "Gyermeknap", "A gyermeknap a gyerekek jogainak és jólétének fontosságát hirdető " +
                        " ünnep, melyet világszerte különböző időpontokban tartanak. Eredete " +
                        " 1925-re nyúlik vissza, amikor a genfi Gyermekjóléti Konferencián " +
                        " elhatározták, hogy kiemelik a gyermekek védelmének fontosságát. " +
                        " Magyarországon 1931-ben tartották meg először, majd 1950 óta május " +
                        " utolsó vasárnapján ünneplik. Ez a nap a gyerekekről szól, akik ilyenkor " +
                        " különféle programokon vehetnek részt, ahol játékokkal és szórakoztató " +
                        " rendezvényekkel ünneplik őket. Az ünnep célja, hogy felhívja a figyelmet " +
                        " a gyermekek jogaira és boldogságuk fontosságára. Magyarországon a " +
                        " Gyermeknapot minden évben május utolsó vasárnapján ünneplik."),
                new FeastDay(2035, 5, 4, "Pünkösd", "Pünkösd a kereszténység egyik legősibb ünnepe, melyet már a 4. " +
                        " századtól kezdve ünnepelnek a keresztény egyházban. Az ünnep Jézus " +
                        " mennybemenetele után ötven nappal emlékezik meg a Szentlélek " +
                        " eljöveteléről, amely erőt adott az apostoloknak a keresztény hit " +
                        " hirdetéséhez. Ez az esemény az egyház születésnapjaként is ismert. A " +
                        " „pünkösd” elnevezés a görög „pentékoszté” szóból ered, ami " +
                        " ötvenediket jelent. Magyarországon a középkor óta tartják számon és " +
                        " számos népi hagyomány kapcsolódik hozzá, például a pünkösdi király " +
                        " és királyné választása. Pünkösd vasárnapját a húsvét utáni 50. " +
                        " napon, tehát 50 nappal húsvétvasárnap után ünneplik. "),
                new FeastDay(2035, 5, 5, "Pünkösdhétfő", "A Pünkösdhétfő a keresztény egyház egyik legfontosabb ünnepének, a " +
                        " pünkösdnek a második napja. Pünkösd a húsvét utáni 50. napon van, és a " +
                        " Szentlélek eljövetelét ünnepli, amikor Jézus tanítványai erőt és " +
                        " útmutatást kaptak küldetésükhöz. Az esemény a keresztény egyház " +
                        " születésnapjának is tekinthető. Az ünnep gyökerei a zsidó pünkösdi " +
                        " ünnepre (Sávuót) vezethetők vissza, amely az aratási hálaadás napja " +
                        " volt. Magyarországon pünkösdhétfő munkaszüneti nap és hivatalosan 1993 " +
                        " óta ünnepeljük újra. Az ünnep a megújulást és a közösség " +
                        " összetartozását jelképezi."),
                new FeastDay(2035, 5, 18, "Apák napja", "Apák napja világszerte az édesapák tiszteletére szolgál, elismerve a " +
                        " családban és a gyermeknevelésben betöltött szerepüket. Az ünnep eredete " +
                        " az Egyesült Államokhoz köthető, ahol először 1910-ben tartották meg " +
                        " Sonora Smart Dodd kezdeményezésére , aki édesapját akarta tiszteletben " +
                        " részesíteni . Magyarországon az apák napja az 1990-es években kezdett " +
                        " elterjedni  és 1994 óta ünnepeljük minden év június harmadik " +
                        " vasárnapján. Célja, hogy elismerje az apák és az apaszerepet betöltő " +
                        " férfiak szeretetét, áldozatvállalását és a családban betöltött " +
                        " szerepét. Ez a nap lehetőséget ad arra, hogy köszönetet mondjunk az apák " +
                        " szeretetéért, támogatásáért és áldozatos munkájáért, amelyet " +
                        " családjukért tesznek. "),
                new FeastDay(2035, 5, 20, "Nyári napforduló", "Az év leghosszabb napja.  " +
                        " Napkelte:  " +
                        " Napnyugta:  " +
                        " Világos időtartam:  " +
                        " A nyári napforduló a leghosszabb nap az évben, amikor a Nap a legmagasabbra " +
                        " emelkedik az égbolton. Ez a jelenség a Föld tengelyferdesége és a " +
                        " keringése miatt következik be. Az északi féltekén a napforduló " +
                        " környékén a Nap több mint 15 órán keresztül a horizont felett " +
                        " tartózkodik. Ezen a napon a Föld az északi féltekén éri el a leghosszabb " +
                        " nappali órákat, így ez a legrövidebb éjszaka."),
                new FeastDay(2035, 7, 20, "Szent István-nap", "Szent István király koronázása körülbelül 1000-ben történt, ezzel " +
                        " megalapozva a keresztény Magyarországot. 1083 augusztus 20.-án szentté " +
                        " avatták a Szent Péter-bazilikában. Az Árpád-korban az István-kultusz az " +
                        " uralkodó dinasztiát támogatta és egységet teremtett a népcsoportok " +
                        " között. 1686-ban XI. Ince pápa elrendelte, hogy az egész katolikus világ " +
                        " emlékezzen meg róla, majd 1771-ben Mária Terézia országos ünneppé tette. " +
                        " Az 1848–49-es forradalom és szabadságharc leverése után egy időre " +
                        " szünetelt az ünneplés, de 1891-ben Ferenc József munkaszüneti nappá " +
                        " nyilvánította. A két világháború között a nemzeti egység " +
                        " szimbólumává vált. 1950-től az új államrendszer ideológiája szerint " +
                        " alakították át az ünnep jelentését, majd 1991-ben ismét állami " +
                        " ünneppé nyilvánították."),
                new FeastDay(2035, 9, 23, "1956-os Forradalom", "Magyarország nemzeti ünnepe, amely az 1956-os forradalom és szabadságharc " +
                        " eseményeire emlékezik. Az ünnep hivatalosan 1989 október 23.-án vált " +
                        " nemzeti ünneppé a rendszerváltás után, amikor a forradalom hőseit " +
                        " ünnepelve tisztelegtek a szabadságért és a demokráciáért folytatott " +
                        " küzdelmük előtt. Az 1956-os forradalom a szovjet megszállás elleni " +
                        " népfelkelés volt, amely bár véresen végződött, alapvető szerepet " +
                        " játszott Magyarország függetlenségi törekvéseiben és a későbbi " +
                        " rendszerváltásban. Az október 23.-i események máig a szabadság és a " +
                        " nemzeti szuverenitás fontos szimbólumai."),
                new FeastDay(2035, 9, 29, "Óraátállítás", "Az őszi óraátállítás (a téli időszámítás kezdete) – (vasárnap), " +
                        " 3:00 → 2:00 " +
                        " Ekkor kell 1 órát visszaállítani az órákat. " +
                        " Az őszi óraátállítás célja, hogy reggel világosabb legyen, így a " +
                        " természetes fényt jobban kihasználjuk. Magyarországon először 1916-ban " +
                        " vezették be az óraátállítást energiatakarékossági okokból. Bár több " +
                        " alkalommal szünetelt, végül 1980-ban újra bevezették, hogy csökkentsék a " +
                        " villamosenergia-felhasználást. A téli időszámítás október utolsó " +
                        " vasárnapján kezdődik, amikor az órákat visszaállítjuk. Ezáltal reggel " +
                        " hamarabb világosodik, de este hamarabb sötétedik.  1996 óta " +
                        " kormányrendelet szabályozza az óraátállítást. " +
                        " "),
                new FeastDay(2035, 10, 1, "Mindenszentek", "Mindenszentek ünnepe november 1.-én van, amikor a katolikus egyház " +
                        " megemlékezik minden üdvözült lélekről, akik elnyerték a mennyországot, " +
                        " de nevüket nem tartják számon a naptárban. Az ünnep eredete a " +
                        " kereszténység előtti halotti kultuszokra is visszavezethető. Hivatalosan a " +
                        " 8. században III. Gergely pápa idején vezették be, aki a Szent " +
                        " Péter-bazilikában kápolnát szentelt minden szent tiszteletére. " +
                        " Magyarországon munkaszüneti nap. Sokan virágokkal és gyertyagyújtással " +
                        " tisztelegnek elhunyt szeretteik emléke előtt. Ez a nap a hit és a " +
                        " megemlékezés ünnepe."),
                new FeastDay(2035, 10, 2, "Halottak napja", "Halottak napja november 2.-án van, amikor a katolikus egyház megemlékezik az " +
                        " elhunyt hívekről. Az ünnep célja, hogy imádságokkal és jó " +
                        " cselekedetekkel segítsük a halottak lelki üdvét és tisztelegjünk " +
                        " szeretteink emléke előtt. Bár gyökerei a kereszténység előtti halotti " +
                        " kultuszokig nyúlnak vissza, mai formáját a 11. században Cluny-i Szent " +
                        " Odiló apát vezette be. Ő rendelte el, hogy a kolostorok imádkozzanak a " +
                        " tisztítótűzben szenvedő lelkekért. A gyertyagyújtás és a virágok " +
                        " elhelyezése máig az emlékezés fontos része. "),
                new FeastDay(2035, 11, 3, "Advent első napja", "Advent a karácsony előtti négyhetes időszak, mely Jézus Krisztus " +
                        " születésére és második eljövetelére való várakozást jelenti. Az " +
                        " advent származása a 4. századra nyúlik vissza, amikor bűnbánattal és " +
                        " lelki felkészüléssel várták a karácsonyt. Kezdetben hosszabb böjti " +
                        " időszak volt, de fokozatosan négyhetesre csökkent. Az adventi koszorú " +
                        " hagyománya a 19. században terjedt el, az örökzöld növények a " +
                        " halhatatlanságot és az újjászületést szimbolizálták. A négy gyertya a " +
                        " hitet, a reményt, az örömöt és a szeretetet jelképezi. Minden vasárnap " +
                        " egy újabb gyertya meggyújtásával közelebb kerülünk a karácsonyhoz.  " +
                        " Advent első vasárnapját a karácsonyt megelőző négy vasárnap egyikeként " +
                        " ünneplik. Ez a nap általában november végére vagy december elejére esik.  " +
                        " "),
                new FeastDay(2035, 11, 6, "Mikulás", " Mikuláskor december 5.-én éjszaka, vagy 6.-án reggel töltik meg a gyerekek " +
                        " csizmáit édességgel és játékokkal. Szent Miklós püspök emlék napja. A " +
                        " 4. században élt szent, aki a gyermekek, a hajósok és a szegények " +
                        " védőszentje lett, híres volt jótékonyságáról. Egy legenda szerint " +
                        " titokban segített egy szegény ember három lányán, akiket így férjhez " +
                        " tudott adni. " +
                        " Ebből a történetből született a hagyomány, hogy Szent Miklós éjjelente " +
                        " ajándékokat hoz a jó gyerekeknek. Ma már világszerte ismert a piros " +
                        " ruhás, fehér szakállú Mikulás.  A Mikulás-hagyományok országonként " +
                        " eltérőek. Egyes helyeken a Mikulás hozza az ajándékokat, máshol a " +
                        " Télapó. A lényeg azonban mindenhol ugyanaz: a szeretet az adakozás és a " +
                        " gyermeki öröm ünnepe"),
                new FeastDay(2035, 11, 13, "Luca napja", "Luca napja a keresztény és a népi hagyományok keveréke. Szent Luca a 3. " +
                        " századi vértanú, a látás védőszentje, de a népi hitvilágban a " +
                        " termékenység, a jólét és a védelem szimbóluma. A hagyomány szerint ezen " +
                        " a napon kezdődött a tél leghosszabb éjszakája, így Luca napja a fény és " +
                        " a sötétség határán áll. Ezen a napon a nők nem dolgoztak, hogy bőséges " +
                        " legyen a következő év termése. Szokás volt Luca-széket készíteni, " +
                        " búzát ültetni és jóslásokat végezni. Luca napja a megújulás és a " +
                        " remény ünnepe, a fény győzelme a sötétség felett és a következő év " +
                        " jólétének biztosítása. " +
                        " "),
                new FeastDay(2035, 11, 21, "Téli napforduló", "Az év legrövidebb napja nap.  " +
                        " Napkelte:  " +
                        " Napnyugta:  " +
                        " Világos időtartam:  " +
                        " A téli napforduló a legrövidebb nap az évben, amikor a Nap a " +
                        " legalacsonyabbra emelkedik az égbolton. Ez a jelenség a Föld " +
                        " tengelyferdesége és keringése miatt következik be. Az északi féltekén a " +
                        " napforduló környékén a Nap csak 8 órán keresztül tartózkodik a horizont " +
                        " felett, így ez a leghosszabb éjszaka. Ezen a napon kezdődik a nappali fény " +
                        " fokozatos növekedése, így a következő hónapokban egyre hosszabbak lesznek " +
                        " a nappalok."),
                new FeastDay(2035, 11, 24, "Szenteste", "Szenteste a karácsonyi ünnepkör egyik legfontosabb napja, amely Jézus " +
                        " Krisztus születésének előestéje. Bár Jézus pontos születési dátuma " +
                        " nem ismert, a keresztény egyház a 4. században december 25.-ét jelölte meg " +
                        " születésének napjaként, összekapcsolva azt a téli napfordulóval. " +
                        " December 24.-én este a családok összegyűlnek, közösen vacsoráznak, " +
                        " ajándékokat adnak egymásnak és karácsonyfát állítanak. Magyarországon " +
                        " a hagyomány szerint a Jézuska hozza az ajándékokat, ezzel is megemlékezve " +
                        " Jézus születésének csodájáról. A fenyőfa, amely a 16. századtól vált " +
                        " a karácsony szimbólumává, az örök életet jelképezi. Az ajándékozás a " +
                        " szeretet és a megbecsülés kifejezése, amely a három királyok " +
                        " ajándékozására utal. Karácsony, bár elsősorban keresztény ünnep, " +
                        " világszerte a szeretet, a béke és a család ünnepe lett."),
                new FeastDay(2035, 11, 25, "Karácsony", "A keresztények számára Jézus Krisztus születésének ünnepe, melyet a 4. " +
                        " század óta tartanak. Az ünnep a téli napfordulóhoz kapcsolódik, " +
                        " szimbolizálva a fény győzelmét a sötétség felett. Karácsony a remény, " +
                        " a szeretet és a megváltás ünnepe, amely a családi együttlét, az öröm " +
                        " és a vallási szertartások ideje. Karácsonyfa, betlehem és csillag a " +
                        " szimbólumok, amelyek Jézus születését, az örök életet és az " +
                        " útmutatást képviselik. Az ünneplés világszerte eltérő, de mindegyik " +
                        " kultúrában a szeretet és a béke fontosságát hangsúlyozza. Karácsony " +
                        " lehetőséget ad az emberi kapcsolatok és az élet értelmének " +
                        " átgondolására"),
                new FeastDay(2035, 11, 26, "Karácsony másnapja", "A keresztény hagyományban Szent István, az első vértanú emléknapja, " +
                        " azonban Magyarországon elsősorban Szent István királyra, az " +
                        " államalapítóra emlékeznek. A 4. századi eredetű ünnep során Jézus " +
                        " születése után következik Szent István napja, aki kulcsszerepet játszott " +
                        " a kereszténység elterjesztésében Magyarországon. Bár Szent István a 11. " +
                        " században élt, az egyház később tette december 26.-át emlékévé. " +
                        " Magyarországon nemzeti hősként is tisztelik, mivel ő alapította az " +
                        " államot. Ezen a napon az emberek szentmisére mennek, megemlékeznek " +
                        " királyukról és különböző ünnepi rendezvényeken is részt vesznek"),
                new FeastDay(2035, 11, 31, "Szilveszter", "A Szilveszter az év utolsó napja, amikor búcsút veszünk az óévtől és " +
                        " ünnepeljük az új év kezdetét. Az ünnep neve Szent Szilveszter pápától " +
                        " származik, aki 335 körül halt meg, de az újév ünneplésének hagyományai " +
                        " sokkal régebbre nyúlnak vissza. Szilveszter éjszakáján családok és " +
                        " barátok együtt ünnepelnek, vacsoráznak, tűzijátékot néznek és " +
                        " jókívánságokat mondanak. A tűzijáték és a zajkeltés az ősi " +
                        " hagyományokból ered. Az ünnep a megújulás, az összetartozás és a " +
                        " remény kifejezője, miközben gazdaságilag is fontos szerepet játszik a " +
                        " fogyasztói társadalomban. " +
                        "  " +
                        " Boldog Új Évet kívánok! Legyen tele örömmel, egészséggel és sikerrel. " +
                        " Hozzon az új év boldog pillanatokat, új lehetőségeket és boldog együtt " +
                        " töltött időt!")
        );
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

        String[] dayNames = {"H", "K", "Sze", "Cs", "P", "Szo", "V"};

        // Header row
        if (showWeek) {
            TextView weekHeader = createHeader("Hét", cellSize);
            dayGrid.addView(weekHeader);
        }

        for (String dayName : dayNames) {
            TextView dayHeader = createHeader(dayName, cellSize);
            dayHeader.setTextColor(dayName.equals("Szo") || dayName.equals("V") ? Color.RED : Color.WHITE);
            dayGrid.addView(dayHeader);
        }

        String[] monthNames = {"JANUÁR", "FEBRUÁR", "MÁRCIUS", "ÁPRILIS", "MÁJUS", "JÚNIUS",
                "JÚLIUS", "AUGUSZTUS", "SZEPTEMBER", "OKTÓBER", "NOVEMBER", "DECEMBER"};
        headerText.setText(monthNames[month]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, currentYear);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7;
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        Calendar prevMonth = (Calendar) calendar.clone();
        prevMonth.add(Calendar.MONTH, -1);
        int daysInPrevMonth = prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH);

        int feastColor = ColorPreference.getFeastColor(this);
        int reminderColor = ColorPreference.getReminderColor(this);

        Calendar today = Calendar.getInstance();
        int todayYear = today.get(Calendar.YEAR);
        int todayMonth = today.get(Calendar.MONTH);
        int todayDay = today.get(Calendar.DAY_OF_MONTH);

        int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
        int prevStart = daysInPrevMonth - firstDayOfWeek + 1;

        if (showWeek) {
            TextView weekNum = createWeekNum(weekNumber, cellSize);
            dayGrid.addView(weekNum);
        }

        // ===== PREVIOUS MONTH DAYS =====
        for (int i = 0; i < firstDayOfWeek; i++) {
            LinearLayout prevContainer = createDayCell(prevStart + i, "#666666", cellSize);
            dayGrid.addView(prevContainer);
        }

        // ===== CURRENT MONTH DAYS =====
        for (int day = 1; day <= daysInMonth; day++) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
            int dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7;

            if (dayOfWeek == 0 && showWeek) {
                weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
                TextView weekNum = createWeekNum(weekNumber, cellSize);
                dayGrid.addView(weekNum);
            }

            // Normal day cell
            LinearLayout container = createDayCell(day, (dayOfWeek == 5 || dayOfWeek == 6) ? "RED" : "WHITE", cellSize);

            // === Highlight current day ===
            if (currentYear == todayYear && month == todayMonth && day == todayDay) {
                container.setBackgroundColor(getColor(R.color.app_color)); // Highlight color
            }

            // Feast days
            for (FeastDay fd : feastDays) {
                if (fd.year == currentYear && fd.month == month && fd.day == day) {
                    TextView fdLabel = createEventLabel(fd.name, feastColor, cellSize);
                    fdLabel.setOnClickListener(v -> showFeastDialog(fd));
                    container.addView(fdLabel);
                }
            }

            // DB events (reminders)
            for (FeastDay fd : dbHelper.getAllEvents()) {
                if (fd.year == currentYear && fd.month == month && fd.day == day) {
                    TextView fdLabel = createEventLabel(fd.name, reminderColor, cellSize);
                    fdLabel.setOnClickListener(v -> showFeastDialog(fd));
                    container.addView(fdLabel);
                }
            }

            dayGrid.addView(container);
        }

        // ===== NEXT MONTH DAYS =====
        int totalCells = dayGrid.getChildCount();
        int cellsAfterHeaders = totalCells - (showWeek ? 8 : 7);
        int remainder = cellsAfterHeaders % (showWeek ? 8 : 7);

        if (remainder != 0) {
            int extraDays = (showWeek ? 8 : 7) - remainder;
            for (int i = 1; i <= extraDays; i++) {
                LinearLayout nextContainer = createDayCell(i, "#666666", cellSize);
                dayGrid.addView(nextContainer);
            }
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
        params.height = size;
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
        tv.setTextSize(9);
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundColor(bgColor);
        tv.setTextColor(Color.BLACK);
        tv.setMaxLines(1);
        return tv;
    }

    // Helper method for day cells
    private LinearLayout createDayCell(int day, String color) {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setGravity(Gravity.CENTER);
        container.setLayoutParams(new LinearLayout.LayoutParams(100, 100));

        TextView dayView = new TextView(this);
        dayView.setText(String.valueOf(day));
        dayView.setTextSize(14);
        dayView.setGravity(Gravity.CENTER);

        if (color.equals("RED")) {
            dayView.setTextColor(Color.RED);
        } else if (color.equals("WHITE")) {
            dayView.setTextColor(Color.WHITE);
        } else {
            dayView.setTextColor(Color.parseColor(color));
        }

        container.addView(dayView);
        return container;
    }


    private void showFeastDialog(FeastDay fd) {
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
                    for (int year = 2025; year <= 2035; year++) {
                        dbHelper.addEvent(year, selectedMonth[0], selectedDay[0], name, story);
                    }
                    Toast.makeText(this, "Esemény mentve minden évre (2025–2035)!", Toast.LENGTH_SHORT).show();
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

    private void applyColors() {
        rootLayout.setBackgroundColor(ColorPreference.getAppColor(this));
    }

    private void applyDimEffect(boolean dim) {
        if (dim) {
            rootLayout.setForeground(new ColorDrawable(Color.parseColor("#88000000"))); // 53% black overlay
        } else {
            rootLayout.setForeground(null);
        }
    }private void showDeleteConfirmation(FeastDay fd) {
        new AlertDialog.Builder(this)
                .setTitle("Esemény törlése")
                .setMessage("Biztosan törölni szeretnéd ezt az eseményt?\n\n" +
                        "Töröljem az összes évben?")
                .setPositiveButton("Csak ezt", (dialog, which) -> {
                    dbHelper.deleteEventByDate(fd.year, fd.month, fd.day);
                    Toast.makeText(this, "Esemény törölve!", Toast.LENGTH_SHORT).show();
                    populateCalendar();
                })
                .setNeutralButton("Minden év", (dialog, which) -> {
                    for (int year = 2025; year <= 2035; year++) {
                        dbHelper.deleteEventByDate(year, fd.month, fd.day);
                    }
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
            WidgetProvider.updateAppWidgetWithDate(this, manager, id, year, month, day, hour, minute);
        }
    }
}
