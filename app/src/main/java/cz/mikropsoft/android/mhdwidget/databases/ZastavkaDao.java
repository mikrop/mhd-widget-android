package cz.mikropsoft.android.mhdwidget.databases;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
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

    /**
     * Vrací zastávky s příznakem {@code Zastavka#favorite = 1}
     *
     * @return oblíbené zastávky
     */
    @Query("SELECT * FROM zastavka WHERE favorite = 1")
    List<Zastavka> findFavorites();

//    /**
//     * Vrací zastávky obsahující v {@link Zastavka#jmeno} předaný text
//     *
//     * @param arg0 jméno zastávky, nebo jen jeho část
//     * @return seznam zastávek, odpovídajících filtru
//     */
//    @Query("SELECT * FROM zastavka WHERE jmeno LIKE '%' || :arg0 || '%'")
//    List<Zastavka> findByJmeno(String arg0);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Zastavka> zastavky);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Zastavka zastavka);

    @Delete
    void delete(Zastavka zastavky);

    @Query("DELETE FROM zastavka")
    void deleteAll();

}
