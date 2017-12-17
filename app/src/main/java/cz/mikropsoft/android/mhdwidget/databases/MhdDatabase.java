package cz.mikropsoft.android.mhdwidget.databases;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import org.joda.time.LocalTime;
import org.junit.Assert;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cz.mikropsoft.android.mhdwidget.MainActivity;
import cz.mikropsoft.android.mhdwidget.model.AktualniSpoj;
import cz.mikropsoft.android.mhdwidget.model.Spoj;
import cz.mikropsoft.android.mhdwidget.model.Zastavka;

@Database(entities = {Zastavka.class, Spoj.class}, version = 1, exportSchema = false)
public abstract class MhdDatabase extends RoomDatabase {

    private static final String TAG = MainActivity.class.getName();
    private static final Comparator<Zastavka> JMENOSMER_COMPARATOR = new Comparator<Zastavka>() {
        @Override
        public int compare(Zastavka o1, Zastavka o2) {
            int compare = o1.getJmeno().compareTo(o2.getJmeno());
            return ((compare == 0) ? o1.getSmer().compareTo(o2.getSmer()) : compare);
        }
    };
    private static MhdDatabase INSTANCE;

    public abstract ZastavkaDao zastavkaDao();
    public abstract SpojDao spojDao();

    /**
     * Vrací instanci na databázi.
     *
     * @param context
     * @return {@link MhdDatabase}
     */
    public static MhdDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MhdDatabase.class, "MhdDatabase")
//                    .addMigrations(MhdWidgetDatabase.MIGRATION_1_2)
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

//    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//
//        }
//    };

    /**
     * Vrací {@code true}, pokud existuje alespoň jeden {@link Spoj} pro predan0 ID zastávky.
     *
     * @param context
     * @param zastavkaId ID zastávky
     * @return {@code true}, pokud spoj existuje, jinak {@code false}
     */
    public static boolean isSpojEmpty(Context context, int zastavkaId) {
        Assert.assertNotNull("ID zastávky nebylo předáno", zastavkaId);
        SpojDao spojDao = getInstance(context).spojDao();
        return spojDao.countByZastavkaId(zastavkaId) < 1;
    }

    /**
     * Vrací příznak, zda se jedná o oblíbenou {@link Zastavka}.
     *
     * @param favorites oblíbené zastávky
     * @param zastavka kontrolovaná zastávky
     * @return {@code true} pokud je předaná zastávky, mezi oblíbenými, jinak {@code false}
     */
    private static boolean isZastavkaInFavorites(List<Zastavka> favorites, Zastavka zastavka) {
        int index = Collections.binarySearch(favorites, zastavka, JMENOSMER_COMPARATOR);
        boolean favorite = index > -1;
        if (favorite)
            Log.i(TAG, "Oblíbenou zastávkou je " + zastavka.getJmeno() + " ve směru " + zastavka.getSmer());
        return favorite;
    }

    /**
     * Aktualizuje databázi zastávek se zacháním příznaku {@link Zastavka#favorite} na oblíbených
     * zastávkách.
     *
     * @param context
     * @param nove zastávky k aktualizaci
     * @return aktualizovaný seznam
     */
    public static List<Zastavka> zastavkyUpdate(Context context, List<Zastavka> nove) {
        ZastavkaDao zastavkaDao = getInstance(context).zastavkaDao();
        List<Zastavka> favorites = zastavkaDao.findFavorites();
        for (Zastavka zastavka : nove) {
            zastavka.setFavorite(isZastavkaInFavorites(favorites, zastavka));
        }
        zastavkaDao.deleteAll();
        zastavkaDao.insertAll(nove);
        return zastavkaDao.getAll();
    }

    /**
     * Aktuální spoj.
     *
     * @param context
     * @param zastavkaId ID zastávky
     * @return aktuální {@link Spoj}, nebo {@code null} pokud pro nebyl spoj nalezen
     */
    @Nullable
    public static AktualniSpoj getAktualniSpoj(Context context, int zastavkaId) {
        Assert.assertNotNull("ID zastávky nebylo předáno", zastavkaId);

        Zastavka zastavka = getInstance(context).zastavkaDao().finOne(zastavkaId);
        Assert.assertNotNull("Zastávka ID: " + zastavkaId + " nebyla nalezena", zastavka);

        if (!isSpojEmpty(context, zastavkaId)) {
            long now = LocalTime.now().toDateTimeToday().getMillis();
            Spoj spoj = getInstance(context).spojDao().findAktualniByZastavkaId(zastavkaId, now);
            Assert.assertNotNull("Aktuální spoj ze zastávky ID " + zastavkaId + " nenalezen", spoj);

            Log.d(TAG, "Aktuální spoj ID " + spoj.getId() + " ze zastávky "
                    + zastavka.getJmeno() + " ve směru " + zastavka.getSmer());

            return new AktualniSpoj(zastavka, spoj);
        }

        Log.d(TAG, "Nepodařilo se načíst aktuální spoj ze zastávky " + zastavka.getJmeno() + " spoje nebyly dosud staženy");
        return null;
    }

}
