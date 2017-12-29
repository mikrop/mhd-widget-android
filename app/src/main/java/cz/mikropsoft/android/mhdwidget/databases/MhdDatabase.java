package cz.mikropsoft.android.mhdwidget.databases;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import org.joda.time.LocalTime;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import cz.mikropsoft.android.mhdwidget.MainActivity;
import cz.mikropsoft.android.mhdwidget.model.AktualniSpoj;
import cz.mikropsoft.android.mhdwidget.model.Spoj;
import cz.mikropsoft.android.mhdwidget.model.Zastavka;

@Database(entities = {Zastavka.class, Spoj.class}, version = 1, exportSchema = false)
public abstract class MhdDatabase extends RoomDatabase {

    private static final String TAG = MainActivity.class.getName();
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
     * Změní příznak preferovaná zastávka,
     *
     * @param context
     * @param zastavkaId ID zastávky, která bude příznak modifikován
     * @param favorite {@code true} pokud má být {@link Zastavka} preferovaná, jinak {@code false}
     * @return aktualizovaná zastávka
     */
    public static Zastavka setFavorite(Context context, int zastavkaId, boolean favorite) {
        Assert.assertNotNull("ID zastávky nebylo předáno", zastavkaId);
        ZastavkaDao zastavkaDao = getInstance(context).zastavkaDao();
        Zastavka zastavka = zastavkaDao.finOne(zastavkaId);
        zastavka.setFavorite(favorite);
        zastavkaDao.update(zastavka);
        return zastavka;
    }

    /**
     * Aktualizuje databázi zastávek se zacháním příznaku {@link Zastavka#favorite} na oblíbených
     * zastávkách.
     *
     * @param context
     * @param nove kolekce zastávek, které bude aktualozovány do aplikace
     * @return aktualizovaný seznam
     */
    public static List<Zastavka> zastavkyUpdate(Context context, List<Zastavka> nove) {
        ZastavkaDao zastavkaDao = getInstance(context).zastavkaDao();
        List<Zastavka> ulozene = zastavkaDao.getAll();

        for (Zastavka nova : new ArrayList<>(nove)) {
            for (Zastavka ulozena : new ArrayList<>(ulozene)) {
                if (Zastavka.JMENOSMER_COMPARATOR.compare(ulozena, nova) == 0 && ulozena.isFavorite()) {
                    nove.remove(nova);
                    ulozene.remove(ulozena);
                }
            }
        }

        zastavkaDao.deleteAll(ulozene); // NEoblíbené zastávky smažeme
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
            SpojDao spojDao = getInstance(context).spojDao();
            Spoj spoj = spojDao.findAktualniByZastavkaId(zastavkaId, now);

            if (spoj == null) { // Dnes již nic nejede, vracím první zítřejší spoj
                spoj = spojDao.findFirstByZastavkaId(zastavkaId);
            }
            Assert.assertNotNull("Aktuální spoj ze zastávky ID " + zastavkaId + " nenalezen", spoj);

            Log.d(TAG, "Aktuální spoj ID " + spoj.getId() + " ze zastávky "
                    + zastavka.getJmeno() + " ve směru " + zastavka.getSmer());

            return new AktualniSpoj(zastavka, spoj);
        }

        Log.d(TAG, "Nepodařilo se načíst aktuální spoj ze zastávky " + zastavka.getJmeno() + " spoje nebyly dosud staženy");
        return null;
    }

}
