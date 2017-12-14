package cz.mikropsoft.android.mhdwidget.databases;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import cz.mikropsoft.android.mhdwidget.model.Zastavka;

@Dao
public interface ZastavkaDao {

    @Query("SELECT * FROM zastavka")
    List<Zastavka> getAll();

    /**
     * Vrací zastávku předaného {@link Zastavka#id}
     *
     * @param id primární klíč
     * @return nalezená zastávka
     */
    @Query("SELECT * FROM zastavka WHERE id = :id LIMIT 1")
    Zastavka finOne(int id);

    @Insert
    void insertAll(List<Zastavka> zastavky);

    @Update
    void update(Zastavka zastavka);

    @Delete
    void delete(Zastavka zastavka);

}
