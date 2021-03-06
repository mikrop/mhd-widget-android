package cz.mikropsoft.android.mhdwidget.databases;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import cz.mikropsoft.android.mhdwidget.model.Spoj;

@Dao
public interface SpojDao {

    @Query("SELECT * FROM spoj")
    List<Spoj> getAll();

    /**
     * Vrací spoj předaného {@link Spoj#id}
     *
     * @param id primární klíč
     * @return nalezený spoj
     */
    @Query("SELECT * FROM spoj WHERE id = :id LIMIT 1")
    Spoj finOne(int id);

    @Query("SELECT * FROM spoj WHERE zastavka_id = :zastavkaId")
    List<Spoj> findByZastavkaId(int zastavkaId);

    /**
     * Počet uložených spojů, k předané zastávce.
     *
     * @param zastavkaId ID zastávky
     * @return počet spojů
     */
    @Query("SELECT count(zastavka_id) FROM spoj WHERE zastavka_id = :zastavkaId")
    int countByZastavkaId(int zastavkaId);

    /**
     * Vrací první spoj z předané zastávky.
     *
     * @param zastavkaId ID zastávky
     * @return první {@link Spoj}
     */
    @Query("SELECT * FROM spoj s WHERE zastavka_id = :zastavkaId AND s.odjezd =" +
            " (SELECT MIN(odjezd) FROM spoj WHERE zastavka_id = s.zastavka_id) LIMIT 1")
    Spoj findFirstByZastavkaId(int zastavkaId);

    /**
     * Aktuální {@link Spoj}, na předané zastávce.
     *
     * @param zastavkaId ID zastávky, pro kterou aktuální spoj hledáme
     * @param now aktuání timestamp
     * @return nalezený {@link Spoj}
     */
    @Query("SELECT * FROM spoj WHERE zastavka_id = :zastavkaId AND odjezd >= :now LIMIT 1")
    Spoj findAktualniByZastavkaId(int zastavkaId, long now);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Iterable<Spoj> spoje);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Spoj spoj);

    @Delete
    void delete(Iterable<Spoj> spoje);

}
