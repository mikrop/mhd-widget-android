package cz.mikropsoft.android.mhdwidget.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Comparator;

import cz.mikropsoft.android.mhdwidget.databases.ProstredekConverter;

@Entity
@TypeConverters(ProstredekConverter.class)
public class Zastavka implements Comparable<Zastavka> {

    /**
     * Comparátor dle zastávek, dle jména a směru.
     */
    public static final Comparator<Zastavka> JMENOSMER_COMPARATOR = new Comparator<Zastavka>() {
        @Override
        public int compare(Zastavka o1, Zastavka o2) {
            int compare = o1.getJmeno().compareTo(o2.getJmeno());
            return ((compare == 0) ? o1.getSmer().compareTo(o2.getSmer()) : compare);
        }
    };

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String linka; // Označení linky

    @NonNull
    private Prostredek prostredek;

    @NonNull
    private String smer;

    @NonNull
    private String jmeno;

    @JsonIgnore
    private boolean favorite;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getLinka() {
        return linka;
    }

    public void setLinka(@NonNull String linka) {
        this.linka = linka;
    }

    @NonNull
    public Prostredek getProstredek() {
        return prostredek;
    }

    public void setProstredek(@NonNull Prostredek prostredek) {
        this.prostredek = prostredek;
    }

    @NonNull
    public String getSmer() {
        return smer;
    }

    public void setSmer(@NonNull String smer) {
        this.smer = smer;
    }

    @NonNull
    public String getJmeno() {
        return jmeno;
    }

    public void setJmeno(@NonNull String jmeno) {
        this.jmeno = jmeno;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public int compareTo(@NonNull Zastavka o) {
        int compare = Boolean.compare(o.isFavorite(), isFavorite());
        return ((compare == 0) ? getJmeno().compareTo(o.getJmeno()) : compare);
    }

}