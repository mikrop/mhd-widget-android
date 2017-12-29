package cz.mikropsoft.android.mhdwidget.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalTimeSerializer;

import org.joda.time.LocalTime;

import cz.mikropsoft.android.mhdwidget.databases.Converters;

@Entity(indices = {@Index(value = "zastavka_id")},
        foreignKeys = @ForeignKey(
        entity = Zastavka.class,
        childColumns = "zastavka_id",
        parentColumns = "id",
        onDelete = ForeignKey.CASCADE)
)
@TypeConverters(Converters.class)
public class Spoj implements Comparable<Spoj> {

    @NonNull
    @JsonIgnore
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "zastavka_id")
    private int zastavkaId;

    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime predchozi;

    @NonNull
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime odjezd;

    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime nasledujici;

    @JsonIgnore
    private boolean aktualni;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public int getZastavkaId() {
        return zastavkaId;
    }

    public void setZastavkaId(int zastavkaId) {
        this.zastavkaId = zastavkaId;
    }

    public LocalTime getPredchozi() {
        return predchozi;
    }

    public void setPredchozi(LocalTime predchozi) {
        this.predchozi = predchozi;
    }

    @NonNull
    public LocalTime getOdjezd() {
        return odjezd;
    }

    public void setOdjezd(@NonNull LocalTime odjezd) {
        this.odjezd = odjezd;
    }

    public LocalTime getNasledujici() {
        return nasledujici;
    }

    public void setNasledujici(LocalTime nasledujici) {
        this.nasledujici = nasledujici;
    }

    /**
     * Příznak, zda se jedná o {@link Spoj}, který je aktuálně nařadě.
     *
     * @return {@code true}, pokud jde o aktu8ln9 spoje, jinak {@code false}
     */
    public boolean isAktualni() {
        return aktualni;
    }

    public void setAktualni(boolean aktualni) {
        this.aktualni = aktualni;
    }

    @Override
    public int compareTo(@NonNull Spoj o) {
        return o.getOdjezd().compareTo(getOdjezd());
    }

}