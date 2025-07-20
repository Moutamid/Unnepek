package com.moutamid.unnepek;

import android.content.Context;
import android.content.Intent;
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
    private final List<DayItem> items = new ArrayList<>();
    private int currentMonth;
    private int currentYear;
    private DBHelper dbHelper;

    public GridRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        dbHelper = new DBHelper(context);

        currentMonth = intent.getIntExtra("month", Calendar.getInstance().get(Calendar.MONTH));
        currentYear = intent.getIntExtra("year", Calendar.getInstance().get(Calendar.YEAR));
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
            DayItem header = new DayItem(-1, false, false, name);
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
        feastDays.add(new FeastDay(2025, 6, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma..."));
        feastDays.add(new FeastDay(2025, 6, 7, "testing", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma..."));

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
        feastDays.add(new FeastDay(2025, 2, 5, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja,\n" +
                " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a\n" +
                " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A\n" +
                " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és\n" +
                " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki\n" +
                " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának\n" +
                " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel,\n" +
                " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére\n" +
                " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A\n" +
                " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap\n" +
                " előtt tartanak. Általában február végére vagy március elejére esik."));
        feastDays.add(new FeastDay(2025, 2, 8, "Nőnap", "Az első Nőnapot 1908 március 8.-án tartották. A női munkások\n" +
                " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az\n" +
                " ünnep célja a nők társadalmi, politikai és gazdasági jogainak\n" +
                " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben\n" +
                " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így\n" +
                " 1911-ben már több európai országban is megünnepelték. Magyarországon\n" +
                " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már\n" +
                " a nők jogainak elismerésére és tiszteletére emelkedett."));
        feastDays.add(new FeastDay(2025, 2, 15, "1848-as Forradalom", " Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a\n" +
                " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti\n" +
                " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a\n" +
                " sajtószabadságért, a jobb életkörülményekért és a függetlenségért.\n" +
                " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című\n" +
                " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet\n" +
                " függetlenségi törekvése a magyar történelem egyik legfontosabb\n" +
                " eseményévé vált. Az események hatására megerősödött a nemzeti\n" +
                " identitás és ma már a magyar szabadság és összetartozás napjaként\n" +
                " ünnepeljük"));
        feastDays.add(new FeastDay(2025, 2, 30, "Óraátállítás", "A tavaszi óraátállítás a nyári időszámítás kezdete. \n" +
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
                " óraátállítást Magyarországon"));
        feastDays.add(new FeastDay(2025, 3, 1, "Bolondok napja", " Április elseje a bolondok napja, a bolondos tréfák és ártalmatlan\n" +
                " átverések napja. Az emberek apró csínyekkel, például hamis hírekkel vagy\n" +
                " kitalált történetekkel szórakoztatják egymást. Az internet\n" +
                " elterjedésével az álhírek és vírusos videók is népszerűvé váltak. A\n" +
                " nap célja, hogy jókedvet és nevetést hozzon, miközben erősíti a\n" +
                " közösségi szellemet. Eredete több elméletre vezethető vissza. A\n" +
                " legnépszerűbb magyarázat szerint a XVI. században, a Gergely-naptár\n" +
                " bevezetésekor azok, akik áprilisban ünnepelték az újévet,\n" +
                " Franciaországban bolondnak számítottak, így tréfák céltáblájává\n" +
                " váltak. Hasonló szokások figyelhetők meg a kelta tavaszi ünnepekben és az\n" +
                " ókori római Hilaria ünnepén is, ahol a bolondos viselkedés a tavaszi\n" +
                " megújulást jelezte."));
        feastDays.add(new FeastDay(2025, 3, 13, "Virágvasárnap", "A Virágvasárnap a húsvét előtti vasárnap, Jézus jeruzsálemi\n" +
                " bevonulásának emlékére van. Az evangéliumok szerint Jézus szamárháton\n" +
                " vonult be a városba, miközben a hívek pálmaágakat lengettek, üdvözölve\n" +
                " őt, mint a Megváltót. A virágok, a pálmák és a zöld ágak használata\n" +
                " az ünnep jelképei, melyek a békét és az üdvözítőt hirdetik. A\n" +
                " keresztény hagyományban Virágvasárnap a nagyböjt végét és a húsvéti\n" +
                " ünnepekre való lelki felkészülést jelzi. Az ünnep hivatalosan a 4.\n" +
                " század óta van jelen az egyházi naptárban és azóta része a keresztény\n" +
                " hagyományoknak világszerte."));
        feastDays.add(new FeastDay(2025, 3, 18, "Nagypéntek", "A Nagypéntek a keresztény húsvéti ünnepkör legfontosabb napja, amikor\n" +
                " Jézus Krisztus kereszthalálát és szenvedését emlékezzük meg. Az ünnep\n" +
                " célja, hogy a hívők átgondolják a megváltásra tett áldozat\n" +
                " jelentőségét. E napot a 4. század óta ünneplik, amikor a keresztény\n" +
                " egyház hivatalosan is elismerte Jézus kereszthalálának fontosságát.\n" +
                " Magyarországon a 20. század elejétől munkaszüneti nap. A keresztény\n" +
                " hagyományokban sötét napként van jelen, amikor sokan böjtölnek és\n" +
                " csendes megemlékezéseket tartanak. Az egyházi szertartások mellett a\n" +
                " keresztény közösség számára Nagypéntek a bűnbánat és a lelki\n" +
                " megújulás napja is.  Nagypénteket a húsvét előtti pénteken ünneplik,\n" +
                " tehát ez a dátum évente változik."));
        feastDays.add(new FeastDay(2025, 3, 20, "Húsvétvasárnap", "Húsvétvasárnap a kereszténység legfontosabb ünnepe, Jézus Krisztus\n" +
                " feltámadásának napja, amely a halál feletti győzelmet és az örök élet\n" +
                " ígéretét hirdeti. Magyarországon 1991 óta munkaszüneti nap, de\n" +
                " jelentősége évszázadok óta meghatározó. Húsvét a 40 napos nagyböjt\n" +
                " végét jelzi, amely a bűnbánat és a lelki megújulás időszaka. A gyerekek\n" +
                " az ajándékaikat hagyományosan ezen a napon kapják meg, édességeket és\n" +
                " tojásokat, amelyeket a húsvéti nyúl hoz. A húsvéti nyúl a 16. századi\n" +
                " Németországból ered és a termékenység, valamint az újjászületés\n" +
                " jelképe. Eredete pogány hagyományokhoz köthető, ahol a nyúl és a tojás\n" +
                " a tavaszi újjászületés szimbóluma volt, melyek később beépültek a\n" +
                " keresztény húsvéti szokásokba. A Húsvétot évente változó dátumon\n" +
                " ünneplik, mivel a tavaszi napéjegyenlőség utáni első teliholdat követő\n" +
                " vasárnapra esik."));
        feastDays.add(new FeastDay(2025, 3, 21, "Húsvéthétfő", "Húsvéthétfő a húsvéti ünnepkör egyik legfontosabb napja, amely Jézus\n" +
                " Krisztus feltámadását követően az új élet és a megújulás\n" +
                " szimbólumává vált. Magyarországon a húsvéti locsolás hagyománya a 17.\n" +
                " századtól terjedt el. A férfiak vízzel vagy illatos kölnivel locsolják\n" +
                " meg a nőket, kívánva nekik egészséget, boldogságot és termékenységet.\n" +
                " A locsolás eredete valószínűleg a megtisztulás és a tavaszi megújulás\n" +
                " ősi hiedelmeiből származik. A húsvéti nyúl a tavasz, az új élet és a\n" +
                " termékenység jelképe, míg a bárány Krisztus áldozatát, ártatlanságát\n" +
                " és a megváltást szimbolizálja. Magyarországon a húsvétot a\n" +
                " kereszténység elterjedése óta ünneplik és a népszokások a 19.\n" +
                " századtól erősödtek meg."));
        feastDays.add(new FeastDay(2025, 3, 22, "A Föld napja", "A Föld napja 1970-ben indult el, amikor az Egyesült Államokban Gaylord\n" +
                " Nelson szenátor kezdeményezésére több millió ember gyűlt össze, hogy\n" +
                " felhívja a figyelmet a környezetszennyezésre és a természeti erőforrások\n" +
                " védelmére. A nap célja, hogy globálisan tudatosítsa az emberekben a\n" +
                " környezetvédelem fontosságát és ösztönözze a fenntartható életmódra\n" +
                " való áttérést. Azóta minden évben április 22.-én világszerte\n" +
                " megemlékeznek róla. Számos esemény, akció és kampány hívja fel a\n" +
                " figyelmet a bolygó védelmére és a környezeti problémák megoldására.\n" +
                " "));
        feastDays.add(new FeastDay(2025, 3, 30, "Májusfa állítás", " A májusfa felállításának legjellemzőbb hagyomány szerinti időpontja a\n" +
                " május 1-jét megelőző nap délután/este volt.  Magyarországon a középkor\n" +
                " óta ismert, tehát legalább a 14. századtól ünneplik. A szokás az ősi\n" +
                " tavaszi rituálékra épül, melyek a természet megújulását és az élet\n" +
                " körforgását szimbolizálták. A fiatal legények a lányok háza elé\n" +
                " állították a feldíszített fákat, hogy kifejezzék szerelmi szándékukat.\n" +
                " A májusfát szalagokkal, virágokkal díszítették és május végén vagy\n" +
                " pünkösdkor táncos mulatság keretében bontották le, amit kitáncolásnak\n" +
                " neveztek. A hagyomány a szerelem és a termékenység ünnepe volt, később\n" +
                " pedig összefonódott a munka ünnepével (május 1.). Mára már kevésbé\n" +
                " elterjedt szokás, de sok helyen igyekeznek még fenntartani."));
        feastDays.add(new FeastDay(2025, 4, 1, "A Munka ünnepe", "Május 1.-e a munka ünnepe. A munkások jogaiért és szolidaritásáért\n" +
                " folytatott küzdelem emléknapja világszerte. Az ünnep gyökerei az 1886-os\n" +
                " chicagói tüntetésekhez nyúlnak vissza, ahol a munkások a nyolcórás\n" +
                " munkaidő bevezetéséért demonstráltak. Az események hatására 1890-től\n" +
                " május 1.-ét a munkások nemzetközi szolidaritási napjává\n" +
                " nyilvánították.  Magyarországon először 1890-ben tartották meg, de\n" +
                " hivatalos állami ünnep csak 1946-ban lett. A szocializmus idején\n" +
                " nagyszabású felvonulásokkal és tömegrendezvényekkel ünnepelték. A\n" +
                " rendszerváltás után a jelentése átalakult, de továbbra is a munka\n" +
                " tiszteletét és a munkavállalók jogainak fontosságát hirdeti."));
        feastDays.add(new FeastDay(2025, 4, 4, "Anyák napja", " Az édesanyák napja a szeretet, a tisztelet és a hála ünnepe, amelyet\n" +
                " világszerte az édesanyák tiszteletére tartanak. Gyökerei az ókori\n" +
                " Görögországba nyúlnak vissza, ahol Rheát, az istenek anyját ünnepelték.\n" +
                " A modern anyák napja az Egyesült Államokból indult 1908-ban, amikor Anna\n" +
                " Jarvis kampányt indított édesanyja emlékére. Magyarországon először\n" +
                " 1925-ben ünnepelték és a májusi Mária-tisztelet hagyományaival is\n" +
                " összekapcsolódott. Ez a nap a gondoskodó és szerető édesanyák, valamint\n" +
                " minden női gondviselő iránti megbecsülés kifejezésére szolgál.\n" +
                " Virágokkal, ajándékokkal és figyelmességgel fejezik ki hálájukat a\n" +
                " gyerekek. Bár világszerte eltérő időpontokban ünneplik, mindenhol az\n" +
                " anyák iránti szeretet és tisztelet áll a középpontban. Anyák napját\n" +
                " Magyarországon minden év május első vasárnapján ünneplik. "));
        feastDays.add(new FeastDay(2025, 4, 25, "Gyermeknap", " A gyermeknap a gyerekek jogainak és jólétének fontosságát hirdető\n" +
                " ünnep, melyet világszerte különböző időpontokban tartanak. Eredete\n" +
                " 1925-re nyúlik vissza, amikor a genfi Gyermekjóléti Konferencián\n" +
                " elhatározták, hogy kiemelik a gyermekek védelmének fontosságát.\n" +
                " Magyarországon 1931-ben tartották meg először, majd 1950 óta május\n" +
                " utolsó vasárnapján ünneplik. Ez a nap a gyerekekről szól, akik ilyenkor\n" +
                " különféle programokon vehetnek részt, ahol játékokkal és szórakoztató\n" +
                " rendezvényekkel ünneplik őket. Az ünnep célja, hogy felhívja a figyelmet\n" +
                " a gyermekek jogaira és boldogságuk fontosságára. Magyarországon a\n" +
                " Gyermeknapot minden évben május utolsó vasárnapján ünneplik."));
        feastDays.add(new FeastDay(2025, 5, 8, "Pünkösd", "Pünkösd a kereszténység egyik legősibb ünnepe, melyet már a 4.\n" +
                " századtól kezdve ünnepelnek a keresztény egyházban. Az ünnep Jézus\n" +
                " mennybemenetele után ötven nappal emlékezik meg a Szentlélek\n" +
                " eljöveteléről, amely erőt adott az apostoloknak a keresztény hit\n" +
                " hirdetéséhez. Ez az esemény az egyház születésnapjaként is ismert. A\n" +
                " „pünkösd” elnevezés a görög „pentékoszté” szóból ered, ami\n" +
                " ötvenediket jelent. Magyarországon a középkor óta tartják számon és\n" +
                " számos népi hagyomány kapcsolódik hozzá, például a pünkösdi király\n" +
                " és királyné választása.  Pünkösd vasárnapját a húsvét utáni 50.\n" +
                " napon, tehát 50 nappal húsvétvasárnap után ünneplik. "));
        feastDays.add(new FeastDay(2025, 5, 9, "Pünkösdhétfő", " A Pünkösdhétfő a keresztény egyház egyik legfontosabb ünnepének, a\n" +
                " pünkösdnek a második napja. Pünkösd a húsvét utáni 50. napon van, és a\n" +
                " Szentlélek eljövetelét ünnepli, amikor Jézus tanítványai erőt és\n" +
                " útmutatást kaptak küldetésükhöz. Az esemény a keresztény egyház\n" +
                " születésnapjának is tekinthető. Az ünnep gyökerei a zsidó pünkösdi\n" +
                " ünnepre (Sávuót) vezethetők vissza, amely az aratási hálaadás napja\n" +
                " volt. Magyarországon pünkösdhétfő munkaszüneti nap és hivatalosan 1993\n" +
                " óta ünnepeljük újra. Az ünnep a megújulást és a közösség\n" +
                " összetartozását jelképezi."));
        feastDays.add(new FeastDay(2025, 5, 15, "Apák napja", " Apák napja világszerte az édesapák tiszteletére szolgál, elismerve a\n" +
                " családban és a gyermeknevelésben betöltött szerepüket. Az ünnep eredete\n" +
                " az Egyesült Államokhoz köthető, ahol először 1910-ben tartották meg\n" +
                " Sonora Smart Dodd kezdeményezésére , aki édesapját akarta tiszteletben\n" +
                " részesíteni . Magyarországon az apák napja az 1990-es években kezdett\n" +
                " elterjedni  és 1994 óta ünnepeljük minden év június harmadik\n" +
                " vasárnapján. Célja, hogy elismerje az apák és az apaszerepet betöltő\n" +
                " férfiak szeretetét, áldozatvállalását és a családban betöltött\n" +
                " szerepét. Ez a nap lehetőséget ad arra, hogy köszönetet mondjunk az apák\n" +
                " szeretetéért, támogatásáért és áldozatos munkájáért, amelyet\n" +
                " családjukért tesznek. "));
        feastDays.add(new FeastDay(2025, 5, 21, "Nyári napforduló", " Az év leghosszabb napja. \n" +
                " Napkelte: Körülbelül 4:42\n" +
                " Napnyugta: Körülbelül 20:19\n" +
                " Világos időtartam: Körülbelül 15 óra és 37\n" +
                " A nyári napforduló a leghosszabb nap az évben, amikor a Nap a legmagasabbra\n" +
                " emelkedik az égbolton. Ez a jelenség a Föld tengelyferdesége és a\n" +
                " keringése miatt következik be. Az északi féltekén a napforduló\n" +
                " környékén a Nap több mint 15 órán keresztül a horizont felett\n" +
                " tartózkodik. Ezen a napon a Föld az északi féltekén éri el a leghosszabb\n" +
                " nappali órákat, így ez a legrövidebb éjszaka."));
        feastDays.add(new FeastDay(2025, 7, 20, "Szent István-nap", "Szent István király koronázása körülbelül 1000-ben történt, ezzel\n" +
                " megalapozva a keresztény Magyarországot. 1083 augusztus 20.-án szentté\n" +
                " avatták a Szent Péter-bazilikában. Az Árpád-korban az István-kultusz az\n" +
                " uralkodó dinasztiát támogatta és egységet teremtett a népcsoportok\n" +
                " között. 1686-ban XI. Ince pápa elrendelte, hogy az egész katolikus világ\n" +
                " emlékezzen meg róla, majd 1771-ben Mária Terézia országos ünneppé tette.\n" +
                " Az 1848–49-es forradalom és szabadságharc leverése után egy időre\n" +
                " szünetelt az ünneplés, de 1891-ben Ferenc József munkaszüneti nappá\n" +
                " nyilvánította. A két világháború között a nemzeti egység\n" +
                " szimbólumává vált. 1950-től az új államrendszer ideológiája szerint\n" +
                " alakították át az ünnep jelentését, majd 1991-ben ismét állami\n" +
                " ünneppé nyilvánították."));
        feastDays.add(new FeastDay(2025, 9, 23, "1956-os Forradalom", "Magyarország nemzeti ünnepe, amely az 1956-os forradalom és szabadságharc\n" +
                " eseményeire emlékezik. Az ünnep hivatalosan 1989 október 23.-án vált\n" +
                " nemzeti ünneppé a rendszerváltás után, amikor a forradalom hőseit\n" +
                " ünnepelve tisztelegtek a szabadságért és a demokráciáért folytatott\n" +
                " küzdelmük előtt. Az 1956-os forradalom a szovjet megszállás elleni\n" +
                " népfelkelés volt, amely bár véresen végződött, alapvető szerepet\n" +
                " játszott Magyarország függetlenségi törekvéseiben és a későbbi\n" +
                " rendszerváltásban. Az október 23.-i események máig a szabadság és a\n" +
                " nemzeti szuverenitás fontos szimbólumai."));
        feastDays.add(new FeastDay(2025, 9, 26, "Óraátállítás", "Az őszi óraátállítás (a téli időszámítás kezdete) – (vasárnap),\n" +
                " 3:00 → 2:00\n" +
                " Ekkor kell 1 órát visszaállítani az órákat.\n" +
                " Az őszi óraátállítás célja, hogy reggel világosabb legyen, így a\n" +
                " természetes fényt jobban kihasználjuk. Magyarországon először 1916-ban\n" +
                " vezették be az óraátállítást energiatakarékossági okokból. Bár több\n" +
                " alkalommal szünetelt, végül 1980-ban újra bevezették, hogy csökkentsék a\n" +
                " villamosenergia-felhasználást. A téli időszámítás október utolsó\n" +
                " vasárnapján kezdődik, amikor az órákat visszaállítjuk. Ezáltal reggel\n" +
                " hamarabb világosodik, de este hamarabb sötétedik. 1996 óta kormányrendelet\n" +
                " szabályozza az óraátállítást."));
        feastDays.add(new FeastDay(2025, 10, 1, "Mindenszentek", "Mindenszentek ünnepe november 1.-én van, amikor a katolikus egyház\n" +
                " megemlékezik minden üdvözült lélekről, akik elnyerték a mennyországot,\n" +
                " de nevüket nem tartják számon a naptárban. Az ünnep eredete a\n" +
                " kereszténység előtti halotti kultuszokra is visszavezethető. Hivatalosan a\n" +
                " 8. században III. Gergely pápa idején vezették be, aki a Szent\n" +
                " Péter-bazilikában kápolnát szentelt minden szent tiszteletére. \n" +
                " Magyarországon munkaszüneti nap. Sokan virágokkal és gyertyagyújtással\n" +
                " tisztelegnek elhunyt szeretteik emléke előtt. Ez a nap a hit és a\n" +
                " megemlékezés ünnepe."));
        feastDays.add(new FeastDay(2025, 10, 2, "Halottak napja", "Halottak napja november 2.-án van, amikor a katolikus egyház megemlékezik az\n" +
                " elhunyt hívekről. Az ünnep célja, hogy imádságokkal és jó\n" +
                " cselekedetekkel segítsük a halottak lelki üdvét és tisztelegjünk\n" +
                " szeretteink emléke előtt. Bár gyökerei a kereszténység előtti halotti\n" +
                " kultuszokig nyúlnak vissza, mai formáját a 11. században Cluny-i Szent\n" +
                " Odiló apát vezette be. Ő rendelte el, hogy a kolostorok imádkozzanak a\n" +
                " tisztítótűzben szenvedő lelkekért. A gyertyagyújtás és a virágok\n" +
                " elhelyezése máig az emlékezés fontos része."));
        feastDays.add(new FeastDay(2025, 10, 30, "Advent első napja", "Advent a karácsony előtti négyhetes időszak, mely Jézus Krisztus\n" +
                " születésére és második eljövetelére való várakozást jelenti. Az\n" +
                " advent származása a 4. századra nyúlik vissza, amikor bűnbánattal és\n" +
                " lelki felkészüléssel várták a karácsonyt. Kezdetben hosszabb böjti\n" +
                " időszak volt, de fokozatosan négyhetesre csökkent. Az adventi koszorú\n" +
                " hagyománya a 19. században terjedt el, az örökzöld növények a\n" +
                " halhatatlanságot és az újjászületést szimbolizálták. A négy gyertya a\n" +
                " hitet, a reményt, az örömöt és a szeretetet jelképezi. Minden vasárnap\n" +
                " egy újabb gyertya meggyújtásával közelebb kerülünk a karácsonyhoz.\n" +
                " Advent első vasárnapját a karácsonyt megelőző négy vasárnap egyikeként\n" +
                " ünneplik. Ez a nap általában november végére vagy december elejére esik. \n" +
                " "));
        feastDays.add(new FeastDay(2025, 11, 6, "Mikulás", "Mikuláskor december 5.-én éjszaka, vagy 6.-án reggel töltik meg a gyerekek\n" +
                " csizmáit édességgel és játékokkal.  Szent Miklós püspök emlék napja.\n" +
                " A 4. században élt szent, aki a gyermekek, a hajósok és a szegények\n" +
                " védőszentje lett, híres volt jótékonyságáról. Egy legenda szerint\n" +
                " titokban segített egy szegény ember három lányán, akiket így férjhez\n" +
                " tudott adni.  Ebből a történetből született a hagyomány, hogy Szent\n" +
                " Miklós éjjelente ajándékokat hoz a jó gyerekeknek. Ma már világszerte\n" +
                " ismert a piros ruhás, fehér szakállú Mikulás.  A Mikulás-hagyományok\n" +
                " országonként eltérőek. Egyes helyeken a Mikulás hozza az ajándékokat,\n" +
                " máshol a Télapó. A lényeg azonban mindenhol ugyanaz: a szeretet az\n" +
                " adakozás és a gyermeki öröm ünnepe."));
        feastDays.add(new FeastDay(2025, 11, 13, "Luca napja", " Luca napja a keresztény és a népi hagyományok keveréke. Szent Luca a 3.\n" +
                " századi vértanú, a látás védőszentje, de a népi hitvilágban a\n" +
                " termékenység, a jólét és a védelem szimbóluma. A hagyomány szerint ezen\n" +
                " a napon kezdődött a tél leghosszabb éjszakája, így Luca napja a fény és\n" +
                " a sötétség határán áll. Ezen a napon a nők nem dolgoztak, hogy bőséges\n" +
                " legyen a következő év termése. Szokás volt Luca-széket készíteni,\n" +
                " búzát ültetni és jóslásokat végezni. Luca napja a megújulás és a\n" +
                " remény ünnepe, a fény győzelme a sötétség felett és a következő év\n" +
                " jólétének biztosítása."));
        feastDays.add(new FeastDay(2025, 11, 21, "Téli napforduló", "Az év legrövidebb napja nap. \n" +
                " Napkelte: Körülbelül  7:30\n" +
                " Napnyugta:  Körülbelül 16:00\n" +
                " Világos időtartam: Körülbelül 8 óra 30 perc\n" +
                " A téli napforduló a legrövidebb nap az évben, amikor a Nap a\n" +
                " legalacsonyabbra emelkedik az égbolton. Ez a jelenség a Föld\n" +
                " tengelyferdesége és keringése miatt következik be. Az északi féltekén a\n" +
                " napforduló környékén a Nap csak 8 órán keresztül tartózkodik a horizont\n" +
                " felett, így ez a leghosszabb éjszaka. Ezen a napon kezdődik a nappali fény\n" +
                " fokozatos növekedése, így a következő hónapokban egyre hosszabbak lesznek\n" +
                " a nappalok."));
        feastDays.add(new FeastDay(2025, 11, 24, "Szenteste", "Szenteste a karácsonyi ünnepkör egyik legfontosabb napja, amely Jézus\n" +
                " Krisztus születésének előestéje. Bár Jézus pontos születési dátuma\n" +
                " nem ismert, a keresztény egyház a 4. században december 25.-ét jelölte meg\n" +
                " születésének napjaként, összekapcsolva azt a téli napfordulóval.\n" +
                " December 24.-én este a családok összegyűlnek, közösen vacsoráznak,\n" +
                " ajándékokat adnak egymásnak és karácsonyfát állítanak. Magyarországon\n" +
                " a hagyomány szerint a Jézuska hozza az ajándékokat, ezzel is megemlékezve\n" +
                " Jézus születésének csodájáról. A fenyőfa, amely a 16. századtól vált\n" +
                " a karácsony szimbólumává, az örök életet jelképezi. Az ajándékozás a\n" +
                " szeretet és a megbecsülés kifejezése, amely a három királyok\n" +
                " ajándékozására utal. Karácsony, bár elsősorban keresztény ünnep,\n" +
                " világszerte a szeretet, a béke és a család ünnepe lett."));
        feastDays.add(new FeastDay(2025, 11, 25, "Karácsony", "A keresztények számára Jézus Krisztus születésének ünnepe, melyet a 4.\n" +
                " század óta tartanak. Az ünnep a téli napfordulóhoz kapcsolódik,\n" +
                " szimbolizálva a fény győzelmét a sötétség felett. Karácsony a remény,\n" +
                " a szeretet és a megváltás ünnepe, amely a családi együttlét, az öröm\n" +
                " és a vallási szertartások ideje. Karácsonyfa, betlehem és csillag a\n" +
                " szimbólumok, amelyek Jézus születését, az örök életet és az\n" +
                " útmutatást képviselik. Az ünneplés világszerte eltérő, de mindegyik\n" +
                " kultúrában a szeretet és a béke fontosságát hangsúlyozza. Karácsony\n" +
                " lehetőséget ad az emberi kapcsolatok és az élet értelmének\n" +
                " átgondolására."));
        feastDays.add(new FeastDay(2025, 11, 26, "Karácsony másnapja", "A keresztény hagyományban Szent István, az első vértanú emléknapja,\n" +
                " azonban Magyarországon elsősorban Szent István királyra, az\n" +
                " államalapítóra emlékeznek. A 4. századi eredetű ünnep során Jézus\n" +
                " születése után következik Szent István napja, aki kulcsszerepet játszott\n" +
                " a kereszténység elterjesztésében Magyarországon. Bár Szent István a 11.\n" +
                " században élt, az egyház később tette december 26.-át emlékévé.\n" +
                " Magyarországon nemzeti hősként is tisztelik, mivel ő alapította az\n" +
                " államot. Ezen a napon az emberek szentmisére mennek, megemlékeznek\n" +
                " királyukról és különböző ünnepi rendezvényeken is részt vesznek.\n"));
        feastDays.add(new FeastDay(2025, 11, 31, "Szilveszter", "A Szilveszter az év utolsó napja, amikor búcsút veszünk az óévtől és\n" +
                " ünnepeljük az új év kezdetét. Az ünnep neve Szent Szilveszter pápától\n" +
                " származik, aki 335 körül halt meg, de az újév ünneplésének hagyományai\n" +
                " sokkal régebbre nyúlnak vissza. Szilveszter éjszakáján családok és\n" +
                " barátok együtt ünnepelnek, vacsoráznak, tűzijátékot néznek és\n" +
                " jókívánságokat mondanak. A tűzijáték és a zajkeltés az ősi\n" +
                " hagyományokból ered. Az ünnep a megújulás, az összetartozás és a\n" +
                " remény kifejezője, miközben gazdaságilag is fontos szerepet játszik a\n" +
                " fogyasztói társadalomban.\n" +
                " \n" +
                " Boldog Új Évet kívánok! Legyen tele örömmel, egészséggel és sikerrel.\n" +
                " Hozzon az új év boldog pillanatokat, új lehetőségeket és boldog együtt\n" +
                " töltött időt!"));
        feastDays.add(new FeastDay(2026, 0, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma.\n" +
                " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az\n" +
                " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy\n" +
                " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és\n" +
                " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a\n" +
                " mondás. Legyen ez a nap a változás első lépése. Boldogságot,\n" +
                " egészséget és kitartást kívánok az új év minden pillanatára!\nAz év első napja a megújulás, az újrakezdés és a remény szimbóluma. Az emberek világszerte különféle szokásokkal és ünnepségekkel köszöntik az új esztendőt, jókívánságokat osztanak, fogadalmakat tesznek, és bíznak abban, hogy az új év jobb lesz, mint az előző."));
        feastDays.add(new FeastDay(2026, 0, 6, "Vízkereszt", "A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a\n" +
                " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus\n" +
                " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus\n" +
                " megkeresztelkedésére és az első csodájára. A három királyok aranyat,\n" +
                " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a\n" +
                " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet\n" +
                " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi\n" +
                " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak\n" +
                " házszenteléseket, Isten áldását kérve az otthonra és az új évre."));
        feastDays.add(new FeastDay(2026, 1, 14, "Valentin nap", "A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint\n" +
                " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században\n" +
                " titokban esketett meg szerelmespárokat, mert II. Claudius császár\n" +
                " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák.\n" +
                " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az\n" +
                " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a\n" +
                " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal\n" +
                " és üdvözlőkártyákkal ünneplik."));
        feastDays.add(new FeastDay(2026, 1, 18, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja,\n" +
                " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a\n" +
                " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A\n" +
                " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és\n" +
                " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki\n" +
                " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának\n" +
                " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel,\n" +
                " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére\n" +
                " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A\n" +
                " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap\n" +
                " előtt tartanak. Általában február végére vagy március elejére esik."));
        feastDays.add(new FeastDay(2026, 2, 8, "Nőnap", "Az első Nőnapot 1908 március 8.-án tartották. A női munkások\n" +
                " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az\n" +
                " ünnep célja a nők társadalmi, politikai és gazdasági jogainak\n" +
                " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben\n" +
                " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így\n" +
                " 1911-ben már több európai országban is megünnepelték. Magyarországon\n" +
                " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már\n" +
                " a nők jogainak elismerésére és tiszteletére emelkedett."));
        feastDays.add(new FeastDay(2026, 2, 15, "1848-as Forradalom", "Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a\n" +
                " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti\n" +
                " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a\n" +
                " sajtószabadságért, a jobb életkörülményekért és a függetlenségért.\n" +
                " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című\n" +
                " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet\n" +
                " függetlenségi törekvése a magyar történelem egyik legfontosabb\n" +
                " eseményévé vált. Az események hatására megerősödött a nemzeti\n" +
                " identitás és ma már a magyar szabadság és összetartozás napjaként\n" +
                " ünnepeljük."));
        feastDays.add(new FeastDay(2026, 2, 29, "Virágvasárnap", "A Virágvasárnap a húsvét előtti vasárnap, Jézus jeruzsálemi\n" +
                " bevonulásának emlékére van. Az evangéliumok szerint Jézus szamárháton\n" +
                " vonult be a városba, miközben a hívek pálmaágakat lengettek, üdvözölve\n" +
                " őt, mint a Megváltót. A virágok, a pálmák és a zöld ágak használata\n" +
                " az ünnep jelképei, melyek a békét és az üdvözítőt hirdetik. A\n" +
                " keresztény hagyományban Virágvasárnap a nagyböjt végét és a húsvéti\n" +
                " ünnepekre való lelki felkészülést jelzi. Az ünnep hivatalosan a 4.\n" +
                " század óta van jelen az egyházi naptárban és azóta része a keresztény\n" +
                " hagyományoknak világszerte."));
        feastDays.add(new FeastDay(2026, 2, 29, "Óraátállítás", "A tavaszi óraátállítás a nyári időszámítás kezdete. \n" +
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
                " óraátállítást Magyarországon."));
        feastDays.add(new FeastDay(2026, 3, 1, "Bolondok napja", "Április elseje a bolondok napja, a bolondos tréfák és ártalmatlan\n" +
                " átverések napja. Az emberek apró csínyekkel, például hamis hírekkel vagy\n" +
                " kitalált történetekkel szórakoztatják egymást. Az internet\n" +
                " elterjedésével az álhírek és vírusos videók is népszerűvé váltak. A\n" +
                " nap célja, hogy jókedvet és nevetést hozzon, miközben erősíti a\n" +
                " közösségi szellemet. Eredete több elméletre vezethető vissza. A\n" +
                " legnépszerűbb magyarázat szerint a XVI. században, a Gergely-naptár\n" +
                " bevezetésekor azok, akik áprilisban ünnepelték az újévet,\n" +
                " Franciaországban bolondnak számítottak, így tréfák céltáblájává\n" +
                " váltak. Hasonló szokások figyelhetők meg a kelta tavaszi ünnepekben és az\n" +
                " ókori római Hilaria ünnepén is, ahol a bolondos viselkedés a tavaszi\n" +
                " megújulást jelezte. "));
        feastDays.add(new FeastDay(2026, 3, 3, "Nagypéntek", "A Nagypéntek a keresztény húsvéti ünnepkör legfontosabb napja, amikor\n" +
                " Jézus Krisztus kereszthalálát és szenvedését emlékezzük meg. Az ünnep\n" +
                " célja, hogy a hívők átgondolják a megváltásra tett áldozat\n" +
                " jelentőségét. E napot a 4. század óta ünneplik, amikor a keresztény\n" +
                " egyház hivatalosan is elismerte Jézus kereszthalálának fontosságát.\n" +
                " Magyarországon a 20. század elejétől munkaszüneti nap. A keresztény\n" +
                " hagyományokban sötét napként van jelen, amikor sokan böjtölnek és\n" +
                " csendes megemlékezéseket tartanak. Az egyházi szertartások mellett a\n" +
                " keresztény közösség számára Nagypéntek a bűnbánat és a lelki\n" +
                " megújulás napja is. Nagypénteket a húsvét előtti pénteken ünneplik,\n" +
                " tehát ez a dátum évente változik."));
        feastDays.add(new FeastDay(2026, 3, 5, "Húsvétvasárnap", "Húsvétvasárnap a kereszténység legfontosabb ünnepe, Jézus Krisztus\n" +
                " feltámadásának napja, amely a halál feletti győzelmet és az örök élet\n" +
                " ígéretét hirdeti. Magyarországon 1991 óta munkaszüneti nap, de\n" +
                " jelentősége évszázadok óta meghatározó. Húsvét a 40 napos nagyböjt\n" +
                " végét jelzi, amely a bűnbánat és a lelki megújulás időszaka. A gyerekek\n" +
                " az ajándékaikat hagyományosan ezen a napon kapják meg, édességeket és\n" +
                " tojásokat, amelyeket a húsvéti nyúl hoz. A húsvéti nyúl a 16. századi\n" +
                " Németországból ered és a termékenység, valamint az újjászületés\n" +
                " jelképe. Eredete pogány hagyományokhoz köthető, ahol a nyúl és a tojás\n" +
                " a tavaszi újjászületés szimbóluma volt, melyek később beépültek a\n" +
                " keresztény húsvéti szokásokba.  A Húsvétot évente változó dátumon\n" +
                " ünneplik, mivel a tavaszi napéjegyenlőség utáni első teliholdat követő\n" +
                " vasárnapra esik."));
        feastDays.add(new FeastDay(2026, 3, 6, "Húsvéthétfő", "Húsvéthétfő a húsvéti ünnepkör egyik legfontosabb napja, amely Jézus\n" +
                " Krisztus feltámadását követően az új élet és a megújulás\n" +
                " szimbólumává vált. Magyarországon a húsvéti locsolás hagyománya a 17.\n" +
                " századtól terjedt el. A férfiak vízzel vagy illatos kölnivel locsolják\n" +
                " meg a nőket, kívánva nekik egészséget, boldogságot és termékenységet.\n" +
                " A locsolás eredete valószínűleg a megtisztulás és a tavaszi megújulás\n" +
                " ősi hiedelmeiből származik.  A húsvéti nyúl a tavasz, az új élet és a\n" +
                " termékenység jelképe, míg a bárány Krisztus áldozatát, ártatlanságát\n" +
                " és a megváltást szimbolizálja. Magyarországon a húsvétot a\n" +
                " kereszténység elterjedése óta ünneplik és a népszokások a 19.\n" +
                " századtól erősödtek meg."));
        feastDays.add(new FeastDay(2026, 3, 22, "A Föld napja", "A Föld napja 1970-ben indult el, amikor az Egyesült Államokban Gaylord\n" +
                " Nelson szenátor kezdeményezésére több millió ember gyűlt össze, hogy\n" +
                " felhívja a figyelmet a környezetszennyezésre és a természeti erőforrások\n" +
                " védelmére. A nap célja, hogy globálisan tudatosítsa az emberekben a\n" +
                " környezetvédelem fontosságát és ösztönözze a fenntartható életmódra\n" +
                " való áttérést. Azóta minden évben április 22.-én világszerte\n" +
                " megemlékeznek róla. Számos esemény, akció és kampány hívja fel a\n" +
                " figyelmet a bolygó védelmére és a környezeti problémák megoldására."));
        feastDays.add(new FeastDay(2026, 3, 30, "Májusfa állítás", "A májusfa felállításának legjellemzőbb hagyomány szerinti időpontja a\n" +
                " május 1-jét megelőző nap délután/este volt.   Magyarországon a\n" +
                " középkor óta ismert, tehát legalább a 14. századtól ünneplik. A szokás\n" +
                " az ősi tavaszi rituálékra épül, melyek a természet megújulását és az\n" +
                " élet körforgását szimbolizálták. A fiatal legények a lányok háza elé\n" +
                " állították a feldíszített fákat, hogy kifejezzék szerelmi szándékukat.\n" +
                " A májusfát szalagokkal, virágokkal díszítették és május végén vagy\n" +
                " pünkösdkor táncos mulatság keretében bontották le, amit kitáncolásnak\n" +
                " neveztek. A hagyomány a szerelem és a termékenység ünnepe volt, később\n" +
                " pedig összefonódott a munka ünnepével (május 1.). Mára már kevésbé\n" +
                " elterjedt szokás, de sok helyen igyekeznek még fenntartani."));
        feastDays.add(new FeastDay(2026, 4, 1, "A Munka ünnepe", "Május 1.-e a munka ünnepe. A munkások jogaiért és szolidaritásáért\n" +
                " folytatott küzdelem emléknapja világszerte. Az ünnep gyökerei az 1886-os\n" +
                " chicagói tüntetésekhez nyúlnak vissza, ahol a munkások a nyolcórás\n" +
                " munkaidő bevezetéséért demonstráltak. Az események hatására 1890-től\n" +
                " május 1.-ét a munkások nemzetközi szolidaritási napjává\n" +
                " nyilvánították.  Magyarországon először 1890-ben tartották meg, de\n" +
                " hivatalos állami ünnep csak 1946-ban lett. A szocializmus idején\n" +
                " nagyszabású felvonulásokkal és tömegrendezvényekkel ünnepelték. A\n" +
                " rendszerváltás után a jelentése átalakult, de továbbra is a munka\n" +
                " tiszteletét és a munkavállalók jogainak fontosságát hirdeti."));
        feastDays.add(new FeastDay(2026, 4, 3, "Anyák napja", "Az édesanyák napja a szeretet, a tisztelet és a hála ünnepe, amelyet\n" +
                " világszerte az édesanyák tiszteletére tartanak. Gyökerei az ókori\n" +
                " Görögországba nyúlnak vissza, ahol Rheát, az istenek anyját ünnepelték.\n" +
                " A modern anyák napja az Egyesült Államokból indult 1908-ban, amikor Anna\n" +
                " Jarvis kampányt indított édesanyja emlékére. Magyarországon először\n" +
                " 1925-ben ünnepelték és a májusi Mária-tisztelet hagyományaival is\n" +
                " összekapcsolódott. Ez a nap a gondoskodó és szerető édesanyák, valamint\n" +
                " minden női gondviselő iránti megbecsülés kifejezésére szolgál.\n" +
                " Virágokkal, ajándékokkal és figyelmességgel fejezik ki hálájukat a\n" +
                " gyerekek. Bár világszerte eltérő időpontokban ünneplik, mindenhol az\n" +
                " anyák iránti szeretet és tisztelet áll a középpontban. Anyák napját\n" +
                " Magyarországon minden év május első vasárnapján ünneplik."));
        feastDays.add(new FeastDay(2026, 4, 24, "Pünkösd", "Pünkösd a kereszténység egyik legősibb ünnepe, melyet már a 4.\n" +
                " századtól kezdve ünnepelnek a keresztény egyházban. Az ünnep Jézus\n" +
                " mennybemenetele után ötven nappal emlékezik meg a Szentlélek\n" +
                " eljöveteléről, amely erőt adott az apostoloknak a keresztény hit\n" +
                " hirdetéséhez. Ez az esemény az egyház születésnapjaként is ismert. A\n" +
                " „pünkösd” elnevezés a görög „pentékoszté” szóból ered, ami\n" +
                " ötvenediket jelent. Magyarországon a középkor óta tartják számon és\n" +
                " számos népi hagyomány kapcsolódik hozzá, például a pünkösdi király\n" +
                " és királyné választása. Pünkösd vasárnapját a húsvét utáni 50.\n" +
                " napon, tehát 50 nappal húsvétvasárnap után ünneplik."));
        feastDays.add(new FeastDay(2026, 4, 25, "Pünkösdhétfő", "A Pünkösdhétfő a keresztény egyház egyik legfontosabb ünnepének, a\n" +
                " pünkösdnek a második napja. Pünkösd a húsvét utáni 50. napon van, és a\n" +
                " Szentlélek eljövetelét ünnepli, amikor Jézus tanítványai erőt és\n" +
                " útmutatást kaptak küldetésükhöz. Az esemény a keresztény egyház\n" +
                " születésnapjának is tekinthető. Az ünnep gyökerei a zsidó pünkösdi\n" +
                " ünnepre (Sávuót) vezethetők vissza, amely az aratási hálaadás napja\n" +
                " volt. Magyarországon pünkösdhétfő munkaszüneti nap és hivatalosan 1993\n" +
                " óta ünnepeljük újra. Az ünnep a megújulást és a közösség\n" +
                " összetartozását jelképezi."));
        feastDays.add(new FeastDay(2026, 4, 31, "Gyermeknap", "A gyermeknap a gyerekek jogainak és jólétének fontosságát hirdető\n" +
                " ünnep, melyet világszerte különböző időpontokban tartanak. Eredete\n" +
                " 1925-re nyúlik vissza, amikor a genfi Gyermekjóléti Konferencián\n" +
                " elhatározták, hogy kiemelik a gyermekek védelmének fontosságát.\n" +
                " Magyarországon 1931-ben tartották meg először, majd 1950 óta május\n" +
                " utolsó vasárnapján ünneplik. Ez a nap a gyerekekről szól, akik ilyenkor\n" +
                " különféle programokon vehetnek részt, ahol játékokkal és szórakoztató\n" +
                " rendezvényekkel ünneplik őket. Az ünnep célja, hogy felhívja a figyelmet\n" +
                " a gyermekek jogaira és boldogságuk fontosságára.  Magyarországon a\n" +
                " Gyermeknapot minden évben május utolsó vasárnapján ünneplik."));
        feastDays.add(new FeastDay(2026, 5, 21, "Apák napja", "Apák napja világszerte az édesapák tiszteletére szolgál, elismerve a\n" +
                " családban és a gyermeknevelésben betöltött szerepüket. Az ünnep eredete\n" +
                " az Egyesült Államokhoz köthető, ahol először 1910-ben tartották meg\n" +
                " Sonora Smart Dodd kezdeményezésére , aki édesapját akarta tiszteletben\n" +
                " részesíteni . Magyarországon az apák napja az 1990-es években kezdett\n" +
                " elterjedni  és 1994 óta ünnepeljük minden év június harmadik\n" +
                " vasárnapján. Célja, hogy elismerje az apák és az apaszerepet betöltő\n" +
                " férfiak szeretetét, áldozatvállalását és a családban betöltött\n" +
                " szerepét. Ez a nap lehetőséget ad arra, hogy köszönetet mondjunk az apák\n" +
                " szeretetéért, támogatásáért és áldozatos munkájáért, amelyet\n" +
                " családjukért tesznek. "));
        feastDays.add(new FeastDay(2026, 5, 21, "Nyári napforduló", "Az év leghosszabb napja. \n" +
                " Napkelte: Körülbelül 05:07\n" +
                " Napnyugta: Körülbelül 22:07\n" +
                " Világos időtartam: Körülbelül 17 óra\n" +
                " A nyári napforduló a leghosszabb nap az évben, amikor a Nap a legmagasabbra\n" +
                " emelkedik az égbolton. Ez a jelenség a Föld tengelyferdesége és a\n" +
                " keringése miatt következik be. Az északi féltekén a napforduló\n" +
                " környékén a Nap több mint 15 órán keresztül a horizont felett\n" +
                " tartózkodik. Ezen a napon a Föld az északi féltekén éri el a leghosszabb\n" +
                " nappali órákat, így ez a legrövidebb éjszaka."));
        feastDays.add(new FeastDay(2026, 7, 20, "Szent István-nap", "Szent István király koronázása körülbelül 1000-ben történt, ezzel\n" +
                " megalapozva a keresztény Magyarországot. 1083 augusztus 20.-án szentté\n" +
                " avatták a Szent Péter-bazilikában. Az Árpád-korban az István-kultusz az\n" +
                " uralkodó dinasztiát támogatta és egységet teremtett a népcsoportok\n" +
                " között. 1686-ban XI. Ince pápa elrendelte, hogy az egész katolikus világ\n" +
                " emlékezzen meg róla, majd 1771-ben Mária Terézia országos ünneppé tette.\n" +
                " Az 1848–49-es forradalom és szabadságharc leverése után egy időre\n" +
                " szünetelt az ünneplés, de 1891-ben Ferenc József munkaszüneti nappá\n" +
                " nyilvánította. A két világháború között a nemzeti egység\n" +
                " szimbólumává vált. 1950-től az új államrendszer ideológiája szerint\n" +
                " alakították át az ünnep jelentését, majd 1991-ben ismét állami\n" +
                " ünneppé nyilvánították."));
        feastDays.add(new FeastDay(2026, 9, 23, "1956-os Forradalom", "Magyarország nemzeti ünnepe, amely az 1956-os forradalom és szabadságharc\n" +
                " eseményeire emlékezik. Az ünnep hivatalosan 1989 október 23.-án vált\n" +
                " nemzeti ünneppé a rendszerváltás után, amikor a forradalom hőseit\n" +
                " ünnepelve tisztelegtek a szabadságért és a demokráciáért folytatott\n" +
                " küzdelmük előtt. Az 1956-os forradalom a szovjet megszállás elleni\n" +
                " népfelkelés volt, amely bár véresen végződött, alapvető szerepet\n" +
                " játszott Magyarország függetlenségi törekvéseiben és a későbbi\n" +
                " rendszerváltásban. Az október 23.-i események máig a szabadság és a\n" +
                " nemzeti szuverenitás fontos szimbólumai."));
        feastDays.add(new FeastDay(2026, 9, 25, "Óraátállítás", "Az őszi óraátállítás (a téli időszámítás kezdete) – (vasárnap),\n" +
                " 3:00 → 2:00\n" +
                " Ekkor kell 1 órát visszaállítani az órákat.\n" +
                " Az őszi óraátállítás célja, hogy reggel világosabb legyen, így a\n" +
                " természetes fényt jobban kihasználjuk. Magyarországon először 1916-ban\n" +
                " vezették be az óraátállítást energiatakarékossági okokból. Bár több\n" +
                " alkalommal szünetelt, végül 1980-ban újra bevezették, hogy csökkentsék a\n" +
                " villamosenergia-felhasználást. A téli időszámítás október utolsó\n" +
                " vasárnapján kezdődik, amikor az órákat visszaállítjuk. Ezáltal reggel\n" +
                " hamarabb világosodik, de este hamarabb sötétedik. 1996 óta kormányrendelet\n" +
                " szabályozza az óraátállítást."));
        feastDays.add(new FeastDay(2026, 10, 1, "Mindenszentek", "Mindenszentek ünnepe november 1.-én van, amikor a katolikus egyház\n" +
                " megemlékezik minden üdvözült lélekről, akik elnyerték a mennyországot,\n" +
                " de nevüket nem tartják számon a naptárban. Az ünnep eredete a\n" +
                " kereszténység előtti halotti kultuszokra is visszavezethető. Hivatalosan a\n" +
                " 8. században III. Gergely pápa idején vezették be, aki a Szent\n" +
                " Péter-bazilikában kápolnát szentelt minden szent tiszteletére.\n" +
                " Magyarországon munkaszüneti nap. Sokan virágokkal és gyertyagyújtással\n" +
                " tisztelegnek elhunyt szeretteik emléke előtt. Ez a nap a hit és a\n" +
                " megemlékezés ünnepe."));
        feastDays.add(new FeastDay(2026, 10, 2, "Halottak napja", "Halottak napja november 2.-án van, amikor a katolikus egyház megemlékezik az\n" +
                " elhunyt hívekről. Az ünnep célja, hogy imádságokkal és jó\n" +
                " cselekedetekkel segítsük a halottak lelki üdvét és tisztelegjünk\n" +
                " szeretteink emléke előtt. Bár gyökerei a kereszténység előtti halotti\n" +
                " kultuszokig nyúlnak vissza, mai formáját a 11. században Cluny-i Szent\n" +
                " Odiló apát vezette be. Ő rendelte el, hogy a kolostorok imádkozzanak a\n" +
                " tisztítótűzben szenvedő lelkekért. A gyertyagyújtás és a virágok\n" +
                " elhelyezése máig az emlékezés fontos része."));
        feastDays.add(new FeastDay(2026, 10, 29, "Advent első napja", "Advent a karácsony előtti négyhetes időszak, mely Jézus Krisztus\n" +
                " születésére és második eljövetelére való várakozást jelenti. Az\n" +
                " advent származása a 4. századra nyúlik vissza, amikor bűnbánattal és\n" +
                " lelki felkészüléssel várták a karácsonyt. Kezdetben hosszabb böjti\n" +
                " időszak volt, de fokozatosan négyhetesre csökkent. Az adventi koszorú\n" +
                " hagyománya a 19. században terjedt el, az örökzöld növények a\n" +
                " halhatatlanságot és az újjászületést szimbolizálták. A négy gyertya a\n" +
                " hitet, a reményt, az örömöt és a szeretetet jelképezi. Minden vasárnap\n" +
                " egy újabb gyertya meggyújtásával közelebb kerülünk a karácsonyhoz. \n" +
                " Advent első vasárnapját a karácsonyt megelőző négy vasárnap egyikeként\n" +
                " ünneplik. Ez a nap általában november végére vagy december elejére esik."));
        feastDays.add(new FeastDay(2026, 11, 6, "Mikulás", "Mikuláskor december 5.-én éjszaka, vagy 6.-án reggel töltik meg a gyerekek\n" +
                " csizmáit édességgel és játékokkal.  Szent Miklós püspök emlék napja.\n" +
                " A 4. században élt szent, aki a gyermekek, a hajósok és a szegények\n" +
                " védőszentje lett, híres volt jótékonyságáról. Egy legenda szerint\n" +
                " titokban segített egy szegény ember három lányán, akiket így férjhez\n" +
                " tudott adni. Ebből a történetből született a hagyomány, hogy Szent\n" +
                " Miklós éjjelente ajándékokat hoz a jó gyerekeknek. Ma már világszerte\n" +
                " ismert a piros ruhás ,fehér szakállú Mikulás.  A Mikulás-hagyományok\n" +
                " országonként eltérőek. Egyes helyeken a Mikulás hozza az ajándékokat,\n" +
                " máshol a Télapó. A lényeg azonban mindenhol ugyanaz: a szeretet az\n" +
                " adakozás és a gyermeki öröm ünnepe."));
        feastDays.add(new FeastDay(2026, 11, 13, "Luca napja", "Luca napja a keresztény és a népi hagyományok keveréke. Szent Luca a 3.\n" +
                " századi vértanú, a látás védőszentje, de a népi hitvilágban a\n" +
                " termékenység, a jólét és a védelem szimbóluma. A hagyomány szerint ezen\n" +
                " a napon kezdődött a tél leghosszabb éjszakája, így Luca napja a fény és\n" +
                " a sötétség határán áll. Ezen a napon a nők nem dolgoztak, hogy bőséges\n" +
                " legyen a következő év termése. Szokás volt Luca-széket készíteni,\n" +
                " búzát ültetni és jóslásokat végezni. Luca napja a megújulás és a\n" +
                " remény ünnepe, a fény győzelme a sötétség felett és a következő év\n" +
                " jólétének biztosítása."));
        feastDays.add(new FeastDay(2026, 11, 21, "Téli napforduló", "Az év legrövidebb napja nap. \n" +
                " Napkelte: Körülbelül 08:49\n" +
                " Napnyugta: Körülbelül 16:29\n" +
                " Világos időtartam: Körülbelül 7 óra 40 perc\n" +
                " A téli napforduló a legrövidebb nap az évben, amikor a Nap a\n" +
                " legalacsonyabbra emelkedik az égbolton. Ez a jelenség a Föld\n" +
                " tengelyferdesége és keringése miatt következik be. Az északi féltekén a\n" +
                " napforduló környékén a Nap csak 8 órán keresztül tartózkodik a horizont\n" +
                " felett, így ez a leghosszabb éjszaka. Ezen a napon kezdődik a nappali fény\n" +
                " fokozatos növekedése, így a következő hónapokban egyre hosszabbak lesznek.\n" +
                " a nappalok."));
        feastDays.add(new FeastDay(2026, 11, 24, "Szenteste", "Szenteste a karácsonyi ünnepkör egyik legfontosabb napja, amely Jézus\n" +
                " Krisztus születésének előestéje. Bár Jézus pontos születési dátuma\n" +
                " nem ismert, a keresztény egyház a 4. században december 25.-ét jelölte meg\n" +
                " születésének napjaként, összekapcsolva azt a téli napfordulóval.\n" +
                " December 24.-én este a családok összegyűlnek, közösen vacsoráznak,\n" +
                " ajándékokat adnak egymásnak és karácsonyfát állítanak. Magyarországon\n" +
                " a hagyomány szerint a Jézuska hozza az ajándékokat, ezzel is megemlékezve\n" +
                " Jézus születésének csodájáról. A fenyőfa, amely a 16. századtól vált\n" +
                " a karácsony szimbólumává, az örök életet jelképezi. Az ajándékozás a\n" +
                " szeretet és a megbecsülés kifejezése, amely a három királyok\n" +
                " ajándékozására utal. Karácsony, bár elsősorban keresztény ünnep,\n" +
                " világszerte a szeretet, a béke és a család ünnepe lett."));
        feastDays.add(new FeastDay(2026, 11, 25, "Karácsony", "A keresztények számára Jézus Krisztus születésének ünnepe, melyet a 4.\n" +
                " század óta tartanak. Az ünnep a téli napfordulóhoz kapcsolódik,\n" +
                " szimbolizálva a fény győzelmét a sötétség felett. Karácsony a remény,\n" +
                " a szeretet és a megváltás ünnepe, amely a családi együttlét, az öröm\n" +
                " és a vallási szertartások ideje. Karácsonyfa, betlehem és csillag a\n" +
                " szimbólumok, amelyek Jézus születését, az örök életet és az\n" +
                " útmutatást képviselik. Az ünneplés világszerte eltérő, de mindegyik\n" +
                " kultúrában a szeretet és a béke fontosságát hangsúlyozza. Karácsony\n" +
                " lehetőséget ad az emberi kapcsolatok és az élet értelmének\n" +
                " átgondolására."));
        feastDays.add(new FeastDay(2026, 11, 26, "Karácsony másnapja", "A keresztény hagyományban Szent István, az első vértanú emléknapja,\n" +
                " azonban Magyarországon elsősorban Szent István királyra, az\n" +
                " államalapítóra emlékeznek. A 4. századi eredetű ünnep során Jézus\n" +
                " születése után következik Szent István napja, aki kulcsszerepet játszott\n" +
                " a kereszténység elterjesztésében Magyarországon. Bár Szent István a 11.\n" +
                " században élt, az egyház később tette december 26.-át emlékévé.\n" +
                " Magyarországon nemzeti hősként is tisztelik, mivel ő alapította az\n" +
                " államot. Ezen a napon az emberek szentmisére mennek, megemlékeznek\n" +
                " királyukról és különböző ünnepi rendezvényeken is részt vesznek."));
        feastDays.add(new FeastDay(2026, 11, 31, "Szilveszter", "A Szilveszter az év utolsó napja, amikor búcsút veszünk az óévtől és\n" +
                " ünnepeljük az új év kezdetét. Az ünnep neve Szent Szilveszter pápától\n" +
                " származik, aki 335 körül halt meg, de az újév ünneplésének hagyományai\n" +
                " sokkal régebbre nyúlnak vissza. Szilveszter éjszakáján családok és\n" +
                " barátok együtt ünnepelnek, vacsoráznak, tűzijátékot néznek és\n" +
                " jókívánságokat mondanak. A tűzijáték és a zajkeltés az ősi\n" +
                " hagyományokból ered. Az ünnep a megújulás, az összetartozás és a\n" +
                " remény kifejezője, miközben gazdaságilag is fontos szerepet játszik a\n" +
                " fogyasztói társadalomban.\n" +
                " Boldog Új Évet kívánok! Legyen tele örömmel, egészséggel és sikerrel.\n" +
                " Hozzon az új év boldog pillanatokat, új lehetőségeket és boldog együtt\n" +
                " töltött időt!"));
        feastDays.add(new FeastDay(2027, 0, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma.\n" +
                " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az\n" +
                " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy\n" +
                " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és\n" +
                " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a\n" +
                " mondás. Legyen ez a nap a változás első lépése. Boldogságot,\n" +
                " egészséget és kitartást kívánok az új év minden pillanatára!"));
        feastDays.add(new FeastDay(2027, 0, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma.\n" +
                " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az\n" +
                " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy\n" +
                " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és\n" +
                " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a\n" +
                " mondás. Legyen ez a nap a változás első lépése. Boldogságot,\n" +
                " egészséget és kitartást kívánok az új év minden pillanatára!\nAz év első napja a megújulás, az újrakezdés és a remény szimbóluma. Az emberek világszerte különféle szokásokkal és ünnepségekkel köszöntik az új esztendőt, jókívánságokat osztanak, fogadalmakat tesznek, és bíznak abban, hogy az új év jobb lesz, mint az előző."));
        feastDays.add(new FeastDay(2027, 0, 6, "Vízkereszt", "A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a\n" +
                " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus\n" +
                " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus\n" +
                " megkeresztelkedésére és az első csodájára. A három királyok aranyat,\n" +
                " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a\n" +
                " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet\n" +
                " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi\n" +
                " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak\n" +
                " házszenteléseket, Isten áldását kérve az otthonra és az új évre."));
        feastDays.add(new FeastDay(2027, 1, 14, "Valentin nap", "A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint\n" +
                " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században\n" +
                " titokban esketett meg szerelmespárokat, mert II. Claudius császár\n" +
                " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák.\n" +
                " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az\n" +
                " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a\n" +
                " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal\n" +
                " és üdvözlőkártyákkal ünneplik."));
        feastDays.add(new FeastDay(2027, 1, 18, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja,\n" +
                " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a\n" +
                " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A\n" +
                " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és\n" +
                " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki\n" +
                " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának\n" +
                " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel,\n" +
                " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére\n" +
                " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A\n" +
                " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap\n" +
                " előtt tartanak. Általában február végére vagy március elejére esik."));
        feastDays.add(new FeastDay(2027, 2, 8, "Nőnap", "Az első Nőnapot 1908 március 8.-án tartották. A női munkások\n" +
                " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az\n" +
                " ünnep célja a nők társadalmi, politikai és gazdasági jogainak\n" +
                " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben\n" +
                " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így\n" +
                " 1911-ben már több európai országban is megünnepelték. Magyarországon\n" +
                " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már\n" +
                " a nők jogainak elismerésére és tiszteletére emelkedett."));
        feastDays.add(new FeastDay(2027, 2, 15, "1848-as Forradalom", "Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a\n" +
                " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti\n" +
                " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a\n" +
                " sajtószabadságért, a jobb életkörülményekért és a függetlenségért.\n" +
                " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című\n" +
                " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet\n" +
                " függetlenségi törekvése a magyar történelem egyik legfontosabb\n" +
                " eseményévé vált. Az események hatására megerősödött a nemzeti\n" +
                " identitás és ma már a magyar szabadság és összetartozás napjaként\n" +
                " ünnepeljük."));
        feastDays.add(new FeastDay(2027, 2, 29, "Virágvasárnap", "A Virágvasárnap a húsvét előtti vasárnap, Jézus jeruzsálemi\n" +
                " bevonulásának emlékére van. Az evangéliumok szerint Jézus szamárháton\n" +
                " vonult be a városba, miközben a hívek pálmaágakat lengettek, üdvözölve\n" +
                " őt, mint a Megváltót. A virágok, a pálmák és a zöld ágak használata\n" +
                " az ünnep jelképei, melyek a békét és az üdvözítőt hirdetik. A\n" +
                " keresztény hagyományban Virágvasárnap a nagyböjt végét és a húsvéti\n" +
                " ünnepekre való lelki felkészülést jelzi. Az ünnep hivatalosan a 4.\n" +
                " század óta van jelen az egyházi naptárban és azóta része a keresztény\n" +
                " hagyományoknak világszerte."));
        feastDays.add(new FeastDay(2027, 2, 29, "Óraátállítás", "A tavaszi óraátállítás a nyári időszámítás kezdete. \n" +
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
                " óraátállítást Magyarországon."));
        feastDays.add(new FeastDay(2027, 3, 1, "Bolondok napja", "Április elseje a bolondok napja, a bolondos tréfák és ártalmatlan\n" +
                " átverések napja. Az emberek apró csínyekkel, például hamis hírekkel vagy\n" +
                " kitalált történetekkel szórakoztatják egymást. Az internet\n" +
                " elterjedésével az álhírek és vírusos videók is népszerűvé váltak. A\n" +
                " nap célja, hogy jókedvet és nevetést hozzon, miközben erősíti a\n" +
                " közösségi szellemet. Eredete több elméletre vezethető vissza. A\n" +
                " legnépszerűbb magyarázat szerint a XVI. században, a Gergely-naptár\n" +
                " bevezetésekor azok, akik áprilisban ünnepelték az újévet,\n" +
                " Franciaországban bolondnak számítottak, így tréfák céltáblájává\n" +
                " váltak. Hasonló szokások figyelhetők meg a kelta tavaszi ünnepekben és az\n" +
                " ókori római Hilaria ünnepén is, ahol a bolondos viselkedés a tavaszi\n" +
                " megújulást jelezte. "));
        feastDays.add(new FeastDay(2027, 3, 3, "Nagypéntek", "A Nagypéntek a keresztény húsvéti ünnepkör legfontosabb napja, amikor\n" +
                " Jézus Krisztus kereszthalálát és szenvedését emlékezzük meg. Az ünnep\n" +
                " célja, hogy a hívők átgondolják a megváltásra tett áldozat\n" +
                " jelentőségét. E napot a 4. század óta ünneplik, amikor a keresztény\n" +
                " egyház hivatalosan is elismerte Jézus kereszthalálának fontosságát.\n" +
                " Magyarországon a 20. század elejétől munkaszüneti nap. A keresztény\n" +
                " hagyományokban sötét napként van jelen, amikor sokan böjtölnek és\n" +
                " csendes megemlékezéseket tartanak. Az egyházi szertartások mellett a\n" +
                " keresztény közösség számára Nagypéntek a bűnbánat és a lelki\n" +
                " megújulás napja is. Nagypénteket a húsvét előtti pénteken ünneplik,\n" +
                " tehát ez a dátum évente változik."));
        feastDays.add(new FeastDay(2027, 3, 5, "Húsvétvasárnap", "Húsvétvasárnap a kereszténység legfontosabb ünnepe, Jézus Krisztus\n" +
                " feltámadásának napja, amely a halál feletti győzelmet és az örök élet\n" +
                " ígéretét hirdeti. Magyarországon 1991 óta munkaszüneti nap, de\n" +
                " jelentősége évszázadok óta meghatározó. Húsvét a 40 napos nagyböjt\n" +
                " végét jelzi, amely a bűnbánat és a lelki megújulás időszaka. A gyerekek\n" +
                " az ajándékaikat hagyományosan ezen a napon kapják meg, édességeket és\n" +
                " tojásokat, amelyeket a húsvéti nyúl hoz. A húsvéti nyúl a 16. századi\n" +
                " Németországból ered és a termékenység, valamint az újjászületés\n" +
                " jelképe. Eredete pogány hagyományokhoz köthető, ahol a nyúl és a tojás\n" +
                " a tavaszi újjászületés szimbóluma volt, melyek később beépültek a\n" +
                " keresztény húsvéti szokásokba.  A Húsvétot évente változó dátumon\n" +
                " ünneplik, mivel a tavaszi napéjegyenlőség utáni első teliholdat követő\n" +
                " vasárnapra esik."));
        feastDays.add(new FeastDay(2027, 3, 6, "Húsvéthétfő", "Húsvéthétfő a húsvéti ünnepkör egyik legfontosabb napja, amely Jézus\n" +
                " Krisztus feltámadását követően az új élet és a megújulás\n" +
                " szimbólumává vált. Magyarországon a húsvéti locsolás hagyománya a 17.\n" +
                " századtól terjedt el. A férfiak vízzel vagy illatos kölnivel locsolják\n" +
                " meg a nőket, kívánva nekik egészséget, boldogságot és termékenységet.\n" +
                " A locsolás eredete valószínűleg a megtisztulás és a tavaszi megújulás\n" +
                " ősi hiedelmeiből származik.  A húsvéti nyúl a tavasz, az új élet és a\n" +
                " termékenység jelképe, míg a bárány Krisztus áldozatát, ártatlanságát\n" +
                " és a megváltást szimbolizálja. Magyarországon a húsvétot a\n" +
                " kereszténység elterjedése óta ünneplik és a népszokások a 19.\n" +
                " századtól erősödtek meg."));
        feastDays.add(new FeastDay(2027, 3, 22, "A Föld napja", "A Föld napja 1970-ben indult el, amikor az Egyesült Államokban Gaylord\n" +
                " Nelson szenátor kezdeményezésére több millió ember gyűlt össze, hogy\n" +
                " felhívja a figyelmet a környezetszennyezésre és a természeti erőforrások\n" +
                " védelmére. A nap célja, hogy globálisan tudatosítsa az emberekben a\n" +
                " környezetvédelem fontosságát és ösztönözze a fenntartható életmódra\n" +
                " való áttérést. Azóta minden évben április 22.-én világszerte\n" +
                " megemlékeznek róla. Számos esemény, akció és kampány hívja fel a\n" +
                " figyelmet a bolygó védelmére és a környezeti problémák megoldására."));
        feastDays.add(new FeastDay(2027, 3, 30, "Májusfa állítás", "A májusfa felállításának legjellemzőbb hagyomány szerinti időpontja a\n" +
                " május 1-jét megelőző nap délután/este volt.   Magyarországon a\n" +
                " középkor óta ismert, tehát legalább a 14. századtól ünneplik. A szokás\n" +
                " az ősi tavaszi rituálékra épül, melyek a természet megújulását és az\n" +
                " élet körforgását szimbolizálták. A fiatal legények a lányok háza elé\n" +
                " állították a feldíszített fákat, hogy kifejezzék szerelmi szándékukat.\n" +
                " A májusfát szalagokkal, virágokkal díszítették és május végén vagy\n" +
                " pünkösdkor táncos mulatság keretében bontották le, amit kitáncolásnak\n" +
                " neveztek. A hagyomány a szerelem és a termékenység ünnepe volt, később\n" +
                " pedig összefonódott a munka ünnepével (május 1.). Mára már kevésbé\n" +
                " elterjedt szokás, de sok helyen igyekeznek még fenntartani."));
        feastDays.add(new FeastDay(2027, 4, 1, "A Munka ünnepe", "Május 1.-e a munka ünnepe. A munkások jogaiért és szolidaritásáért\n" +
                " folytatott küzdelem emléknapja világszerte. Az ünnep gyökerei az 1886-os\n" +
                " chicagói tüntetésekhez nyúlnak vissza, ahol a munkások a nyolcórás\n" +
                " munkaidő bevezetéséért demonstráltak. Az események hatására 1890-től\n" +
                " május 1.-ét a munkások nemzetközi szolidaritási napjává\n" +
                " nyilvánították.  Magyarországon először 1890-ben tartották meg, de\n" +
                " hivatalos állami ünnep csak 1946-ban lett. A szocializmus idején\n" +
                " nagyszabású felvonulásokkal és tömegrendezvényekkel ünnepelték. A\n" +
                " rendszerváltás után a jelentése átalakult, de továbbra is a munka\n" +
                " tiszteletét és a munkavállalók jogainak fontosságát hirdeti."));
        feastDays.add(new FeastDay(2027, 4, 3, "Anyák napja", "Az édesanyák napja a szeretet, a tisztelet és a hála ünnepe, amelyet\n" +
                " világszerte az édesanyák tiszteletére tartanak. Gyökerei az ókori\n" +
                " Görögországba nyúlnak vissza, ahol Rheát, az istenek anyját ünnepelték.\n" +
                " A modern anyák napja az Egyesült Államokból indult 1908-ban, amikor Anna\n" +
                " Jarvis kampányt indított édesanyja emlékére. Magyarországon először\n" +
                " 1925-ben ünnepelték és a májusi Mária-tisztelet hagyományaival is\n" +
                " összekapcsolódott. Ez a nap a gondoskodó és szerető édesanyák, valamint\n" +
                " minden női gondviselő iránti megbecsülés kifejezésére szolgál.\n" +
                " Virágokkal, ajándékokkal és figyelmességgel fejezik ki hálájukat a\n" +
                " gyerekek. Bár világszerte eltérő időpontokban ünneplik, mindenhol az\n" +
                " anyák iránti szeretet és tisztelet áll a középpontban. Anyák napját\n" +
                " Magyarországon minden év május első vasárnapján ünneplik."));
        feastDays.add(new FeastDay(2027, 4, 24, "Pünkösd", "Pünkösd a kereszténység egyik legősibb ünnepe, melyet már a 4.\n" +
                " századtól kezdve ünnepelnek a keresztény egyházban. Az ünnep Jézus\n" +
                " mennybemenetele után ötven nappal emlékezik meg a Szentlélek\n" +
                " eljöveteléről, amely erőt adott az apostoloknak a keresztény hit\n" +
                " hirdetéséhez. Ez az esemény az egyház születésnapjaként is ismert. A\n" +
                " „pünkösd” elnevezés a görög „pentékoszté” szóból ered, ami\n" +
                " ötvenediket jelent. Magyarországon a középkor óta tartják számon és\n" +
                " számos népi hagyomány kapcsolódik hozzá, például a pünkösdi király\n" +
                " és királyné választása. Pünkösd vasárnapját a húsvét utáni 50.\n" +
                " napon, tehát 50 nappal húsvétvasárnap után ünneplik."));
        feastDays.add(new FeastDay(2027, 4, 25, "Pünkösdhétfő", "A Pünkösdhétfő a keresztény egyház egyik legfontosabb ünnepének, a\n" +
                " pünkösdnek a második napja. Pünkösd a húsvét utáni 50. napon van, és a\n" +
                " Szentlélek eljövetelét ünnepli, amikor Jézus tanítványai erőt és\n" +
                " útmutatást kaptak küldetésükhöz. Az esemény a keresztény egyház\n" +
                " születésnapjának is tekinthető. Az ünnep gyökerei a zsidó pünkösdi\n" +
                " ünnepre (Sávuót) vezethetők vissza, amely az aratási hálaadás napja\n" +
                " volt. Magyarországon pünkösdhétfő munkaszüneti nap és hivatalosan 1993\n" +
                " óta ünnepeljük újra. Az ünnep a megújulást és a közösség\n" +
                " összetartozását jelképezi."));
        feastDays.add(new FeastDay(2027, 4, 31, "Gyermeknap", "A gyermeknap a gyerekek jogainak és jólétének fontosságát hirdető\n" +
                " ünnep, melyet világszerte különböző időpontokban tartanak. Eredete\n" +
                " 1925-re nyúlik vissza, amikor a genfi Gyermekjóléti Konferencián\n" +
                " elhatározták, hogy kiemelik a gyermekek védelmének fontosságát.\n" +
                " Magyarországon 1931-ben tartották meg először, majd 1950 óta május\n" +
                " utolsó vasárnapján ünneplik. Ez a nap a gyerekekről szól, akik ilyenkor\n" +
                " különféle programokon vehetnek részt, ahol játékokkal és szórakoztató\n" +
                " rendezvényekkel ünneplik őket. Az ünnep célja, hogy felhívja a figyelmet\n" +
                " a gyermekek jogaira és boldogságuk fontosságára.  Magyarországon a\n" +
                " Gyermeknapot minden évben május utolsó vasárnapján ünneplik."));
        feastDays.add(new FeastDay(2027, 5, 21, "Apák napja", "Apák napja világszerte az édesapák tiszteletére szolgál, elismerve a\n" +
                " családban és a gyermeknevelésben betöltött szerepüket. Az ünnep eredete\n" +
                " az Egyesült Államokhoz köthető, ahol először 1910-ben tartották meg\n" +
                " Sonora Smart Dodd kezdeményezésére , aki édesapját akarta tiszteletben\n" +
                " részesíteni . Magyarországon az apák napja az 1990-es években kezdett\n" +
                " elterjedni  és 1994 óta ünnepeljük minden év június harmadik\n" +
                " vasárnapján. Célja, hogy elismerje az apák és az apaszerepet betöltő\n" +
                " férfiak szeretetét, áldozatvállalását és a családban betöltött\n" +
                " szerepét. Ez a nap lehetőséget ad arra, hogy köszönetet mondjunk az apák\n" +
                " szeretetéért, támogatásáért és áldozatos munkájáért, amelyet\n" +
                " családjukért tesznek. "));
        feastDays.add(new FeastDay(2027, 5, 21, "Nyári napforduló", "Az év leghosszabb napja. \n" +
                " Napkelte: Körülbelül 05:07\n" +
                " Napnyugta: Körülbelül 22:07\n" +
                " Világos időtartam: Körülbelül 17 óra\n" +
                " A nyári napforduló a leghosszabb nap az évben, amikor a Nap a legmagasabbra\n" +
                " emelkedik az égbolton. Ez a jelenség a Föld tengelyferdesége és a\n" +
                " keringése miatt következik be. Az északi féltekén a napforduló\n" +
                " környékén a Nap több mint 15 órán keresztül a horizont felett\n" +
                " tartózkodik. Ezen a napon a Föld az északi féltekén éri el a leghosszabb\n" +
                " nappali órákat, így ez a legrövidebb éjszaka."));
        feastDays.add(new FeastDay(2027, 7, 20, "Szent István-nap", "Szent István király koronázása körülbelül 1000-ben történt, ezzel\n" +
                " megalapozva a keresztény Magyarországot. 1083 augusztus 20.-án szentté\n" +
                " avatták a Szent Péter-bazilikában. Az Árpád-korban az István-kultusz az\n" +
                " uralkodó dinasztiát támogatta és egységet teremtett a népcsoportok\n" +
                " között. 1686-ban XI. Ince pápa elrendelte, hogy az egész katolikus világ\n" +
                " emlékezzen meg róla, majd 1771-ben Mária Terézia országos ünneppé tette.\n" +
                " Az 1848–49-es forradalom és szabadságharc leverése után egy időre\n" +
                " szünetelt az ünneplés, de 1891-ben Ferenc József munkaszüneti nappá\n" +
                " nyilvánította. A két világháború között a nemzeti egység\n" +
                " szimbólumává vált. 1950-től az új államrendszer ideológiája szerint\n" +
                " alakították át az ünnep jelentését, majd 1991-ben ismét állami\n" +
                " ünneppé nyilvánították."));
        feastDays.add(new FeastDay(2027, 9, 23, "1956-os Forradalom", "Magyarország nemzeti ünnepe, amely az 1956-os forradalom és szabadságharc\n" +
                " eseményeire emlékezik. Az ünnep hivatalosan 1989 október 23.-án vált\n" +
                " nemzeti ünneppé a rendszerváltás után, amikor a forradalom hőseit\n" +
                " ünnepelve tisztelegtek a szabadságért és a demokráciáért folytatott\n" +
                " küzdelmük előtt. Az 1956-os forradalom a szovjet megszállás elleni\n" +
                " népfelkelés volt, amely bár véresen végződött, alapvető szerepet\n" +
                " játszott Magyarország függetlenségi törekvéseiben és a későbbi\n" +
                " rendszerváltásban. Az október 23.-i események máig a szabadság és a\n" +
                " nemzeti szuverenitás fontos szimbólumai."));
        feastDays.add(new FeastDay(2027, 9, 25, "Óraátállítás", "Az őszi óraátállítás (a téli időszámítás kezdete) – (vasárnap),\n" +
                " 3:00 → 2:00\n" +
                " Ekkor kell 1 órát visszaállítani az órákat.\n" +
                " Az őszi óraátállítás célja, hogy reggel világosabb legyen, így a\n" +
                " természetes fényt jobban kihasználjuk. Magyarországon először 1916-ban\n" +
                " vezették be az óraátállítást energiatakarékossági okokból. Bár több\n" +
                " alkalommal szünetelt, végül 1980-ban újra bevezették, hogy csökkentsék a\n" +
                " villamosenergia-felhasználást. A téli időszámítás október utolsó\n" +
                " vasárnapján kezdődik, amikor az órákat visszaállítjuk. Ezáltal reggel\n" +
                " hamarabb világosodik, de este hamarabb sötétedik. 1996 óta kormányrendelet\n" +
                " szabályozza az óraátállítást."));
        feastDays.add(new FeastDay(2027, 10, 1, "Mindenszentek", "Mindenszentek ünnepe november 1.-én van, amikor a katolikus egyház\n" +
                " megemlékezik minden üdvözült lélekről, akik elnyerték a mennyországot,\n" +
                " de nevüket nem tartják számon a naptárban. Az ünnep eredete a\n" +
                " kereszténység előtti halotti kultuszokra is visszavezethető. Hivatalosan a\n" +
                " 8. században III. Gergely pápa idején vezették be, aki a Szent\n" +
                " Péter-bazilikában kápolnát szentelt minden szent tiszteletére.\n" +
                " Magyarországon munkaszüneti nap. Sokan virágokkal és gyertyagyújtással\n" +
                " tisztelegnek elhunyt szeretteik emléke előtt. Ez a nap a hit és a\n" +
                " megemlékezés ünnepe."));
        feastDays.add(new FeastDay(2027, 10, 2, "Halottak napja", "Halottak napja november 2.-án van, amikor a katolikus egyház megemlékezik az\n" +
                " elhunyt hívekről. Az ünnep célja, hogy imádságokkal és jó\n" +
                " cselekedetekkel segítsük a halottak lelki üdvét és tisztelegjünk\n" +
                " szeretteink emléke előtt. Bár gyökerei a kereszténység előtti halotti\n" +
                " kultuszokig nyúlnak vissza, mai formáját a 11. században Cluny-i Szent\n" +
                " Odiló apát vezette be. Ő rendelte el, hogy a kolostorok imádkozzanak a\n" +
                " tisztítótűzben szenvedő lelkekért. A gyertyagyújtás és a virágok\n" +
                " elhelyezése máig az emlékezés fontos része."));
        feastDays.add(new FeastDay(2027, 10, 29, "Advent első napja", "Advent a karácsony előtti négyhetes időszak, mely Jézus Krisztus\n" +
                " születésére és második eljövetelére való várakozást jelenti. Az\n" +
                " advent származása a 4. századra nyúlik vissza, amikor bűnbánattal és\n" +
                " lelki felkészüléssel várták a karácsonyt. Kezdetben hosszabb böjti\n" +
                " időszak volt, de fokozatosan négyhetesre csökkent. Az adventi koszorú\n" +
                " hagyománya a 19. században terjedt el, az örökzöld növények a\n" +
                " halhatatlanságot és az újjászületést szimbolizálták. A négy gyertya a\n" +
                " hitet, a reményt, az örömöt és a szeretetet jelképezi. Minden vasárnap\n" +
                " egy újabb gyertya meggyújtásával közelebb kerülünk a karácsonyhoz. \n" +
                " Advent első vasárnapját a karácsonyt megelőző négy vasárnap egyikeként\n" +
                " ünneplik. Ez a nap általában november végére vagy december elejére esik."));
        feastDays.add(new FeastDay(2027, 11, 6, "Mikulás", "Mikuláskor december 5.-én éjszaka, vagy 6.-án reggel töltik meg a gyerekek\n" +
                " csizmáit édességgel és játékokkal.  Szent Miklós püspök emlék napja.\n" +
                " A 4. században élt szent, aki a gyermekek, a hajósok és a szegények\n" +
                " védőszentje lett, híres volt jótékonyságáról. Egy legenda szerint\n" +
                " titokban segített egy szegény ember három lányán, akiket így férjhez\n" +
                " tudott adni. Ebből a történetből született a hagyomány, hogy Szent\n" +
                " Miklós éjjelente ajándékokat hoz a jó gyerekeknek. Ma már világszerte\n" +
                " ismert a piros ruhás ,fehér szakállú Mikulás.  A Mikulás-hagyományok\n" +
                " országonként eltérőek. Egyes helyeken a Mikulás hozza az ajándékokat,\n" +
                " máshol a Télapó. A lényeg azonban mindenhol ugyanaz: a szeretet az\n" +
                " adakozás és a gyermeki öröm ünnepe."));
        feastDays.add(new FeastDay(2027, 11, 13, "Luca napja", "Luca napja a keresztény és a népi hagyományok keveréke. Szent Luca a 3.\n" +
                " századi vértanú, a látás védőszentje, de a népi hitvilágban a\n" +
                " termékenység, a jólét és a védelem szimbóluma. A hagyomány szerint ezen\n" +
                " a napon kezdődött a tél leghosszabb éjszakája, így Luca napja a fény és\n" +
                " a sötétség határán áll. Ezen a napon a nők nem dolgoztak, hogy bőséges\n" +
                " legyen a következő év termése. Szokás volt Luca-széket készíteni,\n" +
                " búzát ültetni és jóslásokat végezni. Luca napja a megújulás és a\n" +
                " remény ünnepe, a fény győzelme a sötétség felett és a következő év\n" +
                " jólétének biztosítása."));
        feastDays.add(new FeastDay(2027, 11, 21, "Téli napforduló", "Az év legrövidebb napja nap. \n" +
                " Napkelte: Körülbelül 08:49\n" +
                " Napnyugta: Körülbelül 16:29\n" +
                " Világos időtartam: Körülbelül 7 óra 40 perc\n" +
                " A téli napforduló a legrövidebb nap az évben, amikor a Nap a\n" +
                " legalacsonyabbra emelkedik az égbolton. Ez a jelenség a Föld\n" +
                " tengelyferdesége és keringése miatt következik be. Az északi féltekén a\n" +
                " napforduló környékén a Nap csak 8 órán keresztül tartózkodik a horizont\n" +
                " felett, így ez a leghosszabb éjszaka. Ezen a napon kezdődik a nappali fény\n" +
                " fokozatos növekedése, így a következő hónapokban egyre hosszabbak lesznek.\n" +
                " a nappalok."));
        feastDays.add(new FeastDay(2027, 11, 24, "Szenteste", "Szenteste a karácsonyi ünnepkör egyik legfontosabb napja, amely Jézus\n" +
                " Krisztus születésének előestéje. Bár Jézus pontos születési dátuma\n" +
                " nem ismert, a keresztény egyház a 4. században december 25.-ét jelölte meg\n" +
                " születésének napjaként, összekapcsolva azt a téli napfordulóval.\n" +
                " December 24.-én este a családok összegyűlnek, közösen vacsoráznak,\n" +
                " ajándékokat adnak egymásnak és karácsonyfát állítanak. Magyarországon\n" +
                " a hagyomány szerint a Jézuska hozza az ajándékokat, ezzel is megemlékezve\n" +
                " Jézus születésének csodájáról. A fenyőfa, amely a 16. századtól vált\n" +
                " a karácsony szimbólumává, az örök életet jelképezi. Az ajándékozás a\n" +
                " szeretet és a megbecsülés kifejezése, amely a három királyok\n" +
                " ajándékozására utal. Karácsony, bár elsősorban keresztény ünnep,\n" +
                " világszerte a szeretet, a béke és a család ünnepe lett."));
        feastDays.add(new FeastDay(2027, 11, 25, "Karácsony", "A keresztények számára Jézus Krisztus születésének ünnepe, melyet a 4.\n" +
                " század óta tartanak. Az ünnep a téli napfordulóhoz kapcsolódik,\n" +
                " szimbolizálva a fény győzelmét a sötétség felett. Karácsony a remény,\n" +
                " a szeretet és a megváltás ünnepe, amely a családi együttlét, az öröm\n" +
                " és a vallási szertartások ideje. Karácsonyfa, betlehem és csillag a\n" +
                " szimbólumok, amelyek Jézus születését, az örök életet és az\n" +
                " útmutatást képviselik. Az ünneplés világszerte eltérő, de mindegyik\n" +
                " kultúrában a szeretet és a béke fontosságát hangsúlyozza. Karácsony\n" +
                " lehetőséget ad az emberi kapcsolatok és az élet értelmének\n" +
                " átgondolására."));
        feastDays.add(new FeastDay(2027, 11, 26, "Karácsony másnapja", "A keresztény hagyományban Szent István, az első vértanú emléknapja,\n" +
                " azonban Magyarországon elsősorban Szent István királyra, az\n" +
                " államalapítóra emlékeznek. A 4. századi eredetű ünnep során Jézus\n" +
                " születése után következik Szent István napja, aki kulcsszerepet játszott\n" +
                " a kereszténység elterjesztésében Magyarországon. Bár Szent István a 11.\n" +
                " században élt, az egyház később tette december 26.-át emlékévé.\n" +
                " Magyarországon nemzeti hősként is tisztelik, mivel ő alapította az\n" +
                " államot. Ezen a napon az emberek szentmisére mennek, megemlékeznek\n" +
                " királyukról és különböző ünnepi rendezvényeken is részt vesznek."));
        feastDays.add(new FeastDay(2027, 11, 31, "Szilveszter", "A Szilveszter az év utolsó napja, amikor búcsút veszünk az óévtől és\n" +
                " ünnepeljük az új év kezdetét. Az ünnep neve Szent Szilveszter pápától\n" +
                " származik, aki 335 körül halt meg, de az újév ünneplésének hagyományai\n" +
                " sokkal régebbre nyúlnak vissza. Szilveszter éjszakáján családok és\n" +
                " barátok együtt ünnepelnek, vacsoráznak, tűzijátékot néznek és\n" +
                " jókívánságokat mondanak. A tűzijáték és a zajkeltés az ősi\n" +
                " hagyományokból ered. Az ünnep a megújulás, az összetartozás és a\n" +
                " remény kifejezője, miközben gazdaságilag is fontos szerepet játszik a\n" +
                " fogyasztói társadalomban.\n" +
                " Boldog Új Évet kívánok! Legyen tele örömmel, egészséggel és sikerrel.\n" +
                " Hozzon az új év boldog pillanatokat, új lehetőségeket és boldog együtt\n" +
                " töltött időt!"));
        feastDays.add(new FeastDay(2028, 0, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma.\n" +
                " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az\n" +
                " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy\n" +
                " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és\n" +
                " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a\n" +
                " mondás. Legyen ez a nap a változás első lépése. Boldogságot,\n" +
                " egészséget és kitartást kívánok az új év minden pillanatára!\n"
        ));
        feastDays.add(new FeastDay(2028, 0, 6, "Vízkereszt", " A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a\n" +
                " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus\n" +
                " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus\n" +
                " megkeresztelkedésére és az első csodájára. A három királyok aranyat,\n" +
                " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a\n" +
                " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet\n" +
                " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi\n" +
                " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak\n" +
                " házszenteléseket, Isten áldását kérve az otthonra és az új évre."));
        feastDays.add(new FeastDay(2028, 1, 14, "Valentin nap", "A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint\n" +
                " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században\n" +
                " titokban esketett meg szerelmespárokat, mert II. Claudius császár\n" +
                " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák.\n" +
                " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az\n" +
                " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a\n" +
                " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal\n" +
                " és üdvözlőkártyákkal ünneplik."));
        feastDays.add(new FeastDay(2028, 2, 1, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja,\n" +
                " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a\n" +
                " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A\n" +
                " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és\n" +
                " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki\n" +
                " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának\n" +
                " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel,\n" +
                " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére\n" +
                " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A\n" +
                " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap\n" +
                " előtt tartanak. Általában február végére vagy március elejére esik."));
        feastDays.add(new FeastDay(2028, 2, 8, "Nőnap", " Az első Nőnapot 1908 március 8.-án tartották. A női munkások\n" +
                " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az\n" +
                " ünnep célja a nők társadalmi, politikai és gazdasági jogainak\n" +
                " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben\n" +
                " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így\n" +
                " 1911-ben már több európai országban is megünnepelték. Magyarországon\n" +
                " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már\n" +
                " a nők jogainak elismerésére és tiszteletére emelkedett."));
        feastDays.add(new FeastDay(2028, 2, 15, "1848-as Forradalom", "Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a\n" +
                " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti\n" +
                " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a\n" +
                " sajtószabadságért, a jobb életkörülményekért és a függetlenségért.\n" +
                " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című\n" +
                " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet\n" +
                " függetlenségi törekvése a magyar történelem egyik legfontosabb\n" +
                " eseményévé vált. Az események hatására megerősödött a nemzeti\n" +
                " identitás és ma már a magyar szabadság és összetartozás napjaként\n" +
                " ünnepeljük.\n" +
                " "));
        feastDays.add(new FeastDay(2028, 2, 26, "Óraátállítás", " A tavaszi óraátállítás a nyári időszámítás kezdete. \n" +
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
                " óraátállítást Magyarországon."));
        feastDays.add(new FeastDay(2028, 3, 1, "Bolondok napja", "Április elseje a bolondok napja, a bolondos tréfák és ártalmatlan\n" +
                " átverések napja. Az emberek apró csínyekkel, például hamis hírekkel vagy\n" +
                " kitalált történetekkel szórakoztatják egymást. Az internet\n" +
                " elterjedésével az álhírek és vírusos videók is népszerűvé váltak. A\n" +
                " nap célja, hogy jókedvet és nevetést hozzon, miközben erősíti a\n" +
                " közösségi szellemet. Eredete több elméletre vezethető vissza. A\n" +
                " legnépszerűbb magyarázat szerint a XVI. században, a Gergely-naptár\n" +
                " bevezetésekor azok, akik áprilisban ünnepelték az újévet,\n" +
                " Franciaországban bolondnak számítottak, így tréfák céltáblájává\n" +
                " váltak. Hasonló szokások figyelhetők meg a kelta tavaszi ünnepekben és az\n" +
                " ókori római Hilaria ünnepén is, ahol a bolondos viselkedés a tavaszi\n" +
                " megújulást jelezte. "));
        feastDays.add(new FeastDay(2028, 3, 9, "Virágvasárnap", " A Virágvasárnap a húsvét előtti vasárnap, Jézus jeruzsálemi\n" +
                " bevonulásának emlékére van. Az evangéliumok szerint Jézus szamárháton\n" +
                " vonult be a városba, miközben a hívek pálmaágakat lengettek, üdvözölve\n" +
                " őt, mint a Megváltót. A virágok, a pálmák és a zöld ágak használata\n" +
                " az ünnep jelképei, melyek a békét és az üdvözítőt hirdetik. A\n" +
                " keresztény hagyományban Virágvasárnap a nagyböjt végét és a húsvéti\n" +
                " ünnepekre való lelki felkészülést jelzi. Az ünnep hivatalosan a 4.\n" +
                " század óta van jelen az egyházi naptárban és azóta része a keresztény\n" +
                " hagyományoknak világszerte."));
        feastDays.add(new FeastDay(2028, 3, 14, "Nagypéntek", "A Nagypéntek a keresztény húsvéti ünnepkör legfontosabb napja, amikor\n" +
                " Jézus Krisztus kereszthalálát és szenvedését emlékezzük meg. Az ünnep\n" +
                " célja, hogy a hívők átgondolják a megváltásra tett áldozat\n" +
                " jelentőségét. E napot a 4. század óta ünneplik, amikor a keresztény\n" +
                " egyház hivatalosan is elismerte Jézus kereszthalálának fontosságát.\n" +
                " Magyarországon a 20. század elejétől munkaszüneti nap. A keresztény\n" +
                " hagyományokban sötét napként van jelen, amikor sokan böjtölnek és\n" +
                " csendes megemlékezéseket tartanak. Az egyházi szertartások mellett a\n" +
                " keresztény közösség számára Nagypéntek a bűnbánat és a lelki\n" +
                " megújulás napja is.  Nagypénteket a húsvét előtti pénteken ünneplik,\n" +
                " tehát ez a dátum évente változik. \n" +
                " "));
        feastDays.add(new FeastDay(2028, 3, 16, "Húsvétvasárnap", "Húsvétvasárnap a kereszténység legfontosabb ünnepe, Jézus Krisztus\n" +
                " feltámadásának napja, amely a halál feletti győzelmet és az örök élet\n" +
                " ígéretét hirdeti. Magyarországon 1991 óta munkaszüneti nap, de\n" +
                " jelentősége évszázadok óta meghatározó. Húsvét a 40 napos nagyböjt\n" +
                " végét jelzi, amely a bűnbánat és a lelki megújulás időszaka. A gyerekek\n" +
                " az ajándékaikat hagyományosan ezen a napon kapják meg, édességeket és\n" +
                " tojásokat, amelyeket a húsvéti nyúl hoz.  A húsvéti nyúl a 16. századi\n" +
                " Németországból ered és a termékenység, valamint az újjászületés\n" +
                " jelképe. Eredete pogány hagyományokhoz köthető, ahol a nyúl és a tojás\n" +
                " a tavaszi újjászületés szimbóluma volt, melyek később beépültek a\n" +
                " keresztény húsvéti szokásokba.\n" +
                " A Húsvétot évente változó dátumon ünneplik, mivel a tavaszi\n" +
                " napéjegyenlőség utáni első teliholdat követő vasárnapra esik."));
        feastDays.add(new FeastDay(2028, 3, 17, "Húsvéthétfő", "Húsvéthétfő a húsvéti ünnepkör egyik legfontosabb napja, amely Jézus\n" +
                " Krisztus feltámadását követően az új élet és a megújulás\n" +
                " szimbólumává vált. Magyarországon a húsvéti locsolás hagyománya a 17.\n" +
                " századtól terjedt el. A férfiak vízzel vagy illatos kölnivel locsolják\n" +
                " meg a nőket, kívánva nekik egészséget, boldogságot és termékenységet.\n" +
                " A locsolás eredete valószínűleg a megtisztulás és a tavaszi megújulás\n" +
                " ősi hiedelmeiből származik. A húsvéti nyúl a tavasz, az új élet és a\n" +
                " termékenység jelképe, míg a bárány Krisztus áldozatát, ártatlanságát\n" +
                " és a megváltást szimbolizálja. Magyarországon a húsvétot a\n" +
                " kereszténység elterjedése óta ünneplik és a népszokások a 19.\n" +
                " századtól erősödtek meg."));
        feastDays.add(new FeastDay(2028, 3, 22, "A Föld napja", "A Föld napja 1970-ben indult el, amikor az Egyesült Államokban Gaylord\n" +
                " Nelson szenátor kezdeményezésére több millió ember gyűlt össze, hogy\n" +
                " felhívja a figyelmet a környezetszennyezésre és a természeti erőforrások\n" +
                " védelmére. A nap célja, hogy globálisan tudatosítsa az emberekben a\n" +
                " környezetvédelem fontosságát és ösztönözze a fenntartható életmódra\n" +
                " való áttérést. Azóta minden évben április 22.-én világszerte\n" +
                " megemlékeznek róla. Számos esemény, akció és kampány hívja fel a\n" +
                " figyelmet a bolygó védelmére és a környezeti problémák megoldására.\n" +
                " \n" +
                " \n" +
                " "));
        feastDays.add(new FeastDay(2028, 3, 30, "Májusfa állítás", "A májusfa felállításának legjellemzőbb hagyomány szerinti időpontja a\n" +
                " május 1-jét megelőző nap délután/este volt. \n" +
                " Magyarországon a középkor óta ismert, tehát legalább a 14. századtól\n" +
                " ünneplik. A szokás az ősi tavaszi rituálékra épül, melyek a természet\n" +
                " megújulását és az élet körforgását szimbolizálták. A fiatal legények\n" +
                " a lányok háza elé állították a feldíszített fákat, hogy kifejezzék\n" +
                " szerelmi szándékukat. A májusfát szalagokkal, virágokkal díszítették\n" +
                " és május végén vagy pünkösdkor táncos mulatság keretében bontották\n" +
                " le, amit kitáncolásnak neveztek. A hagyomány a szerelem és a termékenység\n" +
                " ünnepe volt, később pedig összefonódott a munka ünnepével (május 1.).\n" +
                " Mára már kevésbé elterjedt szokás, de sok helyen igyekeznek még\n" +
                " fenntartani."));
        feastDays.add(new FeastDay(2028, 4, 1, "A Munka ünnepe", "Május 1.-e a munka ünnepe. A munkások jogaiért és szolidaritásáért\n" +
                " folytatott küzdelem emléknapja világszerte. Az ünnep gyökerei az 1886-os\n" +
                " chicagói tüntetésekhez nyúlnak vissza, ahol a munkások a nyolcórás\n" +
                " munkaidő bevezetéséért demonstráltak. Az események hatására 1890-től\n" +
                " május 1.-ét a munkások nemzetközi szolidaritási napjává\n" +
                " nyilvánították.  Magyarországon először 1890-ben tartották meg, de\n" +
                " hivatalos állami ünnep csak 1946-ban lett. A szocializmus idején\n" +
                " nagyszabású felvonulásokkal és tömegrendezvényekkel ünnepelték. A\n" +
                " rendszerváltás után a jelentése átalakult, de továbbra is a munka\n" +
                " tiszteletét és a munkavállalók jogainak fontosságát hirdeti. "));
        feastDays.add(new FeastDay(2028, 4, 7, "Anyák napja", "Az édesanyák napja a szeretet, a tisztelet és a hála ünnepe, amelyet\n" +
                " világszerte az édesanyák tiszteletére tartanak. Gyökerei az ókori\n" +
                " Görögországba nyúlnak vissza, ahol Rheát, az istenek anyját ünnepelték.\n" +
                " A modern anyák napja az Egyesült Államokból indult 1908-ban, amikor Anna\n" +
                " Jarvis kampányt indított édesanyja emlékére. Magyarországon először\n" +
                " 1925-ben ünnepelték és a májusi Mária-tisztelet hagyományaival is\n" +
                " összekapcsolódott. Ez a nap a gondoskodó és szerető édesanyák, valamint\n" +
                " minden női gondviselő iránti megbecsülés kifejezésére szolgál.\n" +
                " Virágokkal, ajándékokkal és figyelmességgel fejezik ki hálájukat a\n" +
                " gyerekek. Bár világszerte eltérő időpontokban ünneplik, mindenhol az\n" +
                " anyák iránti szeretet és tisztelet áll a középpontban. Anyák napját\n" +
                " Magyarországon minden év május első vasárnapján ünneplik. \n" +
                " "));
        feastDays.add(new FeastDay(2028, 4, 28, "Gyermeknap", "A gyermeknap a gyerekek jogainak és jólétének fontosságát hirdető\n" +
                " ünnep, melyet világszerte különböző időpontokban tartanak. Eredete\n" +
                " 1925-re nyúlik vissza, amikor a genfi Gyermekjóléti Konferencián\n" +
                " elhatározták, hogy kiemelik a gyermekek védelmének fontosságát.\n" +
                " Magyarországon 1931-ben tartották meg először, majd 1950 óta május\n" +
                " utolsó vasárnapján ünneplik. Ez a nap a gyerekekről szól, akik ilyenkor\n" +
                " különféle programokon vehetnek részt, ahol játékokkal és szórakoztató\n" +
                " rendezvényekkel ünneplik őket. Az ünnep célja, hogy felhívja a figyelmet\n" +
                " a gyermekek jogaira és boldogságuk fontosságára. Magyarországon a\n" +
                " Gyermeknapot minden évben május utolsó vasárnapján ünneplik."));
        feastDays.add(new FeastDay(2028, 5, 4, "Pünkösd", "Pünkösd a kereszténység egyik legősibb ünnepe, melyet már a 4.\n" +
                " századtól kezdve ünnepelnek a keresztény egyházban. Az ünnep Jézus\n" +
                " mennybemenetele után ötven nappal emlékezik meg a Szentlélek\n" +
                " eljöveteléről, amely erőt adott az apostoloknak a keresztény hit\n" +
                " hirdetéséhez. Ez az esemény az egyház születésnapjaként is ismert. A\n" +
                " „pünkösd” elnevezés a görög „pentékoszté” szóból ered, ami\n" +
                " ötvenediket jelent. Magyarországon a középkor óta tartják számon és\n" +
                " számos népi hagyomány kapcsolódik hozzá, például a pünkösdi király\n" +
                " és királyné választása. Pünkösd vasárnapját a húsvét utáni 50.\n" +
                " napon, tehát 50 nappal húsvétvasárnap után ünneplik. "));
        feastDays.add(new FeastDay(2028, 5, 5, "Pünkösdhétfő", "A Pünkösdhétfő a keresztény egyház egyik legfontosabb ünnepének, a\n" +
                " pünkösdnek a második napja. Pünkösd a húsvét utáni 50. napon van, és a\n" +
                " Szentlélek eljövetelét ünnepli, amikor Jézus tanítványai erőt és\n" +
                " útmutatást kaptak küldetésükhöz. Az esemény a keresztény egyház\n" +
                " születésnapjának is tekinthető. Az ünnep gyökerei a zsidó pünkösdi\n" +
                " ünnepre (Sávuót) vezethetők vissza, amely az aratási hálaadás napja\n" +
                " volt. Magyarországon pünkösdhétfő munkaszüneti nap és hivatalosan 1993\n" +
                " óta ünnepeljük újra. Az ünnep a megújulást és a közösség\n" +
                " összetartozását jelképezi."));
        feastDays.add(new FeastDay(2028, 5, 18, "Apák napja", "Apák napja világszerte az édesapák tiszteletére szolgál, elismerve a\n" +
                " családban és a gyermeknevelésben betöltött szerepüket. Az ünnep eredete\n" +
                " az Egyesült Államokhoz köthető, ahol először 1910-ben tartották meg\n" +
                " Sonora Smart Dodd kezdeményezésére , aki édesapját akarta tiszteletben\n" +
                " részesíteni . Magyarországon az apák napja az 1990-es években kezdett\n" +
                " elterjedni  és 1994 óta ünnepeljük minden év június harmadik\n" +
                " vasárnapján. Célja, hogy elismerje az apák és az apaszerepet betöltő\n" +
                " férfiak szeretetét, áldozatvállalását és a családban betöltött\n" +
                " szerepét. Ez a nap lehetőséget ad arra, hogy köszönetet mondjunk az apák\n" +
                " szeretetéért, támogatásáért és áldozatos munkájáért, amelyet\n" +
                " családjukért tesznek. "));
        feastDays.add(new FeastDay(2028, 5, 20, "Nyári napforduló", "Az év leghosszabb napja. \n" +
                " Napkelte: \n" +
                " Napnyugta: \n" +
                " Világos időtartam: \n" +
                " A nyári napforduló a leghosszabb nap az évben, amikor a Nap a legmagasabbra\n" +
                " emelkedik az égbolton. Ez a jelenség a Föld tengelyferdesége és a\n" +
                " keringése miatt következik be. Az északi féltekén a napforduló\n" +
                " környékén a Nap több mint 15 órán keresztül a horizont felett\n" +
                " tartózkodik. Ezen a napon a Föld az északi féltekén éri el a leghosszabb\n" +
                " nappali órákat, így ez a legrövidebb éjszaka."));
        feastDays.add(new FeastDay(2028, 7, 20, "Szent István-nap", "Szent István király koronázása körülbelül 1000-ben történt, ezzel\n" +
                " megalapozva a keresztény Magyarországot. 1083 augusztus 20.-án szentté\n" +
                " avatták a Szent Péter-bazilikában. Az Árpád-korban az István-kultusz az\n" +
                " uralkodó dinasztiát támogatta és egységet teremtett a népcsoportok\n" +
                " között. 1686-ban XI. Ince pápa elrendelte, hogy az egész katolikus világ\n" +
                " emlékezzen meg róla, majd 1771-ben Mária Terézia országos ünneppé tette.\n" +
                " Az 1848–49-es forradalom és szabadságharc leverése után egy időre\n" +
                " szünetelt az ünneplés, de 1891-ben Ferenc József munkaszüneti nappá\n" +
                " nyilvánította. A két világháború között a nemzeti egység\n" +
                " szimbólumává vált. 1950-től az új államrendszer ideológiája szerint\n" +
                " alakították át az ünnep jelentését, majd 1991-ben ismét állami\n" +
                " ünneppé nyilvánították."));
        feastDays.add(new FeastDay(2028, 9, 23, "1956-os Forradalom", "Magyarország nemzeti ünnepe, amely az 1956-os forradalom és szabadságharc\n" +
                " eseményeire emlékezik. Az ünnep hivatalosan 1989 október 23.-án vált\n" +
                " nemzeti ünneppé a rendszerváltás után, amikor a forradalom hőseit\n" +
                " ünnepelve tisztelegtek a szabadságért és a demokráciáért folytatott\n" +
                " küzdelmük előtt. Az 1956-os forradalom a szovjet megszállás elleni\n" +
                " népfelkelés volt, amely bár véresen végződött, alapvető szerepet\n" +
                " játszott Magyarország függetlenségi törekvéseiben és a későbbi\n" +
                " rendszerváltásban. Az október 23.-i események máig a szabadság és a\n" +
                " nemzeti szuverenitás fontos szimbólumai."));
        feastDays.add(new FeastDay(2028, 9, 29, "Óraátállítás", "Az őszi óraátállítás (a téli időszámítás kezdete) – (vasárnap),\n" +
                " 3:00 → 2:00\n" +
                " Ekkor kell 1 órát visszaállítani az órákat.\n" +
                " Az őszi óraátállítás célja, hogy reggel világosabb legyen, így a\n" +
                " természetes fényt jobban kihasználjuk. Magyarországon először 1916-ban\n" +
                " vezették be az óraátállítást energiatakarékossági okokból. Bár több\n" +
                " alkalommal szünetelt, végül 1980-ban újra bevezették, hogy csökkentsék a\n" +
                " villamosenergia-felhasználást. A téli időszámítás október utolsó\n" +
                " vasárnapján kezdődik, amikor az órákat visszaállítjuk. Ezáltal reggel\n" +
                " hamarabb világosodik, de este hamarabb sötétedik.  1996 óta\n" +
                " kormányrendelet szabályozza az óraátállítást.\n" +
                " "));
        feastDays.add(new FeastDay(2028, 10, 1, "Mindenszentek", "Mindenszentek ünnepe november 1.-én van, amikor a katolikus egyház\n" +
                " megemlékezik minden üdvözült lélekről, akik elnyerték a mennyországot,\n" +
                " de nevüket nem tartják számon a naptárban. Az ünnep eredete a\n" +
                " kereszténység előtti halotti kultuszokra is visszavezethető. Hivatalosan a\n" +
                " 8. században III. Gergely pápa idején vezették be, aki a Szent\n" +
                " Péter-bazilikában kápolnát szentelt minden szent tiszteletére.\n" +
                " Magyarországon munkaszüneti nap. Sokan virágokkal és gyertyagyújtással\n" +
                " tisztelegnek elhunyt szeretteik emléke előtt. Ez a nap a hit és a\n" +
                " megemlékezés ünnepe."));
        feastDays.add(new FeastDay(2028, 10, 2, "Halottak napja", "Halottak napja november 2.-án van, amikor a katolikus egyház megemlékezik az\n" +
                " elhunyt hívekről. Az ünnep célja, hogy imádságokkal és jó\n" +
                " cselekedetekkel segítsük a halottak lelki üdvét és tisztelegjünk\n" +
                " szeretteink emléke előtt. Bár gyökerei a kereszténység előtti halotti\n" +
                " kultuszokig nyúlnak vissza, mai formáját a 11. században Cluny-i Szent\n" +
                " Odiló apát vezette be. Ő rendelte el, hogy a kolostorok imádkozzanak a\n" +
                " tisztítótűzben szenvedő lelkekért. A gyertyagyújtás és a virágok\n" +
                " elhelyezése máig az emlékezés fontos része. "));
        feastDays.add(new FeastDay(2028, 11, 3, "Advent első napja", "Advent a karácsony előtti négyhetes időszak, mely Jézus Krisztus\n" +
                " születésére és második eljövetelére való várakozást jelenti. Az\n" +
                " advent származása a 4. századra nyúlik vissza, amikor bűnbánattal és\n" +
                " lelki felkészüléssel várták a karácsonyt. Kezdetben hosszabb böjti\n" +
                " időszak volt, de fokozatosan négyhetesre csökkent. Az adventi koszorú\n" +
                " hagyománya a 19. században terjedt el, az örökzöld növények a\n" +
                " halhatatlanságot és az újjászületést szimbolizálták. A négy gyertya a\n" +
                " hitet, a reményt, az örömöt és a szeretetet jelképezi. Minden vasárnap\n" +
                " egy újabb gyertya meggyújtásával közelebb kerülünk a karácsonyhoz. \n" +
                " Advent első vasárnapját a karácsonyt megelőző négy vasárnap egyikeként\n" +
                " ünneplik. Ez a nap általában november végére vagy december elejére esik. \n" +
                " "));
        feastDays.add(new FeastDay(2028, 11, 6, "Mikulás", " Mikuláskor december 5.-én éjszaka, vagy 6.-án reggel töltik meg a gyerekek\n" +
                " csizmáit édességgel és játékokkal. Szent Miklós püspök emlék napja. A\n" +
                " 4. században élt szent, aki a gyermekek, a hajósok és a szegények\n" +
                " védőszentje lett, híres volt jótékonyságáról. Egy legenda szerint\n" +
                " titokban segített egy szegény ember három lányán, akiket így férjhez\n" +
                " tudott adni.\n" +
                " Ebből a történetből született a hagyomány, hogy Szent Miklós éjjelente\n" +
                " ajándékokat hoz a jó gyerekeknek. Ma már világszerte ismert a piros\n" +
                " ruhás, fehér szakállú Mikulás.  A Mikulás-hagyományok országonként\n" +
                " eltérőek. Egyes helyeken a Mikulás hozza az ajándékokat, máshol a\n" +
                " Télapó. A lényeg azonban mindenhol ugyanaz: a szeretet az adakozás és a\n" +
                " gyermeki öröm ünnepe"));
        feastDays.add(new FeastDay(2028, 11, 13, "Luca napja", "Luca napja a keresztény és a népi hagyományok keveréke. Szent Luca a 3.\n" +
                " századi vértanú, a látás védőszentje, de a népi hitvilágban a\n" +
                " termékenység, a jólét és a védelem szimbóluma. A hagyomány szerint ezen\n" +
                " a napon kezdődött a tél leghosszabb éjszakája, így Luca napja a fény és\n" +
                " a sötétség határán áll. Ezen a napon a nők nem dolgoztak, hogy bőséges\n" +
                " legyen a következő év termése. Szokás volt Luca-széket készíteni,\n" +
                " búzát ültetni és jóslásokat végezni. Luca napja a megújulás és a\n" +
                " remény ünnepe, a fény győzelme a sötétség felett és a következő év\n" +
                " jólétének biztosítása.\n" +
                " "));
        feastDays.add(new FeastDay(2028, 11, 21, "Téli napforduló", "Az év legrövidebb napja nap. \n" +
                " Napkelte: \n" +
                " Napnyugta: \n" +
                " Világos időtartam: \n" +
                " A téli napforduló a legrövidebb nap az évben, amikor a Nap a\n" +
                " legalacsonyabbra emelkedik az égbolton. Ez a jelenség a Föld\n" +
                " tengelyferdesége és keringése miatt következik be. Az északi féltekén a\n" +
                " napforduló környékén a Nap csak 8 órán keresztül tartózkodik a horizont\n" +
                " felett, így ez a leghosszabb éjszaka. Ezen a napon kezdődik a nappali fény\n" +
                " fokozatos növekedése, így a következő hónapokban egyre hosszabbak lesznek\n" +
                " a nappalok."));
        feastDays.add(new FeastDay(2028, 11, 24, "Szenteste", "Szenteste a karácsonyi ünnepkör egyik legfontosabb napja, amely Jézus\n" +
                " Krisztus születésének előestéje. Bár Jézus pontos születési dátuma\n" +
                " nem ismert, a keresztény egyház a 4. században december 25.-ét jelölte meg\n" +
                " születésének napjaként, összekapcsolva azt a téli napfordulóval.\n" +
                " December 24.-én este a családok összegyűlnek, közösen vacsoráznak,\n" +
                " ajándékokat adnak egymásnak és karácsonyfát állítanak. Magyarországon\n" +
                " a hagyomány szerint a Jézuska hozza az ajándékokat, ezzel is megemlékezve\n" +
                " Jézus születésének csodájáról. A fenyőfa, amely a 16. századtól vált\n" +
                " a karácsony szimbólumává, az örök életet jelképezi. Az ajándékozás a\n" +
                " szeretet és a megbecsülés kifejezése, amely a három királyok\n" +
                " ajándékozására utal. Karácsony, bár elsősorban keresztény ünnep,\n" +
                " világszerte a szeretet, a béke és a család ünnepe lett."));
        feastDays.add(new FeastDay(2028, 11, 25, "Karácsony", "A keresztények számára Jézus Krisztus születésének ünnepe, melyet a 4.\n" +
                " század óta tartanak. Az ünnep a téli napfordulóhoz kapcsolódik,\n" +
                " szimbolizálva a fény győzelmét a sötétség felett. Karácsony a remény,\n" +
                " a szeretet és a megváltás ünnepe, amely a családi együttlét, az öröm\n" +
                " és a vallási szertartások ideje. Karácsonyfa, betlehem és csillag a\n" +
                " szimbólumok, amelyek Jézus születését, az örök életet és az\n" +
                " útmutatást képviselik. Az ünneplés világszerte eltérő, de mindegyik\n" +
                " kultúrában a szeretet és a béke fontosságát hangsúlyozza. Karácsony\n" +
                " lehetőséget ad az emberi kapcsolatok és az élet értelmének\n" +
                " átgondolására"));
        feastDays.add(new FeastDay(2028, 11, 26, "Karácsony másnapja", "A keresztény hagyományban Szent István, az első vértanú emléknapja,\n" +
                " azonban Magyarországon elsősorban Szent István királyra, az\n" +
                " államalapítóra emlékeznek. A 4. századi eredetű ünnep során Jézus\n" +
                " születése után következik Szent István napja, aki kulcsszerepet játszott\n" +
                " a kereszténység elterjesztésében Magyarországon. Bár Szent István a 11.\n" +
                " században élt, az egyház később tette december 26.-át emlékévé.\n" +
                " Magyarországon nemzeti hősként is tisztelik, mivel ő alapította az\n" +
                " államot. Ezen a napon az emberek szentmisére mennek, megemlékeznek\n" +
                " királyukról és különböző ünnepi rendezvényeken is részt vesznek"));
        feastDays.add(new FeastDay(2028, 11, 31, "Szilveszter", "A Szilveszter az év utolsó napja, amikor búcsút veszünk az óévtől és\n" +
                " ünnepeljük az új év kezdetét. Az ünnep neve Szent Szilveszter pápától\n" +
                " származik, aki 335 körül halt meg, de az újév ünneplésének hagyományai\n" +
                " sokkal régebbre nyúlnak vissza. Szilveszter éjszakáján családok és\n" +
                " barátok együtt ünnepelnek, vacsoráznak, tűzijátékot néznek és\n" +
                " jókívánságokat mondanak. A tűzijáték és a zajkeltés az ősi\n" +
                " hagyományokból ered. Az ünnep a megújulás, az összetartozás és a\n" +
                " remény kifejezője, miközben gazdaságilag is fontos szerepet játszik a\n" +
                " fogyasztói társadalomban.\n" +
                " \n" +
                " Boldog Új Évet kívánok! Legyen tele örömmel, egészséggel és sikerrel.\n" +
                " Hozzon az új év boldog pillanatokat, új lehetőségeket és boldog együtt\n" +
                " töltött időt!"));
        feastDays.add(new FeastDay(2029, 0, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma.\n" +
                " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az\n" +
                " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy\n" +
                " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és\n" +
                " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a\n" +
                " mondás. Legyen ez a nap a változás első lépése. Boldogságot,\n" +
                " egészséget és kitartást kívánok az új év minden pillanatára!\n"
        ));
        feastDays.add(new FeastDay(2029, 0, 6, "Vízkereszt", " A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a\n" +
                " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus\n" +
                " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus\n" +
                " megkeresztelkedésére és az első csodájára. A három királyok aranyat,\n" +
                " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a\n" +
                " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet\n" +
                " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi\n" +
                " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak\n" +
                " házszenteléseket, Isten áldását kérve az otthonra és az új évre."));
        feastDays.add(new FeastDay(2029, 1, 14, "Valentin nap", "A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint\n" +
                " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században\n" +
                " titokban esketett meg szerelmespárokat, mert II. Claudius császár\n" +
                " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák.\n" +
                " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az\n" +
                " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a\n" +
                " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal\n" +
                " és üdvözlőkártyákkal ünneplik."));
        feastDays.add(new FeastDay(2029, 2, 1, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja,\n" +
                " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a\n" +
                " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A\n" +
                " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és\n" +
                " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki\n" +
                " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának\n" +
                " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel,\n" +
                " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére\n" +
                " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A\n" +
                " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap\n" +
                " előtt tartanak. Általában február végére vagy március elejére esik."));
        feastDays.add(new FeastDay(2029, 2, 8, "Nőnap", " Az első Nőnapot 1908 március 8.-án tartották. A női munkások\n" +
                " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az\n" +
                " ünnep célja a nők társadalmi, politikai és gazdasági jogainak\n" +
                " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben\n" +
                " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így\n" +
                " 1911-ben már több európai országban is megünnepelték. Magyarországon\n" +
                " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már\n" +
                " a nők jogainak elismerésére és tiszteletére emelkedett."));
        feastDays.add(new FeastDay(2029, 2, 15, "1848-as Forradalom", "Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a\n" +
                " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti\n" +
                " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a\n" +
                " sajtószabadságért, a jobb életkörülményekért és a függetlenségért.\n" +
                " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című\n" +
                " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet\n" +
                " függetlenségi törekvése a magyar történelem egyik legfontosabb\n" +
                " eseményévé vált. Az események hatására megerősödött a nemzeti\n" +
                " identitás és ma már a magyar szabadság és összetartozás napjaként\n" +
                " ünnepeljük.\n" +
                " "));
        feastDays.add(new FeastDay(2029, 2, 26, "Óraátállítás", " A tavaszi óraátállítás a nyári időszámítás kezdete. \n" +
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
                " óraátállítást Magyarországon."));
        feastDays.add(new FeastDay(2029, 3, 1, "Bolondok napja", "Április elseje a bolondok napja, a bolondos tréfák és ártalmatlan\n" +
                " átverések napja. Az emberek apró csínyekkel, például hamis hírekkel vagy\n" +
                " kitalált történetekkel szórakoztatják egymást. Az internet\n" +
                " elterjedésével az álhírek és vírusos videók is népszerűvé váltak. A\n" +
                " nap célja, hogy jókedvet és nevetést hozzon, miközben erősíti a\n" +
                " közösségi szellemet. Eredete több elméletre vezethető vissza. A\n" +
                " legnépszerűbb magyarázat szerint a XVI. században, a Gergely-naptár\n" +
                " bevezetésekor azok, akik áprilisban ünnepelték az újévet,\n" +
                " Franciaországban bolondnak számítottak, így tréfák céltáblájává\n" +
                " váltak. Hasonló szokások figyelhetők meg a kelta tavaszi ünnepekben és az\n" +
                " ókori római Hilaria ünnepén is, ahol a bolondos viselkedés a tavaszi\n" +
                " megújulást jelezte. "));
        feastDays.add(new FeastDay(2029, 3, 9, "Virágvasárnap", " A Virágvasárnap a húsvét előtti vasárnap, Jézus jeruzsálemi\n" +
                " bevonulásának emlékére van. Az evangéliumok szerint Jézus szamárháton\n" +
                " vonult be a városba, miközben a hívek pálmaágakat lengettek, üdvözölve\n" +
                " őt, mint a Megváltót. A virágok, a pálmák és a zöld ágak használata\n" +
                " az ünnep jelképei, melyek a békét és az üdvözítőt hirdetik. A\n" +
                " keresztény hagyományban Virágvasárnap a nagyböjt végét és a húsvéti\n" +
                " ünnepekre való lelki felkészülést jelzi. Az ünnep hivatalosan a 4.\n" +
                " század óta van jelen az egyházi naptárban és azóta része a keresztény\n" +
                " hagyományoknak világszerte."));
        feastDays.add(new FeastDay(2029, 3, 14, "Nagypéntek", "A Nagypéntek a keresztény húsvéti ünnepkör legfontosabb napja, amikor\n" +
                " Jézus Krisztus kereszthalálát és szenvedését emlékezzük meg. Az ünnep\n" +
                " célja, hogy a hívők átgondolják a megváltásra tett áldozat\n" +
                " jelentőségét. E napot a 4. század óta ünneplik, amikor a keresztény\n" +
                " egyház hivatalosan is elismerte Jézus kereszthalálának fontosságát.\n" +
                " Magyarországon a 20. század elejétől munkaszüneti nap. A keresztény\n" +
                " hagyományokban sötét napként van jelen, amikor sokan böjtölnek és\n" +
                " csendes megemlékezéseket tartanak. Az egyházi szertartások mellett a\n" +
                " keresztény közösség számára Nagypéntek a bűnbánat és a lelki\n" +
                " megújulás napja is.  Nagypénteket a húsvét előtti pénteken ünneplik,\n" +
                " tehát ez a dátum évente változik. \n" +
                " "));
        feastDays.add(new FeastDay(2029, 3, 16, "Húsvétvasárnap", "Húsvétvasárnap a kereszténység legfontosabb ünnepe, Jézus Krisztus\n" +
                " feltámadásának napja, amely a halál feletti győzelmet és az örök élet\n" +
                " ígéretét hirdeti. Magyarországon 1991 óta munkaszüneti nap, de\n" +
                " jelentősége évszázadok óta meghatározó. Húsvét a 40 napos nagyböjt\n" +
                " végét jelzi, amely a bűnbánat és a lelki megújulás időszaka. A gyerekek\n" +
                " az ajándékaikat hagyományosan ezen a napon kapják meg, édességeket és\n" +
                " tojásokat, amelyeket a húsvéti nyúl hoz.  A húsvéti nyúl a 16. századi\n" +
                " Németországból ered és a termékenység, valamint az újjászületés\n" +
                " jelképe. Eredete pogány hagyományokhoz köthető, ahol a nyúl és a tojás\n" +
                " a tavaszi újjászületés szimbóluma volt, melyek később beépültek a\n" +
                " keresztény húsvéti szokásokba.\n" +
                " A Húsvétot évente változó dátumon ünneplik, mivel a tavaszi\n" +
                " napéjegyenlőség utáni első teliholdat követő vasárnapra esik."));
        feastDays.add(new FeastDay(2029, 3, 17, "Húsvéthétfő", "Húsvéthétfő a húsvéti ünnepkör egyik legfontosabb napja, amely Jézus\n" +
                " Krisztus feltámadását követően az új élet és a megújulás\n" +
                " szimbólumává vált. Magyarországon a húsvéti locsolás hagyománya a 17.\n" +
                " századtól terjedt el. A férfiak vízzel vagy illatos kölnivel locsolják\n" +
                " meg a nőket, kívánva nekik egészséget, boldogságot és termékenységet.\n" +
                " A locsolás eredete valószínűleg a megtisztulás és a tavaszi megújulás\n" +
                " ősi hiedelmeiből származik. A húsvéti nyúl a tavasz, az új élet és a\n" +
                " termékenység jelképe, míg a bárány Krisztus áldozatát, ártatlanságát\n" +
                " és a megváltást szimbolizálja. Magyarországon a húsvétot a\n" +
                " kereszténység elterjedése óta ünneplik és a népszokások a 19.\n" +
                " századtól erősödtek meg."));
        feastDays.add(new FeastDay(2029, 3, 22, "A Föld napja", "A Föld napja 1970-ben indult el, amikor az Egyesült Államokban Gaylord\n" +
                " Nelson szenátor kezdeményezésére több millió ember gyűlt össze, hogy\n" +
                " felhívja a figyelmet a környezetszennyezésre és a természeti erőforrások\n" +
                " védelmére. A nap célja, hogy globálisan tudatosítsa az emberekben a\n" +
                " környezetvédelem fontosságát és ösztönözze a fenntartható életmódra\n" +
                " való áttérést. Azóta minden évben április 22.-én világszerte\n" +
                " megemlékeznek róla. Számos esemény, akció és kampány hívja fel a\n" +
                " figyelmet a bolygó védelmére és a környezeti problémák megoldására.\n" +
                " \n" +
                " \n" +
                " "));
        feastDays.add(new FeastDay(2029, 3, 30, "Májusfa állítás", "A májusfa felállításának legjellemzőbb hagyomány szerinti időpontja a\n" +
                " május 1-jét megelőző nap délután/este volt. \n" +
                " Magyarországon a középkor óta ismert, tehát legalább a 14. századtól\n" +
                " ünneplik. A szokás az ősi tavaszi rituálékra épül, melyek a természet\n" +
                " megújulását és az élet körforgását szimbolizálták. A fiatal legények\n" +
                " a lányok háza elé állították a feldíszített fákat, hogy kifejezzék\n" +
                " szerelmi szándékukat. A májusfát szalagokkal, virágokkal díszítették\n" +
                " és május végén vagy pünkösdkor táncos mulatság keretében bontották\n" +
                " le, amit kitáncolásnak neveztek. A hagyomány a szerelem és a termékenység\n" +
                " ünnepe volt, később pedig összefonódott a munka ünnepével (május 1.).\n" +
                " Mára már kevésbé elterjedt szokás, de sok helyen igyekeznek még\n" +
                " fenntartani."));
        feastDays.add(new FeastDay(2029, 4, 1, "A Munka ünnepe", "Május 1.-e a munka ünnepe. A munkások jogaiért és szolidaritásáért\n" +
                " folytatott küzdelem emléknapja világszerte. Az ünnep gyökerei az 1886-os\n" +
                " chicagói tüntetésekhez nyúlnak vissza, ahol a munkások a nyolcórás\n" +
                " munkaidő bevezetéséért demonstráltak. Az események hatására 1890-től\n" +
                " május 1.-ét a munkások nemzetközi szolidaritási napjává\n" +
                " nyilvánították.  Magyarországon először 1890-ben tartották meg, de\n" +
                " hivatalos állami ünnep csak 1946-ban lett. A szocializmus idején\n" +
                " nagyszabású felvonulásokkal és tömegrendezvényekkel ünnepelték. A\n" +
                " rendszerváltás után a jelentése átalakult, de továbbra is a munka\n" +
                " tiszteletét és a munkavállalók jogainak fontosságát hirdeti. "));
        feastDays.add(new FeastDay(2029, 4, 7, "Anyák napja", "Az édesanyák napja a szeretet, a tisztelet és a hála ünnepe, amelyet\n" +
                " világszerte az édesanyák tiszteletére tartanak. Gyökerei az ókori\n" +
                " Görögországba nyúlnak vissza, ahol Rheát, az istenek anyját ünnepelték.\n" +
                " A modern anyák napja az Egyesült Államokból indult 1908-ban, amikor Anna\n" +
                " Jarvis kampányt indított édesanyja emlékére. Magyarországon először\n" +
                " 1925-ben ünnepelték és a májusi Mária-tisztelet hagyományaival is\n" +
                " összekapcsolódott. Ez a nap a gondoskodó és szerető édesanyák, valamint\n" +
                " minden női gondviselő iránti megbecsülés kifejezésére szolgál.\n" +
                " Virágokkal, ajándékokkal és figyelmességgel fejezik ki hálájukat a\n" +
                " gyerekek. Bár világszerte eltérő időpontokban ünneplik, mindenhol az\n" +
                " anyák iránti szeretet és tisztelet áll a középpontban. Anyák napját\n" +
                " Magyarországon minden év május első vasárnapján ünneplik. \n" +
                " "));
        feastDays.add(new FeastDay(2029, 4, 28, "Gyermeknap", "A gyermeknap a gyerekek jogainak és jólétének fontosságát hirdető\n" +
                " ünnep, melyet világszerte különböző időpontokban tartanak. Eredete\n" +
                " 1925-re nyúlik vissza, amikor a genfi Gyermekjóléti Konferencián\n" +
                " elhatározták, hogy kiemelik a gyermekek védelmének fontosságát.\n" +
                " Magyarországon 1931-ben tartották meg először, majd 1950 óta május\n" +
                " utolsó vasárnapján ünneplik. Ez a nap a gyerekekről szól, akik ilyenkor\n" +
                " különféle programokon vehetnek részt, ahol játékokkal és szórakoztató\n" +
                " rendezvényekkel ünneplik őket. Az ünnep célja, hogy felhívja a figyelmet\n" +
                " a gyermekek jogaira és boldogságuk fontosságára. Magyarországon a\n" +
                " Gyermeknapot minden évben május utolsó vasárnapján ünneplik."));
        feastDays.add(new FeastDay(2029, 5, 4, "Pünkösd", "Pünkösd a kereszténység egyik legősibb ünnepe, melyet már a 4.\n" +
                " századtól kezdve ünnepelnek a keresztény egyházban. Az ünnep Jézus\n" +
                " mennybemenetele után ötven nappal emlékezik meg a Szentlélek\n" +
                " eljöveteléről, amely erőt adott az apostoloknak a keresztény hit\n" +
                " hirdetéséhez. Ez az esemény az egyház születésnapjaként is ismert. A\n" +
                " „pünkösd” elnevezés a görög „pentékoszté” szóból ered, ami\n" +
                " ötvenediket jelent. Magyarországon a középkor óta tartják számon és\n" +
                " számos népi hagyomány kapcsolódik hozzá, például a pünkösdi király\n" +
                " és királyné választása. Pünkösd vasárnapját a húsvét utáni 50.\n" +
                " napon, tehát 50 nappal húsvétvasárnap után ünneplik. "));
        feastDays.add(new FeastDay(2029, 5, 5, "Pünkösdhétfő", "A Pünkösdhétfő a keresztény egyház egyik legfontosabb ünnepének, a\n" +
                " pünkösdnek a második napja. Pünkösd a húsvét utáni 50. napon van, és a\n" +
                " Szentlélek eljövetelét ünnepli, amikor Jézus tanítványai erőt és\n" +
                " útmutatást kaptak küldetésükhöz. Az esemény a keresztény egyház\n" +
                " születésnapjának is tekinthető. Az ünnep gyökerei a zsidó pünkösdi\n" +
                " ünnepre (Sávuót) vezethetők vissza, amely az aratási hálaadás napja\n" +
                " volt. Magyarországon pünkösdhétfő munkaszüneti nap és hivatalosan 1993\n" +
                " óta ünnepeljük újra. Az ünnep a megújulást és a közösség\n" +
                " összetartozását jelképezi."));
        feastDays.add(new FeastDay(2029, 5, 18, "Apák napja", "Apák napja világszerte az édesapák tiszteletére szolgál, elismerve a\n" +
                " családban és a gyermeknevelésben betöltött szerepüket. Az ünnep eredete\n" +
                " az Egyesült Államokhoz köthető, ahol először 1910-ben tartották meg\n" +
                " Sonora Smart Dodd kezdeményezésére , aki édesapját akarta tiszteletben\n" +
                " részesíteni . Magyarországon az apák napja az 1990-es években kezdett\n" +
                " elterjedni  és 1994 óta ünnepeljük minden év június harmadik\n" +
                " vasárnapján. Célja, hogy elismerje az apák és az apaszerepet betöltő\n" +
                " férfiak szeretetét, áldozatvállalását és a családban betöltött\n" +
                " szerepét. Ez a nap lehetőséget ad arra, hogy köszönetet mondjunk az apák\n" +
                " szeretetéért, támogatásáért és áldozatos munkájáért, amelyet\n" +
                " családjukért tesznek. "));
        feastDays.add(new FeastDay(2029, 5, 20, "Nyári napforduló", "Az év leghosszabb napja. \n" +
                " Napkelte: \n" +
                " Napnyugta: \n" +
                " Világos időtartam: \n" +
                " A nyári napforduló a leghosszabb nap az évben, amikor a Nap a legmagasabbra\n" +
                " emelkedik az égbolton. Ez a jelenség a Föld tengelyferdesége és a\n" +
                " keringése miatt következik be. Az északi féltekén a napforduló\n" +
                " környékén a Nap több mint 15 órán keresztül a horizont felett\n" +
                " tartózkodik. Ezen a napon a Föld az északi féltekén éri el a leghosszabb\n" +
                " nappali órákat, így ez a legrövidebb éjszaka."));
        feastDays.add(new FeastDay(2029, 7, 20, "Szent István-nap", "Szent István király koronázása körülbelül 1000-ben történt, ezzel\n" +
                " megalapozva a keresztény Magyarországot. 1083 augusztus 20.-án szentté\n" +
                " avatták a Szent Péter-bazilikában. Az Árpád-korban az István-kultusz az\n" +
                " uralkodó dinasztiát támogatta és egységet teremtett a népcsoportok\n" +
                " között. 1686-ban XI. Ince pápa elrendelte, hogy az egész katolikus világ\n" +
                " emlékezzen meg róla, majd 1771-ben Mária Terézia országos ünneppé tette.\n" +
                " Az 1848–49-es forradalom és szabadságharc leverése után egy időre\n" +
                " szünetelt az ünneplés, de 1891-ben Ferenc József munkaszüneti nappá\n" +
                " nyilvánította. A két világháború között a nemzeti egység\n" +
                " szimbólumává vált. 1950-től az új államrendszer ideológiája szerint\n" +
                " alakították át az ünnep jelentését, majd 1991-ben ismét állami\n" +
                " ünneppé nyilvánították."));
        feastDays.add(new FeastDay(2029, 9, 23, "1956-os Forradalom", "Magyarország nemzeti ünnepe, amely az 1956-os forradalom és szabadságharc\n" +
                " eseményeire emlékezik. Az ünnep hivatalosan 1989 október 23.-án vált\n" +
                " nemzeti ünneppé a rendszerváltás után, amikor a forradalom hőseit\n" +
                " ünnepelve tisztelegtek a szabadságért és a demokráciáért folytatott\n" +
                " küzdelmük előtt. Az 1956-os forradalom a szovjet megszállás elleni\n" +
                " népfelkelés volt, amely bár véresen végződött, alapvető szerepet\n" +
                " játszott Magyarország függetlenségi törekvéseiben és a későbbi\n" +
                " rendszerváltásban. Az október 23.-i események máig a szabadság és a\n" +
                " nemzeti szuverenitás fontos szimbólumai."));
        feastDays.add(new FeastDay(2029, 9, 29, "Óraátállítás", "Az őszi óraátállítás (a téli időszámítás kezdete) – (vasárnap),\n" +
                " 3:00 → 2:00\n" +
                " Ekkor kell 1 órát visszaállítani az órákat.\n" +
                " Az őszi óraátállítás célja, hogy reggel világosabb legyen, így a\n" +
                " természetes fényt jobban kihasználjuk. Magyarországon először 1916-ban\n" +
                " vezették be az óraátállítást energiatakarékossági okokból. Bár több\n" +
                " alkalommal szünetelt, végül 1980-ban újra bevezették, hogy csökkentsék a\n" +
                " villamosenergia-felhasználást. A téli időszámítás október utolsó\n" +
                " vasárnapján kezdődik, amikor az órákat visszaállítjuk. Ezáltal reggel\n" +
                " hamarabb világosodik, de este hamarabb sötétedik.  1996 óta\n" +
                " kormányrendelet szabályozza az óraátállítást.\n" +
                " "));
        feastDays.add(new FeastDay(2029, 10, 1, "Mindenszentek", "Mindenszentek ünnepe november 1.-én van, amikor a katolikus egyház\n" +
                " megemlékezik minden üdvözült lélekről, akik elnyerték a mennyországot,\n" +
                " de nevüket nem tartják számon a naptárban. Az ünnep eredete a\n" +
                " kereszténység előtti halotti kultuszokra is visszavezethető. Hivatalosan a\n" +
                " 8. században III. Gergely pápa idején vezették be, aki a Szent\n" +
                " Péter-bazilikában kápolnát szentelt minden szent tiszteletére.\n" +
                " Magyarországon munkaszüneti nap. Sokan virágokkal és gyertyagyújtással\n" +
                " tisztelegnek elhunyt szeretteik emléke előtt. Ez a nap a hit és a\n" +
                " megemlékezés ünnepe."));
        feastDays.add(new FeastDay(2029, 10, 2, "Halottak napja", "Halottak napja november 2.-án van, amikor a katolikus egyház megemlékezik az\n" +
                " elhunyt hívekről. Az ünnep célja, hogy imádságokkal és jó\n" +
                " cselekedetekkel segítsük a halottak lelki üdvét és tisztelegjünk\n" +
                " szeretteink emléke előtt. Bár gyökerei a kereszténység előtti halotti\n" +
                " kultuszokig nyúlnak vissza, mai formáját a 11. században Cluny-i Szent\n" +
                " Odiló apát vezette be. Ő rendelte el, hogy a kolostorok imádkozzanak a\n" +
                " tisztítótűzben szenvedő lelkekért. A gyertyagyújtás és a virágok\n" +
                " elhelyezése máig az emlékezés fontos része. "));
        feastDays.add(new FeastDay(2029, 11, 3, "Advent első napja", "Advent a karácsony előtti négyhetes időszak, mely Jézus Krisztus\n" +
                " születésére és második eljövetelére való várakozást jelenti. Az\n" +
                " advent származása a 4. századra nyúlik vissza, amikor bűnbánattal és\n" +
                " lelki felkészüléssel várták a karácsonyt. Kezdetben hosszabb böjti\n" +
                " időszak volt, de fokozatosan négyhetesre csökkent. Az adventi koszorú\n" +
                " hagyománya a 19. században terjedt el, az örökzöld növények a\n" +
                " halhatatlanságot és az újjászületést szimbolizálták. A négy gyertya a\n" +
                " hitet, a reményt, az örömöt és a szeretetet jelképezi. Minden vasárnap\n" +
                " egy újabb gyertya meggyújtásával közelebb kerülünk a karácsonyhoz. \n" +
                " Advent első vasárnapját a karácsonyt megelőző négy vasárnap egyikeként\n" +
                " ünneplik. Ez a nap általában november végére vagy december elejére esik. \n" +
                " "));
        feastDays.add(new FeastDay(2029, 11, 6, "Mikulás", " Mikuláskor december 5.-én éjszaka, vagy 6.-án reggel töltik meg a gyerekek\n" +
                " csizmáit édességgel és játékokkal. Szent Miklós püspök emlék napja. A\n" +
                " 4. században élt szent, aki a gyermekek, a hajósok és a szegények\n" +
                " védőszentje lett, híres volt jótékonyságáról. Egy legenda szerint\n" +
                " titokban segített egy szegény ember három lányán, akiket így férjhez\n" +
                " tudott adni.\n" +
                " Ebből a történetből született a hagyomány, hogy Szent Miklós éjjelente\n" +
                " ajándékokat hoz a jó gyerekeknek. Ma már világszerte ismert a piros\n" +
                " ruhás, fehér szakállú Mikulás.  A Mikulás-hagyományok országonként\n" +
                " eltérőek. Egyes helyeken a Mikulás hozza az ajándékokat, máshol a\n" +
                " Télapó. A lényeg azonban mindenhol ugyanaz: a szeretet az adakozás és a\n" +
                " gyermeki öröm ünnepe"));
        feastDays.add(new FeastDay(2029, 11, 13, "Luca napja", "Luca napja a keresztény és a népi hagyományok keveréke. Szent Luca a 3.\n" +
                " századi vértanú, a látás védőszentje, de a népi hitvilágban a\n" +
                " termékenység, a jólét és a védelem szimbóluma. A hagyomány szerint ezen\n" +
                " a napon kezdődött a tél leghosszabb éjszakája, így Luca napja a fény és\n" +
                " a sötétség határán áll. Ezen a napon a nők nem dolgoztak, hogy bőséges\n" +
                " legyen a következő év termése. Szokás volt Luca-széket készíteni,\n" +
                " búzát ültetni és jóslásokat végezni. Luca napja a megújulás és a\n" +
                " remény ünnepe, a fény győzelme a sötétség felett és a következő év\n" +
                " jólétének biztosítása.\n" +
                " "));
        feastDays.add(new FeastDay(2029, 11, 21, "Téli napforduló", "Az év legrövidebb napja nap. \n" +
                " Napkelte: \n" +
                " Napnyugta: \n" +
                " Világos időtartam: \n" +
                " A téli napforduló a legrövidebb nap az évben, amikor a Nap a\n" +
                " legalacsonyabbra emelkedik az égbolton. Ez a jelenség a Föld\n" +
                " tengelyferdesége és keringése miatt következik be. Az északi féltekén a\n" +
                " napforduló környékén a Nap csak 8 órán keresztül tartózkodik a horizont\n" +
                " felett, így ez a leghosszabb éjszaka. Ezen a napon kezdődik a nappali fény\n" +
                " fokozatos növekedése, így a következő hónapokban egyre hosszabbak lesznek\n" +
                " a nappalok."));
        feastDays.add(new FeastDay(2029, 11, 24, "Szenteste", "Szenteste a karácsonyi ünnepkör egyik legfontosabb napja, amely Jézus\n" +
                " Krisztus születésének előestéje. Bár Jézus pontos születési dátuma\n" +
                " nem ismert, a keresztény egyház a 4. században december 25.-ét jelölte meg\n" +
                " születésének napjaként, összekapcsolva azt a téli napfordulóval.\n" +
                " December 24.-én este a családok összegyűlnek, közösen vacsoráznak,\n" +
                " ajándékokat adnak egymásnak és karácsonyfát állítanak. Magyarországon\n" +
                " a hagyomány szerint a Jézuska hozza az ajándékokat, ezzel is megemlékezve\n" +
                " Jézus születésének csodájáról. A fenyőfa, amely a 16. századtól vált\n" +
                " a karácsony szimbólumává, az örök életet jelképezi. Az ajándékozás a\n" +
                " szeretet és a megbecsülés kifejezése, amely a három királyok\n" +
                " ajándékozására utal. Karácsony, bár elsősorban keresztény ünnep,\n" +
                " világszerte a szeretet, a béke és a család ünnepe lett."));
        feastDays.add(new FeastDay(2029, 11, 25, "Karácsony", "A keresztények számára Jézus Krisztus születésének ünnepe, melyet a 4.\n" +
                " század óta tartanak. Az ünnep a téli napfordulóhoz kapcsolódik,\n" +
                " szimbolizálva a fény győzelmét a sötétség felett. Karácsony a remény,\n" +
                " a szeretet és a megváltás ünnepe, amely a családi együttlét, az öröm\n" +
                " és a vallási szertartások ideje. Karácsonyfa, betlehem és csillag a\n" +
                " szimbólumok, amelyek Jézus születését, az örök életet és az\n" +
                " útmutatást képviselik. Az ünneplés világszerte eltérő, de mindegyik\n" +
                " kultúrában a szeretet és a béke fontosságát hangsúlyozza. Karácsony\n" +
                " lehetőséget ad az emberi kapcsolatok és az élet értelmének\n" +
                " átgondolására"));
        feastDays.add(new FeastDay(2029, 11, 26, "Karácsony másnapja", "A keresztény hagyományban Szent István, az első vértanú emléknapja,\n" +
                " azonban Magyarországon elsősorban Szent István királyra, az\n" +
                " államalapítóra emlékeznek. A 4. századi eredetű ünnep során Jézus\n" +
                " születése után következik Szent István napja, aki kulcsszerepet játszott\n" +
                " a kereszténység elterjesztésében Magyarországon. Bár Szent István a 11.\n" +
                " században élt, az egyház később tette december 26.-át emlékévé.\n" +
                " Magyarországon nemzeti hősként is tisztelik, mivel ő alapította az\n" +
                " államot. Ezen a napon az emberek szentmisére mennek, megemlékeznek\n" +
                " királyukról és különböző ünnepi rendezvényeken is részt vesznek"));
        feastDays.add(new FeastDay(2029, 11, 31, "Szilveszter", "A Szilveszter az év utolsó napja, amikor búcsút veszünk az óévtől és\n" +
                " ünnepeljük az új év kezdetét. Az ünnep neve Szent Szilveszter pápától\n" +
                " származik, aki 335 körül halt meg, de az újév ünneplésének hagyományai\n" +
                " sokkal régebbre nyúlnak vissza. Szilveszter éjszakáján családok és\n" +
                " barátok együtt ünnepelnek, vacsoráznak, tűzijátékot néznek és\n" +
                " jókívánságokat mondanak. A tűzijáték és a zajkeltés az ősi\n" +
                " hagyományokból ered. Az ünnep a megújulás, az összetartozás és a\n" +
                " remény kifejezője, miközben gazdaságilag is fontos szerepet játszik a\n" +
                " fogyasztói társadalomban.\n" +
                " \n" +
                " Boldog Új Évet kívánok! Legyen tele örömmel, egészséggel és sikerrel.\n" +
                " Hozzon az új év boldog pillanatokat, új lehetőségeket és boldog együtt\n" +
                " töltött időt!"));

        feastDays.add(new FeastDay(2030, 0, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma.\n" +
                " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az\n" +
                " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy\n" +
                " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és\n" +
                " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a\n" +
                " mondás. Legyen ez a nap a változás első lépése. Boldogságot,\n" +
                " egészséget és kitartást kívánok az új év minden pillanatára!\n"
        ));
        feastDays.add(new FeastDay(2030, 0, 6, "Vízkereszt", " A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a\n" +
                " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus\n" +
                " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus\n" +
                " megkeresztelkedésére és az első csodájára. A három királyok aranyat,\n" +
                " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a\n" +
                " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet\n" +
                " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi\n" +
                " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak\n" +
                " házszenteléseket, Isten áldását kérve az otthonra és az új évre."));
        feastDays.add(new FeastDay(2030, 1, 14, "Valentin nap", "A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint\n" +
                " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században\n" +
                " titokban esketett meg szerelmespárokat, mert II. Claudius császár\n" +
                " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák.\n" +
                " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az\n" +
                " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a\n" +
                " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal\n" +
                " és üdvözlőkártyákkal ünneplik."));
        feastDays.add(new FeastDay(2030, 2, 1, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja,\n" +
                " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a\n" +
                " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A\n" +
                " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és\n" +
                " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki\n" +
                " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának\n" +
                " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel,\n" +
                " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére\n" +
                " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A\n" +
                " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap\n" +
                " előtt tartanak. Általában február végére vagy március elejére esik."));
        feastDays.add(new FeastDay(2030, 2, 8, "Nőnap", " Az első Nőnapot 1908 március 8.-án tartották. A női munkások\n" +
                " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az\n" +
                " ünnep célja a nők társadalmi, politikai és gazdasági jogainak\n" +
                " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben\n" +
                " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így\n" +
                " 1911-ben már több európai országban is megünnepelték. Magyarországon\n" +
                " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már\n" +
                " a nők jogainak elismerésére és tiszteletére emelkedett."));
        feastDays.add(new FeastDay(2030, 2, 15, "1848-as Forradalom", "Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a\n" +
                " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti\n" +
                " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a\n" +
                " sajtószabadságért, a jobb életkörülményekért és a függetlenségért.\n" +
                " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című\n" +
                " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet\n" +
                " függetlenségi törekvése a magyar történelem egyik legfontosabb\n" +
                " eseményévé vált. Az események hatására megerősödött a nemzeti\n" +
                " identitás és ma már a magyar szabadság és összetartozás napjaként\n" +
                " ünnepeljük.\n" +
                " "));
        feastDays.add(new FeastDay(2030, 2, 26, "Óraátállítás", " A tavaszi óraátállítás a nyári időszámítás kezdete. \n" +
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
                " óraátállítást Magyarországon."));
        feastDays.add(new FeastDay(2030, 3, 1, "Bolondok napja", "Április elseje a bolondok napja, a bolondos tréfák és ártalmatlan\n" +
                " átverések napja. Az emberek apró csínyekkel, például hamis hírekkel vagy\n" +
                " kitalált történetekkel szórakoztatják egymást. Az internet\n" +
                " elterjedésével az álhírek és vírusos videók is népszerűvé váltak. A\n" +
                " nap célja, hogy jókedvet és nevetést hozzon, miközben erősíti a\n" +
                " közösségi szellemet. Eredete több elméletre vezethető vissza. A\n" +
                " legnépszerűbb magyarázat szerint a XVI. században, a Gergely-naptár\n" +
                " bevezetésekor azok, akik áprilisban ünnepelték az újévet,\n" +
                " Franciaországban bolondnak számítottak, így tréfák céltáblájává\n" +
                " váltak. Hasonló szokások figyelhetők meg a kelta tavaszi ünnepekben és az\n" +
                " ókori római Hilaria ünnepén is, ahol a bolondos viselkedés a tavaszi\n" +
                " megújulást jelezte. "));
        feastDays.add(new FeastDay(2030, 3, 9, "Virágvasárnap", " A Virágvasárnap a húsvét előtti vasárnap, Jézus jeruzsálemi\n" +
                " bevonulásának emlékére van. Az evangéliumok szerint Jézus szamárháton\n" +
                " vonult be a városba, miközben a hívek pálmaágakat lengettek, üdvözölve\n" +
                " őt, mint a Megváltót. A virágok, a pálmák és a zöld ágak használata\n" +
                " az ünnep jelképei, melyek a békét és az üdvözítőt hirdetik. A\n" +
                " keresztény hagyományban Virágvasárnap a nagyböjt végét és a húsvéti\n" +
                " ünnepekre való lelki felkészülést jelzi. Az ünnep hivatalosan a 4.\n" +
                " század óta van jelen az egyházi naptárban és azóta része a keresztény\n" +
                " hagyományoknak világszerte."));
        feastDays.add(new FeastDay(2030, 3, 14, "Nagypéntek", "A Nagypéntek a keresztény húsvéti ünnepkör legfontosabb napja, amikor\n" +
                " Jézus Krisztus kereszthalálát és szenvedését emlékezzük meg. Az ünnep\n" +
                " célja, hogy a hívők átgondolják a megváltásra tett áldozat\n" +
                " jelentőségét. E napot a 4. század óta ünneplik, amikor a keresztény\n" +
                " egyház hivatalosan is elismerte Jézus kereszthalálának fontosságát.\n" +
                " Magyarországon a 20. század elejétől munkaszüneti nap. A keresztény\n" +
                " hagyományokban sötét napként van jelen, amikor sokan böjtölnek és\n" +
                " csendes megemlékezéseket tartanak. Az egyházi szertartások mellett a\n" +
                " keresztény közösség számára Nagypéntek a bűnbánat és a lelki\n" +
                " megújulás napja is.  Nagypénteket a húsvét előtti pénteken ünneplik,\n" +
                " tehát ez a dátum évente változik. \n" +
                " "));
        feastDays.add(new FeastDay(2030, 3, 16, "Húsvétvasárnap", "Húsvétvasárnap a kereszténység legfontosabb ünnepe, Jézus Krisztus\n" +
                " feltámadásának napja, amely a halál feletti győzelmet és az örök élet\n" +
                " ígéretét hirdeti. Magyarországon 1991 óta munkaszüneti nap, de\n" +
                " jelentősége évszázadok óta meghatározó. Húsvét a 40 napos nagyböjt\n" +
                " végét jelzi, amely a bűnbánat és a lelki megújulás időszaka. A gyerekek\n" +
                " az ajándékaikat hagyományosan ezen a napon kapják meg, édességeket és\n" +
                " tojásokat, amelyeket a húsvéti nyúl hoz.  A húsvéti nyúl a 16. századi\n" +
                " Németországból ered és a termékenység, valamint az újjászületés\n" +
                " jelképe. Eredete pogány hagyományokhoz köthető, ahol a nyúl és a tojás\n" +
                " a tavaszi újjászületés szimbóluma volt, melyek később beépültek a\n" +
                " keresztény húsvéti szokásokba.\n" +
                " A Húsvétot évente változó dátumon ünneplik, mivel a tavaszi\n" +
                " napéjegyenlőség utáni első teliholdat követő vasárnapra esik."));
        feastDays.add(new FeastDay(2030, 3, 17, "Húsvéthétfő", "Húsvéthétfő a húsvéti ünnepkör egyik legfontosabb napja, amely Jézus\n" +
                " Krisztus feltámadását követően az új élet és a megújulás\n" +
                " szimbólumává vált. Magyarországon a húsvéti locsolás hagyománya a 17.\n" +
                " századtól terjedt el. A férfiak vízzel vagy illatos kölnivel locsolják\n" +
                " meg a nőket, kívánva nekik egészséget, boldogságot és termékenységet.\n" +
                " A locsolás eredete valószínűleg a megtisztulás és a tavaszi megújulás\n" +
                " ősi hiedelmeiből származik. A húsvéti nyúl a tavasz, az új élet és a\n" +
                " termékenység jelképe, míg a bárány Krisztus áldozatát, ártatlanságát\n" +
                " és a megváltást szimbolizálja. Magyarországon a húsvétot a\n" +
                " kereszténység elterjedése óta ünneplik és a népszokások a 19.\n" +
                " századtól erősödtek meg."));
        feastDays.add(new FeastDay(2030, 3, 22, "A Föld napja", "A Föld napja 1970-ben indult el, amikor az Egyesült Államokban Gaylord\n" +
                " Nelson szenátor kezdeményezésére több millió ember gyűlt össze, hogy\n" +
                " felhívja a figyelmet a környezetszennyezésre és a természeti erőforrások\n" +
                " védelmére. A nap célja, hogy globálisan tudatosítsa az emberekben a\n" +
                " környezetvédelem fontosságát és ösztönözze a fenntartható életmódra\n" +
                " való áttérést. Azóta minden évben április 22.-én világszerte\n" +
                " megemlékeznek róla. Számos esemény, akció és kampány hívja fel a\n" +
                " figyelmet a bolygó védelmére és a környezeti problémák megoldására.\n" +
                " \n" +
                " \n" +
                " "));
        feastDays.add(new FeastDay(2030, 3, 30, "Májusfa állítás", "A májusfa felállításának legjellemzőbb hagyomány szerinti időpontja a\n" +
                " május 1-jét megelőző nap délután/este volt. \n" +
                " Magyarországon a középkor óta ismert, tehát legalább a 14. századtól\n" +
                " ünneplik. A szokás az ősi tavaszi rituálékra épül, melyek a természet\n" +
                " megújulását és az élet körforgását szimbolizálták. A fiatal legények\n" +
                " a lányok háza elé állították a feldíszített fákat, hogy kifejezzék\n" +
                " szerelmi szándékukat. A májusfát szalagokkal, virágokkal díszítették\n" +
                " és május végén vagy pünkösdkor táncos mulatság keretében bontották\n" +
                " le, amit kitáncolásnak neveztek. A hagyomány a szerelem és a termékenység\n" +
                " ünnepe volt, később pedig összefonódott a munka ünnepével (május 1.).\n" +
                " Mára már kevésbé elterjedt szokás, de sok helyen igyekeznek még\n" +
                " fenntartani."));
        feastDays.add(new FeastDay(2030, 4, 1, "A Munka ünnepe", "Május 1.-e a munka ünnepe. A munkások jogaiért és szolidaritásáért\n" +
                " folytatott küzdelem emléknapja világszerte. Az ünnep gyökerei az 1886-os\n" +
                " chicagói tüntetésekhez nyúlnak vissza, ahol a munkások a nyolcórás\n" +
                " munkaidő bevezetéséért demonstráltak. Az események hatására 1890-től\n" +
                " május 1.-ét a munkások nemzetközi szolidaritási napjává\n" +
                " nyilvánították.  Magyarországon először 1890-ben tartották meg, de\n" +
                " hivatalos állami ünnep csak 1946-ban lett. A szocializmus idején\n" +
                " nagyszabású felvonulásokkal és tömegrendezvényekkel ünnepelték. A\n" +
                " rendszerváltás után a jelentése átalakult, de továbbra is a munka\n" +
                " tiszteletét és a munkavállalók jogainak fontosságát hirdeti. "));
        feastDays.add(new FeastDay(2030, 4, 7, "Anyák napja", "Az édesanyák napja a szeretet, a tisztelet és a hála ünnepe, amelyet\n" +
                " világszerte az édesanyák tiszteletére tartanak. Gyökerei az ókori\n" +
                " Görögországba nyúlnak vissza, ahol Rheát, az istenek anyját ünnepelték.\n" +
                " A modern anyák napja az Egyesült Államokból indult 1908-ban, amikor Anna\n" +
                " Jarvis kampányt indított édesanyja emlékére. Magyarországon először\n" +
                " 1925-ben ünnepelték és a májusi Mária-tisztelet hagyományaival is\n" +
                " összekapcsolódott. Ez a nap a gondoskodó és szerető édesanyák, valamint\n" +
                " minden női gondviselő iránti megbecsülés kifejezésére szolgál.\n" +
                " Virágokkal, ajándékokkal és figyelmességgel fejezik ki hálájukat a\n" +
                " gyerekek. Bár világszerte eltérő időpontokban ünneplik, mindenhol az\n" +
                " anyák iránti szeretet és tisztelet áll a középpontban. Anyák napját\n" +
                " Magyarországon minden év május első vasárnapján ünneplik. \n" +
                " "));
        feastDays.add(new FeastDay(2030, 4, 28, "Gyermeknap", "A gyermeknap a gyerekek jogainak és jólétének fontosságát hirdető\n" +
                " ünnep, melyet világszerte különböző időpontokban tartanak. Eredete\n" +
                " 1925-re nyúlik vissza, amikor a genfi Gyermekjóléti Konferencián\n" +
                " elhatározták, hogy kiemelik a gyermekek védelmének fontosságát.\n" +
                " Magyarországon 1931-ben tartották meg először, majd 1950 óta május\n" +
                " utolsó vasárnapján ünneplik. Ez a nap a gyerekekről szól, akik ilyenkor\n" +
                " különféle programokon vehetnek részt, ahol játékokkal és szórakoztató\n" +
                " rendezvényekkel ünneplik őket. Az ünnep célja, hogy felhívja a figyelmet\n" +
                " a gyermekek jogaira és boldogságuk fontosságára. Magyarországon a\n" +
                " Gyermeknapot minden évben május utolsó vasárnapján ünneplik."));
        feastDays.add(new FeastDay(2030, 5, 4, "Pünkösd", "Pünkösd a kereszténység egyik legősibb ünnepe, melyet már a 4.\n" +
                " századtól kezdve ünnepelnek a keresztény egyházban. Az ünnep Jézus\n" +
                " mennybemenetele után ötven nappal emlékezik meg a Szentlélek\n" +
                " eljöveteléről, amely erőt adott az apostoloknak a keresztény hit\n" +
                " hirdetéséhez. Ez az esemény az egyház születésnapjaként is ismert. A\n" +
                " „pünkösd” elnevezés a görög „pentékoszté” szóból ered, ami\n" +
                " ötvenediket jelent. Magyarországon a középkor óta tartják számon és\n" +
                " számos népi hagyomány kapcsolódik hozzá, például a pünkösdi király\n" +
                " és királyné választása. Pünkösd vasárnapját a húsvét utáni 50.\n" +
                " napon, tehát 50 nappal húsvétvasárnap után ünneplik. "));
        feastDays.add(new FeastDay(2030, 5, 5, "Pünkösdhétfő", "A Pünkösdhétfő a keresztény egyház egyik legfontosabb ünnepének, a\n" +
                " pünkösdnek a második napja. Pünkösd a húsvét utáni 50. napon van, és a\n" +
                " Szentlélek eljövetelét ünnepli, amikor Jézus tanítványai erőt és\n" +
                " útmutatást kaptak küldetésükhöz. Az esemény a keresztény egyház\n" +
                " születésnapjának is tekinthető. Az ünnep gyökerei a zsidó pünkösdi\n" +
                " ünnepre (Sávuót) vezethetők vissza, amely az aratási hálaadás napja\n" +
                " volt. Magyarországon pünkösdhétfő munkaszüneti nap és hivatalosan 1993\n" +
                " óta ünnepeljük újra. Az ünnep a megújulást és a közösség\n" +
                " összetartozását jelképezi."));
        feastDays.add(new FeastDay(2030, 5, 18, "Apák napja", "Apák napja világszerte az édesapák tiszteletére szolgál, elismerve a\n" +
                " családban és a gyermeknevelésben betöltött szerepüket. Az ünnep eredete\n" +
                " az Egyesült Államokhoz köthető, ahol először 1910-ben tartották meg\n" +
                " Sonora Smart Dodd kezdeményezésére , aki édesapját akarta tiszteletben\n" +
                " részesíteni . Magyarországon az apák napja az 1990-es években kezdett\n" +
                " elterjedni  és 1994 óta ünnepeljük minden év június harmadik\n" +
                " vasárnapján. Célja, hogy elismerje az apák és az apaszerepet betöltő\n" +
                " férfiak szeretetét, áldozatvállalását és a családban betöltött\n" +
                " szerepét. Ez a nap lehetőséget ad arra, hogy köszönetet mondjunk az apák\n" +
                " szeretetéért, támogatásáért és áldozatos munkájáért, amelyet\n" +
                " családjukért tesznek. "));
        feastDays.add(new FeastDay(2030, 5, 20, "Nyári napforduló", "Az év leghosszabb napja. \n" +
                " Napkelte: \n" +
                " Napnyugta: \n" +
                " Világos időtartam: \n" +
                " A nyári napforduló a leghosszabb nap az évben, amikor a Nap a legmagasabbra\n" +
                " emelkedik az égbolton. Ez a jelenség a Föld tengelyferdesége és a\n" +
                " keringése miatt következik be. Az északi féltekén a napforduló\n" +
                " környékén a Nap több mint 15 órán keresztül a horizont felett\n" +
                " tartózkodik. Ezen a napon a Föld az északi féltekén éri el a leghosszabb\n" +
                " nappali órákat, így ez a legrövidebb éjszaka."));
        feastDays.add(new FeastDay(2030, 7, 20, "Szent István-nap", "Szent István király koronázása körülbelül 1000-ben történt, ezzel\n" +
                " megalapozva a keresztény Magyarországot. 1083 augusztus 20.-án szentté\n" +
                " avatták a Szent Péter-bazilikában. Az Árpád-korban az István-kultusz az\n" +
                " uralkodó dinasztiát támogatta és egységet teremtett a népcsoportok\n" +
                " között. 1686-ban XI. Ince pápa elrendelte, hogy az egész katolikus világ\n" +
                " emlékezzen meg róla, majd 1771-ben Mária Terézia országos ünneppé tette.\n" +
                " Az 1848–49-es forradalom és szabadságharc leverése után egy időre\n" +
                " szünetelt az ünneplés, de 1891-ben Ferenc József munkaszüneti nappá\n" +
                " nyilvánította. A két világháború között a nemzeti egység\n" +
                " szimbólumává vált. 1950-től az új államrendszer ideológiája szerint\n" +
                " alakították át az ünnep jelentését, majd 1991-ben ismét állami\n" +
                " ünneppé nyilvánították."));
        feastDays.add(new FeastDay(2030, 9, 23, "1956-os Forradalom", "Magyarország nemzeti ünnepe, amely az 1956-os forradalom és szabadságharc\n" +
                " eseményeire emlékezik. Az ünnep hivatalosan 1989 október 23.-án vált\n" +
                " nemzeti ünneppé a rendszerváltás után, amikor a forradalom hőseit\n" +
                " ünnepelve tisztelegtek a szabadságért és a demokráciáért folytatott\n" +
                " küzdelmük előtt. Az 1956-os forradalom a szovjet megszállás elleni\n" +
                " népfelkelés volt, amely bár véresen végződött, alapvető szerepet\n" +
                " játszott Magyarország függetlenségi törekvéseiben és a későbbi\n" +
                " rendszerváltásban. Az október 23.-i események máig a szabadság és a\n" +
                " nemzeti szuverenitás fontos szimbólumai."));
        feastDays.add(new FeastDay(2030, 9, 29, "Óraátállítás", "Az őszi óraátállítás (a téli időszámítás kezdete) – (vasárnap),\n" +
                " 3:00 → 2:00\n" +
                " Ekkor kell 1 órát visszaállítani az órákat.\n" +
                " Az őszi óraátállítás célja, hogy reggel világosabb legyen, így a\n" +
                " természetes fényt jobban kihasználjuk. Magyarországon először 1916-ban\n" +
                " vezették be az óraátállítást energiatakarékossági okokból. Bár több\n" +
                " alkalommal szünetelt, végül 1980-ban újra bevezették, hogy csökkentsék a\n" +
                " villamosenergia-felhasználást. A téli időszámítás október utolsó\n" +
                " vasárnapján kezdődik, amikor az órákat visszaállítjuk. Ezáltal reggel\n" +
                " hamarabb világosodik, de este hamarabb sötétedik.  1996 óta\n" +
                " kormányrendelet szabályozza az óraátállítást.\n" +
                " "));
        feastDays.add(new FeastDay(2030, 10, 1, "Mindenszentek", "Mindenszentek ünnepe november 1.-én van, amikor a katolikus egyház\n" +
                " megemlékezik minden üdvözült lélekről, akik elnyerték a mennyországot,\n" +
                " de nevüket nem tartják számon a naptárban. Az ünnep eredete a\n" +
                " kereszténység előtti halotti kultuszokra is visszavezethető. Hivatalosan a\n" +
                " 8. században III. Gergely pápa idején vezették be, aki a Szent\n" +
                " Péter-bazilikában kápolnát szentelt minden szent tiszteletére.\n" +
                " Magyarországon munkaszüneti nap. Sokan virágokkal és gyertyagyújtással\n" +
                " tisztelegnek elhunyt szeretteik emléke előtt. Ez a nap a hit és a\n" +
                " megemlékezés ünnepe."));
        feastDays.add(new FeastDay(2030, 10, 2, "Halottak napja", "Halottak napja november 2.-án van, amikor a katolikus egyház megemlékezik az\n" +
                " elhunyt hívekről. Az ünnep célja, hogy imádságokkal és jó\n" +
                " cselekedetekkel segítsük a halottak lelki üdvét és tisztelegjünk\n" +
                " szeretteink emléke előtt. Bár gyökerei a kereszténység előtti halotti\n" +
                " kultuszokig nyúlnak vissza, mai formáját a 11. században Cluny-i Szent\n" +
                " Odiló apát vezette be. Ő rendelte el, hogy a kolostorok imádkozzanak a\n" +
                " tisztítótűzben szenvedő lelkekért. A gyertyagyújtás és a virágok\n" +
                " elhelyezése máig az emlékezés fontos része. "));
        feastDays.add(new FeastDay(2030, 11, 3, "Advent első napja", "Advent a karácsony előtti négyhetes időszak, mely Jézus Krisztus\n" +
                " születésére és második eljövetelére való várakozást jelenti. Az\n" +
                " advent származása a 4. századra nyúlik vissza, amikor bűnbánattal és\n" +
                " lelki felkészüléssel várták a karácsonyt. Kezdetben hosszabb böjti\n" +
                " időszak volt, de fokozatosan négyhetesre csökkent. Az adventi koszorú\n" +
                " hagyománya a 19. században terjedt el, az örökzöld növények a\n" +
                " halhatatlanságot és az újjászületést szimbolizálták. A négy gyertya a\n" +
                " hitet, a reményt, az örömöt és a szeretetet jelképezi. Minden vasárnap\n" +
                " egy újabb gyertya meggyújtásával közelebb kerülünk a karácsonyhoz. \n" +
                " Advent első vasárnapját a karácsonyt megelőző négy vasárnap egyikeként\n" +
                " ünneplik. Ez a nap általában november végére vagy december elejére esik. \n" +
                " "));
        feastDays.add(new FeastDay(2030, 11, 6, "Mikulás", " Mikuláskor december 5.-én éjszaka, vagy 6.-án reggel töltik meg a gyerekek\n" +
                " csizmáit édességgel és játékokkal. Szent Miklós püspök emlék napja. A\n" +
                " 4. században élt szent, aki a gyermekek, a hajósok és a szegények\n" +
                " védőszentje lett, híres volt jótékonyságáról. Egy legenda szerint\n" +
                " titokban segített egy szegény ember három lányán, akiket így férjhez\n" +
                " tudott adni.\n" +
                " Ebből a történetből született a hagyomány, hogy Szent Miklós éjjelente\n" +
                " ajándékokat hoz a jó gyerekeknek. Ma már világszerte ismert a piros\n" +
                " ruhás, fehér szakállú Mikulás.  A Mikulás-hagyományok országonként\n" +
                " eltérőek. Egyes helyeken a Mikulás hozza az ajándékokat, máshol a\n" +
                " Télapó. A lényeg azonban mindenhol ugyanaz: a szeretet az adakozás és a\n" +
                " gyermeki öröm ünnepe"));
        feastDays.add(new FeastDay(2030, 11, 13, "Luca napja", "Luca napja a keresztény és a népi hagyományok keveréke. Szent Luca a 3.\n" +
                " századi vértanú, a látás védőszentje, de a népi hitvilágban a\n" +
                " termékenység, a jólét és a védelem szimbóluma. A hagyomány szerint ezen\n" +
                " a napon kezdődött a tél leghosszabb éjszakája, így Luca napja a fény és\n" +
                " a sötétség határán áll. Ezen a napon a nők nem dolgoztak, hogy bőséges\n" +
                " legyen a következő év termése. Szokás volt Luca-széket készíteni,\n" +
                " búzát ültetni és jóslásokat végezni. Luca napja a megújulás és a\n" +
                " remény ünnepe, a fény győzelme a sötétség felett és a következő év\n" +
                " jólétének biztosítása.\n" +
                " "));
        feastDays.add(new FeastDay(2030, 11, 21, "Téli napforduló", "Az év legrövidebb napja nap. \n" +
                " Napkelte: \n" +
                " Napnyugta: \n" +
                " Világos időtartam: \n" +
                " A téli napforduló a legrövidebb nap az évben, amikor a Nap a\n" +
                " legalacsonyabbra emelkedik az égbolton. Ez a jelenség a Föld\n" +
                " tengelyferdesége és keringése miatt következik be. Az északi féltekén a\n" +
                " napforduló környékén a Nap csak 8 órán keresztül tartózkodik a horizont\n" +
                " felett, így ez a leghosszabb éjszaka. Ezen a napon kezdődik a nappali fény\n" +
                " fokozatos növekedése, így a következő hónapokban egyre hosszabbak lesznek\n" +
                " a nappalok."));
        feastDays.add(new FeastDay(2030, 11, 24, "Szenteste", "Szenteste a karácsonyi ünnepkör egyik legfontosabb napja, amely Jézus\n" +
                " Krisztus születésének előestéje. Bár Jézus pontos születési dátuma\n" +
                " nem ismert, a keresztény egyház a 4. században december 25.-ét jelölte meg\n" +
                " születésének napjaként, összekapcsolva azt a téli napfordulóval.\n" +
                " December 24.-én este a családok összegyűlnek, közösen vacsoráznak,\n" +
                " ajándékokat adnak egymásnak és karácsonyfát állítanak. Magyarországon\n" +
                " a hagyomány szerint a Jézuska hozza az ajándékokat, ezzel is megemlékezve\n" +
                " Jézus születésének csodájáról. A fenyőfa, amely a 16. századtól vált\n" +
                " a karácsony szimbólumává, az örök életet jelképezi. Az ajándékozás a\n" +
                " szeretet és a megbecsülés kifejezése, amely a három királyok\n" +
                " ajándékozására utal. Karácsony, bár elsősorban keresztény ünnep,\n" +
                " világszerte a szeretet, a béke és a család ünnepe lett."));
        feastDays.add(new FeastDay(2030, 11, 25, "Karácsony", "A keresztények számára Jézus Krisztus születésének ünnepe, melyet a 4.\n" +
                " század óta tartanak. Az ünnep a téli napfordulóhoz kapcsolódik,\n" +
                " szimbolizálva a fény győzelmét a sötétség felett. Karácsony a remény,\n" +
                " a szeretet és a megváltás ünnepe, amely a családi együttlét, az öröm\n" +
                " és a vallási szertartások ideje. Karácsonyfa, betlehem és csillag a\n" +
                " szimbólumok, amelyek Jézus születését, az örök életet és az\n" +
                " útmutatást képviselik. Az ünneplés világszerte eltérő, de mindegyik\n" +
                " kultúrában a szeretet és a béke fontosságát hangsúlyozza. Karácsony\n" +
                " lehetőséget ad az emberi kapcsolatok és az élet értelmének\n" +
                " átgondolására"));
        feastDays.add(new FeastDay(2030, 11, 26, "Karácsony másnapja", "A keresztény hagyományban Szent István, az első vértanú emléknapja,\n" +
                " azonban Magyarországon elsősorban Szent István királyra, az\n" +
                " államalapítóra emlékeznek. A 4. századi eredetű ünnep során Jézus\n" +
                " születése után következik Szent István napja, aki kulcsszerepet játszott\n" +
                " a kereszténység elterjesztésében Magyarországon. Bár Szent István a 11.\n" +
                " században élt, az egyház később tette december 26.-át emlékévé.\n" +
                " Magyarországon nemzeti hősként is tisztelik, mivel ő alapította az\n" +
                " államot. Ezen a napon az emberek szentmisére mennek, megemlékeznek\n" +
                " királyukról és különböző ünnepi rendezvényeken is részt vesznek"));
        feastDays.add(new FeastDay(2030, 11, 31, "Szilveszter", "A Szilveszter az év utolsó napja, amikor búcsút veszünk az óévtől és\n" +
                " ünnepeljük az új év kezdetét. Az ünnep neve Szent Szilveszter pápától\n" +
                " származik, aki 335 körül halt meg, de az újév ünneplésének hagyományai\n" +
                " sokkal régebbre nyúlnak vissza. Szilveszter éjszakáján családok és\n" +
                " barátok együtt ünnepelnek, vacsoráznak, tűzijátékot néznek és\n" +
                " jókívánságokat mondanak. A tűzijáték és a zajkeltés az ősi\n" +
                " hagyományokból ered. Az ünnep a megújulás, az összetartozás és a\n" +
                " remény kifejezője, miközben gazdaságilag is fontos szerepet játszik a\n" +
                " fogyasztói társadalomban.\n" +
                " \n" +
                " Boldog Új Évet kívánok! Legyen tele örömmel, egészséggel és sikerrel.\n" +
                " Hozzon az új év boldog pillanatokat, új lehetőségeket és boldog együtt\n" +
                " töltött időt!"));
        feastDays.add(new FeastDay(2031, 0, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma.\n" +
                " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az\n" +
                " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy\n" +
                " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és\n" +
                " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a\n" +
                " mondás. Legyen ez a nap a változás első lépése. Boldogságot,\n" +
                " egészséget és kitartást kívánok az új év minden pillanatára!\n"
        ));
        feastDays.add(new FeastDay(2031, 0, 6, "Vízkereszt", " A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a\n" +
                " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus\n" +
                " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus\n" +
                " megkeresztelkedésére és az első csodájára. A három királyok aranyat,\n" +
                " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a\n" +
                " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet\n" +
                " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi\n" +
                " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak\n" +
                " házszenteléseket, Isten áldását kérve az otthonra és az új évre."));
        feastDays.add(new FeastDay(2031, 1, 14, "Valentin nap", "A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint\n" +
                " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században\n" +
                " titokban esketett meg szerelmespárokat, mert II. Claudius császár\n" +
                " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák.\n" +
                " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az\n" +
                " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a\n" +
                " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal\n" +
                " és üdvözlőkártyákkal ünneplik."));
        feastDays.add(new FeastDay(2031, 2, 1, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja,\n" +
                " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a\n" +
                " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A\n" +
                " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és\n" +
                " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki\n" +
                " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának\n" +
                " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel,\n" +
                " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére\n" +
                " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A\n" +
                " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap\n" +
                " előtt tartanak. Általában február végére vagy március elejére esik."));
        feastDays.add(new FeastDay(2031, 2, 8, "Nőnap", " Az első Nőnapot 1908 március 8.-án tartották. A női munkások\n" +
                " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az\n" +
                " ünnep célja a nők társadalmi, politikai és gazdasági jogainak\n" +
                " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben\n" +
                " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így\n" +
                " 1911-ben már több európai országban is megünnepelték. Magyarországon\n" +
                " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már\n" +
                " a nők jogainak elismerésére és tiszteletére emelkedett."));
        feastDays.add(new FeastDay(2031, 2, 15, "1848-as Forradalom", "Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a\n" +
                " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti\n" +
                " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a\n" +
                " sajtószabadságért, a jobb életkörülményekért és a függetlenségért.\n" +
                " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című\n" +
                " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet\n" +
                " függetlenségi törekvése a magyar történelem egyik legfontosabb\n" +
                " eseményévé vált. Az események hatására megerősödött a nemzeti\n" +
                " identitás és ma már a magyar szabadság és összetartozás napjaként\n" +
                " ünnepeljük.\n" +
                " "));
        feastDays.add(new FeastDay(2031, 2, 26, "Óraátállítás", " A tavaszi óraátállítás a nyári időszámítás kezdete. \n" +
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
                " óraátállítást Magyarországon."));
        feastDays.add(new FeastDay(2031, 3, 1, "Bolondok napja", "Április elseje a bolondok napja, a bolondos tréfák és ártalmatlan\n" +
                " átverések napja. Az emberek apró csínyekkel, például hamis hírekkel vagy\n" +
                " kitalált történetekkel szórakoztatják egymást. Az internet\n" +
                " elterjedésével az álhírek és vírusos videók is népszerűvé váltak. A\n" +
                " nap célja, hogy jókedvet és nevetést hozzon, miközben erősíti a\n" +
                " közösségi szellemet. Eredete több elméletre vezethető vissza. A\n" +
                " legnépszerűbb magyarázat szerint a XVI. században, a Gergely-naptár\n" +
                " bevezetésekor azok, akik áprilisban ünnepelték az újévet,\n" +
                " Franciaországban bolondnak számítottak, így tréfák céltáblájává\n" +
                " váltak. Hasonló szokások figyelhetők meg a kelta tavaszi ünnepekben és az\n" +
                " ókori római Hilaria ünnepén is, ahol a bolondos viselkedés a tavaszi\n" +
                " megújulást jelezte. "));
        feastDays.add(new FeastDay(2031, 3, 9, "Virágvasárnap", " A Virágvasárnap a húsvét előtti vasárnap, Jézus jeruzsálemi\n" +
                " bevonulásának emlékére van. Az evangéliumok szerint Jézus szamárháton\n" +
                " vonult be a városba, miközben a hívek pálmaágakat lengettek, üdvözölve\n" +
                " őt, mint a Megváltót. A virágok, a pálmák és a zöld ágak használata\n" +
                " az ünnep jelképei, melyek a békét és az üdvözítőt hirdetik. A\n" +
                " keresztény hagyományban Virágvasárnap a nagyböjt végét és a húsvéti\n" +
                " ünnepekre való lelki felkészülést jelzi. Az ünnep hivatalosan a 4.\n" +
                " század óta van jelen az egyházi naptárban és azóta része a keresztény\n" +
                " hagyományoknak világszerte."));
        feastDays.add(new FeastDay(2031, 3, 14, "Nagypéntek", "A Nagypéntek a keresztény húsvéti ünnepkör legfontosabb napja, amikor\n" +
                " Jézus Krisztus kereszthalálát és szenvedését emlékezzük meg. Az ünnep\n" +
                " célja, hogy a hívők átgondolják a megváltásra tett áldozat\n" +
                " jelentőségét. E napot a 4. század óta ünneplik, amikor a keresztény\n" +
                " egyház hivatalosan is elismerte Jézus kereszthalálának fontosságát.\n" +
                " Magyarországon a 20. század elejétől munkaszüneti nap. A keresztény\n" +
                " hagyományokban sötét napként van jelen, amikor sokan böjtölnek és\n" +
                " csendes megemlékezéseket tartanak. Az egyházi szertartások mellett a\n" +
                " keresztény közösség számára Nagypéntek a bűnbánat és a lelki\n" +
                " megújulás napja is.  Nagypénteket a húsvét előtti pénteken ünneplik,\n" +
                " tehát ez a dátum évente változik. \n" +
                " "));
        feastDays.add(new FeastDay(2031, 3, 16, "Húsvétvasárnap", "Húsvétvasárnap a kereszténység legfontosabb ünnepe, Jézus Krisztus\n" +
                " feltámadásának napja, amely a halál feletti győzelmet és az örök élet\n" +
                " ígéretét hirdeti. Magyarországon 1991 óta munkaszüneti nap, de\n" +
                " jelentősége évszázadok óta meghatározó. Húsvét a 40 napos nagyböjt\n" +
                " végét jelzi, amely a bűnbánat és a lelki megújulás időszaka. A gyerekek\n" +
                " az ajándékaikat hagyományosan ezen a napon kapják meg, édességeket és\n" +
                " tojásokat, amelyeket a húsvéti nyúl hoz.  A húsvéti nyúl a 16. századi\n" +
                " Németországból ered és a termékenység, valamint az újjászületés\n" +
                " jelképe. Eredete pogány hagyományokhoz köthető, ahol a nyúl és a tojás\n" +
                " a tavaszi újjászületés szimbóluma volt, melyek később beépültek a\n" +
                " keresztény húsvéti szokásokba.\n" +
                " A Húsvétot évente változó dátumon ünneplik, mivel a tavaszi\n" +
                " napéjegyenlőség utáni első teliholdat követő vasárnapra esik."));
        feastDays.add(new FeastDay(2031, 3, 17, "Húsvéthétfő", "Húsvéthétfő a húsvéti ünnepkör egyik legfontosabb napja, amely Jézus\n" +
                " Krisztus feltámadását követően az új élet és a megújulás\n" +
                " szimbólumává vált. Magyarországon a húsvéti locsolás hagyománya a 17.\n" +
                " századtól terjedt el. A férfiak vízzel vagy illatos kölnivel locsolják\n" +
                " meg a nőket, kívánva nekik egészséget, boldogságot és termékenységet.\n" +
                " A locsolás eredete valószínűleg a megtisztulás és a tavaszi megújulás\n" +
                " ősi hiedelmeiből származik. A húsvéti nyúl a tavasz, az új élet és a\n" +
                " termékenység jelképe, míg a bárány Krisztus áldozatát, ártatlanságát\n" +
                " és a megváltást szimbolizálja. Magyarországon a húsvétot a\n" +
                " kereszténység elterjedése óta ünneplik és a népszokások a 19.\n" +
                " századtól erősödtek meg."));
        feastDays.add(new FeastDay(2031, 3, 22, "A Föld napja", "A Föld napja 1970-ben indult el, amikor az Egyesült Államokban Gaylord\n" +
                " Nelson szenátor kezdeményezésére több millió ember gyűlt össze, hogy\n" +
                " felhívja a figyelmet a környezetszennyezésre és a természeti erőforrások\n" +
                " védelmére. A nap célja, hogy globálisan tudatosítsa az emberekben a\n" +
                " környezetvédelem fontosságát és ösztönözze a fenntartható életmódra\n" +
                " való áttérést. Azóta minden évben április 22.-én világszerte\n" +
                " megemlékeznek róla. Számos esemény, akció és kampány hívja fel a\n" +
                " figyelmet a bolygó védelmére és a környezeti problémák megoldására.\n" +
                " \n" +
                " \n" +
                " "));
        feastDays.add(new FeastDay(2031, 3, 30, "Májusfa állítás", "A májusfa felállításának legjellemzőbb hagyomány szerinti időpontja a\n" +
                " május 1-jét megelőző nap délután/este volt. \n" +
                " Magyarországon a középkor óta ismert, tehát legalább a 14. századtól\n" +
                " ünneplik. A szokás az ősi tavaszi rituálékra épül, melyek a természet\n" +
                " megújulását és az élet körforgását szimbolizálták. A fiatal legények\n" +
                " a lányok háza elé állították a feldíszített fákat, hogy kifejezzék\n" +
                " szerelmi szándékukat. A májusfát szalagokkal, virágokkal díszítették\n" +
                " és május végén vagy pünkösdkor táncos mulatság keretében bontották\n" +
                " le, amit kitáncolásnak neveztek. A hagyomány a szerelem és a termékenység\n" +
                " ünnepe volt, később pedig összefonódott a munka ünnepével (május 1.).\n" +
                " Mára már kevésbé elterjedt szokás, de sok helyen igyekeznek még\n" +
                " fenntartani."));
        feastDays.add(new FeastDay(2031, 4, 1, "A Munka ünnepe", "Május 1.-e a munka ünnepe. A munkások jogaiért és szolidaritásáért\n" +
                " folytatott küzdelem emléknapja világszerte. Az ünnep gyökerei az 1886-os\n" +
                " chicagói tüntetésekhez nyúlnak vissza, ahol a munkások a nyolcórás\n" +
                " munkaidő bevezetéséért demonstráltak. Az események hatására 1890-től\n" +
                " május 1.-ét a munkások nemzetközi szolidaritási napjává\n" +
                " nyilvánították.  Magyarországon először 1890-ben tartották meg, de\n" +
                " hivatalos állami ünnep csak 1946-ban lett. A szocializmus idején\n" +
                " nagyszabású felvonulásokkal és tömegrendezvényekkel ünnepelték. A\n" +
                " rendszerváltás után a jelentése átalakult, de továbbra is a munka\n" +
                " tiszteletét és a munkavállalók jogainak fontosságát hirdeti. "));
        feastDays.add(new FeastDay(2031, 4, 7, "Anyák napja", "Az édesanyák napja a szeretet, a tisztelet és a hála ünnepe, amelyet\n" +
                " világszerte az édesanyák tiszteletére tartanak. Gyökerei az ókori\n" +
                " Görögországba nyúlnak vissza, ahol Rheát, az istenek anyját ünnepelték.\n" +
                " A modern anyák napja az Egyesült Államokból indult 1908-ban, amikor Anna\n" +
                " Jarvis kampányt indított édesanyja emlékére. Magyarországon először\n" +
                " 1925-ben ünnepelték és a májusi Mária-tisztelet hagyományaival is\n" +
                " összekapcsolódott. Ez a nap a gondoskodó és szerető édesanyák, valamint\n" +
                " minden női gondviselő iránti megbecsülés kifejezésére szolgál.\n" +
                " Virágokkal, ajándékokkal és figyelmességgel fejezik ki hálájukat a\n" +
                " gyerekek. Bár világszerte eltérő időpontokban ünneplik, mindenhol az\n" +
                " anyák iránti szeretet és tisztelet áll a középpontban. Anyák napját\n" +
                " Magyarországon minden év május első vasárnapján ünneplik. \n" +
                " "));
        feastDays.add(new FeastDay(2031, 4, 28, "Gyermeknap", "A gyermeknap a gyerekek jogainak és jólétének fontosságát hirdető\n" +
                " ünnep, melyet világszerte különböző időpontokban tartanak. Eredete\n" +
                " 1925-re nyúlik vissza, amikor a genfi Gyermekjóléti Konferencián\n" +
                " elhatározták, hogy kiemelik a gyermekek védelmének fontosságát.\n" +
                " Magyarországon 1931-ben tartották meg először, majd 1950 óta május\n" +
                " utolsó vasárnapján ünneplik. Ez a nap a gyerekekről szól, akik ilyenkor\n" +
                " különféle programokon vehetnek részt, ahol játékokkal és szórakoztató\n" +
                " rendezvényekkel ünneplik őket. Az ünnep célja, hogy felhívja a figyelmet\n" +
                " a gyermekek jogaira és boldogságuk fontosságára. Magyarországon a\n" +
                " Gyermeknapot minden évben május utolsó vasárnapján ünneplik."));
        feastDays.add(new FeastDay(2031, 5, 4, "Pünkösd", "Pünkösd a kereszténység egyik legősibb ünnepe, melyet már a 4.\n" +
                " századtól kezdve ünnepelnek a keresztény egyházban. Az ünnep Jézus\n" +
                " mennybemenetele után ötven nappal emlékezik meg a Szentlélek\n" +
                " eljöveteléről, amely erőt adott az apostoloknak a keresztény hit\n" +
                " hirdetéséhez. Ez az esemény az egyház születésnapjaként is ismert. A\n" +
                " „pünkösd” elnevezés a görög „pentékoszté” szóból ered, ami\n" +
                " ötvenediket jelent. Magyarországon a középkor óta tartják számon és\n" +
                " számos népi hagyomány kapcsolódik hozzá, például a pünkösdi király\n" +
                " és királyné választása. Pünkösd vasárnapját a húsvét utáni 50.\n" +
                " napon, tehát 50 nappal húsvétvasárnap után ünneplik. "));
        feastDays.add(new FeastDay(2031, 5, 5, "Pünkösdhétfő", "A Pünkösdhétfő a keresztény egyház egyik legfontosabb ünnepének, a\n" +
                " pünkösdnek a második napja. Pünkösd a húsvét utáni 50. napon van, és a\n" +
                " Szentlélek eljövetelét ünnepli, amikor Jézus tanítványai erőt és\n" +
                " útmutatást kaptak küldetésükhöz. Az esemény a keresztény egyház\n" +
                " születésnapjának is tekinthető. Az ünnep gyökerei a zsidó pünkösdi\n" +
                " ünnepre (Sávuót) vezethetők vissza, amely az aratási hálaadás napja\n" +
                " volt. Magyarországon pünkösdhétfő munkaszüneti nap és hivatalosan 1993\n" +
                " óta ünnepeljük újra. Az ünnep a megújulást és a közösség\n" +
                " összetartozását jelképezi."));
        feastDays.add(new FeastDay(2031, 5, 18, "Apák napja", "Apák napja világszerte az édesapák tiszteletére szolgál, elismerve a\n" +
                " családban és a gyermeknevelésben betöltött szerepüket. Az ünnep eredete\n" +
                " az Egyesült Államokhoz köthető, ahol először 1910-ben tartották meg\n" +
                " Sonora Smart Dodd kezdeményezésére , aki édesapját akarta tiszteletben\n" +
                " részesíteni . Magyarországon az apák napja az 1990-es években kezdett\n" +
                " elterjedni  és 1994 óta ünnepeljük minden év június harmadik\n" +
                " vasárnapján. Célja, hogy elismerje az apák és az apaszerepet betöltő\n" +
                " férfiak szeretetét, áldozatvállalását és a családban betöltött\n" +
                " szerepét. Ez a nap lehetőséget ad arra, hogy köszönetet mondjunk az apák\n" +
                " szeretetéért, támogatásáért és áldozatos munkájáért, amelyet\n" +
                " családjukért tesznek. "));
        feastDays.add(new FeastDay(2031, 5, 20, "Nyári napforduló", "Az év leghosszabb napja. \n" +
                " Napkelte: \n" +
                " Napnyugta: \n" +
                " Világos időtartam: \n" +
                " A nyári napforduló a leghosszabb nap az évben, amikor a Nap a legmagasabbra\n" +
                " emelkedik az égbolton. Ez a jelenség a Föld tengelyferdesége és a\n" +
                " keringése miatt következik be. Az északi féltekén a napforduló\n" +
                " környékén a Nap több mint 15 órán keresztül a horizont felett\n" +
                " tartózkodik. Ezen a napon a Föld az északi féltekén éri el a leghosszabb\n" +
                " nappali órákat, így ez a legrövidebb éjszaka."));
        feastDays.add(new FeastDay(2031, 7, 20, "Szent István-nap", "Szent István király koronázása körülbelül 1000-ben történt, ezzel\n" +
                " megalapozva a keresztény Magyarországot. 1083 augusztus 20.-án szentté\n" +
                " avatták a Szent Péter-bazilikában. Az Árpád-korban az István-kultusz az\n" +
                " uralkodó dinasztiát támogatta és egységet teremtett a népcsoportok\n" +
                " között. 1686-ban XI. Ince pápa elrendelte, hogy az egész katolikus világ\n" +
                " emlékezzen meg róla, majd 1771-ben Mária Terézia országos ünneppé tette.\n" +
                " Az 1848–49-es forradalom és szabadságharc leverése után egy időre\n" +
                " szünetelt az ünneplés, de 1891-ben Ferenc József munkaszüneti nappá\n" +
                " nyilvánította. A két világháború között a nemzeti egység\n" +
                " szimbólumává vált. 1950-től az új államrendszer ideológiája szerint\n" +
                " alakították át az ünnep jelentését, majd 1991-ben ismét állami\n" +
                " ünneppé nyilvánították."));
        feastDays.add(new FeastDay(2031, 9, 23, "1956-os Forradalom", "Magyarország nemzeti ünnepe, amely az 1956-os forradalom és szabadságharc\n" +
                " eseményeire emlékezik. Az ünnep hivatalosan 1989 október 23.-án vált\n" +
                " nemzeti ünneppé a rendszerváltás után, amikor a forradalom hőseit\n" +
                " ünnepelve tisztelegtek a szabadságért és a demokráciáért folytatott\n" +
                " küzdelmük előtt. Az 1956-os forradalom a szovjet megszállás elleni\n" +
                " népfelkelés volt, amely bár véresen végződött, alapvető szerepet\n" +
                " játszott Magyarország függetlenségi törekvéseiben és a későbbi\n" +
                " rendszerváltásban. Az október 23.-i események máig a szabadság és a\n" +
                " nemzeti szuverenitás fontos szimbólumai."));
        feastDays.add(new FeastDay(2031, 9, 29, "Óraátállítás", "Az őszi óraátállítás (a téli időszámítás kezdete) – (vasárnap),\n" +
                " 3:00 → 2:00\n" +
                " Ekkor kell 1 órát visszaállítani az órákat.\n" +
                " Az őszi óraátállítás célja, hogy reggel világosabb legyen, így a\n" +
                " természetes fényt jobban kihasználjuk. Magyarországon először 1916-ban\n" +
                " vezették be az óraátállítást energiatakarékossági okokból. Bár több\n" +
                " alkalommal szünetelt, végül 1980-ban újra bevezették, hogy csökkentsék a\n" +
                " villamosenergia-felhasználást. A téli időszámítás október utolsó\n" +
                " vasárnapján kezdődik, amikor az órákat visszaállítjuk. Ezáltal reggel\n" +
                " hamarabb világosodik, de este hamarabb sötétedik.  1996 óta\n" +
                " kormányrendelet szabályozza az óraátállítást.\n" +
                " "));
        feastDays.add(new FeastDay(2031, 10, 1, "Mindenszentek", "Mindenszentek ünnepe november 1.-én van, amikor a katolikus egyház\n" +
                " megemlékezik minden üdvözült lélekről, akik elnyerték a mennyországot,\n" +
                " de nevüket nem tartják számon a naptárban. Az ünnep eredete a\n" +
                " kereszténység előtti halotti kultuszokra is visszavezethető. Hivatalosan a\n" +
                " 8. században III. Gergely pápa idején vezették be, aki a Szent\n" +
                " Péter-bazilikában kápolnát szentelt minden szent tiszteletére.\n" +
                " Magyarországon munkaszüneti nap. Sokan virágokkal és gyertyagyújtással\n" +
                " tisztelegnek elhunyt szeretteik emléke előtt. Ez a nap a hit és a\n" +
                " megemlékezés ünnepe."));
        feastDays.add(new FeastDay(2031, 10, 2, "Halottak napja", "Halottak napja november 2.-án van, amikor a katolikus egyház megemlékezik az\n" +
                " elhunyt hívekről. Az ünnep célja, hogy imádságokkal és jó\n" +
                " cselekedetekkel segítsük a halottak lelki üdvét és tisztelegjünk\n" +
                " szeretteink emléke előtt. Bár gyökerei a kereszténység előtti halotti\n" +
                " kultuszokig nyúlnak vissza, mai formáját a 11. században Cluny-i Szent\n" +
                " Odiló apát vezette be. Ő rendelte el, hogy a kolostorok imádkozzanak a\n" +
                " tisztítótűzben szenvedő lelkekért. A gyertyagyújtás és a virágok\n" +
                " elhelyezése máig az emlékezés fontos része. "));
        feastDays.add(new FeastDay(2031, 11, 3, "Advent első napja", "Advent a karácsony előtti négyhetes időszak, mely Jézus Krisztus\n" +
                " születésére és második eljövetelére való várakozást jelenti. Az\n" +
                " advent származása a 4. századra nyúlik vissza, amikor bűnbánattal és\n" +
                " lelki felkészüléssel várták a karácsonyt. Kezdetben hosszabb böjti\n" +
                " időszak volt, de fokozatosan négyhetesre csökkent. Az adventi koszorú\n" +
                " hagyománya a 19. században terjedt el, az örökzöld növények a\n" +
                " halhatatlanságot és az újjászületést szimbolizálták. A négy gyertya a\n" +
                " hitet, a reményt, az örömöt és a szeretetet jelképezi. Minden vasárnap\n" +
                " egy újabb gyertya meggyújtásával közelebb kerülünk a karácsonyhoz. \n" +
                " Advent első vasárnapját a karácsonyt megelőző négy vasárnap egyikeként\n" +
                " ünneplik. Ez a nap általában november végére vagy december elejére esik. \n" +
                " "));
        feastDays.add(new FeastDay(2031, 11, 6, "Mikulás", " Mikuláskor december 5.-én éjszaka, vagy 6.-án reggel töltik meg a gyerekek\n" +
                " csizmáit édességgel és játékokkal. Szent Miklós püspök emlék napja. A\n" +
                " 4. században élt szent, aki a gyermekek, a hajósok és a szegények\n" +
                " védőszentje lett, híres volt jótékonyságáról. Egy legenda szerint\n" +
                " titokban segített egy szegény ember három lányán, akiket így férjhez\n" +
                " tudott adni.\n" +
                " Ebből a történetből született a hagyomány, hogy Szent Miklós éjjelente\n" +
                " ajándékokat hoz a jó gyerekeknek. Ma már világszerte ismert a piros\n" +
                " ruhás, fehér szakállú Mikulás.  A Mikulás-hagyományok országonként\n" +
                " eltérőek. Egyes helyeken a Mikulás hozza az ajándékokat, máshol a\n" +
                " Télapó. A lényeg azonban mindenhol ugyanaz: a szeretet az adakozás és a\n" +
                " gyermeki öröm ünnepe"));
        feastDays.add(new FeastDay(2031, 11, 13, "Luca napja", "Luca napja a keresztény és a népi hagyományok keveréke. Szent Luca a 3.\n" +
                " századi vértanú, a látás védőszentje, de a népi hitvilágban a\n" +
                " termékenység, a jólét és a védelem szimbóluma. A hagyomány szerint ezen\n" +
                " a napon kezdődött a tél leghosszabb éjszakája, így Luca napja a fény és\n" +
                " a sötétség határán áll. Ezen a napon a nők nem dolgoztak, hogy bőséges\n" +
                " legyen a következő év termése. Szokás volt Luca-széket készíteni,\n" +
                " búzát ültetni és jóslásokat végezni. Luca napja a megújulás és a\n" +
                " remény ünnepe, a fény győzelme a sötétség felett és a következő év\n" +
                " jólétének biztosítása.\n" +
                " "));
        feastDays.add(new FeastDay(2031, 11, 21, "Téli napforduló", "Az év legrövidebb napja nap. \n" +
                " Napkelte: \n" +
                " Napnyugta: \n" +
                " Világos időtartam: \n" +
                " A téli napforduló a legrövidebb nap az évben, amikor a Nap a\n" +
                " legalacsonyabbra emelkedik az égbolton. Ez a jelenség a Föld\n" +
                " tengelyferdesége és keringése miatt következik be. Az északi féltekén a\n" +
                " napforduló környékén a Nap csak 8 órán keresztül tartózkodik a horizont\n" +
                " felett, így ez a leghosszabb éjszaka. Ezen a napon kezdődik a nappali fény\n" +
                " fokozatos növekedése, így a következő hónapokban egyre hosszabbak lesznek\n" +
                " a nappalok."));
        feastDays.add(new FeastDay(2031, 11, 24, "Szenteste", "Szenteste a karácsonyi ünnepkör egyik legfontosabb napja, amely Jézus\n" +
                " Krisztus születésének előestéje. Bár Jézus pontos születési dátuma\n" +
                " nem ismert, a keresztény egyház a 4. században december 25.-ét jelölte meg\n" +
                " születésének napjaként, összekapcsolva azt a téli napfordulóval.\n" +
                " December 24.-én este a családok összegyűlnek, közösen vacsoráznak,\n" +
                " ajándékokat adnak egymásnak és karácsonyfát állítanak. Magyarországon\n" +
                " a hagyomány szerint a Jézuska hozza az ajándékokat, ezzel is megemlékezve\n" +
                " Jézus születésének csodájáról. A fenyőfa, amely a 16. századtól vált\n" +
                " a karácsony szimbólumává, az örök életet jelképezi. Az ajándékozás a\n" +
                " szeretet és a megbecsülés kifejezése, amely a három királyok\n" +
                " ajándékozására utal. Karácsony, bár elsősorban keresztény ünnep,\n" +
                " világszerte a szeretet, a béke és a család ünnepe lett."));
        feastDays.add(new FeastDay(2031, 11, 25, "Karácsony", "A keresztények számára Jézus Krisztus születésének ünnepe, melyet a 4.\n" +
                " század óta tartanak. Az ünnep a téli napfordulóhoz kapcsolódik,\n" +
                " szimbolizálva a fény győzelmét a sötétség felett. Karácsony a remény,\n" +
                " a szeretet és a megváltás ünnepe, amely a családi együttlét, az öröm\n" +
                " és a vallási szertartások ideje. Karácsonyfa, betlehem és csillag a\n" +
                " szimbólumok, amelyek Jézus születését, az örök életet és az\n" +
                " útmutatást képviselik. Az ünneplés világszerte eltérő, de mindegyik\n" +
                " kultúrában a szeretet és a béke fontosságát hangsúlyozza. Karácsony\n" +
                " lehetőséget ad az emberi kapcsolatok és az élet értelmének\n" +
                " átgondolására"));
        feastDays.add(new FeastDay(2031, 11, 26, "Karácsony másnapja", "A keresztény hagyományban Szent István, az első vértanú emléknapja,\n" +
                " azonban Magyarországon elsősorban Szent István királyra, az\n" +
                " államalapítóra emlékeznek. A 4. századi eredetű ünnep során Jézus\n" +
                " születése után következik Szent István napja, aki kulcsszerepet játszott\n" +
                " a kereszténység elterjesztésében Magyarországon. Bár Szent István a 11.\n" +
                " században élt, az egyház később tette december 26.-át emlékévé.\n" +
                " Magyarországon nemzeti hősként is tisztelik, mivel ő alapította az\n" +
                " államot. Ezen a napon az emberek szentmisére mennek, megemlékeznek\n" +
                " királyukról és különböző ünnepi rendezvényeken is részt vesznek"));
        feastDays.add(new FeastDay(2031, 11, 31, "Szilveszter", "A Szilveszter az év utolsó napja, amikor búcsút veszünk az óévtől és\n" +
                " ünnepeljük az új év kezdetét. Az ünnep neve Szent Szilveszter pápától\n" +
                " származik, aki 335 körül halt meg, de az újév ünneplésének hagyományai\n" +
                " sokkal régebbre nyúlnak vissza. Szilveszter éjszakáján családok és\n" +
                " barátok együtt ünnepelnek, vacsoráznak, tűzijátékot néznek és\n" +
                " jókívánságokat mondanak. A tűzijáték és a zajkeltés az ősi\n" +
                " hagyományokból ered. Az ünnep a megújulás, az összetartozás és a\n" +
                " remény kifejezője, miközben gazdaságilag is fontos szerepet játszik a\n" +
                " fogyasztói társadalomban.\n" +
                " \n" +
                " Boldog Új Évet kívánok! Legyen tele örömmel, egészséggel és sikerrel.\n" +
                " Hozzon az új év boldog pillanatokat, új lehetőségeket és boldog együtt\n" +
                " töltött időt!"));
        feastDays.add(new FeastDay(2032, 0, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma.\n" +
                " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az\n" +
                " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy\n" +
                " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és\n" +
                " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a\n" +
                " mondás. Legyen ez a nap a változás első lépése. Boldogságot,\n" +
                " egészséget és kitartást kívánok az új év minden pillanatára!\n"
        ));
        feastDays.add(new FeastDay(2032, 0, 6, "Vízkereszt", " A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a\n" +
                " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus\n" +
                " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus\n" +
                " megkeresztelkedésére és az első csodájára. A három királyok aranyat,\n" +
                " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a\n" +
                " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet\n" +
                " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi\n" +
                " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak\n" +
                " házszenteléseket, Isten áldását kérve az otthonra és az új évre."));
        feastDays.add(new FeastDay(2032, 1, 14, "Valentin nap", "A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint\n" +
                " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században\n" +
                " titokban esketett meg szerelmespárokat, mert II. Claudius császár\n" +
                " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák.\n" +
                " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az\n" +
                " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a\n" +
                " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal\n" +
                " és üdvözlőkártyákkal ünneplik."));
        feastDays.add(new FeastDay(2032, 2, 1, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja,\n" +
                " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a\n" +
                " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A\n" +
                " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és\n" +
                " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki\n" +
                " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának\n" +
                " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel,\n" +
                " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére\n" +
                " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A\n" +
                " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap\n" +
                " előtt tartanak. Általában február végére vagy március elejére esik."));
        feastDays.add(new FeastDay(2032, 2, 8, "Nőnap", " Az első Nőnapot 1908 március 8.-án tartották. A női munkások\n" +
                " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az\n" +
                " ünnep célja a nők társadalmi, politikai és gazdasági jogainak\n" +
                " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben\n" +
                " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így\n" +
                " 1911-ben már több európai országban is megünnepelték. Magyarországon\n" +
                " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már\n" +
                " a nők jogainak elismerésére és tiszteletére emelkedett."));
        feastDays.add(new FeastDay(2032, 2, 15, "1848-as Forradalom", "Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a\n" +
                " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti\n" +
                " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a\n" +
                " sajtószabadságért, a jobb életkörülményekért és a függetlenségért.\n" +
                " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című\n" +
                " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet\n" +
                " függetlenségi törekvése a magyar történelem egyik legfontosabb\n" +
                " eseményévé vált. Az események hatására megerősödött a nemzeti\n" +
                " identitás és ma már a magyar szabadság és összetartozás napjaként\n" +
                " ünnepeljük.\n" +
                " "));
        feastDays.add(new FeastDay(2032, 2, 26, "Óraátállítás", " A tavaszi óraátállítás a nyári időszámítás kezdete. \n" +
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
                " óraátállítást Magyarországon."));
        feastDays.add(new FeastDay(2032, 3, 1, "Bolondok napja", "Április elseje a bolondok napja, a bolondos tréfák és ártalmatlan\n" +
                " átverések napja. Az emberek apró csínyekkel, például hamis hírekkel vagy\n" +
                " kitalált történetekkel szórakoztatják egymást. Az internet\n" +
                " elterjedésével az álhírek és vírusos videók is népszerűvé váltak. A\n" +
                " nap célja, hogy jókedvet és nevetést hozzon, miközben erősíti a\n" +
                " közösségi szellemet. Eredete több elméletre vezethető vissza. A\n" +
                " legnépszerűbb magyarázat szerint a XVI. században, a Gergely-naptár\n" +
                " bevezetésekor azok, akik áprilisban ünnepelték az újévet,\n" +
                " Franciaországban bolondnak számítottak, így tréfák céltáblájává\n" +
                " váltak. Hasonló szokások figyelhetők meg a kelta tavaszi ünnepekben és az\n" +
                " ókori római Hilaria ünnepén is, ahol a bolondos viselkedés a tavaszi\n" +
                " megújulást jelezte. "));
        feastDays.add(new FeastDay(2032, 3, 9, "Virágvasárnap", " A Virágvasárnap a húsvét előtti vasárnap, Jézus jeruzsálemi\n" +
                " bevonulásának emlékére van. Az evangéliumok szerint Jézus szamárháton\n" +
                " vonult be a városba, miközben a hívek pálmaágakat lengettek, üdvözölve\n" +
                " őt, mint a Megváltót. A virágok, a pálmák és a zöld ágak használata\n" +
                " az ünnep jelképei, melyek a békét és az üdvözítőt hirdetik. A\n" +
                " keresztény hagyományban Virágvasárnap a nagyböjt végét és a húsvéti\n" +
                " ünnepekre való lelki felkészülést jelzi. Az ünnep hivatalosan a 4.\n" +
                " század óta van jelen az egyházi naptárban és azóta része a keresztény\n" +
                " hagyományoknak világszerte."));
        feastDays.add(new FeastDay(2032, 3, 14, "Nagypéntek", "A Nagypéntek a keresztény húsvéti ünnepkör legfontosabb napja, amikor\n" +
                " Jézus Krisztus kereszthalálát és szenvedését emlékezzük meg. Az ünnep\n" +
                " célja, hogy a hívők átgondolják a megváltásra tett áldozat\n" +
                " jelentőségét. E napot a 4. század óta ünneplik, amikor a keresztény\n" +
                " egyház hivatalosan is elismerte Jézus kereszthalálának fontosságát.\n" +
                " Magyarországon a 20. század elejétől munkaszüneti nap. A keresztény\n" +
                " hagyományokban sötét napként van jelen, amikor sokan böjtölnek és\n" +
                " csendes megemlékezéseket tartanak. Az egyházi szertartások mellett a\n" +
                " keresztény közösség számára Nagypéntek a bűnbánat és a lelki\n" +
                " megújulás napja is.  Nagypénteket a húsvét előtti pénteken ünneplik,\n" +
                " tehát ez a dátum évente változik. \n" +
                " "));
        feastDays.add(new FeastDay(2032, 3, 16, "Húsvétvasárnap", "Húsvétvasárnap a kereszténység legfontosabb ünnepe, Jézus Krisztus\n" +
                " feltámadásának napja, amely a halál feletti győzelmet és az örök élet\n" +
                " ígéretét hirdeti. Magyarországon 1991 óta munkaszüneti nap, de\n" +
                " jelentősége évszázadok óta meghatározó. Húsvét a 40 napos nagyböjt\n" +
                " végét jelzi, amely a bűnbánat és a lelki megújulás időszaka. A gyerekek\n" +
                " az ajándékaikat hagyományosan ezen a napon kapják meg, édességeket és\n" +
                " tojásokat, amelyeket a húsvéti nyúl hoz.  A húsvéti nyúl a 16. századi\n" +
                " Németországból ered és a termékenység, valamint az újjászületés\n" +
                " jelképe. Eredete pogány hagyományokhoz köthető, ahol a nyúl és a tojás\n" +
                " a tavaszi újjászületés szimbóluma volt, melyek később beépültek a\n" +
                " keresztény húsvéti szokásokba.\n" +
                " A Húsvétot évente változó dátumon ünneplik, mivel a tavaszi\n" +
                " napéjegyenlőség utáni első teliholdat követő vasárnapra esik."));
        feastDays.add(new FeastDay(2032, 3, 17, "Húsvéthétfő", "Húsvéthétfő a húsvéti ünnepkör egyik legfontosabb napja, amely Jézus\n" +
                " Krisztus feltámadását követően az új élet és a megújulás\n" +
                " szimbólumává vált. Magyarországon a húsvéti locsolás hagyománya a 17.\n" +
                " századtól terjedt el. A férfiak vízzel vagy illatos kölnivel locsolják\n" +
                " meg a nőket, kívánva nekik egészséget, boldogságot és termékenységet.\n" +
                " A locsolás eredete valószínűleg a megtisztulás és a tavaszi megújulás\n" +
                " ősi hiedelmeiből származik. A húsvéti nyúl a tavasz, az új élet és a\n" +
                " termékenység jelképe, míg a bárány Krisztus áldozatát, ártatlanságát\n" +
                " és a megváltást szimbolizálja. Magyarországon a húsvétot a\n" +
                " kereszténység elterjedése óta ünneplik és a népszokások a 19.\n" +
                " századtól erősödtek meg."));
        feastDays.add(new FeastDay(2032, 3, 22, "A Föld napja", "A Föld napja 1970-ben indult el, amikor az Egyesült Államokban Gaylord\n" +
                " Nelson szenátor kezdeményezésére több millió ember gyűlt össze, hogy\n" +
                " felhívja a figyelmet a környezetszennyezésre és a természeti erőforrások\n" +
                " védelmére. A nap célja, hogy globálisan tudatosítsa az emberekben a\n" +
                " környezetvédelem fontosságát és ösztönözze a fenntartható életmódra\n" +
                " való áttérést. Azóta minden évben április 22.-én világszerte\n" +
                " megemlékeznek róla. Számos esemény, akció és kampány hívja fel a\n" +
                " figyelmet a bolygó védelmére és a környezeti problémák megoldására.\n" +
                " \n" +
                " \n" +
                " "));
        feastDays.add(new FeastDay(2032, 3, 30, "Májusfa állítás", "A májusfa felállításának legjellemzőbb hagyomány szerinti időpontja a\n" +
                " május 1-jét megelőző nap délután/este volt. \n" +
                " Magyarországon a középkor óta ismert, tehát legalább a 14. századtól\n" +
                " ünneplik. A szokás az ősi tavaszi rituálékra épül, melyek a természet\n" +
                " megújulását és az élet körforgását szimbolizálták. A fiatal legények\n" +
                " a lányok háza elé állították a feldíszített fákat, hogy kifejezzék\n" +
                " szerelmi szándékukat. A májusfát szalagokkal, virágokkal díszítették\n" +
                " és május végén vagy pünkösdkor táncos mulatság keretében bontották\n" +
                " le, amit kitáncolásnak neveztek. A hagyomány a szerelem és a termékenység\n" +
                " ünnepe volt, később pedig összefonódott a munka ünnepével (május 1.).\n" +
                " Mára már kevésbé elterjedt szokás, de sok helyen igyekeznek még\n" +
                " fenntartani."));
        feastDays.add(new FeastDay(2032, 4, 1, "A Munka ünnepe", "Május 1.-e a munka ünnepe. A munkások jogaiért és szolidaritásáért\n" +
                " folytatott küzdelem emléknapja világszerte. Az ünnep gyökerei az 1886-os\n" +
                " chicagói tüntetésekhez nyúlnak vissza, ahol a munkások a nyolcórás\n" +
                " munkaidő bevezetéséért demonstráltak. Az események hatására 1890-től\n" +
                " május 1.-ét a munkások nemzetközi szolidaritási napjává\n" +
                " nyilvánították.  Magyarországon először 1890-ben tartották meg, de\n" +
                " hivatalos állami ünnep csak 1946-ban lett. A szocializmus idején\n" +
                " nagyszabású felvonulásokkal és tömegrendezvényekkel ünnepelték. A\n" +
                " rendszerváltás után a jelentése átalakult, de továbbra is a munka\n" +
                " tiszteletét és a munkavállalók jogainak fontosságát hirdeti. "));
        feastDays.add(new FeastDay(2032, 4, 7, "Anyák napja", "Az édesanyák napja a szeretet, a tisztelet és a hála ünnepe, amelyet\n" +
                " világszerte az édesanyák tiszteletére tartanak. Gyökerei az ókori\n" +
                " Görögországba nyúlnak vissza, ahol Rheát, az istenek anyját ünnepelték.\n" +
                " A modern anyák napja az Egyesült Államokból indult 1908-ban, amikor Anna\n" +
                " Jarvis kampányt indított édesanyja emlékére. Magyarországon először\n" +
                " 1925-ben ünnepelték és a májusi Mária-tisztelet hagyományaival is\n" +
                " összekapcsolódott. Ez a nap a gondoskodó és szerető édesanyák, valamint\n" +
                " minden női gondviselő iránti megbecsülés kifejezésére szolgál.\n" +
                " Virágokkal, ajándékokkal és figyelmességgel fejezik ki hálájukat a\n" +
                " gyerekek. Bár világszerte eltérő időpontokban ünneplik, mindenhol az\n" +
                " anyák iránti szeretet és tisztelet áll a középpontban. Anyák napját\n" +
                " Magyarországon minden év május első vasárnapján ünneplik. \n" +
                " "));
        feastDays.add(new FeastDay(2032, 4, 28, "Gyermeknap", "A gyermeknap a gyerekek jogainak és jólétének fontosságát hirdető\n" +
                " ünnep, melyet világszerte különböző időpontokban tartanak. Eredete\n" +
                " 1925-re nyúlik vissza, amikor a genfi Gyermekjóléti Konferencián\n" +
                " elhatározták, hogy kiemelik a gyermekek védelmének fontosságát.\n" +
                " Magyarországon 1931-ben tartották meg először, majd 1950 óta május\n" +
                " utolsó vasárnapján ünneplik. Ez a nap a gyerekekről szól, akik ilyenkor\n" +
                " különféle programokon vehetnek részt, ahol játékokkal és szórakoztató\n" +
                " rendezvényekkel ünneplik őket. Az ünnep célja, hogy felhívja a figyelmet\n" +
                " a gyermekek jogaira és boldogságuk fontosságára. Magyarországon a\n" +
                " Gyermeknapot minden évben május utolsó vasárnapján ünneplik."));
        feastDays.add(new FeastDay(2032, 5, 4, "Pünkösd", "Pünkösd a kereszténység egyik legősibb ünnepe, melyet már a 4.\n" +
                " századtól kezdve ünnepelnek a keresztény egyházban. Az ünnep Jézus\n" +
                " mennybemenetele után ötven nappal emlékezik meg a Szentlélek\n" +
                " eljöveteléről, amely erőt adott az apostoloknak a keresztény hit\n" +
                " hirdetéséhez. Ez az esemény az egyház születésnapjaként is ismert. A\n" +
                " „pünkösd” elnevezés a görög „pentékoszté” szóból ered, ami\n" +
                " ötvenediket jelent. Magyarországon a középkor óta tartják számon és\n" +
                " számos népi hagyomány kapcsolódik hozzá, például a pünkösdi király\n" +
                " és királyné választása. Pünkösd vasárnapját a húsvét utáni 50.\n" +
                " napon, tehát 50 nappal húsvétvasárnap után ünneplik. "));
        feastDays.add(new FeastDay(2032, 5, 5, "Pünkösdhétfő", "A Pünkösdhétfő a keresztény egyház egyik legfontosabb ünnepének, a\n" +
                " pünkösdnek a második napja. Pünkösd a húsvét utáni 50. napon van, és a\n" +
                " Szentlélek eljövetelét ünnepli, amikor Jézus tanítványai erőt és\n" +
                " útmutatást kaptak küldetésükhöz. Az esemény a keresztény egyház\n" +
                " születésnapjának is tekinthető. Az ünnep gyökerei a zsidó pünkösdi\n" +
                " ünnepre (Sávuót) vezethetők vissza, amely az aratási hálaadás napja\n" +
                " volt. Magyarországon pünkösdhétfő munkaszüneti nap és hivatalosan 1993\n" +
                " óta ünnepeljük újra. Az ünnep a megújulást és a közösség\n" +
                " összetartozását jelképezi."));
        feastDays.add(new FeastDay(2032, 5, 18, "Apák napja", "Apák napja világszerte az édesapák tiszteletére szolgál, elismerve a\n" +
                " családban és a gyermeknevelésben betöltött szerepüket. Az ünnep eredete\n" +
                " az Egyesült Államokhoz köthető, ahol először 1910-ben tartották meg\n" +
                " Sonora Smart Dodd kezdeményezésére , aki édesapját akarta tiszteletben\n" +
                " részesíteni . Magyarországon az apák napja az 1990-es években kezdett\n" +
                " elterjedni  és 1994 óta ünnepeljük minden év június harmadik\n" +
                " vasárnapján. Célja, hogy elismerje az apák és az apaszerepet betöltő\n" +
                " férfiak szeretetét, áldozatvállalását és a családban betöltött\n" +
                " szerepét. Ez a nap lehetőséget ad arra, hogy köszönetet mondjunk az apák\n" +
                " szeretetéért, támogatásáért és áldozatos munkájáért, amelyet\n" +
                " családjukért tesznek. "));
        feastDays.add(new FeastDay(2032, 5, 20, "Nyári napforduló", "Az év leghosszabb napja. \n" +
                " Napkelte: \n" +
                " Napnyugta: \n" +
                " Világos időtartam: \n" +
                " A nyári napforduló a leghosszabb nap az évben, amikor a Nap a legmagasabbra\n" +
                " emelkedik az égbolton. Ez a jelenség a Föld tengelyferdesége és a\n" +
                " keringése miatt következik be. Az északi féltekén a napforduló\n" +
                " környékén a Nap több mint 15 órán keresztül a horizont felett\n" +
                " tartózkodik. Ezen a napon a Föld az északi féltekén éri el a leghosszabb\n" +
                " nappali órákat, így ez a legrövidebb éjszaka."));
        feastDays.add(new FeastDay(2032, 7, 20, "Szent István-nap", "Szent István király koronázása körülbelül 1000-ben történt, ezzel\n" +
                " megalapozva a keresztény Magyarországot. 1083 augusztus 20.-án szentté\n" +
                " avatták a Szent Péter-bazilikában. Az Árpád-korban az István-kultusz az\n" +
                " uralkodó dinasztiát támogatta és egységet teremtett a népcsoportok\n" +
                " között. 1686-ban XI. Ince pápa elrendelte, hogy az egész katolikus világ\n" +
                " emlékezzen meg róla, majd 1771-ben Mária Terézia országos ünneppé tette.\n" +
                " Az 1848–49-es forradalom és szabadságharc leverése után egy időre\n" +
                " szünetelt az ünneplés, de 1891-ben Ferenc József munkaszüneti nappá\n" +
                " nyilvánította. A két világháború között a nemzeti egység\n" +
                " szimbólumává vált. 1950-től az új államrendszer ideológiája szerint\n" +
                " alakították át az ünnep jelentését, majd 1991-ben ismét állami\n" +
                " ünneppé nyilvánították."));
        feastDays.add(new FeastDay(2032, 9, 23, "1956-os Forradalom", "Magyarország nemzeti ünnepe, amely az 1956-os forradalom és szabadságharc\n" +
                " eseményeire emlékezik. Az ünnep hivatalosan 1989 október 23.-án vált\n" +
                " nemzeti ünneppé a rendszerváltás után, amikor a forradalom hőseit\n" +
                " ünnepelve tisztelegtek a szabadságért és a demokráciáért folytatott\n" +
                " küzdelmük előtt. Az 1956-os forradalom a szovjet megszállás elleni\n" +
                " népfelkelés volt, amely bár véresen végződött, alapvető szerepet\n" +
                " játszott Magyarország függetlenségi törekvéseiben és a későbbi\n" +
                " rendszerváltásban. Az október 23.-i események máig a szabadság és a\n" +
                " nemzeti szuverenitás fontos szimbólumai."));
        feastDays.add(new FeastDay(2032, 9, 29, "Óraátállítás", "Az őszi óraátállítás (a téli időszámítás kezdete) – (vasárnap),\n" +
                " 3:00 → 2:00\n" +
                " Ekkor kell 1 órát visszaállítani az órákat.\n" +
                " Az őszi óraátállítás célja, hogy reggel világosabb legyen, így a\n" +
                " természetes fényt jobban kihasználjuk. Magyarországon először 1916-ban\n" +
                " vezették be az óraátállítást energiatakarékossági okokból. Bár több\n" +
                " alkalommal szünetelt, végül 1980-ban újra bevezették, hogy csökkentsék a\n" +
                " villamosenergia-felhasználást. A téli időszámítás október utolsó\n" +
                " vasárnapján kezdődik, amikor az órákat visszaállítjuk. Ezáltal reggel\n" +
                " hamarabb világosodik, de este hamarabb sötétedik.  1996 óta\n" +
                " kormányrendelet szabályozza az óraátállítást.\n" +
                " "));
        feastDays.add(new FeastDay(2032, 10, 1, "Mindenszentek", "Mindenszentek ünnepe november 1.-én van, amikor a katolikus egyház\n" +
                " megemlékezik minden üdvözült lélekről, akik elnyerték a mennyországot,\n" +
                " de nevüket nem tartják számon a naptárban. Az ünnep eredete a\n" +
                " kereszténység előtti halotti kultuszokra is visszavezethető. Hivatalosan a\n" +
                " 8. században III. Gergely pápa idején vezették be, aki a Szent\n" +
                " Péter-bazilikában kápolnát szentelt minden szent tiszteletére.\n" +
                " Magyarországon munkaszüneti nap. Sokan virágokkal és gyertyagyújtással\n" +
                " tisztelegnek elhunyt szeretteik emléke előtt. Ez a nap a hit és a\n" +
                " megemlékezés ünnepe."));
        feastDays.add(new FeastDay(2032, 10, 2, "Halottak napja", "Halottak napja november 2.-án van, amikor a katolikus egyház megemlékezik az\n" +
                " elhunyt hívekről. Az ünnep célja, hogy imádságokkal és jó\n" +
                " cselekedetekkel segítsük a halottak lelki üdvét és tisztelegjünk\n" +
                " szeretteink emléke előtt. Bár gyökerei a kereszténység előtti halotti\n" +
                " kultuszokig nyúlnak vissza, mai formáját a 11. században Cluny-i Szent\n" +
                " Odiló apát vezette be. Ő rendelte el, hogy a kolostorok imádkozzanak a\n" +
                " tisztítótűzben szenvedő lelkekért. A gyertyagyújtás és a virágok\n" +
                " elhelyezése máig az emlékezés fontos része. "));
        feastDays.add(new FeastDay(2032, 11, 3, "Advent első napja", "Advent a karácsony előtti négyhetes időszak, mely Jézus Krisztus\n" +
                " születésére és második eljövetelére való várakozást jelenti. Az\n" +
                " advent származása a 4. századra nyúlik vissza, amikor bűnbánattal és\n" +
                " lelki felkészüléssel várták a karácsonyt. Kezdetben hosszabb böjti\n" +
                " időszak volt, de fokozatosan négyhetesre csökkent. Az adventi koszorú\n" +
                " hagyománya a 19. században terjedt el, az örökzöld növények a\n" +
                " halhatatlanságot és az újjászületést szimbolizálták. A négy gyertya a\n" +
                " hitet, a reményt, az örömöt és a szeretetet jelképezi. Minden vasárnap\n" +
                " egy újabb gyertya meggyújtásával közelebb kerülünk a karácsonyhoz. \n" +
                " Advent első vasárnapját a karácsonyt megelőző négy vasárnap egyikeként\n" +
                " ünneplik. Ez a nap általában november végére vagy december elejére esik. \n" +
                " "));
        feastDays.add(new FeastDay(2032, 11, 6, "Mikulás", " Mikuláskor december 5.-én éjszaka, vagy 6.-án reggel töltik meg a gyerekek\n" +
                " csizmáit édességgel és játékokkal. Szent Miklós püspök emlék napja. A\n" +
                " 4. században élt szent, aki a gyermekek, a hajósok és a szegények\n" +
                " védőszentje lett, híres volt jótékonyságáról. Egy legenda szerint\n" +
                " titokban segített egy szegény ember három lányán, akiket így férjhez\n" +
                " tudott adni.\n" +
                " Ebből a történetből született a hagyomány, hogy Szent Miklós éjjelente\n" +
                " ajándékokat hoz a jó gyerekeknek. Ma már világszerte ismert a piros\n" +
                " ruhás, fehér szakállú Mikulás.  A Mikulás-hagyományok országonként\n" +
                " eltérőek. Egyes helyeken a Mikulás hozza az ajándékokat, máshol a\n" +
                " Télapó. A lényeg azonban mindenhol ugyanaz: a szeretet az adakozás és a\n" +
                " gyermeki öröm ünnepe"));
        feastDays.add(new FeastDay(2032, 11, 13, "Luca napja", "Luca napja a keresztény és a népi hagyományok keveréke. Szent Luca a 3.\n" +
                " századi vértanú, a látás védőszentje, de a népi hitvilágban a\n" +
                " termékenység, a jólét és a védelem szimbóluma. A hagyomány szerint ezen\n" +
                " a napon kezdődött a tél leghosszabb éjszakája, így Luca napja a fény és\n" +
                " a sötétség határán áll. Ezen a napon a nők nem dolgoztak, hogy bőséges\n" +
                " legyen a következő év termése. Szokás volt Luca-széket készíteni,\n" +
                " búzát ültetni és jóslásokat végezni. Luca napja a megújulás és a\n" +
                " remény ünnepe, a fény győzelme a sötétség felett és a következő év\n" +
                " jólétének biztosítása.\n" +
                " "));
        feastDays.add(new FeastDay(2032, 11, 21, "Téli napforduló", "Az év legrövidebb napja nap. \n" +
                " Napkelte: \n" +
                " Napnyugta: \n" +
                " Világos időtartam: \n" +
                " A téli napforduló a legrövidebb nap az évben, amikor a Nap a\n" +
                " legalacsonyabbra emelkedik az égbolton. Ez a jelenség a Föld\n" +
                " tengelyferdesége és keringése miatt következik be. Az északi féltekén a\n" +
                " napforduló környékén a Nap csak 8 órán keresztül tartózkodik a horizont\n" +
                " felett, így ez a leghosszabb éjszaka. Ezen a napon kezdődik a nappali fény\n" +
                " fokozatos növekedése, így a következő hónapokban egyre hosszabbak lesznek\n" +
                " a nappalok."));
        feastDays.add(new FeastDay(2032, 11, 24, "Szenteste", "Szenteste a karácsonyi ünnepkör egyik legfontosabb napja, amely Jézus\n" +
                " Krisztus születésének előestéje. Bár Jézus pontos születési dátuma\n" +
                " nem ismert, a keresztény egyház a 4. században december 25.-ét jelölte meg\n" +
                " születésének napjaként, összekapcsolva azt a téli napfordulóval.\n" +
                " December 24.-én este a családok összegyűlnek, közösen vacsoráznak,\n" +
                " ajándékokat adnak egymásnak és karácsonyfát állítanak. Magyarországon\n" +
                " a hagyomány szerint a Jézuska hozza az ajándékokat, ezzel is megemlékezve\n" +
                " Jézus születésének csodájáról. A fenyőfa, amely a 16. századtól vált\n" +
                " a karácsony szimbólumává, az örök életet jelképezi. Az ajándékozás a\n" +
                " szeretet és a megbecsülés kifejezése, amely a három királyok\n" +
                " ajándékozására utal. Karácsony, bár elsősorban keresztény ünnep,\n" +
                " világszerte a szeretet, a béke és a család ünnepe lett."));
        feastDays.add(new FeastDay(2032, 11, 25, "Karácsony", "A keresztények számára Jézus Krisztus születésének ünnepe, melyet a 4.\n" +
                " század óta tartanak. Az ünnep a téli napfordulóhoz kapcsolódik,\n" +
                " szimbolizálva a fény győzelmét a sötétség felett. Karácsony a remény,\n" +
                " a szeretet és a megváltás ünnepe, amely a családi együttlét, az öröm\n" +
                " és a vallási szertartások ideje. Karácsonyfa, betlehem és csillag a\n" +
                " szimbólumok, amelyek Jézus születését, az örök életet és az\n" +
                " útmutatást képviselik. Az ünneplés világszerte eltérő, de mindegyik\n" +
                " kultúrában a szeretet és a béke fontosságát hangsúlyozza. Karácsony\n" +
                " lehetőséget ad az emberi kapcsolatok és az élet értelmének\n" +
                " átgondolására"));
        feastDays.add(new FeastDay(2032, 11, 26, "Karácsony másnapja", "A keresztény hagyományban Szent István, az első vértanú emléknapja,\n" +
                " azonban Magyarországon elsősorban Szent István királyra, az\n" +
                " államalapítóra emlékeznek. A 4. századi eredetű ünnep során Jézus\n" +
                " születése után következik Szent István napja, aki kulcsszerepet játszott\n" +
                " a kereszténység elterjesztésében Magyarországon. Bár Szent István a 11.\n" +
                " században élt, az egyház később tette december 26.-át emlékévé.\n" +
                " Magyarországon nemzeti hősként is tisztelik, mivel ő alapította az\n" +
                " államot. Ezen a napon az emberek szentmisére mennek, megemlékeznek\n" +
                " királyukról és különböző ünnepi rendezvényeken is részt vesznek"));
        feastDays.add(new FeastDay(2032, 11, 31, "Szilveszter", "A Szilveszter az év utolsó napja, amikor búcsút veszünk az óévtől és\n" +
                " ünnepeljük az új év kezdetét. Az ünnep neve Szent Szilveszter pápától\n" +
                " származik, aki 335 körül halt meg, de az újév ünneplésének hagyományai\n" +
                " sokkal régebbre nyúlnak vissza. Szilveszter éjszakáján családok és\n" +
                " barátok együtt ünnepelnek, vacsoráznak, tűzijátékot néznek és\n" +
                " jókívánságokat mondanak. A tűzijáték és a zajkeltés az ősi\n" +
                " hagyományokból ered. Az ünnep a megújulás, az összetartozás és a\n" +
                " remény kifejezője, miközben gazdaságilag is fontos szerepet játszik a\n" +
                " fogyasztói társadalomban.\n" +
                " \n" +
                " Boldog Új Évet kívánok! Legyen tele örömmel, egészséggel és sikerrel.\n" +
                " Hozzon az új év boldog pillanatokat, új lehetőségeket és boldog együtt\n" +
                " töltött időt!"));
        feastDays.add(new FeastDay(2033, 0, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma.\n" +
                " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az\n" +
                " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy\n" +
                " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és\n" +
                " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a\n" +
                " mondás. Legyen ez a nap a változás első lépése. Boldogságot,\n" +
                " egészséget és kitartást kívánok az új év minden pillanatára!\n"
        ));
        feastDays.add(new FeastDay(2033, 0, 6, "Vízkereszt", " A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a\n" +
                " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus\n" +
                " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus\n" +
                " megkeresztelkedésére és az első csodájára. A három királyok aranyat,\n" +
                " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a\n" +
                " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet\n" +
                " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi\n" +
                " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak\n" +
                " házszenteléseket, Isten áldását kérve az otthonra és az új évre."));
        feastDays.add(new FeastDay(2033, 1, 14, "Valentin nap", "A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint\n" +
                " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században\n" +
                " titokban esketett meg szerelmespárokat, mert II. Claudius császár\n" +
                " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák.\n" +
                " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az\n" +
                " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a\n" +
                " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal\n" +
                " és üdvözlőkártyákkal ünneplik."));
        feastDays.add(new FeastDay(2033, 2, 1, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja,\n" +
                " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a\n" +
                " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A\n" +
                " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és\n" +
                " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki\n" +
                " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának\n" +
                " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel,\n" +
                " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére\n" +
                " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A\n" +
                " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap\n" +
                " előtt tartanak. Általában február végére vagy március elejére esik."));
        feastDays.add(new FeastDay(2033, 2, 8, "Nőnap", " Az első Nőnapot 1908 március 8.-án tartották. A női munkások\n" +
                " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az\n" +
                " ünnep célja a nők társadalmi, politikai és gazdasági jogainak\n" +
                " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben\n" +
                " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így\n" +
                " 1911-ben már több európai országban is megünnepelték. Magyarországon\n" +
                " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már\n" +
                " a nők jogainak elismerésére és tiszteletére emelkedett."));
        feastDays.add(new FeastDay(2033, 2, 15, "1848-as Forradalom", "Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a\n" +
                " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti\n" +
                " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a\n" +
                " sajtószabadságért, a jobb életkörülményekért és a függetlenségért.\n" +
                " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című\n" +
                " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet\n" +
                " függetlenségi törekvése a magyar történelem egyik legfontosabb\n" +
                " eseményévé vált. Az események hatására megerősödött a nemzeti\n" +
                " identitás és ma már a magyar szabadság és összetartozás napjaként\n" +
                " ünnepeljük.\n" +
                " "));
        feastDays.add(new FeastDay(2033, 2, 26, "Óraátállítás", " A tavaszi óraátállítás a nyári időszámítás kezdete. \n" +
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
                " óraátállítást Magyarországon."));
        feastDays.add(new FeastDay(2033, 3, 1, "Bolondok napja", "Április elseje a bolondok napja, a bolondos tréfák és ártalmatlan\n" +
                " átverések napja. Az emberek apró csínyekkel, például hamis hírekkel vagy\n" +
                " kitalált történetekkel szórakoztatják egymást. Az internet\n" +
                " elterjedésével az álhírek és vírusos videók is népszerűvé váltak. A\n" +
                " nap célja, hogy jókedvet és nevetést hozzon, miközben erősíti a\n" +
                " közösségi szellemet. Eredete több elméletre vezethető vissza. A\n" +
                " legnépszerűbb magyarázat szerint a XVI. században, a Gergely-naptár\n" +
                " bevezetésekor azok, akik áprilisban ünnepelték az újévet,\n" +
                " Franciaországban bolondnak számítottak, így tréfák céltáblájává\n" +
                " váltak. Hasonló szokások figyelhetők meg a kelta tavaszi ünnepekben és az\n" +
                " ókori római Hilaria ünnepén is, ahol a bolondos viselkedés a tavaszi\n" +
                " megújulást jelezte. "));
        feastDays.add(new FeastDay(2033, 3, 9, "Virágvasárnap", " A Virágvasárnap a húsvét előtti vasárnap, Jézus jeruzsálemi\n" +
                " bevonulásának emlékére van. Az evangéliumok szerint Jézus szamárháton\n" +
                " vonult be a városba, miközben a hívek pálmaágakat lengettek, üdvözölve\n" +
                " őt, mint a Megváltót. A virágok, a pálmák és a zöld ágak használata\n" +
                " az ünnep jelképei, melyek a békét és az üdvözítőt hirdetik. A\n" +
                " keresztény hagyományban Virágvasárnap a nagyböjt végét és a húsvéti\n" +
                " ünnepekre való lelki felkészülést jelzi. Az ünnep hivatalosan a 4.\n" +
                " század óta van jelen az egyházi naptárban és azóta része a keresztény\n" +
                " hagyományoknak világszerte."));
        feastDays.add(new FeastDay(2033, 3, 14, "Nagypéntek", "A Nagypéntek a keresztény húsvéti ünnepkör legfontosabb napja, amikor\n" +
                " Jézus Krisztus kereszthalálát és szenvedését emlékezzük meg. Az ünnep\n" +
                " célja, hogy a hívők átgondolják a megváltásra tett áldozat\n" +
                " jelentőségét. E napot a 4. század óta ünneplik, amikor a keresztény\n" +
                " egyház hivatalosan is elismerte Jézus kereszthalálának fontosságát.\n" +
                " Magyarországon a 20. század elejétől munkaszüneti nap. A keresztény\n" +
                " hagyományokban sötét napként van jelen, amikor sokan böjtölnek és\n" +
                " csendes megemlékezéseket tartanak. Az egyházi szertartások mellett a\n" +
                " keresztény közösség számára Nagypéntek a bűnbánat és a lelki\n" +
                " megújulás napja is.  Nagypénteket a húsvét előtti pénteken ünneplik,\n" +
                " tehát ez a dátum évente változik. \n" +
                " "));
        feastDays.add(new FeastDay(2033, 3, 16, "Húsvétvasárnap", "Húsvétvasárnap a kereszténység legfontosabb ünnepe, Jézus Krisztus\n" +
                " feltámadásának napja, amely a halál feletti győzelmet és az örök élet\n" +
                " ígéretét hirdeti. Magyarországon 1991 óta munkaszüneti nap, de\n" +
                " jelentősége évszázadok óta meghatározó. Húsvét a 40 napos nagyböjt\n" +
                " végét jelzi, amely a bűnbánat és a lelki megújulás időszaka. A gyerekek\n" +
                " az ajándékaikat hagyományosan ezen a napon kapják meg, édességeket és\n" +
                " tojásokat, amelyeket a húsvéti nyúl hoz.  A húsvéti nyúl a 16. századi\n" +
                " Németországból ered és a termékenység, valamint az újjászületés\n" +
                " jelképe. Eredete pogány hagyományokhoz köthető, ahol a nyúl és a tojás\n" +
                " a tavaszi újjászületés szimbóluma volt, melyek később beépültek a\n" +
                " keresztény húsvéti szokásokba.\n" +
                " A Húsvétot évente változó dátumon ünneplik, mivel a tavaszi\n" +
                " napéjegyenlőség utáni első teliholdat követő vasárnapra esik."));
        feastDays.add(new FeastDay(2033, 3, 17, "Húsvéthétfő", "Húsvéthétfő a húsvéti ünnepkör egyik legfontosabb napja, amely Jézus\n" +
                " Krisztus feltámadását követően az új élet és a megújulás\n" +
                " szimbólumává vált. Magyarországon a húsvéti locsolás hagyománya a 17.\n" +
                " századtól terjedt el. A férfiak vízzel vagy illatos kölnivel locsolják\n" +
                " meg a nőket, kívánva nekik egészséget, boldogságot és termékenységet.\n" +
                " A locsolás eredete valószínűleg a megtisztulás és a tavaszi megújulás\n" +
                " ősi hiedelmeiből származik. A húsvéti nyúl a tavasz, az új élet és a\n" +
                " termékenység jelképe, míg a bárány Krisztus áldozatát, ártatlanságát\n" +
                " és a megváltást szimbolizálja. Magyarországon a húsvétot a\n" +
                " kereszténység elterjedése óta ünneplik és a népszokások a 19.\n" +
                " századtól erősödtek meg."));
        feastDays.add(new FeastDay(2033, 3, 22, "A Föld napja", "A Föld napja 1970-ben indult el, amikor az Egyesült Államokban Gaylord\n" +
                " Nelson szenátor kezdeményezésére több millió ember gyűlt össze, hogy\n" +
                " felhívja a figyelmet a környezetszennyezésre és a természeti erőforrások\n" +
                " védelmére. A nap célja, hogy globálisan tudatosítsa az emberekben a\n" +
                " környezetvédelem fontosságát és ösztönözze a fenntartható életmódra\n" +
                " való áttérést. Azóta minden évben április 22.-én világszerte\n" +
                " megemlékeznek róla. Számos esemény, akció és kampány hívja fel a\n" +
                " figyelmet a bolygó védelmére és a környezeti problémák megoldására.\n" +
                " \n" +
                " \n" +
                " "));
        feastDays.add(new FeastDay(2033, 3, 30, "Májusfa állítás", "A májusfa felállításának legjellemzőbb hagyomány szerinti időpontja a\n" +
                " május 1-jét megelőző nap délután/este volt. \n" +
                " Magyarországon a középkor óta ismert, tehát legalább a 14. századtól\n" +
                " ünneplik. A szokás az ősi tavaszi rituálékra épül, melyek a természet\n" +
                " megújulását és az élet körforgását szimbolizálták. A fiatal legények\n" +
                " a lányok háza elé állították a feldíszített fákat, hogy kifejezzék\n" +
                " szerelmi szándékukat. A májusfát szalagokkal, virágokkal díszítették\n" +
                " és május végén vagy pünkösdkor táncos mulatság keretében bontották\n" +
                " le, amit kitáncolásnak neveztek. A hagyomány a szerelem és a termékenység\n" +
                " ünnepe volt, később pedig összefonódott a munka ünnepével (május 1.).\n" +
                " Mára már kevésbé elterjedt szokás, de sok helyen igyekeznek még\n" +
                " fenntartani."));
        feastDays.add(new FeastDay(2033, 4, 1, "A Munka ünnepe", "Május 1.-e a munka ünnepe. A munkások jogaiért és szolidaritásáért\n" +
                " folytatott küzdelem emléknapja világszerte. Az ünnep gyökerei az 1886-os\n" +
                " chicagói tüntetésekhez nyúlnak vissza, ahol a munkások a nyolcórás\n" +
                " munkaidő bevezetéséért demonstráltak. Az események hatására 1890-től\n" +
                " május 1.-ét a munkások nemzetközi szolidaritási napjává\n" +
                " nyilvánították.  Magyarországon először 1890-ben tartották meg, de\n" +
                " hivatalos állami ünnep csak 1946-ban lett. A szocializmus idején\n" +
                " nagyszabású felvonulásokkal és tömegrendezvényekkel ünnepelték. A\n" +
                " rendszerváltás után a jelentése átalakult, de továbbra is a munka\n" +
                " tiszteletét és a munkavállalók jogainak fontosságát hirdeti. "));
        feastDays.add(new FeastDay(2033, 4, 7, "Anyák napja", "Az édesanyák napja a szeretet, a tisztelet és a hála ünnepe, amelyet\n" +
                " világszerte az édesanyák tiszteletére tartanak. Gyökerei az ókori\n" +
                " Görögországba nyúlnak vissza, ahol Rheát, az istenek anyját ünnepelték.\n" +
                " A modern anyák napja az Egyesült Államokból indult 1908-ban, amikor Anna\n" +
                " Jarvis kampányt indított édesanyja emlékére. Magyarországon először\n" +
                " 1925-ben ünnepelték és a májusi Mária-tisztelet hagyományaival is\n" +
                " összekapcsolódott. Ez a nap a gondoskodó és szerető édesanyák, valamint\n" +
                " minden női gondviselő iránti megbecsülés kifejezésére szolgál.\n" +
                " Virágokkal, ajándékokkal és figyelmességgel fejezik ki hálájukat a\n" +
                " gyerekek. Bár világszerte eltérő időpontokban ünneplik, mindenhol az\n" +
                " anyák iránti szeretet és tisztelet áll a középpontban. Anyák napját\n" +
                " Magyarországon minden év május első vasárnapján ünneplik. \n" +
                " "));
        feastDays.add(new FeastDay(2033, 4, 28, "Gyermeknap", "A gyermeknap a gyerekek jogainak és jólétének fontosságát hirdető\n" +
                " ünnep, melyet világszerte különböző időpontokban tartanak. Eredete\n" +
                " 1925-re nyúlik vissza, amikor a genfi Gyermekjóléti Konferencián\n" +
                " elhatározták, hogy kiemelik a gyermekek védelmének fontosságát.\n" +
                " Magyarországon 1931-ben tartották meg először, majd 1950 óta május\n" +
                " utolsó vasárnapján ünneplik. Ez a nap a gyerekekről szól, akik ilyenkor\n" +
                " különféle programokon vehetnek részt, ahol játékokkal és szórakoztató\n" +
                " rendezvényekkel ünneplik őket. Az ünnep célja, hogy felhívja a figyelmet\n" +
                " a gyermekek jogaira és boldogságuk fontosságára. Magyarországon a\n" +
                " Gyermeknapot minden évben május utolsó vasárnapján ünneplik."));
        feastDays.add(new FeastDay(2033, 5, 4, "Pünkösd", "Pünkösd a kereszténység egyik legősibb ünnepe, melyet már a 4.\n" +
                " századtól kezdve ünnepelnek a keresztény egyházban. Az ünnep Jézus\n" +
                " mennybemenetele után ötven nappal emlékezik meg a Szentlélek\n" +
                " eljöveteléről, amely erőt adott az apostoloknak a keresztény hit\n" +
                " hirdetéséhez. Ez az esemény az egyház születésnapjaként is ismert. A\n" +
                " „pünkösd” elnevezés a görög „pentékoszté” szóból ered, ami\n" +
                " ötvenediket jelent. Magyarországon a középkor óta tartják számon és\n" +
                " számos népi hagyomány kapcsolódik hozzá, például a pünkösdi király\n" +
                " és királyné választása. Pünkösd vasárnapját a húsvét utáni 50.\n" +
                " napon, tehát 50 nappal húsvétvasárnap után ünneplik. "));
        feastDays.add(new FeastDay(2033, 5, 5, "Pünkösdhétfő", "A Pünkösdhétfő a keresztény egyház egyik legfontosabb ünnepének, a\n" +
                " pünkösdnek a második napja. Pünkösd a húsvét utáni 50. napon van, és a\n" +
                " Szentlélek eljövetelét ünnepli, amikor Jézus tanítványai erőt és\n" +
                " útmutatást kaptak küldetésükhöz. Az esemény a keresztény egyház\n" +
                " születésnapjának is tekinthető. Az ünnep gyökerei a zsidó pünkösdi\n" +
                " ünnepre (Sávuót) vezethetők vissza, amely az aratási hálaadás napja\n" +
                " volt. Magyarországon pünkösdhétfő munkaszüneti nap és hivatalosan 1993\n" +
                " óta ünnepeljük újra. Az ünnep a megújulást és a közösség\n" +
                " összetartozását jelképezi."));
        feastDays.add(new FeastDay(2033, 5, 18, "Apák napja", "Apák napja világszerte az édesapák tiszteletére szolgál, elismerve a\n" +
                " családban és a gyermeknevelésben betöltött szerepüket. Az ünnep eredete\n" +
                " az Egyesült Államokhoz köthető, ahol először 1910-ben tartották meg\n" +
                " Sonora Smart Dodd kezdeményezésére , aki édesapját akarta tiszteletben\n" +
                " részesíteni . Magyarországon az apák napja az 1990-es években kezdett\n" +
                " elterjedni  és 1994 óta ünnepeljük minden év június harmadik\n" +
                " vasárnapján. Célja, hogy elismerje az apák és az apaszerepet betöltő\n" +
                " férfiak szeretetét, áldozatvállalását és a családban betöltött\n" +
                " szerepét. Ez a nap lehetőséget ad arra, hogy köszönetet mondjunk az apák\n" +
                " szeretetéért, támogatásáért és áldozatos munkájáért, amelyet\n" +
                " családjukért tesznek. "));
        feastDays.add(new FeastDay(2033, 5, 20, "Nyári napforduló", "Az év leghosszabb napja. \n" +
                " Napkelte: \n" +
                " Napnyugta: \n" +
                " Világos időtartam: \n" +
                " A nyári napforduló a leghosszabb nap az évben, amikor a Nap a legmagasabbra\n" +
                " emelkedik az égbolton. Ez a jelenség a Föld tengelyferdesége és a\n" +
                " keringése miatt következik be. Az északi féltekén a napforduló\n" +
                " környékén a Nap több mint 15 órán keresztül a horizont felett\n" +
                " tartózkodik. Ezen a napon a Föld az északi féltekén éri el a leghosszabb\n" +
                " nappali órákat, így ez a legrövidebb éjszaka."));
        feastDays.add(new FeastDay(2033, 7, 20, "Szent István-nap", "Szent István király koronázása körülbelül 1000-ben történt, ezzel\n" +
                " megalapozva a keresztény Magyarországot. 1083 augusztus 20.-án szentté\n" +
                " avatták a Szent Péter-bazilikában. Az Árpád-korban az István-kultusz az\n" +
                " uralkodó dinasztiát támogatta és egységet teremtett a népcsoportok\n" +
                " között. 1686-ban XI. Ince pápa elrendelte, hogy az egész katolikus világ\n" +
                " emlékezzen meg róla, majd 1771-ben Mária Terézia országos ünneppé tette.\n" +
                " Az 1848–49-es forradalom és szabadságharc leverése után egy időre\n" +
                " szünetelt az ünneplés, de 1891-ben Ferenc József munkaszüneti nappá\n" +
                " nyilvánította. A két világháború között a nemzeti egység\n" +
                " szimbólumává vált. 1950-től az új államrendszer ideológiája szerint\n" +
                " alakították át az ünnep jelentését, majd 1991-ben ismét állami\n" +
                " ünneppé nyilvánították."));
        feastDays.add(new FeastDay(2033, 9, 23, "1956-os Forradalom", "Magyarország nemzeti ünnepe, amely az 1956-os forradalom és szabadságharc\n" +
                " eseményeire emlékezik. Az ünnep hivatalosan 1989 október 23.-án vált\n" +
                " nemzeti ünneppé a rendszerváltás után, amikor a forradalom hőseit\n" +
                " ünnepelve tisztelegtek a szabadságért és a demokráciáért folytatott\n" +
                " küzdelmük előtt. Az 1956-os forradalom a szovjet megszállás elleni\n" +
                " népfelkelés volt, amely bár véresen végződött, alapvető szerepet\n" +
                " játszott Magyarország függetlenségi törekvéseiben és a későbbi\n" +
                " rendszerváltásban. Az október 23.-i események máig a szabadság és a\n" +
                " nemzeti szuverenitás fontos szimbólumai."));
        feastDays.add(new FeastDay(2033, 9, 29, "Óraátállítás", "Az őszi óraátállítás (a téli időszámítás kezdete) – (vasárnap),\n" +
                " 3:00 → 2:00\n" +
                " Ekkor kell 1 órát visszaállítani az órákat.\n" +
                " Az őszi óraátállítás célja, hogy reggel világosabb legyen, így a\n" +
                " természetes fényt jobban kihasználjuk. Magyarországon először 1916-ban\n" +
                " vezették be az óraátállítást energiatakarékossági okokból. Bár több\n" +
                " alkalommal szünetelt, végül 1980-ban újra bevezették, hogy csökkentsék a\n" +
                " villamosenergia-felhasználást. A téli időszámítás október utolsó\n" +
                " vasárnapján kezdődik, amikor az órákat visszaállítjuk. Ezáltal reggel\n" +
                " hamarabb világosodik, de este hamarabb sötétedik.  1996 óta\n" +
                " kormányrendelet szabályozza az óraátállítást.\n" +
                " "));
        feastDays.add(new FeastDay(2033, 10, 1, "Mindenszentek", "Mindenszentek ünnepe november 1.-én van, amikor a katolikus egyház\n" +
                " megemlékezik minden üdvözült lélekről, akik elnyerték a mennyországot,\n" +
                " de nevüket nem tartják számon a naptárban. Az ünnep eredete a\n" +
                " kereszténység előtti halotti kultuszokra is visszavezethető. Hivatalosan a\n" +
                " 8. században III. Gergely pápa idején vezették be, aki a Szent\n" +
                " Péter-bazilikában kápolnát szentelt minden szent tiszteletére.\n" +
                " Magyarországon munkaszüneti nap. Sokan virágokkal és gyertyagyújtással\n" +
                " tisztelegnek elhunyt szeretteik emléke előtt. Ez a nap a hit és a\n" +
                " megemlékezés ünnepe."));
        feastDays.add(new FeastDay(2033, 10, 2, "Halottak napja", "Halottak napja november 2.-án van, amikor a katolikus egyház megemlékezik az\n" +
                " elhunyt hívekről. Az ünnep célja, hogy imádságokkal és jó\n" +
                " cselekedetekkel segítsük a halottak lelki üdvét és tisztelegjünk\n" +
                " szeretteink emléke előtt. Bár gyökerei a kereszténység előtti halotti\n" +
                " kultuszokig nyúlnak vissza, mai formáját a 11. században Cluny-i Szent\n" +
                " Odiló apát vezette be. Ő rendelte el, hogy a kolostorok imádkozzanak a\n" +
                " tisztítótűzben szenvedő lelkekért. A gyertyagyújtás és a virágok\n" +
                " elhelyezése máig az emlékezés fontos része. "));
        feastDays.add(new FeastDay(2033, 11, 3, "Advent első napja", "Advent a karácsony előtti négyhetes időszak, mely Jézus Krisztus\n" +
                " születésére és második eljövetelére való várakozást jelenti. Az\n" +
                " advent származása a 4. századra nyúlik vissza, amikor bűnbánattal és\n" +
                " lelki felkészüléssel várták a karácsonyt. Kezdetben hosszabb böjti\n" +
                " időszak volt, de fokozatosan négyhetesre csökkent. Az adventi koszorú\n" +
                " hagyománya a 19. században terjedt el, az örökzöld növények a\n" +
                " halhatatlanságot és az újjászületést szimbolizálták. A négy gyertya a\n" +
                " hitet, a reményt, az örömöt és a szeretetet jelképezi. Minden vasárnap\n" +
                " egy újabb gyertya meggyújtásával közelebb kerülünk a karácsonyhoz. \n" +
                " Advent első vasárnapját a karácsonyt megelőző négy vasárnap egyikeként\n" +
                " ünneplik. Ez a nap általában november végére vagy december elejére esik. \n" +
                " "));
        feastDays.add(new FeastDay(2033, 11, 6, "Mikulás", " Mikuláskor december 5.-én éjszaka, vagy 6.-án reggel töltik meg a gyerekek\n" +
                " csizmáit édességgel és játékokkal. Szent Miklós püspök emlék napja. A\n" +
                " 4. században élt szent, aki a gyermekek, a hajósok és a szegények\n" +
                " védőszentje lett, híres volt jótékonyságáról. Egy legenda szerint\n" +
                " titokban segített egy szegény ember három lányán, akiket így férjhez\n" +
                " tudott adni.\n" +
                " Ebből a történetből született a hagyomány, hogy Szent Miklós éjjelente\n" +
                " ajándékokat hoz a jó gyerekeknek. Ma már világszerte ismert a piros\n" +
                " ruhás, fehér szakállú Mikulás.  A Mikulás-hagyományok országonként\n" +
                " eltérőek. Egyes helyeken a Mikulás hozza az ajándékokat, máshol a\n" +
                " Télapó. A lényeg azonban mindenhol ugyanaz: a szeretet az adakozás és a\n" +
                " gyermeki öröm ünnepe"));
        feastDays.add(new FeastDay(2033, 11, 13, "Luca napja", "Luca napja a keresztény és a népi hagyományok keveréke. Szent Luca a 3.\n" +
                " századi vértanú, a látás védőszentje, de a népi hitvilágban a\n" +
                " termékenység, a jólét és a védelem szimbóluma. A hagyomány szerint ezen\n" +
                " a napon kezdődött a tél leghosszabb éjszakája, így Luca napja a fény és\n" +
                " a sötétség határán áll. Ezen a napon a nők nem dolgoztak, hogy bőséges\n" +
                " legyen a következő év termése. Szokás volt Luca-széket készíteni,\n" +
                " búzát ültetni és jóslásokat végezni. Luca napja a megújulás és a\n" +
                " remény ünnepe, a fény győzelme a sötétség felett és a következő év\n" +
                " jólétének biztosítása.\n" +
                " "));
        feastDays.add(new FeastDay(2033, 11, 21, "Téli napforduló", "Az év legrövidebb napja nap. \n" +
                " Napkelte: \n" +
                " Napnyugta: \n" +
                " Világos időtartam: \n" +
                " A téli napforduló a legrövidebb nap az évben, amikor a Nap a\n" +
                " legalacsonyabbra emelkedik az égbolton. Ez a jelenség a Föld\n" +
                " tengelyferdesége és keringése miatt következik be. Az északi féltekén a\n" +
                " napforduló környékén a Nap csak 8 órán keresztül tartózkodik a horizont\n" +
                " felett, így ez a leghosszabb éjszaka. Ezen a napon kezdődik a nappali fény\n" +
                " fokozatos növekedése, így a következő hónapokban egyre hosszabbak lesznek\n" +
                " a nappalok."));
        feastDays.add(new FeastDay(2033, 11, 24, "Szenteste", "Szenteste a karácsonyi ünnepkör egyik legfontosabb napja, amely Jézus\n" +
                " Krisztus születésének előestéje. Bár Jézus pontos születési dátuma\n" +
                " nem ismert, a keresztény egyház a 4. században december 25.-ét jelölte meg\n" +
                " születésének napjaként, összekapcsolva azt a téli napfordulóval.\n" +
                " December 24.-én este a családok összegyűlnek, közösen vacsoráznak,\n" +
                " ajándékokat adnak egymásnak és karácsonyfát állítanak. Magyarországon\n" +
                " a hagyomány szerint a Jézuska hozza az ajándékokat, ezzel is megemlékezve\n" +
                " Jézus születésének csodájáról. A fenyőfa, amely a 16. századtól vált\n" +
                " a karácsony szimbólumává, az örök életet jelképezi. Az ajándékozás a\n" +
                " szeretet és a megbecsülés kifejezése, amely a három királyok\n" +
                " ajándékozására utal. Karácsony, bár elsősorban keresztény ünnep,\n" +
                " világszerte a szeretet, a béke és a család ünnepe lett."));
        feastDays.add(new FeastDay(2033, 11, 25, "Karácsony", "A keresztények számára Jézus Krisztus születésének ünnepe, melyet a 4.\n" +
                " század óta tartanak. Az ünnep a téli napfordulóhoz kapcsolódik,\n" +
                " szimbolizálva a fény győzelmét a sötétség felett. Karácsony a remény,\n" +
                " a szeretet és a megváltás ünnepe, amely a családi együttlét, az öröm\n" +
                " és a vallási szertartások ideje. Karácsonyfa, betlehem és csillag a\n" +
                " szimbólumok, amelyek Jézus születését, az örök életet és az\n" +
                " útmutatást képviselik. Az ünneplés világszerte eltérő, de mindegyik\n" +
                " kultúrában a szeretet és a béke fontosságát hangsúlyozza. Karácsony\n" +
                " lehetőséget ad az emberi kapcsolatok és az élet értelmének\n" +
                " átgondolására"));
        feastDays.add(new FeastDay(2033, 11, 26, "Karácsony másnapja", "A keresztény hagyományban Szent István, az első vértanú emléknapja,\n" +
                " azonban Magyarországon elsősorban Szent István királyra, az\n" +
                " államalapítóra emlékeznek. A 4. századi eredetű ünnep során Jézus\n" +
                " születése után következik Szent István napja, aki kulcsszerepet játszott\n" +
                " a kereszténység elterjesztésében Magyarországon. Bár Szent István a 11.\n" +
                " században élt, az egyház később tette december 26.-át emlékévé.\n" +
                " Magyarországon nemzeti hősként is tisztelik, mivel ő alapította az\n" +
                " államot. Ezen a napon az emberek szentmisére mennek, megemlékeznek\n" +
                " királyukról és különböző ünnepi rendezvényeken is részt vesznek"));
        feastDays.add(new FeastDay(2033, 11, 31, "Szilveszter", "A Szilveszter az év utolsó napja, amikor búcsút veszünk az óévtől és\n" +
                " ünnepeljük az új év kezdetét. Az ünnep neve Szent Szilveszter pápától\n" +
                " származik, aki 335 körül halt meg, de az újév ünneplésének hagyományai\n" +
                " sokkal régebbre nyúlnak vissza. Szilveszter éjszakáján családok és\n" +
                " barátok együtt ünnepelnek, vacsoráznak, tűzijátékot néznek és\n" +
                " jókívánságokat mondanak. A tűzijáték és a zajkeltés az ősi\n" +
                " hagyományokból ered. Az ünnep a megújulás, az összetartozás és a\n" +
                " remény kifejezője, miközben gazdaságilag is fontos szerepet játszik a\n" +
                " fogyasztói társadalomban.\n" +
                " \n" +
                " Boldog Új Évet kívánok! Legyen tele örömmel, egészséggel és sikerrel.\n" +
                " Hozzon az új év boldog pillanatokat, új lehetőségeket és boldog együtt\n" +
                " töltött időt!"));
        feastDays.add(new FeastDay(2034, 0, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma.\n" +
                " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az\n" +
                " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy\n" +
                " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és\n" +
                " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a\n" +
                " mondás. Legyen ez a nap a változás első lépése. Boldogságot,\n" +
                " egészséget és kitartást kívánok az új év minden pillanatára!\n"
        ));
        feastDays.add(new FeastDay(2034, 0, 6, "Vízkereszt", " A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a\n" +
                " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus\n" +
                " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus\n" +
                " megkeresztelkedésére és az első csodájára. A három királyok aranyat,\n" +
                " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a\n" +
                " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet\n" +
                " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi\n" +
                " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak\n" +
                " házszenteléseket, Isten áldását kérve az otthonra és az új évre."));
        feastDays.add(new FeastDay(2034, 1, 14, "Valentin nap", "A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint\n" +
                " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században\n" +
                " titokban esketett meg szerelmespárokat, mert II. Claudius császár\n" +
                " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák.\n" +
                " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az\n" +
                " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a\n" +
                " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal\n" +
                " és üdvözlőkártyákkal ünneplik."));
        feastDays.add(new FeastDay(2034, 2, 1, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja,\n" +
                " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a\n" +
                " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A\n" +
                " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és\n" +
                " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki\n" +
                " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának\n" +
                " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel,\n" +
                " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére\n" +
                " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A\n" +
                " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap\n" +
                " előtt tartanak. Általában február végére vagy március elejére esik."));
        feastDays.add(new FeastDay(2034, 2, 8, "Nőnap", " Az első Nőnapot 1908 március 8.-án tartották. A női munkások\n" +
                " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az\n" +
                " ünnep célja a nők társadalmi, politikai és gazdasági jogainak\n" +
                " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben\n" +
                " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így\n" +
                " 1911-ben már több európai országban is megünnepelték. Magyarországon\n" +
                " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már\n" +
                " a nők jogainak elismerésére és tiszteletére emelkedett."));
        feastDays.add(new FeastDay(2034, 2, 15, "1848-as Forradalom", "Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a\n" +
                " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti\n" +
                " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a\n" +
                " sajtószabadságért, a jobb életkörülményekért és a függetlenségért.\n" +
                " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című\n" +
                " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet\n" +
                " függetlenségi törekvése a magyar történelem egyik legfontosabb\n" +
                " eseményévé vált. Az események hatására megerősödött a nemzeti\n" +
                " identitás és ma már a magyar szabadság és összetartozás napjaként\n" +
                " ünnepeljük.\n" +
                " "));
        feastDays.add(new FeastDay(2034, 2, 26, "Óraátállítás", " A tavaszi óraátállítás a nyári időszámítás kezdete. \n" +
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
                " óraátállítást Magyarországon."));
        feastDays.add(new FeastDay(2034, 3, 1, "Bolondok napja", "Április elseje a bolondok napja, a bolondos tréfák és ártalmatlan\n" +
                " átverések napja. Az emberek apró csínyekkel, például hamis hírekkel vagy\n" +
                " kitalált történetekkel szórakoztatják egymást. Az internet\n" +
                " elterjedésével az álhírek és vírusos videók is népszerűvé váltak. A\n" +
                " nap célja, hogy jókedvet és nevetést hozzon, miközben erősíti a\n" +
                " közösségi szellemet. Eredete több elméletre vezethető vissza. A\n" +
                " legnépszerűbb magyarázat szerint a XVI. században, a Gergely-naptár\n" +
                " bevezetésekor azok, akik áprilisban ünnepelték az újévet,\n" +
                " Franciaországban bolondnak számítottak, így tréfák céltáblájává\n" +
                " váltak. Hasonló szokások figyelhetők meg a kelta tavaszi ünnepekben és az\n" +
                " ókori római Hilaria ünnepén is, ahol a bolondos viselkedés a tavaszi\n" +
                " megújulást jelezte. "));
        feastDays.add(new FeastDay(2034, 3, 9, "Virágvasárnap", " A Virágvasárnap a húsvét előtti vasárnap, Jézus jeruzsálemi\n" +
                " bevonulásának emlékére van. Az evangéliumok szerint Jézus szamárháton\n" +
                " vonult be a városba, miközben a hívek pálmaágakat lengettek, üdvözölve\n" +
                " őt, mint a Megváltót. A virágok, a pálmák és a zöld ágak használata\n" +
                " az ünnep jelképei, melyek a békét és az üdvözítőt hirdetik. A\n" +
                " keresztény hagyományban Virágvasárnap a nagyböjt végét és a húsvéti\n" +
                " ünnepekre való lelki felkészülést jelzi. Az ünnep hivatalosan a 4.\n" +
                " század óta van jelen az egyházi naptárban és azóta része a keresztény\n" +
                " hagyományoknak világszerte."));
        feastDays.add(new FeastDay(2034, 3, 14, "Nagypéntek", "A Nagypéntek a keresztény húsvéti ünnepkör legfontosabb napja, amikor\n" +
                " Jézus Krisztus kereszthalálát és szenvedését emlékezzük meg. Az ünnep\n" +
                " célja, hogy a hívők átgondolják a megváltásra tett áldozat\n" +
                " jelentőségét. E napot a 4. század óta ünneplik, amikor a keresztény\n" +
                " egyház hivatalosan is elismerte Jézus kereszthalálának fontosságát.\n" +
                " Magyarországon a 20. század elejétől munkaszüneti nap. A keresztény\n" +
                " hagyományokban sötét napként van jelen, amikor sokan böjtölnek és\n" +
                " csendes megemlékezéseket tartanak. Az egyházi szertartások mellett a\n" +
                " keresztény közösség számára Nagypéntek a bűnbánat és a lelki\n" +
                " megújulás napja is.  Nagypénteket a húsvét előtti pénteken ünneplik,\n" +
                " tehát ez a dátum évente változik. \n" +
                " "));
        feastDays.add(new FeastDay(2034, 3, 16, "Húsvétvasárnap", "Húsvétvasárnap a kereszténység legfontosabb ünnepe, Jézus Krisztus\n" +
                " feltámadásának napja, amely a halál feletti győzelmet és az örök élet\n" +
                " ígéretét hirdeti. Magyarországon 1991 óta munkaszüneti nap, de\n" +
                " jelentősége évszázadok óta meghatározó. Húsvét a 40 napos nagyböjt\n" +
                " végét jelzi, amely a bűnbánat és a lelki megújulás időszaka. A gyerekek\n" +
                " az ajándékaikat hagyományosan ezen a napon kapják meg, édességeket és\n" +
                " tojásokat, amelyeket a húsvéti nyúl hoz.  A húsvéti nyúl a 16. századi\n" +
                " Németországból ered és a termékenység, valamint az újjászületés\n" +
                " jelképe. Eredete pogány hagyományokhoz köthető, ahol a nyúl és a tojás\n" +
                " a tavaszi újjászületés szimbóluma volt, melyek később beépültek a\n" +
                " keresztény húsvéti szokásokba.\n" +
                " A Húsvétot évente változó dátumon ünneplik, mivel a tavaszi\n" +
                " napéjegyenlőség utáni első teliholdat követő vasárnapra esik."));
        feastDays.add(new FeastDay(2034, 3, 17, "Húsvéthétfő", "Húsvéthétfő a húsvéti ünnepkör egyik legfontosabb napja, amely Jézus\n" +
                " Krisztus feltámadását követően az új élet és a megújulás\n" +
                " szimbólumává vált. Magyarországon a húsvéti locsolás hagyománya a 17.\n" +
                " századtól terjedt el. A férfiak vízzel vagy illatos kölnivel locsolják\n" +
                " meg a nőket, kívánva nekik egészséget, boldogságot és termékenységet.\n" +
                " A locsolás eredete valószínűleg a megtisztulás és a tavaszi megújulás\n" +
                " ősi hiedelmeiből származik. A húsvéti nyúl a tavasz, az új élet és a\n" +
                " termékenység jelképe, míg a bárány Krisztus áldozatát, ártatlanságát\n" +
                " és a megváltást szimbolizálja. Magyarországon a húsvétot a\n" +
                " kereszténység elterjedése óta ünneplik és a népszokások a 19.\n" +
                " századtól erősödtek meg."));
        feastDays.add(new FeastDay(2034, 3, 22, "A Föld napja", "A Föld napja 1970-ben indult el, amikor az Egyesült Államokban Gaylord\n" +
                " Nelson szenátor kezdeményezésére több millió ember gyűlt össze, hogy\n" +
                " felhívja a figyelmet a környezetszennyezésre és a természeti erőforrások\n" +
                " védelmére. A nap célja, hogy globálisan tudatosítsa az emberekben a\n" +
                " környezetvédelem fontosságát és ösztönözze a fenntartható életmódra\n" +
                " való áttérést. Azóta minden évben április 22.-én világszerte\n" +
                " megemlékeznek róla. Számos esemény, akció és kampány hívja fel a\n" +
                " figyelmet a bolygó védelmére és a környezeti problémák megoldására.\n" +
                " \n" +
                " \n" +
                " "));
        feastDays.add(new FeastDay(2034, 3, 30, "Májusfa állítás", "A májusfa felállításának legjellemzőbb hagyomány szerinti időpontja a\n" +
                " május 1-jét megelőző nap délután/este volt. \n" +
                " Magyarországon a középkor óta ismert, tehát legalább a 14. századtól\n" +
                " ünneplik. A szokás az ősi tavaszi rituálékra épül, melyek a természet\n" +
                " megújulását és az élet körforgását szimbolizálták. A fiatal legények\n" +
                " a lányok háza elé állították a feldíszített fákat, hogy kifejezzék\n" +
                " szerelmi szándékukat. A májusfát szalagokkal, virágokkal díszítették\n" +
                " és május végén vagy pünkösdkor táncos mulatság keretében bontották\n" +
                " le, amit kitáncolásnak neveztek. A hagyomány a szerelem és a termékenység\n" +
                " ünnepe volt, később pedig összefonódott a munka ünnepével (május 1.).\n" +
                " Mára már kevésbé elterjedt szokás, de sok helyen igyekeznek még\n" +
                " fenntartani."));
        feastDays.add(new FeastDay(2034, 4, 1, "A Munka ünnepe", "Május 1.-e a munka ünnepe. A munkások jogaiért és szolidaritásáért\n" +
                " folytatott küzdelem emléknapja világszerte. Az ünnep gyökerei az 1886-os\n" +
                " chicagói tüntetésekhez nyúlnak vissza, ahol a munkások a nyolcórás\n" +
                " munkaidő bevezetéséért demonstráltak. Az események hatására 1890-től\n" +
                " május 1.-ét a munkások nemzetközi szolidaritási napjává\n" +
                " nyilvánították.  Magyarországon először 1890-ben tartották meg, de\n" +
                " hivatalos állami ünnep csak 1946-ban lett. A szocializmus idején\n" +
                " nagyszabású felvonulásokkal és tömegrendezvényekkel ünnepelték. A\n" +
                " rendszerváltás után a jelentése átalakult, de továbbra is a munka\n" +
                " tiszteletét és a munkavállalók jogainak fontosságát hirdeti. "));
        feastDays.add(new FeastDay(2034, 4, 7, "Anyák napja", "Az édesanyák napja a szeretet, a tisztelet és a hála ünnepe, amelyet\n" +
                " világszerte az édesanyák tiszteletére tartanak. Gyökerei az ókori\n" +
                " Görögországba nyúlnak vissza, ahol Rheát, az istenek anyját ünnepelték.\n" +
                " A modern anyák napja az Egyesült Államokból indult 1908-ban, amikor Anna\n" +
                " Jarvis kampányt indított édesanyja emlékére. Magyarországon először\n" +
                " 1925-ben ünnepelték és a májusi Mária-tisztelet hagyományaival is\n" +
                " összekapcsolódott. Ez a nap a gondoskodó és szerető édesanyák, valamint\n" +
                " minden női gondviselő iránti megbecsülés kifejezésére szolgál.\n" +
                " Virágokkal, ajándékokkal és figyelmességgel fejezik ki hálájukat a\n" +
                " gyerekek. Bár világszerte eltérő időpontokban ünneplik, mindenhol az\n" +
                " anyák iránti szeretet és tisztelet áll a középpontban. Anyák napját\n" +
                " Magyarországon minden év május első vasárnapján ünneplik. \n" +
                " "));
        feastDays.add(new FeastDay(2034, 4, 28, "Gyermeknap", "A gyermeknap a gyerekek jogainak és jólétének fontosságát hirdető\n" +
                " ünnep, melyet világszerte különböző időpontokban tartanak. Eredete\n" +
                " 1925-re nyúlik vissza, amikor a genfi Gyermekjóléti Konferencián\n" +
                " elhatározták, hogy kiemelik a gyermekek védelmének fontosságát.\n" +
                " Magyarországon 1931-ben tartották meg először, majd 1950 óta május\n" +
                " utolsó vasárnapján ünneplik. Ez a nap a gyerekekről szól, akik ilyenkor\n" +
                " különféle programokon vehetnek részt, ahol játékokkal és szórakoztató\n" +
                " rendezvényekkel ünneplik őket. Az ünnep célja, hogy felhívja a figyelmet\n" +
                " a gyermekek jogaira és boldogságuk fontosságára. Magyarországon a\n" +
                " Gyermeknapot minden évben május utolsó vasárnapján ünneplik."));
        feastDays.add(new FeastDay(2034, 5, 4, "Pünkösd", "Pünkösd a kereszténység egyik legősibb ünnepe, melyet már a 4.\n" +
                " századtól kezdve ünnepelnek a keresztény egyházban. Az ünnep Jézus\n" +
                " mennybemenetele után ötven nappal emlékezik meg a Szentlélek\n" +
                " eljöveteléről, amely erőt adott az apostoloknak a keresztény hit\n" +
                " hirdetéséhez. Ez az esemény az egyház születésnapjaként is ismert. A\n" +
                " „pünkösd” elnevezés a görög „pentékoszté” szóból ered, ami\n" +
                " ötvenediket jelent. Magyarországon a középkor óta tartják számon és\n" +
                " számos népi hagyomány kapcsolódik hozzá, például a pünkösdi király\n" +
                " és királyné választása. Pünkösd vasárnapját a húsvét utáni 50.\n" +
                " napon, tehát 50 nappal húsvétvasárnap után ünneplik. "));
        feastDays.add(new FeastDay(2034, 5, 5, "Pünkösdhétfő", "A Pünkösdhétfő a keresztény egyház egyik legfontosabb ünnepének, a\n" +
                " pünkösdnek a második napja. Pünkösd a húsvét utáni 50. napon van, és a\n" +
                " Szentlélek eljövetelét ünnepli, amikor Jézus tanítványai erőt és\n" +
                " útmutatást kaptak küldetésükhöz. Az esemény a keresztény egyház\n" +
                " születésnapjának is tekinthető. Az ünnep gyökerei a zsidó pünkösdi\n" +
                " ünnepre (Sávuót) vezethetők vissza, amely az aratási hálaadás napja\n" +
                " volt. Magyarországon pünkösdhétfő munkaszüneti nap és hivatalosan 1993\n" +
                " óta ünnepeljük újra. Az ünnep a megújulást és a közösség\n" +
                " összetartozását jelképezi."));
        feastDays.add(new FeastDay(2034, 5, 18, "Apák napja", "Apák napja világszerte az édesapák tiszteletére szolgál, elismerve a\n" +
                " családban és a gyermeknevelésben betöltött szerepüket. Az ünnep eredete\n" +
                " az Egyesült Államokhoz köthető, ahol először 1910-ben tartották meg\n" +
                " Sonora Smart Dodd kezdeményezésére , aki édesapját akarta tiszteletben\n" +
                " részesíteni . Magyarországon az apák napja az 1990-es években kezdett\n" +
                " elterjedni  és 1994 óta ünnepeljük minden év június harmadik\n" +
                " vasárnapján. Célja, hogy elismerje az apák és az apaszerepet betöltő\n" +
                " férfiak szeretetét, áldozatvállalását és a családban betöltött\n" +
                " szerepét. Ez a nap lehetőséget ad arra, hogy köszönetet mondjunk az apák\n" +
                " szeretetéért, támogatásáért és áldozatos munkájáért, amelyet\n" +
                " családjukért tesznek. "));
        feastDays.add(new FeastDay(2034, 5, 20, "Nyári napforduló", "Az év leghosszabb napja. \n" +
                " Napkelte: \n" +
                " Napnyugta: \n" +
                " Világos időtartam: \n" +
                " A nyári napforduló a leghosszabb nap az évben, amikor a Nap a legmagasabbra\n" +
                " emelkedik az égbolton. Ez a jelenség a Föld tengelyferdesége és a\n" +
                " keringése miatt következik be. Az északi féltekén a napforduló\n" +
                " környékén a Nap több mint 15 órán keresztül a horizont felett\n" +
                " tartózkodik. Ezen a napon a Föld az északi féltekén éri el a leghosszabb\n" +
                " nappali órákat, így ez a legrövidebb éjszaka."));
        feastDays.add(new FeastDay(2034, 7, 20, "Szent István-nap", "Szent István király koronázása körülbelül 1000-ben történt, ezzel\n" +
                " megalapozva a keresztény Magyarországot. 1083 augusztus 20.-án szentté\n" +
                " avatták a Szent Péter-bazilikában. Az Árpád-korban az István-kultusz az\n" +
                " uralkodó dinasztiát támogatta és egységet teremtett a népcsoportok\n" +
                " között. 1686-ban XI. Ince pápa elrendelte, hogy az egész katolikus világ\n" +
                " emlékezzen meg róla, majd 1771-ben Mária Terézia országos ünneppé tette.\n" +
                " Az 1848–49-es forradalom és szabadságharc leverése után egy időre\n" +
                " szünetelt az ünneplés, de 1891-ben Ferenc József munkaszüneti nappá\n" +
                " nyilvánította. A két világháború között a nemzeti egység\n" +
                " szimbólumává vált. 1950-től az új államrendszer ideológiája szerint\n" +
                " alakították át az ünnep jelentését, majd 1991-ben ismét állami\n" +
                " ünneppé nyilvánították."));
        feastDays.add(new FeastDay(2034, 9, 23, "1956-os Forradalom", "Magyarország nemzeti ünnepe, amely az 1956-os forradalom és szabadságharc\n" +
                " eseményeire emlékezik. Az ünnep hivatalosan 1989 október 23.-án vált\n" +
                " nemzeti ünneppé a rendszerváltás után, amikor a forradalom hőseit\n" +
                " ünnepelve tisztelegtek a szabadságért és a demokráciáért folytatott\n" +
                " küzdelmük előtt. Az 1956-os forradalom a szovjet megszállás elleni\n" +
                " népfelkelés volt, amely bár véresen végződött, alapvető szerepet\n" +
                " játszott Magyarország függetlenségi törekvéseiben és a későbbi\n" +
                " rendszerváltásban. Az október 23.-i események máig a szabadság és a\n" +
                " nemzeti szuverenitás fontos szimbólumai."));
        feastDays.add(new FeastDay(2034, 9, 29, "Óraátállítás", "Az őszi óraátállítás (a téli időszámítás kezdete) – (vasárnap),\n" +
                " 3:00 → 2:00\n" +
                " Ekkor kell 1 órát visszaállítani az órákat.\n" +
                " Az őszi óraátállítás célja, hogy reggel világosabb legyen, így a\n" +
                " természetes fényt jobban kihasználjuk. Magyarországon először 1916-ban\n" +
                " vezették be az óraátállítást energiatakarékossági okokból. Bár több\n" +
                " alkalommal szünetelt, végül 1980-ban újra bevezették, hogy csökkentsék a\n" +
                " villamosenergia-felhasználást. A téli időszámítás október utolsó\n" +
                " vasárnapján kezdődik, amikor az órákat visszaállítjuk. Ezáltal reggel\n" +
                " hamarabb világosodik, de este hamarabb sötétedik.  1996 óta\n" +
                " kormányrendelet szabályozza az óraátállítást.\n" +
                " "));
        feastDays.add(new FeastDay(2034, 10, 1, "Mindenszentek", "Mindenszentek ünnepe november 1.-én van, amikor a katolikus egyház\n" +
                " megemlékezik minden üdvözült lélekről, akik elnyerték a mennyországot,\n" +
                " de nevüket nem tartják számon a naptárban. Az ünnep eredete a\n" +
                " kereszténység előtti halotti kultuszokra is visszavezethető. Hivatalosan a\n" +
                " 8. században III. Gergely pápa idején vezették be, aki a Szent\n" +
                " Péter-bazilikában kápolnát szentelt minden szent tiszteletére.\n" +
                " Magyarországon munkaszüneti nap. Sokan virágokkal és gyertyagyújtással\n" +
                " tisztelegnek elhunyt szeretteik emléke előtt. Ez a nap a hit és a\n" +
                " megemlékezés ünnepe."));
        feastDays.add(new FeastDay(2034, 10, 2, "Halottak napja", "Halottak napja november 2.-án van, amikor a katolikus egyház megemlékezik az\n" +
                " elhunyt hívekről. Az ünnep célja, hogy imádságokkal és jó\n" +
                " cselekedetekkel segítsük a halottak lelki üdvét és tisztelegjünk\n" +
                " szeretteink emléke előtt. Bár gyökerei a kereszténység előtti halotti\n" +
                " kultuszokig nyúlnak vissza, mai formáját a 11. században Cluny-i Szent\n" +
                " Odiló apát vezette be. Ő rendelte el, hogy a kolostorok imádkozzanak a\n" +
                " tisztítótűzben szenvedő lelkekért. A gyertyagyújtás és a virágok\n" +
                " elhelyezése máig az emlékezés fontos része. "));
        feastDays.add(new FeastDay(2034, 11, 3, "Advent első napja", "Advent a karácsony előtti négyhetes időszak, mely Jézus Krisztus\n" +
                " születésére és második eljövetelére való várakozást jelenti. Az\n" +
                " advent származása a 4. századra nyúlik vissza, amikor bűnbánattal és\n" +
                " lelki felkészüléssel várták a karácsonyt. Kezdetben hosszabb böjti\n" +
                " időszak volt, de fokozatosan négyhetesre csökkent. Az adventi koszorú\n" +
                " hagyománya a 19. században terjedt el, az örökzöld növények a\n" +
                " halhatatlanságot és az újjászületést szimbolizálták. A négy gyertya a\n" +
                " hitet, a reményt, az örömöt és a szeretetet jelképezi. Minden vasárnap\n" +
                " egy újabb gyertya meggyújtásával közelebb kerülünk a karácsonyhoz. \n" +
                " Advent első vasárnapját a karácsonyt megelőző négy vasárnap egyikeként\n" +
                " ünneplik. Ez a nap általában november végére vagy december elejére esik. \n" +
                " "));
        feastDays.add(new FeastDay(2034, 11, 6, "Mikulás", " Mikuláskor december 5.-én éjszaka, vagy 6.-án reggel töltik meg a gyerekek\n" +
                " csizmáit édességgel és játékokkal. Szent Miklós püspök emlék napja. A\n" +
                " 4. században élt szent, aki a gyermekek, a hajósok és a szegények\n" +
                " védőszentje lett, híres volt jótékonyságáról. Egy legenda szerint\n" +
                " titokban segített egy szegény ember három lányán, akiket így férjhez\n" +
                " tudott adni.\n" +
                " Ebből a történetből született a hagyomány, hogy Szent Miklós éjjelente\n" +
                " ajándékokat hoz a jó gyerekeknek. Ma már világszerte ismert a piros\n" +
                " ruhás, fehér szakállú Mikulás.  A Mikulás-hagyományok országonként\n" +
                " eltérőek. Egyes helyeken a Mikulás hozza az ajándékokat, máshol a\n" +
                " Télapó. A lényeg azonban mindenhol ugyanaz: a szeretet az adakozás és a\n" +
                " gyermeki öröm ünnepe"));
        feastDays.add(new FeastDay(2034, 11, 13, "Luca napja", "Luca napja a keresztény és a népi hagyományok keveréke. Szent Luca a 3.\n" +
                " századi vértanú, a látás védőszentje, de a népi hitvilágban a\n" +
                " termékenység, a jólét és a védelem szimbóluma. A hagyomány szerint ezen\n" +
                " a napon kezdődött a tél leghosszabb éjszakája, így Luca napja a fény és\n" +
                " a sötétség határán áll. Ezen a napon a nők nem dolgoztak, hogy bőséges\n" +
                " legyen a következő év termése. Szokás volt Luca-széket készíteni,\n" +
                " búzát ültetni és jóslásokat végezni. Luca napja a megújulás és a\n" +
                " remény ünnepe, a fény győzelme a sötétség felett és a következő év\n" +
                " jólétének biztosítása.\n" +
                " "));
        feastDays.add(new FeastDay(2034, 11, 21, "Téli napforduló", "Az év legrövidebb napja nap. \n" +
                " Napkelte: \n" +
                " Napnyugta: \n" +
                " Világos időtartam: \n" +
                " A téli napforduló a legrövidebb nap az évben, amikor a Nap a\n" +
                " legalacsonyabbra emelkedik az égbolton. Ez a jelenség a Föld\n" +
                " tengelyferdesége és keringése miatt következik be. Az északi féltekén a\n" +
                " napforduló környékén a Nap csak 8 órán keresztül tartózkodik a horizont\n" +
                " felett, így ez a leghosszabb éjszaka. Ezen a napon kezdődik a nappali fény\n" +
                " fokozatos növekedése, így a következő hónapokban egyre hosszabbak lesznek\n" +
                " a nappalok."));
        feastDays.add(new FeastDay(2034, 11, 24, "Szenteste", "Szenteste a karácsonyi ünnepkör egyik legfontosabb napja, amely Jézus\n" +
                " Krisztus születésének előestéje. Bár Jézus pontos születési dátuma\n" +
                " nem ismert, a keresztény egyház a 4. században december 25.-ét jelölte meg\n" +
                " születésének napjaként, összekapcsolva azt a téli napfordulóval.\n" +
                " December 24.-én este a családok összegyűlnek, közösen vacsoráznak,\n" +
                " ajándékokat adnak egymásnak és karácsonyfát állítanak. Magyarországon\n" +
                " a hagyomány szerint a Jézuska hozza az ajándékokat, ezzel is megemlékezve\n" +
                " Jézus születésének csodájáról. A fenyőfa, amely a 16. századtól vált\n" +
                " a karácsony szimbólumává, az örök életet jelképezi. Az ajándékozás a\n" +
                " szeretet és a megbecsülés kifejezése, amely a három királyok\n" +
                " ajándékozására utal. Karácsony, bár elsősorban keresztény ünnep,\n" +
                " világszerte a szeretet, a béke és a család ünnepe lett."));
        feastDays.add(new FeastDay(2034, 11, 25, "Karácsony", "A keresztények számára Jézus Krisztus születésének ünnepe, melyet a 4.\n" +
                " század óta tartanak. Az ünnep a téli napfordulóhoz kapcsolódik,\n" +
                " szimbolizálva a fény győzelmét a sötétség felett. Karácsony a remény,\n" +
                " a szeretet és a megváltás ünnepe, amely a családi együttlét, az öröm\n" +
                " és a vallási szertartások ideje. Karácsonyfa, betlehem és csillag a\n" +
                " szimbólumok, amelyek Jézus születését, az örök életet és az\n" +
                " útmutatást képviselik. Az ünneplés világszerte eltérő, de mindegyik\n" +
                " kultúrában a szeretet és a béke fontosságát hangsúlyozza. Karácsony\n" +
                " lehetőséget ad az emberi kapcsolatok és az élet értelmének\n" +
                " átgondolására"));
        feastDays.add(new FeastDay(2034, 11, 26, "Karácsony másnapja", "A keresztény hagyományban Szent István, az első vértanú emléknapja,\n" +
                " azonban Magyarországon elsősorban Szent István királyra, az\n" +
                " államalapítóra emlékeznek. A 4. századi eredetű ünnep során Jézus\n" +
                " születése után következik Szent István napja, aki kulcsszerepet játszott\n" +
                " a kereszténység elterjesztésében Magyarországon. Bár Szent István a 11.\n" +
                " században élt, az egyház később tette december 26.-át emlékévé.\n" +
                " Magyarországon nemzeti hősként is tisztelik, mivel ő alapította az\n" +
                " államot. Ezen a napon az emberek szentmisére mennek, megemlékeznek\n" +
                " királyukról és különböző ünnepi rendezvényeken is részt vesznek"));
        feastDays.add(new FeastDay(2034, 11, 31, "Szilveszter", "A Szilveszter az év utolsó napja, amikor búcsút veszünk az óévtől és\n" +
                " ünnepeljük az új év kezdetét. Az ünnep neve Szent Szilveszter pápától\n" +
                " származik, aki 335 körül halt meg, de az újév ünneplésének hagyományai\n" +
                " sokkal régebbre nyúlnak vissza. Szilveszter éjszakáján családok és\n" +
                " barátok együtt ünnepelnek, vacsoráznak, tűzijátékot néznek és\n" +
                " jókívánságokat mondanak. A tűzijáték és a zajkeltés az ősi\n" +
                " hagyományokból ered. Az ünnep a megújulás, az összetartozás és a\n" +
                " remény kifejezője, miközben gazdaságilag is fontos szerepet játszik a\n" +
                " fogyasztói társadalomban.\n" +
                " \n" +
                " Boldog Új Évet kívánok! Legyen tele örömmel, egészséggel és sikerrel.\n" +
                " Hozzon az új év boldog pillanatokat, új lehetőségeket és boldog együtt\n" +
                " töltött időt!"));
        feastDays.add(new FeastDay(2035, 0, 1, "Újév", "Az év első napja a megújulás, az újrakezdés és a remény szimbóluma.\n" +
                " Ilyenkor sokan fogadalmakat tesznek, hogy jobbá és tartalmasabbá tegyék az\n" +
                " elkövetkező esztendőt. Kezdd az évet tiszta lappal és jusson eszedbe, hogy\n" +
                " minden nap egy új lehetőség arra, hogy közelebb kerülj céljaidhoz és\n" +
                " álmaidhoz. \"A hosszú út is egyetlen lépéssel kezdődik\" – tartja a\n" +
                " mondás. Legyen ez a nap a változás első lépése. Boldogságot,\n" +
                " egészséget és kitartást kívánok az új év minden pillanatára!\n"
        ));
        feastDays.add(new FeastDay(2035, 0, 6, "Vízkereszt", " A vízkereszt a karácsonyi ünnepkör lezárása, ekkor bontják le a\n" +
                " karácsonyfát, jelezve az ünnepi időszak végét. Az ünnep Jézus\n" +
                " megjelenésére emlékeztet: a napkeleti bölcsek látogatására, Jézus\n" +
                " megkeresztelkedésére és az első csodájára. A három királyok aranyat,\n" +
                " tömjént és mirhát hoztak ajándékul, hogy tiszteletüket fejezzék ki a\n" +
                " Megváltó előtt. A Vízkereszt a tisztulás, a megújulás és az új kezdet\n" +
                " szimbóluma, amely a megkeresztelkedés szentségéhez kapcsolódik. Egyházi\n" +
                " ünnepként a 4. század óta létezik. Sok helyen ekkor tartanak\n" +
                " házszenteléseket, Isten áldását kérve az otthonra és az új évre."));
        feastDays.add(new FeastDay(2035, 1, 14, "Valentin nap", "A Valentin-nap a szerelem és a romantika ünnepe, amely Szent Bálint\n" +
                " (Valentin) római paphoz kötődik. A legenda szerint Valentin a 3. században\n" +
                " titokban esketett meg szerelmespárokat, mert II. Claudius császár\n" +
                " megtiltotta a házasságot, mondván, hogy a nőtlen férfiak jobb katonák.\n" +
                " Valentin emiatt mártírhalált halt és a szeretet szimbólumává vált. Az\n" +
                " ünnepet hivatalosan 496-ban vezette be I. Geláz pápa. Ma világszerte a\n" +
                " szerelmesek megajándékozásáról szól, szív alakú díszekkel, virágokkal\n" +
                " és üdvözlőkártyákkal ünneplik."));
        feastDays.add(new FeastDay(2035, 2, 1, "Hamvazószerda", "A Hamvazószerda a nagyböjt kezdete és a húsvéti ünnepkör első napja,\n" +
                " amely a 7. század óta létezik. A hívők ezen a napon hamuval jelölik meg a\n" +
                " homlokukat, emlékeztetve bűnösségükre és az élet mulandóságára. A\n" +
                " szertartás a Teremtés könyvének szavait idézi: „porból vétettél, és\n" +
                " porrá leszel” (Ter 3:19). A hamu a bűnbánat, a vezeklés és a lelki\n" +
                " megújulás szimbóluma, melyet a korábbi év virágvasárnapi barkájának\n" +
                " elégetésével készítenek. A nagyböjt 40 napja alatt a hívők böjttel,\n" +
                " imával és jótékonykodással készülnek húsvétra, Krisztus szenvedésére\n" +
                " és feltámadására emlékezve, alázatos életet élve Isten előtt.  A\n" +
                " Hamvazószerda a húsvéti böjt kezdete, amit 46 nappal húsvétvasárnap\n" +
                " előtt tartanak. Általában február végére vagy március elejére esik."));
        feastDays.add(new FeastDay(2035, 2, 8, "Nőnap", " Az első Nőnapot 1908 március 8.-án tartották. A női munkások\n" +
                " demonstráltak a jobb munkakörülményekért és az egyenlő jogokért. Az\n" +
                " ünnep célja a nők társadalmi, politikai és gazdasági jogainak\n" +
                " előmozdítása, valamint a nemek közti egyenlőség hangsúlyozása. 1910-ben\n" +
                " Clara Zetkin javasolta, hogy nemzetközi szinten is tartsanak nőnapot, így\n" +
                " 1911-ben már több európai országban is megünnepelték. Magyarországon\n" +
                " 1913 óta hivatalos ünnep. Eredetileg szocialista gyökerei voltak, mára már\n" +
                " a nők jogainak elismerésére és tiszteletére emelkedett."));
        feastDays.add(new FeastDay(2035, 2, 15, "1848-as Forradalom", "Március 15.-e Magyarország egyik legfontosabb nemzeti ünnepe, amely a\n" +
                " 1848-as forradalom és szabadságharc emlékére van. Ezen a napon a pesti\n" +
                " forradalmi események nyomán, a fiatalok és a polgárok kiálltak a\n" +
                " sajtószabadságért, a jobb életkörülményekért és a függetlenségért.\n" +
                " A forradalom központi alakja Petőfi Sándor volt, akinek \"Nemzeti dal\" című\n" +
                " verse a megmozdulás jelképe lett. A 12 pont elfogadása és a nemzet\n" +
                " függetlenségi törekvése a magyar történelem egyik legfontosabb\n" +
                " eseményévé vált. Az események hatására megerősödött a nemzeti\n" +
                " identitás és ma már a magyar szabadság és összetartozás napjaként\n" +
                " ünnepeljük.\n" +
                " "));
        feastDays.add(new FeastDay(2035, 2, 26, "Óraátállítás", " A tavaszi óraátállítás a nyári időszámítás kezdete. \n" +
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
                " óraátállítást Magyarországon."));
        feastDays.add(new FeastDay(2035, 3, 1, "Bolondok napja", "Április elseje a bolondok napja, a bolondos tréfák és ártalmatlan\n" +
                " átverések napja. Az emberek apró csínyekkel, például hamis hírekkel vagy\n" +
                " kitalált történetekkel szórakoztatják egymást. Az internet\n" +
                " elterjedésével az álhírek és vírusos videók is népszerűvé váltak. A\n" +
                " nap célja, hogy jókedvet és nevetést hozzon, miközben erősíti a\n" +
                " közösségi szellemet. Eredete több elméletre vezethető vissza. A\n" +
                " legnépszerűbb magyarázat szerint a XVI. században, a Gergely-naptár\n" +
                " bevezetésekor azok, akik áprilisban ünnepelték az újévet,\n" +
                " Franciaországban bolondnak számítottak, így tréfák céltáblájává\n" +
                " váltak. Hasonló szokások figyelhetők meg a kelta tavaszi ünnepekben és az\n" +
                " ókori római Hilaria ünnepén is, ahol a bolondos viselkedés a tavaszi\n" +
                " megújulást jelezte. "));
        feastDays.add(new FeastDay(2035, 3, 9, "Virágvasárnap", " A Virágvasárnap a húsvét előtti vasárnap, Jézus jeruzsálemi\n" +
                " bevonulásának emlékére van. Az evangéliumok szerint Jézus szamárháton\n" +
                " vonult be a városba, miközben a hívek pálmaágakat lengettek, üdvözölve\n" +
                " őt, mint a Megváltót. A virágok, a pálmák és a zöld ágak használata\n" +
                " az ünnep jelképei, melyek a békét és az üdvözítőt hirdetik. A\n" +
                " keresztény hagyományban Virágvasárnap a nagyböjt végét és a húsvéti\n" +
                " ünnepekre való lelki felkészülést jelzi. Az ünnep hivatalosan a 4.\n" +
                " század óta van jelen az egyházi naptárban és azóta része a keresztény\n" +
                " hagyományoknak világszerte."));
        feastDays.add(new FeastDay(2035, 3, 14, "Nagypéntek", "A Nagypéntek a keresztény húsvéti ünnepkör legfontosabb napja, amikor\n" +
                " Jézus Krisztus kereszthalálát és szenvedését emlékezzük meg. Az ünnep\n" +
                " célja, hogy a hívők átgondolják a megváltásra tett áldozat\n" +
                " jelentőségét. E napot a 4. század óta ünneplik, amikor a keresztény\n" +
                " egyház hivatalosan is elismerte Jézus kereszthalálának fontosságát.\n" +
                " Magyarországon a 20. század elejétől munkaszüneti nap. A keresztény\n" +
                " hagyományokban sötét napként van jelen, amikor sokan böjtölnek és\n" +
                " csendes megemlékezéseket tartanak. Az egyházi szertartások mellett a\n" +
                " keresztény közösség számára Nagypéntek a bűnbánat és a lelki\n" +
                " megújulás napja is.  Nagypénteket a húsvét előtti pénteken ünneplik,\n" +
                " tehát ez a dátum évente változik. \n" +
                " "));
        feastDays.add(new FeastDay(2035, 3, 16, "Húsvétvasárnap", "Húsvétvasárnap a kereszténység legfontosabb ünnepe, Jézus Krisztus\n" +
                " feltámadásának napja, amely a halál feletti győzelmet és az örök élet\n" +
                " ígéretét hirdeti. Magyarországon 1991 óta munkaszüneti nap, de\n" +
                " jelentősége évszázadok óta meghatározó. Húsvét a 40 napos nagyböjt\n" +
                " végét jelzi, amely a bűnbánat és a lelki megújulás időszaka. A gyerekek\n" +
                " az ajándékaikat hagyományosan ezen a napon kapják meg, édességeket és\n" +
                " tojásokat, amelyeket a húsvéti nyúl hoz.  A húsvéti nyúl a 16. századi\n" +
                " Németországból ered és a termékenység, valamint az újjászületés\n" +
                " jelképe. Eredete pogány hagyományokhoz köthető, ahol a nyúl és a tojás\n" +
                " a tavaszi újjászületés szimbóluma volt, melyek később beépültek a\n" +
                " keresztény húsvéti szokásokba.\n" +
                " A Húsvétot évente változó dátumon ünneplik, mivel a tavaszi\n" +
                " napéjegyenlőség utáni első teliholdat követő vasárnapra esik."));
        feastDays.add(new FeastDay(2035, 3, 17, "Húsvéthétfő", "Húsvéthétfő a húsvéti ünnepkör egyik legfontosabb napja, amely Jézus\n" +
                " Krisztus feltámadását követően az új élet és a megújulás\n" +
                " szimbólumává vált. Magyarországon a húsvéti locsolás hagyománya a 17.\n" +
                " századtól terjedt el. A férfiak vízzel vagy illatos kölnivel locsolják\n" +
                " meg a nőket, kívánva nekik egészséget, boldogságot és termékenységet.\n" +
                " A locsolás eredete valószínűleg a megtisztulás és a tavaszi megújulás\n" +
                " ősi hiedelmeiből származik. A húsvéti nyúl a tavasz, az új élet és a\n" +
                " termékenység jelképe, míg a bárány Krisztus áldozatát, ártatlanságát\n" +
                " és a megváltást szimbolizálja. Magyarországon a húsvétot a\n" +
                " kereszténység elterjedése óta ünneplik és a népszokások a 19.\n" +
                " századtól erősödtek meg."));
        feastDays.add(new FeastDay(2035, 3, 22, "A Föld napja", "A Föld napja 1970-ben indult el, amikor az Egyesült Államokban Gaylord\n" +
                " Nelson szenátor kezdeményezésére több millió ember gyűlt össze, hogy\n" +
                " felhívja a figyelmet a környezetszennyezésre és a természeti erőforrások\n" +
                " védelmére. A nap célja, hogy globálisan tudatosítsa az emberekben a\n" +
                " környezetvédelem fontosságát és ösztönözze a fenntartható életmódra\n" +
                " való áttérést. Azóta minden évben április 22.-én világszerte\n" +
                " megemlékeznek róla. Számos esemény, akció és kampány hívja fel a\n" +
                " figyelmet a bolygó védelmére és a környezeti problémák megoldására.\n" +
                " \n" +
                " \n" +
                " "));
        feastDays.add(new FeastDay(2035, 3, 30, "Májusfa állítás", "A májusfa felállításának legjellemzőbb hagyomány szerinti időpontja a\n" +
                " május 1-jét megelőző nap délután/este volt. \n" +
                " Magyarországon a középkor óta ismert, tehát legalább a 14. századtól\n" +
                " ünneplik. A szokás az ősi tavaszi rituálékra épül, melyek a természet\n" +
                " megújulását és az élet körforgását szimbolizálták. A fiatal legények\n" +
                " a lányok háza elé állították a feldíszített fákat, hogy kifejezzék\n" +
                " szerelmi szándékukat. A májusfát szalagokkal, virágokkal díszítették\n" +
                " és május végén vagy pünkösdkor táncos mulatság keretében bontották\n" +
                " le, amit kitáncolásnak neveztek. A hagyomány a szerelem és a termékenység\n" +
                " ünnepe volt, később pedig összefonódott a munka ünnepével (május 1.).\n" +
                " Mára már kevésbé elterjedt szokás, de sok helyen igyekeznek még\n" +
                " fenntartani."));
        feastDays.add(new FeastDay(2035, 4, 1, "A Munka ünnepe", "Május 1.-e a munka ünnepe. A munkások jogaiért és szolidaritásáért\n" +
                " folytatott küzdelem emléknapja világszerte. Az ünnep gyökerei az 1886-os\n" +
                " chicagói tüntetésekhez nyúlnak vissza, ahol a munkások a nyolcórás\n" +
                " munkaidő bevezetéséért demonstráltak. Az események hatására 1890-től\n" +
                " május 1.-ét a munkások nemzetközi szolidaritási napjává\n" +
                " nyilvánították.  Magyarországon először 1890-ben tartották meg, de\n" +
                " hivatalos állami ünnep csak 1946-ban lett. A szocializmus idején\n" +
                " nagyszabású felvonulásokkal és tömegrendezvényekkel ünnepelték. A\n" +
                " rendszerváltás után a jelentése átalakult, de továbbra is a munka\n" +
                " tiszteletét és a munkavállalók jogainak fontosságát hirdeti. "));
        feastDays.add(new FeastDay(2035, 4, 7, "Anyák napja", "Az édesanyák napja a szeretet, a tisztelet és a hála ünnepe, amelyet\n" +
                " világszerte az édesanyák tiszteletére tartanak. Gyökerei az ókori\n" +
                " Görögországba nyúlnak vissza, ahol Rheát, az istenek anyját ünnepelték.\n" +
                " A modern anyák napja az Egyesült Államokból indult 1908-ban, amikor Anna\n" +
                " Jarvis kampányt indított édesanyja emlékére. Magyarországon először\n" +
                " 1925-ben ünnepelték és a májusi Mária-tisztelet hagyományaival is\n" +
                " összekapcsolódott. Ez a nap a gondoskodó és szerető édesanyák, valamint\n" +
                " minden női gondviselő iránti megbecsülés kifejezésére szolgál.\n" +
                " Virágokkal, ajándékokkal és figyelmességgel fejezik ki hálájukat a\n" +
                " gyerekek. Bár világszerte eltérő időpontokban ünneplik, mindenhol az\n" +
                " anyák iránti szeretet és tisztelet áll a középpontban. Anyák napját\n" +
                " Magyarországon minden év május első vasárnapján ünneplik. \n" +
                " "));
        feastDays.add(new FeastDay(2035, 4, 28, "Gyermeknap", "A gyermeknap a gyerekek jogainak és jólétének fontosságát hirdető\n" +
                " ünnep, melyet világszerte különböző időpontokban tartanak. Eredete\n" +
                " 1925-re nyúlik vissza, amikor a genfi Gyermekjóléti Konferencián\n" +
                " elhatározták, hogy kiemelik a gyermekek védelmének fontosságát.\n" +
                " Magyarországon 1931-ben tartották meg először, majd 1950 óta május\n" +
                " utolsó vasárnapján ünneplik. Ez a nap a gyerekekről szól, akik ilyenkor\n" +
                " különféle programokon vehetnek részt, ahol játékokkal és szórakoztató\n" +
                " rendezvényekkel ünneplik őket. Az ünnep célja, hogy felhívja a figyelmet\n" +
                " a gyermekek jogaira és boldogságuk fontosságára. Magyarországon a\n" +
                " Gyermeknapot minden évben május utolsó vasárnapján ünneplik."));
        feastDays.add(new FeastDay(2035, 5, 4, "Pünkösd", "Pünkösd a kereszténység egyik legősibb ünnepe, melyet már a 4.\n" +
                " századtól kezdve ünnepelnek a keresztény egyházban. Az ünnep Jézus\n" +
                " mennybemenetele után ötven nappal emlékezik meg a Szentlélek\n" +
                " eljöveteléről, amely erőt adott az apostoloknak a keresztény hit\n" +
                " hirdetéséhez. Ez az esemény az egyház születésnapjaként is ismert. A\n" +
                " „pünkösd” elnevezés a görög „pentékoszté” szóból ered, ami\n" +
                " ötvenediket jelent. Magyarországon a középkor óta tartják számon és\n" +
                " számos népi hagyomány kapcsolódik hozzá, például a pünkösdi király\n" +
                " és királyné választása. Pünkösd vasárnapját a húsvét utáni 50.\n" +
                " napon, tehát 50 nappal húsvétvasárnap után ünneplik. "));
        feastDays.add(new FeastDay(2035, 5, 5, "Pünkösdhétfő", "A Pünkösdhétfő a keresztény egyház egyik legfontosabb ünnepének, a\n" +
                " pünkösdnek a második napja. Pünkösd a húsvét utáni 50. napon van, és a\n" +
                " Szentlélek eljövetelét ünnepli, amikor Jézus tanítványai erőt és\n" +
                " útmutatást kaptak küldetésükhöz. Az esemény a keresztény egyház\n" +
                " születésnapjának is tekinthető. Az ünnep gyökerei a zsidó pünkösdi\n" +
                " ünnepre (Sávuót) vezethetők vissza, amely az aratási hálaadás napja\n" +
                " volt. Magyarországon pünkösdhétfő munkaszüneti nap és hivatalosan 1993\n" +
                " óta ünnepeljük újra. Az ünnep a megújulást és a közösség\n" +
                " összetartozását jelképezi."));
        feastDays.add(new FeastDay(2035, 5, 18, "Apák napja", "Apák napja világszerte az édesapák tiszteletére szolgál, elismerve a\n" +
                " családban és a gyermeknevelésben betöltött szerepüket. Az ünnep eredete\n" +
                " az Egyesült Államokhoz köthető, ahol először 1910-ben tartották meg\n" +
                " Sonora Smart Dodd kezdeményezésére , aki édesapját akarta tiszteletben\n" +
                " részesíteni . Magyarországon az apák napja az 1990-es években kezdett\n" +
                " elterjedni  és 1994 óta ünnepeljük minden év június harmadik\n" +
                " vasárnapján. Célja, hogy elismerje az apák és az apaszerepet betöltő\n" +
                " férfiak szeretetét, áldozatvállalását és a családban betöltött\n" +
                " szerepét. Ez a nap lehetőséget ad arra, hogy köszönetet mondjunk az apák\n" +
                " szeretetéért, támogatásáért és áldozatos munkájáért, amelyet\n" +
                " családjukért tesznek. "));
        feastDays.add(new FeastDay(2035, 5, 20, "Nyári napforduló", "Az év leghosszabb napja. \n" +
                " Napkelte: \n" +
                " Napnyugta: \n" +
                " Világos időtartam: \n" +
                " A nyári napforduló a leghosszabb nap az évben, amikor a Nap a legmagasabbra\n" +
                " emelkedik az égbolton. Ez a jelenség a Föld tengelyferdesége és a\n" +
                " keringése miatt következik be. Az északi féltekén a napforduló\n" +
                " környékén a Nap több mint 15 órán keresztül a horizont felett\n" +
                " tartózkodik. Ezen a napon a Föld az északi féltekén éri el a leghosszabb\n" +
                " nappali órákat, így ez a legrövidebb éjszaka."));
        feastDays.add(new FeastDay(2035, 7, 20, "Szent István-nap", "Szent István király koronázása körülbelül 1000-ben történt, ezzel\n" +
                " megalapozva a keresztény Magyarországot. 1083 augusztus 20.-án szentté\n" +
                " avatták a Szent Péter-bazilikában. Az Árpád-korban az István-kultusz az\n" +
                " uralkodó dinasztiát támogatta és egységet teremtett a népcsoportok\n" +
                " között. 1686-ban XI. Ince pápa elrendelte, hogy az egész katolikus világ\n" +
                " emlékezzen meg róla, majd 1771-ben Mária Terézia országos ünneppé tette.\n" +
                " Az 1848–49-es forradalom és szabadságharc leverése után egy időre\n" +
                " szünetelt az ünneplés, de 1891-ben Ferenc József munkaszüneti nappá\n" +
                " nyilvánította. A két világháború között a nemzeti egység\n" +
                " szimbólumává vált. 1950-től az új államrendszer ideológiája szerint\n" +
                " alakították át az ünnep jelentését, majd 1991-ben ismét állami\n" +
                " ünneppé nyilvánították."));
        feastDays.add(new FeastDay(2035, 9, 23, "1956-os Forradalom", "Magyarország nemzeti ünnepe, amely az 1956-os forradalom és szabadságharc\n" +
                " eseményeire emlékezik. Az ünnep hivatalosan 1989 október 23.-án vált\n" +
                " nemzeti ünneppé a rendszerváltás után, amikor a forradalom hőseit\n" +
                " ünnepelve tisztelegtek a szabadságért és a demokráciáért folytatott\n" +
                " küzdelmük előtt. Az 1956-os forradalom a szovjet megszállás elleni\n" +
                " népfelkelés volt, amely bár véresen végződött, alapvető szerepet\n" +
                " játszott Magyarország függetlenségi törekvéseiben és a későbbi\n" +
                " rendszerváltásban. Az október 23.-i események máig a szabadság és a\n" +
                " nemzeti szuverenitás fontos szimbólumai."));
        feastDays.add(new FeastDay(2035, 9, 29, "Óraátállítás", "Az őszi óraátállítás (a téli időszámítás kezdete) – (vasárnap),\n" +
                " 3:00 → 2:00\n" +
                " Ekkor kell 1 órát visszaállítani az órákat.\n" +
                " Az őszi óraátállítás célja, hogy reggel világosabb legyen, így a\n" +
                " természetes fényt jobban kihasználjuk. Magyarországon először 1916-ban\n" +
                " vezették be az óraátállítást energiatakarékossági okokból. Bár több\n" +
                " alkalommal szünetelt, végül 1980-ban újra bevezették, hogy csökkentsék a\n" +
                " villamosenergia-felhasználást. A téli időszámítás október utolsó\n" +
                " vasárnapján kezdődik, amikor az órákat visszaállítjuk. Ezáltal reggel\n" +
                " hamarabb világosodik, de este hamarabb sötétedik.  1996 óta\n" +
                " kormányrendelet szabályozza az óraátállítást.\n" +
                " "));
        feastDays.add(new FeastDay(2035, 10, 1, "Mindenszentek", "Mindenszentek ünnepe november 1.-én van, amikor a katolikus egyház\n" +
                " megemlékezik minden üdvözült lélekről, akik elnyerték a mennyországot,\n" +
                " de nevüket nem tartják számon a naptárban. Az ünnep eredete a\n" +
                " kereszténység előtti halotti kultuszokra is visszavezethető. Hivatalosan a\n" +
                " 8. században III. Gergely pápa idején vezették be, aki a Szent\n" +
                " Péter-bazilikában kápolnát szentelt minden szent tiszteletére.\n" +
                " Magyarországon munkaszüneti nap. Sokan virágokkal és gyertyagyújtással\n" +
                " tisztelegnek elhunyt szeretteik emléke előtt. Ez a nap a hit és a\n" +
                " megemlékezés ünnepe."));
        feastDays.add(new FeastDay(2035, 10, 2, "Halottak napja", "Halottak napja november 2.-án van, amikor a katolikus egyház megemlékezik az\n" +
                " elhunyt hívekről. Az ünnep célja, hogy imádságokkal és jó\n" +
                " cselekedetekkel segítsük a halottak lelki üdvét és tisztelegjünk\n" +
                " szeretteink emléke előtt. Bár gyökerei a kereszténység előtti halotti\n" +
                " kultuszokig nyúlnak vissza, mai formáját a 11. században Cluny-i Szent\n" +
                " Odiló apát vezette be. Ő rendelte el, hogy a kolostorok imádkozzanak a\n" +
                " tisztítótűzben szenvedő lelkekért. A gyertyagyújtás és a virágok\n" +
                " elhelyezése máig az emlékezés fontos része. "));
        feastDays.add(new FeastDay(2035, 11, 3, "Advent első napja", "Advent a karácsony előtti négyhetes időszak, mely Jézus Krisztus\n" +
                " születésére és második eljövetelére való várakozást jelenti. Az\n" +
                " advent származása a 4. századra nyúlik vissza, amikor bűnbánattal és\n" +
                " lelki felkészüléssel várták a karácsonyt. Kezdetben hosszabb böjti\n" +
                " időszak volt, de fokozatosan négyhetesre csökkent. Az adventi koszorú\n" +
                " hagyománya a 19. században terjedt el, az örökzöld növények a\n" +
                " halhatatlanságot és az újjászületést szimbolizálták. A négy gyertya a\n" +
                " hitet, a reményt, az örömöt és a szeretetet jelképezi. Minden vasárnap\n" +
                " egy újabb gyertya meggyújtásával közelebb kerülünk a karácsonyhoz. \n" +
                " Advent első vasárnapját a karácsonyt megelőző négy vasárnap egyikeként\n" +
                " ünneplik. Ez a nap általában november végére vagy december elejére esik. \n" +
                " "));
        feastDays.add(new FeastDay(2035, 11, 6, "Mikulás", " Mikuláskor december 5.-én éjszaka, vagy 6.-án reggel töltik meg a gyerekek\n" +
                " csizmáit édességgel és játékokkal. Szent Miklós püspök emlék napja. A\n" +
                " 4. században élt szent, aki a gyermekek, a hajósok és a szegények\n" +
                " védőszentje lett, híres volt jótékonyságáról. Egy legenda szerint\n" +
                " titokban segített egy szegény ember három lányán, akiket így férjhez\n" +
                " tudott adni.\n" +
                " Ebből a történetből született a hagyomány, hogy Szent Miklós éjjelente\n" +
                " ajándékokat hoz a jó gyerekeknek. Ma már világszerte ismert a piros\n" +
                " ruhás, fehér szakállú Mikulás.  A Mikulás-hagyományok országonként\n" +
                " eltérőek. Egyes helyeken a Mikulás hozza az ajándékokat, máshol a\n" +
                " Télapó. A lényeg azonban mindenhol ugyanaz: a szeretet az adakozás és a\n" +
                " gyermeki öröm ünnepe"));
        feastDays.add(new FeastDay(2035, 11, 13, "Luca napja", "Luca napja a keresztény és a népi hagyományok keveréke. Szent Luca a 3.\n" +
                " századi vértanú, a látás védőszentje, de a népi hitvilágban a\n" +
                " termékenység, a jólét és a védelem szimbóluma. A hagyomány szerint ezen\n" +
                " a napon kezdődött a tél leghosszabb éjszakája, így Luca napja a fény és\n" +
                " a sötétség határán áll. Ezen a napon a nők nem dolgoztak, hogy bőséges\n" +
                " legyen a következő év termése. Szokás volt Luca-széket készíteni,\n" +
                " búzát ültetni és jóslásokat végezni. Luca napja a megújulás és a\n" +
                " remény ünnepe, a fény győzelme a sötétség felett és a következő év\n" +
                " jólétének biztosítása.\n" +
                " "));
        feastDays.add(new FeastDay(2035, 11, 21, "Téli napforduló", "Az év legrövidebb napja nap. \n" +
                " Napkelte: \n" +
                " Napnyugta: \n" +
                " Világos időtartam: \n" +
                " A téli napforduló a legrövidebb nap az évben, amikor a Nap a\n" +
                " legalacsonyabbra emelkedik az égbolton. Ez a jelenség a Föld\n" +
                " tengelyferdesége és keringése miatt következik be. Az északi féltekén a\n" +
                " napforduló környékén a Nap csak 8 órán keresztül tartózkodik a horizont\n" +
                " felett, így ez a leghosszabb éjszaka. Ezen a napon kezdődik a nappali fény\n" +
                " fokozatos növekedése, így a következő hónapokban egyre hosszabbak lesznek\n" +
                " a nappalok."));
        feastDays.add(new FeastDay(2035, 11, 24, "Szenteste", "Szenteste a karácsonyi ünnepkör egyik legfontosabb napja, amely Jézus\n" +
                " Krisztus születésének előestéje. Bár Jézus pontos születési dátuma\n" +
                " nem ismert, a keresztény egyház a 4. században december 25.-ét jelölte meg\n" +
                " születésének napjaként, összekapcsolva azt a téli napfordulóval.\n" +
                " December 24.-én este a családok összegyűlnek, közösen vacsoráznak,\n" +
                " ajándékokat adnak egymásnak és karácsonyfát állítanak. Magyarországon\n" +
                " a hagyomány szerint a Jézuska hozza az ajándékokat, ezzel is megemlékezve\n" +
                " Jézus születésének csodájáról. A fenyőfa, amely a 16. századtól vált\n" +
                " a karácsony szimbólumává, az örök életet jelképezi. Az ajándékozás a\n" +
                " szeretet és a megbecsülés kifejezése, amely a három királyok\n" +
                " ajándékozására utal. Karácsony, bár elsősorban keresztény ünnep,\n" +
                " világszerte a szeretet, a béke és a család ünnepe lett."));
        feastDays.add(new FeastDay(2035, 11, 25, "Karácsony", "A keresztények számára Jézus Krisztus születésének ünnepe, melyet a 4.\n" +
                " század óta tartanak. Az ünnep a téli napfordulóhoz kapcsolódik,\n" +
                " szimbolizálva a fény győzelmét a sötétség felett. Karácsony a remény,\n" +
                " a szeretet és a megváltás ünnepe, amely a családi együttlét, az öröm\n" +
                " és a vallási szertartások ideje. Karácsonyfa, betlehem és csillag a\n" +
                " szimbólumok, amelyek Jézus születését, az örök életet és az\n" +
                " útmutatást képviselik. Az ünneplés világszerte eltérő, de mindegyik\n" +
                " kultúrában a szeretet és a béke fontosságát hangsúlyozza. Karácsony\n" +
                " lehetőséget ad az emberi kapcsolatok és az élet értelmének\n" +
                " átgondolására"));
        feastDays.add(new FeastDay(2035, 11, 26, "Karácsony másnapja", "A keresztény hagyományban Szent István, az első vértanú emléknapja,\n" +
                " azonban Magyarországon elsősorban Szent István királyra, az\n" +
                " államalapítóra emlékeznek. A 4. századi eredetű ünnep során Jézus\n" +
                " születése után következik Szent István napja, aki kulcsszerepet játszott\n" +
                " a kereszténység elterjesztésében Magyarországon. Bár Szent István a 11.\n" +
                " században élt, az egyház később tette december 26.-át emlékévé.\n" +
                " Magyarországon nemzeti hősként is tisztelik, mivel ő alapította az\n" +
                " államot. Ezen a napon az emberek szentmisére mennek, megemlékeznek\n" +
                " királyukról és különböző ünnepi rendezvényeken is részt vesznek"));
        feastDays.add(new FeastDay(2035, 11, 31, "Szilveszter", "A Szilveszter az év utolsó napja, amikor búcsút veszünk az óévtől és\n" +
                " ünnepeljük az új év kezdetét. Az ünnep neve Szent Szilveszter pápától\n" +
                " származik, aki 335 körül halt meg, de az újév ünneplésének hagyományai\n" +
                " sokkal régebbre nyúlnak vissza. Szilveszter éjszakáján családok és\n" +
                " barátok együtt ünnepelnek, vacsoráznak, tűzijátékot néznek és\n" +
                " jókívánságokat mondanak. A tűzijáték és a zajkeltés az ősi\n" +
                " hagyományokból ered. Az ünnep a megújulás, az összetartozás és a\n" +
                " remény kifejezője, miközben gazdaságilag is fontos szerepet játszik a\n" +
                " fogyasztói társadalomban.\n" +
                " \n" +
                " Boldog Új Évet kívánok! Legyen tele örömmel, egészséggel és sikerrel.\n" +
                " Hozzon az új év boldog pillanatokat, új lehetőségeket és boldog együtt\n" +
                " töltött időt!"));

        feastDays.addAll(dbHelper.getAllEvents());

        // Empty cells before first actual day
        calendar.set(Calendar.YEAR, currentYear);
        calendar.set(Calendar.MONTH, currentMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

// First day of current month

// ✅ Get Previous Month
        Calendar prevMonth = (Calendar) calendar.clone();
        prevMonth.add(Calendar.MONTH, -1);

// ✅ Get total days in previous month
        int daysInPrevMonth = prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < firstDayOfWeek; i++) {
            int prevDay = (daysInPrevMonth - firstDayOfWeek + 1) + i;
            DayItem prevItem = new DayItem(-2, false, false, null);
            items.add(prevItem);
        }

// Current month days
        for (int day = 1; day <= daysInMonth; day++) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
            int dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7;
            boolean isWeekend = (dayOfWeek == 5 || dayOfWeek == 6);

            String feastLabel = null;
            String feastStory = null;
            boolean isFromDB = false;

            for (FeastDay fd : feastDays) {
                if (fd.year == currentYear && fd.month == currentMonth && fd.day == day) {
                    feastLabel = fd.name;
                    feastStory = fd.story;
                    break;
                }
            }

            DayItem item = new DayItem(day, isWeekend, feastLabel != null, feastLabel);
            item.feastStory = feastStory;
            items.add(item);
        }

// Fill next month cells
        int totalCells = items.size() % 7;
        if (totalCells != 0) {
            int extraDays = 7 - totalCells;
            for (int i = 1; i <= extraDays; i++) {
                DayItem nextItem = new DayItem(-2, false, false, null);
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
        DayItem item = items.get(position);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.calendar_cell);


        // Reset visibility
        rv.setViewVisibility(R.id.feastLabel, View.INVISIBLE);

        // 1️⃣ HEADER ROW (Day names)
        if (item.day == -1 && item.feastLabel != null) {
            rv.setTextViewText(R.id.dayText, item.feastLabel);
            rv.setTextColor(R.id.dayText,
                    (item.feastLabel.equals("Szo") || item.feastLabel.equals("V")) ? Color.RED : Color.WHITE);
            rv.setTextViewTextSize(R.id.dayText, TypedValue.COMPLEX_UNIT_SP, 13);

            // 2️⃣ EMPTY CELL (Previous/Next month)
        } else if (item.day == -2) {
            rv.setTextColor(R.id.dayText, Color.parseColor("#666666")); // Dim color
            rv.setViewVisibility(R.id.feastLabel, View.INVISIBLE);

            // 3️⃣ NORMAL DAY CELL
        } else {
            rv.setTextViewText(R.id.dayText, String.valueOf(item.day));
            rv.setTextColor(R.id.dayText, item.isWeekend ? Color.RED : Color.WHITE);

            // Feast or Reminder indicator
            if (item.isFeastDay) {
                rv.setViewVisibility(R.id.feastLabel, View.VISIBLE);
                rv.setTextViewText(R.id.feastLabel, "Feast Day");

                // If event is from DB → Reminder color, else Feast color
                rv.setInt(R.id.feastLabel, "setBackgroundColor", context.getColor(R.color.app_color));
            }

        }

        // Click intent
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("clicked_day", item.day);
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
}
