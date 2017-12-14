package cz.mikropsoft.android.mhdwidget.databases;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.joda.time.LocalTime;

import java.util.Date;
import java.util.List;

import cz.mikropsoft.android.mhdwidget.model.Spoj;
import cz.mikropsoft.android.mhdwidget.model.Zastavka;

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
     * Aktuální {@link Spoj}, na předané zastávce.
     *
     * @param zastavkaId ID zastávky, pro kterou aktuální spoj hledáme
     * @param now aktuání timestamp
     * @return nalezený {@link Spoj}
     */
    @Query("SELECT * FROM spoj WHERE zastavka_id = :zastavkaId AND odjezd >= :now LIMIT 1")
    Spoj findAktualniByZastavkaId(int zastavkaId, long now);

    @Insert
    void insertAll(List<Spoj> spoje);

    @Update
    void update(Spoj spoj);

    @Delete
    void delete(Spoj spoj);

}
