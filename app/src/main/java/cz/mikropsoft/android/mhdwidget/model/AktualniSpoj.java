package cz.mikropsoft.android.mhdwidget.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalTimeDeserializer;

import org.joda.time.LocalTime;

/**
 * Přepravka, aktuálního spoje.
 */
public class AktualniSpoj {

    private String smer;
    private String zastavka;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime predchozi;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime odjezd;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime nasledujici;

    public String getSmer() {
        return smer;
    }

    public void setSmer(String smer) {
        this.smer = smer;
    }

    public String getZastavka() {
        return zastavka;
    }

    public void setZastavka(String zastavka) {
        this.zastavka = zastavka;
    }

    public LocalTime getPredchozi() {
        return predchozi;
    }

    public void setPredchozi(LocalTime predchozi) {
        this.predchozi = predchozi;
    }

    public LocalTime getOdjezd() {
        return odjezd;
    }

    public void setOdjezd(LocalTime odjezd) {
        this.odjezd = odjezd;
    }

    public LocalTime getNasledujici() {
        return nasledujici;
    }

    public void setNasledujici(LocalTime nasledujici) {
        this.nasledujici = nasledujici;
    }

}
