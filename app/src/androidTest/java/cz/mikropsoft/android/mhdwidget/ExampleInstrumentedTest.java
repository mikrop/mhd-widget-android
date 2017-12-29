package cz.mikropsoft.android.mhdwidget;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Collections;

import cz.mikropsoft.android.mhdwidget.databases.MhdDatabase;
import cz.mikropsoft.android.mhdwidget.databases.ZastavkaDao;
import cz.mikropsoft.android.mhdwidget.model.Prostredek;
import cz.mikropsoft.android.mhdwidget.model.Zastavka;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    Context context;
    MhdDatabase database;
    ZastavkaDao zastavkaDao;

    /**
     * Init. {@link Zastavka}.
     *
     * @return neuložená zastávka
     */
    private static Zastavka createZastavka() {
        Zastavka zastavka = new Zastavka();
        zastavka.setId(1);
        zastavka.setLinka("XYZ");
        zastavka.setProstredek(Prostredek.AUTOBUS);
        zastavka.setSmer("Sem tam");
        zastavka.setJmeno("Testovací");
        return zastavka;
    }

    @Before
    public void setUp() throws IOException {
        this.context = InstrumentationRegistry.getTargetContext().getApplicationContext();
        this.database = Room.inMemoryDatabaseBuilder(context, MhdDatabase.class).build();
        this.zastavkaDao = database.zastavkaDao();
    }

    @After
    public void tearDown() throws IOException {
        database.close();
    }

    @Test
    public void crud() {
        assertEquals("cz.mikropsoft.android.mhdwidget", context.getPackageName());
        assertEquals(0, zastavkaDao.getAll().size());

        Zastavka zastavka = createZastavka();
        assertNotNull(zastavka.getId());
        zastavkaDao.insert(zastavka);

        zastavka.setFavorite(true);
        zastavkaDao.update(zastavka);
        assertTrue(zastavka.isFavorite());

        zastavkaDao.delete(zastavka);
        assertEquals(0, zastavkaDao.getAll().size());
    }
}
